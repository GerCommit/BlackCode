import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { Login } from './login';
import { AuthService } from '../../service/auth-service';
import { SessionActivityService } from '../../service/session-activity-service';
import { UsuarioService } from '../usuario/usuario.service';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: {
                get: () => null,
              },
            },
          },
        },
        {
          provide: AuthService,
          useValue: {
            isAuthenticated: () => false,
            getRememberedUsername: () => '',
            consumeAuthMessage: () => '',
          },
        },
        {
          provide: UsuarioService,
          useValue: {
            solicitarRecuperacion: () => of('ok'),
          },
        },
        {
          provide: SessionActivityService,
          useValue: {
            resetTimer: () => undefined,
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
