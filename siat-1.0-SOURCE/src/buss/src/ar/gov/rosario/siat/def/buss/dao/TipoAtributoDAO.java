//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.TipoAtributo;

public class TipoAtributoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoAtributoDAO.class);	
	
	public TipoAtributoDAO() {
		super(TipoAtributo.class);
	}
	
	/**
	 * Obtiene una aplicacion por su codigo
	 */
	public TipoAtributo findByCodigo(String codigo) throws Exception {
		TipoAtributo tipoAtributo;
		String queryString = "from TipoAtributo t where t.codigo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tipoAtributo = (TipoAtributo) query.uniqueResult();	

		return tipoAtributo; 
	}
	
}
