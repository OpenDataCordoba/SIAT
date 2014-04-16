//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.OpeInv;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvBus;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;

public class OpeInvBusDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OpeInvBusDAO.class);
	
	public OpeInvBusDAO() {
		super(OpeInvBus.class);
	}
	
	public List<OpeInvBus> getListByOpeInv (OpeInv opeInv){
		
		Session session = SiatHibernateUtil.currentSession();
		
		List<OpeInvBus> listOpeInvBus;
		
		String queryString = "FROM OpeInvBus o WHERE o.opeInv.id = " + opeInv.getId();
		Query query = session.createQuery(queryString);
		
		listOpeInvBus = (List<OpeInvBus>)query.list();
		
		return listOpeInvBus;
		
		
	}

	
	public List<OpeInvBus> getBySearchPage(OpeInvBusSearchPage opeInvBusSearchPage) throws Exception {
		log.debug("getBySearchPage - enter - idOpeInv:"+opeInvBusSearchPage.getIdOpeInv());
		String queryString = " FROM OpeInvBus o";
		boolean flagAnd = false;

		//filtra por opeInv
		if(opeInvBusSearchPage.getIdOpeInv()!=null && opeInvBusSearchPage.getIdOpeInv()>0){
			queryString+=flagAnd?" AND ":" WHERE ";
			queryString +=" o.opeInv.id="+opeInvBusSearchPage.getIdOpeInv();
			flagAnd = true;
		}
		
		// filtro por fechaDesde
		if(opeInvBusSearchPage.getFechaDesde()!=null){
			queryString+=flagAnd?" AND ":" WHERE ";
			queryString +=" o.fechaBusqueda > TO_DATE('"+DateUtil.formatDate(opeInvBusSearchPage.getFechaDesde(),
																	DateUtil.ddSMMSYYYY_MASK)  +"', '%d/%m/%Y')";
			flagAnd = true;
		}

		// filtro por fechaHasta
		if(opeInvBusSearchPage.getFechaHasta()!=null){
			queryString+=flagAnd?" AND ":" WHERE ";
			queryString +=" o.fechaBusqueda <= TO_DATE('"+DateUtil.formatDate(opeInvBusSearchPage.getFechaHasta(),
																	DateUtil.ddSMMSYYYY_MASK)  +"', '%d/%m/%Y')";
			flagAnd = true;
		}
		
		// filtro por estadoCorrida
		if(!ModelUtil.isNullOrEmpty(opeInvBusSearchPage.getEstadoCorrida())){
			queryString+=flagAnd?" AND ":" WHERE ";
			queryString +=" o.corrida.estadoCorrida.id = "+opeInvBusSearchPage.getEstadoCorrida().getId();
			flagAnd = true;
		}		
		
		// filtro por tipo de busqueda
		if(opeInvBusSearchPage.getTipBus()!=null){
			queryString+=flagAnd?" AND ":" WHERE ";
			queryString +=" o.tipBus = "+opeInvBusSearchPage.getTipBus();
			flagAnd = true;
			
		}
		
	    if (log.isDebugEnabled()) log.debug("Query: " + queryString);

		List<OpeInvBus> listOpeInvBus = (ArrayList<OpeInvBus>) executeCountedSearch(queryString, opeInvBusSearchPage);
		
		if (log.isDebugEnabled()) log.debug("exit");

		
		return listOpeInvBus;
	}

	public List<Integer> getListIdsCuentaTitular(String queryFrom,Integer firstResult, Integer maxResults) throws Exception{
		log.debug("getIdsCuentaTitular - enter");
		String queryString = "SELECT ";
		
		if(firstResult!=null)
			queryString += "SKIP "+firstResult;
		
		if(maxResults!=null)
			queryString +=" first "+maxResults;

		queryString += queryFrom;
		
		Session session = SiatHibernateUtil.currentSession();
 		Query query = session.createSQLQuery(queryString);
 		List<Integer> listIds = query.list();
		
		return listIds;
	}

}
