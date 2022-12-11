import { Component, OnInit } from '@angular/core';
import { JwtResponse } from '../_model/jwtResponse.model';
import { User } from '../_model/user.model';
import {UserService} from '../_service/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageResponse } from '../_model/messageresponse.model';
import { Role } from '../_model/role.model';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  submitted = false;
  gotRegistrationMessage: MessageResponse;
  registrationErrorMessage: any;

  constructor(
      private formBuilder: FormBuilder,
      private userService: UserService,
      private router:Router
  ) {
    
  }

  // public register(){
  //   console.log("test");
  //   // this.userService.register(this.user);
  //   const observable = this.userService.register(this.user);
  //   observable.subscribe((response:any)=>{
  //     console.log(response);

  //   })
  //   console.error('error has happed'+this.user)
  // }
  // public login(userName:string, password:string){
  //   console.log("un:"+ userName);
  //   this.userService.login(userName,password).subscribe((data)=>{
  //     console.log("DATA: "+data.password + data.username + " "+data.accessToken);

  //   })
  // }




  ngOnInit(): void {
    //this.login("Rudra","pwd1");

    this.registerForm = this.formBuilder.group({
      userName: ['', Validators.required],
      password: ['',[Validators.required, Validators.minLength(3)] ],
      email: ['', Validators.required],
      role: ['', Validators.required],

  });
}

  

    // convenience getter for easy access to form fields
    get f() { return this.registerForm.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.registerForm.invalid) {
        return;
    }

    // building JSON for form data
    let rolename:string = this.f['role'].value==1?"Author":"Reader";
    const formData:User = {
      userName : this.f['userName'].value,
      password : this.f['password'].value,
      email : this.f['email'].value,
      roles : new Role(this.f['role'].value, rolename)
    };

    this.loading = true;
    this.gotRegistrationMessage=null;
    this.registrationErrorMessage=null
    this.userService.register(formData)
        .subscribe({
          next: (data:MessageResponse) => {
            console.log("success: "+ data.message)
            this.gotRegistrationMessage=data
           // this.alertService.success('Registration successful', true);
            this.router.navigate(['/signin']);
        },
          error: err => {
            //this.alertService.error(error);
            //this.loading = false;
            console.log("error: "+ err);
            this.registrationErrorMessage=err;
        }

        });
}


}
