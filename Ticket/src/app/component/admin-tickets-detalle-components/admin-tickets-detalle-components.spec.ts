import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTicketsDetalleComponents } from './admin-tickets-detalle-components';

describe('AdminTicketsDetalleComponents', () => {
  let component: AdminTicketsDetalleComponents;
  let fixture: ComponentFixture<AdminTicketsDetalleComponents>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTicketsDetalleComponents]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTicketsDetalleComponents);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
