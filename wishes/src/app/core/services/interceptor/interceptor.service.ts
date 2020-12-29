import { Injectable, Injector } from '@angular/core';
import {
    HttpInterceptor,
    HttpEvent,
    HttpResponse,
    HttpRequest,
    HttpHandler,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { StoreService } from '../store/store.service';

@Injectable({
    providedIn: 'root',
})
export class InterceptorService implements HttpInterceptor {
    storeService: StoreService;

    constructor(injector: Injector) {
        this.storeService = injector.get<StoreService>(StoreService);
    }

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        const request = req.clone({
            headers: req.headers.set(
                'X-Flock-Event-Token',
                this.storeService.getFlockToken()
            ),
        });
        return next.handle(request);
    }
}
