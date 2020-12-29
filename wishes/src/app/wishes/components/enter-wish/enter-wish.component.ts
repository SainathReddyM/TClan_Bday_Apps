import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { apiPaths } from 'src/app/core/config/api-paths.config';
import { ApiService } from 'src/app/core/services/api-service/api.service';
import { EventType } from '../../enums/event-type.enum';
import { EventModel } from '../../models/event.model';

@Component({
    selector: 'app-enter-wish',
    templateUrl: './enter-wish.component.html',
    styleUrls: ['./enter-wish.component.scss'],
})
export class EnterWishComponent implements OnInit {
    public event: EventModel;

    public message: string;

    public displayMsg = false;

    public isMsgFetched = false;

    public eventType = EventType;

    private testimonialId: number;

    constructor(
        private dialogRef: MatDialogRef<EnterWishComponent>,
        @Inject(MAT_DIALOG_DATA) public data,
        private apiService: ApiService,
        private snackBar: MatSnackBar
    ) {
        this.event = this.data.event;
        this.apiService
            .invokeGet(apiPaths.testimonial + `/${this.event.id}`)
            .pipe()
            .toPromise()
            .then((res) => {
                this.displayMsg = true;
                if (res) {
                    this.message = res.message;
                    this.testimonialId = res.id;
                    this.isMsgFetched = true;
                }
            })
            .catch(() => {
                this.dialogRef.close();
            });
    }

    ngOnInit(): void { }

    postMsg(): void {
        const reqBody = {
            message: this.message,
        };
        if (!this.isMsgFetched) {
            reqBody['eventId'] = this.event.id;
            this.apiService.invokePost(apiPaths.testimonial, reqBody).subscribe(res => {
                this.testimonialId = res.id;
                this.openSnackBar('Message Submitted Successfully');
                this.isMsgFetched = true;
            });
        } else {
            this.apiService.invokePut(apiPaths.testimonial + `/${this.testimonialId}`, reqBody).subscribe(() => {
                this.openSnackBar('Message Updated Successfully');
                this.isMsgFetched = true;
            });
        }
    }

    DeleteMsg(): void {
        if (this.isMsgFetched) {
            this.apiService.invokeDelete(apiPaths.testimonial + `/${this.testimonialId}`).subscribe(() => {
                this.testimonialId = null;
                this.isMsgFetched = false;
                this.openSnackBar('Message Deleted Successfully');
                this.message = null;
            });
        } else {
            this.message = null;
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
