//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.SaldoAFavor;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SaldoAFavorDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SaldoAFavorDAO.class);
	
	public SaldoAFavorDAO() {
		super(SaldoAFavor.class);
	}
	
	public List<SaldoAFavor> getBySearchPage(SaldoAFavorSearchPage saldoAFavorSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from SaldoAFavor t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del SaldoAFavorSearchPage: " + saldoAFavorSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (saldoAFavorSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
	
				
		// filtro saldoAFavor excluidos
 		List<SaldoAFavorVO> listSaldoAFavorExcluidos = (List<SaldoAFavorVO>) saldoAFavorSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listSaldoAFavorExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listSaldoAFavorExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
 		// Filtros aqui
 		
 	   // filtro por Cuenta
 		if (!ModelUtil.isNullOrEmpty(saldoAFavorSearchPage.getSaldoAFavor().getCuenta())) {
            queryString += flagAnd ? " and " : " where ";
		 	queryString += " t.cuenta.id = " +  saldoAFavorSearchPage.getSaldoAFavor().getCuenta().getId();
			flagAnd = true;
		}

 	    // Order By
		queryString += " order by t.id ";
		
 	  	if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<SaldoAFavor> listSaldoAFavor = (ArrayList<SaldoAFavor>) executeCountedSearch(queryString, saldoAFavorSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSaldoAFavor;
	}

	
	/**
	 * Obtiene lista de Saldo A Favor Activos para la Cuenta especificada
	 * @author tecso
	 * @param Cuenta cuenta	
	 * @return List<SaldoAFavor> 
	 */
	public List<SaldoAFavor> getListActivosByCuenta(Cuenta cuenta) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from SaldoAFavor t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idCuenta: " + cuenta.getId()); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.cuenta.id = " + cuenta.getId();

 		// Order By
		queryString += " order by t.descripcion ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<SaldoAFavor> listSaldoAFavor = (ArrayList<SaldoAFavor>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSaldoAFavor;
	}

}
