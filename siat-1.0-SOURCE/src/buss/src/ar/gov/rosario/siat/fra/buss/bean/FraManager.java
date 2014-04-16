//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.fra.buss.dao.FraDAOFactory;

/**
 * Manejador del m&oacute;dulo Fra y submodulo 
 * 
 * @author tecso
 *
 */
public class FraManager {
		
	private static Logger log = Logger.getLogger(FraManager.class);
	
	private static final FraManager INSTANCE = new FraManager();
	
	/**
	 * Constructor privado
	 */
	private FraManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static FraManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Frase
	public Frase createFrase(Frase frase) throws Exception {

		// Validaciones de negocio
		if (!frase.validateCreate()) {
			return frase;
		}

		FraDAOFactory.getFraseDAO().update(frase);

		return frase;
	}
	
	public Frase updateFrase(Frase frase) throws Exception {
		
		// Validaciones de negocio
		if (!frase.validateUpdate()) {
			return frase;
		}

		FraDAOFactory.getFraseDAO().update(frase);
		
		return frase;
	}
	
	public Frase deleteFrase(Frase frase) throws Exception {
	
		// Validaciones de negocio
		if (!frase.validateDelete()) {
			return frase;
		}
		
		FraDAOFactory.getFraseDAO().delete(frase);
		
		return frase;
	}
	// <--- ABM Frase
	
	
		

}
