-- ================================================
-- BASE DE DATOS: BLACK CODE
-- Sistema de Joyería Online con Gestión Administrativa
-- ================================================

DROP DATABASE IF EXISTS black_code;
	CREATE DATABASE black_code CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
	USE black_code;

	-- ================================================
-- TABLA: Rol
-- ================================================
CREATE TABLE Rol (
    ID_Rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Usuario
-- ================================================
CREATE TABLE Usuario (
    ID_Usuario INT AUTO_INCREMENT PRIMARY KEY,
    ID_Rol INT NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Rol) REFERENCES Rol(ID_Rol) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB;


-- ================================================
-- TABLA: Carrito
-- ================================================
CREATE TABLE Carrito (
    ID_Carrito INT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario INT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,  -- permite desactivar al confirmar pedido

    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    INDEX idx_usuario (ID_Usuario)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Detalle_Carrito
-- ================================================
CREATE TABLE Detalle_Carrito (
    ID_Detalle_Carrito INT AUTO_INCREMENT PRIMARY KEY,
    ID_Carrito INT NOT NULL,
    ID_Producto INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (ID_Carrito) REFERENCES Carrito(ID_Carrito) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_carrito (ID_Carrito),
    INDEX idx_producto (ID_Producto)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Categoria
-- ================================================
CREATE TABLE Categoria (
    ID_Categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Producto
-- ================================================
CREATE TABLE Producto (
    ID_Producto INT AUTO_INCREMENT PRIMARY KEY,
    ID_Categoria INT NOT NULL,
    codigo_producto VARCHAR(50) NOT NULL UNIQUE,
    nombre_producto VARCHAR(200) NOT NULL,
    descripcion TEXT,
    material VARCHAR(100),
    color VARCHAR(50),
    talla_medida VARCHAR(20),
    precio_venta DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    precio_compra DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 5,
    marca VARCHAR(50),
    imagen_url VARCHAR(500),  -- Para las imágenes de productos
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Categoria) REFERENCES Categoria(ID_Categoria) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_codigo (codigo_producto),
    INDEX idx_categoria (ID_Categoria),
    INDEX idx_nombre (nombre_producto),
    INDEX idx_marca (marca),
    INDEX idx_stock (stock_actual)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Informacion_Envio
-- ================================================
CREATE TABLE Informacion_Envio (
    ID_InfoEnvio INT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario INT NOT NULL,
    nombre_destinatario VARCHAR(150) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    distrito VARCHAR(100),
    codigo_postal VARCHAR(10),
    telefono_contacto VARCHAR(20) NOT NULL,
    referencia TEXT,
    es_principal BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_usuario (ID_Usuario),
    INDEX idx_principal (es_principal)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Pedido
-- ================================================
CREATE TABLE Pedido (
    ID_Pedido INT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario INT NOT NULL,
    ID_InfoEnvio INT NOT NULL,
    numero_pedido VARCHAR(20) NOT NULL UNIQUE,
    fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- Montos
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    igv DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    
    -- Estado y pago
    estado ENUM('Pendiente', 'Pagado', 'Entregado', 'Cancelado') DEFAULT 'Pendiente',
    metodo_pago ENUM('Efectivo', 'Tarjeta', 'Transferencia', 'Yape', 'Plin') DEFAULT 'Efectivo',
    observaciones TEXT,
    usuario_registro INT,
    
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (ID_InfoEnvio) REFERENCES Informacion_Envio(ID_InfoEnvio) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_numero (numero_pedido),
    INDEX idx_usuario (ID_Usuario),
    INDEX idx_estado (estado),
    INDEX idx_fecha (fecha_pedido),
    INDEX idx_envio (ID_InfoEnvio)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Detalle_Pedido
-- ================================================
CREATE TABLE Detalle_Pedido (
    ID_Detalle_Pedido INT AUTO_INCREMENT PRIMARY KEY,
    ID_Pedido INT NOT NULL,
    ID_Producto INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) DEFAULT 0.00,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (ID_Pedido) REFERENCES Pedido(ID_Pedido) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_pedido (ID_Pedido),
    INDEX idx_producto (ID_Producto)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Comprobante
-- ================================================
CREATE TABLE Comprobante (
    ID_Comprobante INT AUTO_INCREMENT PRIMARY KEY,
    ID_Pedido INT NOT NULL,
    numero_comprobante VARCHAR(20) NOT NULL UNIQUE,
    tipo_comprobante ENUM('Boleta', 'Factura', 'Ticket') DEFAULT 'Boleta',
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(10,2) NOT NULL,
    estado ENUM('Emitido', 'Anulado') DEFAULT 'Emitido',
    FOREIGN KEY (ID_Pedido) REFERENCES Pedido(ID_Pedido) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_numero (numero_comprobante),
    INDEX idx_tipo (tipo_comprobante),
    INDEX idx_fecha (fecha_emision)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Proveedor
-- ================================================
CREATE TABLE Proveedor (
    ID_Proveedor INT AUTO_INCREMENT PRIMARY KEY,
    RUC VARCHAR(11) NOT NULL UNIQUE,
    razon_social VARCHAR(200) NOT NULL,
    nombre_contacto VARCHAR(150),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(255),
    pais VARCHAR(50) DEFAULT 'Perú',
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_ruc (RUC),
    INDEX idx_razon (razon_social)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: OrdenCompra
-- ================================================
CREATE TABLE OrdenCompra (
    ID_OrdenCompra INT AUTO_INCREMENT PRIMARY KEY,
    ID_Proveedor INT NOT NULL,
    numero_orden VARCHAR(20) NOT NULL UNIQUE,
    fecha_orden DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega_esperada DATE,
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    igv DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado ENUM('Pendiente', 'Confirmada', 'Recibida', 'Cancelada') DEFAULT 'Pendiente',
    condiciones_pago VARCHAR(100),
    usuario_registro INT,
    FOREIGN KEY (ID_Proveedor) REFERENCES Proveedor(ID_Proveedor) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_numero (numero_orden),
    INDEX idx_proveedor (ID_Proveedor),
    INDEX idx_estado (estado),
    INDEX idx_fecha (fecha_orden)
) ENGINE=InnoDB;

-- ================================================
-- TABLA: Detalle_OrdenCompra
-- ================================================
CREATE TABLE Detalle_OrdenCompra (
    ID_Detalle_Orden INT AUTO_INCREMENT PRIMARY KEY,
    ID_OrdenCompra INT NOT NULL,
    ID_Producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (ID_OrdenCompra) REFERENCES OrdenCompra(ID_OrdenCompra) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_orden (ID_OrdenCompra),
    INDEX idx_producto (ID_Producto)
) ENGINE=InnoDB;


-- ================================================
-- TABLA: Ticket (Soporte y gestión de reclamos)
-- ================================================

CREATE TABLE Ticket (
    ID_Ticket INT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario INT NULL,
    descripcion TEXT NOT NULL,
    solucion TEXT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('pendiente', 'en proceso', 'resuelto') DEFAULT 'pendiente',
    FOREIGN KEY (ID_Usuario) REFERENCES Usuario(ID_Usuario) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_estado (estado),
    INDEX idx_fecha (fecha_registro),
    INDEX idx_usuario (ID_Usuario)
) ENGINE=InnoDB;

-- ================================================
-- INSERCIÓN DE DATOS INICIALES
-- ================================================


-- ROLES: CLIENTE y ADMINISTRADOR
INSERT INTO Rol (nombre_rol) VALUES
('CLIENTE'),
('ADMINISTRADOR');

-- CATEGORÍAS DE JOYERÍA (solo anillos, aretes, collares)
INSERT INTO Categoria (nombre_categoria, descripcion) VALUES
('Anillos', 'Anillos de diferentes materiales y estilos'),
('Aretes', 'Aretes y pendientes variados'),
('Collares', 'Collares y cadenas variados');

-- USUARIO ADMINISTRADOR (password: admin123)
INSERT INTO Usuario (ID_Rol, username, password_hash, nombres, apellidos, email, telefono) VALUES
(2, 'admin', 'admin1234', 'Administrador', 'Sistema', 'admin@blackcode.com', '999888777');

-- USUARIO CLIENTE DE PRUEBA (password: cliente123)
INSERT INTO Usuario (ID_Rol, username, password_hash, nombres, apellidos, email, telefono) VALUES
(1, 'cliente1', 'cliente123', 'Juan', 'Pérez', 'juan@email.com', '987654321');

-- DIRECCIONES DE ENVÍO DE EJEMPLO (para cliente1 ID=1)
INSERT INTO Informacion_Envio (ID_Usuario, nombre_destinatario, direccion, ciudad, distrito, codigo_postal, telefono_contacto, referencia, es_principal) VALUES
(1, 'Juan Pérez', 'Av. Universitaria 1234', 'Lima', 'San Miguel', '15088', '987654321', 'Frente al parque', TRUE),
(1, 'María Pérez', 'Jr. Las Flores 567', 'Lima', 'Miraflores', '15074', '912345678', 'Casa azul', FALSE);

-- PRODUCTOS DE MUESTRA
INSERT INTO Producto (ID_Categoria, codigo_producto, nombre_producto, descripcion, material, color, talla_medida, precio_venta, precio_compra, stock_actual, stock_minimo, marca) VALUES
-- ANILLOS (ID_Categoria = 1)
(1, 'ANI-001', 'Anillo Romeo 316L', 'Romeo no es simplemente un anillo; es una declaración de amor apasionado y ardiente. Con un diseño distintivo que combina la forma de corazón con llamas danzantes.', 'Acero Inoxidable 316L', 'Negro', '10', 29.00, 15.00, 25, 5, 'Black Code'),
(1, 'ANI-002', 'Anillo Dragón Plateado', 'Anillo con diseño de dragón en relieve, simboliza poder y protección', 'Plata 925', 'Plateado', '11', 89.00, 45.00, 15, 5, 'Black Code'),
(1, 'ANI-003', 'Anillo Calavera Gótico', 'Diseño gótico con calavera detallada, estilo underground', 'Acero Inoxidable', 'Negro Mate', '9', 39.00, 20.00, 30, 5, 'Black Code'),

-- ARETES (ID_Categoria = 2)
(2, 'ARE-001', 'Aretes Cristal Swarovski', 'Aretes con cristales Swarovski de alta calidad', 'Cristal/Aleación', 'Transparente', 'Único', 59.00, 30.00, 20, 5, 'Black Code'),
(2, 'ARE-002', 'Aretes Aro Dorado', 'Aretes tipo aro en acero dorado', 'Acero Dorado', 'Dorado', 'Único', 39.00, 20.00, 25, 5, 'Black Code'),
(2, 'ARE-003', 'Aretes Perla Cultivada', 'Aretes con perla cultivada natural', 'Perla/Plata', 'Blanco', 'Único', 79.00, 40.00, 15, 5, 'Black Code'),

-- COLLARES (ID_Categoria = 3)
(3, 'COL-001', 'Collar Cruz Gótica', 'Collar con colgante de cruz gótica, cadena de acero inoxidable', 'Acero Inoxidable', 'Negro', '60cm', 49.00, 25.00, 20, 5, 'Black Code'),
(3, 'COL-002', 'Collar Cadena Cubana', 'Cadena estilo cubano, eslabones gruesos', 'Acero Dorado', 'Dorado', '55cm', 79.00, 40.00, 18, 5, 'Black Code'),
(3, 'COL-003', 'Colgante Corazón Diamantes', 'Elegante colgante de corazón con circonitas', 'Plata 925', 'Plateado', '45cm', 99.00, 50.00, 12, 5, 'Black Code');

-- PROVEEDOR DE EJEMPLO
INSERT INTO Proveedor (RUC, razon_social, nombre_contacto, telefono, email, direccion) VALUES
('20123456789', 'Joyería Import SAC', 'Carlos Mendoza', '987123456', 'ventas@joyeriaimport.com', 'Av. Gamarra 1234, Lima');


-- Tickets (el cliente1 tiene ID=1, admin tiene ID=2)
INSERT INTO Ticket (ID_Usuario, descripcion, estado, solucion)
VALUES 
(1, 'Mi pedido llegó incompleto', 'pendiente', NULL),
(1, 'No puedo acceder a mi cuenta', 'pendiente', NULL),
(2, 'Me cobraron dos veces', 'resuelto', 'Hemos verificado el doble cobro y ya realizamos el reembolso correspondiente a su método de pago.');


-- ================================================
-- FIN DEL SCRIPT
-- ================================================