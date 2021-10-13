import {Component, OnInit} from '@angular/core';
import {GameInfo} from "../../models/game-info.model";
import {ActivatedRoute} from "@angular/router";
import {Mission} from "../../models/mission.model";
import {Kill} from "../../models/kill.model";
import {AdminAPI} from "../api/admin.api";

@Component({
  selector: 'app-admin.page',
  templateUrl: './admin.page.html',
  styleUrls: ['./admin.page.css']
})
export class AdminPage implements OnInit {
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

  constructor(private readonly adminAPI: AdminAPI, private route: ActivatedRoute) { }

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
  }

  ngOnInit(): void {
    //Finding gameID from the optional params
    this.gameInfo.id = parseInt(this.route.snapshot.paramMap.get("id")!);

    //Getting information about the specific game
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
          this.messagesURL = game.messages;
        });
      });
    //Getting information about map markers.
    this.updateMissions();
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

  get game(): GameInfo {
    return this.gameInfo;
  }
}
