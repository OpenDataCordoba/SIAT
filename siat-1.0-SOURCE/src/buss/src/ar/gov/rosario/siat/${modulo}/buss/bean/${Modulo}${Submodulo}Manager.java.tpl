package ar.gov.rosario.siat.${modulo}.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.${modulo}.buss.dao.${Modulo}DAOFactory;

/**
 * Manejador del m&oacute;dulo ${Modulo} y submodulo ${Submodulo}
 * 
 * @author tecso
 *
 */
public class ${Modulo}${Submodulo}Manager {
		
	private static Logger log = Logger.getLogger(${Modulo}${Submodulo}Manager.class);
	
	private static final ${Modulo}${Submodulo}Manager INSTANCE = new ${Modulo}${Submodulo}Manager();
	
	/**
	 * Constructor privado
	 */
	private ${Modulo}${Submodulo}Manager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static ${Modulo}${Submodulo}Manager getInstance() {
		return INSTANCE;
	}

	// ---> ABM ${Bean}
	public ${Bean} create${Bean}(${Bean} ${bean}) throws Exception {

		// Validaciones de negocio
		if (!${bean}.validateCreate()) {
			return ${bean};
		}

		${Modulo}DAOFactory.get${Bean}DAO().update(${bean});

		return ${bean};
	}
	
	public ${Bean} update${Bean}(${Bean} ${bean}) throws Exception {
		
		// Validaciones de negocio
		if (!${bean}.validateUpdate()) {
			return ${bean};
		}

		${Modulo}DAOFactory.get${Bean}DAO().update(${bean});
		
		return ${bean};
	}
	
	public ${Bean} delete${Bean}(${Bean} ${bean}) throws Exception {
	
		// Validaciones de negocio
		if (!${bean}.validateDelete()) {
			return ${bean};
		}
		
		${Modulo}DAOFactory.get${Bean}DAO().delete(${bean});
		
		return ${bean};
	}
	// <--- ABM ${Bean}
	
	
		

}