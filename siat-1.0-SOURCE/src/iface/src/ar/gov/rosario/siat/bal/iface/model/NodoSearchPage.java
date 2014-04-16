//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;

/**
 * SearchPage de los Nodos (para representar arboles de clasificacion)
 * 
 * @author Tecso
 *
 */
public class NodoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "nodoSearchPageVO";

	private NodoVO nodo = new NodoVO();

	private List<ClasificadorVO> listClasificador = new ArrayList<ClasificadorVO>();
	private List<NodoVO> listNodoRaiz = new ArrayList<NodoVO>(); 
	
	private Map<String, NodoVO> mapNodo = new HashMap<String, NodoVO>();
	
	private Long idNodoSelected = null; 
	
	// Flags
	private boolean arbolExpandido = false;
	
	private boolean checkearConsistencia = false; 
	
	// Constructores
	public NodoSearchPage() {       
       super(BalSecurityConstants.ABM_NODO);
    }

	// Getters y Setters
	public List<NodoVO> getListNodoRaiz() {
		return listNodoRaiz;
	}
	public void setListNodoRaiz(List<NodoVO> listNodoRaiz) {
		this.listNodoRaiz = listNodoRaiz;
	}
	public NodoVO getNodo() {
		return nodo;
	}
	public void setNodo(NodoVO nodo) {
		this.nodo = nodo;
	}
	public List<ClasificadorVO> getListClasificador() {
		return listClasificador;
	}
	public void setListClasificador(List<ClasificadorVO> listClasificador) {
		this.listClasificador = listClasificador;
	}
	
	public String getName(){
		return NAME;
	}

	public Map<String, NodoVO> getMapNodo() {
		return mapNodo;
	}
	public void setMapNodo(Map<String, NodoVO> mapNodo) {
		this.mapNodo = mapNodo;
	}
	public boolean isArbolExpandido() {
		return arbolExpandido;
	}
	public void setArbolExpandido(boolean arbolExpandido) {
		this.arbolExpandido = arbolExpandido;
	}
	public Long getIdNodoSelected() {
		return idNodoSelected;
	}
	public void setIdNodoSelected(Long idNodoSelected) {
		this.idNodoSelected = idNodoSelected;
	}

	public boolean isCheckearConsistencia() {
		return checkearConsistencia;
	}
	public void setCheckearConsistencia(boolean checkearConsistencia) {
		this.checkearConsistencia = checkearConsistencia;
	}
	
}
