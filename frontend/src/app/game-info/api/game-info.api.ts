import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {baseURL} from "../../../assets/base-url";

@Injectable({
  providedIn: 'root'
})
export class GameInfoAPI {

  constructor(private readonly http: HttpClient) {
  }

  public getGameById(gameID: number): Observable<any> {
    return this.http.get<any>(baseURL+"api/game/"+gameID);
  }
  public getCurrentPlayerInfo(gameID: number, playerID: number): Observable<any> {
    return this.http.get<any>(baseURL+"api/game/"+gameID+"/player/"+playerID);
  }

}
