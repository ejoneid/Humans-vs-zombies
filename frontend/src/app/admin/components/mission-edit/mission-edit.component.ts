import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-mission-edit',
  templateUrl: './mission-edit.component.html',
  styleUrls: ['./mission-edit.component.css']
})
export class MissionEditComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: {name: string | null, description: string | null, startTime: string | null, endTime: string | null, isHuman: boolean}) {
  }

  ngOnInit(): void {
  }
}
