import { Component, OnInit } from '@angular/core';
// import { WebSocketApi } from './WebSocketApi';
import {FormBuilder, FormGroup} from "@angular/forms";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
// import { MainComponent } from "./main.component";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  //proxy config doesnt work here..
  webSocketEndPoint: string = 'http://localhost:8080/our-websocket';
  topic: string = "/topic/receivemsg";
  stompClient: any;

  // webSocketAPI: WebSocketApi;
  form: FormGroup;
  receivedMsgs: string[] = [];
  isConnected: boolean = false;


  constructor(private fb: FormBuilder, private authSvc: AuthService) {
    this.form = this.fb.group({
      "message": this.fb.control("default msg"),
    });
  }

  ngOnInit() {
  }

  sendMessage(){
    let message = this.form.get("message").value;
    console.log(message);
    console.log("sending out message over websocket");
    // this.stompClient.send("/ws/sendmessage", {}, JSON.stringify(message));
    this.stompClient.send("/ws/sendmessage", {}, message);
  }

  handleMessage(message){
    console.log("Message Recieved from Server :: " + message);
    this.receivedMsgs.push(message.body);
  }

  connect() {
    console.log("initialize websocket connection");
    // let ws = new SockJS(this.webSocketEndPoint + '?jwt=' + this.authSvc.getToken());
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);

    // this.stompClient.connect({'username': 'Jimbob', 'password': 'pass'}, (frame) => {
    this.stompClient.connect({'access-token': 'Bearer ' + this.authSvc.getToken()}, (frame) => {
      this.stompClient.subscribe(this.topic, (event) => {
        this.handleMessage(event);
      });
      //_this.stompClient.reconnect_delay = 2000;
    }, this.errorCallBack);
  }

  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  errorCallBack(error) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      //todo handle the refresh token
      // this.authSvc.setRefreshToken();
      this.connect();
    }, 5000);
  }


}

