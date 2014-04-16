//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.helper;

import java.util.Properties;

public class LoadConfigProperties {
	
	private String archivoProperties = "";
	
	
	private Properties props = new Properties();
	
	public LoadConfigProperties(String archivoProperties) {
		this.archivoProperties = archivoProperties;
	}

	public  void loadConfig() throws Exception {
		props.load(this.getClass().getClassLoader().getResourceAsStream(archivoProperties));
    }
	
	public  String getValorPropiedad(String propiedad) throws Exception {
		return (String) props.getProperty(propiedad);
    }

}
