//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.iface.model.PartidaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PartidaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PartidaDAO.class);	
	
	public PartidaDAO(){
		super(Partida.class);
	}
	
	/**
	 * Devuelve la Lista de Partidas Activas ordenadas por Codigo
	 * 
	 * @return
	 */
	public List<Partida> getListActivaOrdenadasPorCodigo(){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Partida t ";
	    queryString += " where t.estado = "+ Estado.ACTIVO.getId(); 		
		queryString += " order by t.codPartida ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Partida> listPartida = (ArrayList<Partida>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPartida;
	}
	
	/**
	 * Devuelve la Lista de Partidas Activas del Ejercicio Actual (campo esActual=1)
	 * @return
	 */
	public List<Partida> getListActivaActual(){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Partida t ";
	    queryString += " where t.estado = "+ Estado.ACTIVO.getId(); 		
        queryString += " and t.esActual = 1";
		queryString += " order by t.desPartida ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Partida> listPartida = (ArrayList<Partida>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPartida;
	}
	
	/**
	 * Devuelve la Lista de Partidas 
	 * @return
	 */
	public List<Partida> getListBySearchPage(
			PartidaSearchPage partidaSearchPage) throws Exception {
		
		String queryString = "from Partida t ";
	    boolean flagAnd = false;

	
		// Armamos filtros del HQL
		if (partidaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      flagAnd = true;
		}
		
		// Filtros aqui
		//	filtro Partido excluidos
 		List<PartidaVO> listCueExeExcluidos = (ArrayList<PartidaVO>) partidaSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCueExeExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCueExeExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por desPartida
 		if (!StringUtil.isNullOrEmpty(partidaSearchPage.getPartida().getDesPartida())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desPartida)) like '%" + 
			StringUtil.escaparUpper(partidaSearchPage.getPartida().getDesPartida()) + "%'";
			flagAnd = true;
		}
 		
     	// filtro por codPartida
   		if (!StringUtil.isNullOrEmpty(partidaSearchPage.getPartida().getCodPartida())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codPartida)) like '%" + 
			StringUtil.escaparUpper(partidaSearchPage.getPartida().getCodPartida())+"%'";
			flagAnd = true;
		}
 		 		
 	
		List<Partida> listPartida = (ArrayList<Partida>) 
			executeCountedSearch(queryString, partidaSearchPage);
		
		return listPartida;
	}
	
	/**
	 * Devuelve la Lista de Partidas Activas de Ejercicio No Actual (campo esActual=0)
	 * @return
	 */
	public List<Partida> getListActivaNoActual(){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Partida t ";	    
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
        queryString += " and t.esActual = 0";
		queryString += " order by t.desPartida ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Partida> listPartida = (ArrayList<Partida>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPartida;
	}
	
	/**
	 * Obtiene la partida por el codigo pasado como parametro.
	 * @return
	 */
	public Partida getByCod(String codPartida){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Partida t ";
	    queryString += " where t.codPartida = '"+ codPartida+"'"; 		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Partida partida = (Partida) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return partida;
	}
}
