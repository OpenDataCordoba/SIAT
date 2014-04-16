//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo SaldoAFavor
 * 
 * @author tecso
 *
 */
public class BalSaldoAFavorManager {
		
	private static Logger log = Logger.getLogger(BalSaldoAFavorManager.class);
	
	private static final BalSaldoAFavorManager INSTANCE = new BalSaldoAFavorManager();
	
	/**
	 * Constructor privado
	 */
	private BalSaldoAFavorManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalSaldoAFavorManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM SaldoAFavor
	public SaldoAFavor createSaldoAFavor(SaldoAFavor saldoAFavor) throws Exception {

		// Validaciones de negocio
		if (!saldoAFavor.validateCreate()) {
			return saldoAFavor;
		}

		BalDAOFactory.getSaldoAFavorDAO().update(saldoAFavor);

		return saldoAFavor;
	}
	
	public SaldoAFavor updateSaldoAFavor(SaldoAFavor saldoAFavor) throws Exception {
		
		// Validaciones de negocio
		if (!saldoAFavor.validateUpdate()) {
			return saldoAFavor;
		}

		BalDAOFactory.getSaldoAFavorDAO().update(saldoAFavor);
		
		return saldoAFavor;
	}
	
	public SaldoAFavor deleteSaldoAFavor(SaldoAFavor saldoAFavor) throws Exception {
	
		// Validaciones de negocio
		if (!saldoAFavor.validateDelete()) {
			return saldoAFavor;
		}
		
		BalDAOFactory.getSaldoAFavorDAO().delete(saldoAFavor);
		
		return saldoAFavor;
	}
	// <--- ABM SaldoAFavor
	
	
		

}
