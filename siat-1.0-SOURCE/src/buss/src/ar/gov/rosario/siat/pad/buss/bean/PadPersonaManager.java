//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;


/**
 * Manejador del m&oacute;dulo de Personas
 * 
 * @author tecso
 *
 */
public class PadPersonaManager {
		
	//private static Logger log = Logger.getLogger(PadPersonaManager.class);
	
	private static final PadPersonaManager INSTANCE = new PadPersonaManager();
	
	/**
	 * Constructor privado
	 */
	private PadPersonaManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static PadPersonaManager getInstance() {
		return INSTANCE;
	}
	
	public Persona createPersona(Persona persona) throws Exception {

		// Validaciones 
		if (!persona.validateCreate()) {
			return persona;
		}

		persona = PersonaFacade.getInstance().createPersona(persona);
		
		return persona;
	}
	
	
	public Persona updatePersona(Persona persona) throws Exception {
		
		// Validaciones 
		if (!persona.validateUpdate()) {
			return persona;
		}
		
		persona = PersonaFacade.getInstance().updatePersona(persona);

		return persona;
	}
	
}