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
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.SinPartida;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SinPartidaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SinPartidaDAO.class);
	
	public SinPartidaDAO(){
		super(SinPartida.class);
	}
	
	/**
	 * Obtiene el registro de SinPartida para el Asentamiento, Partida, Mes y Anio pasados como paramtro.
	 * 
	 * @param asentamiento
	 * @param partida
	 * @param mesPago
	 * @param anioPago
	 * @return
	 * @throws Exception
	 */
	public SinPartida getByAseParMesAnio(Asentamiento asentamiento, Partida partida, Long mesPago, Long anioPago) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from SinPartida t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.partida.id = " + partida.getId();
		queryString += " and t.mesPago = " + mesPago;
		queryString += " and t.anioPago = " + anioPago;
        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    SinPartida sinPartida = (SinPartida) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return sinPartida; 
	}
	
	/**
	 * Obtiene una lista de Detalles de Partidas con el importe distribuido acumulado para el Asentamiento indicado.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getListDetalleForReportByAsentamiento(Asentamiento asentamiento) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select p.codPartida, p.desPartida, sum(t.importeEjeAct), sum(t.importeEjeVen) from SinPartida t, Partida p";
		queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		queryString += " and p.id=t.partida";
		queryString += " group by p.codPartida, p.desPartida";
		queryString += " order by p.codPartida";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult; 
	}
	
	/**
	 * Elimina los registros de SinPartida que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from SinPartida t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
