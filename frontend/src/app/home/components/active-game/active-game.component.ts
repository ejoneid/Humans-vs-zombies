import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "@auth0/auth0-angular";

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
  @Input()
  public gameId: number = 0;

  constructor(private readonly router: Router, public auth: AuthService) { }

  ngOnInit(): void {
  }

  toGameInfo(gameId: number): Promise<boolean> {
    return this.router.navigate(["game/"+gameId]);
  }
  toGameInfoAdmin(gameId: number): Promise<boolean> {
    return this.router.navigate(["game/"+gameId+"/admin"]);
  }

}
