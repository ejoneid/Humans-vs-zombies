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
  public message: string = "The cake is a lie and this test message should cover multiple lines in the chat.";

  public date!: string;
  public time!: string

  constructor() { }

  ngOnInit() {
    const dateArr = this.timeSent.split("T")[0].split("-");
    const date = dateArr[2] + "." + dateArr[1] + "." + dateArr[0];
    const time = this.timeSent.split("T")[1].split(".")[0];
    this.timeSent = date + "-" + time;
  }

}
