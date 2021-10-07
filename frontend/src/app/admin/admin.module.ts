import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AdminPage} from './pages/admin.page';
import {AdminRoutingModule} from "./admin-routing.module";

@NgModule({
  declarations: [
    AdminPage
  ],
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
