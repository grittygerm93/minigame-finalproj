import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {AuthenticationGuard} from "./authentication.guard";
import {LoginComponent} from "./login/login.component";
import {BadreqComponent} from "./badreq/badreq.component";
import {RegisterComponent} from "./register/register.component";
import {MainComponent} from "./main/main.component";

const routes: Routes = [

  {
    path: '', canActivate: [AuthenticationGuard], children: [
      {path: '', component: MainComponent},
      {path: 'home', component: HomeComponent},
      {path: 'register', component: RegisterComponent},
      {path: 'login', component: LoginComponent},
      {path: 'badrequest', component: BadreqComponent},
    ],
  },
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
