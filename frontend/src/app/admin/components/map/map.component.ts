import {Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {options} from "src/assets/map-options";
import {MapBorder} from "../../../models/map-border.model";
import {Kill} from "../../../models/kill.model";
import {Mission} from "../../../models/mission.model";
import {MapMarker} from "../../../models/map-marker.model";
import {MapInfoWindow, MapMarker as GoogleMapMarker} from "@angular/google-maps";
import {MissionEditComponent} from "../mission-edit/mission-edit.component";
import {MatDialog} from "@angular/material/dialog";
import {AdminAPI} from "../../api/admin.api";

@Component({
  selector: 'app-map-admin',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, OnChanges {

  @Input()
  mapInfo!: MapBorder | null;
  @Input()
  kills!: Kill[];
  @Input()
  missions!: Mission[];
  @Input()
  public gameID!: number;
  @Output()
  missionUpdate: EventEmitter<any> = new EventEmitter<any>();

  //Is defined from ngAfterViewInit()
  @ViewChild("gmap") gmap!: ElementRef;
  @ViewChild(MapInfoWindow) infoWindow!: MapInfoWindow;

  apiLoaded!: Observable<boolean>;

  //Initial settings for the Google Map
  options: google.maps.MapOptions = options;

  //Markers for the Google Map are put here
  markers: MapMarker[] = [];

  constructor(private readonly httpClient: HttpClient, public dialog: MatDialog, private readonly adminAPI: AdminAPI) {
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

  ngOnChanges() {
    // Populating the marker list
    for (let mission of this.missions) {
      this.markers.push({
        id: mission.id,
        isMission: true,
        description: mission.description,
        position: {lat: mission.lat, lng: mission.lng},
        label: {text: mission.name, color: "#B2BBBD"},
        options: {icon: "../assets/mission-icon.svg"},
        title: mission.name
      });
    }
    for (let kill of this.kills) {
      if (kill.lat != null && kill.lng != null) { //Position data for kills is optional.
        this.markers.push({
          id: kill.id,
          isMission: false,
          description: kill.story,
          position: {lat: kill.lat, lng: kill.lng},
          label: {text: kill.killerName, color: "#B2BBBD"},
          options: {icon: "../assets/tombstone-icon.svg"},
          title: "Kill"
        });
      }
    }
  }

  initMap(): void {
    // Styles a map in night mode.
    new google.maps.Map(
      document.getElementById("map") as HTMLElement,
      this.options
    );
  }

  public editMarker(markerID: string, id: string): void {
    console.log(markerID)
    if (this.markers.find(m => m.id === parseInt(markerID)) != undefined) {
      if (this.markers.find(m => m.id === parseInt(markerID))!.isMission) {
        this.editMission(parseInt(id));
      }
      else {
        this.editKill(parseInt(id));
      }
    }
  }

  private editKill(id: number): void {

  }

  private editMission(id: number): void {
    const mission = this.missions.find(m => m.id === id);
    if (mission != undefined) {
      const dialogRef = this.dialog.open(MissionEditComponent, {
        height: "fit-content",
        width: "fit-content",
        data: {
          name: mission.name,
          description: mission.description,
          startTime: mission.startTime,
          endTime: mission.endTime,
          isHuman: mission.human
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result != undefined) {
          mission.name = result.name;
          mission.human = result.isHuman;
          mission.description = result.description;
          mission.startTime = result.startTime;
          mission.endTime = result.endTime;
          this.adminAPI.updateMission(this.gameID, mission.id, mission)
            .then(result => result.subscribe((response) => {
              console.log(response)
            }));
        }
      });
    } else {
      const dialogRef = this.dialog.open(MissionEditComponent, {
        height: "fit-content",
        width: "fit-content",
        data: {
          name: null,
          description: null,
          startTime: null,
          endTime: null,
          isHuman: true
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        this.logResult(result);
      });
    }
  }

  logResult(result: any) {
    if (result != undefined) {
      console.log(`Mission Name: ${result.name}`);
      if (result.isHuman) console.log(`Mission type: Human`);
      else console.log(`Mission type: Zombie`);
      console.log(`Mission Description: ${result.description}`);
      if (result.startTime != null) console.log(`Start time: ${result.startTime}`);
      if (result.endTime != null) console.log(`End time: ${result.endTime}`);
    }
  }
}
