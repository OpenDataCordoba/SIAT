//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * SearchPage del GesJud
 * 
 * @author Tecso
 *
 */
public class GesJudSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "gesJudSearchPageVO";
	
	private GesJudVO gesJud= new GesJudVO();
	
	private CuentaVO cuenta = new CuentaVO();
	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	
	private List<EstadoGesJudVO> listEstadoGesJud = new ArrayList<EstadoGesJudVO>();
	
	// Constructores
	public GesJudSearchPage() {       
       super(GdeSecurityConstants.ADM_GESJUD);        
    }
	
	// Getters y Setters
	public GesJudVO getGesJud() {
		return gesJud;
	}
	public void setGesJud(GesJudVO gesJud) {
		this.gesJud = gesJud;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<EstadoGesJudVO> getListEstadoGesJud() {
		return listEstadoGesJud;
	}

	public void setListEstadoGesJud(List<EstadoGesJudVO> listEstadoGesjud) {
		this.listEstadoGesJud = listEstadoGesjud;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}
	
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	

	// View getters
	

	//View flags
	public String getRegistrarCaducidadEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ADM_GESJUD, GdeSecurityConstants.REG_CADUCIDAD_GESJUD);
	}
}
