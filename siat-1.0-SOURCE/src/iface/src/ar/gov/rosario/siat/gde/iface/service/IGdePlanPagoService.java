//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.AccionMasivaConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConvenioConsistenciaAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConvenioEstadosAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuotaSaldoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioPagoCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioSalPorCadAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.RescateAdapter;
import ar.gov.rosario.siat.gde.iface.model.RescateIndividualAdapter;
import ar.gov.rosario.siat.gde.iface.model.RescateSearchPage;
import ar.gov.rosario.siat.gde.iface.model.RescateVO;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoAdministrarAdapter;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoSelAdapter;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadSearchPage;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadVO;
import ar.gov.rosario.siat.gde.iface.model.VerPagosConvenioAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;


public interface IGdePlanPagoService {
	
	public RescateSearchPage getRescateSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public RescateSearchPage getRescateSearchPageParamRecurso(UserContext userContext, RescateSearchPage rescateSearchPage) throws DemodaServiceException;
	public RescateSearchPage getRescateSearchPageResult(UserContext userContext, RescateSearchPage rescateSearchPage) throws DemodaServiceException;
	
	public RescateAdapter getRescateAdapterForView(UserContext userContext, CommonKey rescateKey) throws DemodaServiceException;	
	public RescateAdapter getRescateAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public RescateAdapter getRescateAdapterParamRecurso(UserContext userContext, RescateAdapter rescateAdapter) throws DemodaServiceException;
	public RescateAdapter getRescateAdapterForUpdate(UserContext userContext, CommonKey rescateKey) throws DemodaServiceException;

	public RescateVO createRescate(UserContext userContext, RescateVO rescateVO) throws DemodaServiceException;
	public RescateVO updateRescate(UserContext userContext, RescateVO rescateVO) throws DemodaServiceException;
	public RescateVO deleteRescate(UserContext userContext, RescateVO rescateVO) throws DemodaServiceException;
	
	public LiqConvenioSalPorCadAdapter getSalPorCadAdapterForView (UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaAdapterVO) throws DemodaServiceException;
	public LiqConvenioSalPorCadAdapter createSaldoPorCaducidad (UserContext userContext, LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO)throws DemodaServiceException;
	public LiqConvenioSalPorCadAdapter createVueltaAtrasSalPorCad(UserContext userContext, LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO)throws DemodaServiceException;
	public LiqConvenioCuotaSaldoAdapter getCuotaSaldoAdapterForInit(UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuenta)throws DemodaServiceException;
		
	public LiqConvenioPagoCuentaAdapter getPagoCuentaAdapterForInit (UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuenta)throws DemodaServiceException;
	
	public LiqConvenioPagoCuentaAdapter createAplicarPagoCuenta (UserContext userContext, LiqConvenioPagoCuentaAdapter liqConvenioPagoCuenta)throws DemodaServiceException;
	public SaldoPorCaducidadSearchPage getSalPorCadSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public SaldoPorCaducidadSearchPage getSalPorCadSearchPageResults (SaldoPorCaducidadSearchPage saldoCaducidadSearchPage, UserContext userContext)throws Exception;
	
	public SalPorCadMasivoAdapter getSalPorCadMasivoForInit (UserContext userContext)throws Exception;
	
	public SalPorCadMasivoAdapter getSalPorCadMasivoParamRecurso (SalPorCadMasivoAdapter salPorCadMasivo, UserContext userContext)throws Exception;
	
	public SalPorCadMasivoAdapter createSalPorCad (SalPorCadMasivoAdapter salPorCadMasivo, UserContext userContext)throws Exception;
	
	public SalPorCadMasivoAdministrarAdapter getSalPorCadMasAdminInit (SaldoPorCaducidadVO salPorCadVO, UserContext userContext)throws Exception;
	
	public SalPorCadMasivoAdapter getSalPorCadMasivoForView(Long selectedId, UserContext userContext)throws Exception;
	
	public SalPorCadMasivoAdapter editSalPorCadMasivo(SalPorCadMasivoAdapter salPorCadAdapter,UserContext userContext)throws Exception;
	
	public SalPorCadMasivoAdapter deleteSalPorCadMasivo (SalPorCadMasivoAdapter salPorCadAdapter,UserContext userContext)throws Exception;
	
	public SalPorCadMasivoSelAdapter getSaldoPorCaducidadSel (SalPorCadMasivoSelAdapter salPorCadSelVO, UserContext userContext)throws Exception;
		
	public SalPorCadMasivoSelAdapter getSalPorCadSelQuitarConvenio(Long selectedId, SalPorCadMasivoSelAdapter salPorCadSel, UserContext userContext)throws Exception;
	
	public SalPorCadMasivoSelAdapter imprimirSalPorCadMasivoSel(UserContext userContext, SalPorCadMasivoSelAdapter salPorCadSelVO) throws  DemodaServiceException ;
	
	public VerPagosConvenioAdapter getPagosConvenio (LiqConvenioCuentaAdapter liqConvenioCuentaAdapter, UserContext userContext);
	
	public RescateAdapter getRescateAdapterForSeleccion(UserContext userContext, Long idRescate, RescateAdapter rescateAdapter) throws Exception;
	
	public RescateAdapter getRescateQuitarConvenio(Long selectedId, RescateAdapter rescateAdapter, UserContext userContext)throws Exception;
	
	public ConvenioEstadosAdapter getConvenioEstados (UserContext userContext, LiqConvenioVO liqConvenioVO)throws Exception;
	public LiqConvenioSalPorCadAdapter getVueltaAtrasSalPorCadForView (UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaAdapterVO)throws DemodaServiceException;
	
	public SaldoPorCaducidadSearchPage getSalPorCadSearchPageParamRec (UserContext userContext, SaldoPorCaducidadSearchPage salPorCadSearchPage) throws Exception;
	
	public RescateIndividualAdapter getRescateIndAdapterForInit (UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuenta)throws DemodaServiceException;
	
	public RescateIndividualAdapter createRescateIndividual (UserContext userContext, RescateIndividualAdapter rescateIndAdapter)throws DemodaServiceException;
	
	public ConvenioConsistenciaAdapter getConvenioConsistenciaAdapter(UserContext uc, CommonKey selectedId) throws Exception;
	
	public ConvenioConsistenciaAdapter createMoverDeuda(UserContext uc, CommonKey selectedId) throws Exception;
	
	public AccionMasivaConvenioAdapter getAccionMasivaConvenioAdapterForInit (UserContext userContext)throws Exception;
	
	public AccionMasivaConvenioAdapter createAccionMasivaConvenio(AccionMasivaConvenioAdapter accionMasivaAdapter, UserContext userContext)throws Exception;
	
	public AccionMasivaConvenioAdapter getAccionMasivaConvenioAdapterForView (UserContext userContext, AccionMasivaConvenioAdapter accionMasivaAdapter)throws Exception;
}
