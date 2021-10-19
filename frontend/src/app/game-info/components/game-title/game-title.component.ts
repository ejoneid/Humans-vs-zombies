import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-game-title',
  templateUrl: './game-title.component.html',
  styleUrls: ['./game-title.component.css']
})
export class GameTitleComponent {

  @Input()
  public gameTitle: string = "ERROR: No game title found";
  @Output() //Emits when the "join game" button is pressed
  public joinGame = new EventEmitter<any>();

  constructor() { }

  joinGameClicked(): void {
    this.joinGame.emit();
  }

}
