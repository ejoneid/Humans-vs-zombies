import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-bite-code',
  templateUrl: './bite-code.component.html',
  styleUrls: ['./bite-code.component.css']
})
export class BiteCodeComponent implements OnInit {

  @Input()
  public biteCode: string = "ERROR: No bite code found";

  constructor() { }

  ngOnInit(): void {
  }

}
