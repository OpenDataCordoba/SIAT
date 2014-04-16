//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.service;

import java.util.List;

import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.gde.iface.model.AgeRetAdapter;
import ar.gov.rosario.siat.gde.iface.model.AgeRetSearchPage;
import ar.gov.rosario.siat.gde.iface.model.AgeRetVO;
import ar.gov.rosario.siat.gde.iface.model.DesAtrValAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesAtrValVO;
import ar.gov.rosario.siat.gde.iface.model.DesEspAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesEspExeAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesEspExeVO;
import ar.gov.rosario.siat.gde.iface.model.DesEspSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DesEspVO;
import ar.gov.rosario.siat.gde.iface.model.DesGenAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesGenSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DesGenVO;
import ar.gov.rosario.siat.gde.iface.model.DesRecClaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesRecClaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.EventoAdapter;
import ar.gov.rosario.siat.gde.iface.model.EventoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EventoVO;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionAdapter;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionSearchPage;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionVO;
import ar.gov.rosario.siat.gde.iface.model.MandatarioAdapter;
import ar.gov.rosario.siat.gde.iface.model.MandatarioSearchPage;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import ar.gov.rosario.siat.gde.iface.model.PerCobAdapter;
import ar.gov.rosario.siat.gde.iface.model.PerCobSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PerCobVO;
import ar.gov.rosario.siat.gde.iface.model.PlanAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanAtrValAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanAtrValVO;
import ar.gov.rosario.siat.gde.iface.model.PlanClaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanClaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.PlanDescuentoAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanDescuentoVO;
import ar.gov.rosario.siat.gde.iface.model.PlanExeAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanExeVO;
import ar.gov.rosario.siat.gde.iface.model.PlanForActDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanForActDeuVO;
import ar.gov.rosario.siat.gde.iface.model.PlanImpMinAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanImpMinVO;
import ar.gov.rosario.siat.gde.iface.model.PlanIntFinAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanIntFinVO;
import ar.gov.rosario.siat.gde.iface.model.PlanMotCadAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanMotCadVO;
import ar.gov.rosario.siat.gde.iface.model.PlanProrrogaAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanProrrogaVO;
import ar.gov.rosario.siat.gde.iface.model.PlanRecursoAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanSearchPage;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.model.PlanVenAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanVenVO;
import ar.gov.rosario.siat.gde.iface.model.ProRecAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecComAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecComVO;
import ar.gov.rosario.siat.gde.iface.model.ProRecDesHasAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecDesHasVO;
import ar.gov.rosario.siat.gde.iface.model.ProRecVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.SerBanDesGenAdapter;
import ar.gov.rosario.siat.gde.iface.model.SerBanDesGenVO;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaAdapter;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaVO;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoAdapter;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IGdeDefinicionService {

	// ---> ABM Servicio Banco Descuentos Generales
	public SerBanDesGenAdapter getSerBanDesGenAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public SerBanDesGenAdapter getSerBanDesGenAdapterForCreate(UserContext userContext, CommonKey servicioBancoCommonKey) throws DemodaServiceException;
	public SerBanDesGenAdapter getSerBanDesGenAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public SerBanDesGenVO createSerBanDesGen(UserContext userContext, SerBanDesGenVO serBanDesGenVO) throws DemodaServiceException;
	public SerBanDesGenVO updateSerBanDesGen(UserContext userContext, SerBanDesGenVO serBanDesGenVO) throws DemodaServiceException;
	public SerBanDesGenVO deleteSerBanDesGen(UserContext userContext, SerBanDesGenVO serBanDesGenVO) throws DemodaServiceException;
	// <--- ABM Servicio Banco Descuentos Generales
	
	
	// ---> ABM Descuentos Generales
	public DesGenSearchPage getDesGenSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public DesGenSearchPage getDesGenSearchPageResult(UserContext userContext, DesGenSearchPage desGenSearchPage) throws DemodaServiceException;
	public DesGenAdapter getDesGenAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DesGenAdapter getDesGenAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public DesGenVO createDesGen(UserContext userContext, DesGenVO desGenVO) throws DemodaServiceException;
	public DesGenAdapter getDesGenAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DesGenVO updateDesGen(UserContext userContext, DesGenVO desGenVO) throws DemodaServiceException;
	public DesGenVO deleteDesGen(UserContext userContext, DesGenVO desGenVO) throws DemodaServiceException;
	public DesGenVO activarDesGen(UserContext userContext, DesGenVO desGenVO ) throws DemodaServiceException;
	public DesGenVO desactivarDesGen(UserContext userContext, DesGenVO desGenVO ) throws DemodaServiceException;
	public DesGenAdapter imprimirDesGen(UserContext userContext, DesGenAdapter desGenAdapterVO) throws DemodaServiceException;
	// <--- ABM Descuentos Generales
	
	// ---> ABM Descuentos Especiales
	public DesEspSearchPage getDesEspSearchPageInit(UserContext userContext) throws DemodaServiceException ;
	public DesEspSearchPage getDesEspSearchPageResult(UserContext userContext, DesEspSearchPage desespSearchPageVO) throws DemodaServiceException ;
	public DesEspAdapter getDesEspAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException ;
	public DesEspAdapter getDesEspAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException ;
	public DesEspAdapter getDesEspAdapterForCreate(UserContext userContext) throws DemodaServiceException ;
	public DesEspAdapter imprimirDesEsp(UserContext userContext, DesEspAdapter desEspAdapterVO) throws DemodaServiceException;
	public DesEspVO createDesEsp(UserContext userContext, DesEspVO desEsp) throws DemodaServiceException ;
	public DesEspVO updateDesEsp(UserContext userContext, DesEspVO desEsp) throws DemodaServiceException ;
	public DesEspVO deleteDesEsp(UserContext userContext, DesEspVO desEsp) throws DemodaServiceException ;
	public DesEspVO activarDesEsp(UserContext userContext, DesEspVO desEsp) throws DemodaServiceException ;
	public DesEspVO desactivarDesEsp(UserContext userContext, DesEspVO desEsp) throws DemodaServiceException ;
	public DesAtrValAdapter paramAtributoDesAtrVal(UserContext userContext, DesAtrValAdapter desAtrValAdapterVO, Long idAtributo) throws DemodaServiceException;
	
	// <--- ABM Descuentos Especiales
	
	// ---> ABM desRecClaDeu
	public DesRecClaDeuAdapter getDesRecClaDeuAdapterForCreate(UserContext userContext, CommonKey commonKeyIdDesEsp) throws DemodaServiceException;
	public DesRecClaDeuAdapter getDesRecClaDeuAdapterForUpdate(UserContext userContext, CommonKey commonKeyIdRecClaDeu) throws DemodaServiceException;
	public DesRecClaDeuAdapter getDesRecClaDeuAdapterForView(UserContext userContext, CommonKey commonKeyIdRecClaDeu) throws DemodaServiceException;

	public DesRecClaDeuVO createDesRecClaDeu(UserContext userContext, DesRecClaDeuVO DesRecClaDeuVO) throws DemodaServiceException ;
	public DesRecClaDeuVO updateDesRecClaDeu(UserContext userContext, DesRecClaDeuVO desRecClaDeuVO) throws DemodaServiceException ;
	public DesRecClaDeuVO deleteDesRecClaDeu(UserContext userContext, DesRecClaDeuVO desRecClaDeuVO) throws DemodaServiceException ;
	// <--- ABM desRecClaDeu
	
	// ---> ABM DesAtrVal
	public DesAtrValAdapter getDesAtrValAdapterForCreate(UserContext userContext, CommonKey commonKeyIdDesEsp) throws DemodaServiceException;
	public DesAtrValAdapter getDesAtrValAdapterForUpdate(UserContext userContext, CommonKey commonKeyIdAtrVal) throws DemodaServiceException;
	public DesAtrValAdapter getDesAtrValAdapterForView(UserContext userContext, CommonKey commonKeyIdAtrVal) throws DemodaServiceException;

	public DesAtrValVO createDesAtrVal(UserContext userContext, DesAtrValVO desAtrValVO) throws DemodaServiceException ;
	public DesAtrValVO updateDesAtrVal(UserContext userContext, DesAtrValVO desAtrValVO) throws DemodaServiceException ;
	public DesAtrValVO deleteDesAtrVal(UserContext userContext, DesAtrValVO desAtrValVO) throws DemodaServiceException ;
	public List<AtributoVO> getListAtributoDesAtrVal(UserContext userContext, DesAtrValAdapter desAtrValAdapter) throws DemodaServiceException ;
	// <--- ABM DesAtrVal
	
	// ---> ABM DesEspExe
	public DesEspExeAdapter getDesEspExeAdapterForCreate(UserContext userContext, CommonKey commonKeyIdDesEsp) throws DemodaServiceException;
	public DesEspExeAdapter getDesEspExeAdapterForUpdate(UserContext userContext, CommonKey commonKeyIdDesEspExe) throws DemodaServiceException;
	public DesEspExeAdapter getDesEspExeAdapterForView(UserContext userContext, CommonKey commonKeyIdDesEspExe) throws DemodaServiceException;

	public DesEspExeVO createDesEspExe(UserContext userContext, DesEspExeVO desEspExeVO) throws DemodaServiceException ;
	public DesEspExeVO updateDesEspExe(UserContext userContext, DesEspExeVO desEspExeVO) throws DemodaServiceException ;
	public DesEspExeVO deleteDesEspExe(UserContext userContext, DesEspExeVO desEspExeVO) throws DemodaServiceException ;
	// <--- ABM DesEspExe
	
	// ---> ABM Plan
	public PlanSearchPage getPlanSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public PlanSearchPage getPlanSearchPageResult(UserContext usercontext, PlanSearchPage planSearchPage) throws DemodaServiceException;

	public PlanAdapter getPlanAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanAdapter getPlanAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public PlanAdapter getPlanAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanAdapter getPlanAdapterParamFormulario(UserContext userContext, PlanAdapter planAdapterVO) throws DemodaServiceException;
		
	public PlanVO createPlan(UserContext userContext, PlanVO planVO ) throws DemodaServiceException;
	public PlanVO updatePlan(UserContext userContext, PlanVO planVO ) throws DemodaServiceException;
	public PlanVO deletePlan(UserContext userContext, PlanVO planVO ) throws DemodaServiceException;
	public PlanVO activarPlan(UserContext userContext, PlanVO planVO ) throws DemodaServiceException;
	public PlanVO desactivarPlan(UserContext userContext, PlanVO planVO ) throws DemodaServiceException;	
	// <--- ABM Plan
	
	
	// ---> ABM PlanClaDeu
	public PlanClaDeuAdapter getPlanClaDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanClaDeuAdapter getPlanClaDeuAdapterForCreate(UserContext userContext, CommonKey planKey) throws DemodaServiceException;
	public PlanClaDeuAdapter getPlanClaDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanClaDeuVO createPlanClaDeu(UserContext userContext, PlanClaDeuVO planClaDeuVO ) throws DemodaServiceException;
	public PlanClaDeuVO updatePlanClaDeu(UserContext userContext, PlanClaDeuVO planClaDeuVO ) throws DemodaServiceException;
	public PlanClaDeuVO deletePlanClaDeu(UserContext userContext, PlanClaDeuVO planClaDeuVO ) throws DemodaServiceException;
	// <--- ABM PlanClaDeu
	
	// ---> ABM PlanMotCad
	public PlanMotCadAdapter getPlanMotCadAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanMotCadAdapter getPlanMotCadAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanMotCadAdapter getPlanMotCadAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanMotCadVO createPlanMotCad(UserContext userContext, PlanMotCadVO planMotCadVO ) throws DemodaServiceException;
	public PlanMotCadVO updatePlanMotCad(UserContext userContext, PlanMotCadVO planMotCadVO ) throws DemodaServiceException;
	public PlanMotCadVO deletePlanMotCad(UserContext userContext, PlanMotCadVO planMotCadVO ) throws DemodaServiceException;
	// <--- ABM PlanMotCad
	
	
	// ---> ABM PlanForActDeu
	public PlanForActDeuAdapter getPlanForActDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanForActDeuAdapter getPlanForActDeuAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanForActDeuAdapter getPlanForActDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanForActDeuVO createPlanForActDeu(UserContext userContext, PlanForActDeuVO planForActDeuVO ) throws DemodaServiceException;
	public PlanForActDeuVO updatePlanForActDeu(UserContext userContext, PlanForActDeuVO planForActDeuVO ) throws DemodaServiceException;
	public PlanForActDeuVO deletePlanForActDeu(UserContext userContext, PlanForActDeuVO planForActDeuVO ) throws DemodaServiceException;
	// <--- ABM PlanForActDeu
	
	// ---> ABM PlanDescuento
	public PlanDescuentoAdapter getPlanDescuentoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanDescuentoAdapter getPlanDescuentoAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanDescuentoAdapter getPlanDescuentoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanDescuentoVO createPlanDescuento(UserContext userContext, PlanDescuentoVO planDescuentoVO ) throws DemodaServiceException;
	public PlanDescuentoVO updatePlanDescuento(UserContext userContext, PlanDescuentoVO planDescuentoVO ) throws DemodaServiceException;
	public PlanDescuentoVO deletePlanDescuento(UserContext userContext, PlanDescuentoVO planDescuentoVO ) throws DemodaServiceException;
	// <--- ABM PlanDescuento
	
	// ---> ABM PlanIntFin
	public PlanIntFinAdapter getPlanIntFinAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanIntFinAdapter getPlanIntFinAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanIntFinAdapter getPlanIntFinAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanIntFinVO createPlanIntFin(UserContext userContext, PlanIntFinVO planIntFinVO ) throws DemodaServiceException;
	public PlanIntFinVO updatePlanIntFin(UserContext userContext, PlanIntFinVO planIntFinVO ) throws DemodaServiceException;
	public PlanIntFinVO deletePlanIntFin(UserContext userContext, PlanIntFinVO planIntFinVO ) throws DemodaServiceException;
	// <--- ABM PlanIntFin

	// ---> ABM PlanVen
	public PlanVenAdapter getPlanVenAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanVenAdapter getPlanVenAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanVenAdapter getPlanVenAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanVenVO createPlanVen(UserContext userContext, PlanVenVO planVenVO ) throws DemodaServiceException;
	public PlanVenVO updatePlanVen(UserContext userContext, PlanVenVO planVenVO ) throws DemodaServiceException;
	public PlanVenVO deletePlanVen(UserContext userContext, PlanVenVO planVenVO ) throws DemodaServiceException;
	// <--- ABM PlanVen
	
	// ---> ABM PlanExe
	public PlanExeAdapter getPlanExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanExeAdapter getPlanExeAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanExeAdapter getPlanExeAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanExeVO createPlanExe(UserContext userContext, PlanExeVO planExeVO ) throws DemodaServiceException;
	public PlanExeVO updatePlanExe(UserContext userContext, PlanExeVO planExeVO ) throws DemodaServiceException;
	public PlanExeVO deletePlanExe(UserContext userContext, PlanExeVO planExeVO ) throws DemodaServiceException;
	// <--- ABM PlanExe
	
	// ---> ABM PlanProrroga
	public PlanProrrogaAdapter getPlanProrrogaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanProrrogaAdapter getPlanProrrogaAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanProrrogaAdapter getPlanProrrogaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanProrrogaVO createPlanProrroga(UserContext userContext, PlanProrrogaVO planProrrogaVO ) throws DemodaServiceException;
	public PlanProrrogaVO updatePlanProrroga(UserContext userContext, PlanProrrogaVO planProrrogaVO ) throws DemodaServiceException;
	public PlanProrrogaVO deletePlanProrroga(UserContext userContext, PlanProrrogaVO planProrrogaVO ) throws DemodaServiceException;
	// <--- ABM PlanProrroga
	
	
	// ---> ABM PlanAtrVal
	public PlanAtrValAdapter getPlanAtrValAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanAtrValAdapter getPlanAtrValAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanAtrValAdapter getPlanAtrValAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanAtrValAdapter getPlanAtrValAdapterParamAtributo(UserContext userContext, PlanAtrValAdapter planAtrValAdapterVO) throws DemodaServiceException;
	
	public PlanAtrValVO createPlanAtrVal(UserContext userContext, PlanAtrValVO planAtrValVO ) throws DemodaServiceException;
	public PlanAtrValVO updatePlanAtrVal(UserContext userContext, PlanAtrValVO planAtrValVO ) throws DemodaServiceException;
	public PlanAtrValVO deletePlanAtrVal(UserContext userContext, PlanAtrValVO planAtrValVO ) throws DemodaServiceException;
	// <--- ABM PlanAtrVal
	
	
	// ---> ABM PlanImpMin
	public PlanImpMinAdapter getPlanImpMinAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanImpMinAdapter getPlanImpMinAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanImpMinAdapter getPlanImpMinAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public PlanImpMinVO createPlanImpMin(UserContext userContext, PlanImpMinVO planImpMinVO ) throws DemodaServiceException;
	public PlanImpMinVO updatePlanImpMin(UserContext userContext, PlanImpMinVO planImpMinVO ) throws DemodaServiceException;
	public PlanImpMinVO deletePlanImpMin(UserContext userContext, PlanImpMinVO planImpMinVO ) throws DemodaServiceException;	
	// <--- ABM PlanImpMin
	
	// ---> ABM Procurador
	public ProcuradorSearchPage getProcuradorSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public ProcuradorSearchPage getProcuradorSearchPageResult(UserContext userContext, ProcuradorSearchPage procuradorSearchPage) throws DemodaServiceException;
	public ProcuradorAdapter getProcuradorAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProcuradorAdapter getProcuradorAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ProcuradorAdapter getProcuradorAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException;
	
	public ProcuradorVO createProcurador(UserContext userContext, ProcuradorVO procuradorVO) throws DemodaServiceException;
	public ProcuradorVO updateProcurador(UserContext userContext, ProcuradorVO procuradorVO) throws DemodaServiceException;
	public ProcuradorVO deleteProcurador(UserContext userContext, ProcuradorVO procuradorVO) throws DemodaServiceException;
	public ProcuradorVO activarProcurador(UserContext userContext, ProcuradorVO procuradorVO ) throws DemodaServiceException;
	public ProcuradorVO desactivarProcurador(UserContext userContext, ProcuradorVO procuradorVO ) throws DemodaServiceException;
	public ProcuradorAdapter imprimirProcurador(UserContext userContext, ProcuradorAdapter procuradorAdapterVO) throws DemodaServiceException;
		
	// <--- ABM Procurador
	
	//	---> ABM ProRec
	public ProRecAdapter getProRecAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProRecAdapter getProRecAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProRecAdapter getProRecAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ProRecVO createProRec(UserContext userContext, ProRecVO proRecVO) throws DemodaServiceException;
	public ProRecVO updateProRec(UserContext userContext, ProRecVO proRecVO) throws DemodaServiceException;
	public ProRecVO deleteProRec(UserContext userContext, ProRecVO proRecVO) throws DemodaServiceException;
	// <--- ABM ProRec
	
	// ---> ABM ProRecDesHas
	public ProRecDesHasAdapter getProRecDesHasAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProRecDesHasAdapter getProRecDesHasAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProRecDesHasAdapter getProRecDesHasAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ProRecDesHasVO createProRecDesHas(UserContext userContext, ProRecDesHasVO proRecDesHasVO) throws DemodaServiceException;
	public ProRecDesHasVO updateProRecDesHas(UserContext userContext, ProRecDesHasVO proRecDesHasVO) throws DemodaServiceException;
	public ProRecDesHasVO deleteProRecDesHas(UserContext userContext, ProRecDesHasVO proRecDesHasVO) throws DemodaServiceException;
	//	 <--- ABM ProRecDesHas

	// ---> ABM ProRecCom
	public ProRecComAdapter getProRecComAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProRecComAdapter getProRecComAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProRecComAdapter getProRecComAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ProRecComVO createProRecCom(UserContext userContext, ProRecComVO proRecComVO) throws DemodaServiceException;
	public ProRecComVO updateProRecCom(UserContext userContext, ProRecComVO proRecComVO) throws DemodaServiceException;
	public ProRecComVO deleteProRecCom(UserContext userContext, ProRecComVO proRecComVO) throws DemodaServiceException;
	//	 <--- ABM ProRecCom

	//	 ---> ABM Evento
	public EventoSearchPage getEventoSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public EventoSearchPage getEventoSearchPageResult(UserContext userContext, EventoSearchPage eventoSearchPage) throws DemodaServiceException;

	public EventoAdapter getEventoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public EventoAdapter getEventoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public EventoAdapter getEventoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public EventoVO createEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException;	
	public EventoVO updateEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException;
	public EventoVO deleteEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException;
	public EventoVO activarEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException;
	public EventoVO desactivarEvento(UserContext userContext, EventoVO eventoVO) throws DemodaServiceException;
	public EventoAdapter imprimirEvento(UserContext userContext, EventoAdapter eventoAdapterVO) throws DemodaServiceException;
	
	//  <--- ABM Evento
	
	public PlanRecursoAdapter getPlanRecursoAdapterForCreate(UserContext userContext,CommonKey planKey) throws DemodaServiceException;
	public PlanRecursoAdapter getPlanRecursoAdapterForUpdate(UserContext userContext, CommonKey planDescuentoKey) throws DemodaServiceException;
	public PlanRecursoAdapter getPlanRecursoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanRecursoAdapter createPlanRecurso(UserContext userContext, PlanRecursoAdapter planRecursoAdapter)throws DemodaServiceException;
	public PlanRecursoAdapter updatePlanRecurso (UserContext userContext, PlanRecursoAdapter planRecursoAdapter)throws DemodaServiceException;
	public PlanRecursoAdapter deletePlanRecurso (UserContext userContext, PlanRecursoAdapter planRecursoAdapter)throws DemodaServiceException;
	public PlanClaDeuAdapter getPlanClaDeuParam (UserContext userContext, PlanClaDeuAdapter planClaDeuAdapter)throws DemodaServiceException;
	
	public PlanExeAdapter getPlanExeParam (UserContext userContext, PlanExeAdapter planExeAdapter)throws DemodaServiceException;
	
	// ---> Mandatario
	public MandatarioAdapter getMandatarioAdapterForView(UserContext userContext,CommonKey planKey) throws DemodaServiceException;
	public MandatarioAdapter getMandatarioAdapterForUpdate(UserContext userContext,CommonKey planKey) throws DemodaServiceException;
	public MandatarioAdapter getMandatarioAdapterForCreate(UserContext userContext,CommonKey planKey) throws DemodaServiceException;

	public MandatarioVO createMandatario(UserContext userContext, MandatarioVO mandatarioVO) throws DemodaServiceException;
	public MandatarioVO updateMandatario(UserContext userContext, MandatarioVO mandatarioVO) throws DemodaServiceException;
	public MandatarioVO deleteMandatario(UserContext userContext, MandatarioVO mandatarioVO) throws DemodaServiceException;
	
	public MandatarioVO activarMandatario(UserContext userContext, MandatarioVO mandatarioVO) throws DemodaServiceException;
	public MandatarioVO desactivarMandatario(UserContext userContext, MandatarioVO mandatarioVO) throws DemodaServiceException;
	
	public MandatarioSearchPage getMandatarioSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public MandatarioSearchPage getMandatarioSearchPageResult(UserContext userContext, MandatarioSearchPage mandatarioSearchPage) throws DemodaServiceException;
	// <--- Mandatario
	

	// ---> PerCob
	public PerCobAdapter getPerCobAdapterForView(UserContext userContext,CommonKey planKey) throws DemodaServiceException;
	public PerCobAdapter getPerCobAdapterForUpdate(UserContext userContext,CommonKey planKey) throws DemodaServiceException;
	public PerCobAdapter getPerCobAdapterForCreate(UserContext userContext,CommonKey planKey) throws DemodaServiceException;

	public PerCobVO createPerCob(UserContext userContext, PerCobVO perCobVO) throws DemodaServiceException;
	public PerCobVO updatePerCob(UserContext userContext, PerCobVO perCobVO) throws DemodaServiceException;
	public PerCobVO deletePerCob(UserContext userContext, PerCobVO perCobVO) throws DemodaServiceException;
	
	public PerCobVO activarPerCob(UserContext userContext, PerCobVO perCobVO) throws DemodaServiceException;
	public PerCobVO desactivarPerCob(UserContext userContext, PerCobVO perCobVO) throws DemodaServiceException;
	
	public PerCobSearchPage getPerCobSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public PerCobSearchPage getPerCobSearchPageResult(UserContext userContext, PerCobSearchPage perCobSearchPage) throws DemodaServiceException;
	// <--- PerCob
	
	// ---> AgeRet
	public AgeRetAdapter getAgeRetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AgeRetAdapter getAgeRetAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public AgeRetAdapter getAgeRetAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException;
	
	public AgeRetVO createAgeRet(UserContext userContext, AgeRetVO ageRetVO) throws DemodaServiceException;
	public AgeRetVO updateAgeRet(UserContext userContext, AgeRetVO ageRetVO) throws DemodaServiceException;
	public AgeRetVO deleteAgeRet(UserContext userContext, AgeRetVO ageRetVO) throws DemodaServiceException;
	public AgeRetVO activarAgeRet(UserContext userContext, AgeRetVO ageRetVO ) throws DemodaServiceException;
	public AgeRetVO desactivarAgeRet(UserContext userContext, AgeRetVO ageRetVO ) throws DemodaServiceException;
	
	public AgeRetSearchPage getAgeRetSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public AgeRetSearchPage getAgeRetSearchPageResult(UserContext userContext, AgeRetSearchPage ageRetSearchPage) throws DemodaServiceException;
	// <--- AgeRet

	// ---> TipoMulta
	public TipoMultaAdapter getTipoMultaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoMultaAdapter getTipoMultaAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TipoMultaAdapter getTipoMultaAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException;
	public TipoMultaAdapter getTipoMultaAdapterParamRecurso(UserContext userContext, TipoMultaAdapter tipoMultaAdapter) throws DemodaServiceException;
	
	public TipoMultaVO createTipoMulta(UserContext userContext, TipoMultaVO ageRetVO) throws DemodaServiceException;
	public TipoMultaVO updateTipoMulta(UserContext userContext, TipoMultaVO ageRetVO) throws DemodaServiceException;
	public TipoMultaVO deleteTipoMulta(UserContext userContext, TipoMultaVO ageRetVO) throws DemodaServiceException;
	public TipoMultaVO activarTipoMulta(UserContext userContext, TipoMultaVO ageRetVO ) throws DemodaServiceException;
	public TipoMultaVO desactivarTipoMulta(UserContext userContext, TipoMultaVO ageRetVO ) throws DemodaServiceException;
	
	public TipoMultaSearchPage getTipoMultaSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public TipoMultaSearchPage getTipoMultaSearchPageResult(UserContext userContext, TipoMultaSearchPage ageRetSearchPage) throws DemodaServiceException;
	// <--- TipoMulta

	// ---> IndiceCompensacion
	public IndiceCompensacionAdapter getIndiceCompensacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public IndiceCompensacionAdapter getIndiceCompensacionAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public IndiceCompensacionAdapter getIndiceCompensacionAdapterForUpdate(UserContext userContext, CommonKey commonKeyPlan) throws DemodaServiceException;
	
	public IndiceCompensacionVO createIndiceCompensacion(UserContext userContext, IndiceCompensacionVO ageRetVO) throws DemodaServiceException;
	public IndiceCompensacionVO updateIndiceCompensacion(UserContext userContext, IndiceCompensacionVO ageRetVO) throws DemodaServiceException;
	public IndiceCompensacionVO deleteIndiceCompensacion(UserContext userContext, IndiceCompensacionVO ageRetVO) throws DemodaServiceException;
	public IndiceCompensacionVO activarIndiceCompensacion(UserContext userContext, IndiceCompensacionVO ageRetVO ) throws DemodaServiceException;
	public IndiceCompensacionVO desactivarIndiceCompensacion(UserContext userContext, IndiceCompensacionVO ageRetVO ) throws DemodaServiceException;
	
	public IndiceCompensacionSearchPage getIndiceCompensacionSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public IndiceCompensacionSearchPage getIndiceCompensacionSearchPageResult(UserContext userContext, IndiceCompensacionSearchPage ageRetSearchPage) throws DemodaServiceException;
	// <--- IndiceCompensacion

	// --> ABM TipoDefinicion
	public TipoPagoSearchPage getTipoPagoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public TipoPagoSearchPage getTipoPagoSearchPageResult(UserContext usercontext, TipoPagoSearchPage tipoPagoSearchPage) throws DemodaServiceException;

	public TipoPagoAdapter getTipoPagoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoPagoAdapter getTipoPagoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TipoPagoAdapter getTipoPagoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public TipoPagoVO createTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO ) throws DemodaServiceException;
	public TipoPagoVO updateTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO ) throws DemodaServiceException;
	public TipoPagoVO deleteTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO ) throws DemodaServiceException;
	public TipoPagoVO activarTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO ) throws DemodaServiceException;
	public TipoPagoVO desactivarTipoPago(UserContext userContext, TipoPagoVO tipoPagoVO ) throws DemodaServiceException;

	public TipoPagoAdapter imprimirTipoPago(UserContext userContext, TipoPagoAdapter tipoPagoVO ) throws DemodaServiceException;	
	
	// <--- ABM Tipo Definicion
	
	
	
}
