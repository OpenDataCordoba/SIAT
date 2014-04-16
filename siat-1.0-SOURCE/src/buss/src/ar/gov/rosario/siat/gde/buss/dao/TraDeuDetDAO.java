//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.TraDeuDet;
import coop.tecso.demoda.iface.model.Estado;

public class TraDeuDetDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(TraDeuDetDAO.class);	
	
	public TraDeuDetDAO() {
		super(TraDeuDet.class);
	}
	
	/**
	 * Obtiene el TraDeuDet activo para un idDeuda
	 * @param  idDeuda
	 * @return TraDeuDet
	 */
	public TraDeuDet getActivoByIdDeuda(Long idDeuda){
		
		if (idDeuda==null)
			return null;
		
		String queryString = "FROM TraDeuDet tdd WHERE " +
				"tdd.idDeuda=" + idDeuda + " AND tdd.estado = " + Estado.ACTIVO.getId();
		
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString); 

		return (TraDeuDet) query.uniqueResult();
	}
	
}
