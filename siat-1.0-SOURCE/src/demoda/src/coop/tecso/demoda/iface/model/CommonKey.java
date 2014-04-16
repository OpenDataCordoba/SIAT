//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;


/**
 * @author tecso:
 * Este objeto solo posee un ID. 
 * Es generico y sirve para todos los casos que se necesite enviar algun ID como argumento
 * @version 2.0
 */
public class CommonKey extends Common {
	static final long serialVersionUID = 0;
	
	
	public CommonKey(String id) {
		this.setId(new Long(id));
	}
	
	public CommonKey(Long id) {
		this.setId(id);
	}
	
	
}
