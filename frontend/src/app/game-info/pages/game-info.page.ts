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

  //TODO: Find player id from auth
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
  squadURL: string = "";
  messagesURL: string = "";

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const gameID: number = parseInt(this.route.snapshot.paramMap.get("id")!);
    this.gameInfoAPI.getGameById(gameID)
      .subscribe((game) => {
        this.gameName = game.name;
        this.gameState = game.state;
        this.gameDescription = game.description;
        this.squadURL = game.squadURL;
        this.mapInfo = {
          nw_lat: game.nw_lat,
          se_lat: game.se_lat,
          nw_long: game.nw_long,
          se_long: game.se_long
        };
        this.messagesURL = game.messages;
        });

    /*this.gameInfoAPI.getCurrentPlayerInfo(gameID,this.playerID)
      .subscribe((player) => {
        this.biteCode = player.biteCode;
      })*/
  }

}
