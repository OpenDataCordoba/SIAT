//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;


/**
 * Manejador del m&oacute;dulo Esp y submodulo Habilitaci&oacute;n
 * 
 * @author tecso
 *
 */
public class EspHabilitacionManager {

	private static final EspHabilitacionManager INSTANCE = new EspHabilitacionManager();
	
	/**
	 * Constructor privado
	 */
	private EspHabilitacionManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static EspHabilitacionManager getInstance() {
		return INSTANCE;
	}
	
	// ---> ABM Habilitacion	
	public Habilitacion createHabilitacion(Habilitacion habilitacion) throws Exception {

		// Validaciones de negocio
		if (!habilitacion.validateCreate()) {
			return habilitacion;
		}

		EspDAOFactory.getHabilitacionDAO().update(habilitacion);

		return habilitacion;
	}
	
	public Habilitacion updateHabilitacion(Habilitacion habilitacion) throws Exception {
		
		// Validaciones de negocio
		if (!habilitacion.validateUpdate()) {
			return habilitacion;
		}
		
		EspDAOFactory.getHabilitacionDAO().update(habilitacion);
		
	    return habilitacion;
	}
	
	public Habilitacion deleteHabilitacion(Habilitacion habilitacion) throws Exception {

		
		// Validaciones de negocio
		if (!habilitacion.validateDelete()) {
			return habilitacion;
		}
		
		EspDAOFactory.getHabilitacionDAO().delete(habilitacion);
		
		return habilitacion;
	}
	// <--- ABM Habilitacion
	
	
	// ---> ABM ValoresCargados
	public ValoresCargados createValoresCargados(ValoresCargados valoresCargados) throws Exception {

		// Validaciones de negocio
		if (!valoresCargados.validateCreate()) {
			return valoresCargados;
		}

		EspDAOFactory.getValoresCargadosDAO().update(valoresCargados);

		return valoresCargados;
	}
	
	public ValoresCargados updateValoresCargados(ValoresCargados valoresCargados) throws Exception {
		
		// Validaciones de negocio
		if (!valoresCargados.validateUpdate()) {
			return valoresCargados;
		}

		EspDAOFactory.getValoresCargadosDAO().update(valoresCargados);
		
		return valoresCargados;
	}
	
	public ValoresCargados deleteValoresCargados(ValoresCargados valoresCargados) throws Exception {
	
		// Validaciones de negocio
		if (!valoresCargados.validateDelete()) {
			return valoresCargados;
		}
		
		EspDAOFactory.getValoresCargadosDAO().delete(valoresCargados);
		
		return valoresCargados;
	}
	// <--- ABM ValoresCargados
	

	// ---> ABM TipoEntrada
	public TipoEntrada createTipoEntrada(TipoEntrada tipoEntrada) throws Exception {

		// Validaciones de negocio
		if (!tipoEntrada.validateCreate()) {
			return tipoEntrada;
		}

		EspDAOFactory.getValoresCargadosDAO().update(tipoEntrada);

		return tipoEntrada;
	}
	
	public TipoEntrada updateTipoEntrada(TipoEntrada tipoEntrada) throws Exception {
		
		// Validaciones de negocio
		if (!tipoEntrada.validateUpdate()) {
			return tipoEntrada;
		}

		EspDAOFactory.getValoresCargadosDAO().update(tipoEntrada);
		
		return tipoEntrada;
	}
	
	public TipoEntrada deleteTipoEntrada(TipoEntrada tipoEntrada) throws Exception {
	
		// Validaciones de negocio
		if (!tipoEntrada.validateDelete()) {
			return tipoEntrada;
		}
		
		EspDAOFactory.getValoresCargadosDAO().delete(tipoEntrada);
		
		return tipoEntrada;
	}
	// <--- ABM TipoEntrada
	
	// ---> ABM LugarEvento
	public LugarEvento createLugarEvento(LugarEvento lugarEvento) throws Exception {

		// Validaciones de negocio
		if (!lugarEvento.validateCreate()) {
			return lugarEvento;
		}

		EspDAOFactory.getValoresCargadosDAO().update(lugarEvento);

		return lugarEvento;
	}
	
	public LugarEvento updateLugarEvento(LugarEvento lugarEvento) throws Exception {
		
		// Validaciones de negocio
		if (!lugarEvento.validateUpdate()) {
			return lugarEvento;
		}

		EspDAOFactory.getValoresCargadosDAO().update(lugarEvento);
		
		return lugarEvento;
	}
	
	public LugarEvento deleteLugarEvento(LugarEvento lugarEvento) throws Exception {
	
		// Validaciones de negocio
		if (!lugarEvento.validateDelete()) {
			return lugarEvento;
		}
		
		EspDAOFactory.getValoresCargadosDAO().delete(lugarEvento);
		
		return lugarEvento;
	}
	// <--- ABM LugarEvento
}
