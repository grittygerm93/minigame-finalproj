import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {AuthenticationGuard} from "./authentication.guard";
import {LoginComponent} from "./login/login.component";
import {BadreqComponent} from "./badreq/badreq.component";
import {RegisterComponent} from "./register/register.component";
import {MainComponent} from "./main/main.component";
import {ShareComponent} from "./share/share.component";

const routes: Routes = [
  // AuthenticationGuard
  // {
  //   path: '', canActivate: [AuthenticationGuard], children: [
  //     {path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard]},
  //     {path: 'game', component: MainComponent},
  //     {path: 'share', component: ShareComponent},
  //     {path: 'register', component: RegisterComponent},
  //     {path: 'login', component: LoginComponent},
  //     {path: 'badrequest', component: BadreqComponent},
  //   ],
  // },
  {path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard]},
  {path: 'game', component: MainComponent, canActivate: [AuthenticationGuard]},
  {path: 'share', component: ShareComponent, canActivate: [AuthenticationGuard]},
  {path: 'register', component: RegisterComponent, canActivate: [AuthenticationGuard]},
  {path: 'login', component: LoginComponent, canActivate: [AuthenticationGuard]},
  {path: 'badrequest', component: BadreqComponent, canActivate: [AuthenticationGuard]},
  {path: '**', redirectTo: 'home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
