import { Component, OnInit } from '@angular/core';
import {ActiveGame} from "../../models/active-game.model";

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.css']
})
export class HomePage implements OnInit {

  private activeGames: ActiveGame[] = [];

  constructor() { }

  ngOnInit(): void {
  }

  public get games(): ActiveGame[] {
    return this.activeGames;
  }

}
