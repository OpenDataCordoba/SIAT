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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.FormularioAfip;

/**
 * Bean correspondiente al Log Transaccion Afip de Osiris 
 * 
 * @author tecso
 */

@Entity
@Table(name = "log_bal_tranAfip")
public class LogTranAfip extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LogTranAfip.class);

	//idbaltranafip integer not null ,

    //tipo_mov char(1),
    //usuario_mdf char(10),
    //fecha_hora_mdf datetime year to second
    
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
	
	@OneToMany(mappedBy="logTranAfip",fetch=FetchType.LAZY)
	@JoinColumn(name="idTranAfip")
	private List<LogDetallePago> listLogDetallePago;
	
	@OneToMany(mappedBy="logTranAfip",fetch=FetchType.LAZY)
	@JoinColumn(name="idTranAfip")
	@OrderBy("fila,registro")
	private List<LogDetalleDJ> listLogDetalleDJ;
	
	@Transient //Lista de advertencias usadas para generar logs
	private List<String> listAdvertencias = new ArrayList<String>();
	
	// Constructores
	public LogTranAfip(){
		super();
	}
	
	//Metodos de clase
	public static LogTranAfip getById(Long id) {
		return (LogTranAfip) BalDAOFactory.getLogTranAfipDAO().getById(id);  
	}
	
	public static LogTranAfip getByIdNull(Long id) {
		return (LogTranAfip) BalDAOFactory.getLogTranAfipDAO().getByIdNull(id);
	}
	
	public static List<LogTranAfip> getList() {
		return (ArrayList<LogTranAfip>) BalDAOFactory.getLogTranAfipDAO().getList();
	}
	
	public static List<LogTranAfip> getListActivos() {			
		return (ArrayList<LogTranAfip>) BalDAOFactory.getLogTranAfipDAO().getListActiva();
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
	
	public List<LogDetallePago> getListLogDetallePago() {
		return listLogDetallePago;
	}

	public void setListLogDetallePago(List<LogDetallePago> listLogDetallePago) {
		this.listLogDetallePago = listLogDetallePago;
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
	
	public void setListLogDetalleDJ(List<LogDetalleDJ> listLogDetalleDJ) {
		this.listLogDetalleDJ = listLogDetalleDJ;
	}

	public List<LogDetalleDJ> getListLogDetalleDJ() {
		return listLogDetalleDJ;
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
		
		if (GenericDAO.hasReference(this, LogDetallePago.class, "logTranAfip")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							BalError.TRANAFIP_LABEL,BalError.DETALLEPAGO_LABEL );
		}
		
		if (GenericDAO.hasReference(this, LogDetalleDJ.class, "logTranAfip")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							BalError.TRANAFIP_LABEL,BalError.DETALLEDJ_LABEL );
		}
		
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


}
