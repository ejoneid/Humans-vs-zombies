import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import LatLng = google.maps.LatLng;

@Component({
  selector: 'app-create-marker',
  templateUrl: './create-marker.component.html',
  styleUrls: ['./create-marker.component.css']
})
export class CreateMarkerComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: {position: LatLng}) {
  }

  ngOnInit(): void {
  }
}
