//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * SearchPage del DeuJudSinConstancia
 * 
 * @author Tecso
 *
 */
public class DeuJudSinConstanciaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "deuJudSinConstanciaSearchPageVO";
	
    private DeudaVO deuda = new DeudaVO();
    
    private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    private CuentaVO cuenta = new CuentaVO();  
    
    //Lista de ID's seleccionados 
    private String[] listIdSelected;
	
	// Constructores
	public DeuJudSinConstanciaSearchPage() {       
       super(GdeSecurityConstants.ABM_DEUJUDSINCONSTANCIA);        
    }

	//	 Getters y Setters
	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public DeudaVO getDeuda() {
		return deuda;
	}

	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public String[] getListIdSelected() {
		return listIdSelected;
	}

	public void setListIdSelected(String[] listIdSelected) {
		this.listIdSelected = listIdSelected;
	}
}
