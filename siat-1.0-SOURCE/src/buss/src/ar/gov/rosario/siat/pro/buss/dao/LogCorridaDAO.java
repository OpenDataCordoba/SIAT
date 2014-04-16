//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.AseDel;
import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pro.buss.bean.LogCorrida;

public class LogCorridaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(LogCorridaDAO.class);	
	
	public LogCorridaDAO() {
		super(LogCorrida.class);
	}
	
	/**
	 * Elimina los registros de LogCorrida que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from LogCorrida t ";
			   queryString += " where t.corrida.id = "+asentamiento.getCorrida().getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Elimina los registros de LogCorrida que corresponden a la corrida de id pasado.
	 * 
	 * @param idCorrida
	 * @return int
	 */
	public int deleteAllByIdCorrida (Long idCorrida){

		String queryString = "delete from LogCorrida t ";
			   queryString += " where t.corrida.id = "+idCorrida; 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Elimina los registros de LogCorrida que corresponden al Asentamiento Delegado
	 * 
	 * @param aseDel
	 * @return int
	 */
	public int deleteAllByAseDel (AseDel aseDel){

		String queryString = "delete from LogCorrida t ";
			   queryString += " where t.corrida.id = "+aseDel.getCorrida().getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
