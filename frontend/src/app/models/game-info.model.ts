import {SquadInfo} from "./squad-info.model";
import {MapBorder} from "./map-border.model";
import {Message} from "./message.model";

export interface GameInfo {
  id: number,
  player_id: number,
  player_is_human: boolean,
  name: string,
  state: string,
  description: string,
  /*player_count: number,*/ //Not implemented
  bite_code: string,
  squad_info: SquadInfo | null,
  map_info: MapBorder | null,
  messages: Message[] | null
}
