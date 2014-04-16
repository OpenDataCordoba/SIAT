//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Folio de Tesoreria
 * 
 * @author tecso
 *
 */
public class BalFolioTesoreriaManager {

	private static final BalFolioTesoreriaManager INSTANCE = new BalFolioTesoreriaManager();
	
	/**
	 * Constructor privado
	 */
	private BalFolioTesoreriaManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalFolioTesoreriaManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM Folio	
	public Folio createFolio(Folio folio) throws Exception {

		// Validaciones de negocio
		if (!folio.validateCreate()) {
			return folio;
		}

		BalDAOFactory.getFolioDAO().update(folio);

		return folio;
	}
	
	public Folio updateFolio(Folio folio) throws Exception {
		
		// Validaciones de negocio
		if (!folio.validateUpdate()) {
			return folio;
		}
		
		BalDAOFactory.getFolioDAO().update(folio);
		
	    return folio;
	}
	
	public Folio deleteFolio(Folio folio) throws Exception {

		
		// Validaciones de negocio
		if (!folio.validateDelete()) {
			return folio;
		}
		
		BalDAOFactory.getFolioDAO().delete(folio);
		
		return folio;
	}
	// <--- ABM Folio
	
	// ---> ABM TipoCob
	public TipoCob createTipoCob(TipoCob tipoCob) throws Exception {

		// Validaciones de negocio
		if (!tipoCob.validateCreate()) {
			return tipoCob;
		}

		BalDAOFactory.getTipoCobDAO().update(tipoCob);

		return tipoCob;
	}
	
	public TipoCob updateTipoCob(TipoCob tipoCob) throws Exception {
		
		// Validaciones de negocio
		if (!tipoCob.validateUpdate()) {
			return tipoCob;
		}

		BalDAOFactory.getTipoCobDAO().update(tipoCob);
		
		return tipoCob;
	}
	
	public TipoCob deleteTipoCob(TipoCob tipoCob) throws Exception {
	
		// Validaciones de negocio
		if (!tipoCob.validateDelete()) {
			return tipoCob;
		}
		
		BalDAOFactory.getTipoCobDAO().delete(tipoCob);
		
		return tipoCob;
	}
	// <--- ABM TipoCob
	
}
