//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.swe.buss.auth.SweAuthLoginLocal;
import ar.gov.rosario.swe.buss.bean.UsrAuth;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class UsrAuthDAO extends GenericDAO {		
	
	private Logger log = Logger.getLogger(SweAuthLoginLocal.class);
	
	public UsrAuthDAO() {
		super(UsrAuth.class);
	}	

	public UsrAuth getByUsrName(String usrName){		
		String funcName = DemodaUtil.currentMethodName();	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");	
		
		String queryString = "from UsrAuth t where t.nomUsuario= :name";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		Session session = SweHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		query.setString("name", usrName);
		
		return (UsrAuth) query.uniqueResult();	
	}
	
	public UsrAuth getByUsrNameAndPass(String usrName, String usrPass) {
		
		String funcName = DemodaUtil.currentMethodName();	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");	
		
		String queryString = "from UsrAuth t where t.nomUsuario= :name and t.password = :paswd";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		Session session = SweHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		query.setString("name", usrName);
		query.setString("passwd", usrPass);
		
		return (UsrAuth) query.uniqueResult();	
		
	}
	
	public UsrAuth getByUsrNamePassApp(String usrName, String usrPass, String app) {
		
		String funcName = DemodaUtil.currentMethodName();	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");	
		
		String queryString = "from UsrAuth t where t.nomUsuario = :name  and t.password = :passwd";
		queryString+= " and t.aplicacion.codigo = :app";
		
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		Session session = SweHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		query.setString("name", usrName);
		query.setString("passwd", usrPass);
		query.setString("app", app);
		
		return (UsrAuth) query.uniqueResult();	
		
	}
}
