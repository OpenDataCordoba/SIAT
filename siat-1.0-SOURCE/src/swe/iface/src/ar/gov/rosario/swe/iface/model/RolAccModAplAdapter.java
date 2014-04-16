//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;



public class RolAccModAplAdapter extends SweBussImageModel{

	private static final long serialVersionUID = 2573869315001329273L;
	public static final String NAME = "rolAccModAplAdapterVO";
	
	private RolAccModAplVO 		rolAccModApl = new RolAccModAplVO();
	
	
	public RolAccModAplAdapter(){
		super();
	}


	public RolAccModAplVO getRolAccModApl() {
		return rolAccModApl;
	}


	public void setRolAccModApl(RolAccModAplVO rolAccModApl) {
		this.rolAccModApl = rolAccModApl;
	}
	
	
}
