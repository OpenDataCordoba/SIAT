//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.CompFuente;
import ar.gov.rosario.siat.ef.buss.bean.CompFuenteCol;

public class CompFuenteColDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(CompFuenteColDAO.class);
	
	public CompFuenteColDAO() {
		super(CompFuenteCol.class);
	}
	
	/**
	 * Obtiene un CompFuenteCol por su codigo
	 */
	public CompFuenteCol getByCodigo(String codigo) throws Exception {
		CompFuenteCol compFuenteCol;
		String queryString = "from CompFuenteCol t where t.codCompFuenteCol = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		compFuenteCol = (CompFuenteCol) query.uniqueResult();	

		return compFuenteCol; 
	}
	
	public void deleteByCompFuente(CompFuente compFuente){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "DELETE CompFuenteCol c WHERE c.compFuente.id = "+compFuente.getId();
		
		Query query = session.createQuery(queryString);
		
		query.executeUpdate();
	}
	
	public List<CompFuenteCol> getListByCompFuenteSuma(CompFuente compFuente){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM CompFuenteCol c WHERE c.compFuente.id ="+compFuente.getId();
		
		queryString += " AND c.sumaEnTotal = 1";
		
		Query query = session.createQuery(queryString);
		
		
		return (List<CompFuenteCol>)query.list();
	}
	
}
