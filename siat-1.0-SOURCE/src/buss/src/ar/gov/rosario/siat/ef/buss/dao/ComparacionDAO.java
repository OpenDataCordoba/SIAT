//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.Comparacion;

public class ComparacionDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(ComparacionDAO.class);
	
	public ComparacionDAO() {
		super(Comparacion.class);
	}
	

	/**
	 * Obtiene un Comparacion por su codigo
	 */
	public Comparacion getByCodigo(String codigo) throws Exception {
		Comparacion comparacion;
		String queryString = "from Comparacion t where t.codComparacion = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		comparacion = (Comparacion) query.uniqueResult();	

		return comparacion; 
	}
	
}
