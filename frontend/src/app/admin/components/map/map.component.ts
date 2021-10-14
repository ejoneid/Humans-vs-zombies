import {Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {options} from "src/assets/map-options";
import {MapBorder} from "../../../models/map-border.model";
import {Kill} from "../../../models/kill.model";
import {Mission} from "../../../models/mission.model";
import {MapMarker} from "../../../models/map-marker.model";
import {MapInfoWindow} from "@angular/google-maps";
import {MissionEditComponent} from "../mission-edit/mission-edit.component";
import {MatDialog} from "@angular/material/dialog";
import {AdminAPI} from "../../api/admin.api";
import LatLng = google.maps.LatLng;
import {CreateMarkerComponent} from "../create-marker/create-marker.component";
import {KillEditComponent} from "../kill-edit/kill-edit.component";
import {KillOutput} from "../../../models/kill-output.model";

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
  @Input()
  public biteCodes!: {name: string, biteCode: string}[];
  @Input()
  public ids!: {name: string, id: number}[];
  @Output()
  missionUpdate: EventEmitter<any> = new EventEmitter<any>();
  @Output()
  killUpdate: EventEmitter<any> = new EventEmitter<any>();

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

  //Creates the map and applies the borders (If any)
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

  //Updates the markers-array for the map
  ngOnChanges() {
    //Resetting the markers so that they dont get loaded twice when changes are made.
    this.markers = [];
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

  //Checks if the selected marker is for a kill or a mission and opens the proper method.
  public editMarker(id: number, isMission: boolean): void {
    if (isMission) {
      const mission = this.missions.find(m => m.id === id);
      if (mission != undefined) {
        this.editMission(mission);
      }
    }
    else {
      const kill = this.kills.find(k => k.id === id);
      if (kill != undefined) {
        this.editKill(kill);
      }
    }
  }

  //Checks if the admin wants to create a kill- or mission marker.
  public createMarker(position: LatLng): void {
    const dialogRef = this.dialog.open(CreateMarkerComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        position: position
      }
    });
    dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.createMission(position);
        }
        else {
          this.createKill(position);
        }
    });
  }

  //Opens a dialog window for the specified kill
  private editKill(kill: Kill): void {
    let outputKill: KillOutput = {
      biteCode: this.biteCodes.find(b => b.name === kill.victimName)!.biteCode,
      id: kill.id,
      killerId: this.ids.find(b => b.name === kill.killerName)!.id,
      lat: kill.lat,
      lng: kill.lng,
      story: kill.story,
      timeOfDeath: ""
    }
    const dialogRef = this.dialog.open(KillEditComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        killerName: kill.killerName,
        victimName: kill.victimName,
        timeOfDeath: kill.timeOfDeath,
        story: kill.story,
        ids: this.ids,
        biteCodes: this.biteCodes
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        if (!result) {
          this.adminAPI.deleteKill(this.gameID, kill.id)
            .then(result => result.subscribe(() => {
              this.killUpdate.emit();
            }));
        }
        else {
          outputKill.killerId = parseInt(result.killerName); //For ease of coding, killerName holds the id and victimName holds the biteCode
          outputKill.biteCode = result.victimName;
          outputKill.story = result.story;
          outputKill.timeOfDeath = result.timeOfDeath;
          this.adminAPI.updateKill(this.gameID, outputKill.id, outputKill)
            .then(result => result.subscribe(() => {
              this.killUpdate.emit();
            }));
        }
      }
    });
  }

  //Creates a new kill
  private createKill(position: LatLng): void {
    console.log(this.ids)
    console.log(this.biteCodes)
    let kill: KillOutput = {
      timeOfDeath: "",
      killerId: 0,
      biteCode: "",
      story: "",
      id: 0,
      lat: position.lat(),
      lng: position.lng()
    }
    const dialogRef = this.dialog.open(KillEditComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        timeOfDeath: "",
        killerName: "",
        victimName: "",
        story: "",
        biteCodes: this.biteCodes,
        ids: this.ids
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        kill.killerId = parseInt(result.killerName); //For ease of coding, killerName holds the id and victimName holds the biteCode
        kill.biteCode = result.victimName;
        kill.story = result.story;
        kill.timeOfDeath = result.timeOfDeath;
        this.adminAPI.createKill(this.gameID, kill)
          .then(result => result.subscribe(() => {
            this.killUpdate.emit();
          }));
      }
    });
  }

  //Opens a dialog window for the specified mission.
  private editMission(mission: Mission): void {
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
        if (!result) {
          this.adminAPI.deleteMission(this.gameID, mission.id)
            .then(result => result.subscribe(() => {
              this.missionUpdate.emit();
            }));
        }
        else {
          mission.name = result.name;
          mission.human = result.isHuman;
          mission.description = result.description;
          mission.startTime = result.startTime;
          mission.endTime = result.endTime;
          this.adminAPI.updateMission(this.gameID, mission.id, mission)
            .then(result => result.subscribe(() => {
              this.missionUpdate.emit();
            }));
        }
      }
    });
  }

  //Creates a new mission
  private createMission(position: LatLng): void {
    let mission: Mission = {
      name: "",
      description: "",
      startTime: null,
      endTime: null,
      gameId: this.gameID,
      human: false,
      id: 0,
      lat: position.lat(),
      lng: position.lng()
    }
    const dialogRef = this.dialog.open(MissionEditComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        name: "",
        description: "",
        startTime: "",
        endTime: "",
        isHuman: false
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        mission.name = result.name;
        mission.human = result.isHuman;
        mission.description = result.description;
        mission.startTime = result.startTime;
        mission.endTime = result.endTime;
        this.adminAPI.createMission(this.gameID, mission)
          .then(result => result.subscribe(() => {
            this.missionUpdate.emit();
          }));
      }
    });
  }

  //Used as a callback function for the map in ngOnInit.
  initMap(): void {
    // Styles a map in night mode.
    new google.maps.Map(
      document.getElementById("map") as HTMLElement,
      this.options
    );
  }
}
