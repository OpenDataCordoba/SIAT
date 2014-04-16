//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Clasificaci&oacute;n
 * 
 * @author tecso
 *
 */
public class BalClasificacionManager {

	private static final BalClasificacionManager INSTANCE = new BalClasificacionManager();
	
	/**
	 * Constructor privado
	 */
	private BalClasificacionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalClasificacionManager getInstance() {
		return INSTANCE;
	}
	
	
	// ---> ABM Clasificador	
	public Clasificador createClasificador(Clasificador clasificador) throws Exception {

		// Validaciones de negocio
		if (!clasificador.validateCreate()) {
			return clasificador;
		}

		BalDAOFactory.getClasificadorDAO().update(clasificador);

		return clasificador;
	}
	
	public Clasificador updateClasificador(Clasificador clasificador) throws Exception {
		
		// Validaciones de negocio
		if (!clasificador.validateUpdate()) {
			return clasificador;
		}
		
		BalDAOFactory.getClasificadorDAO().update(clasificador);
		
	    return clasificador;
	}
	
	public Clasificador deleteClasificador(Clasificador clasificador) throws Exception {

		// Validaciones de negocio
		if (!clasificador.validateDelete()) {
			return clasificador;
		}
		
		BalDAOFactory.getClasificadorDAO().delete(clasificador);
		
		return clasificador;
	}
	// <--- ABM Clasificador

	// ---> ABM Nodo	
	public Nodo createNodo(Nodo nodo) throws Exception {

		// Validaciones de negocio
		if (!nodo.validateCreate()) {
			return nodo;
		}

		BalDAOFactory.getNodoDAO().update(nodo);

		return nodo;
	}
	
	public Nodo updateNodo(Nodo nodo) throws Exception {
		
		// Validaciones de negocio
		if (!nodo.validateUpdate()) {
			return nodo;
		}
		
		BalDAOFactory.getNodoDAO().update(nodo);
		
	    return nodo;
	}
	
	public Nodo deleteNodo(Nodo nodo) throws Exception {

		
		// Validaciones de negocio
		if (!nodo.validateDelete()) {
			return nodo;
		}
		
		BalDAOFactory.getNodoDAO().delete(nodo);
		
		return nodo;
	}
	// <--- ABM Nodo
	
}
