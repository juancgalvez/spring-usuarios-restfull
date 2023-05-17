create table telefonos (id binary(16) not null, citycode varchar(2) not null, countrycode varchar(3) not null, number varchar(12) not null, primary key (id));
create table usuarios (id binary(16) not null, created timestamp, email varchar(60) not null, isactive boolean, jwt varchar(1024), last_login timestamp, modified timestamp, name varchar(60) not null, password varchar(30) not null, primary key (id));
create table usuarios_telefonos (usuario_id binary(16) not null, telefono_id binary(16) not null);
alter table usuarios add constraint usuario_email_idx unique (email);
alter table usuarios_telefonos add constraint UK_syq3hh3sidpqu94wlac74ypas unique (telefono_id);
alter table usuarios_telefonos add constraint FKblp755qfgmo5mcmtty74tnc22 foreign key (telefono_id) references telefonos;
alter table usuarios_telefonos add constraint FK7nofewgo0o3e0y8khb1s6b8a1 foreign key (usuario_id) references usuarios;
