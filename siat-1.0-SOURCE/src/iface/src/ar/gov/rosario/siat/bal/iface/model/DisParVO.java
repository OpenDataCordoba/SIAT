//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

public class DisParVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String desDisPar;
	private RecursoVO recurso = new RecursoVO();
	private TipoImporteVO tipoImporte = new TipoImporteVO();
	
	// Listas de Entidades Relacionadas con DisPAr
	private List<DisParDetVO> listDisParDet = new ArrayList<DisParDetVO>();
	private List<DisParRecVO> listDisParRec = new ArrayList<DisParRecVO>();;
	private List<DisParPlaVO> listDisParPla = new ArrayList<DisParPlaVO>();;

	// Buss Flags
	private Boolean asociarRecursoViaBussEnabled      = true;
	private Boolean asociarPlanBussEnabled      = true;

	// Flags
	private boolean paramTipoImporte = false;

	//Constructores 
	public DisParVO(){
		super();
	}

	// Getters y Setters
	public String getDesDisPar() {
		return desDisPar;
	}
	public void setDesDisPar(String desDisPar) {
		this.desDisPar = desDisPar;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public List<DisParDetVO> getListDisParDet() {
		return listDisParDet;
	}
	public void setListDisParDet(List<DisParDetVO> listDisParDet) {
		this.listDisParDet = listDisParDet;
	}
	public List<DisParPlaVO> getListDisParPla() {
		return listDisParPla;
	}
	public void setListDisParPla(List<DisParPlaVO> listDisParPla) {
		this.listDisParPla = listDisParPla;
	}
	public List<DisParRecVO> getListDisParRec() {
		return listDisParRec;
	}
	public void setListDisParRec(List<DisParRecVO> listDisParRec) {
		this.listDisParRec = listDisParRec;
	}
	public TipoImporteVO getTipoImporte() {
		return tipoImporte;
	}
	public void setTipoImporte(TipoImporteVO tipoImporte) {
		this.tipoImporte = tipoImporte;
	}
	public boolean isParamTipoImporte() {
		return paramTipoImporte;
	}
	public void setParamTipoImporte(boolean paramTipoImporte) {
		this.paramTipoImporte = paramTipoImporte;
	}

	
	//	 Buss flags getters y setters
	public Boolean getAsociarPlanBussEnabled() {
		return asociarPlanBussEnabled;
	}
	public void setAsociarPlanBussEnabled(Boolean asociarPlanBussEnabled) {
		this.asociarPlanBussEnabled = asociarPlanBussEnabled;
	}
	public String getAsociarPlanEnabled() {
		return this.getAsociarPlanBussEnabled() ? ENABLED : DISABLED;
	}
	public Boolean getAsociarRecursoViaBussEnabled() {
		return asociarRecursoViaBussEnabled;
	}
	public void setAsociarRecursoViaBussEnabled(Boolean asociarRecursoViaBussEnabled) {
		this.asociarRecursoViaBussEnabled = asociarRecursoViaBussEnabled;
	}
	public String getAsociarRecursoViaEnabled() {
		return this.getAsociarRecursoViaBussEnabled() ? ENABLED : DISABLED;
	}

	
}
