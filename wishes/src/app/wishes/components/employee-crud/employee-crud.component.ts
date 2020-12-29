import { Component, Inject, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, of } from 'rxjs';
import {
    catchError,
    debounceTime,
    distinctUntilChanged,
    map,
    startWith,
    switchMap,
} from 'rxjs/operators';
import { apiPaths } from 'src/app/core/config/api-paths.config';
import { ApiService } from 'src/app/core/services/api-service/api.service';
import * as _ from 'lodash-es';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Employee } from '../../models/employee.model';

@Component({
    selector: 'app-employee-crud',
    templateUrl: './Employee-crud.component.html',
    styleUrls: ['./Employee-crud.component.scss'],
})
export class EmployeeCrudComponent implements OnInit {
    public userDetails: Employee[];

    public isMailSet = false;

    private skipCall = false;

    public firstName = '';
    public lastName = '';
    public dob = '';
    public doj = '';
    public email = '';
    public userRole = '';

    public roles = ['user', 'admin'];

    public mailSearch = new FormControl();
    public options: string[] = [];
    public filteredOptions: Observable<string[]>;

    public btnTexts = {
        add: 'Add Employee',
        update: 'Update Employee',
        delete: 'Delete Employee',
    };

    constructor(
        private dialogRef: MatDialogRef<EmployeeCrudComponent>,
        @Inject(MAT_DIALOG_DATA) public data,
        private apiService: ApiService,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit(): void {
        this.filteredOptions = this.mailSearch.valueChanges.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            startWith(''),
            switchMap((value) => {
                this.isMailSet = false;
                if (value.length > 0 && !this.isMailSet && !this.skipCall) {
                    return this._filter(value);
                }
                if (this.skipCall) {
                    if (this.data.type !== 'delete') {
                        this.isMailSet = true;
                    }
                    this.skipCall = false;
                }
                return of([]);
            })
        );
    }

    public setSearchData(): void {
        this.skipCall = true;
        this.userDetails = [
            _.find(this.userDetails, ['emailId', this.mailSearch.value]),
        ];

        this.firstName = this.userDetails[0].firstName;
        this.lastName = this.userDetails[0].lastName;
        this.dob = this.userDetails[0].dob;
        this.doj = this.userDetails[0].doj;
        this.userRole = this.userDetails[0].role;
    }

    private _filter(value: string): Observable<string[]> {
        const filterValue = value.toLowerCase();
        return this.apiService
            .invokeGet(apiPaths.admin + `/${filterValue}`)
            .pipe(
                map((data: any) => {
                    if (data.length !== 0) {
                        this.userDetails = data.map(
                            (item) => (item = new Employee(item))
                        );
                        data = _.map(data, 'emailId');
                        return data as any[];
                    }
                }),
                catchError(() => {
                    return [];
                })
            );
    }

    submitData(): void {
        const reqBody = {
            firstName: this.firstName,
            lastName: this.lastName,
            dob: this.dob,
            doj: this.doj,
            role: this.userRole,
        };

        if (this.data.type === 'add') {
            reqBody['emailId'] = this.email;
            this.apiService
                .invokePost(apiPaths.admin, reqBody)
                .subscribe(() => {
                    this.openSnackBar('Employee Added Successfully');
                    this.dialogRef.close();
                });
        } else if (this.data.type === 'update') {
            this.apiService
                .invokePut(
                    apiPaths.admin + `/${this.userDetails[0].id}`,
                    reqBody
                )
                .subscribe(() => {
                    this.openSnackBar('Employee Updated Successfully');
                    this.dialogRef.close();
                });
        } else {
            this.apiService
                .invokeDelete(apiPaths.admin + `/${this.userDetails[0].id}`)
                .subscribe(() => {
                    this.openSnackBar('Employee Deleted Successfully');
                    this.dialogRef.close();
                });
        }
    }

    private openSnackBar(message: string): void {
        this.snackBar.open(message, '', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom',
            panelClass: 'success',
        });
    }
}
