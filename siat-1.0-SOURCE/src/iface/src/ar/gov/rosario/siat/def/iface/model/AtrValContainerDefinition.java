//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import coop.tecso.demoda.iface.model.BussImageModel;

/**
 * Contenedor de Atributos con Valor
 * @author Tecso Coop. Ltda.
 *
 */
public class AtrValContainerDefinition extends BussImageModel {

	private static final long serialVersionUID = 1L;

	private List<AtrValDefinition> listAtrVal = new ArrayList<AtrValDefinition>();
	
	/**
	 * @return la lista de AtrVal
	 */
	public List<AtrValDefinition> getListAtrVal() {
		return listAtrVal;
	}
	/**
	 * @param listAtrVal the listAtrVal to set
	 */
	public void setListAtrVal(List<AtrValDefinition> listAtrVal) {
		this.listAtrVal = listAtrVal;
	}

	/**
	 * @param codigoAtributo codigo del atributo que se busca.
	 * @return Atributo por su codigo. Es case sensitive. Si no existe retorna null
	 */
	public AtrValDefinition getAtrValByCodigo(String codigoAtributo) {
		return null;
	}
	
	/**
	 * @param id id del atributo que se busca.
	 * @return Atributo por su id. Si no existe retorna null. 
	 */
	public AtrValDefinition getAtrValById(Long id) {
		return null;
	}
}
