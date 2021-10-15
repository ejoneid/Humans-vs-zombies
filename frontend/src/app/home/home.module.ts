import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HomePage} from "./pages/home.page";
import {AuthButtonComponent} from "./components/auth-button/auth-button.component";
import {HomeRoutingModule} from "./home-routing.module";
import { ActiveGameComponent } from './components/active-game/active-game.component';
import { CreateGameComponent } from './components/create-game/create-game.component';
import {MatCardModule} from "@angular/material/card";
import {MatDialogModule} from "@angular/material/dialog";

@NgModule({
  declarations: [
    HomePage,
    AuthButtonComponent,
    ActiveGameComponent,
    CreateGameComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatCardModule,
    MatDialogModule
  ],
  exports: [
  ]
})
export class HomeModule { }
