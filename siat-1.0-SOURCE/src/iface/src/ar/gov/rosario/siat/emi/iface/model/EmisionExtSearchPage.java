//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage de Emision Extraordinaria
 * 
 * @author Tecso
 *
 */
public class EmisionExtSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "emisionExtSearchPageVO";
	
	private EmisionVO emision =  new EmisionVO();

	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private Date fechaDesde; 
	
	private Date fechaHasta; 

	private String fechaDesdeView = "";
	
	private String fechaHastaView = "";

	// Constructores
	public EmisionExtSearchPage() {
 		super(EmiSecurityConstants.ABM_EMISIONEXT);
	}
	
	// Getters y Setters
	public EmisionVO getEmision() {
		return emision;
	}
	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public final Date getFechaDesde() {
		return fechaDesde;
	}

	public final void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public final Date getFechaHasta() {
		return fechaHasta;
	}

	public final void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public final String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public final void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public final String getFechaHastaView() {
		return fechaHastaView;
	}

	public final void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}

}
