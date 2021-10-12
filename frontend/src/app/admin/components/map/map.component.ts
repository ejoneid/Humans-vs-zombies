import {Component, ElementRef, Input, OnChanges, OnInit, ViewChild} from '@angular/core';
import {Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {options} from "src/assets/map-options";
import {MapBorder} from "../../../models/map-border.model";
import {Kill} from "../../../models/kill.model";
import {Mission} from "../../../models/mission.model";
import {MapMarker} from "../../../models/map-marker.model";
import {MapInfoWindow, MapMarker as GoogleMapMarker} from "@angular/google-maps";

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

  //Is defined from ngAfterViewInit()
  @ViewChild("gmap") gmap!: ElementRef;
  @ViewChild(MapInfoWindow) infoWindow!: MapInfoWindow;

  apiLoaded!: Observable<boolean>;

  //Initial settings for the Google Map
  options: google.maps.MapOptions = options;

  //Markers for the Google Map are put here
  markers: MapMarker[] = [];

  constructor(private readonly httpClient: HttpClient) {
  }

  ngOnChanges() {
    // Populating the marker list
    for (let mission of this.missions) {
      this.markers.push({
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
          description: kill.story,
          position: {lat: kill.lat, lng: kill.lng},
          label: {text: kill.killerName, color: "#B2BBBD"},
          options: {icon: "../assets/tombstone-icon.svg"},
          title: "Kill"
        });
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

  //Opens info window next to the marker
  openInfoWindow(m: GoogleMapMarker, description: string | null) {
    if (description != null) {
      //Content is undefined at the beginning
      this.infoWindow.options = {content: description};
    }
    this.infoWindow.open(m);
  }
}
