//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.OrdConBasImp;

public class OrdConBasImpDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(OrdConBasImpDAO.class);
	
	public OrdConBasImpDAO() {
		super(OrdConBasImp.class);
	}
	
	/**
	 * Obtiene un OrdConBasImp por su codigo
	 */
	public OrdConBasImp getByCodigo(String codigo) throws Exception {
		OrdConBasImp ordConBasImp;
		String queryString = "from OrdConBasImp t where t.codOrdConBasImp = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		ordConBasImp = (OrdConBasImp) query.uniqueResult();	

		return ordConBasImp; 
	}
	
}
