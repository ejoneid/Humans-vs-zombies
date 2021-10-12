import { Component, OnInit } from '@angular/core';
import {GameInfoAPI} from "../api/game-info.api";
import {ActivatedRoute} from "@angular/router";
import {PlayerInfo} from "../../models/player-info.model";
import {GameInfo} from "../../models/game-info.model";
import {Mission} from "../../models/mission.model";
import {Kill} from "../../models/kill.model";
import {Message} from "../../models/message.model";

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
    messages: [],
    kills: [],
    missions: []
  };
  private messagesURL!: string;

  private selectedChat = "Global";
  private prevMessageSent: String | undefined;

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute) { }

  ngOnInit(): void {
    //Finding gameID from the optional params
    this.gameInfo.id = parseInt(this.route.snapshot.paramMap.get("id")!);

    //Getting information about the specific game
    this.gameInfoAPI.getGameById(this.gameInfo.id)
      .then((response) => {
        response.subscribe((game) => {
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
      });

    //Getting information about the specific player.
    this.gameInfoAPI.getCurrentPlayerInfo(this.gameInfo.id, this.gameInfo.player_id)
      .then((response) => {
        response.subscribe((player) => {
          this.gameInfo.bite_code = player.biteCode;
        });
      });
    this.gameInfoAPI.getCurrentPlayerSquad(this.gameInfo.id, this.gameInfo.player_id)
      .then((response) => {
        response.subscribe((squad) => {
          const members: PlayerInfo[] = [];
          for (let member of squad.players) {
            members.push({name: member.name, state: member.human});
          }
          this.gameInfo.squad_info = {name: squad.name, members: members, id: squad.id};
        });
      });

    //Getting information about map markers.
    const tempMissions: Mission[] = [];
    this.gameInfoAPI.getMissionsByGame(this.gameInfo.id)
      .then((response) => {
        response.subscribe((missions) => {
          for (let mission of missions) {
            tempMissions.push({
              name: mission.name.toString(),
              description: mission.description.toString(),
              endTime: mission.endTime.toString(),
              startTime: mission.startTime.toString(),
              lat: parseFloat(mission.lat),
              lng: parseFloat(mission.lng),
              isHuman: mission.isHuman
            });
          }
          this.gameInfo.missions = tempMissions;
        });
      });
    const tempKills: Kill[] = [];
    this.gameInfoAPI.getKillsByGame(this.gameInfo.id)
      .then((response) => {
        response.subscribe((kills) => {
          for (let kill of kills) {
            tempKills.push({
              killerName: kill.killerName.toString(),
              lat: parseFloat(kill.lat),
              lng: parseFloat(kill.lng),
              story: kill.story.toString(),
              timeOfDeath: kill.timeOfDeath.toString(),
              victimName: kill.victimName.toString()
            });
          }
          this.gameInfo.kills = tempKills;
        });
      });

    const tempMessages: Message[] = [];
    this.gameInfoAPI.getGameChat(this.gameInfo.id)
      .then((response) => {
        response.subscribe((messages) => {
          for (let message of messages) {
            tempMessages.push({
              id:message.id,
              global: message.global,
              human: message.human,
              sender: message.playerName,
              time: message.messageTime,
              content: message.message
            });
          }
          this.gameInfo.messages = tempMessages;
        })
      });
  }

  get game(): GameInfo {
    return this.gameInfo;
  }

  loadGlobalChat() {
    this.selectedChat = "Global";
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getGameChat(this.gameInfo.id)
      .then((response) => {
        response.subscribe((messages) => {
          for (let message of messages) {
            tempMessages.push({
              id:message.id,
              global: message.global,
              human: message.human,
              sender: message.playerName,
              time: message.messageTime,
              content: message.message
            });
          }
          this.gameInfo.messages = tempMessages;
        })
      });
  }

  loadFactionChat() {
    this.selectedChat = "Faction";
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getFactionChat(this.gameInfo.id, this.gameInfo.player_is_human)
      .then((response) => {
        response.subscribe((messages) => {
          for (let message of messages) {
            tempMessages.push({
              id:message.id,
              global: message.global,
              human: message.human,
              sender: message.playerName,
              time: message.messageTime,
              content: message.message
            });
          }
          this.gameInfo.messages = tempMessages;
        })
      });
  }

  loadSquadChat() {
    this.selectedChat = "Squad";
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getSquadChat(this.gameInfo.id, this.gameInfo.squad_info!.id)
      .then((response) => {
        response.subscribe((messages) => {
          for (let message of messages) {
            tempMessages.push({
              id:message.id,
              global: message.global,
              human: message.human,
              sender: message.playerName,
              time: message.messageTime,
              content: message.message
            });
          }
          this.gameInfo.messages = tempMessages;
        })
      });
  }

  sendChatMessage(message: String) {
    if (this.selectedChat == "Global") {
      this.gameInfoAPI.sendGlobalChat(this.gameInfo.id, this.gameInfo.player_id, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            this.loadGlobalChat();
          })
        });
    } else if (this.selectedChat == "Faction") {
      this.gameInfoAPI.sendFactionChat(this.gameInfo.id, this.gameInfo.player_id, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            this.loadFactionChat();
          })
        });
    } else {
      this.gameInfoAPI.sendSquadChat(this.gameInfo.id, this.gameInfo.squad_info!.id, this.gameInfo.player_id, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            this.loadSquadChat();
          })
        });
    }
  }
}
