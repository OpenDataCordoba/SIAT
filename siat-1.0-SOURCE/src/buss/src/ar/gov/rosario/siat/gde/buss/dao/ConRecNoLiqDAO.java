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
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.iface.model.ConRecNoLiqSearchPage;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;


public class ConRecNoLiqDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConRecNoLiqDAO.class);	
	
	public ConRecNoLiqDAO() {
		super(null);
	}
	
	public List<BaseBO> getBySearchPage(ConRecNoLiqSearchPage conRecNoLiqSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ConRecNoLiqSearchPage: " + conRecNoLiqSearchPage.infoString()); 
		}

		Long idRecurso = conRecNoLiqSearchPage.getIdRecurso()!=null?new Long(conRecNoLiqSearchPage.getIdRecurso()):null;
		Long idProcurador = conRecNoLiqSearchPage.getIdProcurador()!=null?new Long(conRecNoLiqSearchPage.getIdProcurador()):null;
		Integer numero = conRecNoLiqSearchPage.getNumero()!=null?new Integer(conRecNoLiqSearchPage.getNumero()):null;
		Date fechaDesde= conRecNoLiqSearchPage.getFechaDesde();
		Date fechaHasta= conRecNoLiqSearchPage.getFechaHasta();

		// query para recibo
		if(conRecNoLiqSearchPage.getTipoPago().equals("2")){
			queryString ="from Recibo t ";			

			// numeroRecibo		
			if(numero!=null && numero>0){
	 			queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += " t.nroRecibo="+numero; 
				flagAnd = true;
			}
			
			// idRecurso
			if(idRecurso!=null && idRecurso.longValue()>0){
	 			queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += " t.recurso.id="+idRecurso; 
				flagAnd = true;			
			}
			
			// fechaEmisionDesde
			if(fechaDesde!=null){
	 			queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += " t.fechaGeneracion>= TO_DATE('" + 
						DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
				flagAnd = true;			
			}
			
			// fechaEmisionHasta
			if(fechaHasta!=null){
	 			queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += " t.fechaGeneracion<= TO_DATE('" + 
						DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
				flagAnd = true;			
			}
						
			// procurador
			if(idProcurador!=null && idProcurador>0){
	 			queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += " t.procurador.id="+idProcurador; 
				flagAnd = true;			
			}					
			
		}else if(conRecNoLiqSearchPage.getTipoPago().equals("1")){		
		// query para convenios
			
			queryString ="SELECT t from Convenio t ";
			flagAnd = false;
	
			
			// idRecurso
			if(idRecurso!=null && idRecurso.longValue()>0){
	 			//queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += ", PlanRecurso plaRec WHERE t.plan.id =plaRec.plan.id AND plaRec.recurso.id="+idRecurso; 
				flagAnd = true;			
			}
			
			// numero convenio		
			if(numero!=null && numero>0){
				queryString += flagAnd ? " AND " : " 	WHERE ";
				queryString += " t.nroConvenio="+numero; 
				flagAnd = true;
			}
			
			// fechaVtoDesde
			if(fechaDesde!=null){
	 			queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += " t.fechaFor>= TO_DATE('" + 
						DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
				flagAnd = true;			
			}
			
			// fechaVtoHasta
			if(fechaHasta!=null){
	 			queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += " t.fechaFor<= TO_DATE('" + 
						DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
				flagAnd = true;			
			}
			
			// procurador
			if(idProcurador!=null && idProcurador>0){
	 			queryString += flagAnd ? " AND " : " 	WHERE ";
	 			queryString += " t.procurador.id="+idProcurador; 
				flagAnd = true;			
			}
		}
		
		// viaDeuda
		queryString += flagAnd ? " AND " : " 	WHERE ";
		queryString += " t.viaDeuda.id="+ViaDeuda.ID_VIA_JUDICIAL; 
		flagAnd = true;						

		conRecNoLiqSearchPage.setRecsByPage(20L);
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		List<BaseBO> listResult = (ArrayList<BaseBO>) executeSearch(queryString, conRecNoLiqSearchPage);
		conRecNoLiqSearchPage.setMaxRegistros(new Long(getCount(query)));		
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listResult;
	}
}
