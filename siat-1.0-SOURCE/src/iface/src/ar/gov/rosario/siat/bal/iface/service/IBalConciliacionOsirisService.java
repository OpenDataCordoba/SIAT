//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.CierreBancoAdapter;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoVO;
import ar.gov.rosario.siat.bal.iface.model.MovBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.MovBanDetAdapter;
import ar.gov.rosario.siat.bal.iface.model.MovBanDetVO;
import ar.gov.rosario.siat.bal.iface.model.MovBanSearchPage;
import ar.gov.rosario.siat.bal.iface.model.MovBanVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalConciliacionOsirisService {

	// ---> ABM MovBan
	public MovBanSearchPage getMovBanSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public MovBanSearchPage getMovBanSearchPageResult(UserContext userContext, MovBanSearchPage movBanSearchPage) throws DemodaServiceException;
	
	public MovBanAdapter getMovBanAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public MovBanAdapter getMovBanAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	
	public MovBanAdapter createMovBan(UserContext userContext, MovBanAdapter movBanAdapterVO ) throws DemodaServiceException;
	public MovBanVO updateMovBan(UserContext userContext, MovBanVO movBanVO ) throws DemodaServiceException;
	public MovBanVO deleteMovBan(UserContext userContext, MovBanVO movBanVO ) throws DemodaServiceException;
	public MovBanVO conciliarMovBan(UserContext userContext, MovBanVO movBanVO ) throws DemodaServiceException;
	public MovBanAdapter imprimirMovBan(UserContext userContext, MovBanAdapter movBanAdapterVO ) throws DemodaServiceException;
	public MovBanSearchPage imprimirMovBan(UserContext userContext, MovBanSearchPage movBanSearchPage) throws  DemodaServiceException;
	// <--- ABM MovBan

	// ---> ABM Detalle de Movimiento Bancario AFIP
	public MovBanDetAdapter getMovBanDetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public MovBanDetVO conciliarMovBanDet(UserContext userContext, MovBanDetVO movBanDetVO) throws DemodaServiceException;
	public MovBanDetAdapter imprimirMovBanDet(UserContext userContext, MovBanDetAdapter movBanDetAdapterVO ) throws DemodaServiceException;
	// <--- ABM Detalle de Movimiento Bancario AFIP

	// --> Conciliar Cierre Banco
	public CierreBancoSearchPage getCierreBancoSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public CierreBancoSearchPage getCierreBancoSearchPageResult(UserContext userContext, CierreBancoSearchPage cierreBancoSearchPage) throws DemodaServiceException;
	public CierreBancoAdapter imprimirCierreBancoPDF(UserContext userContext, CierreBancoAdapter cierreBancoAdapter) throws  DemodaServiceException;
	
	public CierreBancoAdapter getCierreBancoAdapterForConciliar(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CierreBancoVO conciliarCierreBanco(UserContext userContext, CierreBancoVO cierreBancoVO ) throws DemodaServiceException;
	// <-- Fin Conciliar Cierre Banco
}
