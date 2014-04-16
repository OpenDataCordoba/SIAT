//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;

/**
 * Manejador del m&oacute;dulo de Objeto Imponible
 * 
 * @author tecso
 *
 */
public class DefObjetoImponibleManager {
		
	private static Logger log = Logger.getLogger(DefObjetoImponibleManager.class);
	
	private static final DefObjetoImponibleManager INSTANCE = new DefObjetoImponibleManager();
	
	/**
	 * Constructor privado
	 */
	private DefObjetoImponibleManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static DefObjetoImponibleManager getInstance() {
		return INSTANCE;
	}

	
	public TipObjImp createTipObjImp(TipObjImp tipObjImp) throws Exception {

		// Validaciones de negocio
		if (!tipObjImp.validateCreate()) {
			return tipObjImp;
		}

		DefDAOFactory.getTipObjImpDAO().update(tipObjImp);

		return tipObjImp;
	}
	
	
	public TipObjImp updateTipObjImp(TipObjImp tipObjImp) throws Exception {
		
		// Validaciones de negocio
		if (!tipObjImp.validateUpdate()) {
			return tipObjImp;
		}

		DefDAOFactory.getTipObjImpDAO().update(tipObjImp);

		return tipObjImp;
	}
	
	
	public TipObjImp deleteTipObjImp(TipObjImp tipObjImp) throws Exception {
		
		// Validaciones de negocio
		if (!tipObjImp.validateDelete()) {
			return tipObjImp;
		}

		DefDAOFactory.getTipObjImpDAO().delete(tipObjImp);

		return tipObjImp;
	}
}