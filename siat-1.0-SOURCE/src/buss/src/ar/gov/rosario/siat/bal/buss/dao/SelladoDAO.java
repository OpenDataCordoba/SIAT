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

import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.bal.iface.model.SelladoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SelladoVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class SelladoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(SelladoDAO.class);	
	
	public SelladoDAO() {
		super(Sellado.class);
	}
	
	public List<Sellado> getBySearchPage(SelladoSearchPage selladoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Sellado t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del SelladoSearchPage: " + selladoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (selladoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}			
		
		// filtro sellado excluidos
 		List<SelladoVO> listSelladoExcluidos = (ArrayList<SelladoVO>) selladoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listSelladoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listSelladoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(selladoSearchPage.getSellado().getCodSellado())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codSellado)) like '%" + 
				StringUtil.escaparUpper(selladoSearchPage.getSellado().getCodSellado()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(selladoSearchPage.getSellado().getDesSellado())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desSellado)) like '%" + 
				StringUtil.escaparUpper(selladoSearchPage.getSellado().getDesSellado()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codSellado ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Sellado> listSellado = (ArrayList<Sellado>) executeCountedSearch(queryString, selladoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listSellado;
	}

	/**
	 * Obtiene un Sellado por su codigo
	 */
	public Sellado getByCodigo(String codigo) throws Exception {
		Sellado sellado;
		String queryString = "from Sellado t where t.codSellado = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		sellado = (Sellado) query.uniqueResult();	

		return sellado; 
	}
	
	/**
	 * Retorna un Sellado que contenga una AccionSellado que cumpla con los parametros pasados
	 * @param idRecurso
	 * @param idAccion
	 * @param fecha
	 * @param cantPeriodos
	 * @return
	 */
	public Sellado getSellado(long idRecurso, long idAccion, Date fecha, int cantPeriodos){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "SELECT t.sellado FROM AccionSellado t ";
	    boolean flagAnd = false;
	
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;		
				
		// filtro por idRecurso
 		if (idRecurso>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id=" + idRecurso;
			flagAnd = true;
		}

		// filtro por idAccion
 		if (idAccion>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.accion.id=" +idAccion;
			flagAnd = true;
		}

		// filtro por cantPeriodos
 		if (idAccion>0) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " ( t.cantPeriodos is null OR";
			queryString += " t.cantPeriodos<=" +cantPeriodos+" )";
			flagAnd = true;
		}
 			
        queryString += flagAnd ? " and " : " where ";
		queryString += " t.sellado.estado =" + Estado.ACTIVO.getId();
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Session session = SiatHibernateUtil.currentSession();
		Sellado sellado = (Sellado) session.createQuery(queryString).uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return sellado;
	}
}
