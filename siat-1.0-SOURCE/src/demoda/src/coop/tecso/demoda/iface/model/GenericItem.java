//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.Collection;

/**
 * Item generico que se utiliza para representar cosas genericas
 * Se puede usar para representar un efector logueado 
 * para llenar la lista de efectores para que el usuario seleccione
 * o para llenar la lista de menu
 * @author tecso
 *
 */

public class GenericItem {

	private Long id = new Long(0);
	private Long idParent = new Long(0);
	
	private String desc1;
	private String desc2;
	private String desc3;
	private String desc4;
	private String desc5;
	
	private Collection hijos;

	public GenericItem() {
		super();
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	public String getDesc3() {
		return desc3;
	}

	public void setDesc3(String desc3) {
		this.desc3 = desc3;
	}

	public String getDesc4() {
		return desc4;
	}

	public void setDesc4(String desc4) {
		this.desc4 = desc4;
	}

	public String getDesc5() {
		return desc5;
	}

	public void setDesc5(String desc5) {
		this.desc5 = desc5;
	}

	public Collection getHijos() {
		return hijos;
	}

	public void setHijos(Collection hijos) {
		this.hijos = hijos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdParent() {
		return idParent;
	}

	public void setIdParent(Long idParent) {
		this.idParent = idParent;
	}
	

	
	
	
	
}
