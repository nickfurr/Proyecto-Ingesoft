import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPropietario } from './dashboard-propietario';

describe('DashboardPropietario', () => {
  let component: DashboardPropietario;
  let fixture: ComponentFixture<DashboardPropietario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardPropietario],
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardPropietario);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
