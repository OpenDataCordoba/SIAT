//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class TranArcVO extends SiatBussImageModel {
	private static final long serialVersionUID = 1L;
	
	private ArchivoVO archivo = new ArchivoVO();
	private String linea;
	private Long nroLinea;
	private Double importe;
	
	private String importeView = "";
	private String nroLineaView = "";
	
	// Constructores 
	public TranArcVO(){
		super();
	}

	// Getters Y Setters
	public ArchivoVO getArchivo() {
		return archivo;
	}
	public void setArchivo(ArchivoVO archivo) {
		this.archivo = archivo;
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
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	public Long getNroLinea() {
		return nroLinea;
	}
	public void setNroLinea(Long nroLinea) {
		this.nroLinea = nroLinea;
		this.nroLineaView = StringUtil.formatLong(nroLinea);
	}
	public String getNroLineaView() {
		return nroLineaView;
	}
	public void setNroLineaView(String nroLineaView) {
		this.nroLineaView = nroLineaView;
	}
	
	
	
}
