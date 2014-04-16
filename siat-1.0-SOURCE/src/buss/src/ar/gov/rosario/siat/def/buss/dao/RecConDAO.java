//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.iface.model.RecConSearchPage;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;

public class RecConDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecAtrValDAO.class);	
	
	public RecConDAO() {
		super(RecCon.class);
	}

	public List<RecCon> getListBySearchPage(RecConSearchPage recConSearchPage) throws Exception {
		return null;
	}
	
	public List<RecCon> getListVigentesByIdRecurso(Long idRecurso, List<RecConVO> listVOExcluidos, Date fecha ) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecCon t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + idRecurso); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
			
	    queryString += " and t.recurso.id = " + idRecurso;
	    
	    // filtro recCon excluidos
 		List<RecConVO> listRecConExcluidos = (List<RecConVO>) listVOExcluidos;
 		if (!ListUtil.isNullOrEmpty(listRecConExcluidos)) {
 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listRecConExcluidos);
			queryString += " and  t.id NOT IN ("+ listIdExcluidos + ") ";
		}
	    
 		queryString += " and (t.fechaHasta IS NULL OR t.fechaHasta  >= TO_DATE('" 
			+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
 		
		// Order By
		queryString += " order by t.desRecCon ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecCon> listRecCon = (ArrayList<RecCon>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecCon;
	}

	/**
	 * Obtiene la lista de RecCon para un recurso, ordenada por el orden de visualizacion de los RecCon
	 * @param idRecurso
	 * @return
	 */
	public List<RecCon> getListRecConOrderByVisualizacion(Long idRecurso) {
		Session session = SiatHibernateUtil.currentSession();		
		String queryString = "FROM RecCon t WHERE t.recurso.id = "+idRecurso+" ORDER BY t.ordenVisualizacion";
	    Query query = session.createQuery(queryString);
	    return (ArrayList<RecCon>) query.list();
	}

	/**
	 * Obtiene un Concepto de Recurso por su codigo
	 */
	public RecCon getByIdRecursoAndCodigo(Long idRecurso, String codigo) throws Exception {
		RecCon recCon;
		
		String queryString = "from RecCon t where t.codRecCon = :codigo" + 
							" and t.recurso.id = " + idRecurso;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		recCon = (RecCon) query.uniqueResult();	

		return recCon; 
	}
	
	
	/**
	 * Obtiene un Concepto de Recurso por su orden de visualizacion.
	 */
	public RecCon getByIdRecursoAndOrden(Long idRecurso, int nroOrden) throws Exception {
		RecCon recCon;
		
		String queryString = "from RecCon t where t.ordenVisualizacion = " + nroOrden + 
							" and t.recurso.id = " + idRecurso;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		recCon = (RecCon) query.uniqueResult();	

		return recCon; 
	}
}
