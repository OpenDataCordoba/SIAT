//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.emi.buss.bean.EmiInfCue;
import ar.gov.rosario.siat.emi.iface.model.EmiInfCueSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EmiInfCueDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EmiInfCueDAO.class);
	
	public EmiInfCueDAO() {
		super(EmiInfCueDAO.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<EmiInfCue> getBySearchPage(EmiInfCueSearchPage emiInfCueSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from EmiInfCue t ";
	    boolean flagAnd = false;
	    
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EmiInfCueSearchPage: " + emiInfCueSearchPage.infoString()); 
		}
	
		// filtro por Numero de Cuenta
 		CuentaVO cuentaVO = emiInfCueSearchPage.getEmiInfCue().getCuenta();
		if (!StringUtil.isNullOrEmpty(cuentaVO.getNumeroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += "t.cuenta.numeroCuenta = '" + 
            	StringUtil.formatNumeroCuenta(cuentaVO.getNumeroCuenta()) +"'";
			flagAnd = true;
		}

		// Filtro por Anio
		if (!StringUtil.isNullOrEmpty(cuentaVO.getNumeroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += "t.anio = " +  emiInfCueSearchPage.getResLiqDeu().getAnioView();
			flagAnd = true;
		}

		// Filtro por Periodo Desde
		if (!StringUtil.isNullOrEmpty(cuentaVO.getNumeroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += "t.periodoDesde = " +  emiInfCueSearchPage.getResLiqDeu().getPeriodoDesdeView();
			flagAnd = true;
		}

		// Filtro por Perioodo Hasta
		if (!StringUtil.isNullOrEmpty(cuentaVO.getNumeroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += "t.periodoHasta = " +  emiInfCueSearchPage.getResLiqDeu().getPeriodoHastaView();
			flagAnd = true;
		}

 		// Order By
		queryString += " order by t.tag ";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<EmiInfCue> listEmiInfCue = (ArrayList<EmiInfCue>) executeCountedSearch(queryString, emiInfCueSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEmiInfCue;
	}
	
	public int deleteBy(Long idRecurso, Integer anio, Integer periodoDesde, Integer periodoHasta) {
	 	String strQuery = ""; 
	 	strQuery += "delete from EmiInfCue emiInfCue ";
	 	strQuery +=	"where emiInfCue.recurso.id = :idRecurso ";
	 	strQuery +=	"and emiInfCue.anio = :anio ";
	 	strQuery +=	"and emiInfCue.periodoDesde >= :periodoDesde ";
	 	strQuery +=	"and emiInfCue.periodoHasta <= :periodoHasta ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
						.setLong("idRecurso", idRecurso)
						.setInteger("anio", anio)
						.setInteger("periodoDesde", periodoDesde)
						.setInteger("periodoHasta", periodoHasta);
		
		return query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<EmiInfCue> getListBy(Long idCuenta, Integer anio, Integer periodoDesde, Integer periodoHasta) {
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "select emiInfCue from EmiInfCue emiInfCue ";
		strQuery +=	"where emiInfCue.cuenta.id = :idCuenta ";
	 	strQuery +=	"and emiInfCue.anio = :anio ";
	 	strQuery +=	"and emiInfCue.periodoDesde >= :periodoDesde ";
	 	strQuery +=	"and emiInfCue.periodoHasta <= :periodoHasta ";
		strQuery +=	"order by emiInfCue.tag ";
				
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
						.setLong("idCuenta", idCuenta)
						.setInteger("anio", anio)
						.setInteger("periodoDesde", periodoDesde)
						.setInteger("periodoHasta", periodoHasta);
	
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": exit");
	
		return (ArrayList<EmiInfCue>) query.list(); 
	}

}
