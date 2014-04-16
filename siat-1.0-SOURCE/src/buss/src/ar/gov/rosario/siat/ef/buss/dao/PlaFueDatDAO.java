//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.PlaFueDat;

public class PlaFueDatDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(PlaFueDatDAO.class);
	
	public PlaFueDatDAO() {
		super(PlaFueDat.class);
	}
	
	/**
	 * Obtiene un PlaFueDat por su codigo
	 */
	public PlaFueDat getByCodigo(String codigo) throws Exception {
		PlaFueDat plaFueDat;
		String queryString = "from PlaFueDat t where t.codPlaFueDat = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		plaFueDat = (PlaFueDat) query.uniqueResult();	

		return plaFueDat; 
	}
	
}
