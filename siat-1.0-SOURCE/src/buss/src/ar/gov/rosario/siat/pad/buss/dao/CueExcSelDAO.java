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
import ar.gov.rosario.siat.pad.buss.bean.CueExcSel;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelSearchPage;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CueExcSelDAO extends GenericDAO {
	
	private Logger log = Logger.getLogger(CueExcSelDAO.class);

	private static long migId = -1;
	
	public CueExcSelDAO(){
		super(CueExcSel.class);
	}
	
	public List<CueExcSel> getBySearchPage(CueExcSelSearchPage cueExcSelSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from CueExcSel t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del cueExcSelSearchPage: " + cueExcSelSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (cueExcSelSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro por Recurso
 		if(!ModelUtil.isNullOrEmpty(cueExcSelSearchPage.getCueExcSel().getCuenta().getRecurso())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.recurso = " +  cueExcSelSearchPage.getCueExcSel().getCuenta().getRecurso().getId();
			flagAnd = true;
		}
		
		// filtro por Cuenta
 		if(!StringUtil.isNullOrEmpty(cueExcSelSearchPage.getCueExcSel().getCuenta().getNumeroCuenta())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.numeroCuenta = '" + 
				StringUtil.formatNumeroCuenta(cueExcSelSearchPage.getCueExcSel().getCuenta().getNumeroCuenta()) + "'";
			flagAnd = true;
		}
		
		// filtro por Area
 		if(!ModelUtil.isNullOrEmpty(cueExcSelSearchPage.getCueExcSel().getArea())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.area = " +  cueExcSelSearchPage.getCueExcSel().getArea().getId();
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.id ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CueExcSel> listCueExcSel = (ArrayList<CueExcSel>) executeCountedSearch(queryString, cueExcSelSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCueExcSel;
	}
	
	/**
	 * Obtiene la lista de CueExcSel activas para la cuenta
	 * @param  cuenta
	 * @return List<CueExcSel>
	 */
	public List<CueExcSel> getListCueExcSelActivas(Cuenta cuenta){
		
		Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM CueExcSel ces " + 
						"WHERE ces.cuenta = :cuenta " +
						"AND ces.estado = :estActivo";
		
    	Query query = session.createQuery(sQuery)
    					.setEntity("cuenta", cuenta)
    					.setInteger("estActivo", Estado.ACTIVO.getId());
    	
    	return (ArrayList<CueExcSel>) query.list();
	}

	 /**
	  * Busca una Cuenta Excluida por Numero Cuenta, por Recurso y codigo de Area, 
	  * si no encuentra la conbinacion devuelve null.  
	  * 
	  * @param numeroCuenta
	  * @param idRecurso
	  * @param codArea
	  * @return 
	  * @throws Exception
	  */ 
	public CueExcSel getCueExcSelByCuentaYRecursoYArea(String numeroCuenta,Long idRecurso, String codArea) throws Exception{
		    CueExcSel cueExcSel;
			
		    String queryString = "from CueExcSel t where t.cuenta.numeroCuenta = '" + 
		    					StringUtil.formatNumeroCuenta(numeroCuenta) + "'" +
		    					 " and t.cuenta.recurso.id = " + idRecurso + 
		    					 " and t.area.codArea = '" + codArea + "'";
		    					 
		    Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString);
			try{
				cueExcSel = (CueExcSel) query.uniqueResult();	
			} catch (Exception e) {
				// Si no encuentra la cuenta, o encuentra mas de un numero de cuenta para el recurso
				return null;				
			} 

			return cueExcSel; 
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
	  *  Inserta una linea con los datos de la CueExcSel para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param cueExcSel, output - CueExcSel a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(CueExcSel o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|idcuenta|idarea|usuario|fechaultmdf|estado 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getCuenta().getId());		  
		 line.append("|");
		 line.append(o.getArea().getId());		 
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
