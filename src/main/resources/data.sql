insert into telefonos (id, number, city_code, country_code) values (x'41a7a2af3a2849a99311efd3273cc5d7','12345679','2','56');
insert into telefonos (id, number, city_code, country_code) values (x'a5525293fe29412ab7f3bcac0cbe4a10','87654321','55','56');

insert into usuarios (id, created, email, isactive, jwt, last_login, modified, name, password) values (x'd0761d73cbee4a56b8bc9e5aadefea66','2023-04-28 13:32:09.391','jcgalvezv@localhost.cl',TRUE,null,'2023-04-28 13:32:09.391','2023-04-28 13:32:09.391','Juan Carlos Galvez Villegas','P@ssw0rd');

insert into usuarios_telefonos(usuario_id, telefono_id) values(x'd0761d73cbee4a56b8bc9e5aadefea66',x'41a7a2af3a2849a99311efd3273cc5d7');
insert into usuarios_telefonos(usuario_id, telefono_id) values(x'd0761d73cbee4a56b8bc9e5aadefea66',x'a5525293fe29412ab7f3bcac0cbe4a10');
