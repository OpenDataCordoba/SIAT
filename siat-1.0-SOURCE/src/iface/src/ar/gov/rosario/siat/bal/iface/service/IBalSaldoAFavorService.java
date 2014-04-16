//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorAdapter;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalSaldoAFavorService {
	
	// ---> ABM SaldoAFavor
	public SaldoAFavorSearchPage getSaldoAFavorSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public SaldoAFavorSearchPage getSaldoAFavorSearchPageResult(UserContext usercontext, SaldoAFavorSearchPage saldoAFavorSearchPage) throws DemodaServiceException;

	public SaldoAFavorAdapter getSaldoAFavorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SaldoAFavorAdapter getSaldoAFavorAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public SaldoAFavorAdapter getSaldoAFavorAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public SaldoAFavorVO createSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO ) throws DemodaServiceException;
	public SaldoAFavorVO updateSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO ) throws DemodaServiceException;
	public SaldoAFavorVO deleteSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO ) throws DemodaServiceException;
	public SaldoAFavorVO activarSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO ) throws DemodaServiceException;
	public SaldoAFavorVO desactivarSaldoAFavor(UserContext userContext, SaldoAFavorVO saldoAFavorVO ) throws DemodaServiceException;
	public SaldoAFavorAdapter imprimirSaldoAFavor(UserContext userContext, SaldoAFavorAdapter saldoAFavorVO ) throws DemodaServiceException;	
	// <--- ABM SaldoAFavor
	
}
