//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.List;

import coop.tecso.demoda.iface.helper.ListUtil;

/**
 * Definicion y Valores de cualquier Atributo en General
 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition
 * @author Tecso Coop. Ltda.
 */
public class GenericAtrDefinition extends AtrValDefinition {

	private static final long serialVersionUID = 1L;

	private AtributoVO atributo = new AtributoVO();
	private boolean poseeVigencia = false;
	private boolean esRequerido = false;
	private boolean esMultivalor = false;
	
	
	/**
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getAdmBusPorRan()
	 */
	@Override
	public boolean getAdmBusPorRan() {
		
		return false;
	}
	
	/**
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getAtributo()
	 */
	@Override
	public AtributoVO getAtributo() {
		return this.atributo;
	}

	/**
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getEsMultivalor()
	 */
	@Override
	public boolean getEsMultivalor() {
		return this.esMultivalor;
	}
	
	public void setEsMultivalor(boolean esMultivalor){
		this.esMultivalor = esMultivalor;
	}
	
	/**
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getEsRequerido()
	 */
	@Override
	public boolean getEsRequerido() {
		return this.esRequerido;
	}

	public void setEsRequerido(boolean esRequerido) {
		this.esRequerido = esRequerido;
	}

	/**
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getPoseeVigencia()
	 */
	@Override
	public boolean getPoseeVigencia() {
		return this.poseeVigencia;
	}

	/**
	 * Indica que el Atributo posee vigencia.
	 */
	public void setPoseeVigencia(boolean poseeVigencia) {
		this.poseeVigencia=poseeVigencia;
	}	
	

	/**
	 * @param atributo the atributo to set
	 */
	public void setAtributo(AtributoVO atributo) {
		this.atributo = atributo;
	}

	@Override
	public Long getIdDefinition() {
		if(getAtributo()!= null){
				return getAtributo().getId();
		}else{
				return null;
		}
	}

	
	public void orderListAtrValVigByVal(){
		
		log.debug("orderListAtrValVigByVal");
		
		if (!ListUtil.isNullOrEmpty(getListAtrValVig())){
			
			List<AtrValVigDefinition> listAtrValVigForOrder = getListAtrValVig();
			
			if (listAtrValVigForOrder.size() > 1){ 
			
				for (int i=0; i < listAtrValVigForOrder.size() -1; i++ ){
					
					for (int j=i+1; j < listAtrValVigForOrder.size(); j++ ){
						
						AtrValVigDefinition atr1 = listAtrValVigForOrder.get(i);
						AtrValVigDefinition atr2 = listAtrValVigForOrder.get(j);
						
						if (atr1.getValor().compareTo(atr2.getValor()) > 0){
							listAtrValVigForOrder.set(i, atr2);
							listAtrValVigForOrder.set(j, atr1);
						}
					}
				}
			}
		}		
	}
}
