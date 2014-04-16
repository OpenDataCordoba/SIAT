-- TABLAS DEL SCRIPT
--swe_Aplicacion
--swe_ModApl
--swe_AccModApl
--swe_ItemMenu
--swe_RolApl
--swe_RolAccModApl
--swe_UsrRolApl
--swe_UsrApl

-- Aplicacion
CREATE TABLE swe_Aplicacion (
  id serial PRIMARY KEY,
  codigo varchar(20) not null,
  descripcion varchar(100),
  segTimeOut int not null,
  maxNivelMenu int not null,
  usuario char(10) not null,
  fechaUltMdf datetime year to second not null,
  estado smallint not null
);

-- Modulo Aplicacion
CREATE TABLE swe_ModApl (
   id serial PRIMARY KEY,
   idAplicacion int not null,
   nombreModulo varchar(100) not null,
   usuario char(10) not null,
   fechaUltMdf datetime year to second not null,
   estado smallint not null
);

-- Accion Modulo Aplicacion
CREATE TABLE swe_AccModApl (
   id serial PRIMARY KEY,
   idAplicacion int not null,   
   idModApl int NOT NULL,
   descripcion varchar(100) not null,
   nombreAccion varchar(100) not null,
   nombreMetodo varchar(100),
   usuario char(10) not null,
   fechaUltMdf datetime year to second not null,
   estado smallint not null
);

-- Menu Aplicacion
CREATE TABLE swe_ItemMenu (
  id serial PRIMARY KEY,
  idAplicacion int not null,
  titulo varchar(100) not null,
  descripcion varchar(100),
  nroOrden int,
  url varchar(200) not null,
  idItemMenuPadre int,
  idAccModApl int,
  usuario char(10) not null,
  fechaUltMdf datetime year to second not null,
  estado smallint not null
);

-- Rol Aplicacion
CREATE TABLE swe_RolApl (
   id serial PRIMARY KEY,
   idAplicacion int  not null,
   codigo varchar(20),  
   descripcion varchar(100),
   usuario char(10) not null,
   fechaUltMdf datetime year to second not null,
   estado smallint  not null
);

-- Rol Accion Modulo Aplicacion
CREATE TABLE swe_RolAccModApl (
   id serial PRIMARY KEY,
   idAccModApl int not null,
   idRolApl int not null,
   usuario char(10) not null,
   fechaUltMdf datetime year to second,
   estado smallint  not null
);

-- Usuario Rol Aplicacion
CREATE TABLE swe_UsrRolApl (
   id serial PRIMARY KEY,
   idRolApl int not null,
   idUsrApl int not null,
   usuario char(10) not null,
   fechaUltMdf datetime year to second,
   estado smallint not null
);

-- Usuario Aplicacion
CREATE TABLE swe_UsrApl (
   id serial PRIMARY KEY,
   username char(10) not null,
   idAplicacion int not null, 
   uid int, -- id de usuario en la base general (de usuarios) de la municipalidad	
   fechaalta datetime year to second,
   fechabaja datetime year to second,
   usuario char(10) not null,
   fechaUltMdf datetime year to second not null,
   estado smallint  not null
);

-- Usuario swe aplicacion administrable
CREATE TABLE swe_UsrAplAdmSwe (
  id serial PRIMARY KEY,
  idUsrApl int not null,
  idAplicacion int not null,
  usuario char(10) not null,
  fechaUltMdf datetime year to second not null,
  estado smallint  not null  
)


-- CONSTRAINS DE LAS TABLAS
--swe_Aplicacion
--swe_ModApl
--swe_AccModApl
--swe_ItemMenu
--swe_RolApl
--swe_RolAccModApl
--swe_UsrRolApl
--swe_UsrApl

ALTER TABLE swe_ItemMenu ADD CONSTRAINT (
   FOREIGN KEY (idAccModApl)
   REFERENCES swe_AccModApl (id)
);

ALTER TABLE swe_ItemMenu ADD CONSTRAINT (
   FOREIGN KEY (idItemMenuPadre)
   REFERENCES swe_ItemMenu (id)
);

ALTER TABLE swe_ItemMenu ADD CONSTRAINT (
   FOREIGN KEY (idAplicacion)
   REFERENCES swe_Aplicacion (id)
);
---------------
ALTER TABLE swe_AccModApl ADD CONSTRAINT (
    FOREIGN KEY (idAplicacion)
    REFERENCES swe_Aplicacion(id)
);

ALTER TABLE swe_AccModApl ADD CONSTRAINT (
    FOREIGN KEY (idModApl)
    REFERENCES swe_ModApl(id)
);

-------------
ALTER TABLE swe_ModApl ADD CONSTRAINT (
    FOREIGN KEY (idAplicacion)
    REFERENCES swe_Aplicacion(id)
);

---------------
ALTER TABLE swe_RolApl ADD CONSTRAINT (
	FOREIGN KEY (idAplicacion)
    REFERENCES swe_Aplicacion(id)
);

---------------
ALTER TABLE swe_Usrapl ADD CONSTRAINT (
	FOREIGN KEY (idAplicacion)
    REFERENCES swe_Aplicacion(id)
);

---------------
ALTER TABLE swe_UsrAplAdmSwe ADD CONSTRAINT (
	FOREIGN KEY (idAplicacion)
    REFERENCES swe_Aplicacion(id)
);

ALTER TABLE swe_UsrAplAdmSwe ADD CONSTRAINT (
	FOREIGN KEY (idUsrApl)
    REFERENCES swe_UsrApl(id)
);

--------------
ALTER TABLE swe_UsrRolApl ADD CONSTRAINT (
	FOREIGN KEY (idUsrApl)
	REFERENCES swe_UsrApl(id)
);

ALTER TABLE swe_UsrRolApl ADD CONSTRAINT(
	FOREIGN KEY (idRolApl)
	REFERENCES swe_RolApl(id)
);

---------------
ALTER TABLE swe_RolAccModApl ADD CONSTRAINT(
	FOREIGN KEY (idRolApl)
	REFERENCES swe_RolApl(id)
);

ALTER TABLE swe_RolAccModApl ADD CONSTRAINT(
	FOREIGN KEY (idAccModApl)
	REFERENCES swe_AccModApl(id)
);



-------CONSTRAINTS UNIQUE
alter table swe_aplicacion add constraint unique (codigo);
alter table swe_usrapl add constraint unique (idAplicacion, username);
alter table swe_rolapl add constraint unique (idAplicacion, codigo);
alter table swe_ItemMenu add constraint unique (idItemMenuPadre, titulo);
alter table swe_modapl add constraint unique (idAplicacion, nombremodulo);
alter table swe_accmodapl add constraint unique (idAplicacion, nombreaccion, nombremetodo);


