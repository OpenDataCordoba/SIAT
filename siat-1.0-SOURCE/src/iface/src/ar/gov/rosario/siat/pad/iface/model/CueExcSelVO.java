//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;

public class CueExcSelVO extends SiatBussImageModel {
	private static final long serialVersionUID = 1L;
	
	private CuentaVO cuenta = new CuentaVO();
	private AreaVO   area   = new AreaVO();

	
	
	private List<CueExcSelDeuVO> listCueExcSelDeu = new ArrayList<CueExcSelDeuVO>();
								 
	// Constructores
	public CueExcSelVO() {
		super();
	}

	// Getters y Setters
	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}
	
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public List<CueExcSelDeuVO> getListCueExcSelDeu() {
		return listCueExcSelDeu;
	}

	public void setListCueExcSelDeu(List<CueExcSelDeuVO> listCueExcSelDeu) {
		this.listCueExcSelDeu = listCueExcSelDeu;
	}
	
}
