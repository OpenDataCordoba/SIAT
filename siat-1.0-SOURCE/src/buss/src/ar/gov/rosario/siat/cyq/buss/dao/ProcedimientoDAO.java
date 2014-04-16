//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlContrSearchPage;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ProcedimientoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProcedimientoDAO.class);	
	
	private static long migId = -1;
	
	public ProcedimientoDAO() {
		super(Procedimiento.class);
	}
	
	/**
	 * Busqueda para usuarios de Cyq.
	 * 
	 * 
	 * @param procedimientoSearchPage
	 * @return
	 * @throws Exception
	 */
	public List<Procedimiento> getBySearchPageAva(ProcedimientoSearchPage procedimientoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		
		String strFrom = "from Procedimiento t ";
		String queryString = "";
		
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ProcedimientoSearchPage: " + procedimientoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (procedimientoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro procedimiento excluidos
 		List<ProcedimientoVO> listProcedimientoExcluidos = (ArrayList<ProcedimientoVO>) procedimientoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listProcedimientoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listProcedimientoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
 		// Fecha Alta
 		Date fecha = procedimientoSearchPage.getProcedimiento().getFechaAlta();
 		if (fecha != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaAlta = TO_DATE('" + DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";

			flagAnd = true;
		}

 		// Fecha Verificacion
 		if (!StringUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getFechaVerOpoView())) {
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaVerOpo = TO_DATE('" + DateUtil.formatDate(
					procedimientoSearchPage.getProcedimiento().getFechaVerOpo(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		
		// filtro por numero
 		Integer numero = procedimientoSearchPage.getProcedimiento().getNumero();
 		if (numero!=null && numero.intValue()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.numero =" + numero;
			flagAnd = true;
		}

		// filtro por anio
 		Integer anio = procedimientoSearchPage.getProcedimiento().getAnio();
 		if (anio!=null && anio.intValue()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.anio =" + anio;
			flagAnd = true;
		}

 		//filtro por sindico
 		if (!StringUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getPerOpoDeu())) {
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " UPPER(TRIM(t.perOpoDeu)) like '%" + 
 				StringUtil.escaparUpper(procedimientoSearchPage.getProcedimiento().getPerOpoDeu()) + "%'";
 			flagAnd = true;
		}
 		
 		//filtro por numero expediente
 		if (!StringUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getNumExpView())) {
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.numExp = " + procedimientoSearchPage.getProcedimiento().getNumExp();
 			flagAnd = true;
		}
 		
 		//tipoProceso
 		if(!ModelUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getTipoProceso())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoProceso.id =" + procedimientoSearchPage.getProcedimiento().getTipoProceso().getId();
			flagAnd = true;
 		}
 		
 		// juzgado
 		if(!ModelUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getJuzgado())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.juzgado.id =" + procedimientoSearchPage.getProcedimiento().getJuzgado().getId();
			flagAnd = true;
 		}
 		
 		// abogado
 		if(!ModelUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getAbogado())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.abogado.id =" + procedimientoSearchPage.getProcedimiento().getAbogado().getId();
			flagAnd = true;
 		}

 		// filtro por DesContribuyente
 		if (!StringUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getDesContribuyente())) {
 			
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desContribuyente)) like '%" + 
				StringUtil.escaparUpper(procedimientoSearchPage.getProcedimiento().getDesContribuyente()) + "%'";
			flagAnd = true;
		
		}
 		
 		// contribuyente
 		if(!ModelUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getContribuyente())){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.idContribuyente = " + procedimientoSearchPage.getProcedimiento().getContribuyente().getId();
			flagAnd = true;
 		}

 		// estadoProcedo
 		if(!ModelUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getEstadoProced())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoProced.id =" + procedimientoSearchPage.getProcedimiento().getEstadoProced().getId();
			flagAnd = true;
 		}
 		
 		// hisEstPro
 		// Estado en Historico
 		if (!ModelUtil.isNullOrEmpty(procedimientoSearchPage.getProcedimiento().getHisEstProced().getEstadoProced())) {
            
 			log.debug(funcName + " EstadoEnHistorico -> " + procedimientoSearchPage.getEstadoEnHistorico() +
 					" Estado en Historico: " + procedimientoSearchPage.getProcedimiento().getHisEstProced().getEstadoProced().getId());
 			
 			strFrom = "SELECT t FROM Procedimiento t " +
 							  "JOIN t.listHisEstProced as hisEstProced ";
 			
 			queryString += flagAnd ? " and " : " where ";
 			
 			if (procedimientoSearchPage.getEstadoEnHistorico())
 				queryString += " hisEstProced.estadoProced.id = " + procedimientoSearchPage.getProcedimiento().getHisEstProced().getEstadoProced().getId();
 			else
 				queryString += " hisEstProced.estadoProced.id NOT IN (" + procedimientoSearchPage.getProcedimiento().getHisEstProced().getEstadoProced().getId() + ")";
 			
			flagAnd = true;
 		}
 		
 		queryString = strFrom + queryString;
 		
 		// Order By
		queryString += " order by t.anio desc, t.numero desc";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Procedimiento> listProcedimiento = (ArrayList<Procedimiento>) executeCountedSearch(queryString, procedimientoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listProcedimiento;
	}

	/**
	 * Busqueda para usuarios de cobranzas
	 * 
	 * 
	 * @param procedimientoSearchPage
	 * @return
	 * @throws Exception
	 */
	public List<Procedimiento> getBySearchPage(ProcedimientoSearchPage procedimientoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		
		String strFrom = "from Procedimiento t ";
		String queryString = "";
		
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ProcedimientoSearchPage: " + procedimientoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (procedimientoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro procedimiento excluidos
 		List<ProcedimientoVO> listProcedimientoExcluidos = (ArrayList<ProcedimientoVO>) procedimientoSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listProcedimientoExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listProcedimientoExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por numero
 		Integer numero = procedimientoSearchPage.getProcedimiento().getNumero();
 		if (numero!=null && numero.intValue()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.numero =" + numero;
			flagAnd = true;
		}

		// filtro por anio
 		Integer anio = procedimientoSearchPage.getProcedimiento().getAnio();
 		if (anio!=null && anio.intValue()>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.anio =" + anio;
			flagAnd = true;
		}

 		// filtro por Fecha Balance Desde
		if (procedimientoSearchPage.getFechaVerOpoDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaVerOpo >= TO_DATE('" + 
					DateUtil.formatDate(procedimientoSearchPage.getFechaVerOpoDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Balance Hasta
		if (procedimientoSearchPage.getFechaVerOpoHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaVerOpo <= TO_DATE('" + 
					DateUtil.formatDate(procedimientoSearchPage.getFechaVerOpoHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
 		 
		if (procedimientoSearchPage.getPoseeOrdenControl().getEsSI()){
			queryString += flagAnd ? " and " : " where ";	
			queryString += " size(t.listOrdenControl) > 0 ";
		}
		
 		queryString = strFrom + queryString;
 		
 		// Order By
		queryString += " order by t.fechaVerOpo desc";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Procedimiento> listProcedimiento = (ArrayList<Procedimiento>) executeCountedSearch(queryString, procedimientoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listProcedimiento;
	}
	
	/**
	 * Obtiene un Procedimiento por su numero y anio
	 */
	public Procedimiento getByNumeroyAnio(Integer numero, Integer anio) throws Exception {
		Procedimiento procedimiento;
		String queryString = "from Procedimiento t where t.numero = :numero and t.anio = :anio";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							  .setInteger("numero", numero)
							  .setInteger("anio", anio);
		procedimiento = (Procedimiento) query.uniqueResult();	

		return procedimiento; 
	}
	
	public static void setMigId(long migId) {
		ProcedimientoDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}
	
	/**
	 *  Devuelve el proximo valor de id a asignar. 
	 *  Para se inicializa obteniendo el ultimo id asignado el archivo de migracion con datos pasados como parametro
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
	 *  Inserta una linea con los datos del Procedimiento para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param convenio, output - El Convenio a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(Procedimiento o, LogFile output) throws Exception {

		// Obtenemos el valor del id autogenerado a insertar.
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|numero|anio|idestadoproced|fechaalta|fechaboletin|idtipoproceso|idjuzgado|caratula|numexp|anioexp|fechaveropo|fechaaltaver|peropodeu|lugaroposicion|telefonooposicion|observacion|fechahomo|auto|fechaauto|idprocyq|fechabaja|idmotivobaja|idcontribuyente|descontribuyente|domicilio|usuario|fechaultmdf|estado
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getNumero());
		line.append("|");
		line.append(o.getAnio());		 
		line.append("|");
		line.append(o.getEstadoProced().getId());
		line.append("|");
		line.append(DateUtil.formatDate(o.getFechaAlta(), "yyyy-MM-dd"));		 
		line.append("|");
		if(o.getFechaBoletin()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaBoletin(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.		 		 
		line.append("|");
		line.append(o.getTipoProceso().getId());
		line.append("|");
		if(o.getJuzgado()!=null){
			line.append(o.getJuzgado().getId());
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getCaratula() != null)
			line.append(o.getCaratula());		 
		line.append("|");
		if(o.getNumExp() != null)
			line.append(o.getNumExp());		 
		line.append("|");
		if(o.getAnioExp() != null)
			line.append(o.getAnioExp());		 
		line.append("|");
		if(o.getFechaVerOpo()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaVerOpo(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getFechaAltaVer()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaAltaVer(), "yyyy-MM-dd HH:mm:SS"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getPerOpoDeu() != null)
			line.append(o.getPerOpoDeu());		 
		line.append("|");
		if(o.getLugarOposicion() != null)
			line.append(o.getLugarOposicion());		 
		line.append("|");
		if(o.getTelefonoOposicion() != null)
			line.append(o.getTelefonoOposicion());		 
		line.append("|");
		if (o.getObservacion() != null)
			line.append(o.getObservacion());
		line.append("|");
		if(o.getFechaHomo()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaHomo(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if (o.getAuto() != null)
			line.append(o.getAuto());
		line.append("|");
		if(o.getFechaAuto()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaAuto(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getProcedAnt()!=null){
			line.append(o.getProcedAnt().getId());
		} // Si es null no se inserta nada, viene el proximo pipe.	 
		line.append("|");
		if(o.getFechaBaja()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaBaja(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe. 
		line.append("|");
		if (o.getMotivoBaja() != null)
			line.append(o.getMotivoBaja().getId());
		line.append("|");
		if (o.getContribuyente() != null)
			line.append(o.getContribuyente().getId());
		line.append("|");
		if (o.getDesContribuyente() != null)
			line.append(o.getDesContribuyente());
		line.append("|");		
		if(o.getDomicilio() != null)
			line.append(o.getDomicilio());
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

	
	/**
	 * Devuelve el proximo numero de Procedimiento a generar para un alta.
	 * Devuele el maximo numero para el anio actual ya que por 
	 * migracion pueden existir numeros repetido para distinos anios.
	 * 
	 * Si para un anio no encuentra resultado, obtiene el maximo del anio anterior.  
	 * 
	 * @return
	 * @throws Exception
	 */
	public Integer getNextNumero() throws Exception {
			
		int anio =  DateUtil.getAnio(new Date());
		
		Integer nextNumero = null;
		
		while (nextNumero == null){
			String queryString = "SELECT MAX(t.numero) + 1 FROM Procedimiento t WHERE t.anio = " + anio;
	
			Session session = SiatHibernateUtil.currentSession();
			
			Query query = session.createQuery(queryString);
			query.setMaxResults(1);
		
			nextNumero = (Integer) query.uniqueResult();
			
			anio = anio - 1;
		}	
		
		return nextNumero;	
	}

	
	/**
	 * Dado el id de procedimiento recibido, checkea contra todas las tablas de deuda, si existe algun registro relacionado.
	 * 	 
	 * @param idProcedimientoCyQ
	 * @return
	 */
	public boolean hasReferenceDeuda(Long idProcedimientoCyQ){
		
		String queryFrom = "SELECT count(t.id) FROM "; 
		String queryWhere = " t WHERE t.idProcedimientoCyQ = " + idProcedimientoCyQ;			
		
		// Deuda Administrativa
		String queryString = queryFrom + "gde_deudaadmin" + queryWhere;
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString);
				
		log.debug("hasReferenceDeuda " + queryString);
		BigDecimal count = (BigDecimal) query.uniqueResult();
		 
		if (count.intValue() > 0){
			return true;
		} 
		
		// Deuda judicial
		queryString = queryFrom + "gde_deudajudicial" + queryWhere;
		query = session.createSQLQuery(queryString);
				
		log.debug("hasReferenceDeuda " + queryString);
		count = (BigDecimal) query.uniqueResult();
		 
		if (count.intValue() > 0){
			return true;
		} 
		
		// Deuda anulada
		queryString = queryFrom + "gde_deudaanulada" + queryWhere;
		query = session.createSQLQuery(queryString);
				
		log.debug("hasReferenceDeuda " + queryString);
		count = (BigDecimal) query.uniqueResult();
		 
		if (count.intValue() > 0){
			return true;
		}
		
		// Deuda cancelada
		queryString = queryFrom + "gde_deudacancelada" + queryWhere;
		query = session.createSQLQuery(queryString);
		
		log.debug("hasReferenceDeuda " + queryString);				
		count = (BigDecimal) query.uniqueResult();
		 
		if (count.intValue() > 0){
			return true;
		} 
		 
		return false;
	}
	
	/**
	 * Obtiene una lista de Procedimientos por search page de orden de control
	 */
	public List<Procedimiento> getByOrdenControlSearchPage(OrdenControlContrSearchPage ordenControlSearchPage) throws Exception {
		List<Procedimiento> listProcedimiento;
		boolean flagAnd=false;
		String queryString = "from Procedimiento t";
		
		if (ordenControlSearchPage.getOrdenControl().getProcedimiento().getNumero()!=null){
			queryString += (flagAnd==true)?" AND ": " WHERE ";
			queryString += " t.numero = "+ ordenControlSearchPage.getOrdenControl().getProcedimiento().getNumero();
			flagAnd=true;
		}
		
		if (ordenControlSearchPage.getOrdenControl().getProcedimiento().getAnio()!=null){
			queryString += (flagAnd==true)?" AND ": " WHERE ";
			queryString += " t.anio = "+ ordenControlSearchPage.getOrdenControl().getProcedimiento().getAnio();
			flagAnd=true;
		}
		
		if (!ModelUtil.isNullOrEmpty(ordenControlSearchPage.getOrdenControl().getProcedimiento().getTipoProceso())){
			queryString += (flagAnd==true)?" AND ": " WHERE ";
			queryString += " t.tipoProceso.id = "+ ordenControlSearchPage.getOrdenControl().getProcedimiento().getTipoProceso().getId();
			flagAnd=true;
		}
		

		
		return executeCountedSearch(queryString, ordenControlSearchPage); 
	}
	
}
