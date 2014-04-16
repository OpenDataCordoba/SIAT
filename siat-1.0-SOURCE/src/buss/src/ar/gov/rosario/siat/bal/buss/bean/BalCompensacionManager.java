//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;

/**
 * Manejador del m&oacute;dulo Bal y submodulo Compensacion
 * 
 * @author tecso
 *
 */
public class BalCompensacionManager {
		
	private static Logger log = Logger.getLogger(BalCompensacionManager.class);
	
	private static final BalCompensacionManager INSTANCE = new BalCompensacionManager();
	
	/**
	 * Constructor privado
	 */
	private BalCompensacionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static BalCompensacionManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM TipoCom
	public TipoCom createTipoCom(TipoCom tipoCom) throws Exception {

		// Validaciones de negocio
		if (!tipoCom.validateCreate()) {
			return tipoCom;
		}

		BalDAOFactory.getTipoComDAO().update(tipoCom);

		return tipoCom;
	}
	
	public TipoCom updateTipoCom(TipoCom tipoCom) throws Exception {
		
		// Validaciones de negocio
		if (!tipoCom.validateUpdate()) {
			return tipoCom;
		}

		BalDAOFactory.getTipoComDAO().update(tipoCom);
		
		return tipoCom;
	}
	
	public TipoCom deleteTipoCom(TipoCom tipoCom) throws Exception {
	
		// Validaciones de negocio
		if (!tipoCom.validateDelete()) {
			return tipoCom;
		}
		
		BalDAOFactory.getTipoComDAO().delete(tipoCom);
		
		return tipoCom;
	}
	// <--- ABM TipoCom
	
	
	// ---> ABM Compensacion
	public Compensacion createCompensacion(Compensacion compensacion) throws Exception {

		// Validaciones de negocio
		if (!compensacion.validateCreate()) {
			return compensacion;
		}

		BalDAOFactory.getCompensacionDAO().update(compensacion);

		return compensacion;
	}
	
	public Compensacion updateCompensacion(Compensacion compensacion) throws Exception {
		
		// Validaciones de negocio
		if (!compensacion.validateUpdate()) {
			return compensacion;
		}

		BalDAOFactory.getCompensacionDAO().update(compensacion);
		
		return compensacion;
	}
	
	public Compensacion deleteCompensacion(Compensacion compensacion) throws Exception {
	
		// Validaciones de negocio
		if (!compensacion.validateDelete()) {
			return compensacion;
		}
		
		BalDAOFactory.getCompensacionDAO().delete(compensacion);
		
		return compensacion;
	}
	// <--- ABM Compensacion

}