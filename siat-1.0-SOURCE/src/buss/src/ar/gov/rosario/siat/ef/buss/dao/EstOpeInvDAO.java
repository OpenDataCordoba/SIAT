//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;


import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.EstOpeInv;

public class EstOpeInvDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(EstOpeInvDAO.class);
	
	public EstOpeInvDAO() {
		super(EstOpeInv.class);
	}
	
	/**
	 * Obtiene un EstOpeInv por su codigo
	 */
	public EstOpeInv getByCodigo(String codigo) throws Exception {
		EstOpeInv estOpeInv;
		String queryString = "from EstOpeInv t where t.codEstOpeInv = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		estOpeInv = (EstOpeInv) query.uniqueResult();	

		return estOpeInv; 
	}
	
}
