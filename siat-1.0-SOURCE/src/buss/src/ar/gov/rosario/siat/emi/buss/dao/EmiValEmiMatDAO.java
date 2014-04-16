//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.emi.buss.bean.EmiValEmiMat;

public class EmiValEmiMatDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(EmiValEmiMatDAO.class);
	
	public EmiValEmiMatDAO() {
		super(EmiValEmiMat.class);
	}
	
	/**
	 * Obtiene un EmiValEmiMat por su codigo
	 */
	public EmiValEmiMat getByCodigo(String codigo) throws Exception {
		EmiValEmiMat emiValEmiMat;
		String queryString = "from EmiValEmiMat t where t.codEmiValEmiMat = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		emiValEmiMat = (EmiValEmiMat) query.uniqueResult();	

		return emiValEmiMat; 
	}
	
}
