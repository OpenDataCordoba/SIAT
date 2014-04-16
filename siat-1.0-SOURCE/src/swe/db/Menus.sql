delete swe_itemmenu where idaplicacion = 3;

/* nivel 0 */
INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (32,  3,'Gestion Deuda','Gestion de la Deuda',null,null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (33,  3,'Admin Deuda','Administracion de la Deuda',null,null,'admin     ',{ts '2007-08-14 16:07:18'},1);


/* nivel 1 */
INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (34,  3,'Liquidacion por CUIT/DNI', 'Liquidacion por CUIT/DNI',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (35,  3,'Deuda', 'Deuda',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (36,  3,'Convenios', 'Convenios',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);


/* nivel 1 */
INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (37,  3,'Control de Deuda', 'Control de Deuda',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (38,  3,'Control de Deuda', 'Control de Deuda',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (39,  3,'Intimaciones', 'Intimaciones',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);


/* nivel 2 */
INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (40,  3,'TGI', 'Taza General de Inmuebles',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (41,  3,'Contribucion de Mejoras', 'Contribucion de Mejoras',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (42,  3,'DReI', 'Derecho de Registro e Inspeccion',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (43,  3,'Gravamenes Especiales', 'Gravamenes Especiales',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);

INSERT INTO swe_itemmenu (id, idaplicacion,titulo,descripcion,iditemmenupadre,idaccmodapl,usuario,fechaultmdf,estado) 
VALUES (44,  3,'Taza Cementerio', 'Taza Cementerio',null, null,'admin     ',{ts '2007-08-14 16:07:18'},1);


// seteamos los padres
update swe_itemmenu set iditemmenupadre=32 where id in (34,35,36);
update swe_itemmenu set iditemmenupadre=33 where id in (37,38,39);
update swe_itemmenu set iditemmenupadre=35 where id in (40,41,42,43,44);
