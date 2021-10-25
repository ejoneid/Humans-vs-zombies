import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {options} from "src/assets/map-options";
import {MapBorder} from "../../../models/input/map-border.model";
import {Kill} from "../../../models/input/kill.model";
import {Mission} from "../../../models/input/mission.model";
import {MapMarker} from "../../../models/input/map-marker.model";
import {MapInfoWindow, MapMarker as GoogleMapMarker} from "@angular/google-maps";
import LatLng = google.maps.LatLng;
import {createMapMarkers} from "../../../shared/maps/map.functions";
import {SquadCheckIn} from "../../../models/input/squad-check-in.model";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, OnChanges {

  @Input()
  mapInfo!: MapBorder;
  @Input()
  kills!: Kill[];
  @Input()
  missions!: Mission[];
  @Input()
  squadCheckIns!: SquadCheckIn[];
  @Input()
  locationRequested: boolean = false;
  @Output()
  currentLocation = new EventEmitter<LatLng>();

  //Defined from ngAfterViewInit()
  @ViewChild(MapInfoWindow) infoWindow!: MapInfoWindow;

  apiLoaded!: Observable<boolean>;

  //Initial settings for the Google Map
  options: google.maps.MapOptions = options;

  //Markers for the Google Map are put here
  markers: MapMarker[] = [];

  public isMobile: boolean;

  constructor(private readonly httpClient: HttpClient) {
    this.isMobile = window.innerWidth < 768;
  }

  //Either loads new markers or requests the user's current location
  ngOnChanges() {
    if (this.locationRequested) {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((position) => {
          this.currentLocation.emit(new LatLng(position.coords.latitude, position.coords.longitude))
          this.locationRequested = false;
        });
      }
    }
    else {
      // Populating the marker list
      this.markers = createMapMarkers(this.kills, this.missions, this.squadCheckIns, this.mapInfo);
    }
  }

  /**
   * Creates the map and applies the borders (If any)
   * This is a duplicate code, but it creates a specific part for this component, so it should be here.
   */
  ngOnInit() {
    if (this.mapInfo.nw_lat != null && this.mapInfo.nw_long != null && this.mapInfo.se_lat != null && this.mapInfo.se_long != null) {
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
