//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CheckPerformanceFilter implements Filter {

	private static Log log = LogFactory.getLog(CheckPerformanceFilter.class);
	
	public void init(FilterConfig arg0) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long stopTime = System.currentTimeMillis();
        
        String url = ((HttpServletRequest) request).getRequestURL().toString();
        if(url==null) url = "";
        String reqAttMethod = request.getParameter("method");
        if(reqAttMethod==null) reqAttMethod = "";
		String reqAttSelectedId = request.getParameter("selectedId");
		if(reqAttSelectedId==null) reqAttSelectedId = "";
		String act = request.getParameter("act");
		if(act==null) act = "";
		String info = "(CHEQUEO DE TIEMPOS DE RESPUESTA) Url: '"+url+"', Method: "+reqAttMethod+", Act: "+act+", SelectedId: "+reqAttSelectedId+", ";
       
        log.info(info+"Tiempo de ejecución: " + (stopTime - startTime) + " ms");
        //System.out.println("Time to execute request: " + (stopTime - startTime) + " milliseconds");

	}


}
