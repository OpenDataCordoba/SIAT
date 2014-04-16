//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cyq.buss.bean.Juzgado;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class JuzgadoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(JuzgadoDAO.class);	
	
	public JuzgadoDAO() {
		super(Juzgado.class);
	}
	
	public List<Juzgado> getBySearchPage(JuzgadoSearchPage juzgadoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Juzgado t ";
	    boolean flagAnd = false;
     
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del JuzgadoSearchPage: " + juzgadoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (juzgadoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro Juzgado excluidos
 		List<JuzgadoVO> listJuzgadoExcluidos = (List<JuzgadoVO>) juzgadoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listJuzgadoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listJuzgadoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
	 	 	
 	  // filtro por Descripcion Juzgado
 		if (!StringUtil.isNullOrEmpty(juzgadoSearchPage.getJuzgado().getDesJuzgado())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desJuzgado)) like '%" + 
				StringUtil.escaparUpper(juzgadoSearchPage.getJuzgado().getDesJuzgado()) + "%'";
			flagAnd = true;
		}

	
 		// Order By
		 queryString += " order by t.id ";
	
	   if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Juzgado> listJuzgado = (ArrayList<Juzgado>) executeCountedSearch(queryString, juzgadoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listJuzgado;
	}

	/**
	 * Obtiene un Juzgado por su codigo
	 */
	public Juzgado getByCodigo(String codigo) throws Exception {
		Juzgado juzgado;
		String queryString = "from Juzgado t where t.codJuzgado = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		juzgado = (Juzgado) query.uniqueResult();	

		return juzgado; 
	}
}
