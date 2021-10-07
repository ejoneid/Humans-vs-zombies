import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AdminPage} from './pages/admin.page';
import {AdminRoutingModule} from "./admin-routing.module";
import { MapComponent } from './components/map/map.component';
import { ChatComponent } from './components/chat/chat.component';
import { MissionComponent } from './components/mission/mission.component';
import { GameComponent } from './components/game/game.component';
import { PlayerComponent } from './components/player/player.component';

@NgModule({
  declarations: [
    AdminPage,
    MapComponent,
    ChatComponent,
    MissionComponent,
    GameComponent,
    PlayerComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
