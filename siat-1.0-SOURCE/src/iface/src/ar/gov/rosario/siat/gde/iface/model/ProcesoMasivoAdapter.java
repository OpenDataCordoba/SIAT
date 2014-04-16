//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.SiNoLuego;

/**
 * Adapter de Proceso Masivo
 * 
 * @author tecso
 */
public class ProcesoMasivoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoMasivoAdapterVO";
	
    private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    private List<SiNo> listConCuentaExcSel = new ArrayList<SiNo>(); // para la seleccion de conCuentaExcSel  
    
    private List<SiNo> listUtilizaCriterio = new ArrayList<SiNo>(); // para la seleccion de utilizaCriterio

    private List<SiNoLuego> listGeneraConstancia = new ArrayList<SiNoLuego>(); // para la seleccion de generacion constancia
    
    private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();

    private List<FormularioVO> listFormulario = new ArrayList<FormularioVO>(); // lista de formularios VO

    private List<FormatoSalida> listFormatoSalida = new ArrayList<FormatoSalida>(); 
    
    private Boolean mostrarListForCam = false;
    
    private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();

    private List<ProcesoMasivoVO> listProcesosProcurador = new ArrayList<ProcesoMasivoVO>();

    
    public ProcesoMasivoAdapter(){
    	// setea ACCION_AGREGAR, ACCION_MODIFICAR, ACCION_ELIMINAR, ACCION_VER, ACCION_ACTIVAR, ACCION_DESACTIVAR
    	super(GdeSecurityConstants.ABM_PROCESO_MASIVO);    	
    }

    // Getters y setters
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}
	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}
	public List<FormularioVO> getListFormulario() {
		return listFormulario;
	}
	public void setListFormulario(List<FormularioVO> listFormulario) {
		this.listFormulario = listFormulario;
	}
	
	public List<FormatoSalida> getListFormatoSalida() {
		return listFormatoSalida;
	}

	public void setListFormatoSalida(List<FormatoSalida> listFormatoSalida) {
		this.listFormatoSalida = listFormatoSalida;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<SiNo> getListConCuentaExcSel() {
		return listConCuentaExcSel;
	}
	public void setListConCuentaExcSel(List<SiNo> listConCuentaExcSel) {
		this.listConCuentaExcSel = listConCuentaExcSel;
	}
	public List<SiNo> getListUtilizaCriterio() {
		return listUtilizaCriterio;
	}
	public void setListUtilizaCriterio(List<SiNo> listUtilizaCriterio) {
		this.listUtilizaCriterio = listUtilizaCriterio;
	}
	public Boolean getMostrarListForCam() {
		return mostrarListForCam;
	}
	public void setMostrarListForCam(Boolean mostrarListForCam) {
		this.mostrarListForCam = mostrarListForCam;
	}
	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}
	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public void setListProcesosProcurador(List<ProcesoMasivoVO> listProcesosProcurador) {
		this.listProcesosProcurador = listProcesosProcurador;
	}

	public List<ProcesoMasivoVO> getListProcesosProcurador() {
		return listProcesosProcurador;
	}

	public void setListGeneraConstancia(List<SiNoLuego> listGeneraConstancia) {
		this.listGeneraConstancia = listGeneraConstancia;
	}

	public List<SiNoLuego> getListGeneraConstancia() {
		return listGeneraConstancia;
	}
	
}
