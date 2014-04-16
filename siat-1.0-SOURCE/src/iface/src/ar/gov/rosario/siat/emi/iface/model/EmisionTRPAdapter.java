//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.SituacionInmuebleVO;

public class EmisionTRPAdapter extends SiatAdapterModel {
		 
	private static final long serialVersionUID = 1L;

	public static final String NAME	= "emisionTRPAdapterVO";
	
	private EmisionVO emision = new EmisionVO();
	
	private String nroExpediente;
	
	private Double importe;
	
	private String recibo1 = "";
	
	private String recibo2 = "";
	
	private String visacionPrevia;
	
	private String aplicaAjuste;
	
	private String descVisacionPrevia;
	
	private SituacionInmuebleVO situacionInmueble = new SituacionInmuebleVO();
	
	private List<SituacionInmuebleVO> listSituacionInmueble = new ArrayList<SituacionInmuebleVO>();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<PlanoDetalleVO> listPlanoDetalle = new ArrayList<PlanoDetalleVO>();
	
	private String importeView = "";
	
	// Constructor
	public EmisionTRPAdapter() {
	}
		
	// Getters y Setters
	public String getNroExpediente() {
		return nroExpediente;
	}
	
	public void setNroExpediente(String nroExpediente) {
		this.nroExpediente = nroExpediente;
	}

	public double getImporte() {
		return importe;
	}


	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getVisacionPrevia() {
		return visacionPrevia;
	}

	public void setVisacionPrevia(String visacionPrevia) {
		this.visacionPrevia = visacionPrevia;
	}

	public String getAplicaAjuste() {
		return aplicaAjuste;
	}

	public void setAplicaAjuste(String aplicaAjuste) {
		this.aplicaAjuste = aplicaAjuste;
	}

	public String getDescVisacionPrevia() {
		return descVisacionPrevia;
	}

	public void setDescVisacionPrevia(String descVisacionPrevia) {
		this.descVisacionPrevia = descVisacionPrevia;
	}

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

	public List<PlanoDetalleVO> getListPlanoDetalle() {
		return listPlanoDetalle;
	}

	public void setListPlanoDetalle(List<PlanoDetalleVO> listPlanoDetalle) {
		this.listPlanoDetalle = listPlanoDetalle;
	}

	public List<SituacionInmuebleVO> getListSituacionInmueble() {
		return listSituacionInmueble;
	}

	public void setListSituacionInmueble(
			List<SituacionInmuebleVO> listSituacionInmueble) {
		this.listSituacionInmueble = listSituacionInmueble;
	}

	public SituacionInmuebleVO getSituacionInmueble() {
		return situacionInmueble;
	}

	public void setSituacionInmueble(SituacionInmuebleVO situacionInmueble) {
		this.situacionInmueble = situacionInmueble;
	}

	public String getRecibo1() {
		return recibo1;
	}

	public void setRecibo1(String recibo1) {
		this.recibo1 = recibo1;
	}

	public String getRecibo2() {
		return recibo2;
	}

	public void setRecibo2(String recibo2) {
		this.recibo2 = recibo2;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public String getImporteView() {
		return importeView;
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public void clearCheckBoxs() {
		this.setVisacionPrevia(null);
		this.setDescVisacionPrevia(null);
		this.setAplicaAjuste(null);
	}
}

