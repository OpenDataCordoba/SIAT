//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Search Page de Seleccion de agregacion de Deudas Incluidas del Proceso de Envio Judicial 
 * @author tecso
 *
 */
public class DeudaExcProMasAgregarSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;
	                                   
	public static final String NAME = "deudaExcProMasAgregarSearchPageVO";
	public static final String FILE_NAME = "deudaExcProMasAgregar";
	public static final Long CTD_MAX_REG_SELECC_IND = 150L;
	
	// Envio Judicial de la deuda incluida.
	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO(); 
	
	// parametros de la deuda
	// recurso obtenido desde procesoMasivo.recurso
	// Clasificacion de la deuda:
	// la lista de RecClaDeu del recurso obviamente esta contenida en el recurso.
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>(); //lista de RecClaDeu seleccionada
	private String[] listIdRecClaDeu = {};  // String[] es lo mas adecuado para Struts
	
	private Date   fechaVencimientoDesde;
	private String fechaVencimientoDesdeView;
	private Date   fechaVencimientoHasta;
	private String fechaVencimientoHastaView;
	
	// parametros de la cuenta
	private CuentaVO cuenta = new CuentaVO();

	// contiene la lista de Ids DeudaAdmin cuando se realiza la seleccion individual
	private String[] listIdDeudaAdmin = {};  // String[] es lo mas adecuado para Struts

	// bandera que controla la habilitacion de la seleccion individual.
	// Se encuentra en el Search Page y no el envio judicial porque la deuda todavia no fue agregada 
	private Boolean agregarSeleccionIndividualBussEnabled = Boolean.TRUE;

	// Constructor
	public DeudaExcProMasAgregarSearchPage(){
		super(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO);
	}

	// Getters y Setters
	
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaVencimientoDesde() {
		return fechaVencimientoDesde;
	}
	public void setFechaVencimientoDesde(Date fechaVencimientoDesde) {
		this.fechaVencimientoDesde = fechaVencimientoDesde;
		this.fechaVencimientoDesdeView = DateUtil.formatDate(fechaVencimientoDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVencimientoDesdeView() {
		return fechaVencimientoDesdeView;
	}
	public void setFechaVencimientoDesdeView(String fechaVencimientoDesdeView) {
		this.fechaVencimientoDesdeView = fechaVencimientoDesdeView;
	}
	public Date getFechaVencimientoHasta() {
		return fechaVencimientoHasta;
	}
	public void setFechaVencimientoHasta(Date fechaVencimientoHasta) {
		this.fechaVencimientoHasta = fechaVencimientoHasta;
		this.fechaVencimientoHastaView = DateUtil.formatDate(fechaVencimientoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVencimientoHastaView() {
		return fechaVencimientoHastaView;
	}
	public void setFechaVencimientoHastaView(String fechaVencimientoHastaView) {
		this.fechaVencimientoHastaView = fechaVencimientoHastaView;
	}
	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}
	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}
	public void clearListRecClaDeu() {
		this.listRecClaDeu.clear();
	}
	public String[] getListIdRecClaDeu() {
		return listIdRecClaDeu;
	}
	public void setListIdRecClaDeu(String[] listIdRecClaDeu) {
		this.listIdRecClaDeu = listIdRecClaDeu;
	}
	public String[] getListIdDeudaAdmin() {
		return listIdDeudaAdmin;
	}
	public void setListIdDeudaAdmin(String[] listIdDeudaAdmin) {
		this.listIdDeudaAdmin = listIdDeudaAdmin;
	}
	public Boolean getAgregarSeleccionIndividualBussEnabled() {
		return agregarSeleccionIndividualBussEnabled;
	}
	public void setAgregarSeleccionIndividualBussEnabled(
			Boolean agregarSeleccionIndividualBussEnabled) {
		this.agregarSeleccionIndividualBussEnabled = agregarSeleccionIndividualBussEnabled;
	}

	public Long getCtdTotalResultados(){
		Long ctdTotalResultados = 0L;
		for (Iterator iter = this.getListResult().iterator(); iter.hasNext();) {
			PlanillaVO planilla = (PlanillaVO) iter.next();
			Long ctdResultadosPlanilla = planilla.getCtdResultados();
			if(ctdResultadosPlanilla != null){
				ctdTotalResultados += planilla.getCtdResultados();
			}
		}
		return ctdTotalResultados;
	}

	public String getCtdTotalResultadosView(){
		return StringUtil.formatLong(this.getCtdTotalResultados());
	}
	
	/**
	 * Obtiene el detalle de Logs de los filtros cargados en el SearchPage
	 * @return String
	 */
	public String getDetalleLog(){
		
		String detalleLog = "";

		// filtro Recurso requerido
		detalleLog += " Recurso: " + this.getProcesoMasivo().getRecurso().getDesRecurso();
		
		// clasificacion deuda
		// TODO obtener las descripciones a partir de los ids
		if (this.getListIdRecClaDeu() != null &&          // puede ser nula 
				this.getListIdRecClaDeu().length > 0){
			//TODO
			detalleLog += " Clasificacion Deuda: FALTA obtener las descripciones " + StringUtil.getStringComaSeparate(this.getListIdRecClaDeu());
		}
		// filtro Fecha Vencimiento Desde
 		if (this.getFechaVencimientoDesde() != null ) {
			detalleLog += " Fecha Vencimiento Desde: " + DateUtil.formatDate(this.getFechaVencimientoDesde(), DateUtil.ddSMMSYYYY_MASK);
		}
		// filtro Fecha Vencimiento Hasta
 		if (this.getFechaVencimientoHasta() != null ) {
 			detalleLog += " Fecha Vencimiento Hasta: " + DateUtil.formatDate(this.getFechaVencimientoHasta(), DateUtil.ddSMMSYYYY_MASK);
		}
 		// numero de cuenta
 		String numeroCuenta = this.getCuenta().getNumeroCuenta();
 		if (!StringUtil.isNullOrEmpty(numeroCuenta) ) {
			detalleLog += " Numero de Cuenta = '" + numeroCuenta + "'";
		}
		
		return detalleLog;
	}
	
	public DeudaExcProMasAgregarSearchPage getNewInstance(){
		DeudaExcProMasAgregarSearchPage nuevaInst = new DeudaExcProMasAgregarSearchPage();
		
		nuevaInst.setProcesoMasivo(this.getProcesoMasivo());
		nuevaInst.setListRecClaDeu(this.getListRecClaDeu());
		nuevaInst.setListIdRecClaDeu(this.getListIdRecClaDeu());
		nuevaInst.setFechaVencimientoDesde(this.getFechaVencimientoDesde());
		nuevaInst.setFechaVencimientoHasta(this.getFechaVencimientoHasta());
		nuevaInst.setCuenta(this.getCuenta());
		
		// parametros de objeto imponible
		// Para TGI y CdM: mostrar los atributos Zona y Radio del TipObjImp parcela
		// Cada uno con los checks correspondientes valores del 
		
		//nuevaInst.setListIdDeuda(this.getListIdDeuda());
		//nuevaInst.setPlanillaFiltro(this.getPlanillaFiltro());
		nuevaInst.setListIdDeudaAdmin(this.getListIdDeudaAdmin());
	
		return nuevaInst;
	}
	
	public boolean getContieneResultados(){
		Long ctdResultados = this.getCtdTotalResultados();
		return (ctdResultados != null && ctdResultados > 0);
	}

}
