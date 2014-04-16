//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;

public class ExentaAreaCache {
	
	private static Logger log = Logger.getLogger(ExentaAreaCache.class);

	private Map<String, String> mapExcDeu = new HashMap<String, String>(); //mapa de idDeuda y lista de idAreas separadas por coma
	private Map<String, String> mapExcCue = new HashMap<String, String>(); //mapa de idCuenta y lista de idAreas separadas por coma
	private Map<String, String> mapExcCueDeu = new HashMap<String, String>(); //mapa de idCuenta y lista de idDeudas separadas por coma
	
	public void initializeCache() throws Exception {
		int i = 0;
		List<CueExcSel> list = CueExcSel.getListActivos();
		for(CueExcSel exc: list) {
			String idCuenta = exc.getIdCuenta().toString();
			String idArea = exc.getArea().getId().toString();
			
			String idsCueArea = mapExcCue.get(idCuenta);
			if (idsCueArea == null) 
				idsCueArea = idArea;
			else
				idsCueArea += "," + idArea;
			
			mapExcCue.put(idCuenta, idsCueArea);
		}
		
		PadDAOFactory.getCueExcSelDeuDAO().loadAllExcDeuMap(mapExcDeu, mapExcCueDeu);
		log.info("CueExcSelDeu cargadas: " + mapExcDeu.size());
	}
	
	public synchronized List<Long> esDeudaExcluida(Deuda deuda) {
		List<Long> retAreas = new ArrayList<Long>();
		String idDeuda = deuda.getId().toString();
		String idCuenta = deuda.getCuenta().getId().toString();
		String idsDeuda = this.mapExcCueDeu.get(idCuenta);

		String idAreas = null; // ids a retornar
		String tmp = this.mapExcCue.get(deuda.getCuenta().getId().toString());
		if (tmp != null && idsDeuda != null) {
			//esta la cuenta y tiene un conjunto de deuda, buscamos la deuda particularmente. -> excluimos si encontramos la deuda.
			idAreas = this.mapExcDeu.get(idDeuda);
		} else if (tmp != null && idsDeuda == null) {
			// esta la cuenta y no tiene conjunto de deuda. -> excluimos
			idAreas = tmp;
		}
		
		if (idAreas == null)
			return retAreas;
		
		for(String id : idAreas.split(",")) { retAreas.add(Long.parseLong(id)); }
		return retAreas;
	}
	
	public void dump() {
		for(String key: this.mapExcCue.keySet()) {
			log.debug("mapExcCue '" + key + "':'" + this.mapExcCue.get(key));
		}
		
		for(String key: this.mapExcDeu.keySet()) {
			log.debug("mapExcDeu '" + key + "':'" + this.mapExcDeu.get(key));
		}

		for(String key: this.mapExcCueDeu.keySet()) {
			log.debug("mapExcCueDeu '" + key + "':'" + this.mapExcCueDeu.get(key));
		}
	}
}
