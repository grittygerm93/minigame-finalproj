import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../auth.service";
import {HttpClient} from "@angular/common/http";
import {link1, simpleperson} from "../models";

@Component({
  selector: 'app-share',
  templateUrl: './share.component.html',
  styleUrls: ['./share.component.css']
})
export class ShareComponent implements OnInit {

  form: FormGroup;
  successfulAuth = false;
  people: simpleperson[] = [];

  constructor(private fb: FormBuilder, private router: Router,
              private authSvc: AuthService, private http: HttpClient) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      "code": this.fb.control(""),
    });
  }

  onSubmit() {
    const code = this.form.get("code").value;
    this.authSvc.oauthAuthorize(code)
      .subscribe((resp: link1) => {
          console.log(resp.accessToken);
          // sessionStorage.setItem('oauth-token', resp.accessToken);
          this.successfulAuth = true;
        });
  }

  getPeople() {
    this.http.get<simpleperson[]>("/api/contacts")
      .subscribe(resp => {
        console.log(resp[0].photoURL);
        this.people = resp;
      })
  }

  sendInvitation(email: string) {
    console.log(email);
    this.http.post("/api/messages", email)
      .subscribe(resp => console.log(resp));
  }
}
