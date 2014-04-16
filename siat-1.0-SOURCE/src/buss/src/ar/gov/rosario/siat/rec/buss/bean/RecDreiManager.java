//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;

/**
 * Manejador del m&oacute;dulo Recurso DReI
 * 
 * @author tecso
 *
 */
public class RecDreiManager {
		
	private static Logger log = Logger.getLogger(RecDreiManager.class);
	
	private static final RecDreiManager INSTANCE = new RecDreiManager();
	
	/**
	 * Constructor privado
	 */
	private RecDreiManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static RecDreiManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM CategoriaRS Drei
	
	public CatRSDrei createCategoriaRSDrei(CatRSDrei catRSDrei) throws Exception {
	
		// Validaciones de negocio
		if (!catRSDrei.validateCreate()) {
			return catRSDrei;
		}

		DefDAOFactory.getCategoriaRSDreiDAO().update(catRSDrei);
		
		return catRSDrei;
	}
	
	public CatRSDrei deleteCategoriaRSDrei(CatRSDrei catRSDrei) throws Exception {
		
		// Validaciones de negocio
		if (!catRSDrei.validateDelete()) {
			return catRSDrei;
		}
		
		DefDAOFactory.getCategoriaRSDreiDAO().delete(catRSDrei);
		
		return catRSDrei;
	}
	
	public CatRSDrei updateCategoriaRSDrei(CatRSDrei catRSDrei) throws Exception {
		
		// Validaciones de negocio
		if (!catRSDrei.validateUpdate()) {
			return catRSDrei;
		}

		DefDAOFactory.getCategoriaRSDreiDAO().update(catRSDrei);
		
		return catRSDrei;
	}
	
	// <--- ABM CategoriaRS Drei
	
		
	
}
