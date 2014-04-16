//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del TipoMotor
 * @author tecso
 *
 */
public class TipoMotorVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoMotorVO";
	
	private Integer codTipoMotor;
	private String desTipoMotor="";
	
	// Buss Flags
	
	
	// View Constants
	private String codTipoMotorView="";
	
	// Constructores
	public TipoMotorVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoMotorVO(Integer cod, String desc) {
		super();
		setCodTipoMotor(cod);
		setDesTipoMotor(desc);
	}

	// Getters y Setters
	
	public Integer getCodTipoMotor() {
		return codTipoMotor;
	}

	public void setCodTipoMotor(Integer codTipoMotor) {
		this.codTipoMotor = codTipoMotor;
		this.codTipoMotorView = StringUtil.formatInteger(codTipoMotor);
	}

	public String getDesTipoMotor() {
		return desTipoMotor;
	}

	public void setDesTipoMotor(String desTipoMotor) {
		this.desTipoMotor = desTipoMotor;
	}

	// View getters
	
	public String getCodTipoMotorView() {
		return codTipoMotorView;
	}

	public void setCodTipoMotorView(String codTipoMotorView) {
		this.codTipoMotorView = codTipoMotorView;
	}

	public String getTipoMotorView(){
		return getCodTipoMotorView()+" - "+getDesTipoMotor();
	}
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	
}
