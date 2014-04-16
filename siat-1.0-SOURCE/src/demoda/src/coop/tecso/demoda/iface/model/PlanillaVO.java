//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.io.File;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * @author tecso
 *
 */
public class PlanillaVO extends BussImageModel {
	private static final long serialVersionUID = 0;

	private String fileName = "";
	private Long   ctdResultados = 0L;
	private String descripcion = "";
	private String titulo = "";
	
	public PlanillaVO() {
		super();
	}
	
	public PlanillaVO(String fileName) {
		this.fileName = fileName;
	}

	public Long getCtdResultados() {
		return ctdResultados;
	}
	public void setCtdResultados(Long ctdResultados) {
		this.ctdResultados = ctdResultados;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCtdResultadosView(){
		return StringUtil.formatLong(ctdResultados);
	}
	public String getFileNameView() {
		if(fileName == null) return "";
		int lastIndex = fileName.lastIndexOf(File.separatorChar);
		return fileName.substring(lastIndex + 1);
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
}
