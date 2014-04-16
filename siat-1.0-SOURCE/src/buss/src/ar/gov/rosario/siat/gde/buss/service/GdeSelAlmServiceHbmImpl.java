//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

/**
 * Implementacion de servicios de submodulo GDeudaJudicial del modulo gde
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.gde.buss.bean.SelAlm;
import ar.gov.rosario.siat.gde.buss.bean.TipProMas;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.SelAlmAgregarParametrosSearchPage;
import ar.gov.rosario.siat.gde.iface.model.SelAlmVO;
import ar.gov.rosario.siat.gde.iface.model.TipoSelAlmVO;
import ar.gov.rosario.siat.gde.iface.service.IGdeSelAlmService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeSelAlmServiceHbmImpl implements IGdeSelAlmService { 
	
	private Logger log = Logger.getLogger(GdeSelAlmServiceHbmImpl.class);

	public SelAlmAgregarParametrosSearchPage getSelAlmDeudaAgregarSearchPageInit(UserContext userContext, CommonKey recursoKey, CommonKey corridaKey, CommonKey selAlmKey, CommonKey tipoProcMasKey, CommonKey viaDeudaKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName(); 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPage = new SelAlmAgregarParametrosSearchPage();

			// aplica en forma predeterminada al total de la deuda
			selAlmAgregarParametrosSearchPage.setAplicaAlTotalDeuda(SiNo.SI);
			
			// la selAlm y la corrida seram utilizadas en metodos posteriores.
			selAlmAgregarParametrosSearchPage.setSelAlm((SelAlmVO) SelAlm.getById(selAlmKey.getId()).toVO(0));
			Corrida corrida = Corrida.getById(corridaKey.getId());
			selAlmAgregarParametrosSearchPage.setCorrida((CorridaVO) corrida.toVO(0));

			TipProMas tipProMas = TipProMas.getById(tipoProcMasKey.getId());
			ViaDeuda viaDeuda = ViaDeuda.getById(viaDeudaKey.getId());
			
			//Seteo si es un envio Judicial
			selAlmAgregarParametrosSearchPage.setEsEnvioJudicial(tipProMas.getEsEnvioJudicial());

			// obtencion, toVO y carga de la lista de clasificacion de la deuda del recurso del Envio Judicial
			Recurso recurso = Recurso.getById(recursoKey.getId());
			RecursoVO recursoVO = (RecursoVO) recurso.toVO(0);

			//	lista clasificacion de la deuda cargada dentro del recurso del envio judicial
			recursoVO.setListRecClaDeu((ArrayList<RecClaDeuVO>)
					ListUtilBean.toVO(RecClaDeu.getListByIdRecurso(recurso.getId()),1));
			
			selAlmAgregarParametrosSearchPage.setRecurso(recursoVO);
			
			// seteo cantidad minima por defecto
			selAlmAgregarParametrosSearchPage.setCantidadMinimaDeuda(2L);

			// para cdm obras con estado distinta de creada
			if (recurso.getEsCategoriaCdM() ){
				selAlmAgregarParametrosSearchPage.setListObra(
						(ArrayList<ObraVO>) ListUtilBean.toVO(
								Obra.getListEstadoNoCreada(),0 ,new ObraVO(-1, StringUtil.SELECT_OPCION_TODOS))	);
			}
			
			// carga la lista de exenciones que permiten envio a judiciales
			/* fedel 2009-01-27: quitamos la eleccion de exenciones, 
			 * ahora se excluyen siempre todas las deuda de cualquier cuenta que 
			 * tenga una exencion con enviaJudicial en 0
			 *
			selAlmAgregarParametrosSearchPage.setListExencion(
					(ArrayList<ExencionVO>) ListUtilBean.toVO(
							Exencion.getListPermitenEnvioJudicial(),0));
			 */
			
			// fecha hasta = 31/12/(anio actual - 2)
			int anioActual = DateUtil.getAnioActual();
			// Esto vale para TGI
			if (recurso.equals(Recurso.getTGI()))
				selAlmAgregarParametrosSearchPage.setFechaVencimientoHasta(
					SelAlmAgregarParametrosSearchPage.getFechaVencimientoLimite()); // recordar que el mes arranca en cero
			// fecha desde = 01/01/(anio actual - 2)
			if (recurso.equals(Recurso.getTGI()))
				selAlmAgregarParametrosSearchPage.setFechaVencimientoDesde(DateUtil.getDate(anioActual - 2, 0, 1));  // 0 significa mes 1
			
			// carga parametros del contribuyente
			selAlmAgregarParametrosSearchPage.setListAtributo(
					(ArrayList<AtributoVO>) ListUtilBean.toVO(
							Atributo.getListAtributoDeContribuyenteForBusquedaMasiva(),0));
			
			// carga parametros del objeto imponible
			TipObjImp tipObjImp = recurso.getTipObjImp();
			if (tipObjImp != null) { 
				selAlmAgregarParametrosSearchPage.setTipObjImpDefinition( tipObjImp.getDefinitionForBusqueda(TipObjImp.FOR_BUS_MASIVA));
				// tipo de objeto imponible del recurso necesario en la busq con param obj imp
				selAlmAgregarParametrosSearchPage.getRecurso().setTipObjImp((TipObjImpVO)tipObjImp.toVO(0));
			}
			// cargo la lista de tipoSelAlmDet de acuerdo a la via de la deuda para el tipo de proceso masivo
			selAlmAgregarParametrosSearchPage.setListTipoSelAlmDet(
					(ArrayList<TipoSelAlmVO>)ListUtilBean.toVO(tipProMas.getListTipoSelAlmDetForProcesoMasivo(viaDeuda)));
			
			// visualiza u oculta los parametros de tipo de detalle de seleccion almacenada
			boolean verParametrosTipoSelAlmDet = (tipProMas.getEsSeleccionDeuda() || tipProMas.getEsPreEnvioJudicial());
			
			selAlmAgregarParametrosSearchPage.setVerParametrosTipoSelAlmDetBussEnabled(verParametrosTipoSelAlmDet);
			
			// seteo el tipoSelAlmDet que corresponde a la deuda administrativa o judicial segun la via
			if (viaDeuda.getEsViaAdmin()){
				selAlmAgregarParametrosSearchPage.setTipoSelAlmDet((TipoSelAlmVO) TipoSelAlm.getTipoSelAlmDetDeudaAdm().toVO(0));
			}else if (viaDeuda.getEsViaJudicial()){
				selAlmAgregarParametrosSearchPage.setTipoSelAlmDet((TipoSelAlmVO) TipoSelAlm.getTipoSelAlmDetDeudaJud().toVO(0));
			} 

			// visualizo los parametros de deuda y oculto los parametros de convenio
			selAlmAgregarParametrosSearchPage.setVerParametrosDeudaBussEnabled(Boolean.TRUE);
			selAlmAgregarParametrosSearchPage.setVerParametrosConvenioBussEnabled(Boolean.FALSE);
			
			// se decidio no implementar parametros del proceso
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return selAlmAgregarParametrosSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SelAlmAgregarParametrosSearchPage getSelAlmDeudaAgregarSearchPageParam(UserContext userContext, SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// visualizacion de los parametros de deuda y de los parametros de convenioCuota
			// tipoSelAlmDet siempre esta seleccionado
			TipoSelAlm tipoSelAlmDet = TipoSelAlm.getById(selAlmAgregarParametrosSearchPage.getTipoSelAlmDet().getId());
			
			Boolean verParametrosDeudaBussEnabled = tipoSelAlmDet.getEsTipoSelAlmDetDeuda(); 
			selAlmAgregarParametrosSearchPage.setVerParametrosDeudaBussEnabled(verParametrosDeudaBussEnabled);
			selAlmAgregarParametrosSearchPage.setVerParametrosConvenioBussEnabled(!verParametrosDeudaBussEnabled);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return selAlmAgregarParametrosSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SelAlmAgregarParametrosSearchPage cargarParametrosSelAlmDeuda(UserContext userContext, SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			selAlmAgregarParametrosSearchPage.clearError();
			selAlmAgregarParametrosSearchPage.setListResult(new ArrayList());
			
			// fechaVencimientoLimite = 31 /12 del anioActual - 2 (no se utiliza mas ya que era valido solo para tgi)
			//Date fechaVencimientoLimite =SelAlmAgregarParametrosSearchPage.getFechaVencimientoLimite();
			
			Long idTipoSelAlmDet = selAlmAgregarParametrosSearchPage.getTipoSelAlmDet().getId();
			TipoSelAlm tipoSelAlmDet = TipoSelAlm.getById(idTipoSelAlmDet);
			
			if (tipoSelAlmDet.getEsTipoSelAlmDetDeuda()){
				// Validacion: fecha vencimiento desde no sea mayor a fecha vencimiento hasta (pueden ser nulas)
				if ( selAlmAgregarParametrosSearchPage.getFechaVencimientoDesde() != null && 
						selAlmAgregarParametrosSearchPage.getFechaVencimientoHasta() != null &&
						DateUtil.isDateAfter(selAlmAgregarParametrosSearchPage.getFechaVencimientoDesde(), selAlmAgregarParametrosSearchPage.getFechaVencimientoHasta())){
					selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
							GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTODESDE, GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOHASTA);
					return selAlmAgregarParametrosSearchPage;
				}
				
				// Validacion: fecha vencimiento desde no sea mayor a fecha vencimiento limite (puede ser nula la fecha de vencimiento desde)
				// se agrega validacion que solo se verifique cuando es envio judicial
				// Se saca esta validacion ya que solo era valida para tgi
				/*
				if(selAlmAgregarParametrosSearchPage.getFechaVencimientoDesde() != null && selAlmAgregarParametrosSearchPage.getEsEnvioJudicial()){
					if (DateUtil.isDateAfter(selAlmAgregarParametrosSearchPage.getFechaVencimientoDesde(), fechaVencimientoLimite)){
						selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
							GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTODESDE, GdeError.DEUDA_EXC_PRO_MAS_SEARCHPAGE_FECHAVTOLIMITE);
						return selAlmAgregarParametrosSearchPage;
					}
				}
				*/

				// si es envio a judicial, la fecha hasta es requerida 
				if(selAlmAgregarParametrosSearchPage.getFechaVencimientoHasta() == null && 
						selAlmAgregarParametrosSearchPage.getEsEnvioJudicial()){
					selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
							GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOHASTA);
					return selAlmAgregarParametrosSearchPage;
				}

				// Validacion: fecha vencimiento hasta no sea mayor a fecha vencimiento limite (puede ser nula la fecha de vencimiento hasta)
				// se agrega validacion que solo se verifique cuando es envio judicial
				//Se quita esta validacion ya que es solo valida para tgi
				/*if(selAlmAgregarParametrosSearchPage.getFechaVencimientoHasta() != null && selAlmAgregarParametrosSearchPage.getEsEnvioJudicial()){
					if (DateUtil.isDateAfter(selAlmAgregarParametrosSearchPage.getFechaVencimientoHasta(), fechaVencimientoLimite)){
						selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
							GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOHASTA, GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOLIMITE);
						return selAlmAgregarParametrosSearchPage;
					}
				}
				*/
				
				// Validacion: importe historico hasta no sea mayor a importe historico desde (pueden ser nulas)
				if ( selAlmAgregarParametrosSearchPage.getImporteHistoricoDesde() != null && 
						selAlmAgregarParametrosSearchPage.getImporteHistoricoHasta() != null &&
						selAlmAgregarParametrosSearchPage.getImporteHistoricoDesde() > selAlmAgregarParametrosSearchPage.getImporteHistoricoHasta()){
					selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_IMPORTEHISTDESDE, GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_IMPORTEHISTHASTA);
					return selAlmAgregarParametrosSearchPage;
				}
				
				// Validacion: importe actualizado hasta no sea mayor a importe actualizado desde (pueden ser nulas)
				if ( selAlmAgregarParametrosSearchPage.getImporteActualizadoDesde() != null && 
						selAlmAgregarParametrosSearchPage.getImporteActualizadoHasta() != null &&
						selAlmAgregarParametrosSearchPage.getImporteActualizadoDesde() > selAlmAgregarParametrosSearchPage.getImporteActualizadoHasta()){
					selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_IMPORTEACTDESDE, GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_IMPORTEACTHASTA);
					return selAlmAgregarParametrosSearchPage;
				}
				
				// Validacion: aplica al total de la deuda requerido
				if (!SiNo.getEsValido(selAlmAgregarParametrosSearchPage.getAplicaAlTotalDeuda().getId()) ){
					selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_APLIC_TOTAL_DEU);
					return selAlmAgregarParametrosSearchPage;
				}
				
				// Validacion: cantidad minima de deuda requerido
				if (selAlmAgregarParametrosSearchPage.getCantidadMinimaDeuda() == null  ){
					selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_MIN_DEUDA);
					return selAlmAgregarParametrosSearchPage;
				}else{
					if(selAlmAgregarParametrosSearchPage.getCantidadMinimaDeuda() < 1){
						selAlmAgregarParametrosSearchPage.addRecoverableError(
								GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_MIN_DEUDA_MENOR);
					}
				}
			}else if (tipoSelAlmDet.getEsTipoSelAlmDetConvCuot()){
				
				// Validacion: cantidad minima de cuotas requerido
				if (selAlmAgregarParametrosSearchPage.getCantidadCuotasPlan() == null  ){
					selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_MIN_CUOTA);
					return selAlmAgregarParametrosSearchPage;
				}else{
					if(selAlmAgregarParametrosSearchPage.getCantidadCuotasPlan() < 1){
						selAlmAgregarParametrosSearchPage.addRecoverableError(
								GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_MIN_CUOTA_MENOR);
					}
				}
				// Validacion: fechaVencimiento requerido				
				if(selAlmAgregarParametrosSearchPage.getFechaVencimiento() == null ){
					selAlmAgregarParametrosSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
							GdeError.SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVENCIMIENTO);
				}

			}
			
			/* por restriccion en implementacion, 
			 * solo permitimos filtrar un unico atributo de contribuyente. 
			 * Esta validacion la agrego yo porque de lo contrario funcionaria 
			 * incorrectamente la implementacion de los query en deudaadmindao.java
			 * fedel (2009-09-19)
			 */
			if (selAlmAgregarParametrosSearchPage.getListAtributoSI().size() 
					+ selAlmAgregarParametrosSearchPage.getListAtributoNO().size() > 1) {
				selAlmAgregarParametrosSearchPage.addRecoverableValueError("Solo se permite aplicar un filtro por atributo de contribuyente.");				
			}
			
			
			String detalle = this.getDetalleLog(selAlmAgregarParametrosSearchPage);
			
			if (StringUtil.isNullOrEmpty(detalle)){
				log.error("detalle de filtros,  no cargado");
				selAlmAgregarParametrosSearchPage.addRecoverableError("Error en la formacion del detalle de log");
			}

			if (selAlmAgregarParametrosSearchPage.hasError()){
				return selAlmAgregarParametrosSearchPage;
			}
			
			this.cargarParametrosAdp(selAlmAgregarParametrosSearchPage, detalle);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return selAlmAgregarParametrosSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void cargarParametrosAdp(SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPage, String detalle) throws Exception{

		// obtengo el run de la corrida
		// seteo los parametros como mensaje de la corrida,
		// y paso todos los parametros cargados en el GUI al los paremtros de la corrida.
		AdpRun run = AdpRun.getRun(selAlmAgregarParametrosSearchPage.getCorrida().getId());			
		run.changeMessage("Parametros seleccionados: " + detalle);

		// borro los parametros antiguos de la corrida
		run.clearTempParameters();
		
		run.putTempParameter(SelAlmAgregarParametrosSearchPage.ID_RECURSO, selAlmAgregarParametrosSearchPage.getRecurso().getIdView() );
		
		run.putTempParameter(SelAlmAgregarParametrosSearchPage.ID_TIP_OBJ_IMP, selAlmAgregarParametrosSearchPage.getRecurso().getTipObjImp().getIdView() );
		
		Long idTipoSelAlmDet = selAlmAgregarParametrosSearchPage.getTipoSelAlmDet().getId();
		TipoSelAlm tipoSelAlmDet = TipoSelAlm.getById(idTipoSelAlmDet);
		
		run.putTempParameter(SelAlmAgregarParametrosSearchPage.ID_TIPO_SEL_ALM_DET, selAlmAgregarParametrosSearchPage.getTipoSelAlmDet().getIdView());
		
		if (tipoSelAlmDet.getEsTipoSelAlmDetDeuda()){
			String idsRecClaDeu = StringUtil.getStringComaSeparate(
					selAlmAgregarParametrosSearchPage.getListIdRecClaDeu());
			if(!StringUtil.isNullOrEmpty(idsRecClaDeu)){
				run.putTempParameter(SelAlmAgregarParametrosSearchPage.IDS_REC_CLA_DEU, idsRecClaDeu);
			}
			if(!StringUtil.isNullOrEmpty(selAlmAgregarParametrosSearchPage.getFechaVencimientoDesdeView())){
				run.putTempParameter(SelAlmAgregarParametrosSearchPage.FECHA_VENCIMIENTO_DESDE, selAlmAgregarParametrosSearchPage.getFechaVencimientoDesdeView() );
			}
			if(!StringUtil.isNullOrEmpty(selAlmAgregarParametrosSearchPage.getFechaVencimientoHastaView())){
				run.putTempParameter(SelAlmAgregarParametrosSearchPage.FECHA_VENCIMIENTO_HASTA, selAlmAgregarParametrosSearchPage.getFechaVencimientoHastaView() );
			}
			if(!StringUtil.isNullOrEmpty(selAlmAgregarParametrosSearchPage.getImporteHistoricoDesdeView())){
				run.putTempParameter(SelAlmAgregarParametrosSearchPage.IMPORTE_HISTORICO_DESDE, selAlmAgregarParametrosSearchPage.getImporteHistoricoDesdeView() );
			}
			if(!StringUtil.isNullOrEmpty(selAlmAgregarParametrosSearchPage.getImporteHistoricoHastaView())){
				run.putTempParameter(SelAlmAgregarParametrosSearchPage.IMPORTE_HISTORICO_HASTA, selAlmAgregarParametrosSearchPage.getImporteHistoricoHastaView() );
			}
			// es requerido para los parametros de deuda
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.APLICA_AL_TOTAL_DEUDA, selAlmAgregarParametrosSearchPage.getAplicaAlTotalDeuda().getId().toString() );
			// es requerido para los parametros de deuda
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.CANTIDAD_MINIMA_DEUDA, selAlmAgregarParametrosSearchPage.getCantidadMinimaDeudaView() );
			
		}else if (tipoSelAlmDet.getEsTipoSelAlmDetConvCuot()){
			// carga de atributos de cuotas de convenio
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.FECHA_VENCIMIENTO, selAlmAgregarParametrosSearchPage.getFechaVencimientoView());
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.CANTIDAD_CUOTAS_PLAN, selAlmAgregarParametrosSearchPage.getCantidadCuotasPlanView());
		}

		if(!StringUtil.isNullOrEmpty(selAlmAgregarParametrosSearchPage.getCuenta().getNumeroCuenta())){
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.NUMERO_CUENTA, selAlmAgregarParametrosSearchPage.getCuenta().getNumeroCuenta() );
		}

		if(!ModelUtil.isNullOrEmpty(selAlmAgregarParametrosSearchPage.getObra())){
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.ID_OBRA, selAlmAgregarParametrosSearchPage.getObra().getIdView() );
		}
		
		// cargo el idSelAlm para luego recuperarlo desde el Worker que corresponda
		run.putTempParameter(SelAlmAgregarParametrosSearchPage.ID_SEL_ALM_INC, selAlmAgregarParametrosSearchPage.getSelAlm().getIdView() );
		
		// carga de parametros del objeto imponible
		for (Iterator iter = selAlmAgregarParametrosSearchPage.getParamObjImp().keySet().iterator(); iter.hasNext();) {
			String clave = (String) iter.next();
			String valor = selAlmAgregarParametrosSearchPage.getParamObjImp().get(clave);
			run.putTempParameter(clave, valor);
		}
		
		// carga de exenciones seleccionadas SI
		String idsExeSI = StringUtil.getStringComaSeparate(
				selAlmAgregarParametrosSearchPage.getListExencionSI());
		if(!StringUtil.isNullOrEmpty(idsExeSI)){
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.IDS_EXENCIONES_SI, idsExeSI);
		}
		
		// carga de exenciones seleccionadas NO
		String idsExeNO = StringUtil.getStringComaSeparate(
				selAlmAgregarParametrosSearchPage.getListExencionNO());
		if(!StringUtil.isNullOrEmpty(idsExeNO)){
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.IDS_EXENCIONES_NO, idsExeNO);
		}

		PersonaVO persona = selAlmAgregarParametrosSearchPage.getPersona();
		if(!ModelUtil.isNullOrEmpty(persona)){
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.ID_PERSONA, persona.getId().toString());
		}

		// carga de atributos seleccionados SI
		String idsAtrSI = StringUtil.getStringComaSeparate(
				selAlmAgregarParametrosSearchPage.getListAtributoSI());
		if(!StringUtil.isNullOrEmpty(idsAtrSI)){
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.IDS_ATRIBUTOS_SI, idsAtrSI);
		}

		// carga de atributos seleccionados NO
		String idsAtrNO = StringUtil.getStringComaSeparate(
				selAlmAgregarParametrosSearchPage.getListAtributoNO());
		if(!StringUtil.isNullOrEmpty(idsAtrNO)){
			run.putTempParameter(SelAlmAgregarParametrosSearchPage.IDS_ATRIBUTOS_NO, idsAtrNO);
		}
		
		// carga del detalle del log
		run.putTempParameter(SelAlmAgregarParametrosSearchPage.DETALLE_LOG, detalle);	
	}
	
	private String getDetalleLog(SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPage){

		String detalleLog = "";
		Map exencionesSeleccionadas = selAlmAgregarParametrosSearchPage.getExencionesSeleccionadas();
		boolean aplicarFiltroExen =  exencionesSeleccionadas != null && !exencionesSeleccionadas.isEmpty();

		Map atribContribSeleccionados = selAlmAgregarParametrosSearchPage.getAtributosSeleccionados();
		boolean aplicaFiltroAtribContrib = atribContribSeleccionados != null && !atribContribSeleccionados.isEmpty();

		/* estado activo?
		// Armamos filtros del HQL
		if (selAlmAgregarParametrosSearchPage.getModoSeleccionar()) {
		  detalleLog += flagAnd ? " and " : " where ";
	      detalleLog += " d.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		 */

		// filtro Recurso requerido
		detalleLog += " Recurso: " + selAlmAgregarParametrosSearchPage.getRecurso().getDesRecurso();

		Long idTipoSelAlmDet = selAlmAgregarParametrosSearchPage.getTipoSelAlmDet().getId();
		TipoSelAlm tipoSelAlmDet = TipoSelAlm.getById(idTipoSelAlmDet);
		detalleLog += " Tipo Detalle Selec Alm: " + tipoSelAlmDet.getDesTipoSelAlm();
		
		// PARAMETROS DE LA DEUDA
		if(tipoSelAlmDet.getEsTipoSelAlmDetDeuda()){
			// clasificacion deuda
			// obtiene las descripciones a partir de los ids 
			if (selAlmAgregarParametrosSearchPage.getListIdRecClaDeu() != null &&          // puede ser nula 
					selAlmAgregarParametrosSearchPage.getListIdRecClaDeu().length > 0){
				detalleLog += " Clasificacion Deuda (";	
				for (String id : selAlmAgregarParametrosSearchPage.getListIdRecClaDeu()) {
					RecClaDeuVO recClaDeuVO = (RecClaDeuVO) ModelUtil.getBussImageModelByIdForList( NumberUtil.getLong(id), selAlmAgregarParametrosSearchPage.getRecurso().getListRecClaDeu());
					detalleLog += recClaDeuVO.getDesClaDeu() + " ";
				}
				detalleLog += " ) ";
			}

			// filtro Fecha Vencimiento Desde
			if (selAlmAgregarParametrosSearchPage.getFechaVencimientoDesde() != null ) {
				detalleLog += " Fecha Vencimiento Desde: " + DateUtil.formatDate(selAlmAgregarParametrosSearchPage.getFechaVencimientoDesde(), DateUtil.ddSMMSYYYY_MASK);
			}

			// filtro Fecha Vencimiento Hasta
			if (selAlmAgregarParametrosSearchPage.getFechaVencimientoHasta() != null ) {
				detalleLog += " Fecha Vencimiento Hasta: " + DateUtil.formatDate(selAlmAgregarParametrosSearchPage.getFechaVencimientoHasta(), DateUtil.ddSMMSYYYY_MASK);
			}

			// filtro Importe Historico Desde
			if (selAlmAgregarParametrosSearchPage.getImporteHistoricoDesde() != null ) {
				detalleLog += " Importe Historico Desde: " + selAlmAgregarParametrosSearchPage.getImporteHistoricoDesde();
			}
			// filtro Importe Historico Hasta
			if (selAlmAgregarParametrosSearchPage.getImporteHistoricoHasta() != null ) {
				detalleLog += " Importe Historico Hasta: " + selAlmAgregarParametrosSearchPage.getImporteHistoricoHasta();
			}
			// filtro Importe Actualizado Desde
			if (selAlmAgregarParametrosSearchPage.getImporteActualizadoDesde() != null ) {
				detalleLog += " Importe Actualizado Desde >= " + selAlmAgregarParametrosSearchPage.getImporteActualizadoDesde();
			}
			// filtro Importe Historico Hasta
			if (selAlmAgregarParametrosSearchPage.getImporteActualizadoHasta() != null ) {
				detalleLog += " Importe Actualizado Hasta >= " + selAlmAgregarParametrosSearchPage.getImporteActualizadoHasta();
			}

			// aplica al total de la deuda
			detalleLog += " Aplica al Total Deuda = " + selAlmAgregarParametrosSearchPage.getAplicaAlTotalDeuda().getValue()  ;
		}

		// PARAMETROS DE CUOTAS DE CONVENIO
		if (tipoSelAlmDet.getEsTipoSelAlmDetConvCuot()){
			detalleLog += " Fecha de Vto Convenio = "  + selAlmAgregarParametrosSearchPage.getFechaVencimientoView()  ;
			detalleLog += " Ctd Cuotas de Convenio = " + selAlmAgregarParametrosSearchPage.getCantidadCuotasPlanView()  ;
		}
		
		// numero de cuenta
		String numeroCuenta = selAlmAgregarParametrosSearchPage.getCuenta().getNumeroCuenta();
		if (!StringUtil.isNullOrEmpty(numeroCuenta) ) {
			detalleLog += " Numero de Cuenta = " + numeroCuenta;
		}

		// obra
		if (!ModelUtil.isNullOrEmpty(selAlmAgregarParametrosSearchPage.getObra()) ) {
			detalleLog += " Obra = " + selAlmAgregarParametrosSearchPage.getObra().getDesObra()  ;
		}

		// filtro exenciones
		if (aplicarFiltroExen){

			List<String> listExencionSI = selAlmAgregarParametrosSearchPage.getListExencionSI();
			List<String> listExencionNO = selAlmAgregarParametrosSearchPage.getListExencionNO();

			if (listExencionSI.size() > 0){
				detalleLog += " Exenciones Incluidas (";	
				for (String id : listExencionSI) {
					ExencionVO exencionVO = (ExencionVO) ModelUtil.getBussImageModelByIdForList( NumberUtil.getLong(id), selAlmAgregarParametrosSearchPage.getListExencion());
					detalleLog += exencionVO.getDesExencion() + " ";
				}
				detalleLog += " ) ";
			}
			if (listExencionNO.size() > 0){
				detalleLog += " Exenciones Excluidas (";	
				for (String id : listExencionNO) {
					ExencionVO exencionVO = (ExencionVO) ModelUtil.getBussImageModelByIdForList( NumberUtil.getLong(id), selAlmAgregarParametrosSearchPage.getListExencion());
					detalleLog += exencionVO.getDesExencion() + " ";
				}
				detalleLog += " ) ";
			}
		}

		//Contribuyente
		PersonaVO persona = selAlmAgregarParametrosSearchPage.getPersona();
		if (!ModelUtil.isNullOrEmpty(persona) ) {
			detalleLog += " Contribuyente = " + persona.getRepresent();
		}

		// filtro atributos del contribuyente
		if (aplicaFiltroAtribContrib){

			List<String> listAtribContribSI = selAlmAgregarParametrosSearchPage.getListAtributoSI();
			List<String> listAtribContribNO = selAlmAgregarParametrosSearchPage.getListAtributoNO();

			if (listAtribContribSI.size() > 0){
				detalleLog += " Atrib. Contrib. Incluidos (";	
				for (String id : listAtribContribSI) {
					AtributoVO atributoVO = (AtributoVO) ModelUtil.getBussImageModelByIdForList( NumberUtil.getLong(id), selAlmAgregarParametrosSearchPage.getListAtributo());
					detalleLog += atributoVO.getDesAtributo() + " ";
				}
				detalleLog += " ) ";
			}
			if (listAtribContribNO.size() > 0){
				detalleLog += " Atrib. Contrib. Excluidos (";	
				for (String id : listAtribContribNO) {
					AtributoVO atributoVO = (AtributoVO) ModelUtil.getBussImageModelByIdForList( NumberUtil.getLong(id), selAlmAgregarParametrosSearchPage.getListAtributo());
					detalleLog += atributoVO.getDesAtributo() + " ";
				}
				detalleLog += " ) ";
			}
		}

		// parametros del objeto imponible.
		if(selAlmAgregarParametrosSearchPage.getTipObjImpDefinition().poseeValoresCargados()){
			detalleLog += " Param Obj Imp = ( "; 
			for (TipObjImpAtrDefinition tipObjImpAtrDefinition : selAlmAgregarParametrosSearchPage.getTipObjImpDefinition().getListTipObjImpAtrDefinition()) {
				if (tipObjImpAtrDefinition.poseeValorCargado()){
					//selAlmAgregarParametrosSearchPage.getTipObjImpDefinition().getListTipObjImpAtrDefinition().get(2).getListValor();
					String desAtributo = tipObjImpAtrDefinition.getAtributo().getDesAtributo();

					boolean desdeFlag = false;
					for (Object listValor : tipObjImpAtrDefinition.getListValor()) {
						Object[] valor = (Object[]) listValor;
						String valorStr = "" + valor[1]; // no ejecutar (String) valor[1];

						// Posee dominio
						if (tipObjImpAtrDefinition.getPoseeDominio()){
							// Admite Bus por rango: clave.id1 -> on, clave.id2 -> on, clave.idn -> on (CHECKBOX)
							if (tipObjImpAtrDefinition.getAdmBusPorRan()){
								detalleLog += desAtributo + ": " + valorStr + "; ";
							}else {						   
								// NO Admite Bus por rango: clave -> id (COMBO)
								detalleLog += desAtributo + ": " + valorStr + "; ";
							}	
						} 				   

						// No posee dominio
						if (!tipObjImpAtrDefinition.getPoseeDominio()){
							// Admite Bus por rango: clave.desde -> valor , clave.hasta -> valor (TEXTBOX.DESDE Y TEXTBOX.HASTA)
							if (tipObjImpAtrDefinition.getAdmBusPorRan()){ 						  
								//String valDesde = parametrosRequest.get(strName + ".desde");
								//String valHasta = parametrosRequest.get(strName + ".hasta");
								if(!desdeFlag){
									detalleLog += desAtributo + " desde: " + valorStr + "; ";
									desdeFlag = true;
								}else{
									detalleLog += desAtributo + " hasta: " + valorStr + "; ";
								}
							}else {
								// NO admite Bus por rango: clave -> valor (TEXTBOX)
								detalleLog += desAtributo + ": " + valorStr + "; ";
							}
						}
					}
				}
			}
			detalleLog += ") ";
		}

		// no se implementaron filtros del proceso 

		return detalleLog;
	}

	public SelAlmAgregarParametrosSearchPage paramPersona(UserContext userContext, SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPageVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			Persona persona = Persona.getByIdNull(selAlmAgregarParametrosSearchPageVO.getPersona().getId());
			PersonaVO personaVO = (PersonaVO) persona.toVO(1);
			selAlmAgregarParametrosSearchPageVO.setPersona(personaVO);
			
			return selAlmAgregarParametrosSearchPageVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

}
