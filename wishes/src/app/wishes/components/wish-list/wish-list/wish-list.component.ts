import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApiService } from 'src/app/core/services/api-service/api.service';
import { apiPaths } from 'src/app/core/config/api-paths.config';
import { EnterWishComponent } from '../../enter-wish/enter-wish.component';
import { StoreService } from 'src/app/core/services/store/store.service';
import { EventModel } from '../../../models/event.model';
import { EventType } from '../../../enums/event-type.enum';

@Component({
    selector: 'app-wish-list',
    templateUrl: './wish-list.component.html',
    styleUrls: ['./wish-list.component.scss'],
})
export class WishListComponent implements OnInit {
    public eventDates = [];
    public events = {};

    public eventType = EventType;

    constructor(
        private dialog: MatDialog,
        private apiService: ApiService,
        private store: StoreService
    ) {}

    ngOnInit(): void {
        const eventId = this.store.getEventId();
        if (eventId) {
            this.apiService.invokeGet(apiPaths.event + `/${eventId}`).subscribe(res => {
                const event = new EventModel(res);
                this.openTestimonial(event);
                this.getEvents();
            });
        } else {
            this.getEvents();
        }
    }

    getEvents(): void {
        this.apiService.invokeGet(apiPaths.testimonial).subscribe((res) => {
            this.eventDates = Object.keys(res).sort();
            this.eventDates.forEach((date) => {
                res[date] = res[date].map(
                    (item) => (item = new EventModel(item))
                );
            });
            this.events = res;
        });
    }

    openTestimonial(event): void {
        const dialogRef = this.dialog.open(EnterWishComponent, {
            maxWidth: '100vw',
            height: '100vh',
            width: '100vw',
            panelClass: 'employeeDialog',
            data: { event: event },
        });
    }
}
