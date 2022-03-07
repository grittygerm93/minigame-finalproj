import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SampleService} from "./sample.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {RequestInterceptor} from "./request.interceptor";
import {ReactiveFormsModule} from "@angular/forms";
import {BadreqComponent} from './badreq/badreq.component';
import {RegisterComponent} from './register/register.component';
import {MainComponent} from './main/main.component';
import {GameComponent} from './game/game.component';
import {SingleplayergameComponent} from './singleplayergame/singleplayergame.component';
import { ShareComponent } from './share/share.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatDividerModule} from "@angular/material/divider";
import {MatCardModule} from "@angular/material/card";
import {AuthenticationGuard} from "./authentication.guard";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    BadreqComponent,
    RegisterComponent,
    MainComponent,
    GameComponent,
    SingleplayergameComponent,
    ShareComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    FlexLayoutModule,
    MatFormFieldModule,
    MatInputModule,
    MatDividerModule,
    MatCardModule
  ],
  providers: [SampleService, {provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi: true}, AuthenticationGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
