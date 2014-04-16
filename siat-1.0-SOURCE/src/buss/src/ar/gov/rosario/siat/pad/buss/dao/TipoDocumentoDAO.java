//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.pad.buss.bean.TipoDocumento;

public class TipoDocumentoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoDocumentoDAO.class);
	
	public TipoDocumentoDAO() {
		super(TipoDocumento.class);
	}
	

//	/**
//	 * Obtiene un TipoDocumento por su codigo
//	 */
//	public TipoDocumento getByCodigo(String codigo) throws Exception {
//		TipoDocumento TipoDocumento;
//		String queryString = "from TipoDocumento t where t.codTipoDocumento = :codigo";
//		Session session = SatHibernateUtil.currentSession();
//
//		Query query = session.createQuery(queryString).setString("codigo", codigo);
//		TipoDocumento = (TipoDocumento) query.uniqueResult();	
//
//		return TipoDocumento; 
//	}
	
}
