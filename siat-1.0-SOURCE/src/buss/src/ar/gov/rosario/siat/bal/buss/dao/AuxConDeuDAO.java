//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxConDeu;
import ar.gov.rosario.siat.bal.buss.bean.Transaccion;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class AuxConDeuDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(AuxConDeuDAO.class);
	
	public AuxConDeuDAO(){
		super(AuxConDeu.class);
	}
	
	/**
	 *  Obtiene el registro de AuxConDeu con la Deuda a Cancelar. Buscando el registro para el asentamiento y convenio
	 *  pasados y con SaldoEnPlan mayor que cero.
	 * 
	 * @param asentamiento
	 * @param convenio
	 * @return auxConDeu
	 * @throws Exception
	 */
	public AuxConDeu getDeudaACancelarByAsentamientoYConvenio(Asentamiento asentamiento, Convenio convenio) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxConDeu t ";
	    
		// Armamos filtros del HQL		
		queryString += " where t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.convenioDeuda.convenio.id = " + convenio.getId();
		queryString += " and t.saldoEnPlan > 0";
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    AuxConDeu auxConDeu = (AuxConDeu) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return auxConDeu; 
	}

	/**
	 * Elimina los registros de AuxConDeu que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxConDeu t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene la lista de AuxConDeu de manera paginada
	 * @param asentamiento
	 * @param firstResult
	 * @param maxResults
	 * @return List<AuxConDeu>
	 */
	public List<AuxConDeu> getListByAsentamiento(Asentamiento asentamiento, Integer firstResult, Integer maxResults ){
		
		String queryStringH = " FROM AuxConDeu t  " +
			"WHERE t.asentamiento = :asentamiento ORDER BY t.fechaPago, t.id";			

		String queryString = "SELECT SKIP "+firstResult+" FIRST "+maxResults+" * FROM bal_auxConDeu t  " +
			"WHERE t.idAsentamiento = "+asentamiento.getId();			
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		if(firstResult==null && maxResults == null){
			query = session.createQuery(queryStringH).setEntity("asentamiento", asentamiento);			
		}else{
			query = session.createSQLQuery(queryString).addEntity(AuxConDeu.class);			
		}
		
		return (ArrayList<AuxConDeu>) query.list();
	}

	/**
	 *  Obtiene la lista de registros de AuxConDeu a asentar para la Transaccion.
	 * 
	 * @param asentamiento
	 * @param transaccion
	 * @return listAuxConDeu
	 * @throws Exception
	 */
	public List<AuxConDeu> getListByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxConDeu t ";
	    
		// Armamos filtros del HQL		
		queryString += " where t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.transaccion.id = " + transaccion.getId();
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<AuxConDeu> listAuxConDeu = (ArrayList<AuxConDeu>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAuxConDeu; 
	}
}
