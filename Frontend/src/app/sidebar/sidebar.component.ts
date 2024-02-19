import { Component, OnInit, inject, ChangeDetectionStrategy } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { StorageService } from '../_services/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SidebarComponent implements OnInit {
  private breakpointObserver = inject(BreakpointObserver);
  private roles: string[] = [];
  isLoggedIn = false;
  isPatient = false;
  isDoctor = false;

  constructor(private storageService: StorageService, private router: Router) { }

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  ngOnInit(): void {
    this.storageService.getLoggedInStatus().subscribe(status => {
      this.isLoggedIn = status;
    });

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user.roles;
      this.isPatient = this.roles.includes('ROLE_USER');
      this.isDoctor = this.roles.includes('ROLE_DOCTOR');
    }
  }

  logout(): void {
    this.storageService.clean();
    this.router.navigate(['/login'])
    .then(() => {
      window.location.reload();
    });
  }
}
