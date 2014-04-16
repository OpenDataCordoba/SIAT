//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;


/**
 * Adapter de Categoria
 * 
 * @author tecso
 */
public class CategoriaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "categoriaAdapterVO";
	
    private CategoriaVO categoria = new CategoriaVO();
    private List<TipoVO> listTipo = new ArrayList<TipoVO>();
    
    public CategoriaAdapter(){
    	super(DefSecurityConstants.ABM_CATEGORIA);    	
    }

    // Getters y setters
	public CategoriaVO getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaVO categoria) {
		this.categoria = categoria;
	}
	public List<TipoVO> getListTipo() {
		return listTipo;
	}
	public void setListTipo(List<TipoVO> listTipo) {
		this.listTipo = listTipo;
	}
		
}