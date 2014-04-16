//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import coop.tecso.demoda.iface.helper.DemodaUtil;

/**
 * 
 * Contiene las listas que requiere el balance.
 * 
 * Al comienzo del mismo, se debe inicializar, en ese
 * momento, se levantan las listas y quedan en un Cache. Estas listas se
 * implementan en mapas. 
 *  
 * @author tecso
 * 
 */
public class BalanceCache {

	private static Logger log = Logger.getLogger(BalanceCache.class);

	private static BalanceCache INSTANCE = null;

	private Exception cacheException = null; // indica si el cache posee
												// algun error que lo hace
												// invalido.

	/* genericos */
	Map<String, Sistema> mapSistema = new HashMap<String, Sistema>();
	Map<String, Partida> mapPartida = new HashMap<String, Partida>();
	
	// Mapa de CuentaBancaria vigente por codPartida
	Map<String, CuentaBanco> mapCuentaBanco = new HashMap<String, CuentaBanco>();
	
	private BalanceCache() {
	}

	/**
	 * Devuelve unica instancia
	 */
	public synchronized static BalanceCache getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BalanceCache();
		}
		return INSTANCE;
	}

	/**
	 * Este metodo inicializa los mapas genericos
	 * 
	 * @throws Exception
	 */
	public synchronized boolean initialize() {
		String key;

		try {
			// limpia la exception
			this.cacheException = null;

			log.info(DemodaUtil.currentMethodName() + "CACHE:  Inicio");
			// inicializa Sistemas
			List<Sistema> listSistema = Sistema.getList();
			mapSistema.clear();
			for (Sistema sistema : listSistema) {
				key = sistema.getNroSistema().toString();
				this.mapSistema.put(key, sistema);
				log.info(DemodaUtil.currentMethodName()
						+ "CACHE: mapSistema - " + key);
			}
			
			// inicializa Partidas y CuentaBanco
			List<Partida> listPartida = Partida.getList();
			mapPartida.clear();
			for (Partida partida : listPartida) {
				key = partida.getId().toString();
				
				this.mapPartida.put(key, partida);				
				log.info(DemodaUtil.currentMethodName()
						+ "CACHE: mapPartida - " + key);
				
				// Obtenemos la Cuenta Bancaria Vigente a la fecha para cada partida y la cargamos en un mapa.
				CuentaBanco cuentaBanco = partida.getCuentaBancoVigente(new Date());
				if(cuentaBanco != null){
					cuentaBanco.toVO(1);
					this.mapCuentaBanco.put(partida.getCodPartida(), cuentaBanco);									
					log.info(DemodaUtil.currentMethodName()
							+ "CACHE: mapCuentaBanco - " + key);
				}		
			}
			
			return true;

		} catch (Exception e) {
			log.error(DemodaUtil.currentMethodName() + " Fallo cleanCache", e);
			cacheException = e;
			return false;
		}

	}

	/**
	 * Busca Sistema por numero
	 * 
	 * @param nroSistema
	 * @return
	 * @throws Exception
	 */
	public synchronized Sistema getSistemaByNro(Long nroSistema) throws Exception {
		if (cacheException != null)
			throw new Exception("BalanceCache invalido.", cacheException);
		String key = nroSistema.toString();
		Sistema sistema = mapSistema.get(key);
		return sistema;
	}
	
	/**
	 * Busca Partida por Id
	 * 
	 * @param idPartida
	 * @return
	 * @throws Exception
	 */
	public synchronized Partida getPartidaById(Long idPartida) throws Exception {
		if (cacheException != null)
			throw new Exception("BalanceCache invalido.", cacheException);
		String key = idPartida.toString();
		Partida partida = mapPartida.get(key);
		return partida;
	}
	
	/**
	 * Busca la Cuenta Bancaria vigente para la partida de codigo pasado
	 * 
	 * @param codPartida
	 * @return cuentaBanco
	 * @throws Exception
	 */
	public synchronized CuentaBanco getCuentaBancoByCodPartida(String codPartida) throws Exception {
		if (cacheException != null)
			throw new Exception("BalanceCache invalido.", cacheException);
		String key = codPartida;
		CuentaBanco cuentaBanco = mapCuentaBanco.get(key);
		return cuentaBanco;
	}
}
