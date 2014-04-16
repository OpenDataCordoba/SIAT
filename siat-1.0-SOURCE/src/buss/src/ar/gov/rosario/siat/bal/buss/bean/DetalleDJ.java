//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.bean.ActLoc;
import ar.gov.rosario.siat.afi.buss.bean.DatosDomicilio;
import ar.gov.rosario.siat.afi.buss.bean.DatosPagoCta;
import ar.gov.rosario.siat.afi.buss.bean.DecActLoc;
import ar.gov.rosario.siat.afi.buss.bean.ExeActLoc;
import ar.gov.rosario.siat.afi.buss.bean.ForDecJur;
import ar.gov.rosario.siat.afi.buss.bean.HabLoc;
import ar.gov.rosario.siat.afi.buss.bean.Local;
import ar.gov.rosario.siat.afi.buss.bean.OtrosPagos;
import ar.gov.rosario.siat.afi.buss.bean.RetYPer;
import ar.gov.rosario.siat.afi.buss.bean.Socio;
import ar.gov.rosario.siat.afi.buss.bean.TotDerYAccDJ;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.AgeRet;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.PropietarioAfip;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a detalles de Declaraciones Juradas de transacciones Osiris 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_detalleDJ")
public class DetalleDJ extends BaseBO {
	
	private static final long serialVersionUID = 1L;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTranAfip")
	private TranAfip tranAfip;

	@Column(name="fechaProceso")
	private Date fechaProceso;

	@Column(name="registro")
	private Integer registro;

	@Column(name="fila")
	private Integer fila;
		
	@Column(name="contenido")
	private String contenido;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstDetDJ")
	private EstDetDJ estDetDJ;
	
	@Column(name="c01n")
	private Double c01n;	
	@Column(name="c02n")
	private Double c02n;	
	@Column(name="c03n")
	private Double c03n;	
	@Column(name="c04n")
	private Double c04n;	
	@Column(name="c05n")
	private Double c05n;	
	@Column(name="c06n")
	private Double c06n;	
	@Column(name="c07n")
	private Double c07n;	
	@Column(name="c08n")
	private Double c08n;	
	@Column(name="c09n")
	private Double c09n;	
	@Column(name="c10n")
	private Double c10n;	
	@Column(name="c11n")
	private Double c11n;	
	@Column(name="c12n")
	private Double c12n;	
	@Column(name="c13n")
	private Double c13n;	
	@Column(name="c14n")
	private Double c14n;	
	@Column(name="c15n")
	private Double c15n;	
	@Column(name="c16n")
	private Double c16n;	
	@Column(name="c17n")
	private Double c17n;	
	@Column(name="c18n")
	private Double c18n;	
	@Column(name="c19n")
	private Double c19n;	
	@Column(name="c20n")
	private Double c20n;	
	@Column(name="c21n")
	private Double c21n;	
	@Column(name="c22n")
	private Double c22n;	
	@Column(name="c23n")
	private Double c23n;	
	@Column(name="c24n")
	private Double c24n;	
	@Column(name="c25n")
	private Double c25n;	
	@Column(name="c26n")
	private Double c26n;	
	@Column(name="c27n")
	private Double c27n;	
	@Column(name="c28n")
	private Double c28n;	
	@Column(name="c29n")
	private Double c29n;	
	@Column(name="c30n")
	private Double c30n;	
		
	
	// Constructores
	public DetalleDJ(){
		super();
	}
	
	
	//Metodos de clase
	public static DetalleDJ getById(Long id) {
		return (DetalleDJ) BalDAOFactory.getDetalleDJDAO().getById(id);  
	}
	
	public static DetalleDJ getByIdNull(Long id) {
		return (DetalleDJ) BalDAOFactory.getDetalleDJDAO().getByIdNull(id);
	}
	
	public static List<DetalleDJ> getList() {
		return (List<DetalleDJ>) BalDAOFactory.getDetalleDJDAO().getList();
	}
	
	public static List<DetalleDJ> getListActivos() {			
		return (List<DetalleDJ>) BalDAOFactory.getDetalleDJDAO().getListActiva();
	}

	
	//Getters y Setters
	public TranAfip getTranAfip() {
		return tranAfip;
	}
	public void setTranAfip(TranAfip tranAfip) {
		this.tranAfip = tranAfip;
	}
	public Integer getRegistro() {
		return registro;
	}
	public void setRegistro(Integer registro) {
		this.registro = registro;
	}
	public Double getC01n() {
		return c01n;
	}
	public void setC01n(Double c01n) {
		this.c01n = c01n;
	}
	public Double getC02n() {
		return c02n;
	}
	public void setC02n(Double c02n) {
		this.c02n = c02n;
	}
	public Double getC03n() {
		return c03n;
	}
	public void setC03n(Double c03n) {
		this.c03n = c03n;
	}
	public Double getC04n() {
		return c04n;
	}
	public void setC04n(Double c04n) {
		this.c04n = c04n;
	}
	public Double getC05n() {
		return c05n;
	}
	public void setC05n(Double c05n) {
		this.c05n = c05n;
	}
	public Double getC06n() {
		return c06n;
	}
	public void setC06n(Double c06n) {
		this.c06n = c06n;
	}
	public Double getC07n() {
		return c07n;
	}
	public void setC07n(Double c07n) {
		this.c07n = c07n;
	}
	public Double getC08n() {
		return c08n;
	}
	public void setC08n(Double c08n) {
		this.c08n = c08n;
	}
	public Double getC09n() {
		return c09n;
	}
	public void setC09n(Double c09n) {
		this.c09n = c09n;
	}
	public Double getC10n() {
		return c10n;
	}
	public void setC10n(Double c10n) {
		this.c10n = c10n;
	}
	public Double getC11n() {
		return c11n;
	}
	public void setC11n(Double c11n) {
		this.c11n = c11n;
	}
	public Double getC12n() {
		return c12n;
	}
	public void setC12n(Double c12n) {
		this.c12n = c12n;
	}
	public Double getC13n() {
		return c13n;
	}
	public void setC13n(Double c13n) {
		this.c13n = c13n;
	}
	public Double getC14n() {
		return c14n;
	}
	public void setC14n(Double c14n) {
		this.c14n = c14n;
	}
	public Double getC15n() {
		return c15n;
	}
	public void setC15n(Double c15n) {
		this.c15n = c15n;
	}
	public Double getC16n() {
		return c16n;
	}
	public void setC16n(Double c16n) {
		this.c16n = c16n;
	}
	public Double getC17n() {
		return c17n;
	}
	public void setC17n(Double c17n) {
		this.c17n = c17n;
	}
	public Double getC18n() {
		return c18n;
	}
	public void setC18n(Double c18n) {
		this.c18n = c18n;
	}
	public Double getC19n() {
		return c19n;
	}
	public void setC19n(Double c19n) {
		this.c19n = c19n;
	}
	public Double getC20n() {
		return c20n;
	}
	public void setC20n(Double c20n) {
		this.c20n = c20n;
	}
	public Double getC21n() {
		return c21n;
	}
	public void setC21n(Double c21n) {
		this.c21n = c21n;
	}
	public Double getC22n() {
		return c22n;
	}
	public void setC22n(Double c22n) {
		this.c22n = c22n;
	}
	public Double getC23n() {
		return c23n;
	}
	public void setC23n(Double c23n) {
		this.c23n = c23n;
	}
	public Double getC24n() {
		return c24n;
	}
	public void setC24n(Double c24n) {
		this.c24n = c24n;
	}
	public Double getC25n() {
		return c25n;
	}
	public void setC25n(Double c25n) {
		this.c25n = c25n;
	}
	public Double getC26n() {
		return c26n;
	}
	public void setC26n(Double c26n) {
		this.c26n = c26n;
	}
	public Double getC27n() {
		return c27n;
	}
	public void setC27n(Double c27n) {
		this.c27n = c27n;
	}
	public Double getC28n() {
		return c28n;
	}
	public void setC28n(Double c28n) {
		this.c28n = c28n;
	}
	public Double getC29n() {
		return c29n;
	}
	public void setC29n(Double c29n) {
		this.c29n = c29n;
	}
	public Double getC30n() {
		return c30n;
	}
	public void setC30n(Double c30n) {
		this.c30n = c30n;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public EstDetDJ getEstDetDJ() {
		return estDetDJ;
	}
	public void setEstDetDJ(EstDetDJ estDetDJ) {
		this.estDetDJ = estDetDJ;
	}
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public Integer getFila() {
		return fila;
	}
	public void setFila(Integer fila) {
		this.fila = fila;
	}


	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}



	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		//clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones

		if (hasError()) {
			return false;
		}

		return true;
	}



	/**
	 *  Devuelve un string con la información de las columnas (c01n..c30n) y la del campo contenido en un string formateado.
	 * 
	 */
	public String getDataStr(){
		return EnviosOsirisHelper.obtenerDataStrParaDetalleDJ(this);
	}
	
	
	/**
	 * Agrega un '|' como divisor de campo segun el nro de registro.
	 * TODO Abrir en dos metodos: uno para DREI y otro para ETUR
	 * 
	 * @return string
	 */
	public String getContenidoParaParser(){
		return EnviosOsirisHelper.prepararContenidoParaParser(this);
	}
	
	/**
	 *  Devuelve un string con la información de las columnas (c01n..c30n) parseadas del campo contenido en un string preformateado.
	 *
	 * @return string 
	 */
	public String getContenidoParseado(){
		return EnviosOsirisHelper.obtenerDataStrFromContenido(this);
	}
	
	
	/**
	 *  Carga los datos de la Cabecera Encriptada en el ForDecJur pasado. 
	 * 
	 * @param forDecJur
	 */
	public void prepararForDecJur(ForDecJur forDecJur) throws Exception{

		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		// Fecha de Presentacion (se toma de la transaccion)
		forDecJur.setFechaPresentacion(this.getTranAfip().getFechaProceso());
		
		// Codigo de Jurisdiccion Cabecera
		try {
			forDecJur.setCodJurCab(this.getC01n().intValue());			
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Código de Jurisdicción (c01n) del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Numero de Formulario
		try {
			forDecJur.setNroFormulario(this.getC02n().intValue());
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Número de Formulario (c02n) del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Impuesto
		long impuesto = 0L;
		try {
			impuesto = this.getC03n().longValue();
			forDecJur.setImpuesto(impuesto);
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Código de Impuesto (c03n) del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Determinar Recurso
		Recurso recurso = null;
		if(impuesto == 6050)
			recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_DReI); 
		else if(impuesto == 6053)
			recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_ETuR);
		
		if(recurso == null){
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo identificar el Recurso a partir del código de impuesto: " + impuesto;
			forDecJur.addRecoverableError(err);
			AdpRun.logRun(err);
			return;
		}
		
		forDecJur.setRecurso(recurso);		
		
		// Concepto
		try {
			forDecJur.setConcepto(this.getC04n().intValue());
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Concepto (c04n) del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// CUIT
		try {
			forDecJur.setCuit(StringUtil.formatDouble(this.getC05n(),"0"));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el CUIT (c05n) del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Nro de Inscripcion en IIBB o Convenio Multilateral
		try {			
			forDecJur.setNroInscripcion(StringUtil.formatDouble(this.getC06n(),"0"));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Nro de Inscripción en IIBB o Convenio Multilateral (c06n) del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Determinar periodo fiscal
		Integer anio = null;
		Integer periodo = null;
		try {	
			String periodoFiscal = StringUtil.formatDouble(this.getC07n(),"0");
			if(periodoFiscal.length() >= 6){
				anio = Integer.valueOf(periodoFiscal.substring(0, 4));
				periodo = Integer.valueOf(periodoFiscal.substring(4, 6));
			}
			forDecJur.setAnio(anio);
			forDecJur.setPeriodo(periodo);
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Año y Periodo Fiscal (c07n) del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Cuota
		try{
			forDecJur.setCuota(this.getC08n().intValue());
		} catch (Exception e) {
			String err = "Advertencia al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Nro de Cuota (c08n) del registro cabecera para el ForDecJur.";
			forDecJur.getTranAfip().getListAdvertencias().add(err);
			AdpRun.logRun(err + "Este registro actualmente no es necesario para continuar el procesamiento, por esta razón no se lo considera como error.");
			forDecJur.setCuota(0);
		}

		// Codigo de rectificativa
		try{
			forDecJur.setCodRectif(this.getC09n().intValue());
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Código de Rectificativa (c09n) del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}

		// Hora
		try{
			forDecJur.setHora(DateUtil.formatDate(DateUtil.getTime(StringUtil.formatDouble(this.getC10n(),"0"),"HHmmss"),"HH:mm:ss"));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener la hora del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}

		// Version
		try{
			forDecJur.setVersion(this.getC11n().intValue());
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener la Versión del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Release
		try{
			forDecJur.setRelease(this.getC12n().intValue());
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Release del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}

		// Version Interna del Aplicativo
		try{
			forDecJur.setVersionInterna(StringUtil.formatDouble(this.getC13n(),"0"));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener la Versión Interna del Aplicativo del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Numero Verificador
		try{
			forDecJur.setNroVerificador(this.getC14n().longValue());
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Número Verificador del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Fecha de Vencimiento
		try{
			forDecJur.setFechaVencimiento(DateUtil.getDate(StringUtil.formatDouble(this.getC15n(),"0"),DateUtil.YYYYMMDD_MASK));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener la Fecha de Vencimiento del registro cabecera para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
	}
	
	/**
	 *  Carga los Datos Generales de la Empresa en el ForDecJur pasado. 
	 * 
	 * @param forDecJur no puede ser null
	 */
	public void cargarDatosEmpresa(ForDecJur forDecJur) throws Exception{

		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido();
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);				
			return;
		}
		
		// Tipo Organizacion
		try{
			forDecJur.setTipoOrg(datum.getInteger(0));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Tipo de Organizacion de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Categoria
		try{
			forDecJur.setCategoria(datum.getInteger(1));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener la Categoría de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}

		// Numero de Inscripcion en el impuesto sobre los IIBB
		try{
			forDecJur.setNroInsImpIIBB(datum.getCols(2));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Número de Inscripción en el impuesto sobre los IIBB de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}

		// Fecha de Inscripcion en IIBB
		try{
			forDecJur.setFechaInsIIBB(DateUtil.getDate(datum.getCols(3),DateUtil.YYYYMMDD_MASK));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener la Fecha de Inscripcion en IIBB de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Fecha de Baja en IIBB
		try{
			forDecJur.setFechaBajaIIBB(DateUtil.getDate(datum.getCols(4),DateUtil.YYYYMMDD_MASK));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener la Fecha de Baja en IIBB de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}

		// Numero de Telefono
		try{
			forDecJur.setNroTelefono(datum.getCols(5));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Número de Teléfono de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Correo Electronico
		try{
			forDecJur.setEmail(datum.getCols(6));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el Correo Electrónico de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}

		// Otros Locales en la Prov de Santa Fe fuera de Rosario
		try{
			forDecJur.setOtrLocFueCiu(datum.getInteger(7));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Otros Locales en la Prov de Santa Fe fuera de Rosario' de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Contribuyente Concursado
		try{
			forDecJur.setConcursado(datum.getInteger(8));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Contribuyente Concursado' de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
		
		// Fecha de Declaracion de Quiebra
		try{
			forDecJur.setFechaDecQui(DateUtil.getDate(datum.getCols(11),DateUtil.YYYYMMDD_MASK));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener la Fecha de Declaración de Quiebra de los Datos Generales de la Empresa para el ForDecJur.";
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			AdpRun.logRun(err,e);
		}
	
	}

	/**
	 *  Carga los Datos de Convenio en el ForDecJur pasado. 
	 * 
	 * @param forDecJur
	 */
	public void cargarDatosConvenio(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		// Regimen General
		try{
			forDecJur.setRegimenGeneral(datum.getInteger(0));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Régimen General' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Régimen General' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Coeficiente de Santa Fe
		try{
			forDecJur.setCoeficienteSF(datum.getDouble(1)/10000);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Coeficiente de Santa Fe' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Coeficiente de Santa Fe' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}

		// Regimen Especial
		try{
			forDecJur.setRegimenEspecial(datum.getInteger(2));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Régimen Especial' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Régimen Especial' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}

		// Articulo 6
		try{
			forDecJur.setArticulo6(datum.getInteger(3));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 6' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 6' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Articulo 7
		try{
			forDecJur.setArticulo7(datum.getInteger(4));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 7' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 7' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Articulo 8
		try{
			forDecJur.setArticulo8(datum.getInteger(5));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 8' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 8' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Articulo 9
		try{
			forDecJur.setArticulo9(datum.getInteger(6));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 9' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 9' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Articulo 10
		try{
			forDecJur.setArticulo10(datum.getInteger(7));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 10' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 10' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Articulo 11
		try{
			forDecJur.setArticulo11(datum.getInteger(8));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 11' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 11' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Articulo 12
		try{
			forDecJur.setArticulo12(datum.getInteger(9));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 12' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 12' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Articulo 13
		try{
			forDecJur.setArticulo13(datum.getInteger(10));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 13' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Articulo 13' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Otros Locales en la Prov. de Santa Fe fuera de Rosario
		try{
			forDecJur.setOtrLocFueCiuPorCon(datum.getInteger(11));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Otros Locales en la Prov. de Santa Fe fuera de Rosario' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Otros Locales en la Prov. de Santa Fe fuera de Rosario' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Coeficiente Intercomunal
		try{
			forDecJur.setCoefIntercomunal(datum.getDouble(12)/10000);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Coeficiente Intercomunal' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Coeficiente Intercomunal' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Otros Locales en el Pais fuera de la Prov de Santa Fe
		try{
			forDecJur.setOtrLocFueProvPorCon(datum.getInteger(13));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Otros Locales en el Pais fuera de la Prov de Santa Fe' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Otros Locales en el Pais fuera de la Prov de Santa Fe' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Fecha de Inscripcion en Convenio Multilateral
		try{
			forDecJur.setFechaInsConMul(DateUtil.getDate(datum.getCols(14),DateUtil.YYYYMMDD_MASK));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Fecha de Inscripcion en Convenio Multilateral' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Fecha de Inscripcion en Convenio Multilateral' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
		
		// Fecha de baja en Convenio Multilateral
		try{
			forDecJur.setFechaBajaConMul(DateUtil.getDate(datum.getCols(15),DateUtil.YYYYMMDD_MASK));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Fecha de baja en Convenio Multilateral' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo obtener el campo 'Fecha de baja en Convenio Multilateral' de los Datos de Convenio para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
		}
	}

	
	/**
	 *  Agrega Socios y Firmantes al ForDecJur pasado. 
	 * 
	 * @param forDecJur
	 */
	public void agregarSocio(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		// Crear Socio/Firmante
		Socio socio = new Socio();
		socio.setForDecJur(forDecJur);
		
		socio.setApellido(datum.getCols(0));
		// Si corresponde a un Socio
		if(this.getRegistro().intValue() == 4 ){
			try{
				socio.setApellidoMaterno(datum.getCols(1));
				socio.setNombre(datum.getCols(2));
				socio.setEnCaracterDe(datum.getInteger(3));
				socio.setTipoDocumento(datum.getInteger(4));
				socio.setNroDocumento(datum.getCols(5));
				socio.setCuit(datum.getCols(6));
				socio.setSoloFirmante(SiNo.NO.getId());
			} catch (Exception e) {
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Socio para el ForDecJur.");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Socio para el ForDecJur.");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
				return;
			}
		}
		// Si corresponde a un Firmante
		if(this.getRegistro().intValue() == 5){
			try{
				socio.setNombre(datum.getCols(1));
				socio.setEnCaracterDe(datum.getInteger(2));
				socio.setTipoDocumento(datum.getInteger(3));
				socio.setNroDocumento(datum.getCols(4));
				socio.setCuit(datum.getCols(5));
				socio.setSoloFirmante(SiNo.SI.getId());
			} catch (Exception e) {
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Firmante para el ForDecJur.");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Firmante para el ForDecJur.");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
				return;
			}
		}
		
		if(forDecJur.getListSocio() == null)
			forDecJur.setListSocio(new ArrayList<Socio>());
		forDecJur.getListSocio().add(socio);
		
	}
	
	
	/**
	 *  Agrega Locales al ForDecJur pasado. Carga los datos iniciales del local. 
	 * 
	 * @param forDecJur
	 */
	public void agregarLocal(ForDecJur forDecJur) throws Exception{
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido();
			forDecJur.addRecoverableValueError(err);
			AdpRun.logRun(err);
			return;
		}
		
		// Crear Local
		Local local = new Local();
		local.setForDecJur(forDecJur);
		try{
			local.setNumeroCuenta(datum.getCols(0));
			local.setCantPersonal(datum.getInteger(1));
			local.setFecIniAct(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
			local.setFecCesAct(DateUtil.getDate(datum.getCols(3),DateUtil.YYYYMMDD_MASK));
			local.setNombreFantasia(datum.getCols(4));
			local.setCentralizaIngresos(datum.getInteger(5));
			local.setContribEtur(datum.getInteger(6));
			local.setRadio(datum.getInteger(7));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Local para el ForDecJur.";
			forDecJur.addRecoverableValueError(err + "\n");
			AdpRun.logRun(err, e);
			return;
		}
		
		Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(forDecJur.getRecurso().getId(), local.getNumeroCuenta());
		local.setCuenta(cuenta);
		if(cuenta == null){
			String err = "Envio: "+envioAfip+", Advertencia al procesar Detalle DJ de id: "+this.getId()+". La cuenta de Nro: "+local.getNumeroCuenta()+" del Recurso: "+forDecJur.getRecurso().getCodRecurso()+" no se encuentra en la db."; 
			forDecJur.getTranAfip().getListAdvertencias().add(err);
			AdpRun.logRun(err);
		}
		
		if(forDecJur.getListLocal() == null)
			forDecJur.setListLocal(new ArrayList<Local>());
		forDecJur.getListLocal().add(local);
		
		// Se carga en un mapa para relacionar sus detalles
		forDecJur.getMapLocal().put(local.getNumeroCuenta(), local);
	}
	
	/**
	 *  Agrega Habilitaciones de Locales. 
	 * 
	 * @param forDecJur
	 */
	public void agregarHabLoc(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		HabLoc habLoc = new HabLoc();
		try{
			habLoc.setNumeroCuenta(datum.getCols(0));
			habLoc.setCodRubro(datum.getLong(1));
			habLoc.setFechaHabilitacion(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Habilitación de Local para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Habilitación de Local para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
		
		// Buscar Local que corresponde
		Local local = forDecJur.getMapLocal().get(habLoc.getNumeroCuenta());
		habLoc.setLocal(local);
		if(local == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+habLoc.getNumeroCuenta()+" informado.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+habLoc.getNumeroCuenta()+" informado.");
			return;
		}
		
		if(local.getListHabLoc() == null)
			local.setListHabLoc(new ArrayList<HabLoc>());
		local.getListHabLoc().add(habLoc);
		
	}
	
	
	/**
	 *  Agrega Actividades de Locales. 
	 * 
	 * @param forDecJur
	 */
	public void agregarActLoc(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		ActLoc actLoc = new ActLoc();
		try{
			actLoc.setNumeroCuenta(datum.getCols(0));
			actLoc.setCodActividad(datum.getLong(1));
			actLoc.setFechaInicio(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
			actLoc.setMarcaPrincipal(datum.getInteger(3));
			actLoc.setTratamiento(datum.getInteger(4));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Actividad de Local para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Actividad de Local para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}	
		
		// Buscar Local que corresponde
		Local local = forDecJur.getMapLocal().get(actLoc.getNumeroCuenta());
		actLoc.setLocal(local);
		if(local == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+actLoc.getNumeroCuenta()+" informado.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+actLoc.getNumeroCuenta()+" informado.");
			return;
		}
		
		// Se intenta relacionar con la Actividad en Siat 
		RecConADec recConADec =  RecConADec.getByCodConceptoRecurso(forDecJur.getRecurso().getId(), actLoc.getCodActividad().toString());
		if (recConADec == null) {
			// Si no la puedo relacionar, intento relacionarla la busqueda por DReI 
			recConADec =  RecConADec.getByCodConceptoRecurso(Recurso.getDReI().getId(), actLoc.getCodActividad().toString());
			if(recConADec == null){
				//verificar nuevamente y marcar advertencia
				String err = "Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se encuentra en SIAT la Actividad de código: "+ actLoc.getCodActividad().toString() + " para el Recurso "+forDecJur.getRecurso().getCodRecurso();
				forDecJur.getTranAfip().getListAdvertencias().add(err);
				AdpRun.logRun(err);
			}
		}
		actLoc.setRecConAdec(recConADec);
		
		if(local.getListActLoc() == null)
			local.setListActLoc(new ArrayList<ActLoc>());
		local.getListActLoc().add(actLoc);
		
		// Se carga en un mapa para relacionar sus detalles
		forDecJur.getMapActLoc().put(actLoc.getNumeroCuenta()+"|"+actLoc.getCodActividad(), actLoc);
	}	
	
	/**
	 *  Agrega Exenciones de Actividades
	 * 
	 * @param forDecJur
	 */
	public void agregarExeActLoc(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		ExeActLoc exeActLoc = new ExeActLoc();
		
		try{
			exeActLoc.setNumeroCuenta(datum.getCols(0));
			exeActLoc.setCodActividad(datum.getLong(1));
			exeActLoc.setNroResolucion(datum.getCols(2));
			exeActLoc.setFechaEmision(DateUtil.getDate(datum.getCols(3),DateUtil.YYYYMMDD_MASK));
			exeActLoc.setFechaDesde(DateUtil.getDate(datum.getCols(4),DateUtil.YYYYMMDD_MASK));
			exeActLoc.setFechaHasta(DateUtil.getDate(datum.getCols(5),DateUtil.YYYYMMDD_MASK));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Exenciones de Actividades para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Exenciones de Actividades para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
		
		// Buscar Local que corresponde
		ActLoc actLoc = forDecJur.getMapActLoc().get(exeActLoc.getNumeroCuenta()+"|"+exeActLoc.getCodActividad());
		exeActLoc.setActLoc(actLoc);
		if(actLoc == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar la actividad declarada para el local con nro cuenta: "+exeActLoc.getNumeroCuenta()+" y código de actividad: "+exeActLoc.getCodActividad()+".");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar la actividad declarada para el local con nro cuenta: "+exeActLoc.getNumeroCuenta()+" y código de actividad: "+exeActLoc.getCodActividad()+".");
			return;
		}
		
		if(actLoc.getListExeActLoc() == null)
			actLoc.setListExeActLoc(new ArrayList<ExeActLoc>());
		actLoc.getListExeActLoc().add(exeActLoc);
		
	}	
	
	/**
	 *  Agrega Otros Pagos por Locales. 
	 * 
	 * @param forDecJur
	 */
	public void agregarOtrosPagos(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();
		
		// Si se llama al metodo desde un DetalleDJ que no corresponde al registro nro 10 se retorna el ForDecJur sin modificar.
		if(this.getRegistro().intValue() != 10 || forDecJur == null){
			if(forDecJur != null){
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");					
			}
			return;
		}

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		OtrosPagos otrosPagos = new OtrosPagos();
		try{
			otrosPagos.setNumeroCuenta(datum.getCols(0));
			otrosPagos.setTipoPago(datum.getInteger(1));
			otrosPagos.setFechaPago(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
			otrosPagos.setPeriodoPago(datum.getInteger(3));
			otrosPagos.setNroResolucion(datum.getCols(4));
			otrosPagos.setAnio(datum.getInteger(5));
			otrosPagos.setImportePago(datum.getDouble(6)/100);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Otros Pagos para Local para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Otros Pagos para Local para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
		
		// Buscar Local que corresponde
		Local local = forDecJur.getMapLocal().get(otrosPagos.getNumeroCuenta());
		otrosPagos.setLocal(local);
		if(local == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+otrosPagos.getNumeroCuenta()+" informado.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+otrosPagos.getNumeroCuenta()+" informado.");
			return;
		}
		
		if(local.getListOtrosPagos() == null)
			local.setListOtrosPagos(new ArrayList<OtrosPagos>());
		local.getListOtrosPagos().add(otrosPagos);
		
	}
	
	/**
	 *  Agrega  Declaracion de Actividades por Local.
	 * 
	 * @param forDecJur
	 */
	public void agregarDecActLocDrei(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();
		
		// Si se llama al metodo desde un DetalleDJ que no corresponde al registro nro 11 se retorna el ForDecJur sin modificar.
		if(this.getRegistro().intValue() != 11 || forDecJur == null){
			if(forDecJur != null){
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");				
			}
			return;
		}

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		DecActLoc decActLoc = new DecActLoc();
		
		try{
			decActLoc.setNumeroCuenta(datum.getCols(0));
			decActLoc.setCodActividad(datum.getLong(1));
			decActLoc.setBaseImpExenta(datum.getDouble(2));
			decActLoc.setBaseImponible(datum.getDouble(3)/100);
			Double ajuCamCoe = datum.getDouble(5)/100; 
			if(datum.getInteger(4) == 1)	
				ajuCamCoe = ajuCamCoe * (-1);
			decActLoc.setAjuCamCoe(ajuCamCoe);
			Double baseImpAju = datum.getDouble(7)/100; 
			if(datum.getInteger(6) == 1)	
				baseImpAju = baseImpAju * (-1);
			decActLoc.setBaseImpAjustada(baseImpAju);
			decActLoc.setAliCuota(datum.getDouble(8)/10000);
			Double derechoCalculado = datum.getDouble(10)/100;
			if(datum.getInteger(9) == 1)	
				derechoCalculado = derechoCalculado * (-1);
			decActLoc.setDerechoCalculado(derechoCalculado);
			decActLoc.setCantidad(datum.getDouble(11)/100);
			decActLoc.setUnidadMedida(datum.getInteger(12));
			decActLoc.setTipoUnidad(datum.getInteger(13));
			decActLoc.setMinimoPorUnidad(datum.getDouble(14)/100);
			decActLoc.setMinimoCalculado(datum.getDouble(15)/100);
			Double derechoDet = datum.getDouble(17)/100;
			if(datum.getInteger(16) == 1)	
				derechoDet = derechoDet * (-1);
			decActLoc.setDerechoDet(derechoDet);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Declaracion de Actividades por Local para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Declaracion de Actividades por Local para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
		
		// Se intenta relacionar con la Actividad en Siat TODO ver si lo agregamos
		//RecConADec recConADec =  RecConADec.getByCodConceptoRecurso(forDecJur.getRecurso().getId(), decActLoc.getCodActividad().toString());
		//decActLoc.setRecConADec(recConADec);
		
		// Buscar Local que corresponde
		Local local = forDecJur.getMapLocal().get(decActLoc.getNumeroCuenta());
		decActLoc.setLocal(local);
		if(local == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+decActLoc.getNumeroCuenta()+" informado.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+decActLoc.getNumeroCuenta()+" informado.");
			return;
		}
		
		if(local.getListDecActLoc() == null)
			local.setListDecActLoc(new ArrayList<DecActLoc>());
		local.getListDecActLoc().add(decActLoc);
	
		// Se carga en un mapa para relacionar sus detalles
		forDecJur.getMapDecActLoc().put(decActLoc.getNumeroCuenta()+"|"+decActLoc.getCodActividad(), decActLoc);
	
	}
	
	/**
	 *  Cargar datos de Totales por Local de las Actividades Declaradas
	 * 
	 * @param forDecJur
	 */
	public void cargarTotalesParaLocalDrei(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();
		
		// Si se llama al metodo desde un DetalleDJ que no corresponde al registro nro 12 se retorna el ForDecJur sin modificar.
		if(this.getRegistro().intValue() != 12 || forDecJur == null){
			if(forDecJur != null){
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");					
			}
			return;
		}

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		// Buscar Local que corresponde
		Local local = forDecJur.getMapLocal().get(datum.getCols(0));
		if(local == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+datum.getCols(0)+" informado.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+datum.getCols(0)+" informado.");
			return;
		}
		
		try{
			local.setPagaMinimo(datum.getInteger(1));
			Double derDetTot = datum.getDouble(4)/100;
			if(datum.getInteger(3) == 1)	
				derDetTot = derDetTot * (-1);
			local.setDerDetTot(derDetTot);
			local.setMinimoGeneral(datum.getDouble(5)/100);
			Double derecho = datum.getDouble(7)/100;
			if(datum.getInteger(6) == 1)	
				derecho = derecho * (-1);
			local.setDerecho(derecho);
			local.setAlicuotaPub(datum.getDouble(8)/10000);
			local.setPublicidad(datum.getDouble(9)/100);
			local.setAlicuotaMesYSil(datum.getDouble(10)/10000);
			local.setMesasYSillas(datum.getDouble(10)/100);
			Double subTotal1 = datum.getDouble(13)/100; 
			if(datum.getInteger(12) == 1) 
				subTotal1 = subTotal1 * (-1);
			local.setSubTotal1(subTotal1);
			local.setOtrosPagos(datum.getDouble(14)/100);
			local.setComputado(datum.getDouble(15)/100);
			local.setResto(datum.getDouble(16)/100);
			Double derechoTotal = datum.getDouble(18)/100; 
			if(datum.getInteger(17) == 1) 
				derechoTotal = derechoTotal * (-1);
			local.setDerechoTotal(derechoTotal);
			local.setPaga(datum.getInteger(19));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el local con Totales de las Actividades Declaradas para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el local con Totales de las Actividades Declaradas para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
	}
	
	/**
	 *  Agrega  Retenciones y Percepciones a la ForDecJur.
	 * 
	 * @param forDecJur
	 */
	public void agregarRetYPer(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();
		
		// Si se llama al metodo desde un DetalleDJ que no corresponde al registro nro 13 se retorna el ForDecJur sin modificar.
		if(this.getRegistro().intValue() != 13 || forDecJur == null){
			if(forDecJur != null){
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");				
			}
			return;
		}

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		RetYPer retYPer = new RetYPer();
		retYPer.setForDecJur(forDecJur);
		try{
			retYPer.setTipoDeduccion(datum.getInteger(0));
			retYPer.setCuitAgente(datum.getCols(1));
			retYPer.setDenominacion(datum.getCols(2));
			retYPer.setFecha(DateUtil.getDate(datum.getCols(3),DateUtil.YYYYMMDD_MASK));
			retYPer.setNroConstancia(datum.getCols(4));
			retYPer.setImporte(datum.getDouble(5)/100);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Retenciones y Percepciones para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Retenciones y Percepciones para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
		
		// Se intenta obtener el Agente de Retencion desde SIAT
		AgeRet ageRet = AgeRet.getByCuitYRecurso(retYPer.getCuitAgente(), forDecJur.getRecurso().getId()); 
		retYPer.setAgeRet(ageRet);
		if(ageRet == null){
			String err = "Envio: "+envioAfip+", Advertencia al procesar Detalle DJ de id: "+this.getId()+". No se encuentra en SIAT el Agente de Retencion de CUIT: "+ retYPer.getCuitAgente() + " para el Recurso "+forDecJur.getRecurso().getCodRecurso();
			forDecJur.getTranAfip().getListAdvertencias().add(err);
			AdpRun.logRun(err);
		}
		
		if(forDecJur.getListRetYPer() == null)
			forDecJur.setListRetYPer(new ArrayList<RetYPer>());
		forDecJur.getListRetYPer().add(retYPer);
				
	}
	
	/**
	 *  Cargar Ajuste Base Imponible por cambio de Coeficiente por local (se completa el ActLoc)
	 * 
	 * @param forDecJur
	 */
	public void cargarAjuBasImpParaActLoc(ForDecJur forDecJur) throws Exception{
		
		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();
		
		// Si se llama al metodo desde un DetalleDJ que no corresponde al registro nro 14 se retorna el ForDecJur sin modificar.
		if(this.getRegistro().intValue() != 14 || forDecJur == null){
			if(forDecJur != null){
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");						
			}
			return;
		}

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		// Buscar DecActLoc que corresponde
		DecActLoc decActLoc = forDecJur.getMapDecActLoc().get(datum.getCols(0)+"|"+datum.getLong(1));
		if(decActLoc == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar la declaración de actividad del local de nro cuenta: "+datum.getCols(0)+" y código de actividad: "+datum.getLong(1));
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+datum.getCols(0)+" informado.");
			return;
		}
		
		try{
			decActLoc.setDifBaseEnero(datum.getDouble(2)/100);
			decActLoc.setDifBaseFebrero(datum.getDouble(3)/100);
			decActLoc.setDifBaseMarzo(datum.getDouble(4)/100);
			decActLoc.setDifBaseAbril(datum.getDouble(5)/100);
			decActLoc.setDifBaseMayo(datum.getDouble(6)/100);
			decActLoc.setDifBaseJunio(datum.getDouble(7)/100);
			decActLoc.setDifBaseJulio(datum.getDouble(8)/100);
			decActLoc.setDifBaseAgosto(datum.getDouble(9)/100);
			decActLoc.setDifBaseSeptiembre(datum.getDouble(10)/100);
			decActLoc.setDifBaseOctubre(datum.getDouble(11)/100);
			decActLoc.setDifBaseNoviembre(datum.getDouble(12)/100);
			decActLoc.setDifBaseDiciembre(datum.getDouble(13)/100);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el la Declaración de Actividad del Local con Ajustes de Base Imponible por cambio de Coeficiente.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el la Declaración de Actividad del Local con Ajustes de Base Imponible por cambio de Coeficiente.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}		
	}
	
	/**
	 *  Cargar Liquidacion de DJ Mensual DREI  (se completa el ForDecJur)
	 * 
	 * @param forDecJur
	 */
	public void cargarLiqDJMensualDrei(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();
		
		// Si se llama al metodo desde un DetalleDJ que no corresponde al registro nro 15 se retorna el ForDecJur sin modificar.
		if(this.getRegistro().intValue() != 15 || forDecJur == null){
			if(forDecJur != null){
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");						
			}
			return;
		}

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		try{
			forDecJur.setDerecho(datum.getDouble(0)/100);
			forDecJur.setTotRetYPer(datum.getDouble(1)/100);
			forDecJur.setRetYPerPerAnt(datum.getInteger(2));
			forDecJur.setPerRetYPerPerAnt(datum.getCols(3));
			forDecJur.setCodRecRetPerPerAnt(datum.getInteger(4));
			forDecJur.setMontoRetPerPerAnt(datum.getDouble(5)/100);
			forDecJur.setAFavorContrib(datum.getDouble(6)/100);
			forDecJur.setAFavorDirMun(datum.getDouble(7)/100);
			forDecJur.setFecVenLiqMen(DateUtil.getDate(datum.getCols(8),DateUtil.YYYYMMDD_MASK));
			forDecJur.setFecPagPre(DateUtil.getDate(datum.getCols(9),DateUtil.YYYYMMDD_MASK));
			forDecJur.setTasaInteres(datum.getDouble(10)/1000);
			forDecJur.setRecargoInteres(datum.getDouble(11)/100);
			forDecJur.setDerechoAdeuda(datum.getDouble(12)/100);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el Formulario de Declaración Jurada con los Totales de Liquidacion de DJ Mensual DREI.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el Formulario de Declaración Jurada con los Totales de Liquidacion de DJ Mensual DREI.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
	}

	/**
	 *  Agregar Declaracion de Actividades ETUR por Local
	 * 
	 * @param forDecJur
	 */
	public void agregarDecActLocEtur(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		DecActLoc decActLoc = new DecActLoc();
		
		try{
			decActLoc.setNumeroCuenta(datum.getCols(0));
			decActLoc.setCodActividad(datum.getLong(1));
			decActLoc.setAlcanceEtur(datum.getInteger(2));
			decActLoc.setBaseImpExenta(0D);
			decActLoc.setBaseImponible(datum.getDouble(3)/100);
			decActLoc.setAjuCamCoe(0D);
			decActLoc.setBaseImpAjustada(datum.getDouble(3)/100);
			decActLoc.setAliCuota(datum.getDouble(4)/10000);
			decActLoc.setDerechoCalculado(datum.getDouble(5)/100);
			decActLoc.setCantidad(null);
			decActLoc.setUnidadMedida(null);
			decActLoc.setTipoUnidad(null);
			decActLoc.setMinimoPorUnidad(null);
			decActLoc.setMinimoCalculado(datum.getDouble(6)/100);
			decActLoc.setDerechoDet(datum.getDouble(7)/100);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Declaracion de Actividades ETUR para Local.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Declaracion de Actividades ETUR para Local.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
		
		// Buscar Local que corresponde
		Local local = forDecJur.getMapLocal().get(decActLoc.getNumeroCuenta());
		decActLoc.setLocal(local);
		if(local == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+decActLoc.getNumeroCuenta()+" informado.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+decActLoc.getNumeroCuenta()+" informado.");
			return;
		}
		
		if(local.getListDecActLoc() == null)
			local.setListDecActLoc(new ArrayList<DecActLoc>());
		local.getListDecActLoc().add(decActLoc);
	
		// Se carga en un mapa para relacionar sus detalles
		forDecJur.getMapDecActLoc().put(decActLoc.getNumeroCuenta()+"|"+decActLoc.getCodActividad(), decActLoc);
	
	}
	
	/**
	 *  Cargar Totales por Local de las Actividades Declaradas ETUR (completa el Local)
	 * 
	 * @param forDecJur
	 */
	public void cargarTotalesParaLocalEtur(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		// Buscar Local que corresponde
		Local local = forDecJur.getMapLocal().get(datum.getCols(0));
		if(local == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+datum.getCols(0)+" informado.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+datum.getCols(0)+" informado.");
			return;
		}
		
		try{
			local.setPagaMinimo(null);
			local.setDerDetTot(null);
			local.setMinimoGeneral(null);
			local.setDerecho(null);
			local.setAlicuotaPub(null);
			local.setPublicidad(null);
			local.setAlicuotaMesYSil(null);
			local.setMesasYSillas(null);
			local.setSubTotal1(null);
			local.setComputado(null);
			local.setResto(null);
			local.setDerechoTotal(datum.getDouble(1)/100);
			local.setPaga(datum.getInteger(2));
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el local con Totales de las Actividades Declaradas ETUR para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el local con Totales de las Actividades Declaradas ETUR para el ForDecJur.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
		
		Double mayorValorDeterminado = 0D;
		for(DecActLoc decActLoc: local.getListDecActLoc()){
			if(decActLoc.getDerechoDet() > mayorValorDeterminado){
				mayorValorDeterminado = decActLoc.getDerechoDet();				
			}
		}
		// Con el mayor valor determinado por actividad y el valor de contribucion etur calculado obtenemos el valor para otros pagos
		Double otrosPagos = mayorValorDeterminado - local.getDerechoTotal();
		if(otrosPagos < 0)
			otrosPagos = 0D;
		local.setOtrosPagos(otrosPagos);
	
	}
	
	/**
	 *  Cargar Liquidacion de DJ Mensual ETUR  (se completa el ForDecJur)
	 * 
	 * @param forDecJur
	 */
	public void cargarLiqDJMensualEtur(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		try{
			forDecJur.setDerecho(datum.getDouble(0)/100);
			forDecJur.setTotRetYPer(null);
			forDecJur.setRetYPerPerAnt(null);
			forDecJur.setPerRetYPerPerAnt(null);
			forDecJur.setCodRecRetPerPerAnt(null);
			forDecJur.setMontoRetPerPerAnt(null);
			forDecJur.setAFavorContrib(null);
			forDecJur.setAFavorDirMun(null);
			
			forDecJur.setFecVenLiqMen(DateUtil.getDate(datum.getCols(1),DateUtil.YYYYMMDD_MASK));
			forDecJur.setFecPagPre(DateUtil.getDate(datum.getCols(2),DateUtil.YYYYMMDD_MASK));
			forDecJur.setTasaInteres(datum.getDouble(3)/1000);
			forDecJur.setRecargoInteres(datum.getDouble(4)/100);
			forDecJur.setDerechoAdeuda(datum.getDouble(5)/100);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el Formulario de Declaración Jurada con los Totales de Liquidacion de DJ Mensual ETUR.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo completar el Formulario de Declaración Jurada con los Totales de Liquidacion de DJ Mensual ETUR.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
	}
	
	/**
	 *  Agregar Datos de Domicilios
	 * 
	 * @param forDecJur
	 */
	public void agregarDomicilio(ForDecJur forDecJur) throws Exception{

		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			AdpRun.logRun("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		DatosDomicilio datosDomicilio = new DatosDomicilio();
		datosDomicilio.setForDecJur(forDecJur);
		
		try{
			datosDomicilio.setCodPropietario(datum.getInteger(0));
			datosDomicilio.setCodInterno(datum.getInteger(1));
			datosDomicilio.setCalle(datum.getCols(2));
			datosDomicilio.setNumero(datum.getInteger(3));
			datosDomicilio.setAdicional(datum.getCols(4));
			datosDomicilio.setTorre(datum.getCols(5));
			datosDomicilio.setPiso(datum.getCols(6));
			datosDomicilio.setDptoOficina(datum.getCols(7));
			datosDomicilio.setSector(datum.getCols(8));
			//datosDomicilio.setBarrio(datum.getCols(9)); // TODO VER DATO BARRIO!!!!
			datosDomicilio.setLocalidad(datum.getCols(10));
			datosDomicilio.setCodPostal(datum.getCols(11));
			datosDomicilio.setProvincia(datum.getInteger(12));
		} catch (Exception e) {
			String err = "Envio: "+envioAfip+", Advertencia! Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Domicilio.";
			forDecJur.getTranAfip().getListAdvertencias().add(err);
			AdpRun.logRun(err,e);
			return;
		}
		
		// Segun el codigo de propietario se intenta relacionar con un local o socio o firmante.
		if(datosDomicilio.getCodPropietario().intValue() == PropietarioAfip.LOCAL.getId().intValue()){
			try {
				//. Se busca el Local de la lista considerando que se agregaron en orden y utilizando el codigo interno como indice de busqueda.
				Local local = forDecJur.getListLocal().get(datosDomicilio.getCodInterno()-1);
				local.setDatosDomicilio(datosDomicilio);
			} catch (Exception e) {
				String err = "Envio: "+envioAfip+", Advertencia!. Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo relacionar el domicilio con el Local.";
				forDecJur.getTranAfip().getListAdvertencias().add(err);
				AdpRun.logRun(err, e);
			}
		}else if(datosDomicilio.getCodPropietario().intValue() == PropietarioAfip.SOCIO.getId().intValue()){
			//. Se busca el Socio de la lista considerando que se agregaron en orden y utilizando el codigo interno como indice de busqueda. (primero se filtra para descartar solo firmantes)
			List<Socio> listSocio = new ArrayList<Socio>();
			for(Socio socio: forDecJur.getListSocio()){
				if(socio.getSoloFirmante() != null || socio.getSoloFirmante().intValue() == SiNo.NO.getId().intValue())
					listSocio.add(socio);
			}
			try {
				Socio socio = listSocio.get(datosDomicilio.getCodInterno()-1);
				socio.setDatosDomicilio(datosDomicilio);
			} catch (Exception e) {
				String err = "Envio: "+envioAfip+", Advertencia!. Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo relacionar el domicilio con el Socio.";
				forDecJur.getTranAfip().getListAdvertencias().add(err);
				AdpRun.logRun(err, e);
			}
		}else if(datosDomicilio.getCodPropietario().intValue() == PropietarioAfip.FIRMANTE.getId().intValue()){
			//. Se busca el Firmante de la lista considerando que se agregaron en orden y utilizando el codigo interno como indice de busqueda. (primero se filtra para descartar los no firmantes)
			List<Socio> listFirmante = new ArrayList<Socio>();
			for(Socio socio: forDecJur.getListSocio()){
				if(socio.getSoloFirmante().intValue() == SiNo.SI.getId().intValue())
					listFirmante.add(socio);
			}
			try {
				Socio firmante = listFirmante.get(datosDomicilio.getCodInterno()-1);
				firmante.setDatosDomicilio(datosDomicilio);
			} catch (Exception e) {
				String err = "Envio: "+envioAfip+", Advertencia!. Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo relacionar el domicilio con el Firmante.";
				forDecJur.getTranAfip().getListAdvertencias().add(err);
				AdpRun.logRun(err, e);
			}
		}
		
		if(forDecJur.getListDatosDomicilio() == null)
			forDecJur.setListDatosDomicilio(new ArrayList<DatosDomicilio>());
		forDecJur.getListDatosDomicilio().add(datosDomicilio);
	}
	
	/**
	 *  Agrega Datos de pago por Cuenta 
	 * 
	 * @param forDecJur
	 */
	public void agregarPagoCuenta(ForDecJur forDecJur) throws Exception{
		
		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();

		Datum datum = Datum.parse(this.getContenidoParaParser());
		if(datum == null || datum.getColNumMax() <= 0){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo parsear el contendido: "+this.getContenido());				
			return;
		}
		
		DatosPagoCta datosPagoCta = new DatosPagoCta();
		
		try{
			datosPagoCta.setNumeroCuenta(datum.getCols(0));
			datosPagoCta.setCodImpuesto(datum.getInteger(1));
			datosPagoCta.setTotalMontoIngresado(datum.getDouble(2)/100);
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de pago por Cuenta.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de pago por Cuenta.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". ",e);
			return;
		}
		
		// Buscar Local que corresponde
		Local local = forDecJur.getMapLocal().get(datosPagoCta.getNumeroCuenta());
		datosPagoCta.setLocal(local);
		if(local == null){
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+datosPagoCta.getNumeroCuenta()+" informado.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo encontrar el local de nro cuenta: "+datosPagoCta.getNumeroCuenta()+" informado.");
			return;
		}
		
		if(local.getListDatosPagoCta() == null)
			local.setListDatosPagoCta(new ArrayList<DatosPagoCta>());
		local.getListDatosPagoCta().add(datosPagoCta);
	}
	
	/**
	 *  Agrega Totales de Derecho y Accesorios de la DJ
	 * 
	 * @param forDecJur
	 */
	public void agregarTotalesDerecho(ForDecJur forDecJur) throws Exception{

		AdpRun run = AdpRun.currentRun();
		
		long envioAfip = this.getTranAfip().getEnvioOsiris().getIdEnvioAfip();
		
		// Si se llama al metodo desde un DetalleDJ que no corresponde al registro nro 98 se retorna el ForDecJur sin modificar.
		if(this.getRegistro().intValue() != 98 || forDecJur == null){
			if(forDecJur != null){
				forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");
				if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". Nro de registro "+this.getRegistro()+" incorrecto");					
			}
			return;
		}
	
		TotDerYAccDJ totDerYAccDJ = new TotDerYAccDJ();
		totDerYAccDJ.setForDecJur(forDecJur);

		try{
			totDerYAccDJ.setCodImpuesto(this.getC01n().intValue());
			totDerYAccDJ.setConcepto(this.getC02n().intValue());
			totDerYAccDJ.setTotalMontoIngresado(this.getC03n());
		} catch (Exception e) {
			forDecJur.addRecoverableValueError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Totales de Derecho y Accesorios de la DJ.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+". No se pudo agregar información de Totales de Derecho y Accesorios de la DJ.");
			if(run!=null) run.logError("Envio: "+envioAfip+", Error al procesar Detalle DJ de id: "+this.getId()+".",e);
			return;
		}
		
		if(forDecJur.getListTotDerYAccDJ() == null)
			forDecJur.setListTotDerYAccDJ(new ArrayList<TotDerYAccDJ>());
		forDecJur.getListTotDerYAccDJ().add(totDerYAccDJ);
	}
	
	
}
