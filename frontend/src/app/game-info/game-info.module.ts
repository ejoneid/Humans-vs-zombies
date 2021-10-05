import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameInfoPage } from './pages/game-info.page';
import { GameDescriptionComponent } from './game-description/game-description.component';
import { GameTitleComponent } from './game-title/game-title.component';
import { SquadComponent } from './squad/squad.component';
import { ChatComponent } from './chat/chat.component';
import { BiteCodeComponent } from './bite-code/bite-code.component';
import {GameInfoRoutingModule} from "./game-info-routing.module";
import { MapComponent } from './map/map.component';

@NgModule({
  declarations: [
    GameInfoPage,
    GameDescriptionComponent,
    GameTitleComponent,
    SquadComponent,
    ChatComponent,
    BiteCodeComponent,
    MapComponent
  ],
  imports: [
    CommonModule,
    GameInfoRoutingModule
  ],
  exports: [
    BiteCodeComponent,
    ChatComponent,
    GameDescriptionComponent,
    GameTitleComponent,
    SquadComponent
  ]
})
export class GameInfoModule { }
