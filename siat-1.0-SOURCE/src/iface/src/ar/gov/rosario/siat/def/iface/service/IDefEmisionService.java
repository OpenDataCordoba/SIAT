//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.service;

import ar.gov.rosario.siat.def.iface.model.CodEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.CodEmiSearchPage;
import ar.gov.rosario.siat.def.iface.model.CodEmiVO;
import ar.gov.rosario.siat.def.iface.model.ColEmiMatAdapter;
import ar.gov.rosario.siat.def.iface.model.ColEmiMatVO;
import ar.gov.rosario.siat.def.iface.model.EmiMatAdapter;
import ar.gov.rosario.siat.def.iface.model.EmiMatSearchPage;
import ar.gov.rosario.siat.def.iface.model.EmiMatVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IDefEmisionService {
	
	// ---> ABM EmiMat
	public EmiMatSearchPage getEmiMatSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public EmiMatSearchPage getEmiMatSearchPageResult(UserContext usercontext, EmiMatSearchPage emiMatSearchPage) 
		throws DemodaServiceException;

	public EmiMatAdapter getEmiMatAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public EmiMatAdapter getEmiMatAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public EmiMatAdapter getEmiMatAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public EmiMatAdapter imprimirEmiMat(UserContext userContext, EmiMatAdapter emiMatAdapterVO ) 
		throws DemodaServiceException;

	public EmiMatVO createEmiMat(UserContext userContext, EmiMatVO emiMatVO ) 
		throws DemodaServiceException;
	public EmiMatVO updateEmiMat(UserContext userContext, EmiMatVO emiMatVO ) 
		throws DemodaServiceException;
	public EmiMatVO deleteEmiMat(UserContext userContext, EmiMatVO emiMatVO ) 
		throws DemodaServiceException;
	public EmiMatVO activarEmiMat(UserContext userContext, EmiMatVO emiMatVO ) 
		throws DemodaServiceException;
	public EmiMatVO desactivarEmiMat(UserContext userContext, EmiMatVO emiMatVO ) 
		throws DemodaServiceException;	
	// <--- ABM EmiMat
	
	// ---> ABM ColEmiMat
	public ColEmiMatAdapter getColEmiMatAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ColEmiMatAdapter getColEmiMatAdapterForCreate(UserContext userContext,CommonKey commonKey) 
		throws DemodaServiceException;
	public ColEmiMatAdapter getColEmiMatAdapterForUpdate(UserContext userContext,CommonKey commonKey) 
		throws DemodaServiceException;

	public ColEmiMatVO createColEmiMat(UserContext userContext, ColEmiMatVO colEmiMatVO) 
		throws DemodaServiceException;
	public ColEmiMatVO updateColEmiMat(UserContext userContext, ColEmiMatVO colEmiMatVO) 
		throws DemodaServiceException;
	public ColEmiMatVO deleteColEmiMat(UserContext userContext, ColEmiMatVO colEmiMatVO) 
		throws DemodaServiceException;
	// <--- ABM ColEmiMat
	
	// ---> ABM CodEmi
	public CodEmiSearchPage getCodEmiSearchPageInit(UserContext usercontext) 
		throws DemodaServiceException;
	public CodEmiSearchPage getCodEmiSearchPageResult(UserContext usercontext, CodEmiSearchPage codEmiSearchPage) 
		throws DemodaServiceException;

	public CodEmiAdapter getCodEmiAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public CodEmiAdapter getCodEmiAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public CodEmiAdapter getCodEmiAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public CodEmiAdapter getCodEmiAdapterParamTipCodEmi(UserContext userContext, CodEmiAdapter codEmiAdapter) 
		throws DemodaServiceException;

	public CodEmiVO createCodEmi(UserContext userContext, CodEmiVO codEmiVO) 
		throws DemodaServiceException;
	public CodEmiVO updateCodEmi(UserContext userContext, CodEmiVO codEmiVO) 
		throws DemodaServiceException;
	public CodEmiVO deleteCodEmi(UserContext userContext, CodEmiVO codEmiVO) 
		throws DemodaServiceException;
	public CodEmiVO activarCodEmi(UserContext userContext, CodEmiVO codEmiVO) 
		throws DemodaServiceException;
	public CodEmiVO desactivarCodEmi(UserContext userContext, CodEmiVO codEmiVO) 
		throws DemodaServiceException;	
	
	public CodEmiAdapter testCodEmi(UserContext usercontext, CodEmiAdapter codEmiAdapter)
		throws Exception;
	// <--- ABM EmiMat
}
