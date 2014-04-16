//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.LongVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Adapter de Servicio Banco
 * 
 * @author tecso
 */
public class ServicioBancoAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "servicioBancoAdapterVO";
	public static final String ENC_NAME = "encServicioBancoAdapterVO";
	
	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
	
	private List<LongVO> listTipoAsentamiento = new ArrayList<LongVO>();
	
	private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
	
	private List<SiNo> listSiNo= new ArrayList<SiNo>();
	
	public ServicioBancoAdapter(){
		super(DefSecurityConstants.ABM_SERVICIO_BANCO);
		ACCION_MODIFICAR_ENCABEZADO = DefSecurityConstants.ABM_SERVICIO_BANCO_ENC;
	}
	
	// Getters y Setters
	public ServicioBancoVO getServicioBanco(){
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBancoVO servicioBanco){
		this.servicioBanco = servicioBanco;
	}
	public List<PartidaVO> getListPartida() {
		return listPartida;
	}
	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}
	public List<LongVO> getListTipoAsentamiento() {
		return listTipoAsentamiento;
	}
	public void setListTipoAsentamiento(List<LongVO> listTipoAsentamiento) {
		this.listTipoAsentamiento = listTipoAsentamiento;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	// Permisos para ABM SerBanRec
	public String getVerSerBanRecEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO, BaseSecurityConstants.VER);
	}
	public String getModificarSerBanRecEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarSerBanRecEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarSerBanRecEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO, BaseSecurityConstants.AGREGAR);
	}
	// Permisos para ABM SerBanDesGen
	public String getVerSerBanDesGenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES, BaseSecurityConstants.VER);
	}
	public String getModificarSerBanDesGenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarSerBanDesGenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarSerBanDesGenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES, BaseSecurityConstants.AGREGAR);
	}
	
}
