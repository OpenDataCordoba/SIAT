//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter utilizado en el Desbloqueo de 'Tramite o Sellado' para el Control de Informe de Deuda de Escribano
 * @author tecso
 *
 */
public class CtrlInfDeuAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	private CtrlInfDeuVO ctrlInfDeu = new CtrlInfDeuVO();
	
	public static final String NAME = "ctrlInfDeuAdapterVO";
	
	private String codigo = "";
	
	private boolean paramEncontrado = false;
	private boolean paramNoEncontrado = false;
	
	public CtrlInfDeuAdapter(){
		super(GdeSecurityConstants.ABM_CTRLINFDEU);
	}
	
	// Getters Y Setters
	public CtrlInfDeuVO getCtrlInfDeu() {
		return ctrlInfDeu;
	}
	public void setCtrlInfDeu(CtrlInfDeuVO ctrlInfDeu) {
		this.ctrlInfDeu = ctrlInfDeu;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public boolean isParamEncontrado() {
		return paramEncontrado;
	}
	public void setParamEncontrado(boolean paramEncontrado) {
		this.paramEncontrado = paramEncontrado;
	}
	public boolean isParamNoEncontrado() {
		return paramNoEncontrado;
	}
	public void setParamNoEncontrado(boolean paramNoEncontrado) {
		this.paramNoEncontrado = paramNoEncontrado;
	}

	public String getName(){
		return NAME;
	}

}
