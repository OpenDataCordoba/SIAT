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

import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Area;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CuentaBancoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CuentaBancoDAO.class);
	
	public CuentaBancoDAO() {
		super(CuentaBanco.class);
	}
	
	public List<CuentaBanco> getBySearchPage(CuentaBancoSearchPage cuentaBancoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from CuentaBanco t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CuentaBancoSearchPage: " + cuentaBancoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (cuentaBancoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro cuentaBanco excluidos
 		List<CuentaBancoVO> listCuentaBancoExcluidos = (List<CuentaBancoVO>) cuentaBancoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCuentaBancoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCuentaBancoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Banco
 		if (!ModelUtil.isNullOrEmpty(cuentaBancoSearchPage.getCuentaBanco().getBanco())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " t.banco.id = " +  cuentaBancoSearchPage.getCuentaBanco().getBanco().getId();
			flagAnd = true;
		}
 		 	 	

		// filtro por Tipo de Cuenta
 		if (!ModelUtil.isNullOrEmpty(cuentaBancoSearchPage.getCuentaBanco().getTipCueBan())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " t.tipCueBan.id = " +  cuentaBancoSearchPage.getCuentaBanco().getTipCueBan().getId();
			flagAnd = true;
		}
 		
 	    // filtro por Area
 		if (!ModelUtil.isNullOrEmpty(cuentaBancoSearchPage.getCuentaBanco().getArea())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " t.area.id = " +  cuentaBancoSearchPage.getCuentaBanco().getArea().getId();
			flagAnd = true;
		}
 		
 		// filtro por nroCuenta
   		if (!StringUtil.isNullOrEmpty(cuentaBancoSearchPage.getCuentaBanco().getNroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.nroCuenta)) like '%" + 
			StringUtil.escaparUpper(cuentaBancoSearchPage.getCuentaBanco().getNroCuenta())+"%'";
			flagAnd = true;
		}
   		
 		// Order By
		queryString += " order by t.id ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CuentaBanco> listCuentaBanco = (ArrayList<CuentaBanco>) executeCountedSearch(queryString, cuentaBancoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCuentaBanco;
	}

	/**
	 * Obtiene un CuentaBanco por su Numero
	 */
	public CuentaBanco getByNroCuenta(String nroCuenta) throws Exception {
		CuentaBanco cuentaBanco;
		String queryString = "from CuentaBanco t where t.nroCuenta = :nroCuenta";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("nroCuenta", nroCuenta);
		cuentaBanco = (CuentaBanco) query.uniqueResult();	

		return cuentaBanco; 
	}
	
	/**
	 * Obtiene lista de CuentaBanco Activos para el Area especificada
	 * @author tecso
	 * @param Area area	
	 * @return List<CuentaBanco> 
	 */
	public List<CuentaBanco> getListActivosByArea(Area area) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from CuentaBanco t ";
	   
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.area.id = " + area.getId();

 		// Order By
		queryString += " order by t.nroCuenta ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<CuentaBanco> listCuentaBanco = (ArrayList<CuentaBanco>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCuentaBanco;
	}
	
	/**
	 * Obtiene la lista de Cuenta Banco ordenadas por descripcion
	 */
	public List<CuentaBanco> getListActivasOrderByNro() throws Exception {
		List<CuentaBanco> listCuentaBanco;
		String queryString = "from CuentaBanco t where t.estado = 1 order by t.nroCuenta";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		listCuentaBanco = (ArrayList<CuentaBanco>) query.list();	

		return listCuentaBanco; 
	}
	
}
