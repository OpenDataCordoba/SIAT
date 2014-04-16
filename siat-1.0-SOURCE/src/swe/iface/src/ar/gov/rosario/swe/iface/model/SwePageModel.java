//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;
                                                                         
import coop.tecso.demoda.iface.model.PageModel;


public class SwePageModel extends PageModel {

	public SwePageModel(String sweActionName){
		super(sweActionName);
	}

	// Application Specific View flags
	public String getAgregarEnabled() {
		return SweBussImageModel.hasEnabledFlag(this.getAgregarBussEnabled(), ACCION_AGREGAR, METODO_AGREGAR);
	}

	public String getModificarEnabled() {
		return SweBussImageModel.hasEnabledFlag(this.getModificarBussEnabled(), ACCION_MODIFICAR, METODO_MODIFICAR);
	}
	
	public String getEliminarEnabled() {
		return SweBussImageModel.hasEnabledFlag(this.getEliminarBussEnabled(), ACCION_ELIMINAR, METODO_ELIMINAR);
	}
	public String getAltaEnabled() {
		return SweBussImageModel.hasEnabledFlag(this.getAltaBussEnabled(), ACCION_ALTA, METODO_ALTA);
	}

	public String getBajaEnabled() {
		return SweBussImageModel.hasEnabledFlag(this.getBajaBussEnabled(), ACCION_BAJA, METODO_BAJA);
	}
}
