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
import ar.gov.rosario.siat.ef.buss.bean.TipoOrden;

public class TipoOrdenDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoOrdenDAO.class);
	
	public TipoOrdenDAO() {
		super(TipoOrden.class);
	}
	
	/**
	 * Obtiene un TipoOrden por su codigo
	 */
	public TipoOrden getByCodigo(String codigo) throws Exception {
		TipoOrden tipoOrden;
		String queryString = "from TipoOrden t where t.codTipoOrden = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoOrden = (TipoOrden) query.uniqueResult();	

		return tipoOrden; 
	}
	
}
