//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.MotAnuDeu;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class MotAnuDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(MotAnuDeuDAO.class);	
	
	public MotAnuDeuDAO() {
		super(MotAnuDeu.class);
	}

	/**
	 * Devuelve los motivos de anulacion que "Anulan Deuda", activos.
	 *  
	 * 
	 * @author Cristian
	 * @return
	 */
	public List<MotAnuDeu> getListMotAnuDeuAnulan(){
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		// Armamos filtros del HQL
		String queryString = "from MotAnuDeu t " +
							 " where t.estado = " + Estado.ACTIVO.getId() +
							 " and t.id > 1 and t.id <> " + MotAnuDeu.ID_CAMBIOPLAN_CDM;
	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Query query = session.createQuery(queryString);
	    List<MotAnuDeu> listMotAnuDeu = (ArrayList<MotAnuDeu>) query.list();

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMotAnuDeu;
	}
	
}
