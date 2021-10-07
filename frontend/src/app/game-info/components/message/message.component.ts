import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {

  @Input()
  public timeSent: string = "99:99";
  @Input()
  public sender: string = "ERROR: No Name";
  @Input()
  public message: string = "The cake is a lie and this message should cover multiple lines in the chat.";

  constructor() { }

  ngOnInit(): void {
  }

}
