import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: FormGroup;
  formVerify: FormGroup;

  constructor(private fb: FormBuilder, private router: Router,
              private authSvc: AuthService) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      "email": this.fb.control("miffy@gmail.com"),
      "username": this.fb.control("miffy"),
      "password": this.fb.control("1234"),
    });
    this.formVerify = this.fb.group({
      "email": this.fb.control(""),
      "verifyId": this.fb.control(""),
    });
  }

  onSubmit() {
    const username = this.form.get("username").value;
    const password = this.form.get("password").value;
    const email = this.form.get("email").value;
    this.authSvc.register(username, password, email);
    //   .subscribe(tokens => {
    //   if (tokens) {
    //     sessionStorage.setItem('access-token', tokens.access_token);
    //     sessionStorage.setItem('refresh-token', tokens.refresh_token);
    //     this.router.navigate(['']);
    //   } else {
    //     alert("Authentication failed.")
    //   }
    // });
    this.formVerify.get("email").setValue(email);
  }

  onSubmitVerify() {
    const username = this.formVerify.get("email").value;
    const verifyId = this.formVerify.get("verifyId").value;
    console.log(verifyId)
    this.authSvc.verify(username, verifyId);
  }
}
