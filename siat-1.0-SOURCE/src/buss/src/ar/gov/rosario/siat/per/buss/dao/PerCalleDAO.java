//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.per.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.per.buss.bean.PerCalle;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class PerCalleDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PerCalleDAO.class);
	
	public PerCalleDAO() {
		super(PerCalle.class);
	}
	
	/**
	 * Obtiene un Calle por su codigo
	 */
	public PerCalle getByCodigo(String codigo) throws Exception {
		PerCalle calle;
		String queryString = "from PerCalle t where t.codCalle = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		calle = (PerCalle) query.uniqueResult();	

		return calle; 
	}


	@SuppressWarnings("unchecked")
	public List<PerCalle> getListCalleByNombre(String nombreCalle) {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		String queryString = "from PerCalle c where ";	
		queryString+=" UPPER(TRIM(c.nombreCalle)) like '%nomCalle%'";		
		queryString+=" or UPPER(TRIM(c.nomabrev)) like '%nomCalle%'";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString).setString("nomCalle", 
				StringUtil.escaparUpper(nombreCalle));
		List<PerCalle> listCalle = (ArrayList<PerCalle>) query.list();		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return listCalle;
		
	}

	public PerCalle getByNombre(String nombreCalle) {
		
		PerCalle calle;
		String queryString = "from PerCalle c where UPPER(TRIM(c.nombreCalle)) like :nomCalle";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("nomCalle", 
				StringUtil.escaparUpper(nombreCalle));
		calle = (PerCalle) query.uniqueResult();	

		return calle; 
	}
}
