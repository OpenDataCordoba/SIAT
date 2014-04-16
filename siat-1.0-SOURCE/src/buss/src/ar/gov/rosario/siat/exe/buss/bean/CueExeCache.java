//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.SiNo;


public class CueExeCache {
	
	private static Logger log = Logger.getLogger(CueExeCache.class);
	
	private Map<Long, List<CueExe>> mapCueExe = null; 

	public boolean initialize(Recurso recurso, Date fecha) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// Instanciamos el mapa del cache
		this.mapCueExe =  new HashMap<Long, List<CueExe>>();

		List<CueExe> listCueExeVigentes = ExeDAOFactory.getCueExeDAO().initializeCache(recurso,fecha);

		for (CueExe cueExe: listCueExeVigentes) {
			Long idCuenta = cueExe.getIdCuenta();
			List<CueExe> listCueExe = mapCueExe.get(idCuenta);

			if (ListUtil.isNullOrEmpty(listCueExe)) 
				listCueExe =  new ArrayList<CueExe>();

			listCueExe.add(cueExe);
			mapCueExe.put(idCuenta, listCueExe);
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return true;
	}
	
	public boolean initialize(Recurso recurso) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// Instanciamos el mapa del cache
		this.mapCueExe =  new HashMap<Long, List<CueExe>>();

		List<CueExe> allCueExe = ExeDAOFactory.getCueExeDAO().initializeCache(recurso);

		for (CueExe cueExe: allCueExe) {
			Long idCuenta = cueExe.getIdCuenta();
			List<CueExe> listCueExe = mapCueExe.get(idCuenta);

			if (ListUtil.isNullOrEmpty(listCueExe)) 
				listCueExe =  new ArrayList<CueExe>();

			listCueExe.add(cueExe);
			mapCueExe.put(idCuenta, listCueExe);
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return true;
	}

	/**
	 * Retorna las lista de CueExe asociadas a la cuenta
	 * en el cache
	 */
	public List<CueExe> getListCueExe(Long idCuenta) {
		try {
			return mapCueExe.get(idCuenta);
		}
		catch (Exception e) {
			log.error("No se pudo obtener la entrada en el cache para la cuenta con id " + idCuenta, e);
			return null;
		}
	}

	/**
	 * Retorna las lista de CueExe asociadas a la cuenta en
	 * el cache vigentes a la fecha pasada como parametro
	 */
	public List<CueExe> getListCueExe(Long idCuenta, Date fechaAnalisis) {
		try {
			
			List<CueExe> listCueExe = mapCueExe.get(idCuenta);
			
			if (listCueExe == null) return null;

			List<CueExe> listRet = new ArrayList<CueExe>();
			
			for (CueExe cueExe: listCueExe) {
				if (cueExe.getEsVigente(fechaAnalisis)) {
					listRet.add(cueExe);
				}
			}
					
			return listRet;
		}
		catch (Exception e) {
			log.error("No se pudo obtener la entrada en el cache para la cuenta con id " + idCuenta, e);
			return null;
		}
	}
	
	/**
	 * Retorna las lista de Exenciones asociadas a cada CueExe Vigente de a la cuenta.
	 * Llamar a este metodo es lo mismo que llamar a getListCueExe() 
	 * y obtener la Exencion de cada CueExe de la lista.
	 * Si la cuenta no tiene exenciones, retorna una lista vacia.
	 */
	public List<Exencion> getListExencion(Long idCuenta) {
		try {
			List<Exencion> listExencion = new ArrayList<Exencion>();
			List<CueExe> list = this.getListCueExe(idCuenta);
			
			if (list != null) {
				for (CueExe cueExe: this.getListCueExe(idCuenta)) 
					listExencion.add(cueExe.getExencion());
			}

			return listExencion;
		}
		catch (Exception e) {
			log.error("No se pudo obtener la entrada en el cache para la cuenta con id " + idCuenta, e);
			return null;
		}
	}

	/**
	 * Imprime el contenido del cache 
	 */
	public void printContent() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		log.debug("Imprimiendo contenido del cache");

		for (Long idCuenta : this.mapCueExe.keySet()) {
		
			log.debug("Exenciones de la cuenta con id" + idCuenta);

			for (CueExe cueExe : this.getListCueExe(idCuenta)) {
				
				log.debug(" Exencion" + cueExe.getExencion().getDesExencion());

				if (cueExe.getTipoSujeto() != null) 
					log.debug(" Tipo Sujeto"+ cueExe.getTipoSujeto().getDesTipoSujeto());
				
			}
		
		}
	
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	}

	/**
	 *  Arma las listas del mapa con Exenciones que no actualizan deuda.
	 * 
	 * @return
	 */
	public boolean initializeEspecial() {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");

		try {
			
			// Instanciamos el mapa del cache
			this.mapCueExe =  new HashMap<Long, List<CueExe>>();
			
			List<CueExe> listCueExeNoAct = ExeDAOFactory.getCueExeDAO().getListNoActDeuda();
			
			for (CueExe cueExe: listCueExeNoAct) {
			
				Long idCuenta = cueExe.getIdCuenta();
	
				List<CueExe> listCueExe = mapCueExe.get(idCuenta);
				
				if (ListUtil.isNullOrEmpty(listCueExe)) 
					listCueExe =  new ArrayList<CueExe>();

				listCueExe.add(cueExe);
				mapCueExe.put(idCuenta, listCueExe);
			}

			if (log.isDebugEnabled())
				log.debug(funcName + ": exit");
			return true;
		}
		catch (Exception e) {
			log.error("No se pudo inicializar el cache: Excepcion ", e);
			return false;
		}
	}
	
	/**
	 * Retorna las lista de CueExe asociadas a la cuenta en el cache
	 * Y que tiene seteada la marca EnviaJudicial == 0.
	 * Si la marca esta null se interpreta como que se envia.
	 */
	public List<CueExe> getListCueExeNoEnviaJudicial(Long idCuenta) {
		try {
			List<CueExe> ret = null;
			List<CueExe> listCueExe = mapCueExe.get(idCuenta);
			
			if (listCueExe == null)
				return null;
			
			ret = new ArrayList<CueExe>();
			for(CueExe cueExe: listCueExe) {
	    		if (SiNo.NO.getId().equals(cueExe.getExencion().getEnviaJudicial()) ) {
	    			ret.add(cueExe);
	    		}
			}
			return ret;
		} catch (Exception e) {
			log.error("No se pudo obtener la entrada en el cache para la cuenta con id " + idCuenta, e);
			return null;
		}
	}
}