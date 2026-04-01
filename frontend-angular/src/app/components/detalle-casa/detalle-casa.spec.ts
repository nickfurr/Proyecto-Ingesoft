import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleCasa } from './detalle-casa';

describe('DetalleCasa', () => {
  let component: DetalleCasa;
  let fixture: ComponentFixture<DetalleCasa>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleCasa],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleCasa);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
