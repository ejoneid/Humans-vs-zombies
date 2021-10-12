import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
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
  @Input()
  public playerHasSquad: boolean = false;

  public submitText: String = "";

  constructor() { }

  ngOnInit(): void {
  }

  @Output() globalChat: EventEmitter<any> = new EventEmitter<any>();
  displayGlobal() {
    this.globalChat.emit();
  }

  @Output() factionChat: EventEmitter<any> = new EventEmitter<any>();
  displayFaction() {
    this.factionChat.emit();
  }

  @Output() squadChat: EventEmitter<any> = new EventEmitter<any>();
  displaySquad() {
    this.squadChat.emit();
  }

  @Output() sendChat: EventEmitter<any> = new EventEmitter<any>();
  submitChat() {
    if (this.submitText != "") {
      this.sendChat.emit(this.submitText);
      this.submitText = "";
    }
  }
}
