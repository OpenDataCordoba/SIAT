//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;

/**
 * Manejador del m&oacute;dulo Cyq y submodulo Definicion
 * 
 * @author tecso
 *
 */
public class CyqDefinicionManager {
		
	private static Logger log = Logger.getLogger(CyqDefinicionManager.class);
	
	private static final CyqDefinicionManager INSTANCE = new CyqDefinicionManager();
	
	/**
	 * Constructor privado
	 */
	private CyqDefinicionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static CyqDefinicionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Abogado
	public Abogado createAbogado(Abogado abogado) throws Exception {

		// Validaciones de negocio
		if (!abogado.validateCreate()) {
			return abogado;
		}

		CyqDAOFactory.getAbogadoDAO().update(abogado);

		return abogado;
	}
	
	public Abogado updateAbogado(Abogado abogado) throws Exception {
		
		// Validaciones de negocio
		if (!abogado.validateUpdate()) {
			return abogado;
		}

		CyqDAOFactory.getAbogadoDAO().update(abogado);
		
		return abogado;
	}
	
	public Abogado deleteAbogado(Abogado abogado) throws Exception {
	
		// Validaciones de negocio
		if (!abogado.validateDelete()) {
			return abogado;
		}
		
		CyqDAOFactory.getAbogadoDAO().delete(abogado);
		
		return abogado;
	}
	// <--- ABM Abogado
	
	

	// ---> ABM Juzgado
	public Juzgado createJuzgado(Juzgado juzgado) throws Exception {

		// Validaciones de negocio
		if (!juzgado.validateCreate()) {
			return juzgado;
		}

		CyqDAOFactory.getJuzgadoDAO().update(juzgado);

		return juzgado;
	}
	
	public Juzgado updateJuzgado(Juzgado juzgado) throws Exception {
		
		// Validaciones de negocio
		if (!juzgado.validateUpdate()) {
			return juzgado;
		}

		CyqDAOFactory.getJuzgadoDAO().update(juzgado);
		
		return juzgado;
	}
	
	public Juzgado deleteJuzgado(Juzgado juzgado) throws Exception {
	
		// Validaciones de negocio
		if (!juzgado.validateDelete()) {
			return juzgado;
		}
		
		CyqDAOFactory.getJuzgadoDAO().delete(juzgado);
		
		return juzgado;
	}
	// <--- ABM Juzgado
		

}
