import { Component, OnInit } from '@angular/core';
import {ActiveGame} from "../../models/active-game.model";

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.css']
})
export class HomePage implements OnInit {

  private activeGames: ActiveGame[] = [];

  constructor() {
    //-----
    //Just for testing purposes
    for (let i = 0; i < 12; i++) {
      this.activeGames.push(new TestGame());
    }
    //-----
  }

  ngOnInit(): void {
  }

  public get games(): ActiveGame[] {
    return this.activeGames;
  }
}

//-----
//Just for testing purposes
class TestGame implements ActiveGame {
  id =  1;
  name = "Test name";
  nw_lat = null;
  nw_long = null;
  se_lat = null;
  se_long = null;
  state = "Zombies are bad";
}
//-----
