import {Component, Input, OnInit} from '@angular/core';
import {Mission} from "../../../models/mission.model";
import {MissionEditComponent} from "../mission-edit/mission-edit.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-mission',
  templateUrl: './mission.component.html',
  styleUrls: ['./mission.component.css']
})
export class MissionComponent implements OnInit {
  @Input()
  public missions: Mission[] = [];

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  public editMission(mission: Mission): void {
    const dialogRef = this.dialog.open(MissionEditComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        name: mission.name,
        description: mission.description
    }});

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

}
