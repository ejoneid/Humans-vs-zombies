import { Component, OnInit } from '@angular/core';
import {GameInfoAPI} from "../api/game-info.api";
import {ActivatedRoute} from "@angular/router";
import {PlayerInfo} from "../../models/player-info.model";
import {GameInfo} from "../../models/game-info.model";

@Component({
  selector: 'app-game-info-page',
  templateUrl: './game-info.page.html',
  styleUrls: ['./game-info.page.css']
})
export class GameInfoPage implements OnInit {
  //Holder for the game, initialized in ngOnInit
  private gameInfo: GameInfo = {
    id: 0,
    //All the variables are initialized in safe states or error states.
    //TODO: Find player id from auth
    player_id: 1,
    player_is_human: true,
    name: "ERROR: No game name found",
    state: "ERROR: No game state found",
    description: "",
    bite_code: "ERROR: No bite code found",
    squad_info: null,
    map_info: null,
    //TODO: Filter messages in HEAD
    messages: null
  };
  private messagesURL!: string;

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute) { }

  ngOnInit(): void {
    //Finding gameID from the optional params
    this.gameInfo.id = parseInt(this.route.snapshot.paramMap.get("id")!);

    //Getting information about the specific game
    this.gameInfoAPI.getGameById(this.gameInfo.id)
      .subscribe((game) => {
        this.gameInfo.name = game.name;
        this.gameInfo.state = game.state;
        this.gameInfo.description = game.description;
        this.gameInfo.map_info = {
          nw_lat: 59.945500,//game.nw_lat,
          se_lat: 59.897553,//game.se_lat,
          nw_long: 10.687306,//game.nw_long,
          se_long: 10.831628//game.se_long
        };
        this.messagesURL = game.messages;
      });

    //Getting information about the specific player.
    this.gameInfoAPI.getCurrentPlayerInfo(this.gameInfo.id, this.gameInfo.player_id)
      .subscribe((player) => {
        this.gameInfo.bite_code = player.biteCode;
      });
    this.gameInfoAPI.getCurrentPlayerSquad(this.gameInfo.id, this.gameInfo.player_id)
      .subscribe((squad) => {
        const members: PlayerInfo[] = [];
        for (let member of squad.members) {
          members.push({name: member.name, state: member.is_human})
        }
        this.gameInfo.squad_info = {name: squad.name, members: members};
      });
  }

  get game(): GameInfo {
    return this.gameInfo;
  }
}
