//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.buss.ws.stat;

import java.io.PrintWriter;
import java.io.StringWriter;

import ar.gov.rosario.siat.base.buss.bean.ConsoleManager;

/** 
 * Minimal webservice de referencia.
 * 
 * Para implementar una webservice hacer:
 * 
 * 1- Crear un paquete con el nombre del ws:
 *    ar.gov.rosario.siat.${MODULO}.buss.ws.${WSNAME}
 *
 * 2- Agregar archivo services.xml en el root del paquete
 * 	  Modificarlo adecuadamente.
 *
 * 3- modificar /build.xml target 'webservices'.
 * 	  Agregar una entrada para compliar y empaquetar el nuevo webservice
 *    
 * NOTAS / RESTRICCIONES:
 *    - Este sistema soporta un webservice por paquete
 *    - ant webservices genera un war entero, con axis2 mas todos
 *      los webservices del siat.
 *      Para generar este .war se usa axis2.war que esta en external jars-
 *      Se opto por esto para: 
 *      	- agregar nuevos webservices facilmente.
 *			- facilidad en el esquema actual de deploy.
 *			- facilidad para compartir todos los recursos buss del siat
 *			- posibilidad de desplegar multiples webservices facilmente 
 *			  sin replicar la bola de .jars del siat.
 *			- desplegable desde el tomcat manager sin necesidad de entrar a la
 *			  consola de axis2.
 *
 * 		Por estas ventajas preferimos no desplegar los ws como aar y si con war entero.
 * 		Aunque signifique tener que agregar el axis2.war en trunk del repositorio.
 */	
public class StatWebService {
	public String status() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ConsoleManager.getInstance().status(pw);
		return sw.toString();
	}

	public String echo(String in) {
		return in;
	}

}
