import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtResponse } from '../_model/jwtResponse.model';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { User } from '../_model/user.model';
import { Role } from '../_model/role.model';


const url = "http://localhost:8081/digitalbooks/sign-up"
const urlSignIn = "http://localhost:8081/digitalbooks/sign-in"
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private currentUserSubject: BehaviorSubject<JwtResponse>;
  public currentUser: Observable<JwtResponse>;

  constructor(private http: HttpClient) {
      this.currentUserSubject = new BehaviorSubject<JwtResponse>(JSON.parse(localStorage.getItem('currentUser')));
      this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): JwtResponse {
    return this.currentUserSubject.value;
}

  register(userdata: User) {
    console.log("It is saved" + userdata.id);
    // var user:User={
    //   userName: userdata.userName,
    //   password: userdata.password,
    //   email: userdata.email,
    //   role: userdata.role
    // }
    console.log(" user "+ userdata.roles.id+ userdata.roles.roleName)
    return this.http.post(url, userdata);
  }

  login(username: string, password: string){
    console.log("usern: "+username)
    return this.http.post(urlSignIn, {userName : username, password : password })
    .pipe(map((jwtResponse:any) => {
      // login successful if there's a jwt token in the response
      console.log(jwtResponse);
      if (jwtResponse && jwtResponse.accessToken) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(jwtResponse));
          this.currentUserSubject.next(jwtResponse);
      }

      return jwtResponse;
  }));

  }

  
  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    
}


}
