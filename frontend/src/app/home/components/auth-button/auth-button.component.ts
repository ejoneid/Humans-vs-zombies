import { Component, Inject, OnInit } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { DOCUMENT } from '@angular/common';
import {HomeAPI} from "../../api/home.api";

@Component({
  selector: 'app-auth-button',
  templateUrl: './auth-button.component.html',
  styleUrls: ['./auth-button.component.css']
})
export class AuthButtonComponent implements OnInit {
  getRequestSent = false;
  postRequestSent = false;

  constructor(@Inject(DOCUMENT) public document: Document, public auth: AuthService, private readonly homeAPI: HomeAPI) { }

  ngOnInit(): void {
    this.auth.idTokenClaims$.subscribe((token) => {
      if (!this.getRequestSent) { //Ensures only one request is sent
        this.getRequestSent = true;
        this.homeAPI.checkUser()
          .then(res => {
            res.subscribe(
            data => data, //If the user is found
            () => { //If user doesn't exist
              if (!this.postRequestSent) { //Ensures only one request is sent
                this.postRequestSent = true;
                this.homeAPI.createUser({firstName: token!.given_name, lastName: token!.family_name})
                  .then(res => {res.subscribe(data => data)});
              }
            });
        });
      }
    });
  }
}
