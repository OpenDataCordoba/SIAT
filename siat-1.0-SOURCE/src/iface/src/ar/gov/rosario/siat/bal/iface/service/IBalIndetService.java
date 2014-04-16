//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.IndetAdapter;
import ar.gov.rosario.siat.bal.iface.model.IndetReingAdapter;
import ar.gov.rosario.siat.bal.iface.model.IndetReingSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalIndetService {
	
	// ---> ABM Indet
	public IndetSearchPage getIndetSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public IndetSearchPage getIndetSearchPageResult(UserContext usercontext, IndetSearchPage indetSearchPage) throws DemodaServiceException;

	public IndetAdapter getIndetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public IndetAdapter getIndetAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public IndetAdapter getIndetAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public IndetAdapter getIndetAdapterForDesgloce(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public IndetAdapter getIndetAdapterForGenSaldo(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public IndetAdapter getIndetAdapterParamCuenta(UserContext userContext, IndetAdapter indetAdapter) throws DemodaServiceException;
	
	
	public IndetVO createIndet(UserContext userContext, IndetVO indetVO ) throws DemodaServiceException;
	public IndetVO updateIndet(UserContext userContext, IndetVO indetVO ) throws DemodaServiceException;
	public IndetVO deleteIndet(UserContext userContext, IndetVO indetVO ) throws DemodaServiceException;
	public IndetVO reingresarIndet(UserContext userContext, IndetVO indetVO ) throws DemodaServiceException;
	public IndetVO desglozarIndet(UserContext userContext, IndetVO indetVO) throws DemodaServiceException;
	public IndetVO genSaldoAFavorForIndet(UserContext userContext, IndetAdapter indetAdapterVO) throws DemodaServiceException;
	public IndetAdapter imprimirIndet(UserContext userContext, IndetAdapter indetAdapterVO ) throws DemodaServiceException;
	
	public IndetSearchPage reingresoMasivo(UserContext userContext, IndetSearchPage indetSearchPage ) throws DemodaServiceException;
	// <--- ABM Indet
	
	// ---> ABM Reingresos de Indeterminados
	public IndetReingSearchPage getIndetReingSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public IndetReingSearchPage getIndetReingSearchPageResult(UserContext usercontext, IndetReingSearchPage indetReingSearchPage) throws DemodaServiceException;

	public IndetReingAdapter getIndetReingAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM Reingresos de Indeterminados
	
	// ---> ABM IndetReing
	public IndetVO vueltaAtrasReing(UserContext userContext, IndetVO indetVO) throws DemodaServiceException;
	// <--- ABM IndetReing
}