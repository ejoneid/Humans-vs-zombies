import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from "@angular/material/core";
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";

export const MY_FORMATS = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'DD MM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'DDDD MMMM YYYY',
  },
};

@Component({
  selector: 'app-mission-edit',
  templateUrl: './mission-edit.component.html',
  styleUrls: ['./mission-edit.component.css'],
  providers: [{
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
    },
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ],
})
export class MissionEditComponent implements OnInit {

  buttonClicked = false;
  illegalDate = false;

  constructor(public dialogRef: MatDialogRef<MissionEditComponent>, @Inject(MAT_DIALOG_DATA) public data: {name: string | null, description: string | null, startTime: string | null, endTime: string | null, isHuman: boolean}) {
  }

  checkDate(): boolean {
    if (this.data.startTime != null) this.data.startTime = JSON.stringify(this.data.startTime).split("\"")[1];
    if (this.data.endTime != null) this.data.endTime = JSON.stringify(this.data.endTime).split("\"")[1];
    if (this.data.startTime != null && this.data.endTime != null) {
      if (this.data.startTime > this.data.endTime) {
        return false;
      }
    }
    return true;
  }

  closeDialog() {
    this.buttonClicked = true;
    if (this.checkDate()) {
      if (this.data.name != undefined && this.data.name.length > 0) {
        if (this.data.description != undefined && this.data.description.length > 0)
          this.dialogRef.close(this.data);
      }
    }
    else {
      this.illegalDate = true;
    }
  }

  ngOnInit(): void {
  }
}
