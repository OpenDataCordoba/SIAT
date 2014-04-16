//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Date;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.ConAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteDefinition;

/**
 * Manejador del subm&oacute;dulo de contribuyente
 * 
 * @author tecso
 *
 */
public class PadContribuyenteManager {
		
	private static Logger log = Logger.getLogger(PadContribuyenteManager.class);
	
	public static final PadContribuyenteManager INSTANCE = new PadContribuyenteManager();
	
	/**
	 * Constructor privado
	 */
	private PadContribuyenteManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static PadContribuyenteManager getInstance() {
		return INSTANCE;
	}
	
    // ---> ABM Contribuyente
	
	
	public Contribuyente createContribuyente(Persona persona) throws Exception {
		
		Contribuyente contribuyente = new Contribuyente();
		contribuyente.setFechaDesde(new Date());
		contribuyente.setPersona(persona);
		
		// Validaciones de negocio
		if (!contribuyente.validateCreate()) {
			return contribuyente;
		}
		
		contribuyente.setId(PadDAOFactory.getContribuyenteDAO().updateManualId(contribuyente));
		
		// Crea los Atributos para el Contribuyente con valor igual al valor por defecto. 
		ContribuyenteDefinition contribuyenteDefinition = Contribuyente.getDefinitionForManual();
		for(ConAtrDefinition item: contribuyenteDefinition.getListConAtrDefinition()){
			item.addValor(item.getConAtr().getValorDefecto(), new Date(), new Date(), null);
		}
		contribuyenteDefinition = contribuyente.updateConAtrDefinition(contribuyenteDefinition);
		
		return contribuyente;
	}

	
	public Contribuyente updateContribuyente(Contribuyente contribuyente) throws Exception {
		
		// Validaciones de negocio
		if (!contribuyente.validateUpdate()) {
			return contribuyente;
		}

		PadDAOFactory.getContribuyenteDAO().update(contribuyente);
		
		return contribuyente;
	}

	// <--- ABM Contribuyente
	
}