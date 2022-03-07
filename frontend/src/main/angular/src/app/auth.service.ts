import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {link, link1, tokens} from "./models";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authObs = new Subject();
  errorMsg: string;
  user: string

  constructor(private http: HttpClient, private router: Router) {
  }

  login(username: string, password: string) {
    const userDetails = {'username': username, 'password': password}
    this.user = username;

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
  authorizeGoogleOauth() {
    // const headers = {responseType: 'text'}
    let params = new HttpParams()
      .set('redirectUrl', 'http://localhost:4200/share');
    console.log(params.toString())
    this.http.get<link>('/api/authorizationCode', {params}).subscribe(resp => {
      window.location.href = resp.link;
    //  todo make the oauthauthorize trigger on it's own...
    });
  }

  oauthAuthorize(code: any) {
    const body = {code: code, redirectUrl: 'http://localhost:4200/share'}
    return this.http.post<link1>('/api/token',
      body)
  }
}
