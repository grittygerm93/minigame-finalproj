import {Injectable} from '@angular/core';
import {Observable, Subject} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {tokens} from "./models";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authObs = new Subject();
  errorMsg: string;

  constructor(private http: HttpClient, private router: Router) {
  }

  login(username: string, password: string) {
    const userDetails = {'username': username, 'password': password}

    let body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);

    const headers = new HttpHeaders()
      .set('Content-Type', 'application/x-www-form-urlencoded')

    this.http.post<tokens>('/api/login',
      body, {headers})
      .subscribe(tokens => {
        if (tokens) {
          sessionStorage.setItem('access-token', tokens.access_token);
          sessionStorage.setItem('refresh-token', tokens.refresh_token);
          this.router.navigate(['']);
        } else {
          alert("Authentication failed.")
        }
      });
  }

  getAllUsers() {
    this.http.get('/api/users', {observe: 'response'})
      .subscribe(resp => {
        console.log(resp);
      }, error => {
        const errorMsg: string = error.error['error_message'];
        if (errorMsg.includes("expired")) {
          this.getNewAccessToken()
        }
        this.errorPage(error);
      });
  }

  private getNewAccessToken() {
    const headers = new HttpHeaders()
      .set('expired', 'expired');
    this.http.get<tokens>("/api/token/refresh", {headers})
      .subscribe(tokens => {
        if (tokens) {
          sessionStorage.setItem('access-token', tokens.access_token);
          sessionStorage.setItem('refresh-token', tokens.refresh_token);
        }
      })

  }

  register(username: string, password: string, email: string) {
    const user = {'username': username, 'password': password, 'email': email};
    this.http.post<tokens>('/api/register',
      user, {observe: 'response'})
      .subscribe(resp => {
          console.log(resp);
          // this.router.navigate(['']);
        },
        error => {
          this.errorPage(error);
        })
  }

  verify(email: string, verifyId: string) {
    const verify = {'email': email, 'verifyId': verifyId};
    return this.http.post('/api/verify',
      verify, {observe: 'response'})
      .subscribe(resp => {
        },
        error => {
          this.errorPage(error);
        });
  }

  getToken() {
    return sessionStorage.getItem('access-token');
  //  todo handle expired token??
  }

  private errorPage(error) {
    console.log(error.error['error_message'])
    this.errorMsg = error.error['error_message'];
    this.authObs.next(error);
    this.router.navigate(['/badrequest']);
  }

  // setRefreshToken() {
  //   //
  //   sessionStorage.setItem('access-token', sessionStorage.getItem('refresh-token'));
  // }
}
