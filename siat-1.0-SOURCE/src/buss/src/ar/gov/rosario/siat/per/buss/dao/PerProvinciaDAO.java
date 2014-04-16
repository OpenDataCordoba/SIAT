//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.per.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.per.buss.bean.PerProvincia;

public class PerProvinciaDAO extends GenericDAO {

	public PerProvinciaDAO() {
		super(PerProvincia.class);
	}
	
	/**
	 * Obtiene un Provincia por su codigo
	 */
	public PerProvincia getByCodigo(String codigo) throws Exception {
		PerProvincia provincia;
		String queryString = "from PerProvincia t where t.codProvincia = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		provincia = (PerProvincia) query.uniqueResult();	

		return provincia; 
	}
	
}
