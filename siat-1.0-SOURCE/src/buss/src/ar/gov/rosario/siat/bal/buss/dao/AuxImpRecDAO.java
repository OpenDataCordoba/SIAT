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
import ar.gov.rosario.siat.bal.buss.bean.AuxImpRec;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AuxImpRecDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(AuxImpRecDAO.class);
	
	public AuxImpRecDAO(){
		super(AuxImpRec.class);
	}

	public List<AuxImpRec> getListByAsentamiento(Asentamiento asentamiento) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxImpRec t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.asentamiento.id = " +asentamiento.getId();
		        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<AuxImpRec> listAuxImpRec = (ArrayList<AuxImpRec>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAuxImpRec; 
	}

	/**
	 * Obtiene el AuxImpRec para el Asentamiento, Sistema y Fecha de Pago de la transaccion pasada como parametro, 
	 * y para el ImpRec, la Partida y esImporteFijo tambien indicados en el parametro ParSel. 
	 * 
	 * @param transaccion
	 * @param parSel
	 * @return
	 * @throws Exception
	 */
	public AuxImpRec getForAsentamiento(Long idRecurso, Long idTipoImporte, Long idAsentamiento) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxImpRec t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.asentamiento.id = " + idAsentamiento;
		queryString += " and t.recurso.id = " + idRecurso;
		queryString += " and t.tipoImporte.id = " + idTipoImporte;
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    AuxImpRec auxImpRec = (AuxImpRec) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return auxImpRec; 
	}
	
	
	/**
	 * Obtiene una lista de Detalles de Partidas con el importe acumulado en AuxImpRec. 
	 *   
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListDetalleForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select r.desRecurso, t.tipoImporte.id, sum(t.importe) from AuxImpRec t, Recurso r";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and r.id = t.recurso.id";
		queryString += " group by r.desRecurso, t.tipoImporte.id";
		queryString += " order by r.desRecurso";
		
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	
	/**
	 * Elimina los registros de AuxImpRec que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxImpRec t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
