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
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.iface.model.FeriadoSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * DAO correspondiente al bean Feriado
 * 
 * @author Tecso
 *
 */
public class FeriadoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(FeriadoDAO.class);	
	
	public FeriadoDAO() {
		super(Feriado.class);
	}
	
	public List<Feriado> getListBySearchPage(FeriadoSearchPage feriadoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Feriado t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del FeriadoSearchPage: " + feriadoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (feriadoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		  
		// filtro por fecha Desde
 		if (feriadoSearchPage.getFechaDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaFeriado >= TO_DATE('" + 
				DateUtil.formatDate(feriadoSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// 	 filtro por fecha Hasta
 		if (feriadoSearchPage.getFechaHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaFeriado <= TO_DATE('" + 
			DateUtil.formatDate(feriadoSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}

 		// 	 filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(feriadoSearchPage.getDesFeriado())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desFeriado)) like '%" + 
			StringUtil.escaparUpper(feriadoSearchPage.getDesFeriado()) + "%'";          		
			flagAnd = true;
		}

 		// Order By
		queryString += " order by t.fechaFeriado ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Feriado> listFeriado = (ArrayList<Feriado>) executeCountedSearch(queryString, feriadoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listFeriado;
	}

	/**
	 * 
	 * @param fecha
	 * @param estado Si es NULL no se lo tiene en cuenta
	 * @return
	 */
	public static Feriado get(Date fecha, Integer estado) {
		String queryString = "from Feriado t where t.fechaFeriado = TO_DATE('" + 
			DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
		if(estado!=null){
			queryString += " AND t.estado="+estado;
		}		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		Feriado feriado = (Feriado) query.uniqueResult();
		return feriado;
	}
	
	/**
	 * 
	 * @param fecha
	 * @param estado Si es NULL no se lo tiene en cuenta
	 * @return
	 */
	public static Boolean existeFeriado(Date fecha, Integer estado){
		return (get(fecha, estado)!=null?true:false);
	}
	
	

		
}
