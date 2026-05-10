import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { AuthService } from '../../service/auth-service';
import { SessionActivityService } from '../../service/session-activity-service';
import { UsuarioService } from '../usuario/usuario.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  loginForm!: FormGroup;
  registroForm!: FormGroup;
  recuperarForm!: FormGroup;

  activeTab = 'login';
  loading = false;
  errorMessage = '';
  successMessage = '';

  showLoginPassword = false;
  showRegPassword = false;
  showRegPasswordConfirm = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService,
    private usuarioService: UsuarioService,
    private sessionActivityService: SessionActivityService,
  ) {}

  ngOnInit(): void {
    this.initForms();

    const rememberedUsername = this.authService.getRememberedUsername();
    if (rememberedUsername) {
      this.loginForm.patchValue({
        username: rememberedUsername,
        recordarme: true,
      });
    }

    const authMessage = this.authService.consumeAuthMessage();
    if (authMessage) {
      this.errorMessage = authMessage;
    }

    if (this.authService.isAuthenticated()) {
      this.router.navigate([this.authService.getRedirectRouteForCurrentUser()]);
    }
  }

  initForms(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      recordarme: [false],
    });

    this.registroForm = this.formBuilder.group(
      {
        username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
        email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
        nombres: ['', [Validators.required, Validators.maxLength(100)]],
        apellidos: ['', [Validators.required, Validators.maxLength(100)]],
        telefono: ['', [Validators.required, Validators.maxLength(20)]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        passwordConfirm: ['', [Validators.required]],
        terminos: [false, [Validators.requiredTrue]],
      },
      {
        validators: this.passwordMatchValidator,
      },
    );

    this.recuperarForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  passwordMatchValidator(group: FormGroup): { passwordMismatch: true } | null {
    const password = group.get('password')?.value;
    const passwordConfirm = group.get('passwordConfirm')?.value;
    return password === passwordConfirm ? null : { passwordMismatch: true };
  }

  showTab(tabName: string): void {
    this.activeTab = tabName;
    this.errorMessage = '';
    this.successMessage = '';
  }

  togglePassword(field: string): void {
    switch (field) {
      case 'login':
        this.showLoginPassword = !this.showLoginPassword;
        break;
      case 'reg':
        this.showRegPassword = !this.showRegPassword;
        break;
      case 'regConfirm':
        this.showRegPasswordConfirm = !this.showRegPasswordConfirm;
        break;
    }
  }

  onLogin(): void {
    if (this.loginForm.invalid) {
      this.errorMessage = 'Por favor completa todos los campos correctamente.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const { username, password, recordarme } = this.loginForm.getRawValue();

    this.authService.login(username, password, recordarme).subscribe({
      next: (usuario) => {
        this.loading = false;
        this.successMessage = `Bienvenido ${usuario.nombres}.`;
        this.sessionActivityService.resetTimer();

        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
        const redirectUrl = returnUrl || this.authService.getRedirectRoute(usuario);

        setTimeout(() => {
          this.router.navigateByUrl(redirectUrl);
        }, 700);
      },
      error: (error: Error) => {
        this.loading = false;
        this.errorMessage = error.message;
      },
    });
  }

  onRegister(): void {
    if (this.registroForm.invalid) {
      this.errorMessage = this.registroForm.hasError('passwordMismatch')
        ? 'Las contrasenas no coinciden.'
        : 'Por favor completa todos los campos correctamente.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const formValue = this.registroForm.getRawValue();

    this.authService
      .register({
        username: formValue.username,
        passwordHash: formValue.password,
        nombres: formValue.nombres,
        apellidos: formValue.apellidos,
        email: formValue.email,
        telefono: formValue.telefono,
      })
      .subscribe({
        next: (usuario) => {
          this.loading = false;
          this.successMessage = `Registro exitoso. Ya puedes iniciar sesion con ${usuario.username}.`;

          setTimeout(() => {
            this.showTab('login');
            this.loginForm.patchValue({ username: usuario.username });
            this.registroForm.reset();
          }, 1200);
        },
        error: (error: Error) => {
          this.loading = false;
          this.errorMessage = error.message;
        },
      });
  }

  onRecuperar(): void {
    if (this.recuperarForm.invalid) {
      this.errorMessage = 'Por favor ingresa un email valido.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const email = this.recuperarForm.get('email')?.value ?? '';

    this.usuarioService.solicitarRecuperacion(email).subscribe({
      next: (responseMessage) => {
        this.loading = false;
        this.successMessage = responseMessage;

        setTimeout(() => {
          this.showTab('login');
          this.recuperarForm.reset();
          this.successMessage = '';
        }, 3000);
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Ocurrio un error al intentar enviar el correo.';
      },
    });
  }

  irATienda(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate([this.authService.getRedirectRouteForCurrentUser()]);
      return;
    }

    this.router.navigate(['/login']);
  }
}
