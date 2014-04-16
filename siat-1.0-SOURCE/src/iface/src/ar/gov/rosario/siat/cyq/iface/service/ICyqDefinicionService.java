//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.service;


import ar.gov.rosario.siat.cyq.iface.model.AbogadoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface ICyqDefinicionService {
	
	// ---> ABM Abogado
	public AbogadoSearchPage getAbogadoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public AbogadoSearchPage getAbogadoSearchPageResult(UserContext usercontext, AbogadoSearchPage abogadoSearchPage) throws DemodaServiceException;

	public AbogadoAdapter getAbogadoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AbogadoAdapter getAbogadoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public AbogadoAdapter getAbogadoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public AbogadoVO createAbogado(UserContext userContext, AbogadoVO abogadoVO ) throws DemodaServiceException;
	public AbogadoVO updateAbogado(UserContext userContext, AbogadoVO abogadoVO ) throws DemodaServiceException;
	public AbogadoVO deleteAbogado(UserContext userContext, AbogadoVO abogadoVO ) throws DemodaServiceException;
	public AbogadoVO activarAbogado(UserContext userContext, AbogadoVO abogadoVO ) throws DemodaServiceException;
	public AbogadoVO desactivarAbogado(UserContext userContext, AbogadoVO abogadoVO ) throws DemodaServiceException;	
	public AbogadoAdapter imprimirAbogado(UserContext userContext, AbogadoAdapter abogadoAdapter ) throws DemodaServiceException;	
	// <--- ABM Abogado
	

	// ---> ABM Juzgado
	public JuzgadoSearchPage getJuzgadoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public JuzgadoSearchPage getJuzgadoSearchPageResult(UserContext usercontext, JuzgadoSearchPage juzgadoSearchPage) throws DemodaServiceException;

	public JuzgadoAdapter getJuzgadoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public JuzgadoAdapter getJuzgadoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public JuzgadoAdapter getJuzgadoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public JuzgadoVO createJuzgado(UserContext userContext, JuzgadoVO juzgadoVO ) throws DemodaServiceException;
	public JuzgadoVO updateJuzgado(UserContext userContext, JuzgadoVO juzgadoVO ) throws DemodaServiceException;
	public JuzgadoVO deleteJuzgado(UserContext userContext, JuzgadoVO juzgadoVO ) throws DemodaServiceException;
	public JuzgadoVO activarJuzgado(UserContext userContext, JuzgadoVO juzgadoVO ) throws DemodaServiceException;
	public JuzgadoVO desactivarJuzgado(UserContext userContext, JuzgadoVO juzgadoVO ) throws DemodaServiceException;
	public JuzgadoAdapter imprimirJuzgado(UserContext userContext, JuzgadoAdapter juzgadoAdapter ) throws DemodaServiceException;
	
	// <--- ABM Juzgado
}
