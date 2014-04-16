//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.service;

import ar.gov.rosario.siat.gde.iface.model.CambioPlanCDMAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanAdapter;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraVO;
import ar.gov.rosario.siat.rec.iface.model.ContratoAdapter;
import ar.gov.rosario.siat.rec.iface.model.ContratoSearchPage;
import ar.gov.rosario.siat.rec.iface.model.ContratoVO;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoAdapter;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoSearchPage;
import ar.gov.rosario.siat.rec.iface.model.FormaPagoVO;
import ar.gov.rosario.siat.rec.iface.model.ObrRepVenAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObrRepVenVO;
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraFormaPagoAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraFormaPagoVO;
import ar.gov.rosario.siat.rec.iface.model.ObraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetSearchPage;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetVO;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraVO;
import ar.gov.rosario.siat.rec.iface.model.ProcesoAnulacionObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.TipoObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.TipoObraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.TipoObraVO;
import ar.gov.rosario.siat.rec.iface.model.UsoCdMAdapter;
import ar.gov.rosario.siat.rec.iface.model.UsoCdMSearchPage;
import ar.gov.rosario.siat.rec.iface.model.UsoCdMVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IRecCdmService {
	
	// ---> ABM TipoObra
	public TipoObraSearchPage getTipoObraSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public TipoObraSearchPage getTipoObraSearchPageResult(UserContext usercontext, TipoObraSearchPage tipoObraSearchPage) throws DemodaServiceException;

	public TipoObraAdapter getTipoObraAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoObraAdapter getTipoObraAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TipoObraAdapter getTipoObraAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
		
	public TipoObraVO createTipoObra(UserContext userContext, TipoObraVO tipoObraVO ) throws DemodaServiceException;
	public TipoObraVO updateTipoObra(UserContext userContext, TipoObraVO tipoObraVO ) throws DemodaServiceException;
	public TipoObraVO deleteTipoObra(UserContext userContext, TipoObraVO tipoObraVO ) throws DemodaServiceException;
	public TipoObraVO activarTipoObra(UserContext userContext, TipoObraVO tipoObraVO ) throws DemodaServiceException;
	public TipoObraVO desactivarTipoObra(UserContext userContext, TipoObraVO tipoObraVO ) throws DemodaServiceException;
	public TipoObraAdapter imprimirTipoObra(UserContext userContext, TipoObraAdapter tipoObraAdapter) throws DemodaServiceException;
	// <--- ABM TipoObra
	
	// ---> ABM FormaPago
	public FormaPagoSearchPage getFormaPagoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public FormaPagoSearchPage getFormaPagoSearchPageResult(UserContext usercontext, FormaPagoSearchPage formaPagoSearchPage) throws DemodaServiceException;

	public FormaPagoAdapter getFormaPagoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FormaPagoAdapter getFormaPagoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public FormaPagoAdapter getFormaPagoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FormaPagoAdapter getFormaPagoAdapterParamRecurso(UserContext userContext,FormaPagoAdapter formaPagoAdapter) throws DemodaServiceException;
	public FormaPagoAdapter getFormaPagoAdapterParamEsEspecial(UserContext userContext, FormaPagoAdapter formaPagoAdapter) throws DemodaServiceException;
	
	public FormaPagoVO createFormaPago(UserContext userContext, FormaPagoVO formaPagoVO ) throws DemodaServiceException;
	public FormaPagoVO updateFormaPago(UserContext userContext, FormaPagoVO formaPagoVO ) throws DemodaServiceException;
	public FormaPagoVO deleteFormaPago(UserContext userContext, FormaPagoVO formaPagoVO ) throws DemodaServiceException;
	public FormaPagoVO activarFormaPago(UserContext userContext, FormaPagoVO formaPagoVO) throws DemodaServiceException;
	public FormaPagoVO desactivarFormaPago(UserContext userContext, FormaPagoVO formaPagoVO) throws DemodaServiceException;	
	// <--- ABM FormaPago
	
	// ---> ABM Contrato
	public ContratoSearchPage getContratoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ContratoSearchPage getContratoSearchPageResult(UserContext usercontext, ContratoSearchPage contratoSearchPage) throws DemodaServiceException;

	public ContratoAdapter getContratoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ContratoAdapter getContratoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ContratoAdapter getContratoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ContratoVO createContrato(UserContext userContext, ContratoVO contratoVO) throws DemodaServiceException;
	public ContratoVO updateContrato(UserContext userContext, ContratoVO contratoVO) throws DemodaServiceException;
	public ContratoVO deleteContrato(UserContext userContext, ContratoVO contratoVO) throws DemodaServiceException;
	public ContratoVO activarContrato(UserContext userContext, ContratoVO contratoVO) throws DemodaServiceException;
	public ContratoVO desactivarContrato(UserContext userContext, ContratoVO contratoVO) throws DemodaServiceException;	
	// <--- ABM Contrato

	// ---> ABM PlanillaCuadra
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageInit (UserContext userContext, PlanillaCuadraSearchPage planillaCuadraSearchPageParam) throws DemodaServiceException;
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageParamCalle(UserContext userContext, PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException;
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageParamRecurso(UserContext userContext, PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException;
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageParamContrato(UserContext userContext, PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException;
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageParamTipoObra(UserContext userContext, PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException;
	public PlanillaCuadraSearchPage getPlanillaCuadraSearchPageResult(UserContext usercontext, PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException;

	public PlanillaCuadraAdapter getPlanillaCuadraAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterForUpdate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterForCambiarEstado (UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterParamCalle(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapterVO) throws DemodaServiceException;
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterParamRecurso (UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapter) throws DemodaServiceException;
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterParamContrato(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapter) throws DemodaServiceException;
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterParamTipoObra(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapter) throws DemodaServiceException;
	public PlanillaCuadraAdapter getPlanillaCuadraAdapterLimpiarCalles(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapterVO) throws DemodaServiceException;
	
	public PlanillaCuadraVO createPlanillaCuadra(UserContext userContext, PlanillaCuadraVO planillaCuadraVO) throws DemodaServiceException;
	public PlanillaCuadraVO updatePlanillaCuadra(UserContext userContext, PlanillaCuadraVO planillaCuadraVO) throws DemodaServiceException;
	public PlanillaCuadraVO deletePlanillaCuadra(UserContext userContext, PlanillaCuadraVO planillaCuadraVO) throws DemodaServiceException;
	public PlanillaCuadraVO cambiarEstadoPlanillaCuadra(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapter ) throws DemodaServiceException;	
	public PlanillaCuadraAdapter imprimirPlanillaCuadra(UserContext userContext, PlanillaCuadraAdapter planillaCuadraAdapterVO) throws DemodaServiceException;	
	// <--- ABM PlanillaCuadra

	// ---> ABM PlaCuaDet
	public PlaCuaDetSearchPage getPlaCuaDetSearchPageInit(UserContext userContext,PlaCuaDetSearchPage plaCuaDetSearchPage) throws DemodaServiceException;	
	public PlaCuaDetSearchPage getPlaCuaDetSearchPageResult(UserContext userContext,PlaCuaDetSearchPage plaCuaDetSearchPage) throws DemodaServiceException;	

	public PlaCuaDetAdapter getPlaCuaDetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlaCuaDetAdapter getPlaCuaDetAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public PlaCuaDetSearchPage createPlaCuaDet(UserContext userContext,PlaCuaDetSearchPage plaCuaDetSearchPage) throws DemodaServiceException;
	public PlaCuaDetVO updatePlaCuaDet(UserContext userContext, PlaCuaDetVO plaCuaDetVO ) throws DemodaServiceException;
	public PlaCuaDetVO deletePlaCuaDet(UserContext userContext, PlaCuaDetVO plaCuaDetVO ) throws DemodaServiceException;
	public PlaCuaDetAdapter imprimirPlaCuaDet(UserContext userContext, PlaCuaDetAdapter plaCuaDetAdapterVO) throws DemodaServiceException;	
	// <--- ABM PlaCuaDet
	
	//	 ---> ABM Obra
	public ObraSearchPage getObraSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ObraSearchPage getObraSearchPageResult(UserContext usercontext, ObraSearchPage obraSearchPage) throws DemodaServiceException;

	public ObraAdapter getObraAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public ObraAdapter getObraAdapterForCambiarEstado(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;	
	public ObraAdapter getObraAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ObraAdapter getObraAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ObraAdapter getObraAdapterParamValuacion(UserContext userContext, ObraAdapter obraAdapter) throws DemodaServiceException;
	public ObraAdapter getObraAdapterParamEsCostoEsp(UserContext userContext, ObraAdapter obraAdapter) throws DemodaServiceException;
	public ObraAdapter imprimirInfObrRep(UserContext userContext, ObraAdapter obraAdapter) throws DemodaServiceException;
	public ObraAdapter imprimirReporte(UserContext userContext, ObraAdapter obraAdapter) throws DemodaServiceException;
	public PrintModel getPrintModelForEmitirInforme(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ObraVO createObra(UserContext userContext, ObraVO obraVO ) throws DemodaServiceException;
	public ObraVO updateObra(UserContext userContext, ObraVO obraVO ) throws DemodaServiceException;
	public ObraVO deleteObra(UserContext userContext, ObraVO obraVO ) throws DemodaServiceException;
	public ObraVO cambiarEstadoObra(UserContext userContext, ObraAdapter obraAdapter) throws DemodaServiceException;
	// <--- ABM Obra
	
	// ---> ObraFormaPago
	public ObraFormaPagoAdapter getObraFormaPagoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ObraFormaPagoAdapter getObraFormaPagoAdapterForCreate(UserContext userContext, ObraFormaPagoAdapter obraFormaPagoAdapter) throws DemodaServiceException;
	public ObraFormaPagoAdapter getObraFormaPagoAdapterParamEsEspecial(UserContext userContext, ObraFormaPagoAdapter obraFormaPagoAdapter) throws DemodaServiceException;
	public ObraFormaPagoVO activarObraFormaPago(UserContext userContext,ObraFormaPagoVO obraFormaPagoVO) throws DemodaServiceException;
	public ObraFormaPagoVO desactivarObraFormaPago(UserContext userContext,ObraFormaPagoVO obraFormaPagoVO) throws DemodaServiceException;

	public ObraFormaPagoAdapter getObraFormaPagoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ObraFormaPagoVO createObraFormaPago(UserContext userContext, ObraFormaPagoVO obraFormaPagoVO ) throws DemodaServiceException;
	public ObraFormaPagoVO updateObraFormaPago(UserContext userContext, ObraFormaPagoVO obraFormaPagoVO ) throws DemodaServiceException;
	public ObraFormaPagoVO deleteObraFormaPago(UserContext userContext, ObraFormaPagoVO obraFormaPagoVO ) throws DemodaServiceException;
	// <--- ABM ObraFormaPago

	// ---> ABM ObraPlanillaCuadra
	public PlanillaCuadraAdapter getObraPlanillaCuadraAdapterForView
		(UserContext userContext, CommonKey planillaCuadraKey) throws DemodaServiceException;

	public PlanillaCuadraSearchPage createObraPlanillaCuadra(UserContext userContext, 
		PlanillaCuadraSearchPage planillaCuadraSearchPage ) throws DemodaServiceException;
	public PlanillaCuadraVO deleteObraPlanillaCuadra
		(UserContext userContext, PlanillaCuadraVO planillaCuadraVO) throws DemodaServiceException;
	// <--- ABM ObraPlanillaCuadra
	
	// ---> Asignar repartidores a planillas de la obra.
	public PlanillaCuadraSearchPage getPlanillaCuadraForAsignarRepartidoresInit
		(UserContext userContext, CommonKey obraKey) throws DemodaServiceException;
	
	public PlanillaCuadraSearchPage getPlanillaCuadraForAsignarRepartidoresResult
		(UserContext userContext, PlanillaCuadraSearchPage planillaCuadraSearchPage) throws DemodaServiceException;
	
	public PlanillaCuadraSearchPage asignarDesasignarRepartidor(UserContext userContext, 
		PlanillaCuadraSearchPage planillaCuadraSearchPage, CommonKey repartidorKey)throws DemodaServiceException;	
	// <--- Asignar repartidores a planillas de la obra.
	
	// ---> Cambio de Plan
	public CambioPlanCDMAdapter validarDeudaCDMNoVencida(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException;
	public CambioPlanCDMAdapter validarDeudaCDMVencida(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException;
	public CambioPlanCDMAdapter getCambioPlanCDMAdapterInit(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException;
	public CambioPlanCDMAdapter cambiarPlanCDM(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException;	
	public CambioPlanCDMAdapter getUltimoCambioPlan(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException;
	public PrintModel getPrintRecibo(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException;
	public PrintModel getPrintForm(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException;	
	public CambioPlanCDMAdapter getCuotaSaldoCDMAdapterInit(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException;
	public CambioPlanCDMAdapter generarCuotaSaldoCDM(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException;
	public CambioPlanCDMAdapter getUltimaCuotaSaldo(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException;
	public PrintModel getPrintReciboCuotaSaldo(UserContext userContext, CambioPlanCDMAdapter cambioPlanCDMAdapterVO) throws DemodaServiceException;
	public PlanAdapter imprimirPlan(UserContext userContext, PlanAdapter planAdapter) throws DemodaServiceException;
	// <--- Cambio de Plan
	
	// ---> ABM Obra ObrRepVen 
	public ObrRepVenAdapter getObrRepVenAdapterForCreate
		(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ObrRepVenVO createObrRepVen
		(UserContext userContext, ObrRepVenVO obrRepVenVO) throws DemodaServiceException;
	// <--- ABM Obra ObrRepVen	

	// ---> ABM Uso CdM
	public UsoCdMSearchPage getUsoCdMSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public UsoCdMSearchPage getUsoCdMSearchPageResult(UserContext userContext, UsoCdMSearchPage usoCdMSearchPage) throws DemodaServiceException;
	
	public UsoCdMAdapter getUsoCdMAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public UsoCdMAdapter getUsoCdMAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public UsoCdMAdapter getUsoCdMAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public UsoCdMVO createUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException;
	public UsoCdMVO updateUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException;
	public UsoCdMVO deleteUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException;
	public UsoCdMVO activarUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException;
	public UsoCdMVO desactivarUsoCdM(UserContext userContext, UsoCdMVO usoCdMVO) throws DemodaServiceException;
	// <--- ABM Uso CdM

	// ---> ABM Anulacion Obra
	public AnulacionObraSearchPage getAnulacionObraSearchPageInit(UserContext userContext) 
		throws DemodaServiceException;
	public AnulacionObraSearchPage getAnulacionObraSearchPageResult(UserContext userContext, 
		AnulacionObraSearchPage anulacionObraSearchPage) throws DemodaServiceException;

	public AnulacionObraAdapter getAnulacionObraAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public AnulacionObraAdapter getAnulacionObraAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException;
	public AnulacionObraAdapter getAnulacionObraAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException;
	public AnulacionObraAdapter getAnulacionObraAdapterParamObra(UserContext userContext, AnulacionObraAdapter 
			anulacionObraAdapter) throws DemodaServiceException;
	public AnulacionObraAdapter getAnulacionObraAdapterParamPlanillaCuadra(UserContext userContext, AnulacionObraAdapter 
			anulacionObraAdapter) throws DemodaServiceException;

	public AnulacionObraVO createAnulacionObra(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException;
	public AnulacionObraVO updateAnulacionObra(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException;
	public AnulacionObraVO deleteAnulacionObra(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException;

	public ProcesoAnulacionObraAdapter getProcesoAnulacionObraAdapterInit(UserContext userContext, 
			CommonKey commonKey) throws DemodaServiceException;
	public AnulacionObraVO activar(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException;
	public AnulacionObraVO reprogramar(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException;
	public AnulacionObraVO cancelar(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException;
	public AnulacionObraVO reiniciar(UserContext userContext, AnulacionObraVO anulacionObraVO) throws DemodaServiceException;
	// <--- ABM Anulacion Obra
}
