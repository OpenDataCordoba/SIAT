//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Evento;
import ar.gov.rosario.siat.gde.iface.model.EventoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EventoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class EventoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EventoDAO.class);	
	
	public EventoDAO() {
		super(Evento.class);
	}
	
	public List<Evento> getBySearchPage(EventoSearchPage eventoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Evento t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EventoSearchPage: " + eventoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (eventoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros 
		
		// filtro evento excluidos
 		List<EventoVO> listEventoExcluidos = (ArrayList<EventoVO>) eventoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listEventoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listEventoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!(eventoSearchPage.getEvento().getCodigo() == null)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.codigo = " + (eventoSearchPage.getEvento().getCodigo());
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(eventoSearchPage.getEvento().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(eventoSearchPage.getEvento().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
		// filtro por Etapa Procesal
 		if(!ModelUtil.isNullOrEmpty(eventoSearchPage.getEvento().getEtapaProcesal())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.etapaProcesal = " +  eventoSearchPage.getEvento().getEtapaProcesal().getId();
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codigo ";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Evento> listEvento = (ArrayList<Evento>) executeCountedSearch(queryString, eventoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEvento;
	}

	/**
	 * Obtiene un Evento por su codigo
	 */
	public Evento getByCodigo(Integer codigo) throws Exception {
		Evento evento;
		String queryString = "from Evento t where t.codigo = " + codigo.toString(); 
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		evento = (Evento) query.uniqueResult();	

		return evento; 
	}
	
	/**
	 * Obtiene la lista de Eventos con codigo distinto al argumento
	 */
	public List<Evento> getComplementByCodigo(Integer codigo) {
		
		String queryString = "from Evento t where t.codigo <> " + codigo.toString(); 
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		return (ArrayList<Evento>) query.list() ;
	}
}
