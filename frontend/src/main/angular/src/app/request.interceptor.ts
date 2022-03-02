import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let isExpired = false;
    if(request.headers.get("expired")) {
      isExpired = true;
    }

    if(!isExpired) {
      let token = sessionStorage.getItem('access-token');
      if (token) {
        request = request.clone(
          {headers: request.headers.set('Authorization', `Bearer ${token}`)});
      }
    } else {
      let token = sessionStorage.getItem('refresh-token');
      if (token) {
        request = request.clone(
          {headers: request.headers.set('Authorization', `Bearer ${token}`)});
      }
    }

    return next.handle(request);
  }
}
