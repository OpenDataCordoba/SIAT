//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.service;

import ar.gov.rosario.siat.def.iface.model.TipObjImpAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAreOAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAreOVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpSearchPage;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IDefObjetoImponibleService {
	
	// ---> ABM TipObjImp
	public TipObjImpSearchPage getTipObjImpSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public TipObjImpSearchPage getTipObjImpSearchPageResult(UserContext usercontext, TipObjImpSearchPage tipObjImpSearchPage) throws DemodaServiceException;

	public TipObjImpAdapter getTipObjImpAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipObjImpAdapter getTipObjImpAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipObjImpAdapter getTipObjImpAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public TipObjImpVO createTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO ) throws DemodaServiceException;
	public TipObjImpVO updateTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO ) throws DemodaServiceException;
	public TipObjImpVO deleteTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO ) throws DemodaServiceException;
	
	public TipObjImpVO activarTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO ) throws DemodaServiceException;
	public TipObjImpVO desactivarTipObjImp(UserContext userContext, TipObjImpVO tipObjImpVO ) throws DemodaServiceException;
	public TipObjImpAdapter getTipObjImpAdapterParam(UserContext userContext, TipObjImpAdapter tipObjImpEncAdapterVO) throws DemodaServiceException;
	// <--- ABM TipObjImp
	
	// ---> ABM TipObjImpAtr
	public TipObjImpAtrAdapter getTipObjImpAtrAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipObjImpAtrAdapter getTipObjImpAtrAdapterForCreate(UserContext userContext, CommonKey tipObjImpKey, CommonKey atributoKey) throws DemodaServiceException;
	public TipObjImpAtrAdapter getTipObjImpAtrAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public TipObjImpAtrAdapter getTipObjImpAtrAdapterParamAtributo(UserContext userContext, TipObjImpAtrAdapter tipObjImpAtrAdapter) throws DemodaServiceException;
	
	public TipObjImpAtrVO createTipObjImpAtr(UserContext userContext, TipObjImpAtrVO tipObjImpAtrVO ) throws DemodaServiceException;
	public TipObjImpAtrVO updateTipObjImpAtr(UserContext userContext, TipObjImpAtrVO tipObjImpAtrVO ) throws DemodaServiceException;
	public TipObjImpAtrVO deleteTipObjImpAtr(UserContext userContext, TipObjImpAtrVO tipObjImpAtrVO ) throws DemodaServiceException;
	// <--- ABM TipObjImpAtr
	
	// ---> ABM TipObjImpAreO
	public TipObjImpAreOAdapter getTipObjImpAreOAdapterForView(UserContext userContext, CommonKey keyTipObjImpAreO) throws DemodaServiceException;
	public TipObjImpAreOAdapter getTipObjImpAreOAdapterForCreate(UserContext userContext, CommonKey keyTipObjImp) throws DemodaServiceException;
	
	public TipObjImpAreOVO createTipObjImpAreO(UserContext userContext, TipObjImpAreOVO tipObjImpAreOVO ) throws DemodaServiceException;
	public TipObjImpAreOVO deleteTipObjImpAreO(UserContext userContext, TipObjImpAreOVO tipObjImpAreOVO ) throws DemodaServiceException;
	public TipObjImpAreOVO activarTipObjImpAreO(UserContext userContext, TipObjImpAreOVO tipObjImpAreOVO ) throws DemodaServiceException;
	public TipObjImpAreOVO desactivarTipObjImpAreO(UserContext userContext, TipObjImpAreOVO tipObjImpAreOVO ) throws DemodaServiceException;
	// <--- ABM TipObjImpAreO


}