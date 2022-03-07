import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

  constructor(private router: Router, private authSvc: AuthService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    console.log("in guard start")

    if(state.url == "/login" || state.url =='/register') {
      return true;
    }

    if(state.url == "/badrequest") {
      if(this.authSvc.authObs.subscribe(err  => {
        if(err) {
          return true;
        }
      }))
      return false;
    }

    let token = sessionStorage.getItem('access-token');
    console.log("in guard")

    if(!token) {
      console.log("in guard")
      return this.router.parseUrl('/login');
    }





    return true;
  }

}
