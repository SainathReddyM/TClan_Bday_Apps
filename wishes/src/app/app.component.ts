import { Component, OnInit } from '@angular/core';
import { apiPaths } from './core/config/api-paths.config';
import { ApiService } from './core/services/api-service/api.service';
import { StoreService } from './core/services/store/store.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
    private theme: string;

    public isRoleSet = false;

    public eventId: string;

    constructor(private store: StoreService, private apiService: ApiService) {}

    ngOnInit(): void {
        const queryString = window.location.search;
        const url = new URLSearchParams(queryString);
        this.theme = JSON.parse(url.get('flockTheme'))['name'] + '-theme';
        this.store.setEventId(url.get('eventId'));
        document.body.classList.add(this.theme);

        this.store.setFlockToken(url.get('flockEventToken'));

        this.apiService
            .invokeGet(apiPaths.role)
            .pipe()
            .toPromise()
            .then((res) => {
                this.store.setUserRole(res.role);
                this.isRoleSet = true;
            })
            .catch(() => {
                this.isRoleSet = true;
            });
    }
}
