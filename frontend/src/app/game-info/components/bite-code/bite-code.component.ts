import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import LatLng = google.maps.LatLng;
import {KillOutput} from "../../../models/output/kill-output.model";
import {GameInfoAPI} from "../../api/game-info.api";

@Component({
  selector: 'app-bite-code',
  templateUrl: './bite-code.component.html',
  styleUrls: ['./bite-code.component.css']
})
export class BiteCodeComponent implements OnInit {

  @Input()
  public isHuman!: boolean;
  @Input()
  playerID!: number;
  @Input()
  public biteCode: string = "";
  @Input()
  locationOfKill: LatLng | null = null;
  @Input()
  public biteCodeError: boolean = false;
  @Input()
  public gameID!: number
  @Output()
  updateKills = new EventEmitter<any>();

  @Output()
  requestLocation = new EventEmitter<any>();

  public biteCodeInput: string = "";
  public storyInput: string = "";

  constructor(private readonly gameInfoAPI: GameInfoAPI) {}

  ngOnInit(): void {
  }

  saveKill(): void {
    if (this.biteCodeInput.length === 10) {
      let lat = null;
      let lng = null;
      if (this.locationOfKill != null) {
        lat = this.locationOfKill.lat();
        lng = this.locationOfKill.lng();
      }
      const kill: KillOutput = {
        biteCode: this.biteCodeInput,
        killerID: this.playerID,
        id: 0,
        lat: lat,
        lng: lng,
        story: this.storyInput,
        timeOfDeath: new Date().getTime().toString()
      };
      console.log(kill)
      this.gameInfoAPI.createKill(this.gameID, kill)
        .then(res => res.subscribe(
          () => {
            this.biteCodeError = false;
            this.updateKills.emit()
          },
          () => {
            this.biteCodeError = true;
          }
        ))
    }
    else {
      this.biteCodeError = true;
    }
  }

  getLocation(): void {
    this.requestLocation.emit();
  }

}
