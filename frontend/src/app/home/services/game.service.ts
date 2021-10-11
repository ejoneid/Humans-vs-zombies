import { HttpClient } from '@angular/common/http';
import { GameInfo } from './../../models/game-info.model';
import { Injectable } from "@angular/core";


@Injectable({
    providedIn: "root"
})
export class GameService {
    private _games: GameInfo[] = [];

    constructor(private readonly http: HttpClient) {}

    public fetchGames(): void {
        const url: string = "http://localhost:8080/api/game"
        const request$ = this.http.get<GameInfo[]>(url);

        request$.subscribe((res: GameInfo[]) => {
            console.log(res);
            
        })
    }
    
    public addGame(): void {
        const url: string = "http://localhost:8080/api/game"
        const request$ = this.http.post<any>(encodeURI(url), {
            name: "The coolest game",
            gameState: "Some state"
        });

        request$.subscribe((res: any) => {
            console.log(res);
            
        })
    }

    public getGames(): GameInfo[] {
        return this._games;
    }

    

    // public fetchCatalogue(page: number): void {
    //     this._initialized = true;
    //     const url: string = this.apiService.generateUrl(page);
    //     const request$ = this.http.get<PokemonResponse>(url);
    
    //     request$.subscribe((res: PokemonResponse) => {
    //       const pokemon: Pokemon[] = res.results.map(this.addIDAndImage);
    //       this._catalogue = pokemon;
    //     });
    //   }
    
}