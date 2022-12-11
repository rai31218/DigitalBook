import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import {UserService} from '../_service/user.service'

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private authenticationService: UserService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            console.log("In error Interceptor" + err.status)

            if (err.status === 401) {
                // auto logout if 401 response returned from api
                this.authenticationService.logout();
              //  location.reload(true);
              return throwError(()=>"Unauthorized ");
            }

            if (err.status === 404) {
                return throwError(()=>"Not Found "+err.error.message);
            }
             
             const error = err.error.message || err.statusText;
             return throwError(()=>""+error);
        }))
    }
}
