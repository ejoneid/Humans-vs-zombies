import {SquadInfo} from "./squad-info.model";
import {MapInfo} from "./map-info.model";
import {Message} from "./message.model";

export interface GameInfo {
  id: number,
  player_id: number,
  name: string,
  state: string,
  description: string,
  player_count: number,
  bite_code: string,
  squad_info: SquadInfo | null,
  map_info: MapInfo | null,
  messages: Message[] | null
}
