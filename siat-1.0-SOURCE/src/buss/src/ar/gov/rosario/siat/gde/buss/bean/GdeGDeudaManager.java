//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.gde.buss.dao.DeudaDAO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Manejador del subm&oacute;dulo Gesti&oacute;n Deuda del subm&oacute;dulo Gesti&oacute;n Deuda
 * 
 * @author tecso
 *
 */
public class GdeGDeudaManager {
	
	private static Logger log = Logger.getLogger(GdeGDeudaManager.class);
	
	public static final GdeGDeudaManager INSTANCE = new GdeGDeudaManager();
	
	/**
	 * Constructor privado
	 */
	private GdeGDeudaManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static GdeGDeudaManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM DeudaAdmin	
	public DeudaAdmin createDeudaAdmin(DeudaAdmin deudaAdmin) throws Exception {

		// Validaciones de negocio
		if (!deudaAdmin.validateCreate()) {
			return deudaAdmin;
		}

		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);

		return deudaAdmin;
	}
	
	/**
	 * Crea un registro de deuda admin y sus concepto.
	 * Valida todo los datos requeridos.
	 * 
	 * @param deudaAdmin
	 * @param listDeuAdmRecCon
	 * @return
	 * @throws Exception
	 */
	public DeudaAdmin createDeudaAdmin(DeudaAdmin deudaAdmin, List<DeuAdmRecCon> listDeuAdmRecCon) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = SiatHibernateUtil.currentSession();
		
		try {
			// CodRefPag
			if(deudaAdmin.getCodRefPag() == null)
				deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_CODREFPAG);
			// Recurso
            if(deudaAdmin.getRecurso() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
            // Cuenta
            if(deudaAdmin.getCuenta() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
            // Estado Deuda
            if(deudaAdmin.getEstadoDeuda() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "Estado Deuda");
            // Via Deuda
            if(deudaAdmin.getViaDeuda() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VIADEUDA_LABEL);
            // RecClaDeu
            if(deudaAdmin.getRecClaDeu()==null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCLADEU_LABEL);
            // Fecha Emision
            if(deudaAdmin.getFechaEmision() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_FECHAEMISION);
            // Fecha Vencimiento
            if(deudaAdmin.getFechaVencimiento() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_FECHAVENCIMIENTO);
            // Esta impresa
            if(deudaAdmin.getEstaImpresa() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_ESTAIMPRESA);
            // Sistema
            if(deudaAdmin.getSistema() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.SISTEMA_LABEL);	
            // Importe Bruto	
            if(deudaAdmin.getImporteBruto() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_IMPORTEBRUTO);
            // Importe
            if(deudaAdmin.getImporte() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_IMPORTE);
            // Saldo
            if(deudaAdmin.getSaldo() == null)
            	deudaAdmin.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_SALDO);

            if (deudaAdmin.hasError())
            	return deudaAdmin;
            
            deudaAdmin.setStrConceptosByListRecCon(listDeuAdmRecCon);
			log.debug("seteamos el strConceptos");

			// 30-09-2009: Fix Bug 821
			log.debug("Seteamos el atributo de asentamiento");
			Cuenta cuenta = deudaAdmin.getCuenta();
	        if (cuenta.getRecurso().getAtributoAse() != null) {
		        String atrAseVal = cuenta.getValorAtributo(cuenta.getRecurso().getAtributoAse().getId(),new Date());
		        deudaAdmin.setAtrAseVal(atrAseVal);
	        }

   			// Creo el registro de deudaAdmin
			GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);
			session.flush();
			log.debug("creamos la deuda admin");

			// Graba la lista de DeuAdmRecCon
			for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
				deuAdmRecCon.setDeuda(deudaAdmin);
				deuAdmRecCon = deudaAdmin.createDeuAdmRecCon(deuAdmRecCon);
				
				if (deuAdmRecCon.hasError()){
					deuAdmRecCon.passErrorMessages(deudaAdmin);
					return deudaAdmin;
				}
				
				
				log.debug(funcName + " idDeuda: " + deuAdmRecCon.getDeuda().getId());
				log.debug(funcName + " idRecCon: " + deuAdmRecCon.getRecCon().getId());
			}
			log.debug("creamos los conceptos");
			session.flush();
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
        
		return deudaAdmin;
	}
	
	public DeudaAdmin createDeudaAdminFromAuxDeuda(AuxDeuda auxDeuda, Cuenta cuentaCDM, Emision emision) throws Exception {

		DeudaAdmin deudaAdmin = new DeudaAdmin();
		
		deudaAdmin.setCodRefPag(auxDeuda.getCodRefPag());
		deudaAdmin.setCuenta(cuentaCDM);
		deudaAdmin.setRecClaDeu(auxDeuda.getRecClaDeu());
		deudaAdmin.setViaDeuda(auxDeuda.getViaDeuda());
		deudaAdmin.setEstadoDeuda(auxDeuda.getEstadoDeuda());
		deudaAdmin.setAnio(auxDeuda.getAnio());
		deudaAdmin.setPeriodo(auxDeuda.getPeriodo());
		deudaAdmin.setEmision(emision);
		deudaAdmin.setFechaEmision(auxDeuda.getFechaEmision());
		deudaAdmin.setFechaVencimiento(auxDeuda.getFechaVencimiento());
		deudaAdmin.setImporte(auxDeuda.getImporte());
		deudaAdmin.setImporteBruto(auxDeuda.getImporteBruto());
		deudaAdmin.setSaldo(auxDeuda.getSaldo());
		deudaAdmin.setActualizacion(auxDeuda.getActualizacion());
		deudaAdmin.setSistema(auxDeuda.getSistema());
		deudaAdmin.setRecurso(auxDeuda.getRecurso());
		deudaAdmin.setResto(auxDeuda.getResto());
		deudaAdmin.setRepartidor(auxDeuda.getRepartidor());
		deudaAdmin.setEstaImpresa(0);

		// Validaciones de negocio
		if (!deudaAdmin.validateCreate()) {
			return deudaAdmin;
		}

		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);

		// obtener la lista de los RecCon del recurso
		//List<RecCon> listRecCon = cuentaCDM.getRecurso().getListRecCon();
		
		// crear los conceptos
		// capital
		DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
		deuAdmRecCon.setDeuda(deudaAdmin);

		// de la lista tomamos el que tiene codigo=Capital
		//RecCon recConCapital = new RecCon(); // en lugar de esto, iterar la lista y tomar capital
		
		RecCon recConCapital=null;
		String strConceptos="";
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP)) {
			recConCapital = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_CAPITAL_PAV);
			strConceptos = "<8>" + auxDeuda.getConc1() + "</8>";
		}
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG)) {
			recConCapital = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_CAPITAL_GAS);
			strConceptos = "<6>" + auxDeuda.getConc1() + "</6>";
		}
		deuAdmRecCon.setRecCon(recConCapital );
		deuAdmRecCon.setImporte(auxDeuda.getConc1());
		deuAdmRecCon.setImporteBruto(auxDeuda.getConc1());
		deuAdmRecCon.setSaldo(auxDeuda.getConc1());
		
		// update
		deudaAdmin.createDeuAdmRecCon(deuAdmRecCon);
		
		// interes
		DeuAdmRecCon deuAdmRecCon2 = new DeuAdmRecCon();
		deuAdmRecCon2.setDeuda(deudaAdmin);
		// de la lista tomamos el que tiene codigo=Capital
		RecCon recConInteres=null;
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP)) {
			recConInteres = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_INTERES_PAV);
			strConceptos += "<9>" + auxDeuda.getConc2() + "</9>";
		}
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG)) {
			recConInteres = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_INTERES_GAS);
			strConceptos += "<7>" + auxDeuda.getConc2() + "</7>";
		}
		deuAdmRecCon2.setRecCon(recConInteres);
		deuAdmRecCon2.setImporteBruto(auxDeuda.getConc2());
		deuAdmRecCon2.setImporte(auxDeuda.getConc2());
		deuAdmRecCon2.setSaldo(auxDeuda.getConc2());
		
		
		
		// update
		deudaAdmin.createDeuAdmRecCon(deuAdmRecCon2);
		
		deudaAdmin.setStrConceptosProp(strConceptos);
		deudaAdmin.setEmision(emision);
		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);
		
		return deudaAdmin;
	}
	
	public DeudaAdmin createDeudaAdminFromAuxDeuda2(AuxDeuda auxDeuda, Cuenta cuentaCDM) throws Exception {

		DeudaAdmin deudaAdmin = new DeudaAdmin();
		
		deudaAdmin.setCodRefPag(auxDeuda.getCodRefPag());
		if(cuentaCDM == null){
			deudaAdmin.setCuenta(auxDeuda.getCuenta());
		}else{
			deudaAdmin.setCuenta(cuentaCDM);
		}
		deudaAdmin.setRecClaDeu(auxDeuda.getRecClaDeu());
		deudaAdmin.setViaDeuda(auxDeuda.getViaDeuda());
		deudaAdmin.setEstadoDeuda(auxDeuda.getEstadoDeuda());
//		deudaAdmin.setServicioBanco(auxDeuda.getServicioBanco()); se quito el servicio banco de la deuda
		deudaAdmin.setAnio(auxDeuda.getAnio());
		deudaAdmin.setPeriodo(auxDeuda.getPeriodo());
		deudaAdmin.setFechaEmision(auxDeuda.getFechaEmision());
		deudaAdmin.setFechaVencimiento(auxDeuda.getFechaVencimiento());
		deudaAdmin.setImporte(auxDeuda.getImporte());
		deudaAdmin.setImporteBruto(auxDeuda.getImporteBruto());
		deudaAdmin.setSaldo(auxDeuda.getSaldo());
		deudaAdmin.setActualizacion(auxDeuda.getActualizacion());
		deudaAdmin.setSistema(auxDeuda.getSistema());
		deudaAdmin.setResto(auxDeuda.getResto());
		deudaAdmin.setRepartidor(auxDeuda.getRepartidor());
		deudaAdmin.setEstaImpresa(0);
		
		// Validaciones de negocio
		if (!deudaAdmin.validateCreate()) {
			return deudaAdmin;
		}

		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);

		// obtener la lista de los RecCon del recurso
		//List<RecCon> listRecCon = cuentaCDM.getRecurso().getListRecCon();
		
		// crear los conceptos
		// capital
		DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
		deuAdmRecCon.setDeuda(deudaAdmin);

		// de la lista tomamos el que tiene codigo=Capital
		//RecCon recConCapital = new RecCon(); // en lugar de esto, iterar la lista y tomar capital
		
		RecCon recConCapital=null;
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP))
			recConCapital = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_CAPITAL_PAV);
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG))
			recConCapital = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_CAPITAL_GAS);
		deuAdmRecCon.setRecCon(recConCapital );
		deuAdmRecCon.setImporte(auxDeuda.getConc1());
		deuAdmRecCon.setImporteBruto(auxDeuda.getConc1());
		deuAdmRecCon.setSaldo(auxDeuda.getConc1());
		
		// update
		deudaAdmin.createDeuAdmRecCon(deuAdmRecCon);
		
		// interes
		DeuAdmRecCon deuAdmRecCon2 = new DeuAdmRecCon();
		deuAdmRecCon2.setDeuda(deudaAdmin);
		// de la lista tomamos el que tiene codigo=Capital
		RecCon recConInteres=null;
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP))
			recConInteres = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_INTERES_PAV);
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG))
			recConInteres = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_INTERES_GAS);
		deuAdmRecCon2.setRecCon(recConInteres);
		deuAdmRecCon2.setImporteBruto(auxDeuda.getConc2());
		deuAdmRecCon2.setImporte(auxDeuda.getConc2());
		deuAdmRecCon2.setSaldo(auxDeuda.getConc2());
		
		// update
		deudaAdmin.createDeuAdmRecCon(deuAdmRecCon2);
		
		return deudaAdmin;
	}


	
	public DeudaAdmin updateDeudaAdmin(DeudaAdmin deudaAdmin) throws Exception {
		
		// Validaciones de negocio
		if (!deudaAdmin.validateUpdate()) {
			return deudaAdmin;
		}
		
		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);
		
	    return deudaAdmin;
	}
	
	public DeudaAdmin deleteDeudaAdmin(DeudaAdmin deudaAdmin) throws Exception {

		// Validaciones de negocio
		if (!deudaAdmin.validateDelete()) {
			return deudaAdmin;
		}
		
		GdeDAOFactory.getDeudaAdminDAO().delete(deudaAdmin);
		
		return deudaAdmin;
	}
	// <--- ABM DeudaAdmin	
	
	
	
	// ---> Administrar Deuda Reclamada (SINC)
	public DeudaAdmin marcarDeudaAdmin(DeudaAdmin deudaAdmin) throws Exception {
		
		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);
		
	    return deudaAdmin;
	}

	public DeudaJudicial marcarDeudaJudicial(DeudaJudicial deudaJudicial) throws Exception {
		
		GdeDAOFactory.getDeudaJudicialDAO().update(deudaJudicial);
		
	    return deudaJudicial;
	}
	// <--- Administrar Deuda Reclamada (SINC)
	
	// ---> Envio de Deuda a Concurso y Quiebra (SINC)
	public DeudaAdmin enviarDeudaCyq(DeudaAdmin deudaAdmin) throws Exception {
		
		
		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);
		
		log.debug("Deuda Admin Enviada a CyQ id=" + deudaAdmin.getId() );
		
	    return deudaAdmin;
	}

	public DeudaJudicial enviarDeudaCyq(DeudaJudicial deudaJudicial) throws Exception {
		
		GdeDAOFactory.getDeudaJudicialDAO().update(deudaJudicial);
		
		log.debug("Deuda Judicial Enviada a CyQ id=" + deudaJudicial.getId() );
		
	    return deudaJudicial;
	}
	// <--- Envio de Deuda a Concurso y Quiebra (SINC)
	
	// ---> ABM PagoDeuda	
	public PagoDeuda createPagoDeuda(PagoDeuda pagoDeuda) throws Exception {

		// Validaciones de negocio
		if (!pagoDeuda.validateCreate()) {
			return pagoDeuda;
		}

		GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);

		return pagoDeuda;
	}
	
	public PagoDeuda updatePagoDeuda(PagoDeuda pagoDeuda) throws Exception {
		
		// Validaciones de negocio
		if (!pagoDeuda.validateUpdate()) {
			return pagoDeuda;
		}
		
		GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);
		
	    return pagoDeuda;
	}
	
	public PagoDeuda deletePagoDeuda(PagoDeuda pagoDeuda) throws Exception {

		// Validaciones de negocio
		if (!pagoDeuda.validateDelete()) {
			return pagoDeuda;
		}
		
		GdeDAOFactory.getPagoDeudaDAO().delete(pagoDeuda);
		
		return pagoDeuda;
	}
	// <--- ABM PagoDeuda
	
	/**
	 * Ejecuta el update de un registro de deuda segun su estado. 
	 * 
	 */
	public Deuda update(Deuda deuda) throws Exception {
		
		DeudaDAO deudaDAO = new DeudaDAO(deuda.getClass());
		
		deudaDAO.update(deuda);
		
	    return deuda;
	}
	/**
	 * Llama al DAO para cambiar el Estado de una Deuda y mover el registro a su correspondiente tabla
	 * No realiza ninguna accion extra que pueda estar asociada al cambio de Estado en el Negocio,
	 * @param deuda
	 * @param estadoOrigen
	 * @param estadoDestino
	 * @throws Exception
	 */
	public void moverDeuda (Deuda deuda, EstadoDeuda estadoOrigen, EstadoDeuda estadoDestino) throws Exception{
		DeudaDAO deudaDAO = new DeudaDAO (deuda.getClass());
		deudaDAO.moverDeudaDeEstado(deuda, estadoOrigen, estadoDestino);
		
	}
	
	public Anulacion anularListDeuda(Anulacion anulacion, List<Deuda> listDeudaAAnular) throws Exception {
		
		// Validar Requedidos FechaAnulacion, Motivo, Caso, Observacion y listIdDeudaSelected
		// Recuperar la deuda seleccinada
		// Moverla a deuda anulada junto con sus conceptos 
		// 	- Borrar la deuda origen (ya lo hace duda dao).
		
		// Registrar uso caso
		
		// A la deuda anulada setearle: 
		//	el estadoDeuda corresponiente al motivo
		//  fechaAnulacion
		//  idMotAnuDeu
		//  Observacion
		//  idCaso
				
		// Por cada deuda anulada insertar un registro en anulacion.
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

			if (anulacion.getFechaAnulacion() == null){
				anulacion.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ANULACION_FECHAANULACION);
			}
			if (anulacion.getMotAnuDeu() == null){
				anulacion.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MOTANUDEU_LABEL);
			}			
			if (StringUtil.isNullOrEmpty(anulacion.getObservacion())){
				anulacion.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ANULACION_OBSERVACION);
			}
			
			if (anulacion.hasError()){
				return anulacion;
			}
			
        	
			EstadoDeuda estadoDeduaAnulada = EstadoDeuda.getById(EstadoDeuda.ID_ANULADA);
			
			// Movemos la deuda a cancelada
			for (Deuda deuda: listDeudaAAnular ){
				EstadoDeuda estadoDeudaOrig = deuda.getEstadoDeuda(); 
				moverDeuda(deuda, estadoDeudaOrig, estadoDeduaAnulada);
				log.debug(funcName + " Deuda moviva: id=" + deuda.getId() + " " + 
						estadoDeudaOrig.getDesEstadoDeuda() + " -> " + estadoDeduaAnulada.getDesEstadoDeuda());
			}
			
			// Recuperamos la deuda movida 
			List<DeudaAnulada> listDeudaAnulada = new ArrayList<DeudaAnulada>();
			
			for(Deuda deudaACancelar:listDeudaAAnular){
				Long idEstadoDeuda = EstadoDeuda.ID_ANULADA;
				
				Deuda deudaCancelada = Deuda.getById(deudaACancelar.getId(), idEstadoDeuda);
				listDeudaAnulada.add((DeudaAnulada)deudaCancelada);					
			}
			
			// Recuperamos el motivo real de anulacion
			MotAnuDeu motAnuDeu = MotAnuDeu.getById(anulacion.getMotAnuDeu().getId());
						
			Long idEstadoDeuda = null;
			if (motAnuDeu.getId().longValue() == MotAnuDeu.ID_ANULACION ||
					motAnuDeu.getId().longValue() == MotAnuDeu.ID_FALLECIMIENTO_TITULAR ||
					motAnuDeu.getId().longValue() == MotAnuDeu.ID_CAMBIOPLAN_CDM){
				idEstadoDeuda = EstadoDeuda.ID_ANULADA;	
			}
			
			if (motAnuDeu.getId().longValue() == MotAnuDeu.ID_CONDONACION)
				idEstadoDeuda = EstadoDeuda.ID_CONDONADA;
			if (motAnuDeu.getId().longValue() == MotAnuDeu.ID_PRESCRIPCION)
				idEstadoDeuda = EstadoDeuda.ID_PRESCRIPTA;
			
			
			// Recuperamos el estado de deuda real
			EstadoDeuda estadoDeudaUpdate = EstadoDeuda.getById(idEstadoDeuda); 
			
			Date fechaAnulacion = anulacion.getFechaAnulacion();
			String observacion = anulacion.getObservacion();
			String idCaso = anulacion.getIdCaso();
						
			// Actualizamos al deuda anulada (movida) 
			for (DeudaAnulada deudaAnulada:listDeudaAnulada){
				deudaAnulada.setEstadoDeuda(estadoDeudaUpdate);
				deudaAnulada.setMotAnuDeu(motAnuDeu);
				deudaAnulada.setFechaAnulacion(fechaAnulacion);
				deudaAnulada.setObservacion(observacion);
				deudaAnulada.setIdCaso(idCaso);
				deudaAnulada.setCorrida(null);
				GdeGDeudaManager.getInstance().update(deudaAnulada);
				
				// Por cada deuda anulada insertamos un registro en anulacion.
				Anulacion anulacionInsert = new Anulacion();
				anulacionInsert.setFechaAnulacion(fechaAnulacion);
				anulacionInsert.setMotAnuDeu(motAnuDeu);
				anulacionInsert.setIdDeuda(deudaAnulada.getId());
				anulacionInsert.setIdCaso(idCaso);
				anulacionInsert.setObservacion(observacion);
				anulacionInsert.setRecurso(deudaAnulada.getRecurso());
				anulacionInsert.setViaDeuda(deudaAnulada.getViaDeuda());
				
			    GdeDAOFactory.getAnulacionDAO().update(anulacionInsert);
			    
			    log.debug( funcName + " anulacion: id:" + anulacion.getId() +
			    		" fecha:" + anulacion.getFechaAnulacion() + 
			    		" idDeuda: " + anulacion.getIdDeuda());
			}
			
			log.debug(funcName + ": exit");
			return anulacion;
		
	}
	
	public Anulacion anularDeuda(Anulacion anulacion, Deuda deuda, Corrida corrida) throws Exception {
		EstadoDeuda estDeuOrigen  = deuda.getEstadoDeuda();
    	EstadoDeuda estDeuAnulada = EstadoDeuda.getById(EstadoDeuda.ID_ANULADA);
		GdeGDeudaManager.getInstance().moverDeuda(deuda, estDeuOrigen, estDeuAnulada);
			
		DeudaAnulada deudaAnulada =
			(DeudaAnulada) Deuda.getById(deuda.getId(), EstadoDeuda.ID_ANULADA);
									
		// Recuperamos el motivo real de anulacion
		MotAnuDeu motAnuDeu = anulacion.getMotAnuDeu();
		Long idEstDeu = null;
		/*if (motAnuDeu.getId().equals(MotAnuDeu.ID_ANULACION)||
			motAnuDeu.getId().equals(MotAnuDeu.ID_FALLECIMIENTO_TITULAR )||
			motAnuDeu.getId().equals(MotAnuDeu.ID_DEGLOSE_AJUSTE)||
			motAnuDeu.getId().equals(MotAnuDeu.ID_CAMBIOPLAN_CDM)){
			idEstDeu = EstadoDeuda.ID_ANULADA;	
		}*/
		
		if (motAnuDeu.getId().equals(MotAnuDeu.ID_CONDONACION)) {
			idEstDeu = EstadoDeuda.ID_CONDONADA;
		} else if (motAnuDeu.getId().longValue() == MotAnuDeu.ID_PRESCRIPCION) {
			idEstDeu = EstadoDeuda.ID_PRESCRIPTA;
		} else {
			idEstDeu = EstadoDeuda.ID_ANULADA;
		}
			
		EstadoDeuda estDeu = EstadoDeuda.getById(idEstDeu); 
			
		Date fechaAnulacion = anulacion.getFechaAnulacion();
		String observacion = anulacion.getObservacion();
		String idCaso = anulacion.getIdCaso();
		
		deudaAnulada.setMotAnuDeu(motAnuDeu);
		deudaAnulada.setFechaAnulacion(fechaAnulacion);
		deudaAnulada.setObservacion(observacion);
		deudaAnulada.setIdCaso(idCaso);
		deudaAnulada.setEstadoDeuda(estDeu);
		deudaAnulada.setCorrida(corrida);
		
		GdeDAOFactory.getDeudaAnuladaDAO().update(deudaAnulada);
		
		// Se comenta el siguiente codigo porque creaba otro registro de Anulacion. Se deja el update por si no se hizo antes de llamar al anular deuda.
		// Se deberia eliminar esta parte del codigo.
		
		//Anulacion anulacionInsert = new Anulacion();
		//anulacionInsert.setFechaAnulacion(fechaAnulacion);
		//anulacionInsert.setMotAnuDeu(motAnuDeu);
		//anulacionInsert.setIdDeuda(deudaAnulada.getId());
		//anulacionInsert.setIdCaso(idCaso);
		//anulacionInsert.setObservacion(observacion);
		//anulacionInsert.setRecurso(deudaAnulada.getRecurso());
		//anulacionInsert.setViaDeuda(deudaAnulada.getViaDeuda());
		
	    //GdeDAOFactory.getAnulacionDAO().update(anulacionInsert);
	    GdeDAOFactory.getAnulacionDAO().update(anulacion);
			    
		return anulacion;
	}

	
	// ---> Control Informe Deuda Escribano
	public CtrlInfDeu createCtrlInfDeu(CtrlInfDeu ctrlInfDeu) throws Exception{
		
		// Validaciones de negocio
		if (!ctrlInfDeu.validateCreate()) {
			return ctrlInfDeu;
		}
		
		GdeDAOFactory.getCtrlInfDeuDAO().update(ctrlInfDeu);
		
	    return ctrlInfDeu;
	}
	
	public CtrlInfDeu deleteCtrlInfDeu(CtrlInfDeu ctrlInfDeu) throws Exception {
		
		// Validaciones de negocio
		if (!ctrlInfDeu.validateDelete()) {
			return ctrlInfDeu;
		}
		
		GdeDAOFactory.getCtrlInfDeuDAO().delete(ctrlInfDeu);
		
		return ctrlInfDeu;
	}
	// <--- Control Informe Deuda Escribano

	// ---> Respaldo de registros de Control Informe Deuda Escribano desbloqueados
	public HisInfDeu createHisInfDeu(HisInfDeu hisInfDeu) throws Exception{
		
		// Validaciones de negocio
		if (!hisInfDeu.validateCreate()) {
			return hisInfDeu;
		}
		
		GdeDAOFactory.getHisInfDeuDAO().update(hisInfDeu);
		
	    return hisInfDeu;
	}
	
	public HisInfDeu deleteHisInfDeu(HisInfDeu hisInfDeu) throws Exception {
		
		// Validaciones de negocio
		if (!hisInfDeu.validateDelete()) {
			return hisInfDeu;
		}
		
		GdeDAOFactory.getHisInfDeuDAO().delete(hisInfDeu);
		
		return hisInfDeu;
	}
	// <---  Respaldo de registros de Control Informe Deuda Escribano desbloqueados

	// ---> DecJur
	public DecJur deleteDecJur(DecJur decJur) throws Exception {
		
		// Validaciones de negocio
		if (!decJur.validateDelete()) {
			return decJur;
		}
		
		GdeDAOFactory.getDecJurDAO().delete(decJur);
		
		return decJur;
	}
	// <--- DecJur
	/**
	 * Crea una deuda admin periodo original en cero
	 */
	public DeudaAdmin getDeudaAdminOrigAutoLiquidable(Cuenta cuenta, Vencimiento vencimiento, Date fechaEmitir){
		DeudaAdmin deudaAdmin=new DeudaAdmin();
		
		
			
		
		RecClaDeu recClaDeu =cuenta.getRecurso().getRecClaDeuOriginal(fechaEmitir);
		Date fechaVencimiento=Vencimiento.getFechaVencimiento(fechaEmitir, vencimiento.getId());
		
		deudaAdmin.setRecClaDeu(recClaDeu);
		deudaAdmin.setImporteBruto(0D);
		deudaAdmin.setImporte(0D);
		deudaAdmin.setActualizacion(0D);
		deudaAdmin.setSaldo(0D);
		deudaAdmin.setPeriodo(new Long(DateUtil.getMes(fechaEmitir)));
		deudaAdmin.setAnio(new Long(DateUtil.getAnio(fechaEmitir)));
		deudaAdmin.setFechaVencimiento(fechaVencimiento);
		deudaAdmin.setRecurso(cuenta.getRecurso());
		if(cuenta.getRecurso().getAtributoAse()!=null)
			deudaAdmin.setAtrAseVal("0");
		
		deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
        deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
        deudaAdmin.setFechaEmision(new Date());
        deudaAdmin.setEstaImpresa(SiNo.NO.getId());
        deudaAdmin.setSistema(Sistema.getSistemaEmision(cuenta.getRecurso())); 
		deudaAdmin.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());
		
        deudaAdmin.setCuenta(cuenta);
        deudaAdmin.setReclamada(SiNo.NO.getId());
        deudaAdmin.setResto(0L);
        deudaAdmin.setEstado(Estado.ACTIVO.getId());
		return deudaAdmin;
	}
	
	public List<DeuAdmRecCon>getListDeuAdmRecConForDeuAuto(DeudaAdmin deudaAdmin){
		
		List<DeuAdmRecCon>listDeuAdmRecCon=new ArrayList<DeuAdmRecCon>();
		for(RecCon recCon: deudaAdmin.getRecurso().getListRecCon()){

	        DeuAdmRecCon deuAdmRecConNuevo = new DeuAdmRecCon();
        	
        	deuAdmRecConNuevo.setDeuda(deudaAdmin);
        	deuAdmRecConNuevo.setRecCon(recCon); 
        	
        	Double porcentajeRecCon = deuAdmRecConNuevo.getRecCon().getPorcentaje();
			Double importeDeuRecCon=NumberUtil.round(deudaAdmin.getImporte() * porcentajeRecCon, SiatParam.DEC_IMPORTE_DB);
			deuAdmRecConNuevo.setImporte(importeDeuRecCon);
			deuAdmRecConNuevo.setImporteBruto(importeDeuRecCon);
			deuAdmRecConNuevo.setSaldo(0D);
			
            listDeuAdmRecCon.add(deuAdmRecConNuevo);
       }
		
		return listDeuAdmRecCon;
	}
	
	public Cuenta createDeudaAdminFromCreateCuentaObjImp(Cuenta cuenta) throws Exception {
		
		Recurso recurso = cuenta.getRecurso();
		Date fechaUltPerEmi= DateUtil.getDate("01/"+recurso.getUltPerEmi().substring(4,6)+"/"+recurso.getUltPerEmi().substring(0, 4), DateUtil.ddSMMSYYYY_MASK);
		Date fechaPriPerEmi=cuenta.getFechaAlta();

        Calendar gcalendar = new GregorianCalendar(); 
        // Obtengo anio de prescripcion [ year(today) - 6 years]
        gcalendar.setTime(new Date()); 
        int anioPrescripcion = gcalendar.get(Calendar.YEAR) - 6;
        
        // Obtengo anio de inicio de actividad
        gcalendar.setTime(fechaPriPerEmi);
        int anioInicioActividad = gcalendar.get(Calendar.YEAR);
        
        if (anioInicioActividad < anioPrescripcion) {
			// Emitir a partir del "01/01/AnioPrescripcion"
        	fechaPriPerEmi = DateUtil.getDate("01/01/"+anioPrescripcion);
		}
		
		List<Date> listFechaEmision=DateUtil.getListFirstDayEachMonth(fechaPriPerEmi, fechaUltPerEmi);
		log.debug("Fecha inicio: "+DateUtil.formatDate(fechaPriPerEmi, DateUtil.ddSMMSYY_MASK)+ ", Fecha ult perEmi: "+DateUtil.formatDate(fechaUltPerEmi, DateUtil.ddSMMSYYYY_MASK));
		Vencimiento vencimiento = recurso.getVencimiento();
		if (vencimiento==null){
			cuenta.addRecoverableValueError("El Recurso "+cuenta.getRecurso().getDesRecurso()+" no posee definido un vencimiento");
			return cuenta;
		}
		
		if(!ListUtil.isNullOrEmpty(listFechaEmision)){
			for (Date fechaPeriodo : listFechaEmision){
				DeudaAdmin deudaAdmin = getDeudaAdminOrigAutoLiquidable(cuenta, vencimiento, fechaPeriodo);
				List<DeuAdmRecCon>listDeuAdmRecCon=getListDeuAdmRecConForDeuAuto(deudaAdmin);
				deudaAdmin = createDeudaAdmin(deudaAdmin, listDeuAdmRecCon);
				if(deudaAdmin.hasError()){
					deudaAdmin.passErrorMessages(cuenta);
					break;
				}
				
			}
		}
		

				
		return cuenta;
	}
}
