//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;

/**
 * Value Object del TipoActa
 * @author tecso
 *
 */
public class TipoActaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoActaVO";
	
	private FormularioVO formulario = new FormularioVO();
	
	private String desTipoActa = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoActaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoActaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoActa(desc);
	}
	
	// Getters y Setters
	public FormularioVO getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioVO formulario) {
		this.formulario = formulario;
	}

	public String getDesTipoActa() {
		return desTipoActa;
	}

	public void setDesTipoActa(String desTipoActa) {
		this.desTipoActa = desTipoActa;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
