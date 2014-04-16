//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.service;


import org.apache.log4j.Logger;

public class EfMapaFacade {

	private static Logger log = Logger.getLogger(EfMapaFacade.class);
	
	private static final EfMapaFacade INSTANCE = new EfMapaFacade();

	public static EfMapaFacade getInstance() {
		return INSTANCE;
	}

	
	
	public String getUrlMapa(int ancho, int alto, Double[][] listPuntosXY) throws java.net.UnknownHostException {
		return "";
	}
}
