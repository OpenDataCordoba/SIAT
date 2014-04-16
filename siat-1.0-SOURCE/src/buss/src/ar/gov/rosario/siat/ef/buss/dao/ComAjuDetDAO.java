//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.ComAjuDet;

public class ComAjuDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ComAjuDetDAO.class);
	
	public ComAjuDetDAO() {
		super(ComAjuDet.class);
	}
	
	/**
	 * Obtiene un ComAjuDet por su codigo
	 */
	public ComAjuDet getByCodigo(String codigo) throws Exception {
		ComAjuDet comAjuDet;
		String queryString = "from ComAjuDet t where t.codComAjuDet = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		comAjuDet = (ComAjuDet) query.uniqueResult();	

		return comAjuDet; 
	}
	
}
