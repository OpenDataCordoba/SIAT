//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Adapter de Servicio Banco Recurso
 * 
 * @author tecso
 */
public class SerBanRecAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "serBanRecAdapterVO";
	
	private SerBanRecVO serBanRec = new SerBanRecVO();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	public SerBanRecAdapter(){
		super(DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO);
	}
	
	//Getters y Setter
	public SerBanRecVO getSerBanRec(){
		return serBanRec;
	}
	public void setSerBanRec(SerBanRecVO serBanRec){
		this.serBanRec = serBanRec;
	}
	public List<RecursoVO> getListRecurso(){
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso){
		this.listRecurso = listRecurso;
	}
	
}
