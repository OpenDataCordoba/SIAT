//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.view.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

import ar.gov.rosario.siat.fra.iface.util.Frase;


public class FraseFactory extends MessageResourcesFactory {
	
	private Log log = LogFactory.getLog(FraseFactory.class);
	
	@Override
	public MessageResources createResources(String arg0) {
		
		log.info("FraseFactory -> createResources");
		
		Frase frase = Frase.getInstance();
		
		return frase;
	}
}
