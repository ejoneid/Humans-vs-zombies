import { Component, OnInit } from '@angular/core';
import {Message} from "../../models/message.model";
import {GameInfoAPI} from "../api/game-info.api";
import {ActivatedRoute} from "@angular/router";
import {SquadInfo} from "../../models/squad-info.model";
import {MapInfo} from "../../models/map-info.model";
import {PlayerInfo} from "../../models/player-info.model";
import {GameInfo} from "../../models/game-info.model";

@Component({
  selector: 'app-game-info-page',
  templateUrl: './game-info.page.html',
  styleUrls: ['./game-info.page.css']
})
export class GameInfoPage implements OnInit {
  //Holder for the game, initialized in ngOnInit
  private gameInfo!: GameInfo;

  //All the variables are initialized in safe states or error states.
  //TODO: Find player id from auth
  private playerID:number = 1;
  private playerIsHuman: boolean = true;
  private gameName: string = "ERROR: No game name found";
  private gameState: string = "ERROR: No game state found";
  private gameDescription: string = "";
  private biteCode: string = "ERROR: No bite code found";
  private squad: SquadInfo | null = null;
  private mapInfo: MapInfo = {nw_lat: 0, se_lat: 0, nw_long: 0, se_long: 0};
  private messages: Message[] | null = null;
  private squadURL: string = "";
  private messagesURL: string = "";

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute) { }

  ngOnInit(): void {
    //Finding gameID from the optional params
    const gameID: number = parseInt(this.route.snapshot.paramMap.get("id")!);

    //Getting information about the specific game
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

    //Getting information about the specific player.
    this.gameInfoAPI.getCurrentPlayerInfo(gameID,this.playerID)
      .subscribe((player) => {
        this.biteCode = player.biteCode;
      });
    this.gameInfoAPI.getCurrentPlayerSquad(gameID,this.playerID)
      .subscribe((squad) => {
        const members: PlayerInfo[] = [];
        for (let member of squad.members) {
          members.push({name: member.name, state: member.is_human})
        }
        this.squad = {name: squad.name, members: members};
      });

    //Setting all the info into a single object.
    this.gameInfo = {
      bite_code: this.biteCode,
      description: this.gameDescription,
      id: gameID,
      player_is_human: this.playerIsHuman,
      player_id: this.playerID,
      map_info: this.mapInfo,
      messages: this.messages,
      name: this.gameName,
      player_count: 0,
      squad_info: this.squad,
      state: this.gameState
    }
  }

  get game(): GameInfo {
    return this.gameInfo;
  }
}
