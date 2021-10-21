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
import {SquadCheckIn} from "../../models/input/squad-check-in.model";

@Component({
  selector: 'app-game-info-page',
  templateUrl: './game-info.page.html',
  styleUrls: ['./game-info.page.css']
})
export class GameInfoPage implements OnInit {
  //Holder for the game, initialized in ngOnInit
  private gameInfo: GameInfo = {
    gameID: 0,
    //All the variables are initialized in safe states or error states.
    //TODO: Find player id from auth
    playerID: 1,
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
    missions: [],
    squadCheckIns: []
  };
  public allSquads = null;

  private messagesURL!: string;

  private selectedChat = "Global";
  private prevMessageSent: String | undefined;

  private webSocketAPI!: WebSocketAPI;

  locationRequested: boolean = false;

  playerLocation: LatLng | null = null;

  public isMobile: boolean;

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute) {
    this.isMobile = window.innerWidth < 768;
  }

  ngOnInit(): void {
    //Finding gameID and playerID from the optional params
    this.gameInfo.gameID = parseInt(this.route.snapshot.paramMap.get("id")!);
    this.gameInfo.playerID = parseInt(this.route.snapshot.paramMap.get("playerId")!);

    //Getting information about the specific game
    this.gameInfoAPI.getGameById(this.gameInfo.gameID)
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
    this.gameInfoAPI.getCurrentPlayerInfo(this.gameInfo.gameID, this.gameInfo.playerID)
      .then((response) => {
        response.subscribe((player) => {
          this.gameInfo.bite_code = player.biteCode;
        });
      });
    this.updateSquad(); //Also updates the squad check-ins.
    //Getting information about map markers.
    this.updateMissions();
    this.updateKills();
    this.updateMessagesGlobal();

    this.gameInfoAPI.getAllSquads(this.gameInfo.gameID)
      .then(res => {
        res.subscribe(squads => this.allSquads = squads)
      });

    // Connecting the WebSocket
    this.webSocketAPI = new WebSocketAPI(this);
    this.connect();
  }

  updateMissions() {
    const tempMissions: Mission[] = [];
    this.gameInfoAPI.getMissionsByGame(this.gameInfo.gameID)
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
              gameId: this.game.gameID
            });
          }
          this.gameInfo.missions = tempMissions;
        });
      });
  }

  updateCheckIns() {
    const tempCheckIns: SquadCheckIn[] = [];
    if (this.gameInfo.squad_info != null) {
      this.gameInfoAPI.getSquadCheckIns(this.gameInfo.gameID, this.gameInfo.squad_info.id)
        .then((response) => {
          response.subscribe((checkIns) => {
            console.log(checkIns)
            for (let checkIn of checkIns) {
              tempCheckIns.push({
                id: checkIn.id,
                lat: parseFloat(checkIn.lat),
                lng: parseFloat(checkIn.lng),
                time: checkIn.time.toString(),
                member:  {
                  name: checkIn.member.name.toString(),
                  human: checkIn.member.human,
                },
              });
            }
            this.gameInfo.squadCheckIns = tempCheckIns;
          });
        });
    }
  }

  updateKills() {
    const tempKills: Kill[] = [];
    this.gameInfoAPI.getKillsByGame(this.gameInfo.gameID)
      .then((response) => {
        response.subscribe((kills) => {
          for (let kill of kills) {
            tempKills.push({
              id: kill.id,
              killerName: kill.killerName.toString(),
              lat: parseFloat(kill.lat),
              lng: parseFloat(kill.lng),
              story: kill.story,
              timeOfDeath: kill.timeOfDeath.toString(),
              victimName: kill.victimName.toString()
            });
          }
          this.gameInfo.kills = tempKills;
        });
      });
  }

  updateMessagesGlobal() {
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getGameChat(this.gameInfo.gameID)
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

  updateSquad() {
    this.gameInfoAPI.getCurrentPlayerSquad(this.gameInfo.gameID, this.gameInfo.playerID)
      .then((response) => {
        response.subscribe((squads) => {
          const members: PlayerInfo[] = [];
          for (let member of squads[0].members) {
            members.push({name: member.player.name, state: member.player.human});
          }
          this.gameInfo.squad_info = {name: squads[0].name, members: members, id: squads[0].id};
          this.updateCheckIns();
        });
      });
  }

  getLocation(): void {
    this.locationRequested = true;
  }

  locationFound(location: LatLng): void {
    this.locationRequested = false;
    this.playerLocation = location;
  }

  registerUser(): void {
    //Check if the user has a player

    //Create a player if not

    //Register for the game
    this.gameInfoAPI.registerForGame(this.gameInfo.gameID, {userID: this.gameInfo.playerID})
      .then(res => res.subscribe(
        data => console.log(data)
      ))
  }

  // Connect to the WebSocket
  connect(){
    this.webSocketAPI._connect();
  }

  // Disconnect from the WebSocket
  disconnect(){
    this.webSocketAPI._disconnect();
  }

  // Send a message to the backend
  sendMessage(){
    this.webSocketAPI._send(this.gameInfo.gameID);
  }

  // Handle message from backend (notification)
  // Reloads the chat
  handleMessage(){
    if (this.selectedChat == "Global") this.loadGlobalChat();
    else if (this.selectedChat == "Faction") this.loadFactionChat();
    else if (this.selectedChat == "Squad") this.loadSquadChat();
  }

  get game(): GameInfo {
    return this.gameInfo;
  }

  // Loads the global chat into the chat window
  public loadGlobalChat() {
    this.selectedChat = "Global";
    this.updateMessagesGlobal();
  }

  // Loads the faction chat into the chat window
  loadFactionChat() {
    this.selectedChat = "Faction";
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getFactionChat(this.gameInfo.gameID, this.gameInfo.player_is_human)
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

  // Loads the squad chat into the chat window
  loadSquadChat() {
    this.selectedChat = "Squad";
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getSquadChat(this.gameInfo.gameID, this.gameInfo.squad_info!.id)
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

  // Method for sending a new chat message to the selected chat
  sendChatMessage(message: String) {
    if (this.selectedChat == "Global") {
      this.gameInfoAPI.sendGlobalChat(this.gameInfo.gameID, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            // Reload chat when a message is sent
            this.loadGlobalChat();
          })
        });
    } else if (this.selectedChat == "Faction") {
      this.gameInfoAPI.sendFactionChat(this.gameInfo.gameID, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            // Reload chat when a message is sent
            this.loadFactionChat();
          })
        });
    } else {
      this.gameInfoAPI.sendSquadChat(this.gameInfo.gameID, this.gameInfo.squad_info!.id, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            // Reload chat when a message is sent
            this.loadSquadChat();
          })
        });
    }
    this.sendMessage();
  }

  joinSquad(squadID: number) {
    this.gameInfoAPI.joinSquad(this.gameInfo.gameID, squadID, this.gameInfo.playerID)
      .then(res => {
        res.subscribe(() => {
          this.updateSquad();
        })
      });
  }

  createSquad(squadName: string) {
    this.gameInfoAPI.createSquad(this.gameInfo.gameID, squadName, this.gameInfo.player_is_human)
      .then(res => {
        res.subscribe(() => {
          this.updateSquad();
        })
      })
  }

  leaveSquad() {
    if (this.gameInfo.squad_info) {
      this.gameInfoAPI.leaveSquad(this.gameInfo.gameID, this.gameInfo.squad_info!.id)
        .then(res => {
          res.subscribe()
        });
      this.gameInfo.squad_info = null;
    }
  }
}
