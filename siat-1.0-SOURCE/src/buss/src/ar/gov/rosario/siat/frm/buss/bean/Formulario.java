//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.DesImp;
import ar.gov.rosario.siat.frm.buss.dao.FrmDAOFactory;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.frm.iface.util.FrmError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PrintModel;

/**
 * Bean correspondiente a Formulario
 * 
 * @author tecso
 */
@Entity
@Table(name = "for_Formulario")
public class Formulario extends BaseBO {

	private static final long serialVersionUID = 1L;

	/** Representa el codigo del formulario correspondiente al Informe de Deuda	 */
	public static final String COD_FRM_INFORME_DEUDA="INF_DEUDA";
	
	/** Representa el codigo del formulario correspondiente para imprimir una Constancia de Deuda */
	public static final String COD_FRM_CONDEU_IMPRIMIR ="CONDEU_IMPRIMIR";
	
	/** Representa el codigo del formulario correspondiente para recomponer una Constancia de Deuda */
	public static final String COD_FRM_CONDEU_RECOMPONER = "CONDEU_RECOMPONER";
	
	/** Representa el codigo del formulario correspondiente para imprimir padron de una planilla de envio de Deuda a judicial*/
	public static final String COD_FRM_PLAENVDEUPRO_IMP_PAD = "PLAENVDEUPRO_IMP_PAD";
	
	/** Representa el codigo del formulario para recomponer una planilla de envio de Deuda a judicial*/
	public static final String COD_FRM_PLAENVDEUPRO_RECOMP = "PLAENVDEUPRO_RECOMP";
	
	public static final String COD_FRM_ESTADO_CUENTA = "ESTADO_CUENTA";

	public static final String COD_FRM_SOLCUEEXE_ENVIAR_CATASTRO = "SOLCUEEXE_ENV_CAT";

	public static final String COD_FRM_SOLCUEEXE_IMPRIMR = "SOLCUEEXE_IMPRIMIR";

	public static final String COD_FRM_SOLCUEEXE_ENVIAR_SINTYS = "SOLCUEEXE_ENV_SINTYS";

	public static final String COD_FRM_SOLCUEEXE_ENVIAR_DG = "SOLCUEEXE_ENV_DG";

	/** Representa el codigo del formulario correspondiente a la Liquidacion de Comisiones a Procurador */
	public static final String COD_FRM_LIQCOM = "LIQCOM";
	
	public static final String COD_FRM_PLAN_ALT_CUOTAS = "PLAN_ALT_CUOTAS";

	/** Reporte de Importe a Recaudar por Planes*/
	public static final String COD_FRM_IMPORTE_RECAUDAR_PROCURADOR_PLAN = "IMPORTE_RECAUDAR_PRO_PLA";

    /** Reporte recaudado por planes Detallado*/
	public static final String COD_FRM_IMPORTE_RECAUDADO_PLANES_DETALLADO = "IMP_RECAUDADO_PL_DET";
	/** Reporte recaudado por planes Resumido*/
	public static final String COD_FRM_IMPORTE_RECAUDADO_PLANES_RESUMIDO = "IMP_RECAUDADO_PL_RES";

	public static final String COD_FRM_CAMBIO_PLAN_CDM = "FRM_CAMBIO_PLAN_CDM";
	
	public static final String COD_FRM_INFORME_OBRA_CDM = "FRM_INFORME_OBRA_CDM";
	
	public static final String COD_FRM_NOTIF_PREENVIO_JUD = "NOTIF_PREENVIO_JUD";
	
	/** Reporte de Convenios Formalizados*/
	public static final String COD_REP_CONVENIOS_FORM = "REP_CONVENIOS_FORM";
	
	/** Reporte de Respuesta Operativos*/
	public static final String COD_FRM_RESPUESTA_OPERATIVOS = "RESPUESTA_OPERATIVOS";

	/** Reporte de Convenios a Caducar */
	public static final String COD_FRM_CONVENIO_A_CADUCAR = "CONVENIO_A_CADUCAR";

	/** Mail de reclamo de asentamiento*/
	public static final String COD_FRM_MAIL_ASE = "MAIL_ASE";

	public static final String COD_GDE_GESGUDREPORT = "GDE_GESGUDREPORT";
	
	/** Informe de historico de exenciones de un cuenta*/
	public static final String COD_FRM_INFCUEEXE= "INF_CUEEXEREPORT";

	public static final String COD_FRM_ORDENCONTROL = "EF_ORDENCONTROL";
	
	public static final String FRM_CARATULA_CYQ = "CARATULA_CYQ";
	
	/** Reporte de Clasificador*/
	public static final String COD_FRM_CLASIFICADOR = "CLASIFICADOR";

	/** Reporte de Rentas*/
	public static final String COD_FRM_RENTAS = "RENTAS";
	
	/** Consulta de Total por Nodo (de Clasificador)*/
	public static final String COD_FRM_TOTAL_NODO= "TOTAL_NODO";
	public static final String COD_FRM_TOTAL_NODO_ESPECIAL= "TOTAL_NODO_ESPECIAL";

	/** Reporte de Clasificador Comparativo*/
	public static final String COD_FRM_CLACOM = "CLACOM";
	
	/** Consulta de Total por Partida*/
	public static final String COD_FRM_TOTAL_PAR= "TOTAL_PAR";
	
	public static final String COD_FRM_CONSTANCIA_CYQ ="CONSTANCIA_CYQ";
	
	/** Acta de requerimientos de informacion*/
	public static final String COD_FRM_ACTA_REQ_INF ="ACTA_REQ_INF_EF";
	
	/** Acta Inicio de Procedimiento*/
	public static final String COD_FRM_ACTA_INI_PROC_EF ="ACTA_INI_PROC_EF";
	
	/** Acta de Procedimiento*/
	public static final String COD_FRM_ACTA_PROC_EF ="ACTA_PROC_EF";
	
	 
	/** Listado Fuente*/
	/** Multa*/
	public static final String COD_FRM_MULTA_GDE ="MULTA_GDE";
	
	/** Constancia de Cierre Fiscal Definitivo **/             
	public static final String COD_FRM_CONSTANCIA_CIERRE_GDE ="CONSTANCIA_GDE";

	/** Listado Fuente*/
	public static final String COD_FRM_LISTADO_FUENTE_EF ="LISTADO_FUENTE_EF";
	
	/** Listado Base Imponible*/
	public static final String COD_FRM_LISTADO_BASE_IMP_EF ="LISTADO_BASE_IMP_EF";
	
	/**  Recibo de Pago/Transferencia de cyq */
	public static final String FRM_RECIBO_PAGO_CYQ = "RECIBO_PAGO_CYQ";
	
	/** TramiteRA*/
	public static final String COD_FRM_TRAMITERA_ROD ="TRAMITERA_ROD";
	
	/** Planilla de habilitacion*/ 
	public static final String COD_FRM_HABILITACION_ESP = "PLANILLA_HAB";
	
	/** Listado de entradas habilitadas que no poseen entradas venidas, por habilitacion*/
	public static final String COD_FRM_HAB_SINENTVEN_ESP = "PLANILLA_HAB_SINVEN";
	
	/** Listado de habilitaciones que no poseen entradas venidas*/
	public static final String COD_FRM_ENTHAB_SINENTVEN_ESP = "PLANILLA_ENTHAB";
	
	/** Detalle de deuda por contribuyente*/
	public static final String COD_FRM_DEUDA_CONTRIBUYENTE = "DEUDA_CONTRIBUYENTE";
	
	/** Declaracion Jurada Masiva - Simulacion */
	public static final String COD_FRM_DECJURMAS_SIMULA = "DECJURMAS_SIMULA";
	
	/** Formulario para pre formalizacion de convenios para recursos autoliquidables. */
	public static final String COD_FORM_CONVENIO_AUTO = "FORM_CONVENIOAUTO"; 

	/** Formulario totales del envio judicial. */
	public static final String COD_TOTAL_ENVJUD = "TOTAL_ENVJUD";
	
	/** Formulario totales por procurador del envio judicial. */
	public static final String COD_TOTAL_ENVJUDPROCUR = "TOTAL_ENVJUDPROCUR"; 

	/** Reporte de Habilitacion */
	public static final String COD_FRM_HAB_REPORTE_ESP = "REPORTE_HAB";
	
	/** Reporte de total de notificaciones/boletas generadas por broche en Proceso Masivo (Notificaciones o Reconfecciones)*/
	public static final String COD_FRM_PRO_MAS_TOT_NOTIF = "PRO_MAS_TOT_NOTIF";

	/** Reportes de Cuentas con observaciones al procesar Novedades de Regimen Simplificado*/
	public static final String COD_NOVEDADRS_CTAS_INF = "NOVEDADRS_CTAS_INF";
	
	/** Reportes de Padrón de Firmas en Impresión Masiva de Deuda*/
	public static final String COD_FRM_PADRON_IMP_MASIVA = "REP_PADRON_IMP_MASIVA";
	
	@Column(name = "codFormulario")
	private String codFormulario;

	@Column(name = "desFormulario")
	private String desFormulario;

	@OneToMany( mappedBy="formulario")
	@JoinColumn(name = "idFormulario")
	@OrderBy(clause="codCampo")
	@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	private List<ForCam> listForCam;

	//@Column(name = "xsl")
	@Transient
	private String xsl;

	@Transient
	private String xslTxt;

	//@Column(name = "xml_test")
	@Transient
	private String xmlTest;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDesImp")
	private DesImp desImp;
	
	// <#Propiedades#>



	// Constructores
	public Formulario() {
		super();
		// Seteo de valores default
	}

	public Formulario(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static Formulario getById(Long id) {
		return (Formulario) FrmDAOFactory.getFormularioDAO().getById(id);
	}

	public static Formulario getByCodigo(String codigo) throws Exception{
		return (Formulario) FrmDAOFactory.getFormularioDAO().getByCodigo(codigo);
	}
	
	public static Formulario getByIdNull(Long id) {
		return (Formulario) FrmDAOFactory.getFormularioDAO().getByIdNull(id);
	}

	public static List<Formulario> getList() {
		return (ArrayList<Formulario>) FrmDAOFactory.getFormularioDAO().getList();
	}

	public static List<Formulario> getListActivos() {
		return (ArrayList<Formulario>) FrmDAOFactory.getFormularioDAO()
				.getListActiva();
	}

	public static List<Formulario> getListActivosOrdenada() throws Exception {
		return (ArrayList<Formulario>) FrmDAOFactory.getFormularioDAO().getListActivosOrdenada();
	}
	
	public static List<Formulario> getListFormularioActivoByDesImp(DesImp desImp) throws Exception {
		return (ArrayList<Formulario>) FrmDAOFactory.getFormularioDAO()
				.getListFormularioActivoByDesImp(desImp);
	}

	
	// Getters y setters
	public String getCodFormulario() {
		return codFormulario;
	}

	public void setCodFormulario(String codFormulario) {
		this.codFormulario = codFormulario;
	}

	public String getDesFormulario() {
		return desFormulario;
	}

	public void setDesFormulario(String desFormulario) {
		this.desFormulario = desFormulario;
	}

	public List<ForCam> getListForCam() {
		return listForCam;
	}

	public void setListForCam(List<ForCam> listForCam) {
		this.listForCam = listForCam;
	}

	public String getXmlTest() throws Exception {
		if (this.xmlTest == null) {
			FrmDAOFactory.getFormularioDAO().loadXmlTest(this);
		}
		return xmlTest;
	}

	public void setXmlTest(String xml_test) {
		this.xmlTest = xml_test;
	}

	public String getXsl() throws Exception {
		if (this.xsl == null) {
			FrmDAOFactory.getFormularioDAO().loadXsl(this);
		}
		return xsl;
	}

	public void setXsl(String xsl) {
		this.xsl = xsl;
	}

	public String getXslTxt() throws Exception {
		if (this.xslTxt == null) {
			FrmDAOFactory.getFormularioDAO().loadXslTxt(this);
		}
		return xslTxt;
	}

	public void setXslTxt(String xslTxt) {
		this.xslTxt = xslTxt;
	}
	
	public DesImp getDesImp() {
		return desImp;
	}

	public void setDesImp(DesImp desImp) {
		this.desImp = desImp;
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
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (StringUtil.isNullOrEmpty(getCodFormulario())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					FrmError.FORMULARIO_CODFORMULARIO);
		}

		if (StringUtil.isNullOrEmpty(getDesFormulario())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					FrmError.FORMULARIO_DESFORMULARIO);
		}

		if(getDesImp()==null || getDesImp().getId()<=0){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					FrmError.FORMULARIO_DESIMP);			
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codFormulario");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					FrmError.FORMULARIO_CODFORMULARIO);
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el Formulario. Previamente valida la activacion.
	 * 
	 */
	public void activar() throws Exception {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		FrmDAOFactory.getFormularioDAO().update(this);
	}

	/**
	 * Desactiva el Formulario. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() throws Exception {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		FrmDAOFactory.getFormularioDAO().update(this);
	}

	/**
	 * Valida la activacion del Formulario
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del Formulario
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// Administracion de ForCam
	public ForCam createForCam(ForCam forCam) throws Exception {		
		// Validaciones de negocio
		if (!forCam.validateCreate()) {
			return forCam;
		}

		FrmDAOFactory.getForCamDAO().update(forCam);	
		
		return forCam;
	}

	public ForCam deleteForCam(ForCam forCam) {
		// Validaciones de negocio
		if (!forCam.validateDelete()) {
			return forCam;
		}

		FrmDAOFactory.getForCamDAO().delete(forCam);	
		
		return forCam;
	}

	public ForCam updateForCam(ForCam forCam) throws Exception {
		// Validaciones de negocio
		if (!forCam.validateUpdate()) {
			return forCam;
		}

		FrmDAOFactory.getForCamDAO().update(forCam);	
		
		return forCam;
	}	

	/**
	 * Obtiene un PrintModel seteado para salida formatoSalida
	 * Y con un archivo de exclusion de metodos: /publico/general/reportes/default.exclude
	 */
	public static PrintModel getPrintModel(String codigo, FormatoSalida formatoSalida) throws Exception{
	
		Formulario formulario = Formulario.getByCodigo(codigo);
	
		if (formulario == null || formulario.getId() == null || formulario.getId().longValue() == 0) {
			throw new Exception("No se encontro formulario con codigo:'" + codigo + "'");
		}
		// Se crea el Print Model.
		PrintModel printModel = new PrintModel();
		
		printModel.setRenderer(formatoSalida.getId());
		printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
		printModel.setXslPdfString(formulario.getXsl());
		printModel.setXslTxtString(formulario.getXslTxt());
	
		return printModel;
	}
	
	/**
	 * Obtiene un PrintModel seteado para salida por PDF
	 * Y con un archivo de exclusion de metodos: /publico/general/reportes/default.exclude
	 */
	public static PrintModel getPrintModelForPDF(String codigo) throws Exception {
		return Formulario.getPrintModel(codigo, FormatoSalida.PDF);
	}
	
	
}
