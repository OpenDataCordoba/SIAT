package ar.gov.rosario.siat.${modulo}.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del ${Bean}
 * @author tecso
 *
 */
public class ${Bean}VO extends OtecBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "${bean}VO";
	
	private String propiedad_ejemplo;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ${Bean}VO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ${Bean}VO(int id, String desc) {
		super();
		setId(new Long(id));
		setDes${Bean}(desc);
	}
	
	// Getters y Setters
	public String getPropiedad_ejemplo() {
		return propiedad_ejemplo;
	}
	public void setPropiedad_ejemplo(String propiedad_ejemplo) {
		this.propiedad_ejemplo = propiedad_ejemplo;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
