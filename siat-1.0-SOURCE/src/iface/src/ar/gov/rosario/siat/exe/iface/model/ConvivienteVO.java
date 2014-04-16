//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Conviviente
 * @author tecso
 *
 */
public class ConvivienteVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "convivienteVO";
	
	private CueExeVO cueExe;
	
	private String convNombre;
	
	private String convTipodoc;
	
	private String convNrodoc;
	
	private String convParentesco;
	
	private Integer convEdad;

	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ConvivienteVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ConvivienteVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}

	// Getters y Setters
	public CueExeVO getCueExe() {
		return cueExe;
	}

	public void setCueExe(CueExeVO cueExe) {
		this.cueExe = cueExe;
	}

	public String getConvNombre() {
		return convNombre;
	}

	public void setConvNombre(String convNombre) {
		this.convNombre = convNombre;
	}

	public String getConvTipodoc() {
		return convTipodoc;
	}

	public void setConvTipodoc(String convTipodoc) {
		this.convTipodoc = convTipodoc;
	}

	public String getConvNrodoc() {
		return convNrodoc;
	}

	public void setConvNrodoc(String convNrodoc) {
		this.convNrodoc = convNrodoc;
	}

	public String getConvParentesco() {
		return convParentesco;
	}

	public void setConvParentesco(String convParentesco) {
		this.convParentesco = convParentesco;
	}

	public Integer getConvEdad() {
		return convEdad;
	}

	public void setConvEdad(Integer convEdad) {
		this.convEdad = convEdad;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getConvEdadView(){
		return StringUtil.formatInteger(convEdad);
	}
}
