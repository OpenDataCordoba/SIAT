//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.service;

import java.util.List;

import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.ConAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.ConAtrSearchPage;
import ar.gov.rosario.siat.def.iface.model.ConAtrVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IDefContribuyenteService {
	
	// ---> ABM ConAtr
	public ConAtrSearchPage getConAtrSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public ConAtrSearchPage getConAtrSearchPageResult(UserContext userContext, ConAtrSearchPage conAtrSearchPage) throws DemodaServiceException;
	public List<AtributoVO> getListAtributoConAtr(UserContext userContext) throws DemodaServiceException;	
	
	public ConAtrAdapter getConAtrAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ConAtrAdapter getConAtrAdapterForCreate(UserContext userContext, CommonKey commonKeyAtributo) throws DemodaServiceException;
	public ConAtrAdapter getConAtrAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ConAtrAdapter paramAtributo(UserContext userContext, ConAtrAdapter conAtrAdapter) throws DemodaServiceException;	
		
	public ConAtrVO createConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException;
	public ConAtrVO updateConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException;
	public ConAtrVO deleteConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException;
	public ConAtrVO activarConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException;
	public ConAtrVO desactivarConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException;
	// <--- ABM ConAtr
	
}	