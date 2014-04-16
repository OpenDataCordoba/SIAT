//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter de las Planillas de las deudas incluida del Envio Judicial
 * 
 * @author tecso
 */
public class DeudaProMasPlanillasDeudaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "deudaProMasPlanillasDeudaAdapterVO";
	
	public static final String FILE_NAME = "planillaDeudaProMas";
	public static final String FILE_NAME_CUO_CONV = "planillaCuotaConvProMas";
	
    private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();

    // bandera que indica que trabaja con deudas y no con cuotas de convenios
    private Boolean deudas = Boolean.TRUE;
    
    public DeudaProMasPlanillasDeudaAdapter(){
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

	public Boolean getDeudas() {
		return deudas;
	}
	public void setDeudas(Boolean deudas) {
		this.deudas = deudas;
	}
	
	
}
