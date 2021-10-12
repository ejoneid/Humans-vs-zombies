import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {baseURL} from "../../../assets/base-url";

@Injectable({
  providedIn: 'root'
})
export class AdminAPI {

  constructor(private readonly http: HttpClient) {
  }

  public async getGameById(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID);
  }
  public async getMissionsByGame(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/mission");
  }
  public async getKillsByGame(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/kill");
  }
  public async getPlayerByName(gameID: number, playerName: string): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/player/"+playerName);
  }
  public async getKillsByPlayer(gameID: number, playerID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/player/"+playerID+"/kill");
  }
  public async getMessagesByPlayer(gameID: number, playerID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/player/"+playerID+"/message");
  }
}
