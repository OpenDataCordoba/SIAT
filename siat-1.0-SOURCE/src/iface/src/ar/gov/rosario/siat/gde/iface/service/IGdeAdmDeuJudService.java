//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import ar.gov.rosario.siat.gde.iface.model.ConDeuDetAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.ConDeuTitAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConDeuTitVO;
import ar.gov.rosario.siat.gde.iface.model.ConRecNoLiqAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConRecNoLiqSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.CuentasProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeuCueGesJudSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeuJudSinConstanciaAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeuJudSinConstanciaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.gde.iface.model.EmisionExternaAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudDeuVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudEventoAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudEventoVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudReport;
import ar.gov.rosario.siat.gde.iface.model.GesJudSearchPage;
import ar.gov.rosario.siat.gde.iface.model.GesJudVO;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeAdmDeuJudService{

	// ---> Adm. Planillas Envio Deuda
	public PlaEnvDeuProSearchPage getPlaEnvDeuProSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public PlaEnvDeuProSearchPage getPlaEnvDeuProSearchPageResult(UserContext userContext, PlaEnvDeuProSearchPage searchPage) throws DemodaServiceException;
	public PlaEnvDeuProAdapter getPlaEnvDeuProAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlaEnvDeuProAdapter getPlaEnvDeuProAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlaEnvDeuProVO updatePlaEnvDeuPro(UserContext userContext, PlaEnvDeuProVO plaEnvDeuProVO) throws DemodaServiceException;
	public PlaEnvDeuProAdapter getPlaEnvDeuProAdapterForHabilitar(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;	
	public PlaEnvDeuProVO habilitarPlanilla(UserContext userContext,  PlaEnvDeuProVO plaEnvDeuProVO)throws DemodaServiceException;
	public PlaEnvDeuProSearchPage getPlaEnvDeuProSearchPageParamProcuradorByRecurso(UserContext userContext,PlaEnvDeuProSearchPage plaEnvDeuProSearchPage) throws DemodaServiceException;
	
	public PlaEnvDeuProAdapter getPlaEnvDeuProGenerarArchivoCD(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;

	public PrintModel recomponerPlanilla(UserContext userContext, PlaEnvDeuProVO plaEnvDeuProVO)throws DemodaServiceException;
	public PrintModel imprimirPadron(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;
	public PrintModel imprimirConstanciasPlanilla(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;
	public PlaEnvDeuProAdapter getPlaEnvDeuProAdapterForHabilitarConstancias(UserContext userContext, PlaEnvDeuProAdapter plaEnvDeuProAdapter)throws DemodaServiceException;
	public PlaEnvDeuProAdapter habilitarConstancias(UserContext userContext,  PlaEnvDeuProAdapter plaEnvDeuProAdapter)throws DemodaServiceException;
	public ProcesoMasivoAdapter getArchivosProcuradorForView(UserContext userContext) throws DemodaServiceException;
	// <--- Adm. Planillas Envio Deuda
	
	// ---> Adm. Constancias Deuda
	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageInit(UserContext usercontext, ConstanciaDeuSearchPage constanciaDeuSearchPageFiltro) throws DemodaServiceException;
	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageParamProcuradorByRecurso(UserContext userContext,ConstanciaDeuSearchPage constanciaDeuSearchPageVO) throws DemodaServiceException;
	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageResult(UserContext userContext, ConstanciaDeuSearchPage searchPage) throws DemodaServiceException;
	
	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageParamCuenta(UserContext userContext, ConstanciaDeuSearchPage searchPage)	throws DemodaServiceException;
	public ConstanciaDeuAdapter getConstanciaDeuAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ConstanciaDeuVO createConstanciaDeu(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapterVO) throws DemodaServiceException;
	public ConstanciaDeuVO deleteConstanciaDeu(UserContext userContext, ConstanciaDeuVO constanciaDeuVO) throws DemodaServiceException;
	public ConstanciaDeuAdapter habilitarConstanciaDeu(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter) throws DemodaServiceException;
	public PrintModel recomponerConstanciaDeu(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapterVO) throws DemodaServiceException;
	public PrintModel imprimirConstanciaDeu(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapterVO) throws DemodaServiceException;
	public ConstanciaDeuAdapter getConstanciaDeuAdapterParamProcuradorByRecurso(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter)	throws DemodaServiceException;
	public ConstanciaDeuAdapter getConstanciaDeuAdapterParamCuenta(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter)	throws DemodaServiceException;
	
	public ConstanciaDeuAdapter getConstanciaDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey)	throws DemodaServiceException;
	public ConstanciaDeuAdapter getConstanciaDeuAdapterForView(UserContext userContext, CommonKey commonKey)	throws DemodaServiceException;
	public ConstanciaDeuVO anularConstanciaDeu(UserContext userContext, ConstanciaDeuVO constanciaDeuVO) throws DemodaServiceException;
	public ConstanciaDeuAdapter getConstanciaDeuAdapterForUpdateDomicilioEnvio(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ConstanciaDeuVO updateConstanciaDeu(UserContext userContext, ConstanciaDeuVO constanciaDeuVO) throws DemodaServiceException;
	public ConstanciaDeuAdapter updateConstanciaDeuDomicilio(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter) throws DemodaServiceException;
	
	public ConDeuTitAdapter getConDeuTitAdapterForView(UserContext userContext, CommonKey commonKey)	throws DemodaServiceException;
	public ConDeuTitVO deleteConDeuTit(UserContext userContext, ConDeuTitVO conDeuTitVO) throws DemodaServiceException;
	public ConstanciaDeuAdapter agregarConDeuTit(UserContext userContext, ConstanciaDeuAdapter constanciaDeuAdapter, Long idPersona) throws DemodaServiceException;

	public ConDeuDetAdapter getConDeuDetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ConDeuDetAdapter createConDeuDet(UserContext userContext,	ConDeuDetAdapter conDeuDetAdapter) throws DemodaServiceException;
	public ConDeuDetAdapter getConDeuDetAdapterForCreate(UserContext userContext, Long idConstanciaDeu) throws DemodaServiceException;
	public ConDeuDetAdapter getConDeuDetAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ConDeuDetVO updateConDeuDet(UserContext userContext,	ConDeuDetVO conDeuDet) throws DemodaServiceException;
	public ConDeuDetVO deleteConDeuDet(UserContext userContext, ConDeuDetVO conDeuDet) throws DemodaServiceException;
	// <--- Adm. Constancias Deuda	
	
	// ---> Deuda Judicial Sin Constancia 
	public DeuJudSinConstanciaSearchPage getDeuJudSinConstanciaSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public DeuJudSinConstanciaSearchPage getDeuJudSinConstanciaSearchPageResult(UserContext userContext, DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPage) throws DemodaServiceException;
	public DeuJudSinConstanciaSearchPage getDeuJudSinConstanciaSearchPageParamCuenta(UserContext userContext, DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPage) throws DemodaServiceException;

	public DeuJudSinConstanciaAdapter getDeuJudSinConstanciaAdapterForView(UserContext userContext, DeudaVO deuda, String[] listIdSelected) throws DemodaServiceException;
	public DeuJudSinConstanciaAdapter getDeuJudSinConstanciaAdapterParamConstancia(UserContext userContext, DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapter) throws DemodaServiceException;
	public DeuJudSinConstanciaAdapter createConstanciaDeuDet(UserContext userContext,	DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapter) throws DemodaServiceException;
	// <--- Deuda Judicial Sin Constancia
	
	// ---> Consulta de cuentas por Procurador
	public CuentasProcuradorSearchPage getCuentasProcuradorSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public CuentasProcuradorSearchPage getCuentasProcuradorSearchPageResult(UserContext userContext, CuentasProcuradorSearchPage searchPage) throws DemodaServiceException ;
	public CuentasProcuradorSearchPage getCuentasProcuradorParamProcurador(UserContext userContext, CuentasProcuradorSearchPage searchPage) throws DemodaServiceException;
	public CuentasProcuradorSearchPage getCuentasProcuradorParamCuenta(UserContext userContext, CuentasProcuradorSearchPage searchPage) throws DemodaServiceException;
	// <--- Consulta de cuentas por Procurador

	// ---> ADM Gestion Judicial
	public GesJudSearchPage getGesJudSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public GesJudSearchPage getGesJudSearchPageResult(UserContext userContext, GesJudSearchPage searchPage)	throws DemodaServiceException;
	public GesJudSearchPage getGesJudSearchPageParamCuenta(UserContext userContext, GesJudSearchPage searchPage) throws DemodaServiceException;
	public GesJudAdapter getGesJudAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public GesJudVO createGesJud(UserContext userContext, GesJudVO gesJudVO) throws DemodaServiceException;
	public GesJudAdapter getGesJudAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public GesJudVO updateGesJud(UserContext userContext, GesJudVO gesJudVO) throws DemodaServiceException;
	public GesJudAdapter getGesJudAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public GesJudVO registrarCaducidad(UserContext userContext, GesJudVO gesJudVO) throws DemodaServiceException;
	public GesJudVO deleteGesJud(UserContext userContext, GesJudVO gesJudVO) throws DemodaServiceException;
	
	public GesJudDeuAdapter getGesJudDeuAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException ;
	public GesJudDeuAdapter getGesJudDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException ;
	public GesJudDeuAdapter getGesJudDeuAdapterResult(UserContext userContext, GesJudDeuAdapter gesJudDeuAdapter)	throws DemodaServiceException ;
	public GesJudDeuAdapter getGesJudDeuAdapterForCreate(UserContext userContext, CommonKey idGesJud) throws DemodaServiceException ;
	public GesJudDeuAdapter getGesJudDeuAdapterForCreateFromConstancia(UserContext userContext, Long idGesjud, Long idConstanciaDeu) throws DemodaServiceException;
	public GesJudDeuAdapter createGesJudDeu(UserContext userContext, GesJudDeuAdapter gesJudDeuAdapter) throws DemodaServiceException ;
	public GesJudDeuVO updateGesJudDeu(UserContext userContext,	GesJudDeuVO gesJudDeu) throws DemodaServiceException ;
	public GesJudDeuVO deleteGesJudDeu(UserContext userContext,	GesJudDeuVO gesJudDeu) throws DemodaServiceException ;
	public GesJudDeuAdapter getGesJudDeuAdapterParamCuenta(UserContext userContext, GesJudDeuAdapter gesJudDeuAdapter)	throws DemodaServiceException;
	public ConstanciaDeuSearchPage getConstanciaDeuSearchPageforGesJud(UserContext userContext) throws DemodaServiceException;
	
	public GesJudEventoAdapter getGesJudEventoAdapterForCreate(UserContext userContext, CommonKey idGesJud) throws DemodaServiceException;
	public GesJudEventoVO createGesJudEvento(UserContext userContext, GesJudEventoVO gesJudEventoVO) throws DemodaServiceException;
	public GesJudEventoAdapter getGesJudEventoAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException;	
	public GesJudEventoVO deleteGesJudEvento(UserContext userContext,	GesJudEventoVO gesJudEventoVO) throws DemodaServiceException;
	// <--- ADM Gestion Judicial
	
	// ---> Consultar Convenios/Recibos no liquidables
	public ConRecNoLiqSearchPage getConRecNoLiqSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public ConRecNoLiqSearchPage getConRecNoLiqSearchPageResult(UserContext userContext, ConRecNoLiqSearchPage searchPage)	throws DemodaServiceException;
	public ConRecNoLiqAdapter procesarConRecNoLiq(UserContext userContext, ConRecNoLiqAdapter conRecNoLiqAdapter) throws DemodaServiceException;
	public ConRecNoLiqSearchPage getConRecNoLiqSearchPageParamRecurso(UserContext userContext, ConRecNoLiqSearchPage searchPage)	throws DemodaServiceException ;
	public ConRecNoLiqAdapter volverLiquidablesConRecNoLiq(UserContext userContext, ConRecNoLiqAdapter conRecNoLiqAdapter) throws DemodaServiceException;
	// <--- Consultar Convenios/Recibos no liquidables
	
	// ---> Carga de eventos de gestion Judicial por archivo
	public EmisionExternaAdapter uploadFileEventosGesJud(UserContext userContext,EmisionExternaAdapter uploadEventoGesJudAdapter);
	public EmisionExternaAdapter cargarEventosGesJud(UserContext userContext,EmisionExternaAdapter uploadEventoGesJudAdapter) throws DemodaServiceException;
	
	// ---> Reportes de Seguimiento de las Gestión Judicial
	public GesJudReport getGesJudReportInit(UserContext userContext) throws DemodaServiceException;
	public GesJudReport getGesJudReportResult(UserContext userContext, GesJudReport gesJudReport) throws DemodaServiceException;
	public GesJudReport getGesJudReportParamRecurso(UserContext userContext, GesJudReport gesJudReport) throws DemodaServiceException;
	public GesJudReport getGesJudReportParamCuenta(UserContext userContext, GesJudReport gesJudReport) throws DemodaServiceException;
	// <--- Reportes de Seguimiento de las Gestión Judicial

	// ---> Reporte de deudas de cuenta en gestion judicial
	public DeuCueGesJudSearchPage getDeuCueGesJudSearchPageInit(UserContext userContext) throws Exception;
	public DeuCueGesJudSearchPage getDeuCueGesJudSearchPageParamCuenta(UserContext userContext, DeuCueGesJudSearchPage deuCueGesJudSearchPage) throws DemodaServiceException;
	public DeuCueGesJudSearchPage getDeuCueGesJudSearchPageResult(UserContext userContext, DeuCueGesJudSearchPage deuCueGesJudSearchPage) throws DemodaServiceException;
	// <--- Reporte de deudas de cuenta en gestion judicial	
}

