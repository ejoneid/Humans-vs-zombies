import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {AuthService} from "@auth0/auth0-angular";
import LatLng = google.maps.LatLng;
import {Kill} from "../../../models/input/kill.model";
import {KillOutput} from "../../../models/output/kill-output.model";

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
  public biteCode: string = "ERROR: No bite code found";
  @Input()
  locationOfKill: LatLng | null = null;
  @Output()
  requestLocation = new EventEmitter<any>();

  constructor() {}

  ngOnInit(): void {
  }

  saveKill(story: string | null, biteCode: string | null): void {
    if (biteCode != null) {
      let lat = null;
      let lng = null;
      if (this.locationOfKill != null) {
        lat = this.locationOfKill.lat();
        lng = this.locationOfKill.lng();
      }
      const kill: KillOutput = {
        biteCode: biteCode,
        killerID: this.playerID,
        id: 0,
        lat: lat,
        lng: lng,
        story: story,
        timeOfDeath: new Date().getTime().toString()
      };
      console.log(kill)
    }
  }

  getLocation(): void {
    this.requestLocation.emit();
  }

}
