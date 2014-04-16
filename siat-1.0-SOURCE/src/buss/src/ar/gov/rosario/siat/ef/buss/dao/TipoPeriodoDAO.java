//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.TipoPeriodo;

public class TipoPeriodoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoPeriodoDAO.class);
	
	public TipoPeriodoDAO() {
		super(TipoPeriodo.class);
	}
	
	/**
	 * Obtiene un TipoPeriodo por su codigo
	 */
	public TipoPeriodo getByCodigo(String codigo) throws Exception {
		TipoPeriodo tipoPeriodo;
		String queryString = "from TipoPeriodo t where t.codTipoPeriodo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoPeriodo = (TipoPeriodo) query.uniqueResult();	

		return tipoPeriodo; 
	}
	
}
