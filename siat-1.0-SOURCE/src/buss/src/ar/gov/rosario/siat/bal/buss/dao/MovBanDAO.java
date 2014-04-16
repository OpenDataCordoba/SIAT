//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.MovBan;
import ar.gov.rosario.siat.bal.iface.model.MovBanSearchPage;
import ar.gov.rosario.siat.bal.iface.model.MovBanVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class MovBanDAO extends GenericDAO {

	private Log log = LogFactory.getLog(MovBanDAO.class);
	
	public MovBanDAO() {
		super(MovBan.class);
	}

	public List<MovBan> getListBySearchPage(MovBanSearchPage movBanSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from MovBan t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del MovBanSearchPage: " + movBanSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (movBanSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro MovBan excluidos
 		List<MovBanVO> listMovBanExcluidos = (ArrayList<MovBanVO>) movBanSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listMovBanExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listMovBanExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Banco Adm
 		if(movBanSearchPage.getMovBan().getBancoAdm() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.bancoAdm = " +  movBanSearchPage.getMovBan().getBancoAdm();
			flagAnd = true;
		}

 		// filtro por EstadoArc
 		if(movBanSearchPage.getMovBan().getConciliado() != null && movBanSearchPage.getMovBan().getConciliado().getId().intValue() != SiNo.OpcionTodo.getId().intValue()){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.conciliado = " +  movBanSearchPage.getMovBan().getConciliado().getId();
			flagAnd = true;
		}
 		
 		// 	 filtro por Fecha Acreditacion Desde
		if (movBanSearchPage.getFechaDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaAcredit >= TO_DATE('" + 
					DateUtil.formatDate(movBanSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Acreditacion Hasta
		if (movBanSearchPage.getFechaHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaAcredit <= TO_DATE('" + 
					DateUtil.formatDate(movBanSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// Order By
		queryString += " order by t.fechaAcredit DESC, t.bancoAdm";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<MovBan> listMovBan = (ArrayList<MovBan>) executeCountedSearch(queryString, movBanSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listMovBan;
	}
	
	/**
	 *  Devuelve verdadero si existe algun Movimiento Bancario cargado con la fecha de acreditación indicada 
	 * 
	 * @param boolean
	 * @return
	 * @throws Exception
	 */
	public Boolean existeParaFechaAcredit(Date fechaAcredit) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from MovBan t";
		queryString += " where t.fechaAcredit = :fecha";
	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString).setDate("fecha", fechaAcredit);
	    query.setMaxResults(1);
	    MovBan movBan = (MovBan) query.uniqueResult();
		if(movBan != null)
			return true;
		
		return false;
	}
}
