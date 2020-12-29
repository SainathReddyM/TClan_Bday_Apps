import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class StoreService {
    private flockToken: string;

    private userRole = 'user';

    private eventId: string;

    constructor() {}

    getFlockToken(): string {
        return this.flockToken;
    }

    setFlockToken(token: string): void {
        this.flockToken = token;
    }

    getUserRole(): string {
        return this.userRole;
    }

    setUserRole(role: string): void {
        this.userRole = role;
    }

    getEventId(): string {
        return this.eventId;
    }

    setEventId(id): void {
        this.eventId = id;
    }
}
