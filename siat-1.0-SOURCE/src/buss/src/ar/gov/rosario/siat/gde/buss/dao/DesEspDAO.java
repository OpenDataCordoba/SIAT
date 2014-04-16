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
import ar.gov.rosario.siat.gde.buss.bean.DesEsp;
import ar.gov.rosario.siat.gde.iface.model.DesEspSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DesEspVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DesEspDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DesEspDAO.class);	
	
	public DesEspDAO() {
		super(DesEsp.class);
	}
	
	public List<DesEsp> getBySearchPage(DesEspSearchPage desEspSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DesEsp t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DesEspSearchPage: " + desEspSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (desEspSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui		
		
		// filtro desEsp excluidos
 		List<DesEspVO> listDesEspExcluidos = (ArrayList<DesEspVO>) desEspSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDesEspExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDesEspExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(desEspSearchPage.getDesEsp().getDesDesEsp())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDesEsp)) like '%" + 
				StringUtil.escaparUpper(desEspSearchPage.getDesEsp().getDesDesEsp()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por recurso
 		if(desEspSearchPage.getDesEsp().getRecurso()!=null && desEspSearchPage.getDesEsp().getRecurso().getId()>0){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.recurso.id="+desEspSearchPage.getDesEsp().getRecurso().getId();
 			flagAnd = true;
 		}
 		
 		//filtro por tipoDeuda
 		if(desEspSearchPage.getDesEsp().getTipoDeuda()!=null && desEspSearchPage.getDesEsp().getTipoDeuda().getId()>0){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.tipoDeuda.id="+desEspSearchPage.getDesEsp().getTipoDeuda().getId();
 			flagAnd = true;
 		}
 		
 		//filtro por ViaDeuda
 		if(desEspSearchPage.getDesEsp().getViaDeuda()!=null && desEspSearchPage.getDesEsp().getViaDeuda().getId()>0){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.viaDeuda.id="+desEspSearchPage.getDesEsp().getViaDeuda().getId();
 			flagAnd = true;
 		}
 		
 		// Order By
		//queryString += " order by t.codDesEsp ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DesEsp> listDesEsp = (ArrayList<DesEsp>) executeCountedSearch(queryString, desEspSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDesEsp;
	}

	/**
	 * Obtiene un DesEsp por su codigo
	 */
	public DesEsp getByCodigo(String codigo) throws Exception {
		DesEsp desEsp;
		String queryString = "from DesEsp t where t.codDesEsp = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		desEsp = (DesEsp) query.uniqueResult();	

		return desEsp; 
	}
	
	/**
	 * Devuelve los descuentos especiales vigenets a la fecha de Vto. de la deuda, pasada como parametro
	 * @param idRecurso
	 * @param idTipoDeuda
	 * @param idViaDeuda
	 * @param fechaVtoDeuda
	 * @param activos
	 * @return
	 */
	public List<DesEsp> getListVigentes(Long idRecurso, Long idTipoDeuda, Long idViaDeuda, Date fechaVtoDeuda, Boolean activos) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DesEsp t ";
	    boolean flagAnd = false;
				 		
 		// filtro por recurso
 		if(idRecurso!=null && idRecurso>0){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.recurso.id="+idRecurso;
 			flagAnd = true;
 		}
 		
 		//filtro por tipoDeuda
 		if(idTipoDeuda!=null && idTipoDeuda>0){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.tipoDeuda.id="+idTipoDeuda;
 			flagAnd = true;
 		}
 		
 		//filtro por ViaDeuda
 		if(idViaDeuda!=null && idViaDeuda>0){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.viaDeuda.id="+idViaDeuda;
 			flagAnd = true;
 		}
 		//filtro por fechas
 		if(fechaVtoDeuda!=null){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.fechaVtoDeudaDesde <= TO_DATE('" + 
			DateUtil.formatDate(fechaVtoDeuda, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
			" AND (t.fechaVtoDeudaHasta IS NULL OR t.fechaVtoDeudaHasta >= TO_DATE('"+DateUtil.formatDate(fechaVtoDeuda, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
 		}
 		
 		//filtro por estado	activo/inactivo
		if(activos!=null){
			queryString += flagAnd ? " and " : " where ";
			if (activos) {				
			      queryString += " t.estado = "+ Estado.ACTIVO.getId();			      
			}else{
				queryString += " t.estado = "+ Estado.INACTIVO.getId();
			}
			flagAnd = true;
		}
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		List<DesEsp> listDesEsp = (ArrayList<DesEsp>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDesEsp;
	}
}
