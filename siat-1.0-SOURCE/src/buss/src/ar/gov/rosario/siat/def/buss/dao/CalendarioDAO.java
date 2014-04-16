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
import ar.gov.rosario.siat.def.buss.bean.Calendario;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.CalendarioSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CalendarioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CalendarioDAO.class);	
	
	public CalendarioDAO() {
		super(Calendario.class);
	}
	
	
	public List<Calendario> getByRecursoFecha(Recurso recurso, Date fechaAnalisis) {
		String funcName = DemodaUtil.currentMethodName();
		log.debug(funcName + ": enter");

		String sql="";
		
		sql += " from Calendario c ";
		sql += "  where c.recurso.id=" + recurso.getId();
		sql += "    and c.fechaVencimiento < :fechaAnalisis";
		sql += "  order by c.periodo desc";
		
		Session session = currentSession();
		Query query = session.createQuery(sql).setDate("fechaAnalisis", fechaAnalisis) ;

        return (ArrayList<Calendario>) query.list() ;
	}
	
	public List<Calendario> getBySearchPage(CalendarioSearchPage calendarioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Calendario t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CalendarioSearchPage: " + calendarioSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (calendarioSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro por recurso
 		if (!(ModelUtil.isNullOrEmpty(calendarioSearchPage.getCalendario().getRecurso()))) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + calendarioSearchPage.getCalendario().getRecurso().getId(); 
			flagAnd = true;
		}

		// filtro por fecha Desde
 		if (!(calendarioSearchPage.getFechaDesde() == null)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaVencimiento >= TO_DATE('" + 
				DateUtil.formatDate(calendarioSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + 
				"','%d/%m/%Y')"; 
			flagAnd = true;
		}

		// filtro por fecha Hasta
 		if (!(calendarioSearchPage.getFechaHasta() == null)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaVencimiento <= TO_DATE('" + 
				DateUtil.formatDate(calendarioSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + 
				"','%d/%m/%Y')"; 
			flagAnd = true;
		}
 		
 		
 		// Order By
		queryString += " order by t.fechaVencimiento ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Calendario> listCalendario = (ArrayList<Calendario>) executeCountedSearch(queryString, calendarioSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCalendario;
	}


	/**
	 * Retorna una instancia de Calendario para el idRecurso, idZona,
	 * periodo y anio suministrados como parametros.
	 * null si no lo encuentra o no es unico.
	 * 
	 * Este metodo no es thread-safe.
	 * 
	 * @author juanma
	 */
	public Calendario getBy(Long idRecurso, Long idZona, Long periodo, Long anio) {
		if (log.isDebugEnabled()) {
			String funcName = DemodaUtil.currentMethodName();
			log.debug(funcName + ": enter");
		}

		StringBuilder buffer = new StringBuilder();
		buffer.append("from Calendario c");
		buffer.append(" where c.recurso.id = ");
		buffer.append(idRecurso);
		buffer.append(	" and c.zona.id = ");
		buffer.append(idZona);
		buffer.append(	" and c.periodo = ");
		buffer.append(anio);
		// Si el periodo es un numero de un solo
		// digito, le agregamos un cero adelante.
		if (periodo < 10) buffer.append('0');
		buffer.append(periodo);
				
		Session session = currentSession();
		Query query = session.createQuery(buffer.toString());
		
		try {
			return (Calendario) query.uniqueResult();
		} catch (Exception e) {
			return null;
		}	
	}
}
