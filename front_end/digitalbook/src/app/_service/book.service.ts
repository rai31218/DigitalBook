import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Book } from '../_model/book.model';
import { Observable } from 'rxjs';
import {UserService} from '../_service/user.service';
import { JwtResponse } from '../_model/jwtResponse.model';
import { Subscription } from '../_model/subscription.model';
import { SubscriptionPayLoad } from '../_model/subscriptionpayload.model';




const commonURl="http://localhost:8081/digitalbooks/"
@Injectable({
  providedIn: 'root'
})
export class BookService {
 
  book:Book;
  currentUser:JwtResponse;
  constructor(private http: HttpClient, private userService:UserService) {
    this.currentUser = this.userService.currentUserValue;
   }


 public getSeachedBooks(category:string, title:string, author:string, price:number,publisher:string ){
  let url = commonURl+"searchBook"
  url=url+"?category="+category+"&title="+title+"&author="+author+"&price="+price+"&publisher="+publisher;
 console.log("url: "+url)
  return this.http.get(url);
  //.pipe(map(response => response));

 }

 public createBooks(logo:File, category:string, title:string, authorId:number, price:number,
  publisher:string, active:boolean, content:string)
{
  let savebookurl=commonURl+"author"
  console.log("Ami create a")
  const formdata: FormData = new FormData(); 
  const bookInfo:Book = {
    //logo:logo,
    title: title,
    category: category,
    price:price,
    publisher:publisher,
    active:active,
    content:content
  }; 
  return this.http.post(savebookurl+"/"+authorId+"/books/", bookInfo);
}


public updateBooks(logo:File, category:string, title:string, authorId:number, price:number,
  publisher:string, active:boolean, content:string, bookId:number)
{
  let updatebookurl= commonURl+"author"
  console.log("Ami create a")
  const formdata: FormData = new FormData(); 
  const bookInfo:Book = {
    //logo:logo,
    title: title,
    category: category,
    price:price,
    publisher:publisher,
    active:active,
    content:content
  }; 
  return this.http.post(updatebookurl+"/"+authorId+"/books/"+bookId, bookInfo);
}

  public uploadlogo(logo:File) {
    let uploadlogourl=commonURl+"uploadlogo"
    console.log("Ami upload a")
    const formdata: FormData = new FormData();  
    formdata.append('file',logo);
    return this.http.post(uploadlogourl, formdata)
  }

  public subscribe(bookId: number) {
    let subscriptionurl = commonURl;
    let subcribe:SubscriptionPayLoad ={
      bookId:bookId,
      email: this.currentUser.email
    }
    const formdata: FormData = new FormData();  
    formdata.append('bookId',JSON.stringify(bookId));
    formdata.append('email', this.currentUser.email)
    return this.http.post(subscriptionurl+bookId+"/subscribe", subcribe)
  }

  public getAllSubscribedBook(email: string) {

    let subscribedvbookurl = commonURl+"readers/";
    return this.http.get(subscribedvbookurl+email+"/books");
    
  }


  public getSubscriptionIdOfEachBook(userId:number, bookId:number){
    
let subscribedvbookurl = commonURl+"readers/";
    subscribedvbookurl=subscribedvbookurl+userId+"/"+bookId;
   console.log("url: "+subscribedvbookurl)
    return this.http.get(subscribedvbookurl);
    //.pipe(map(response => response));
  
   }
  public unsubscribe(email:string, subscriptionId: number) {
    let subscribedvbookurl = commonURl+"readers/"+email+"/books/"+subscriptionId+"/cancel-subscription"
    return this.http.put(subscribedvbookurl, null);
  }


  public openBook(email:string,subscriptionId: string) {
    let subscribedvbookurl = commonURl+"readers/"+email+"/books/"+subscriptionId+"/read"
    return this.http.get(subscribedvbookurl);
  }

  public getAllCreatedBook(authorId: number) {
    let createdbookurl = commonURl+"createdbook/"+authorId;
    return this.http.get(createdbookurl);
}

public blockBook(userId: number, bookId: number, active:string) {
  let blockbookurl = commonURl+"author/"+userId+"/books/"+bookId+"?block="+active;
  console.log(blockbookurl)
  return this.http.put(blockbookurl, null);
}

getBookById(bookId: number) {
  let fetchbookforupdateurl = commonURl+"book/"+bookId;
  return this.http.get(fetchbookforupdateurl);
}

getBookBySubscriptionId(emailId:string,subscriptionId: any) {
  let fetchbookfBySubscriptionIdurl = commonURl+"readers/"+emailId+"/books/"+subscriptionId;
  return this.http.get(fetchbookfBySubscriptionIdurl);
}


}
