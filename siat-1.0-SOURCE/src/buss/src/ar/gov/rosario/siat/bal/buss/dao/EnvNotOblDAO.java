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

import ar.gov.rosario.siat.bal.buss.bean.CierreBanco;
import ar.gov.rosario.siat.bal.buss.bean.EnvNotObl;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class EnvNotOblDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EnvNotOblDAO.class);
	
	public EnvNotOblDAO() {
		super(EnvNotObl.class);
	}
	
//	public List<EnvNovObl> getBySearchPage(EnvNovOblSearchPage envNovOblSearchPage) throws Exception {
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
//		
//		String queryString = "from EnvNovObl t ";
//	    boolean flagAnd = false;
//
//		if (log.isDebugEnabled()) { 
//			log.debug("log de filtros del EnvNovOblSearchPage: " + envNovOblSearchPage.infoString()); 
//		}
//	
//		// Armamos filtros del HQL
//		if (envNovOblSearchPage.getModoSeleccionar()) {
//		  queryString += flagAnd ? " and " : " where ";
//	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
//	      flagAnd = true;
//		}
//		
//		// Filtros aqui
//		/* Ejemplos:
//		
//		// filtro envNovObl excluidos
// 		List<EnvNovOblVO> listEnvNovOblExcluidos = (List<EnvNovOblVO>) envNovOblSearchPage.getListVOExcluidos();
// 		if (!ListUtil.isNullOrEmpty(listEnvNovOblExcluidos)) {
// 			queryString += flagAnd ? " and " : " where ";
//
// 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listEnvNovOblExcluidos);
//			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
//			flagAnd = true;
//		}
//		
//		// filtro por codigo
// 		if (!StringUtil.isNullOrEmpty(envNovOblSearchPage.getEnvNovObl().getCodEnvNovObl())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.codEnvNovObl)) like '%" + 
//				StringUtil.escaparUpper(envNovOblSearchPage.getEnvNovObl().getCodEnvNovObl()) + "%'";
//			flagAnd = true;
//		}
//
//		// filtro por descripcion
// 		if (!StringUtil.isNullOrEmpty(envNovOblSearchPage.getEnvNovObl().getDesEnvNovObl())) {
//            queryString += flagAnd ? " and " : " where ";
//			queryString += " UPPER(TRIM(t.desEnvNovObl)) like '%" + 
//				StringUtil.escaparUpper(envNovOblSearchPage.getEnvNovObl().getDesEnvNovObl()) + "%'";
//			flagAnd = true;
//		}
// 		
// 		// Order By
//		queryString += " order by t.codEnvNovObl ";
//		*/
//		
//	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
//
//		List<EnvNovObl> listEnvNovObl = (ArrayList<EnvNovObl>) executeCountedSearch(queryString, envNovOblSearchPage);
//		
//		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
//		return listEnvNovObl;
//	}
	
	/**
	 * Obtiene la lista de Notas de Obligación para el Banco y CierreBanco que se va a conciliar 
	 * @param cierreBanco
	 * @return
	 */
	public List<EnvNotObl> getListEnvNotOblForCierreBanco(CierreBanco cierreBanco){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM EnvNotObl e WHERE (e.banco ="+cierreBanco.getBanco();
			   queryString +=" AND e.nroCierreBanco ="+cierreBanco.getNroCierreBanco()+") ";
			   queryString +=" OR (e.bancoOriginal ="+cierreBanco.getBanco();
			   queryString +=" AND e.nroCieBanOrig ="+cierreBanco.getNroCierreBanco()+") ";
		
		Query query = session.createQuery(queryString);
		
		return (ArrayList<EnvNotObl>) query.list();
	}

	/**
	 * Obtiene la sumatoria de Totales Acreditados en los EnvNotObl
	 * 
	 * @param cierreBanco
	 * @return
	 */
	public Double getTotalAcreditado(CierreBanco cierreBanco){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT SUM(e.totalAcreditado) FROM EnvNotObl e ";
			   queryString +=" WHERE e.bancoOriginal ="+cierreBanco.getBanco();
			   queryString +=" AND e.nroCieBanOrig ="+cierreBanco.getNroCierreBanco();
		
		Query query = session.createQuery(queryString);
				
		Double total = (Double) query.uniqueResult();
		
		if (null == total) return 0D;
		return total;
	}
	
	
	/**
	 * Obtiene sumatoria de TotalCredito de transacciones rendidas en los EnvNotObl
	 * 
	 * @param cierreBanco
	 * @return
	 */
	public Double getTotalRendidoForCierreBanco(CierreBanco cierreBanco){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT SUM(e.totalCredito) FROM EnvNotObl e ";
			   queryString +=" WHERE e.banco ="+cierreBanco.getBanco();
			   queryString +=" AND e.nroCierreBanco ="+cierreBanco.getNroCierreBanco();
		
		Query query = session.createQuery(queryString);
				
		Double total = (Double) query.uniqueResult();
		
		if (null == total) return 0D;
		return total;
	}
}
