//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.buss.bean;

import org.apache.log4j.Logger;

/**
 * Manejador del m&oacute;dulo Administraci&oacute;n
 * 
 * @author tecso
 *
 */
public class BaseManager {
	
	private static Logger log = Logger.getLogger(BaseManager.class);
	
	public static final BaseManager INSTANCE = new BaseManager();
	
	/**
	 * Constructor privado
	 */
	private BaseManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BaseManager getInstance() {
		return INSTANCE;
	}
	
	
}