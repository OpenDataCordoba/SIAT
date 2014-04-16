//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Search Page de Servicio Banco Recurso
 * @author tecso
 *
 */
public class SerBanRecSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "serBanRecSearchPageVO";

	private SerBanRecVO serBanRec = new SerBanRecVO();
	
	List<SerBanRecVO> listSerBanRec = new ArrayList<SerBanRecVO>();

	public SerBanRecSearchPage(){
		super(DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO);
	}
	
	// Getters y Setters
	public SerBanRecVO getSerBanRec(){
		return serBanRec;
	}
	public void setSerBanRec(SerBanRecVO serBanRec){
		this.serBanRec = serBanRec;
	}
	public List<SerBanRecVO> getListSerBanRec(){
		return listSerBanRec;
	}
	public void setListSerBanRec(List<SerBanRecVO> listSerBanRec){
		this.listSerBanRec = listSerBanRec;
	}
}
