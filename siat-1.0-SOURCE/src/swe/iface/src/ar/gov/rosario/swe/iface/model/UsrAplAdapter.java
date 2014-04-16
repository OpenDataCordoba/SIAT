//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.Estado;

public class UsrAplAdapter extends SweAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "usrAplAdapterVO";
	
	public UsrAplVO usrApl = new UsrAplVO();
	
	private List listAplicaciones = new ArrayList<AplicacionVO>();
	
	private Long[] listIdsAppSelected;
	
	private List<PermiteWeb> listPermiteWeb = new ArrayList<PermiteWeb>();
	
	public UsrAplAdapter(){
		super();
	}

	public UsrAplAdapter(AplicacionVO aplicacionVO){
		this();
		this.usrApl.setAplicacion(aplicacionVO);
	}
	
	public UsrAplAdapter(UsrAplVO usrAplVO){
		this();
		this.usrApl = usrAplVO;
	}

	public UsrAplVO getUsrApl() {
		return usrApl;
	}
	
	public void setUsrApl(UsrAplVO usrApl) {
		this.usrApl = usrApl;
	}

	public List getListEstado() {
		return java.util.Arrays.asList(Estado.ACTIVO, Estado.INACTIVO);
		//return java.util.Arrays.asList(new EstadoVO(0, "InActivo"), new EstadoVO(1, "Activo"));
	}


	public List getListAplicaciones() {
		return listAplicaciones;
	}

	public void setListAplicaciones(List listAplicaciones) {
		this.listAplicaciones = listAplicaciones;
	}


	public Long[] getListIdsAppSelected() {
		return listIdsAppSelected;
	}

	public void setListIdsAppSelected(Long[] listIdsAppSelected) {
		this.listIdsAppSelected = listIdsAppSelected;
	}

	public List<PermiteWeb> getListPermiteWeb() {
		return listPermiteWeb;
	}

	public void setListPermiteWeb(List<PermiteWeb> listPermiteWeb) {
		this.listPermiteWeb = listPermiteWeb;
	}
}
