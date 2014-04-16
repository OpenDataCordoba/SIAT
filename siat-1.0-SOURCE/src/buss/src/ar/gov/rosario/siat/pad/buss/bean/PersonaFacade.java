//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.PersonaSearchPage;

/**
 * Manejador del m&oacute;dulo de Personas
 * 
 * @author tecso
 *
 */
public class PersonaFacade {
		
	private static Logger log = Logger.getLogger(PersonaFacade.class);
	
	private static final PersonaFacade INSTANCE = new PersonaFacade();
	
	/**
	 * Constructor privado
	 */
	private PersonaFacade() {
		
	}

	/**
	 * Devuelve unica instancia
	 */
	public static PersonaFacade getInstance() {
		return INSTANCE;
	}

	// <-- Persona
	/** Obtiene una persona de SIAT a partir de un id, para esto
	 *  consulta a la BD general, y luego transforma la persona
	 *  recuperada en una persona de SIAT. Si no la encuentra 
	 *  devulve una excepcion.
	 * 
	 * @param  idPersona
	 * @return Persona
	 * @throws Exception
	 */
	public Persona getPersonaById(Long idPersona) throws Exception {

		// obtengo la persona 
		Persona personaSIAT = this.getPersonaByIdNull(idPersona);
		
		if (personaSIAT == null) { 
			log.error("La Persona con id: " + idPersona + ". No se encontro en la base de Datos General");
			throw new Exception("La Persona con id: " + idPersona + 
				". No se encontro en la base de Datos General" );
		}

		return personaSIAT;
	}
	
	/** Obtiene una persona de SIAT a partir de un id, para esto
	 *  consulta a la BD general, y luego transforma la persona
	 *  recuperada en una persona de SIAT. Si no la encuentra 
	 *  devuelve null.
	 * 
	 * @param idPersona
	 * @return Persona
	 * @throws Exception
	 */
	public Persona getPersonaByIdNull(Long idPersona) throws Exception {
		return (Persona) PadDAOFactory.getPersonaDAO().getByIdNull(idPersona);
	}
	
	/**
	 * Realiza la busqueda de personas fisicas y juridicas por el cuit pasado como parametro, sin tener en cuenta la letraCuit
	 * @param cuit
	 * @return
	 */
	public List<Persona> getListPersonaByCuit(String cuit) throws Exception {
		PersonaSearchPage personaSearchPage = new PersonaSearchPage();
		personaSearchPage.getPersona().setLetraCuit(null);
		personaSearchPage.getPersona().setCuit(cuit);

		return getListPersonaBySearchPage(personaSearchPage);	
	}
	
	/**
	 * Obtiene la lista de personas
	 * @param personaSearchPage
	 * @return List<ar.gov.rosario.siat.pad.buss.bean.Persona>
	 * @throws Exception
	 */
	public List<Persona> getListPersonaBySearchPage(PersonaSearchPage personaSearchPage) throws Exception{
		return PadDAOFactory.getPersonaDAO().getBySearchPage(personaSearchPage);
	}
	

	/**
	 * Da de alta una persona a partir de los datos proporcionados
	 * @param personaSIAT
	 * @return Persona
	 * @throws Exception
	 */
    public Persona createPersona(Persona persona) throws Exception {
		if (!persona.validateCreate()) {
			return persona;
		}

		persona.getDomicilio().setEsValidado(0);
		persona.getDomicilio().setTipoDomicilio(TipoDomicilio.getById(2L));
		PadDAOFactory.getDomicilioDAO().update(persona.getDomicilio());
		PadDAOFactory.getPersonaDAO().update(persona);
		
		return persona;    	
    }
   
    /**
     * Realiza la actualizacion de la Persona
     * @param  personaSIAT
     * @param  personaSIAT
     * @return Persona
     * @throws Exception
     */
    public Persona updatePersona(Persona persona) throws Exception {
		// Validaciones 
		if (!persona.validateUpdate()) {
			return persona;
		}
		
		PadDAOFactory.getPersonaDAO().update(persona);
		
		return persona;    
	}
	// <-- Persona
	
	// <-- Tipo documento
    /**
     * Obtiene la lista de Tipo de Documento.
     * @return List<TipoDocumento>
     */
	@SuppressWarnings("unchecked")
	public List<TipoDocumento> getListTipoDocumento(){
		return PadDAOFactory.getTipoDocumentoDAO().getList();
	}
}
