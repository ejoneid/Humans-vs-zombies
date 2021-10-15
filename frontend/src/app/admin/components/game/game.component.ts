import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit {

  @Input()
  gameDescription!: string;
  @Output()
  gameDescriptionChange = new EventEmitter<string>();

  constructor() { }

  ngOnInit(): void {
  }

}
