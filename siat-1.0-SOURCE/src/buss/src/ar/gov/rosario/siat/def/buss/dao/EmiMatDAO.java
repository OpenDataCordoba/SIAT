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
import ar.gov.rosario.siat.def.buss.bean.EmiMat;
import ar.gov.rosario.siat.def.iface.model.EmiMatSearchPage;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EmiMatDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EmiMatDAO.class);
	
	public EmiMatDAO() {
		super(EmiMat.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<EmiMat> getBySearchPage(EmiMatSearchPage emiMatSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from EmiMat t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EmiMatSearchPage: " + emiMatSearchPage.infoString()); 
		}

		// Filtros 

		// filtro por codigo
		 String codEmiMat = emiMatSearchPage.getEmiMat().getCodEmiMat();
		if (!StringUtil.isNullOrEmpty(codEmiMat)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codEmiMat)) like '%" + 
				StringUtil.escaparUpper(codEmiMat) + "%'";
			flagAnd = true;
		}

 		// filtro por Recurso
 		RecursoVO recursoVO = emiMatSearchPage.getEmiMat().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recursoVO)) {
 			queryString += flagAnd ? " and " : " where ";
 			queryString += "t.recurso.id = " + recursoVO.getId();
 			flagAnd = true;
 		}
 		
 		// Order By
		queryString += " order by t.recurso.desRecurso, t.codEmiMat";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<EmiMat> listEmiMat = (ArrayList<EmiMat>) executeCountedSearch(queryString, emiMatSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEmiMat;
	}

	/**
	 * Obtiene una Matriz de Emision por su codigo
	 */
	public EmiMat getByCodigo(String codEmiMat) throws Exception {
		EmiMat emiMat;
		String queryString = "from EmiMat t where t.codEmiMat = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codEmiMat);
		emiMat = (EmiMat) query.uniqueResult();	

		return emiMat; 
	}
	
	/**
	 * Obtiene la lista de matrices de emision activas
	 * asociadas al recurso con id idRecurso
	 * 
	 * @param idRecurso
	 * @return List<EmiMat>
	 */
	@SuppressWarnings("unchecked")
	public List<EmiMat> getListActivosBy(Long idRecurso) throws Exception {

		String queryString = "";
		queryString += "from EmiMat t where t.recurso.id = :idRecurso ";
		queryString += "order by t.recurso.codRecurso, t.recurso.desRecurso";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							 .setLong("idRecurso", idRecurso);

		return (ArrayList<EmiMat>) query.list();	
	}

}
