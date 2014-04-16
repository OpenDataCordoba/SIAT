//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.AccionSellado;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;

public class AccionSelladoDAO extends GenericDAO {	
	
	public AccionSelladoDAO() {
		super(AccionSellado.class);
	}

	/**
	 * Obtiene un AccionSellado por su codigo
	 */
	public AccionSellado getByCodigo(String codigo) throws Exception {
		AccionSellado accionSellado;
		String queryString = "from AccionSellado t where t.codAccionSellado = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		accionSellado = (AccionSellado) query.uniqueResult();	

		return accionSellado; 
	}
	
	/**
	 * devuelve True si hay una AccionSellado por un idSellado y una idAccion segun un rango de fechas
	 */
	public Boolean existeAccionSellado(Long idSellado, Long idAccion, Date fechaDesde, Date fechaHasta, Long idRecurso){
		boolean fechaHastaNula=(fechaHasta==null);
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM AccionSellado p WHERE p.sellado.id = "+idSellado;
		queryString += " AND p.accion.id ="+idAccion;
		if(!fechaHastaNula){queryString += " AND (p.fechaDesde <=  :fechaHasta";}
		queryString += " AND (p.fechaHasta is NULL OR p.fechaHasta >= :fechaDesde)";
		if(!fechaHastaNula)queryString+=")";
		queryString += " AND p.recurso.id = "+idRecurso;
		
		Query query = session.createQuery(queryString);
		query.setDate("fechaDesde", fechaDesde);
		if(!fechaHastaNula){query.setDate("fechaHasta", fechaHasta);}
		
		query.setMaxResults(1);
		
		AccionSellado accionSellado = (AccionSellado) query.uniqueResult();
			
		if(accionSellado != null)
			return true;
			
		return false;
		
	}

	
}
