//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;

/**
 * Adapter del Nodo (para representar arboles de clasificacion) 
 * 
 * @author tecso
 */
public class NodoAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "nodoAdapterVO";
	public static final String ENC_NAME = "encNodoAdapterVO";

    private NodoVO nodo = new NodoVO();

    private List<ClasificadorVO> listClasificador = new ArrayList<ClasificadorVO>();
    private List<NodoVO> listNodoPadre = new ArrayList<NodoVO>();
    
    private List<RelPartidaVO> listRelPartida = new ArrayList<RelPartidaVO>();
    private List<RelClaVO> listRelCla = new ArrayList<RelClaVO>();
    
    // Flags
    private boolean paramRelCla = false;
    private boolean paramRelPartida = false;
    private boolean paramClasificador = false;
    
    // Constructores
    public NodoAdapter(){
    	super(BalSecurityConstants.ABM_NODO);
		ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_NODO_ENC;
    }

    // Getters y Setters
	public List<RelClaVO> getListRelCla() {
		return listRelCla;
	}
	public void setListRelCla(List<RelClaVO> listRelCla) {
		this.listRelCla = listRelCla;
	}
	public List<RelPartidaVO> getListRelPartida() {
		return listRelPartida;
	}
	public void setListRelPartida(List<RelPartidaVO> listRelPartida) {
		this.listRelPartida = listRelPartida;
	}
	public NodoVO getNodo() {
		return nodo;
	}
	public void setNodo(NodoVO nodo) {
		this.nodo = nodo;
	}
	public boolean isParamRelCla() {
		return paramRelCla;
	}
	public void setParamRelCla(boolean paramRelCla) {
		this.paramRelCla = paramRelCla;
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
	public List<NodoVO> getListNodoPadre() {
		return listNodoPadre;
	}
	public void setListNodoPadre(List<NodoVO> listNodoPadre) {
		this.listNodoPadre = listNodoPadre;
	}
	public boolean isParamRelPartida() {
		return paramRelPartida;
	}
	public void setParamRelPartida(boolean paramRelPartida) {
		this.paramRelPartida = paramRelPartida;
	}

	// Permisos para ABM RelCla
	public String getVerRelClaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_RELCLA, BaseSecurityConstants.VER);
	}
	public String getModificarRelClaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_RELCLA, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRelClaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_RELCLA, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRelClaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_RELCLA, BaseSecurityConstants.AGREGAR);
	}
    
	// Permisos para ABM RelPartida
	public String getVerRelPartidaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_RELPARTIDA, BaseSecurityConstants.VER);
	}
	public String getModificarRelPartidaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_RELPARTIDA, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRelPartidaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_RELPARTIDA, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRelPartidaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_RELPARTIDA, BaseSecurityConstants.AGREGAR);
	}

	
}

