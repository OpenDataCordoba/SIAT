//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;

import ar.gov.rosario.siat.gde.iface.model.LiqReclamoVO;
import ar.gov.rosario.siat.gde.iface.model.LiqTramiteWeb;



public interface IPadVariosWebFacade {
	
	
	public boolean validateReclamoWeb(LiqReclamoVO liqReclamoVO) throws Exception;
	
	public int createReclamoWeb(LiqReclamoVO liqReclamoVO) throws Exception;
	
	public void grabarTramiteWeb(LiqTramiteWeb liqTramiteWeb) throws Exception;
	
}
