//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AdpSiatServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static private Logger log = Logger.getLogger(AdpSiatServlet.class);	
	
	public void init(ServletConfig config) throws ServletException {
		log.info("Iniciando servlet ");
	}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
    	doRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
    	doRequest(request,response);
    }   

    protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
    }
}
