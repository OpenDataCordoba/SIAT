//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adaptador del Objeto Imponible
 * @author Tecso Coop. Ltda.
 */
public class ObjImpAdapter extends SiatAdapterModel {

	
	private static final long serialVersionUID = 1L;
	public static final String NAME = "objImpAdapterVO";
	public static final String ENC_NAME = "encObjImpAdapterVO";	
	
	
	private ObjImpVO objImp = new ObjImpVO();
	
	private List<TipObjImpVO> listTipObjImp = new ArrayList<TipObjImpVO>();
	private TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
	
	// Construtores
	public ObjImpAdapter() {
		super(PadSecurityConstants.ABM_OBJIMP);
    	ACCION_MODIFICAR_ENCABEZADO = PadSecurityConstants.ABM_OBJIMP_ENC;		
	}
	
	
	// Getters y Setters
	public List<TipObjImpVO> getListTipObjImp() {
		return listTipObjImp;
	}


	public void setListTipObjImp(List<TipObjImpVO> listTipObjImp) {
		this.listTipObjImp = listTipObjImp;
	}

	/**
	 * Contenedor con definicion y valores de atributos del objeto imponible.
	 * @return
	 */
	public TipObjImpDefinition getTipObjImpDefinition() {
		return tipObjImpDefinition;
	}

	/**
	 * @param tipObjImpDefinition the defTipObjImp to set
	 */
	public void setTipObjImpDefinition(TipObjImpDefinition tipObjImpDefinition) {
		this.tipObjImpDefinition = tipObjImpDefinition;
	}


	/**
	 * Objeto Imponible
	 * @return the objImp
	 */
	public ObjImpVO getObjImp() {
		return objImp;
	}
	/**
	 * @param objImp the objImp to set
	 */
	public void setObjImp(ObjImpVO objImp) {
		this.objImp = objImp;
	}

	
	// Metodos para la seguridad en a vista
	public String getVerObjImpAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_OBJIMPATRVAL, BaseSecurityConstants.VER);
	}
	
	public String getModificarObjImpAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_OBJIMPATRVAL, BaseSecurityConstants.MODIFICAR);
	}
	
}
