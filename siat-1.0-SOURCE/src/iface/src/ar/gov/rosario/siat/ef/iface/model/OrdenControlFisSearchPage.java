//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del OrdenControlFis
 * 
 * @author Tecso
 *
 */
public class OrdenControlFisSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ordenControlFisSearchPageVO";
	
	private OrdenControlVO ordenControl= new OrdenControlVO();
	
	private OrigenOrdenVO origenOrdenProJud = new OrigenOrdenVO();
	
	private Date fechaEmisionDesde;
	private Date fechaEmisionHasta;
	
	private List<OrigenOrdenVO> listOrigenOrdenVO = new ArrayList<OrigenOrdenVO>();
	private List<OrigenOrdenVO> listOrigenOrdenProJud = new ArrayList<OrigenOrdenVO>();
	private List<OpeInvVO> listOpeInv = new ArrayList<OpeInvVO>();
	private List<EstadoOrdenVO> listEstadoOrdenVO = new ArrayList<EstadoOrdenVO>();
	private List<TipoOrdenVO> listTipoOrdenVO = new ArrayList<TipoOrdenVO>();

	//View properties
	private String fechaEmisionDesdeView="";
	private String fechaEmisionHastaView="";

	private boolean asignarEnabled = true;
	private boolean administrarEnabled = true;
	
	private boolean buscarInspectorBussEnabled = true;
	private boolean buscarSupervisorBussEnabled = true;
	
	// Constructores
	public OrdenControlFisSearchPage() {       
       super(EfSecurityConstants.ADM_ORDENCONTROLFIS);        
    }
	
	// Getters y Setters
    public String getName(){    
		return NAME;
	}

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public List<OrigenOrdenVO> getListOrigenOrdenVO() {
		return listOrigenOrdenVO;
	}

	public void setListOrigenOrdenVO(List<OrigenOrdenVO> listOrigenOrdenVO) {
		this.listOrigenOrdenVO = listOrigenOrdenVO;
	}

	public List<EstadoOrdenVO> getListEstadoOrdenVO() {
		return listEstadoOrdenVO;
	}

	public void setListEstadoOrdenVO(List<EstadoOrdenVO> listEstadoOrdenVO) {
		this.listEstadoOrdenVO = listEstadoOrdenVO;
	}

	public List<TipoOrdenVO> getListTipoOrdenVO() {
		return listTipoOrdenVO;
	}

	public void setListTipoOrdenVO(List<TipoOrdenVO> listTipoOrdenVO) {
		this.listTipoOrdenVO = listTipoOrdenVO;
	}

	public Date getFechaEmisionDesde() {
		return fechaEmisionDesde;
	}

	public void setFechaEmisionDesde(Date fechaEmisionDesde) {
		this.fechaEmisionDesde = fechaEmisionDesde;
		this.fechaEmisionDesdeView = DateUtil.formatDate(fechaEmisionDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaEmisionHasta() {
		return fechaEmisionHasta;
	}

	public void setFechaEmisionHasta(Date fechaEmisionHasta) {
		this.fechaEmisionHasta = fechaEmisionHasta;
		this.fechaEmisionHastaView = DateUtil.formatDate(fechaEmisionHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	
	public List<OrigenOrdenVO> getListOrigenOrdenProJud() {
		return listOrigenOrdenProJud;
	}

	public void setListOrigenOrdenProJud(List<OrigenOrdenVO> listOrigenOrdenProJud) {
		this.listOrigenOrdenProJud = listOrigenOrdenProJud;
	}

	public List<OpeInvVO> getListOpeInv() {
		return listOpeInv;
	}

	public void setListOpeInv(List<OpeInvVO> listOpeInv) {
		this.listOpeInv = listOpeInv;
	}
	
	public OrigenOrdenVO getOrigenOrdenProJud() {
		return origenOrdenProJud;
	}
	
	public void setOrigenOrdenProJud(OrigenOrdenVO origenOrdenProJud) {
		this.origenOrdenProJud = origenOrdenProJud;
	}
	
	
	
	// View getters
	public String getFechaEmisionDesdeView() {
		return fechaEmisionDesdeView;
	}

	public void setFechaEmisionDesdeView(String fechaEmisionDesdeView) {
		this.fechaEmisionDesdeView = fechaEmisionDesdeView;
	}

	public String getFechaEmisionHastaView() {
		return fechaEmisionHastaView;
	}

	public void setFechaEmisionHastaView(String fechaEmisionHastaView) {
		this.fechaEmisionHastaView = fechaEmisionHastaView;
	}


	// flag getters
	public String getAsignarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(asignarEnabled, EfSecurityConstants.ADM_ORDENCONTROLFIS, 
																	EfSecurityConstants.MTD_ASIGNAR);
	}
	
	public String getAdministrarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(administrarEnabled, EfSecurityConstants.ADM_ORDENCONTROLFIS, 
																	EfSecurityConstants.MTD_ADMINISTRAR);
	}

	
	public boolean getBuscarInspectorBussEnabled() {
		return buscarInspectorBussEnabled;
	}

	public void setBuscarInspectorBussEnabled(boolean buscarInspectorBussEnabled) {
		this.buscarInspectorBussEnabled = buscarInspectorBussEnabled;
	}

	public boolean getBuscarSupervisorBussEnabled() {
		return buscarSupervisorBussEnabled;
	}

	public void setBuscarSupervisorBussEnabled(boolean buscarSupervisorBussEnabled) {
		this.buscarSupervisorBussEnabled = buscarSupervisorBussEnabled;
	}

	

	
}
