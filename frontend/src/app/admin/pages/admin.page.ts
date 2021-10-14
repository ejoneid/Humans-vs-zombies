import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Mission} from "../../models/mission.model";
import {Kill} from "../../models/kill.model";
import {AdminAPI} from "../api/admin.api";
import {GameInfoAdmin} from "../../models/game-info-admin.model";
import {PlayerInfoFull} from "../../models/player-info-full.model";

@Component({
  selector: 'app-admin.page',
  templateUrl: './admin.page.html',
  styleUrls: ['./admin.page.css']
})
export class AdminPage implements OnInit {
  //Holder for the game, initialized in ngOnInit
  private gameInfo: GameInfoAdmin = {
    id: 0,
    //All the variables are initialized in safe states or error states.
    name: "ERROR: No game name found",
    state: "ERROR: No game state found",
    description: "",
    squad_info: null,
    map_info: null,
    kills: [],
    missions: [],
    players: []
  };

  private humanBiteCodesArray: {name: string, biteCode: string}[] = [];
  private zombieIDsArray: {name: string, id: number}[] = [];

  constructor(private readonly adminAPI: AdminAPI, private route: ActivatedRoute) { }

  ngOnInit(): void {
    //Finding gameID from the optional params
    this.gameInfo.id = parseInt(this.route.snapshot.paramMap.get("id")!);
    //Getting information about the specific game
    this.updateGame();
    //Getting information about map markers.
    this.updateMissions();
    this.updateKills();
    this.updatePlayers();
  }

  updateMissions() {
    const tempMissions: Mission[] = [];
    this.adminAPI.getMissionsByGame(this.gameInfo.id)
      .then((response) => {
        response.subscribe((missions) => {
          for (let mission of missions) {
            tempMissions.push({
              name: mission.name.toString(),
              id: mission.id,
              description: mission.description.toString(),
              endTime: mission.endTime,
              startTime: mission.startTime,
              lat: parseFloat(mission.lat),
              lng: parseFloat(mission.lng),
              human: mission.human,
              gameId: this.game.id
            });
          }
          this.gameInfo.missions = tempMissions;
        });
      });
  }

  updateKills() {
    const tempKills: Kill[] = [];
    this.adminAPI.getKillsByGame(this.gameInfo.id)
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
  }

  updateGame() {
    this.adminAPI.getGameById(this.gameInfo.id)
      .then((response) => {
        response.subscribe((game) => {
          this.gameInfo.name = game.name;
          this.gameInfo.state = game.state;
          this.gameInfo.description = game.description;
          this.gameInfo.map_info = {
            nw_lat: game.nw_lat,
            se_lat: game.se_lat,
            nw_long: game.nw_long,
            se_long: game.se_long
          };
        });
      });
  }

  updatePlayers() {
    const tempPlayers: PlayerInfoFull[] = [];
    this.adminAPI.getAllPlayers(this.game.id)
      .then((response) => {
        response.subscribe((players) => {
          for (let player of players) {
            if (player.human) { //Used in the map component for creating and updating kills.
              this.humanBiteCodesArray.push({name: player.name, biteCode: player.biteCode});
            }
            else {
              this.zombieIDsArray.push({name: player.name, id: player.id});
            }
            tempPlayers.push({ //Used in the players component as a list
              id: player.id,
              name: player.name,
              isHuman: player.human,
              biteCode: player.biteCode,
              kills: player.kills,
              messages: player.messages
            });
          }
          this.gameInfo.players = tempPlayers;
        });
      });
  }

  get humanBiteCodes(): {name: string, biteCode: string}[] {
    return this.humanBiteCodesArray;
  }
  get zombieIDs(): {name: string, id: number}[] {
    return this.zombieIDsArray;
  }
  get game(): GameInfoAdmin {
    return this.gameInfo;
  }
}
