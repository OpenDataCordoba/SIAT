//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecursoArea;
import coop.tecso.demoda.iface.model.Estado;

public class RecursoAreaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecursoAreaDAO.class);
	
	public RecursoAreaDAO() {
		super(RecursoArea.class);
	}
	
	public List<RecursoArea> getListByAreaActivos(Long idArea) throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM RecursoArea r " +
						" WHERE r.area.id = " + idArea +
						" AND r.estado = " + Estado.ACTIVO.getId();
    	
		Query query = session.createQuery(sQuery);
    	
    	return  (ArrayList<RecursoArea>) query.list();
    }
	
	public RecursoArea getByRecursoArea(Long idRecurso, Long idArea) throws Exception {
		
		Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM RecursoArea r " +
						" WHERE r.area.id = " + idArea +
						" AND r.recurso.id = " + idRecurso +
						" AND r.estado = " + Estado.ACTIVO.getId();
    	
		Query query = session.createQuery(sQuery);
    	
    	return  (RecursoArea) query.uniqueResult();
		
	}
}
