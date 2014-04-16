//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.AliComFueCol;

public class AliComFueColDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(AliComFueColDAO.class);
	
	public AliComFueColDAO() {
		super(AliComFueCol.class);
	}
	
	/**
	 * Obtiene un AliComFueCol por su codigo
	 */
	public AliComFueCol getByCodigo(String codigo) throws Exception {
		AliComFueCol aliComFueCol;
		String queryString = "from AliComFueCol t where t.codAliComFueCol = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		aliComFueCol = (AliComFueCol) query.uniqueResult();	

		return aliComFueCol; 
	}
	
	public List<AliComFueCol> getByIdDetAju(Long id) throws Exception {
		
		String queryString = "from AliComFueCol t where t.detAju.id = :idDetAju";
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString).setLong("idDetAju", id);
		
		return (List<AliComFueCol>)query.list();
	}

	
}
