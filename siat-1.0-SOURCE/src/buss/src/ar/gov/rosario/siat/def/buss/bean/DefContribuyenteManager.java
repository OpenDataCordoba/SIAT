//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;

/**
 * Manejador del subm&oacute;dulo Gravamenes del subm&oacute;dulo Definici&oacute;n
 * 
 * @author tecso
 *
 */
public class DefContribuyenteManager {
		
	private static Logger log = Logger.getLogger(DefContribuyenteManager.class);
	
	public static final DefContribuyenteManager INSTANCE = new DefContribuyenteManager();
	
	/**
	 * Constructor privado
	 */
	private DefContribuyenteManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static DefContribuyenteManager getInstance() {
		return INSTANCE;
	}
	
    // ---> ABM ConAtr
	public ConAtr createConAtr(ConAtr conAtr) throws Exception {
		// TODO Faltan validaciones y cambio del por tipo atributo. Implementar Definition.
		// Validaciones de negocio
		if (!conAtr.validateCreate()) {
			return conAtr;
		}

		DefDAOFactory.getConAtrDAO().update(conAtr);

		return conAtr;
	}
	
	public ConAtr updateConAtr(ConAtr conAtr) throws Exception {
		
		// Validaciones de negocio
		if (!conAtr.validateUpdate()) {
			return conAtr;
		}

		DefDAOFactory.getConAtrDAO().update(conAtr);
		
		return conAtr;
	}
	
	public ConAtr deleteConAtr(ConAtr conAtr) throws Exception {
	
		// Validaciones de negocio
		if (!conAtr.validateDelete()) {
			return conAtr;
		}
		
		DefDAOFactory.getConAtrDAO().delete(conAtr);
		
		return conAtr;
	}
	// <--- ABM ConAtr
	
}