//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.EstadoOrden;

public class EstadoOrdenDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EstadoOrdenDAO.class);
	
	public EstadoOrdenDAO() {
		super(EstadoOrden.class);
	}
	
	/**
	 * Obtiene un EstadoOrden por su codigo
	 */
	public EstadoOrden getByCodigo(String codigo) throws Exception {
		EstadoOrden estadoOrden;
		String queryString = "from EstadoOrden t where t.codEstadoOrden = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		estadoOrden = (EstadoOrden) query.uniqueResult();	

		return estadoOrden; 
	}
	
	
	public List<EstadoOrden> getListForFiscalizacion(){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from EstadoOrden e where e.id != "+EstadoOrden.ID_EMITIDA;
		
		queryString += " and e.id != "+EstadoOrden.ID_ANULADA_POR_ERROR;
		
		queryString += " and e.estado = 1";
		
		
		Query query = session.createQuery(queryString);
		
		return (List<EstadoOrden>)query.list();
	}
	
}
