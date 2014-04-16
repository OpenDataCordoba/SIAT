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
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del OpeInvCon
 * 
 * @author Tecso
 *
 */
public class OpeInvBusSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "opeInvBusSearchPageVO";
	
	private Long idOpeInv = null;
	private Integer tipBus;
	private Date fechaDesde;
	private Date fechaHasta;
	
	private EstadoCorridaVO estadoCorrida = new EstadoCorridaVO();
	
	private List<EstadoCorridaVO> listEstadoCorridaVO = new ArrayList<EstadoCorridaVO>();
	
	private String fechaDesdeView="";
	private String fechaHastaView="";
	
	// Constructores
	public OpeInvBusSearchPage() {       
       super(EfSecurityConstants.ADM_OPEINVCON);        
    }

	// Getters y Setters
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

	public EstadoCorridaVO getEstadoCorrida() {
		return estadoCorrida;
	}

	public void setEstadoCorrida(EstadoCorridaVO estadoCorrida) {
		this.estadoCorrida = estadoCorrida;
	}

	public List<EstadoCorridaVO> getListEstadoCorridaVO() {
		return listEstadoCorridaVO;
	}

	public void setListEstadoCorridaVO(List<EstadoCorridaVO> listEstadoCorridaVO) {
		this.listEstadoCorridaVO = listEstadoCorridaVO;
	}


	
	public Integer getTipBus() {
		return tipBus;
	}

	public void setTipBus(Integer tipBus) {
		this.tipBus = tipBus;
	}

	public Long getIdOpeInv() {
		return idOpeInv;
	}

	public void setIdOpeInv(Long idOpeInv) {
		this.idOpeInv = idOpeInv;
	}

	// View getters
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
	
	
	public String getAdministrarProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(EfSecurityConstants.ABM_OPEINVBUS, EfSecurityConstants.MTD_ADM_PROCESO);
	}	

	
	
}
