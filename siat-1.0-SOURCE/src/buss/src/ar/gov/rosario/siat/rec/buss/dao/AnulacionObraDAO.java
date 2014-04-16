//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.rec.buss.bean.AnulacionObra;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AnulacionObraDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AnulacionObraDAO.class);
	
	public AnulacionObraDAO() {
		super(AnulacionObra.class);
	}
	
	@SuppressWarnings({"unchecked"})
	public List<AnulacionObra> getBySearchPage(AnulacionObraSearchPage anulacionObraSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from AnulacionObra t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AnulacionObraSearchPage: " + anulacionObraSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (anulacionObraSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro por Obra
 		if (!ModelUtil.isNullOrEmpty(anulacionObraSearchPage.getAnulacionObra().getObra())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " t.obra.id = " + anulacionObraSearchPage.getAnulacionObra().getObra().getId();
			flagAnd = true;
		}

 		// filtro por el Estado de la Corrida
 		EstadoCorridaVO estadoCorridaVO = anulacionObraSearchPage
 												.getAnulacionObra().getCorrida().getEstadoCorrida();
 		if (!ModelUtil.isNullOrEmpty(estadoCorridaVO)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id = " + estadoCorridaVO.getId(); 
			flagAnd = true;
		}
 		
 		// filtro por fecha Desde
 		Date fechaDesde = anulacionObraSearchPage.getFechaDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaAnulacion >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// filtro por fecha Hasta
 		Date fechaHasta = anulacionObraSearchPage.getFechaHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaAnulacion <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}

 		queryString += flagAnd ? " and " : " where ";
 		queryString += " t.esVueltaAtras = " + 0; 
 		
  		// Order By Fecha de Anulacion en forma descendente
		queryString += " order by t.fechaAnulacion desc, t.fechaUltMdf desc";

		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<AnulacionObra> listAnulacionObra = (ArrayList<AnulacionObra>) 
								executeCountedSearch(queryString, anulacionObraSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAnulacionObra;
	}

	@SuppressWarnings("unchecked")
	public List<AnulacionObra> getListActivaByIdObra(Long idObra) {
	
		Session session = SiatHibernateUtil.currentSession();
    	String sQuery = "FROM AnulacionObra t " +
    					" WHERE t.obra.id = " + idObra +
    					" AND t.estado = 1";

    	Query query = session.createQuery(sQuery);
    	return (ArrayList<AnulacionObra>) query.list();		
	}
	
}
