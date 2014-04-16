//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;

public class IndeterminadoCache {

	private static Logger log = Logger.getLogger(IndeterminadoCache.class);
	private static IndeterminadoCache INSTANCE = null;
	//private Map<String, Long> indet = new ConcurrentHashMap<String, Long>();
	private Map<String, IndetVO> indetMap = new HashMap<String, IndetVO>();
	private Timer timer;
	private Exception cacheException = null; //indica si el cache posee algun error que lo hace invalido.

	private IndeterminadoCache() {
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public synchronized static IndeterminadoCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new IndeterminadoCache();
			INSTANCE.initialize();
		}
		return INSTANCE;
	}
		
	private void initialize() {	
		//Invalidamos cache para que se cargue por primera vez.
		invalidateCache();
		//Programos timer para que se invalide cada un hora.
		//TODO: sacar tiempo de invalidacion de cache de indeterminados de param
		int refreshMin = 30; // cada media hora
		timer = new Timer();
		timer.scheduleAtFixedRate(new Invalidator(), refreshMin * 60*1000, refreshMin * 60*1000);		
	}

	class Invalidator extends TimerTask  {
		public void run () {
			IndeterminadoCache.getInstance().invalidateCache();
		}
	}	

	public synchronized IndetVO getIndetVO(String key) throws Exception {
		if (cacheException != null) throw new Exception("No se pudo verificar indeterminado.", cacheException);
		IndetVO ret = indetMap.get(key);
		return ret;
	}

	public synchronized Long get(String key) throws Exception {
		if (cacheException != null) throw new Exception("No se pudo verificar indeterminado.", cacheException);
		IndetVO ret = indetMap.get(key);
		return ret == null ? 0L : 1L;
	}
	
	public Long get(Long nroSistema, String nroCuenta, String nroClave, Long resto) throws Exception {
		String key = IndeterminadoCache.formatKey(nroSistema, nroCuenta, nroClave, resto);
		Long ret = get(key);
		log.debug("IndeterminadosCache: get: '" + key + "' : " + ret);
		return ret;
	}
	public IndetVO getIndetVO(Long nroSistema, String nroCuenta, String nroClave, Long resto) throws Exception {
		String key = IndeterminadoCache.formatKey(nroSistema, nroCuenta, nroClave, resto);
		return getIndetVO(key);
	}
/*	public synchronized void invalidateCacheOld() {
		try {
			List<String> indetTot = BalDAOFactory.getIndeterminadoJDBCDAO().getAllIndetTot();
			indet.clear();
			for(String key : indetTot) {
				long v = 1;
				if (indet.containsKey(key)) {
					v = indet.get(key).longValue() + 1;
				}
				indet.put(key, v);
				//log.debug("Cargando Indeterminado: '" + key + "'");
			}
			cacheException = null;
			log.info("Datos actualizados: cache.size(): " + indet.size());
		} catch (Exception e) {
			log.error("Fallo el timer de actualizacion de cache. No se pudo invalidad cache, para leer nuevamente los datos de indet_tot en indeterminados.", e);
			cacheException = e;
		}
	}
*/
	
	public synchronized void invalidateCache() {
		try {
			Map<String, IndetVO> indetTot = BalDAOFactory.getIndeterminadoJDBCDAO().getMapAllIndet();
			this.indetMap.clear();
			this.indetMap = indetTot;
			cacheException = null;
			log.info("Datos actualizados: cache.size(): " + indetMap.size());
		} catch (Exception e) {
			log.error("Fallo el timer de actualizacion de cache. No se pudo invalidad cache, para leer nuevamente los datos de indeterminados.", e);
			cacheException = e;
		}
	}
		
	/**
	 * Arma una clave para el mapa.
	 * Este metodo formatea la clave añadiendo los ceros a la derecha.
	 */
	static public String formatKey(Long nroSistema, String nroCuenta, String nroClave, Long resto) {
		StringBuilder sb = new StringBuilder();
		try {
			String sistema = nroSistema.toString();
			String cuenta = Long.valueOf(nroCuenta).toString();
			String clave = Long.valueOf(nroClave).toString();
			String res = resto.toString();

			sb.append(sistema).append("-");
			sb.append(cuenta).append("-");
			sb.append(clave).append("-");
			sb.append(res);
		} catch (Exception e) {
   			log.info("Fallo formateo clave para indeterminados: " + nroSistema + "-" + nroCuenta + "-" + nroClave + "-" + resto + ". Exception:"+ e);
		}

		return sb.toString();
	}
	
	/**
	 * Arma una clave para el mapa co formato "sistema-cuenta-clave-resto"
	 * Este método trimea los 00 delante de los string convirtiendolos a Long()
	 * <p>- Si la cuenta posee un valor invalido (no numerico) lanza un excepcion
	 * <p>- Si el resto o la clave posee null, "" o datos invalidos pone 0
	 */
	static public String formatKey(String sistemaO, String cuentaO, String claveO, String restoO) {
		StringBuilder sb = new StringBuilder();
		sb.append(Long.valueOf(sistemaO.trim()).toString()).append("-");
		sb.append(Long.valueOf(cuentaO.trim()).toString()).append("-");
		try { sb.append(Long.valueOf(claveO.trim()).toString()).append("-"); } catch (Exception e) { sb.append("0-"); }
		try { sb.append(Long.valueOf(restoO.trim()).toString()); } catch (Exception e) { sb.append("0"); }
		return sb.toString();
	}

	public synchronized Exception getCacheException() {
		return cacheException;
	}
}
