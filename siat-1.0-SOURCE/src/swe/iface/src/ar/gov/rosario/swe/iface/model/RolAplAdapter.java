//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.CommonKey;

public class RolAplAdapter extends SweAdapterModel {
	
	private static final long serialVersionUID = -1360366611195214212L;
	public static final String NAME = "rolAplAdapterVO";
	
	private RolAplVO 				rolApl = new RolAplVO();
	private List<PermiteWeb> listPermiteWeb = new ArrayList<PermiteWeb>();
	
		
	// business flags
	// Aca deberian definirse los flags de negocio, pero en este caso no hay.
	
	
	public RolAplAdapter() {
        super();
    }

	/**
	 * @param servicio
	 */
	public RolAplAdapter(CommonKey rolKey) {
		super();
		this.rolApl = new RolAplVO();
		
	}

	/**
	 * @param servicio
	 */
	public RolAplAdapter(RolAplVO RolVO) {
		super();
		this.rolApl = RolVO;
	}
	

	// ------------------------ Getters y Setters --------------------------------//
	public RolAplVO getRolApl() {
		return rolApl;
	}

	public void setRolApl(RolAplVO rolApl) {
		this.rolApl = rolApl;
	}

		
	//	------------------------ control de acceso a botones --------------------------------//
	// getModificarEstadoEnabled()
	//-------------------------------------------------------------------------------------//

	public String getModificarEstadoEnabled() {
		return ENABLED;
	}

	public List<PermiteWeb> getListPermiteWeb() {
		return listPermiteWeb;
	}

	public void setListPermiteWeb(List<PermiteWeb> listPermiteWeb) {
		this.listPermiteWeb = listPermiteWeb;
	}
}
