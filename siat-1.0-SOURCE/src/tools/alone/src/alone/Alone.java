//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import coop.tecso.demoda.buss.helper.StandaloneContext;

public class Alone {
	public static void init(boolean initHibernate, boolean initSiat) throws Exception {
		System.out.printf("%s", "Alone in the Siat: ");
		Properties logProperties = new Properties();
		String confpath = System.getProperty("alone.confpath");
	    
		System.out.printf("props ");
		System.getProperties().load(new FileInputStream(confpath + "/alone.properties"));			

		System.out.printf("log4j ");
		InputStream stream = new FileInputStream(confpath + "/log4j.properties");
    	logProperties.load(stream);
    	PropertyConfigurator.configure(logProperties);

		System.out.printf("ds ");
		Map<String, Map<String, String>> dsm = StandaloneContext.parseDatasoruces(confpath + "/server.xml", confpath + "/context.xml");
		StandaloneContext.bindDatasources(dsm);

		if (initHibernate) {
			System.out.printf("hibernate ");
			Recurso.getById(14L).getDesRecurso();
		}

		if (initSiat) {
			System.out.printf("siat ");
			DefServiceLocator.getConfiguracionService().initializeSiat();
		}	
		
		System.out.printf("%s\n", "OK!");
	}
}
