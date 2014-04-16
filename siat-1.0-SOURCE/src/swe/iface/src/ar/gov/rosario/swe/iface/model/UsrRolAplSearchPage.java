//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;

public class UsrRolAplSearchPage extends SwePageModel {
	
	private static final long serialVersionUID = 1L;

	//private Log log = LogFactory.getLog(getClass());
	
	public static final String NAME = "usrRolAplSearchPageVO";
	
	private UsrAplVO usrApl = new UsrAplVO();
	
    public UsrRolAplSearchPage() {
        super(SweSecurityConstants.ABM_ROLESUSUARIO);
    }
    public UsrRolAplSearchPage(UsrAplVO usrAplVO) {
    	this();
    	this.usrApl = usrAplVO;
    }

	public String infoString(){
		String infoString = 
			" username:          " + this.usrApl.getId() + "\r\n" +
			" aplicacion.id:     " + this.usrApl.getUsername() + "\r\n";
		return infoString;
	}
	public UsrAplVO getUsrApl() {
		return usrApl;
	}
	public void setUsrApl(UsrAplVO usrApl) {
		this.usrApl = usrApl;
	}
	
	


}
