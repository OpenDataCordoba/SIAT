//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.buss.bean.Contrato;
import ar.gov.rosario.siat.rec.iface.model.ContratoSearchPage;
import ar.gov.rosario.siat.rec.iface.model.ContratoVO;
import ar.gov.rosario.siat.rec.iface.model.TipoContratoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ContratoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ContratoDAO.class);	

	public ContratoDAO() {
		super(Contrato.class);
	}

	public List<Contrato> getBySearchPage(ContratoSearchPage contratoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "from Contrato contrato ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ContratoSearchPage: " + contratoSearchPage.infoString()); 
		}

		// Armamos filtros del HQL
		if (contratoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " contrato.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// Filtros aqui
		// filtro contrato excluidos
 		List<ContratoVO> listContratoExcluidos = (ArrayList<ContratoVO>) contratoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listContratoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listContratoExcluidos);
			queryString += " contrato.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
 		
		// filtro por recurso
 		RecursoVO recurso = contratoSearchPage.getContrato().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " contrato.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}
 		
		// filtro por tipo
 		TipoContratoVO tipoContrato = contratoSearchPage.getContrato().getTipoContrato();
 		if (!ModelUtil.isNullOrEmpty(tipoContrato)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " contrato.tipoContrato.id = " + tipoContrato.getId(); 
			flagAnd = true;
		}
 		
		// filtro por numero
 		if (!StringUtil.isNullOrEmpty(contratoSearchPage.getContrato().getNumero())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " contrato.numero = '" + contratoSearchPage.getContrato().getNumero() + "'"; 
			flagAnd = true;
		}
 		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(contratoSearchPage.getContrato().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(contrato.descripcion)) like '%" + 
				StringUtil.escaparUpper(contratoSearchPage.getContrato().getDescripcion()) + "%'";
			flagAnd = true;
		}

        // Order By
		queryString += " order by contrato.recurso.desRecurso, contrato.tipoContrato, contrato.numero ";
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Contrato> listContrato = (ArrayList<Contrato>) executeCountedSearch(queryString, contratoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listContrato;
	}
	
	/**
	 * Obtiene lista de Contratos Activos para el Recurso especificado
	 * @author tecso
	 * @param Long idRecurso	
	 * @return List<Contrato> 
	 */
	public List<Contrato> getListActivosByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Contrato t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idContrato: " + id); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.recurso.id = " + id;

        // Order By
		queryString += " order by t.descripcion ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Contrato> listContrato = (ArrayList<Contrato>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listContrato;
	}

	/**
	 * Obtiene lista de Contratos para el Recurso especificado
	 * @author tecso
	 * @param Long idRecurso	
	 * @return List<Contrato> 
	 */
	public List<Contrato> getListByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Contrato t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idContrato: " + id); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.recurso.id = " + id;

        // Order By
		queryString += " order by t.descripcion ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Contrato> listContrato = (ArrayList<Contrato>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listContrato;
	}
	
}


