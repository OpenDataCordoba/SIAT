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

import ar.gov.rosario.siat.bal.buss.bean.Nodo;
import ar.gov.rosario.siat.bal.buss.bean.RelCla;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;

public class RelClaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RelClaDAO.class);	
	
	public RelClaDAO(){
		super(RelCla.class);
	}
	
	/**
	 * Obtiene el Nodo Origen relacionado al Nodo Destino de id pasado como parametro.
	 * 
	 * @param idNodo
	 * @return Nodo
	 */
	@Deprecated
	public Nodo getNodoByIdNodo(Long idNodo){
		
		String queryString = "SELECT n FROM Nodo n,RelCla t WHERE t.nodo2.id = "+idNodo
							+" AND n.id = t.nodo1.id";			
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString);			
		
		return (Nodo) query.uniqueResult();
	}
	
	/**
	 * Verifica si existe alguna relacion con nodos teniedo al de id pasado como principal (nodo1) .
	 * 
	 * @param idNodo
	 * @return Nodo
	 */
	public boolean existeRelClaByIdNodo(Long idNodo){
		
		String queryString = "FROM RelCla t WHERE t.nodo1.id = "+idNodo;
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString);			
		query.setMaxResults(1);
		RelCla relCla = (RelCla) query.uniqueResult();
		
		if(relCla != null)
			return true;
		else
			return false;	
	}
	
	/**
	 * Obtiene la lista de Nodos Origen relacionado al Nodo Destino de id pasado como parametro.
	 * 
	 * @param idNodo
	 * @return Nodo
	 */
	public List<Nodo> getListNodoByIdNodo(Long idNodo){
		
		String queryString = "SELECT n FROM Nodo n,RelCla t WHERE t.nodo2.id = "+idNodo
							+" AND n.id = t.nodo1.id"		
							+" AND t.fechaDesde <= :fechaActual AND " +
							"(t.fechaHasta IS NULL OR " + 
							"t.fechaHasta > :fechaActual) ";
		
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString).setDate("fechaActual", new Date());			
	
		
		return (ArrayList<Nodo>) query.list();
	}
	
	/**
	 * Obtiene la lista de Nodos Origen relacionado al Nodo Destino de id pasado como parametro.
	 * 
	 * @param idNodo
	 * @return Nodo
	 */
	public List<Nodo> getListNodoByIdNodo(Long idNodo, Date fechaConsulta){
		
		String queryString = "SELECT n FROM Nodo n,RelCla t WHERE t.nodo2.id = "+idNodo
							+" AND n.id = t.nodo1.id"		
							+" AND t.fechaDesde <= :fechaConsulta AND " +
							"(t.fechaHasta IS NULL OR " + 
							"t.fechaHasta > :fechaConsulta) ";
		
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString).setDate("fechaConsulta", fechaConsulta);			
	
		
		return (ArrayList<Nodo>) query.list();
	}
	
	/**
	 * Verifica si existe una relacion con nodo de rubro vigente para el nodo pasado.
	 * 
	 * @param idNodo
	 * @return Nodo
	 */
	public Boolean existeRelClaVigenteForNodo(Long idNodo){
		
		String queryString = "FROM RelCla t WHERE t.nodo2.id = "+idNodo	
							+" AND t.fechaDesde <= :fechaActual AND " +
							"(t.fechaHasta IS NULL OR " + 
							"t.fechaHasta > :fechaActual) ";
		
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		query = session.createQuery(queryString).setDate("fechaActual", new Date());			
	
		query.setMaxResults(1);
		RelCla relCla = (RelCla) query.uniqueResult();
		
		if(relCla != null)
			return true;
		else
			return false;	
	}
}
