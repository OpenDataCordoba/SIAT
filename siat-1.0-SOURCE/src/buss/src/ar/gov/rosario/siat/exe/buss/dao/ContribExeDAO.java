//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.exe.buss.bean.ContribExe;
import ar.gov.rosario.siat.exe.iface.model.ContribExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ContribExeDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ContribExeDAO.class);	

	public ContribExeDAO() {
		super(ContribExe.class);
	}

	public List<ContribExe> getBySearchPage(ContribExeSearchPage contribExeSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ContribExe contribExe ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ContribExeSearchPage: " + contribExeSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		// filtro de activos
		if (contribExeSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " contribExe.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro atributos excluidos
 		List<ContribExe> listContribExeExcluidos = (List<ContribExe>) contribExeSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listContribExeExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listContribExeExcluidos);
			queryString += " contribExe.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

		// filtro por descripcion
		String desContribExe = contribExeSearchPage.getContribExe().getDesContribExe();
 		if (!StringUtil.isNullOrEmpty(desContribExe)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(contribExe.desContribExe)) like '%" + 
				StringUtil.escaparUpper(desContribExe) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por contribuyente
 		ContribuyenteVO contribuyenteVO = contribExeSearchPage.getContribExe().getContribuyente();  
 		if (!ModelUtil.isNullOrEmpty(contribuyenteVO)){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " contribExe.contribuyente.id = " + contribuyenteVO.getId();
			flagAnd = true; 			
 		}
 		
 		// filtro por exencion
 		ExencionVO exencionVO = contribExeSearchPage.getContribExe().getExencion();  
 		if (!ModelUtil.isNullOrEmpty(exencionVO)){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " contribExe.exencion.id = " + exencionVO.getId();
			flagAnd = true; 			
 		}
 		
 		//filtro por fecha
 		Date fechaDesde = contribExeSearchPage.getFechaDesde();
 		Date fechaHasta = contribExeSearchPage.getFechaHasta();
 		
 		if(fechaDesde != null  && fechaHasta != null){
 			queryString += flagAnd ? " and " : " where ";
            queryString += " contribExe.fechaDesde >= TO_DATE('" + 
			DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 				
            queryString += " AND (contribExe.fechaHasta IS NOT NULL AND contribExe.fechaHasta <= TO_DATE('" + 
			DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
 		}else{
 			if (fechaDesde != null){// Solamente se ingresó fecha desde
 	            queryString += flagAnd ? " and " : " where ";
 				queryString += " contribExe.fechaDesde >= TO_DATE('" + 
 					DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 				
 				flagAnd = true; 			
 			}
 			if (fechaHasta != null){// Solamente se ingresó fecha hasta
 	            queryString += flagAnd ? " and " : " where ";
 	            queryString += " contribExe.fechaDesde <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 				
 	            queryString += " AND (contribExe.fechaHasta IS NULL OR (contribExe.fechaHasta IS NOT NULL AND contribExe.fechaHasta <= TO_DATE('" + 
 				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')))"; 				
 				flagAnd = true; 			
 			}
 		}
 		
 		// Order By
		queryString += " order by contribExe.desContribExe ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ContribExe> listContribExe = (ArrayList<ContribExe>) 
			executeCountedSearch(queryString, contribExeSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listContribExe;
	}
	
	/** 
	 * 
	 * Obtiene la cantidad de ContribExe que 
	 *  coinciden con el ContribExe, Clasificacion Deuda,
	 *  periodo y anio del pasado como parametro
	 * 
	 */

	public Long getTotalByContribExe(ContribExe contribExeToCompare) 
		throws Exception {

		Session session = SiatHibernateUtil.currentSession();

		Date fechaHasta = contribExeToCompare.getFechaHasta();

		// si la fecha hasta es nula cargo una fecha muy alta
		// para comparar contra esa
		if (fechaHasta == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient( false );
			fechaHasta = sdf.parse("30/12/2999");
		}

		// SELECT COUNT(contribExe) 

		String queryString = "SELECT COUNT(contribExe) FROM ContribExe contribExe " +
				             "WHERE contribExe.contribuyente.id = :idContribuyente AND ";
				            
		if (contribExeToCompare.getExencion() != null)
			queryString += "contribExe.exencion = :exencion AND ";
				             
		queryString += "((contribExe.fechaHasta IS NOT NULL AND " +
		             "contribExe.fechaHasta >= :fechaDesde AND " +
		             "contribExe.fechaDesde <= :fechaHasta) OR " +
		             "(contribExe.fechaHasta IS NULL AND " +
		             "contribExe.fechaDesde <= :fechaHasta)) ";
		
		// si estoy en una actualizacion verifico no comprar contra el mismo 
		// objeto pasado como parametro
		if (contribExeToCompare.getId() != null) {
			queryString = queryString + "AND contribExe.id <> :id ";
		}

		Query query = session.createQuery(queryString)
							.setLong("idContribuyente", contribExeToCompare.getContribuyente().getId());
							
		if (contribExeToCompare.getExencion() != null)					
			query.setEntity("exencion", contribExeToCompare.getExencion());
				
		query.setDate("fechaDesde", contribExeToCompare.getFechaDesde())
			 .setDate("fechaHasta", fechaHasta);

		if (contribExeToCompare.getId() != null) {
			query.setLong("id", contribExeToCompare.getId());
		}

		Long totalContribExe = (Long) query.uniqueResult();	
		//List<ContribExe> list = (ArrayList<ContribExe>) query.list();

		return totalContribExe; 
	}
	
	/**
	 * Obtiene un ContribExe Vigente por id de Contribuyente
	 */
	public ContribExe getVigenteByIdContribuyente(Long idContribuyente, Date fechaToCompare) throws Exception {
		ContribExe ContribExe;
		String queryString = "from ContribExe t where t.contribuyente.id = "+idContribuyente
							+ " and t.fechaDesde<= TO_DATE('"+DateUtil.formatDate(fechaToCompare, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"
							+" and (t.fechaHasta is null or t.fechaHasta>=TO_DATE('"+DateUtil.formatDate(fechaToCompare, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		//		+ " and t.fechaDesde<"+fechaToCompare
		//		+" and (t.fechaHasta is null or t.fechaHasta>"+fechaToCompare+")";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		ContribExe = (ContribExe) query.uniqueResult();	

		return ContribExe; 
	}

}
