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
import ar.gov.rosario.siat.gde.buss.bean.Multa;
import ar.gov.rosario.siat.gde.iface.model.MultaSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class MultaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(MultaDAO.class);
	
	public MultaDAO() {
		super(Multa.class);
	}
	
	public List<Multa> getBySearchPage(MultaSearchPage multaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Multa t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del MultaSearchPage: " + multaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (multaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro id del multa
		if (!ModelUtil.isNullOrEmpty(multaSearchPage.getMulta())){
			// se cargo el nro de id de la multa desde la vista
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.id = " + multaSearchPage.getMulta().getId();
			flagAnd = true;
		}
		// filtro Cuenta
		if (!ModelUtil.isNullOrEmpty(multaSearchPage.getMulta().getCuenta())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.id = " + multaSearchPage.getMulta().getCuenta().getId();
			flagAnd = true;
		}
		
		// filtro Fecha Envio Desde
 		if (multaSearchPage.getFechaEmisionDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision >= TO_DATE('" + 
				DateUtil.formatDate(multaSearchPage.getFechaEmisionDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// filtro Fecha Envio Hasta
 		if (multaSearchPage.getFechaEmisionHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaEmision <= TO_DATE('" + 
			DateUtil.formatDate(multaSearchPage.getFechaEmisionHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Multa> listMulta = (ArrayList<Multa>) executeCountedSearch(queryString, multaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMulta;
	}

	/**
	 * Obtiene lista de Multas Activas para la Cuenta especificado
	 * @author tecso
	 * @param Long idCategoria	
	 * @return List<Multa> 
	 */
	public List<Multa> getListByCuenta(Long idCuenta) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Multa t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idCuenta: " + idCuenta); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.cuenta.id = " + idCuenta;

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Multa> listMulta = (ArrayList<Multa>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMulta;
	}
	
	/**
	 * Obtiene lista de Multas Activas para la Cuenta especificado
	 * @author tecso
	 * @param Long idCategoria	
	 * @return List<Multa> 
	 */
	public List<Multa> getListByCuentaTipoMulta(Long idCuenta, Long idTipoMulta) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Multa t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idCuenta: " + idCuenta); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.cuenta.id = " + idCuenta;
        queryString += " and t.tipoMulta.id = " + idTipoMulta;

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Multa> listMulta = (ArrayList<Multa>) query.list();
		
	    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMulta;
	}
	
	
	public List<Multa> getListByOrdenControl(Long idOrdenControl,Long idTipoMulta) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from Multa t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idOrdenControl: " + idOrdenControl); 
			log.debug("log de filtros: idTipoMulta: " + idTipoMulta);
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.ordenControl.id = " + idOrdenControl;        
        queryString += " and t.tipoMulta.id = " + idTipoMulta;  
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Multa> listMulta = (ArrayList<Multa>) query.list();
		
	    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMulta;
	}
}
