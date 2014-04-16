//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxPagDeu;
import ar.gov.rosario.siat.bal.buss.bean.Transaccion;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AuxPagDeuDAO extends GenericDAO {

	public AuxPagDeuDAO(){
		super(AuxPagDeu.class);
	}
	
	/**
	 * Obtiene el Pago a Deuda Auxiliar para determinado Asentamiento y Deuda.
	 * 
	 * @param asentamiento
	 * @param deuda
	 * @return
	 */
	public AuxPagDeu getByAsentamientoYDeuda(Asentamiento asentamiento,Deuda deuda) {
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxPagDeu t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.idDeuda = " + deuda.getId();
		
		Query query = session.createQuery(queryString);
		
		AuxPagDeu AuxPagDeu = (AuxPagDeu) query.uniqueResult();

		return AuxPagDeu;
	}
	
	/**
	 * Obtiene el Pago a Deuda Auxiliar para determinado Asentamiento y Recibo.
	 * 
	 * @param asentamiento
	 * @param recibo
	 * @return
	 */
	public AuxPagDeu getByAsentamientoYRecibo(Asentamiento asentamiento,Recibo recibo) {
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxPagDeu t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.recibo.id = " + recibo.getId();
		
		Query query = session.createQuery(queryString);
		
		AuxPagDeu AuxPagDeu = (AuxPagDeu) query.uniqueResult();

		return AuxPagDeu;
	}
	
	/**
	 * Elimina los registros de AuxPagDeu que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxPagDeu t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene la lista de AuxPagDeu de manera paginada
	 * @param asentamiento
	 * @param firstResult
	 * @param maxResults
	 * @return List<AuxPagDeu>
	 */
	public List<AuxPagDeu> getListByAsentamiento(Asentamiento asentamiento, Integer firstResult, Integer maxResults ){
		
		String queryStringH = " FROM AuxPagDeu t  " +
			"WHERE t.asentamiento = :asentamiento ORDER BY t.fechaPago, t.id";			

		String queryString = "SELECT SKIP "+firstResult+" FIRST "+maxResults+" * FROM bal_auxPagDeu t  " +
			"WHERE t.idAsentamiento = "+asentamiento.getId();			
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		if(firstResult==null && maxResults == null){
			query = session.createQuery(queryStringH).setEntity("asentamiento", asentamiento);			
		}else{
			query = session.createSQLQuery(queryString).addEntity(AuxPagDeu.class);			
		}
		
		return (ArrayList<AuxPagDeu>) query.list();
	}
	
	/**
	 *  Obtiene la lista de registros de AuxPagDeu a asentar para la Transaccion.
	 * 
	 * @param asentamiento
	 * @param transaccion
	 * @return listAuxPagDeu
	 * @throws Exception
	 */
	public List<AuxPagDeu> getListByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxPagDeu t ";
	    
		// Armamos filtros del HQL		
		queryString += " where t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.transaccion.id = " + transaccion.getId();
            
	    Query query = session.createQuery(queryString);
	    List<AuxPagDeu> listAuxPagDeu = (ArrayList<AuxPagDeu>) query.list();
		
		return listAuxPagDeu; 
	}
	
	public List<Long>getListIdDeudaByListId(List<Long>listId)throws Exception{
		Session session = SiatHibernateUtil.currentSession();
		List<Long>idEncontrados=new ArrayList<Long>();
		if (ListUtil.isNullOrEmpty(listId)){
			return idEncontrados;
		}
		
		String sqlString = "SELECT idDeuda FROM bal_auxPagDeu a WHERE idDeuda IN ("+ ListUtil.getStringList(listId) + ") ";
		
		Query query = session.createSQLQuery(sqlString).addScalar("idDeuda",Hibernate.LONG);
		
		/**List<Object[]> list = query.list();
		for (Object[] obj:list){
			idEncontrados.add(new Long((Integer)obj[0]));
		}
		*/
		
		return query.list();
	}

}
