//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de DisParDet
 * 
 * @author tecso
 */
public class DisParDetAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "disParDetAdapterVO";

	DisParDetVO disParDet = new DisParDetVO();
	
	private List<RecConVO> listRecCon = new ArrayList<RecConVO>();
	private List<TipoImporteVO> listTipoImporte = new ArrayList<TipoImporteVO>();
	private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
	
	private List<SiNo> listSiNo= new ArrayList<SiNo>();
	
	//Flags
	private boolean paramTipoImporte = false;
	private boolean paramRecNoTrib = false;

	public DisParDetAdapter(){
		super(BalSecurityConstants.ABM_DISPARDET);
	}

	// Getters y Setters
	public DisParDetVO getDisParDet() {
		return disParDet;
	}
	public void setDisParDet(DisParDetVO disParDet) {
		this.disParDet = disParDet;
	}
	public List<RecConVO> getListRecCon() {
		return listRecCon;
	}
	public void setListRecCon(List<RecConVO> listRecCon) {
		this.listRecCon = listRecCon;
	}
	public List<TipoImporteVO> getListTipoImporte() {
		return listTipoImporte;
	}
	public void setListTipoImporte(List<TipoImporteVO> listTipoImporte) {
		this.listTipoImporte = listTipoImporte;
	}
	public List<PartidaVO> getListPartida() {
		return listPartida;
	}
	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}
	public boolean isParamTipoImporte() {
		return paramTipoImporte;
	}
	public void setParamTipoImporte(boolean paramTipoImporte) {
		this.paramTipoImporte = paramTipoImporte;
	}
	public boolean isParamRecNoTrib() {
		return paramRecNoTrib;
	}
	public void setParamRecNoTrib(boolean paramRecNoTrib) {
		this.paramRecNoTrib = paramRecNoTrib;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

}
