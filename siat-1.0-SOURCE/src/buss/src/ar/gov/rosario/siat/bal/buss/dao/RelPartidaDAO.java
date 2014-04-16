//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.RelPartida;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class RelPartidaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RelPartidaDAO.class);	
	
	public RelPartidaDAO(){
		super(RelPartida.class);
	}
	
	
	/**
	 * Verifica si existe alguna relacion con partidas para el nodo de id indicado.
	 * 
	 * @param idNodo
	 * @return Nodo
	 */
	public boolean existeRelPartidaByIdNodo(Long idNodo){
		
		String queryString = "FROM RelPartida t WHERE t.nodo.id = "+idNodo;
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString);			
		query.setMaxResults(1);
		RelPartida relPartida = (RelPartida) query.uniqueResult();
		
		if(relPartida != null)
			return true;
		else
			return false;	
	}
	
	/**
	 * Obtiene el RelPartida para el Codigo de Partida pasado como parametro.
	 * 
	 * @param codPartida
	 * @return RelPartida
	 */
	public RelPartida getRelPartidaVigenteByCodPartida(String codPartida){
		
		String queryString = "FROM RelPartida t WHERE t.partida.codPartida = '"+codPartida
							  +"' AND t.fechaDesde <= :fechaActual AND " +
				  			  "(t.fechaHasta IS NULL OR " + 
				  			  "t.fechaHasta > :fechaActual) ";
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString).setDate("fechaActual", new Date());			
		
		return (RelPartida) query.uniqueResult();
	}
	
	/**
	 * Obtiene la lista de partidas relacionadas al Nodo de id pasado como parametro vigentes al dia de hoy.
	 * 
	 * @param idNodo
	 * @return listPartida
	 */
	public List<Partida> getListPartidaVigenteByIdNodo(Long idNodo){
		
		String queryString = "select p FROM Partida p, RelPartida t WHERE t.nodo.id = "+idNodo
							  +" AND p.id = t.partida.id"+
							  " AND t.fechaDesde <= :fechaActual AND " +
				  			  "(t.fechaHasta IS NULL OR " + 
				  			  "t.fechaHasta > :fechaActual) ";
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString).setDate("fechaActual", new Date());				
		
		return (ArrayList<Partida>) query.list();
	}
	
	/**
	 * Obtiene la lista de partidas relacionadas al Nodo de id pasado como parametro vigentes a la fecha de consulta.
	 * 
	 * @param idNodo
	 * @return listPartida
	 */
	public List<Partida> getListPartidaVigenteByIdNodo(Long idNodo, Date fechaConsulta){
		
		String queryString = "select p FROM Partida p, RelPartida t WHERE t.nodo.id = "+idNodo
							  +" AND p.id = t.partida.id"+
							  " AND t.fechaDesde <= :fechaConsulta AND " +
				  			  "(t.fechaHasta IS NULL OR " + 
				  			  "t.fechaHasta >= :fechaConsulta) ";
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString).setDate("fechaConsulta", fechaConsulta);				
		
		return (ArrayList<Partida>) query.list();
	}
	
	/**
	 * Obtiene la lista de partidas relacionadas al Nodo de id pasado como parametro vigentes a la fecha de consulta.
	 * 
	 * @param idNodo
	 * @return listPartida
	 */
	public List<RelPartida> getListByIdPartida(Long idPartida){
		
		String queryString = "FROM RelPartida t WHERE t.partida.id = "+idPartida+" order by fechaDesde DESC";
							  
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString);				
		
		return (ArrayList<RelPartida>) query.list();
	}
}
