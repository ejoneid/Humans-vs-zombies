import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../../../models/message.model";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  @Input()
  public chatMessages: Message[] | null = null;
  @Input()
  public playerIsHuman: boolean = true;

  constructor() { }

  ngOnInit(): void {
  }

}
