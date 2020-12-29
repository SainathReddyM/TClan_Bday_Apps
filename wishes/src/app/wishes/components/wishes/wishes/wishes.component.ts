import { Component, OnInit } from '@angular/core';
import { StoreService } from 'src/app/core/services/store/store.service';

@Component({
    selector: 'app-wishes',
    templateUrl: './wishes.component.html',
    styleUrls: ['./wishes.component.scss'],
})
export class WishesComponent implements OnInit {
    public userRole: string;

    constructor(private store: StoreService) {
        this.userRole = this.store.getUserRole();
    }

    ngOnInit(): void {}
}
