//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Mapa de propiedades a verificar la unicidad de un Objeto en la Base de Datos.
 * Esta clase posee un Mapa interno donde se van cargando todas las propiedades
 * por las cuales verificar para realizar una prueba de unicidad invocando a 
 * checkIsUnique() de GenericAbastractDAO
*/
public class UniqueMap {
	private Logger log = Logger.getLogger(getClass());
	private Map filters = new LinkedHashMap();
	static public String REFLECT_COOKIE = "__@reflect__";

    public UniqueMap() { 
	}

	/**
	 * Limpia el mapa interno de propiedades cargadas.
	*/
	public UniqueMap clean() {
		filters = new LinkedHashMap();
		return this;
	}

	/**
	 * Agrega una propiedad String para verificar unicidad con value.
	 * El chequeo de igualdad sera Case InSensitive (pej: fedel = FEDEL)
	*/
	public UniqueMap addString(String propName, String value) {
		filters.put("S" + propName, value);
		return this;
	}

	/**
	 * Agrega una propiedad String para verificar unicidad invocando por 
	 * reflection al metodo getter de propName del Objeto a chequear.
	 * El chequeo de igualdad sera Case InSensitive (pej: fedel = FEDEL)
	*/
	public UniqueMap addString(String propName) {
		filters.put("S" + propName, REFLECT_COOKIE);
		return this;
	}

	/**
	 * Agrega una propiedad String para verificar unicidad.
	 * El chequeo de igualdad sera Case Sensitive (pej: fedel != FEDEL)
	*/
	public UniqueMap addStringCase(String propName, String value) {
		filters.put("s" + propName, value);
		return this;
	}

	/**
	 * Agrega una propiedad String para verificar unicidad invocando por 
	 * reflection al metodo getter de propName del Objeto a chequear.
	 * El chequeo de igualdad sera Case Sensitive (pej: fedel != FEDEL)
	*/
	public UniqueMap addStringCase(String propName) {
		filters.put("s" + propName, REFLECT_COOKIE);
		return this;
	}

	/**
	 * Agrega una propiedad Long para verificar unicidad con value.
	*/
	public UniqueMap addLong(String propName, Long value) {
		filters.put("L" + propName, value);
		return this;
	}

	/**
	 * Agrega una propiedad Long para verificar unicidad invocando por 
	 * reflection al metodo getter de propName del Objeto a chequear.
	*/
	public UniqueMap addLong(String propName) {
		filters.put("L" + propName, REFLECT_COOKIE);
		return this;
	}

	/**
	 * Agrega una propiedad Integer para verificar unicidad invocando por 
	 * reflection al metodo getter de propName del Objeto a chequear.
	*/
	public UniqueMap addInteger(String propName) {
		filters.put("I" + propName, REFLECT_COOKIE);
		return this;
	}
	
	
	/**
	 * Agrega una propiedad cuyo tipo representa una entidad en Hibernate 
	 * para verificar unicidad con value.
	*/
	public UniqueMap addEntity(String propName, Object value) {
		filters.put("E" + propName, value);
		return this;
	}

	/**
	 * Agrega una propiedad cuyo tipo representa una entidad de Hibernate 
	 * para verificar unicidad invocando por reflection al metodo getter de 
	 * propName del Objeto a chequear.
	*/
	public UniqueMap addEntity(String propName) {
		filters.put("E" + propName, REFLECT_COOKIE);
		return this;
	}

	/**
	 * Retorna el mapa interno del filtros
	*/
	public Map getFilters() {
		return this.filters;
	}
}

