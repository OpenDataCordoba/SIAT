//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del MotAnuDeu
 * @author tecso
 *
 */
public class MotAnuDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "motAnuDeuVO";
	
	private String desMotAnuDeu;
	
	// Constructores
	public MotAnuDeuVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MotAnuDeuVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesMotAnuDeu(desc);
	}
	
	// Getters y Setters
	public String getDesMotAnuDeu() {
		return desMotAnuDeu;
	}	
	public void setDesMotAnuDeu(String desMotAnuDeu) {
		this.desMotAnuDeu = desMotAnuDeu;
	}
	
}
