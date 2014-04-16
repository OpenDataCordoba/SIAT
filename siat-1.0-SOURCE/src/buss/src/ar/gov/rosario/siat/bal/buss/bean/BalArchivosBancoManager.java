//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Archivos Banco
 * 
 * @author tecso
 *
 */
public class BalArchivosBancoManager {

	private static final BalArchivosBancoManager INSTANCE = new BalArchivosBancoManager();
	
	/**
	 * Constructor privado
	 */
	private BalArchivosBancoManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalArchivosBancoManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM Archivo	
	public Archivo createArchivo(Archivo archivo) throws Exception {

		// Validaciones de negocio
		if (!archivo.validateCreate()) {
			return archivo;
		}

		BalDAOFactory.getArchivoDAO().update(archivo);

		return archivo;
	}
	
	public Archivo updateArchivo(Archivo archivo) throws Exception {
		
		// Validaciones de negocio
		if (!archivo.validateUpdate()) {
			return archivo;
		}
		
		BalDAOFactory.getArchivoDAO().update(archivo);
		
	    return archivo;
	}
	
	public Archivo deleteArchivo(Archivo archivo) throws Exception {

		
		// Validaciones de negocio
		if (!archivo.validateDelete()) {
			return archivo;
		}
		
		BalDAOFactory.getArchivoDAO().delete(archivo);
		
		return archivo;
	}
	// <--- ABM Archivo
	
	
}
