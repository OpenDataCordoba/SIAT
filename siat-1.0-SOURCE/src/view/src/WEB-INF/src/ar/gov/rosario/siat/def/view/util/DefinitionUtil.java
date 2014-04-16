//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.def.iface.model.AtrValDefinition;
import coop.tecso.demoda.iface.helper.DateUtil;


public class DefinitionUtil {
    
	private static Log log = LogFactory.getLog(DefinitionUtil.class);
	
	/**
	 * Dado un AtrValDefinition recorre el request y si encuentra un submit que coincida con el codigo del atributo, 
	 * lo popula segun los distintos casos que pueden existir para la busqueda:
	 * 
	 * @param AtrValDefinition
	 * @param request
	 */
	public static void populateAtrVal4Busqueda(AtrValDefinition atrVal , HttpServletRequest request) {

		try {
			String funcName = "populateAtrVal4Busqueda";
			
			log.debug(funcName + ": enter -------------------------------------------------");
			
			String codAtributo = atrVal.getAtributo().getCodAtributo();
			// Banderas utilizadas para los checkbox, 
			boolean limpiarListValor = true;			
			boolean checkboxSubmited = false;
			
			Enumeration e = request.getParameterNames();
			
			while( e.hasMoreElements() ){
			   String strElemetName = (String) e.nextElement() ;
			   String strValor = request.getParameter(strElemetName);
			   //log.debug(funcName + ": request: " + strElemetName + " -> " + strValor); 
			   
			   // Split para serpara el CodAtributo del id para el caso de los multivalor Ej: "Rubro.23432"
			   String[] arrName = strElemetName.split("\\.");
			   String strName = arrName[0]; 
			   
			   if (strName.toUpperCase().equals(codAtributo.toUpperCase()) ){
				   
				   log.debug(funcName + " strElemetName: " + strElemetName + "    clave: " + strName + "   valor: " + strValor);
				   log.debug(funcName + " poseeDominio: " + atrVal.getPoseeDominio() 
						   			  + "   admBusPorRan: " + atrVal.getAdmBusPorRan()
						   			  + "   tipoDato: " + atrVal.getAtributo().getTipoAtributo().getCodTipoAtributo());
				   
				   // blanqueo la lista de valores
				   if (limpiarListValor){
					   atrVal.resetListValor();
					   limpiarListValor = false;
				   }
				   
				   // Posee dominio
				   if (atrVal.getPoseeDominio()){
					   // Admite Bus por rango: clave.id1 -> on, clave.id2 -> on, clave.idn -> on (CHECKBOX)
					   if (atrVal.getAdmBusPorRan()){
						   atrVal.addValorView(arrName[1]);
						   
						   checkboxSubmited = true;
					   // NO Admite Bus por rango: clave -> id (COMBO)
					   } else {					
						   // Es Multivalor: clave.id1 -> on, clave.id2 -> on, clave.idn -> on (CHECKBOX)
						   if (atrVal.getEsMultivalor()){
							   atrVal.addValorView(arrName[1]);
							   
							   checkboxSubmited = true;
						   // NO Es Multivalor: clave -> id (COMBO)
						   } else {		
							   atrVal.addValorView(strValor);
							   return;
						   }
					   }					   
				   }
				   
				   // No posee dominio
				   if (!atrVal.getPoseeDominio()){
					   // Admite Bus por rango: clave.desde -> valor , clave.hasta -> valor (TEXTBOX.DESDE Y TEXTBOX.HASTA)
					   if (atrVal.getAdmBusPorRan()){ 						  
						   String valDesde = request.getParameter(strName + ".desde");
						   String valHasta = request.getParameter(strName + ".hasta");
						   atrVal.addValorView(valDesde);
						   atrVal.addValorView(valHasta);
						   						   
						   return;
					   // NO admite Bus por rango: clave -> valor (TEXTBOX)
					   } else {						   
						   atrVal.addValorView(strValor);
						   
						   return;
					   }
				   }
			   }
			}  
			
			// Si el atrVal fue dibujado con un checkbox y ninguno fue submitido
			if (atrVal.getPoseeDominio() && 
					atrVal.getAdmBusPorRan() &&
					   checkboxSubmited == false){
				
				atrVal.resetListValor();				
			}
			
			log.debug(funcName + ": exit ---------------------------------------------------------");
			
		} catch (Exception e){
			e.printStackTrace();			
		}
	}

		
	/**
	 * Dado un AtrValDefinition recorre el request y si encuentra un submit que coincida con el codigo del atributo, 
	 * lo popula segun los distintos casos que pueden existir para una edicion:
	 * 
	 * @param AtrValDefinition
	 * @param request
	 */
	public static void populateAtrVal4Edit(AtrValDefinition atrVal , HttpServletRequest request) {

		try {
			String funcName = "populateAtrVal4Edit";
			
			log.debug(funcName + ": enter -------------------------------------------------");
			
			String codAtributo = atrVal.getAtributo().getCodAtributo();
			// Banderas utilizadas para los checkbox, 
			boolean limpiarListValor = true;			
			boolean checkboxSubmited = false;
			
			Enumeration e = request.getParameterNames();
			
			while( e.hasMoreElements() ){
			   String strElemetName = (String) e.nextElement() ;
			   String strValor = request.getParameter(strElemetName);
			  // log.debug(funcName + ": request: " + strElemetName + " -> " + strValor); 
			   
			   // Split para serpara el CodAtributo del id para el caso de los multivalor Ej: "Rubro.23432"
			   String[] arrName = strElemetName.split("\\.");
			   String strName = arrName[0]; 
			   
			   if (strName.toUpperCase().equals(codAtributo.toUpperCase()) ){
				   
				   log.debug(funcName + " strElemetName: " + strElemetName + "    clave: " + strName + "   valor: " + strValor);
				   log.debug(funcName + " poseeDominio: " + atrVal.getPoseeDominio() 
						   			  + "   admBusPorRan: " + atrVal.getAdmBusPorRan()
						   			  + "   tipoDato: " + atrVal.getAtributo().getTipoAtributo().getCodTipoAtributo());
				   
				   // blanqueo la lista de valores
				   if (limpiarListValor){
					   atrVal.resetListValor();
					   limpiarListValor = false;
				   }
				   
				   // Posee dominio
				   if (atrVal.getPoseeDominio()){
					   // Es Multivalor: clave.id1 -> on, clave.id2 -> on, clave.idn -> on (CHECKBOX)
					   if (atrVal.getEsMultivalor()){
						   atrVal.addValorView(arrName[1]);
						   
						   checkboxSubmited = true;
					   // NO Es Multivalor: clave -> id (COMBO)
					   } else {						   
						   atrVal.addValorView(strValor);
						   return;
					   }					   
				   }
				   
				   // No posee dominio: clave -> valor (TEXTBOX)
				   if (!atrVal.getPoseeDominio()){
					   atrVal.addValorView(strValor);
					   return;					  
				   }
			   }
			}  
			
			// Si el atrVal fue dibujado con un checkbox y ninguno fue submitido
			if (atrVal.getPoseeDominio() && 
					atrVal.getAdmBusPorRan() &&
					   checkboxSubmited == false){
				
				atrVal.resetListValor();				
			}
			
			log.debug(funcName + ": exit ---------------------------------------------------------");
			
		} catch (Exception e){
			e.printStackTrace();			
		}
	}
	
	public static void populateVigenciaAtrVal(AtrValDefinition atrVal , HttpServletRequest request) {

		try {
			String funcName = "populateVigenciaAtrVal";
			
			log.debug(funcName + ": enter -------------------------------------------------");
			
			if (atrVal.getPoseeVigencia()){				
				String strFechaNovedad = request.getParameter("fechaNovedadView");
				String strFechaDesde = request.getParameter("fechaDesdeView");
				String strFechaHasta = request.getParameter("fechaHastaView");
				
				log.debug(funcName + " strFechaNovedad: " + strFechaNovedad + "    strFechaDesde: " + strFechaDesde + "    strFechaHasta: " + strFechaHasta);
				
				atrVal.setFechaNovedad( DateUtil.getDate(strFechaNovedad, DateUtil.ddSMMSYYYY_MASK) );
				atrVal.setFechaNovedadView(strFechaNovedad);
				atrVal.setFechaDesde( DateUtil.getDate(strFechaDesde, DateUtil.ddSMMSYYYY_MASK) );
				atrVal.setFechaDesdeView(strFechaDesde);
				atrVal.setFechaHasta( DateUtil.getDate(strFechaHasta, DateUtil.ddSMMSYYYY_MASK) );
				atrVal.setFechaHastaView(strFechaHasta);
			}
			
			log.debug(funcName + ": exit ---------------------------------------------------------");
			
		} catch (Exception e){
			e.printStackTrace();			
		}
	}
	
	public static void requestValues(HttpServletRequest request ){
		String funcName = "requestValues"; 
		log.debug(funcName + ": enter ---------------------------------------------------------");
		Enumeration e = request.getParameterNames();
		while( e.hasMoreElements() ){
		   String strElemetName = (String) e.nextElement() ;
		   String strValor = request.getParameter(strElemetName);
		   log.debug(funcName + ": request: " + strElemetName + " -> " + strValor);
		}
		log.debug(funcName + ": exit ---------------------------------------------------------");
	}
	
	public static Map<String,String> requestToHashMap(HttpServletRequest request ){
		String funcName = "requestToHashMap"; 
		log.debug(funcName + ": enter ---------------------------------------------------------");
		
		Map<String, String> hm = new HashMap<String, String>();
		
		Enumeration e = request.getParameterNames();
		while( e.hasMoreElements() ){
		   String strElemetName = (String) e.nextElement() ;
		   String strValor = request.getParameter(strElemetName);
		   log.debug(funcName + ": request: " + strElemetName + " -> " + strValor);
		   hm.put(strElemetName, strValor);
		}
		log.debug(funcName + ": exit ---------------------------------------------------------");
		return hm;
	}
	
}