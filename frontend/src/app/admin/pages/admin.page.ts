import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-admin.page',
  templateUrl: './admin.page.html',
  styleUrls: ['./admin.page.css']
})
export class AdminPage implements OnInit {

  mockTitle: string = "Test Game Title";

  constructor() { }

  ngOnInit(): void {
  }

}
