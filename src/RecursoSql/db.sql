/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Eudy
 * Created: 25-feb-2017
 */
/*
    db
*/
create database vyr_db;
use vyr_db;

/*
   USUARIOS
*/
create table usuario(
    id int not null primary key auto_increment,
    nombre_usuario varchar(100),
    clave_usuario varchar(150),
    fecha_creado datetime,
    tipo_usuario enum('admin','cajero','tecnico','supervisor','versatil'),
    usuario_id int 
);
insert into usuario 
(nombre_usuario, clave_usuario, fecha_creado,usuario_id,tipo_usuario) 
values
('vyr','vyr123456@',now(),'1','admin');

/*
    PERSONA
*/
create table persona(
    id int not null auto_increment primary key,
    nombre varchar(100),
    apellido varchar(100),
    cedula varchar(30) unique,
    fecha_nacimiento date,
    sexo enum('masculino','femenino'),
    fecha_creado datetime,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    display int(1) default '1'
);

create table tipo_telefono(
    id int not null auto_increment primary key,
    nombre varchar(20),
    fecha_creado datetime,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    display int(1) default '1'
);
insert into tipo_telefono 
(nombre, fecha_creado,usuario_id) 
values
('casa',now(),'1'),('celular',now(),'1'),('oficina',now(),'1'),('whatsapp',now(),'1'),('otro',now(),'1');


create table telephone(
    id int not null auto_increment primary key,
    telephone varchar(20),
    fecha_creado datetime,
    persona_id int,
    usuario_id int,
    tipo_telefono_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (persona_id) references persona(id),
    foreign key (tipo_telefono_id) references tipo_telefono(id),
    display int(1) default '1'
);

create table email(
    id int not null auto_increment primary key,
    email varchar(20),
    fecha_creado datetime,
    persona_id int,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (persona_id) references persona(id),
    display int(1) default '1'
);

create table direccion(
    id int not null auto_increment primary key,
    sector varchar(50),
    provincia varchar(50),
    localidad text,
    fecha_creado datetime,
    persona_id int,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (persona_id) references persona(id),
    display int(1) default '1'
);

/*
    EMPLEADO
*/ 
create table empleado (
    id int not null primary key auto_increment,
    persona_id int,
    fecha_creado datetime,
    usuario_id int,
    usuario_empleado_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (usuario_empleado_id) references usuario(id),
    foreign key (persona_id) references persona(id),
    display int(1) default '1'
);

/*
    CLIENTE
*/ 
create table cliente (
    id int not null primary key auto_increment,
    persona_id int,
    fecha_creado datetime,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (persona_id) references persona(id),
    display int(1) default '1'
);
create table nota_cliente(
    id int not null primary key auto_increment,
    cliente_id int,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (cliente_id) references cliente(id),
    display int(1) default '1'
);


/*
    INVENTARIO
     AUTOCOMPLETAR
    no repetir
    validad que no se repitan los nombres si existe el producto con cantidad
     y se factura y se reduce la cantidad
   
*/
create table producto_inventariado(
    id int not null primary key auto_increment,
    usuario_id int,
    nombre varchar(250) unique,
    cantidad_comprada int,
    precio_compra decimal(20,2),
    precio_venta decimal(20,2),
    foreign key (usuario_id) references usuario(id),
    display int(1) default '1'
);



/*
    FACTURA
*/

create table factura(
    id int not null primary key auto_increment,
    usuario_id int,
    cuadrada int(1) default '0',
    sub_total decimal(20,2),
    itbis decimal(20,2),
    tiene_itbis int(1) default '1',
    total decimal(20,2),
    numero_comprovante_fiscal varchar(50),
    fecha_creada datetime,
    cliente_id int,
    estado enum('anulada','saldada','pendiente'),
    nota text,
    porciente_itbis int,
    tiene_comprovante_fiscal int(1) default '0',
    balance_deuda decimal(20,2),
    monto_pagado decimal(20,2),
    cambio_efectivo  decimal(20,2),
    usuario_id_anulado int,
    comentario_anulado text,
    foreign key (usuario_id) references usuario(id),
    foreign key (usuario_id_anulado) references usuario(id),
    foreign key (cliente_id) references cliente(id),
    display int(1) default 1
);

create table factura_detalle(
    id int not null primary key auto_increment,
    usuario_id int,
    nombre varchar(200),
    precio decimal(20,2),
    cantidad int,
    total decimal(20,2),
    fecha_creada datetime,
    producto_inventariado_id int,
    cliente_id int,
    estado enum('anulada','pendiente') default 'pendiente',    
    usuario_id_anulado int,
    comentario_anulado text,
    foreign key (usuario_id) references usuario(id),
    foreign key (producto_inventariado_id) references producto_inventariado(id),
    foreign key (usuario_id_anulado) references usuario(id),
    display int(1) default 1
);

/*
    COTIZACION
*/

create table cotizacion(
    id int not null primary key auto_increment,
    usuario_id int,
    sub_total decimal(20,2),
    itbis decimal(20,2),
    tiene_itbis int(1) default 1,
    total decimal(20,2),
    fecha_creada datetime,
    cliente_id int,
    estado enum('anulada','facturada','pendiente'),
    nota text,
    porciente_itbis int,
    usuario_id_anulado int,
    comentario_anulado text,
    foreign key (usuario_id) references usuario(id),
    foreign key (usuario_id_anulado) references usuario(id),
    foreign key (cliente_id) references cliente(id),
    display int(1) default 1
);

create table cotizacion_detalle(
    id int not null primary key auto_increment,
    usuario_id int,
    nombre varchar(200),
    precio decimal(20,2),
    cantidad int,
    total decimal(20,2),
    fecha_creada datetime,
    producto_inventariado_id int,
    cliente_id int,
    estado enum('anulada','pendiente') default 'pendiente',    
    usuario_id_anulado int,
    comentario_anulado text,
    foreign key (usuario_id) references usuario(id),
    foreign key (producto_inventariado_id) references producto_inventariado(id),
    foreign key (usuario_id_anulado) references usuario(id),
    display int(1) default 1
);

/*
    CUADRE DE CAJA
*/
create table cuadre(
    id int not null primary key auto_increment,
    usuario_id int,
    fecha_creado datetime,
    fecha_desde date,
    fecha_hasta date,
    monto_facturado decimal(20,2),
    monto_en_caja decimal(20,2),
    foreign key (usuario_id) references usuario(id),
    display int(1) default 1
);
create table cuadre_detalle(
    id int not null primary key auto_increment,
    usuario_id int,
    fecha_creado datetime,
    factura_id int,
    foreign key (factura_id) references factura(id),
    foreign key (usuario_id) references usuario(id),
    display int(1) default 1
);

/*
    PAGOS
*/
create table pagos(
    id int not null primary key auto_increment,
    usuario_id int,
    fecha_creado datetime,
    monto_pagar decimal(20,2),
    balance_anterior decimal(20,2),
    balance_despues_del_pago decimal(20,2),
    cambio decimal(20,2),
    tipo_pago enum('efectivo','cheque','tarjeta','transferencia'),
    factura_id int,
    foreign key (factura_id) references factura(id),
    foreign key (usuario_id) references usuario(id),
    display int(1) default 1
);

/*
    NOMINA DE PAGOS
*/
create table pago_nomina_empleado(
    id int not null primary key auto_increment,
    usuario_id int,
    fecha_creado datetime,
    monto_pagar_al_empleado decimal(20,2),
    foreign key (usuario_id) references usuario(id),
    display int(1) default 1
);

/*
    REPARACION
*/
create table inventario_reparaciones(
    id int not null primary key auto_increment,
    usuario_id int,
    nombre varchar(250) unique,
    cantidad_compada int,
    precio_compra decimal(20,2),
    precio_venta decimal(20,2),
    foreign key (usuario_id) references usuario(id),
    display int(1) default 1
);

create table reparacion(
    id int not null primary key auto_increment,
    usuario_id int,
    sub_total decimal(20,2),
    fecha_creada datetime,
    cliente_id int,
    estado enum('anulada','procesando','pendiente','completo'),
    nota text,
    usuario_id_anulado int,
    comentario_anulado text,
    foreign key (usuario_id) references usuario(id),
    foreign key (usuario_id_anulado) references usuario(id),
    foreign key (cliente_id) references cliente(id),
    display int(1) default 1
);

create table reparacion_detalle(
    id int not null primary key auto_increment,
    usuario_id int,
    nombre varchar(200),
    precio decimal(20,2),
    precio_completado decimal(20,2),
    cantidad int,
    total decimal(20,2),
    fecha_creada datetime,
    inventario_reparaciones_id int,
    cliente_id int,
    estado enum('anulada','pendiente','completado','procesando','incompleto') default 'pendiente',
    nota_incompleto_por text,
    usuario_id_incompleto_por int,
    nota text,
    usuario_id_completado int,
    usuario_id_anulado int,
    comentario_anulado text,
    foreign key (usuario_id) references usuario(id),
    foreign key (usuario_id_completado) references usuario(id),   
     foreign key (usuario_id_incompleto_por) references usuario(id),
    foreign key (inventario_reparaciones_id) references inventario_reparaciones(id),
    display int(1) default 1
);