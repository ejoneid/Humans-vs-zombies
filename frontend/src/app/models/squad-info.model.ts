import {PlayerInfo} from "./player-info.model";

export interface SquadInfo {
  id: number,
  name: string,
  members: [
    PlayerInfo
  ],
}
