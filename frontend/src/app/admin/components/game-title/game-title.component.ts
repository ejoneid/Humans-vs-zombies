import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AdminAPI} from "../../api/admin.api";

@Component({
  selector: 'app-game-title-admin',
  templateUrl: './game-title.component.html',
  styleUrls: ['./game-title.component.css']
})
export class GameTitleComponent implements OnInit {

  @Input()
  public gameTitle: string = "ERROR: No game title found";
  @Output()
  public gameTitleChange = new EventEmitter<string>();

  constructor() { }

  ngOnInit(): void {
  }

  saveChanges(): void {
    this.gameTitleChange.emit(this.gameTitle);
  }

}
