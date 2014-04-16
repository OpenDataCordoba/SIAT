//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.swe.buss.bean.UsrAplAdmSwe;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class UsrAplAdmiSweDAO extends GenericDAO {

	private Log log = LogFactory.getLog(UsrAplAdmiSweDAO.class);
	
	public UsrAplAdmiSweDAO() {
		super(UsrAplAdmSwe.class);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Elimina los registros de UsrAplAdmSwe (Aplicaciones permitidas) que contengan el idUsr
	 * @param idUsr
	 * @throws Exception
	 */
	public void deleteForUser(Long idUsr) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from UsrAplAdmSwe t where t.usrApl.id ="+idUsr;
		Session session = SweHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<UsrAplAdmSwe> listUsrAplAdmSwe = (ArrayList<UsrAplAdmSwe>) query.list();
		for(UsrAplAdmSwe usrAplAdmSwe: listUsrAplAdmSwe){
			delete(usrAplAdmSwe);
		}
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
	}
	
	/**
	 * 
	 * @param idApp id de la aplicaci�n
	 * @param idUsr id del usuario
	 * @return un objeto UsrAplAdmSwe, con el idApp y el idUsr
	 * @throws Exception
	 */
	public UsrAplAdmSwe get(Long idApp, Long idUsr) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from UsrAplAdmSwe t where t.usrApl.id ="+idUsr+" and t.aplicacion.id="+idApp;
		Session session = SweHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		UsrAplAdmSwe usrAplAdmSwe = (UsrAplAdmSwe) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return usrAplAdmSwe;
	}
	
	public List<UsrAplAdmSwe> getByIdUsr(Long idUsr) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from UsrAplAdmSwe t where t.usrApl.id ="+idUsr+" order by t.aplicacion.codigo";
		Session session = SweHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<UsrAplAdmSwe> listUsrAplAdmSwe = (ArrayList<UsrAplAdmSwe>)getList(query);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return listUsrAplAdmSwe;
	}
}
