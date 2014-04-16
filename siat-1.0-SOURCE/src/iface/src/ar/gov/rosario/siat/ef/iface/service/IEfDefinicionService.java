//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.service;

import ar.gov.rosario.siat.ef.iface.model.DocSopAdapter;
import ar.gov.rosario.siat.ef.iface.model.DocSopSearchPage;
import ar.gov.rosario.siat.ef.iface.model.DocSopVO;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoAdapter;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoSearchPage;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoVO;
import ar.gov.rosario.siat.ef.iface.model.InsSupAdapter;
import ar.gov.rosario.siat.ef.iface.model.InsSupVO;
import ar.gov.rosario.siat.ef.iface.model.InspectorAdapter;
import ar.gov.rosario.siat.ef.iface.model.InspectorSearchPage;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorAdapter;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorSearchPage;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.model.SupervisorAdapter;
import ar.gov.rosario.siat.ef.iface.model.SupervisorSearchPage;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEfDefinicionService {
	
	// ---> ABM Inspector
	public InspectorSearchPage getInspectorSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public InspectorSearchPage getInspectorSearchPageResult(UserContext usercontext, InspectorSearchPage inspectorSearchPage) throws DemodaServiceException;

	public InspectorAdapter getInspectorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public InspectorAdapter getInspectorAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public InspectorAdapter getInspectorAdapterParamPersona(UserContext userContext, InspectorAdapter inspectorAdapter) throws DemodaServiceException;
	public InspectorAdapter getInspectorAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public InspectorVO createInspector(UserContext userContext, InspectorVO inspectorVO ) throws DemodaServiceException;
	public InspectorVO updateInspector(UserContext userContext, InspectorVO inspectorVO ) throws DemodaServiceException;
	public InspectorVO deleteInspector(UserContext userContext, InspectorVO inspectorVO ) throws DemodaServiceException;
	
	public InspectorAdapter imprimirInspector(UserContext userContext, InspectorAdapter inspectorAdapterVO ) throws DemodaServiceException;
	// <--- ABM Inspector

	// ---> ABM InsSup
	public InsSupAdapter getInsSupAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public InsSupAdapter getInsSupAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public InsSupAdapter getInsSupAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public InsSupVO createInsSup(UserContext userContext, InsSupVO insSupVO) throws DemodaServiceException;
	public InsSupVO updateInsSup(UserContext userContext, InsSupVO insSupVO) throws DemodaServiceException;
	public InsSupVO deleteInsSup(UserContext userContext, InsSupVO insSupVO) throws DemodaServiceException;
	
	public InsSupAdapter imprimirInsSup(UserContext userContext, InsSupAdapter insSupAdapterVO) throws DemodaServiceException;
	// <--- ABM InsSup
	
	// ---> ABM Investigador
	public InvestigadorSearchPage getInvestigadorSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public InvestigadorSearchPage getInvestigadorSearchPageResult(UserContext usercontext, InvestigadorSearchPage investigadorSearchPage) throws DemodaServiceException;

	public InvestigadorAdapter getInvestigadorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public InvestigadorAdapter getInvestigadorAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public InvestigadorAdapter getInvestigadorAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public InvestigadorVO createInvestigador(UserContext userContext, InvestigadorVO investigadorVO ) throws DemodaServiceException;
	public InvestigadorAdapter getInvestigadorAdapterParamPersona(UserContext userContext, InvestigadorAdapter investigadorAdapter) throws DemodaServiceException ;
	public InvestigadorVO updateInvestigador(UserContext userContext, InvestigadorVO investigadorVO ) throws DemodaServiceException;
	public InvestigadorVO deleteInvestigador(UserContext userContext, InvestigadorVO investigadorVO ) throws DemodaServiceException;
	// <--- ABM Investigador
	
	// ---> ABM Supervisor
	public SupervisorSearchPage getSupervisorSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public SupervisorSearchPage getSupervisorSearchPageResult(UserContext usercontext, SupervisorSearchPage supervisorSearchPage) throws DemodaServiceException;

	public SupervisorAdapter getSupervisorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SupervisorAdapter getSupervisorAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public SupervisorAdapter getSupervisorAdapterParamPersona(UserContext userContext, SupervisorAdapter supervisorAdapter) throws DemodaServiceException;
	public SupervisorAdapter getSupervisorAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public SupervisorVO createSupervisor(UserContext userContext, SupervisorVO supervisorVO ) throws DemodaServiceException;
	public SupervisorVO updateSupervisor(UserContext userContext, SupervisorVO supervisorVO ) throws DemodaServiceException;
	public SupervisorVO deleteSupervisor(UserContext userContext, SupervisorVO supervisorVO ) throws DemodaServiceException;
	public SupervisorAdapter imprimirSupervisor(UserContext userContext, SupervisorAdapter supervisorAdapterVO ) throws DemodaServiceException;

	// <--- ABM Supervisor
	
	// ---> ABM Fuente Info
	public FuenteInfoSearchPage getFuenteInfoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public FuenteInfoSearchPage getFuenteInfoSearchPageResult(UserContext usercontext, FuenteInfoSearchPage fuenteInfoSearchPage) throws DemodaServiceException;

	public FuenteInfoAdapter getFuenteInfoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FuenteInfoAdapter getFuenteInfoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public FuenteInfoAdapter getFuenteInfoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FuenteInfoVO activarFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO ) throws DemodaServiceException;
	public FuenteInfoVO desactivarFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO ) throws DemodaServiceException;	
	
	public FuenteInfoVO createFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO ) throws DemodaServiceException;
	public FuenteInfoVO updateFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO ) throws DemodaServiceException;
	public FuenteInfoVO deleteFuenteInfo(UserContext userContext, FuenteInfoVO fuenteInfoVO ) throws DemodaServiceException;
	public FuenteInfoAdapter imprimirFuenteInfo(UserContext userContext, FuenteInfoAdapter fuenteInfoAdapterVO ) throws DemodaServiceException;

	// <--- ABM Fuente Info
	
	// ---> ABM DocSop
	public DocSopSearchPage getDocSopSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public DocSopSearchPage getDocSopSearchPageResult(UserContext usercontext, DocSopSearchPage docSopSearchPage) throws DemodaServiceException;

	public DocSopAdapter getDocSopAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DocSopAdapter getDocSopAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public DocSopAdapter getDocSopAdapterParamPersona(UserContext userContext, DocSopAdapter docSopAdapter) throws DemodaServiceException;
	public DocSopAdapter getDocSopAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public DocSopVO createDocSop(UserContext userContext, DocSopVO docSopVO ) throws DemodaServiceException;
	public DocSopVO updateDocSop(UserContext userContext, DocSopVO docSopVO ) throws DemodaServiceException;
	public DocSopVO deleteDocSop(UserContext userContext, DocSopVO docSopVO ) throws DemodaServiceException;
	public DocSopAdapter imprimirDocSop(UserContext userContext, DocSopAdapter docSopAdapterVO ) throws DemodaServiceException;
	public DocSopVO activarDocSop(UserContext userContext,DocSopVO docSopVO)throws DemodaServiceException;
	public DocSopVO desactivarDocSop(UserContext userContext, DocSopVO docSopVO)throws DemodaServiceException;
	// <--- ABM DocSop
}
