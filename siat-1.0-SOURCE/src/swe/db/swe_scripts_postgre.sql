--
-- Name: swe_aplicacion_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_aplicacion_id_seq
    START WITH 10000
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_aplicacion; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_aplicacion (
    id integer DEFAULT nextval('swe_aplicacion_id_seq') NOT NULL,
    codigo character varying(20) NOT NULL,
    descripcion character varying(100),
    segtimeout integer NOT NULL,
    maxnivelmenu integer NOT NULL,
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL,
    id_tipoauth integer NOT NULL
);

--
-- Name: swe_modapl_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_modapl_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_modapl; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_modapl (
    id integer DEFAULT nextval('swe_modapl_id_seq') NOT NULL,
    idaplicacion integer NOT NULL,
    nombremodulo character varying(100) NOT NULL,
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL
);


--
-- Name: swe_usrapl_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_usrapl_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_usrapl; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_usrapl (
    id integer DEFAULT nextval('swe_usrapl_id_seq') NOT NULL,
    username character varying(60),
    idaplicacion integer NOT NULL,
    uid integer,
    fechaalta timestamp without time zone,
    fechabaja timestamp without time zone,
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL,
    permiteweb smallint   
);

--
-- Name: swe_usrrolapl_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_usrrolapl_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_usrrolapl; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_usrrolapl (
    id integer DEFAULT nextval('swe_usrrolapl_id_seq') NOT NULL,
    idrolapl integer NOT NULL,
    idusrapl integer NOT NULL,
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone,
    estado smallint NOT NULL
);

--
-- Name: swe_rolapl_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_rolapl_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_rolapl; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_rolapl (
    id integer DEFAULT nextval('swe_rolapl_id_seq') NOT NULL,
    idaplicacion integer NOT NULL,
    codigo character varying(20),
    descripcion character varying(100),
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL,
    permiteweb integer
);

--
-- Name: swe_accmodapl_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_accmodapl_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_accmodapl; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_accmodapl (
    id integer DEFAULT nextval('swe_accmodapl_id_seq') NOT NULL,
    idaplicacion integer NOT NULL,
    idmodapl integer NOT NULL,
    descripcion character varying(100) NOT NULL,
    nombreaccion character varying(100) NOT NULL,
    nombremetodo character varying(100),
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL
);

--
-- Name: swe_rolaccmodapl_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_rolaccmodapl_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_rolaccmodapl; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_rolaccmodapl (
    id integer DEFAULT nextval('swe_rolaccmodapl_id_seq') NOT NULL,
    idaccmodapl integer NOT NULL,
    idrolapl integer NOT NULL,
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone,
    estado smallint NOT NULL
);

--
-- Name: swe_itemmenu_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_itemmenu_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_itemmenu; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_itemmenu (
    id integer DEFAULT nextval('swe_itemmenu_id_seq') NOT NULL,
    idaplicacion integer NOT NULL,
    titulo character varying(100) NOT NULL,
    descripcion character varying(100),
    iditemmenupadre integer,
    idaccmodapl integer,
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL,
    url character varying(200),
    nroorden integer
);

--
-- Name: swe_usrapladmswe_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_usrapladmswe_id_seq
    START WITH 10000
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_usrapladmswe; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_usrapladmswe (
    id integer DEFAULT nextval('swe_usrapladmswe_id_seq') NOT NULL,
    idusrapl integer NOT NULL,
    idaplicacion integer NOT NULL,
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL
);


--
-- Name: swe_usrauth_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_usrauth_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_usrauth; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_usrauth (
    id integer DEFAULT nextval('swe_usrauth_id_seq') NOT NULL,       
    nomUsuario character(10) NOT NULL,
	password character varying(35) NOT NULL,
	idaplicacion integer NOT NULL,
	usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL
);


--
-- Name: swe_tipoauth_id_seq; Type: SEQUENCE; Schema: public; Owner: tecso
--

CREATE SEQUENCE swe_tipoauth_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: swe_tipoauth; Type: TABLE; Schema: public; Owner: tecso; Tablespace: 
--

CREATE TABLE swe_tipoauth (
    id integer DEFAULT nextval('swe_tipoauth_id_seq') NOT NULL,
    descripcion character varying(100),
    usuario character(10) NOT NULL,
    fechaultmdf timestamp without time zone NOT NULL,
    estado smallint NOT NULL
);


-- 
-- CONSTRAINTS
--

--
-- Name: swe_aplicacion_codigo_key; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_aplicacion
    ADD CONSTRAINT swe_aplicacion_codigo_key UNIQUE (codigo);


--
-- Name: swe_aplicacion_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_aplicacion
    ADD CONSTRAINT swe_aplicacion_pkey PRIMARY KEY (id);

	

--
-- Name: fk_accmodapl; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_accmodapl
    ADD CONSTRAINT fk_accmodapl FOREIGN KEY (idaplicacion) REFERENCES swe_aplicacion(id);
	

--
-- Name: fk_itemmenu_aplication; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_itemmenu
    ADD CONSTRAINT fk_itemmenu_aplication FOREIGN KEY (idaplicacion) REFERENCES swe_aplicacion(id);


--
-- Name: fk_modapl_aplication; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_modapl
    ADD CONSTRAINT fk_modapl_aplication FOREIGN KEY (idaplicacion) REFERENCES swe_aplicacion(id);


--
-- Name: fk_rolapl_aplication; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_rolapl
    ADD CONSTRAINT fk_rolapl_aplication FOREIGN KEY (idaplicacion) REFERENCES swe_aplicacion(id);


--
-- Name: fk_usrapl_aplication; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_usrapl
    ADD CONSTRAINT fk_usrapl_aplication FOREIGN KEY (idaplicacion) REFERENCES swe_aplicacion(id);

--
-- Name: fk_usrapladmswe_aplication; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_usrapladmswe
    ADD CONSTRAINT fk_usrapladmswe_aplication FOREIGN KEY (idaplicacion) REFERENCES swe_aplicacion(id);

--
-- Name: swe_modapl_idaplicacion_key; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_modapl
    ADD CONSTRAINT swe_modapl_idaplicacion_key UNIQUE (idaplicacion, nombremodulo);


--
-- Name: swe_modapl_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_modapl
    ADD CONSTRAINT swe_modapl_pkey PRIMARY KEY (id);

--
-- Name: fk_accmodapl_modapl; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_accmodapl
    ADD CONSTRAINT fk_accmodapl_modapl FOREIGN KEY (idmodapl) REFERENCES swe_modapl(id);
	
--
-- Name: swe_usrapl_idaplicacion_key; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_usrapl
    ADD CONSTRAINT swe_usrapl_idaplicacion_key UNIQUE (idaplicacion, username);


--
-- Name: swe_usrapl_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_usrapl
    ADD CONSTRAINT swe_usrapl_pkey PRIMARY KEY (id);

--
-- Name: username; Type: INDEX; Schema: public; Owner: tecso; Tablespace: 
--

CREATE INDEX username ON swe_usrapl USING btree (username);

--
-- Name: fk_usrapladmswe_usrapl; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_usrapladmswe
    ADD CONSTRAINT fk_usrapladmswe_usrapl FOREIGN KEY (idusrapl) REFERENCES swe_usrapl(id);


--
-- Name: fk_usrrolpl_usrapl; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_usrrolapl
    ADD CONSTRAINT fk_usrrolpl_usrapl FOREIGN KEY (idusrapl) REFERENCES swe_usrapl(id);

--
-- Name: swe_usrrolapl_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_usrrolapl
    ADD CONSTRAINT swe_usrrolapl_pkey PRIMARY KEY (id);

--
-- Name: fk_usrrolapl_rolapl; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_usrrolapl
    ADD CONSTRAINT fk_usrrolapl_rolapl FOREIGN KEY (idrolapl) REFERENCES swe_rolapl(id);
	
--
-- Name: fk_rolaccmodapl_rolapl; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_rolaccmodapl
    ADD CONSTRAINT fk_rolaccmodapl_rolapl FOREIGN KEY (idrolapl) REFERENCES swe_rolapl(id);

	
--
-- Name: swe_rolapl_idaplicacion_key; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_rolapl
    ADD CONSTRAINT swe_rolapl_idaplicacion_key UNIQUE (idaplicacion, codigo);


--
-- Name: swe_rolapl_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_rolapl
    ADD CONSTRAINT swe_rolapl_pkey PRIMARY KEY (id);


--
-- Name: swe_accmodapl_idaplicacion_key; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_accmodapl
    ADD CONSTRAINT swe_accmodapl_idaplicacion_key UNIQUE (idaplicacion, nombreaccion, nombremetodo);


--
-- Name: swe_accmodapl_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_accmodapl
    ADD CONSTRAINT swe_accmodapl_pkey PRIMARY KEY (id);


--
-- Name: fk_rolaccmodapl_accmodapl; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_rolaccmodapl
    ADD CONSTRAINT fk_rolaccmodapl_accmodapl FOREIGN KEY (idaccmodapl) REFERENCES swe_accmodapl(id);


--
-- Name: fk_itemmenu; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_itemmenu
    ADD CONSTRAINT fk_itemmenu FOREIGN KEY (idaccmodapl) REFERENCES swe_accmodapl(id);

--
-- Name: swe_rolaccmodapl_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_rolaccmodapl
    ADD CONSTRAINT swe_rolaccmodapl_pkey PRIMARY KEY (id);
	
--
-- Name: swe_itemmenu_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_itemmenu
    ADD CONSTRAINT swe_itemmenu_pkey PRIMARY KEY (id);


--
-- Name: swe_usrapladmswe_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_usrapladmswe
    ADD CONSTRAINT swe_usrapladmswe_pkey PRIMARY KEY (id);
    
    
--
-- Name: swe_tipoauth_pkey; Type: CONSTRAINT; Schema: public; Owner: tecso; Tablespace: 
--

ALTER TABLE ONLY swe_tipoauth
    ADD CONSTRAINT swe_tipoauth_pkey PRIMARY KEY (id);

--
-- Name: fk_accmodapl; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--

ALTER TABLE ONLY swe_aplicacion
    ADD CONSTRAINT fk_aplicacion FOREIGN KEY (id_tipoauth) REFERENCES swe_tipoauth(id);


   
--
-- Name: fk_usrauth; Type: FK CONSTRAINT; Schema: public; Owner: tecso
--
    
ALTER TABLE ONLY swe_usrauth
    ADD CONSTRAINT fk_usrauth FOREIGN KEY (idaplicacion) REFERENCES swe_aplicacion(id);  