//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rec.buss.bean.TipoTramiteRS;

public class TipoTramiteRSDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(TipoTramiteRSDAO.class);	

	private static long migId = -1;
	
	public TipoTramiteRSDAO() {
		super(TipoTramiteRS.class);
	}
	
	/**
	 * Obtiene un Abogado por su codigo
	 */
	public TipoTramiteRS getByDes(String des) {
		
		String queryString = "from TipoTramiteRS t where t.desTipoTramite = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", des);
		return (TipoTramiteRS) query.uniqueResult();	

	}

	
	
}
