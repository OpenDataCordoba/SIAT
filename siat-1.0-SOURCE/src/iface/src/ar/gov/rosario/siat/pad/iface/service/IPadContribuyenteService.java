//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;

import ar.gov.rosario.siat.pad.iface.model.ConAtrValAdapter;
import ar.gov.rosario.siat.pad.iface.model.ConAtrValSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteAdapter;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IPadContribuyenteService {
	
	// ---> ABM Contribuyente
	public ContribuyenteSearchPage getContribuyenteSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ContribuyenteSearchPage getContribuyenteSearchPageResult(UserContext usercontext, ContribuyenteSearchPage contribuyenteSearchPage) throws DemodaServiceException;
	
	public ContribuyenteAdapter getContribuyenteAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ContribuyenteAdapter getContribuyenteAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ContribuyenteAdapter getContribuyenteAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ContribuyenteVO createContribuyente(UserContext userContext, ContribuyenteVO contribuyenteVO ) throws DemodaServiceException;
	public ContribuyenteVO updateContribuyente(UserContext userContext, ContribuyenteVO contribuyenteVO ) throws DemodaServiceException;
	public ContribuyenteVO deleteContribuyente(UserContext userContext, ContribuyenteVO contribuyenteVO ) throws DemodaServiceException;
	public ContribuyenteVO activarContribuyente(UserContext userContext, ContribuyenteVO contribuyenteVO ) throws DemodaServiceException;
	public ContribuyenteVO desactivarContribuyente(UserContext userContext, ContribuyenteVO contribuyenteVO ) throws DemodaServiceException;	
	// <--- ABM Contribuyente

	// ---> ABM ConAtrVal
	public ConAtrValSearchPage getConAtrValSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ConAtrValSearchPage getConAtrValSearchPageResult(UserContext usercontext, ConAtrValSearchPage contribuyenteSearchPage) throws DemodaServiceException;

	public ConAtrValAdapter getConAtrValAdapterForView(UserContext userContext, CommonKey contribuyenteKey, CommonKey conAtrKey ) throws DemodaServiceException;
	public ConAtrValAdapter getConAtrValAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ConAtrValAdapter getConAtrValAdapterForUpdate(UserContext userContext, CommonKey contribuyenteKey, CommonKey conAtrKey) throws DemodaServiceException;

	public ConAtrValAdapter createConAtrVal(UserContext userContext, ConAtrValAdapter conAtrValAdapter) throws DemodaServiceException;
	public ConAtrValAdapter updateConAtrVal(UserContext userContext, ConAtrValAdapter conAtrValAdapter) throws DemodaServiceException;
	public ConAtrValAdapter deleteConAtrVal(UserContext userContext, ConAtrValAdapter conAtrValAdapter) throws DemodaServiceException;
	// <--- ABM ConAtrVal
	
}