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

/**
 * Adapter de Asentamiento
 * 
 * @author tecso
 */
public class AsentamientoAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "asentamientoAdapterVO";
	
	private AsentamientoVO asentamiento = new AsentamientoVO();
	
	private List<ServicioBancoVO> listServicioBanco = new ArrayList<ServicioBancoVO>();
	
	//Flags
	private String paramEstadoEjercicio = "";
	
	public AsentamientoAdapter(){
		super(BalSecurityConstants.ABM_ASENTAMIENTO);
	}

	// Getters y Setters
	public AsentamientoVO getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(AsentamientoVO asentamiento) {
		this.asentamiento = asentamiento;
	}
	public List<ServicioBancoVO> getListServicioBanco() {
		return listServicioBanco;
	}
	public void setListServicioBanco(List<ServicioBancoVO> listServicioBanco) {
		this.listServicioBanco = listServicioBanco;
	}

	// Flags
	public String getParamEstadoEjercicio() {
		return paramEstadoEjercicio;
	}
	public void setParamEstadoEjercicio(String paramEstadoEjercicio) {
		this.paramEstadoEjercicio = paramEstadoEjercicio;
	}
	
}
