//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;



/**
 * Adapter de Modulo Aplicacion
 * 
 * @author tecso
 */
public class ModAplAdapter extends SweAdapterModel {

	public static final String NAME = "modAplAdapterVO";

    private ModAplVO modApl = new ModAplVO();

    public ModAplAdapter(){

    }

    public ModAplVO getModApl() {
		return modApl;
	}

	public void setModApl(ModAplVO modApl) {
		this.modApl = modApl;
	}
	
}
