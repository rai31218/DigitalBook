import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BooksComponent } from './books/books.component';
import { CreateNewBookComponent } from './create-new-book/create-new-book.component';
import { CreatedBookComponent } from './created-book/created-book.component';
import { LoginComponent } from './login/login.component';
import { SubscribedBookComponent } from './subscribed-book/subscribed-book.component';
import { UsersComponent } from './users/users.component';

const routes: Routes = [
  { path: '', component: BooksComponent
  //, canActivate: [AuthGuard] 
},
{ path: 'signup', component: UsersComponent
},
{ path: 'signin', component: LoginComponent
},
{ path: 'subescribedbooks', component: SubscribedBookComponent
},
{ path: 'createdbooks', component: CreatedBookComponent
},
{ path: 'createbooks', component: CreateNewBookComponent
},


  // { path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
  // { path: 'selfposts', component: PostsComponent, canActivate: [AuthGuard]},
  // { path: 'login', component: LoginComponent },
  // { path: 'register', component: RegisterComponent },

  // // otherwise redirect to home
  // { path: '**', component: NotfoundComponent }




];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }


