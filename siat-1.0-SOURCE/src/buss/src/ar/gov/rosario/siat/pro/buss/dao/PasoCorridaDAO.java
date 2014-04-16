//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.AseDel;
import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;

public class PasoCorridaDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(PasoCorridaDAO.class);	
	
	public PasoCorridaDAO() {
		super(PasoCorrida.class);
	}
	
	/**
	 * Obtiene el PasoCorrida a partir de la Corrida y del Paso
	 * @param  corrida
	 * @param  paso
	 * @return PasoCorrida
	 */
	public PasoCorrida getByCorridaPaso(Corrida corrida, Integer paso){
		
		Session session = SiatHibernateUtil.currentSession();
		String sQuery = "FROM PasoCorrida pc WHERE pc.corrida = :corrida " +
				"AND paso = :paso";

		Query query = session.createQuery(sQuery)
			.setEntity("corrida", corrida)
			.setInteger("paso", paso);
		return (PasoCorrida) query.uniqueResult();		

		
	}
	
	/**
	 * Elimina los registros de PasoCorrida que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from PasoCorrida t ";
			   queryString += " where t.corrida.id = "+asentamiento.getCorrida().getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/* no usar desde aca, se va a hace desde adp
	public int deleteByCorridaDesdePaso (Corrida corrida, Integer paso){

		String queryString = "DELETE FROM PasoCorrida t ";
			   queryString += " WHERE t.corrida = :corrida AND paso >= :paso"; 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString)
		.setEntity("corrida", corrida)
		.setInteger("paso", paso);
	    
		return query.executeUpdate();
	}
	*/
	
	/**
	 * Elimina los registros de PasoCorrida que corresponden a la corrida de id pasado.
	 * 
	 * @param idCorrida
	 * @return int
	 */
	public int deleteAllByIdCorrida (Long idCorrida){

		String queryString = "delete from PasoCorrida t ";
			   queryString += " where t.corrida.id = "+idCorrida; 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Elimina los registros de PasoCorrida que corresponden al Asentamiento Delegado
	 * 
	 * @param aseDel
	 * @return int
	 */
	public int deleteAllByAseDel (AseDel aseDel){

		String queryString = "delete from PasoCorrida t ";
			   queryString += " where t.corrida.id = "+aseDel.getCorrida().getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
