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

import ar.gov.rosario.siat.bal.buss.bean.Reclamo;
import ar.gov.rosario.siat.bal.iface.model.ReclamoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ReclamoVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ReclamoDAO extends GenericDAO {

	private static final String SEQUENCE_NRO_RECLAMO = "bal_nro_reclamo_sq";
	private Log log = LogFactory.getLog(ReclamoDAO.class);
	
	public ReclamoDAO() {
		super(Reclamo.class);
	}
	
	public List<Reclamo> getBySearchPage(ReclamoSearchPage reclamoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Reclamo t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ReclamoSearchPage: " + reclamoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (reclamoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro por id (nroReclamo)
 		if (reclamoSearchPage.getReclamo().getId() != null) {
            queryString += flagAnd ? " and " : " where ";
		 	queryString += " t.id = " +  reclamoSearchPage.getReclamo().getId();
			flagAnd = true;
		}
		
		
		// filtro reclamo excluidos
 		List<ReclamoVO> listReclamoExcluidos = (List<ReclamoVO>) reclamoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listReclamoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listReclamoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if (!ModelUtil.isNullOrEmpty(reclamoSearchPage.getReclamo().getRecurso())) {
            queryString += flagAnd ? " and " : " where ";
		 	queryString += " t.recurso.id = " +  reclamoSearchPage.getReclamo().getRecurso().getId();
			flagAnd = true;
		}

 	   // filtro por Estado Reclamo
 	 	if (!ModelUtil.isNullOrEmpty(reclamoSearchPage.getReclamo().getEstadoReclamo())) {
            queryString += flagAnd ? " and " : " where ";
		 	queryString += " t.estadoReclamo.id = " +  reclamoSearchPage.getReclamo().getEstadoReclamo().getId();
			flagAnd = true;
		}
 	 	
 		// filtro por fecha Desde
 		Date fechaDesde = reclamoSearchPage.getFechaDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaAlta >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// fecha Hasta
 		Date fechaHasta = reclamoSearchPage.getFechaHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaAlta <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
 		
 		// filtro por Cuenta
 		if (reclamoSearchPage.getReclamo().getNroCuenta() != null) {
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.nroCuenta = " +  reclamoSearchPage.getReclamo().getNroCuenta();

 			flagAnd = true;

 		}
 		// Order By
 		queryString += " order by t.fechaAlta desc";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Reclamo> listReclamo = (ArrayList<Reclamo>) executeCountedSearch(queryString, reclamoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listReclamo;
	}

	/**
	 * Sequencia de Nro. Reclamo 
	 */
	public Long getNextNroReclamo() {
		//Por ahora no lo usamo, y usamo el id como nro reclamo.
		return super.getNextVal(SEQUENCE_NRO_RECLAMO);
	}

	public List<Reclamo> getByIdElemento(Long idElemento, Long tipoBoleta) {
		String queryString = "from Reclamo t where t.idElemento = :idElemento and t.tipoBoleta = :idTipoBoleta";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
						.setLong("idElemento", idElemento)
						.setLong("idTipoBoleta", tipoBoleta);

		return (ArrayList<Reclamo>) query.list();
	}
}
