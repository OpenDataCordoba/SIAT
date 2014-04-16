//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * Adapter del GesJudDeu
 * 
 * @author tecso
 */
public class GesJudDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "gesJudDeuAdapterVO";
	
	private CuentaVO cuenta = new CuentaVO();
	
    private GesJudDeuVO gesJudDeu = new GesJudDeuVO();
    
    private ConstanciaDeuVO constanciaDeu = new ConstanciaDeuVO();
    
    private String[] idsDeudaSelected = new String[0];
    
    private List<DeudaJudicialVO>	listDeudaJudicial = new ArrayList<DeudaJudicialVO>();
    
    //view flags
    private boolean verResultados = false;
    
    // Constructores
    public GesJudDeuAdapter(){
    	super(GdeSecurityConstants.ABM_GESJUDDEU);
    }
    
    //  Getters y Setters
	public GesJudDeuVO getGesJudDeu() {
		return gesJudDeu;
	}

	public void setGesJudDeu(GesJudDeuVO gesJudDeuVO) {
		this.gesJudDeu = gesJudDeuVO;
	}

	public List<DeudaJudicialVO> getListDeudaJudicial() {
		return listDeudaJudicial;
	}

	public void setListDeudaJudicial(List<DeudaJudicialVO> listDeudaJudicial) {
		this.listDeudaJudicial = listDeudaJudicial;
	}

	public String[] getIdsDeudaSelected() {
		return idsDeudaSelected;
	}

	public void setIdsDeudaSelected(String[] idsDeudaSelected) {
		this.idsDeudaSelected = idsDeudaSelected;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public ConstanciaDeuVO getConstanciaDeu() {
		return constanciaDeu;
	}

	public void setConstanciaDeu(ConstanciaDeuVO constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}

	public boolean getVerResultados() {
		return verResultados;
	}

	public void setVerResultados(boolean verResultados) {
		this.verResultados = verResultados;
	}
	
	// View getters
}
