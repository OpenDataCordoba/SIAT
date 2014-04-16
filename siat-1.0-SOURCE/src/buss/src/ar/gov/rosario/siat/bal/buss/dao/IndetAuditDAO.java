//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.IndetAudit;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class IndetAuditDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(IndetAuditDAO.class);
	
	public IndetAuditDAO(){
		super(IndetAudit.class);
	}
	
	/**
	 * Obtiene un IndetAudit por su idOrigen
	 */
	public IndetAudit getByIdOrigen(Long idOrigen) {
		IndetAudit indetAudit;
		String queryString = "from IndetAudit t where t.idOrigen = "+idOrigen;
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		indetAudit = (IndetAudit) query.uniqueResult();	

		return indetAudit; 
	}
}

