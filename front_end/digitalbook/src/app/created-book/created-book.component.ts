import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { BooksWithLogo } from '../_model/bookswithlogo.model';
import { JwtResponse } from '../_model/jwtResponse.model';
import { BookService } from '../_service/book.service';
import { UserService } from '../_service/user.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Book } from '../_model/book.model';
import { Router, ActivatedRoute } from "@angular/router";

@Component({
  selector: 'app-created-book',
  templateUrl: './created-book.component.html',
  styleUrls: ['./created-book.component.css']
})
export class CreatedBookComponent implements OnInit {
  currentUser:JwtResponse;
  createdBookList : any[];
  subscriptionIdList: any[];
  thumbnail: any[];
  res: number;
  subdIdList: any[];
  isOpenBook: boolean=false;
  content: any;
  updateBookForm: FormGroup;

  isUpdate:boolean=false;
  submittedForCreation: boolean;
  file: any;
  createstatus: any;

  loading = false;
  updatableValue: BooksWithLogo;
  isActive:boolean=true;

  constructor(private bookService: BookService, private userService: UserService,
     private sanitizer: DomSanitizer,
     private formBuilder: FormBuilder,
     private router: Router) { 
      this.isUpdate=false;
      console.log("isupdated: "+  this.isUpdate)
     }

  ngOnInit(): void {
   this.getAllCreatedBook();
 

   this.updateBookForm = this.formBuilder.group({
    logo: ['', Validators.required],
    category: ['', Validators.required],
    title: ['', Validators.required],
    publisher: ['', Validators.required],
    price: ['',Validators.required],
    active: ['',Validators.required],
    content: ['',Validators.required],
    bookId: []
});


  //  this.updateBookForm = new FormGroup({
  //   "logo": new FormControl("",Validators.required ),
  //   "category": new FormControl("",Validators.required ),
  //   "title": new FormControl("",Validators.required ) ,
  //   "publisher" : new FormControl("",Validators.required ),
  //   "price":  new FormControl("",Validators.required ),
  //   "active":  new FormControl(""),
  //   "content":  new FormControl("",Validators.required ),
  //   "bookId": new FormControl()
//});
  }
  public getAllCreatedBook(){
    this.currentUser = this.userService.currentUserValue;
    const subscribedBooks = this.bookService.getAllCreatedBook(this.currentUser.id);
    var that = this;
    this.createdBookList =[]
    this.thumbnail=[];
    this.subscriptionIdList=[];
    this.subdIdList=[];
    subscribedBooks.subscribe({

      next: (data:BooksWithLogo[])=>   {
      console.log("Subscribed???")
        for( let i:number=0 ;i<data.length;i++){
          console.log("I"+i)
             this.createdBookList[i]=(data[i].books)

             let objectURL = 'data:image/jpeg;base64,' + data[i].logo;
             this.thumbnail.push(this.sanitizer.bypassSecurityTrustUrl(objectURL));
             console.log(this.thumbnail);
        }
       },
       error:(err)=>{console.log("error: "+err)}
     
    });
  }

  public block(bookId:number, active:boolean){
    console.log("bookid: "+bookId)
    var observable=null;
    if(active) {
      observable = this.bookService.blockBook(this.currentUser.id, bookId, "yes")
    }
    else{
      observable = this.bookService.blockBook(this.currentUser.id, bookId, "no")
    }
    observable.subscribe({
next:(data)=>{console.log(data);
  this.router.navigate(["/createdbooks"]); 
},
error:(err)=>{console.log(err)}

    })  
  }


public update(bookId:number){
  this.isUpdate=true;
  var observable = this.bookService.getBookById(bookId);
  observable.subscribe({
    next:(data:BooksWithLogo)=>{console.log(data); this.updatableValue=data;
      this.updateBookForm = this.formBuilder.group({
        // someProperty1 - name of the formControl
        // projectData.someProperty1 - value of your object property
        logo: this.updatableValue.books.logo,
        category: this.updatableValue.books.category,
        title: this.updatableValue.books.title, 
        publisher: this.updatableValue.books.publisher,
        content: this.updatableValue.books.content,
        price:this.updatableValue.books.price,
        bookId:this.updatableValue.books.id,
        active: this.updatableValue.books.active,
    });
      //this.updateBookForm.get('category').setValue(this.updatableValue.books.category);
      // this.updateBookForm.get('title').setValue(this.updatableValue.books.title);
      // this.updateBookForm.get('publisher').setValue(this.updatableValue.books.publisher);
      // this.updateBookForm.get('content').setValue(this.updatableValue.books.content);
      // this.updateBookForm.get('price').setValue(this.updatableValue.books.price);
      // this.updateBookForm.get('bookId').setValue(this.updatableValue.books.id);

      this.isActive= this.updatableValue.books.active;

    },
    error:(err)=>{console.log(err)}
    
        })  
  
}


  get f1() { 
    return this.updateBookForm.controls; }
  onChange(event) {
    this.file = event.target.files[0];
 }

 
 onSubmitToUpdate() {
  console.log("came");
  this.submittedForCreation = true;
  // stop here if form is invalid
  console.log("current user: "+ this.currentUser);

  if (this.updateBookForm.invalid) {
    console.log("Invalid?")
      return;
  }  

 console.log("CHECK:::::::::::::::::::::::::::::::::::::"+this.f1['category'].value)
  // building JSON for form data
  const formData = {
    logo: this.f1['logo'].value,
    category : this.f1['category'].value,
    title : this.f1['title'].value,
  // author : this.f1['author'].value,
    publisher : this.f1['publisher'].value,
    price : this.f1['price'].value,
    content: this.f1['content'].value,
    bookId: this.f1['bookId'].value,
  }; 
  console.log("Form DATA: "+ this.file);
 // this.loading = true;
    let activeBoolean:boolean =this.isActive
    console.log("activeBoolean" +activeBoolean);
    const observablelogo = this.bookService.uploadlogo(this.file);
    observablelogo.subscribe(data=>{console.log("expected to be uploaed: "+ data)   
    const observable =  this.bookService.updateBooks(this.file, formData.category, formData.title,  this.currentUser.id,
      formData.price,formData.publisher, activeBoolean, formData.content, formData.bookId );
      
      
      observable.subscribe({
      next:(data:Book)=> {
        console.log("DAT   "+data);
        this.isUpdate=false;
        //this.router.navigate(["/createdbooks"]); 
     //   location.reload();
      },
      error:(err)=> {console.log("Error:" + err.message); this.createstatus=err}

    }); 
  })

            
}

}
