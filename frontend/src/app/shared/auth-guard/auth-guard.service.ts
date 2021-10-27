import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {HomeAPI} from "../../home/api/home.api";

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private readonly router: Router, private readonly homeAPI: HomeAPI) {
  }

  //If injected in a module, it ensures that the user has logged in.
  canActivate(next: ActivatedRouteSnapshot): Promise<boolean>|boolean {
    let login = false;
    let loggingIn = true;
    this.homeAPI.checkUser().then(() => {
      login = true;
      loggingIn = false;
    }).then(() => {return true})
    return login;
  }
}
