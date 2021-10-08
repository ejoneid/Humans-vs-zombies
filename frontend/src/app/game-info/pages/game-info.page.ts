import { Component, OnInit } from '@angular/core';
import {Message} from "../../models/message.model";
import {GameInfoAPI} from "../api/game-info.api";
import {ActivatedRoute} from "@angular/router";
import {SquadInfo} from "../../models/squad-info.model";
import {MapInfo} from "../../models/map-info.model";
/*import {GameInfo} from "../../models/game-info.model";*/

@Component({
  selector: 'app-game-info-page',
  templateUrl: './game-info.page.html',
  styleUrls: ['./game-info.page.css']
})
export class GameInfoPage implements OnInit {

  // Should be set from a request to the backend in the constructor
  /*gameInfo!: GameInfo;*/

  playerID:number = 1;

  playerName: string = "ERROR: No player name found";
  playerIsHuman: boolean = true;

  id: number = 0;
  gameName: string = "ERROR: No game name found";
  gameState: string = "ERROR: No game state found";
  gameDescription: string = "";
  biteCode: string = "ERROR: No bite code found";
  squad: SquadInfo | null = null;
  mapInfo: MapInfo | null = null;
  messages: Message[] | null = null;

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const gameID: number = parseInt(this.route.snapshot.paramMap.get("id")!);
    this.gameInfoAPI.getGameById("api/game/"+gameID)
      /*.subscribe((game) => {
        const name = game.name;
        const state = game.state;
        }

      )*/
    this.gameInfoAPI.getCurrentPlayerInfo(gameID,this.playerID)
  }

}
