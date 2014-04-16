//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.view.util; 

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;

public class SiatListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
		try {
	    	System.out.println("INICIANDO SIAT...");
			DefServiceLocator.getConfiguracionService().initializeSiat();
		} catch (Exception e) {
			System.out.println("**************************************");
			System.out.println("SIAT ERROR:");
			System.out.println("No se pudo Inicializar SIAT.");
			System.out.println("El comportamiento de la aplicacion es inesperado.");
			System.out.println("El error fue: " + e);
			e.printStackTrace();
			System.out.println("**************************************");
		}
    }

    public void contextDestroyed(ServletContextEvent event) {
		try {
	    	System.out.println("DESTRUYENDO SIAT...");
			DefServiceLocator.getConfiguracionService().destroySiat();
		} catch (Exception e) {
			System.out.println("**************************************");
			System.out.println("SIAT ERROR:");
			System.out.println("No se pudo Destruir SIAT.");
			System.out.println("El comportamiento de la aplicacion es inesperado.");
			System.out.println("El error fue: " + e);
			e.printStackTrace();
			System.out.println("**************************************");
		}
   }
}
