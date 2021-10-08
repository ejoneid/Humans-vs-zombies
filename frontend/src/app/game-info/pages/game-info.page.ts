import { Component, OnInit } from '@angular/core';
import {PlayerInfo} from "../../models/player-info.model";
import {Message} from "../../models/message.model";
import {GameInfoAPI} from "../api/game-info.api";
import {ActivatedRoute} from "@angular/router";
/*import {GameInfo} from "../../models/game-info.model";*/

@Component({
  selector: 'app-game-info-page',
  templateUrl: './game-info.page.html',
  styleUrls: ['./game-info.page.css']
})
export class GameInfoPage implements OnInit {

  // Should be set from a request to the backend in the constructor
  /*gameInfo!: GameInfo;*/

  //Mock data for the gameInfo
  mockDescription: string = "This game description contains a lot of information about how the game works and whatever special rules it has. The important part is that it is long enough to enable the scrolling function of the view ;)"
  mockSquad: PlayerInfo[] = [new TestPlayer1(), new TestPlayer2(), new TestPlayer1(), new TestPlayer1(), new TestPlayer2(), new TestPlayer2()];
  mockSquadName: string = "Test Squadron";
  mockChat: Message[] = [new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage()];
  mockBiteCode: string = "ThisCouldBeRandomlyGenerated30";
  mockTitle: string = "Test Title for Awesome Game";

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute) { }

  ngOnInit(): void {
    const gameID: string = this.route.snapshot.paramMap.get("id")!;
    this.gameInfoAPI.getGameById("api/game/"+gameID)
      /*.subscribe((game) => {
        const name = game.name;
        const state = game.state;
        }

      )*/
  }

}
//-----
//Just for testing purposes
class TestMessage implements Message {
  chat = "global";
  content = "This is a chat message that should cover more than one line please";
  id = 3;
  sender = "Test User";
  squad = null;
  time = "14:31";
}
class TestPlayer1 implements PlayerInfo {
  public name = "Test User";
  public state = true;
}
class TestPlayer2 implements PlayerInfo {
  public name = "Test User Longname";
  public state = false;
}
//-----
