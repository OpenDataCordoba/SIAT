//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.PlanDescuento;

public class PlanDescuentoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanDescuentoDAO.class);	
	
	public PlanDescuentoDAO() {
		super(PlanDescuento.class);
	}
	
	/**
	 * Obtiene un PlanDescuento por su codigo
	 */
	public PlanDescuento getByCodigo(String codigo) throws Exception {
		PlanDescuento planDescuento;
		String queryString = "from PlanDescuento t where t.codPlanDescuento = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		planDescuento = (PlanDescuento) query.uniqueResult();	

		return planDescuento; 
	}
	
	public boolean chkSolapaVigenciayValor(PlanDescuento planDescuento){
		
		String queryString = "SELECT COUNT(t) FROM PlanDescuento t WHERE " +
							 " t.plan = :plan AND " +
							 " t.fechaDesde <= :fechaHasta AND " +
							 " t.fechaHasta >= :fechaDesde AND " +
							 " t.cantidadCuotasPlan = :cantidadCuotasPlan AND "+
							 " t.aplTotImp = :aplicaTotImp";
		
		if (planDescuento.getId() != null){
			queryString += " AND t.id != :id ";
		}

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setEntity("plan", planDescuento.getPlan())
			.setDate("fechaHasta", planDescuento.getFechaHasta())
			.setDate("fechaDesde", planDescuento.getFechaDesde())
			.setInteger("cantidadCuotasPlan", planDescuento.getCantidadCuotasPlan())
			.setInteger("aplicaTotImp", planDescuento.getAplTotImp());
			
		if (planDescuento.getId() != null){
			query.setLong("id", planDescuento.getId());
		}
		
		Long cant = (Long) query.uniqueResult(); 
		
		if (cant.intValue() >= 1)
			return true;
		else
			return false;
	}
}
