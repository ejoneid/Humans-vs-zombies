import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-game-title',
  templateUrl: './game-title.component.html',
  styleUrls: ['./game-title.component.css']
})
export class GameTitleComponent implements OnInit {

  @Input()
  public gameTitle: string = "ERROR: No game title found";
  @Output()
  public joinGame = new EventEmitter<any>();

  constructor() { }

  ngOnInit(): void {
  }

  joinGameClicked(): void {
    this.joinGame.emit();
  }

}
