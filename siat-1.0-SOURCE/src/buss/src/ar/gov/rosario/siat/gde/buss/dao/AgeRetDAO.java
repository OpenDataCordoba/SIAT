//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.AgeRet;
import ar.gov.rosario.siat.gde.iface.model.AgeRetSearchPage;
import ar.gov.rosario.siat.gde.iface.model.AgeRetVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AgeRetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AgeRetDAO.class);	
	
	public AgeRetDAO() {
		super(AgeRet.class);
	}
	

	public List<AgeRet> getBySearchPage(AgeRetSearchPage ageRetSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from AgeRet t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AgeRetSearchPage: " + ageRetSearchPage.infoString()); 
		}
		
		// Armamos filtros del HQL
		if (ageRetSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		
		// filtro mandatario excluidos
 		List<AgeRetVO> listAgeRetExcluidos = (List<AgeRetVO>) ageRetSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listAgeRetExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAgeRetExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(ageRetSearchPage.getAgeRet().getDesAgeRet())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desAgeRet)) like '%" + 
				StringUtil.escaparUpper(ageRetSearchPage.getAgeRet().getDesAgeRet()) + "%'";
			flagAnd = true;
		}
		// filtro por Recurso
		if (!ModelUtil.isNullOrEmpty(ageRetSearchPage.getAgeRet().getRecurso())){
			
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + ageRetSearchPage.getAgeRet().getRecurso().getId();
			flagAnd = true;
		}
  		
 		queryString += " order by t.id ";
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		List<AgeRet> listAgeRet = (ArrayList<AgeRet>) executeCountedSearch(queryString, ageRetSearchPage);
		
		log.debug("EN GETY BY SEARCH PAGE DESPUES DE LA LISTA");
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAgeRet;
	}

	
	
	/**
	 * Retorna la lista de AgeRet para un recurso dado
	 * 
	 * @param area
	 * @return
	 */
	public List<AgeRet> getListActivosByRecurso(Recurso recurso){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM AgeRet p WHERE p.estado = "+Estado.ACTIVO.getId();
		
		if (recurso!=null)
			queryString += " AND p.recurso.id = "+recurso.getId();
		
		Query query = session.createQuery(queryString);
		
		return (List<AgeRet>)query.list();
		
	}
	
	/**
	 * Obtiene el Agente de Retencion para un CUIT y Recurso dado
	 * 
	 * @param cuit
	 * @param recurso
	 * @return
	 */
	public AgeRet getByCuitYRecurso(String cuit, Long idRecurso) {
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM AgeRet p WHERE " + "p.recurso.id = "+idRecurso;
			 
		       queryString += " AND p.cuit = '"+cuit+"'";
			   queryString += " AND p.estado = "+Estado.ACTIVO.getId();
				
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		
		return (AgeRet) query.uniqueResult();
	}
}
