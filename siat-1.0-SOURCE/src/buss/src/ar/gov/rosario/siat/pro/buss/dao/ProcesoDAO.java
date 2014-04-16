//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.ProcesoSearchPage;
import coop.tecso.demoda.iface.helper.StringUtil;

public class ProcesoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProcesoDAO.class);	
	
	public ProcesoDAO() {
		super(Proceso.class);
	}

	public List<Proceso> getListBySearchPage(ProcesoSearchPage searchPage){		
		String queryString = "";

		// filtro la deuda para una determinada cuenta/
		queryString = "FROM Proceso p " ;
		boolean flagAnd = false;
		try{	
	
	    	//Filtro por codProceso
	    	String codProceso = searchPage.getProceso().getCodProceso();
			if(!StringUtil.isNullOrEmpty(codProceso) && !StringUtil.iguales(codProceso, StringUtil.SELECT_OPCION_SELECCIONAR)){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" p.codProceso='"+codProceso+"'";
	    		flagAnd = true;    		
	    	}
	    	
	    	// Filtro por descripcion del proceso
	    	String desProceso = searchPage.getProceso().getDesProceso();
	    	if(!StringUtil.isNullOrEmpty(desProceso)){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString += " UPPER(TRIM(p.desProceso)) like '%" + 
							StringUtil.escaparUpper(desProceso) + "%'";	    		
	    		flagAnd = true;    		
	    	}
	    	
	    	// Filtro por Locked
	    	if(searchPage.getProceso().getLocked().getId()!=null && 
	    										searchPage.getProceso().getLocked().getId().intValue()>=0){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" p.locked="+searchPage.getProceso().getLocked().getId();
	    		flagAnd = true;
	    	}
	    	
	    	log.debug("getListBySearchPage - query: "+queryString);
	    	List<Proceso> listProceso = (ArrayList<Proceso>) executeCountedSearch(queryString, searchPage);
	    	
	    	return listProceso; 
		}catch(Exception e){
			log.error(e);			
		}
		return null;
	}
	
	/**
	 * Obtiene un Proceso por su codigo
	 */
	public Proceso getByCodigo(String codigo) throws Exception {
		Proceso proceso;
		String queryString = "from Proceso t where t.codProceso = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		proceso = (Proceso) query.uniqueResult();	

		return proceso; 
	}
	
	/**
	 * Obtiene todos los Codigos de los diferentes procesos cargados
	 * @return
	 */
	public List<String> getListCodigos(){
		String queryString = "SELECT p.codProceso FROM Proceso p ORDER BY p.codProceso";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		return (ArrayList<String>)query.list();
	}
}
