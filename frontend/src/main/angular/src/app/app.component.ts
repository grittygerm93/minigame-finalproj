import {Component, OnInit} from '@angular/core';
import {SampleService} from "./sample.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  alive: boolean = false;

  constructor(private sampleSvc: SampleService) {
  }

  ngOnInit(): void {
    this.sampleSvc.isAlive().subscribe(res => {
      console.log(res);
      if (res) {
        this.alive = true;
      }
    })
  }


}
