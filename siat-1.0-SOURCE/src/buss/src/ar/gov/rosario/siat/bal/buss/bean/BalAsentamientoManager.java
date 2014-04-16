//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Asentamiento
 * 
 * @author tecso
 *
 */
public class BalAsentamientoManager {

	private static final BalAsentamientoManager INSTANCE = new BalAsentamientoManager();
	
	/**
	 * Constructor privado
	 */
	private BalAsentamientoManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalAsentamientoManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM Asentamiento	
	public Asentamiento createAsentamiento(Asentamiento asentamiento) throws Exception {

		// Validaciones de negocio
		if (!asentamiento.validateCreate()) {
			return asentamiento;
		}

		BalDAOFactory.getAsentamientoDAO().update(asentamiento);

		return asentamiento;
	}
	
	public Asentamiento updateAsentamiento(Asentamiento asentamiento) throws Exception {
		
		// Validaciones de negocio
		if (!asentamiento.validateUpdate()) {
			return asentamiento;
		}
		
		BalDAOFactory.getAsentamientoDAO().update(asentamiento);
		
	    return asentamiento;
	}
	
	public Asentamiento deleteAsentamiento(Asentamiento asentamiento) throws Exception {

		
		// Validaciones de negocio
		if (!asentamiento.validateDelete()) {
			return asentamiento;
		}
		
		BalDAOFactory.getAsentamientoDAO().delete(asentamiento);
		
		return asentamiento;
	}
	// <--- ABM Asentamiento
	
	
}
