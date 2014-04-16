//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del LiqCom
 * 
 * @author tecso
 */
public class LiqComAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqComAdapterVO";
	
    private LiqComVO liqCom = new LiqComVO();
    
    private List<ServicioBancoVO> listServicioBanco = new ArrayList<ServicioBancoVO>();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();

    
    // Constructores
    public LiqComAdapter(){
    	super(GdeSecurityConstants.ABM_LIQCOM);
    }
    
    //  Getters y Setters
	public LiqComVO getLiqCom() {
		return liqCom;
	}

	public void setLiqCom(LiqComVO liqComVO) {
		this.liqCom = liqComVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<ServicioBancoVO> getListServicioBanco() {
		return listServicioBanco;
	}

	public void setListServicioBanco(List<ServicioBancoVO> listServicioBanco) {
		this.listServicioBanco = listServicioBanco;
	}

	
	// View getters
}
