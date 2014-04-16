//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;


/**
 * Manejador del m&oacute;dulo Bal y submodulo Distribuci&oacute;n
 * 
 * @author tecso
 *
 */
public class BalDistribucionManager {

	private static final BalDistribucionManager INSTANCE = new BalDistribucionManager();
	
	/**
	 * Constructor privado
	 */
	private BalDistribucionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalDistribucionManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM DisPar	
	public DisPar createDisPar(DisPar disPar) throws Exception {

		// Validaciones de negocio
		if (!disPar.validateCreate()) {
			return disPar;
		}

		BalDAOFactory.getDisParDAO().update(disPar);

		return disPar;
	}
	
	public DisPar updateDisPar(DisPar disPar) throws Exception {
		
		// Validaciones de negocio
		if (!disPar.validateUpdate()) {
			return disPar;
		}
		
		BalDAOFactory.getDisParDAO().update(disPar);
		
	    return disPar;
	}
	
	public DisPar deleteDisPar(DisPar disPar) throws Exception {

		
		// Validaciones de negocio
		if (!disPar.validateDelete()) {
			return disPar;
		}
		
		BalDAOFactory.getDisParDAO().delete(disPar);
		
		return disPar;
	}
	// <--- ABM DisPar
	

}
