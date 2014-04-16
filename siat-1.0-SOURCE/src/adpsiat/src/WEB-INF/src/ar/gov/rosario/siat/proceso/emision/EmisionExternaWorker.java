//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.emision;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;

/**
 * Worker del Proceso de Emision Masiva de Deuda
 * 
 * @author Tecso Coop. Ltda.
 */
public class EmisionExternaWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(EmisionExternaWorker.class);

	public void execute(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Long pasoActual = run.getPasoActual();
		log.debug("Ejecutando paso " + pasoActual);
		try {
			String infoMsg = "";
			
			if (pasoActual.equals(1L)) {
				ejecutarPaso1(run);
				infoMsg = "La deuda administrativa ha sido generada " +
				  		  "exitosamente";
				run.changeState(AdpRunState.FIN_OK, infoMsg);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		} catch (Exception e) {			
			String infoError = "Ocurrieron errores durante la ejecucion del " + 
							   "paso " + pasoActual + ". Consulte los logs.";
			run.addError(infoError + ": " + e.getMessage());
			// Cambiamos el estado a: FINALIZADO CON ERROR e informamos al usuario
			run.changeState(AdpRunState.FIN_ERROR, infoError);
		}
	}

	private void ejecutarPaso1(AdpRun run) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx =  null;
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			// Obtenemos la Emision
			Long idEmision  = Long.parseLong(run.getParameter(Emision.ADP_PARAM_ID));
			Emision emision = Emision.getById(idEmision);
			
			// Generamos la deuda administrativa
			this.generarDeudaAdmin(run, emision);

			if(emision.hasError()){
 				// borrado de los registros DeuAdmRecCon	
 				int deuAdmRecConBorrados = GdeDAOFactory.getDeuAdmRecConDAO().deleteListDeuAdmRecConByEmision(emision);
 				log.debug("registros borrados de DeuAdmRecCon: " + deuAdmRecConBorrados);
 				// borrado DeudaAdmin creados
 				int regBorrados = GdeDAOFactory.getDeudaAdminDAO().deleteListDeudaAdminByEmision(emision);
 				log.debug("registros borrados de la DeudaAdmin: " + regBorrados);

 				String descripcion = "Error al generar los registros de Deuda Administrativa. Se limpiaron todos los registros de Deuda y Conceptos de esta Emision ";
				run.changeState(AdpRunState.FIN_ERROR, descripcion, false);
				run.logError(descripcion);
	 		}else{
	 			// borrado de los registros auxdeu de la emision
	 			int regBorrados = EmiDAOFactory.getAuxDeudaDAO().deleteAuxDeudaByIdEmision(emision);
	 			log.debug("registros borrados de la AuxDeuda: " + regBorrados);
				// cambiar estado, incremetar paso, y actualiza el pasoCorrida (que fue creado cuando se activo (esqueduleo)) 
				run.changeState(AdpRunState.FIN_OK, "Registros de Deuda Administrativas generados exitosamente", true); 
	 		}			
			
	 		// Si o si realiza el commit.
			SiatHibernateUtil.currentSession().getTransaction().commit();

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		} catch (Exception e) {
			log.error("Ocurrio una excepcion durante la ejecucion del paso",  e);
			tx = SiatHibernateUtil.currentSession().getTransaction();
		    try { if(tx != null) tx.rollback(); } catch (Exception ex) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();			
		}
	}

	private void generarDeudaAdmin(AdpRun adpRun, Emision emision) throws Exception {
		//mapa de las cuentas a las que se le genero deuda
 		Map<String, Long> cuentas = new HashMap<String, Long>(); 

 		// Inicializamos el mapa de conceptos de la emision
 		emision.initializeMapCodRecCon();

 		// cada 2500 registros hace commit y beginTransaction. 
		// No flush y clear
		int first = 0;
		int pageSize = 2500;
 		boolean contieneAuxDeuda = true;
 		while (contieneAuxDeuda) {
 			// obtencion de la lista de pares AuxDeuda-NumeroCuenta paginada
  			List<Object[]> listAuxDeuda = AuxDeuda.getListAuxDeudaByIdEmision(emision.getId(), first, pageSize);
  			
  			contieneAuxDeuda = (listAuxDeuda.size() > 0);
 			
 			for (Object[] datos : listAuxDeuda) {
 				AuxDeuda auxDeuda = (AuxDeuda) datos[0];
 				String nroCuenta  = (String) datos[1];
 				AdpRun.changeRunMessage("Cuentas procesadas: " + first, 30);
 				DeudaAdmin deudaAdmin = emision.createDeudaAdminFromAuxDeuda(auxDeuda);
 				if (deudaAdmin.hasError()) {
	     			String descripcion = "Error al crear el registro de deudaAdmin a partir de la auxDeuda: " + auxDeuda.getId();
	     			log.error(descripcion);
	     			AdpRun.logRun(descripcion);
	     			emision.addRecoverableValueError(descripcion);
	     			auxDeuda.addErrorMessages(emision);
	     		} else {
	     			Long periodoGrabado = cuentas.get(nroCuenta);
	     			if (periodoGrabado == null) {
	     				int bimestre = SiatUtil.calcularBimestre(deudaAdmin.getPeriodo().intValue());
	     				cuentas.put(nroCuenta, deudaAdmin.getAnio()*100 + bimestre);
	     			}
	     		}
 			}
 			
 			first += pageSize; // paginacion
 			
 			SiatHibernateUtil.currentSession().getTransaction().commit();
			SiatHibernateUtil.closeSession();

			SiatHibernateUtil.currentSession();
 			SiatHibernateUtil.currentSession().beginTransaction();
 		}

 	}

	public void cancel(AdpRun adpRun) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void reset(AdpRun adpRun) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	
}