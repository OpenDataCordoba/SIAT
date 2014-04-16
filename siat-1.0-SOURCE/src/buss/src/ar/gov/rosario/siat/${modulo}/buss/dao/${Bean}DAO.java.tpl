package ar.gov.rosario.siat.${modulo}.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.classic.Session;
import org.hibernate.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.${modulo}.buss.bean.${Bean};
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}SearchPage;
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}VO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ${Bean}DAO extends GenericDAO {

	private Log log = LogFactory.getLog(${Bean}DAO.class);
	
	public ${Bean}DAO() {
		super(${Bean}.class);
	}
	
	public List<${Bean}> getBySearchPage(${Bean}SearchPage ${bean}SearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ${Bean} t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ${Bean}SearchPage: " + ${bean}SearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (${bean}SearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro ${bean} excluidos
 		List<${Bean}VO> list${Bean}Excluidos = (List<${Bean}VO>) ${bean}SearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(list${Bean}Excluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(list${Bean}Excluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(${bean}SearchPage.get${Bean}().getCod${Bean}())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.cod${Bean})) like '%" + 
				StringUtil.escaparUpper(${bean}SearchPage.get${Bean}().getCod${Bean}()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(${bean}SearchPage.get${Bean}().getDes${Bean}())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.des${Bean})) like '%" + 
				StringUtil.escaparUpper(${bean}SearchPage.get${Bean}().getDes${Bean}()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.cod${Bean} ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<${Bean}> list${Bean} = (ArrayList<${Bean}>) executeCountedSearch(queryString, ${bean}SearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return list${Bean};
	}

	/**
	 * Obtiene un ${Bean} por su codigo
	 */
	public ${Bean} getByCodigo(String codigo) throws Exception {
		${Bean} ${bean};
		String queryString = "from ${Bean} t where t.cod${Bean} = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		${bean} = (${Bean}) query.uniqueResult();	

		return ${bean}; 
	}
	
}
