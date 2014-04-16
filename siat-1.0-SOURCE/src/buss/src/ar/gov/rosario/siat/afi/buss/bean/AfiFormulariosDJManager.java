//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;

/**
 * Manejador del m&oacute;dulo Afi y submodulo DecJur
 * 
 * @author tecso
 *
 */
public class AfiFormulariosDJManager {
		
	private static Logger log = Logger.getLogger(AfiFormulariosDJManager.class);
	
	private static final AfiFormulariosDJManager INSTANCE = new AfiFormulariosDJManager();
	
	/**
	 * Constructor privado
	 */
	private AfiFormulariosDJManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static AfiFormulariosDJManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM ForDecJur
	public ForDecJur createForDecJur(ForDecJur forDecJur) throws Exception {

		// Validaciones de negocio
		if (!forDecJur.validateCreate()) {
			return forDecJur;
		}

		AfiDAOFactory.getForDecJurDAO().update(forDecJur);

		return forDecJur;
	}
	
	public ForDecJur updateForDecJur(ForDecJur forDecJur) throws Exception {
		
		// Validaciones de negocio
		if (!forDecJur.validateUpdate()) {
			return forDecJur;
		}

		AfiDAOFactory.getForDecJurDAO().update(forDecJur);
		
		return forDecJur;
	}

	// <--- ABM ForDecJur
	
	
		

}