//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Search Page de Servicio Banco Descuentos Generales
 * @author tecso
 *
 */
public class SerBanDesGenSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "serBanDesGenSearchPageVO";

	private SerBanDesGenVO serBanDesGen = new SerBanDesGenVO();
	
	List<SerBanDesGenVO> listSerBanDesGen = new ArrayList<SerBanDesGenVO>();
		
	public SerBanDesGenSearchPage(){
		super(DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES);
	}

	// Getters y Setters
	public SerBanDesGenVO getSerBanDesGen(){
		return serBanDesGen;
	}
	public void setSerBanDesGen(SerBanDesGenVO serBanDesGen){
		this.serBanDesGen = serBanDesGen;
	}
	public List<SerBanDesGenVO> getListSerBanDesGen(){
		return listSerBanDesGen;
	}
	public void setListSerBanDesGen(List<SerBanDesGenVO> listSerBanDesGen){
		this.listSerBanDesGen = listSerBanDesGen;
	}

}