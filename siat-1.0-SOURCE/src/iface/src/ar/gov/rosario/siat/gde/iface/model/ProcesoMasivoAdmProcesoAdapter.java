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
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;

/**
 * Adapter de Administracion de los Proceso Masivos
 * 
 * @author tecso
 */
public class ProcesoMasivoAdmProcesoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoMasivoAdmProcesoAdapterVO";
	
    private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();
    
    // Lista de Ids de Deuda seleccionados para incluir.
    private List<Long> listIdDeuda = new ArrayList<Long>();
    
    // Dupla de banderas que sirver para controlar el calculo y la visualizacion
    // de los totales de las selAlmDet incluidas y excluidas
    private Boolean calcularTotalesDeuda   = Boolean.FALSE;
    private Boolean visualizarTotalesDeuda = Boolean.FALSE;
    
    private Boolean verSeleccionAlmacenada         = Boolean.FALSE;
    private Boolean verPlanillaConvenioCuotaEnviar = Boolean.FALSE;

    private Boolean verPlanillasEnabled = Boolean.TRUE;
    
    
    public ProcesoMasivoAdmProcesoAdapter(){
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
	
	public List<Long> getListIdDeuda() {
		return listIdDeuda;
	}
	public void setListIdDeuda(List<Long> listIdDeuda) {
		this.listIdDeuda = listIdDeuda;
	}

	public String getModificarProcesoMasivoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_MODIFICAR_PROCESO_MASIVO);
	}
	
	//consultaDeudaEnabled
	
	
	//Seleccionar Deuda Enviar
	public String getSeleccionarDeudaEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_SELECCIONAR_DEUDA_ENVIAR);
	}
	//Eliminar Deuda Enviar
	public String getEliminarDeudaEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_ELIMINAR_DEUDA_ENVIAR);
	}
	//Limpiar Seleccion
	public String getLimpiarSeleccionDeudaEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_LIMPIAR_SELECCION_DEUDA_ENVIAR);
	}
	//Logs Armado de la Seleccion  siempre habilitado -->
	
	//Planilla Deuda -->
	public String getPlanillaDeudaEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_PLANILLA_DEUDA_ENVIAR);
	}
	//Planilla Cuota Convenio -->
	public String getPlanillaConvenioCuotaEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_PLANILLA_CONVENIO_CUOTA_ENVIAR);
	}
	//Consulta Deuda -->
	public String getConsultarDeudaEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_CONSULTAR_DEUDA_ENVIAR);
	}

	// agregarProMasProExcEnabled
	public String getAgregarProMasProExcEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROMASPROEXC, BaseSecurityConstants.AGREGAR);
	}

	// eliminarProMasProExcEnabled
	public String getEliminarProMasProExcEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROMASPROEXC, BaseSecurityConstants.ELIMINAR);
	}
	
	// verReportesDeudaEnabled
	public String getVerReportesDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_VER_REPORTES_DEUDA);
	}

	public Boolean getCalcularTotalesDeuda() {
		return calcularTotalesDeuda;
	}
	public void setCalcularTotalesDeuda(Boolean calcularTotalesDeuda) {
		this.calcularTotalesDeuda = calcularTotalesDeuda;
	}
	public Boolean getVisualizarTotalesDeuda() {
		return visualizarTotalesDeuda;
	}
	public void setVisualizarTotalesDeuda(Boolean visualizarTotalesDeuda) {
		this.visualizarTotalesDeuda = visualizarTotalesDeuda;
	}

	public Boolean getVerPlanillaConvenioCuotaEnviar() {
		return verPlanillaConvenioCuotaEnviar;
	}
	public void setVerPlanillaConvenioCuotaEnviar(
			Boolean verPlanillaConvenioCuotaEnviar) {
		this.verPlanillaConvenioCuotaEnviar = verPlanillaConvenioCuotaEnviar;
	}
	public Boolean getVerSeleccionAlmacenada() {
		return verSeleccionAlmacenada;
	}
	public void setVerSeleccionAlmacenada(Boolean verSeleccionAlmacenada) {
		this.verSeleccionAlmacenada = verSeleccionAlmacenada;
	}

	public Boolean getTieneArchivosCdProcu() {
		List<FileCorridaVO> files = this.getProcesoMasivo().getListFileCorridaRealizarEnvio();
		for(FileCorridaVO file: files) {
			if (file.getFileName().indexOf("cdproc_") >= 0) return true;
		}
		
		return false;
	}

	public void setVerPlanillasEnabled(Boolean verPlanillasEnabled) {
		this.verPlanillasEnabled = verPlanillasEnabled;
	}

	public Boolean getVerPlanillasEnabled() {
		return verPlanillasEnabled;
	}

}
