//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Criterios de Asignación a Procuradores
 * @author tecso
 *
 */
public class CriAsiProVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "criAsiProVO";
	
	private String desCriAsiPro;

	// Contructores
	public CriAsiProVO(){
		super();
	}
	public CriAsiProVO(int id, String desCriAsiPro) {
		super(id);
		setDesCriAsiPro(desCriAsiPro);
	}


	// Getters y Setters
	public String getDesCriAsiPro(){
		return desCriAsiPro;
	}
	public void setDesCriAsiPro(String desCriAsiPro){
		this.desCriAsiPro = desCriAsiPro;
	}
	
}
