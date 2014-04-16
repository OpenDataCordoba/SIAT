//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.buss.bean.SolicitudCUIT;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class SolicitudCUITDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SolicitudCUITDAO.class);
	
	public SolicitudCUITDAO() {
		super(SolicitudCUIT.class);
	}

	public SolicitudCUIT getBySolicitud(Solicitud solicitud) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from SolicitudCUIT WHERE solicitud= :solicitud";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
					  .setEntity("solicitud", solicitud);
		return (SolicitudCUIT) query.uniqueResult();
	}
	

}
