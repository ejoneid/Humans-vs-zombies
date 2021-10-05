import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HomePage} from "./pages/home.page";
import {AuthButtonComponent} from "./components/auth-button/auth-button.component";
import {HomeRoutingModule} from "./home-routing.module";
import { ActiveGameComponent } from './components/active-game/active-game.component';

@NgModule({
  declarations: [
    HomePage,
    AuthButtonComponent,
    ActiveGameComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule
  ],
  exports: [
  ]
})
export class HomeModule { }
