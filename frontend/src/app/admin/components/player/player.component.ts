import {Component, Input, OnInit} from '@angular/core';
import {PlayerInfoFull} from "../../../models/player-info-full.model";
import {AdminAPI} from "../../api/admin.api";
import {Kill} from "../../../models/kill.model";

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.css']
})
export class PlayerComponent implements OnInit {

  @Input()
  gameID!: number;

  public playerName: string = "";

  private playerInfo: PlayerInfoFull = {
    name: "",
    biteCode: "",
    id: 0,
    isHuman: false,
    kills: [],
    messages: []
  }

  constructor(private readonly adminAPI: AdminAPI) { }

  ngOnInit(): void {
  }

  public onPlayerSearch() {
    //Find PlayerID and info
    this.adminAPI.getPlayerByName(this.gameID, this.playerName)
      .then((response) => {
        response.subscribe((player) => {
          this.playerInfo = {
            id: player.id,
            name: player.name,
            biteCode: player.biteCode,
            isHuman: player.isHuman,
            messages: [],
            kills: [],
          };
        });
      });
    //Get player kills
    this.adminAPI.getKillsByPlayer(this.gameID, this.playerInfo.id)
      .then((response) => {
        response.subscribe((kills) => {
          const tempKills: Kill[] = [];
          for (let kill of kills) {
            tempKills.push({
              killerName: kill.killerName.toString(),
              lat: parseFloat(kill.lat),
              lng: parseFloat(kill.lng),
              story: kill.story.toString(),
              timeOfDeath: kill.timeOfDeath,
              victimName: kill.victimName
            });
          }
          this.playerInfo.kills = tempKills;
        });
      });
    //TODO: Get player messages
  }

  get player(): PlayerInfoFull {
    return this.playerInfo;
  }


}
