//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.pad.buss.bean.BroCue;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.BroCueSearchPage;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class BroCueDAO extends GenericDAO {
	
	private Logger log = Logger.getLogger(BroCueDAO.class);
	
	private static long migId = -1;
	
	public BroCueDAO(){
		super(BroCue.class);
	}
	
	public List<BroCue> getListBySearchPage(BroCueSearchPage broCueSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from BroCue t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del BroCueSearchPage: " + broCueSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (broCueSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro BroCue excluidos
 		List<BroCueVO> listBroCueExcluidos = (ArrayList<BroCueVO>) broCueSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listBroCueExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listBroCueExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if(!ModelUtil.isNullOrEmpty(broCueSearchPage.getBroCue().getBroche())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.broche.id = " +  broCueSearchPage.getBroCue().getBroche().getId();
			flagAnd = true;
		}
 		 		
 		// Order By
		//queryString += " order by t.fechaAlta";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<BroCue> listBroCue = (ArrayList<BroCue>) executeCountedSearch(queryString, broCueSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listBroCue;
	}

	
	public BroCue getVigenteByCuenta(Cuenta cuenta){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from BroCue t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idCuenta: " + cuenta.getId()); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.cuenta.id = " + cuenta.getId();
        
    	queryString += " and t.fechaBaja is null";
 						
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    BroCue broCue = (BroCue) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return broCue;
	}

	 /**
	  *  Devuelve el proximo valor de id a asignar. 
	  *  Se inicializa obteniendo el ultimo id asignado en el archivo de migracion con datos pasados como parametro
	  *  y luego en cada llamada incrementa el valor.
	  * 
	  * @return long - el proximo id a asignar.
	  * @throws Exception
	  */
	 public long getNextId(String path, String nameFile) throws Exception{
		 // Si migId==-1 buscar MaxId en el archivo de load. Si migId!=-1, migId++ y retornar migId;
		 if(migId==-1){
				 migId = this.getLastId(path, nameFile)+1;
		 }else{
			 migId++;
		 }
		 
		 return migId;
	 }


	 /**
	  *  Inserta una linea con los datos del registro BroCue para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param broCue, output - BroCue a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(BroCue o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|idbroche|idcuenta|fechaalta|usuario|fechaultmdf|estado 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getBroche().getId());		 
		 line.append("|");
		 line.append(o.getCuenta().getId());		 
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaAlta(), "yyyy-MM-dd"));
		 line.append("|");
		 line.append(DemodaUtil.currentUserContext().getUserName());
		 line.append("|");
		 line.append("2010-01-01 00:00:00");
		 line.append("|");
		 line.append("1");
		 line.append("|");
	      
		 output.addline(line.toString());
		 
		 // Seteamos el id generado en el bean.
		 o.setId(id);
	
		 return id;
	 }


}
