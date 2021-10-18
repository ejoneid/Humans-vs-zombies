import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
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
  @Input()
  public allSquads = null;

  public join = false;
  public create = false;
  public squads = [];

  public createSquadName = "";

  constructor() { }

  ngOnInit(): void {
  }

  @Output() joinThis: EventEmitter<any> = new EventEmitter<any>();
  joinSquad(id: number) {
    this.joinThis.emit(id);
    this.join = false;
    this.create = false;
  }

  @Output() createThis: EventEmitter<any> = new EventEmitter<any>();
  createSquad() {
    this.createThis.emit(this.createSquadName);
    this.createSquadName = "";
    this.join = false;
    this.create = false;
  }

}

