//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;

/**
 * Value Object del ConDeuTit
 * @author tecso
 *
 */
public class ConDeuTitVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "conDeuTitVO";
	
	private ConstanciaDeuVO constanciaDeu;
	
	private PersonaVO persona;
	
	private Long idPersona;
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ConDeuTitVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ConDeuTitVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	
	public ConstanciaDeuVO getConstanciaDeu() {
		return constanciaDeu;
	}

	public void setConstanciaDeu(ConstanciaDeuVO constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}

	public PersonaVO getPersona() {
		return persona;
	}

	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
