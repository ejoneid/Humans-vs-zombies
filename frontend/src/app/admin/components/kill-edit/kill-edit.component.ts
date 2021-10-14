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
  selector: 'app-kill-edit',
  templateUrl: './kill-edit.component.html',
  styleUrls: ['./kill-edit.component.css'],
  providers: [{
    provide: DateAdapter,
    useClass: MomentDateAdapter,
    deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
  },
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ],
})
export class KillEditComponent implements OnInit {

  buttonClicked = false;

  constructor(public dialogRef: MatDialogRef<KillEditComponent>, @Inject(MAT_DIALOG_DATA) public data: {victimName: string | null, story: string | null, timeOfDeath: string | null, killerName: string | null}) {
  }

  ngOnInit(): void {
  }

  closeDialog(edit: boolean) {
    if (edit) {
      this.buttonClicked = true;
      this.data.timeOfDeath = JSON.stringify(this.data.timeOfDeath).split("\"")[1];
      if (this.data.victimName != undefined && this.data.victimName.length > 0) {
        if (this.data.killerName != undefined && this.data.killerName.length > 0) {
          if (this.data.timeOfDeath != undefined && this.data.timeOfDeath.length > 0) {
            this.dialogRef.close(this.data);
          }
        }
      }
    }
    else {
      this.dialogRef.close(false);
    }
  }

}
