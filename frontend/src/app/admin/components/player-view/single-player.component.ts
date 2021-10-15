import {Component, Input, OnInit} from '@angular/core';
import {PlayerInfoFull} from "../../../models/input/player-info-full.model";

@Component({
  selector: 'app-single-player',
  templateUrl: './single-player.component.html',
  styleUrls: ['./single-player.component.css']
})
export class SinglePlayerComponent implements OnInit {

  public playerName: string = "";

  @Input()
  public playerInfo!: PlayerInfoFull;

  constructor() { }

  ngOnInit(): void {
  }

  get player(): PlayerInfoFull {
    return this.playerInfo;
  }
}
