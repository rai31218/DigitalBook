import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Book } from '../_model/book.model';
import { BookService } from '../_service/book.service';
import {UserService} from '../_service/user.service';
import { DomSanitizer } from '@angular/platform-browser';
import { JwtResponse } from '../_model/jwtResponse.model';
import { MessageResponse } from '../_model/messageresponse.model';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
  searchBookForm: FormGroup;
  createBookForm: FormGroup;
  loading = false;
  submitted = false;
  status:any="";
  thumbnail: any;
  searchedData:any;

  submittedForCreation=false;
  currentUser:JwtResponse;
  file: any;
  createstatus: any;

  gotSerachResult:boolean=false;
  subscriptionData: MessageResponse;
  subrcriptionError: any;


  constructor(private formBuilder: FormBuilder,
    private router: Router,
    private bookService: BookService,
    private sanitizer: DomSanitizer,
    private userService: UserService) {

      this.currentUser = this.userService.currentUserValue;
     }

  ngOnInit(): void {
    this.searchBookForm = new FormGroup({
      "category": new FormControl("",Validators.required ),
      "title": new FormControl("",Validators.required ) ,
      "author": new FormControl("",Validators.required ),
      "publisher" : new FormControl("",Validators.required ),
      "price":  new FormControl("",Validators.required )
  });

  

  }

    // convenience getter for easy access to form fields
    get f() { return this.searchBookForm.controls; }


    onSubmit() {
        this.submitted = true;
        // stop here if form is invalid
        if (this.searchBookForm.invalid) {
            return;
        }  
        // building JSON for form data
        const formData = {
          category : this.f['category'].value,
          title : this.f['title'].value,
          author : this.f['author'].value,
          publisher : this.f['publisher'].value,
          price : this.f['price'].value
        };
  
        console.log("Form DATA: "+formData.title);
       // this.loading = true;
         const observable =  this.bookService.getSeachedBooks(formData.category, formData.title, formData.author,formData.price,formData.publisher );
        observable.subscribe({
          next:(data:any)=> {
            console.log("DAT   "+data.books.title);
            this.gotSerachResult = true;
            this.searchedData=data.books;
            let objectURL = 'data:image/jpeg;base64,' + data.logo;
             this.thumbnail = this.sanitizer.bypassSecurityTrustUrl(objectURL);
             console.log(this.thumbnail);

          },
          error:(err)=> {console.log("Error:" + err); this.status=err}

        });               
    }




  public subscribe(bookId:number){
    const observable = this.bookService.subscribe(bookId)
    this.subscriptionData=null;
    this.subrcriptionError = null;
    observable.subscribe({
      next :  (data:MessageResponse)=>{console.log("subscriptio: "+data);
      this.subscriptionData = data;
    },             
      error : err=>{this.subrcriptionError = err}
    });
      
     
  }


}
