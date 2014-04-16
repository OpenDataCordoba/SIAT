//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatSearchPage;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class UsuarioSiatDAO extends GenericDAO {

	private Log log = LogFactory.getLog(UsuarioSiatDAO.class);	
	
	
	public UsuarioSiatDAO() {
		super(UsuarioSiat.class);
	}
	
	public List<UsuarioSiat> getBySearchPage(UsuarioSiatSearchPage usuarioSiatSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "FROM UsuarioSiat usuarioSiat";

	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del UsuarioSiatSearchPage: " + usuarioSiatSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (usuarioSiatSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " usuarioSiat.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro usuarioSiat excluidos
 		List<UsuarioSiatVO> listUsuarioSiatExcluidos = (List<UsuarioSiatVO>) usuarioSiatSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listUsuarioSiatExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listUsuarioSiatExcluidos);
			queryString += " usuarioSiat.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por nombre usuario
 		if (!StringUtil.isNullOrEmpty(usuarioSiatSearchPage.getUsuarioSiat().getUsuarioSIAT())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(usuarioSiat.usuarioSIAT)) like '%" + 
				StringUtil.escaparUpper(usuarioSiatSearchPage.getUsuarioSiat().getUsuarioSIAT()) + "%'";
			flagAnd = true;
		}

		// filtro por area
 		AreaVO area = usuarioSiatSearchPage.getUsuarioSiat().getArea();
 		if (!ModelUtil.isNullOrEmpty(area)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " usuarioSiat.area.id = " + area.getId();
			flagAnd = true;
		}

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<UsuarioSiat> listUsuarioSiat = (ArrayList<UsuarioSiat>) executeCountedSearch(queryString, usuarioSiatSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return listUsuarioSiat;

	}
	
	/**
	 * Obtiene un UsuarioSiat por su nombre
	 */
	public UsuarioSiat getByUserName(String userName) throws Exception {
		UsuarioSiat usuarioSiat;
		String queryString = "from UsuarioSiat t where t.usuarioSIAT = :userName";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("userName", userName);
		usuarioSiat = (UsuarioSiat) query.uniqueResult();	

		return usuarioSiat; 
	}
}
