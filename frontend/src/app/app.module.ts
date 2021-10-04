import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { AuthModule } from '@auth0/auth0-angular';
import { AuthButtonComponent } from './auth-button/auth-button.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthButtonComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthModule.forRoot({
      domain: 'dev-fwlq8v0n.eu.auth0.com',
      clientId: 'UhMx2hMd70OxMqZZFhZZctAIWRuVvaA2'
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
