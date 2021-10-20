import {Component, Input, OnChanges} from '@angular/core';
import {Router} from "@angular/router";
import {UserPlayer} from "../../../models/input/user-player.model";
import {HomeAPI} from "../../api/home.api";
import {AuthService} from "@auth0/auth0-angular";

@Component({
  selector: 'app-active-game',
  templateUrl: './active-game.component.html',
  styleUrls: ['./active-game.component.css']
})
export class ActiveGameComponent implements OnChanges {

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

  constructor(private readonly router: Router, private readonly homeAPI: HomeAPI, public auth: AuthService) { }

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
    if (playerId == null) { //TODO: Make this only apply when a game is actually joined. This will also fix the issues with admin access.
      this.homeAPI.createPlayer(gameId)
        .then(res => res.subscribe(
          data => { //If the response is ok, there is a player that can be used.
            playerId = data.id
            return this.router.navigate(["game/"+gameId+"/player/"+playerId]);
          },
          () => { //If the user is an admin, they can't get a player, so we send them in without any player info.
            return this.router.navigate(["game/"+gameId]);
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
