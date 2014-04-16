//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxConDeuCuo;
import ar.gov.rosario.siat.bal.buss.bean.Transaccion;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class AuxConDeuCuoDAO extends GenericDAO {
	
	public AuxConDeuCuoDAO(){
		super(AuxConDeuCuo.class);
	}
	
	/**
	 * Elimina los registros de AuxConDeuCuo que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxConDeuCuo t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene la lista de AuxConDeuCuo de manera paginada
	 * @param asentamiento
	 * @param firstResult
	 * @param maxResults
	 * @return List<AuxConDeuCuo>
	 */
	public List<AuxConDeuCuo> getListByAsentamiento(Asentamiento asentamiento, Integer firstResult, Integer maxResults ){
		
		String queryStringH = " FROM AuxConDeuCuo t  " +
			"WHERE t.asentamiento = :asentamiento ORDER BY t.fechaPago, t.id";			

		String queryString = "SELECT SKIP "+firstResult+" FIRST "+maxResults+" * FROM bal_auxConDeuCuo t  " +
			"WHERE t.idAsentamiento = "+asentamiento.getId();			
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		if(firstResult==null && maxResults == null){
			query = session.createQuery(queryStringH).setEntity("asentamiento", asentamiento);			
		}else{
			query = session.createSQLQuery(queryString).addEntity(AuxConDeuCuo.class);			
		}
		
		return (ArrayList<AuxConDeuCuo>) query.list();
	}
	
	/**
	 *  Obtiene la lista de registros de AuxConDeuCuo a asentar para la Transaccion.
	 * 
	 * @param asentamiento
	 * @param transaccion
	 * @return listAuxConDeuCuo
	 * @throws Exception
	 */
	public List<AuxConDeuCuo> getListByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxConDeuCuo t ";
	    
		// Armamos filtros del HQL		
		queryString += " where t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.transaccion.id = " + transaccion.getId();
            
	    Query query = session.createQuery(queryString);
	    List<AuxConDeuCuo> listAuxConDeuCuo = (ArrayList<AuxConDeuCuo>) query.list();
		
		return listAuxConDeuCuo; 
	}
}
