import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { WishesRoutingModule } from './wishes-routing.module';
import { SharedModule } from '../shared/shared.module';

import { EnterWishComponent } from './components/enter-wish/enter-wish.component';
import { WishListComponent } from './components/wish-list/wish-list/wish-list.component';
import { WishesComponent } from './components/wishes/wishes/wishes.component';
import { AdminPanelComponent } from './components/admin-panel/admin-panel.component';
import { EmployeeCrudComponent } from './components/employee-crud/employee-crud.component';

@NgModule({
    declarations: [
        EnterWishComponent,
        WishListComponent,
        WishesComponent,
        AdminPanelComponent,
        EmployeeCrudComponent,
    ],
    imports: [CommonModule, WishesRoutingModule, SharedModule],
})
export class WishesModule {}
