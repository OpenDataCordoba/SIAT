//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.iface.model.BrocheSearchPage;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class BrocheDAO extends GenericDAO {
	
	private Logger log = Logger.getLogger(BrocheDAO.class);
	
	public BrocheDAO(){
		super(Broche.class);
	}
	
	/**
	 * Obtiene lista de Broche Activos para el Tipo de Broche y Recurso cuyos ids son pasados como parametros.
	 * @author tecso
	 * @param idTipoBroche, idRecurso. Si idTipoBroche es null, retorna todos los broches del recurso.
	 * @return List<Broche> 
	 */
	public List<Broche> getListActivosByIdTipoBrocheYIdRecurso(Long idTipoBroche, Long idRecurso) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Broche t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTipoBroche: " + idTipoBroche); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		if (idTipoBroche != null) { 
			queryString += " and t.tipoBroche.id = " + idTipoBroche;
		}

        queryString += " and t.recurso.id = " + idRecurso;
 		// Order By
		queryString += " order by t.tipoBroche.id, t.id ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Broche> listBroche = (ArrayList<Broche>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listBroche;
	}
	
	public List<Broche> getListBySearchPage(BrocheSearchPage brocheSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Broche t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del BrocheSearchPage: " + brocheSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (brocheSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Broche excluidos
 		List<BrocheVO> listBrocheExcluidos = (ArrayList<BrocheVO>) brocheSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listBrocheExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listBrocheExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if(!ModelUtil.isNullOrEmpty(brocheSearchPage.getBroche().getRecurso())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso = " +  brocheSearchPage.getBroche().getRecurso().getId();
			flagAnd = true;
		}
 		
 		// filtro por Tipo de Broche
 		if(!ModelUtil.isNullOrEmpty(brocheSearchPage.getBroche().getTipoBroche())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoBroche = " +  brocheSearchPage.getBroche().getTipoBroche().getId();
			flagAnd = true;
		}
 		
 		// filtro por id de Broche
 		if(brocheSearchPage.getBroche().getId()!=null && brocheSearchPage.getBroche().getId()>0){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.id = " +  brocheSearchPage.getBroche().getId();
			flagAnd = true;
		}
 		// Order By
		queryString += " order by t.tipoBroche";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Broche> listBroche = (ArrayList<Broche>) executeCountedSearch(queryString, brocheSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listBroche;
	}


}
