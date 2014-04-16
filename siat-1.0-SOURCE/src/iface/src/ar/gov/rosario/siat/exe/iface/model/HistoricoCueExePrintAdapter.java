//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;

/**
 * Adapter de Historico de Exencines
 * 
 * @author tecso
 */
public class HistoricoCueExePrintAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "historicoCueExePrintAdapterVO";
	
	// Propiedades
    private LiqCuentaVO cuenta = new LiqCuentaVO(); 
    private List<CueExeVO> listExenciones = new ArrayList<CueExeVO>();
    
	// Constructores
    public HistoricoCueExePrintAdapter(){
    	super(ExeSecurityConstants.ABM_CUEEXE);
    }

    //  Getters y Setters
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public List<CueExeVO> getListExenciones() {
		return listExenciones;
	}
	public void setListExenciones(List<CueExeVO> listExenciones) {
		this.listExenciones = listExenciones;
	}

}
