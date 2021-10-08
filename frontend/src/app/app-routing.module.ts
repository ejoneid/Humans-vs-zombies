import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '',
    loadChildren: () => import("./home/home.module").then(m => m.HomeModule)
  },
  { path: 'game/:id',
    loadChildren: () => import("./game-info/game-info.module").then(m => m.GameInfoModule)
  },
  { path: 'admin',
    loadChildren: () => import("./admin/admin.module").then(m => m.AdminModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
