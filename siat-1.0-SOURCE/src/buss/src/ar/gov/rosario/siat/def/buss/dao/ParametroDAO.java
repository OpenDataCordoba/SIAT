//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Parametro;
import ar.gov.rosario.siat.def.iface.model.ParametroSearchPage;
import ar.gov.rosario.siat.def.iface.model.ParametroVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ParametroDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ParametroDAO.class);	
	
	public ParametroDAO() {
		super(Parametro.class);
	}
	
	public List<Parametro> getBySearchPage(ParametroSearchPage parametroSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Parametro t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ParametroSearchPage: " + parametroSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (parametroSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro parametro excluidos
 		List<ParametroVO> listParametroExcluidos = (ArrayList<ParametroVO>) parametroSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listParametroExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listParametroExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codParam
 		if (!StringUtil.isNullOrEmpty(parametroSearchPage.getParametro().getCodParam())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codParam)) like '%" + 
				StringUtil.escaparUpper(parametroSearchPage.getParametro().getCodParam()) + "%'";
			flagAnd = true;
		}

		// filtro por desParam
 		if (!StringUtil.isNullOrEmpty(parametroSearchPage.getParametro().getDesParam())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desParam)) like '%" + 
				StringUtil.escaparUpper(parametroSearchPage.getParametro().getDesParam()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codParam ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Parametro> listParametro = (ArrayList<Parametro>) executeCountedSearch(queryString, parametroSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listParametro;
	}

	/**
	 * Obtiene un Parametro por su codigo
	 */
	public Parametro getByCodigo(String codigo) throws Exception {
		Parametro parametro;
		String queryString = "from Parametro t where t.codParam = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		parametro = (Parametro) query.uniqueResult();	

		return parametro; 
	}
	
}
