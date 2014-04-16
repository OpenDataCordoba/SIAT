//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CasoCache {
	
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(CasoCache.class);
	
	public static final Long ID_MEGE = 1L;
	public static final Long ID_NOTAS = 2L;
	public static final Long ID_OTROS = 3L;
	
	private static CasoCache INSTANCE = new CasoCache();
	
	private List<SistemaOrigenVO> listSistemaOrigen = new ArrayList<SistemaOrigenVO>();
	
		
	public static CasoCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CasoCache();
		}
		return INSTANCE;
	}
	
	
	public CasoCache() {
		log.info("#######  Iniciando Caso.INSTANCE #########");
	}
	
	
	public List<SistemaOrigenVO> getListSistemaOrigen() {
		return listSistemaOrigen;
	}

	public void setListSistemaOrigen(List<SistemaOrigenVO> listSistemaOrigen) {
		this.listSistemaOrigen = listSistemaOrigen;
	}
	
	
	public SistemaOrigenVO obtenerSistemaOrigenById(Long id){
		
		if (id == null)
			return null;
		
		for (SistemaOrigenVO tc:getListSistemaOrigen()){
			if (tc.getId().longValue() == id.longValue()){
				
				SistemaOrigenVO soVO = new SistemaOrigenVO(tc.getId(), tc.getDesSistemaOrigen(), tc.getEsValidable());
				return soVO;
			}
		}
		
		return null;
	} 
}