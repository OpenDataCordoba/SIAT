//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;

/**
 * Indica los valores de un atributo del objeto imponible para los cuales debe generarse la cuenta del recurso indicado.
 *
 * @author tecso
 *
 */
public class PerCobVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "PerCobVO";

	private AreaVO area = new AreaVO();
	private String nombreApellido="";
	
	
	// Constructores
	public PerCobVO(){
		super();
	}
	public PerCobVO(int id, String desc) {
		super();
		setId(new Long(id));
		setNombreApellido(desc);
	}
	
	// Getters y Setters
	
	public AreaVO getArea() {
		return area;
	}
	public void setArea(AreaVO area) {
		this.area = area;
	}
	public String getNombreApellido() {
		return nombreApellido;
	}
	public void setNombreApellido(String nombreApellido) {
		this.nombreApellido = nombreApellido;
	}
		
	

}
