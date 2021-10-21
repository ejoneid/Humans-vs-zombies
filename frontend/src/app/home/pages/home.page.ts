import { Component, OnInit } from '@angular/core';
import {ActiveGame} from "../../models/input/active-game.model";
import {HomeAPI} from "../api/home.api";
import {CreateGameComponent} from "../components/create-game/create-game.component";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {UserPlayer} from "../../models/input/user-player.model";

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.css']
})
export class HomePage implements OnInit {

  private activeGames: ActiveGame[] = [];

  public players: UserPlayer[] = [];

  public isMobile: boolean;

  constructor(private readonly homeAPI: HomeAPI, private dialog: MatDialog, private readonly router: Router) {
    this.isMobile = window.innerWidth < 768;
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
    let gameCreated = false;
    const dialogRef = this.dialog.open(CreateGameComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined && !gameCreated) {
        gameCreated = true; //Ensures only one request is sent to the server.
        this.homeAPI.createGame(result)
          .then(res => res.subscribe(
            data => this.router.navigate(["game/"+data.id+"/admin"])
          ));
      }
    });
  }

  public get games(): ActiveGame[] {
    return this.activeGames;
  }

  public localError() {
    throw Error('The app component has thrown an error!');
  }
}
