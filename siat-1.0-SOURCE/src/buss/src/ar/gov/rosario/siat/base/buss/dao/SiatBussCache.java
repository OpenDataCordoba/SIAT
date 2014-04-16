//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.buss.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.def.buss.bean.ConAtr;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.Common;

/**
 * Singleton de Caches de Siat
 * @author Coop. Tecso Ltda.
 *
 */
public class SiatBussCache extends Common {
	
	private static Log log = LogFactory.getLog(SiatBussCache.class);
	private static final SiatBussCache INSTANCE = new SiatBussCache();
	
	/*
	 *	key = id de Tipo de Objeto Imponible  
	 *  value = lista de TipObjImpAtr, osea la definicion: TipObjImpAtr, Atributo, DomAtr y DomAtrVal 
	 * 
	 */
	private HashMap<Long, List<TipObjImpAtr>> tipObjImpAtrDefinitionMap = new HashMap<Long, List<TipObjImpAtr>>();  
	
	private List<ConAtr> listActivosForWeb = null;
	
	
	/*
	 * Definicion de Atributos de Recurso/Cuenta 
	 * 
	 * Recurso.listRecAtrCue 
	 *  	Cuenta.listRecAtrCueV
	 *   
	 */
	private HashMap<Long, RecursoDefinition> listRecursoDefinitionMap = new HashMap<Long, RecursoDefinition>(); 
	
	/**
	 * Constructor privado
	 */
	private SiatBussCache() {
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static SiatBussCache getInstance() {
		return INSTANCE;
	}

	/**
	 * Devuelve la definicion de un tipo de objeto imponible, desde cache si existe, sino lo carga y lo devuelve.
	 * 
	 * @param idTipObjImp
	 * @return
	 * @throws DemodaServiceException
	 */
	synchronized public List<TipObjImpAtr> getListTipObjImpAtrManual(Long idTipObjImp) throws Exception {
		
		//TODO: Ver el tema de la fecha
		
		if (tipObjImpAtrDefinitionMap.get(idTipObjImp) == null ){
			
			List<TipObjImpAtr> listTipObjImpAtrManual = DefDAOFactory.getTipObjImpAtrDAO().
					getListVigentesByIdTipObjImp(TipObjImp.FOR_MANUAL, idTipObjImp, new Date());
			
			ListUtilBean.toVO(listTipObjImpAtrManual, 6);
			tipObjImpAtrDefinitionMap.put(idTipObjImp, listTipObjImpAtrManual);
			
			log.debug("SiatBussCache -> recuperando List<TipObjImpAtr> definicion from DB...");
			
		} else {
			log.debug("SiatBussCache -> devolviendo List<TipObjImpAtr> definicion from Cache...");
		}
		
		return this.tipObjImpAtrDefinitionMap.get(idTipObjImp);
	}
	
	/**
	 * Recarga el cache de definicion de Tipo de Objeto Imponible 
	 * 
	 * 
	 * @param idTipObjImp
	 * @throws Exception 
	 */
	synchronized public void reloadTipObjImpAtr(Long idTipObjImp) throws Exception {
		log.debug("SiatBussCache -> idTipObjImp=" + idTipObjImp + " recargando Cache ...");
		List<TipObjImpAtr> listTipObjImpAtrManual = DefDAOFactory.getTipObjImpAtrDAO().
			getListVigentesByIdTipObjImp(TipObjImp.FOR_MANUAL, idTipObjImp, new Date());
		ListUtilBean.toVO(listTipObjImpAtrManual, 6);
		tipObjImpAtrDefinitionMap.put(idTipObjImp, listTipObjImpAtrManual);
	}
	
	synchronized public void reloadAllTipObjImpAtr() throws Exception {
		List<TipObjImp> list = DefDAOFactory.getTipObjImpDAO().getListActiva();
		for(TipObjImp tipObjImp : list) {
			reloadTipObjImpAtr(tipObjImp.getId());
		}		
	}

	
	/**
	 * Devuelve la definicion de los atributos del contribuyente.
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	synchronized public List<ConAtr> getListActivosForWeb() throws Exception {
		
		if (listActivosForWeb == null){
			log.debug("SiatBussCache -> recuperando List<ConAtr> definicion from DB...");
			listActivosForWeb = ConAtr.getListActivosForWeb();
		} else {
			log.debug("SiatBussCache -> devolviendo List<ConAtr> definicion from Cache...");
		}
		
		
		return this.listActivosForWeb;
	}

	synchronized public void setListActivosForWeb(List<ConAtr> listActivosForWeb) {
		this.listActivosForWeb = listActivosForWeb;
	}

	
	/**
	 * Devuelve la definicion de los atributos de recurso / cuenta.
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	synchronized public RecursoDefinition getAtributoRecursoCuentaDefinition(Long idRecurso) throws Exception {
		
		if (listRecursoDefinitionMap.get(idRecurso) == null ){
			RecursoDefinition recursoDefinition = Recurso.getDefinitionRecAtrCueValue(idRecurso);
			
			listRecursoDefinitionMap.put(idRecurso, recursoDefinition);
			log.debug("SiatBussCache -> recuperando AtributoRecursoCuentaDefinition from DB...");
		} else {
			log.debug("SiatBussCache -> devolviendo AtributoRecursoCuentaDefinition from Cache...");
		}
		
		return this.listRecursoDefinitionMap.get(idRecurso);
	}
	
	/**
	 * Recarga la definicion de los atributos de recurso / cuenta para un recurso.  
	 * 
	 * @param idRecurso
	 * @throws Exception
	 */
	synchronized public void reloadAtributoRecursoCuentaDefinition(Long idRecurso) throws Exception {
		RecursoDefinition recursoDefinition = Recurso.getDefinitionRecAtrCueValue(idRecurso);
		listRecursoDefinitionMap.put(idRecurso, recursoDefinition);
	}
	
	synchronized public void reloadAllAtributoRecursoCuentaDefinition() throws Exception {
		
		Set<Long> listIdRecurso = listRecursoDefinitionMap.keySet();
		
		for (Long idRecurso:listIdRecurso){
			RecursoDefinition recursoDefinition = Recurso.getDefinitionRecAtrCueValue(idRecurso);
			listRecursoDefinitionMap.put(idRecurso, recursoDefinition);
		}
	}
	
	
}	
