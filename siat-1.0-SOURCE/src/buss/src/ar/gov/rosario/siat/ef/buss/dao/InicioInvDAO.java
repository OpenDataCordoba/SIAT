//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.InicioInv;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;

public class InicioInvDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(InicioInvDAO.class);
	
	public InicioInvDAO() {
		super(InicioInv.class);
	}
	
	/**
	 * Obtiene un ${Bean} por su codigo
	 */
	public InicioInv getByOrdenControl(OrdenControl ordenControl) throws Exception {
		String queryString = "from InicioInv t where t.ordenControl = :ordenControl";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("ordenControl", ordenControl);
		return  (InicioInv) query.uniqueResult();	
		
	}

}
