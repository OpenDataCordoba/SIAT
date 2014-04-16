//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;


/**
 * Value Object del SiatScript
 * @author tecso
 *
 */
public class SiatScriptVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "siatScriptVO";
	
	private String codigo;
	
	private String descripcion;

	private String path;
	
	private List<SiatScriptUsrVO> listSiatScriptUsr;
	
	private String scriptFile;
		
		
	// Constructores
	public SiatScriptVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public SiatScriptVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setListSiatScriptUsr(List<SiatScriptUsrVO> listSiatScriptUsr) {
		this.listSiatScriptUsr = listSiatScriptUsr;
	}

	public List<SiatScriptUsrVO> getListSiatScriptUsr() {
		return listSiatScriptUsr;
	}

	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile;
	}

	public String getScriptFile() {
		return scriptFile;
	}



}
