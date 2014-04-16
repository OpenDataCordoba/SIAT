//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.logging.Logger;

import ar.gov.rosario.siat.def.iface.model.AtrValDefinition;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrVO;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Especializacion de Atributo para Objetos Imponibles
 * Compone con la clase existente TipObjImpAtrVO
 * 
 * @author Tecso Coop. Ltda.
 *
 */
public class TipObjImpAtrDefinition extends AtrValDefinition {

	Logger log = Logger.getLogger(TipObjImpAtrDefinition.class.getName()); 
	
	private static final long serialVersionUID = 1L;

	private TipObjImpAtrVO tipObjImpAtr = null;

	/**
	 * @return the tipObjImpAtr
	 */
	public TipObjImpAtrVO getTipObjImpAtr() {
		return tipObjImpAtr;
	}
	/**
	 * @param tipObjImpAtr the tipObjImpAtr to set
	 */
	public void setTipObjImpAtr(TipObjImpAtrVO tipObjImpAtr) {
		this.tipObjImpAtr = tipObjImpAtr;
	}

	/**	
	 * Indica si el atributo es multivalor.
	 * @return true si es multivalor
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getEsMultivalor()
	 */
	@Override
	public boolean getEsMultivalor() {
		if ( this.tipObjImpAtr != null && this.tipObjImpAtr.getEsMultivalor().equals(SiNo.SI) )
			return true;
		else
			return false;
	}
	
	/**	
	 * Indica si el atributo es requerido.
	 * @return true si es requerido
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getEsRequerido()
	 */
	@Override
	public boolean getEsRequerido() {
		if ( this.tipObjImpAtr != null && this.tipObjImpAtr.getEsRequerido().equals(SiNo.SI) )
			return true;
		else
			return false;
	}
	
	/**	
	 * Indica si el atributo esta marcado como que posee vigencia.
	 * @return true si posee vigencia
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getPoseeVigencia()
	 */
	@Override
	public boolean getPoseeVigencia() {
		if ( this.tipObjImpAtr != null && this.tipObjImpAtr.getPoseeVigencia().equals(SiNo.SI) )
			return true;
		else
			return false;
	}

	/**	
	 * Indica si el atributo Adminite Busquedas por Rango
	 * @return true si admite busquedas.
	 * @see ar.gov.rosario.siat.def.iface.model.AtrValDefinition#getAdmBusPorRan()
	 */
	@Override
	public boolean getAdmBusPorRan() {
		if ( this.tipObjImpAtr != null && this.tipObjImpAtr.getAdmBusPorRan().equals(SiNo.SI) )
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
		return this.tipObjImpAtr.getAtributo();
	}
	
	@Override
	public Long getIdDefinition() {
		return this.tipObjImpAtr.getId();
	}
	
	/**
	 * @return El valor Inicial del rango de Columna para el TipObjImpAtr asociado a este Definition.
	 */
	public Integer getPosColInt(){
		return tipObjImpAtr.getPosColInt();
	}
	
	/**
	 * @return El valor Final del rango de Columna para el TipObjImpAtr asociado a este Definition.
	 */
	public Integer getPosColIntHas(){
		return tipObjImpAtr.getPosColIntHas();
	}

	
	public boolean getEsClaveFuncional(){
		if ( this.tipObjImpAtr != null && this.tipObjImpAtr.getEsClaveFuncional().equals(SiNo.SI) )
			return true;
		else
			return false;
		
	}
	
	public boolean getEsCogGesCue(){
		if ( this.tipObjImpAtr != null && SiNo.SI.equals(this.tipObjImpAtr.getEsCodGesCue()))
			return true;
		else
			return false;
	}
	
	
	public boolean getEsClave(){
		if ( this.tipObjImpAtr != null && this.tipObjImpAtr.getEsClave().equals(SiNo.SI) )
			return true;
		else
			return false;
		
	}
	
	public boolean getEsClaveOClaveFuncional(){
		if(this.getEsClave() || this.getEsClaveFuncional())
			return true;
		else 
			return false;
	}
	
	
	/**
	 * Metodo para el seteo de permiso de modificacion en la vista.
	 * Solo se permiten modificar los atributos que no sean ninguna de las dos "Claves" 
	 * y que esten marcados como "Atributo Siat" 
	 * 
	 * @author Cristian
	 * @return
	 */
	public boolean getEsModificable(){
		if(this.getEsClave() || this.getEsClaveFuncional())
			return false;
		else {			
			if (getTipObjImpAtr().getEsAtributoSIAT().getEsSI())
				return true;
			else
				return false;
		}
	} 
	
	
	
	/**
	 * Metodo auxiliar que determina si posee valor cargado
	 * @return boolean
	 */
	public boolean poseeValorCargado(){
		return ( !this.getValorString().trim().equals("") && 
				!this.getValorString().trim().equals("-1")); 
	}

	/**	
	 * Indica si el atributo es visible en Siat.
	 * @return true si es visible
	 */
	public boolean getEsVisible() {
		//log.info(this.tipObjImpAtr.getEsVisible().getValue());
		if ( this.tipObjImpAtr != null && this.tipObjImpAtr.getEsVisible().equals(SiNo.SI) )
			return true;
		else
			return false;
	}
}
