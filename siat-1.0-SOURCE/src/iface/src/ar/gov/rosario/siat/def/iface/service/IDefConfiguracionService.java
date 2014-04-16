//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.service;

import ar.gov.rosario.siat.def.iface.model.AreaAdapter;
import ar.gov.rosario.siat.def.iface.model.AreaSearchPage;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.ParametroAdapter;
import ar.gov.rosario.siat.def.iface.model.ParametroSearchPage;
import ar.gov.rosario.siat.def.iface.model.ParametroVO;
import ar.gov.rosario.siat.def.iface.model.RecursoAreaAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoAreaVO;
import ar.gov.rosario.siat.def.iface.model.SiatScriptAdapter;
import ar.gov.rosario.siat.def.iface.model.SiatScriptSearchPage;
import ar.gov.rosario.siat.def.iface.model.SiatScriptUsrAdapter;
import ar.gov.rosario.siat.def.iface.model.SiatScriptUsrVO;
import ar.gov.rosario.siat.def.iface.model.SiatScriptVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IDefConfiguracionService {
	
	// ---> ABM Area
	public AreaSearchPage getAreaSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public AreaSearchPage getAreaSearchPageResult(UserContext usercontext, AreaSearchPage areaSearchPage) throws DemodaServiceException;

	public AreaAdapter getAreaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AreaAdapter getAreaAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public AreaAdapter getAreaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AreaAdapter getAdapterForRecursoAreaView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public AreaVO createArea(UserContext userContext, AreaVO areaVO ) throws DemodaServiceException;
	public AreaVO updateArea(UserContext userContext, AreaVO areaVO ) throws DemodaServiceException;
	public AreaVO deleteArea(UserContext userContext, AreaVO areaVO ) throws DemodaServiceException;
	
	public AreaVO activarArea(UserContext userContext, AreaVO areaVO ) throws DemodaServiceException;
	public AreaVO desactivarArea(UserContext userContext, AreaVO areaVO ) throws DemodaServiceException;
	public AreaAdapter imprimirArea(UserContext userContext, AreaAdapter areaAdapterVO ) throws DemodaServiceException;
	// <--- ABM Area
	
	// ---> ABM Parametro
	public ParametroSearchPage getParametroSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ParametroSearchPage getParametroSearchPageResult(UserContext usercontext, ParametroSearchPage ParametroSearchPage) throws DemodaServiceException;

	public ParametroAdapter getParametroAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ParametroAdapter getParametroAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ParametroAdapter getParametroAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ParametroVO createParametro(UserContext userContext, ParametroVO ParametroVO ) throws DemodaServiceException;
	public ParametroVO updateParametro(UserContext userContext, ParametroVO ParametroVO ) throws DemodaServiceException;
	public ParametroVO deleteParametro(UserContext userContext, ParametroVO ParametroVO ) throws DemodaServiceException;
	
	public ParametroVO activarParametro(UserContext userContext, ParametroVO ParametroVO ) throws DemodaServiceException;
	public ParametroVO desactivarParametro(UserContext userContext, ParametroVO ParametroVO ) throws DemodaServiceException;
	// <--- ABM Parametro
	
	// ---> ABM RecursoArea	
	public RecursoAreaAdapter getRecursoAreaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecursoAreaAdapter getRecursoAreaAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecursoAreaAdapter getRecursoAreaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public RecursoAreaVO createRecursoArea(UserContext userContext, RecursoAreaVO recursoAreaVO ) throws DemodaServiceException;
	public RecursoAreaVO updateRecursoArea(UserContext userContext, RecursoAreaVO recursoAreaVO ) throws DemodaServiceException;
	public RecursoAreaVO deleteRecursoArea(UserContext userContext, RecursoAreaVO recursoAreaVO ) throws DemodaServiceException;
	// <--- ABM RecursoArea
	
	public void updateSiatParam()throws DemodaServiceException;
	
	public void initializeSiat()throws DemodaServiceException;
	public void destroySiat()throws DemodaServiceException;
	public void refreshCache(String cacheFlag) throws DemodaServiceException;
	
	// ---> ABM SiatScript	
	
	public SiatScriptSearchPage getSiatScriptSearchPageInit(UserContext userContext) throws DemodaServiceException; 		
	public SiatScriptSearchPage getSiatScriptSearchPageResult(UserContext userContext, SiatScriptSearchPage siatScriptSearchPageVO) throws DemodaServiceException;
	
	public SiatScriptVO createSiatScript(UserContext userContext, SiatScriptVO siatScript) throws DemodaServiceException;
	public SiatScriptVO updateSiatScript(UserContext userContext, SiatScriptVO siatScript) throws DemodaServiceException;
	public SiatScriptVO deleteSiatScript(UserContext userContext,SiatScriptVO siatScript) throws DemodaServiceException;
	public SiatScriptVO activarSiatScript(UserContext userContext, SiatScriptVO siatScript) throws DemodaServiceException;
	public SiatScriptVO desactivarSiatScript(UserContext userContext,SiatScriptVO siatScript) throws DemodaServiceException;
	
	
	public SiatScriptAdapter getSiatScriptAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public SiatScriptAdapter getSiatScriptAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SiatScriptAdapter getSiatScriptAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SiatScriptAdapter getSiatScriptAdapterParam(UserContext userContext,	SiatScriptAdapter siatScriptAdapterVO) throws DemodaServiceException;
	public SiatScriptAdapter imprimirSiatScript(UserContext userContext,SiatScriptAdapter siatScriptAdapterVO) throws DemodaServiceException;	
	// <--- ABM SiatScript	
	
	// ---> ABM SiatScriptUsr
	public SiatScriptUsrAdapter getSiatScriptUsrAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SiatScriptUsrAdapter getSiatScriptUsrAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SiatScriptUsrAdapter getSiatScriptUsrAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SiatScriptUsrVO updateSiatScriptUsr(UserContext userContext,	SiatScriptUsrVO siatScriptUsr) throws DemodaServiceException;
	
	
	// <--- ABM SiatScriptUsr	
	public SiatScriptUsrAdapter imprimirSiatScriptUsr(UserContext userContext,SiatScriptUsrAdapter siatScriptUsrAdapterVO) throws DemodaServiceException;
	public SiatScriptUsrAdapter getSiatScriptUsrAdapterParam(UserContext userContext, SiatScriptUsrAdapter siatScriptUsrAdapterVO) throws DemodaServiceException;
	public SiatScriptUsrVO desactivarSiatScriptUsr(UserContext userContext,	SiatScriptUsrVO siatScriptUsr) throws DemodaServiceException;
	public SiatScriptUsrVO deleteSiatScriptUsr(UserContext userContext,	SiatScriptUsrVO siatScriptUsr) throws DemodaServiceException;
	public SiatScriptUsrVO createSiatScriptUsr(UserContext userContext, SiatScriptUsrVO siatScriptUsr) throws DemodaServiceException;
	public SiatScriptUsrVO activarSiatScriptUsr(UserContext userContext, SiatScriptUsrVO siatScriptUsr) throws DemodaServiceException;
	
}