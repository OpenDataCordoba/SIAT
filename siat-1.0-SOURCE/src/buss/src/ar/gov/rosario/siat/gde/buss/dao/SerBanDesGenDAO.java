//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.gde.buss.bean.SerBanDesGen;
import ar.gov.rosario.siat.gde.iface.model.SerBanDesGenSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SerBanDesGenDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(SerBanDesGenDAO.class);	
	
	public SerBanDesGenDAO() {
		super(SerBanDesGen.class);
	}

	public List<SerBanDesGen> getListBySearchPage(SerBanDesGenSearchPage serBanDesGenSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from SerBanDesGen t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del SerBanDesGenSearchPage: " + serBanDesGenSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (serBanDesGenSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro por Servicio Banco
		if(!ModelUtil.isNullOrEmpty(serBanDesGenSearchPage.getSerBanDesGen().getServicioBanco())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.servicioBanco = " +  serBanDesGenSearchPage.getSerBanDesGen().getServicioBanco().getId();
			flagAnd = true;
		}
				
		// Order By
				
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<SerBanDesGen> listSerBanDesGen = (ArrayList<SerBanDesGen>) executeCountedSearch(queryString, serBanDesGenSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSerBanDesGen;
	}

	public SerBanDesGen getVigenteBySerBanYFecha(ServicioBanco servicioBanco, Date fecha){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from SerBanDesGen t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.servicioBanco.id = " + servicioBanco.getId();
        
        queryString += "and (t.fechaDesde <= TO_DATE('" + 
					DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
        queryString += "and ((t.fechaHasta is NULL)) or (t.fechaHasta >= TO_DATE('" + 
        			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<SerBanDesGen> listSerBanDesGen = (ArrayList<SerBanDesGen>) query.list();
	   
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		if(ListUtil.isNullOrEmpty(listSerBanDesGen)){
			return null;
		}else {
			return listSerBanDesGen.get(0);
		}
	}
	
}
