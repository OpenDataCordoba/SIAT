//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adaptador del Atributo de Objeto Imponible
 * @author Tecso Coop. Ltda.
 */
public class ObjImpAtrValAdapter extends SiatAdapterModel {
	
	public static final String NAME = "objImpAtrValAdapterVO";

	private static final long serialVersionUID = 1L;

	private ObjImpVO objImp = new ObjImpVO();
	private TipObjImpAtrDefinition tipObjImpAtrDefinition = new TipObjImpAtrDefinition();

	//private TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
		
	// Constructores
    public ObjImpAtrValAdapter() {
    	super(PadSecurityConstants.ABM_OBJIMPATRVAL);
    }

/*	/**
	 * Contenedor con definicion y valores de atributos del objeto imponible.
	 * @return the defTipObjImp
	 * /
	public TipObjImpDefinition getDefTipObjImp() {
		return tipObjImpDefinition;
	}
	/**
	 * @param tipObjImpDefinition the defTipObjImp to set
	 * /
	public void setDefTipObjImp(TipObjImpDefinition tipObjImpDefinition) {
		this.tipObjImpDefinition = tipObjImpDefinition;
	}
	*/
    
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

	
	public TipObjImpAtrDefinition getTipObjImpAtrDefinition() {
		return tipObjImpAtrDefinition;
	}

	public void setTipObjImpAtrDefinition(
			TipObjImpAtrDefinition tipObjImpAtrDefinition) {
		this.tipObjImpAtrDefinition = tipObjImpAtrDefinition;
	}

	
}
