//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.service;

import ar.gov.rosario.siat.def.iface.model.AtributoAdapter;
import ar.gov.rosario.siat.def.iface.model.AtributoSearchPage;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.DomAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.DomAtrSearchPage;
import ar.gov.rosario.siat.def.iface.model.DomAtrVO;
import ar.gov.rosario.siat.def.iface.model.DomAtrValAdapter;
import ar.gov.rosario.siat.def.iface.model.DomAtrValVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IDefAtributoService {
	
	// ---> ABM Atributo
	public AtributoSearchPage getAtributoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public AtributoSearchPage getAtributoSearchPageResult(UserContext usercontext, AtributoSearchPage atributoSearchPage) throws DemodaServiceException;

	public AtributoAdapter getAtributoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AtributoAdapter getAtributoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public AtributoAdapter getAtributoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AtributoAdapter getAtributoAdapterParamTipoAtributo(UserContext userContext, AtributoAdapter atributoAdapter) throws DemodaServiceException;
	
	public AtributoVO createAtributo(UserContext userContext, AtributoVO atributoVO ) throws DemodaServiceException;
	public AtributoVO updateAtributo(UserContext userContext, AtributoVO atributoVO ) throws DemodaServiceException;
	public AtributoVO deleteAtributo(UserContext userContext, AtributoVO atributoVO ) throws DemodaServiceException;
	public AtributoVO activarAtributo(UserContext userContext, AtributoVO atributoVO ) throws DemodaServiceException;
	public AtributoVO desactivarAtributo(UserContext userContext, AtributoVO atributoVO ) throws DemodaServiceException;
	// <--- ABM Atributo

	// ---> ABM DomAtr
	public DomAtrSearchPage getDomAtrSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public DomAtrSearchPage getDomAtrSearchPageResult(UserContext usercontext, DomAtrSearchPage domAtrSearchPage) throws DemodaServiceException;

	public DomAtrAdapter getDomAtrAdapterForView(UserContext userContext, CommonKey domAtrCommonKey) throws DemodaServiceException;
	public DomAtrAdapter getDomAtrAdapterForCreate(UserContext userContext, CommonKey atributoCommonKey) throws DemodaServiceException;
	public DomAtrAdapter getDomAtrAdapterForUpdate(UserContext userContext, CommonKey domAtrCommonKey) throws DemodaServiceException;
	
	public DomAtrVO createDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException;
	public DomAtrVO updateDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException;
	public DomAtrVO deleteDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException;
	
	public DomAtrVO activarDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException;
	public DomAtrVO desactivarDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException;
	// ---> ABM DomAtr	
	
	// ---> ABM DomAtrVal
	public DomAtrValAdapter getDomAtrValAdapterForView(UserContext userContext, CommonKey domAtrValCommonKey) throws DemodaServiceException;
	public DomAtrValAdapter getDomAtrValAdapterForCreate(UserContext userContext, CommonKey domAtrCommonKey) throws DemodaServiceException;
	public DomAtrValAdapter getDomAtrValAdapterForUpdate(UserContext userContext, CommonKey domAtrValCommonKey) throws DemodaServiceException;
		
	public DomAtrValVO createDomAtrVal(UserContext userContext, DomAtrValVO domAtrValVO ) throws DemodaServiceException;
	public DomAtrValVO updateDomAtrVal(UserContext userContext, DomAtrValVO domAtrValVO ) throws DemodaServiceException;
	public DomAtrValVO deleteDomAtrVal(UserContext userContext, DomAtrValVO domAtrValVO ) throws DemodaServiceException;
	// <--- ABM DomAtrVal
	
}