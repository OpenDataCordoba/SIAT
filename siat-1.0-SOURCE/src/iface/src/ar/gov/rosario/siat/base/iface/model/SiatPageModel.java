//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.model;
                                                                         
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.model.PageModel;


public class SiatPageModel extends PageModel implements ISiatModel {

	
	public SiatPageModel(String sweActionName){
		super(sweActionName);
		try {
			this.getReport().setReportFileSharePath(SiatParam.getString("FileSharePath"));
		} catch (Exception e) {
			this.getReport().setReportFileSharePath("/mnt/siat");
		}
	}
	
	// Application Specific View flags
	public String getVerEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getVerBussEnabled(), ACCION_VER, METODO_VER);
	}
	
	public String getAgregarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAgregarBussEnabled(), ACCION_AGREGAR, METODO_AGREGAR);
	}

	public String getModificarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getModificarBussEnabled(), ACCION_MODIFICAR, METODO_MODIFICAR);
	}
	
	public String getEliminarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEliminarBussEnabled(), ACCION_ELIMINAR, METODO_ELIMINAR);
	}
	
	public String getActivarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getActivarBussEnabled(), ACCION_ACTIVAR, METODO_ACTIVAR);
	}
	
	public String getDesactivarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getDesactivarBussEnabled(), ACCION_DESACTIVAR, METODO_DESACTIVAR);
	}
	
	public String getAltaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAltaBussEnabled(), ACCION_ALTA, METODO_ALTA);
	}

	public String getBajaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getBajaBussEnabled(), ACCION_BAJA, METODO_BAJA);
	}

	/**
	 * Metodo utilizado para que la intefaz de Sercivio de Caso "ICasCasoService"
	 * pueda recibir un SiatBussImageModel que implemente el getCaso().
	 * 
	 * @author Cristian
	 * @return
	 */
	public CasoVO getCaso(){
		return null;
	}

}
