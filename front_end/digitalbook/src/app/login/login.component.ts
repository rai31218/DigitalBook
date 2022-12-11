import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { UserService } from "../_service/user.service";
import { MessageId } from "@angular/localize";
import { MessageResponse } from "../_model/messageresponse.model";
import { JwtResponse } from "../_model/jwtResponse.model";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"]
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  loginErrorMessage: MessageResponse;
  alertService: any;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
  ) {
    // redirect to home if already logged in
    if (this.userService.currentUserValue) {
      if(this.userService.currentUserValue.roles[0]=="Reader"){
        this.router.navigate(["/"]);
      }
      if(this.userService.currentUserValue.roles[0]=="Author"){
        this.router.navigate(["/createbooks"]); 
    }
      
    }
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      userName: ["", Validators.required],
      password: ["", Validators.required]
    });

    // // get return url from route parameters or default to '/'
    // this.returnUrl = this.route.snapshot.queryParams["returnUrl"] || "/";
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.loginErrorMessage=null
    this.userService
      .login(this.f['userName'].value, this.f['password'].value)
      .subscribe({
        next: (data:JwtResponse) => {
          console.log("Role::::::::::::::::::;: "+ data.roles[0])
          if(data.roles[0]=="Reader"){
            this.router.navigate(["/"]);
          }
          if(data.roles[0]=="Author"){
            this.router.navigate(["/createbooks"]); 
        }
      },
        error: err=>{this.loginErrorMessage= err}
      })
    
      
  }
}
