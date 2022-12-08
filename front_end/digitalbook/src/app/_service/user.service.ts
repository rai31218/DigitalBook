import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const url ="http://localhost:8081/digitalbooks/sign-up"
@Injectable({
  providedIn: 'root'
})
export class UserService {
save(user:any){
  console.log("It is saved");
  return this.http.post(url, user);
}
  constructor(public http: HttpClient) { }
}
