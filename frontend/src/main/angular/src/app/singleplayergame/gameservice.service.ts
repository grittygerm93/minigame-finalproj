import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import {GameScene} from "./singleplayergame.component";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class GameService {
  Sub = new Subject();
  abc = 123

  constructor(private http: HttpClient) { }

  storeScore(data: number) {
    this.Sub.next(data);
  }
}
