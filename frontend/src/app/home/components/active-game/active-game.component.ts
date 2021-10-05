import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-active-game',
  templateUrl: './active-game.component.html',
  styleUrls: ['./active-game.component.css']
})
export class ActiveGameComponent implements OnInit {

  @Input()
  public gameName: String = "";
  @Input()
  public gameStart: String = "";
  @Input()
  public gameEnd: String = "";
  @Input()
  public gameStatus: String = "";

  constructor() { }

  ngOnInit(): void {
  }

}
