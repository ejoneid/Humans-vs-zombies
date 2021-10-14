import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PlayerInfoFull} from "../../../models/player-info-full.model";

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css']
})
export class PlayersComponent implements OnInit {

  @Input()
  public playerList!: PlayerInfoFull[];

  @Output()
  public playerUpdate: EventEmitter<any> = new EventEmitter<any>();

  constructor() { }

  ngOnInit(): void {
  }

  get players(): PlayerInfoFull[] {
    return this.playerList;
  }


}
