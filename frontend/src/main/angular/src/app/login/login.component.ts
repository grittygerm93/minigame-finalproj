import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup;

  constructor(private fb: FormBuilder, private router: Router,
              private authSvc: AuthService) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      "username": this.fb.control("pipi"),
      "password": this.fb.control("1234"),
    });
  }

  onSubmit() {
    const username = this.form.get("username").value;
    const password = this.form.get("password").value;
    this.authSvc.login(username, password);
    //   .subscribe(tokens => {
    //   if (tokens) {
    //     sessionStorage.setItem('access-token', tokens.access_token);
    //     sessionStorage.setItem('refresh-token', tokens.refresh_token);
    //     this.router.navigate(['']);
    //   } else {
    //     alert("Authentication failed.")
    //   }
    // });
  }

}
