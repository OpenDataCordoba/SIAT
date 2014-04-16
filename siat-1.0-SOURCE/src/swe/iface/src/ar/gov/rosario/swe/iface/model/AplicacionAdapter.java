//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter de Aplicacion
 * 
 * @author tecso
 */
public class AplicacionAdapter extends SweAdapterModel{
	
	public static final String NAME = "aplicacionAdapterVO";
	
    private AplicacionVO aplicacion = new AplicacionVO();
	private List<TipoAuthVO> 	listTipoAuth = new ArrayList<TipoAuthVO>();
    
    public AplicacionAdapter(){

    }

	public AplicacionVO getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(AplicacionVO aplicacionVO) {
		this.aplicacion = aplicacionVO;
	}

	public void setListTipoAuth(List<TipoAuthVO> listTipoAuth) {
		this.listTipoAuth = listTipoAuth;
	}

	public List<TipoAuthVO> getListTipoAuth() {
		return listTipoAuth;
	}

	
}
