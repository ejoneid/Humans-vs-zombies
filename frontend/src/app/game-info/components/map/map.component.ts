import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {options} from "src/assets/map-options";
import {MapBorder} from "../../../models/map-border.model";
import {Kill} from "../../../models/kill.model";
import {Mission} from "../../../models/mission.model";
import {MapMarker} from "../../../models/map-marker.model";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  @Input()
  mapInfo!: MapBorder | null;
  @Input()
  kills!: Kill[];
  @Input()
  missions!: Mission[];

  //Is defined from ngAfterViewInit()
  @ViewChild("gmap") gmap!: ElementRef;

  apiLoaded!: Observable<boolean>;

  //Initial settings for the Google Map
  options: google.maps.MapOptions = options;

  //Icons for the Google Map custom markers can be put here as file references
  icons: Record<string, {icon: string}> = {
    mission: {
      icon: "src/assets/mission_icon_temp.png"
    },
    gravestone: {
      icon: "src/assets/gravestone_icon_temp.png"
    }
  };

  //Markers for the Google Map are put here
  markers: MapMarker[] = [/*{ //Test marker.
      name: "markerName",
      description: null,
      position: {
        lat: 59.9115856, lng: 10.750967
      },
      options: {
        icon: "../assets/gravestone_icon_temp.png"
      }
    }*/];

  constructor(private readonly httpClient: HttpClient) {
    // Populating the marker list
    if (this.missions != undefined) {
      for (let mission of this.missions) {
        this.markers.push({
          name: mission.name,
          description: mission.description,
          position: {lat: mission.lat, lng: mission.lng},
          options: {icon: "../assets/mission_icon_temp.png"}
        });
      }
    }
    if (this.kills != undefined) {
      for (let kill of this.kills) {
        if (kill.lat != null && kill.lng != null) { //Position data for kills is optional.
          this.markers.push({
            name: "Kill",
            description: kill.story,
            position: {lat: kill.lat, lng: kill.lng},
            options: {icon: "../assets/kill_icon_temp.png"}
          });
        }
      }
    }
  }

  ngOnInit() {
    if (this.mapInfo != null) {
      this.options.restriction = {latLngBounds: {
        east: this.mapInfo.se_long,
        north: this.mapInfo.nw_lat,
        south: this.mapInfo.se_lat,
        west: this.mapInfo.nw_long
      }};
    }
    //Maps API key: AIzaSyDLrbUDvEj78cTcTCheVdJbIH5IT5xPAkQ
    this.apiLoaded = this.httpClient.jsonp('https://maps.googleapis.com/maps/api/js?key=AIzaSyDLrbUDvEj78cTcTCheVdJbIH5IT5xPAkQ', 'initMap')
      .pipe(
        map(() => true),
        catchError(() => of(false)),
      );
  }

  initMap(): void {
    // Styles a map in night mode.
    new google.maps.Map(
      document.getElementById("map") as HTMLElement,
      this.options
    );
  }
}
