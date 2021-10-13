import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AdminPage} from './pages/admin.page';
import {AdminRoutingModule} from "./admin-routing.module";
import { MapComponent } from './components/map/map.component';
import { ChatComponent } from './components/chat/chat.component';
import { MissionComponent } from './components/mission/mission.component';
import { GameComponent } from './components/game/game.component';
import { PlayerComponent } from './components/player/player.component';
import {GameTitleComponent} from "./components/game-title/game-title.component";
import {GoogleMapsModule} from "@angular/google-maps";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MissionEditComponent } from './components/mission-edit/mission-edit.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";

@NgModule({
  declarations: [
    AdminPage,
    MapComponent,
    ChatComponent,
    MissionComponent,
    GameComponent,
    PlayerComponent,
    GameTitleComponent,
    MissionEditComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    GoogleMapsModule,
    FormsModule,
    MatDialogModule,
    MatInputModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatSlideToggleModule
  ],
  entryComponents: [
    MissionEditComponent
  ]
})
export class AdminModule { }
