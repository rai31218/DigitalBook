import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { Book } from '../_model/book.model';
import { BookContentResponse } from '../_model/bookcontentresponse.model';
import { BooksWithLogo } from '../_model/bookswithlogo.model';
import { BooksWithLogoAndUserName } from '../_model/bookswithlogoandusername.model';
import { JwtResponse } from '../_model/jwtResponse.model';
import { MessageResponse } from '../_model/messageresponse.model';
import { Subscription } from '../_model/subscription.model';
import { BookService } from '../_service/book.service';
import {UserService} from '../_service/user.service';

@Component({
  selector: 'app-subscribed-book',
  templateUrl: './subscribed-book.component.html',
  styleUrls: ['./subscribed-book.component.css']
})
export class SubscribedBookComponent implements OnInit {
  currentUser:JwtResponse;
  subscribeBookList : any[];
  subscriptionIdList: any[];
  thumbnail: any[];
  res: number;
  subscriptionId:Subscription;
  subdIdList = new Map<number, Subscription>();
  isOpenBook: boolean=false;
  content: any;
  unsubscribeMessage: MessageResponse;
  unsubscribeErrorMessage: any;
  parameter:string;
  searchSubscriptionForm: FormGroup;
  subscribedbook: any;
  subscribedbookError: any;
  submitted: boolean;
  contentError: any;

  constructor(private bookService: BookService, private userService: UserService,
     private sanitizer: DomSanitizer, private route: ActivatedRoute) {
     
      this.parameter=this.route.snapshot.paramMap.get('id');
   
  }

  ngOnInit(): void { 
    this.getAllSubscribedBook();
    this.searchSubscriptionForm = new FormGroup({
      "subscriptionId": new FormControl("",Validators.required ),
  });
  }
  get f() { return this.searchSubscriptionForm.controls; }


  public getAllSubscribedBook(){
    this.currentUser = this.userService.currentUserValue;
    const subscribedBooks = this.bookService.getAllSubscribedBook(this.currentUser.email);
    var that = this;
    this.subscribeBookList =[]
    this.thumbnail=[];
    this.subscriptionIdList=[];
    //this.subdIdList=[];
    subscribedBooks.subscribe({

      next: (data:BooksWithLogoAndUserName[])=>   {

        for( let i:number=0 ;i<data.length;i++){
          console.log("I"+i)
             this.subscribeBookList[i]=(data[i])

             let objectURL = 'data:image/jpeg;base64,' + data[i].logo;
             this.thumbnail.push(this.sanitizer.bypassSecurityTrustUrl(objectURL));
             console.log(this.thumbnail);
            this.subsid(this.currentUser.id, data[i].books.id );
        }
       },
       error:(err)=>{console.log("error: "+err)}
     
    });
  }

  public subsid(userId:number, bookId:number){
   // const observable =  this.bookService.getSubscriptionIdOfEachBook(userId, bookId);
    
    this.currentUser = this.userService.currentUserValue;
    const subscribedBooks = this.bookService.getSubscriptionIdOfEachBook(userId, bookId);
    console.log("success")
    this.subscriptionId=null;
    var id =this.subscriptionId;
    subscribedBooks.subscribe({

      next: (data:Subscription)=>   {
        console.log("success")
        this.subscriptionId=data;
        this.subdIdList.set(bookId,this.subscriptionId);
      //  map.set(key, value)
        
       },
       error:(err)=>{console.log("error: "+err)}
     
    });
    console.log("success "+this.subscriptionId)       
  }
  public unsubscribe(id:string){
    this.unsubscribeMessage=null;
    this.unsubscribeErrorMessage=null;
   this.bookService.unsubscribe(this.currentUser.email,id).subscribe({
    next:(data)=>{console.log(data);
    this.unsubscribeMessage=data
  
     },
    error:(err)=>{console.log(err);
    this.unsubscribeErrorMessage=err}
   })
    
  }


  public openBook(subscriptionId:string){
    this.isOpenBook=true;
    this.bookService.openBook(this.currentUser.email, subscriptionId).subscribe({
    next:(data:BookContentResponse)=>{this.content=data},
    error:(err)=>{this.contentError=err}
    })
  }

  public onSubmitToSearchForSubscription(){
    this.submitted = true;
    const formData = {
      subscriptionId : this.f['subscriptionId'].value,
    };
    this.subscribedbook=null;
    this.subscribedbookError=null;
    this.bookService.getBookBySubscriptionId(this.currentUser.email,formData.subscriptionId).
    subscribe({
      next:(data)=>{console.log("getBookBySubscriptionId "+ data);
     this.subscribedbook= data},
      error: (err)=>{console.log("getBookBySubscriptionId "+ err);
        this.subscribedbookError= err;}
    })
  }
}
