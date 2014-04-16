//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DevDeuDet;
import coop.tecso.demoda.iface.model.Estado;

public class DevDeuDetDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(DevDeuDetDAO.class);	
	
	public DevDeuDetDAO() {
		super(DevDeuDet.class);
	}
	
	public DevDeuDet getActivoByIdDeuda(Long idDeuda){
		
		if (idDeuda==null)
			return null;
		
		String queryString = "FROM DevDeuDet ddd WHERE " +
				"ddd.idDeuda=" + idDeuda + " AND ddd.estado = " + Estado.ACTIVO.getId();
		
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString); 

		return (DevDeuDet) query.uniqueResult();
	}

}
