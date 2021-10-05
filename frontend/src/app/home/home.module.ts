import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HomePage} from "./pages/home.page";
import {AuthButtonComponent} from "./components/auth-button/auth-button.component";
import {LoginComponent} from "./components/login/login.component";
import {HomeRoutingModule} from "./home-routing.module";

@NgModule({
  declarations: [
    HomePage,
    AuthButtonComponent,
    LoginComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule
  ],
  exports: [
    LoginComponent
  ]
})
export class HomeModule { }
