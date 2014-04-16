//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import coop.tecso.demoda.iface.helper.StringUtil;

public class OtrIngTesRecConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private OtrIngTesVO otrIngTes = new OtrIngTesVO();
	private RecConVO recCon = new RecConVO();
	private Double importe;
	
	private String importeView = "";
	
	// Constructores 
	public OtrIngTesRecConVO(){
		super();
	}

	// Getters Y Setters
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	public OtrIngTesVO getOtrIngTes() {
		return otrIngTes;
	}
	public void setOtrIngTes(OtrIngTesVO otrIngTes) {
		this.otrIngTes = otrIngTes;
	}
	public RecConVO getRecCon() {
		return recCon;
	}
	public void setRecCon(RecConVO recCon) {
		this.recCon = recCon;
	}
	
	
}
