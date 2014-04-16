//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.servlet; 

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import coop.tecso.adpcore.AdpEngine;

public class AdpSiatListener implements ServletContextListener {
	private static AdpEngine engine = null;

	synchronized public void contextInitialized(ServletContextEvent event) {
		try {
	    	System.out.println("INICIANDO ADPSIAT...");
	    	System.out.println("  INICIANDO Siat");
			DefServiceLocator.getConfiguracionService().initializeSiat();
			
			if (engine == null) {
		    	System.out.println("  INICIANDO Adp Engine");
				engine = new AdpEngine();
				engine.start();
			}
	    	System.out.println("INICIANDO ADPSIAT... OK");
			
		} catch (Exception e) {
			System.out.println("**************************************");
			System.out.println("ADPSIAT ERROR:");
			System.out.println("No se pudo Inicializar SIAT.");
			System.out.println("El comportamiento de la aplicacion es inesperado.");
			System.out.println("El error fue: " + e);
			e.printStackTrace();
			System.out.println("**************************************");
		}
    }

    public void contextDestroyed(ServletContextEvent event) {
		try {
	    	System.out.println("DESTRUYENDO ADPSIAT...");
			if (engine != null) {
		    	System.out.println("  DESTRUYENDO Adp Engine");
				engine.stop();
			}
	    	System.out.println("  DESTRUYENDO Siat");
	    	DefServiceLocator.getConfiguracionService().destroySiat();
	    	System.out.println("DESTRUYENDO ADPSIAT... OK");
		} catch (Exception e) {
			System.out.println("**************************************");
			System.out.println("ADPSIAT ERROR:");
			System.out.println("No se pudo Destruir SIAT.");
			System.out.println("El comportamiento de la aplicacion es inesperado.");
			System.out.println("El error fue: " + e);
			e.printStackTrace();
			System.out.println("**************************************");
		}
   }
}
