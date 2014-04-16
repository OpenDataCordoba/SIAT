//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.rec;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.bean.BalEnvioOsirisManager;
import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.bean.EstadoEnvio;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.UserContext;

public class NovOsiris {
	
	static Logger log = Logger.getLogger(NovOsiris.class);
	
	
	public void getEnvios(AdpRun run) throws Exception {	
		UserContext userContext = new UserContext();
		userContext.setUserName("siat");
		userContext.setIdUsuarioSiat(UsuarioSiat.ID_USUARIO_SIAT);
		String msg="";

		
		try{
			SiatHibernateUtil.currentSession().beginTransaction();
			
			Date fechaUltRegistroMulat = BalDAOFactory.getEnvioOsirisDAO().getUltimaFechaRegistroMulat();
			if(fechaUltRegistroMulat == null){
				// Si no encuentra registros en la tabla de envios, verifico de un mes hacia atras.
				fechaUltRegistroMulat= DateUtil.getDate(2009, 11, 1); //DateUtil.addMonthsToDate(new Date(), -1);			
			}
			
			run.logDebug("Se buscan envios a desde la fecha: "+DateUtil.formatDateForReport(fechaUltRegistroMulat));
			//Busco envios nuevos
			List<EnvioOsiris> listEnvioOsiris = BalDAOFactory.getMulatorJDBCDAO().getListNuevosEnviosByFechaMayorIgual(fechaUltRegistroMulat);
			
			//si esta definido este parametro solo procesamos los envios validos que estan 
			//en el rango del parametro OBTENERENVIO_ENVIOID con formato min:max
			String findeos = SiatParam.getString(SiatParam.OBTENERENVIO_ENVIOID, "");
			if (!findeos.equals("")) {
				List<EnvioOsiris> ltmp = new ArrayList<EnvioOsiris>();
				String[] ids = findeos.split(":");
				long idmin = Long.valueOf(ids[0]);
				long idmax = Long.valueOf(ids[1]);
				log.debug("Obtener EnvioOsiris: " + findeos + " de lista.size():" + listEnvioOsiris.size());
				for(EnvioOsiris eo : listEnvioOsiris) {
					long id = eo.getIdEnvioAfip();
					if (idmin <= id && id <= idmax) {
						ltmp.add(eo);
					} else {
						log.debug("Obtener EnvioOsiris: id " + id + " se descarta. por fuera de rango: " + findeos);
					}
					
				}
				listEnvioOsiris = ltmp;
				for(EnvioOsiris eo : listEnvioOsiris) {	log.debug("ObtenerEnvio: id:" + eo.getIdEnvioAfip()); }
			}
			
			run.logDebug("Cantidad de Envios encontrados: "+listEnvioOsiris.size());
			log.debug("Lista de Envios: "+listEnvioOsiris.size());
			boolean errores=false;

			// agregamos los envios inconsistentes
			// listEnvioOsiris.addAll(EnvioOsiris.getListInconsistentes()); TODO Verificar si al encontrarse un envio inconsistente Mulat corrige el mismo o envia uno nuevo!!!!!!!!!!
			// La linea anterior se debe habilitar si al detectar un envio inconsistente AFIP corrige el mismo registro en lugar de enviar uno nuevo. 			
			for (EnvioOsiris envioOsiris: listEnvioOsiris){
				
				/* desactivamos las validaciones, porque los envios que mandan suelen no ser consistentes */
				//BalDAOFactory.getMulatorJDBCDAO().validateConsistenciaMulator(envioOsiris);
    			envioOsiris.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PENDIENTE));
    			
    			String observaciones = DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
    			observaciones +="\n- Validaciones realizadas con Exito";
    			envioOsiris.setObservacion(observaciones);
				
				BalEnvioOsirisManager.getInstance().createEnvioOsiris(envioOsiris);
				run.logDebug("Obteniendo Envio de id = "+envioOsiris.getIdEnvioAfip()+" , Estado Envio = "+envioOsiris.getEstadoEnvio().getDesEstado());
				SiatHibernateUtil.currentSession().flush();
				if(envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_PENDIENTE)
					envioOsiris = getEnvioCompleto(envioOsiris);
					
				if (envioOsiris.hasError()){
					run.logDebug("Envio con Error. No se pudo completar la obtención de datos.");
					errores=true;
					break;
				}
				
				//aplico las novedades de rechazo de cheques para los cierreBanco del envio
				BalEnvioOsirisManager.getInstance().aplicarNovedadRechazoCheque(envioOsiris);
				
				//determino si un envio se obtuvo en forma concistente 
				if (!envioOsiris.getEsConcistente()) {
					// marco envioOsiris como inconsistente
	    			envioOsiris.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_INCONSISTENTE));
				} else if (envioOsiris.getCantTranPago().equals(0L)) {
					//si envioOsiris no posee TranAfip que inserten Pago, lo paso directamente a Conciliado 
					envioOsiris.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_CONCILIADO));
				}
				
				run.logDebug("El envio se obtuvo sin errores.");
				
				//cada 1 envios commit y reset de la trasaccion de Hibernate
				//suele haber envios de hasta 3000 transacciones y c/u puede tener hasta 27 detalles de pagos.
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction();
			}
			
			if(!errores){
				SiatHibernateUtil.currentSession().getTransaction().commit();
				msg = "Proceso de Novedad de Envios, Se realizo con Exito";
				run.changeStateFinOk(msg);				
			}else{
				msg = "Proceso de Novedad con Errores";
				SiatHibernateUtil.currentSession().getTransaction().rollback();
			}
			
		} catch (Exception e) {
			msg += "Error Grave: " + e.toString();
			run.logError(msg, e);
			run.changeStateError(msg, e);			
			SiatHibernateUtil.currentSession().getTransaction().rollback();
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	
	public EnvioOsiris getEnvioCompleto(EnvioOsiris envioOsiris) throws Exception{
		
		envioOsiris = BalDAOFactory.getMulatorJDBCDAO().getEnvioCompleto(envioOsiris);
		
		return envioOsiris;
	}
	
}
