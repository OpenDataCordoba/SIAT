//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.LiqComProDeu;

public class LiqComProDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(LiqComProDeuDAO.class);	
	
	public LiqComProDeuDAO() {
		super(LiqComProDeu.class);
	}
	

	/**
	 * Obtiene un LiqComProDeu por su codigo
	 */
	public LiqComProDeu getByCodigo(String codigo) throws Exception {
		LiqComProDeu liqComProDeu;
		String queryString = "from LiqComProDeu t where t.codLiqComProDeu = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		liqComProDeu = (LiqComProDeu) query.uniqueResult();	

		return liqComProDeu; 
	}
	
}
