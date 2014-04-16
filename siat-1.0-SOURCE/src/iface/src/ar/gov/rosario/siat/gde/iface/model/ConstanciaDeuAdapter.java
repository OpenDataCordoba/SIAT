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
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ConstanciaDeu
 * 
 * @author tecso
 */
public class ConstanciaDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "constanciaDeuAdapterVO";
	public static final String ENC_NAME = "encConstanciaDeuAdapterVO";
	
    private ConstanciaDeuVO constanciaDeu = new ConstanciaDeuVO();
    private RecursoVO recurso = new RecursoVO();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<SiNo> listSiNo = new ArrayList<SiNo>();
   
	private List<ConstanciaDeuVO> listConstanciaDeu = new ArrayList<ConstanciaDeuVO>();// Se usa en la impresion
	// flags permisos
	private boolean eliminarConDeuDetBussEnabled=true;
	private boolean modificarConDeuDetBussEnabled=true;
	private String agregarConDeuDetBussEnabled = "";
	
    // Constructores
    public ConstanciaDeuAdapter(){
    	super(GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL);
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL;
    }
    
    //  Getters y Setters
	public ConstanciaDeuVO getConstanciaDeu() {
		return constanciaDeu;
	}

	public void setConstanciaDeu(ConstanciaDeuVO constanciaDeuVO) {
		this.constanciaDeu = constanciaDeuVO;
	}


	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<ConstanciaDeuVO> getListConstanciaDeu() {
		return listConstanciaDeu;
	}

	public void setListConstanciaDeu(List<ConstanciaDeuVO> listConstanciaDeu) {
		this.listConstanciaDeu = listConstanciaDeu;
	}

	// View getters
	public String getModificarDomicilioEnvioEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getAgregarDomicilioEnvioEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.AGREGAR);
	}

	public String getEliminarTitularEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ADM_TITULARES_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getAgregarTitularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ADM_TITULARES_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.AGREGAR);
	}
	
	public String getAgregarConDeuDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_CONDEUDET, BaseSecurityConstants.AGREGAR);
	}
	
	
	public String getModificarConDeuDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_CONDEUDET, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getEliminarConDeuDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_CONDEUDET, BaseSecurityConstants.ELIMINAR);
	}

	public boolean getEliminarConDeuDetBussEnabled() {
		return eliminarConDeuDetBussEnabled;
	}

	public void setEliminarConDeuDetBussEnabled(boolean eliminarConDeuDetBussEnabled) {
		this.eliminarConDeuDetBussEnabled = eliminarConDeuDetBussEnabled;
	}

	public String getAgregarConDeuDetBussEnabled() {
		return agregarConDeuDetBussEnabled;
	}

	public void setAgregarConDeuDetBussEnabled(String agregarConDeuDetBussEnabled) {
		this.agregarConDeuDetBussEnabled = agregarConDeuDetBussEnabled;
	}

	public boolean isModificarConDeuDetBussEnabled() {
		return modificarConDeuDetBussEnabled;
	}

	public void setModificarConDeuDetBussEnabled(
			boolean modificarConDeuDetBussEnabled) {
		this.modificarConDeuDetBussEnabled = modificarConDeuDetBussEnabled;
	}
	

}
