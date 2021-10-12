import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-mission-edit',
  templateUrl: './mission-edit.component.html',
  styleUrls: ['./mission-edit.component.css']
})
export class MissionEditComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<MissionEditComponent>, @Inject(MAT_DIALOG_DATA) public data: {name: string, description: string | null}) { }

  ngOnInit(): void {
  }

}
