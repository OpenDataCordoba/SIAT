//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de RelCla (Relacion entre Nodos de distintos Clasificadores)
 * 
 * @author tecso
 */
public class RelClaAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "relClaAdapterVO";

	private RelClaVO relCla = new RelClaVO();
	
	private List<ClasificadorVO> listClasificador = new ArrayList<ClasificadorVO>();
	
	private List<NodoVO> listNodo = new ArrayList<NodoVO>();
	
	// Flags
	private boolean paramClasificador = false;
	
	public RelClaAdapter(){
		super(BalSecurityConstants.ABM_RELCLA);
	}

	// Getters y Setters
	public RelClaVO getRelCla() {
		return relCla;
	}
	public void setRelCla(RelClaVO relCla) {
		this.relCla = relCla;
	}
	public List<ClasificadorVO> getListClasificador() {
		return listClasificador;
	}
	public void setListClasificador(List<ClasificadorVO> listClasificador) {
		this.listClasificador = listClasificador;
	}
	public boolean isParamClasificador() {
		return paramClasificador;
	}
	public void setParamClasificador(boolean paramClasificador) {
		this.paramClasificador = paramClasificador;
	}
	public List<NodoVO> getListNodo() {
		return listNodo;
	}
	public void setListNodo(List<NodoVO> listNodo) {
		this.listNodo = listNodo;
	}

}
