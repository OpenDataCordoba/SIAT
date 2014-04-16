//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.TipoOrigen;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class TipoOrigenDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoOrigenDAO.class);
	
	public TipoOrigenDAO() {
		super(TipoOrigen.class);
	}
	

	/**
	 * Obtiene un TipoOrigen por su codigo
	 */
	public TipoOrigen getByCodigo(String codigo) throws Exception {
		TipoOrigen tipoOrigen;
		String queryString = "from TipoOrigen t where t.codTipoOrigen = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoOrigen = (TipoOrigen) query.uniqueResult();	

		return tipoOrigen; 
	}
	
}
