import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetalleCasaDialog } from './detalle-casa-dialog';

describe('DetalleCasaDialog', () => {
  let component: DetalleCasaDialog;
  let fixture: ComponentFixture<DetalleCasaDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleCasaDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(DetalleCasaDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
