//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Manager del modulo Gde y submodulo AdmDeuCon
 * 
 * @author tecso
 */
public class GdeAdmDeuConManager {
	
	private static Logger log = Logger.getLogger(GdeAdmDeuConManager.class);
	
	private static final GdeAdmDeuConManager INSTANCE = new GdeAdmDeuConManager();
	
	/**
	 * Constructor privado
	 */
	private GdeAdmDeuConManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static GdeAdmDeuConManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM ProPreDeu
	public ProPreDeu createProPreDeu(ProPreDeu proPreDeu) throws Exception {

		// Validaciones de negocio
		if (!proPreDeu.validateCreate()) {
			return proPreDeu;
		}

		// Creacion de la corrida del proceso
		Date fechaTope = proPreDeu.getFechaTope();
		String desCorrida = "Prescripci\u00F3n Masiva de Deuda al " + 
								DateUtil.formatDate(fechaTope, DateUtil.ddSMMSYYYY_MASK) + ". " +
							"Fecha de Creaci\u00F3n: " + DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
		
		AdpRun run =  AdpRun.newRun(Proceso.PROCESO_PRESCRIPCION_DEUDA, desCorrida);
		
		run.create();
		
		Corrida corrida = Corrida.getByIdNull(run.getId());
		
		if (corrida == null) {
			String desError = "No se pudo crear la corrida del proceso de Prescripcion de Deuda"; 
			log.error(desError);
			proPreDeu.addRecoverableValueError(desError);
		}
		
		proPreDeu.setCorrida(corrida);
		
		GdeDAOFactory.getProPreDeuDAO().update(proPreDeu);
		
		// Pasomo el id como parametro de la corrida
		run.putParameter(ProPreDeu.ADP_PARAM_ID, proPreDeu.getId().toString());

		return proPreDeu;
	}
	
	public ProPreDeu updateProPreDeu(ProPreDeu proPreDeu) throws Exception {
		
		// Validaciones de negocio
		if (!proPreDeu.validateUpdate()) {
			return proPreDeu;
		}

		GdeDAOFactory.getProPreDeuDAO().update(proPreDeu);
		
		return proPreDeu;
	}
	
	public ProPreDeu deleteProPreDeu(ProPreDeu proPreDeu) throws Exception {
	
		// Validaciones de negocio
		if (!proPreDeu.validateDelete()) {
			return proPreDeu;
		}
		
		// Eliminamos el Proceso de Prescripcion de la BD
		GdeDAOFactory.getProPreDeuDAO().delete(proPreDeu);

		// Sincronizamos con la BD
		SiatHibernateUtil.currentSession().flush();
		
		// Eliminamos la corrida
		AdpRun.deleteRun(proPreDeu.getCorrida().getId());

		return proPreDeu;
	}
	// <--- ABM ProPreDeu
}
