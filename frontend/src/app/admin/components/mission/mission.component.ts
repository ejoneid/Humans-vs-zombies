import {Component, Input, OnInit} from '@angular/core';
import {Mission} from "../../../models/mission.model";
import {MissionEditComponent} from "../mission-edit/mission-edit.component";
import {MatDialog} from "@angular/material/dialog";
import {AdminAPI} from "../../api/admin.api";

@Component({
  selector: 'app-mission',
  templateUrl: './mission.component.html',
  styleUrls: ['./mission.component.css']
})
export class MissionComponent implements OnInit {
  @Input()
  public missions!: Mission[];
  @Input()
  public gameID!: number;

  constructor(public dialog: MatDialog, private readonly adminAPI: AdminAPI) { }

  ngOnInit(): void {
  }

  public editMission(mission: Mission | null): void {
    if (mission != null) {
      const dialogRef = this.dialog.open(MissionEditComponent, {
        height: "fit-content",
        width: "fit-content",
        data: {
          name: mission.name,
          description: mission.description,
          startTime: mission.startTime,
          endTime: mission.endTime,
          isHuman: mission.isHuman
        }});
      dialogRef.afterClosed().subscribe(result => {
        if (result != undefined) {
          mission.name = result.name;
          mission.isHuman = result.isHuman;
          mission.description = result.description;
          mission.startTime = result.startTime;
          mission.endTime = result.endTime;
          this.adminAPI.updateMission(this.gameID, mission.id, mission);
        }
        this.logResult(result);
      });
    }
    else {
      const dialogRef = this.dialog.open(MissionEditComponent, {
        height: "fit-content",
        width: "fit-content",
        data: {
          name: null,
          description: null,
          startTime: null,
          endTime: null,
          isHuman: true
        }});
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
