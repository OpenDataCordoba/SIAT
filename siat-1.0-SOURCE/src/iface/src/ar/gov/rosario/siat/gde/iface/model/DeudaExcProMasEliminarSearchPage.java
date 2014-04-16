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
 * Search Page de Seleccion de eliminacion de Deudas a Excluir del Proceso de Envio Judicial 
 * @author tecso
 *
 */
public class DeudaExcProMasEliminarSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "deudaExcProMasEliminarSearchPageVO";
	public static final String FILE_NAME = "deudaExcProMasEliminar";
	
	// Envio Judicial de la deuda incluida.
	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO(); 
	
	// parametros de la deuda
	// recurso obtenido desde procesoMasivo.recurso
	// Clasificacion de la deuda:
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	private String[] listIdRecClaDeu = {};  // String[] es lo mas adecuado para Struts
	
	
	private Date   fechaVencimientoDesde;
	private String fechaVencimientoDesdeView;
	private Date   fechaVencimientoHasta;
	private String fechaVencimientoHastaView;

	// parametros de la cuenta
	private CuentaVO cuenta = new CuentaVO();

	private PlanillaVO planillaFiltro = new PlanillaVO();
	
	// contiene la lista de Sel alm det cuando se realiza la seleccion individual
	private List<SelAlmDetVO> listSelAlmDet = new ArrayList<SelAlmDetVO>();
	
	// contiene la lista de Ids SelAlmDet cuando se realiza la seleccion individual
	private String[] listIdSelAlmDet = {};  // String[] es lo mas adecuado para Struts
	
	
	public DeudaExcProMasEliminarSearchPage(){
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
	public List<SelAlmDetVO> getListSelAlmDet() {
		return listSelAlmDet;
	}
	public void setListSelAlmDet(List<SelAlmDetVO> listSelAlmDet) {
		this.listSelAlmDet = listSelAlmDet;
	}
	public PlanillaVO getPlanillaFiltro() {
		return planillaFiltro;
	}
	public void setPlanillaFiltro(PlanillaVO planillaFiltro) {
		this.planillaFiltro = planillaFiltro;
	}
	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}
	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}
	public String[] getListIdRecClaDeu() {
		return listIdRecClaDeu;
	}
	public void setListIdRecClaDeu(String[] listIdRecClaDeu) {
		this.listIdRecClaDeu = listIdRecClaDeu;
	}
	public String[] getListIdSelAlmDet() {
		return listIdSelAlmDet;
	}
	public void setListIdSelAlmDet(String[] listIdSelAlmDet) {
		this.listIdSelAlmDet = listIdSelAlmDet;
	}

	public String getCtdTotalResultadosView(){
		Long ctdTotalResultados = 0L;
		for (Iterator iter = this.getListResult().iterator(); iter.hasNext();) {
			PlanillaVO planilla = (PlanillaVO) iter.next();
			ctdTotalResultados += planilla.getCtdResultados();
		}
		return StringUtil.formatLong(ctdTotalResultados);
	}
	
	public String getDetalleLog(){
		
		String detalleLog = "";
	    
	    /* estado activo?
		// Armamos filtros del HQL
		if (this.getModoSeleccionar()) {
		  detalleLog += flagAnd ? " and " : " where ";
	      detalleLog += " d.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		*/
	    
		// filtro Recurso requerido
		detalleLog += " Recurso: " + this.getProcesoMasivo().getRecurso().getDesRecurso();
		
		// clasificacion deuda
		// TODO obtener las descripciones a partir de los ids
		if (this.getListIdRecClaDeu() != null &&          // puede ser nula 
				this.getListIdRecClaDeu().length > 0){
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
		
 		// TODO SEGUIR A MEDIDA QUE SE AGREGUEN MAS FILTROS
		return detalleLog;
	}
}
