import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-game-title-admin',
  templateUrl: './game-title.component.html',
  styleUrls: ['./game-title.component.css']
})
export class GameTitleComponent {

  //A custom [(ngModel)] that keeps track of what an updated title contains.
  @Input()
  public gameTitle: string = "ERROR: No game title found";
  @Output()
  public gameTitleChange = new EventEmitter<string>();
  @Input()
  public gameState!: string; //"Registration" | "Closed" | "Complete";
  @Output()
  public gameStateChange = new EventEmitter<string>();

  constructor() { }

  //Called when the save button is clicked
  saveChanges(): void {
    this.gameTitleChange.emit(this.gameTitle);
    this.gameStateChange.emit(this.gameState);
  }

}
