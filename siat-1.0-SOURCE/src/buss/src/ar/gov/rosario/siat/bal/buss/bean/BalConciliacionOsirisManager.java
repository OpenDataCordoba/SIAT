//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Conciliacion Osiris
 * 
 * @author tecso
 *
 */
public class BalConciliacionOsirisManager {
		
	private static Logger log = Logger.getLogger(BalConciliacionOsirisManager.class);
	
	private static final BalConciliacionOsirisManager INSTANCE = new BalConciliacionOsirisManager();
	
	/**
	 * Constructor privado
	 */
	private BalConciliacionOsirisManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalConciliacionOsirisManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM MovBan
	public MovBan createMovBan(MovBan movBan) throws Exception {

		// Validaciones de negocio
		if (!movBan.validateCreate()) {
			return movBan;
		}

		BalDAOFactory.getMovBanDAO().update(movBan);

		return movBan;
	}
	
	public MovBan updateMovBan(MovBan movBan) throws Exception {
		
		// Validaciones de negocio
		if (!movBan.validateUpdate()) {
			return movBan;
		}

		BalDAOFactory.getMovBanDAO().update(movBan);
		
		return movBan;
	}
	
	public MovBan deleteMovBan(MovBan movBan) throws Exception {
	
		// Validaciones de negocio
		if (!movBan.validateDelete()) {
			return movBan;
		}
		
		// Eliminar el detalle 
		for(MovBanDet movBanDet: movBan.getListMovBanDet()){
			movBan.deleteMovBanDet(movBanDet);
		}
		
		// Eliminar MovBan
		BalDAOFactory.getMovBanDAO().delete(movBan);
		
		return movBan;
	}
	// <--- ABM MovBan
}
