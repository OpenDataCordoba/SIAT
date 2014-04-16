//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.afi.buss.bean.ActLoc;
import ar.gov.rosario.siat.afi.buss.bean.DatosDomicilio;
import ar.gov.rosario.siat.afi.buss.bean.DatosPagoCta;
import ar.gov.rosario.siat.afi.buss.bean.DecActLoc;
import ar.gov.rosario.siat.afi.buss.bean.EstForDecJur;
import ar.gov.rosario.siat.afi.buss.bean.ExeActLoc;
import ar.gov.rosario.siat.afi.buss.bean.ForDecJur;
import ar.gov.rosario.siat.afi.buss.bean.HabLoc;
import ar.gov.rosario.siat.afi.buss.bean.Local;
import ar.gov.rosario.siat.afi.buss.bean.OtrosPagos;
import ar.gov.rosario.siat.afi.buss.bean.RetYPer;
import ar.gov.rosario.siat.afi.buss.bean.Socio;
import ar.gov.rosario.siat.afi.buss.bean.TotDerYAccDJ;
import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FormularioAfip;

/**
 * Bean correspondiente a Tipo Operacion de transacciones de Osiris 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tranAfip")
public class TranAfip extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TranAfip.class);
/*	
	public static final int FORMULARIO_DJ_Y_PAGO_DREI_6050 = 6050; // 6031; Modificado por doc. enviado por afip (ver Mantis 4948)
	public static final int FORMULARIO_DJ_DREI_6062 = 6062; // 6032;  Modificado por doc. enviado por afip (ver Mantis 4948)
	public static final int FORMULARIO_PAGO_DREI_6052 = 6052; // 6033;  Modificado por doc. enviado por afip (ver Mantis 4948)
	public static final int FORMULARIO_MULTA_DREI_6053 = 6053; // 6034;  Modificado por doc. enviado por afip (ver Mantis 4948)
	public static final int FORMULARIO_DJ_Y_PAGO_ETUR_6054 = 6054; // 6035; Modificado por doc. enviado por afip (ver Mantis 4948)
	public static final int FORMULARIO_DJ_ETUR_6055 = 6055; // 6036;  Modificado por doc. enviado por afip (ver Mantis 4948)
	public static final int FORMULARIO_PAGO_ETUR_6056 = 6056; // 6037;  Modificado por doc. enviado por afip (ver Mantis 4948)
	public static final int FORMULARIO_REGIMEN_SIMPLIFICADO_6057 = 6057;
*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idEnvioOsiris")
	private EnvioOsiris envioOsiris;
	
	@Column(name="idTransaccionAfip")
	private Long idTransaccionAfip;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTipoOperacion")
	private TipoOperacion tipoOperacion;
		
	@Column(name="formulario")
	private Integer formulario;
	
	@Column(name="fechaProceso")
	private Date fechaProceso;
	
	@Column(name="fechaAnulacion")
	private Date fechaAnulacion;
	
	@Column(name="totMontoIngresado")
	private Double totMontoIngresado;
	
	@Column(name="cuit")
	private String cuit;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCierreBanco")
	private CierreBanco cierreBanco;
	
	@Column(name="observacion")
	private String observacion;
	
	@Column(name="canPago")
	private Integer canPago;
	
	@Column(name="canDecJur")
	private Integer canDecJur;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstTranAfip")
	private EstTranAfip estTranAfip;
	
	@Column(name="vep")
	private String vep;

	@Column(name="cumur")
	private String cumur;
	
	@Column(name="nroTran")
	private Long nroTran;

	@Column(name="nroTranPres")
	private Long nroTranPres;
	
	@Column(name="nroCheque")
	private Integer nroCheque;
	
	@Column(name="ctaCteCheque")
	private Integer ctaCteCheque;
	
	@Column(name="bancoCheque")
	private Integer bancoCheque;
	
	@Column(name="sucursalCheque")
	private Integer sucursalCheque;
	
	@Column(name="codPostalCheque")
	private String codPostalCheque;
	
	@Column(name="fechaAcredCheque")
	private Date fechaAcredCheque;
	
	@OneToMany(mappedBy="tranAfip",fetch=FetchType.LAZY)
	@JoinColumn(name="idTranAfip")
	private List<DetallePago> listDetallePago;
	
	@OneToMany(mappedBy="tranAfip",fetch=FetchType.LAZY)
	@JoinColumn(name="idTranAfip")
	@OrderBy("fila,registro")
	private List<DetalleDJ> listDetalleDJ;
	
	@Transient //Lista de advertencias usadas para generar logs
	private List<String> listAdvertencias = new ArrayList<String>();
	
	// Constructores
	public TranAfip(){
		super();
	}
	
	//Metodos de clase
	public static TranAfip getById(Long id) {
		return (TranAfip) BalDAOFactory.getTranAfipDAO().getById(id);  
	}
	
	public static TranAfip getByIdNull(Long id) {
		return (TranAfip) BalDAOFactory.getTranAfipDAO().getByIdNull(id);
	}
	
	public static List<TranAfip> getList() {
		return (ArrayList<TranAfip>) BalDAOFactory.getTranAfipDAO().getList();
	}
	
	public static List<TranAfip> getListActivos() {			
		return (ArrayList<TranAfip>) BalDAOFactory.getTranAfipDAO().getListActiva();
	}
	
	//Getters y Setters
	public EnvioOsiris getEnvioOsiris() {
		return envioOsiris;
	}


	public void setEnvioOsiris(EnvioOsiris envioOsiris) {
		this.envioOsiris = envioOsiris;
	}


	public Long getIdTransaccionAfip() {
		return idTransaccionAfip;
	}


	public void setIdTransaccionAfip(Long idTransaccionAfip) {
		this.idTransaccionAfip = idTransaccionAfip;
	}


	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}


	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Integer getFormulario() {
		return formulario;
	}


	public void setFormulario(Integer formulario) {
		this.formulario = formulario;
	}


	public Date getFechaProceso() {
		return fechaProceso;
	}


	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}


	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}


	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}


	public Double getTotMontoIngresado() {
		return totMontoIngresado;
	}


	public void setTotMontoIngresado(Double totMontoIngresado) {
		this.totMontoIngresado = totMontoIngresado;
	}


	public String getCuit() {
		return cuit;
	}


	public void setCuit(String cuit) {
		this.cuit = cuit;
	}


	public CierreBanco getCierreBanco() {
		return cierreBanco;
	}


	public void setCierreBanco(CierreBanco cierreBanco) {
		this.cierreBanco = cierreBanco;
	}

	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public Integer getCanPago() {
		return canPago;
	}


	public void setCanPago(Integer canPago) {
		this.canPago = canPago;
	}


	public Integer getCanDecJur() {
		return canDecJur;
	}


	public void setCanDecJur(Integer canDecJur) {
		this.canDecJur = canDecJur;
	}


	public EstTranAfip getEstTranAfip() {
		return estTranAfip;
	}


	public void setEstTranAfip(EstTranAfip estTranAfip) {
		this.estTranAfip = estTranAfip;
	}	

	
	public String getVep() {
		return vep;
	}


	public void setVep(String vep) {
		this.vep = vep;
	}
	
	public List<DetallePago> getListDetallePago() {
		return listDetallePago;
	}

	public void setListDetallePago(List<DetallePago> listDetallePago) {
		this.listDetallePago = listDetallePago;
	}

	public String getCumur() {
		return cumur;
	}


	public void setCumur(String cumur) {
		this.cumur = cumur;
	}

	public void setSucursalCheque(Integer sucursalCheque) {
		this.sucursalCheque = sucursalCheque;
	}

	public Integer getSucursalCheque() {
		return sucursalCheque;
	}

	public Long getNroTran() {
		return nroTran;
	}

	public void setNroTran(Long nroTran) {
		this.nroTran = nroTran;
	}

	public Long getNroTranPres() {
		return nroTranPres;
	}

	public void setListAdvertencias(List<String> listAdvertencias) {
		this.listAdvertencias = listAdvertencias;
	}


	public List<String> getListAdvertencias() {
		return listAdvertencias;
	}


	public void setNroTranPres(Long nroTranPres) {
		this.nroTranPres = nroTranPres;
	}
	
	public void setListDetalleDJ(List<DetalleDJ> listDetalleDJ) {
		this.listDetalleDJ = listDetalleDJ;
	}


	public List<DetalleDJ> getListDetalleDJ() {
		return listDetalleDJ;
	}
	
	
	public Integer getNroCheque() {
		return nroCheque;
	}


	public void setNroCheque(Integer nroCheque) {
		this.nroCheque = nroCheque;
	}


	public Integer getCtaCteCheque() {
		return ctaCteCheque;
	}


	public void setCtaCteCheque(Integer ctaCteCheque) {
		this.ctaCteCheque = ctaCteCheque;
	}


	public Integer getBancoCheque() {
		return bancoCheque;
	}


	public void setBancoCheque(Integer bancoCheque) {
		this.bancoCheque = bancoCheque;
	}


	public String getCodPostalCheque() {
		return codPostalCheque;
	}


	public void setCodPostalCheque(String codPostalCheque) {
		this.codPostalCheque = codPostalCheque;
	}


	public Date getFechaAcredCheque() {
		return fechaAcredCheque;
	}


	public void setFechaAcredCheque(Date fechaAcredCheque) {
		this.fechaAcredCheque = fechaAcredCheque;
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

		/*
		 * Validaciones para no borrar tranAfip si hay DJ o DP.
		 * 
		if (GenericDAO.hasReference(this, DetallePago.class, "tranAfip")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							BalError.TRANAFIP_LABEL,BalError.DETALLEPAGO_LABEL );
		}
		
		if (GenericDAO.hasReference(this, DetalleDJ.class, "tranAfip")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							BalError.TRANAFIP_LABEL,BalError.DETALLEDJ_LABEL );
		}
		*/
		
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

	
	public Recurso getRecursoSegunFormulario(){
		String cr = getCodRecursoSegunFormulario();
		
		if (cr == null)
			return null;
		
		if (cr.equals(Recurso.COD_RECURSO_DReI))
			return Recurso.getDReI();
		
		if (cr.equals(Recurso.COD_RECURSO_ETuR))
			return Recurso.getETur();
		
		return null;
	}

	public String getCodRecursoSegunFormulario(){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		if(this.formulario ==null) {
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return null;
		}
		
		int frm = this.formulario.intValue();
		if (frm == FormularioAfip.DREI_PRES_Y_PAGO_6050.getId()
				|| frm == FormularioAfip.DREI_PRES_Y_PAGO_WEB_6058.getId()
			    || frm == FormularioAfip.DREI_SOLO_PAGO_6052.getId()
			    || frm == FormularioAfip.DREI_SOLO_PAGO_6063_BETA.getId()
			    || frm == FormularioAfip.DREI_SOLO_PRES_6062.getId()
			    || frm == FormularioAfip.RS_6057.getId()) {

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return Recurso.COD_RECURSO_DReI;
		} else if (frm == FormularioAfip.ETUR_PRES_Y_PAGO_6054.getId()
				|| frm == FormularioAfip.ETUR_PRES_Y_PAGO_WEB_6059.getId()
			    || frm == FormularioAfip.ETUR_SOLO_PAGO_6056.getId()
			    || frm == FormularioAfip.ETUR_SOLO_PAGO_6064_BETA.getId()
			    || frm == FormularioAfip.ETUR_SOLO_PRES_6055.getId()) {

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return Recurso.COD_RECURSO_ETuR;
		} else {
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return null;
		}
	}
	
	/**
	 * 
	 * Validaciones previas a la generación del Formulario de Declaraciones Juradas AFIP para la transaccion.  
	 *
	 */
	public void validar() throws Exception{
		// Si la transaccion se encuentra anulada se sale sin procesar
		if(this.getFechaAnulacion() != null){
			// Ver si se agrega un error o log para el proceso masivo de decJur de un envio
			//this.addRecoverableValueError("TranAfip de id: "+this.getId()+". .");
			// TODO VER BIEN ESTE CASO. SE DEBERIA IGNORAR LA TRANSACCION. QUIZA NUNCA LLEGAR HASTA ESTE PUNTO FILTRANDO LA LISTA DE TRANSACCIONES SOBRE LA QUE SE TRABAJA
			return;
		}
		// Si no corresponde a un formulario con DJ no se genera ninguna Declaracion Jurada
		if(this.getFormulario().intValue() != FormularioAfip.DREI_SOLO_PRES_6062.getId() && this.getFormulario().intValue() != FormularioAfip.DREI_PRES_Y_PAGO_6050.getId()
				&& this.getFormulario().intValue() != FormularioAfip.ETUR_SOLO_PRES_6055.getId() && this.getFormulario().intValue() != FormularioAfip.ETUR_PRES_Y_PAGO_6054.getId()) {
			// TODO VER BIEN ESTE CASO. SE DEBERIA IGNORAR LA TRANSACCION. QUIZA NUNCA LLEGAR HASTA ESTE PUNTO FILTRANDO LA LISTA DE TRANSACCIONES SOBRE LA QUE SE TRABAJA
			return;
		}
		// Si no posee detalle de DJ se sale sin procesar
		if(this.getCanDecJur().intValue() <= 0){
			this.addRecoverableValueError("TranAfip de id: "+this.getId()+". Transacción inconsistente. No se encuentra detalle de Declaración Jurada asociada.");
			return;
		}	
	}
	
	/**
	 * 
	 * Genera el Formulario de Declaraciones Juradas AFIP para la transaccion. Luego a partir de este se generan las declaraciones juradas Siat.  
	 *
	 */
	public ForDecJur generarForDecJur() throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
				
		ForDecJur forDecJur = new ForDecJur();
		forDecJur.setEstado(Estado.ACTIVO.getId());
		forDecJur.setTranAfip(this);
		forDecJur.setEnvioOsiris(this.getEnvioOsiris());
		forDecJur.setEstForDecJur(EstForDecJur.getById(EstForDecJur.ID_SIN_PROCESAR));
		forDecJur.setMapLocal(new HashMap<String, Local>()); 
		forDecJur.setMapActLoc(new HashMap<String, ActLoc>());
		forDecJur.setMapDecActLoc(new HashMap<String, DecActLoc>());
		
		boolean hasRegistro1 = false;
		for(DetalleDJ detalleDJ: this.getListDetalleDJ()){
			switch (detalleDJ.getRegistro()) {
	        case 1: // Cabecera Encriptada  
	        	detalleDJ.prepararForDecJur(forDecJur);
	        	hasRegistro1 = true;
	        	break;
	        case 2: // Datos Generales de la Empresa
	        	detalleDJ.cargarDatosEmpresa(forDecJur);
	    		break;
			case 3: // Datos de Convenio
				detalleDJ.cargarDatosConvenio(forDecJur);
				break;
			case 4: // Socios
				detalleDJ.agregarSocio(forDecJur);
				break;
			case 5: // Firmantes
				detalleDJ.agregarSocio(forDecJur);
				break;
			case 6: // Locales 
				detalleDJ.agregarLocal(forDecJur);
				break;	
			case 7: // Habilitaciones de los Locales
				detalleDJ.agregarHabLoc(forDecJur);
				break;
			case 8: // Actividades de los Locales
				detalleDJ.agregarActLoc(forDecJur);
				break;
			case 9: // Exenciones de las Actividades 
				detalleDJ.agregarExeActLoc(forDecJur);
				break;
			case 10: // Otros Pagos
				detalleDJ.agregarOtrosPagos(forDecJur);
				break;
			case 11: // Declaracion de Actividades por Local
				detalleDJ.agregarDecActLocDrei(forDecJur);
				break;
			case 12: // Totales por Local de las Actividades Declaradas
				detalleDJ.cargarTotalesParaLocalDrei(forDecJur);
				break;
			case 13: // Retenciones y Percepciones 
				detalleDJ.agregarRetYPer(forDecJur);
				break;
			case 14: // Ajuste Base Imponible por cambio de Coeficiente por local
				detalleDJ.cargarAjuBasImpParaActLoc(forDecJur);
				break;
			case 15: // Liquidacion de DJ Mensual DREI 
				detalleDJ.cargarLiqDJMensualDrei(forDecJur);
				break;
			case 16: // Declaracion de Actividades ETUR por Local
				detalleDJ.agregarDecActLocEtur(forDecJur);
				break;
			case 17: // Totales por Local de las Actividades Declaradas ETUR
				detalleDJ.cargarTotalesParaLocalEtur(forDecJur);
				break;
			case 18: // Liquidacion de DJ Mensual ETUR
				detalleDJ.cargarLiqDJMensualEtur(forDecJur);
				break;
			case 20: // Datos de Domicilios 
				detalleDJ.agregarDomicilio(forDecJur);
				break;
			case 96: // Datos de pago por Cuenta 
				detalleDJ.agregarPagoCuenta(forDecJur);
				break;
			case 98: // Totales de Derecho y Accesorios de la DJ
				detalleDJ.agregarTotalesDerecho(forDecJur);
				break;
	        default: // Todos los registros restantes
				break;
			}
		}
	
		//Validaciones de negocio
		if (!hasRegistro1) {
			forDecJur.addRecoverableError("No se pudo identificar el Recurso a partir de los DetallesDJ asociados a esta transaccion");
		}
		
		if (forDecJur.hasError()) {
			if (log.isDebugEnabled()) log.error(funcName + ": exit");
			return forDecJur;
		}
		
		// Persistir datos cargados
		AfiDAOFactory.getForDecJurDAO().update(forDecJur);
		
		if(forDecJur.getListRetYPer() != null){
			for(RetYPer retYPer: forDecJur.getListRetYPer())
				AfiDAOFactory.getRetYPerDAO().update(retYPer);			
		}
		if(forDecJur.getListTotDerYAccDJ() != null){
			for(TotDerYAccDJ totDerYAccDJ: forDecJur.getListTotDerYAccDJ())
				AfiDAOFactory.getTotDerYAccDJDAO().update(totDerYAccDJ);			
		}
		if(forDecJur.getListDatosDomicilio() != null){
			for(DatosDomicilio datosDomicilio: forDecJur.getListDatosDomicilio())
				AfiDAOFactory.getDatosDomicilioDAO().update(datosDomicilio);			
		}
		if(forDecJur.getListSocio() != null){
			for(Socio socio: forDecJur.getListSocio())
				AfiDAOFactory.getSocioDAO().update(socio);			
		}
		if(forDecJur.getListLocal() != null){
			for(Local local: forDecJur.getListLocal()){
				AfiDAOFactory.getLocalDAO().update(local);
				
				if(local.getListHabLoc() != null){
					for(HabLoc habLoc: local.getListHabLoc())
						AfiDAOFactory.getHabLocDAO().update(habLoc);					
				}
				if(local.getListActLoc() != null){
					for(ActLoc actLoc: local.getListActLoc()){
						AfiDAOFactory.getActLocDAO().update(actLoc);
						if(actLoc.getListExeActLoc() != null){
							for(ExeActLoc exeActLoc: actLoc.getListExeActLoc())
								AfiDAOFactory.getExeActLocDAO().update(exeActLoc);							
						}
					}					
				}
				if(local.getListDecActLoc() != null){
					for(DecActLoc decActLoc: local.getListDecActLoc())
						AfiDAOFactory.getDecActLocDAO().update(decActLoc);					
				}
				if(local.getListDatosPagoCta() != null){
					for(DatosPagoCta datosPagoCta: local.getListDatosPagoCta())
						AfiDAOFactory.getDatosPagoCtaDAO().update(datosPagoCta);					
				}
				if(local.getListOtrosPagos() != null){
					for(OtrosPagos otrosPagos: local.getListOtrosPagos())
						AfiDAOFactory.getOtrosPagosDAO().update(otrosPagos);					
				}
			}			
		}
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return forDecJur;
	}

	/*
	 * Metedos delete de detalles
	 */
	
	public DetalleDJ deleteDetalleDJ(DetalleDJ detalleDJ) throws Exception {
		
		// Validaciones de negocio
		if (!detalleDJ.validateDelete()) {
			return detalleDJ;
		}
		
		BalDAOFactory.getDetalleDJDAO().delete(detalleDJ);
		
		return detalleDJ;
	}
	
	public DetallePago deleteDetallePago(DetallePago detallePago) throws Exception {
		
		// Validaciones de negocio
		if (!detallePago.validateDelete()) {
			return detallePago;
		}
		
		BalDAOFactory.getDetallePagoDAO().delete(detallePago);
		
		return detallePago;
	}


}
