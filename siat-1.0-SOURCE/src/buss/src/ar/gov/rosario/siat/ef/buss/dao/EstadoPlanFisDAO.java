//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.EstadoPlanFis;

public class EstadoPlanFisDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(EstadoPlanFisDAO.class);
	
	public EstadoPlanFisDAO() {
		super(EstadoPlanFis.class);
	}
	


	/**
	 * Obtiene un EstadoPlanFis por su codigo
	 */
	public EstadoPlanFis getByCodigo(String codigo) throws Exception {
		EstadoPlanFis estadoPlanFis;
		String queryString = "from EstadoPlanFis t where t.codEstadoPlanFis = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		estadoPlanFis = (EstadoPlanFis) query.uniqueResult();	

		return estadoPlanFis; 
	}
	
}
