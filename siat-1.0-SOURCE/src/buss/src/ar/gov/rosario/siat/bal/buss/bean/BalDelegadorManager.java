//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Delegador
 * 
 * @author tecso
 *
 */
public class BalDelegadorManager {

	private static final BalDelegadorManager INSTANCE = new BalDelegadorManager();
	
	/**
	 * Constructor privado
	 */
	private BalDelegadorManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalDelegadorManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM AseDel	
	public AseDel createAseDel(AseDel aseDel) throws Exception {

		// Validaciones de negocio
		if (!aseDel.validateCreate()) {
			return aseDel;
		}

		BalDAOFactory.getAseDelDAO().update(aseDel);

		return aseDel;
	}
	
	public AseDel updateAseDel(AseDel aseDel) throws Exception {
		
		// Validaciones de negocio
		if (!aseDel.validateUpdate()) {
			return aseDel;
		}
		
		BalDAOFactory.getAseDelDAO().update(aseDel);
		
	    return aseDel;
	}
	
	public AseDel deleteAseDel(AseDel aseDel) throws Exception {

		
		// Validaciones de negocio
		if (!aseDel.validateDelete()) {
			return aseDel;
		}
		
		BalDAOFactory.getAseDelDAO().delete(aseDel);
		
		return aseDel;
	}
	// <--- ABM AseDel
}
