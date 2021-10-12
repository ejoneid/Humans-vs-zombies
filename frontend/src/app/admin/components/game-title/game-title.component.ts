import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-game-title-admin',
  templateUrl: './game-title.component.html',
  styleUrls: ['./game-title.component.css']
})
export class GameTitleComponent implements OnInit {

  @Input()
  public gameTitle: string = "ERROR: No game title found";

  constructor() { }

  ngOnInit(): void {
  }

}
