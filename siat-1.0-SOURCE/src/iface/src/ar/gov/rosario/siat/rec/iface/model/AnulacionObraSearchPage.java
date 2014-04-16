//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del AnulacionObra
 * 
 * @author Tecso
 *
 */
public class AnulacionObraSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "anulacionObraSearchPageVO";
	
	private AnulacionObraVO anulacionObra= new AnulacionObraVO();
	
	private List<ObraVO> listObra = new ArrayList<ObraVO>();
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();

	private Date 	fechaDesde; 
	private Date 	fechaHasta; 
	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";
	
	// Constructores
	public AnulacionObraSearchPage() {       
       super(RecSecurityConstants.ABM_ANULACIONOBRA);        
    }
	
	// Getters y Setters
	public AnulacionObraVO getAnulacionObra() {
		return anulacionObra;
	}
	public void setAnulacionObra(AnulacionObraVO anulacionObra) {
		this.anulacionObra = anulacionObra;
	}

	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}

	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
	}

	public List<ObraVO> getListObra() {
		return listObra;
	}

	public void setListObra(List<ObraVO> listObra) {
		this.listObra = listObra;
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
			(RecSecurityConstants.ABM_ANULACIONOBRA, RecSecurityConstants.MTD_ANULACIONOBRA_ADMINISTRARPROCESO);
	}
}
