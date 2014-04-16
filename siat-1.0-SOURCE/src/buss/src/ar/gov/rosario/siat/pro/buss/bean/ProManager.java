//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;

/**
 * Manejador del modulo Pro
 * 
 * @author tecso
 *
 */
public class ProManager {
		
	private static Logger log = Logger.getLogger(ProManager.class);
	
	public static final ProManager INSTANCE = new ProManager();
	
	/**
	 * Constructor privado
	 */
	private ProManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static ProManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM 	
		
	public Corrida deleteCorrida(Corrida corrida) throws Exception {

		// Validaciones de negocio
		if (!corrida.validateDelete()) {
			return corrida;
		}
		
		ProDAOFactory.getCorridaDAO().delete(corrida);
		
		return corrida;
	}
	// <--- ABM Corrida	

	// ---> ABM proceso
	public Proceso createProceso(Proceso proceso) throws Exception {

		// Validaciones de negocio
		if (!proceso.validateCreate()) {
			return proceso;
		}
		
		ProDAOFactory.getProcesoDAO().update(proceso);
				
		return proceso;
	}

	
	public Proceso updateProceso(Proceso proceso) throws Exception {
		
		// Validaciones de negocio
		if (!proceso.validateUpdate()) {
			return proceso;
		}

		ProDAOFactory.getProcesoDAO().update(proceso);
		
		return proceso;
	}
	
	public Proceso deleteProceso(Proceso proceso) throws Exception {
	
		// Validaciones de negocio
		if (!proceso.validateDelete()) {
			return proceso;
		}
		
		ProDAOFactory.getProcesoDAO().delete(proceso);
		
		return proceso;
	}
	
	// <--- ABM Proceso
}

