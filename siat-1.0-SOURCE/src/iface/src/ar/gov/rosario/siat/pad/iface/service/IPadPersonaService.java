//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;

import ar.gov.rosario.siat.pad.iface.model.PersonaAdapter;
import ar.gov.rosario.siat.pad.iface.model.PersonaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IPadPersonaService {
	
	// ---> ABM Persona
	public PersonaSearchPage getPersonaSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public PersonaSearchPage getPersonaSearchPageResult(UserContext usercontext, PersonaSearchPage personaSearchPage) throws DemodaServiceException;
	
	public PersonaAdapter getPersonaAdapterForView(UserContext userContext, CommonKey keyPersona) throws DemodaServiceException;
	public PersonaAdapter getPersonaAdapterForCreate(UserContext userContext, PersonaVO personaVO) throws DemodaServiceException;
	public PersonaAdapter getPersonaAdapterForUpdate(UserContext userContext, CommonKey keyPersona) throws DemodaServiceException;
	//public PersonaAdapter getPersonaAdapterParamLocalidad(UserContext userContext, PersonaAdapter personaAdapter) throws DemodaServiceException;
	
	public PersonaVO createPersona(UserContext userContext, PersonaVO personaVO ) throws DemodaServiceException;
	public PersonaVO updatePersona(UserContext userContext, PersonaVO personaVO ) throws DemodaServiceException;
	// <--- ABM Persona
	
	public boolean existePersonaBySexoyNroDoc(UserContext userContext, PersonaVO personaVO ) throws DemodaServiceException;
	
	public PersonaVO getPersonaBySexoyNroDoc(UserContext userContext, PersonaVO personaVO ) throws DemodaServiceException;

	
}