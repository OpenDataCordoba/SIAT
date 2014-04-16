//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.gde.buss.bean.CierreComercio;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.iface.model.CierreComercioSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CierreComercioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CierreComercioDAO.class);
	
	public CierreComercioDAO() {
		super(CierreComercio.class);
	}
	
	
	public CierreComercio getCierreComercioByObjImp(ObjImp objImp){
		String queryString = "FROM CierreComercio c WHERE c.objImp.id = " + objImp.getId();
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query= session.createQuery(queryString);
		
		return (CierreComercio) query.uniqueResult();
		
	}

/*
	public List<CierreComercio> getBySearchPage(CierreComercioSearchPage cierreComercioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from CierreComercio t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CierreComercioSearchPage: " + cierreComercioSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (cierreComercioSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/ * Ejemplos:
		
		// filtro cierreComercio excluidos
 		List<CierreComercioVO> listCierreComercioExcluidos = (List<CierreComercioVO>) cierreComercioSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCierreComercioExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCierreComercioExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(cierreComercioSearchPage.getCierreComercio().getCodCierreComercio())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codCierreComercio)) like '%" + 
				StringUtil.escaparUpper(cierreComercioSearchPage.getCierreComercio().getCodCierreComercio()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(cierreComercioSearchPage.getCierreComercio().getDesCierreComercio())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desCierreComercio)) like '%" + 
				StringUtil.escaparUpper(cierreComercioSearchPage.getCierreComercio().getDesCierreComercio()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codCierreComercio ";
		* /
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CierreComercio> listCierreComercio = (ArrayList<CierreComercio>) executeCountedSearch(queryString, cierreComercioSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCierreComercio;
	}
*/

	/**
	 * Obtiene un CierreComercio por su codigo
	 */
	public CierreComercio getByCodigo(String codigo) throws Exception {
		CierreComercio cierreComercio;
		String queryString = "from CierreComercio t where t.codCierreComercio = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		cierreComercio = (CierreComercio) query.uniqueResult();	

		return cierreComercio; 
	}

	/**
	 * Obtiene un CierreComercio por cuenta
	 */
	public CierreComercio getByObjImp(ObjImp objImp){
		CierreComercio cierreComercio;
		String queryString = "from CierreComercio t where t.objImp.id = "+objImp.getId();
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		cierreComercio = (CierreComercio) query.uniqueResult();	

		return cierreComercio; 
	}

	@SuppressWarnings("unchecked")
	public List<CierreComercio> getBySearchPage(CierreComercioSearchPage cierreComercioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from CierreComercio t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CierreComercioSearchPage: " + cierreComercioSearchPage.infoString()); 
		}
		
		// Armamos filtros del HQL
		if (cierreComercioSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro por objeto imponible
 		if (!ModelUtil.isNullOrEmpty(cierreComercioSearchPage.getCierreComercio().getCuentaVO().getObjImp())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += " t.objImp.id = " + cierreComercioSearchPage.getCierreComercio().getCuentaVO().getObjImp().getId();
			flagAnd = true;
		}

		if(cierreComercioSearchPage.getFechaCierreDefDesde()!=null){
			  queryString += flagAnd ? " and " : " where ";	  
			  queryString += " (t.fechaCierreDef >= TO_DATE('" + 
						cierreComercioSearchPage.getFechaCierreDefDesdeView() + "','%d/%m/%Y')) ";
		      flagAnd = true;
		}

		if(cierreComercioSearchPage.getFechaCierreDefHasta()!=null){
			  queryString += flagAnd ? " and " : " where ";	  
			  queryString += " (t.fechaCierreDef <= TO_DATE('" + 
						cierreComercioSearchPage.getFechaCierreDefHastaView() + "','%d/%m/%Y')) ";
		      flagAnd = true;
		}
 		
		if(cierreComercioSearchPage.getFechaCeseActividadDesde()!=null){
			  queryString += flagAnd ? " and " : " where ";	  
			  queryString += " (t.fechaCeseActividad >= TO_DATE('" + 
						cierreComercioSearchPage.getFechaCeseActividadDesdeView() + "','%d/%m/%Y')) ";
		      flagAnd = true;
		}

		if(cierreComercioSearchPage.getFechaCeseActividadHasta()!=null){
			  queryString += flagAnd ? " and " : " where ";	  
			  queryString += " (t.fechaCeseActividad <= TO_DATE('" + 
						cierreComercioSearchPage.getFechaCeseActividadHastaView() + "','%d/%m/%Y')) ";
		      flagAnd = true;
		}
 		
		// Expediente
 		CasoVO caso = cierreComercioSearchPage.getCierreComercio().getCaso();
		if(!StringUtil.isNullOrEmpty(caso.getIdFormateado())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.idCaso='"+caso.getIdFormateado()+"'"; 
			flagAnd = true;
		}
		
 		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    log.debug(funcName + ": Query: " + queryString);
	    
		List<CierreComercio> listCierreComercio = (ArrayList<CierreComercio>) executeCountedSearch(queryString, cierreComercioSearchPage);
		
		log.debug("EN GETY BY SEARCH PAGE DESPUES DE LA LISTA");
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCierreComercio;
	}

}
