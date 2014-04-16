-- Aplicacion "SWE"
INSERT INTO swe_aplicacion 
(id,codigo,descripcion,usuario,fechaultmdf,estado,segtimeout,maxnivelmenu) VALUES 
(1 ,'SWE' ,'SWE - Seguridad Web Extendida','admin',{ts '2007-08-24 11:18:59'},1,900,1);


-- Modulo
INSERT INTO swe_modapl 
(id,idaplicacion,nombremodulo,usuario,fechaultmdf,estado) VALUES 
(1, 1, 'seg', 'admin', {ts '2007-08-24 11:55:16'}, 1);


-- Usuario "admin"
INSERT INTO swe_usrapl 
(id,username,idaplicacion,uid,fechaalta,fechabaja,usuario,fechaultmdf,estado) VALUES 
(1 ,'admin' ,1 ,0 ,{ts '2007-08-24 11:21:40'}, null, 'admin', {ts '2007-08-24 11:21:40'}, 1);


-- Rol "Administrador"
INSERT INTO swe_rolapl 
(id, idaplicacion,codigo,descripcion,usuario,fechaultmdf,estado) VALUES 
(1 , 1, '1','Administrador','admin' ,{ts '2007-08-24 11:23:51'} ,1 );


-- Usuario Rol
INSERT INTO swe_usrrolapl 
(id,idrolapl,idusrapl,usuario,fechaultmdf,estado) VALUES 
(1, 1, 1, 'admin', {ts '2007-08-24 11:28:17'}, 1);


INSERT INTO swe_accmodapl
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(24, 1, 1, 'Login', 'sweRefresh', 'admin', {ts '2007-08-24 11:54:08'}, 1, 'Reinicializa Datos de SWE');

INSERT INTO swe_accmodapl
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(25, 1, 1, 'ABM_Aplicacion', 'buscar', 'admin', {ts '2007-08-24 11:54:08'}, 1, 'Busqueda de la aplicacion');

INSERT INTO swe_accmodapl
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(26, 1, 1, 'ABM_Aplicacion', 'ver', 'admin', {ts '2007-08-24 11:54:08'}, 1, 'Busqueda de la aplicacion');

INSERT INTO swe_accmodapl
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(27, 1, 1, 'ABM_Aplicacion', 'agregar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Inserta una aplicacion');

INSERT INTO swe_accmodapl
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(28, 1, 1, 'ABM_Aplicacion', 'modificar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Actualiza una aplicacion');

INSERT INTO swe_accmodapl
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(29, 1, 1, 'ABM_Aplicacion', 'eliminar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Borra una aplicacion');

INSERT INTO swe_accmodapl
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(30, 1, 1, 'ABM_Usuarios', 'buscar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Busqueda de usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(32, 1, 1, 'ABM_Usuarios', 'ver', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Ver usuarios');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(33, 1, 1, 'ABM_Usuarios', 'agregar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Inserta usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(34, 1, 1, 'ABM_Usuarios', 'modificar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Actualiza usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(35, 1, 1, 'ABM_Usuarios', 'eliminar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Borra usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(36, 1, 1, 'ABM_RolesUsuario', 'buscar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Bbusqueda de roles');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(38, 1, 1, 'ABM_RolesUsuario', 'ver', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Ver Lista de Roles de Usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(39, 1, 1, 'ABM_RolesUsuario', 'agregar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Agregar Rol A Usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(40, 1, 1, 'ABM_RolesUsuario', 'modificar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Cambia Rol a Usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(41, 1, 1, 'ABM_RolesUsuario', 'eliminar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Eliminar Rol a Usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(42, 1, 1, 'ABM_Modulos', 'buscar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Busqueda de Modulos');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(44, 1, 1, 'ABM_Modulos', 'ver', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Ver un modulo de una aplicacion');
           
INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(45, 1, 1, 'ABM_Modulos', 'agregar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Agrega un modulo a una aplicacion');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(46, 1, 1, 'ABM_Modulos', 'modificar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Modifica un modulo de una aplicacion');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(47, 1, 1, 'ABM_Modulos', 'eliminar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Borra un modulo de una aplicacion');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(48, 1, 1, 'ABM_Roles', 'buscar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Busqueda de roles');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(50, 1, 1, 'ABM_Roles', 'agregar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Agrega un Rol a Usuario');
  
INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(51, 1, 1, 'ABM_Roles', 'modificar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Cambia un Rol a Usuario');
 
INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(52, 1, 1, 'ABM_Roles', 'eliminar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Borra un Rol a Usuario');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(53, 1, 1, 'ABM_AccionesRol', 'buscar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Busqueda de acciones');
           
INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(55, 1, 1, 'ABM_AccionesRol', 'agregarMultiple', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Inserta Multiples acciones a un rol');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(56, 1, 1, 'ABM_AccionesRol', 'eliminar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Borra una accion a un rol');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(57, 1, 1, 'ABM_AccionesModulo', 'buscar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Busqueda de acciones desde un modulo');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(59, 1, 1, 'ABM_AccionesModulo', 'ver', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Ver una accion de un modulo');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(60, 1, 1, 'ABM_AccionesModulo', 'agregar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Agrega una accion de un modulo');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(61, 1, 1, 'ABM_AccionesModulo', 'modificar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Modifica una accion de un modulo');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(62, 1, 1, 'ABM_AccionesModulo', 'eliminar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Borra una accion de un modulo');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(63, 1, 1, 'ABM_Menu', 'buscar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Busqueda de Items de menu');
               
INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(65, 1, 1, 'ABM_Menu', 'ver', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Ver un de item de menu');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(66, 1, 1, 'ABM_Menu', 'agregar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Agrega un de item de menu');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(67, 1, 1, 'ABM_Menu', 'modificar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Modifica un de item de menu');

INSERT INTO swe_accmodapl 
(id,idaplicacion,idmodapl,nombreaccion,nombremetodo,usuario,fechaultmdf,estado,descripcion) VALUES 
(68, 1, 1, 'ABM_Menu', 'eliminar', 'admin', {ts '2007-08-24 11:54:08'}, 1,'Borra un de item de menu');

INSERT INTO swe_accmodapl (idaplicacion,idmodapl,descripcion,nombreaccion,nombremetodo,usuario,fechaultmdf,estado) VALUES 
(1 ,1 ,'Agregar una accion a un rol' ,'ABM_AccionesRol' ,'agregar','admin' ,{ts '2007-08-24 11:54:08'} ,1 )
      
--Para que funciones la aplicacion--

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(1, 25, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);    

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(12, 20, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);                                
        
INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(13, 21, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(14, 22, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);          

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(15, 23, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);
   
INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(16, 24, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);        

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(17, 26, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(18, 27, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(19, 28, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);                    

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(20, 29, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(21, 30, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(22, 31, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(23, 32, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(24, 33, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(25, 34, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(26, 35, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(27, 36, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(28, 37, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(29, 38, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(30, 39, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(31, 40, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(32, 41, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(33, 42, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(34, 43, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(35, 44, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(36, 45, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(37, 46, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(38, 47, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   
   
INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(39, 48, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(40, 49, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(41, 50, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(42, 51, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(43, 52, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(44, 53, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(45, 54, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(46, 55, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(47, 56, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(48, 57, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(49, 58, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(50, 59, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(51, 60, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(52, 61, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(53, 62, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   
    
INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(54, 63, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(55, 64, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(56, 65, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(57, 66, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(58, 67, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(59, 68, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(60, 69, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(61, 70, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl 
(id,idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES 
(62, 71, 1, 'admin', {ts '2007-08-24 11:54:08'}, 1);   

INSERT INTO swe_rolaccmodapl (idaccmodapl,idrolapl,usuario,fechaultmdf,estado) VALUES
 (531 ,1 ,'admin' ,{ts '2008-03-13 16:09:48'},1 );
         
