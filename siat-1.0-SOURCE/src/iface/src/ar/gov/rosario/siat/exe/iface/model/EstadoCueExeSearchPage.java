//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.model.TipoEstadoCueExe;

/**
 * SearchPage del EstadoCueExe
 * 
 * @author Tecso
 *
 */
public class EstadoCueExeSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoCueExeSearchPageVO";
	
	private EstadoCueExeVO estadoCueExe= new EstadoCueExeVO();
	
	private List<TipoEstadoCueExe> listTipoEstadoCueExe = new ArrayList<TipoEstadoCueExe>();

	private TipoEstadoCueExe tipoEstadoCueExe=TipoEstadoCueExe.SELECCIONAR;
	
	public TipoEstadoCueExe getTipoEstadoCueExe() {
		return tipoEstadoCueExe;
	}
	public void setTipoEstadoCueExe(TipoEstadoCueExe tipoEstadoCueExe) {
		this.tipoEstadoCueExe = tipoEstadoCueExe;
	}
	
	// Constructores
	public EstadoCueExeSearchPage() {       
       super(ExeSecurityConstants.ABM_ESTADOCUEEXE);        
    }
	
	// Getters y Setters
	public EstadoCueExeVO getEstadoCueExe() {
		return estadoCueExe;
	}
	public void setEstadoCueExe(EstadoCueExeVO estadoCueExe) {
		this.estadoCueExe = estadoCueExe;
	}

	public List<TipoEstadoCueExe> getListTipoEstadoCueExe() {
		return listTipoEstadoCueExe;
	}

	public void setListTipoEstadoCueExe(List<TipoEstadoCueExe> listEstadoCueExe) {
		this.listTipoEstadoCueExe = listEstadoCueExe;
	}

	// View getters
}
