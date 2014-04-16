//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.rec.buss.bean.PlanillaCuadra;
import ar.gov.rosario.siat.rec.iface.model.ContratoVO;
import ar.gov.rosario.siat.rec.iface.model.EstPlaCuaVO;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraVO;
import ar.gov.rosario.siat.rec.iface.model.TipoObraVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PlanillaCuadraDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlanillaCuadraDAO.class);	
	
	public PlanillaCuadraDAO() {
		super(PlanillaCuadra.class);
	}
	
	public List<PlanillaCuadra> getBySearchPage(PlanillaCuadraSearchPage planillaCuadraSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from PlanillaCuadra planillaCuadra ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del PlanillaCuadraSearchPage: " + planillaCuadraSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (planillaCuadraSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " planillaCuadra.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui

		// filtro planillaCuadra excluidos
 		List<PlanillaCuadraVO> listPlanillaCuadraExcluidos = (ArrayList<PlanillaCuadraVO>) 
 			planillaCuadraSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listPlanillaCuadraExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listPlanillaCuadraExcluidos);
			queryString += " planillaCuadra.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
 		
 		// filtro por recurso
 		RecursoVO recurso = planillaCuadraSearchPage.getPlanillaCuadra().getRecurso();
 		if (!ModelUtil.isNullOrEmpty(recurso)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " planillaCuadra.recurso.id = " + recurso.getId(); 
			flagAnd = true;
		}
 		
 		//filtro por contrato
 		ContratoVO contrato = planillaCuadraSearchPage.getPlanillaCuadra().getContrato();
 		if (!ModelUtil.isNullOrEmpty(contrato)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " planillaCuadra.contrato.id = " + contrato.getId(); 
			flagAnd = true;
		}
 		
 		//filtro por Tipo de Obra
 		TipoObraVO tipoObra = planillaCuadraSearchPage.getPlanillaCuadra().getTipoObra();
 		if (!ModelUtil.isNullOrEmpty(tipoObra)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " planillaCuadra.tipoObra.id = " + tipoObra.getId(); 
			flagAnd = true;
		}

		// filtro por id
 		Long idPlanillaCuadra = planillaCuadraSearchPage.getPlanillaCuadra().getId(); 
 		if (idPlanillaCuadra != null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " planillaCuadra.id = " + idPlanillaCuadra;
			flagAnd = true;
		}

 		// filtro por estado
 		EstPlaCuaVO estPlaCua = planillaCuadraSearchPage.getPlanillaCuadra().getEstPlaCua();
 		if (!ModelUtil.isNullOrEmpty(estPlaCua)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " planillaCuadra.estPlaCua.id = " + estPlaCua.getId(); 
			flagAnd = true;
		}
 		
 		// filtro por codigo de calle
 		CalleVO callePpal = planillaCuadraSearchPage.getPlanillaCuadra().getCallePpal();
 		if (!ModelUtil.isNullOrEmpty(callePpal)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " planillaCuadra.codCallePpal = " + callePpal.getId(); 
			flagAnd = true;
		}
 		
 		// filtro por descripcion
 		String descripcion = planillaCuadraSearchPage.getPlanillaCuadra().getDescripcion();
 		if (!StringUtil.isNullOrEmpty(descripcion)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(planillaCuadra.descripcion)) LIKE '%" 
				+ StringUtil.escaparUpper(descripcion) + "%'"; 
			flagAnd = true;
		}
 		
 		// filtro por obra de la planilla cuadra
 		// usado para la busqueda para asignacion de repartidores
 		ObraVO obra = planillaCuadraSearchPage.getPlanillaCuadra().getObra();
 		if (!ModelUtil.isNullOrEmpty(obra)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " planillaCuadra.obra.id = " + obra.getId(); 
			flagAnd = true;
		}
 		
 		queryString += " order by planillaCuadra.id ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<PlanillaCuadra> listPlanillaCuadra = (ArrayList<PlanillaCuadra>) executeCountedSearch(queryString, planillaCuadraSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPlanillaCuadra;
	}
	
	/**
	 * Retorna la lista de planillas involucradas en las obras con 
	 * id en listIdObra, ordenada por numero de Repartidor, numero 
	 * de Obra y numero de Cuadra
	 * 
	 * @param listIdObra
	 * @return List<PlanillaCuadra>
	 * */
	public List<PlanillaCuadra> getListByListIdObra(Long[] listIdObra) {
		Session session = SiatHibernateUtil.currentSession();
    	
		if (listIdObra == null )
			return null;
		
		String queryString = "from PlanillaCuadra planillaCuadra";
    
    	queryString += " where planillaCuadra.obra.id in ( " + StringUtil.getStringComaSeparate(listIdObra) + ")";
    	
    	//Ordenamos por numero de Repartidor, numero de Obra y numero de Cuadra 
    	queryString +=" order by planillaCuadra.repartidor.nroRepartidor, " +
    							"planillaCuadra.obra.numeroObra, " +
    							"planillaCuadra.numeroCuadra";
    	
    	Query query = session.createQuery(queryString);
    	
    	return (ArrayList<PlanillaCuadra>) query.list();		
	}


	/**
	* Retorna el maximo de los numeros de cuadra de las planillas 
	* asignadas a una obra
	* 
	* @param idObra
	* @return Integer
	**/
	public Integer getMaxNumCua(Long idObra) {
		Session session = SiatHibernateUtil.currentSession();
    	
		if (idObra == null )
			return null;
		
		String queryString = "select max(planillaCuadra.numeroCuadra) from PlanillaCuadra planillaCuadra";
    
    	queryString += " where planillaCuadra.obra.id = " + idObra;
    	
       	Query query = session.createQuery(queryString);
    	
    	return (Integer) query.uniqueResult();		
	}
	
	public PlanillaCuadra getByIdCuenta (Long idCuenta)throws Exception{
		PlanillaCuadra planillaCuadra;
		
		String queryString = "SELECT pc FROM PlanillaCuadra pc, PlaCuaDet pcd WHERE pc.id = pcd.planillaCuadra.id";
		queryString += " AND pcd.cuentaCdM.id = "+idCuenta;
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		planillaCuadra = (PlanillaCuadra) query.uniqueResult();
		
		return planillaCuadra;
	}

}
