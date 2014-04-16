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
import ar.gov.rosario.siat.def.buss.bean.RecAli;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class RecAliDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecAliDAO.class);	
	
	public RecAliDAO() {
		super(RecAli.class);
	}

	/**
	 * Obtiene lista de Alicuotas definidas en el Recurso especificado a la fecha pasada como parametro
	 * @author Tecso
	 * @param Long idRecurso, Date fecha
	 * @return List<RecConADec> 
	 */
	public List<RecAli> getListVigenteByIdRecursoYCodigo(Long id, Date fecha, String codigoAlicuota) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecAli r ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where r.recurso.id = " + id;

        queryString += " AND r.recTipAli.cod = '"+codigoAlicuota +"'";
        
        queryString += " AND r.fechaDesde <= :fecha";

        queryString += " AND (r.fechaHasta is null OR r.fechaHasta >= :fecha )";
        
        queryString += " ORDER BY r.alicuota ASC";
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString).setDate("fecha", fecha);
	    List<RecAli> listRecAli = (ArrayList<RecAli>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecAli;
	}
	
}
