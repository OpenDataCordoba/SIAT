//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanImpMin;
import coop.tecso.demoda.iface.model.Estado;

public class PlanImpMinDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanImpMinDAO.class);	
	
	public PlanImpMinDAO() {
		super(PlanImpMin.class);
	}

	public boolean chkSolapaVigenciayValor(PlanImpMin planImpMin){
		
		String queryString = "SELECT COUNT(t) FROM PlanImpMin t WHERE " +
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde AND " +
							 " t.cantidadCuotas = :cantidadCuotas AND " +
							 " t.impMinCuo = :impMinCuo AND " +
							 " t.impMinDeu = :impMinDeu";
		
		if (planImpMin.getId() != null){
			queryString += " AND t.id != :id ";
		}

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("plan", planImpMin.getPlan())
			.setDate("fechaHasta", planImpMin.getFechaHasta())
			.setDate("fechaDesde", planImpMin.getFechaDesde())
			.setInteger("cantidadCuotas", planImpMin.getCantidadCuotas())
			.setDouble("impMinCuo", planImpMin.getImpMinCuo())
			.setDouble("impMinDeu", planImpMin.getImpMinDeu());
			
		if (planImpMin.getId() != null){
			query.setLong("id", planImpMin.getId());
		}

		Long cant = (Long) query.uniqueResult(); 
		
		log.debug("plan.id: " + planImpMin.getPlan().getId());
		log.debug("fechaHasta: " + planImpMin.getFechaHasta());
		log.debug("fechaDesde: " + planImpMin.getFechaDesde());
		log.debug("cantidadCuotas: " + planImpMin.getCantidadCuotas());
		log.debug("impMinCuo: " + planImpMin.getImpMinCuo());
		log.debug("impMinDeu: " + planImpMin.getImpMinDeu());
		log.debug("chkSolapaVigenciayValor: " + cant);
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}
	
	
	/**
	 * Obtiene la lista vigentes a la fecha pasada de Importes Minimos (de Deuda y Cuota) para las alternativas de cuotas. 
	 * 
	 * @return
	 */
	public List<PlanImpMin> getListVigentes(Date fecha, Plan plan){
		
		String queryStr = " FROM PlanImpMin t WHERE t.estado = "+Estado.ACTIVO.getId();
	   	   	   queryStr += " AND t.plan.id = "+plan.getId();
		   	   queryStr += " AND t.fechaDesde <= :fecha ";
		   	   queryStr += " AND (t.fechaHasta is null ";
		   	   queryStr += " OR t.fechaHasta >= :fecha) ";
		  	
		  	queryStr += " ORDER BY t.cantidadCuotas";
			   
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query = session.createQuery(queryStr);
			  query.setDate("fecha", fecha);
		
		return (ArrayList<PlanImpMin>) query.list();
	}
	
}
