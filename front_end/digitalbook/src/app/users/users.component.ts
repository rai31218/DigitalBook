import { Component, OnInit } from '@angular/core';
import { User } from '../_model/user.model';
import {UserService} from '../_service/user.service'

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  // user={
  //   userName: "Debo",
  //   email: "debo@gmaol.com",
  //  password: "123"
  // }
     user:User=new User;


  constructor(public userService:UserService) { }

  public register(){
    console.log("test");
    this.userService.save(this.user);
    const observable = this.userService.save(this.user);
    observable.subscribe((response:any)=>{
      console.log(response);

    })
    console.error('error has happed'+this.user)
  }

  ngOnInit(): void {
  }

}
