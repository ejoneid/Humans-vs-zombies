import { Component, OnInit } from '@angular/core';
import {GameInfoAPI} from "../api/game-info.api";
import {ActivatedRoute} from "@angular/router";
import {PlayerInfo} from "../../models/input/player-info.model";
import {GameInfo} from "../../models/input/game-info.model";
import {Mission} from "../../models/input/mission.model";
import {Kill} from "../../models/input/kill.model";
import {Message} from "../../models/input/message.model";
import {WebSocketAPI} from "../api/WebSocketApi.api";
import LatLng = google.maps.LatLng;

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
    map_info: {nw_lat: null, nw_long: null, se_lat: null, se_long: null},
    //TODO: Filter messages in HEAD
    messages: [],
    kills: [],
    missions: []
  };
  private messagesURL!: string;

  private selectedChat = "Global";
  private prevMessageSent: String | undefined;

  private webSocketAPI!: WebSocketAPI;

  locationRequested: boolean = false;

  biteLocation: LatLng | null = null;

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute) { }

  ngOnInit(): void {
    //Finding gameID from the optional params
    this.gameInfo.id = parseInt(this.route.snapshot.paramMap.get("id")!);

    //Getting information about the specific game
    this.gameInfoAPI.getGameById(this.gameInfo.id)
      .then((response) => {
        response.subscribe((game) => {
            this.gameInfo.name = game.name;
            this.gameInfo.state = game.gameState;
            this.gameInfo.description = game.description;
            this.gameInfo.map_info = {
              nw_lat: game.nw_lat,
              se_lat: game.se_lat,
              nw_long: game.nw_long,
              se_long: game.se_long
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
        response.subscribe((squads) => {
          const members: PlayerInfo[] = [];
          for (let member of squads[0].members) {
            members.push({name: member.name, state: member.human});
          }
          this.gameInfo.squad_info = {name: squads[0].name, members: members, id: squads[0].id};
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
              id: mission.id,
              description: mission.description.toString(),
              endTime: mission.endTime.toString(),
              startTime: mission.startTime.toString(),
              lat: parseFloat(mission.lat),
              lng: parseFloat(mission.lng),
              human: mission.isHuman,
              gameId: this.game.id
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
              id: kill.id,
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

    this.webSocketAPI = new WebSocketAPI(this);
    this.connect();
  }

  getLocation(): void {
    this.locationRequested = true;
  }

  locationFound(location: LatLng): void {
    this.locationRequested = false;
    this.biteLocation = location;
  }

  registerUser(): void {
    this.gameInfoAPI.registerForGame(this.gameInfo.id, {userID: this.gameInfo.player_id})
      .then(res => res.subscribe(
        data => console.log(data)
      ))
  }

  connect(){
    this.webSocketAPI._connect();
  }

  disconnect(){
    this.webSocketAPI._disconnect();
  }

  sendMessage(){
    this.webSocketAPI._send(this.gameInfo.id);
  }

  handleMessage(){
    if (this.selectedChat == "Global") this.loadGlobalChat();
    else if (this.selectedChat == "Faction") this.loadFactionChat();
    else if (this.selectedChat == "Squad") this.loadSquadChat();
  }

  get game(): GameInfo {
    return this.gameInfo;
  }

  public loadGlobalChat() {
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
      this.gameInfoAPI.sendGlobalChat(this.gameInfo.id, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            this.loadGlobalChat();
          })
        });
    } else if (this.selectedChat == "Faction") {
      this.gameInfoAPI.sendFactionChat(this.gameInfo.id, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            this.loadFactionChat();
          })
        });
    } else {
      this.gameInfoAPI.sendSquadChat(this.gameInfo.id, this.gameInfo.squad_info!.id, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            this.loadSquadChat();
          })
        });
    }
    this.sendMessage();
  }
}
