import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EnterWishComponent } from './components/enter-wish/enter-wish.component';
import { WishListComponent } from './components/wish-list/wish-list/wish-list.component';
import { WishesComponent } from './components/wishes/wishes/wishes.component';

const routes: Routes = [{ path: '', component: WishesComponent }];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class WishesRoutingModule {}
