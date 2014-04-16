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
import ar.gov.rosario.siat.ef.buss.bean.CompFuenteRes;
import ar.gov.rosario.siat.ef.buss.bean.Comparacion;

public class CompFuenteResDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(CompFuenteResDAO.class);
	
	public CompFuenteResDAO() {
		super(CompFuenteRes.class);
	}
	
	/**
	 * Obtiene un CompFuenteRes por su codigo
	 */
	public CompFuenteRes getByCodigo(String codigo) throws Exception {
		CompFuenteRes compFuenteRes;
		String queryString = "from CompFuenteRes t where t.codCompFuenteRes = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		compFuenteRes = (CompFuenteRes) query.uniqueResult();	

		return compFuenteRes; 
	}

	
	public List<CompFuenteRes> getListByOrdCon(Long idOrdenControl) {
		String queryString = "from CompFuenteRes t where t.comparacion.ordenControl.id = "+idOrdenControl;
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);

		return query.list();
	}
	
	public void deleteListByComparacion(Comparacion comparacion){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "DELETE CompFuenteRes c where c.comparacion.id ="+comparacion.getId();
		
		Query query = session.createQuery(queryString);
		
		query.executeUpdate();
		
	}
	
}
