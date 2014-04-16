//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.ValEmiMat;
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ValEmiMatDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ValEmiMatDAO.class);
	
	public ValEmiMatDAO() {
		super(ValEmiMat.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<ValEmiMat> getBySearchPage(ValEmiMatSearchPage valEmiMatSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ValEmiMat t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ValEmiMatSearchPage: " + valEmiMatSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (valEmiMatSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui

		// filtro por
 		RecursoVO recursoVO = valEmiMatSearchPage.getValEmiMat().getEmiMat().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recursoVO)) {
 			queryString += flagAnd ? " and " : " where ";
 			queryString += "t.emiMat.recurso.id = " + recursoVO.getId();
 			flagAnd = true;
 		}
		
 		// Order By
		queryString += " order by t.emiMat.codEmiMat ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ValEmiMat> listValEmiMat = (ArrayList<ValEmiMat>) executeCountedSearch(queryString, valEmiMatSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listValEmiMat;
	}

	@SuppressWarnings("unchecked")
	public List<ValEmiMat> getListBy(Long idRecurso) {
		 
	 	String strQuery = ""; 
	 	strQuery += "from ValEmiMat v ";
	 	strQuery += "where v.emiMat.recurso.id = " + idRecurso;
	 	
	 	Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);

		return (ArrayList<ValEmiMat>) query.list(); 
	 }
	
	/**
	 * Obtiene una valoriacion para la Matriz de Emision de id pasado como parametro.
	 *  
	 */
	public ValEmiMat getByIdEmiMat(Long idEmiMat) throws Exception {
		ValEmiMat valEmiMat;
		String queryString = "from ValEmiMat t where t.emiMat.id = :idEmiMat";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("idEmiMat", idEmiMat);
		valEmiMat = (ValEmiMat) query.uniqueResult();	

		return valEmiMat; 
	}
}
