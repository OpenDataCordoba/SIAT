//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;

/**
 * Manejador del m&oacute;dulo Definici&oacute;n
 * 
 * @author tecso
 *
 */
public class DefAtributoManager {
		
	private static Logger log = Logger.getLogger(DefAtributoManager.class);
	
	private static final DefAtributoManager INSTANCE = new DefAtributoManager();
	
	/**
	 * Constructor privado
	 */
	private DefAtributoManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static DefAtributoManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM Atributo
	public Atributo createAtributo(Atributo atributo) throws Exception {

		// Validaciones de negocio
		if (!atributo.validateCreate()) {
			return atributo;
		}

		DefDAOFactory.getAtributoDAO().update(atributo);

		return atributo;
	}
	
	public Atributo updateAtributo(Atributo atributo) throws Exception {
		
		// Validaciones de negocio
		if (!atributo.validateUpdate()) {
			return atributo;
		}

		DefDAOFactory.getAtributoDAO().update(atributo);
		
		return atributo;
	}
	
	public Atributo deleteAtributo(Atributo atributo) throws Exception {
	
		// Validaciones de negocio
		if (!atributo.validateDelete()) {
			return atributo;
		}
		
		DefDAOFactory.getAtributoDAO().delete(atributo);
		
		return atributo;
	}
	// <--- ABM Atributo
	
	// ---> ABM Dominio Atributo	
	public DomAtr createDomAtr(DomAtr domAtr) throws Exception {

		// Validaciones de negocio
		if (!domAtr.validateCreate()) {
			return domAtr;
		}

		DefDAOFactory.getDomAtrDAO().update(domAtr);

		return domAtr;
	}
	
	public DomAtr updateDomAtr(DomAtr domAtr) throws Exception {
		
		// Validaciones de negocio
		if (!domAtr.validateUpdate()) {
			return domAtr;
		}
		
		DefDAOFactory.getDomAtrDAO().update(domAtr);
		
	    return domAtr;
	}
	
	public DomAtr deleteDomAtr(DomAtr domAtr) throws Exception {

		// Validaciones de negocio
		if (!domAtr.validateDelete()) {
			return domAtr;
		}
		
		DefDAOFactory.getDomAtrDAO().delete(domAtr);
		
		return domAtr;
	}
	// <--- ABM Dominio Atributo	

}