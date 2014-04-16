//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adaptador de la Cuenta
 * @author Tecso Coop. Ltda.
 */
public class CuentaAdapter extends SiatAdapterModel {

	
	private static final long serialVersionUID = 1L;
	public static final String NAME     = "cuentaAdapterVO";
	public static final String ENC_NAME = "encCuentaAdapterVO";	
	public static final String REL_NAME = "relacionarCuentaAdapterVO";
	
	private CuentaVO cuenta = new CuentaVO();
	
	private List<CuentaRelVO> listCuentaRel = new ArrayList<CuentaRelVO>(); 
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>(); // utilizada en la creacion 
	
	private List<SiNo>   listSiNo = SiNo.getList(SiNo.OpcionSelecionar);  
			   // es bis o no utilizada en la creacion de cta y modificacion de domicilio de envio
	
	private boolean esCMD = false;
	
	private boolean poseeDatosPersona = true;
	private PersonaVO titular = new PersonaVO();

	private boolean buscarObjImpEnabled = true;
	private boolean numeroCuentaEnabled = true;
	private boolean codGesCueEnabled = true;
	
	private boolean comboRecursoEnabled = false;
	
	private boolean modificarTitularEnabled = true;
	
	// Construtores
	public CuentaAdapter() {
		super(PadSecurityConstants.ABM_CUENTA);
    	ACCION_MODIFICAR_ENCABEZADO = PadSecurityConstants.ABM_CUENTA_ENC;		
	}
	
	
	// Getters y Setters
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public boolean isPoseeDatosPersona() {
		return poseeDatosPersona;
	}
	public void setPoseeDatosPersona(boolean poseeDatosPersona) {
		this.poseeDatosPersona = poseeDatosPersona;
	}

	public PersonaVO getTitular() {
		return titular;
	}
	public void setTitular(PersonaVO titular) {
		this.titular = titular;
	}
	
	public boolean isBuscarObjImpEnabled() {
		return buscarObjImpEnabled;
	}
	public void setBuscarObjImpEnabled(boolean buscarObjImpEnabled) {
		this.buscarObjImpEnabled = buscarObjImpEnabled;
	}

	public boolean isNumeroCuentaEnabled() {
		return numeroCuentaEnabled;
	}
	public void setNumeroCuentaEnabled(boolean numeroCuentaEnabled) {
		this.numeroCuentaEnabled = numeroCuentaEnabled;
	}

	public boolean isCodGesCueEnabled() {
		return codGesCueEnabled;
	}
	public void setCodGesCueEnabled(boolean codGesCueEnabled) {
		this.codGesCueEnabled = codGesCueEnabled;
	}

	public boolean isEsCMD() {
		return esCMD;
	}
	public void setEsCMD(boolean esCMD) {
		this.esCMD = esCMD;
	}
	
	public List<CuentaRelVO> getListCuentaRel() {
		return listCuentaRel;
	}
	public void setListCuentaRel(List<CuentaRelVO> listCuentaRel) {
		this.listCuentaRel = listCuentaRel;
	}


	public boolean isComboRecursoEnabled() {
		return comboRecursoEnabled;
	}
	public void setComboRecursoEnabled(boolean comboRecursoEnabled) {
		this.comboRecursoEnabled = comboRecursoEnabled;
	}

	public boolean isModificarTitularEnabled() {
		return modificarTitularEnabled;
	}
	public void setModificarTitularEnabled(boolean modificarTitularEnabled) {
		this.modificarTitularEnabled = modificarTitularEnabled;
	}


	// Metodos para la seguridad en la vista de las cuentas titular
	public String getVerCuentaTitularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTATITULAR, BaseSecurityConstants.VER);
	}
	
	public String getModificarCuentaTitularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTATITULAR, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarCuentaTitularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTATITULAR, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarCuentaTitularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTATITULAR, BaseSecurityConstants.AGREGAR);
	}
	public String getMarcarPrincipalEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTATITULAR, PadSecurityConstants.MTD_MARCAR_PRINCIPAL);
	}
	
	// Metodos para la seguridad en la vista del Domicilio de envio
	
	public String getModificarDomicilioEnvioEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTA_DOMICILIO_ENV, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getAgregarDomicilioEnvioEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTA_DOMICILIO_ENV, BaseSecurityConstants.AGREGAR);
	}

	// Metodos para la seguridad en la vista de los broches
	public String getAsignarBrocheEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_BROCHE, PadSecurityConstants.ABM_BROCHE_ADM_BROCHE_CUENTA);
	}

	public boolean getPoseeBroche() {
		return !ModelUtil.isNullOrEmpty(getCuenta().getBroche());
	}
	
	
	
	public String getVerRecAtrCueVEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_RECATRCUEV, BaseSecurityConstants.VER);
	}
	
	public String getModificarRecAtrCueVEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_RECATRCUEV, BaseSecurityConstants.MODIFICAR);
	}
		
	public String getAgregarRecAtrCueEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_RECATRCUEV, BaseSecurityConstants.AGREGAR);
	}
	
	//	 Metodos para la seguridad en la vista de las cuentas relacionadas
	public String getVerCuentaRelEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTAREL, BaseSecurityConstants.VER);
	}
	
	public String getModificarCuentaRelEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTAREL, BaseSecurityConstants.MODIFICAR);
	}

	public String getAgregarCuentaRelEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTAREL, BaseSecurityConstants.AGREGAR);
	}
	
	public String getEliminarCuentaRelEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_CUENTAREL, BaseSecurityConstants.ELIMINAR);
	}
}
