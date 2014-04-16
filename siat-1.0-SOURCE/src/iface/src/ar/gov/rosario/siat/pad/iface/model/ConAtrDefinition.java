//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.logging.Logger;

import ar.gov.rosario.siat.def.iface.model.AtrValDefinition;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.ConAtrVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Especializacion de Atributo para 
 * Compone con la clase existente ConAtrVO
 * 
 * @author Tecso Coop. Ltda.
 *
 */
public class ConAtrDefinition extends AtrValDefinition {

	Logger log = Logger.getLogger(ConAtrDefinition.class.getName()); 
	
	private static final long serialVersionUID = 1L;

	private ConAtrVO conAtr = null;

	public ConAtrVO getConAtr() {
		return conAtr;
	}

	public void setConAtr(ConAtrVO conAtr) {
		this.conAtr = conAtr;
	}

	/**	
	 * Indica si el atributo es multivalor.
	 * @return true si es multivalor
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getEsMultivalor()
	 */
	@Override
	public boolean getEsMultivalor() {
		return false;
	}
	
	/**	
	 * Indica si el atributo es requerido.
	 * @return true si es requerido
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getEsRequerido()
	 */
	@Override
	public boolean getEsRequerido() {
		return true;
	}
	
	/**	
	 * Indica si el atributo esta marcado como que posee vigencia.
	 * @return true si posee vigencia
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getPoseeVigencia()
	 */
	@Override
	public boolean getPoseeVigencia() {
		return true;
	}

	/**	
	 * Indica si el atributo Adminite Busquedas por Rango
	 * @return true si admite busquedas.
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getAdmBusPorRan()
	 */
	@Override
	public boolean getAdmBusPorRan() {
		if ( this.getConAtr() != null && this.getConAtr().getAdmBusPorRan().equals(SiNo.SI) )
			return true;
		else
			return false;
	}
	
	/**
	 * Informacion del Atributo. e.g: Tipo de Atributo, Descripcion, Codigo,
	 * Dominio, Valores del Dominico, etc.
	 * @return the atributo
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getAtributo()
	 */
	@Override
	public AtributoVO getAtributo() {
		return this.getConAtr().getAtributo();
	}
	
	@Override
	public Long getIdDefinition() {
		return this.getConAtr().getId();
	}

}
