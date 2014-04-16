//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

public class AtrEmisionDefinition  {

	private static final long serialVersionUID = 1L;

	// Atributos Planos
	private List<GenericAtrDefinition> listAtributosEmision   = new ArrayList<GenericAtrDefinition>();
	
	// Modelamos la tabla de atributos como una lista de listas de Atributos
	private List<List<GenericAtrDefinition>> tablaAtributos = new ArrayList<List<GenericAtrDefinition>>();

	public List<GenericAtrDefinition> getListAtributosEmision() {
		return listAtributosEmision;
	}

	public void setListAtributosEmision(
			List<GenericAtrDefinition> listAtributosEmision) {
		this.listAtributosEmision = listAtributosEmision;
	}
	
	public List<List<GenericAtrDefinition>> getTablaAtributos() {
		return tablaAtributos;
	}

	public void setTablaAtributos(List<List<GenericAtrDefinition>> tablaAtributos) {
		this.tablaAtributos = tablaAtributos;
	}

	/**
	 * Busca el GenericAtrDefinition en la lista por el idAtributo pasado como parametro
	 * @param idAtributo
	 * @return null si no lo encuentra
	 */
	public GenericAtrDefinition getGenericAtrDefinition(Long idAtributo){
		if(listAtributosEmision != null){
			for(GenericAtrDefinition def: listAtributosEmision){
				if(def.getAtributo().getId().longValue()==idAtributo.longValue())
					return def;
			}
		}
		return null;
	}

	/**
	 * Busca el GenericAtrDefinition en la lista por el codigo 
	 * pasado como parametro
	 * 
	 * @param codAtributo
	 * @return null si no lo encuentra
	 */
	public GenericAtrDefinition getGenericAtrDefinition(String codAtributo){
		if (listAtributosEmision != null) {
			for(GenericAtrDefinition def: listAtributosEmision){
				if (def.getAtributo().getCodAtributo().equals(codAtributo)) { 
					return def;
				}
			}
		}
		return null;
	}
	
	/**
	 * Busca el valor de un atributo en el definition 
 	 * por su codigo. 
	 * 
	 * @param codAtributo
	 * @return null si no lo encuentra
	 */
	public Object getValor(String codAtributo){
		if (listAtributosEmision != null) {
			for (GenericAtrDefinition def: listAtributosEmision) {
				if(def.getAtributo().getCodAtributo().equals(codAtributo)) {
					return def.convertFromDB(def.getValorString());
				}
			}
		}
		return null;
	}

	/**
	 * Retorna el encabezado de la tabla  
	 */
	public List<String> getHeaderFromTabla() {
		List<String> listHedaer = new ArrayList<String>();
		
		for (GenericAtrDefinition atr: tablaAtributos.get(0)) {
			listHedaer.add(atr.getAtributo().getDesAtributo());
		} 
		
		return listHedaer;
	}
	
	/**
	 * Retorna las filas de la tabla  
	 */
	public List<List<GenericAtrDefinition>> getListRowFromTabla() {
		return tablaAtributos;
	}
	
	public void addAtributo(GenericAtrDefinition atributo) {
		this.getListAtributosEmision().add(atributo);
	}
}
