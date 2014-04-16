//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.AdpEngine;

public class AdpServlet extends HttpServlet {
	private static final long serialVersionUID = 4036523544680102473L;
	private static AdpEngine engine = null;
	static private Logger log = Logger.getLogger(AdpServlet.class);	
	
	protected synchronized static void start(ServletConfig config) throws Exception {
		if (engine == null) {
			//BasicConfigurator.configure();
			//Logger.getRootLogger().setLevel(Level.ALL);
			log = Logger.getLogger(AdpServlet.class);
			
			engine = new AdpEngine();
			//talvez algo de configuracion previa
			engine.start();
		}
	}
	
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	try {
    		log.debug("Iniciando servlet");
    		AdpServlet.start(config);
    	} catch (Exception e) {
    		log.error("No se pudo inicializar engine", e);
    		throw new ServletException(e);
    	}
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
    	doRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
    	doRequest(request,response);
    }   

    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
    	//algo
    }

}
