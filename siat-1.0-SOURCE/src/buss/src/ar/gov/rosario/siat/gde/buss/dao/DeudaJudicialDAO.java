//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.AuxGesJudReport;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.DevolucionDeuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Evento;
import ar.gov.rosario.siat.gde.buss.bean.GesJud;
import ar.gov.rosario.siat.gde.buss.bean.GesJudEvento;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasConsPorCtaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoGesJudVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudReport;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;

public class DeudaJudicialDAO extends DeudaDAO {
	
	private Log log = LogFactory.getLog(DeudaJudicialDAO.class);	
	
	private static long migId = -1;
	
	public DeudaJudicialDAO() {
		super(DeudaJudicial.class);
	}
	
	/**
	 * Obtiene todos los registros de deuda de la tabla gde_deudaJudicial.
	 * Indistintamente devolvera deuda en via Judicial o Concurso y Quiebra.
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @return
	 */
	public List<DeudaJudicial> getListDeudaJudicial(Cuenta cuenta){
		Session session = SiatHibernateUtil.currentSession();
    	
		EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_JUDICIAL); 
				
		String sQuery = "FROM DeudaJudicial d " +
			"WHERE d.cuenta = :cuenta AND " +
			" d.estadoDeuda = :estadoDeuda ";
		
		// Filtros agregados para R4.
		if (cuenta.getLiqCuentaFilter() != null){
			// Aca no utilizamos RecClaDeu para evitar otro getById()
			if (!ModelUtil.isNullOrEmpty(cuenta.getLiqCuentaFilter().getRecClaDeu()))
				sQuery += " AND d.recClaDeu.id = " + cuenta.getLiqCuentaFilter().getRecClaDeu().getId();
			
			if (cuenta.getLiqCuentaFilter().getFechaVtoDesde() != null)
				sQuery += " AND d.fechaVencimiento >= :fechaVtoDesde ";
			
			if (cuenta.getLiqCuentaFilter().getFechaVtoHasta() != null)
				sQuery += " AND d.fechaVencimiento <= :fechaVtoHasta ";
			
			//06/08/09 Se sacan los filtros de declaracion jurada
			// Alguna de las opciones que necesita join con DecJur
			/*if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsJoinDeclaracion()){
				
				String listIdsDeudaDecJur = "";
				String qryDecJur = "SELECT decJur.idDeuda FROM gde_decjur decJur " +
								   "WHERE estado = 1 AND decJur.idCuenta = " + cuenta.getId();
				
				Query sqlQry = session.createSQLQuery(qryDecJur);
				
				List<Integer> listId = (ArrayList<Integer>) sqlQry.list();
				
				if(!ListUtil.isNullOrEmpty(listId)){
					listIdsDeudaDecJur = "(";
					for(Integer id: listId){
						if(!listIdsDeudaDecJur.equals("("))
							listIdsDeudaDecJur += ",";
						listIdsDeudaDecJur += id.toString();
					}
					listIdsDeudaDecJur +=")";
				}
				
				log.debug("listIdsDeudaDecJur: " + listIdsDeudaDecJur);				
			
			
				// En Declaracion Jurada
				if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsConDeclaracion() &&
						listIdsDeudaDecJur != ""){
					sQuery += " AND d.id IN " + listIdsDeudaDecJur; 
				}
				// Resultado vacio, ya que solicita deuda en declaracion, y no existe declaracion
				if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsConDeclaracion() &&
						listIdsDeudaDecJur == ""){
					return new ArrayList<DeudaAdmin>();
				}
				
				// No en Declaracion Jurada
				if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsSinDeclaracion() && 
						listIdsDeudaDecJur != ""){
					sQuery += " AND d.id NOT IN " + listIdsDeudaDecJur; 
				}
			
			// Si no necesitamos joinear con DecJur. 	
			} else {*/
				
			// Declarados Impagos
			if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsInpago()){
				sQuery += " AND d.saldo > 0 ";
			}
				
			// Declarados Pagos
			if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsPago()){
				sQuery += " AND d.importe > 0 AND d.saldo = 0 ";
			}				
			
			// No Determinados
			if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsNoDeterminado()){
				sQuery += " AND d.importe = 0 AND d.saldo = 0 ";
			}
			
			// Adeudado + No Determinados + Determinados Impagos
			if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsAdeudado()){
				sQuery += " AND (d.saldo > 0 OR (d.importe = 0 AND d.saldo = 0)) ";
			}
		}
		
		// Cambio en el order by por mantener gestion en dos vias.
		//sQuery += "ORDER BY d.idProcedimientoCyQ, d.procurador, d.anio, d.periodo";
		
		// Nuevo cambio en order by para soportar gravamenes
		//sQuery += "ORDER BY d.procurador, d.anio, d.periodo, d.idProcedimientoCyQ";
    	
		sQuery += "ORDER BY d.procurador, d.fechaVencimiento, d.idProcedimientoCyQ";
		
    	Query query = session.createQuery(sQuery)
			.setEntity("cuenta", cuenta)
			.setEntity("estadoDeuda", estadoDeuda );
    	
		if (cuenta.getLiqCuentaFilter() != null ){
			if(cuenta.getLiqCuentaFilter().getFechaVtoDesde() != null)
				query.setDate("fechaVtoDesde", cuenta.getLiqCuentaFilter().getFechaVtoDesde());
			
			if(cuenta.getLiqCuentaFilter().getFechaVtoHasta() != null)
				query.setDate("fechaVtoHasta", cuenta.getLiqCuentaFilter().getFechaVtoHasta());

		}
    	
    	return query.list();		
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
	 *  Inserta una linea con los datos de la Deuda Judicial para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param deudaJudicial, output - La Deuda Judicial a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(DeudaJudicial o, LogFile output) throws Exception {
		 
		// Obtenemos el valor del id autogenerado a insertar.
		long id = 0;
		if(migId == -1){
			long idJudicial = this.getLastId(output.getPath(), output.getNameFile());
			long idAdmin = this.getLastId(output.getPath(), "deudaAdmin.txt");
			long idCancelada = this.getLastId(output.getPath(), "deudaCancelada.txt");
			long idAnulada = this.getLastId(output.getPath(), "deudaAnulada.txt");
			long idPrescripta = this.getLastId(output.getPath(), "deudaPrescripta.txt");
			if(idJudicial>=idAdmin && idJudicial>=idCancelada && idJudicial>=idAnulada && idJudicial>=idPrescripta){
				id = getNextId(output.getPath(), output.getNameFile());				 
			}else{
				if(idCancelada>=idAdmin && idCancelada>=idAnulada && idCancelada>=idPrescripta)
					id = getNextId(output.getPath(), "deudaCancelada.txt");
				else if(idAdmin>=idAnulada && idAdmin>=idPrescripta)
					id = getNextId(output.getPath(), "deudaAdmin.txt");
				else if(idAnulada>=idPrescripta)
					id = getNextId(output.getPath(), "deudaAnulada.txt");
				else
					id = getNextId(output.getPath(), "deudaPrescripta.txt");
			}
			// Id Preseteado (Inicialmente usado para la migracion de CdM)
			// Archivo con una unica linea:
			// 54378|
			long idPreset = this.getLastId(output.getPath(), "idDeuda.set");
			if(id <= idPreset){
				id = idPreset;
			 }
			migId = id;				 
		}else{
			id = getNextId(output.getPath(), output.getNameFile());
		}
		 
		DecimalFormat decimalFormat = new DecimalFormat("#.0000000000");
		// Estrucura de la linea:
		// id|idProcedimientoCyQ|actualizacionCyQ|codrefpag|idcuenta|idsistema|idreccladeu|idviadeuda|idestadodeuda|idrecurso|anio|periodo|fechavencimiento|fechapago|importe|importebruto|saldo|actualizacion|fechaemision|estaimpresa|resto|reclamada|idprocurador|idconvenio|usuario|fechaultmdf|estado|
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		if (o.getIdProcedimientoCyQ()!= null)
			line.append(o.getIdProcedimientoCyQ());
		line.append("|");
		if(o.getActualizacionCyQ()!=null)
			line.append(decimalFormat.format(o.getActualizacionCyQ()));
		line.append("|");
		line.append(o.getCodRefPag());
		line.append("|");
		line.append(o.getCuenta().getId());		 
		line.append("|");
		line.append(o.getSistema().getId());		 
		line.append("|");
		line.append(o.getRecClaDeu().getId());
		line.append("|");
		line.append(o.getViaDeuda().getId());
		line.append("|");
		line.append(o.getEstadoDeuda().getId());
		line.append("|");

//		line.append(o.getServicioBanco().getId()); se quito el servicio banco de la deuda
//		line.append("|");
		line.append(o.getRecurso().getId());
		line.append("|");
		line.append(o.getAnio());
		line.append("|");
		line.append(o.getPeriodo());
		line.append("|");
		if(o.getFechaVencimiento()!=null){
			line.append(DateUtil.formatDate(o.getFechaVencimiento(), "yyyy-MM-dd"));
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getFechaPago()!=null){
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(decimalFormat.format(o.getImporte()));
		line.append("|");
		line.append(decimalFormat.format(o.getImporteBruto()));
		line.append("|");
		line.append(decimalFormat.format(o.getSaldo()));
		line.append("|");
		if(o.getActualizacion()!=null)
			line.append(decimalFormat.format(o.getActualizacion()));
		line.append("|");
		if(o.getFechaEmision()!=null){
			line.append(DateUtil.formatDate(o.getFechaEmision(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(o.getEstaImpresa());
		line.append("|");
		line.append(o.getResto());
		line.append("|");
		line.append(o.getReclamada());	 
		line.append("|");
		if(o.getProcurador()!=null)
			line.append(o.getProcurador().getId());
		line.append("|");
		if(o.getConvenio()!=null)
			line.append(o.getConvenio().getId());
		line.append("|");
		line.append(o.getStrConceptosProp());
 		line.append("|");
 		if(o.getAtrAseVal()!=null)
 			line.append(o.getAtrAseVal());
 		line.append("|");
 		if(o.getStrEstadoDeuda()!=null)
 			line.append(o.getStrEstadoDeuda());
 		line.append("|");
		line.append(DemodaUtil.currentUserContext().getUserName());
		line.append("|");
		 
		//StringBuffer actualDate = new StringBuffer(DateUtil.formatDate(Calendar.getInstance().getTime(), "yyyy-MM-dd"));
		//actualDate.append(" 00:00:00");
		line.append("2010-01-01 00:00:00");//actualDate.toString());
		 
		line.append("|");
		line.append("1");
		line.append("|");
		 
		output.addline(line.toString());
		 
		// Seteamos el id generado en el bean.
		o.setId(id);
	
		return id;
	}

	/**
	 * Copia las Deuda Administrativas a Deudas Judiciales del Envio Judicial 
	 * que fueron cargadas en las constancias de deuda de las planillas del envio judicial 
	 * @param procesoMasivo
	 * @return int
	 * @throws Exception
	 */
	public int copiarDeudaAJudicialJdbc(ProcesoMasivo procesoMasivo) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// TODO ver si copiamos todos los campos con un SELECT * y un IN
		/*
		  String sqlInsert = "INSERT INTO gde_deudajudicial "+ 
		  "SELECT da.id, da.codrefpag, da.idcuenta, da.idreccladeu, da.idviadeuda, da.idestadodeuda, " +
		  "da.idserviciobanco, da.periodo, da.anio, da.fechaemision, da.fechavencimiento, da.importebruto, " +
		  "da.importe, da.saldo, da.actualizacion, da.strconceptosprop, da.strestadodeuda, da.estaimpresa, " +
		  "da.idrepartidor, da.idprocurador, da.fechapago, da.idprocedimientocyq, da.actualizacioncyq, " +
		  "da.idemision, da.obsmotnopre, da.reclamada, da.idsistema, da.resto, da.idconvenio, da.usuario, " +
		  "da.fechaultmdf, da.estado " +
		  "FROM gde_deudaadmin da " +
		  "INNER JOIN gde_condeudet cdd ON (da.id == cdd.idDeuda) " +
		  "INNER JOIN gde_constanciadeu cd ON (cdd.idconstanciadeu == cd.id) " +
		  " WHERE cd.idProcesoMasivo = " + procesoMasivo.getId();
		*/
		 
		String sqlInsert = "INSERT INTO gde_deudajudicial " + 
			"SELECT * FROM gde_deudaadmin da WHERE da.id IN (" +
		 	"SELECT cdd.iddeuda FROM gde_condeudet cdd " +
		 	"INNER JOIN gde_constanciadeu cd ON (cdd.idconstanciadeu == cd.id) " +
		 	"WHERE cd.idProcesoMasivo = " + procesoMasivo.getId() + ")";

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + sqlInsert);

		Connection        con;
		PreparedStatement ps;

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = SiatHibernateUtil.currentSession().connection();
		 
		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
		con.setAutoCommit(false);
		// ejecucion de la consulta de resultado
		ps = con.prepareStatement(sqlInsert);
		int resultado = ps.executeUpdate();
		ps.close();

		return resultado;
	}

	public String getIdsStringCopiarDeudaAJudicial(ProcesoMasivo procesoMasivo) throws Exception {

		//String sql = "SELECT da.id idDeuda FROM gde_deudaadmin da WHERE da.id IN (" +
		//	"SELECT cdd.iddeuda FROM gde_condeudet cdd " +
		//	"INNER JOIN gde_constanciadeu cd ON (cdd.idconstanciadeu == cd.id) " +
		//	"WHERE cd.idProcesoMasivo = " + procesoMasivo.getId() + ")";
		
		String sql = "SELECT pmdi.idDeuda FROM gde_promasdeuinc pmdi " +
		"WHERE pmdi.idprocesomasivo == " + procesoMasivo.getId();
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(sql).addScalar("idDeuda", Hibernate.LONG);
		List<Long> ids = query.list();
		StringBuilder sb = new StringBuilder("");
		for(Long id: ids) {
			sb.append(id).append(",");
		}
		return sb.toString();
	}

	 
	public int copiarDeudaAJudicial(DeudaAdmin deudaAdmin, Procurador procurador, Date fechaEnvio) throws Exception {
		String usuario = DemodaUtil.currentUserContext().getUserName();
		String fechaEnvioStr = DateUtil.formatDate(fechaEnvio, DateUtil.yyyy_MM_dd_MASK);
		String fechaAhora = DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
		String camposSelect = "id, codrefpag, idcuenta, idreccladeu, " + ViaDeuda.ID_VIA_JUDICIAL + " idviadeuda, " + 
					EstadoDeuda.ID_JUDICIAL + " idestadodeuda, " +
					" periodo, anio, fechaemision, fechavencimiento, " +
					" importebruto, importe, saldo, actualizacion, " +
					" strconceptosprop, strestadodeuda, estaimpresa, " +
					" idrepartidor," + procurador.getId() + " idprocurador, " +
					" fechapago, idprocedimientocyq, actualizacioncyq, idemision, " +
					" obsmotnopre, reclamada, idsistema, resto, idconvenio, " +
					" '" + usuario + "' usuario, " +
					" to_date('" + fechaAhora + "', '%Y-%m-%d %H:%M:%S') fechaultmdf, " +
					" estado, idrecurso, atraseval, to_date('" + fechaEnvioStr + "', '%Y-%m-%d') fechaEnvio";
		
		String sqlInsert = "INSERT INTO gde_deudajudicial " +
			"SELECT " + camposSelect + " FROM gde_deudaadmin " +
			"WHERE id = " + deudaAdmin.getId();
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(sqlInsert);
		
		return query.executeUpdate();
	}
	
		
	public DeudaJudicial getByCodRefPag(Long codRefPag){
		DeudaJudicial deudaJudicial;
		String queryString = "from DeudaJudicial t where t.codRefPag = :codRefPag";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("codRefPag", codRefPag);
		deudaJudicial = (DeudaJudicial) query.uniqueResult();	

		return deudaJudicial; 
	}
		 
	public DeudaJudicial getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		DeudaJudicial deudaJudicial;
		String queryString = "from DeudaJudicial t where t.cuenta.numeroCuenta = :nroCta and t.periodo = :per";
		queryString += " and t.anio = :anio and t.sistema.nroSistema = :nroSis and t.resto = :resto";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		// 14/09/2009: Con la introduccion de GrE y cuentas alfanumericas,
		// numeroCuenta no puede ser long nunca.
		query.setString("nroCta", nroCta.toString());
		query.setLong("per", periodo);
		query.setLong("anio", anio);
		query.setLong("nroSis", nroSistema);
		query.setLong("resto", resto);
		deudaJudicial = (DeudaJudicial) query.uniqueResult();	

		return deudaJudicial; 
	}
	
	/**
	 * Obtiene anio, periodo, cantidad de deuda, suma de saldos, cantidad de constancias de deuda, 
	 * agrupada por anio y periodo,
	 * para el envio judicial
	 * @param  procesoMasivo
	 * @return List<Object[]>
	 */
	public List<Object[]> totalesPorAnioPeriodo(ProcesoMasivo procesoMasivo){

		log.debug("totalesPorAnioPeriodo");
			
		String queryString = "SELECT dj.anio, dj.periodo, COUNT(dj), SUM(dj.saldo), COUNT(cdd.constanciaDeu) " +
			"FROM DeudaJudicial dj, ConDeuDet cdd " +
			"WHERE dj.id = cdd.idDeuda " +
			"AND cdd.constanciaDeu.procesoMasivo = :procesoMasivo " +
			"GROUP BY dj.anio, dj.periodo ";

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("procesoMasivo", procesoMasivo);
			
		return (ArrayList<Object[]>) query.list();	
	}
	
	/**
	 * Busca deudas por numero de cuenta y id de procurador
	 * @param nroCuenta Si es nulo o menor a 0 no se tiene en cuenta  
	 * @param idProcurador Si es nulo o menor a  0 no se tiene en cuenta
	 * @return
	 */
	public List<DeudaJudicial> getByNroCtaYProcurador(String numeroCuenta, Long idProcurador){
		String funcName = DemodaUtil.currentMethodName();
		
		String queryString = "from DeudaJudicial t ";
	    boolean flagAnd = false;
	
		// filtro por nroCuenta
 		if (!StringUtil.isNullOrEmpty(numeroCuenta)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta)+ "'";
			flagAnd = true;
		}

		// filtro por Procurador
 		if (idProcurador!=null && idProcurador>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.procurador.id =" + idProcurador;
			flagAnd = true;
		}
 		
		// Order By Periodo/Año
		queryString += " order by t.anio, t.periodo ";
 
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    List<DeudaJudicial> listResult = (ArrayList<DeudaJudicial>) query.list();
		
		return listResult;
	}
	
	/**
	 * Busca deudas judiciales sin constancia por id de Procurador, id de Recurso , Numero de cuenta.
	 * @param idProcurador Si es nulo o menor a  0 no se tiene en cuenta
 	 * @param idRecurso Si es nulo o menor a  0 no se tiene en cuenta
	 * @param nroCuenta Si es nulo o menor a 0 no se tiene en cuenta  
	 * @return
	 */
	
	public List<DeudaJudicial> getDeuJudSinConstanciaByProcuradorYRecusoYNroCta(Long idProcurador, Long idRecurso, String numeroCuenta) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DeudaJudicial deuJud ";
	    boolean flagAnd = false;
	

	    // filtro por Procurador
 		if (idProcurador!=null && idProcurador>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " deuJud.procurador.id = " + idProcurador;
			flagAnd = true;
		}
	    
		// filtro por Recurso
 		if (idRecurso!=null && idRecurso>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " deuJud.cuenta.recurso.id = " + idRecurso;
			flagAnd = true;
		}
	    
		// filtro por nroCuenta
 		if (!StringUtil.isNullOrEmpty(numeroCuenta)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " deuJud.cuenta.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
			flagAnd = true;
		}

 		// Filtro de Deuda Sin Constancia - Puede ser que existan deudas en ConDeuDet pero inactivas
 		String subSelect= "select conDeuDet.idDeuda from ConDeuDet as conDeuDet WHERE conDeuDet.estado="+
 																					Estado.ACTIVO.getId();
 		queryString += " and deuJud.id not in ( "+subSelect+")";
 		
 		// Filtro de Activas 
 		//queryString += " and conDeuDet.estado = " + Estado.ACTIVO.getId();
 		
 		// Order By Periodo/Año
		queryString += " order by deuJud.anio, deuJud.periodo ";
 		
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    List<DeudaJudicial> listResult = (ArrayList<DeudaJudicial>) query.list();
		
		return listResult;
		
		
		
	}

	/**
	 * Obtiene deudas judiciales asignada al idProcurador pasado como parametro, que no esten en ninguna constancia de deuda y que tampoco esten en ninguna Gestion Judicial
	 * @param idProcurador - si es nulo no se tiene en cuenta (trae todas las deudas)
	 * @param idCuenta - si es nulo no se tiene en cuenta (trae todas las deudas)
	 * @param estadoActivo - Si es True se traen solo las activas
	 * @return
	 * @throws Exception
	 */
	public List<DeudaJudicial> getSinConstanciaYSinGesJud(Long idProcurador, Long idCuenta, boolean estadoActivo) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DeudaJudicial deuJud ";
	    boolean flagAnd = false;
	

	    // filtro por Procurador
 		if (idProcurador!=null && idProcurador>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " deuJud.procurador.id = " + idProcurador;
			flagAnd = true;
		}

	    // filtro por cuenta
 		if (idProcurador!=null && idProcurador>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " deuJud.cuenta.id = " + idCuenta;
			flagAnd = true;
		}
 		
 		// Filtro de Activas
 		if(estadoActivo)
 			queryString += " and deuJud.estado = " + Estado.ACTIVO.getId();
 		
 		// Filtro de Deuda Sin Constancia
 		queryString += " and deuJud.id not in ( select conDeuDet.idDeuda from ConDeuDet as conDeuDet )";
 		
 		// Filtro de Deuda sin gestiones judiciales
 		queryString += " and deuJud.id not in ( select gesJudDeu.idDeuda from GesJudDeu as gesJudDeu )";
 		
 		
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    List<DeudaJudicial> listResult = (ArrayList<DeudaJudicial>) query.list();
		
		return listResult;
	}
	
	public int deleteListDeudaJudByDevolucionDeuda (DevolucionDeuda devolucionDeuda){

		String queryString = "DELETE FROM DeudaJudicial dj WHERE dj IN (" +
			"SELECT idDeuda FROM DevDeuDet ddd WHERE ddd.devolucionDeuda = :devolucionDeuda) ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		query.setEntity("devolucionDeuda", devolucionDeuda);
	    
		return query.executeUpdate();
	}
	
	public List<DeudaJudicial>getListDeudaJudSinConvenio (Cuenta cuenta, Procurador procurador){
		Session session = SiatHibernateUtil.currentSession();
		
		EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_JUDICIAL);
		
		String sQuery = "FROM DeudaJudicial d " +
						"WHERE d.cuenta = :cuenta AND " +
						" d.estadoDeuda = :estadoDeuda AND " +
						" d.procurador = :procurador AND " +
						" d.idProcedimientoCyQ is null AND " +
						" d.reclamada = 0 AND " +
						" d.convenio.id is null " + 
						" ORDER BY d.anio, d.periodo";

		Query query = session.createQuery(sQuery)
						.setEntity("cuenta", cuenta)
						.setEntity("estadoDeuda", estadoDeuda )
						.setEntity("procurador", procurador);
		
		return query.list();	
	}

	
	
	 /**
	  * Devuelve una lista de deuda vencida a la fechaVencimiento data para una cuenta y recurso.  
	  * 
	  * 
	  * @author Cristian
	  * @param idCuenta
	  * @param idRecurso
	  * @param fechaVencimiento
	  * @return
	  * @throws Exception
	  */
	 public List<DeudaJudicial> getListDeudaVencidaByIdCuentaIdRecursoFecha(Long idCuenta, Long idRecurso, 
			 Date fechaVencimiento) throws Exception {
		 
	 	String queryString = "SELECT deuda.* " + 
			"FROM gde_deudaJudicial deuda "+
			"WHERE deuda.idCuenta = " + idCuenta + " AND " +
			"deuda.idRecurso = " + idRecurso + " AND " +
			"deuda.idEstadoDeuda = " + EstadoDeuda.ID_JUDICIAL + " AND " +
			"deuda.idViaDeuda = " + ViaDeuda.ID_VIA_JUDICIAL + " AND " +
			"deuda.fechaVencimiento < TO_DATE('" + DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') " +
			"ORDER BY idCuenta, anio, periodo ";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString).addEntity("deuda", DeudaJudicial.class);
		
		return (ArrayList<DeudaJudicial>) query.list(); 
	 }

		/**
		 * Obtiene la lista de DeudaJudicial de manera paginada para la seleccion almacenada y 
		 * el tipo de Detalle de Seleccion Almanacenada
		 * @param selAlm
		 * @param firstResult
		 * @param maxResults
		 * @return List<DeudaJudicial>
		 */
		public List<DeudaJudicial> getListBySelAlm(SelAlmDeuda selAlmDeuda, Integer firstResult, Integer maxResults) {
			String queryString = "SELECT SKIP %s FIRST %s dj.* " +
				"FROM gde_deudaJudicial dj, gde_selAlmDet sad " +
				"WHERE sad.idSelAlm = %s " +
				"AND sad.idTipoSelAlmDet = %s " +
				"AND dj.id = sad.idElemento";
			
			queryString = String.format(queryString, firstResult, maxResults, selAlmDeuda.getId(), TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD);
			Session session = currentSession();

			// obtenemos el resultado de la consulta
			Query query = session.createSQLQuery(queryString).addEntity(DeudaJudicial.class);

			return (ArrayList<DeudaJudicial>) query.list();
		}

		
		public String getSQLBySearchPage(DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws Exception {

			String queryString = " FROM gde_deudaJudicial deu " +
				"INNER JOIN pad_cuenta cta ON (deu.idcuenta == cta.id) " +
				"INNER JOIN def_recurso rec ON (deu.idrecurso == rec.id) " +
				"INNER JOIN def_recClaDeu rcd ON (deu.idreccladeu == rcd.id) ";
			
			// que no hayan sido agregadas a la selAlmDet de SelAlm 
			queryString += "LEFT JOIN gde_selalmdet sad ON (sad.idelemento == deu.id " +
				"AND sad.idselalm = " + deudaExcProMasAgregarSearchPage.getProcesoMasivo().getSelAlmExc().getId() + 
				" AND sad.idTipoSelAlmDet = " + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD + ") " +
				"WHERE sad.id IS NULL  "; 

			// filtro Recurso requerido
			queryString += " AND deu.idrecurso = " + deudaExcProMasAgregarSearchPage.getProcesoMasivo().getRecurso().getId();
			
			// debe estar en via judicial
			queryString += " AND deu.idViaDeuda = "+ ViaDeuda.ID_VIA_JUDICIAL;
			// no debe estar cancelada (pagada): no hace falta aplicar un filtro
			// no debe estar reclamada
			queryString += " AND (deu.reclamada IS NULL OR deu.reclamada <> " + SiNo.SI.getId() +") ";
			// TODO no debe estar indeterminada:
			// no debe estar prescripta ni condonada: no hace falta aplicar un filtro
			// no debe estar asociada a ningun convenio, excepto que este recompuesto
			queryString += " AND deu.idConvenio IS NULL ";
			// no debe tener saldo cero
			queryString += " AND deu.saldo <> 0 ";
			// debe estar activa
			queryString += " AND deu.estado = "+ Estado.ACTIVO.getId();

			// clasificacion deuda
			if (deudaExcProMasAgregarSearchPage.getListIdRecClaDeu() != null &&          // puede ser nula 
					deudaExcProMasAgregarSearchPage.getListIdRecClaDeu().length > 0){
				queryString += " AND deu.idRecClaDeu IN (" + StringUtil.getStringComaSeparate(deudaExcProMasAgregarSearchPage.getListIdRecClaDeu()) + ") ";
			}
			// filtro Fecha Vencimiento Desde
			if (deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde() != null ) {
				queryString += " AND deu.fechaVencimiento >= TO_DATE('" + 
				DateUtil.formatDate(deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			}
			// filtro Fecha Vencimiento Hasta
			if (deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta() != null ) {
				queryString += " AND deu.fechaVencimiento <= TO_DATE('" + 
				DateUtil.formatDate(deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			}

			// numero de cuenta
			String numeroCuenta = deudaExcProMasAgregarSearchPage.getCuenta().getNumeroCuenta();
			if (!StringUtil.isNullOrEmpty(numeroCuenta) ) {
				queryString += " AND cta.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
			}
			
			return queryString;
		}

		/**
		 * Obtiene la lista de Deudas Judiciales a partir del SearchPage
		 * @param deudaIncProMasConsPorCtaSearchPage
		 * @return List<DeudaJudicial>
		 */
		public List<DeudaJudicial> getListDeudaJudicialIncluidaBySearchPage(DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPage){

			String queryString = " FROM DeudaJudicial da, SelAlmDet sad " +
			" WHERE da.cuenta.id = " + deudaIncProMasConsPorCtaSearchPage.getCuenta().getId() +  
			" AND da.id = sad.idElemento" +
			" AND sad.selAlm.id = " + deudaIncProMasConsPorCtaSearchPage.getProcesoMasivo().getSelAlmInc().getId() +
			" AND sad.tipoSelAlmDet.id = " + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD;

			Session session = currentSession();

			Query query = session.createQuery("SELECT COUNT(da) " + queryString);
			Long cantidadMaxima = (Long) query.uniqueResult();
			deudaIncProMasConsPorCtaSearchPage.setMaxRegistros(cantidadMaxima);

			// TODO ver el order by

			// obtenemos el resultado de la consulta
			query = session.createQuery("SELECT da " + queryString);

			if (deudaIncProMasConsPorCtaSearchPage.isPaged()) {
				query.setMaxResults(deudaIncProMasConsPorCtaSearchPage.getRecsByPage().intValue());
				query.setFirstResult(deudaIncProMasConsPorCtaSearchPage.getFirstResult());
			}

			return (ArrayList<DeudaJudicial>) query.list();
		}

		/**
		 * Obtiene la cantidad y el total(campo saldo) de deudas Judiciales con FechaEnvio entre el rango de fechas ingresado
		 * @param fechaDesde
		 * @param fechaHasta
		 * @return
		 */
		public Object[] getTotalEnviadasAJudicial(Procurador procurador, Date fechaDesde, Date fechaHasta){
			String queryString = "select count(*), sum(saldo) FROM gde_deudaJudicial t ";
			boolean flagAnd = false;
			
			if(procurador!=null){
				queryString += flagAnd?" AND ":" WHERE ";
				queryString +=" t.idProcurador= "+procurador.getId();
				flagAnd = true;
			}
			
			if(fechaDesde!=null){
				queryString += flagAnd?" AND ":" WHERE ";
				queryString +=" t.fechaEnvio>= :fechaDesde";
				flagAnd = true;
			}
			
			if(fechaHasta!=null){
				queryString += flagAnd?" AND ":" WHERE ";
				queryString +=" t.fechaEnvio<= :fechaHasta";
				flagAnd = true;
			}
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createSQLQuery(queryString);			
			if(fechaDesde!=null) query.setDate("fechaDesde", fechaDesde);
			if(fechaHasta!=null) query.setDate("fechaHasta", fechaHasta);
			
			Object[] result = (Object[]) query.uniqueResult();
			
			return result;
		}
		
		/**
		 * Metodo para generar el reporte de las gestiones judiciales
		 * @param auxGesJudReport
		 * @return
		 * @throws Exception 
		 */
		public AuxGesJudReport generarPdfForGesJudReport(AuxGesJudReport auxGesJudReport) throws Exception {			
			log.debug("generarPdfForGesJudReport - enter");
			
			Cuenta cuenta = auxGesJudReport.getCuenta();
			String codTipoJuzgado = (auxGesJudReport.getTipoJuzgado()!=null?auxGesJudReport.getTipoJuzgado().getCodTipoJuzgado():null);
			
			String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);						
			String idCorrida = AdpRun.currentRun().getId().toString();
			String fileName = idCorrida+"_ReporteGesJud_"+ auxGesJudReport.getUserId();
			
			PlanillaVO planilla = new PlanillaVO();
			ContenedorVO contenedorVO = new ContenedorVO("ContenedorGesJud");			
						
			// Genera la tabla de filtros de la busqueda
			contenedorVO.setTablaFiltros(generarTablaFiltro(auxGesJudReport));
			
			// Realiza la busqueda - obtiene una lista de: idGesJud - cantDeudas - totalImporteDeudas
			List<Object[]> listResult = GdeDAOFactory.getGesJudDAO().getList(auxGesJudReport.getProcurador(),
							cuenta, codTipoJuzgado, auxGesJudReport.getFechaDesde(), auxGesJudReport.getFechaHasta());
								
			// Genera la lista de Gestiones Judiciales
			TablaVO tablaGesJud = new TablaVO("Lista de Gestiones Judiciales");			
			tablaGesJud.setTitulo("Lista de Gestiones Judiciales");
					
			contenedorVO.add(tablaGesJud);			
			
			Long totalCantDeudasEncontradas = 0L;
			Double totalImpDeudasEncontradas = 0D;

			String[] eventosOpe	   = auxGesJudReport.getStrEventosOpe().split("\\"+GesJudReport.SEPARADOR_EVENTOS);
			HashMap<Long, Integer> mapContadorEventos = new HashMap<Long, Integer>();
			
			// recorre la lista de gesJud
			for(Object[] object: listResult){
				Long idGesJud = ((Integer) object[0]).longValue();
				Long cantDeudas = ((BigDecimal) object[1]).longValue();
				Double totalImpDeudasGesJud =  object[2]!=null?((BigDecimal) object[2]).doubleValue():0D;
				GesJud gesJud = GesJud.getById(idGesJud);
				
				// Verifica si la gesJud actual pasa los filtros de los eventos ingresados
				boolean validaEvento = true;
				boolean gesJudValida = false;
				if(eventosOpe.length>0 && !StringUtil.isNullOrEmpty(eventosOpe[0])){
					
					for(String strEvento: eventosOpe){
						validaEvento = true;
						String[] evSplit = strEvento.split(GesJudReport.SEPARADOR_CAMPOS_EVENTOS);
						Long idEvento = new Long(evSplit[0]);
						int idOperacion = Integer.parseInt(evSplit[1]);
						int idOptiempo =  Integer.parseInt(evSplit[2]);
						int tiempo =  Integer.parseInt(evSplit[3].equals("")?"-1":evSplit[3]);
						int unidad =  Integer.parseInt(evSplit[4]);
											
						GesJudEvento gesJudEvento = gesJud.getGesJudEvento(idEvento);
						
						if(idOperacion>0){
							// Valida la operacion seleccionada para el evento
							validaEvento = validarOperacionEvento(gesJud, idOperacion, gesJudEvento);
							
							log.debug("antes devalidar tiempos -gesjud:"+gesJud.getDesGesJud()+"              validaEvento:"+validaEvento);
							if(validaEvento && (tiempo>0) && (gesJudEvento!=null)){
								// valida los tiempos que se ingresaron en los campos de texto para cada evento y las operaciones seleccionadas
								
								Date fechaHoy = new Date();
								Date fechaEvento = gesJudEvento.getFechaEvento();
								
								double difEntreFechas = 0;						
								if(unidad==0){// Se calculan los dias
									difEntreFechas = DateUtil.getDifEntreFechas(fechaEvento, fechaHoy, Calendar.DAY_OF_YEAR);
								}else if(unidad==1)// Se calculan los meses
									difEntreFechas= DateUtil.getDifEntreFechas(fechaEvento, fechaHoy, Calendar.MONTH);
								else// Se calculan los anios
									difEntreFechas= DateUtil.getDifEntreFechas(fechaEvento, fechaHoy, Calendar.YEAR);
								
								if(idOptiempo==0)// mayor a
									validaEvento = (difEntreFechas>tiempo);
								
								if(idOptiempo==1)// menor a
									validaEvento = (difEntreFechas<tiempo);
								
								if(idOptiempo==2)// igual a
									validaEvento = (difEntreFechas==tiempo);	
								
								log.debug("Valido la diferencia de tiempo:");
								log.debug("Evento:"+gesJudEvento.getEvento().getDescripcion());							
								log.debug("fecha Hoy:"+fechaHoy);
								log.debug("Fecha evento:"+fechaEvento);
								log.debug("unidad:"+unidad+"      optiempo:"+idOptiempo+"    "+"tiempo:"+tiempo+
										"       difEntreFechas:"+difEntreFechas);
								log.debug("validaEvento:"+validaEvento);
							}
							
							if(validaEvento)
									gesJudValida = true;							
						}
					}
				}else{// No se selecciono ningun evento => la gesJud es valida
					gesJudValida = true;
				}
				// fin validacion de eventos
				log.debug("Despues de realizar validaciones - gesJudValida:"+gesJudValida);
				if(gesJudValida){
										
					// actualiza el contador para cada evento ACTIVO que contiene la gesJud actual
					for(GesJudEvento gesJudEvento: gesJud.getListGesJudEvento()){
						if(gesJudEvento.getEvento().getEstado().equals(Estado.ACTIVO.getId())){
							Long idEvento = gesJudEvento.getEvento().getId();
							Integer cantActual;
							if(mapContadorEventos.containsKey(idEvento))
								cantActual = mapContadorEventos.get(idEvento);
							else
								cantActual = 0;
							mapContadorEventos.put(idEvento, ++cantActual);
						}
					}
					
					// Agrega la Gesjud
					FilaVO filaGesJud = new FilaVO();
					
					// Obtiene la descripcion del estado para la gestion judicial
					String desEstado ="";
					if(gesJud.getEstado().intValue()==Estado.ACTIVO.getId().intValue())
						desEstado =EstadoGesJudVO.VIGENTE;
					else if(gesJud.getEstado().intValue()==Estado.INACTIVO.getId().intValue())
						desEstado = EstadoGesJudVO.CADUCO;
					
					filaGesJud.add(new CeldaVO(gesJud.getDesGesJud()));
					filaGesJud.add(new CeldaVO(gesJud.getJuzgado()));
					filaGesJud.add(new CeldaVO(DateUtil.formatDate(gesJud.getFechaAlta(), DateUtil.ddSMMSYYYY_MASK)));
					filaGesJud.add(new CeldaVO(gesJud.getIdCaso()));
					filaGesJud.add(new CeldaVO(desEstado));
					filaGesJud.add(new CeldaVO(DateUtil.formatDate(gesJud.getFechaCaducidad(), DateUtil.ddSMMSYYYY_MASK)));
					filaGesJud.add(new CeldaVO(cantDeudas.toString()));
					filaGesJud.add(new CeldaVO("$"+StringUtil.redondearDecimales(totalImpDeudasGesJud, 1, 2)));
					
					totalCantDeudasEncontradas += cantDeudas;
					totalImpDeudasEncontradas += totalImpDeudasGesJud;
					
					TablaVO tablaGesJudTmp = new TablaVO("");					
					tablaGesJudTmp.add(filaGesJud);
					
					// calculo de controles estadisticos de cada geJud
					log.debug("Va a realizar los calculos estadisticos");
					Integer diasEntreEventos = gesJud.getDiasEntreEventos(
							Evento.COD_ENTREGA_DEUDA_FEHACIENTE,Evento.COD_PRESENTACION_DEMANDA);					
					String diasRecepDeudaIniJuicio = (diasEntreEventos!=null?
							diasEntreEventos.toString():"");
					
					String diasUltEvento = gesJud.getDiasUltEvento();
					String descripcionUltEvento = (gesJud.getUltimoEventoIngresado()!=null?
							gesJud.getUltimoEventoIngresado().getEvento().getDescripcion():"");
					String StrDiasUltEvento = (diasUltEvento!=null?
							diasUltEvento+" ("+descripcionUltEvento+")":"");
					
					diasEntreEventos = gesJud.getDiasEntreEventos(
							Evento.COD_ENTREGA_DEUDA_FEHACIENTE,Evento.COD_INTIMACION_EXTRAJUD);
					String diasDeuFehaIntExtraJud = (diasEntreEventos!=null?diasEntreEventos.toString():"");
					
					diasEntreEventos = gesJud.getDiasEntreEventos(
							Evento.COD_ENTREGA_CONST_DEUDA,Evento.COD_PRESENTACION_DEMANDA);
					String diasConstPresDemanda = (diasEntreEventos!=null?diasEntreEventos.toString():"");
					
					diasEntreEventos = gesJud.getDiasEntreEventos(
							Evento.COD_PRESENTACION_DEMANDA,Evento.COD_SENTENCIA_NRO_Y_FECHA);
					String diasInicioDemandaSentencia = (diasEntreEventos!=null?diasEntreEventos.toString():"");
					
					String diasDesdeSentencia = gesJud.getDiasDesdeEvento(Evento.COD_SENTENCIA_NRO_Y_FECHA);
					String strDiasSentencia = (diasDesdeSentencia!=null?diasDesdeSentencia:"");
					
					FilaVO filaGesJudEstad = new FilaVO();					
					filaGesJudEstad.add(new CeldaVO(diasRecepDeudaIniJuicio, "", ""));
					filaGesJudEstad.add(new CeldaVO(StrDiasUltEvento, "", ""));
					filaGesJudEstad.add(new CeldaVO(diasDeuFehaIntExtraJud, "", ""));
					filaGesJudEstad.add(new CeldaVO(diasConstPresDemanda, "", ""));
					filaGesJudEstad.add(new CeldaVO(diasInicioDemandaSentencia, "", ""));
					filaGesJudEstad.add(new CeldaVO(strDiasSentencia, "", ""));
					tablaGesJudTmp.add(filaGesJudEstad);
					
					contenedorVO.add(tablaGesJudTmp);
				}
			}
			
			// Se hace el bloque de resumen	y se agrega al contenedor
			auxGesJudReport.setTotalCantDeudasEncontradas(totalCantDeudasEncontradas);
			auxGesJudReport.setTotalImpDeudasEncontradas(totalImpDeudasEncontradas);
			
			contenedorVO.getListBloque().add(generarBloqueResumen(auxGesJudReport, mapContadorEventos));			
			
			// Generacion del PrintModel
			PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_GDE_GESGUDREPORT);	
			printModel.setTopeProfundidad(10);
			printModel.setData(contenedorVO);
			
			printModel.putCabecera("TituloReporte", "Reporte de Gestiones Judiciales");
			printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
			printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			printModel.putCabecera("Usuario", auxGesJudReport.getUserName());
								
			// Genera el PDF
			String fileNamePdf = fileName + ".pdf";			
			byte[] byteStream = printModel.getByteArray();
			FileOutputStream gesJudReportFile = new FileOutputStream(fileDir+"/"+fileNamePdf);
			gesJudReportFile.write(byteStream);
			gesJudReportFile.close();
			
			// Setea en el adapter, los datos del archivo generado
			planilla.setFileName(fileDir+"/"+fileNamePdf);
			planilla.setDescripcion("Reporte de Gestiones Judiciales");									
			auxGesJudReport.setReporteGenerado(planilla);
			
			log.debug("generarPdfForGesJudReport - exit");
			return auxGesJudReport;
		}

		
		/**
		 * Valida que la gesJud cumpla con la operacion para el evento del gesJudEvento
		 * @param gesJud
		 * @param idOperacion
		 * @param gesJudEvento
		 * @return
		 */
		private boolean validarOperacionEvento(GesJud gesJud, int idOperacion, GesJudEvento gesJudEvento){
			if(idOperacion==1 && gesJudEvento==null){
				// operacion es "que exista" y el evento no existe en la gesJud
				return false;
			}
			

			if (idOperacion==2){
				GesJudEvento ultimoEventoIngresado = gesJud.getUltimoEventoIngresado();
				if(ultimoEventoIngresado==null || gesJudEvento==null ||
					!ultimoEventoIngresado.getEvento().getId().equals(gesJudEvento.getEvento().getId())){
					// operacion es "que exista y sea el ultimo ingresado" y no lo es o no lo tiene
					return false;
				}							
			}
			
			if (idOperacion==3 && gesJudEvento!=null){
				// operacion es "que NO exista" y la gesJud lo contiene
				return false;
			}
			return true;
		}
		
		/**
		 * Arma la tabla cabecera con los filtros de busqueda, para el reporte de Gestiones Judiciales
		 * @param auxGesJudReport
		 * @return
		 */
		private TablaVO generarTablaFiltro(AuxGesJudReport auxGesJudReport){
			
			String desRecurso 	   = auxGesJudReport.getDesRecurso();
			String desProcurador   = auxGesJudReport.getProcurador()!=null?auxGesJudReport.getProcurador().getDescripcion():null;
			String nroCuenta	   = auxGesJudReport.getCuenta()!=null?auxGesJudReport.getCuenta().getNumeroCuenta():null;
			String desTipoJuzgado = (auxGesJudReport.getTipoJuzgado()!=null?auxGesJudReport.getTipoJuzgado().getDesTipoJuzgado():null);			
			String fechaDesdeView  = auxGesJudReport.getFechaDesdeView();
			String fechaHastaView  = auxGesJudReport.getFechaHastaView();
			String[] eventosOpe	   = auxGesJudReport.getStrEventosOpe().split("\\"+GesJudReport.SEPARADOR_EVENTOS);
						
			FilaVO filaDeCabecera = new FilaVO();
			
			filaDeCabecera.add(new CeldaVO(desRecurso, "Recurso", "Recurso"));
			filaDeCabecera.add(new CeldaVO(desProcurador, "Procurador", "Procurador"));
			if(!StringUtil.isNullOrEmpty(nroCuenta))
				filaDeCabecera.add(new CeldaVO(nroCuenta, "Cuenta", "Cuenta"));
			if(!StringUtil.isNullOrEmpty(desTipoJuzgado))
				filaDeCabecera.add(new CeldaVO(desTipoJuzgado, "Tipo Juzgado", "Tipo Juzgado"));			
			if(!StringUtil.isNullOrEmpty(fechaDesdeView))
				filaDeCabecera.add(new CeldaVO(fechaDesdeView,"Fecha Desde","Fecha Desde"));
			if(!StringUtil.isNullOrEmpty(fechaHastaView))
				filaDeCabecera.add(new CeldaVO(fechaHastaView,"Fecha Hasta","Fecha Hasta"));
						
			if(eventosOpe.length>0 && !StringUtil.isNullOrEmpty(eventosOpe[0])){
				filaDeCabecera.add(new CeldaVO("","Eventos","Eventos"));
			
				for(String str: eventosOpe){
					String[] evSplit = str.split(GesJudReport.SEPARADOR_CAMPOS_EVENTOS);
					
					Long idEvento = new Long(evSplit[0]);
					int idOperacion = Integer.parseInt(evSplit[1]);
					int idOptiempo =  Integer.parseInt(evSplit[2]);
					int tiempo =  Integer.parseInt(evSplit[3].equals("")?"-1":evSplit[3]);
					int unidad =  Integer.parseInt(evSplit[4]);
					
					if(idOperacion>0){
						Evento evento = Evento.getById(idEvento);
						String contenidoCelda = evento.getDescripcion();
						
						if(idOperacion==1)
							contenidoCelda+=" - Que Exista";
						else if(idOperacion==2)
							contenidoCelda+=" - Que Exista y sea el último";
						else if(idOperacion==3)
							contenidoCelda+=" - Que NO Exista";
						
						if(tiempo>0){
							if(idOptiempo==0)
								contenidoCelda+=" - con un tiempo mayor a ";
							else if(idOptiempo==1)
								contenidoCelda+=" - con un tiempo menor a ";
							else if(idOptiempo==2)
								contenidoCelda+=" - con un tiempo igual a ";				
							
							contenidoCelda += tiempo;
							
							if(unidad==0)
								contenidoCelda+=" (Dia)";
							else if(unidad==1)
								contenidoCelda+=" (Mes)";
							else if(unidad==2)
								contenidoCelda+=" (Año)";
						}
						
						filaDeCabecera.add(new CeldaVO(contenidoCelda, "Evento", idEvento.toString()));
					}
				}
			}
			
			TablaVO tablaFiltros = new TablaVO("Filtros Aplicados");
			tablaFiltros.add(filaDeCabecera);
			tablaFiltros.setTitulo("filtros de Búsqueda");
			
			return tablaFiltros;
		}
		
		
		/**
		 * Genera el bloque de resumen para el reporte de Gestiones Judiciales
		 * @param totalCantDeudas
		 * @param totalImpDeudas
		 * @return
		 */
		private ContenedorVO generarBloqueResumen(AuxGesJudReport auxGesJudReport, HashMap<Long, Integer> mapContadorEventos){
			TablaVO tablaResumen = new TablaVO("Resumen");
			tablaResumen.setTitulo("Resumen Gestiones Judiciales");			
			FilaVO filaResumen1 = new FilaVO();
			
			// total de la busqueda
			Long totalCantDeudasEncontradas = auxGesJudReport.getTotalCantDeudasEncontradas();
			Double totalImpDeudasEncontradas = auxGesJudReport.getTotalImpDeudasEncontradas();
			filaResumen1.add(new CeldaVO(totalCantDeudasEncontradas.toString(), "totalCantDeudas", "Cantidad Deudas en Juicio"));
			filaResumen1.add(new CeldaVO("$"+StringUtil.redondearDecimales(totalImpDeudasEncontradas, 1,2), 
													"totalImpDeudas", "Total Importe deudas en Juicio"));

			// Total de deudas enviadas a Judicial entre el rango de fechas Ingresado
			Object[] totalDeudasEnviadas = getTotalEnviadasAJudicial(auxGesJudReport.getProcurador(), auxGesJudReport.getFechaDesde(),
																		auxGesJudReport.getFechaHasta());
			Long cantDeudasEnviadas = ((BigDecimal) totalDeudasEnviadas[0]).longValue();
			Double totalImpDeudasEnviadas =  totalDeudasEnviadas[1]!=null?((BigDecimal) totalDeudasEnviadas[1]).doubleValue():0D;

			filaResumen1.add(new CeldaVO(cantDeudasEnviadas.toString(), "totalCantDeudasEnviadas", "Cantidad Deudas enviadas a Vía judicial"));
			filaResumen1.add(new CeldaVO("$"+StringUtil.redondearDecimales(totalImpDeudasEnviadas, 1, 2),
										"totalImpDeudasEnviadas", "Total deudas enviadas a Vía judicial"));
			
			
			// Relacion porcentual entra las encontradas y las enviadas
			if(totalImpDeudasEnviadas.doubleValue()>0){				
				Double porcentaje = new Double(totalCantDeudasEncontradas)/new Double(cantDeudasEnviadas)*100D;
				filaResumen1.add(new CeldaVO(StringUtil.redondearDecimales(porcentaje, 1,2)+" %", 
						"relacionPorcentual", "Relación porcentual entre deudas enviadas a la vía judicial y deudas incluídas en juicio"));				
			}
			
			// total deudas por Evento
			if(mapContadorEventos!=null){
				for(Long idEvento: mapContadorEventos.keySet()){
					Evento evento = Evento.getById(idEvento);
					String cantGesjudEvento = String.valueOf(mapContadorEventos.get(idEvento));
					filaResumen1.add(new CeldaVO(cantGesjudEvento, evento.getDescripcion(), "Total Deuda en Juicio con "+evento.getDescripcion()));
				}
			}
			
			tablaResumen.setFilaCabecera(filaResumen1);
			ContenedorVO contenedorResumen = new ContenedorVO("");
			contenedorResumen.setTablaCabecera(tablaResumen);
			
			return contenedorResumen;
		}
		
	/**
	 *  Devuelve la deuda Administrativa incluida en un procedimiento de Concurso y Quiebra, ordenada por cuenta
	 *  
	 * @param procedimiento
	 * @return
	 */	 
	 public List<DeudaJudicial> getByProcedimientoCyq(Long idProcedimiento){
		 String funcName = DemodaUtil.currentMethodName();
		 if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
		 Session session = SiatHibernateUtil.currentSession();
		 
		String sQuery = "FROM DeudaJudicial d " +
						"WHERE d.idProcedimientoCyQ = :idProcedimiento "; // + "ORDER BY d.cuenta, d.procurador, d.anio, d.periodo";
		
		log.debug(funcName + ": Query: " + sQuery);
		
		Query query = session.createQuery(sQuery).setLong("idProcedimiento", idProcedimiento );

		return query.list();	
		 
	 }		

	 /**
	  * Devuelve una lista de deuda judicial 
	  * vencida a la fechaVencimiento  
	  *
	  * @param idCuenta
	  * @param idRecurso
	  * @param fechaVencimiento
	  * @return
	  * @throws Exception
	  */
	@SuppressWarnings("unchecked") 
	public List<DeudaJudicial> getListDeudaJudicialBy(Long idCuenta, Long idRecurso, Date fechaVencimiento) throws Exception {
		 
	 	String strQuery = ""; 
	 	strQuery += "from DeudaJudicial deuda ";
	 	strQuery +=	"where deuda.cuenta.id  = :idCuenta ";
	 	strQuery +=	  "and deuda.recurso.id = :idRecurso ";
	 	strQuery +=	  "and deuda.estadoDeuda.id = :idEstadoDeuda ";
	 	strQuery +=	  "and deuda.viaDeuda.id = :idViaDeuda ";
	 	strQuery +=	  "and deuda.fechaVencimiento < :fechaVencimiento ";
	 	strQuery +=	"order by deuda.cuenta.id, anio, periodo ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idCuenta", idCuenta)
							 .setLong("idRecurso", idRecurso)
							 .setLong("idEstadoDeuda", EstadoDeuda.ID_JUDICIAL)
							 .setLong("idViaDeuda", ViaDeuda.ID_VIA_JUDICIAL)
							 .setDate("fechaVencimiento", fechaVencimiento);
		
		return (ArrayList<DeudaJudicial>) query.list(); 
	 }

	public List<DeudaJudicial> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		List<DeudaJudicial> listDeudaJudicial;
		String queryString = "from DeudaJudicial t where t.cuenta.numeroCuenta = :nroCta and t.periodo = :per";
		queryString += " and t.anio = :anio and t.sistema.nroSistema = :nroSis and t.resto = :resto";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		// 14/09/2009: Con la introduccion de GrE y cuentas alfanumericas,
		// numeroCuenta no puede ser long nunca.
		query.setString("nroCta", nroCta.toString());
		query.setLong("per", periodo);
		query.setLong("anio", anio);
		query.setLong("nroSis", nroSistema);
		query.setLong("resto", resto);
		listDeudaJudicial = (ArrayList<DeudaJudicial>) query.list();	

		return listDeudaJudicial; 
	}

	public static void setMigId(long migId) {
		DeudaJudicialDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

	/**
	 * Obtiene un registro de deuda para Clasificacion de Deuda de Regimen Simplificado.
	 * 
	 * @author Tecso
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param nroSistema
	 * @param resto
	 * @return
	 */
	public Deuda getByCtaPerAnioSisResForRS(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		Deuda deuda;
		
		String queryString = "FROM DeudaJudicial deuda " + 
							" WHERE deuda.cuenta.numeroCuenta = :nroCta AND " +
							" deuda.sistema.nroSistema = :nroSis AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio AND" +
							" deuda.resto = :resto AND" +
							" deuda.recClaDeu.abrClaDeu = '"+RecClaDeu.ABR_RS+"'";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							.setString("nroCta", nroCta.toString())
							.setLong("nroSis", nroSistema)
							.setLong("periodo", periodo)
							.setLong("anio", anio)
							.setLong("resto", resto);
		
		deuda = (Deuda) query.uniqueResult();	

		return deuda; 
	}
	
	/**
	 * Obtiene un registro de deuda para Clasificacion de Deuda Original.
	 * 
	 * @author Tecso
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param nroSistema
	 * @param resto
	 * @return
	 */
	public Deuda getOriginalByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		Deuda deuda;
		
		String queryString = "FROM DeudaJudicial deuda " + 
							" WHERE deuda.cuenta.numeroCuenta = :nroCta AND " +
							" deuda.sistema.nroSistema = :nroSis AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio AND" +
							" deuda.resto = :resto AND" +
							" deuda.recClaDeu.esOriginal = 1";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							.setString("nroCta", nroCta.toString())
							.setLong("nroSis", nroSistema)
							.setLong("periodo", periodo)
							.setLong("anio", anio)
							.setLong("resto", resto);
		
		deuda = (Deuda) query.uniqueResult();	

		return deuda; 
	}
}
