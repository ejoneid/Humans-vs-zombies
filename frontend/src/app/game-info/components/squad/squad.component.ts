import {Component, Input, OnInit} from '@angular/core';
import {PlayerInfo} from "../../../models/player-info.model";

@Component({
  selector: 'app-squad',
  templateUrl: './squad.component.html',
  styleUrls: ['./squad.component.css']
})
export class SquadComponent implements OnInit {

  @Input()
  public squadName: string = "ERROR: No squad name";
  @Input()
  public members: PlayerInfo[] = [];

  constructor() { }

  ngOnInit(): void {
  }
}

