import {Component, Input, OnChanges} from '@angular/core';
import {Router} from "@angular/router";
import {UserPlayer} from "../../../models/input/user-player.model";
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
  public activePlayer: UserPlayer | null = null;
  @Input()
  public isMobile!: boolean;
  @Input()
  public isAdmin!: boolean;

  public playerID: number | null = null;

  constructor(private readonly router: Router, public auth: AuthService) { }

  ngOnChanges() {
    if (this.activePlayer != null) {
        if (this.activePlayer.gameID === this.gameId) {
          this.playerID = this.activePlayer.id
        }
    }
  }

  toGameInfo(gameId: number, playerId: number | null): Promise<boolean> | void {
    if (playerId == null) { //TODO: Make this only apply when a game is actually joined. This will also fix the issues with admin access.
      return this.router.navigate(["game/"+gameId]);
    }
    else {
      return this.router.navigate(["game/"+gameId+"/player/"+playerId]);
    }

  }
  toGameInfoAdmin(gameId: number): Promise<boolean> {
    return this.router.navigate(["game/"+gameId+"/admin"]);
  }

}
