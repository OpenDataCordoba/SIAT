//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class OtrIngTesParVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	private OtrIngTesVO otrIngTes = new OtrIngTesVO();
	private PartidaVO partida = new PartidaVO();
	private Double importe;
	
	private String importeView = "";
	
	// Constructores 
	public OtrIngTesParVO(){
		super();
	}

	// Getters Y Setters
	public OtrIngTesVO getOtrIngTes() {
		return otrIngTes;
	}
	public void setOtrIngTes(OtrIngTesVO otrIngTes) {
		this.otrIngTes = otrIngTes;
	}
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
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
	
}
