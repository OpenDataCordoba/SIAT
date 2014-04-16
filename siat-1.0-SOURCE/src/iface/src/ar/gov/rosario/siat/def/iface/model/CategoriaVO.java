//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Categoria
 * @author tecso
 *
 */
public class CategoriaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "categoriaVO";
	
	private String desCategoria;
	private TipoVO tipo = new TipoVO();
	
	// Constructores
	public CategoriaVO() {
		super();
	}
	public CategoriaVO(int id, String desCategoria) {
		super(id);
		setDesCategoria(desCategoria);
	}

	// Getters y Setters
	public String getDesCategoria() {
		return desCategoria;
	}
	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}
	public TipoVO getTipo() {
		return tipo;
	}
	public void setTipo(TipoVO tipo) {
		this.tipo = tipo;
	}
	
}
