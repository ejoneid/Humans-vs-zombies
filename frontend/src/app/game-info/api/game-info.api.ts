import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {baseURL} from "../../../assets/base-url";
import {options} from "../../../assets/map-options";

@Injectable({
  providedIn: 'root'
})
export class GameInfoAPI {

  constructor(private readonly http: HttpClient) {
  }

  public async getGameById(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID);
  }
  public async getCurrentPlayerInfo(gameID: number, playerID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL + "api/game/" + gameID + "/player/" + playerID);
  }
  public async getCurrentPlayerSquad(gameID: number, playerID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/player/"+playerID+"/squad");
  }
  public async getMissionsByGame(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/mission");
  }
  public async getKillsByGame(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/kill");
  }
  public async getGameChat(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/chat");
  }
  public async getFactionChat(gameID: number, isHuman: boolean) {
    let header = new HttpHeaders({"human": JSON.stringify(isHuman)});
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/chat", {headers: header})
  }
  public async getSquadChat(gameID: number, squadID: number) {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/squad/"+squadID+"/chat");
  }
  public async sendGlobalChat(gameID: number, playerID: number, message: String): Promise<Observable<any>> {
    console.log(message);
    let header = new HttpHeaders({"playerID": JSON.stringify(playerID)});
    return await this.http.post(baseURL+"api/game/"+gameID+"/chat", {"message":message, "faction":false}, {headers: header});
  }
  public async sendFactionChat(gameID: number, playerID: number, message: String): Promise<Observable<any>> {
    let header = new HttpHeaders({"playerID": JSON.stringify(playerID)});
    return await this.http.post(baseURL+"api/game/"+gameID+"/chat", {"message":message, "faction":true}, {headers: header});
  }
  public async sendSquadChat(gameID: number, squadID: number, playerID: number, message: String): Promise<Observable<any>> {
    let header = new HttpHeaders({"playerID": JSON.stringify(playerID)});
    return await this.http.post(baseURL+"api/game/"+gameID+"/squad/"+squadID+"/chat", {"message":message, "faction":false}, {headers:header});
  }
}
