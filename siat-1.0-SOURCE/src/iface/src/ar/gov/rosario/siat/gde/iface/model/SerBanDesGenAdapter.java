//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Adapter de Servicio Banco Descuentos Generales
 * 
 * @author tecso
 */
public class SerBanDesGenAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "serBanDesGenAdapterVO";
	
	private SerBanDesGenVO	serBanDesGen = new SerBanDesGenVO();
 	private List<DesGenVO> listDesGen = new ArrayList<DesGenVO>();

	public SerBanDesGenAdapter(){
		super(DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES);
	}
	
	// Getter y Setters
	public SerBanDesGenVO getSerBanDesGen(){
		return serBanDesGen;
	}
	public void setSerBanDesGen(SerBanDesGenVO serBanDesGen){
		this.serBanDesGen = serBanDesGen;
	}
	public List<DesGenVO> getListDesGen(){
		return listDesGen;
	}
	public void setListDesGen(List<DesGenVO> listDesGen){
		this.listDesGen = listDesGen;
	}
	
}
