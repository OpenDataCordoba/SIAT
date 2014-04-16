//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;

/**
 * Manejador del modulo Proceso y submodulo Consulta
 * 
 * @author tecso
 *
 */
public class ProConsultaManager {
		
	private static Logger log = Logger.getLogger(ProConsultaManager.class);
	
	private static final ProConsultaManager INSTANCE = new ProConsultaManager();
	
	/**
	 * Constructor privado
	 */
	private ProConsultaManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static ProConsultaManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Corrida
	public Corrida createCorrida(Corrida corrida) throws Exception {

		// Validaciones de negocio
		/*if (!corrida.validateCreate()) {
			return corrida;
		}*/

		ProDAOFactory.getCorridaDAO().update(corrida);

		return corrida;
	}
		// <--- ABM Corrida
	
	
		

}
