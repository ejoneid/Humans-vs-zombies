import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { AuthModule } from '@auth0/auth0-angular';
import {HomeModule} from "./home/home.module";
import {GameInfoModule} from "./game-info/game-info.module";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HomeModule,
    GameInfoModule,
    AuthModule.forRoot({
      domain: 'dev-fwlq8v0n.eu.auth0.com',
      clientId: 'UhMx2hMd70OxMqZZFhZZctAIWRuVvaA2'
    }),
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
