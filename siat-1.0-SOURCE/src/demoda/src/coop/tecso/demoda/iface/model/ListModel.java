//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.Collection;
import java.util.LinkedList;

public class ListModel extends Common {

	private BussImageModel bussModel;
	
    private Collection colModel = new LinkedList();

	public ListModel(){
		
	}
	
    public ListModel(Collection col){
		this.colModel = col;
	}
    
    public Collection getColModel() {
		return colModel;
	}

	public void setColModel(Collection colModel) {
		this.colModel = colModel;
	}

	/**
	 * @return Devuelve el atributo bussModel.
	 */
	public BussImageModel getBussModel() {
		return bussModel;
	}

	/**
	 * @param bussModel Fija el atributo bussModel.
	 */
	public void setBussModel(BussImageModel bussModel) {
		this.bussModel = bussModel;
	}

	
    
    
}
