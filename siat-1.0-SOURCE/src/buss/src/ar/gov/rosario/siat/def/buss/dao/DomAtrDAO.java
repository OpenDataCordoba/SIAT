//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.DomAtr;
import ar.gov.rosario.siat.def.iface.model.DomAtrSearchPage;
import ar.gov.rosario.siat.def.iface.model.DomAtrVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DomAtrDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DomAtrDAO.class);	
	
	public DomAtrDAO() {
		super(DomAtr.class);
	}
	
	public List<DomAtr> getListBySearchPage(DomAtrSearchPage domAtrSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DomAtr t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DomAtrSearchPage: " + domAtrSearchPage.infoString()); 
		}
	
		// Filtros aqui
		// Armamos filtros del HQL
		if (domAtrSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro Dominio Atributo excluidos
 		List<DomAtrVO> listDomAtrExcluidos = (List<DomAtrVO>) domAtrSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDomAtrExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDomAtrExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

		// filtro por codigo
		String codDomAtr = domAtrSearchPage.getDomAtr().getCodDomAtr();
 		if (!StringUtil.isNullOrEmpty(codDomAtr)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codDomAtr)) like '%" + 
				StringUtil.escaparUpper(codDomAtr) + "%'";
			flagAnd = true;
		}

		// filtro por descDomAtr
		String desDomAtr = domAtrSearchPage.getDomAtr().getDesDomAtr();
 		if (!StringUtil.isNullOrEmpty(desDomAtr)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDomAtr)) like '%" + 
				StringUtil.escaparUpper(desDomAtr) + "%'";
			flagAnd = true;
		}

 		// Order By
		queryString += " order by t.desDomAtr ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DomAtr> listDomAtr = (ArrayList<DomAtr>) executeCountedSearch(queryString, domAtrSearchPage);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDomAtr;
	}
	
	/**
	 * Obtiene la lista de Dominios Atributo activos para el id del tipo de atributo
	 * @param idTipoAtributo id del Tipo de Atributo 
	 * @return List<DomAtr>
	 */
	public List<DomAtr> getListActivosByIdTipoAtributo(Long idTipoAtributo){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DomAtr t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del tipoAtributo: " + idTipoAtributo); 
		}
	
		// Armamos filtros del HQL
		queryString += flagAnd ? " and " : " where ";
	    queryString += " t.estado = "+ Estado.ACTIVO.getId();
	    flagAnd = true;
 		
        queryString += flagAnd ? " and " : " where ";
		queryString += " t.tipoAtributo.id = " + idTipoAtributo;

 		// Order By
		queryString += " order by t.desDomAtr ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
		List<DomAtr> listDomAtr = (ArrayList<DomAtr>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDomAtr;
		
	}
	
	/**
	 * Obtiene una aplicacion por su codigo
	 */
	public DomAtr findByCodigo(String codigo) throws Exception {
		DomAtr domAtr;
		String queryString = "from DomAtr t where t.codigo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		domAtr = (DomAtr) query.uniqueResult();	

		return domAtr; 
	}
	
}
