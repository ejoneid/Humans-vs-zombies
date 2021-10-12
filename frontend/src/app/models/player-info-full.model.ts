import {Kill} from "./kill.model";
import {Message} from "./message.model";

export interface PlayerInfoFull {
  id: number,
  isHuman: boolean,
  biteCode: string,
  name: string,
  kills: Kill[],
  messages: Message[]
}
