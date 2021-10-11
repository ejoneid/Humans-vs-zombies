import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { AuthHttpInterceptor, AuthModule } from '@auth0/auth0-angular';
import {HomeModule} from "./home/home.module";
import {GameInfoModule} from "./game-info/game-info.module";
import {HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";

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
      // The domain and clientId were configured in the previous chapter
      domain: 'dev-fwlq8v0n.eu.auth0.com',
      clientId: 'UhMx2hMd70OxMqZZFhZZctAIWRuVvaA2',
    
      // Request this audience at user authentication time
      audience: 'https://hvz/api',
    
      // Request this scope at user authentication time
      scope: 'admin:permissions',
      
    
      // Specify configuration for the interceptor              
      httpInterceptor: {
        allowedList: ['http://localhost:4200/*', 'http://localhost:8080/api/game']
      }
    }),
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
