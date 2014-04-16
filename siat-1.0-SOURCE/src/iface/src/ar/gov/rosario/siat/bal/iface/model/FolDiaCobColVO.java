//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class FolDiaCobColVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Double importe;
	private TipoCobVO tipoCob = new TipoCobVO();
	private FolDiaCobVO folDiaCob = new FolDiaCobVO();

	private String importeView = "";
	
	//Constructores 
	public FolDiaCobColVO(){
		super();
	}

	// Getters Y Setters
	public FolDiaCobVO getFolDiaCob() {
		return folDiaCob;
	}
	public void setFolDiaCob(FolDiaCobVO folDiaCob) {
		this.folDiaCob = folDiaCob;
	}
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
	public TipoCobVO getTipoCob() {
		return tipoCob;
	}
	public void setTipoCob(TipoCobVO tipoCob) {
		this.tipoCob = tipoCob;
	}
	
}
