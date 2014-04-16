//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;


/**
 * Tipo Objeto Imponible Area Origen
 * @author tecso
 *
 */
public class TipObjImpAreOVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipObjImpAreOVO";

	private TipObjImpVO  tipObjImp = new TipObjImpVO();
	private AreaVO areaOrigen  = new AreaVO();

	// Buss Flags
	
	// View Constants
	
	// Constructores
	public TipObjImpAreOVO() {
		super();
	}

	public TipObjImpAreOVO(TipObjImpVO tipObjImp ) {
		super();
		this.tipObjImp = tipObjImp;
	}
	
	//	 Getters y Setters	
	public AreaVO getAreaOrigen() {
		return areaOrigen;
	}
	public void setAreaOrigen(AreaVO areaOrigen) {
		this.areaOrigen = areaOrigen;
	}
	public TipObjImpVO getTipObjImp() {
		return tipObjImp;
	}
	public void setTipObjImp(TipObjImpVO tipObjImp) {
		this.tipObjImp = tipObjImp;
	}
	
	// View flags getters
	
	// View getters
	
}
