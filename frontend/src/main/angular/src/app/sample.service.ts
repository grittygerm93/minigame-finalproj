import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class SampleService {

  constructor(private http: HttpClient) {
  }

  // isAlive() {
  //   //toggle this on if testing client and server separately
  //   // return this.http.get("http://localhost:8080/api/grit");
  //
  //   //switch to this if doing the final build
  //   return this.http.get("/api/grit");
  // }

}
