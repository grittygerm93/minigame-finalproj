import { Component, OnInit } from '@angular/core';
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-badreq',
  templateUrl: './badreq.component.html',
  styleUrls: ['./badreq.component.css']
})
export class BadreqComponent implements OnInit {

  errorMsg: string;

  constructor(private authSvc: AuthService) { }

  ngOnInit(): void {
    this.errorMsg = this.authSvc.errorMsg;
  }

}
