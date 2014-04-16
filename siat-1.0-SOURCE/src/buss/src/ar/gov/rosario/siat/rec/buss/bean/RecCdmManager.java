//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.ObjImpAtrVal;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Manejador del m&oacute;dulo Padron y submodulo CDM
 * 
 * @author tecso
 *
 */
public class RecCdmManager {
		
	private static Logger log = Logger.getLogger(RecCdmManager.class);
	
	private static final RecCdmManager INSTANCE = new RecCdmManager();
	
	/**
	 * Constructor privado
	 */
	private RecCdmManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static RecCdmManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM TipoObra
	public TipoObra createTipoObra(TipoObra tipoObra) throws Exception {

		// Validaciones de negocio
		if (!tipoObra.validateCreate()) {
			return tipoObra;
		}

		RecDAOFactory.getTipoObraDAO().update(tipoObra);

		return tipoObra;
	}
	
	public TipoObra updateTipoObra(TipoObra tipoObra) throws Exception {
		
		// Validaciones de negocio
		if (!tipoObra.validateUpdate()) {
			return tipoObra;
		}

		RecDAOFactory.getTipoObraDAO().update(tipoObra);
		
		return tipoObra;
	}
	
	public TipoObra deleteTipoObra(TipoObra tipoObra) throws Exception {
	
		// Validaciones de negocio
		if (!tipoObra.validateDelete()) {
			return tipoObra;
		}
		
		RecDAOFactory.getTipoObraDAO().delete(tipoObra);
		
		return tipoObra;
	}
	// <--- ABM TipoObra
	
	// ---> ABM FormaPago
	public FormaPago createFormaPago(FormaPago formaPago) throws Exception {

		// Validaciones de negocio
		if (!formaPago.validateCreate()) {
			return formaPago;
		}

		//Si los datos estan completos, generamos la descripcion
		formaPago.setDesFormaPago(null);
		
		RecDAOFactory.getFormaPagoDAO().update(formaPago);

		return formaPago;
	}
	
	public FormaPago updateFormaPago(FormaPago formaPago) throws Exception {
		
		// Validaciones de negocio
		if (!formaPago.validateUpdate()) {
			return formaPago;
		}

		RecDAOFactory.getFormaPagoDAO().update(formaPago);
		
		return formaPago;
	}
	
	public FormaPago deleteFormaPago(FormaPago formaPago) throws Exception {
	
		// Validaciones de negocio
		if (!formaPago.validateDelete()) {
			return formaPago;
		}
		
		RecDAOFactory.getFormaPagoDAO().delete(formaPago);
		
		return formaPago;
	}
	// <--- ABM TipoObra
	
	// ---> ABM Contrato
	public Contrato createContrato(Contrato contrato) throws Exception {

		// Validaciones de negocio
		if (!contrato.validateCreate()) {
			return contrato;
		}

		RecDAOFactory.getContratoDAO().update(contrato);

		return contrato;
	}
	
	public Contrato updateContrato(Contrato contrato) throws Exception {
		
		// Validaciones de negocio
		if (!contrato.validateUpdate()) {
			return contrato;
		}

		RecDAOFactory.getContratoDAO().update(contrato);
		
		return contrato;
	}
	
	public Contrato deleteContrato(Contrato contrato) throws Exception {
	
		// Validaciones de negocio
		if (!contrato.validateDelete()) {
			return contrato;
		}
		
		RecDAOFactory.getContratoDAO().delete(contrato);
		
		return contrato;
	}
	// <--- ABM Contrato
	
	// ---> ABM PlanillaCuadra
	public PlanillaCuadra createPlanillaCuadra(PlanillaCuadra planillaCuadra) throws Exception {

		// Validaciones de negocio
		if (!planillaCuadra.validateCreate()) {
			return planillaCuadra;
		}

		// Obtnemos las manzanas involucradas
		planillaCuadra.setearManzanas();

		RecDAOFactory.getPlanillaCuadraDAO().update(planillaCuadra);
		
		// Guardamos un evento en el historial
		HisEstPlaCua hisEstPlaCua = planillaCuadra.createHisEstPlaCua
			(planillaCuadra.getEstPlaCua(), SiatUtil.getValueFromBundle("rec.planillaCuadra.planillaCreada"));

		planillaCuadra.getListError().addAll(hisEstPlaCua.getListError());

		return planillaCuadra;
	}
	
	public PlanillaCuadra updatePlanillaCuadra(PlanillaCuadra planillaCuadra) throws Exception {
		
		// Validaciones de negocio
		if (!planillaCuadra.validateUpdate()) {
			return planillaCuadra;
		}

		// Seteamos las manzanas, por si cambiaron o se borraron las calles
		planillaCuadra.setearManzanas();
		
		RecDAOFactory.getPlanillaCuadraDAO().update(planillaCuadra);
		
		// Guardamos el historial
		HisEstPlaCua hisEstPlaCua = planillaCuadra.createHisEstPlaCua
			(planillaCuadra.getEstPlaCua(), SiatUtil.getValueFromBundle("rec.planillaCuadra.planillaModificada"));

		planillaCuadra.getListError().addAll(hisEstPlaCua.getListError());

		return planillaCuadra;
	}
	
	public PlanillaCuadra deletePlanillaCuadra(PlanillaCuadra planillaCuadra) throws Exception {
	
		// Validaciones de negocio
		if (!planillaCuadra.validateDelete()) {
			return planillaCuadra;
		}
		
		// Se Limpia el Historial de Estados de la Planilla
		for (HisEstPlaCua item: planillaCuadra.getListHisEstPlaCua()) {
			planillaCuadra.deleteHisEstPlaCua(item);
		}
		
		// Se Limpia el Historial de Repartidores de la Planilla
		for (HisObrRepPla item: planillaCuadra.getListHisObrRepPla()) {
			planillaCuadra.deleteHisObrRepPla(item);
		}
		
		// Borramos la planilla
		RecDAOFactory.getPlanillaCuadraDAO().delete(planillaCuadra);
		
		return planillaCuadra;
	}

	// <--- ABM PlanillaCuadra
	
	// ---> ABM Obra
	public Obra createObra(Obra obra, Double montoMinimoCuota, Double interesFinaciero) throws Exception {

		// Validaciones de negocio
		obra.validateCreate();
			
		// Validaciones para Forma de Pago 
		if (montoMinimoCuota == null) {
			obra.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_MONTOMINIMOCUOTA);
		}

		if (montoMinimoCuota != null && montoMinimoCuota < 0D) {
			obra.addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.OBRAFORMAPAGO_MONTOMINIMOCUOTA);
		}

		if (interesFinaciero == null) {
			obra.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_INTERESFINANCIERO);
		}

		if ((interesFinaciero != null) && (interesFinaciero < 0D || (interesFinaciero > 1L))) {
			obra.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, RecError.OBRAFORMAPAGO_INTERESFINANCIERO);
		}
		
		if (obra.hasError()) {
			return obra;
		}
		
		RecDAOFactory.getObraDAO().update(obra);
		
		// Creamos las fomas de pago para la obra
		List<FormaPago> listFormaPago = FormaPago.getListActivos();
		
		for (FormaPago formaPago:listFormaPago) {
			ObraFormaPago obraFormaPago = new ObraFormaPago(formaPago,montoMinimoCuota,interesFinaciero);
			obra.createObraFormaPago(obraFormaPago);
		}

		// creo un registro en el historico con el estado creado
		HisEstadoObra hisEstadoObra = obra.createHisEstadoObra(obra.getEstadoObra(),
				SiatUtil.getValueFromBundle("rec.obra.obraCreada"));
		obra.getListError().addAll(hisEstadoObra.getListError());
		
		return obra;
	}
	
	public Obra updateObra(Obra obra) throws Exception {
		
		// Validaciones de negocio
		if (!obra.validateUpdate()) {
			return obra;
		}

		RecDAOFactory.getObraDAO().update(obra);
		
		// creo un registro en el historico con el estado de la obra
		HisEstadoObra hisEstadoObra = obra.createHisEstadoObra(obra.getEstadoObra(),
				SiatUtil.getValueFromBundle("rec.obra.obraModificada"));
		obra.getListError().addAll(hisEstadoObra.getListError());

		
		return obra;
	}
	
	public Obra deleteObra(Obra obra) throws Exception {
	
		// Validaciones de negocio
		if (!obra.validateDelete()) {
			return obra;
		}
		
		// Se Limpia el Historial de Estados de la Obra
		for (HisEstadoObra estadoObra: obra.getListHisEstadoObra()) {
			obra.deleteHisEstadoObra(estadoObra);
		}
		
		RecDAOFactory.getObraDAO().delete(obra);
		
		return obra;
	}
	// <--- ABM Obra
	
	/** Devuelve una objeto imponible
	 *  agrupados por la carpeta pasada como parametro, recorriendo
	 *  los valores de la listCompleta tb pasada como parametro.
	 * 
	 * @param listCompleta
	 * @param agrupador
	 * @return
	 */
	
	public List<ObjImp> getListObjImpAgrupada (List<ObjImp> listCompleta, String agrupador) {

		List<ObjImp> listAgrupada = new ArrayList<ObjImp>();

		// recorro los detalles
		for(ObjImp objImp:listCompleta) {

			// recupero el agrupador
			String agrupadorObjimp = null;
			ObjImpAtrVal agrupadorAtrVal = objImp.getObjImpAtrValByIdTipo(ObjImpAtrVal.ID_AGRUPADOR_CATASTRAL);

			if (agrupadorAtrVal != null) {
				agrupadorObjimp = agrupadorAtrVal.getStrValor();

				// si coincide la carpeta con el agrupador de objImp
				// lo agrego a la lista
				if ( !StringUtil.isNullOrEmpty(agrupadorObjimp) && 
					agrupador.trim().equalsIgnoreCase(agrupadorObjimp.trim()) ) {

					listAgrupada.add(objImp);

				}
				
			}

		}
		return listAgrupada;
	}	

	// ---> ABM UsoCdM
	public UsoCdM createUsoCdM(UsoCdM usoCdM) throws Exception {

		// Validaciones de negocio
		if (!usoCdM.validateCreate()) {
			return usoCdM;
		}

		RecDAOFactory.getUsoCdMDAO().update(usoCdM);

		return usoCdM;
	}
	
	public UsoCdM updateUsoCdM(UsoCdM usoCdM) throws Exception {
		
		// Validaciones de negocio
		if (!usoCdM.validateUpdate()) {
			return usoCdM;
		}

		RecDAOFactory.getUsoCdMDAO().update(usoCdM);
		
		return usoCdM;
	}
	
	public UsoCdM deleteUsoCdM(UsoCdM usoCdM) throws Exception {
	
		// Validaciones de negocio
		if (!usoCdM.validateDelete()) {
			return usoCdM;
		}
		
		RecDAOFactory.getUsoCdMDAO().delete(usoCdM);
		
		return usoCdM;
	}
	// <--- ABM UsoCdM

	// ---> ABM AnulacionObra
	public AnulacionObra createAnulacionObra(AnulacionObra anulacionObra) throws Exception {

		// Validaciones de negocio
		if (!anulacionObra.validateCreate()) {
			return anulacionObra;
		}

		// Creamos la corrida
		AdpRun run = AdpRun.newRun(Proceso.PROCESO_ANULACION_OBRA_CDM, 
			"Corrida de Anulacion de Obra " + anulacionObra.getObra().getDesObra() + ". Fecha Creacion:" + new Date());
		run.create();

		Corrida corrida = Corrida.getByIdNull(run.getId());
        
        if(corrida == null) {
        	log.error("No se pudo obtener la Corrida creada");
        	anulacionObra.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return anulacionObra;
		}
        
        // Seteamos la corrida a la anulacion
        anulacionObra.setCorrida(corrida);

		// Creamos el registro de la Anulacion
        RecDAOFactory.getAnulacionObraDAO().update(anulacionObra);
        
		// Cargamos los parametros para Adp
		run.putParameter(AnulacionObra.ID_ANULACION_OBRA, anulacionObra.getId().toString());


		return anulacionObra;
	}
	
	public AnulacionObra updateAnulacionObra(AnulacionObra anulacionObra) throws Exception {

		// Validaciones de negocio
		if (!anulacionObra.validateUpdate()) {
			return anulacionObra;
		}

		// Recuperamos la corrida
		AdpRun run = AdpRun.getRun(anulacionObra.getCorrida().getId());
		
        if(run == null){
        	log.error("No se pudo obtener la Corrida");
        	anulacionObra.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return anulacionObra;
		}

		// Cargamos los parametros para Adp
		run.putParameter(AnulacionObra.ID_ANULACION_OBRA, anulacionObra.getId().toString());        
        
		RecDAOFactory.getAnulacionObraDAO().update(anulacionObra);

		return anulacionObra;
	}
	
	public AnulacionObra deleteAnulacionObra(AnulacionObra anulacionObra) throws Exception {

		// Validaciones de negocio
		if (!anulacionObra.validateDelete()) {
			return anulacionObra;
		}
		
		// Se elimina la anulacion
		RecDAOFactory.getAnulacionObraDAO().delete(anulacionObra);

		return anulacionObra;
	}
	// <--- ABM AnulacionObra
}
