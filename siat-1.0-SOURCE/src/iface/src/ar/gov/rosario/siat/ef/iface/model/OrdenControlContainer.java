//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class OrdenControlContainer extends SiatBussImageModel {
		
	private static final long serialVersionUID = 1L;
	private List<OrdenControlVO> listOrdenControlVO = new ArrayList<OrdenControlVO>();

	public List<OrdenControlVO> getListOrdenControl() {
		return listOrdenControlVO;
	}

	public void setListOrdenControl(List<OrdenControlVO> listOrdenControlVO) {
		this.listOrdenControlVO = listOrdenControlVO;
	}
	
	
}
