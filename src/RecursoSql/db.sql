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
(nombre_usuario, clave_usuario, fecha_creado,usuario_id,type_of_user) 
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
    display int(1) default 1
);

create table tipo_telefono(
    id int not null auto_increment primary key,
    nombre varchar(20),
    fecha_creado datetime,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    display int(1) default 1
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
    display int(1) default 1
);

create table email(
    id int not null auto_increment primary key,
    email varchar(20),
    fecha_creado datetime,
    persona_id int,
    usuario_id int,
    foreign key (usuario_id) references u(id),
    foreign key (persona_id) references persona(id),
    display int(1) default 1
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
    display int(1) default 1
);

/*
    EMPLEADO
*/ 
create table empleado (){
    id int not null primary key auto_increment,
    persona_id int,
    fecha_creado datetime,
    usuario_id int,
    usuario_empleado_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (usuario_empleado_id) references usuario(id),
    foreign key (persona_id) references persona(id),
    display int(1) default 1
}

/*
    CLIENTE
*/ 
create table cliente (){
    id int not null primary key auto_increment,
    persona_id int,
    fecha_creado datetime,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (persona_id) references persona(id),
    display int(1) default 1
}
create table nota(){
    id int not null primary key auto_increment,
    cliente_id int,
    usuario_id int,
    foreign key (usuario_id) references usuario(id),
    foreign key (cliente_id) references cliente(id),
    display int(1) default 1
}


/*
    INVENTARIO
     AUTOCOMPLETAR
    no repetir
    validad que no se repitan los nombres si existe el producto con cantidad
     y se factura y se reduce la cantidad
   
*/
create table producto_inventariado(){
    id int not null primary key auto_increment,
    usuario_id int,
    nombre unique varchar(250),
    cantidad_comprada int,
    precio_compra decimal(20,2),
    precio_venta decimal(20,2),
    foreign key (usuario_id) references usuario(id),
    display int(1) default 1
}

/*
    
    no repetir
*/
create table auto_completar(){
    id int not null primary key auto_increment,
    nombre varchar(250) unique,
    precio_venta decimal(20,2),
    display int(1) default 1
}