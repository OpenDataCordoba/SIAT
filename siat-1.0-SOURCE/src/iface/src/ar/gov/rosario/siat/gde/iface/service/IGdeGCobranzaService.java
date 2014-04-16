//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;


import ar.gov.rosario.siat.gde.iface.model.CobranzaAdapter;
import ar.gov.rosario.siat.gde.iface.model.CobranzaSearchPage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeGCobranzaService {
	
	
	// ---> ABM Cobranza
	public CobranzaSearchPage getCobranzaSearchPageInit(UserContext userContext) throws DemodaServiceException;
	
	public CobranzaSearchPage getCobranzaSearchPageResult(UserContext userContext, CobranzaSearchPage cobranzaSearchPage) throws DemodaServiceException;
	
	public CobranzaAdapter getCobranzaAdapterForUpdate(UserContext userContext, CommonKey selectedId)throws DemodaServiceException;
	
	public CobranzaAdapter createEmisionAjustes(UserContext userContext, CobranzaAdapter cobranzaAdapter)throws DemodaServiceException;
	
	public CobranzaAdapter createGesCob(UserContext userContext, CobranzaAdapter cobranzaAdapter)throws DemodaServiceException;
	
	public CobranzaAdapter getCobranzaAdapterForAsign(UserContext userContext, CommonKey selectedId)throws DemodaServiceException;
	
	public CobranzaAdapter asignarPerCob(UserContext userContext, CobranzaAdapter cobranzaAdapter)throws DemodaServiceException;
	
	public CobranzaSearchPage paramPersona(UserContext userContext, CobranzaSearchPage cobranzaSearchPage, Long selectedId) throws DemodaServiceException ;
	
	public CobranzaAdapter quitarCaso(UserContext userContext, CobranzaAdapter cobranzaAdapter)throws DemodaServiceException;

}
