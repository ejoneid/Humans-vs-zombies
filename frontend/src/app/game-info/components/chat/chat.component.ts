import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../../../models/message.model";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  @Input()
  public chatMessages: Message[] = [new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage(), new TestMessage()];

  constructor() { }

  ngOnInit(): void {
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
//-----
