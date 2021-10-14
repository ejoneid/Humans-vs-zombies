import {Component, Input, OnInit} from '@angular/core';
import {PlayerInfoFull} from "../../../models/player-info-full.model";

@Component({
  selector: 'app-player-view',
  templateUrl: './player-view.component.html',
  styleUrls: ['./player-view.component.css']
})
export class PlayerViewComponent implements OnInit {

  public playerName: string = "";

  public hasPlayer: boolean = false;

  @Input()
  private playerInfo!: PlayerInfoFull;

  constructor() { }

  ngOnInit(): void {
  }

  get player(): PlayerInfoFull {
    return this.playerInfo;
  }
}
