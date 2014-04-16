//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del armado de la seleccion de la deuda incluida del Envio Judicial
 * Se utiliza para:
 * 		visualizar los logs de armado y 
 * 		limpiar la seleccion (borrar los selAlmDet y los logs)
 * @author tecso
 */
public class DeudaProMasArmadoSeleccionAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "deudaProMasArmadoSeleccionAdapterVO";
	
    private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();
    
    // Utilizada en el jsp para habilitar o no el boton de limpiar seleccion
    public boolean esLimpiarSeleccion = false;
    
    public DeudaProMasArmadoSeleccionAdapter(){
    	// setea ACCION_AGREGAR, ACCION_MODIFICAR, ACCION_ELIMINAR, ACCION_VER, ACCION_ACTIVAR, ACCION_DESACTIVAR
    	super(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO);    	
    }

    // Getters y setters
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public void setEsLimpiarSeleccion(boolean esLimpiarSeleccion) {
		this.esLimpiarSeleccion = esLimpiarSeleccion;
	}
	public boolean getEsLimpiarSeleccion(){
		return this.esLimpiarSeleccion;
	}
}
