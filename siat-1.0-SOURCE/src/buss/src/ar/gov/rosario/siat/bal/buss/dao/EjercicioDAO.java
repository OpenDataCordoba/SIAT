//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Ejercicio;
import ar.gov.rosario.siat.bal.iface.model.EjercicioSearchPage;
import ar.gov.rosario.siat.bal.iface.model.EjercicioVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class EjercicioDAO extends GenericDAO {

	public EjercicioDAO(){
		super(Ejercicio.class);
	}

	public List<Ejercicio> getListBySearchPage(
			EjercicioSearchPage ejercicioSearchPage) throws Exception {
		
		String queryString = "from Ejercicio t ";
	    boolean flagAnd = false;

	
		// Armamos filtros del HQL
		if (ejercicioSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		//	filtro Ejercicio excluidos
 		List<EjercicioVO> listCueExeExcluidos = (List<EjercicioVO>) ejercicioSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listCueExeExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCueExeExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por desEjercicio
 		if (!StringUtil.isNullOrEmpty(ejercicioSearchPage.getEjercicio().getDesEjercicio())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desEjercicio)) like '%" + 
			StringUtil.escaparUpper(ejercicioSearchPage.getEjercicio().getDesEjercicio()) + "%'";
			flagAnd = true;
		}
 		 		
 		// filtro por fecha inicio
 		Date fechaInicio = ejercicioSearchPage.getEjercicio().getFecIniEje();
 		if (fechaInicio != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fecIniEje >= TO_DATE('" + 
				DateUtil.formatDate(fechaInicio, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
		
 		// fecha fin eje
 		Date fechaFineje = ejercicioSearchPage.getEjercicio().getFecFinEje();
 		if (fechaFineje != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fecFinEje <= TO_DATE('" + 
				DateUtil.formatDate(fechaFineje, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}
 		

		List<Ejercicio> listEjercicio = (ArrayList<Ejercicio>) 
			executeCountedSearch(queryString, ejercicioSearchPage);
		
		return listEjercicio;
	}
	
	/**
	 * Obtiene un Ejercicio por la fecha de Balance
	 */
	public Ejercicio getByFechaBalance(Date fechaBalance) throws Exception {
		Ejercicio ejercicio;
		String dateToCompare = DateUtil.formatDate(fechaBalance, DateUtil.ddSMMSYYYY_MASK);
		String queryString = "from Ejercicio t where t.fecIniEje <= TO_DATE('"+dateToCompare+"','%d/%m/%Y') and t.fecFinEje >= TO_DATE('"+dateToCompare+"','%d/%m/%Y')";
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString); 
		ejercicio = (Ejercicio) query.uniqueResult();	
		  
		return ejercicio; 
	}


}
