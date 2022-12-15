import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Book } from '../_model/book.model';
import { JwtResponse } from '../_model/jwtResponse.model';
import { BookService } from '../_service/book.service';
import { UserService } from '../_service/user.service';

@Component({
  selector: 'app-create-new-book',
  templateUrl: './create-new-book.component.html',
  styleUrls: ['./create-new-book.component.css']
})
export class CreateNewBookComponent implements OnInit {
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
  isSuccess: string="";


  constructor(private formBuilder: FormBuilder,
    private router: Router,
    private bookService: BookService,
    private sanitizer: DomSanitizer,
    private userService: UserService) {

      this.currentUser = this.userService.currentUserValue;
     }
  ngOnInit(): void {
    this.createBookForm = new FormGroup({
      "logo": new FormControl("",Validators.required ),
      "category": new FormControl("",Validators.required ),
      "title": new FormControl("",Validators.required ) ,
      "publisher" : new FormControl("",Validators.required ),
      "price":  new FormControl("",Validators.required ),
      "active":  new FormControl("",Validators.required ),
      "content":  new FormControl("",Validators.required )
});
  }
  get f1() { return this.createBookForm.controls; }


  
  onChange(event) {
    this.file = event.target.files[0];
 }

  onSubmitToCreate() {
    console.log("came");
    this.submittedForCreation = true;
    // stop here if form is invalid
    console.log("current user: "+ this.currentUser);

    if (this.createBookForm.invalid) {
      console.log("Invalid?")
        return;
    }  
    // building JSON for form data
    const formData = {
      logo: this.f1['logo'].value,
      category : this.f1['category'].value,
      title : this.f1['title'].value,
    // author : this.f1['author'].value,
      publisher : this.f1['publisher'].value,
      price : this.f1['price'].value,
      active: this.f1['active'].value,
      content: this.f1['content'].value
    }; 
    console.log("Form DATA: "+ this.file);
   // this.loading = true;
      let activeBoolean:boolean =formData.active==1?true:false;
      console.log("activeBoolean" +activeBoolean);
      const observablelogo = this.bookService.uploadlogo(this.file);
      observablelogo.subscribe(data=>{console.log("expected to be uploaed: "+ data)   
      const observable =  this.bookService.createBooks(this.file, formData.category, formData.title,  this.currentUser.id,
        formData.price,formData.publisher, activeBoolean, formData.content );
        
        
        observable.subscribe({
        next:(data:number)=> {
          this.isSuccess="Book is created";
        },
        error:(err)=> {console.log("Error:" + err.message); this.createstatus=err}

      }); 
    })

              
}

}
