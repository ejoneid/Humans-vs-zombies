import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserPlayer} from "../../../models/input/user-player.model";
import {HomeAPI} from "../../api/home.api";

@Component({
  selector: 'app-active-game',
  templateUrl: './active-game.component.html',
  styleUrls: ['./active-game.component.css']
})
export class ActiveGameComponent implements OnInit, OnChanges {

  @Input()
  public gameName: String = "";
  @Input()
  public gameStart: String = "";
  @Input()
  public gameEnd: String = "";
  @Input()
  public gameStatus: String = "";
  @Input()
  public gameId: number = 0;
  @Input()
  public activePlayers: UserPlayer[] | null = null;

  public playerID: number | null = null;

  constructor(private readonly router: Router, private readonly homeAPI: HomeAPI) { }

  ngOnInit(): void {

  }

  ngOnChanges() {
    if (this.activePlayers != null) {
      for (let player of this.activePlayers) {
        if (player.gameID === this.gameId) {
          this.playerID = player.id
        }
      }
    }
  }

  toGameInfo(gameId: number, playerId: number | null): Promise<boolean> | void {
    if (playerId == null) {
      this.homeAPI.createPlayer(gameId)
        .then(res => res.subscribe( //TODO: Fix the 404 here
          data => {
            playerId = data.id
            return this.router.navigate(["game/"+gameId+"/player/"+playerId]);
          }
        ));
    }
    else {
      return this.router.navigate(["game/"+gameId+"/player/"+playerId]);
    }

  }
  toGameInfoAdmin(gameId: number): Promise<boolean> {
    return this.router.navigate(["game/"+gameId+"/admin"]);
  }

}
