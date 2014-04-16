//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.DesImpVO;
import ar.gov.rosario.siat.frm.iface.util.FrmSecurityConstants;

/**
 * Adapter del Formulario
 * 
 * @author tecso
 */
public class FormularioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "formularioAdapterVO";
	
	public static final String ENC_NAME = "encFormularioAdapterVO";
	
    private FormularioVO formulario = new FormularioVO();
    
    private List<DesImpVO> listDesImp = new ArrayList<DesImpVO>();
    
    private List<FormatoSalida> listFormatoSalida = new ArrayList<FormatoSalida>();
    
    private String  idFormatoSalidaSelec = null;
    
    // Constructores
    public FormularioAdapter(){
    	super(FrmSecurityConstants.ABM_FORMULARIO);
    	ACCION_MODIFICAR_ENCABEZADO = FrmSecurityConstants.ABM_FORMULARIO_ENC;
    }
    
    
	// Permisos para ABM ForCam
	public String getVerForCamEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.VER);
	}
	public String getModificarForCamEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarForCamEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarForCamEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(FrmSecurityConstants.ABM_FORCAM, BaseSecurityConstants.AGREGAR);
	}

    //  Getters y Setters
	public FormularioVO getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioVO formularioVO) {
		this.formulario = formularioVO;
	}

	public List<DesImpVO> getListDesImp() {
		return listDesImp;
	}

	public void setListDesImp(List<DesImpVO> listDesImp) {
		this.listDesImp = listDesImp;
	}

	public List<FormatoSalida> getListFormatoSalida() {
		return listFormatoSalida;
	}
	public void setListFormatoSalida(List<FormatoSalida> listFormatoSalida) {
		this.listFormatoSalida = listFormatoSalida;
	}
	public String getIdFormatoSalidaSelec() {
		return idFormatoSalidaSelec;
	}
	public void setIdFormatoSalidaSelec(String idFormatoSalidaSelec) {
		this.idFormatoSalidaSelec = idFormatoSalidaSelec;
	}
	
	// View getters
}
