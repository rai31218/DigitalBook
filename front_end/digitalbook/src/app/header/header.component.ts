import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from '../_model/subscription.model';
import { User } from '../_model/user.model';
import { UserService } from '../_service/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  user: User;
  userDetailsSubs: Subscription;
  isLoggedInFlag: boolean=false;
 
  isReaderLoggedIn: boolean=false;
  isAuthorLoggedIn: boolean=false;
  currentUser: any;
  indication="subscription"
  userName: string;
  constructor(
    private router: Router,
    private userService: UserService
  ) {

   }

  ngOnInit() {
    console.log("header");
    this.userService.currentUser.subscribe({
      next: x => {
         if(x){
           this.isLoggedInFlag = true;
           console.log(x.roles+"-----------------");
           if(x.roles[0]=="Author"){
            this.isAuthorLoggedIn=true;
            this.userName=x.username;
           }
           if(x.roles[0]=="Reader"){
            this.isReaderLoggedIn=true;
            this.userName=x.username;
           }

           
         }
 
       },
       error: err=>{console.log("header: "+err)}
     });
    // if(this.currentUser!=null && this.currentUser.roles[0]=="Reader"){
    //   this.isLoggedInFlag=true;
    //   this.isReaderLoggedIn=true;
    //   console.log("1: "+this.isLoggedInFlag+ " 2: "+this.isReaderLoggedIn)
    // }
    // if(this.currentUser!=null && this.currentUser.roles[0]=="Author"){
    //   this.isLoggedInFlag=true;
    //   this.isAuthorLoggedIn=true;
      
    //   console.log("1: "+this.isLoggedInFlag+ " 2: "+this.isReaderLoggedIn)
     
    // }


      }
  logout() {
    this.userService.logout();
    this.isLoggedInFlag = false;
    this.isAuthorLoggedIn = false;
    this.isReaderLoggedIn = false;
    this.router.navigate(['/']);
  }
}
