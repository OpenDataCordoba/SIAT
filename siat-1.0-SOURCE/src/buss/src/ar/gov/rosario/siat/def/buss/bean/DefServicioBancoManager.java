//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;

/**
 * Manejador del m&oacute;dulo Definici&oacute;n, submodulo Servicio Banco
 * 
 * @author tecso
 *
 */
public class DefServicioBancoManager {
	
	private static Logger log = Logger.getLogger(DefServicioBancoManager.class);
	
	private static final DefServicioBancoManager INSTANCE = new DefServicioBancoManager();
	
	/**
	 * Constructor privado
	 */
	private DefServicioBancoManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static DefServicioBancoManager getInstance() {
		return INSTANCE;
	}
	
	//	 ---> ABM Servicio Banco	
	public ServicioBanco createServicioBanco(ServicioBanco servicioBanco) throws Exception {

		// Validaciones de negocio
		if (!servicioBanco.validateCreate()) {
			return servicioBanco;
		}

		DefDAOFactory.getServicioBancoDAO().update(servicioBanco);

		return servicioBanco;
	}
	
	public ServicioBanco updateServicioBanco(ServicioBanco servicioBanco) throws Exception {
		
		// Validaciones de negocio
		if (!servicioBanco.validateUpdate()) {
			return servicioBanco;
		}
		
		DefDAOFactory.getServicioBancoDAO().update(servicioBanco);
		
	    return servicioBanco;
	}
	
	public ServicioBanco deleteServicioBanco(ServicioBanco servicioBanco) throws Exception {

		// Validaciones de negocio
		if (!servicioBanco.validateDelete()) {
			return servicioBanco;
		}
		
		DefDAOFactory.getServicioBancoDAO().delete(servicioBanco);
		
		return servicioBanco;
	}
	// <--- ABM Servicio Banco	

	// ---> ABM Banco	
	public Banco createBanco(Banco banco) throws Exception {

		// Validaciones de negocio
		if (!banco.validateCreate()) {
			return banco;
		}

		DefDAOFactory.getBancoDAO().update(banco);

		return banco;
	}
	
	public Banco updateBanco(Banco banco) throws Exception {
		
		// Validaciones de negocio
		if (!banco.validateUpdate()) {
			return banco;
		}
		
		DefDAOFactory.getBancoDAO().update(banco);
		
	    return banco;
	}
	
	public Banco deleteBanco(Banco banco) throws Exception {

		// Validaciones de negocio
		if (!banco.validateDelete()) {
			return banco;
		}
		
		DefDAOFactory.getBancoDAO().delete(banco);
		
		return banco;
	}
	// <--- ABM Banco	
}
