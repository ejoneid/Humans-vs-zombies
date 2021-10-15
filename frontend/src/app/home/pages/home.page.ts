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

  createGame(): void {

  }

  public get games(): ActiveGame[] {
    return this.activeGames;
  }
}
