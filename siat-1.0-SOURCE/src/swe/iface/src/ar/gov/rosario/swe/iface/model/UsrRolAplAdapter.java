//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;


public class UsrRolAplAdapter extends SweAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "usrRolAplAdapterVO";
	
	
	private UsrRolAplVO usrRolApl = new UsrRolAplVO();
	
	public UsrRolAplAdapter(){
		super();
	}

	public UsrRolAplAdapter(UsrRolAplVO usrRolAplVO){
		this();
		this.usrRolApl = usrRolAplVO;
	}

	public UsrRolAplVO getUsrRolApl() {
		return usrRolApl;
	}
	public void setUsrRolApl(UsrRolAplVO usrRolApl) {
		this.usrRolApl = usrRolApl;
	}
	
}
