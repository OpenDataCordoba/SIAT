//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.EstObjImp;

public class EstObjImpDAO extends GenericDAO {
	
	public EstObjImpDAO() {
		super(EstObjImp.class);
	}
	

	/**
	 * Obtiene un EstObjImp por su codigo
	 */
	public EstObjImp getByCodigo(String codigo) throws Exception {
		EstObjImp estObjImp;
		String queryString = "from EstObjImp t where t.codEstObjImp = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		estObjImp = (EstObjImp) query.uniqueResult();	

		return estObjImp; 
	}
	
}
