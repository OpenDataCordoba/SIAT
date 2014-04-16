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
import ar.gov.rosario.siat.ef.buss.bean.OpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvConCue;


public class OpeInvConCueDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OpeInvConCueDAO.class);
	
	public OpeInvConCueDAO() {
		super(OpeInvConCue.class);
	}
	
	/**
	 *Elimina los registros de opeInvConCue para el valor pasado como parametro
	 */ 
	public void delete(OpeInvCon opeInvCon){
		log.debug("delete - enter");
		String queryString = "Delete from OpeInvConCue t where t.opeInvCon = :opeInvCon";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("opeInvCon", opeInvCon);
		query.executeUpdate();
		log.debug("delete - exit");
	}
}
