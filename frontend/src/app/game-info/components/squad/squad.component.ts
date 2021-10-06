import {Component, Input, OnInit} from '@angular/core';
import {PlayerInfo} from "../../../models/player-info.model";

@Component({
  selector: 'app-squad',
  templateUrl: './squad.component.html',
  styleUrls: ['./squad.component.css']
})
export class SquadComponent implements OnInit {

  @Input()
  public squadName: string = "ERROR: No squad name";
  @Input()
  public members: PlayerInfo[] = [new TestPlayer1(), new TestPlayer2(), new TestPlayer1(), new TestPlayer1(), new TestPlayer2(), new TestPlayer2()];

  constructor() { }

  ngOnInit(): void {
  }

}
//-----
//Just for testing purposes
class TestPlayer1 implements PlayerInfo {
  public name = "Test User";
  public state = true;
}
class TestPlayer2 implements PlayerInfo {
  public name = "Test User Longname";
  public state = false;
}
//-----
