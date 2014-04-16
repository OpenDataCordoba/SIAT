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

import ar.gov.rosario.siat.bal.buss.bean.Clasificador;
import ar.gov.rosario.siat.bal.buss.bean.Nodo;
import ar.gov.rosario.siat.bal.iface.model.NodoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.NodoVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;


public class NodoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(NodoDAO.class);	
	
	public NodoDAO(){
		super(Nodo.class);
	}
	
	public List<Nodo> getListBySearchPage(NodoSearchPage nodoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Nodo t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del NodoSearchPage: " + nodoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (nodoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Asentamiento excluidos
 		List<NodoVO> listNodoExcluidos = (ArrayList<NodoVO>) nodoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listNodoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listNodoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Clasificador
 		if(!ModelUtil.isNullOrEmpty(nodoSearchPage.getNodo().getClasificador())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.clasificador = " +  nodoSearchPage.getNodo().getClasificador().getId();
			flagAnd = true;
		}
		
 		queryString += " order by t.codigo ";
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    List<Nodo> listNodo = (ArrayList<Nodo>) executeSearch(queryString);
	    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listNodo;
	}
	
	/**
	 *  Devuelve la lista de Nodos para el Nivel indicado
	 * 
	 * @param nivel
	 * @return
	 */
	public List<Nodo> getListActivosByNivel(Integer nivel) {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Nodo t ";
	    
		// Armamos filtros del HQL 		
        queryString += " where t.nivel = "+nivel;
        queryString += " order by t.codigo ";
                
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Nodo> listNodo = (ArrayList<Nodo>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listNodo; 
	}
	
	/**
	 *  Devuelve la lista de Nodos para el Clasificador y Nivel indicados
	 * 
	 * @param clasificador
	 * @param nivel
	 * @return
	 */
	public List<Nodo> getListActivosByClasificadorYNivel(Clasificador clasificador,Integer nivel) {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Nodo t ";
	    
		// Armamos filtros del HQL 		
        queryString += " where t.clasificador.id = "+clasificador.getId();
        queryString += " and t.nivel = "+nivel;
        queryString += " order by t.codigo ";
        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Nodo> listNodo = (ArrayList<Nodo>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listNodo; 
	}
	
	/**
	 *  Devuelve la lista de Nodos para el Clasificador
	 * 
	 * @param clasificador
	 * @return
	 */
	public List<Nodo> getListActivosByClasificador(Clasificador clasificador) {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Nodo t ";
	    
		// Armamos filtros del HQL 		
        queryString += " where t.clasificador.id = "+clasificador.getId();
        queryString += " order by t.codigo ";
        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Nodo> listNodo = (ArrayList<Nodo>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listNodo; 
	}
	
	
	/**
	 *  Devuelve la lista de Nodos Hojas (que no tienen hijos para el Clasificador)
	 * 
	 * @param clasificador
	 * @return
	 */
	public List<Nodo> getListNodosHojasByClasificador(Clasificador clasificador) {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		/*
		 select n from bal_nodo n where n.idClasificador=2 and n.id not in (select c.idNodoPadre from bal_nodo c where c.idClasificador=2 and c.idNodoPadre is not null);
		 */ 		
		String queryString = "select * from bal_nodo n where n.idClasificador="+clasificador.getId()+
						"and n.id not in (select c.idNodoPadre from bal_nodo c where c.idClasificador="+clasificador.getId()+
						"and c.idNodoPadre is not null)";
	            
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createSQLQuery(queryString).addEntity(Nodo.class);
	    
	    List<Nodo> listNodo = (ArrayList<Nodo>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listNodo; 
	}
	

	/**
	 *  Devuelve el Nodo por Clasificador, Nivel y Codigos indicados
	 * 
	 * @param idClasificador
	 * @param nivel
	 * @param codigo
	 * @return
	 */
	public Nodo getByIdClaNivelIdNodoPadreYCod(Long idClasificador, Integer nivel, Long idNodoPadre, String codigo) {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Nodo t ";
	    
		// Armamos filtros del HQL 		
        queryString += " where t.clasificador.id = " +idClasificador;
        queryString += " and t.nivel = "+nivel;
        if(idNodoPadre != null)
        	queryString += " and t.nodoPadre.id = "+idNodoPadre;
        queryString += " and t.codigo = '"+codigo+"'";
                
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Nodo nodo = (Nodo) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return nodo; 
	}
}
