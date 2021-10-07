import {SquadInfo} from "./squad-info.model";
import {MapInfo} from "./map-info.model";
import {Message} from "./message.model";

export interface GameInfo {
  id: number,
  name: string,
  state: string,
  description: string,
  player_count: number,
  bite_code: string,
  squad_info: SquadInfo,
  map_info: MapInfo
  messages: [
    Message
  ]
}
