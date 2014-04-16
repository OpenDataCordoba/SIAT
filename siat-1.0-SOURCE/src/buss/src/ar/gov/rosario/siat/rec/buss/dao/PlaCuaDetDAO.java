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
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.rec.buss.bean.PlaCuaDet;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class PlaCuaDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlaCuaDetDAO.class);	
	
	public PlaCuaDetDAO() {
		super(PlaCuaDet.class);
	}

	public PlaCuaDet getByIdCuentaTGI(Long idCuentaTGI, Long idObra) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		PlaCuaDet plaCuaDet;
		
		String queryString = "FROM PlaCuaDet t WHERE t.cuentaTGI.id = " + idCuentaTGI;
	    	   queryString+= " AND t.planillaCuadra.obra.id =" + idObra;

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		try{
		
			plaCuaDet = (PlaCuaDet) query.uniqueResult();	

		} catch (Exception e) {
			// Si no encuentra el detalle de planilla, o encuentra mas de uno 
			return null;				
		} 

		return plaCuaDet; 
	}
	
	public PlaCuaDet getByCuentaCdM(Cuenta cuenta) throws Exception {
		PlaCuaDet plaCuaDet;
		
	    String queryString = "from PlaCuaDet t where t.cuentaCdM = :cuenta";
	    					 
	    Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
		 					.setEntity("cuenta", cuenta);
		
		
		plaCuaDet = (PlaCuaDet) query.uniqueResult();	
		 
		return plaCuaDet; 
	}
}
