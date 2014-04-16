//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.DocSop;
import ar.gov.rosario.siat.ef.iface.model.DocSopSearchPage;
import ar.gov.rosario.siat.ef.iface.model.DocSopVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DocSopDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DocSopDAO.class);
	
	public DocSopDAO() {
		super(DocSop.class);
	}
	
	public List<DocSop> getBySearchPage(DocSopSearchPage docSopSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DocSop t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DocSopSearchPage: " + docSopSearchPage.infoString()); 
		}
		
		if (docSopSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
	      // filtra los vigentes
		 }
		
		// Filtros aqui		
		
		// filtro supervisor excluidos
 		List<DocSopVO> listDocSopExcluidos = (List<DocSopVO>) docSopSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDocSopExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDocSopExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}				

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(docSopSearchPage.getDocSop().getDesDocSop())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDocSop)) like '%" + 
				StringUtil.escaparUpper(docSopSearchPage.getDocSop().getDesDocSop()) + "%'";
			flagAnd = true;
		}
 		 		
 		// Order By
		queryString += " order by t.desDocSop ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DocSop> listDocSop = (ArrayList<DocSop>) executeCountedSearch(queryString, docSopSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDocSop;
	}
	/**
	 * Obtiene un DocSop por su codigo
	 */
	public DocSop getByCodigo(String codigo) throws Exception {
		DocSop docSop;
		String queryString = "from DocSop t where t.codDocSop = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		docSop = (DocSop) query.uniqueResult();	

		return docSop; 
	}
	
}
