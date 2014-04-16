//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.SelAlm;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmLog;

public class SelAlmLogDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(SelAlmLogDAO.class);	
	
	public SelAlmLogDAO() {
		super(SelAlmLog.class);
	}
	
	/**
	 * Obtiene la lista de SelAlmLog del SelAlm
	 * @param  idSelAlm
	 * @return List<SelAlmLog>
	 */
	public List<SelAlmLog> getListSelAlmLogByIdSelAlm(SelAlm selAlm){
		
		String queryString = "FROM SelAlmLog sal WHERE sal.selAlm = :selAlm ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		query.setEntity("selAlm",selAlm);
	    
		return (ArrayList<SelAlmLog>) query.list();
	}
	
	/**
	 * Borra la lista de SelAlmLog de la SelAlm
	 * @param SelAlm 
	 * @return int
	 */
	public int deleteListSelAlmLogBySelAlm(SelAlm selAlm){
		
		String queryString = "DELETE FROM SelAlmLog sal WHERE sal.selAlm.id = :selAlm ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		query.setEntity("selAlm",selAlm);
	    
		return query.executeUpdate();
 
	}
}
