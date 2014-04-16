//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adapter del CueExcSel
 * 
 * @author tecso
 */
public class CueExcSelAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "cueExcSelAdapterVO";
	public static final String ENC_NAME = "encCueExcSelAdapterVO";
	
    private CueExcSelVO cueExcSel = new CueExcSelVO();
	
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    //Flags de la vista
    private boolean agregarCueExcSelDeuEnabled = false;
    private boolean activarCueExcSelDeuEnabled = false;
    private boolean desactivarCueExcSelDeuEnabled = false;
    
     // Constructores
    public CueExcSelAdapter(){
    	super(PadSecurityConstants.ABM_CUEEXCSEL);
      	ACCION_MODIFICAR_ENCABEZADO = PadSecurityConstants.ABM_CUEEXCSEL_ENC;
    }
    
    //  Getters y Setters
	public CueExcSelVO getCueExcSel() {
		return cueExcSel;
	}

	public void setCueExcSel(CueExcSelVO cueExcSelVO) {
		this.cueExcSel = cueExcSelVO;
	}
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	// Permisos para CueExcSelDeu
	public boolean isAgregarCueExcSelDeuEnabled() {
		return agregarCueExcSelDeuEnabled;
	}

	public void setAgregarCueExcSelDeuEnabled(boolean agregarCueExcSelDeuEnabled) {
		this.agregarCueExcSelDeuEnabled = agregarCueExcSelDeuEnabled;
	}

	public boolean isActivarCueExcSelDeuEnabled() {
		return activarCueExcSelDeuEnabled;
	}

	public void setActivarCueExcSelDeuEnabled(boolean activarCueExcSelDeuEnabled) {
		this.activarCueExcSelDeuEnabled = activarCueExcSelDeuEnabled;
	}

	public boolean isDesactivarCueExcSelDeuEnabled() {
		return desactivarCueExcSelDeuEnabled;
	}

	public void setDesactivarCueExcSelDeuEnabled(
			boolean desactivarCueExcSelDeuEnabled) {
		this.desactivarCueExcSelDeuEnabled = desactivarCueExcSelDeuEnabled;
	}
}
