//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rec.buss.bean.CatRSDrei;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CatRSDreiDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(CatRSDreiDAO.class);	

	private static long migId = -1;
	
	public CatRSDreiDAO() {
		super(CatRSDrei.class);
	}
	
	/**
	 * Retorna las categorias de DReI vigentes a la fecha actual
	 * @return Lista de CatRSDrei
	 */
	public List<CatRSDrei> getListVigentes(){
		Session session = SiatHibernateUtil.currentSession();
		List<CatRSDrei> listCategoria=new ArrayList<CatRSDrei>();
		
		String queryString = "FROM CatRSDrei c WHERE c.fechaDesde <= :fecha";
		queryString += " AND (c.fechaHasta IS NULL OR c.fechaHasta >= :fecha )";
		queryString += " ORDER BY c.nroCategoria";
		
		Query query = session.createQuery(queryString).setDate("fecha", new Date());
		
		listCategoria = (List<CatRSDrei>) query.list();
		
		return listCategoria;
	}

	
	/**
	 * Retorna la categoria segun el numero
	 * @return CatRSDrei
	 */
	public CatRSDrei getCatByNro( Integer nroCategoria) {
		Session session = SiatHibernateUtil.currentSession();
		CatRSDrei categoria=new CatRSDrei();
		
		
		String queryString = "FROM CatRSDrei c WHERE c.nroCategoria = :nroCategoria";
		
		log.debug(queryString + " nroCategoria=" + nroCategoria);
		
		Query query = session.createQuery(queryString).setInteger("nroCategoria", nroCategoria);
		
		query.setMaxResults(1);
		
		categoria = (CatRSDrei) query.uniqueResult();
		
		return categoria;
	}
	
	
	/**
	 * Retorna la categoria con mayor valor de ingresos brutos
	 * @return CatRSDrei
	 */
	public CatRSDrei getCatVigMayorIngBru(){
		Session session = SiatHibernateUtil.currentSession();
		CatRSDrei categoria=new CatRSDrei();
		
		String queryString = "FROM CatRSDrei c WHERE c.fechaDesde <= :fecha";
		queryString += " AND (c.fechaHasta IS NULL OR c.fechaHasta >= :fecha )";
		queryString += " ORDER BY c.ingBruAnu DESC";
		
		Query query = session.createQuery(queryString).setDate("fecha", new Date());
		
		query.setMaxResults(1);
		
		categoria = (CatRSDrei) query.uniqueResult();
		
		return categoria;
	}
	
	/**
	 * Retorna la categoria con mayor valor de superficie afectada
	 * @return CatRSDrei
	 */
	public CatRSDrei getCatVigMayorSuperficie(){
		Session session = SiatHibernateUtil.currentSession();
		CatRSDrei categoria=new CatRSDrei();
		
		String queryString = "FROM CatRSDrei c WHERE c.fechaDesde <= :fecha";
		queryString += " AND (c.fechaHasta IS NULL OR c.fechaHasta >= :fecha )";
		queryString += " ORDER BY c.superficie DESC";
		
		Query query = session.createQuery(queryString).setDate("fecha", new Date());
		
		query.setMaxResults(1);
		
		categoria = (CatRSDrei) query.uniqueResult();
		
		return categoria;
	}
	
	public List<CatRSDrei> getBySearchPage(CatRSDreiSearchPage catRSDreiSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String nroCategoria = null;
		if (null != catRSDreiSearchPage.getCatRSDrei().getNroCategoria()) {
			nroCategoria = catRSDreiSearchPage.getCatRSDrei().getNroCategoria().toString();
		}
		
		String queryString = "from CatRSDrei c ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CatRSDreiSearchPage: " + catRSDreiSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (catRSDreiSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " c.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
				
 		if (!StringUtil.isNullOrEmpty(nroCategoria)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " (c.nroCategoria) = " + nroCategoria + " ";				
			flagAnd = true;
		}
		
 		// filtro Fecha Envio Desde
 		if (catRSDreiSearchPage.getFechaCatRSDreiDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " c.fechaDesde >= TO_DATE('" + 
				DateUtil.formatDate(catRSDreiSearchPage.getFechaCatRSDreiDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// filtro Fecha Envio Hasta
 		if (catRSDreiSearchPage.getFechaCatRSDreiHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " c.fechaHasta <= TO_DATE('" + 
			DateUtil.formatDate(catRSDreiSearchPage.getFechaCatRSDreiHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 				
 		// Order By
		queryString += " order by c.nroCategoria ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CatRSDrei> listCatRSDrei = (ArrayList<CatRSDrei>) executeCountedSearch(queryString, catRSDreiSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCatRSDrei;
	}
	
	
}
