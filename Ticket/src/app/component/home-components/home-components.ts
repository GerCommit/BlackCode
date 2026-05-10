import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import { ProductoVista } from '../../model/producto-vista';
import { CarritoService } from '../../service/carrito-service';
import { AuthService } from '../../service/auth-service';
import { SessionActivityService } from '../../service/session-activity-service';

type Categoria = 'anillos' | 'aretes' | 'collares';

@Component({
  selector: 'app-home-components',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home-components.html',
  styleUrl: './home-components.css',
})
export class HomeComponents implements OnInit {
  usuarioActual: any = null;
  idUsuario = 0;
  menuAbierto = false;
  Lista: ProductoVista[] = [];
  categoriaSeleccionada: Categoria | null = null;
  productoSeleccionado: ProductoVista | null = null;
  modalAbierto = false;
  cantidadModal = 1;

  constructor(
    private carritoService: CarritoService,
    private router: Router,
    private authService: AuthService,
    private sessionActivityService: SessionActivityService,
  ) {}

  ngOnInit(): void {
    const usuario = this.authService.currentUserValue;
    if (usuario) {
      this.usuarioActual = usuario;
      this.idUsuario = usuario.idUsuario ?? 0;
    }

    this.cargarProductos();
  }

  cargarProductos(): void {
    this.carritoService.listarProductos().subscribe({
      next: (data) => {
        this.Lista = ((data ?? []) as any[]).map((p: any) => ({
          idProducto: p.idProducto ?? 0,
          nombre: p.nombreProducto ?? p.nombre ?? 'Sin nombre',
          precio: Number(p.precioVenta) ?? Number(p.precio) ?? 0,
          material: p.material ?? 'No especificado',
          talla: p.tallaMedida ?? 'No especificado',
          marca: p.marca ?? 'No especificado',
          descripcion: p.descripcion ?? 'Sin descripcion',
          imagenUrl: p.imagenUrl ?? '',
          stockActual: p.stockActual ?? null,
        }));
      },
      error: () => {
        this.Lista = [];
      },
    });
  }

  cargarProductosPorCategoria(categoria: string): void {
    this.carritoService.listarProductosPorCategoria(categoria).subscribe({
      next: (data) => {
        this.Lista = ((data ?? []) as any[]).map((p: any) => ({
          idProducto: p.idProducto ?? 0,
          nombre: p.nombreProducto ?? p.nombre ?? 'Sin nombre',
          precio: Number(p.precioVenta) ?? Number(p.precio) ?? 0,
          material: p.material ?? 'No especificado',
          talla: p.tallaMedida ?? 'No especificado',
          marca: p.marca ?? 'No especificado',
          descripcion: p.descripcion ?? 'Sin descripcion',
          imagenUrl: p.imagenUrl ?? '',
          stockActual: p.stockActual ?? null,
        }));
      },
      error: () => {
        this.Lista = [];
      },
    });
  }

  seleccionarCategoria(categoria: Categoria): void {
    this.categoriaSeleccionada = categoria;
    const categoriaMap: Record<Categoria, string> = {
      anillos: 'Anillos',
      aretes: 'Aretes',
      collares: 'Collares',
    };
    this.cargarProductosPorCategoria(categoriaMap[categoria]);
    this.scrollTo('catalogo');
  }

  limpiarCategoria(): void {
    this.categoriaSeleccionada = null;
    this.cargarProductos();
  }

  get tituloCatalogo(): string {
    if (!this.categoriaSeleccionada) {
      return 'PRODUCTOS DESTACADOS';
    }
    return `CATALOGO: ${this.categoriaSeleccionada.toUpperCase()}`;
  }

  abrirModalProducto(producto: ProductoVista): void {
    this.productoSeleccionado = producto;
    this.cantidadModal = this.maxCantidadPermitida > 0 ? 1 : 0;
    this.modalAbierto = true;
  }

  cerrarModal(): void {
    this.modalAbierto = false;
    this.productoSeleccionado = null;
    this.cantidadModal = 1;
  }

  get productoAgotado(): boolean {
    if (!this.productoSeleccionado) {
      return false;
    }
    return this.productoSeleccionado.stockActual == null || this.productoSeleccionado.stockActual <= 0;
  }

  get maxCantidadPermitida(): number {
    const stock = this.productoSeleccionado?.stockActual ?? 0;
    if (stock <= 0) {
      return 0;
    }
    return Math.min(3, stock);
  }

  incrementarCantidadModal(): void {
    const max = this.maxCantidadPermitida;
    if (this.cantidadModal < max) {
      this.cantidadModal += 1;
    }
  }

  decrementarCantidadModal(): void {
    if (this.cantidadModal > 0) {
      this.cantidadModal -= 1;
    }
  }

  agregarAlCarritoDesdeModal(): void {
    if (!this.productoSeleccionado || this.productoAgotado || this.cantidadModal <= 0) {
      return;
    }
    if (this.idUsuario === 0) {
      alert('Debes iniciar sesion para anadir productos al carrito');
      this.router.navigate(['/login']);
      return;
    }

    this.carritoService
      .agregarProducto(this.idUsuario, this.productoSeleccionado.idProducto, this.cantidadModal, 0)
      .subscribe({
        next: () => {
          alert('Producto agregado al carrito');
          this.cerrarModal();
        },
        error: (err) => console.error('Error al agregar al carrito', err),
      });
  }

  toggleMenu(): void {
    this.menuAbierto = !this.menuAbierto;
  }

  cerrarSesion(): void {
    this.authService.logout().subscribe(() => {
      this.sessionActivityService.clearTimer();
      this.usuarioActual = null;
      this.idUsuario = 0;
      this.menuAbierto = false;
      this.router.navigate(['/login']);
    });
  }

  agregarAlCarrito(idProducto: number, event: Event): void {
    event.stopPropagation();

    if (this.idUsuario === 0) {
      alert('Debes iniciar sesion para anadir productos al carrito');
      this.router.navigate(['/login']);
      return;
    }

    this.carritoService.agregarProducto(this.idUsuario, idProducto, 1, 0).subscribe({
      next: () => {
        alert('Producto agregado al carrito');
      },
      error: () => {
        alert('Error al agregar producto');
      },
    });
  }

  scrollTo(section: string): void {
    const element = document.getElementById(section);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }
}
