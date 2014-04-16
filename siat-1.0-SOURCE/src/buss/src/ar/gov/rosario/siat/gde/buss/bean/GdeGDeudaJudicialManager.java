//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.ForCamVO;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;

/**
 * Manejador del subm&oacute;dulo Gesti&oacute;n Deuda Judicial del m&oacute;dulo Gesti&oacute;n Deuda Judicial
 * 
 * @author tecso
 *
 */
public class GdeGDeudaJudicialManager {
	
	private static Logger log = Logger.getLogger(GdeGDeudaJudicialManager.class);
	
	public static final GdeGDeudaJudicialManager INSTANCE = new GdeGDeudaJudicialManager();
	
	/**
	 * Constructor privado
	 */
	private GdeGDeudaJudicialManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static GdeGDeudaJudicialManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM SelAlm	
	public SelAlm createSelAlm(SelAlm selAlm) throws Exception {

		// Validaciones de negocio
		if (!selAlm.validateCreate()) {
			return selAlm;
		}

		GdeDAOFactory.getSelAlmDAO().update(selAlm);

		return selAlm;
	}
	
	public SelAlm updateSelAlm(SelAlm selAlm) throws Exception {
		
		// Validaciones de negocio
		if (!selAlm.validateUpdate()) {
			return selAlm;
		}
		
		GdeDAOFactory.getSelAlmDAO().update(selAlm);
		
	    return selAlm;
	}
	
	public SelAlm deleteSelAlm(SelAlm selAlm) throws Exception {

		// Validaciones de negocio
		if (!selAlm.validateDelete()) {
			return selAlm;
		}
		
		selAlm.deleteListSelAlmDet();
		
		selAlm.deleteListSelAlmLog();
		
		GdeDAOFactory.getSelAlmDAO().delete(selAlm);
		
		return selAlm;
	}
	// <--- ABM SelAlm	
	
	// ---> ABM ProcesoMasivo	
	
	/**
	 * Realiza la Creacion del envio judicial.
	 * Previamente realiza las validaciones
	 * @param procesoMasivo
	 * @param formularioVO (para procesos que generan reportes con campos parametricos)
	 * @return ProcesoMasivo
	 * @throws Exception
	 */
	public ProcesoMasivo createProcesoMasivo(ProcesoMasivo procesoMasivo, FormularioVO formularioVO) 
		throws Exception {

		// Validaciones de negocio
		if (!procesoMasivo.validateCreate()) {
			return procesoMasivo;
		}
		
		// Validacion de los campos del formulario
		log.debug("validando campos del form - formulario:"+formularioVO);
		if(!ModelUtil.isNullOrEmpty(formularioVO) && !formularioVO.validateCampos()){
			formularioVO.passErrorMessages(procesoMasivo);
			return procesoMasivo;					
		}
		
		// Creacion de la Seleccion Almacenada Incluida
		SelAlmDeuda selAlmInc = new SelAlmDeuda();
        
		GdeDAOFactory.getSelAlmDAO().update(selAlmInc);
		
		procesoMasivo.setSelAlmInc(selAlmInc);
		
		// Creacion de la Seleccion Almacenada Excluida
		SelAlmDeuda selAlmExc = new SelAlmDeuda();
        
        GdeDAOFactory.getSelAlmDAO().update(selAlmExc);
        
        procesoMasivo.setSelAlmExc(selAlmExc);
        
        // Creacion de la Corrida
        String desCorrida = Proceso.PROCESO_PROCESO_MASIVO + " - " + 
        	procesoMasivo.getRecurso().getDesRecurso() + " - " + 
        	DateUtil.formatDate(procesoMasivo.getFechaEnvio(), DateUtil.ddSMMSYYYY_MASK);
        
        AdpRun run = AdpRun.newRun(Proceso.PROCESO_PROCESO_MASIVO, desCorrida);
		run.create();
		
        Corrida corrida = Corrida.getByIdNull(run.getId());
        
        if(corrida == null){
        	log.error("no se pudo obtener la Corrida creada por el proceso");
			procesoMasivo.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_CORRIDA);
			return procesoMasivo;
		}
        
        
        procesoMasivo.setCorrida(corrida);

		GdeDAOFactory.getProcesoMasivoDAO().update(procesoMasivo);
		
		// carga del parametro idProcesoMasivo para que este disponible en la ejecucion de pasos posterior
		// TODO crear una constante estatica
		run.putParameter("idProcesoMasivo", procesoMasivo.getId().toString());
		run.putParameter("idSelAlm", selAlmInc.getId().toString());
		
		
		// Si es un proceso con reportes parametrizados
		if (!ModelUtil.isNullOrEmpty(formularioVO)) {
			
			// Obtenemos el Formulario
			Formulario formulario = Formulario.getByIdNull(formularioVO.getId());
			run.putParameter("codFormulario", formulario.getCodFormulario());
		
			if (FormatoSalida.getEsValido(formularioVO.getFormatoSalida().getId())) {
				// Seteamos el formato de la salida
				String outputFormat = ""; 
				outputFormat = formularioVO.getFormatoSalida().getId().toString();
				run.putParameter("outputFormat", outputFormat);
			}

			if (!ListUtil.isNullOrEmpty(formularioVO.getListForCam())) {
				// Seteamos los campos parametricos del reporte
				for (ForCamVO campo: formularioVO.getListForCam()) {
					run.putParameter(campo.getCodForCam(), campo.getValorDefecto());
				} 
			} 
		
		}
		
		return procesoMasivo;
	}
	
	public ProcesoMasivo updateProcesoMasivo(ProcesoMasivo procesoMasivo, FormularioVO formularioVO) throws Exception {
		
		// Validaciones de negocio
		if (!procesoMasivo.validateUpdate()) {
			return procesoMasivo;
		}
		
		// Validacion de los campos del formulario
		log.debug("validando campos del form - formulario:"+formularioVO);
		if(!ModelUtil.isNullOrEmpty(formularioVO) && !formularioVO.validateCampos()){
			formularioVO.passErrorMessages(procesoMasivo);
			return procesoMasivo;					
		}
		
		// no hace falta limpiar la lista de procuradores excluidos 
		// porque la actualizacion solo es permitida en el paso 1, por lo cual no tiene procuradores excluidos
		
		GdeDAOFactory.getProcesoMasivoDAO().update(procesoMasivo);
		
		// Obtenemos la corrida
		AdpRun run = AdpRun.getRun(procesoMasivo.getCorrida().getId());
		
        if(run == null){
        	log.error("no se pudo obtener la Corrida");
			return procesoMasivo;
		}
	
		// Si es un proceso con reportes parametrizados
		if (!ModelUtil.isNullOrEmpty(formularioVO)) {
			
			// Obtenemos el Formulario
			Formulario formulario = Formulario.getByIdNull(formularioVO.getId());
			run.putParameter("codFormulario", formulario.getCodFormulario());
		
			if (FormatoSalida.getEsValido(formularioVO.getFormatoSalida().getId())) {
				// Seteamos el formato de la salida
				String outputFormat = ""; 
				outputFormat = formularioVO.getFormatoSalida().getId().toString();
				run.putParameter("outputFormat", outputFormat);
			}

			if (!ListUtil.isNullOrEmpty(formularioVO.getListForCam())) {
				// Seteamos los campos parametricos del reporte
				for (ForCamVO campo: formularioVO.getListForCam()) {
					run.putParameter(campo.getCodForCam(), campo.getValorDefecto());
				} 
			} 
		
		}
		
	    return procesoMasivo;
	}
	
	public ProcesoMasivo deleteProcesoMasivo(ProcesoMasivo procesoMasivo) throws Exception {

		// Validaciones de negocio
		if (!procesoMasivo.validateDelete()) {
			return procesoMasivo;
		}
		
		// borrar las selecciones almacenadas y sus detalles
		SelAlmDeuda selAlmInc = procesoMasivo.getSelAlmInc();
		SelAlmDeuda selAlmExc = procesoMasivo.getSelAlmExc();
		
		procesoMasivo.setSelAlmInc(null); // necesario
		procesoMasivo.setSelAlmExc(null); // necesario
		
		this.deleteSelAlm(selAlmInc);
		this.deleteSelAlm(selAlmExc);
		
		// borrar logs asociados a la seleccion almacenada: gde_selAlmlog
		
		// borrar procuradores excluidos: gde_proMasProExc del envio
		procesoMasivo.deleteListProMasProExc();
		
		// borrar pasos de proceso: gde_proMasPas
		
		// borrar envio
		GdeDAOFactory.getProcesoMasivoDAO().delete(procesoMasivo);
		
		//ProManager.getInstance().deleteCorrida(corrida);
		
		return procesoMasivo;
	}
	// <--- ABM ProcesoMasivo

	// ---> ABM Procurador	
	public Procurador createProcurador(Procurador procurador) throws Exception {

		// Validaciones de negocio
		if (!procurador.validateCreate()) {
			return procurador;
		}

		GdeDAOFactory.getProcuradorDAO().update(procurador);

		return procurador;
	}
	
	public Procurador updateProcurador(Procurador procurador) throws Exception {
		
		// Validaciones de negocio
		if (!procurador.validateUpdate()) {
			return procurador;
		}
		
		GdeDAOFactory.getProcuradorDAO().update(procurador);
		
	    return procurador;
	}
	
	public Procurador deleteProcurador(Procurador procurador) throws Exception {

		// Validaciones de negocio
		if (!procurador.validateDelete()) {
			return procurador;
		}
		
		GdeDAOFactory.getProcuradorDAO().delete(procurador);
		
		return procurador;
	}
	// <--- ABM Procurador	

	// ---> ABM DeudaJudicial	
	public DeudaJudicial createDeudaJudicial(DeudaJudicial deudaJudicial) throws Exception {

		// Validaciones de negocio
		if (!deudaJudicial.validateCreate()) {
			return deudaJudicial;
		}

		GdeDAOFactory.getDeudaJudicialDAO().update(deudaJudicial);

		return deudaJudicial;
	}
	
	public DeudaJudicial updateDeudaJudicial(DeudaJudicial deudaJudicial) throws Exception {
		
		// Validaciones de negocio
		if (!deudaJudicial.validateUpdate()) {
			return deudaJudicial;
		}
		
		GdeDAOFactory.getDeudaJudicialDAO().update(deudaJudicial);
		
	    return deudaJudicial;
	}
	
	public DeudaJudicial deleteDeudaJudicial(DeudaJudicial deudaJudicial) throws Exception {

		// Validaciones de negocio
		if (!deudaJudicial.validateDelete()) {
			return deudaJudicial;
		}
		
		GdeDAOFactory.getDeudaJudicialDAO().delete(deudaJudicial);
		
		return deudaJudicial;
	}
	// <--- ABM DeudaJudicial	
	

	// ---> ABM PlaEnvDeuPro	
	/**
	 * Crea la planilla de envio a procuradores y graba el historico del estado EMITIDA 
	 */
	public PlaEnvDeuPro createPlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) throws Exception {

		// Validaciones de negocio
		if (!plaEnvDeuPro.validateCreate()) {
			return plaEnvDeuPro;
		}

		GdeDAOFactory.getPlaEnvDeuProDAO().update(plaEnvDeuPro);
		grabarHistoricoEstado(plaEnvDeuPro, HistEstPlaEnvDP.getLogEstado(EstPlaEnvDeuPr.ID_EMITIDA));
		return plaEnvDeuPro;
	}
	
	/**
	 * Actualiza la planilla de envio a procuradores y graba el historico del estado correspondiente 
	 */
	public PlaEnvDeuPro updatePlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) throws Exception {
		
		updatePlaEnvDeuPro(plaEnvDeuPro, HistEstPlaEnvDP.getLogEstado(plaEnvDeuPro.getEstPlaEnvDeuPr().getId()));
		return plaEnvDeuPro;
	}
	
	/**
	 * Actualiza la planilla de envio a procuradores y graba el historico del estado con el logEstado pasado como parametro 
	 */
	public PlaEnvDeuPro updatePlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro, String logEstado) throws Exception {
		
		// Validaciones de negocio
		if (!plaEnvDeuPro.validateUpdate()) {
			return plaEnvDeuPro;
		}

		GdeDAOFactory.getPlaEnvDeuProDAO().update(plaEnvDeuPro);		
		grabarHistoricoEstado(plaEnvDeuPro, logEstado);
		return plaEnvDeuPro;
	}
	
	public PlaEnvDeuPro deletePlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) throws Exception {
	
		// Validaciones de negocio
		if (!plaEnvDeuPro.validateDelete()) {
			return plaEnvDeuPro;
		}
		
		GdeDAOFactory.getPlaEnvDeuProDAO().delete(plaEnvDeuPro);
		
		return plaEnvDeuPro;
	}
	
	/**
	 * Graba un historico estado(HisEstPlaEnvDP) para la planilla pasa como parametro, con el log pasado como parametro
	 * @param  PlaEnvDeuPro plaEnvDeuPro
	 * @param  String logEstado
	 */
	public void grabarHistoricoEstado(PlaEnvDeuPro plaEnvDeuPro, String logEstado){
		HistEstPlaEnvDP historico = new HistEstPlaEnvDP();
		historico.setEstPlaEnvDeuPr(plaEnvDeuPro.getEstPlaEnvDeuPr());
		historico.setPlaEnvDeuPro(plaEnvDeuPro);
		historico.setFechaDesde(new Date());
		historico.setLogEstado(logEstado);
		GdeDAOFactory.getHistEstPlaEnvDPDAO().update(historico);
	}		
	// <--- ABM PlaEnvDeuPro
	
	// ---> ABM ConstanciaDeu
	
	/**
	 * Realiza la creacion de la Constancia de la Deuda y la creacion del Historico de la misma.<br>
	 * El estado de la constancia se obtiene de constanciaDeu.getEstConDeu();
	 * @return ConstanciaDeu
	 * @throws Exception
	 */
	public ConstanciaDeu createConstanciaDeu(ConstanciaDeu constanciaDeu, Long idEstConDeu) throws Exception{
		// Validaciones de negocio
		if (!constanciaDeu.validateCreate()) {
			return constanciaDeu;
		}

		GdeDAOFactory.getConstanciaDeuDAO().update(constanciaDeu);
		
		// creacion del historico del estado de la constancia de deuda 
		grabarHistoricoEstado(constanciaDeu, HistEstConDeu.getLogEstado(idEstConDeu));
		//histEstConDeu.addErrorMessages(constanciaDeu);
		
		return constanciaDeu;
		}
	
	/**
	 * Realiza la creacion de la Constancia de la Deuda y la creacion del Historico de la misma con estado EMITIDA
	 * @return ConstanciaDeu
	 * @throws Exception
	 */
	public ConstanciaDeu createConstanciaDeu(ConstanciaDeu constanciaDeu) throws Exception{
		// Validaciones de negocio
		if (!constanciaDeu.validateCreate()) {
			return constanciaDeu;
		}

		GdeDAOFactory.getConstanciaDeuDAO().update(constanciaDeu);
		
		// creacion del historico del estado de la constancia de deuda 
		grabarHistoricoEstado(constanciaDeu, HistEstConDeu.getLogEstado(EstConDeu.ID_EMITIDA));
		//histEstConDeu.addErrorMessages(constanciaDeu);
		
		return constanciaDeu;
		}

	/**
	 * Actualiza la Constancia de deuda
	 * @param constanciaDeu
	 * @return ConstanciaDeu
	 * @throws Exception
	 */
	public ConstanciaDeu updateConstanciaDeu(ConstanciaDeu constanciaDeu) throws Exception {
		
		// Validaciones de negocio
		if (!constanciaDeu.validateUpdate()) {
			return constanciaDeu;
		}

		GdeDAOFactory.getConstanciaDeuDAO().update(constanciaDeu);
		
		// creacion del historico del estado de la constancia de deuda 
		grabarHistoricoEstado(constanciaDeu, HistEstConDeu.getLogEstado(EstConDeu.ID_MODIFICADA));

		return constanciaDeu;
	}	

	/**
	 * Actualiza la planilla de envio a procuradores y graba el historico del estado con el logEstado pasado como parametro 
	 */
	public ConstanciaDeu updateConstanciaDeu(ConstanciaDeu constanciaDeu, String logEstado) throws Exception {
		
		// Validaciones de negocio
		if (!constanciaDeu.validateUpdate()) {
			return constanciaDeu;
		}

		GdeDAOFactory.getConstanciaDeuDAO().update(constanciaDeu);
		
		// creacion del historico del estado de la constancia de deuda 
		grabarHistoricoEstado(constanciaDeu, logEstado);

		return constanciaDeu;
	}

	
	/**
	 * Elimina la Constancia de deuda
	 * @param  constanciaDeu
	 * @return ConstanciaDeu 
	 * @throws Exception
	 */
	public ConstanciaDeu deleteConstanciaDeu(ConstanciaDeu constanciaDeu) throws Exception {
		
		// Validaciones de negocio
		if (!constanciaDeu.validateDelete()) {
			return constanciaDeu;
		}

		GdeDAOFactory.getConstanciaDeuDAO().delete(constanciaDeu);
		
		// TODO ver HistEstConDeu
		return constanciaDeu;
	}	
	
	
	/**
	 * Graba un historico estado(HisEstConDeu) para la constanciaDeu pasa como parametro, con el log pasado como parametro
	 * @param  PlaEnvDeuPro plaEnvDeuPro
	 * @param  String logEstado
	 */
	public void grabarHistoricoEstado(ConstanciaDeu constanciaDeu, String logEstado){
		HistEstConDeu historico = new HistEstConDeu();
		historico.setEstConDeu(constanciaDeu.getEstConDeu());
		historico.setConstanciaDeu(constanciaDeu);
		historico.setFechaDesde(new Date());
		historico.setLogEstado(logEstado);
		GdeDAOFactory.getHistEstConDeuDAO().update(historico);
	}		
	// <--- ABM ConstanciaDeu
	
	
	// ---> ABM TraspasoDeuda	
	
	public TraspasoDeuda createTraspasoDeuda(TraspasoDeuda traspasoDeuda) throws Exception {

		// Validaciones de negocio
		if (!traspasoDeuda.validateCreate()) {
			log.debug("error en las validaciones de creacion del TraspasoDeuda");
			return traspasoDeuda;
		}
		
		GdeDAOFactory.getTraspasoDeudaDAO().update(traspasoDeuda);
		
		return traspasoDeuda;
	}
	
	public TraspasoDeuda updateTraspasoDeuda(TraspasoDeuda traspasoDeuda) throws Exception {
		
		// Validaciones de negocio
		if (!traspasoDeuda.validateUpdate()) {
			return traspasoDeuda;
		}
		
		GdeDAOFactory.getTraspasoDeudaDAO().update(traspasoDeuda);
		
	    return traspasoDeuda;
	}
	
	public TraspasoDeuda deleteTraspasoDeuda(TraspasoDeuda traspasoDeuda) throws Exception {

		// Validaciones de negocio
		if (!traspasoDeuda.validateDelete()) {
			return traspasoDeuda;
		}
		
		// borrar envio
		GdeDAOFactory.getTraspasoDeudaDAO().delete(traspasoDeuda);
		
		return traspasoDeuda;
	}
	// <--- ABM TraspasoDeuda

	// ---> ABM DevolucionDeuda	
	
	public DevolucionDeuda createDevolucionDeuda(DevolucionDeuda devolucionDeuda) throws Exception {

		// Validaciones de negocio
		if (!devolucionDeuda.validateCreate()) {
			log.debug("error en las validaciones de creacion de la DevolucionDeuda");
			return devolucionDeuda;
		}
		
		GdeDAOFactory.getDevolucionDeudaDAO().update(devolucionDeuda);
		
		return devolucionDeuda;
	}
	
	public DevolucionDeuda updateDevolucionDeuda(DevolucionDeuda devolucionDeuda) throws Exception {
		
		// Validaciones de negocio
		if (!devolucionDeuda.validateUpdate()) {
			return devolucionDeuda;
		}
		
		GdeDAOFactory.getDevolucionDeudaDAO().update(devolucionDeuda);
		
	    return devolucionDeuda;
	}
	
	public DevolucionDeuda deleteDevolucionDeuda(DevolucionDeuda devolucionDeuda) throws Exception {

		// Validaciones de negocio
		if (!devolucionDeuda.validateDelete()) {
			return devolucionDeuda;
		}
		
		// borrar envio
		GdeDAOFactory.getDevolucionDeudaDAO().delete(devolucionDeuda);
		
		return devolucionDeuda;
	}
	// <--- ABM DevolucionDeuda

	// --->ADM Gestiones Judiciales
	/**
	 * Realiza la creacion de la gestion judicial y la creacion del Historico de la misma con el estado que posea y el log pasado como parametro
	 * @throws Exception 
	 * 
	 */
	public GesJud createGesJud(GesJud gesJud, String logHistorico) throws Exception {
		// Validaciones de negocio
		if (!gesJud.validateCreate()) {
			return gesJud;
		}

		GdeDAOFactory.getGesJudDAO().update(gesJud);
		
		// creacion del historico del estado de la gestion Judicial 
		grabarHistoricoEstado(gesJud, logHistorico);
		return gesJud;
	}
	
	/**
	 * Actualiza la gestion judicial pasada como parametro y graba el historico estado con el log pasado como parametro
	 * @param gesJud
	 * @param logHistorico
	 * @return
	 * @throws Exception
	 */
	public GesJud updateGesjud(GesJud gesJud, String logHistorico) throws Exception {
		// Validaciones de negocio
		if (!gesJud.validateUpdate()) {
			return gesJud;
		}

		GdeDAOFactory.getGesJudDAO().update(gesJud);
		
		// creacion del historico del estado de la gestion Judicial 
		grabarHistoricoEstado(gesJud, logHistorico);
		return gesJud;
	}

	public GesJud deleteGesjud(GesJud gesJud) throws Exception {
		// Validaciones de negocio
		if (!gesJud.validateDelete()) {
			return gesJud;
		}

		GdeDAOFactory.getGesJudDAO().delete(gesJud);
		
		return gesJud;
	}

	/**
	 * Graba un historico estado(HistGesJud) para gestion judicial pasa como parametro, con el log pasado como parametro
	 * @param  GesJud gesJud
	 * @param  String logEstado
	 * @throws Exception 
	 */
	public void grabarHistoricoEstado(GesJud gesJud, String logEstado) throws Exception{
		HistGesJud historico = new HistGesJud();
		historico.setDescripcion(logEstado);
		historico.setFecha(new Date());
		historico.setGesJud(gesJud);
		gesJud.createHistGesJud(historico);		
	}		
	// <---ADM Gestiones Judiciales	


	// ---> ABM LiqCom
	/**
	 * Crea una LiqCom. Ademas crea una corrida con los parametros y se la asigna
	 */
	public LiqCom createLiqCom(LiqCom liqCom) throws Exception {
		if (!liqCom.validateCreate()) {
			return liqCom;
		}

		// Validaciones de Negocio 
		Long idProcurador = liqCom.getProcurador()!=null?liqCom.getProcurador().getId():-1L;
		if(liqCom.getRecurso() != null){
			Long idRecurso = liqCom.getRecurso().getId();
			List<LiqCom> listLiqCom = LiqCom.getByRecursoProcuradorSinTerminar(idRecurso, idProcurador, liqCom.getId());
			if (listLiqCom!=null && !listLiqCom.isEmpty()) {
				liqCom.addRecoverableError(GdeError.LIQCOM_YA_EXISTE_LIQCOM);
				return liqCom;
			}			
		}else{
			List<Recurso> listRecurso = liqCom.getServicioBanco().getListRecursoVigenteQueEnviaJudicial();
			for(Recurso recurso: listRecurso){
				Long idRecurso = recurso.getId();
				List<LiqCom> listLiqCom = LiqCom.getByRecursoProcuradorSinTerminar(idRecurso, idProcurador, liqCom.getId());
				if (listLiqCom!=null && !listLiqCom.isEmpty()) {
					liqCom.addRecoverableError(GdeError.LIQCOM_YA_EXISTE_LIQCOM);
					return liqCom;
				}	
			}
		}

		// genera una corrida
		AdpRun run = AdpRun.newRun(Proceso.PROCESO_LIQ_COM, 
				"Corrida de Liquidacion de Comisiones - Fecha Creacion:" + new Date());
			run.create();
			
			Corrida corrida = Corrida.getByIdNull(run.getId());
	        
	        if(corrida == null){
	        	log.error("no se pudo obtener la Corrida creada");
	        	liqCom.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CORRIDA_LABEL);
				return liqCom;
			}
	        
        // le seteo la corrida a la liquidacion
	    log.debug("paso actual corrida:"+corrida.getPasoActual());
        liqCom.setCorrida(corrida);			
		
		
		// graba liqCom
		GdeDAOFactory.getLiqComDAO().update(liqCom);

		// carga de parametros para adp
		run.putParameter(LiqCom.ID_LIQCOM, liqCom.getId().toString());
		
		return liqCom;
	}
	
	/**
	 * 
	 */
	public LiqCom updateLiqCom(LiqCom liqCom) throws Exception {
		if (!liqCom.validateUpdate()) {
			return liqCom;
		}

		// Validaciones de Negocio
		Long idProcurador = liqCom.getProcurador()!=null?liqCom.getProcurador().getId():-1L;
		if(liqCom.getRecurso() != null){
			Long idRecurso = liqCom.getRecurso().getId();
			List<LiqCom> listLiqCom = LiqCom.getByRecursoProcuradorSinTerminar(idRecurso, idProcurador, liqCom.getId());
			if (listLiqCom!=null && !listLiqCom.isEmpty()) {
				liqCom.addRecoverableError(GdeError.LIQCOM_YA_EXISTE_LIQCOM);
				return liqCom;
			}			
		}else{
			List<Recurso> listRecurso = liqCom.getServicioBanco().getListRecursoVigenteQueEnviaJudicial();
			for(Recurso recurso: listRecurso){
				Long idRecurso = recurso.getId();
				List<LiqCom> listLiqCom = LiqCom.getByRecursoProcuradorSinTerminar(idRecurso, idProcurador, liqCom.getId());
				if (listLiqCom!=null && !listLiqCom.isEmpty()) {
					liqCom.addRecoverableError(GdeError.LIQCOM_YA_EXISTE_LIQCOM);
					return liqCom;
				}	
			}
		}

		
		GdeDAOFactory.getLiqComDAO().update(liqCom);
		
		return liqCom;
	}

	public LiqCom deleteLiqCom(LiqCom liqCom) throws Exception {
		// Validaciones de negocio
		if (!liqCom.validateDelete()) {
			return liqCom;
		}
		
		// Elimina losLiqComPro
		liqCom.deleteListLiqComPro();

		log.debug("Va a eliminar la liqCom");
		GdeDAOFactory.getLiqComDAO().delete(liqCom);
		
		
		return liqCom;
	}
	
	// <--- ABM LiqCom	
}
