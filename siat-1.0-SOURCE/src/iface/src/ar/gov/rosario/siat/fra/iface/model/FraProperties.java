//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.iface.model;

import java.util.Properties;

/**
 * Contenedor de Properties del modulo Frases.
 *
 * @author tecso
 */
public class FraProperties {

	// Contiene todas las key
	private Properties propiedadesPublicas = new Properties();
	// Contiene solo las key que tengan estado = "0"
	private Properties propiedadesPrivadas = new Properties();
		
	// Constructores
	public FraProperties(){
		
	}

	// Getters y Setters
	public Properties getPropiedadesPrivadas() {
		return propiedadesPrivadas;
	}
	public void setPropiedadesPrivadas(Properties propiedadesPrivadas) {
		this.propiedadesPrivadas = propiedadesPrivadas;
	}

	public Properties getPropiedadesPublicas() {
		return propiedadesPublicas;
	}
	public void setPropiedadesPublicas(Properties propiedadesPublicas) {
		this.propiedadesPublicas = propiedadesPublicas;
	}

}
