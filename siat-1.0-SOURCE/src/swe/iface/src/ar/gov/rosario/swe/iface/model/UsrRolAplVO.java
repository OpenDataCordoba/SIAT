//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;



public class UsrRolAplVO extends SweBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "usrRolAplVO";
	
	private UsrAplVO usrApl = new UsrAplVO();
	private RolAplVO rolApl = new RolAplVO();
	
	public UsrRolAplVO(){
		super();
	}

	public RolAplVO getRolApl() {
		return rolApl;
	}

	public void setRolApl(RolAplVO rolApl) {
		this.rolApl = rolApl;
	}

	public UsrAplVO getUsrApl() {
		return usrApl;
	}

	public void setUsrApl(UsrAplVO usrApl) {
		this.usrApl = usrApl;
	}

	
	
}
