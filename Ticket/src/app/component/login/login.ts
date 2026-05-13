import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { AuthService } from '../../service/auth-service';
import { SessionActivityService } from '../../service/session-activity-service';
import { UsuarioService } from '../usuario/usuario.service';

const EMAIL_DOMAINS = ['gmail.com', 'hotmail.com', 'outlook.com', 'yahoo.com', 'icloud.com'];

function usernameValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null;
    if (!/^[a-zA-Z0-9]+$/.test(value)) {
      return { usernameInvalid: 'Solo letras y números sin espacios' };
    }
    if (value.length < 6 || value.length > 20) {
      return { usernameLength: 'Mínimo 6 y máximo 20 caracteres' };
    }
    return null;
  };
}

function emailDomainValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null;
    const domain = value.split('@')[1]?.toLowerCase();
    if (!domain || !EMAIL_DOMAINS.includes(domain)) {
      return { emailDomain: 'Dominio debe ser: gmail.com, hotmail.com, outlook.com, yahoo.com o icloud.com' };
    }
    return null;
  };
}

function nombreApellidoValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null;
    if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/.test(value)) {
      return { soloLetras: 'Solo se permiten letras' };
    }
    if (value.trim() !== value) {
      return { espaciosBlanco: 'No se permiten espacios al inicio o final' };
    }
    if (/\s{2,}/.test(value)) {
      return { espaciosSeguidos: 'No se permiten 2 o más espacios seguidos' };
    }
    if (value.length < 3 || value.length > 200) {
      return { nombreLength: 'Mínimo 3 y máximo 200 caracteres' };
    }
    return null;
  };
}

function telefonoValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null;
    if (!/^[0-9]+$/.test(value)) {
      return { telefonoSoloNumeros: 'Solo se permiten números' };
    }
    if (value.length !== 8) {
      return { telefonoLength: 'Debe tener exactamente 8 dígitos' };
    }
    return null;
  };
}

function passwordValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null;
    if (value.length < 10 || value.length > 50) {
      return { passwordLength: 'Mínimo 10 y máximo 50 caracteres' };
    }
    if (!/[a-zA-Z]/.test(value)) {
      return { passwordLetter: 'Debe contener al menos una letra' };
    }
    if (!/[0-9]/.test(value)) {
      return { passwordNumber: 'Debe contener al menos un número' };
    }
    if (!/[!@#$%^&*]/.test(value)) {
      return { passwordSpecial: 'Debe contener al menos un carácter especial (!@#$%^&*)' };
    }
    return null;
  };
}

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
      username: ['', [Validators.required, Validators.minLength(6)]],
      password: ['', [Validators.required, Validators.minLength(10)]],
      recordarme: [false],
    });

    this.registroForm = this.formBuilder.group(
      {
        username: ['', [Validators.required, usernameValidator()]],
        email: ['', [Validators.required, Validators.email, Validators.maxLength(100), emailDomainValidator()]],
        nombres: ['', [Validators.required, nombreApellidoValidator()]],
        apellidos: ['', [Validators.required, nombreApellidoValidator()]],
        telefono: ['', [Validators.required, telefonoValidator()]],
        password: ['', [Validators.required, passwordValidator()]],
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
    } else {
      this.router.navigate(['/home']);
    }
  }

irATienda(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate([this.authService.getRedirectRouteForCurrentUser()]);
    } else {
      this.router.navigate(['/home']);
    }
  }

  getUsernameError(): string {
    const control = this.registroForm.get('username');
    if (control?.hasError('required')) return 'El username es obligatorio';
    if (control?.hasError('usernameInvalid')) return control.getError('usernameInvalid');
    if (control?.hasError('usernameLength')) return control.getError('usernameLength');
    return 'Username inválido';
  }

  getEmailError(): string {
    const control = this.registroForm.get('email');
    if (control?.hasError('required')) return 'El email es obligatorio';
    if (control?.hasError('email')) return 'Email inválido';
    if (control?.hasError('emailDomain')) return control.getError('emailDomain');
    if (control?.hasError('maxlength')) return 'Máximo 100 caracteres';
    return 'Email inválido';
  }

  getNombresError(): string {
    const control = this.registroForm.get('nombres');
    if (control?.hasError('required')) return 'Los nombres son obligatorios';
    if (control?.hasError('soloLetras')) return control.getError('soloLetras');
    if (control?.hasError('espaciosBlanco')) return control.getError('espaciosBlanco');
    if (control?.hasError('espaciosSeguidos')) return control.getError('espaciosSeguidos');
    if (control?.hasError('nombreLength')) return control.getError('nombreLength');
    return 'Nombres inválidos';
  }

  getApellidosError(): string {
    const control = this.registroForm.get('apellidos');
    if (control?.hasError('required')) return 'Los apellidos son obligatorios';
    if (control?.hasError('soloLetras')) return control.getError('soloLetras');
    if (control?.hasError('espaciosBlanco')) return control.getError('espaciosBlanco');
    if (control?.hasError('espaciosSeguidos')) return control.getError('espaciosSeguidos');
    if (control?.hasError('nombreLength')) return control.getError('nombreLength');
    return 'Apellidos inválidos';
  }

  getTelefonoError(): string {
    const control = this.registroForm.get('telefono');
    if (control?.hasError('required')) return 'El teléfono es obligatorio';
    if (control?.hasError('telefonoSoloNumeros')) return control.getError('telefonoSoloNumeros');
    if (control?.hasError('telefonoLength')) return control.getError('telefonoLength');
    return 'Teléfono inválido';
  }

  getPasswordError(): string {
    const control = this.registroForm.get('password');
    if (control?.hasError('required')) return 'La contraseña es obligatoria';
    if (control?.hasError('passwordLength')) return control.getError('passwordLength');
    if (control?.hasError('passwordLetter')) return control.getError('passwordLetter');
    if (control?.hasError('passwordNumber')) return control.getError('passwordNumber');
    if (control?.hasError('passwordSpecial')) return control.getError('passwordSpecial');
    return 'Contraseña inválida';
  }
  }
}
