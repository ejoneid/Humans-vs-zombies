import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PlayerInfoFull} from "../../../models/input/player-info-full.model";

@Component({
  selector: 'app-player-edit',
  templateUrl: './player-edit.component.html',
  styleUrls: ['./player-edit.component.css']
})
export class PlayerEditComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<PlayerEditComponent>, @Inject(MAT_DIALOG_DATA) public data: PlayerInfoFull) { }

  buttonClicked = false;

  ngOnInit(): void {
  }

  closeDialog(edit: boolean) {
    if (edit) {
      this.buttonClicked = true;
      if (this.data.name != undefined && this.data.name.length > 0) {
          this.dialogRef.close(this.data);
      }
    }
    else {
      this.dialogRef.close(false);
    }
  }

}
