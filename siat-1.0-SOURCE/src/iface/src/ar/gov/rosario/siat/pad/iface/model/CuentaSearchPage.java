//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

public class CuentaSearchPage extends SiatPageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "cuentaSearchPageVO";
	public static final String AUX_NAME = "auxCuentaSearchPageVO";
	
	public static final String PARAM_ID_RECURSO = "idRecurso";
	
	private CuentaTitularVO cuentaTitular = new CuentaTitularVO();
		
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
		
	private boolean esCMD = false;
	
	private Boolean relacionarBussEnabled   = true;
	
	public CuentaSearchPage() {
		super(PadSecurityConstants.ABM_CUENTA);
		ACCION_AGREGAR = PadSecurityConstants.ABM_CUENTA_ENC;
		ACCION_MODIFICAR = PadSecurityConstants.ABM_CUENTA;
    }
	// ------------------------ Getters y Setters --------------------------------//

	public CuentaTitularVO getCuentaTitular() {
		return cuentaTitular;
	}
	public void setCuentaTitular(CuentaTitularVO cuentaTitular) {
		this.cuentaTitular = cuentaTitular;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public boolean isEsCMD() {
		return esCMD;
	}
	public void setEsCMD(boolean esCMD) {
		this.esCMD = esCMD;
	}
	
	// Getter para struts ------------------------------------------------------------------------------------------------

	//	Flags Seguridad
	public Boolean getRelacionarBussEnabled() {
		return relacionarBussEnabled;
	}

	public void setRelacionarBussEnabled(Boolean relacionarBussEnabled) {
		this.relacionarBussEnabled = relacionarBussEnabled;
	}
	
	public String getRelacionarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getRelacionarBussEnabled(), 
				PadSecurityConstants.ABM_CUENTA, BaseSecurityConstants.RELACIONAR);
	}
}
