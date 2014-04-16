//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.PeriodoDeuda;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.SituacionInmueble;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.model.EmisionCorCdMAdapter;
import ar.gov.rosario.siat.emi.iface.model.ImpresionCdMAdapter;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.esp.buss.bean.TipoEvento;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Manejador del m&oacute;dulo Emi y submodulo Emision de contribucion
 * de mejoras
 * 
 * @author tecso
 *
 */
public class EmiEmisionManager {
		
	private static Logger log = Logger.getLogger(EmiEmisionManager.class);
	
	private static final EmiEmisionManager INSTANCE = new EmiEmisionManager();
	
	/**
	 * Constructor privado
	 */
	private EmiEmisionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static EmiEmisionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Emision CdM
	/** Crea una emision de CdM, con la correspondiente corrida,  seteando
	 *  como parametros el id de obra y la fecha de vencimiento de la primer cuota
	 * 
	 * @param emision
	 * @param obra
	 * @param fechaVencimiento
	 * @return
	 * @throws Exception
	 */
	public Emision createEmisionCdM(Emision emision, Long idObra, Date fechaVencimiento) throws Exception {

		// valido los datos de la emision
		emision.validateCreate();

		// valido los datos especificos de cdm
		emision = this.validateEmisionCdM(emision, idObra, fechaVencimiento);

		// si hubo errores
		if (emision.hasError()) {
			return emision;
		}

		// creo la corrida
		AdpRun run = AdpRun.newRun(Proceso.PROCESO_EMISION_CDM, 
			"Corrida de CdM. Fecha Creacion:" + new Date());
		run.create();
		
		Corrida corrida = Corrida.getByIdNull(run.getId());
        
        if(corrida == null){
        	log.error("no se pudo obtener la Corrida creada");
        	emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return emision;
		}
        
        // le seteo la corrida a la emision
        emision.setCorrida(corrida);
        
		// creo el registro de emision
		EmiDAOFactory.getEmisionDAO().update(emision);
        
		// carga de parametros para adp
		run.putParameter(Emision.ADP_PARAM_ID, emision.getId().toString());
		run.putParameter(Obra.ID_OBRA, idObra.toString());
		run.putParameter(Obra.FECHA_VENCIMIENTO, DateUtil.formatDate
			(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK));
        
		return emision;
	}
	
	private Emision validateEmisionCdM(Emision emision, Long idObra, Date fechaVencimiento) 
		throws Exception {

		// valido que se halla ingresado la obra
		if (idObra == null || idObra < 1) {
			emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_LABEL);
		}

		// valido que se halla ingresado la fecha de vencimiento
		if (fechaVencimiento == null) {
			emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISIONCDM_FECHA_VENCIMIENTO);
		}

		return emision;
	}
	
	public Emision updateEmisionCdM(Emision emision, Long idObra, Date fechaVencimiento) throws Exception {
		
		// valido los datos de la emision
		emision.validateUpdate();

		// valido los datos especificos de cdm
		emision = this.validateEmisionCdM(emision, idObra, fechaVencimiento);

		// si hubo errores
		if (emision.hasError()) {
			return emision;
		}

		// recupero la corrida
		AdpRun run = AdpRun.getRun(emision.getCorrida().getId());
		
        if(run == null){
        	log.error("no se pudo obtener la Corrida");
        	emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return emision;
		}

		// carga de parametros para adp
		run.putParameter(Obra.ID_OBRA, idObra.toString());
		run.putParameter(Obra.FECHA_VENCIMIENTO, DateUtil.formatDate
			(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK));
        
        // creo el registro de emision
		EmiDAOFactory.getEmisionDAO().update(emision);
		
		return emision;
	}
	
	public Emision deleteEmisionCdM(Emision emision) throws Exception {
	
		// Validaciones de negocio
		if (!emision.validateDelete()) {
			return emision;
		}
		
		//Se elimina la emision
		EmiDAOFactory.getEmisionDAO().delete(emision);
		
		return emision;
	}
	// <--- ABM Emision CdM
	
	// ---> ABM Impresion CdM
	/** Crea una impresion de CdM, con la correspondiente corrida,  seteando
	 *  como parametros el id de obra y la fecha de vencimiento de la primer cuota
	 * 
	 * @param emision
	 * @param idObra
	 * @param anio
	 * @param mes
	 * @return
	 * @throws Exception
	 */
	public Emision createImpresionCdM(Emision emision, Long idObra, 
			Integer anio, Integer mes, SiNo impresionTotal, FormatoSalida formatoSalida) throws Exception {

		// validamos los datos de la emision
		emision.validateCreate();

		// validamos los datos de la impresion de CdM
		emision = this.validateImpresionCdM(emision, idObra, anio, mes, formatoSalida);
		
		// si hubo errores
		if (emision.hasError()) {
			return emision;
		}

		// si no hubo errores, creamos la corrida
		AdpRun run = AdpRun.newRun(Proceso.PROCESO_IMPRESION_CDM, 
			"Corrida de Impresion de CdM. Fecha Creacion:" + new Date());

		run.create();
		
		Corrida corrida = Corrida.getByIdNull(run.getId());
        if(corrida == null){
        	log.error("no se pudo obtener la Corrida creada");
        	emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return emision;
		}

        //le seteamos la corrida a la emision
        emision.setCorrida(corrida);
  		// creamos el registro de emision
		EmiDAOFactory.getEmisionDAO().update(emision);
        
		// carga de parametros para adp
		run.putParameter(Emision.ADP_PARAM_ID, emision.getId().toString());
		run.putParameter(Obra.ID_OBRA, idObra.toString());
		run.putParameter(ImpresionCdMAdapter.MES, mes.toString());
		run.putParameter(ImpresionCdMAdapter.ANIO, anio.toString());
		run.putParameter(ImpresionCdMAdapter.FORMATOSALIDA, formatoSalida.getId().toString());
		run.putParameter(ImpresionCdMAdapter.IMPRESIONTOTAL, impresionTotal.getId().toString());

   		return emision;
	}

	public Emision updateImpresionCdM(Emision emision, Long idObra, 
			Integer anio, Integer mes, SiNo impresionTotal, FormatoSalida formatoSalida) throws Exception {
		
		// valido los datos de la emision
		emision.validateUpdate();

		// validamos los datos de la impresion de CdM
		emision = this.validateImpresionCdM(emision, idObra, anio, mes, formatoSalida);

		// si hubo errores
		if (emision.hasError()) {
			return emision;
		}

		// recupero la corrida
		AdpRun run = AdpRun.getRun(emision.getCorrida().getId());
		
        if(run == null){
        	log.error("no se pudo obtener la Corrida");
        	emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return emision;
		}

		// carga de parametros para adp
		run.putParameter(Emision.ADP_PARAM_ID, emision.getId().toString());
		run.putParameter(Obra.ID_OBRA, idObra.toString());
		run.putParameter(ImpresionCdMAdapter.MES, mes.toString());
		run.putParameter(ImpresionCdMAdapter.ANIO, anio.toString());
		run.putParameter(ImpresionCdMAdapter.FORMATOSALIDA, formatoSalida.getId().toString());
		run.putParameter(ImpresionCdMAdapter.IMPRESIONTOTAL, impresionTotal.getId().toString());
        
        // creo el registro de emision
		EmiDAOFactory.getEmisionDAO().update(emision);
		
		return emision;
	}
	
	public Emision deleteImpresionCdM(Emision emision) throws Exception {
		
		// Validaciones de negocio
		if (!emision.validateDelete()) {
			return emision;
		}
		
		//Se elimina la emision
		EmiDAOFactory.getEmisionDAO().delete(emision);
		
		return emision;
	}
	
	private Emision validateImpresionCdM(Emision emision, Long idObra, Integer anio, 
			Integer mes, FormatoSalida formatoSalida) throws Exception {

		// Validamos que se halla ingresado la obra
		if (idObra == null) {
			emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_LABEL);
		}
	
		// Validamos que se halla ingresado el anio de vencimiento
		if (anio == null) {
			emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.IMPRESIONCDM_ANIO);
		}

		// Validamos el anio
		if (anio != null && !DateUtil.isValidAnio(anio)) {
			emision.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, EmiError.IMPRESIONCDM_ANIO);
		}
		
		// Validamos que se halla ingresado el mes de vencimiento
		if (mes == null) {
			emision.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.IMPRESIONCDM_MES);
		}
		
		// Validamos el mes 
		if (mes != null && !DateUtil.isValidMes(mes)) {
			emision.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, EmiError.IMPRESIONCDM_MES);
		}

		// Validamos que sea valido el Formato de Salida
		if (!FormatoSalida.getEsValido(formatoSalida.getId())) {
			emision.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, EmiError.IMPRESIONCDM_FORMATO_SALIDA);
		}

		return emision;
	}
	// <--- ABM Impresion CdM	

	// ---> ABM Emision Extraordinaria
	public Emision createEmisionExt(Emision emision, DeudaAdmin deudaAdmin, 
			List<DeuAdmRecCon> listDeuAdmRecCon) throws Exception {
		
		Session session = SiatHibernateUtil.currentSession();
		try {
			// Creamos el registro de Emision
			EmiDAOFactory.getEmisionDAO().update(emision);
			session.flush();
			log.debug("Creando emision");
			
			deudaAdmin.setStrConceptosByListRecCon(listDeuAdmRecCon);
			log.debug("Creando strConceptos");

			// Creamos el registro de Deuda Administrativa
			GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);
			session.flush();
			log.debug("Creando deuda administrativa");
			
			// Graba la lista de DeuAdmRecCon
			for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
				deudaAdmin.createDeuAdmRecCon(deuAdmRecCon);
			}
			log.debug("creo los conceptos");
			
			
		} catch (Exception e) {
			log.error(e);			
		}
        
		return emision;
	}
	// <--- ABM Emision Extraordinaria
	
	// ---> ABM Emision Corregida CdM
	public Emision createEmisionCorCdM(Emision emisionCorCdM, Long idObra, Integer idValActTipObr) throws Exception {

		// valido los datos de la emision
		emisionCorCdM.validateCreate();

		// valido los datos especificos de cdm
		emisionCorCdM = this.validateEmisionCorCdM(emisionCorCdM, idObra, idValActTipObr);

		// si hubo errores
		if (emisionCorCdM.hasError()) {
			return emisionCorCdM;
		}

		// Creamos la corrida
		AdpRun run = AdpRun.newRun(Proceso.PROCESO_EMISION_COR_CDM, 
				"Corrida de Emision Corregida CdM. Fecha Creacion:" + new Date());
		run.create();
		
		Corrida corrida = Corrida.getByIdNull(run.getId());
        
        if(corrida == null){
        	log.error("No se pudo obtener la Corrida del Proceso Creada");
        	emisionCorCdM.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
        	return emisionCorCdM;
		}
        
        // Seteamos la corrida a la emision
        emisionCorCdM.setCorrida(corrida);
        
		// Creamos el registro de emision
		EmiDAOFactory.getEmisionDAO().update(emisionCorCdM);
        
		// Cargamos de parametros para adp
		run.putParameter(Emision.ADP_PARAM_ID, emisionCorCdM.getId().toString());
		run.putParameter(EmisionCorCdMAdapter.ID_OBRA, idObra.toString());
		run.putParameter(EmisionCorCdMAdapter.ID_VALACTTIPOBR, idValActTipObr.toString());
		
   		return emisionCorCdM;
	}
	
	private Emision validateEmisionCorCdM(Emision emisionCorCdM, Long idObra, Integer idValActTipObr) 
		throws Exception {

		// Validamos que se halla ingresado la obra
		if (idObra == null || idObra < 1) {
			emisionCorCdM.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_LABEL);
		}

		// Validamos que se halla elegido si emite con los valores actuales o anteriores de
		// Tipo de Obra
		if (idValActTipObr == null || !SiNo.getEsValido(idValActTipObr)) {
			emisionCorCdM.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISIONCORCDM_VALACTTIPOBR);
		}
		
		return emisionCorCdM;
	}
	
	public Emision updateEmisionCorCdM(Emision emisionCorCdM, Long idObra, Integer idValActTipObr) throws Exception {
		
		// valido los datos de la emision
		emisionCorCdM.validateUpdate();

		// valido los datos especificos de cdm
		emisionCorCdM = this.validateEmisionCorCdM(emisionCorCdM, idObra, idValActTipObr);

		// si hubo errores
		if (emisionCorCdM.hasError()) {
			return emisionCorCdM;
		}

		// recupero la corrida
		AdpRun run = AdpRun.getRun(emisionCorCdM.getCorrida().getId());
		
        if(run == null){
        	log.error("No se pudo obtener la Corrida del Proceso Creada");
        	emisionCorCdM.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_LABEL);
			return emisionCorCdM;
		}

		// Cargamos de parametros para adp
		run.putParameter(Emision.ADP_PARAM_ID, emisionCorCdM.getId().toString());
		run.putParameter(EmisionCorCdMAdapter.ID_OBRA, idObra.toString());
		run.putParameter(EmisionCorCdMAdapter.ID_VALACTTIPOBR, idValActTipObr.toString());
        
		// Actualizo el registro de Emision
		EmiDAOFactory.getEmisionDAO().update(emisionCorCdM);
		
		return emisionCorCdM;
	}
	
	public Emision deleteEmisionCorCdM(Emision emisionCorCdM) throws Exception {
	
		// Validaciones de negocio
		if (!emisionCorCdM.validateDelete()) {
			return emisionCorCdM;
		}
		
		//Se elimina la emision
		EmiDAOFactory.getEmisionDAO().delete(emisionCorCdM);
		
		return emisionCorCdM;
	}
	// <--- ABM Emision Corregida CdM

	// ---> ABM EmiValEmiMat
	public EmiValEmiMat createEmiValEmiMat(EmiValEmiMat emiValEmiMat) throws Exception {

		EmiDAOFactory.getEmiValEmiMatDAO().update(emiValEmiMat);

		return emiValEmiMat;
	}
	
	public EmiValEmiMat updateEmiValEmiMat(EmiValEmiMat emiValEmiMat) throws Exception {
		

		EmiDAOFactory.getEmiValEmiMatDAO().update(emiValEmiMat);
		
		return emiValEmiMat;
	}
	
	public EmiValEmiMat deleteEmiValEmiMat(EmiValEmiMat emiValEmiMat) throws Exception {
	
		
		EmiDAOFactory.getEmiValEmiMatDAO().delete(emiValEmiMat);
		
		return emiValEmiMat;
	}
	// <--- ABM EmiValEmiMat

	// ---> ABM Emision Masiva
	public Emision createEmisionMas(Emision emision) throws Exception {

		// Validaciones de negocio
		if (!emision.validateCreate()) {
			return emision;
		}

		AdpRun run = null;

		// Creamos la corrida
		run = AdpRun.newRun(Proceso.PROCESO_EMISION_MAS, "Corrida de Emision Masiva: " + 
				DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));	
		run.create();

		Corrida corrida = Corrida.getByIdNull(run.getId());
		emision.setCorrida(corrida);
		
		EmiDAOFactory.getEmisionDAO().update(emision);

		run.putParameter(Emision.ADP_PARAM_ID, emision.getId().toString());


		return emision;
	}
	
	public Emision updateEmisionMas(Emision emision) throws Exception {

		// Validaciones de negocio
		if (!emision.validateUpdate()) {
			return emision;
		}
		
		AdpRun run = AdpRun.getRun(emision.getCorrida().getId());
		run.putParameter(Emision.ADP_PARAM_ID, emision.getId().toString());

		Corrida corrida = Corrida.getByIdNull(run.getId());
		emision.setCorrida(corrida);
		
		EmiDAOFactory.getEmisionDAO().update(emision);
		
		return emision;
	}
	
	public Emision deleteEmisionMas(Emision emision) throws Exception {
	
		// Validaciones de negocio
		if (!emision.validateDelete()) {
			return emision;
		}

		// Obtenemos la corrida
		Corrida corrida = emision.getCorrida();

		EmiDAOFactory.getEmisionDAO().delete(emision);
		
		// Sincronizamos con la BD
		SiatHibernateUtil.currentSession().flush();

		// Eliminamos la corrida
		AdpRun.deleteRun(corrida.getId());

		return emision;
	}
	// <--- ABM Emision Masiva

	// ---> ABM Emision Puntual
	public Emision createEmisionPuntual(Emision emision, Cuenta cuenta,
			List<GenericAtrDefinition> listAtributos) throws Exception {

		if (!emision.validateCreate()) {
			return emision;
		}
		
		// Creamos el registro de Emision
		//EmiDAOFactory.getEmisionDAO().update(emision);
	
		Recurso recurso = emision.getRecurso();
		String codigo = recurso.getCodigoEmisionBy(new Date());
		
		// Inicializamos el evaluador
		emision.ininitializaEngine(codigo);

		Integer cantDeuPer = emision.getCantDeuPer();
		Integer anio = emision.getAnio();
		Integer periodoDesde = emision.getPeriodoDesde();
		Integer periodoHasta = emision.getPeriodoHasta();

		if (recurso.getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO)) {
			anio = 0;
			periodoDesde = 0;
			periodoHasta = 0;
		}

		emision.setListAuxDeuda(new ArrayList<AuxDeuda>());
		for (int periodo=periodoDesde; periodo <= periodoHasta; periodo++) {
			Date fechaAnalisis = DateUtil.getFirstDatOfMonth(periodo, anio);

			// Obtenemos las Exenciones de la cuenta
			List<CueExe> listCueExe = cuenta.getListCueExeVigente(fechaAnalisis);

			for (int i=0; i < cantDeuPer; i++) {
				
				// Creamos la deuda temporal
				AuxDeuda auxDeuda = emision.eval(cuenta, listCueExe, anio, periodo, listAtributos);

				if (auxDeuda != null) {
					emision.getListAuxDeuda().add(auxDeuda);
				} else {
					log.info("Emision de deuda cancelada.");
					continue;
				}								
			}
		}
		
		return emision;
	}
	
	public Emision deleteEmisionPuntual(Emision emision) throws Exception {

		if (!emision.validateDelete()) {
			return emision;
		}

		EmiDAOFactory.getEmiValEmiMatDAO().delete(emision);
		
		return emision;
	}
	// <--- ABM Emision Puntual
	
	
	// ---> Emision Externa
	public Emision createEmisionExterna(Emision emision) throws Exception {

		// Validaciones de negocio
		if (!emision.validateCreate()) {
			return emision;
		}

		AdpRun run = null;

		// Creamos la corrida
		run = AdpRun.newRun(Proceso.PROCESO_EMISION_EXTERNA, "Corrida de Emision Externa: " + 
				DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK));	
		run.create();

		Corrida corrida = Corrida.getByIdNull(run.getId());
		emision.setCorrida(corrida);
		
		EmiDAOFactory.getEmisionDAO().update(emision);

		run.putParameter(Emision.ADP_PARAM_ID, emision.getId().toString());
		
		return emision;
	}

	public Emision deleteEmisionExterna(Emision emision) throws Exception {
		
		// Validaciones de negocio
		if (!emision.validateDelete()) {
			return emision;
		}

		// Obtenemos la corrida
		Corrida corrida = emision.getCorrida();

		EmiDAOFactory.getEmisionDAO().delete(emision);
		
		// Sincronizamos con la BD
		SiatHibernateUtil.currentSession().flush();

		// Eliminamos la corrida
		AdpRun.deleteRun(corrida.getId());

		return emision;
	}

	// <--- Emision Externa
	
	// ---> Emision Espectaculos publicos
	public Emision createEmisionEspectaculos(Emision emision, Date fechaHab, Date fechaVencimiento, String tab,TipoEvento tipoEvento, 
			List<Exencion> listExenciones, List<GenericAtrDefinition> listAtributos) throws Exception {

		if (!emision.validateCreate()) {
			return emision;
		}
		
		// Creamos el registro de Emision
		// EmiDAOFactory.getEmisionDAO().update(emision);
		
		Recurso recurso = emision.getRecurso();
		String codigo = recurso.getCodigoEmisionBy(new Date());
		
		// Inicializamos el evaluador
		emision.ininitializaEngine(codigo);
		emision.addTable("esp_entradas", tab);
		String strFecHab = DateUtil.formatDate(fechaHab, DateUtil.YYYYMMDD_MASK);
		emision.addElementToContext("fechaHab",Integer.parseInt(strFecHab));
		String strFecDec = DateUtil.formatDate(new Date(), DateUtil.YYYYMMDD_MASK);
		emision.addElementToContext("fechaDec",Integer.parseInt(strFecDec));
		emision.addElementToContext("cuenta", emision.getCuenta());
		emision.addElementToContext("tipoEvento", tipoEvento);
		emision.addElementToContext("listExencion", listExenciones);

		AuxDeuda auxDeuda = emision.eval(emision.getCuenta(), null, 0, 0,listAtributos);
		
		if (auxDeuda == null) {
			String msg = "La emision de la deuda ha sido cancelada";
			log.info(msg);
			emision.addMessageValue(msg);
			return emision;
		}
		
		auxDeuda.setFechaVencimiento(fechaVencimiento);
		
		emision.setListAuxDeuda(new ArrayList<AuxDeuda>());
		
		emision.getListAuxDeuda().add(auxDeuda);
				
		return emision;
	}
	
	public Emision deleteEmisionEspectaculos(Emision emision) throws Exception {

		if (!emision.validateDelete()) {
			return emision;
		}

		EmiDAOFactory.getEmiValEmiMatDAO().delete(emision);
		
		return emision;
	}
	// <--- Fin Emision Espectaculos publicos
	
	public Emision createEmisionTRP(Emision emision,Cuenta cuenta,
			SituacionInmueble s, Integer ajuste, Integer visac,
			String strValEmiMat, Double totRecibo,String atrTab) throws Exception {

		if (!emision.validateCreate()) {
			return emision;
		}
		
		Recurso recurso = emision.getRecurso();
		String codigo = recurso.getCodigoEmisionBy(new Date());
		
		// Inicializamos el evaluador
		emision.ininitializaEngine(codigo);
		emision.addTable("detalles", strValEmiMat);
		emision.addElementToContext("situacInmub", s.getId());
		emision.addElementToContext("aplica_ajuste", ajuste.intValue());
		emision.addElementToContext("visacion_previa", visac.intValue());
		emision.addElementToContext("descuento", totRecibo);
		
		Integer anio = emision.getAnio();
		Integer periodoDesde = emision.getPeriodoDesde();
		Integer periodoHasta = emision.getPeriodoHasta();
		Integer cantDeuPer = emision.getCantDeuPer();
		
		if (cantDeuPer == null) { 
			cantDeuPer = 1;
		}
		
		
		Date fechaAnalisis = new Date();
		// Obtenemos las Exenciones de la cuenta
		List<CueExe> listCueExe = cuenta.getListCueExeVigente(fechaAnalisis);
		
		emision.setListAuxDeuda(new ArrayList<AuxDeuda>());
		for (int i=0; i < cantDeuPer; i++) {
			// Creamos la deuda temporal
			AuxDeuda auxDeuda = emision.eval(cuenta, listCueExe, 0, 0);
			if (auxDeuda == null) {
				log.info("Emision de deuda cancelada.");
				continue;
			}
			auxDeuda.setAtrAseVal(atrTab);
			emision.getListAuxDeuda().add(auxDeuda);
			
		}
		
		
		return emision;
	}

	public Emision createEmisionTRPExtraordinaria(Emision emision,Cuenta cuenta,
		Double importe, String nroExpediente) throws Exception {

		if (!emision.validateCreate()) {
			return emision;
		}
		
		// Creamos el registro de Emision
		//EmiDAOFactory.getEmisionDAO().update(emision);
	
		Integer cantDeuPer = emision.getCantDeuPer();		
		if (cantDeuPer == null) { 
			cantDeuPer = 1;
		}
		
		emision.setListAuxDeuda(new ArrayList<AuxDeuda>());
		for (int i=0; i < cantDeuPer; i++) {
			// Creamos la deuda temporal
			Integer anio = 0;
			Integer periodo = 0;
			Date fechaVencimiento;
			String atrAsentamiento = "";
			Double importeBruto = importe;
			Double concepto1 = importe;
			Atributo expediente = Atributo.getByCodigo("nroExpediente");
			String atrAseVal = "";
			Vencimiento vencimineto = emision.getRecurso().getVencimiento();
			fechaVencimiento = Vencimiento.getFechaVencimiento(new Date(), vencimineto.getId());
			AuxDeuda auxDeuda = emision.createAuxDeuda(cuenta, anio, periodo, fechaVencimiento, null, 
					atrAsentamiento, importe, importeBruto, concepto1, null, null, null, "", atrAseVal, "");
			atrAseVal = StringUtil.setXMLContentByTag(atrAseVal, "A"+ expediente.getId(), nroExpediente);
			auxDeuda.setAtrAseVal(atrAseVal);
			auxDeuda.setLeyenda("Tasa/Derecho");
			emision.getListAuxDeuda().add(auxDeuda);
		}
		
		
		return emision;
	}

}