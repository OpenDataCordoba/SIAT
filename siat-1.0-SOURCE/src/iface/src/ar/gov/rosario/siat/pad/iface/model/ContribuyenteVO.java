//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;

public class ContribuyenteVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	// Propiedades
	private PersonaVO persona = new PersonaVO(); 
	private Date fechaDesde;
	private Date fechaHasta;	
	private DomicilioVO domicilioFiscal = new DomicilioVO();
	private CasoVO caso = new CasoVO();
	private String nroIsib="";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private Date fechaDesdeIngBru;
	private List<ConAtrValVO>     listConAtrVal     = new ArrayList<ConAtrValVO>();	
	private List<CuentaTitularVO> listCuentaTitular = new ArrayList<CuentaTitularVO>();
	
	private Boolean imprimirListDeudaContribBussEnabled = Boolean.TRUE;
	// Constructores
	public ContribuyenteVO(){
		super();
	}
	
	// Getters y setters
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO casoDomFis) {
		this.caso = casoDomFis;
	}
	public DomicilioVO getDomicilioFiscal() {
		return domicilioFiscal;
	}
	public void setDomicilioFiscal(DomicilioVO domicilioFiscal) {
		this.domicilioFiscal = domicilioFiscal;
	}
	public PersonaVO getPersona() {
		return persona;
	}
	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);		
	}
	public List<ConAtrValVO> getListConAtrVal() {
		return listConAtrVal;
	}
	public void setListConAtrVal(List<ConAtrValVO> listConAtrVal) {
		this.listConAtrVal = listConAtrVal;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public List<CuentaTitularVO> getListCuentaTitular() {
		return listCuentaTitular;
	}
	public void setListCuentaTitular(List<CuentaTitularVO> listCuentaTitular) {
		this.listCuentaTitular = listCuentaTitular;
	}

	

	public Date getFechaDesdeIngBru() {
		return fechaDesdeIngBru;
	}

	public void setFechaDesdeIngBru(Date fechaDesdeIngBru) {
		this.fechaDesdeIngBru = fechaDesdeIngBru;
	}

	public Boolean getImprimirListDeudaContribBussEnabled() {
		return imprimirListDeudaContribBussEnabled;
	}
	public void setImprimirListDeudaContribBussEnabled(
			Boolean imprimirListDeudaContribBussEnabled) {
		this.imprimirListDeudaContribBussEnabled = imprimirListDeudaContribBussEnabled;
	}

	public String getNroIsib() {
		return nroIsib;
	}

	public void setNroIsib(String nroIsib) {
		this.nroIsib = nroIsib;
	}

	/**
	 * Devuelve una representacion de la persona correspondiente. 
	 * 
	 * @return
	 */
	public String getView(){		
		return getPersona().getView();
	}
	
	/**
	 * Devuelve una representacion de la persona correspondiente con el nro de cuit. 
	 * 
	 * @return
	 */
	public String getViewWithCuit(){		
		return getPersona().getViewWithCuit();
	}
	
	public String getFechaDesdeIngBruView(){
		return (this.getFechaDesdeIngBru()!=null)?DateUtil.formatDate(this.fechaDesdeIngBru, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
}
