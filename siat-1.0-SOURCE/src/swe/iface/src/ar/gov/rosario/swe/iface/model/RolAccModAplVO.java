//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;



public class RolAccModAplVO extends SweBussImageModel{

	private static final long serialVersionUID = 2573869315001329273L;
	
	private RolAplVO 		rolApl = new RolAplVO();
	private AccModAplVO 	accModApl = new AccModAplVO();
	private AplicacionVO 	aplicacion = new AplicacionVO();
	
	
	/**
	 * constructor vacio
	 */
	public RolAccModAplVO() {
		super();
	}


	
	
	
	public AccModAplVO getAccModApl() {
		return accModApl;
	}


	public void setAccModApl(AccModAplVO accModApl) {
		this.accModApl = accModApl;
	}


	public AplicacionVO getAplicacion() {
		return aplicacion;
	}


	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}


	public RolAplVO getRolApl() {
		return rolApl;
	}


	public void setRolApl(RolAplVO rolApl) {
		this.rolApl = rolApl;
	}
	
}
