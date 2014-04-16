//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.service;

import ar.gov.rosario.siat.emi.iface.model.ValEmiMatAdapter;
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEmiDefinicionService {
	// ---> ABM ValEmiMat
	public ValEmiMatSearchPage getValEmiMatSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public ValEmiMatSearchPage getValEmiMatSearchPageResult(UserContext usercontext, 
		ValEmiMatSearchPage valEmiMatSearchPage) throws DemodaServiceException;

	public ValEmiMatAdapter getValEmiMatAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ValEmiMatAdapter getValEmiMatAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public ValEmiMatAdapter getValEmiMatAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ValEmiMatAdapter getValEmiMatAdapterParamRecurso(UserContext userContext, 
			ValEmiMatAdapter valEmiMatAdapter)throws DemodaServiceException;
	
	public ValEmiMatVO createValEmiMat(UserContext userContext, ValEmiMatVO valEmiMatVO ) throws DemodaServiceException;
	public ValEmiMatVO updateValEmiMat(UserContext userContext, ValEmiMatVO valEmiMatVO ) throws DemodaServiceException;
	public ValEmiMatVO deleteValEmiMat(UserContext userContext, ValEmiMatVO valEmiMatVO ) throws DemodaServiceException;
	// <--- ABM ValEmiMat
}
