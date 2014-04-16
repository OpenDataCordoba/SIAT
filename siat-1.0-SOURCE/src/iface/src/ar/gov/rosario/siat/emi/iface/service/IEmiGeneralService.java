//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.service;

import ar.gov.rosario.siat.emi.iface.model.EmiInfCueSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoProPasDebAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProcesoResLiqDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEmiGeneralService {
	
	// ---> ABM ResLiqDeu
	public ResLiqDeuSearchPage getResLiqDeuSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public ResLiqDeuSearchPage getResLiqDeuSearchPageResult(UserContext usercontext, ResLiqDeuSearchPage resLiqDeuSearchPage) 
		throws DemodaServiceException;

	public ResLiqDeuAdapter getResLiqDeuAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ResLiqDeuAdapter getResLiqDeuAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public ResLiqDeuAdapter getResLiqDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ResLiqDeuAdapter getResLiqDeuAdapterParamRecurso(UserContext userContext, ResLiqDeuAdapter resLiqDeuAdapter) 
		throws DemodaServiceException;

	public ResLiqDeuVO createResLiqDeu(UserContext userContext, ResLiqDeuVO resLiqDeuVO ) 
		throws DemodaServiceException;
	public ResLiqDeuVO updateResLiqDeu(UserContext userContext, ResLiqDeuVO resLiqDeuVO ) 
		throws DemodaServiceException;
	public ResLiqDeuVO deleteResLiqDeu(UserContext userContext, ResLiqDeuVO resLiqDeuVO ) 
		throws DemodaServiceException;

	public ProcesoResLiqDeuAdapter getProcesoResLiqDeuAdapterInit(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	// <--- ABM ResLiqDeu
	
	// ---> Consulta EmiInfCue
 	public EmiInfCueSearchPage getEmiInfCueSearchPageInit(UserContext userContext, Long idResLiqDeu) 
 		throws DemodaServiceException;
 	public EmiInfCueSearchPage getEmiInfCueSearchPageResult(UserContext userContext, 
 			EmiInfCueSearchPage emiInfCueSearchPage) throws DemodaServiceException;
	// <--- Consulta EmiInfCue

	// ---> ABM ProPasDeb
	public ProPasDebSearchPage getProPasDebSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public ProPasDebSearchPage getProPasDebSearchPageResult(UserContext usercontext, ProPasDebSearchPage ProPasDebSearchPage) 
		throws DemodaServiceException;

	public ProPasDebAdapter getProPasDebAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ProPasDebAdapter getProPasDebAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public ProPasDebAdapter getProPasDebAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ProPasDebAdapter getProPasDebAdapterParamRecurso(UserContext userContext, ProPasDebAdapter ProPasDebAdapter) 
		throws DemodaServiceException;

	public ProPasDebVO createProPasDeb(UserContext userContext, ProPasDebVO ProPasDebVO ) 
		throws DemodaServiceException;
	public ProPasDebVO updateProPasDeb(UserContext userContext, ProPasDebVO ProPasDebVO ) 
		throws DemodaServiceException;
	public ProPasDebVO deleteProPasDeb(UserContext userContext, ProPasDebVO ProPasDebVO ) 
		throws DemodaServiceException;

	public ProcesoProPasDebAdapter getProcesoProPasDebAdapterInit(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ProPasDebVO reiniciar(UserContext userContext, ProPasDebVO ProPasDebVO) 
		throws DemodaServiceException;
	// <--- ABM ProPasDeb

}
