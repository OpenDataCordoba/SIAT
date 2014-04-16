//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Balance
 * 
 * @author tecso
 *
 */
public class BalBalanceManager {
		
	private static Logger log = Logger.getLogger(BalBalanceManager.class);
	
	private static final BalBalanceManager INSTANCE = new BalBalanceManager();
	
	/**
	 * Constructor privado
	 */
	private BalBalanceManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalBalanceManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Caja7
	public Caja7 createCaja7(Caja7 caja7) throws Exception {

		// Validaciones de negocio
		if (!caja7.validateCreate()) {
			return caja7;
		}

		BalDAOFactory.getCaja7DAO().update(caja7);

		return caja7;
	}
	
	public Caja7 updateCaja7(Caja7 caja7) throws Exception {
		
		// Validaciones de negocio
		if (!caja7.validateUpdate()) {
			return caja7;
		}

		BalDAOFactory.getCaja7DAO().update(caja7);
		
		return caja7;
	}
	
	public Caja7 deleteCaja7(Caja7 caja7) throws Exception {
	
		// Validaciones de negocio
		if (!caja7.validateDelete()) {
			return caja7;
		}
		
		BalDAOFactory.getCaja7DAO().delete(caja7);
		
		return caja7;
	}
	// <--- ABM Caja7
	
	
	// ---> ABM Balance	
	public Balance createBalance(Balance balance) throws Exception {

		// Validaciones de negocio
		if (!balance.validateCreate()) {
			return balance;
		}

		BalDAOFactory.getBalanceDAO().update(balance);

		return balance;
	}
	
	public Balance updateBalance(Balance balance) throws Exception {
		
		// Validaciones de negocio
		if (!balance.validateUpdate()) {
			return balance;
		}
		
		BalDAOFactory.getBalanceDAO().update(balance);
		
	    return balance;
	}
	
	public Balance deleteBalance(Balance balance) throws Exception {

		
		// Validaciones de negocio
		if (!balance.validateDelete()) {
			return balance;
		}
		
		BalDAOFactory.getBalanceDAO().delete(balance);
		
		return balance;
	}
	// <--- ABM Balance
		
	//	 ---> ABM Reingreso
	public Reingreso createReingreso(Reingreso reingreso) throws Exception {

		// Validaciones de negocio
		if (!reingreso.validateCreate()) {
			return reingreso;
		}

		BalDAOFactory.getReingresoDAO().update(reingreso);

		return reingreso;
	}
	
	public Reingreso updateReingreso(Reingreso reingreso) throws Exception {
		
		// Validaciones de negocio
		if (!reingreso.validateUpdate()) {
			return reingreso;
		}

		BalDAOFactory.getReingresoDAO().update(reingreso);
		
		return reingreso;
	}
	
	public Reingreso deleteReingreso(Reingreso reingreso) throws Exception {
	
		// Validaciones de negocio
		if (!reingreso.validateDelete()) {
			return reingreso;
		}
		
		BalDAOFactory.getReingresoDAO().delete(reingreso);
		
		return reingreso;
	}
	// <--- ABM Reingreso
	
	// ---> ABM Caja69
	public Caja69 createCaja69(Caja69 caja69) throws Exception {

		// Validaciones de negocio
		if (!caja69.validateCreate()) {
			return caja69;
		}

		BalDAOFactory.getCaja69DAO().update(caja69);

		return caja69;
	}
	
	public Caja69 updateCaja69(Caja69 caja69) throws Exception {
		
		// Validaciones de negocio
		if (!caja69.validateUpdate()) {
			return caja69;
		}

		BalDAOFactory.getCaja69DAO().update(caja69);
		
		return caja69;
	}
	
	public Caja69 deleteCaja69(Caja69 caja69) throws Exception {
	
		// Validaciones de negocio
		if (!caja69.validateDelete()) {
			return caja69;
		}
		
		BalDAOFactory.getCaja69DAO().delete(caja69);
		
		return caja69;
	}
	// <--- ABM Caja69
	
	// ---> ABM TranBal
	public TranBal createTranBal(TranBal tranBal) throws Exception {

		// Validaciones de negocio
		if (!tranBal.validateCreate()) {
			return tranBal;
		}

		BalDAOFactory.getTranBalDAO().update(tranBal);

		return tranBal;
	}
	
	public TranBal updateTranBal(TranBal tranBal) throws Exception {
		
		// Validaciones de negocio
		if (!tranBal.validateUpdate()) {
			return tranBal;
		}

		BalDAOFactory.getTranBalDAO().update(tranBal);
		
		return tranBal;
	}
	
	public TranBal deleteTranBal(TranBal tranBal) throws Exception {
	
		// Validaciones de negocio
		if (!tranBal.validateDelete()) {
			return tranBal;
		}
		
		BalDAOFactory.getTranBalDAO().delete(tranBal);
		
		return tranBal;
	}
	// <--- ABM TranBal
	
	// ---> ABM AuxCaja7
	public AuxCaja7 createAuxCaja7(AuxCaja7 auxCaja7) throws Exception {

		// Validaciones de negocio
		if (!auxCaja7.validateCreate()) {
			return auxCaja7;
		}

		BalDAOFactory.getAuxCaja7DAO().update(auxCaja7);

		return auxCaja7;
	}
	
	public AuxCaja7 updateAuxCaja7(AuxCaja7 auxCaja7) throws Exception {
		
		// Validaciones de negocio
		if (!auxCaja7.validateUpdate()) {
			return auxCaja7;
		}

		BalDAOFactory.getAuxCaja7DAO().update(auxCaja7);
		
		return auxCaja7;
	}
	
	public AuxCaja7 deleteAuxCaja7(AuxCaja7 auxCaja7) throws Exception {
	
		// Validaciones de negocio
		if (!auxCaja7.validateDelete()) {
			return auxCaja7;
		}
		
		BalDAOFactory.getAuxCaja7DAO().delete(auxCaja7);
		
		return auxCaja7;
	}
	// <--- ABM AuxCaja7
}
