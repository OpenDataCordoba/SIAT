//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.AproOrdCon;

public class AproOrdConDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(AproOrdConDAO.class);
	
	public AproOrdConDAO() {
		super(AproOrdCon.class);
	}
	
	/**
	 * Obtiene un AproOrdCon por su codigo
	 */
	public AproOrdCon getByCodigo(String codigo) throws Exception {
		AproOrdCon aproOrdCon;
		String queryString = "from AproOrdCon t where t.codAproOrdCon = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		aproOrdCon = (AproOrdCon) query.uniqueResult();	

		return aproOrdCon; 
	}
	
}
