//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import ar.gov.rosario.siat.rod.iface.util.RodSecurityConstants;

/**
 * SearchPage del Propietario
 * 
 * @author Tecso
 *
 */
public class PropietarioAdapter extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "propietarioAdapterVO";
	
	private PropietarioVO propietario= new PropietarioVO();
	
	private List<TipoDocVO> listTipoDoc = new ArrayList<TipoDocVO>();
    //private List<TipoSexoVO> listTipoSexo = new ArrayList<TipoSexoVO>();
	private List<Sexo> listSexo = Sexo.getList(Sexo.OpcionSeleccionar);
    private List<EstadoCivilVO> listEstadoCivil = new ArrayList<EstadoCivilVO>();
    private List<TipoPropietarioVO> listTipoPropietario = new ArrayList<TipoPropietarioVO>();
	
	// Constructores
	public PropietarioAdapter() {       
       super(RodSecurityConstants.ABM_PROPIETARIO);        
    }
	
	// Getters y Setters
	public PropietarioVO getPropietario() {
		return propietario;
	}
	public void setPropietario(PropietarioVO propietario) {
		this.propietario = propietario;
	}           

    public String getName(){    
		return NAME;
	}

	public List<TipoDocVO> getListTipoDoc() {
		return listTipoDoc;
	}

	public void setListTipoDoc(List<TipoDocVO> listTipoDoc) {
		this.listTipoDoc = listTipoDoc;
	}

	/*public List<TipoSexoVO> getListTipoSexo() {
		return listTipoSexo;
	}

	public void setListTipoSexo(List<TipoSexoVO> listTipoSexo) {
		this.listTipoSexo = listTipoSexo;
	}*/
	

	public List<EstadoCivilVO> getListEstadoCivil() {
		return listEstadoCivil;
	}

	public List<Sexo> getListSexo() {
		return listSexo;
	}

	public void setListSexo(List<Sexo> listSexo) {
		this.listSexo = listSexo;
	}

	public void setListEstadoCivil(List<EstadoCivilVO> listEstadoCivil) {
		this.listEstadoCivil = listEstadoCivil;
	}

	public List<TipoPropietarioVO> getListTipoPropietario() {
		return listTipoPropietario;
	}

	public void setListTipoPropietario(List<TipoPropietarioVO> listTipoPropietario) {
		this.listTipoPropietario = listTipoPropietario;
	}

    
	// View getters
}
