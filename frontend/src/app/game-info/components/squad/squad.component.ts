import {Component, Input, OnInit} from '@angular/core';
import {PlayerInfo} from "../../../models/input/player-info.model";
import {SquadInfo} from "../../../models/input/squad-info.model";

@Component({
  selector: 'app-squad',
  templateUrl: './squad.component.html',
  styleUrls: ['./squad.component.css']
})
export class SquadComponent implements OnInit {

  @Input()
  public squad: SquadInfo | null = null;

  constructor() { }

  ngOnInit(): void {
  }
}

