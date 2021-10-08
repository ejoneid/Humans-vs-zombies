import { Component, OnInit } from '@angular/core';
import {ActiveGame} from "../../models/active-game.model";
import {HomeAPI} from "../api/home.api";

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.css']
})
export class HomePage implements OnInit {

  private activeGames: ActiveGame[] = [];

  constructor(private readonly homeAPI: HomeAPI) {

    //-----
    //Just for testing purposes
    /*for (let i = 0; i < 12; i++) {
      this.activeGames.push(new TestGame());
    }*/
    //-----
  }

  ngOnInit(): void {
    //Filling the list with active games
    this.homeAPI.getGames()
      .subscribe((games) => {
        for (let game of games) {
          this.activeGames.push(
            {id: game.id, name: game.name, gameState: game.gameState}
          )
        }
      })
  }

  public get games(): ActiveGame[] {
    return this.activeGames;
  }
}

//-----
//Just for testing purposes
/*class TestGame implements ActiveGame {
  id =  1;
  name = "Test name";
  gameState = "Open";
}*/
//-----
