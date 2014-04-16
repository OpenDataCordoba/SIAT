//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Otros Ingresos de Tesoreria
 * 
 * @author tecso
 *
 */
public class BalOtroIngresoTesoreriaManager {


	private static final BalOtroIngresoTesoreriaManager INSTANCE = new BalOtroIngresoTesoreriaManager();
	
	/**
	 * Constructor privado
	 */
	private BalOtroIngresoTesoreriaManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalOtroIngresoTesoreriaManager getInstance() {
		return INSTANCE;
	}
	
	
	// ---> ABM OtrIngTes	
	public OtrIngTes createOtrIngTes(OtrIngTes otrIngTes) throws Exception {

		// Validaciones de negocio
		if (!otrIngTes.validateCreate()) {
			return otrIngTes;
		}

		BalDAOFactory.getOtrIngTesDAO().update(otrIngTes);

		return otrIngTes;
	}
	
	public OtrIngTes updateOtrIngTes(OtrIngTes otrIngTes) throws Exception {
		
		// Validaciones de negocio
		if (!otrIngTes.validateUpdate()) {
			return otrIngTes;
		}
		
		BalDAOFactory.getOtrIngTesDAO().update(otrIngTes);
		
	    return otrIngTes;
	}
	
	public OtrIngTes deleteOtrIngTes(OtrIngTes otrIngTes) throws Exception {

		
		// Validaciones de negocio
		if (!otrIngTes.validateDelete()) {
			return otrIngTes;
		}
		
		BalDAOFactory.getOtrIngTesDAO().delete(otrIngTes);
		
		return otrIngTes;
	}
	// <--- ABM OtrIngTes

}
