//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.esp.buss.bean.TipoEntrada;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaSearchPage;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TipoEntradaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipoEntradaDAO.class);	
	
	public TipoEntradaDAO(){
		super(TipoEntrada.class);
	}
	
	public List<TipoEntrada> getBySearchPage(TipoEntradaSearchPage tipoEntradaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TipoEntrada t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TipoEntradaSearchPage: " + tipoEntradaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tipoEntradaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		
		// filtro tipoEntrada excluidos
 		List<TipoEntradaVO> listTipoEntradaExcluidos = (List<TipoEntradaVO>) tipoEntradaSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTipoEntradaExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTipoEntradaExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
	
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tipoEntradaSearchPage.getTipoEntrada().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(tipoEntradaSearchPage.getTipoEntrada().getDescripcion()) + "%'";
			flagAnd = true;
		}
 	 	
 		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tipoEntradaSearchPage.getTipoEntrada().getCodigo())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codigo)) like '%" + 
				StringUtil.escaparUpper(tipoEntradaSearchPage.getTipoEntrada().getCodigo()) + "%'";
			flagAnd = true;
		}
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TipoEntrada> listTipoEntrada = (ArrayList<TipoEntrada>) executeCountedSearch(queryString, tipoEntradaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoEntrada;
	}

	/**
	 * Obtiene un TipoEntrada por su codigo
	 */
	public TipoEntrada getByCodigo(String codigo) {
		
		try {
			TipoEntrada tipoEntrada;
			String queryString = "from TipoEntrada t where t.codigo = :codigo";
			Session session = SiatHibernateUtil.currentSession();
	
			Query query = session.createQuery(queryString).setString("codigo", codigo);
			tipoEntrada = (TipoEntrada) query.uniqueResult();	
	
			return tipoEntrada;
		} catch (Exception e){
			return null;
		}	
	}

	@SuppressWarnings("unchecked")
	public List<TipoEntrada> getListActivaOrdenada() {
		try {
			String queryString = "";
			queryString += "from TipoEntrada t where t.estado = :estado ";
			queryString += "order by t.descripcion ";
			Session session = SiatHibernateUtil.currentSession();
	
			Query query = session.createQuery(queryString)
								 .setInteger("estado", Estado.ACTIVO.getBussId());
			
			return (ArrayList<TipoEntrada>) query.list();
		
		} catch (Exception e) {
			log.error("No se pudo obtener la lista de Tipos de Entrada", e);
			return null;
		}	
	}

}