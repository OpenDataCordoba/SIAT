//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.OrigenOrden;

public class OrigenOrdenDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OrigenOrdenDAO.class);
	
	public OrigenOrdenDAO() {
		super(OrigenOrden.class);
	}
	
	/**
	 * Obtiene un OrigenOrden por su codigo
	 */
	public OrigenOrden getByCodigo(String codigo) throws Exception {
		OrigenOrden origenOrden;
		String queryString = "from OrigenOrden t where t.codOrigenOrden = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		origenOrden = (OrigenOrden) query.uniqueResult();	

		return origenOrden; 
	}

	
	public List<OrigenOrden> getListProJud() {
		String queryString = "from OrigenOrden t where t.esProJud = 1";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		List<OrigenOrden> listOrigenOrden = query.list();	

		return listOrigenOrden; 

	}
	
	public List<OrigenOrden> getListNotOperativoActivos() {
		String queryString = "from OrigenOrden t where t.id != "+OrigenOrden.ID_OPERATIVOS;
		
		queryString += " AND t.estado=1";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		List<OrigenOrden> listOrigenOrden = query.list();	

		return listOrigenOrden; 

	}
	
}
