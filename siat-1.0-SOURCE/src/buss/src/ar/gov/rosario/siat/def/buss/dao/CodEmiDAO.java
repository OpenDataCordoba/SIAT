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
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.CodEmi;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.CodEmiSearchPage;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CodEmiDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CodEmiDAO.class);
	
	public CodEmiDAO() {
		super(CodEmi.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<CodEmi> getBySearchPage(CodEmiSearchPage codEmiSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from CodEmi t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CodEmiSearchPage: " + codEmiSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (codEmiSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro por Recurso
 		RecursoVO recurso = codEmiSearchPage.getCodEmi().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}
		
		// filtro por Fecha Desde
 		Date fechaDesde = codEmiSearchPage.getCodEmi().getFechaDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		
 		// filtro por Fecha Hasta
 		Date fechaHasta = codEmiSearchPage.getCodEmi().getFechaHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		
		queryString += " order by t.fechaUltMdf desc ";

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CodEmi> listCodEmi = (ArrayList<CodEmi>) executeCountedSearch(queryString, codEmiSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCodEmi;
	}

	 /**
	 * Obtiene un codigo de emision por su tipo
	 * 
	 * @param  idTipCodEmi
	 * @return List<CodEmi>
	 */
	
	@SuppressWarnings("unchecked")
	public List<CodEmi> getListActivosBy(Long idTipCodEmi) {
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "from CodEmi codEmi ";
		strQuery +=	"where codEmi.tipCodEmi.id = :idTipCodEmi ";
		strQuery +=	  "and codEmi.estado = :estado ";
		
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idTipCodEmi", idTipCodEmi)
							 .setInteger("estado", Estado.ACTIVO.getId());
	
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");
	
		return (ArrayList<CodEmi>) query.list(); 
	}
	
	 /**
	 * Obtiene un codigo de emision vigente para un 
	 * recurso y una fecha determinada
	 * 
	 * @param  recurso
	 * @param  fecha
	 * @return CodEmi
	 */
	public CodEmi getCodEmiActivoBy(Recurso recurso, Date fecha) {
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "from CodEmi codEmi ";
		strQuery +=	"where codEmi.recurso.id = :idRecurso ";
		strQuery +=	  "and codEmi.fechaDesde <= :fecha ";
		strQuery +=	  "and (codEmi.fechaHasta is null or codEmi.fechaHasta >= :fecha) ";
		strQuery +=	  "and codEmi.estado = :estado ";
		
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
						.setLong("idRecurso", recurso.getId())
						.setDate("fecha", fecha)
						.setInteger("estado", Estado.ACTIVO.getId());
	
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");
		return (CodEmi) query.uniqueResult(); 
	}

}
