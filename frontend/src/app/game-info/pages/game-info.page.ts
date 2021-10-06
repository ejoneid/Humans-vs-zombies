import { Component, OnInit } from '@angular/core';
/*import {GameInfo} from "../../models/game-info.model";*/

@Component({
  selector: 'app-game-info-page',
  templateUrl: './game-info.page.html',
  styleUrls: ['./game-info.page.css']
})
export class GameInfoPage implements OnInit {

  // Should be set from a request to the backend in the constructor
  /*gameInfo: GameInfo;*/

  //Mock data for the gameInfo
  mockDescription: string = "This game description contains a lot of information about how the game works and whatever special rules it has. The important part is that it is long enough to enable the scrolling function of the view ;)"

  constructor() { }

  ngOnInit(): void {
  }

}
