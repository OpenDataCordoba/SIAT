//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;

/**
 * Manejador del m&oacute;dulo Exe y submodulo Exencion
 * 
 * @author tecso
 *
 */
public class ExeExencionManager {
		
	private static Logger log = Logger.getLogger(ExeExencionManager.class);
	
	private static final ExeExencionManager INSTANCE = new ExeExencionManager();
	
	/**
	 * Constructor privado
	 */
	private ExeExencionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static ExeExencionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM CueExe
	public CueExe createCueExe(CueExe cueExe) throws Exception {

		// Validaciones de negocio
		if (!cueExe.validateCreate()) {
			return cueExe;
		}

		ExeDAOFactory.getCueExeDAO().update(cueExe);

		return cueExe;
	}
	
	public CueExe updateCueExe(CueExe cueExe) throws Exception {
		
		// Validaciones de negocio
		if (!cueExe.validateUpdate()) {
			return cueExe;
		}

		ExeDAOFactory.getCueExeDAO().update(cueExe);
		
		return cueExe;
	}
	
	public CueExe deleteCueExe(CueExe cueExe) throws Exception {
	
		// Validaciones de negocio
		if (!cueExe.validateDelete()) {
			return cueExe;
		}
		
		ExeDAOFactory.getCueExeDAO().delete(cueExe);
		
		return cueExe;
	}
	// <--- ABM CueExe

}
