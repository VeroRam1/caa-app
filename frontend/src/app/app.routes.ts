import { Routes } from '@angular/router';
import { HomePage } from './home-page/home-page';
import { Dashboard } from './dashboard/dashboard';
import { BoardView } from './board-view/board-view';
import { GuestPage } from './guest-page/guest-page';


export const routes: Routes = [
    { path: '', component: HomePage },
    { path: 'dashboard', component: Dashboard },
    { path: 'board/:id', component: BoardView },
    { path: 'guest', component: GuestPage }, 
    { path: '**', redirectTo: '' } // Any unknown path redirects to home
];
