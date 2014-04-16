//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter de los Reportes de las deudas incluidas y excluidas del Envio Judicial
 * 
 * @author tecso
 */
public class ProcesoMasivoReportesDeudaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoMasivoReportesDeudaAdapterVO";
	
	public static final String FILE_NAME_DEUDA      = "reporteDeudaProMas";
	public static final String FILE_NAME_CONV_CUOTA = "reporteConvCuoProMas";
	
    private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();
    
    public ProcesoMasivoReportesDeudaAdapter(){
    	// setea ACCION_AGREGAR, ACCION_MODIFICAR, ACCION_ELIMINAR, ACCION_VER, ACCION_ACTIVAR, ACCION_DESACTIVAR
    	// ver constantes de acceso......
    	super(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO);    	
    }

    // Getters y setters
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
}
