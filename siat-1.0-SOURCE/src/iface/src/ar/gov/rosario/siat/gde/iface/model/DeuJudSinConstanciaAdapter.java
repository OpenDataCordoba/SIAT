//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del BuscarDeuJudSinConstancia
 * 
 * @author tecso
 */
public class DeuJudSinConstanciaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "deuJudSinConstanciaAdapterVO";
	
	private DeudaVO deuda = new DeudaVO();
	
	private ConstanciaDeuVO constanciaDeu = new ConstanciaDeuVO();
    
    //Lista de ID's seleccionados 
    private String[] listIdSelected;
    
    // Constructores
    public DeuJudSinConstanciaAdapter(){
    	super(GdeSecurityConstants.ABM_DEUJUDSINCONSTANCIA);
    }

    //  Getters y Setters
	public ConstanciaDeuVO getConstanciaDeu() {
		return constanciaDeu;
	}

	public void setConstanciaDeu(ConstanciaDeuVO constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}

	public DeudaVO getDeuda() {
		return deuda;
	}

	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}

	public String[] getListIdSelected() {
		return listIdSelected;
	}

	public void setListIdSelected(String[] listIdSelected) {
		this.listIdSelected = listIdSelected;
	}
	
	// View getters
}
