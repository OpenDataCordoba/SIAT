//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.AuxConvenioReport;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import ar.gov.rosario.siat.gde.iface.model.ConvenioReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoSelAdapter;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.UserContext;

public class ConvenioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConvenioDAO.class);	
	
	private static long migId = -1;
	
	public ConvenioDAO() {
		super(Convenio.class);
	}
	
	public List<Convenio> getBySearchPage(ConvenioSearchPage convenioSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		UserContext userContext = DemodaUtil.currentUserContext();
		
		String queryString = "from Convenio t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ConvenioSearchPage: " + convenioSearchPage.infoString()); 
		}
	
		// filtro convenio excluidos
 		List<ConvenioVO> listConvenioExcluidos = (List<ConvenioVO>) convenioSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listConvenioExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listConvenioExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if (!ModelUtil.isNullOrEmpty(convenioSearchPage.getRecurso())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " + convenioSearchPage.getRecurso().getId();
			flagAnd = true;
		// Filtro por Recursos habilitados por Area de Usuario (Solicitado en Mantis 4752)
		}else if(convenioSearchPage.getListIdRecursoFiltro() != null){
			queryString += flagAnd ? " and " : " where ";
			if(!StringUtil.isNullOrEmpty(convenioSearchPage.getListIdRecursoFiltro())){
		 		queryString += " t.recurso.id in  (" + convenioSearchPage.getListIdRecursoFiltro() +")";
		 	}else{
		 		return new ArrayList<Convenio>();
		 	}
			flagAnd = true;
		}

		// filtro por Cuenta
 		if (!StringUtil.isNullOrEmpty(convenioSearchPage.getConvenio().getCuenta().getNumeroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
			
            if (convenioSearchPage.getConvenio().getCuenta().getNumeroCuenta().startsWith("-") && 
            		StringUtil.isNumeric(convenioSearchPage.getConvenio().getCuenta().getNumeroCuenta())){
            	queryString += " t.cuenta.id = " + 
            		convenioSearchPage.getConvenio().getCuenta().getNumeroCuenta().substring(1) ;
 			} else {  	
 				queryString += " t.cuenta.numeroCuenta = '" + 
 				StringUtil.formatNumeroCuenta(convenioSearchPage.getConvenio().getCuenta().getNumeroCuenta()) + "'";
 			}
            
			flagAnd = true;
		}
 		
 		// filtro por Numero Convenio
 		if (!StringUtil.isNullOrEmpty(convenioSearchPage.getConvenio().getNroConvenioView())) {
            queryString += flagAnd ? " and " : " where ";
			
            if (convenioSearchPage.getConvenio().getNroConvenio().intValue() < 0){
            	queryString += " t.id = " + convenioSearchPage.getConvenio().getNroConvenio().intValue() * -1;
 			} else {  	
 				queryString += " t.nroConvenio = " + StringUtil.escaparUpper(convenioSearchPage.getConvenio().getNroConvenioView());
 			}

 			flagAnd = true;
		}
 		
 		//Se definio el 16/10/08 en cobranza judicial que todos pueden ver todos los convenios
 		/**
 		// Si es Operador Judicial, solo convenios con via judidial
 		// Si es Procurador, solo ve concenios que le pertenezcan
 		if (userContext.getEsOperadorJudicial()){
            queryString += flagAnd ? " and " : " where ";
            queryString += " t.viaDeuda.id = " + ViaDeuda.ID_VIA_JUDICIAL;            
			flagAnd = true; 			
 		}
 		
 		if (userContext.getEsProcurador()){
 			queryString += flagAnd ? " and " : " where ";
            queryString += " t.procurador.id = " + userContext.getIdProcurador();            
			flagAnd = true; 			
 		}
 		**/
 		
 		// Order By
		queryString += " order by t.nroConvenio ";
				
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Convenio> listConvenio = (ArrayList<Convenio>) executeCountedSearch(queryString, convenioSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listConvenio;
	}
	
	public List<Long> getListIdConvenioSelAlmBySalPorCad (SalPorCadMasivoSelAdapter salPorCadSelVO)throws Exception{
				
		String queryString ="SELECT c.id FROM Convenio c, SelAlmDet s WHERE c.id = s.idElemento AND s.selAlm.id = "+ salPorCadSelVO.getSaldoPorCaducidad().getIdSelAlm();
		queryString += " ORDER BY c.nroConvenio";
		
		List<Long> listIdConvenio = (ArrayList<Long>)executeCountedQuery(queryString, salPorCadSelVO);
		
		return listIdConvenio;
	}
	


	/**
	 * Obtiene un Convenio por su numero
	 */
	public Convenio getByNroConvenio(String nroConvenio) throws Exception {
		Convenio convenio;
		String queryString = "from Convenio t where t.nroConvenio = :nroConvenio";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("nroConvenio", nroConvenio);
		convenio = (Convenio) query.uniqueResult();	

		return convenio; 
	}
	
	/**
	 * Obtiene Convenios por Plan y Numero
	 */
	public List<Convenio> getListByPlanYNumero(Plan plan, Long numero) throws Exception {
		List<Convenio> listConvenio;
		String queryString = "from Convenio t where t.plan.id = :idPlan and t.nroConvenio = :numero";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("idPlan", plan.getId());
		query.setLong("numero", numero);
		listConvenio = (ArrayList<Convenio>) query.list();	

		return listConvenio; 
	}
	
	/**
	 * Obtiene el Convenio por NroSistema y NroConvenio
	 */
	public Convenio getByNroSistemaYNroConvenio(String nroSistema, String nroConvenio) throws Exception {
		Convenio convenio = null;
		String queryString = "from Convenio t where t.sistema.nroSistema = :nroSistema and t.nroConvenio = :nroConvenio";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("nroSistema", nroSistema);
		query.setString("nroConvenio", nroConvenio);
		convenio = (Convenio) query.uniqueResult();	

		return convenio; 
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
		if(getMigId()==-1){
			setMigId(this.getLastId(path, nameFile)+1);
		}else{
			setMigId(getMigId() + 1);
		}

		return getMigId();
	}

	/**
	 *  Inserta una linea con los datos del Convenio para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param convenio, output - El Convenio a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(Convenio o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idConvenio.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
				 
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|nroconvenio|idplan|idcuenta|idviadeuda|idcanal|idprocurador|idestadoconvenio|idsistema|usuariofor|fechafor|idtipoperfor|idperfor|observacionfor|totcapitaloriginal|descapitaloriginal|totactualizacion|desactualizacion|totinteres|desinteres|totimporteconvenio|cantidadcuotasplan|ultcuoimp|recurso|usuario|fechaultmdf|estado

		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getNroConvenio());
		line.append("|");
		line.append(o.getPlan().getId());		 
		line.append("|");
		line.append(o.getCuenta().getId());		 
		line.append("|");
		line.append(o.getViaDeuda().getId());		 
		line.append("|");
		line.append(o.getCanal().getId());		 
		line.append("|");
		if(o.getProcurador()!=null){
			line.append(o.getProcurador().getId());
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(o.getEstadoConvenio().getId());		 
		line.append("|");
		line.append(o.getSistema().getId());		 
		line.append("|");
		if("".equals(o.getUsuarioFor())){
			line.append("  ");		 			
		}else{
			line.append(o.getUsuarioFor());
		}
		line.append("|");
		if(o.getFechaFor()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaFor(), "yyyy-MM-dd HH:mm:ss"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(o.getTipoPerFor().getId());		 
		line.append("|");
		if(o.getIdPerFor()!=null){
			line.append(o.getIdPerFor());		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(o.getObservacionFor());		 
		line.append("|");
		line.append(decimalFormat.format(o.getTotCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getDesCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotActualizacion()));
		line.append("|");
		line.append(decimalFormat.format(o.getDesActualizacion()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotInteres()));
		line.append("|");
		line.append(decimalFormat.format(o.getDesInteres()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotImporteConvenio()));
		line.append("|");
		line.append(o.getCantidadCuotasPlan());		 
		line.append("|");
		line.append(o.getUltCuoImp());		
		line.append("|");
		line.append(o.getRecurso().getId());		
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

	private String getSQLForConvenioReport(ConvenioReport convenioReport) throws Exception{
		
		boolean planSeleccionado       = convenioReport.getPlanSeleccionado();
		boolean viaDeudaSeleccionada   = convenioReport.getViaDeudaSeleccionada();
		boolean procuradorSeleccionado = convenioReport.getProcuradorSeleccionado();
		boolean estadoConvenioSeleccionado = convenioReport.getEstadoConvenioSeleccionado();
		boolean esDetallado      = convenioReport.getTipoReporte().getEsDetallado();
		
		String catalogoGeneral = SiatParam.getGeneralDbName();
		
		String queryString = "SELECT conv.id,"; 
		if (!estadoConvenioSeleccionado){
			queryString += "estConv.desestadoconvenio, ";
		}
		if (!planSeleccionado){
			queryString += "plan.desplan, ";
		}
		if (!viaDeudaSeleccionada){
			queryString += "via.desviadeuda, ";
		}
		if (!procuradorSeleccionado){
			queryString += "proc.descripcion, ";
		}
		queryString += "conv.nroconvenio, ";
		
		if (esDetallado){
			queryString += "cta.numerocuenta, (per.apellido || per.nombre_per) AS perform, "; 
		}
		queryString += "(conv.totcapitaloriginal - conv.descapitaloriginal) as capital, (conv.totactualizacion - conv.desactualizacion) as actualizacion, (conv.totinteres - conv.desinteres) as interes, conv.cantidadcuotasplan, " +
		"conCuo1.importecuota as anticipo, conCuo2.importecuota as restantes " +
		"FROM gde_convenio conv "+ 
		"INNER JOIN gde_plan plan ON (conv.idplan == plan.id) "+
		"INNER JOIN gde_estadoconvenio estConv ON (conv.idestadoconvenio == estConv.id) "+
		"INNER JOIN def_viadeuda via ON (conv.idviadeuda == via.id) "+
		"LEFT JOIN gde_procurador proc ON (conv.idprocurador == proc.id) ";

		if (esDetallado){
			queryString +=
			"INNER JOIN pad_cuenta cta ON (conv.idcuenta == cta.id) "+
			"LEFT JOIN " + catalogoGeneral + ":persona_id pid ON (conv.idperfor == pid.id_persona) "+
			"LEFT JOIN " + catalogoGeneral + ":personas per ON (pid.cuit00 = per.cuit00 AND pid.cuit01 = per.cuit01 AND pid.cuit02 = per.cuit02 AND pid.cuit03 = per.cuit03) ";
		}
		queryString +=
		"LEFT JOIN gde_conveniocuota conCuo1 ON (conCuo1.idconvenio == conv.id and conCuo1.nrocuotaimputada == 1) "+
		"LEFT JOIN gde_conveniocuota conCuo2 ON (conCuo2.idconvenio == conv.id and conCuo2.nrocuotaimputada == 2) " ;
		
		boolean flagAnd = false;
		
		// filtro plan
		if (planSeleccionado) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.idplan = " + convenioReport.getConvenio().getPlan().getId();
			flagAnd = true;
		}
		// via deuda
		if (viaDeudaSeleccionada) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.idviadeuda = " + convenioReport.getConvenio().getViaDeuda().getId();
			flagAnd = true;
		}
		// procurador
		if (procuradorSeleccionado) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.idprocurador = " + convenioReport.getConvenio().getProcurador().getId();
			flagAnd = true;
		}
		
		// filtro fechaConvenioDesde
		if (convenioReport.getFechaConvenioDesde() != null) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.fechafor >= TO_DATE('" + DateUtil.formatDate(convenioReport.getFechaConvenioDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		
		// filtro fechaConvenioHasta
		if (convenioReport.getFechaConvenioHasta() != null) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.fechafor <= TO_DATE('" + DateUtil.formatDate(convenioReport.getFechaConvenioHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}

		// filtro cuotaDesde
		if (convenioReport.getCuotaDesde() != null) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.cantidadcuotasplan >= " + convenioReport.getCuotaDesde() ;
			flagAnd = true;
		}

		// filtro cuotaHasta
		if (convenioReport.getCuotaHasta() != null) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.cantidadcuotasplan <= " + convenioReport.getCuotaHasta() ;
			flagAnd = true;
		}
		
		// filtro estado convenio
		if (estadoConvenioSeleccionado) {
			queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.idestadoconvenio = " + convenioReport.getConvenio().getEstadoConvenio().getId();
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if (!ModelUtil.isNullOrEmpty(convenioReport.getRecurso())) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " conv.idrecurso = " + convenioReport.getRecurso().getId();
			flagAnd = true;
		}

 		// ORDER BY
 		boolean flagOrderBy = false;
		if(!procuradorSeleccionado){
			queryString += flagOrderBy ? ", " : " ORDER BY ";
			queryString += " proc.descripcion";
			flagOrderBy = true;
		}
		if(!estadoConvenioSeleccionado){
			queryString += flagOrderBy ? ", " : " ORDER BY ";
			queryString += " estConv.id";
			flagOrderBy = true;
		}
		if(!planSeleccionado){
			queryString += flagOrderBy ? ", " : " ORDER BY ";
			queryString += " plan.desplan";
			flagOrderBy = true;
		}

		if(log.isDebugEnabled()){
			log.debug("queryString: " + queryString);
		}
		
		return queryString;
	}
	
	
	/**
	 * Genera el Reporte pdf de "Convenio".
	 * 
	 * @param convenioReport contiene los filtros de busqueda
	 * @return auxConvenioReport
	 */
	public AuxConvenioReport generarPdfForReport(AuxConvenioReport auxConvenioReport) throws Exception{
		
		// A partir de la implementacion de Adp en la generacion de Reporte se tuvo que utilizar una clase auxiliar
		// para mantener el codigo de generacion existente. Entonces en este punto se crea un convenioReport y
		// se pasan los datos necesarios.
		ConvenioReport convenioReport = new ConvenioReport();	

		convenioReport.setRecurso((RecursoVO) auxConvenioReport.getRecurso().toVO(1,false));

		if(auxConvenioReport.getPlanSeleccionado()){
			Plan plan = auxConvenioReport.getConvenio().getPlan();
			convenioReport.getConvenio().setPlan((PlanVO) plan.toVO(0));
		}
		if(auxConvenioReport.getViaDeudaSeleccionada()){
			ViaDeuda viaDeuda = auxConvenioReport.getConvenio().getViaDeuda();
			convenioReport.getConvenio().setViaDeuda((ViaDeudaVO) viaDeuda.toVO(0));
		}
		if(auxConvenioReport.getProcuradorSeleccionado()){
			Procurador procurador = auxConvenioReport.getConvenio().getProcurador();
			convenioReport.getConvenio().setProcurador((ProcuradorVO) procurador.toVO(0));
		}
		if(auxConvenioReport.getEstadoConvenioSeleccionado()){
			EstadoConvenio estadoConvenio = auxConvenioReport.getConvenio().getEstadoConvenio();
			convenioReport.getConvenio().setEstadoConvenio((EstadoConvenioVO) estadoConvenio.toVO(0));
		}
		
		convenioReport.setTipoReporte(auxConvenioReport.getTipoReporte());
		convenioReport.setIdTipoReporte(auxConvenioReport.getIdTipoReporte());
		
		convenioReport.setFechaConvenioDesde(auxConvenioReport.getFechaConvenioDesde());
		convenioReport.setFechaConvenioHasta(auxConvenioReport.getFechaConvenioHasta());
		convenioReport.setCuotaDesde(auxConvenioReport.getCuotaDesde());
		convenioReport.setCuotaHasta(auxConvenioReport.getCuotaHasta());
		// en este punto se termino de armar el ConvenioReport que antes se pasaba como parametro. 
		
		PlanillaVO reporte = new PlanillaVO();
		
		// obtencion del query a ejecutar
		String queryString = this.getSQLForConvenioReport(convenioReport);
		
		boolean planSeleccionado = convenioReport.getPlanSeleccionado();
		boolean esDetallado = convenioReport.getTipoReporte().getEsDetallado();
		boolean estadoConvenioSeleccionado = convenioReport.getEstadoConvenioSeleccionado();
		boolean viaDeudaSeleccionada = convenioReport.getViaDeudaSeleccionada();
		boolean procuradorSeleccionado = convenioReport.getProcuradorSeleccionado();
		boolean esTotalizado = convenioReport.getTipoReporte().getEsTotalizado();

		// formateador de decimales para las sumas
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		String idUsuario = auxConvenioReport.getUserId();
		
		//String fileSharePath = SiatParam.getString("FileSharePath"); 
		//String fileDir = fileSharePath + "/tmp"; // TODO obtener adecuadamente
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
		
		//	Datos Encabezado:
		String recurso   = convenioReport.getRecurso().getDesRecurso();
		String fechaDesde = DateUtil.formatDate(convenioReport.getFechaConvenioDesde(), DateUtil.ddSMMSYYYY_MASK);
		String fechaHasta = DateUtil.formatDate(convenioReport.getFechaConvenioHasta(), DateUtil.ddSMMSYYYY_MASK);
		
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileNameXML = idCorrida+"ReporteConvenio"+ idUsuario +".xml"; 
		OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(fileDir+"/"+fileNameXML), "ISO-8859-1");
		
		// Armado del PDF.
		// El Formulario fabrica el Print Model adecuado segun el codigo de formulario.
		PrintModel printModel = Formulario.getPrintModelForPDF("REP_CONVENIO");
		
		// obtencion dummy del string xsl
		// Abrimos archivo para parsearlo
		
		/* obtencion del xsl a partir de un archivo
		StringBuffer sf = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(new FileReader("/mnt/siat/tmp/convenio.xsl"));
		String line = "";         
		while ((line = bufferedReader.readLine()) != null) {
			sf.append(line);
			}
		bufferedReader.close();
		printModel.setXslString(sf.toString());
		*/
		
		// Datos del Encabezado del PDF
		printModel.putCabecera("TituloReporte", "Reporte de Convenios");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxConvenioReport.getUserName());// DemodaUtil.currentUserContext().getUserName());
		
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("Contenedor de Tablas");
		
		// armado de la tabla cabecera
		TablaVO tablaCabecera = new TablaVO("Filtros Aplicados");
	
		FilaVO filaDeCabecera = new FilaVO();
		filaDeCabecera.add(new CeldaVO(recurso,"recurso","Recurso"));
		if (planSeleccionado){
			String desPlan     = convenioReport.getConvenio().getPlan().getDesPlan();
			filaDeCabecera.add(new CeldaVO(desPlan,"desPlan", "Plan"));
		}
		filaDeCabecera.add(new CeldaVO(fechaDesde,"fechaConvenioDesde", "Fecha Convenio Desde"));
		filaDeCabecera.add(new CeldaVO(fechaHasta,"fechaConvenioHasta", "Fecha Convenio Hasta"));
		if (viaDeudaSeleccionada){
			String desViaDeuda = convenioReport.getConvenio().getViaDeuda().getDesViaDeuda();
			filaDeCabecera.add(new CeldaVO(desViaDeuda,"desViaDeuda", "V\u00EDa Deuda"));
		}
		if (procuradorSeleccionado){
			String descProcurador = convenioReport.getConvenio().getProcurador().getDescripcion();
			filaDeCabecera.add(new CeldaVO(descProcurador,"procurador", "Procurador"));
		}
		filaDeCabecera.add(new CeldaVO(convenioReport.getCuotaDesdeView(),"cuotaDesde", "Cuota Desde"));
		filaDeCabecera.add(new CeldaVO(convenioReport.getCuotaHastaView(),"cuotaHasta", "Cuota Hasta"));
		if (estadoConvenioSeleccionado){
			String desEstadoConvenio     = convenioReport.getConvenio().getEstadoConvenio().getDesEstadoConvenio();
			filaDeCabecera.add(new CeldaVO(desEstadoConvenio,"desEstadoConvenio", "Estado Convenio"));
		}
		filaDeCabecera.add(new CeldaVO(convenioReport.getTipoReporte().getValue(),"tipoReporte", "Tipo Reporte"));
		
		tablaCabecera.add(filaDeCabecera);
		
		contenedor.setTablaCabecera(tablaCabecera);
		
		// Se arman una tabla para el Resultado Total 
		TablaVO tabla = new TablaVO("Tabla Convenio");
		FilaVO filaTitulo = new FilaVO();
		// titulo de la tabla Convenios
		if(esTotalizado){
			filaTitulo.setNombre("Listado Totalizado de Convenios");      
		}else{
			filaTitulo.setNombre("Listado de Convenios");
		}
		filaTitulo.add(new CeldaVO("none","Reporte de Convenios"));
		tabla.setFilaTitulo(filaTitulo);
		
		FilaVO filaCabecera = new FilaVO();

		if(!esTotalizado){
			if (!estadoConvenioSeleccionado){
				filaCabecera.add(new CeldaVO("Estado","estadoConvenio"));
			}
			if(!planSeleccionado){
				CeldaVO c = new CeldaVO("Plan","plan"); 
				c.setWidth(45); 
				filaCabecera.add(c);
			}
			if(!viaDeudaSeleccionada){
				filaCabecera.add(new CeldaVO("Via Deuda","viaDeuda"));
			}
			
			/*if(!procuradorSeleccionado){
				CeldaVO c = new CeldaVO("Procurador","procurador"); 
				c.setWidth(45);
				filaCabecera.add(c);
			}*/
		}

		filaCabecera.add(new CeldaVO("Nro. Convenio","convenios"));

		if (!esTotalizado && esDetallado){
			filaCabecera.add(new CeldaVO("Cuenta","cuenta"));
			CeldaVO c = new CeldaVO("Persona Formalizo","perFor"); 
			c.setWidth(45);
			filaCabecera.add(c);
		}
		filaCabecera.add(new CeldaVO("Capital","capital"));
		filaCabecera.add(new CeldaVO("Actualizacion","actualizacion"));
		filaCabecera.add(new CeldaVO("Interes","interes"));
		filaCabecera.add(new CeldaVO("Ctd. Cuotas","ctdCuotas"));
		filaCabecera.add(new CeldaVO("Anticipo","anticipo"));
		filaCabecera.add(new CeldaVO("Restantes","restantes"));
		tabla.setFilaCabecera(filaCabecera);
		
		contenedor.add(tabla);
		
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// obtencion de la sesion y conexion para ejecutar la busqueda
	    Session session = currentSession();
	    Connection con = session.connection();
		PreparedStatement ps;
		ResultSet  rs;
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		// escritura del archivo xml que contiene los datos del reporte
		printModel.writeDataBegin(osWriter);
		osWriter.append("\n" + "<ContenedorVO>");
		osWriter.append("\n" + "<Nombre>ContenedorTablaConvenios </Nombre>");
		osWriter.append("\n" + "<ListTabla>");
		osWriter.append("\n" + "<TablaVO>");
		osWriter.append("\n" + "<Nombre>TablaConvenios</Nombre>");
		printModel.writeDataObject(osWriter, filaCabecera, 1,"FilaCabecera");
		osWriter.append("\n" + "<ListFila>");

		// Contadores 
		long   ctdConvenios = 0;
		double sumCapital   = 0;
		double sumActualizacion = 0;
		double sumInteres   = 0;
		long   sumCtdCuotas = 0;
		double sumAnticipo  = 0;
		double sumRestantes = 0;

		// ejecucion de la consulta e iteracion de los resultados
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();
		
		// Temporales para cortes de control.
		String descProcuradorAnterior = "";
		
		while(rs.next()){ 
			ctdConvenios ++;
			log.debug("id convenio = " + rs.getLong("id") + " - " + ctdConvenios);
			FilaVO fila    = new FilaVO(); // usamos la misma instancia
			FilaVO filaCorte = new FilaVO();
			
			double capital = rs.getDouble("capital");
			double actualizacion = rs.getDouble("actualizacion");
			double interes   = rs.getDouble("interes");
			long   ctdCuotas = rs.getLong("cantidadcuotasplan");
			double anticipo  = rs.getDouble("anticipo");
			double restantes = rs.getDouble("restantes");
				
			sumCapital += capital;
			sumActualizacion += actualizacion;
			sumInteres   += interes;
			sumCtdCuotas += ctdCuotas;
			sumAnticipo  += anticipo;
			sumRestantes += restantes;
			
			if (!esTotalizado){
				if (!estadoConvenioSeleccionado){
					fila.add(new CeldaVO(" " +rs.getString("desestadoconvenio"),"estadoConvenio"));
				}
				if (!planSeleccionado){
					fila.add(new CeldaVO(" " +rs.getString("desplan"),"plan"));
				}
				if (!viaDeudaSeleccionada){
					fila.add(new CeldaVO(" " +rs.getString("desviadeuda"),"viaDeuda"));
				}
				if (!procuradorSeleccionado){
					
					String  descProcurador = rs.getString("descripcion");
					
					if (descProcuradorAnterior != null) {
						if (descProcuradorAnterior.equalsIgnoreCase(descProcurador)){
							// Mismo procurador, no agrego cdc...
						} else {
							descProcuradorAnterior=descProcurador;
							// Agrego cdc...
							CeldaVO celda = new CeldaVO(StringUtil.cut(descProcurador),"procurador");
							celda.setWidth(45);
							filaCorte.add(celda);
							filaCorte.setNombre("filaCdC");
							printModel.writeDataObject(osWriter, filaCorte, 1);
						}
					}
					
				}
				fila.add(new CeldaVO(" " +rs.getString("nroconvenio"),"convenio"));
				if (!esTotalizado && esDetallado){
					fila.add(new CeldaVO(" " +rs.getString("numerocuenta"),"cuenta"));
					String perform = rs.getString("perform");
					fila.add(new CeldaVO((perform == null)?"":StringUtil.cut(perform) ,"perFor"));
				}
				
				
				fila.add(new CeldaVO(decimalFormat.format(capital),"capital"));
				fila.add(new CeldaVO(decimalFormat.format(actualizacion),"actualizacion"));
				fila.add(new CeldaVO(decimalFormat.format(interes),"interes"));
				fila.add(new CeldaVO(" " + ctdCuotas,"ctdCuotas"));
				fila.add(new CeldaVO(decimalFormat.format(anticipo),"anticipo"));
				fila.add(new CeldaVO(decimalFormat.format(restantes),"restantes"));

				// escribe en el bufferWrite el xml correspondiente a cada resultado de convenio			
				printModel.writeDataObject(osWriter, fila, 1);
				
				
				if( ctdConvenios == ConvenioReport.CTD_MAX_REG){
					reporte.addRecoverableValueError("El resultado del reporte exedio el tamaño maximo de registros permitidos (" + ConvenioReport.CTD_MAX_REG + " registros)");
					log.warn("Ctd Convenio resultado excedida.Supera a " + ConvenioReport.CTD_MAX_REG);
					break;
				}
			}
		}

		rs.close();
		ps.close();
		
		if (reporte.hasError()){
			osWriter.close();
			reporte.passErrorMessages(auxConvenioReport);
			return auxConvenioReport;
		}
		
		List<FilaVO> listFilaPie = new ArrayList<FilaVO>();
		FilaVO filaPie = new FilaVO();
		
		if (!esTotalizado){
			
			if (!estadoConvenioSeleccionado){
				filaPie.add(new CeldaVO(" "));
			}
			if (!planSeleccionado){
				filaPie.add(new CeldaVO(" "));
			}
			if (!viaDeudaSeleccionada){
				filaPie.add(new CeldaVO(" "));
			}
			/*if (!procuradorSeleccionado){
				filaPie.add(new CeldaVO(" "));
			}*/
			// TOTALES
			if (filaPie.getListCelda().size() > 0){
				CeldaVO c = filaPie.getListCelda().get(0);
				c.setValor("TOTALES");
			}
		}
		
		filaPie.add(new CeldaVO(""+ ctdConvenios,"ctdConvenios"));
		if (!esTotalizado && esDetallado){
			// nro de cuenta			
			filaPie.add(new CeldaVO(""));
			// pers que formalizo
			filaPie.add(new CeldaVO(""));
		}
		filaPie.add(new CeldaVO(decimalFormat.format(sumCapital),"capital"));
		filaPie.add(new CeldaVO(decimalFormat.format(sumActualizacion),"actualizacion"));
		filaPie.add(new CeldaVO(decimalFormat.format(sumInteres),"interes"));
		filaPie.add(new CeldaVO("" + sumCtdCuotas,"ctdCuotas"));
		filaPie.add(new CeldaVO(decimalFormat.format(sumAnticipo),"anticipo"));
		filaPie.add(new CeldaVO(decimalFormat.format(sumRestantes),"restantes"));
		
		listFilaPie.add(filaPie);

		osWriter.append("\n" + "</ListFila>");
		
		printModel.writeDataListObject(osWriter, listFilaPie,  2,"ListFilaPie");
		
		printModel.writeDataObject(osWriter, filaTitulo,  1,"FilaTitulo");
		
		osWriter.append("\n" + "</TablaVO>");
		
		osWriter.append("\n" + "</ListTabla>");
		
		osWriter.append("\n" + "<!-- Tabla cabecera -->");
		printModel.writeDataObject(osWriter, tablaCabecera, 2,"TablaCabecera");
		osWriter.append("\n" + "<!-- Fin Tabla cabecera -->");
		
		osWriter.append("\n" + "</ContenedorVO>");
		
		printModel.writeDataEnd(osWriter);
		osWriter.close();

		// Archivo xml generado
		File xmlTmpFile = new File(fileDir+"/"+fileNameXML);
		
		// Archivo pdf a generar
		String fileNamePdf = idCorrida+"ReporteConvenio"+ idUsuario +".pdf"; 
		File pdfFile = new File(fileDir+"/"+fileNamePdf);
		
		// generar el archivo pdf en base al xml (y al xsl) 
		printModel.fopRender(xmlTmpFile, pdfFile);
		
		reporte.setFileName(fileDir+"/"+fileNamePdf);
		reporte.setDescripcion("Reporte de Convenios");
		reporte.setCtdResultados(ctdConvenios);
		
		auxConvenioReport.setReporteGenerado(reporte);
		
		// Eliminamos el XML
		if(xmlTmpFile.exists())
			xmlTmpFile.delete();

		return auxConvenioReport;
	}


	/**
	 * Cualquiera de los parametros, si es null o <0, no se tiene en cuenta
	 * @param numeroConvenio
	 * @param idRecurso
	 * @param fechaFormalizacionDesde
	 * @param fechaFormalizacionHasta
	 * @param idViaDeuda
	 * @param noLiquidables - valores posibles: null, 0 y 1
	 * @return
	 */
	public ArrayList<Convenio> getList(Integer numeroConvenio,
			Long idRecurso, Date fechaFormalizacionDesde,
			Date fechaFormalizacionHasta, Long idViaDeuda, Long idProcurador, Integer noLiquidables) {
		String queryString ="from Convenio t ";
		boolean flagAnd = false;

		// numero convenio		
		if(numeroConvenio!=null && numeroConvenio>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.nroConvenio="+numeroConvenio; 
			flagAnd = true;
		}
		
		// idRecurso
		if(idRecurso!=null && idRecurso.longValue()>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.recurso.id="+idRecurso; 
			flagAnd = true;			
		}
		
		// fechaVtoDesde
		if(fechaFormalizacionDesde!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.fechaFor>= TO_DATE('" + 
					DateUtil.formatDate(fechaFormalizacionDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
			flagAnd = true;			
		}
		
		// fechaVtoHasta
		if(fechaFormalizacionHasta!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.fechaFor<= TO_DATE('" + 
					DateUtil.formatDate(fechaFormalizacionHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
			flagAnd = true;			
		}
		
		// viaDeuda
		if(idViaDeuda!=null && idViaDeuda.longValue()>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.viaDeuda.id="+idViaDeuda; 
			flagAnd = true;			
		}
		
		// procurador
		if(idProcurador!=null && idProcurador>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.procurador.id="+idProcurador; 
			flagAnd = true;			
		}
		
		// noLiquidables
		if(noLiquidables!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.noLiqComPro="+noLiquidables; 
			flagAnd = true;			
		}
		
		Session session = SiatHibernateUtil.currentSession();
		return (ArrayList<Convenio>)session.createQuery(queryString).list();
	}


	/**
	 * Utiliza SQL, no HQL. Ordena por procurador<br>
	 * cualquier de los parametros puede ser null
	 * @param listProcurador
	 * @param idRecurso
	 * @param idViaDeuda
	 * @param firstResult
	 * @param maxResults
	 * @param noLiqComPro - Para filtrar los que son liquidables
	 * @return
	 * @throws Exception
	 */
	public List<Convenio> getList(List<Procurador> listProcurador, Long idRecurso,
			Long idViaDeuda, boolean liquidable, Integer firstResult, Integer maxResults) throws Exception {
		String queryString="SELECT ";
		if(firstResult!=null){
			queryString += " SKIP "+firstResult;
		}
		
		if(maxResults!=null){
			queryString += " FIRST "+maxResults;
		}
		
		
		boolean flagAnd = false;
		queryString +=" DISTINCT convenio.* from gde_convenio convenio, gde_convenioCuota cc ";
		
		if(idRecurso!=null){
			queryString +=" WHERE  convenio.idRecurso="+idRecurso;
			flagAnd = true;
		}		
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " convenio.id = cc.idConvenio AND (cc.idEstadoConCuo= "+ EstadoConCuo.ID_PAGO_BUENO+
						" OR cc.idEstadoConCuo = "+EstadoConCuo.ID_PAGO_A_CUENTA + " )"+
						" AND cc.idLiqComPro IS NULL";
		flagAnd = true;
		
		if(listProcurador!=null && !listProcurador.isEmpty()){
 			String listIdProcurador = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listProcurador, 0, false));
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" convenio.idprocurador IN ("+listIdProcurador+") ";
 			flagAnd = true;
		}
		
		if(idViaDeuda!=null){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" convenio.idViaDeuda="+idViaDeuda;
 			flagAnd = true; 			
		}
		
		if(liquidable){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" (convenio.noLiqComPro IS NULL OR convenio.noLiqComPro=0) ";
 			flagAnd = true;			
		}
		
		//para test
		//queryString +=" AND convenio.id in (126070, 126083, 126126, 126056, 126088)";	
		
		queryString +=" ORDER BY convenio.idProcurador, convenio.id";			

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString).addEntity(Convenio.class);
		log.debug("Va a buscar convenios: query: "+queryString);
		return (ArrayList<Convenio>)query.list();		
	}

	public Integer getCount(List<Procurador> listProcurador, Long idRecurso,
			Long idViaDeuda, boolean liquidable) throws Exception {
		String queryString="SELECT ";		
		
		boolean flagAnd = false;
		queryString +=" count(DISTINCT convenio.id) from gde_convenio convenio, gde_convenioCuota cc ";
		
		if(idRecurso!=null){
			queryString +=" WHERE  convenio.idRecurso="+idRecurso;
			flagAnd = true;
		}		
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " convenio.id = cc.idConvenio AND (cc.idEstadoConCuo= "+ EstadoConCuo.ID_PAGO_BUENO+
						" OR cc.idEstadoConCuo = "+EstadoConCuo.ID_PAGO_A_CUENTA + " )"+
						" AND idLiqComPro IS NULL";
		flagAnd = true;
		
		if(listProcurador!=null && !listProcurador.isEmpty()){
 			String listIdProcurador = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listProcurador, 0, false));
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" convenio.idprocurador IN ("+listIdProcurador+") ";
 			flagAnd = true;
		}
		
		if(idViaDeuda!=null){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" convenio.idViaDeuda="+idViaDeuda;
 			flagAnd = true; 			
		}
		
		if(liquidable){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" (convenio.noLiqComPro IS NULL OR convenio.noLiqComPro=0) ";
 			flagAnd = true;			
		}
		
		//para test
		//queryString +=" AND convenio.id in (126070, 126083, 126126, 126056, 126088)";		
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString);
		log.debug("Va a buscar convenios: query: "+queryString);
		return ((BigDecimal) query.uniqueResult()).intValue();		
	}

	/**
	 * Devuelve una lista de convenios vigentes de forma paginada
	 * 
	 * @param skip
	 * @param first
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Convenio> getListVigentes(Long skip, Long first) throws Exception {
	 	String queryString = ""; 
	    queryString += "select skip " + skip + " first " + first + " convenio.* "; 
	    queryString += "from gde_convenio convenio ";
	    queryString	+= "where convenio.idEstadoConvenio = " + EstadoConvenio.ID_VIGENTE + " "; 
	    queryString	+= "order by id";
	    
	    Session session = SiatHibernateUtil.currentSession();
	    Query query = session.createSQLQuery(queryString)
	    				.addEntity("convenio", Convenio.class);
		
	    return (ArrayList<Convenio>) query.list();
	}
	
	public boolean tieneCuotaSaldoPaga(Convenio convenio){
		ReciboConvenio reciboConvenio= null;
		
		String queryString = "FROM ReciboConvenio r WHERE r.esCuotaSaldo = 1";
		queryString += "AND r.convenio.id = "+ convenio.getId();
		queryString += " AND fechaPago IS NOT NULL";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		query.setMaxResults(1);
		
		reciboConvenio = (ReciboConvenio) query.uniqueResult();
		
		return reciboConvenio!=null ? true: false;
	}
	
	 
	public List<Object[]> getConveniosFormReport(Long idViaDeuda, Date fechaDesde, Date fechaHasta, Long idProcurador) throws Exception {
		
			String queryContado = "SELECT conv.idplan, conv.idrecurso, conv.idarea, conv.idoficina, conv.idprocurador, ";
				
		 	queryContado += "COUNT(conv.id) as cantidad, SUM(cta.importecuota) AS anticipo, " +
		 				"SUM(conv.totimporteconvenio) - SUM(cta.importecuota) AS resto " +
					 	"FROM gde_convenio conv, gde_conveniocuota cta " +
					 	"WHERE conv.id = cta.idconvenio " +
					 	"AND conv.idviadeuda = " + idViaDeuda + " " +
					 	"AND cta.numerocuota = 1 " +
					 	"AND conv.cantidadcuotasplan = 1 ";
					 	
		 	if (!(StringUtil.isNullOrEmpty(idProcurador.toString()) || idProcurador.equals(-1L))){// Agregado
		 		queryContado += "AND conv.idprocurador = " + idProcurador + " ";
		 	}
		 	
		 	queryContado += "AND conv.fechafor >= TO_DATE('" + DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') " +
					 	"AND conv.fechafor <= TO_DATE('" + DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') " +
					 	"GROUP BY conv.idplan, conv.idrecurso, conv.idarea, conv.idoficina, conv.idprocurador " +
					 	"ORDER BY conv.idplan, conv.idrecurso, conv.idarea, conv.idoficina, conv.idprocurador";
		 	
		 	log.debug("queryContado: " + queryContado );

		    Session session = currentSession();
		    Connection con = session.connection();
			PreparedStatement ps;
			ResultSet  rs;
		 	
			// ejecucion de la consulta e iteracion de los resultados
			ps = con.prepareStatement(queryContado);
			rs = ps.executeQuery();
			
			ArrayList<Object[]> listResult = new ArrayList();
			
			int cant = 0;
			
			while(rs.next()){ 
				Object[] reg = new Object[9];

				long idplan = rs.getLong("idplan"); 
				long idrecurso = rs.getLong("idrecurso"); 
				long idarea = rs.getLong("idarea"); 
				long idoficina = rs.getLong("idoficina");
				long idprocurador = rs.getLong("idprocurador"); 
				int cantidad = rs.getInt("cantidad");
				double anticipo = rs.getDouble("anticipo"); 
				double resto = rs.getDouble("resto");
				
				reg[0] = idplan;
				reg[1] = idrecurso;
				reg[2] = idarea;
				reg[3] = idoficina;
				reg[4] = idprocurador;
				reg[5] = 1;
				reg[6] = cantidad;
				reg[7] = anticipo;
				reg[8] = resto;
				
				listResult.add(reg);
				
				cant ++;
			}

			
			log.debug(" contado: " + cant);
			
			rs.close();
			ps.close();

			// Agregamos la consulta de los convenios financiados
			String queryFinanciado = "SELECT conv.idplan, conv.idrecurso, conv.idarea, conv.idoficina, conv.idprocurador, " +
				"COUNT(conv.id) as cantidad, SUM(cta.importecuota) AS anticipo, " +
				"SUM(conv.totimporteconvenio) - SUM(cta.importecuota) AS resto " +
			 	"FROM gde_convenio conv, gde_conveniocuota cta " +
			 	"WHERE conv.id = cta.idconvenio " +
			 	"AND conv.idviadeuda = " + idViaDeuda + " " +
			 	"AND cta.numerocuota = 1 " +
			 	"AND conv.cantidadcuotasplan > 1 ";
			
			if (!(StringUtil.isNullOrEmpty(idProcurador.toString()) || idProcurador.equals(-1L))){// Agregado
		 		queryFinanciado += "AND conv.idprocurador = " + idProcurador + " ";
		 	}
		 	
		 	queryFinanciado+= "AND conv.fechafor >= TO_DATE('" + DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') " +
			 	"AND conv.fechafor <= TO_DATE('" + DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') " +
			 	"GROUP BY conv.idplan, conv.idrecurso, conv.idarea, conv.idoficina, conv.idprocurador " +
			 	"ORDER BY conv.idplan, conv.idrecurso, conv.idarea, conv.idoficina, conv.idprocurador";
	
			log.debug("queryFinanciado: " + queryFinanciado );
				
			// ejecucion de la consulta e iteracion de los resultados
			ps = con.prepareStatement(queryFinanciado);
			rs = ps.executeQuery();
			
			cant = 0;
			
			while(rs.next()){ 
				Object[] reg = new Object[9];
			
				long idplan = rs.getLong("idplan"); 
				long idrecurso = rs.getLong("idrecurso"); 
				long idarea = rs.getLong("idarea"); 
				long idoficina = rs.getLong("idoficina");
				long idprocurador = rs.getLong("idprocurador"); 
				int cantidad = rs.getInt("cantidad");
				double anticipo = rs.getDouble("anticipo"); 
				double resto = rs.getDouble("resto");
				
				reg[0] = idplan;
				reg[1] = idrecurso;
				reg[2] = idarea;
				reg[3] = idoficina;
				reg[4] = idprocurador;
				reg[5] = 2;
				reg[6] = cantidad;
				reg[7] = anticipo;
				reg[8] = resto;
				
				listResult.add(reg);
				
				cant ++;
			}
			
			
			log.debug(" financiado: " + cant);
			
			rs.close();
			ps.close();
			
			
			return listResult; 
	 }


	/**
	 * Obtiene la lista de Recurso para el Reporte de Convenios a Caducar segun los filtros de busquedas 
	 * pasados como parametros.
	 * 
	 * @param recurso
	 * @param plan
	 * @param viaDeuda
	 * @param cuotaDesde
	 * @param cuotaHasta
	 * @param importeCuotaDesde
	 * @param importeCuotaHasta
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param estadoConvenio
	 * @return listConvenio
	 * @throws Exception
	 */
	public List<Convenio> getListForReport(Recurso recurso,Plan plan,ViaDeuda viaDeuda,Integer cuotaDesde,Integer cuotaHasta,Double importeCuotaDesde,Double importeCuotaHasta,Date fechaDesde,Date fechaHasta, EstadoConvenio estadoConvenio) throws Exception {
		List<Convenio> listConvenio;
		
		/*
		select distinct c.* from gde_convenio c, gde_conveniocuota cc where idrecurso=14 
        and cc.idconvenio = c.id
        and c.idplan = 1 and c.idviadeuda = 1 and c.idestadoConvenio = 1
        and c.fechafor>='2008-06-01 00:00:00' and c.fechafor<='2008-09-01 00:00:00'
        and c.cantidadcuotasplan>10 and c.cantidadcuotasplan<120
        and ((cc.numerocuota = 1 or cc.numerocuota=2) and (cc.importecuota>10 and cc.importecuota<300));
		 */
	    String  queryString = "FROM Convenio c WHERE c.recurso.id = "+recurso.getId();
	    
	    //queryString += " AND cc.convenio.id = c.id";
	    
	    if(plan != null)
	    	queryString += " AND c.plan.id = "+plan.getId();	
	    else if(viaDeuda != null)
	    	queryString += " AND c.viaDeuda.id = "+viaDeuda.getId();	    	
	    if(estadoConvenio != null)
	    	queryString += " AND c.estadoConvenio.id = "+estadoConvenio.getId();
	    if(fechaDesde != null)
	    	queryString += " AND c.fechaFor >= TO_DATE('"+DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    if(fechaHasta != null)
	    	queryString += " AND c.fechaFor <= TO_DATE('"+DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	    if(cuotaDesde != null)
	    	queryString += " AND c.cantidadCuotasPlan >= "+cuotaDesde;
	    if(cuotaDesde != null)
	    	queryString += " AND c.cantidadCuotasPlan >= "+cuotaHasta;
	    if(importeCuotaDesde != null || importeCuotaHasta!= null){
	    	queryString += " AND (( c.convenioCuota.numeroCuota = 1 OR c.convenioCuota.numeroCuota=2 ) AND ( ";
	    	if(importeCuotaDesde != null)
	    		queryString += " c.convenioCuota.importeCuota >= "+importeCuotaDesde;
	    	if(importeCuotaDesde != null && importeCuotaHasta!= null)
	    		queryString += " AND ";
	    	if(importeCuotaHasta != null)
	    		queryString += " c.convenioCuota.importeCuota <= "+importeCuotaHasta;
	    	queryString += " )) ";
	    }
	    
	    Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		listConvenio = (ArrayList<Convenio>) query.list();

		return listConvenio; 
	}

	/**
	 * Retorna el ultimo convenio en el que estuvo
	 * la deuda con id idDeuda.
	 * Null si no lo encuentra
	 * 
	 * @param idDeuda
	 * @return Convenio
	 * */
	public Convenio getLastByIdDeuda(Long idDeuda) {
		try {
		
			if (log.isDebugEnabled()) 
				log.debug(DemodaUtil.currentMethodName() + ": enter");
			
			String strQuery = "";
			strQuery += "select convenio from Convenio convenio, ConvenioDeuda conDeu ";
			strQuery +=	"where convenio.id = conDeu.convenio.id "; 
			strQuery +=	  "and conDeu.idDeuda = " + idDeuda + " ";
			strQuery += "order by convenio.fechaFor desc";
			
			if (log.isDebugEnabled()) 
				log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(strQuery);
		
			if (log.isDebugEnabled()) 
				log.debug(DemodaUtil.currentMethodName() + ": exit");
			
			return (Convenio) query.list().get(0); 
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Long> getListIdConNoProcSalPorCad (Long idSalPorCad){
		String queryString = " SELECT scd.convenio.id FROM SalPorCadDet scd WHERE scd.salPorCad.id = "+idSalPorCad;
		
		queryString += " AND (scd.procesado is null OR scd.procesado < 1 )";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		List <Long> listIds=query.list();
		
		return listIds;
		
	}

	public static void setMigId(long migId) {
		ConvenioDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

	
	/**
	 * Utiliza SQL, no HQL. Ordena por procurador<br>
	 * cualquier de los parametros puede ser null
	 * @param listProcurador
	 * @param listRecurso
	 * @param idViaDeuda
	 * @param firstResult
	 * @param maxResults
	 * @param noLiqComPro - Para filtrar los que son liquidables
	 * @return
	 * @throws Exception
	 */
	public List<Convenio> getListByListRecurso(List<Procurador> listProcurador, List<Recurso> listRecurso,
			Long idViaDeuda, boolean liquidable, Integer firstResult, Integer maxResults) throws Exception {
		String queryString="SELECT ";
		if(firstResult!=null){
			queryString += " SKIP "+firstResult;
		}
		
		if(maxResults!=null){
			queryString += " FIRST "+maxResults;
		}
		
		boolean flagAnd = false;
		queryString +=" DISTINCT convenio.* from gde_convenio convenio, gde_convenioCuota cc ";
		
		if(!ListUtil.isNullOrEmpty(listRecurso)){
			String listIdRecurso = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listRecurso, 0, false));
			queryString +=" WHERE  convenio.idRecurso IN ("+listIdRecurso+") ";
			flagAnd = true;
		}		
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " convenio.id = cc.idConvenio AND (cc.idEstadoConCuo= "+ EstadoConCuo.ID_PAGO_BUENO+
						" OR cc.idEstadoConCuo = "+EstadoConCuo.ID_PAGO_A_CUENTA + " )"+
						" AND cc.idLiqComPro IS NULL";
		flagAnd = true;
		
		if(listProcurador!=null && !listProcurador.isEmpty()){
 			String listIdProcurador = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listProcurador, 0, false));
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" convenio.idprocurador IN ("+listIdProcurador+") ";
 			flagAnd = true;
		}
		
		if(idViaDeuda!=null){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" convenio.idViaDeuda="+idViaDeuda;
 			flagAnd = true; 			
		}
		
		if(liquidable){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" (convenio.noLiqComPro IS NULL OR convenio.noLiqComPro=0) ";
 			flagAnd = true;			
		}
				
		queryString +=" ORDER BY convenio.idProcurador, convenio.id";			

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString).addEntity(Convenio.class);
		log.debug("Va a buscar convenios: query: "+queryString);
		return (ArrayList<Convenio>)query.list();		
	}
	
	public Integer getCountByListRecurso(List<Procurador> listProcurador, List<Recurso> listRecurso,
			Long idViaDeuda, boolean liquidable) throws Exception {
		String queryString="SELECT ";		
		
		boolean flagAnd = false;
		queryString +=" count(DISTINCT convenio.id) from gde_convenio convenio, gde_convenioCuota cc ";
		
		if(!ListUtil.isNullOrEmpty(listRecurso)){
			String listIdRecurso = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listRecurso, 0, false));
			queryString +=" WHERE  convenio.idRecurso IN ("+listIdRecurso+") ";
			flagAnd = true;
		}	
		
		queryString += flagAnd?" AND ":" WHERE ";
		queryString += " convenio.id = cc.idConvenio AND (cc.idEstadoConCuo= "+ EstadoConCuo.ID_PAGO_BUENO+
						" OR cc.idEstadoConCuo = "+EstadoConCuo.ID_PAGO_A_CUENTA + " )"+
						" AND idLiqComPro IS NULL";
		flagAnd = true;
		
		if(listProcurador!=null && !listProcurador.isEmpty()){
 			String listIdProcurador = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listProcurador, 0, false));
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" convenio.idprocurador IN ("+listIdProcurador+") ";
 			flagAnd = true;
		}
		
		if(idViaDeuda!=null){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" convenio.idViaDeuda="+idViaDeuda;
 			flagAnd = true; 			
		}
		
		if(liquidable){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" (convenio.noLiqComPro IS NULL OR convenio.noLiqComPro=0) ";
 			flagAnd = true;			
		}
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString);
		log.debug("Va a buscar convenios: query: "+queryString);
		return ((BigDecimal) query.uniqueResult()).intValue();		
	}

}
