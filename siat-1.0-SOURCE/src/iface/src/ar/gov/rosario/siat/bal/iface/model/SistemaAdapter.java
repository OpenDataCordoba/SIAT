//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.TipoDeudaVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Sistema
 * 
 * @author tecso
 */
public class SistemaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "sistemaAdapterVO";
	
    private SistemaVO 			  sistema = new SistemaVO();
    
    private List<ServicioBancoVO> listServicioBanco= new ArrayList<ServicioBancoVO>();
    
    private List<TipoDeudaVO> 	  listTipoDeuda = new ArrayList<TipoDeudaVO>();
    
    private List<SiNo>            listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public SistemaAdapter(){
    	super(BalSecurityConstants.ABM_SISTEMA);
    }
    
    //  Getters y Setters
	public SistemaVO getSistema() {
		return sistema;
	}

	public void setSistema(SistemaVO sistemaVO) {
		this.sistema = sistemaVO;
	}

	public List<ServicioBancoVO> getListServicioBanco() {
		return listServicioBanco;
	}

	public void setListServicioBanco(List<ServicioBancoVO> listServicioBanco) {
		this.listServicioBanco = listServicioBanco;
	}

	public List<TipoDeudaVO> getListTipoDeuda() {
		return listTipoDeuda;
	}

	public void setListTipoDeuda(List<TipoDeudaVO> listTipoDeuda) {
		this.listTipoDeuda = listTipoDeuda;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	// View getters
}
