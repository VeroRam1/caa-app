import { Routes } from '@angular/router';
import { HomePage } from './home-page/home-page';
import { Dashboard } from './dashboard/dashboard';
import { BoardView } from './board-view/board-view';
import { GuestPage } from './guest-page/guest-page';
import { MyBoardsComponent } from './my-boards/my-boards';
import { BoardEditor } from './board-editor/board-editor';
import { boardAccessGuard } from './guards/board-access.guard';
import { authGuard } from './guards/auth.guard';
import { ErrorPage } from './error-page/error-page';


export const routes: Routes = [
    { path: '', component: HomePage },
    { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
    { path: 'board/:id', component: BoardView, canActivate: [boardAccessGuard] },
    { path: 'guest-page', component: GuestPage }, 
    {path: 'my-boards', component: MyBoardsComponent, canActivate: [authGuard]},
    { path: 'board-editor/:id', component: BoardEditor, canActivate: [authGuard]},
    { path: '**', component: ErrorPage } // Any unknown path redirects to error page
];
