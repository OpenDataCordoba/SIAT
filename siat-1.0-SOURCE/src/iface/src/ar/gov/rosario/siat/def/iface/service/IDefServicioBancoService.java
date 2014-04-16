//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.service;

import ar.gov.rosario.siat.def.iface.model.BancoAdapter;
import ar.gov.rosario.siat.def.iface.model.BancoSearchPage;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import ar.gov.rosario.siat.def.iface.model.SerBanRecAdapter;
import ar.gov.rosario.siat.def.iface.model.SerBanRecVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoAdapter;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoSearchPage;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;


public interface IDefServicioBancoService {

	// ---> ABM Servicio Banco
	public ServicioBancoSearchPage getServicioBancoSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public ServicioBancoSearchPage getServicioBancoSearchPageResult(UserContext userContext, ServicioBancoSearchPage servicioBancoSearchPage) throws DemodaServiceException;

	public ServicioBancoAdapter getServicioBancoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ServicioBancoAdapter getServicioBancoAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public ServicioBancoAdapter getServicioBancoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public ServicioBancoVO createServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException;
	public ServicioBancoVO updateServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException;
	public ServicioBancoVO deleteServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException;
	public ServicioBancoVO activarServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException;
	public ServicioBancoVO desactivarServicioBanco(UserContext userContext, ServicioBancoVO servicioBancoVO) throws DemodaServiceException;
	// <--- ABM Servicio Banco
	
	// ---> ABM Servicio Banco Recurso
	public SerBanRecAdapter getSerBanRecAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SerBanRecAdapter getSerBanRecAdapterForCreate(UserContext userContext, CommonKey servicioBancoCommonKey) throws DemodaServiceException;	
	public SerBanRecAdapter getSerBanRecAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public SerBanRecVO createSerBanRec(UserContext userContext, SerBanRecVO serBanRecVO) throws DemodaServiceException;
	public SerBanRecVO updateSerBanRec(UserContext userContext, SerBanRecVO serBanRecVO) throws DemodaServiceException;
	public SerBanRecVO deleteSerBanRec(UserContext userContext, SerBanRecVO serBanRecVO) throws DemodaServiceException;
	// <--- ABM Servicio Banco Recurso

	// ---> ABM Banco
	public BancoSearchPage getBancoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public BancoSearchPage getBancoSearchPageResult(UserContext userContext, BancoSearchPage bancoSearchPage) throws DemodaServiceException;
    public BancoAdapter getBancoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public BancoAdapter getBancoAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public BancoVO createBanco(UserContext userContext, BancoVO bancoVO) throws DemodaServiceException;
	public BancoAdapter getBancoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public BancoVO updateBanco(UserContext userContext, BancoVO bancoVO) throws DemodaServiceException;
	public BancoVO deleteBanco(UserContext userContext, BancoVO bancoVO) throws DemodaServiceException;
	public BancoAdapter imprimirBanco(UserContext userContext, BancoAdapter bancoAdapterVO ) throws DemodaServiceException;
	// <--- ABM Banco	
}
