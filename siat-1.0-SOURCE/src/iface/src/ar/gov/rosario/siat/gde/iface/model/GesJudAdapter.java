//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del GesJud
 * 
 * @author tecso
 */
public class GesJudAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "gesJudAdapterVO";

	public static final String ENC_NAME = "encGesJudAdapterVO";
	
	private GesJudVO gesJud= new GesJudVO();
	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	
	private List<TipoJuzgadoVO> listTipoJuzgado = new ArrayList<TipoJuzgadoVO>();
    
    // Constructores
    public GesJudAdapter(){
    	super(GdeSecurityConstants.ADM_GESJUD);
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ADM_GESJUD;
    }
    
    //  Getters y Setters
	public GesJudVO getGesJud() {
		return gesJud;
	}

	public void setGesJud(GesJudVO gesJudVO) {
		this.gesJud = gesJudVO;
	}
	
	public List<TipoJuzgadoVO> getListTipoJuzgado() {
		return listTipoJuzgado;
	}

	public void setListTipoJuzgado(List<TipoJuzgadoVO> listTipoJuzgado) {
		this.listTipoJuzgado = listTipoJuzgado;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	// View getters
	public String getModificarEncEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ADM_GESJUD_ENC, BaseSecurityConstants.MODIFICAR);
	}
	
	// permisos para ABM GesJudDeu
	public String getAgregarGesJudDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_GESJUDDEU, BaseSecurityConstants.AGREGAR);
	}
	
	public String getEliminarGesJudDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_GESJUDDEU, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getModificarGesJudDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_GESJUDDEU, BaseSecurityConstants.MODIFICAR);
	}	

	// permisos para ABM GesJudEvento
	public String getAgregarGesJudEventoEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_GESJUDEVENTO, BaseSecurityConstants.AGREGAR);
	}
	
	public String getEliminarGesJudEventoEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_GESJUDEVENTO, BaseSecurityConstants.ELIMINAR);
	}		

}
