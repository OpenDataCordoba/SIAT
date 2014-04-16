//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

public class OtrIngTesParAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "otrIngTesParAdapterVO";

	OtrIngTesParVO otrIngTesPar = new OtrIngTesParVO();
	
	private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
	
	// Constructores
	public OtrIngTesParAdapter(){
		super(BalSecurityConstants.ABM_OTRINGTESPAR);
	}

	// Getters Y Setters
	public OtrIngTesParVO getOtrIngTesPar() {
		return otrIngTesPar;
	}
	public void setOtrIngTesPar(OtrIngTesParVO otrIngTesPar) {
		this.otrIngTesPar = otrIngTesPar;
	}
	public List<PartidaVO> getListPartida() {
		return listPartida;
	}
	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}
	
}
