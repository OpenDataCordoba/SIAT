//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.PORCENTAJE;


/**
 * Descuentos Generales
 * @author tecso
 *
 */
public class DesGenVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "desGenVO";
	
	private String desDesGen;
	
	private Double porDes;
	private String porDesView;
	private CasoVO caso = new CasoVO();
	private String leyendaDesGen;

	
    // Constructores
	public DesGenVO() {
		super();
	}

	public DesGenVO(int id, String desDesGen) {
		super();
		setId(new Long(id));
		setDesDesGen(desDesGen);
	}

	// Getters y Setters
	public String getDesDesGen() {
		return desDesGen;
	}
	public void setDesDesGen(String desDesGen) {
		this.desDesGen = desDesGen;
	}
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	public Double getPorDes() {
		return porDes;
	}
	
	@PORCENTAJE
	public void setPorDes(Double porDes) {
		this.porDes = porDes;
		this.porDesView = (porDes!=null?String.valueOf(porDes):"");
	}
	public String getLeyendaDesGen() {
		return leyendaDesGen;
	}
	public void setLeyendaDesGen(String leyendaDesGen) {
		this.leyendaDesGen = leyendaDesGen;
	}

	//View getters
	public String getPorDesView() {
		return porDesView;
	}
	public void setPorDesView(String porDes) {
		this.porDesView = porDes;
	}

}