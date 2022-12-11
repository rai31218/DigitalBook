import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

import {UserService} from '../_service/user.service'

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authenticationService: UserService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const req = /sign-in/gi;/* g modifier: global. All matches (don't return on first match),i modifier: insensitive. Case insensitive match (ignores case of [a-zA-Z])*/
    const req1 = /searchBook/gi;
   
    // add authorization header with jwt token if available
    console.log("Coming interceptor " + request.url.search(req) + " "+ request.url.search(req1))
    if (request.url.search(req) === -1 || request.url.search(req1) ===-1) {

      let currentUser = this.authenticationService.currentUserValue;
      if (currentUser && currentUser.accessToken) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${currentUser.accessToken}`

          }
        });
        //console.log(request);
      }
    }

    return next.handle(request);
  }
}
