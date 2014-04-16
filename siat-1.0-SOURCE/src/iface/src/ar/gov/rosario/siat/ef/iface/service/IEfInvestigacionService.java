//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.service;

import ar.gov.rosario.siat.ef.iface.model.ActaInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.ActasInicioAdapter;
import ar.gov.rosario.siat.ef.iface.model.AprobacionActaInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConVO;
import ar.gov.rosario.siat.ef.iface.model.OpeInvSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlContrSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlSearchPage;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalSearchPage;
import ar.gov.rosario.siat.ef.iface.model.PlanFiscalVO;
import ar.gov.rosario.siat.ef.iface.model.ProcesoOpeInvBusAdapter;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEfInvestigacionService {
	
	// ---> ABM PlanFiscal
	public PlanFiscalSearchPage getPlanFiscalSearchPageInit(UserContext usercontext) throws Exception;
	public PlanFiscalSearchPage getPlanFiscalSearchPageResult(UserContext usercontext, PlanFiscalSearchPage planFiscalSearchPage) throws DemodaServiceException;

	public PlanFiscalAdapter getPlanFiscalAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanFiscalAdapter getPlanFiscalAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public PlanFiscalAdapter getPlanFiscalAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanFiscalVO createPlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO ) throws DemodaServiceException;
	public PlanFiscalVO updatePlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO ) throws DemodaServiceException;
	public PlanFiscalVO deletePlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO ) throws DemodaServiceException;
	public PlanFiscalVO activarPlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO ) throws DemodaServiceException;
	public PlanFiscalVO desactivarPlanFiscal(UserContext userContext, PlanFiscalVO planFiscalVO ) throws DemodaServiceException;	
	// <--- ABM PlanFiscal

	// ---> ABM OpeInv
	public OpeInvSearchPage getOpeInvSearchPageInit(UserContext userContext) throws Exception;
	public OpeInvSearchPage getOpeInvSearchPageResult(UserContext userContext, OpeInvSearchPage opeInvSearchPage) throws DemodaServiceException;
	public OpeInvAdapter getOpeInvAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public OpeInvVO createOpeInv(UserContext userContext, OpeInvVO opeInvVO) throws DemodaServiceException;
	public OpeInvAdapter getOpeInvAdapterForUpdate(UserContext userContext, CommonKey commonKeyOpeInv) throws DemodaServiceException;
	public OpeInvVO updateOpeInv(UserContext userContext, OpeInvVO opeInvVO) throws DemodaServiceException;
	public OpeInvAdapter getOpeInvAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OpeInvVO deleteOpeInv(UserContext userContext, OpeInvVO opeInvVO) throws DemodaServiceException;
	// <--- ABM OpeInv	
	
	// ---> ADM OpeInvCon
	public OpeInvConSearchPage getOpeInvConSearchPageResult(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws DemodaServiceException;
	public OpeInvConAdapter getOpeInvConAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OpeInvConAdapter getOpeInvConAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OpeInvConAdapter getOpeInvConAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OpeInvConAdapter getOpeInvConAdapterParamPersona(UserContext userContext, OpeInvConAdapter opeInvConAdapter, CommonKey ckIdPersona) throws DemodaServiceException;
	public OpeInvConAdapter getOpeInvConAdapterParamSelectCuenta(UserContext userContext, OpeInvConAdapter opeInvConAdapter, Integer indexCtaSelec)	throws DemodaServiceException;
	public OpeInvConAdapter getOpeInvConAdapterParamEstado(UserContext userContext, OpeInvConAdapter opeInvConAdapter)	throws DemodaServiceException;
	public OpeInvConVO createOpeInvCon(UserContext userContext, OpeInvConVO opeInvConVO) throws DemodaServiceException;
	public OpeInvConVO updateOpeInvCon(UserContext userContext, OpeInvConVO opeInvConVO) throws DemodaServiceException;
	public OpeInvConVO excluirDeSeleccion(UserContext userContext, OpeInvConVO opeInvConVO) throws DemodaServiceException;
	public EstadoCuentaSearchPage getEstadoCuentaSeachPageFiltro(UserContext userContext,OpeInvConVO opeInvConVO, Long idOpeInvConCue);

	public OpeInvBusSearchPage getOpeInvBusSearchPageInit(UserContext userContext, Long idOpeInv, Integer tipBus) throws Exception;
	public OpeInvBusSearchPage getOpeInvBusSearchPageResult(UserContext userContext, OpeInvBusSearchPage opeInvBusSearchPage) throws Exception;

	public OpeInvBusAdapter getOpeInvBusAdapterForCreate(UserContext userContext, CommonKey commonKey, Integer tipBus) throws DemodaServiceException;
	public OpeInvBusAdapter getOpeInvBusAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OpeInvBusAdapter getOpeInvBusAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OpeInvBusAdapter createOpeInvBus(UserContext userContext, OpeInvBusAdapter OpeInvBusAdapter) throws DemodaServiceException;
	public OpeInvBusVO deleteOpeInvBusVO(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException;
	public OpeInvBusVO updateOpeInvBus(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException;

	public ProcesoOpeInvBusAdapter getProcesoOpeInvBusAdapterInit(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;
	public OpeInvBusVO activar(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException ;
	public OpeInvBusVO reprogramar(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException ;
	public OpeInvBusVO cancelar(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException;
	public OpeInvBusVO reiniciar(UserContext userContext, OpeInvBusVO opeInvBusVO) throws DemodaServiceException;
	// <--- ADM OpeInvCon
	
	// ---> ABM OpeInvConActas
	public OpeInvConSearchPage getOpeInvConActasInicioSearchPageInit(UserContext userContext) throws Exception;
	public OpeInvConSearchPage getOpeInvConActasInicioSearchPageResult(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception;
	public ActasInicioAdapter getActasInicioInvestigadorAdapterInit(UserContext userContext, ActasInicioAdapter asignarInvestigadorAdapter) throws Exception;
	public ActasInicioAdapter getActasInicioHojaRutaAdapterInit(UserContext userContext, ActasInicioAdapter asignarInvestigadorAdapter) throws Exception;
	public ActasInicioAdapter asignarInvestigador(UserContext userContext, ActasInicioAdapter asignarInvestigadorAdapter) throws Exception;
	public ActasInicioAdapter verMapa(UserContext userContext, ActasInicioAdapter actasInicioAdapter) throws Exception;
	public PrintModel getHojaDeRutaPrintModel(UserContext userContext, ActasInicioAdapter asignarInvestigadorAdapter) throws Exception;

	// ---> ADM actas
	public ActaInvAdapter getActaInvAdapterInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ActaInvAdapter getActaInvAdapterParamPersona(UserContext userContext, ActaInvAdapter actaInvAdapter) throws DemodaServiceException;
	public ActaInvAdapter getGuardarActaInv(UserContext userContext, ActaInvAdapter actaInvAdapter) throws DemodaServiceException;
	public ActaInvAdapter getPedidoAprobacionActaInv(UserContext userContext, ActaInvAdapter actaInvAdapter) throws DemodaServiceException;
	public OpeInvConSearchPage getOpeInvConActasSearchPageInit(UserContext userContext) throws Exception;
	public OpeInvConSearchPage getOpeInvConActasSearchPageParamPlan(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception;
	public OpeInvConSearchPage getOpeInvConActasSearchPageResult(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception;
	// <--- ADM actas
	
	// ---> Aprobacion de Actas
	public OpeInvConSearchPage getAprobacionActasSearchPageInit(UserContext userContext) throws Exception;
	public OpeInvConSearchPage getAprobacionActasSearchPageParamPlan(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception;
	public OpeInvConSearchPage getAprobacionActasSearchPageResult(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws Exception;
	public AprobacionActaInvAdapter getAprobacionActaInvAdapterInit(UserContext userContext,CommonKey commonKey) throws DemodaServiceException;
	public AprobacionActaInvAdapter cambiarEstadoAprobActaInv(UserContext userContext, AprobacionActaInvAdapter aprobacionActaInvAdapter) throws DemodaServiceException;
	// <--- Aprobacion de Actas
	
	// ---> Emitir Ordenes de Control
	public OrdenControlSearchPage getOrdenControlSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public OrdenControlSearchPage getOrdenControlSearchPageParamPersona(UserContext userContext, OrdenControlSearchPage ordenControlSearchPage) throws DemodaServiceException;
	public OrdenControlSearchPage getOrdenControlSearchPageResult(UserContext userContext, OrdenControlSearchPage ordenControlSearchPage) throws DemodaServiceException;
	public OrdenControlContrSearchPage OrdenControlContrSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public OrdenControlContrSearchPage getOrdenControlContrSearchPageParamPlan(UserContext userContext, OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception;
	public OrdenControlContrSearchPage getOrdenControlContrSearchPageResult(UserContext userContext, OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception;
	public OrdenControlContrSearchPage emitirOrdenControl(UserContext userContext, OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception;
	public PrintModel getPDFImpresionOrdenControl(UserContext userContext, OrdenControlAdapter ordenControlAdapterVO) throws Exception;
	public OrdenControlContrSearchPage getOrdenContrManualSearchPageInit(UserContext userContext) throws DemodaServiceException;
	// <--- Emitir Ordenes de Control
	
	public OrdenControlContrSearchPage getOrdenControlContrSearchPageParamPersona(UserContext userContext, OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception;
	
	public OrdenControlAdapter getOrdenControlAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public OrdenControlAdapter updateOrdenControl(UserContext userContext, OrdenControlAdapter ordenControlAdapter) throws DemodaServiceException;
	public OpeInvConSearchPage getOpeInvConSearchPageParamPersona(UserContext userContext, OpeInvConSearchPage opeInvConSearchPage) throws DemodaServiceException;
}
