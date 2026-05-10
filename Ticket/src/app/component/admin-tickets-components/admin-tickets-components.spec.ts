import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTicketsComponents } from './admin-tickets-components';

describe('AdminTicketsComponents', () => {
  let component: AdminTicketsComponents;
  let fixture: ComponentFixture<AdminTicketsComponents>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTicketsComponents]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTicketsComponents);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
