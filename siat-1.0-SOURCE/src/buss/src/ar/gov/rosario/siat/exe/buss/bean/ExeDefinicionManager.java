//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.iface.helper.DemodaUtil;

/**
 * Manejador del m&oacute;dulo Exencion y submodulo Definicion
 * 
 * @author tecso
 *
 */
public class ExeDefinicionManager {
		
	private static Logger log = Logger.getLogger(ExeDefinicionManager.class);
	
	private static final ExeDefinicionManager INSTANCE = new ExeDefinicionManager();
	
	/**
	 * Constructor privado
	 */
	private ExeDefinicionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static ExeDefinicionManager getInstance() {
		return INSTANCE;
	}

	//	---> ABM Exencion
	public Exencion createExencion(Exencion exencion) throws Exception {

		// Validaciones de negocio
		if (!exencion.validateCreate()) {
			return exencion;
		}

		ExeDAOFactory.getExencionDAO().update(exencion);

		return exencion;
	}
	
	public Exencion updateExencion(Exencion exencion) throws Exception {
		
		// Validaciones de negocio
		if (!exencion.validateUpdate()) {
			return exencion;
		}

		ExeDAOFactory.getExencionDAO().update(exencion);
		
		return exencion;
	}
	
	public Exencion deleteExencion(Exencion exencion) throws Exception {
	
		// Validaciones de negocio
		if (!exencion.validateDelete()) {
			return exencion;
		}
		
		ExeDAOFactory.getExencionDAO().delete(exencion);
		
		return exencion;
	}
	// <--- ABM Exencion
	
	//	---> ABM ContribExe
	@Deprecated
	public ContribExe createContribExe(ContribExe contribExe) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = SiatHibernateUtil.currentSession();
		
		// Obtenemos las cuentas que pueda poseer el contribuyente
		// Por cada Cuenta:
		//		Si ContribExe tiene broche, y solo si la cuenta tiene broche nulo, se lo seteamos a la cuenta.
		// 		Si la cuenta no posee la exencion, 
		//		             o si la posee en un estado distinto de Ha Lugar
		//		             o si los periodos no se solapan.
		
		// Validaciones de negocio
		if (!contribExe.validateCreate()) {
			return contribExe;
		}

		ExeDAOFactory.getContribExeDAO().update(contribExe);
		log.debug(funcName + " Creo contribExe");
		
		Long idRecurso = contribExe.getExencion().getRecurso().getId();
		log.debug(funcName + " Recurso de la exencion: " + contribExe.getExencion().getRecurso().getDesRecurso());
		
		EstadoCueExe estadoHaLugar = EstadoCueExe.getById(EstadoCueExe.ID_HA_LUGAR); 
		
		// Si se selecciona una exencion.
		List<Exencion> listExencion = new ArrayList<Exencion>();
		if (contribExe.getExencion() != null){
			listExencion.add(contribExe.getExencion());
		}
		
		long skip = 0;
		long first = 1000;
		
		boolean existenCuentas = true;
		
		log.debug(funcName + " idContribuyente: " + contribExe.getContribuyente().getId());
		
		while (existenCuentas) {
			
			SiatHibernateUtil.closeSession();
			session = SiatHibernateUtil.currentSession();
			
			List<Cuenta> listCuentas = contribExe.getContribuyente().getListCuentaVigentesForTitular(skip, first);
			
			log.debug(funcName + " listCuentasVigentes.size: " + listCuentas.size());

			existenCuentas = (listCuentas.size() > 0);

			if(existenCuentas){
			
				for(Cuenta cuenta :listCuentas){
					log.debug(funcName + " nroCuenta: "+cuenta.getNumeroCuenta()+" id: "+cuenta.getId() + " recurso: " + cuenta.getRecurso().getDesRecurso());
					
					// Seteo de Broche solo para cuentas TGI.
					if (Recurso.COD_RECURSO_TGI.equals(cuenta.getRecurso().getCodRecurso()) &&  
							contribExe.getBroche() != null && contribExe.getBroche().getId() != null){
						cuenta.setBroche(contribExe.getBroche());
						PadDAOFactory.getCuentaDAO().update(cuenta);
						log.debug(funcName + " Broche asignado: " + contribExe.getBroche().getId() + " - " + contribExe.getBroche().getDesBroche());
					}
					
					// Creacion de CueExe, Si se selecciona, corresponde al mismo recurso y no existe una vigente para la cuenta.
					if (contribExe.getExencion() != null){
						if (cuenta.getRecurso().getId().longValue() == idRecurso.longValue()){
							if (!cuenta.tieneAlgunaExencion(listExencion, contribExe.getFechaDesde())) {
								//crea la exencion
								CueExe cueExe = new CueExe();
								cueExe.setCuenta(cuenta);
								cueExe.setExencion(contribExe.getExencion());
								cueExe.setFechaDesde(contribExe.getFechaDesde());
								cueExe.setEstadoCueExe(estadoHaLugar);
								
								ExeDAOFactory.getCueExeDAO().update(cueExe);
								log.debug(funcName + " CueExe creada: " + cueExe.getId());
							} else {
								log.debug(funcName + " La cuenta ya posee la exencion");
							}
							
						} else {
							log.debug(funcName + " No se crea exencion por no ser recurso");
						}
					}
				}
				
				session.flush();
				session.clear();
				
				skip += first; // incremento el indice del 1er registro
				log.info( skip + " Cuentas procesadas ");
				
			}
		
		} // while existenCuentas
		
		
		SiatHibernateUtil.closeSession();
		session = SiatHibernateUtil.currentSession();
		
		log.debug(funcName + " FIN createContribExe()");
		return contribExe;
	}
	
	public ContribExe updateContribExe(ContribExe contribExe) throws Exception {
		
		// Validaciones de negocio
		if (!contribExe.validateUpdate()) {
			return contribExe;
		}

		ExeDAOFactory.getContribExeDAO().update(contribExe);
		
		return contribExe;
	}
	
	public ContribExe deleteContribExe(ContribExe contribExe) throws Exception {

		// Validaciones de negocio
		if (!contribExe.validateDelete()) {
			return contribExe;
		}

		// TODO: despues analizar esto, pero hoy por hoy no es
		// necesario pq solo hay 2 sujetos exentos y 
		// nunca se deben borrar. Ver que hacer si se 
		// decide que se pueden crear mas, y que hacer cdo se borran.
		/*
		for(CueExe cueExe:contribExe.getContribuyente().getListCueExeVigente()) {
			cueExe.setFechaHasta(new Date());
			ExeDAOFactory.getCueExeDAO().update(cueExe);
		}
		*/

		ExeDAOFactory.getContribExeDAO().delete(contribExe);

		return contribExe;
	}
	// <--- ABM ContribExe	
	
	// ---> ABM TipoSujeto
	public TipoSujeto createTipoSujeto(TipoSujeto tipoSujeto) throws Exception {

		// Validaciones de negocio
		if (!tipoSujeto.validateCreate()) {
			return tipoSujeto;
		}

		ExeDAOFactory.getTipoSujetoDAO().update(tipoSujeto);

		return tipoSujeto;
	}
	
	public TipoSujeto updateTipoSujeto(TipoSujeto tipoSujeto) throws Exception {
		
		// Validaciones de negocio
		if (!tipoSujeto.validateUpdate()) {
			return tipoSujeto;
		}

		ExeDAOFactory.getTipoSujetoDAO().update(tipoSujeto);
		
		return tipoSujeto;
	}
	
	public TipoSujeto deleteTipoSujeto(TipoSujeto tipoSujeto) throws Exception {
	
		// Validaciones de negocio
		if (!tipoSujeto.validateDelete()) {
			return tipoSujeto;
		}
		
		ExeDAOFactory.getTipoSujetoDAO().delete(tipoSujeto);
		
		return tipoSujeto;
	}
	// <--- ABM TipoSujeto
	
	// ---> ABM EstadoCueExe
	public EstadoCueExe createEstadoCueExe(EstadoCueExe estadoCueExe) throws Exception {

		// Validaciones de negocio
		if (!estadoCueExe.validateCreate()) {
			return estadoCueExe;
		}

		ExeDAOFactory.getEstadoCueExeDAO().update(estadoCueExe);

		return estadoCueExe;
	}
	
	public EstadoCueExe updateEstadoCueExe(EstadoCueExe estadoCueExe) throws Exception {
		
		// Validaciones de negocio
		if (!estadoCueExe.validateUpdate()) {
			return estadoCueExe;
		}

		ExeDAOFactory.getEstadoCueExeDAO().update(estadoCueExe);
		
		return estadoCueExe;
	}
	
	public EstadoCueExe deleteEstadoCueExe(EstadoCueExe estadoCueExe) throws Exception {
	
		// Validaciones de negocio
		if (!estadoCueExe.validateDelete()) {
			return estadoCueExe;
		}
		
		ExeDAOFactory.getEstadoCueExeDAO().delete(estadoCueExe);
		
		return estadoCueExe;
	}
	// <--- ABM EstadoCueExe
}
