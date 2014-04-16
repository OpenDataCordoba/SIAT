//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

public class GenericDefinition extends AtrValDefinition {

	private static final long serialVersionUID = 1L;

	private List<GenericAtrDefinition> listGenericAtrDef = new ArrayList<GenericAtrDefinition>();
	
	@Override
	public boolean getEsMultivalor() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getEsRequerido() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getPoseeVigencia() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getAdmBusPorRan() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AtributoVO getAtributo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getIdDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<GenericAtrDefinition> getListGenericAtrDef() {
		return listGenericAtrDef;
	}

	public void setListGenericAtrDef(List<GenericAtrDefinition> listGenericAtrDef) {
		this.listGenericAtrDef = listGenericAtrDef;
	}

	/**
	 * Busca el GenericAtrDefinition en la lista por el idAtributo pasado como parametro
	 * @param idAtributo
	 * @return null si no lo encuentra
	 */
	public GenericAtrDefinition getGenericAtrDefinition(Long idAtributo){
		if(listGenericAtrDef!=null){
			for(GenericAtrDefinition def: listGenericAtrDef){
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
		if (listGenericAtrDef != null) {
			for(GenericAtrDefinition def: listGenericAtrDef){
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
		if (listGenericAtrDef != null) {
			for (GenericAtrDefinition def: listGenericAtrDef) {
				if(def.getAtributo().getCodAtributo().equals(codAtributo)) {
					return def.convertFromDB(def.getValorString());
				}
			}
		}
		return null;
	}
}
