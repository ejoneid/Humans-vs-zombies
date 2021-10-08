import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {baseURL} from "../../../assets/base-url";

@Injectable({
  providedIn: 'root'
})
export class HomeAPI {

  constructor(private readonly http: HttpClient) {
  }

  public getGames(): Observable<any> {
    return this.http.get<any>(baseURL+"api/game");
  }

}
