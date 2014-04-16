//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.CorridaProcesoMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaIncProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasArmadoSeleccionAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasConsPorCtaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasPlanillasDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.DevolucionDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqComAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqComSearchPage;
import ar.gov.rosario.siat.gde.iface.model.LiqComVO;
import ar.gov.rosario.siat.gde.iface.model.ProMasProExcAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProMasProExcVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoLiqComAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdmProcesoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoReportesDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.model.SelAlmDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaSearchPage;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeGDeudaJudicialService {

	public ProcesoMasivoSearchPage getProcesoMasivoSearchPageInit(UserContext userContext, CommonKey tipProMasKey) throws DemodaServiceException;
	public ProcesoMasivoSearchPage getProcesoMasivoSearchPageResult(UserContext userContext, ProcesoMasivoSearchPage procesoMasivoSearchPage) throws DemodaServiceException;
	
	public ProcesoMasivoAdapter getProcesoMasivoAdapterForView(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;	
	public ProcesoMasivoAdapter getProcesoMasivoAdapterForCreate(UserContext userContext, CommonKey tipProMasKey) throws DemodaServiceException;
	public ProcesoMasivoAdapter paramFormulario(UserContext userContext, ProcesoMasivoAdapter procesoMasivoAdapterVO) throws DemodaServiceException;
	public ProcesoMasivoAdapter getProcesoMasivoAdapterParamFecEnvRec(UserContext userContext, ProcesoMasivoAdapter procesoMasivoAdapter) throws DemodaServiceException;
	public ProcesoMasivoAdapter getProcesoMasivoAdapterForUpdate(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	public ProcesoMasivoAdapter getProcesoMasivoAdapterParamUtCri(UserContext userContext, ProcesoMasivoAdapter procesoMasivoAdapter) throws DemodaServiceException;

	public ProcesoMasivoAdmProcesoAdapter getProcesoMasivoAdmProcesoAdapterInit(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	public ProcesoMasivoAdmProcesoAdapter cargarTotalesProcesoMasivoAdmProcesoAdapter(UserContext userContext, ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapter) throws DemodaServiceException;

	
	public ProcesoMasivoAdmProcesoAdapter generarArchivosCDProcuradores(UserContext userContext, ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapter) throws DemodaServiceException;

	// sacar public ProcesoMasivoAdmProcesoAdapter getProcesoMasivoAdmProcesoAdapterSeleccionarDeudaIncluir(UserContext userContext, ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapter) throws DemodaServiceException;

	
	public ProcesoMasivoVO createProcesoMasivo(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException;
	public ProcesoMasivoVO updateProcesoMasivo(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException;
	public ProcesoMasivoVO deleteProcesoMasivo(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException;
	public ProcesoMasivoVO enviadoContr(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException;

	// DEUDA INCLUIDA

	// eliminacion de la deuda incluida 
	public DeudaIncProMasEliminarSearchPage getDeudaIncProMasEliminarSearchPageInit(UserContext userContext,  CommonKey procesoMasivoKey) throws DemodaServiceException;
	public DeudaIncProMasEliminarSearchPage getDeudaIncProMasEliminarSearchPageResult(UserContext userContext, DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage) throws DemodaServiceException;
	public DeudaIncProMasEliminarSearchPage eliminarTodaDeudaProMasicialSeleccionada(UserContext userContext, DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO) throws DemodaServiceException;
	// eliminacion de la deuda incluida: a traves de la seleccion individual
	public DeudaIncProMasEliminarSearchPage getDeudaIncProMasElimSelIndSeachPageInit(UserContext userContext, DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage) throws DemodaServiceException;
	public DeudaIncProMasEliminarSearchPage eliminarSelIndDeudaIncProMas(UserContext userContext, DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage) throws DemodaServiceException;
	// logs de armado de la seleccion de la deuda incluida
	public DeudaProMasArmadoSeleccionAdapter getDeudaIncProMasArmadoSeleccionAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	// limpiarSeleccion de la deuda incluida 
	public SelAlmDeudaVO limpiarSelAlmDetDeudaIncluidaProcesoMasivo(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	// planillas de las deudas incluida
	public DeudaProMasPlanillasDeudaAdapter getDeudaIncProMasPlanillasDeudaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	// planillas de las cuotas de convenios incluidas en la selAlmDet
	public DeudaProMasPlanillasDeudaAdapter getConvenioCuotaIncProMasPlanillasDeudaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	
	// consulta de deuda a incluir por cuenta
	public DeudaProMasConsPorCtaSearchPage getDeudaProMasConsPorCtaSearchPageInit(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	public DeudaProMasConsPorCtaSearchPage getDeudaProMasConsPorCtaSearchPageParamCuenta(UserContext userContext, DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPage) throws DemodaServiceException;
	public DeudaProMasConsPorCtaSearchPage getDeudaIncProMasConsPorCtaSearchPageResult(UserContext userContext, DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPage) throws DemodaServiceException;

	// DEUDA EXCLUIDA
	// agregacion de la deuda excluida
	public DeudaExcProMasAgregarSearchPage getDeudaExcProMasAgregarSearchPageInit(UserContext userContext,  CommonKey procesoMasivoKey) throws DemodaServiceException;
	public DeudaExcProMasAgregarSearchPage getDeudaExcProMasAgregarSearchPageResult(UserContext userContext, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws DemodaServiceException;
	public DeudaExcProMasAgregarSearchPage agregarTodaDeudaExcProMasicialSeleccionada(UserContext userContext, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPageVO) throws DemodaServiceException;

	// agregacion de la deuda excluida: a traves de la seleccion individual
	public DeudaExcProMasAgregarSearchPage getDeudaExcProMasAgregarSelIndSeachPage(UserContext userContext, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws DemodaServiceException;
	public DeudaExcProMasAgregarSearchPage agregarSelIndDeudaExcProMas(UserContext userContext, DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws DemodaServiceException;

	// eliminacion de la deuda excluida 
	public DeudaExcProMasEliminarSearchPage getDeudaExcProMasEliminarSearchPageInit(UserContext userContext,  CommonKey procesoMasivoKey) throws DemodaServiceException;
	public DeudaExcProMasEliminarSearchPage getDeudaExcProMasEliminarSearchPageResult(UserContext userContext, DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws DemodaServiceException;
	public DeudaExcProMasEliminarSearchPage eliminarTodaDeudaExcProMasicialSeleccionada(UserContext userContext, DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO) throws DemodaServiceException;
	public SelAlmDeudaVO limpiarSelAlmDetDeudaExcluidaProcesoMasivo(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	// eliminacion de la deuda excluida: a traves de la seleccion individual
	public DeudaExcProMasEliminarSearchPage getDeudaExcProMasElimSelIndSeachPageInit(UserContext userContext, DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws DemodaServiceException;
	public DeudaExcProMasEliminarSearchPage eliminarSelIndDeudaExcProMas(UserContext userContext, DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage) throws DemodaServiceException;

	// logs de armado de la seleccion de la deuda excluida
	public DeudaProMasArmadoSeleccionAdapter getDeudaExcProMasArmadoSeleccionAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;

	// planillas de las deudas excluida
	public DeudaProMasPlanillasDeudaAdapter getDeudaExcProMasPlanillasDeudaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	public DeudaProMasConsPorCtaSearchPage getDeudaExcProMasConsPorCtaSearchPageResult(UserContext userContext, DeudaProMasConsPorCtaSearchPage deudaExcProMasConsPorCtaSearchPage) throws DemodaServiceException;
	public DeudaProMasPlanillasDeudaAdapter getCuentaIncProMasPlanillasDeudaAdapter(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	// Procurador a Excluir del Envio Judicial
	public ProMasProExcAdapter getProMasProExcAdapterForView(UserContext userContext, CommonKey proMasProExcKey) throws DemodaServiceException;	
	public ProMasProExcAdapter getProMasProExcAdapterForCreate(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	public ProMasProExcVO createProMasProExc(UserContext userContext, ProMasProExcVO proMasProExcVO) throws DemodaServiceException;
	public ProMasProExcVO deleteProMasProExc(UserContext userContext, ProMasProExcVO proMasProExcVO) throws DemodaServiceException;

	// reportes de las deudas incluida
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesDeudaIncluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;

	// reportes de las deudas excluida
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesDeudaExcluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;

	// reportes de los convenioCuota incluidos
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesConvenioCuotaIncluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;

	// reportes de las cuenta incluidas
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesCuentaIncluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;

	// reportes de los convenioCuota excluidos
	public ProcesoMasivoReportesDeudaAdapter getProcesoMasivoReportesConvenioCuotaExcluidaAdapter(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;

	// retroceder paso sobre el envio judicial sin usar adp
	public CorridaProcesoMasivoAdapter getCorridaProcesoMasivoAdapterForView(UserContext userContext, CommonKey procesoMasivoKey) throws DemodaServiceException;
	public ProcesoMasivoVO retrocederPaso(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException;
	public ProcesoMasivoVO reiniciarPaso(UserContext userContext, ProcesoMasivoVO procesoMasivoVO) throws DemodaServiceException;
	
	// Traspaso de Deuda entre Procuradores y Devoluciones de deuda a Via Administrativa
	public TraspasoDevolucionDeudaSearchPage getTraspasoDevolucionDeudaSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public TraspasoDevolucionDeudaSearchPage getTraspasoDevolucionDeudaSearchPageParamCuenta(UserContext userContext, TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage) throws DemodaServiceException;
	public TraspasoDevolucionDeudaSearchPage getTraspasoDevolucionDeudaSearchPageParamRecurso(UserContext userContext, TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage) throws DemodaServiceException;
	public TraspasoDevolucionDeudaSearchPage getTraspasoDevolucionDeudaSearchPageResult(UserContext userContext, TraspasoDevolucionDeudaSearchPage traspasoDevolucionDeudaSearchPage) throws DemodaServiceException;

	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterForView(UserContext userContext, CommonKey traspasoDevolucionKey, CommonKey accionKey) throws DemodaServiceException;
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterForCreateByConstancia(UserContext userContext, CommonKey constanciaKey) throws DemodaServiceException;
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterParamRecurso(UserContext userContext, TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter) throws DemodaServiceException;
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterParamCuenta(UserContext userContext, TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter) throws DemodaServiceException;
	public TraspasoDevolucionDeudaAdapter getTraspasoDevolucionDeudaAdapterForUpdate(UserContext userContext, CommonKey traspasoDevolucionKey, CommonKey accionKey) throws DemodaServiceException;

	public TraspasoDeudaVO createTraspasoDeuda(UserContext userContext, TraspasoDeudaVO traspasoDeudaVO) throws DemodaServiceException;
	public TraspasoDeudaVO updateTraspasoDeuda(UserContext userContext, TraspasoDeudaVO traspasoDeudaVO) throws DemodaServiceException;
	public TraspasoDeudaVO deleteTraspasoDeuda(UserContext userContext, TraspasoDeudaVO traspasoDeudaVO) throws DemodaServiceException;

	public DevolucionDeudaVO createDevolucionDeuda(UserContext userContext, DevolucionDeudaVO devolucionDeudaVO) throws DemodaServiceException;
	public DevolucionDeudaVO updateDevolucionDeuda(UserContext userContext, DevolucionDeudaVO devolucionDeudaVO) throws DemodaServiceException;
	public DevolucionDeudaVO deleteDevolucionDeuda(UserContext userContext, DevolucionDeudaVO devolucionDeudaVO) throws DemodaServiceException;
	
	public TraspasoDevolucionDeudaAdapter getTraDeuDetAdapterForCreate(UserContext userContext, CommonKey traspasoDeudaKey, CommonKey constanciaKey) throws DemodaServiceException;
	public TraspasoDevolucionDeudaAdapter getDevDeuDetAdapterForCreate(UserContext userContext, CommonKey devolucionDeudaKey) throws DemodaServiceException;
	
	public TraspasoDevolucionDeudaAdapter createListTraDeuDet(UserContext userContext, TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter) throws DemodaServiceException;
	public TraspasoDevolucionDeudaAdapter createListDevDeuDet(UserContext userContext, TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapter) throws DemodaServiceException;

	// ---> ABM LiqCom
	public LiqComSearchPage getLiqComSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public LiqComSearchPage getLiqComSearchPageResult(UserContext userContext, LiqComSearchPage searchPage) throws DemodaServiceException;
	public LiqComSearchPage getLiqComSearchPageParamProcuradores(UserContext userContext, LiqComSearchPage liqComSearchPage) throws DemodaServiceException;

	public LiqComAdapter getLiqComAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public LiqComVO createLiqCom(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException;
	public LiqComAdapter getLiqComAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public LiqComVO updateLiqCom(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException;
	public LiqComAdapter getLiqComAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException;
	public LiqComVO deleteLiqCom(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException;
	public LiqComAdapter getLiqComAdapterParamProcuradores(UserContext userContext, LiqComAdapter liqComAdapterVO) throws DemodaServiceException;
	public LiqComSearchPage getLiqComSearchPageParamRecurso(UserContext userContext, LiqComSearchPage liqComSearchPage) throws DemodaServiceException;
	public LiqComAdapter getLiqComAdapterParamRecurso(UserContext userContext, LiqComAdapter liqComAdapter) throws DemodaServiceException;	
	
	public ProcesoLiqComAdapter getProcesoLiqComAdapterInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public LiqComVO activar(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException;
	public LiqComVO reprogramar(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException;
	public LiqComVO cancelar(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException;
	public LiqComVO reiniciar(UserContext userContext, LiqComVO liqComVO) throws DemodaServiceException;
	public ProcesoMasivoAdmProcesoAdapter getProcMasivoActProcRecofAdapter(UserContext userContext, ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO)throws DemodaServiceException;
	// <--- ABM LiqCom

}	
