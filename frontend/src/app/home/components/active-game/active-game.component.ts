import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {UserPlayer} from "../../../models/input/user-player.model";
import {HomeAPI} from "../../api/home.api";

@Component({
  selector: 'app-active-game',
  templateUrl: './active-game.component.html',
  styleUrls: ['./active-game.component.css']
})
export class ActiveGameComponent implements OnInit {

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
  public activePlayers!: UserPlayer[];

  public playerID: number | null = null;

  constructor(private readonly router: Router, private readonly homeAPI: HomeAPI) { }

  ngOnInit(): void {
    for (let player of this.activePlayers) {
      if (player.gameID === this.gameId) {
        this.playerID = player.id
      }
    }
  }

  toGameInfo(gameId: number, playerId: number | null): Promise<boolean> {
    if (playerId == null) {
      this.homeAPI.createPlayer(gameId)
        .then(res => res.subscribe(
          data => playerId = data.id
        ))
    }
    return this.router.navigate(["game/"+gameId+"/player/"+playerId]);
  }
  toGameInfoAdmin(gameId: number): Promise<boolean> {
    return this.router.navigate(["game/"+gameId+"/admin"]);
  }

}
