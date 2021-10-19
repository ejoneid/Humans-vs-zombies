import { Component, OnInit } from '@angular/core';
import {ActiveGame} from "../../models/input/active-game.model";
import {HomeAPI} from "../api/home.api";
import {CreateGameComponent} from "../components/create-game/create-game.component";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.css']
})
export class HomePage implements OnInit {

  private activeGames: ActiveGame[] = [];

  public players: {name: string, id: number}[] = [];

  constructor(private readonly homeAPI: HomeAPI, private dialog: MatDialog, private readonly router: Router) {
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
}
