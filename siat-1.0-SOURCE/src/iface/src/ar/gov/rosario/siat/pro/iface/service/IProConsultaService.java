//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.service;

import ar.gov.rosario.siat.pro.iface.model.CorridaAdapter;
import ar.gov.rosario.siat.pro.iface.model.CorridaSearchPage;
import ar.gov.rosario.siat.pro.iface.model.ProcesoAdapter;
import ar.gov.rosario.siat.pro.iface.model.ProcesoSearchPage;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IProConsultaService {
	
	// ---> ABM_CORRIDA
	public CorridaSearchPage getCorridaSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public CorridaSearchPage getCorridaSearchPageResult(UserContext usercontext, CorridaSearchPage corridaSearchPage) throws DemodaServiceException;
	public CorridaAdapter getCorridaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM_CORRIDA
	
	// ---> ABM Proceso
	public ProcesoSearchPage getProcesoSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public ProcesoSearchPage getProcesoSearchPageResult(UserContext userContext, ProcesoSearchPage procesoSearchPage) throws DemodaServiceException;
	public ProcesoAdapter getProcesoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ProcesoVO createProceso(UserContext userContext, ProcesoVO procesoVO) throws DemodaServiceException;
	public ProcesoVO updateProceso(UserContext userContext, ProcesoVO procesoVO) throws DemodaServiceException;
	public ProcesoVO deleteProceso(UserContext userContext, ProcesoVO procesoVO) throws DemodaServiceException;
	public ProcesoAdapter getProcesoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException ;
	public ProcesoAdapter getProcesoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException ;	
	
	public ProcesoAdapter getProcesoAdapterParamTipoEjecucion(UserContext userContext, ProcesoAdapter procesoAdapter) throws DemodaServiceException;
	// <--- ABM Proceso	
	
}