//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.ConvenioSearchPage;
import ar.gov.rosario.siat.gde.iface.model.CtrlInfDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.CtrlInfDeuVO;
import ar.gov.rosario.siat.gde.iface.model.CuentaObjImpSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurDetAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurPagAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DecJurVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.InformeDeudaCaratula;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDetalleDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReclamoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ReciboAdapter;
import ar.gov.rosario.siat.gde.iface.model.ReciboSearchPage;
import ar.gov.rosario.siat.gde.iface.model.TramiteAdapter;
import ar.gov.rosario.siat.gde.iface.model.TramiteSearchPage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TipoRecibo;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeGDeudaService {

	// ---> Cuentas por Contribuyente
	public DeudaContribSearchPage getDeudaContribSearchPageInit(UserContext userContext, CommonKey contribuyenteKey) throws DemodaServiceException;
	public DeudaContribSearchPage getDeudaContribSearchPageParamContribuyente(UserContext userContext, DeudaContribSearchPage deudaContribSearchPage) throws DemodaServiceException;
	public DeudaContribSearchPage getDeudaContribSearchPageParamServicioBanco(UserContext userContext, DeudaContribSearchPage deudaContribSearchPage) throws DemodaServiceException;
	// <--- Cuentas por Contribuyente
	
	// ---> Liquidacion de Deuda
	
	public LiqDeudaAdapter initLiqDeudaAdapter(LiqDeudaAdapter liqDeudaAdapter) throws DemodaServiceException;
	
	public LiqDeudaAdapter getLiqDeudaAdapterGRInit(UserContext userContext) throws DemodaServiceException;

	public LiqDeudaAdapter getLiqDeudaAdapterContrInit(UserContext userContext, CommonKey recursoKey) throws DemodaServiceException;
	
	public LiqDeudaAdapter getLiqDeudaAdapterParamRecurso(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	public LiqDeudaAdapter getLiqDeudaAdapterParamCuenta(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
		
	public LiqDeudaAdapter getLiqDeudaAdapterByRecursoNroCuenta(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	public LiqDeudaAdapter getIdCuentaByNroCuentaCodGesPer(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	public LiqDeudaAdapter getLiqDeudaAdapterByIdCuenta(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	public LiqDetalleDeudaAdapter getLiqDetalleDeudaAdapter(UserContext userContext, LiqDetalleDeudaAdapter liqDetalleDeudaAdapterVO) throws DemodaServiceException;

	public LiqReclamoAdapter getLiqReclamoAdapterInit(UserContext userContext, LiqReclamoAdapter liqReclamoAdapterVO) throws DemodaServiceException;
	
	public LiqReclamoAdapter getLiqReclamoCuotaAdapterInit(UserContext userContext, LiqReclamoAdapter liqReclamoAdapterVO) throws DemodaServiceException;
	
	public LiqReclamoAdapter createReclamoDeuda(UserContext userContext, LiqReclamoAdapter liqReclamoAdapterVO) throws DemodaServiceException;
	
	public LiqReclamoAdapter createReclamoCuota(UserContext userContext, LiqReclamoAdapter liqReclamoAdapterVO) throws DemodaServiceException;
	
	public LiqConvenioCuentaAdapter getLiqConvenioCuentaInit(UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuenta)throws DemodaServiceException;
	
	public PrintModel imprimirInformeDeudaEscribano(UserContext userContext,long idCuenta, InformeDeudaCaratula informeDeudaCaratula) throws DemodaServiceException;
	
	public PrintModel imprimirInformeLiqDeuda(UserContext userContext,LiqCuentaVO cuenta, InformeDeudaCaratula informeDeudaCaratula) throws DemodaServiceException;
	
	public LiqDeudaAdapter getByRecursoNroCuenta4DeudaReclamada(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
		
	public ConvenioSearchPage getConvenioSearchPageInit (UserContext userContext) throws DemodaServiceException;
	
	public ConvenioSearchPage getConvenioSearchPageResult (UserContext userContext, ConvenioSearchPage convenioSearchPageVO) throws DemodaServiceException;
	
	public LiqDeudaAdapter getIdCuentaByRecursoNroCuenta(UserContext userContext, LiqDeudaAdapter liqDeudaAdapter) throws DemodaServiceException;

	public PrintModel imprimirCierreComercio(UserContext userContext,long idCuenta) throws DemodaServiceException;

	public LiqDeudaAdapter getByRecursoNroCuenta4CuentaExencion(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	public LiqDeudaAdapter getByRecursoNroCuenta4DeudaExencion(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	public LiqDeudaAdapter aplicarQuitarExencion(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	// <--- Liquidacion de Deuda
	
	
	// ---> Consultar Cuenta por Objeto Imponible
	public CuentaObjImpSearchPage getCuentaObjImpSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public CuentaObjImpSearchPage getCuentaObjImpSearchPageParamObjImp(UserContext userContext, CuentaObjImpSearchPage cuentaObjImpSearchPage) throws DemodaServiceException;
	// <--- Consultar Cuenta por Objeto Imponible
	
	// ---> Consultar Estado de Cuenta
	public EstadoCuentaSearchPage getEstadoCuentaSearchPageInit(UserContext userContext, EstadoCuentaSearchPage searchPageFiltro) throws DemodaServiceException;
	public EstadoCuentaSearchPage getEstadoCuentaSearchPageParamClasificacion(UserContext userContext, EstadoCuentaSearchPage searchPage) throws DemodaServiceException;
	public EstadoCuentaSearchPage getEstadoCuentaSearchPageParamCuenta(UserContext userContext, EstadoCuentaSearchPage searchPage) throws DemodaServiceException;
	public EstadoCuentaAdapter getLiqEstadoCuentaAdapter(UserContext userContext, EstadoCuentaSearchPage searchPage) throws DemodaServiceException;
	public PrintModel getImprimirEstadoCuenta(UserContext userContext, EstadoCuentaAdapter estadoCuentaAdapter) throws Exception;
	public EstadoCuentaSearchPage getEstadoCuentaSeachPageFiltro(UserContext userContext, Long idCuenta) throws DemodaServiceException;
	// <--- Consultar Estado de Cuenta
	
	// ---> Informe Escribano
	public TramiteSearchPage getTramiteSearchPageInit (UserContext userContext) throws DemodaServiceException;
	public TramiteSearchPage getTramiteSearchPageResult (UserContext userContext, TramiteSearchPage tramiteSearchPage) throws DemodaServiceException;
	public TramiteAdapter getTramiteAdapterInit(UserContext userContext)  throws DemodaServiceException;
	public TramiteAdapter validarTramite(UserContext userContext, TramiteAdapter tramiteAdapter)  throws DemodaServiceException;
	public TramiteAdapter registrarUsoTramite(UserContext userContext, TramiteAdapter tramiteAdapter)  throws DemodaServiceException;
	
	public CtrlInfDeuAdapter getCtrlInfDeuAdapterForDesbloquear(UserContext userContext, CtrlInfDeuAdapter ctrlInfDeuAdapter)  throws DemodaServiceException;	
	// <--- Informe Escribano
	
	// ---> Anular/Deanular Deuda
	public LiqDeudaAdapter getByRecursoNroCuenta4AnularDeuda(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	public LiqDeudaAdapter getLiqDeudaAdapterAnularInit(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	public LiqDeudaAdapter getAnularDeuda(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	public void vueltaAtrasAnular(UserContext userContext, CommonKey deudaKey) throws DemodaServiceException;
	// <--- Anular/Deanular Deuda
	
	// ---> Consulta Recibo
	public ReciboSearchPage getReciboSearchPageInit (UserContext userContext) throws DemodaServiceException;
	public ReciboSearchPage getReciboSearchPageResult (UserContext userContext, ReciboSearchPage reciboSearchPage) throws DemodaServiceException;
	public ReciboAdapter getReciboAdapterForView(UserContext userContext, CommonKey reciboKey, TipoRecibo tipoRecibo) throws DemodaServiceException;
	// <--- Consulta Recibo

	public LiqConvenioCuentaAdapter imprimirLiqConvenioCuenta(UserContext userContext, LiqConvenioCuentaAdapter liqConvenioCuentaAdapter) throws DemodaServiceException;
	
	
	// ---> Consulta Codigo Referenia Pago
	public LiqCodRefPagSearchPage getLiqCodRefPagSearchPageInit(UserContext userContext)throws DemodaServiceException;
	public LiqCodRefPagSearchPage getLiqCodRefPagSearchPageResult(UserContext userContext, LiqCodRefPagSearchPage liqCodRefPagSearchPage)throws DemodaServiceException;
	// <--- Consulta Codigo Referenia Pago

	// ---> Consulta Declaracion Jurada
	public DecJurSearchPage getDecJurSearchPageInit(UserContext userContext, DecJurSearchPage decJurSearchPage) throws DemodaServiceException;
	public DecJurSearchPage getDecJurSearchPageResult(UserContext userContext, DecJurSearchPage decJurSearchPage) throws DemodaServiceException;

	//public DecJurAdapter getDecJurAdapterForCreate(UserContext userContext, Long idCuenta, Long idDeuda) throws DemodaServiceException;
	public DecJurAdapter getDecJurAdapterForUpdate(UserContext userContext, Long idDecJur) throws DemodaServiceException;
	public DecJurAdapter getDecJurAdapterForView(UserContext userContext, Long idDecJur) throws DemodaServiceException;
	//public DecJurAdapter createDecJur(UserContext userContext, DecJurAdapter decJurAdapter) throws DemodaServiceException;
	public DecJurVO deleteDecJur(UserContext userContext, DecJurVO decJurVO) throws DemodaServiceException;
	public DecJurVO vueltaAtrasDecJur(UserContext userContext, DecJurVO decJurVO) throws DemodaServiceException;
	
	public DecJurAdapter updateDecJurAdicionalesAdapter (UserContext userContext, DecJurAdapter decJurAdapter)throws DemodaServiceException;
	//public DecJurAdapter procesarDDJJ (UserContext userContext, DecJurAdapter decJurAdapter)throws DemodaServiceException;
	
	public DecJurDetAdapter getDecJurDetAdapterForCreate(UserContext userContext, Long idDecJur) throws DemodaServiceException;
	public DecJurDetAdapter getDecJurDetAdapterForUpdate(UserContext userContext, CommonKey selectedId) throws DemodaServiceException;
	public DecJurDetAdapter getDecJurDetAdapterForView(UserContext userContext, CommonKey selectedId) throws DemodaServiceException;
	public DecJurDetAdapter getDecJurDetParamUnidad(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException;
	public DecJurDetAdapter getDecJurDetParamTipUni(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException;
	public DecJurDetAdapter createDecJurDet(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException;
	public DecJurDetAdapter updateDecJurDet(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException;
	public DecJurDetAdapter deleteDecJurDet(UserContext userContext, DecJurDetAdapter decJurDetAdapter) throws DemodaServiceException;
	
	public DecJurPagAdapter getDecJurPagAdapterForCreate(UserContext userContext, Long idDecJur) throws DemodaServiceException;
	public DecJurPagAdapter getDecJurPagAdapterForView(UserContext userContext, CommonKey selectedId) throws DemodaServiceException;
	public DecJurPagAdapter getDecJurPagAdapterForUpdate(UserContext userContext, CommonKey selectedId) throws DemodaServiceException;
	public DecJurPagAdapter createDecJurPag(UserContext userContext, DecJurPagAdapter decJurPagAdapter) throws DemodaServiceException;
	public DecJurPagAdapter deleteDecJurPag(UserContext userContext, DecJurPagAdapter decJurPagAdapter) throws DemodaServiceException;
	public DecJurPagAdapter updateDecJurPag(UserContext userContext, DecJurPagAdapter decJurPagAdapter) throws DemodaServiceException;
	// <--- Consulta Declaracion Jurada
	
	public PrintModel imprimirListDeudaContrib(UserContext userContext, DeudaContribSearchPage deudaContribSearchPage ) throws DemodaServiceException;
	
	public LiqDetalleDeudaAdapter modificarDeuda(UserContext userContext, LiqDetalleDeudaAdapter liqDetalleDeudaAdapterVO) throws DemodaServiceException ;
	public CtrlInfDeuVO deleteCtrlInfDeu(UserContext userContext, CtrlInfDeuVO ctrlInfDeuVO) throws DemodaServiceException;
}	


