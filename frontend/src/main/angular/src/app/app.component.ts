import {Component, OnInit} from '@angular/core';
import {SampleService} from "./sample.service";
import {HttpClient} from "@angular/common/http";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  // alive: boolean = false;
  //
  // constructor(private http: HttpClient) {
  // }

  constructor() {
  }

  ngOnInit(): void {
/*    this.sampleSvc.isAlive().subscribe(res => {
      console.log(res);
      if (res) {
        this.alive = true;
      }
    })*/
  }


}
