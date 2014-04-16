//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.exe.buss.bean.EstadoCueExe;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class EstadoCueExeDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EstadoCueExeDAO.class);
	
	public EstadoCueExeDAO() {
		super(EstadoCueExe.class);
	}

	public List<EstadoCueExe> getList() {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM EstadoCueExe estadoCueExe ORDER BY estadoCueExe.desEstadoCueExe";

		List<EstadoCueExe> listEstadoCueExe = session.createQuery(query).list();
		
		return listEstadoCueExe;
	}
	
	
	public List<EstadoCueExe> getListEstados() throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM EstadoCueExe estadoCueExe WHERE estadoCueExe.tipo = 'E' ORDER BY estadoCueExe.desEstadoCueExe";

		List<EstadoCueExe> listEstadoCueExe = session.createQuery(query).list();
		
		return listEstadoCueExe;
	}
	
	
	public List<EstadoCueExe> getListEstadosIniciales() throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM EstadoCueExe estadoCueExe WHERE estadoCueExe.tipo = 'E' AND estadoCueExe.esInicial = 1 ORDER BY estadoCueExe.desEstadoCueExe";

		List<EstadoCueExe> listEstadoCueExe = session.createQuery(query).list();
		
		return listEstadoCueExe;
	}
	
	public List<EstadoCueExe> getListAcciones() throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM EstadoCueExe estadoCueExe WHERE estadoCueExe.tipo = 'A' ORDER BY estadoCueExe.desEstadoCueExe";

		List<EstadoCueExe> listEstadoCueExe = session.createQuery(query).list();
		
		return listEstadoCueExe;
	}	
	
	public List<EstadoCueExe> getListSolicitudes() throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM EstadoCueExe estadoCueExe WHERE estadoCueExe.tipo = 'S' ORDER BY estadoCueExe.desEstadoCueExe";

		List<EstadoCueExe> listEstadoCueExe = session.createQuery(query).list();
		
		return listEstadoCueExe;
	}
	
	
	
	public List<EstadoCueExe> getListTransicionesForEstado(EstadoCueExe estadoCueExe) throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String transiciones = estadoCueExe.getTransiciones();
		
		if(StringUtil.isNullOrEmpty(transiciones))
			return new ArrayList<EstadoCueExe>();
		
		String query = "";
				
		if (transiciones.equals("*")){
			query = "WHERE (estadoCueExe.tipo = 'E' OR estadoCueExe.tipo = 'A') " +
					"AND estadoCueExe.id > 1 AND estadoCueExe.id <> " + estadoCueExe.getId();
		} else {
			query = "WHERE (estadoCueExe.tipo = 'E' OR estadoCueExe.tipo = 'A') " +
					"AND estadoCueExe.id IN (" + transiciones + ")";
		}
		
		query= "FROM EstadoCueExe estadoCueExe " + query + " ORDER BY estadoCueExe.desEstadoCueExe";
		
		List<EstadoCueExe> listEstadoCueExe = session.createQuery(query).list();
		
		return listEstadoCueExe;
	}
	
	public List<EstadoCueExe> getListResoluciones() throws Exception {

       Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM EstadoCueExe estadoCueExe WHERE estadoCueExe.esResolucion = 1 ORDER BY estadoCueExe.desEstadoCueExe";

		List<EstadoCueExe> listEstadoCueExe = session.createQuery(query).list();
		
		return listEstadoCueExe;
	}

	public List<EstadoCueExe> getBySearchPage(EstadoCueExeSearchPage estadoCueExeSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from EstadoCueExe t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del EstadoCueExeSearchPage: " + estadoCueExeSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (estadoCueExeSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro estadoCueExe excluidos
 		List<EstadoCueExeVO> listEstadoCueExeExcluidos = (List<EstadoCueExeVO>) estadoCueExeSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listEstadoCueExeExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listEstadoCueExeExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
 		
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(estadoCueExeSearchPage.getEstadoCueExe().getDesEstadoCueExe())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desEstadoCueExe)) like '%" + 
				StringUtil.escaparUpper(estadoCueExeSearchPage.getEstadoCueExe().getDesEstadoCueExe()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por modulo
 		if (estadoCueExeSearchPage.getTipoEstadoCueExe().getId()!=0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipo = '" + 
			 estadoCueExeSearchPage.getTipoEstadoCueExe().getCod() + "'";
			flagAnd = true;
		}
          
 		
 	 	// Order By
		queryString += " order by t.desEstadoCueExe ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<EstadoCueExe> listEstadoCueExe = (ArrayList<EstadoCueExe>) executeCountedSearch(queryString, estadoCueExeSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEstadoCueExe;
	}

}
