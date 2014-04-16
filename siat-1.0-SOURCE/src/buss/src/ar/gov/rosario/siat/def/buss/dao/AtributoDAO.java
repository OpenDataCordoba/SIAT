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
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.iface.model.AtributoSearchPage;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AtributoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AtributoDAO.class);	
	
	public AtributoDAO() {
		super(Atributo.class);
	}
	
	public List<Atributo> getListBySearchPage(AtributoSearchPage atributoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Atributo t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AtributoSearchPage: " + atributoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		// Si estoy en modo seleccionar solo muestro los activos
		if (atributoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro atributos excluidos
 		List<AtributoVO> listAtributosExcluidos = (List<AtributoVO>) atributoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listAtributosExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAtributosExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codAtributo
 		String codAtributo = atributoSearchPage.getAtributo().getCodAtributo();
 		if (!StringUtil.isNullOrEmpty(codAtributo)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codAtributo)) like '%" + 
				StringUtil.escaparUpper(codAtributo) + "%'";
			flagAnd = true;
		}
		
		// filtro por descAtributo
 		String desAtributo = atributoSearchPage.getAtributo().getDesAtributo();
 		if (!StringUtil.isNullOrEmpty(desAtributo)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desAtributo)) like '%" + 
				StringUtil.escaparUpper(desAtributo) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codAtributo ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		List<Atributo> listAtributo = (ArrayList<Atributo>) executeCountedSearch(queryString, atributoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAtributo;
	}
	
	/**
	 * Obtiene una aplicacion por su codigo
	 */
	public Atributo getByCodigo(String codigo) throws Exception {
		Atributo atributo;
		String queryString = "from Atributo t where t.codAtributo = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		atributo = (Atributo) query.uniqueResult();	

		return atributo; 
	}

	/**
	 * Obtiene la lista de Atributos de Contribuyente marcados como atributos de busqueda
	 * @return List<Atributo>
	 * @throws Exception
	 */
	public List<Atributo> getListAtributoDeContribuyenteForBusquedaMasiva() throws Exception {
		
		String queryString = "SELECT DISTINCT(ca.atributo) FROM ConAtr ca WHERE ca.esAtrSegmentacion = 1 ";

		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		return  (ArrayList<Atributo>) query.list();
	}

	/**
	 * Obtiene una lista de atributos con id en la lista 
	 * pasada como parametro.
	 */
	@SuppressWarnings("unchecked")
	public List<Atributo> getList(List<Long> listId) throws Exception {
		
		String queryString = "from Atributo t where t.id in (:listId)";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString)
							 .setParameterList("listId", listId);
		
		return query.list();	
	}
	
}
