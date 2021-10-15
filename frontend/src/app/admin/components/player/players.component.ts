import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PlayerInfoFull} from "../../../models/input/player-info-full.model";
import {MatDialog} from "@angular/material/dialog";
import {AdminAPI} from "../../api/admin.api";
import {PlayerEditComponent} from "../player-edit/player-edit.component";

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css']
})
export class PlayersComponent implements OnInit {

  @Input()
  public playerList!: PlayerInfoFull[];
  @Input()
  public gameID!: number;

  @Output()
  public playerUpdate: EventEmitter<any> = new EventEmitter<any>();

  constructor(private dialog: MatDialog, private readonly adminAPI: AdminAPI) { }

  ngOnInit(): void {
  }

  editPlayer(player: PlayerInfoFull): void {
    const dialogRef = this.dialog.open(PlayerEditComponent, {data: player});
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        this.adminAPI.updatePlayer(this.gameID, player.id, result)
          .then(res => res.subscribe(
            data => console.log(data)
          ));
      }
    });
  }

  get players(): PlayerInfoFull[] {
    return this.playerList;
  }
}
