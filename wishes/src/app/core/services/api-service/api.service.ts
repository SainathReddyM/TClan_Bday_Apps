import { Injectable, Injector } from '@angular/core';
import {
    HttpErrorResponse,
    HttpClient
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { MatSnackBar } from '@angular/material/snack-bar';
import { StoreService } from '../store/store.service';

@Injectable({
    providedIn: 'root',
})
export class ApiService {
    constructor(
        private httpClient: HttpClient,
        private snackBar: MatSnackBar,
    ) {}

    // HttpClient Options to be used for reference
    // options: {
    //   headers?: HttpHeaders | {[header: string]: string | string[]},
    //   observe?: 'body' | 'events' | 'response',
    //   params?: HttpParams|{[param: string]: string | string[]},
    //   reportProgress?: boolean,
    //   responseType?: 'arraybuffer'|'blob'|'json'|'text',
    //   withCredentials?: boolean,
    // }

    public invokeGet(
        url,
        options?,
        shouldhandleGlobally = true
    ): Observable<any> {
        return this.httpClient.get(url, options).pipe(
            catchError((error) => {
                if (shouldhandleGlobally) {
                    this.handleError(error);
                }
                return throwError(error);
            })
        );
    }

    public invokePost(
        url,
        body,
        options?,
        shouldhandleGlobally = true
    ): Observable<any> {
        return this.httpClient.post(url, body, options).pipe(
            catchError((error) => {
                if (shouldhandleGlobally) {
                    this.handleError(error);
                }
                return throwError(error);
            })
        );
    }

    public invokePut(
        url,
        body,
        options?,
        shouldhandleGlobally = true
    ): Observable<any> {
        return this.httpClient.put(url, body, options).pipe(
            catchError((error) => {
                if (shouldhandleGlobally) {
                    this.handleError(error);
                }
                return throwError(error);
            })
        );
    }

    public invokePatch(
        url,
        body,
        options?,
        shouldhandleGlobally = true
    ): Observable<any> {
        return this.httpClient.patch(url, body, options).pipe(
            catchError((error) => {
                if (shouldhandleGlobally) {
                    this.handleError(error);
                }
                return throwError(error);
            })
        );
    }

    public invokeDelete(
        url,
        options?,
        shouldhandleGlobally = true
    ): Observable<any> {
        return this.httpClient.delete(url, options).pipe(
            catchError((error) => {
                if (shouldhandleGlobally) {
                    this.handleError(error);
                }
                return throwError(error);
            })
        );
    }

    private openSnackBar(message: string): void {
        this.snackBar.open(message, '', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom',
            panelClass: 'error',
        });
    }

    private handleError(error: HttpErrorResponse): void {
        let message = '';
        if (error.error instanceof ErrorEvent) {
            message = `Error: ${error.error.message}`;
        } else {
            message = error.status + ' ' + this.fetchServerErrorMessage(error);
        }
        this.openSnackBar(message);
    }

    private fetchServerErrorMessage(error: HttpErrorResponse): string {
        switch (error.status) {
            case 400: {
                return 'Bad Request';
            }
            case 401: {
                return 'Not Authenticated';
            }
            case 403: {
                return 'Not Authorized';
            }
            case 404: {
                return 'Not Found';
            }
            case 405: {
                return 'Not Allowed';
            }
            case 500: {
                return 'System Error';
            }
            default: {
                return 'Server Error';
            }
        }
    }
}
