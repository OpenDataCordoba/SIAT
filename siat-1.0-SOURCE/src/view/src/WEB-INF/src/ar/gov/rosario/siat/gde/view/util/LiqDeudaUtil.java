//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.EstadoPeriodo;

/**
 * LiqDeudaUtil, helper para gestionar los filtros que se mantienen durante la navegacion de la liquidacion de deuda.
 * 
 *  * La utilizacion de esta clase va de la mano de la inclusion de "includeFilterCuenta.jsp" al final de las respectivas paginas.
 * 
 * @author burano
 */
public class LiqDeudaUtil {
	
	private static Log log = LogFactory.getLog(LiqDeudaUtil.class);
	
	public LiqDeudaUtil() {
		super();
	}


	/**
	 * Populate utilizado mantener los filtros de la liquidacion de deuda durante la navegacion.
	 * 
	 * @param liqCuenta
	 * @param request
	 */
	public static void populateCuentaFilters(LiqCuentaVO liqCuenta, HttpServletRequest request, String prefix){
		
		String funcName = "populateCuentaFilters() ";
		
		log.debug(funcName + prefix + "esRecursoAutoliquidable: " + request.getParameter(prefix + "esRecursoAutoliquidable"));
		if ( request.getParameter(prefix + "esRecursoAutoliquidable") != null){
			if (request.getParameter(prefix + "esRecursoAutoliquidable").equals("true"))
				liqCuenta.setEsRecursoAutoliquidable(true);
			else
				liqCuenta.setEsRecursoAutoliquidable(false);
		}
		
		log.debug(funcName + prefix + "estadoPeriodo.id: " + request.getParameter(prefix + "estadoPeriodo.id"));
		if ( request.getParameter(prefix + "estadoPeriodo.id") != null){
			liqCuenta.setEstadoPeriodo(EstadoPeriodo.getById(new Integer(request.getParameter(prefix + "estadoPeriodo.id"))));
		}

		log.debug(funcName + prefix + "recClaDeu.id: " + request.getParameter(prefix + "recClaDeu.id"));
		if (!StringUtil.isNullOrEmpty(request.getParameter(prefix + "recClaDeu.id"))){
			liqCuenta.getRecClaDeu().setId(new Long(request.getParameter(prefix + "recClaDeu.id")));
		}

		log.debug(funcName + prefix + "fechaDesde: " + request.getParameter(prefix + "fechaVtoDesdeView"));
		if(!StringUtil.isNullOrEmpty(request.getParameter(prefix + "fechaVtoDesdeView"))){
			if( DateUtil.isValidDate(request.getParameter(prefix + "fechaVtoDesdeView"), DateUtil.ddSMMSYYYY_MASK)){
				liqCuenta.setFechaVtoDesde(DateUtil.getDate(request.getParameter(prefix + "fechaVtoDesdeView"), DateUtil.ddSMMSYYYY_MASK));
			} else{
				liqCuenta.setFechaVtoDesdeView(request.getParameter(prefix + "fechaVtoDesdeView"));
			}
		} 

		log.debug(funcName + prefix + "fechaHasta: " + request.getParameter(prefix + "fechaVtoHastaView"));
		if(!StringUtil.isNullOrEmpty(request.getParameter(prefix + "fechaVtoHastaView"))){
			if( DateUtil.isValidDate(request.getParameter(prefix + "fechaVtoHastaView"), DateUtil.ddSMMSYYYY_MASK)){
				liqCuenta.setFechaVtoHasta(DateUtil.getDate(request.getParameter(prefix + "fechaVtoHastaView"), DateUtil.ddSMMSYYYY_MASK));
			} else{
				liqCuenta.setFechaVtoHastaView(request.getParameter(prefix + "fechaVtoHastaView"));
			}
		}		
	}
	
	/**
	 * Llama al populateCuentaFilters con prefijo vacio
	 * 
	 * @param liqCuenta
	 * @param request
	 */
	public static void populateCuentaFilters(LiqCuentaVO liqCuenta, HttpServletRequest request){
		
		populateCuentaFilters(liqCuenta, request, "");
		
	}
	
}
