import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'location',
        data: { pageTitle: 'adpaiekevinApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'adpaiekevinApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'employeur',
        data: { pageTitle: 'adpaiekevinApp.employeur.home.title' },
        loadChildren: () => import('./employeur/employeur.module').then(m => m.EmployeurModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'adpaiekevinApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'taux-d-imposition',
        data: { pageTitle: 'adpaiekevinApp.tauxDImposition.home.title' },
        loadChildren: () => import('./taux-d-imposition/taux-d-imposition.module').then(m => m.TauxDImpositionModule),
      },
      {
        path: 'mention',
        data: { pageTitle: 'adpaiekevinApp.mention.home.title' },
        loadChildren: () => import('./mention/mention.module').then(m => m.MentionModule),
      },
      {
        path: 'conge',
        data: { pageTitle: 'adpaiekevinApp.conge.home.title' },
        loadChildren: () => import('./conge/conge.module').then(m => m.CongeModule),
      },
      {
        path: 'bonus',
        data: { pageTitle: 'adpaiekevinApp.bonus.home.title' },
        loadChildren: () => import('./bonus/bonus.module').then(m => m.BonusModule),
      },
      {
        path: 'contrat',
        data: { pageTitle: 'adpaiekevinApp.contrat.home.title' },
        loadChildren: () => import('./contrat/contrat.module').then(m => m.ContratModule),
      },
      {
        path: 'fiche-de-paie',
        data: { pageTitle: 'adpaiekevinApp.ficheDePaie.home.title' },
        loadChildren: () => import('./fiche-de-paie/fiche-de-paie.module').then(m => m.FicheDePaieModule),
      },
      {
        path: 'convention-collective',
        data: { pageTitle: 'adpaiekevinApp.conventionCollective.home.title' },
        loadChildren: () => import('./convention-collective/convention-collective.module').then(m => m.ConventionCollectiveModule),
      },
      {
        path: 'cotisation',
        data: { pageTitle: 'adpaiekevinApp.cotisation.home.title' },
        loadChildren: () => import('./cotisation/cotisation.module').then(m => m.CotisationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
