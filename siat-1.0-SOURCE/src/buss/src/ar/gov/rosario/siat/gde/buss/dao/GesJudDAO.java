//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.GesJud;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.iface.model.GesJudSearchPage;
import ar.gov.rosario.siat.gde.iface.model.GesJudVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class GesJudDAO extends GenericDAO {

	private Log log = LogFactory.getLog(GesJudDAO.class);	
	
	private static long migId = -1;
	
	public GesJudDAO() {
		super(GesJud.class);
	}

	public List<GesJud> getBySearchPage(GesJudSearchPage searchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		String queryString = " From GesJud t ";
	    boolean flagAnd = false;
	
		// Armamos filtros del HQL
		if (searchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}else{
			// filtro estado
	 		if (searchPage.getGesJud().getEstadoGesJudVO().getIdEstadoGesJud()!=null && searchPage.getGesJud().getEstadoGesJudVO().getIdEstadoGesJud()>=0) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.estado =" + searchPage.getGesJud().getEstadoGesJudVO().getIdEstadoGesJud();
				flagAnd = true;
			} 		

		}
		
		// filtro excluidos
 		List<GesJudVO> listExcluidos = (ArrayList<GesJudVO>) searchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}

		// filtro por Procurador
 		if (searchPage.getGesJud().getProcurador().getId()!=null && searchPage.getGesJud().getProcurador().getId()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.procurador.id =" + searchPage.getGesJud().getProcurador().getId();
			flagAnd = true;
		}
 		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(searchPage.getGesJud().getDesGesJud())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desGesJud)) like '%" + 
				StringUtil.escaparUpper(searchPage.getGesJud().getDesGesJud()) + "%'";
			flagAnd = true;
		}

		// filtro por juzgado
 		if (!StringUtil.isNullOrEmpty(searchPage.getGesJud().getJuzgado())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.juzgado)) like '%" + 
				StringUtil.escaparUpper(searchPage.getGesJud().getJuzgado()) + "%'";
			flagAnd = true;
		}
 		
 		// filtro por Nro Exp. Judicial
 		if (searchPage.getGesJud().getNroExpediente() !=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroExpediente = " + searchPage.getGesJud().getNroExpediente();
			flagAnd = true;
		}
 		
 		// filtro por Anio Exp. Judicial
 		if (searchPage.getGesJud().getAnioExpediente() !=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.anioExpediente = " + searchPage.getGesJud().getAnioExpediente();
			flagAnd = true;
		}
 		
 		// filtro por Cuenta
 		if (!ModelUtil.isNullOrEmpty(searchPage.getCuenta()) || !StringUtil.isNullOrEmpty(searchPage.getCuenta().getNumeroCuenta()) ){
 			
 			log.debug(funcName + " idCuenta: " + searchPage.getCuenta().getId());
 			
 			ArrayList<Long> listIdDeuda = (ArrayList<Long>) Deuda.getListAllIdByCuentaVO(searchPage.getCuenta());
 			
 			if (listIdDeuda.size() > 0){
 				queryString += flagAnd ? " and " : " where ";
 				queryString += " t.id IN ( SELECT DISTINCT gesJudDeu.gesJud.id FROM GesJudDeu gesJudDeu " +
 						" WHERE gesJudDeu.idDeuda IN ( " + ListUtil.getStringList(listIdDeuda) + "))";
 				
 			} 			
 		}

 		log.debug(funcName + " queryString:" + queryString);	
 		
	    List<GesJud> listResult = (ArrayList<GesJud>) executeCountedSearch(queryString, searchPage);
		
		return listResult;
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
		 if(getMigId()==-1){
				 setMigId(this.getLastId(path, nameFile)+1);
		 }else{
			 setMigId(getMigId() + 1);
		 }
		 
		 return getMigId();
	 }


	 /**
	  *  Inserta una linea con los datos de la GesJud para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param gesJud, output - Gestion Judicial a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(GesJud o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|desgesjud|idprocurador|fechacaducidad|juzgado|observacion|fechaalta|nroexpediente|anioexpediente|usrcreador|usuario|fechaultmdf|estado 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 if(o.getDesGesJud()!=null)
			 line.append(o.getDesGesJud());		 
		 line.append("|");
		 line.append(o.getProcurador().getId());		 
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaCaducidad(), "yyyy-MM-dd"));
		 line.append("|");
		 line.append(o.getJuzgado());		 
		 line.append("|");
		 if(o.getObservacion()!=null)
			 line.append(o.getObservacion());		 
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaAlta(), "yyyy-MM-dd"));
		 line.append("|");
		 line.append(o.getNroExpediente());		 
		 line.append("|");
		 line.append(o.getAnioExpediente());		 
		 line.append("|");
		 line.append(o.getUsrCreador());		 
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

	
	 public GesJud getByDes(String descripcion) {
			
			String queryString = " From GesJud t WHERE UPPER(TRIM(t.desGesJud))='"+
			StringUtil.escaparUpper(descripcion)+"'";
			
			Session session = SiatHibernateUtil.currentSession();
			return (GesJud) session.createQuery(queryString).list().get(0);		
	}

	
	 public List<Object[]> getList(Procurador procurador, Cuenta cuenta, String codTipoJuzgado, Date fechaAltaDesde, Date fechaAltaHasta) {		
		
		String queryString = "SELECT gesJud.id, count(gesJudDeu.id), sum(deudaJudicial.saldo)" +
				" from gde_gesJud gesJud left join gde_gesJudDeu gesJudDeu on gesJudDeu.idGesJud=gesJud.id " +
				" left join gde_deudajudicial deudaJudicial on gesJudDeu.iddeuda=deudaJudicial.id ";
	    boolean flagAnd = false;

	    // filtro por procurador
	    if(procurador!=null){
	    	queryString += flagAnd ? " and " : " where ";
			//queryString += " gesJud.procurador = :procurador";
	    	queryString += " gesJud.idProcurador = "+procurador.getId();
			flagAnd = true;
	    }
	    
	    // filtro por juzgado
 		if (!StringUtil.isNullOrEmpty(codTipoJuzgado)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " gesJud.codtipjuz = '"+
				StringUtil.escaparUpper(codTipoJuzgado) + "'";
			flagAnd = true;
		}

 		// filtro por fechaAltaDesde
 		if(fechaAltaDesde!=null){
 			queryString += flagAnd ? " and " : " where ";
 			queryString +=" gesJud.fechaAlta >= :fechaAltaDesde";
 			flagAnd = true;
 		}
 		
 		// filtro por fechaAltaDesde
 		if(fechaAltaHasta!=null){
 			queryString += flagAnd ? " and " : " where ";
 			queryString +=" gesJud.fechaAlta <= :fechaAltaHasta";
 			flagAnd = true;
 		}
 		
 		// filtro por cuenta
 		if(cuenta!=null){
 			queryString += flagAnd ? " and " : " where ";
 			//queryString +=" deudaJudicial.cuenta= :cuenta";
 			queryString +=" deudaJudicial.idCuenta= "+cuenta.getId();
 			flagAnd = true;
 		}
 		
 		queryString += " group by gesJud.id";
 		//queryString += " ORDER BY gesJud.fechaAlta ";
 		
 		Session session = SiatHibernateUtil.currentSession();
 		Query query = session.createSQLQuery(queryString);
 		//if(procurador!=null) query.setEntity("procurador", procurador);
 		//if(cuenta!=null) query.setEntity("cuenta", cuenta);
 		if(fechaAltaDesde!=null) query.setDate("fechaAltaDesde", fechaAltaDesde);
 		if(fechaAltaHasta!=null) query.setDate("fechaAltaHasta", fechaAltaHasta);
 		 		
 		return query.list();
	}

	public static void setMigId(long migId) {
		GesJudDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

}
