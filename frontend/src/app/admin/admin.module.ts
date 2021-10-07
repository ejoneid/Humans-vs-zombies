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

@NgModule({
  declarations: [
    AdminPage,
    MapComponent,
    ChatComponent,
    MissionComponent,
    GameComponent,
    PlayerComponent,
    GameTitleComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    GoogleMapsModule
  ]
})
export class AdminModule { }
