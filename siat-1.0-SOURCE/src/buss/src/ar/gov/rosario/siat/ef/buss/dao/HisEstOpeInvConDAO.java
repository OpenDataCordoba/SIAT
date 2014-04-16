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
import ar.gov.rosario.siat.ef.buss.bean.HisEstOpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvCon;

public class HisEstOpeInvConDAO extends GenericDAO {

	private Log log = LogFactory.getLog(HisEstOpeInvConDAO.class);
	
	public HisEstOpeInvConDAO() {
		super(HisEstOpeInvCon.class);
	}
	
	/**
	 * Obtiene un HisEstOpeInvCon por su codigo
	 */
	public HisEstOpeInvCon getByCodigo(String codigo) throws Exception {
		HisEstOpeInvCon hisEstOpeInvCon;
		String queryString = "from HisEstOpeInvCon t where t.codHisEstOpeInvCon = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		hisEstOpeInvCon = (HisEstOpeInvCon) query.uniqueResult();	

		return hisEstOpeInvCon; 
	}

	/**
	 * Elimina los registro de historico para el valor pasado como parametro
	 * @param opeInvCon
	 */
	public void delete(OpeInvCon opeInvCon){
		log.debug("delete - enter");
		String queryString = "Delete from HisEstOpeInvCon t where t.opeInvCon = :opeInvCon";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("opeInvCon", opeInvCon);
		query.executeUpdate();
		log.debug("delete - exit");
	}
}
