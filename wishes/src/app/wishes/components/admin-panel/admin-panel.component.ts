import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EmployeeCrudComponent } from '../employee-crud/employee-crud.component';

@Component({
    selector: 'app-admin-panel',
    templateUrl: './admin-panel.component.html',
    styleUrls: ['./admin-panel.component.scss'],
})
export class AdminPanelComponent implements OnInit {
    constructor(private dialog: MatDialog) {}

    ngOnInit(): void {}

    memberConfig(configType: string): void {
        const dialogRef = this.dialog.open(EmployeeCrudComponent, {
            maxWidth: '100vw',
            height: '100vh',
            width: '100vw',
            panelClass: 'employeeDialog',
            data: { type: configType },
        });
    }
}
