//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.service;

import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoImpMasDeuAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEmiImpresionService {
	
	// ---> ABM Impresion Masiva de Deuda
	public ImpMasDeuSearchPage getImpMasDeuSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public ImpMasDeuSearchPage getImpMasDeuSearchPageResult(UserContext usercontext, ImpMasDeuSearchPage impMasDeuSearchPage) 
		throws DemodaServiceException;

	public ImpMasDeuAdapter getImpMasDeuAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ImpMasDeuAdapter getImpMasDeuAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public ImpMasDeuAdapter getImpMasDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ImpMasDeuAdapter getImpMasDeuAdapterParamRecurso(UserContext userContext, ImpMasDeuAdapter impMasDeuAdapterVO) 
		throws DemodaServiceException;
	
	public ImpMasDeuVO createImpMasDeu(UserContext userContext, ImpMasDeuVO impMasDeuVO) 
		throws DemodaServiceException;
	public ImpMasDeuVO updateImpMasDeu(UserContext userContext, ImpMasDeuVO impMasDeuVO) 
		throws DemodaServiceException;
	public ImpMasDeuVO deleteImpMasDeu(UserContext userContext, ImpMasDeuVO impMasDeuVO) 
		throws DemodaServiceException;

	public ProcesoImpMasDeuAdapter getProcesoImpMasDeuAdapterInit(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ImpMasDeuVO   activar(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException;
	public ImpMasDeuVO  cancelar(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException;
	public ImpMasDeuVO reiniciar(UserContext userContext, ImpMasDeuVO impMasDeuVO) throws DemodaServiceException;
	// <--- ABM Impresion Masiva de Deuda
	
}
