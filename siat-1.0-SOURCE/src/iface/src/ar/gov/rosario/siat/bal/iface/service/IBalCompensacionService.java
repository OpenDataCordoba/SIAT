//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;
import ar.gov.rosario.siat.bal.iface.model.ComDeuAdapter;
import ar.gov.rosario.siat.bal.iface.model.ComDeuVO;
import ar.gov.rosario.siat.bal.iface.model.CompensacionAdapter;
import ar.gov.rosario.siat.bal.iface.model.CompensacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CompensacionVO;
import ar.gov.rosario.siat.bal.iface.model.DupliceAdapter;
import ar.gov.rosario.siat.bal.iface.model.DupliceSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.model.TipoComAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoComSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoComVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;



public interface IBalCompensacionService {

	
	// --> ABM TipoCompensacion
	public TipoComSearchPage getTipoComSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public TipoComSearchPage getTipoComSearchPageResult(UserContext usercontext, TipoComSearchPage tipoComSearchPage) throws DemodaServiceException;

	public TipoComAdapter getTipoComAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoComAdapter getTipoComAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TipoComAdapter getTipoComAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public TipoComVO createTipoCom(UserContext userContext, TipoComVO tipoComVO ) throws DemodaServiceException;
	public TipoComVO updateTipoCom(UserContext userContext, TipoComVO tipoComVO ) throws DemodaServiceException;
	public TipoComVO deleteTipoCom(UserContext userContext, TipoComVO tipoComVO ) throws DemodaServiceException;
	public TipoComVO activarTipoCom(UserContext userContext, TipoComVO tipoComVO ) throws DemodaServiceException;
	public TipoComVO desactivarTipoCom(UserContext userContext, TipoComVO tipoComVO ) throws DemodaServiceException;

	public TipoComAdapter imprimirTipoCom(UserContext userContext, TipoComAdapter tipoComVO ) throws DemodaServiceException;	
	
	// <--- ABM Tipo Compensacion
	
	// ---> ABM Compensacion
	public CompensacionSearchPage getCompensacionSearchPageInit(UserContext userContext, CompensacionSearchPage compensacionSPFiltro) throws DemodaServiceException;
	public CompensacionSearchPage getCompensacionSearchPageResult(UserContext userContext, CompensacionSearchPage compensacionSearchPage) throws DemodaServiceException;
	public CompensacionSearchPage getCompensacionSearchPageParamCuenta(UserContext userContext, CompensacionSearchPage compensacionSearchPage) throws DemodaServiceException;
	
	public CompensacionAdapter getCompensacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CompensacionAdapter getCompensacionAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CompensacionAdapter getCompensacionAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public CompensacionAdapter getCompensacionAdapterParamCuenta(UserContext userContext, CompensacionAdapter compensacionAdapter) throws DemodaServiceException;
	public CompensacionAdapter getCompensacionAdapterForIncluirSaldo(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CompensacionAdapter getCompensacionAdapterForExcluirSaldo(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CompensacionAdapter incluirSaldoAFavor(UserContext userContext, CompensacionAdapter compensacionAdapter) throws DemodaServiceException; 
	public CompensacionAdapter excluirSaldoAFavor(UserContext userContext, CompensacionAdapter compensacionAdapter) throws DemodaServiceException;
		
	public CompensacionVO createCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException;
	public CompensacionVO updateCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException;
	public CompensacionVO deleteCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException;
	
	public CompensacionVO enviarCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException;
	public CompensacionVO devolverCompensacion(UserContext userContext, CompensacionVO compensacionVO) throws DemodaServiceException;

	// <--- ABM Compensacion

	// ---> ABM ComDeu	
	public ComDeuAdapter getComDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ComDeuAdapter getComDeuAdapterForCreate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;	
	public ComDeuAdapter getComDeuAdapterParamCuenta(UserContext userContext, ComDeuAdapter comDeuAdapter) throws DemodaServiceException;
	public ComDeuAdapter getComDeuAdapterParamDeuda(UserContext userContext, ComDeuAdapter comDeuAdapter) throws DemodaServiceException;		

	public ComDeuAdapter createListComDeu(UserContext userContext, ComDeuAdapter comDeuAdapter) throws DemodaServiceException;
	public ComDeuVO createComDeu(UserContext userContext, ComDeuVO comDeuVO) throws DemodaServiceException;
	public ComDeuVO deleteComDeu(UserContext userContext, ComDeuVO comDeuVO) throws DemodaServiceException;
	// <--- ABM ComDeu
	
	
	// --> ABM Duplice
	public DupliceSearchPage getDupliceSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public DupliceSearchPage getDupliceSearchPageResult(UserContext usercontext, DupliceSearchPage dupliceSearchPage) throws DemodaServiceException;

	public DupliceAdapter getDupliceAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DupliceAdapter getDupliceAdapterForImputar(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DupliceAdapter getDupliceAdapterForGenSaldo(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DupliceAdapter getDupliceAdapterParamCuenta(UserContext userContext, DupliceAdapter dupliceAdapter) throws DemodaServiceException;
	public DupliceAdapter getDupliceAdapterParamActualizar(UserContext userContext, DupliceAdapter dupliceAdapter) throws DemodaServiceException;	
	public DupliceAdapter getDupliceAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	
	public IndetVO imputarDuplice(UserContext userContext, DupliceAdapter dupliceAdapter) throws DemodaServiceException;
	public IndetVO genSaldoAFavorForDuplice(UserContext userContext, DupliceAdapter dupliceAdapter) throws DemodaServiceException;
	public IndetVO deleteDuplice(UserContext userContext, IndetVO dupliceVO) throws DemodaServiceException;
	public IndetVO createDuplice(UserContext userContext, IndetVO dupliceVO) throws DemodaServiceException;
	// <--- ABM Duplice
	
}
