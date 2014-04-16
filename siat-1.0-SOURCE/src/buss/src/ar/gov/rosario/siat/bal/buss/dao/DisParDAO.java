//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.DisPar;
import ar.gov.rosario.siat.bal.iface.model.DisParSearchPage;
import ar.gov.rosario.siat.bal.iface.model.DisParVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DisParDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DisParDAO.class);	
	
	public DisParDAO(){
		super(DisPar.class);
	}
	
	public List<DisPar> getListBySearchPage(DisParSearchPage disParSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DisPar t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DisParSearchPage: " + disParSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (disParSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro DisPar excluidos
 		List<DisParVO> listDisParExcluidos = (ArrayList<DisParVO>) disParSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDisParExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDisParExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if(!ModelUtil.isNullOrEmpty(disParSearchPage.getDisPar().getRecurso())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso = " +  disParSearchPage.getDisPar().getRecurso().getId();
			flagAnd = true;
		}
 		
 		// filtro por Descripcion de DisPar
 		if(disParSearchPage.getDisPar().getDesDisPar()!=null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desDisPar)) like '%" + 
			StringUtil.escaparUpper(disParSearchPage.getDisPar().getDesDisPar()) + "%'";
			flagAnd = true;
		}
 		 		
 		// Order By
		queryString += " order by t.desDisPar";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DisPar> listDisPar = (ArrayList<DisPar>) executeCountedSearch(queryString, disParSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisPar;
	}

	/**
	 * Obtiene el Distribuidor de Partida que corresponde para el Tipo de Importe pasado. 
	 * (solo tipos de importe que no abren por concepto)
	 *  
	 * @param idTipoImporte
	 * @return
	 * @throws Exception
	 */
	public DisPar getByCodRecursoYidTipoImporte(String codRecurso, Long idTipoImporte) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DisPar t ";
	    
		// Armamos filtros del HQL 		
        queryString += " where t.tipoImporte = "+ idTipoImporte;
        queryString += " and t.recurso.codRecurso = '"+ codRecurso+"'";
        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    DisPar disPar = (DisPar) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return disPar; 
	}
	
	/**
	 * Obtiene la lista de Distribuidores de Partida Genericos por Recurso. 
	 * (solo se pueden cargar genericos para tipo de importe indeterminado)
	 *  
	 * @return
	 * @throws Exception
	 */
	public List<DisPar> getListActivaGenericos() throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from DisPar t ";
	    
		// Armamos filtros del HQL 		
        queryString += " where t.tipoImporte IS NOT NULL ";
                
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<DisPar> listDisPar = (ArrayList<DisPar>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDisPar; 
	}
}
