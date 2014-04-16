//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;

/**
 * Adapter del ${Bean}
 * 
 * @author tecso
 */
public class ContratoAdapter extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "contratoAdapterVO";
	
    private ContratoVO contrato = new ContratoVO();
    
    private List<RecursoVO>	listRecurso = new ArrayList<RecursoVO>();
    private List<TipoContratoVO> listTipoContrato = new ArrayList<TipoContratoVO>();    
    
    // Constructores
    public ContratoAdapter(){
    	super(RecSecurityConstants.ABM_CONTRATO);
    }
    
    //  Getters y Setters
	public ContratoVO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoVO contratoVO) {
		this.contrato = contratoVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipoContratoVO> getListTipoContrato() {
		return listTipoContrato;
	}

	public void setListTipoContrato(List<TipoContratoVO> listTipoContrato) {
		this.listTipoContrato = listTipoContrato;
	}
	
	// View getters
}