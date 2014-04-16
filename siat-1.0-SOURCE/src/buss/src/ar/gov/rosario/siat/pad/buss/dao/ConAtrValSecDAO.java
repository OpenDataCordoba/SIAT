//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.ConAtrValSec;


public class ConAtrValSecDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConAtrValSecDAO.class);
	
	public ConAtrValSecDAO() {
		super(ConAtrValSec.class);
	}

	/**
	 * Obtiene un ConAtrValSec por su codigo
	 */
	public ConAtrValSec getByCodigo(String codigo) throws Exception {
		ConAtrValSec conAtrValSec;
		String queryString = "from ConAtrValSec t where t.codConAtrValSec = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		conAtrValSec = (ConAtrValSec) query.uniqueResult();	

		return conAtrValSec; 
	}
	
}
