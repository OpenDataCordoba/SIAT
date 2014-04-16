//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.service;

import ar.gov.rosario.siat.cyq.iface.model.DeudaPrivilegioAdapter;
import ar.gov.rosario.siat.cyq.iface.model.DeudaPrivilegioVO;
import ar.gov.rosario.siat.cyq.iface.model.LiqDeudaCyqAdapter;
import ar.gov.rosario.siat.cyq.iface.model.PagoPrivAdapter;
import ar.gov.rosario.siat.cyq.iface.model.PagoPrivVO;
import ar.gov.rosario.siat.cyq.iface.model.ProCueNoDeuAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProCueNoDeuVO;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface ICyqConcursoyQuiebraService {
	
	// ---> ABM Procedimiento
	public ProcedimientoSearchPage getProcedimientoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ProcedimientoSearchPage getProcedimientoSearchPageResult(UserContext usercontext, ProcedimientoSearchPage procedimientoSearchPage) throws DemodaServiceException;
	public ProcedimientoSearchPage getProcedimientoSearchPageAvaResult(UserContext usercontext, ProcedimientoSearchPage procedimientoSearchPage) throws DemodaServiceException;
	public ProcedimientoSearchPage getProcedimientoSearchPageParamJuzgado(UserContext usercontext, ProcedimientoSearchPage procedimientoSearchPage) throws DemodaServiceException;
	public ProcedimientoSearchPage getProcedimientoSearchPageParamTitular(UserContext userContext, ProcedimientoSearchPage procedimientoSearchPage) throws DemodaServiceException;
	
	public ProcedimientoAdapter getProcedimientoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ProcedimientoVO createProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO ) throws DemodaServiceException;
	public ProcedimientoAdapter getProcedimientoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProcedimientoAdapter getProcedimientoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	@Deprecated
	public ProcedimientoAdapter getProcedimientoAdapterParamJuzgado(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException;
	public ProcedimientoAdapter getProcedimientoAdapterParamContribuyente(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException;
	public ProcedimientoAdapter getProcedimientoAdapterForBaja(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProcedimientoAdapter getProcedimientoAdapterForConversion(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProcedimientoAdapter getProcedimientoAdapterForInforme(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ProcedimientoVO updateProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO ) throws DemodaServiceException;
	public ProcedimientoVO deleteProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO ) throws DemodaServiceException;
	public ProcedimientoVO bajaProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO ) throws DemodaServiceException;
	public ProcedimientoVO conversionProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO ) throws DemodaServiceException;
	public ProcedimientoVO informarProcedimiento(UserContext userContext, ProcedimientoVO procedimientoVO ) throws DemodaServiceException;
	
	
	public LiqDeudaAdapter getLiqDeudaAdapterForCyqInit(UserContext userContext, CommonKey procedimientoKey) throws DemodaServiceException;

	public LiqDeudaAdapter validarCuentaEnvioCyQ(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	public LiqDeudaAdapter validarDeudaEnvioCyQ(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	public LiqDeudaAdapter getLiqDeudaAdapterForEnvioCyq(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	public LiqDeudaAdapter enviarDeudaCyq(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;

	public LiqDeudaAdapter agregarCuentaNoDeu(UserContext userContext, LiqDeudaAdapter liqDeudaAdapterVO) throws DemodaServiceException;
	
	public ProcedimientoAdapter quitarDeudaCyq(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException;
	
	public ProcedimientoAdapter getProcedimientoAdapterForCambioEstado(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ProcedimientoAdapter cambiarEstadoProcedimiento(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException;
	
	public PrintModel imprimirCaratula(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException;
	public PrintModel imprimirDeudaAdminCyq(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException;
	public PrintModel imprimirDeudaJudicialCyq(UserContext userContext, ProcedimientoAdapter procedimientoAdapterVO) throws DemodaServiceException;

	
	public LiqDeudaCyqAdapter getLiqDeudaCyqInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public DeudaPrivilegioAdapter getDeudaPrivilegioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DeudaPrivilegioAdapter getDeudaPrivilegioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DeudaPrivilegioAdapter getDeudaPrivilegioAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DeudaPrivilegioAdapter getDeudaPrivilegioAdapterParamRecurso(UserContext userContext, DeudaPrivilegioAdapter deudaPrivilegioAdapter) throws DemodaServiceException;
	
	public DeudaPrivilegioVO createDeudaPrivilegio(UserContext userContext, DeudaPrivilegioVO deudaPrivilegioVO) throws DemodaServiceException;
	public DeudaPrivilegioVO updateDeudaPrivilegio(UserContext userContext, DeudaPrivilegioVO deudaPrivilegioVO) throws DemodaServiceException;
	public DeudaPrivilegioVO deleteDeudaPrivilegio(UserContext userContext, DeudaPrivilegioVO deudaPrivilegioVO) throws DemodaServiceException;
	
	
	public PagoPrivAdapter getPagoPrivAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PagoPrivAdapter getPagoPrivAdapterForCreate(UserContext userContext, CommonKey commonKey, String[] listIdDeudaSelected) throws DemodaServiceException;
	public PagoPrivAdapter getPagoPrivAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PagoPrivAdapter getPagoPrivAdapterParamCancelaDeuda(UserContext userContext,  PagoPrivAdapter pagoPrivAdapter) throws DemodaServiceException;
	
	public PagoPrivAdapter createPagoPriv(UserContext userContext, PagoPrivAdapter pagoPrivAdapter) throws DemodaServiceException;
	public PagoPrivVO updatePagoPriv(UserContext userContext, PagoPrivVO pagoPrivVO) throws DemodaServiceException;
	public PagoPrivVO deletePagoPriv(UserContext userContext, PagoPrivVO pagoPrivVO) throws DemodaServiceException;	
	public PagoPrivVO generarOIT(UserContext userContext, PagoPrivVO pagoPrivVO) throws DemodaServiceException;
	
	public PagoPrivAdapter getPagoPrivAdapterParamCuentaBanco(UserContext userContext, PagoPrivAdapter pagoPrivAdapterVO) throws DemodaServiceException;
	public PrintModel imprimirRecibo(UserContext userContext, PagoPrivAdapter pagoPrivAdapter) throws Exception;
	
	public LiqFormConvenioAdapter getLiqFormConvenioInit(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws DemodaServiceException;
	public LiqFormConvenioAdapter getPlanes(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	public LiqFormConvenioAdapter getPlanesEsp (UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	public LiqFormConvenioAdapter getAlternativaCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	public LiqFormConvenioAdapter getSimulacionCuotas(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	public LiqFormConvenioAdapter getSimulacionCuotasEsp(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	
	public LiqFormConvenioAdapter validarCuotasEsp(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	
	public LiqFormConvenioAdapter getFormalizarPlanInit(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception ;
	public LiqFormConvenioAdapter formalizarPlan(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	public LiqFormConvenioAdapter paramPersona(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO, Long selectedId) throws Exception;
	public LiqFormConvenioAdapter getConvenioFormalizado(UserContext userContext, LiqFormConvenioAdapter liqFormConvenioAdapterVO) throws Exception;
	
	// <--- ABM Procedimiento

	
	public ProCueNoDeuAdapter getProCueNoDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ProCueNoDeuVO updateProCueNoDeu(UserContext userContext, ProCueNoDeuVO proCueNoDeuVO) throws DemodaServiceException;
	public ProCueNoDeuVO deleteProCueNoDeu(UserContext userContext, ProCueNoDeuVO proCueNoDeuVO) throws DemodaServiceException;
}
