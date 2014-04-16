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

import ar.gov.rosario.siat.bal.buss.bean.Archivo;
import ar.gov.rosario.siat.bal.iface.model.ArchivoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ArchivoVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ArchivoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ArchivoDAO.class);	
	
	public ArchivoDAO(){
		super(Archivo.class);
	}
	
	public List<Archivo> getListBySearchPage(ArchivoSearchPage archivoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Archivo t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ArchivoSearchPage: " + archivoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (archivoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro Archivo excluidos
 		List<ArchivoVO> listArchivoExcluidos = (ArrayList<ArchivoVO>) archivoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listArchivoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listArchivoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por NroBanco
 		if(archivoSearchPage.getArchivo().getNroBanco() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroBanco = " +  archivoSearchPage.getArchivo().getNroBanco();
			flagAnd = true;
		}

		// filtro por Prefijo
 		if(!StringUtil.isNullOrEmpty(archivoSearchPage.getArchivo().getPrefix())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.prefix)) = '" +  archivoSearchPage.getArchivo().getPrefix().toUpperCase()+"'";
			flagAnd = true;
		}

 		// filtro por EstadoArc
 		if(!ModelUtil.isNullOrEmpty(archivoSearchPage.getArchivo().getEstadoArc())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoArc.id = " +  archivoSearchPage.getArchivo().getEstadoArc().getId();
			flagAnd = true;
		}

 		//  filtro por TipoArc
 		if(!ModelUtil.isNullOrEmpty(archivoSearchPage.getArchivo().getTipoArc())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoArc.id = " +  archivoSearchPage.getArchivo().getTipoArc().getId();
			flagAnd = true;
		}
 		
 		// filtro por inclusion en Balance
 		if(archivoSearchPage.getParamExBalance()){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.balance is null ";
			flagAnd = true;
		}
 		
 		// 	 filtro por Fecha Banco Desde
		if (archivoSearchPage.getFechaBancoDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaBanco >= TO_DATE('" + 
					DateUtil.formatDate(archivoSearchPage.getFechaBancoDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Banco Hasta
		if (archivoSearchPage.getFechaBancoHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaBanco <= TO_DATE('" + 
					DateUtil.formatDate(archivoSearchPage.getFechaBancoHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

		// filtro por estar en Balance
 		if(!ModelUtil.isNullOrEmpty(archivoSearchPage.getBalance())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.balance is null ";
			flagAnd = true;
		}
 		// Order By
		queryString += " order by t.fechaBanco DESC, t.nroBanco";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Archivo> listArchivo = (ArrayList<Archivo>) executeCountedSearch(queryString, archivoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listArchivo;
	}

	/**
	 *  Devuelve total de Caja 7 Auxiliar para los registros Activos (no incluidos en Balance)
	 *  
	 * @return double
	 */
	public Double getTotalForControl(Date fechaDesde, Date fechaHasta, Long idTipoArc, Long idEstadoArc, List<String> listPrefijosAExcluir){
		Session session = SiatHibernateUtil.currentSession();

		String queryString = "select sum(t.total) from Archivo t";
		queryString += " where t.estadoArc.id = "+idEstadoArc;
		if(idTipoArc > 0)
			queryString += " and t.tipoArc.id = "+idTipoArc;  
	    queryString += "and t.fechaBanco >= TO_DATE('" + DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
		queryString += "and t.fechaBanco <= TO_DATE('" +DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
		if(!ListUtil.isNullOrEmpty(listPrefijosAExcluir)){
			for(String prefijo: listPrefijosAExcluir)
				queryString += "and UPPER(TRIM(t.prefix)) <> '" +prefijo.toUpperCase()+"'";
		}
	    	    
	    Query query = session.createQuery(queryString);
	
		Double total = (Double) query.uniqueResult();
	    Double result = 0D;
		if(total != null)
			result = total;
	    
		return result;
	}
	
}
