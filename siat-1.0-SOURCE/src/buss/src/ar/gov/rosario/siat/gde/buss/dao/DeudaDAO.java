//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.buss.dao.SiatJDBCDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.gde.buss.bean.AuxDeudaProcuradorReport;
import ar.gov.rosario.siat.gde.buss.bean.AuxDistribucionReport;
import ar.gov.rosario.siat.gde.buss.bean.AuxEmisionReport;
import ar.gov.rosario.siat.gde.buss.bean.AuxRecaudacionReport;
import ar.gov.rosario.siat.gde.buss.bean.AuxRecaudadoReport;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAnulada;
import ar.gov.rosario.siat.gde.buss.bean.DeudaCancelada;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.DeudaProcuradorReportHelper;
import ar.gov.rosario.siat.gde.buss.bean.DistribucionReportHelper;
import ar.gov.rosario.siat.gde.buss.bean.EstPlaEnvDeuPr;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.FilaReporteDeudaProcurador;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.RecaudadoReportHelper;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.gde.iface.model.RecaudacionForReportVO;
import ar.gov.rosario.siat.gde.iface.model.RecaudacionReport;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TablaVO;

public class DeudaDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(DeudaDAO.class);
	private String deudaClass = this.bOClass.getSimpleName();
	
	public static String SEQUENCE_COD_REF_PAGO = "deu_codrefpag_sq";	
	
	public static String TABLA_DEUDA_ADMIN      = "gde_deudaAdmin";
	public static String TABLA_DEUDA_ANULADA    = "gde_deudaAnulada";
	public static String TABLA_DEUDA_CANCELADA  = "gde_deudaCancelada";
	public static String TABLA_DEUDA_JUDICIAL   = "gde_deudaJudicial";
	
	
	public DeudaDAO() {
		super(null);
	}
	
	public DeudaDAO(Class boClass) {
		super(boClass);
	}
	
	public Long getNextCodRefPago(){
		return super.getNextVal(SEQUENCE_COD_REF_PAGO);
	}
	
	/** Obtiene todos los registros de deuda para la cuenta pasada
	 *  como parametro, los parametros fecha desde y hasta, solo
	 *  seran tenidos en cuenta si estan presentes
	 * 
	 * @param cuenta
	 * @param fechaVtoDesde
	 * @param fechaHsta
	 * @return
	 */
	public List<Deuda> getListByCuentayVto(Cuenta cuenta, Date fechaVtoDesde, Date fechaVtoHasta) {

		Session session = SiatHibernateUtil.currentSession();
		String sQuery = "";

		// filtro la deuda para una determinada cuenta
		sQuery = "FROM " + this.deudaClass + " deuda WHERE deuda.cuenta = :cuenta " ;

		// si solo tiene fecha desde filtro la deuda a partir de esa fecha
		if (fechaVtoDesde != null) {
			sQuery = sQuery + "AND deuda.fechaVencimiento >= :fechaVtoDesde " ;
		}

		// si solo tiene fecha hasta filtro la deuda hasta esa fecha
		if (fechaVtoHasta != null) {
			sQuery = sQuery + "AND deuda.fechaVencimiento <= :fechaVtoHasta " ;
		}

		sQuery = sQuery + "ORDER BY deuda.anio, deuda.periodo ";

    	Query query = session.createQuery(sQuery);

    	// seteo los filtros
    	query.setEntity("cuenta", cuenta);
    	if (fechaVtoDesde != null) {
    		query.setDate("fechaVtoDesde", fechaVtoDesde);
    	}
    	if (fechaVtoHasta != null) {
    		query.setDate("fechaVtoHasta", fechaVtoHasta);
    	}

    	return (ArrayList<Deuda>)query.list();		
	}

	
	/**
	 * Devuelve true si se encuentran de registros de deuda que cumplan todos los filtros que recibe. 
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @param listIdsAExcluir
	 * @param idTipoDeudaPlan
	 * @param listRecClaDeuPlan
	 * @param fechaVenDeuDes
	 * @param fechaVenDeuHas
	 * @return
	 */
	public List<Deuda> existeDeudaNotIn(Cuenta cuenta, Long[] listIdsAExcluir,  
			Long[] listIdsRecClaDeuExcluir, Date fechaVenDeuDes, Date fechaVenDeuHas, Long idViaDeudaSelected) {

		Session session = SiatHibernateUtil.currentSession();
		String sQuery = "";

		// filtro la deuda para una determinada cuenta
		sQuery = "FROM " + this.deudaClass + " deuda WHERE deuda.cuenta = :cuenta " ;
		
		sQuery += "AND deuda.fechaVencimiento >= :fechaVenDeuDes " ;
		sQuery += "AND deuda.fechaVencimiento <= :fechaVenDeuHas " ;
		sQuery += "AND deuda.id NOT IN (" + StringUtil.getStringComaSeparate(listIdsAExcluir) + ") "; 
		
		// Si existe RecClaDeu a excluir en la consulta
		if (listIdsRecClaDeuExcluir != null && listIdsRecClaDeuExcluir.length > 0) {
			sQuery += "AND deuda.recClaDeu.id NOT IN ("+ StringUtil.getStringComaSeparate(listIdsRecClaDeuExcluir) +")";
		}
		
		// Solo para la via de deuda seleccionada
		sQuery += "AND deuda.viaDeuda.id = :idViaDeudaSelected ";
		
		sQuery += "AND deuda.fechaPago IS NULL ";
			
    	Query query = session.createQuery(sQuery);

    	// seteo los filtros
    	query.setEntity("cuenta", cuenta);
    	query.setDate("fechaVenDeuDes", fechaVenDeuDes);
    	query.setDate("fechaVenDeuHas", fechaVenDeuHas);
    	query.setLong("idViaDeudaSelected", idViaDeudaSelected);
    	
    	log.debug("DeudaDAO.existeDeudaNotIn() : " +  sQuery);

    	List<Deuda> listDeuda = (ArrayList<Deuda>)query.list(); 
    	
    	log.debug("DeudaDAO " + listDeuda.size() + " Registros de deuda encontrados.");
    	
    	return listDeuda;
	}		

	public List<Deuda> getEstadoCuentaBySearchPage(EstadoCuentaSearchPage searchPage){
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "";

		// filtro la deuda para una determinada cuenta/
		queryString = "FROM " + this.deudaClass + " deuda " ;
		boolean flagAnd = false;
		try{	
	    	// Filtro por cuenta
	    	if(searchPage.getCuenta()!=null && searchPage.getCuenta().getId()!=null && searchPage.getCuenta().getId()>0){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" deuda.cuenta.id="+searchPage.getCuenta().getId();
	    		flagAnd = true;    		
	    	}
	    	
	    	//Filtro por fechaEmisionDesde
	    	if(searchPage.getFechaEmisionDesde()!=null){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" deuda.fechaEmision >=TO_DATE('"+
												DateUtil.formatDate(searchPage.getFechaEmisionDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
	    		flagAnd = true;    		
	    	}
	    	
	    	//Filtro por fechaEmisionHasta
	    	if(searchPage.getFechaEmisionHasta()!=null){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" deuda.fechaEmision <=TO_DATE('"+
												DateUtil.formatDate(searchPage.getFechaEmisionHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
	    		flagAnd = true;    		
	    	}
	    	
	    	//Filtro por fechaVtoDesde
	    	if(searchPage.getFechaVtoDesde()!=null){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" deuda.fechaVencimiento >=TO_DATE('"+
												DateUtil.formatDate(searchPage.getFechaVtoDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
	    		flagAnd = true;    		
	    	}
	    	
	    	//Filtro por fechaVtoHasta
	    	if(searchPage.getFechaVtoHasta()!=null){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" deuda.fechaVencimiento <=TO_DATE('"+
												DateUtil.formatDate(searchPage.getFechaVtoHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
	    		flagAnd = true;    		
	    	}
	    	
	    	//Filtro por Via
	    	if(searchPage.getViaDeuda()!=null && searchPage.getViaDeuda().getId()!=null && searchPage.getViaDeuda().getId()>0){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" deuda.viaDeuda.id ="+searchPage.getViaDeuda().getId() ;
	    		flagAnd = true;    		
	    	}
	
	    	//Filtro por Clasificacion
	    	if(searchPage.getRecClaDeu()!=null && searchPage.getRecClaDeu().getId()>0){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" deuda.recClaDeu.id="+searchPage.getRecClaDeu().getId();
	    		flagAnd = true;    		
	    	}
	    	
	    	// filtro por estado
	    	if(!ModelUtil.isNullOrEmpty(searchPage.getEstadoDeuda())){
	    		queryString += flagAnd ? " and " : " where ";
	    		queryString +=" deuda.estadoDeuda.id="+searchPage.getEstadoDeuda().getId();
	    		flagAnd = true;	    		
	    	}
	    	
	    	log.debug("QUERY STRING ADMIN= "+queryString);
	    	Query query = session.createQuery(queryString);	    	
	    	List<Deuda> listResult = (ArrayList<Deuda>)query.list();
	    	return listResult; 
		}catch(Exception e){
			log.error(e);			
		}
		return null;
	}
	
	/**
	 * Cambia el estado de una Deuda y mueve su registro a la tabla correspondiente
	 * No aplica para traspaso de Deuda Judicial a Administrativa o visceversa, se debe setear procurador, etc.
	 * 
	 * @param deuda
	 * @param estadoOrigen
	 * @param estadoDestino
	 * @throws Exception
	 */
	public void moverDeudaDeEstado (Deuda deuda, EstadoDeuda estadoOrigen, EstadoDeuda estadoDestino) throws Exception {
		
		String tablaOrigen="";
		String tablaDestino ="";
		String tablaConOrigen="";
		String tablaConDestino="";
		
		String camposComunesDeuda = "id, " +
									"codrefpag, " +
									"idcuenta, " +
									"idreccladeu, " +
									"idviadeuda, " +
									"idestadodeuda, " +
									"periodo, " +
									"anio, " +
									"fechaemision, " +
									"fechavencimiento, " +
									"importebruto, " +
									"importe, " +
									"saldo, " +
									"actualizacion, " +
									"strconceptosprop, " +
									"strestadodeuda, " +
									"estaimpresa, " +
									"idrepartidor, " +
									"idprocurador, " +
									"fechapago, " +
									"idprocedimientocyq, " +
									"actualizacioncyq, " +
									"idemision, " +
									"obsmotnopre, " +
									"reclamada, " +
									"idsistema, " +
									"resto, " +
									"idconvenio, " +
									"usuario, " +
									"fechaultmdf, " +
									"estado," +
									"idRecurso," +
									"atrAseVal" ;
		
		
		String camposComunesDeudaValues = camposComunesDeuda;
	
		String camposComunesConcepto =  "iddeuda, " +
										"idreccon, " +
										"importebruto, " +
										"importe, " +
										"saldo, " +
										"usuario, " +
										"fechaultmdf, " +
										"estado ";

		// Obtengo la tabla Origen
		switch (estadoOrigen.getId().intValue()){
			case ((int)(EstadoDeuda.ID_CANCELADA)):{
				tablaOrigen = "gde_deudaCancelada";
				tablaConOrigen = "gde_deuCanRecCon";
				break;
			}
			case ((int)(EstadoDeuda.ID_ANULADA)):{
				tablaOrigen = "gde_deudaAnulada";
				tablaConOrigen="gde_deuAnuRecCon";
				break;
			}			
			case ((int)(EstadoDeuda.ID_ADMINISTRATIVA)):{
				tablaOrigen = "gde_deudaAdmin";
				tablaConOrigen="gde_deuAdmRecCon";
				break;
			}
			case ((int)(EstadoDeuda.ID_JUDICIAL)):{
				tablaOrigen="gde_deudaJudicial";
				tablaConOrigen="gde_deuJudRecCon";
				break;
			}
			case ((int)(EstadoDeuda.ID_PRESCRIPTA)):{
				tablaOrigen = "gde_deudaAnulada";
				tablaConOrigen="gde_deuAnuRecCon";
				break;
			}
		}
		
		// Obtengo la tabla Destino
		switch (estadoDestino.getId().intValue()){
			case ((int)(EstadoDeuda.ID_CANCELADA)):{
				tablaDestino = "gde_deudaCancelada";
				tablaConDestino="gde_deuCanRecCon";
				break;
			}
			case ((int)(EstadoDeuda.ID_ANULADA)):{
				tablaDestino = "gde_deudaAnulada";
				tablaConDestino="gde_deuAnuRecCon";
				
				camposComunesDeuda += ", fechaanulacion, " + 
									  "idmotanudeu, " + 
									  "observacion, "+
									  "idcaso, " +
									  "idcorrida";
				
				camposComunesDeudaValues += ", '2008-01-01' fechaanulacion, " + 
											"2 idmotanudeu, " + 
											"'' observacion, "+
											"0 idcaso, " +
											"0 idcorrida";
				break;
			}			
			case ((int)(EstadoDeuda.ID_PRESCRIPTA)):{
				tablaDestino = "gde_deudaAnulada";
				tablaConDestino="gde_deuAnuRecCon";
				
				camposComunesDeuda += ", fechaanulacion, " + 
									  "idmotanudeu, " + 
									  "observacion, "+
									  "idcaso, " +
									  "idcorrida";
				
				camposComunesDeudaValues += ", '2008-01-01' fechaanulacion, " + 
											"5 idmotanudeu, " + 
											"'' observacion, "+
											"0 idcaso, " +
											"0 idcorrida";
				break;
			}
			case ((int)(EstadoDeuda.ID_ADMINISTRATIVA)):{
				tablaDestino = "gde_deudaAdmin";
				tablaConDestino = "gde_deuAdmRecCon";
				break;
			}
			case ((int)(EstadoDeuda.ID_JUDICIAL)):{
				tablaDestino="gde_deudaJudicial";
				tablaConDestino ="gde_deuJudRecCon";
				break;
			}
		}
		
		// Armo las queries
		Long idDeuda = deuda.getId().longValue();
		String strInsertDeuda="INSERT INTO "+ tablaDestino + " (" + camposComunesDeuda + ") SELECT " + camposComunesDeudaValues +" from "+tablaOrigen + " where id = " +idDeuda;
		String strInsertConDeu = "INSERT INTO "+tablaConDestino + " (" +camposComunesConcepto+ ") SELECT "+camposComunesConcepto+" from " + tablaConOrigen + " where idDeuda= "+ idDeuda;
		String strDeleteDeuda ="DELETE from " + tablaOrigen + " where id = "+idDeuda;
		String strDeleteConDeu = "DELETE from " + tablaConOrigen + " where idDeuda = "+idDeuda;
		log.debug("strInsertDeuda: "+strInsertDeuda);
		log.debug("strInsertConDeu: "+strInsertConDeu);
		log.debug("strDeleteDeuda: "+strDeleteDeuda);
		log.debug("strDeleteConDeu: "+strDeleteConDeu);
		
		Session session = SiatHibernateUtil.currentSession();
		
		//Copia la Deuda
		Query query = session.createSQLQuery(strInsertDeuda);
		query.executeUpdate();
		log.debug("paso insert1: Copia la Deuda");

		// Copia los Conceptos
		query = session.createSQLQuery(strInsertConDeu);
		query.executeUpdate();
		log.debug("paso insert2: Copia los Conceptos");

		//Borra los Conceptos
		query = session.createSQLQuery(strDeleteConDeu);
		query.executeUpdate();
		log.debug("paso delete1: Borra los Conceptos");
		
		//Borra la Deuda
		query = session.createSQLQuery(strDeleteDeuda);
		query.executeUpdate();
		log.debug("paso delete2: Borra la Deuda");
		
		Deuda deudaMovida = Deuda.getById(idDeuda, estadoDestino.getId());
		deudaMovida.setEstadoDeuda(estadoDestino);
		GdeDAOFactory.getDeudaAnuladaDAO().update(deudaMovida);

	}
	
	/**
	 *  Obtiene una Deuda por Nro de Cuenta, Periodo, Anio, y Sistema. Busca en todas las tablas de deuda hasta que encuentra una.
	 *  Si no encuentra ninguna devuelve null.
	 *  La busqueda se hace por JDBC. (NO ES EFICIENTE) 
	 * 
	 * @param nroCta
	 * @param periodo
	 * @param anio
	 * @param nroSistema
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public Deuda getByCtaPerAnioSisUsingJDBC(Long nroCta, Long periodo, Long anio, Long nroSistema) throws Exception{
		Deuda deuda;
		//Session session = SiatHibernateUtil.currentSession();
		Connection con;
		PreparedStatement ps;
		ResultSet rs;
		con = SiatHibernateUtil.currentSession().connection();
		
		//con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
		//con.setAutoCommit(false);
		
		String queryDeudaAdmin = "select * from gde_deudaAdmin t, pad_cuenta c, bal_sistema s where t.idcuenta = c.id and t.idsistema = s.id and "; 
		queryDeudaAdmin += " c.numeroCuenta = '" + nroCta + "'";
		queryDeudaAdmin += " and t.periodo = "+periodo;
		queryDeudaAdmin += " and t.anio = "+anio;
		queryDeudaAdmin += " and s.nroSistema = "+nroSistema;
		
		String queryDeudaJudicial = "select * from gde_deudaJudicial t, pad_cuenta c, bal_sistema s where t.idcuenta = c.id and t.idsistema = s.id and "; 
		queryDeudaJudicial += " c.numeroCuenta = '" + nroCta + "'";
		queryDeudaJudicial += " and t.periodo = "+periodo;
		queryDeudaJudicial += " and t.anio = "+anio;
		queryDeudaJudicial += " and s.nroSistema = "+nroSistema;
		
		String queryDeudaCancelada = "select * from gde_deudaCancelada t, pad_cuenta c, bal_sistema s where t.idcuenta = c.id and t.idsistema = s.id and "; 
		queryDeudaCancelada += " c.numeroCuenta = '" + nroCta + "'";
		queryDeudaCancelada += " and t.periodo = "+periodo;
		queryDeudaCancelada += " and t.anio = "+anio;
		queryDeudaCancelada += " and s.nroSistema = "+nroSistema;
				
		String queryDeudaAnulada = "select * from gde_deudaAnulada t, pad_cuenta c, bal_sistema s where t.idcuenta = c.id and t.idsistema = s.id and "; 
		queryDeudaAnulada += " c.numeroCuenta = '" + nroCta + "'";
		queryDeudaAnulada += " and t.periodo = "+periodo;
		queryDeudaAnulada += " and t.anio = "+anio;
		queryDeudaAnulada += " and s.nroSistema = "+nroSistema;
		
		ps = con.prepareStatement(queryDeudaAdmin);
		rs = ps.executeQuery();
		if(!rs.next()){
			ps = con.prepareStatement(queryDeudaJudicial);
			rs = ps.executeQuery();
			if(!rs.next()){
				ps = con.prepareStatement(queryDeudaCancelada);
				rs = ps.executeQuery();
				if(!rs.next()){
					ps = con.prepareStatement(queryDeudaAnulada);
					rs = ps.executeQuery();
					if(!rs.next()){
						return null;
					}else{
						deuda = new DeudaAnulada();
					}
				
				}else{
					deuda = new DeudaCancelada();
				}
			}else{
				deuda = new DeudaJudicial();
			}
		}else{
			deuda = new DeudaAdmin();
		}
		if(deuda != null){
			deuda.setId(rs.getLong("id"));
			deuda.setCodRefPag(rs.getLong("codrefpag"));
			Cuenta cuenta = new Cuenta();
			cuenta.setId(rs.getLong("idcuenta"));
			deuda.setCuenta(cuenta);
			RecClaDeu recClaDeu = new RecClaDeu();
			recClaDeu.setId(rs.getLong("idreccladeu"));
			deuda.setRecClaDeu(recClaDeu);
			ViaDeuda viaDeuda = new ViaDeuda();
			viaDeuda.setId(rs.getLong("idviadeuda"));
			deuda.setViaDeuda(viaDeuda);
			EstadoDeuda estadoDeuda = new EstadoDeuda();
			estadoDeuda.setId(rs.getLong("idestadodeuda"));
			deuda.setEstadoDeuda(estadoDeuda);
//			ServicioBanco servicioBanco = new ServicioBanco(); se quito el servicio banco de la deuda
//			servicioBanco.setId(rs.getLong("idserviciobanco"));
//			deuda.setServicioBanco(servicioBanco); se quito el servicio banco de la deuda
			deuda.setPeriodo(rs.getLong("periodo"));
			deuda.setAnio(rs.getLong("anio"));
			if(rs.getDate("fechaemision")!=null){
				deuda.setFechaEmision(rs.getDate("fechaemision"));				
			}
			if(rs.getDate("fechavencimiento")!=null){
				deuda.setFechaVencimiento(rs.getDate("fechavencimiento"));				
			}
			deuda.setImporteBruto(rs.getDouble("importebruto"));
			deuda.setImporte(rs.getDouble("importe"));
			deuda.setSaldo(rs.getDouble("saldo"));
			deuda.setActualizacion(rs.getDouble("actualizacion"));
			deuda.setStrConceptosProp(rs.getString("strconceptosprop"));
			deuda.setStrEstadoDeuda(rs.getString("strestadodeuda"));
			deuda.setEstaImpresa(rs.getInt("estaImpresa"));
			if(rs.getLong("idrepartidor")!= 0){
				Repartidor repartidor = new Repartidor(); 
				repartidor.setId(rs.getLong("idrepartidor"));
				deuda.setRepartidor(repartidor);
			}
			if(rs.getLong("idprocurador")!= 0){
				Procurador procurador = new Procurador(); 
				procurador.setId(rs.getLong("idprocurador"));
				deuda.setProcurador(procurador);
			}
			if(rs.getDate("fechapago")!=null){
				deuda.setFechaPago(rs.getDate("fechapago"));
			}
			if(rs.getLong("idprocedimientocyq")!= 0){
				deuda.setIdProcedimientoCyQ(rs.getLong("idprocedimientocyq"));
			}
			deuda.setActualizacionCyQ(rs.getDouble("actualizacioncyq"));
			/*if(rs.getLong("idemision")!= 0){
				Emision emision = new Emision(); 
				emision.setId(rs.getLong("idemision"));
				deuda.setEmision(emision);
			}*/
			deuda.setObsMotNoPre(rs.getString("obsmotnopre"));
			deuda.setReclamada(rs.getInt("reclamada"));
			if(rs.getLong("idsistema")!= 0){
				Sistema sistema = new Sistema(); 
				sistema.setId(rs.getLong("idsistema"));
				deuda.setSistema(sistema);
			}
			deuda.setResto(rs.getLong("resto"));
			if(rs.getLong("idconvenio")!= 0){
				Convenio convenio = new Convenio(); 
				convenio.setId(rs.getLong("idconvenio"));
				deuda.setConvenio(convenio);
			}
			deuda.setUsuarioUltMdf(rs.getString("usuario"));
			deuda.setFechaUltMdf(rs.getDate("fechaultmdf"));
			deuda.setEstado(rs.getInt("estado"));
		}

		return deuda; 
	}

	private String getSQLTotalEmitidoForRecaudacionReport(RecaudacionReport recaudacionReport, String tablaDeuda){
		
		String queryString = this.getSelectSQLForRecaudacionReport(recaudacionReport);
		queryString += this.getFromInnerSQLForRecaudacionReport(recaudacionReport, tablaDeuda);
		queryString += this.getWhereSQLForRecaudacionReport(recaudacionReport, tablaDeuda);
		queryString += this.getGrouBySQLForRecaudacionReport(recaudacionReport);

		if(log.isDebugEnabled()){
			log.debug("queryString: " + queryString);
		}
		
		return queryString;
	}

	
	
	private String getSelectSQLForRecaudacionReport(RecaudacionReport recaudacionReport){
		
		String queryString = "";
		
		if(recaudacionReport.getRecursoSeleccionado()){
			queryString = "SELECT deuda.anio, deuda.periodo , SUM (deuda.importe) resultado ";
		}else{
			queryString = "SELECT deuda.idrecurso, deuda.anio, deuda.periodo , SUM (deuda.importe) resultado ";
		}
		return queryString;
	}
	
	private String getFromInnerSQLForRecaudacionReport(RecaudacionReport recaudacionReport, String tablaDeuda){
		
		boolean recursoSeleccionado   = recaudacionReport.getRecursoSeleccionado();
		boolean categoriaSeleccionada = recaudacionReport.getCategoriaSeleccionada();
		
		String queryString = "FROM " + tablaDeuda + " deuda ";
		if (!recursoSeleccionado && categoriaSeleccionada){
			queryString += "INNER JOIN def_recurso rec ON (deuda.idRecurso == rec.id)";
		}
		
		return queryString;
	}
	
	private String getWhereSQLForRecaudacionReport(RecaudacionReport recaudacionReport, String tablaDeuda){
		
		boolean recursoSeleccionado       = recaudacionReport.getRecursoSeleccionado();
		boolean categoriaSeleccionada       = recaudacionReport.getCategoriaSeleccionada();
		boolean viaDeudaSeleccionada       = recaudacionReport.getViaDeudaSeleccionada();
		
		String queryString = "";
		boolean flagAnd = false;
		
		// filtro recurso
		if (recursoSeleccionado) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " deuda.idrecurso = " + recaudacionReport.getRecurso().getId();
			flagAnd = true;
		}else if (categoriaSeleccionada) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " rec.idCategoria = " + recaudacionReport.getRecurso().getCategoria().getId();
			flagAnd = true;
		}
		if (viaDeudaSeleccionada) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " deuda.idviadeuda = " + recaudacionReport.getViaDeuda().getId();
			flagAnd = true;
		}
		
		String campoFecha ="";
		if(recaudacionReport.getTipoFecha().intValue()==1){
			campoFecha = "fechaPago";
		}else if(recaudacionReport.getTipoFecha().intValue()==2){
			campoFecha = "fechaVencimiento";
		}
		
		// filtro fechaDesde
		if (recaudacionReport.getFechaDesde() != null) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " deuda."+campoFecha+" >= TO_DATE('" + DateUtil.formatDate(recaudacionReport.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		
		// filtro fechaHasta
		if (recaudacionReport.getFechaHasta() != null) {
            queryString += flagAnd ? " AND " : " WHERE ";
			queryString += " deuda."+campoFecha+" <= TO_DATE('" + DateUtil.formatDate(recaudacionReport.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		
		return queryString;
	}
	
	private String getGrouBySQLForRecaudacionReport(RecaudacionReport recaudacionReport){
		
		String queryString = ""; 
		if(recaudacionReport.getRecursoSeleccionado()){
			queryString = " GROUP BY 1 ,2 ";
		}else{
			queryString = " GROUP BY 1 ,2 ,3 ";
		}
		// GROUP BY  NO LO ORDENA
		return queryString;
	}

	// para deudaAdmin y cancelada
	private String getSQLTotalRecaudadoVtoForRecaudacionReport(RecaudacionReport recaudacionReport, String tablaDeuda){
		
		String queryString = this.getSelectSQLForRecaudacionReport(recaudacionReport);
		queryString += this.getFromInnerSQLForRecaudacionReport(recaudacionReport, tablaDeuda);
		queryString += this.getWhereSQLForRecaudacionReport(recaudacionReport, tablaDeuda);
		queryString += " AND deuda.fechapago <= deuda.fechavencimiento AND deuda.saldo == 0 ";
		queryString += this.getGrouBySQLForRecaudacionReport(recaudacionReport);

		if(log.isDebugEnabled()){
			log.debug("queryString: " + queryString);
		}
		
		return queryString;
	}

	private String getSQLTotalRecaudadoGeneralForRecaudacionReport(RecaudacionReport recaudacionReport, String tablaDeuda){
		
		String queryString = this.getSelectSQLForRecaudacionReport(recaudacionReport);
		queryString += this.getFromInnerSQLForRecaudacionReport(recaudacionReport, tablaDeuda);
		queryString += " INNER JOIN gde_pagoDeuda pg ON (deuda.id == pg.idDeuda) ";
		queryString += this.getWhereSQLForRecaudacionReport(recaudacionReport, tablaDeuda);
		queryString += " AND deuda.saldo < deuda.importe ";
		queryString += this.getGrouBySQLForRecaudacionReport(recaudacionReport);

		if(log.isDebugEnabled()){
			log.debug("queryString: " + queryString);
		}
		
		return queryString;
	}
	
	private String getSQLTotalImpagoForRecaudacionReport(RecaudacionReport recaudacionReport, String tablaDeuda){
		
		String queryString = this.getSelectSQLForRecaudacionReport(recaudacionReport);
		queryString += this.getFromInnerSQLForRecaudacionReport(recaudacionReport, tablaDeuda);
		queryString += this.getWhereSQLForRecaudacionReport(recaudacionReport, tablaDeuda);
		queryString += " AND deuda.saldo > 0 ";
		queryString += this.getGrouBySQLForRecaudacionReport(recaudacionReport);

		if(log.isDebugEnabled()){
			log.debug("queryString: " + queryString);
		}
		
		return queryString;
	}

	private void ejecutarTotalEmitidoForRecaudacion(RecaudacionReport recaudacionReport, String tablaDeuda, Map< String, RecaudacionForReportVO> mapRecaudaciones){
		String sqlQuery = this.getSQLTotalEmitidoForRecaudacionReport(recaudacionReport, tablaDeuda);
		this.ejecutarQueryForRecaudacion(recaudacionReport, sqlQuery, mapRecaudaciones, RecaudacionForReportVO.EMITIDO);
	}
	
	private void ejecutarQueryForRecaudacion(RecaudacionReport recaudacionReport, String sqlQuery, Map< String, RecaudacionForReportVO> mapRecaudaciones, Integer alternativa ){
		
		Session session = SiatHibernateUtil.currentSession();
		SQLQuery query = session.createSQLQuery(sqlQuery);
		if(!recaudacionReport.getRecursoSeleccionado()){
			query.addScalar("idRecurso", Hibernate.LONG);
		}
		query.addScalar("anio", Hibernate.LONG)
		.addScalar("periodo", Hibernate.LONG)
		.addScalar("resultado", Hibernate.DOUBLE);
		
		log.debug("ejecucion de la consulta: " + sqlQuery);
		List<Object[]> listResultado = (ArrayList<Object[]>) query.list(); // idRecurso, anio, mes, sumaImporte
		log.debug("fin ejecucion de la consulta: ");

		Long idRecurso = recaudacionReport.getRecurso().getId();
		for (Object[] obj : listResultado) {
			int i = 0;
			if(!recaudacionReport.getRecursoSeleccionado()){
				idRecurso = (Long) obj[i++];
			}
			Long anio = (Long) obj[i++];
			Long periodo = (Long) obj[i++];
			Double resultado = (Double) obj[i++]; 
			log.debug(idRecurso + "-" + anio + "-" + periodo + "-" + resultado);
			RecaudacionForReportVO recaudacion = new RecaudacionForReportVO();
			// carga de datos a la recaudacion
			recaudacion.getRecurso().setId(idRecurso);
			recaudacion.setAnio(anio);
			recaudacion.setPeriodo(periodo);
			String clave = recaudacion.getClave();
			RecaudacionForReportVO r = mapRecaudaciones.get(clave);
			if (r == null){
				// no encontro la recaudacion en el mapa
				recaudacion.addTotal(resultado, alternativa);
				mapRecaudaciones.put(clave, recaudacion);
			}
			else{
				// ya existe la recaudacion en el mapa
				r.addTotal(resultado, alternativa);
			}
		}
	}

	
	private void ejecutarTotalRecaudadoVtoForRecaudacion(RecaudacionReport recaudacionReport, String tablaDeuda, Map< String, RecaudacionForReportVO> mapRecaudaciones){

		String sqlQuery = this.getSQLTotalRecaudadoVtoForRecaudacionReport(recaudacionReport, tablaDeuda);
		this.ejecutarQueryForRecaudacion(recaudacionReport,sqlQuery, mapRecaudaciones, RecaudacionForReportVO.RECAUDADO_VTO);
	}

	private void ejecutarTotalRecaudadoGeneralForRecaudacion(RecaudacionReport recaudacionReport, String tablaDeuda, Map< String, RecaudacionForReportVO> mapRecaudaciones){
		
		String sqlQuery = this.getSQLTotalRecaudadoGeneralForRecaudacionReport(recaudacionReport, tablaDeuda);
		this.ejecutarQueryForRecaudacion(recaudacionReport, sqlQuery, mapRecaudaciones, RecaudacionForReportVO.RECAUDADO_GRAL);
	}

	private void ejecutarTotalImpagoForRecaudacion(RecaudacionReport recaudacionReport, String tablaDeuda, Map< String, RecaudacionForReportVO> mapRecaudaciones){
		
		String sqlQuery = this.getSQLTotalImpagoForRecaudacionReport(recaudacionReport, tablaDeuda);
		this.ejecutarQueryForRecaudacion(recaudacionReport, sqlQuery, mapRecaudaciones, RecaudacionForReportVO.IMPAGO);
	}

	
	public Map< String, RecaudacionForReportVO> ejecutarReporteRecaudacion(RecaudacionReport recaudacionReport){
		
		Map< String, RecaudacionForReportVO> mapRecaudaciones = new HashMap<String, RecaudacionForReportVO>();
		
		this.ejecutarTotalEmitidoForRecaudacion(recaudacionReport,mapRecaudaciones);
		this.ejecutarTotalRecaudadoVtoForRecaudacion(recaudacionReport,mapRecaudaciones);
		this.ejecutarTotalRecaudadoGeneralForRecaudacion(recaudacionReport,mapRecaudaciones);
		this.ejecutarTotalImpagoForRecaudacion(recaudacionReport,mapRecaudaciones);
		
		return mapRecaudaciones;
	} 

	/*
	private Map< String, RecaudacionForReportVO> inicializarMapRecaudaciones(RecaudacionReport recaudacionReport){
		
		Date fechaDesde = recaudacionReport.getFechaDesde();
		Date fechaHasta = recaudacionReport.getFechaHasta();
		int anioIndice = DateUtil.getAnio(fechaDesde);
		int anioHasta = DateUtil.getAnio(fechaHasta);
		int mesIndice = DateUtil.getMes(fechaDesde);
		int mesHasta  = DateUtil.getMes(fechaHasta);
		
		Map< String, RecaudacionForReportVO> mapRecaudaciones = new LinkedHashMap<String, RecaudacionForReportVO>();
		while(anioIndice <= anioHasta){
			if (anioIndice == anioHasta){
				while(mesIndice <= mesHasta){
					RecaudacionForReportVO recaudacion = new RecaudacionForReportVO();
					
					recaudacion.setAnio(anioIndice);
					recaudacion.setPeriodo(mesIndice);
					mapRecaudaciones.put(recaudacion.getClave(), recaudacion);
					mesIndice ++;
				}
				anioIndice ++; // para salir del while de anio
			}else{
				// anioIndice < anioHasta
				while(mesIndice <= 12){
					RecaudacionForReportVO recaudacion = new RecaudacionForReportVO();
					recaudacion.setAnio(anioIndice);
					recaudacion.setPeriodo(mesIndice);
					mapRecaudaciones.put(recaudacion.getClave(), recaudacion);
					mesIndice ++;
				}
				if (mesIndice > 12) {mesIndice = 1;}
				anioIndice ++;
			}
		}
		
		return mapRecaudaciones;
	}
	*/
	
	private void ejecutarTotalEmitidoForRecaudacion(RecaudacionReport recaudacionReport, Map< String, RecaudacionForReportVO> mapRecaudaciones){
	
		String tablaDeuda = DeudaDAO.TABLA_DEUDA_ADMIN;
		this.ejecutarTotalEmitidoForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
		tablaDeuda = DeudaDAO.TABLA_DEUDA_CANCELADA;
		this.ejecutarTotalEmitidoForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
		tablaDeuda = DeudaDAO.TABLA_DEUDA_JUDICIAL;
		this.ejecutarTotalEmitidoForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
	}
	
	private void ejecutarTotalRecaudadoVtoForRecaudacion(RecaudacionReport recaudacionReport, Map< String, RecaudacionForReportVO> mapRecaudaciones){
		
		String tablaDeuda = DeudaDAO.TABLA_DEUDA_ADMIN;
		this.ejecutarTotalRecaudadoVtoForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
		tablaDeuda = DeudaDAO.TABLA_DEUDA_CANCELADA;
		this.ejecutarTotalRecaudadoVtoForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
	}
	

	private void ejecutarTotalRecaudadoGeneralForRecaudacion(RecaudacionReport recaudacionReport, Map< String, RecaudacionForReportVO> mapRecaudaciones){
		
		String tablaDeuda = DeudaDAO.TABLA_DEUDA_ADMIN;
		this.ejecutarTotalRecaudadoGeneralForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
		tablaDeuda = DeudaDAO.TABLA_DEUDA_CANCELADA;
		this.ejecutarTotalRecaudadoGeneralForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
		tablaDeuda = DeudaDAO.TABLA_DEUDA_JUDICIAL;
		this.ejecutarTotalRecaudadoGeneralForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
	}


	private void ejecutarTotalImpagoForRecaudacion(RecaudacionReport recaudacionReport, Map< String, RecaudacionForReportVO> mapRecaudaciones){
		
		String tablaDeuda = DeudaDAO.TABLA_DEUDA_ADMIN;
		this.ejecutarTotalImpagoForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
		tablaDeuda = DeudaDAO.TABLA_DEUDA_CANCELADA;
		this.ejecutarTotalImpagoForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
		tablaDeuda = DeudaDAO.TABLA_DEUDA_JUDICIAL;
		this.ejecutarTotalImpagoForRecaudacion(recaudacionReport, tablaDeuda, mapRecaudaciones);
	}

	
	public AuxRecaudacionReport generarPdfForReport(AuxRecaudacionReport auxRecaudacionReport) throws Exception{
		
		// A partir de la implementacion de Adp en la generacion de Reporte se tuvo que utilizar una clase auxiliar
		// para mantener el codigo de generacion existente. Entonces en este punto se crea un recaudacionReport y
		// se pasan los datos necesarios.
		RecaudacionReport recaudacionReport = new RecaudacionReport();
		if(auxRecaudacionReport.getRecursoSeleccionado()){
			Recurso recurso = auxRecaudacionReport.getRecurso();
			recaudacionReport.setRecurso((RecursoVO) recurso.toVO(1,false));
		}else if(auxRecaudacionReport.getCategoriaSeleccionada()){
			Categoria categoria = auxRecaudacionReport.getRecurso().getCategoria();
			recaudacionReport.getRecurso().setCategoria((CategoriaVO) categoria.toVO(0));
			//recaudacionReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: auxRecaudacionReport.getListRecurso()){				
				recaudacionReport.getListRecurso().add(item.toVOWithCategoria());							
			}
		}
		if(auxRecaudacionReport.getViaDeudaSeleccionada()){
			ViaDeuda viaDeuda = auxRecaudacionReport.getViaDeuda();
			recaudacionReport.setViaDeuda((ViaDeudaVO) viaDeuda.toVO(0));				
		}
		recaudacionReport.setFechaDesde(auxRecaudacionReport.getFechaDesde());
		recaudacionReport.setFechaHasta(auxRecaudacionReport.getFechaHasta());
		recaudacionReport.setTipoFecha(auxRecaudacionReport.getTipoFecha());
		
		// en este punto se termino de armar el RecaudacionReport que antes se pasaba como parametro. 
		
		PlanillaVO reporte = new PlanillaVO();
		
		// selecciono un recurso o una lista de recurso
		boolean recursoSeleccionado = recaudacionReport.getRecursoSeleccionado();
		
		// formateador de decimales para las sumas
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		String idUsuario = auxRecaudacionReport.getUserId();
		
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
		
		//	Datos Encabezado:
		String categoria  = recaudacionReport.getRecurso().getCategoria().getDesCategoria();
		String recurso    = recaudacionReport.getRecurso().getDesRecurso();
		String viaDeuda   = recaudacionReport.getViaDeuda().getDesViaDeuda();
		
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileNameXML = idCorrida+"ReporteRecaudacion"+ idUsuario +".xml"; 
		OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(fileDir+"/"+fileNameXML), "ISO-8859-1");
		
		// Armado del PDF.
		// El Formulario fabrica el Print Model adecuado segun el codigo de formulario.
		PrintModel printModel = Formulario.getPrintModelForPDF("REPORTE_GENERICO");
		
		// obtencion dummy del string xsl
		// Abrimos archivo para parsearlo
		
		/* obtencion del xsl a partir de un archivo
		StringBuffer sf = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(new FileReader("/mnt/siat/tmp/recaudacion.xsl"));
		String line = "";         
		while ((line = bufferedReader.readLine()) != null) {
			sf.append(line);
			}
		bufferedReader.close();
		printModel.setXslString(sf.toString());
		*/
		
		// Datos del Encabezado del PDF
		printModel.putCabecera("TituloReporte", "Reporte de Recaudaci\u00F3n por Recurso");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxRecaudacionReport.getUserName());//DemodaUtil.currentUserContext().getUserName());
		
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("Contenedor de Tablas");
		
		// armado de la tabla cabecera
		TablaVO tablaCabecera = new TablaVO("Filtros Aplicados");
	
		FilaVO filaDeCabecera = new FilaVO();
		
		filaDeCabecera.add(new CeldaVO(categoria,"categoria","Categor\u00EDa"));
		filaDeCabecera.add(new CeldaVO(recurso,"recurso","Recurso"));
		filaDeCabecera.add(new CeldaVO(viaDeuda,"viaDeuda","Via Deuda"));
		
		String tipoFecha = "";
		if(recaudacionReport.getTipoFecha().intValue()==1)
			tipoFecha="Fecha de Pago";
		else if(recaudacionReport.getTipoFecha().intValue()==2)
			tipoFecha="Fecha Vto.";
		filaDeCabecera.add(new CeldaVO(tipoFecha, "Tipo de Fecha", "Tipo de Fecha"));
		
		filaDeCabecera.add(new CeldaVO(recaudacionReport.getFechaDesdeView(),"fechaDesde", "Fecha Desde"));
		filaDeCabecera.add(new CeldaVO(recaudacionReport.getFechaHastaView(),"fechaHasta", "Fecha Hasta"));
		
		tablaCabecera.add(filaDeCabecera);
		
		contenedor.setTablaCabecera(tablaCabecera);
		
		Map< String, RecaudacionForReportVO> mapRecaudaciones = new HashMap<String, RecaudacionForReportVO>();
		mapRecaudaciones = this.ejecutarReporteRecaudacion(recaudacionReport);
		
		// ordenamiento de las claves
		Object[] claves =  mapRecaudaciones.keySet().toArray();
		Arrays.sort(claves);

		List<RecursoVO> listRecursosSeleccionados = new ArrayList<RecursoVO>();
		if (recursoSeleccionado){
			listRecursosSeleccionados.add(recaudacionReport.getRecurso());
		}else{
			listRecursosSeleccionados.addAll(recaudacionReport.getListRecurso());
			//listRecursosSeleccionados.remove(0);
		}

		printModel.setTopeProfundidad(4);
		
		// escritura del archivo xml que contiene los datos del reporte
		printModel.writeDataBegin(osWriter);
		osWriter.append("\n" + "<ContenedorVO>");
		osWriter.append("\n" + "<Nombre>ContenedorTablaRecaudaciones </Nombre>");
		osWriter.append("\n" + "<ListTabla>");
		
		long ctdResultados = 0; // contador de todos los resultados encontrados
		for (RecursoVO recursoVO : listRecursosSeleccionados) {
			
			// escribir cada tabla de recurso
			// Se arman una tabla para cada recurso 
			TablaVO tabla = new TablaVO("Tabla Recaudacion");
			FilaVO filaTitulo = new FilaVO();
			// titulo de cada tabla Recaudaciones por Recurso
			filaTitulo.add(new CeldaVO("none","Reporte de Recaudaciones por Recurso"));
			filaTitulo.setNombre("Listado de Recaudaciones del Recurso: " + 
					Recurso.getById(recursoVO.getId()).getDesRecurso());
			tabla.setFilaTitulo(filaTitulo);

			String strTitFec ="";
			
			if(recaudacionReport.getTipoFecha().intValue()==1)
				strTitFec="a la Fecha de Pago";
			else if(recaudacionReport.getTipoFecha().intValue()==2)
				strTitFec="al Vto.";
			
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Anio/Periodo"));
			filaCabecera.add(new CeldaVO("Total Emitido"));
			filaCabecera.add(new CeldaVO("Total Recaudado "+strTitFec));
			filaCabecera.add(new CeldaVO("Total Recaudado en Gral."));
			filaCabecera.add(new CeldaVO("Total Impago"));
			filaCabecera.add(new CeldaVO("% Recaudaci\u00F3n "+strTitFec));
			filaCabecera.add(new CeldaVO("% Recaudaci\u00F3n"));
			filaCabecera.add(new CeldaVO("% Impago"));

			tabla.setFilaCabecera(filaCabecera);
			
			contenedor.add(tabla);
			
			// Cargamos los datos en el Print Model
			printModel.setData(contenedor);		
			
			osWriter.append("\n" + "<TablaVO>");
			osWriter.append("\n" + "<Nombre>TablaRecaudaciones</Nombre>");
			printModel.writeDataObject(osWriter, filaCabecera, 1,"FilaCabecera");
			osWriter.append("\n" + "<ListFila>");

			// Contadores
			Double sumaTotalEmitido = null;
			Double sumaTotalRecaudadoVto  = null;
			Double sumaTotalRecaudadoGral = null;
			Double sumaTotalImpago = null;
			long ctdPeriodos = 0;
			 // itera la lista ordenada de claves del recurso seleccionado			
			for (String clave : this.getClavesForRecurso(recursoVO.getIdView(), claves)) {
				
				RecaudacionForReportVO r = mapRecaudaciones.get(clave);
				
				FilaVO fila = new FilaVO(); // usamos la misma instancia
				fila.add(new CeldaVO(r.getAnioPeriodo(), "anioPeriodo"));
				 
				Double totalEmitido = NumberUtil.roundNull(r.getTotalEmitido(), SiatParam.DEC_IMPORTE_VIEW);
				Double totalRecaudadoVto  = NumberUtil.roundNull(r.getTotalRecaudadoVto(), SiatParam.DEC_IMPORTE_VIEW);
				Double totalRecaudadoGral = NumberUtil.roundNull(r.getTotalRecaudadoGral(), SiatParam.DEC_IMPORTE_VIEW);
				Double totalImpago = NumberUtil.roundNull(r.getTotalImpago(), SiatParam.DEC_IMPORTE_VIEW);
				Double porcRecVto  = NumberUtil.roundNull(r.getPorcRecVto(), SiatParam.DEC_IMPORTE_VIEW);
				Double porcRecGral = NumberUtil.roundNull(r.getPorcRecGral(), SiatParam.DEC_IMPORTE_VIEW);
				Double porcImpago  = NumberUtil.roundNull(r.getPorcImpago(), SiatParam.DEC_IMPORTE_VIEW);
				
				sumaTotalEmitido = NumberUtil.addDoubles(sumaTotalEmitido, totalEmitido);
				sumaTotalRecaudadoVto= NumberUtil.addDoubles(sumaTotalRecaudadoVto, totalRecaudadoVto);
				sumaTotalRecaudadoGral= NumberUtil.addDoubles(sumaTotalRecaudadoGral, totalRecaudadoGral);
				sumaTotalImpago = NumberUtil.addDoubles(sumaTotalImpago, totalImpago);
				ctdPeriodos ++;
				
				fila.add(new CeldaVO(StringUtil.formatDouble(totalEmitido,decimalFormat),"totalEmitido"));
				fila.add(new CeldaVO(StringUtil.formatDouble(totalRecaudadoVto,decimalFormat),"totalRecaudadoVto"));
				fila.add(new CeldaVO(StringUtil.formatDouble(totalRecaudadoGral,decimalFormat),"totalRecaudadoGral"));
				fila.add(new CeldaVO(StringUtil.formatDouble(totalImpago,decimalFormat),"totalImpago"));
				fila.add(new CeldaVO(StringUtil.formatDouble(porcRecVto,decimalFormat),"porcRecVto"));
				fila.add(new CeldaVO(StringUtil.formatDouble(porcRecGral,decimalFormat),"porcRecGral"));
				fila.add(new CeldaVO(StringUtil.formatDouble(porcImpago,decimalFormat),"porcImpago"));

				// escribe en el bufferWrite el xml correspondiente a cada resultado de recaudacion			
				printModel.writeDataObject(osWriter, fila, 1);
			}
			
			ctdResultados += ctdPeriodos;

			if (reporte.hasError()){
				osWriter.close();
				reporte.passErrorMessages(auxRecaudacionReport);
				return auxRecaudacionReport;
			}
			
			List<FilaVO> listFilaPie = new ArrayList<FilaVO>();
			FilaVO filaPie = new FilaVO();
			
			filaPie.add(new CeldaVO(StringUtil.formatLong(ctdPeriodos),"ctdPeriodos"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(sumaTotalEmitido,decimalFormat),"sumaTotalEmitido"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(sumaTotalRecaudadoVto,decimalFormat),"sumaTotalRecaudadoVto"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(sumaTotalRecaudadoGral,decimalFormat),"sumaTotalRecaudadoGral"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(sumaTotalImpago,decimalFormat),"sumaTotalImpago"));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			filaPie.add(new CeldaVO(""));
			
			listFilaPie.add(filaPie);

			osWriter.append("\n" + "</ListFila>");
			
			printModel.writeDataListObject(osWriter, listFilaPie,2 ,"ListFilaPie");
			
			printModel.writeDataObject(osWriter, filaTitulo,1 ,"FilaTitulo");
			
			osWriter.append("\n" + "</TablaVO>");
		}
		
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
		String fileNamePdf = idCorrida+"ReporteRecaudacion"+ idUsuario +".pdf"; 
		File pdfFile = new File(fileDir+"/"+fileNamePdf);
		
		// generar el archivo pdf en base al xml (y al xsl) 
		printModel.fopRender(xmlTmpFile, pdfFile);
		
		reporte.setFileName(fileDir+"/"+fileNamePdf);
		reporte.setDescripcion("Reporte de Recaudaciones por Recurso");
		reporte.setCtdResultados(ctdResultados);
		
		auxRecaudacionReport.setReporteGenerado(reporte);
		
		// Eliminamos el XML
		if(xmlTmpFile.exists())
			xmlTmpFile.delete();

		return auxRecaudacionReport;
	}

	
	private List<String> getClavesForRecurso(String idRecurso, Object[] claves){

		List<String> clavesForRecurso = new ArrayList<String>();
		for (int i = 0; i < claves.length; i++) {
			String clave = (String) claves[i];
			int ind = clave.indexOf("-");
			String idRecursoClave = clave.substring(0, ind);
			if (idRecurso.equals(idRecursoClave)){
				clavesForRecurso.add(clave);
			}
		}
		return clavesForRecurso;
	}
	
	/**
	 * Devuelve la deuda cancelada para un plan de CDM. 
	 * 
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @param idPlan
	 * @return
	 */	
	public List<Deuda> getListDeudaCDMCancelada(Cuenta cuenta){
		
		Session session = SiatHibernateUtil.currentSession();
		String sQuery = "";

		EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_CANCELADA);
		
		// filtro la deuda para una determinada cuenta
		sQuery = "FROM " + this.deudaClass + " deuda " + 
				" WHERE deuda.estadoDeuda = :estadoDeuda AND " +
				" deuda.cuenta = :cuenta" ;
		
    	Query query = session.createQuery(sQuery);

    	// seteo los filtros
    	query.setEntity("estadoDeuda", estadoDeuda );
    	query.setEntity("cuenta", cuenta);
    	
    	log.debug("DeudaDAO.getListDeudaCDMCancelada() : " +  sQuery);

    	List<Deuda> listDeuda = (ArrayList<Deuda>)query.list(); 
    	
    	log.debug("DeudaDAO " + listDeuda.size() + " Registros de deuda encontrados.");
    	
    	return listDeuda;
		
	}
	
	
	/**
	 * Obtiene un registro de deuda para una cuenta, periodo y anio.
	 * 
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public Deuda getByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		Deuda deuda;
		
		String queryString = "FROM " + this.deudaClass + " deuda " + 
							" WHERE deuda.cuenta = :cuenta AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio" ;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
						.setEntity("cuenta", cuenta)
						.setLong("periodo", periodo)
						.setInteger("anio", anio);
		
		deuda = (Deuda) query.uniqueResult();	

		return deuda; 
	}
	
	public List<Deuda> getListByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
	
		String queryString = "FROM " + this.deudaClass + " deuda " + 
							" WHERE deuda.cuenta = :cuenta AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio" ;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
						.setEntity("cuenta", cuenta)
						.setLong("periodo", periodo)
						.setInteger("anio", anio);
		
		//deuda = (Deuda) query.uniqueResult();
		List<Deuda> listDeuda = (ArrayList<Deuda>)query.list(); 

		return listDeuda; 
	}
	
	
	public List<Deuda> getByCodRefPagBySearchPage(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
				
		String queryString = "from " + this.deudaClass + " t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del LiqCodRefPagSearchPage: " + liqCodRefPagSearchPage.infoString()); 
		}
	
 		// filtro por codRefPag
 		if (liqCodRefPagSearchPage.getCodRefPag() != null) {
            queryString += flagAnd ? " and " : " where ";
           	queryString += "  t.codRefPag = " + liqCodRefPagSearchPage.getCodRefPag();
 			flagAnd = true;
		}
 		
 		// filtro por importe
 		if (liqCodRefPagSearchPage.getImporte() != null) {
            queryString += flagAnd ? " and " : " where ";
           	queryString += "  t.importe = " + liqCodRefPagSearchPage.getImporte();
 			flagAnd = true;
		}

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Deuda> listDeuda = (ArrayList<Deuda>) executeCountedSearch(queryString, liqCodRefPagSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDeuda;
	}
	
	/**
	 * Dados una cuenta, periodo, anio y un lista de Clasificaciones de deuda, devuelve las deuda que cumplan las condiciones.  
	 * 
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param listRecClaDeu
	 * @return
	 */
	public List<Deuda> getListByCuentaPeriodoAnioListRecClaDeu(Cuenta cuenta, Integer periodo, Integer anio, List<Long> listIdsRecClaDeu){
	
		String queryString = "FROM " + this.deudaClass + " deuda " + 
							" WHERE deuda.cuenta = :cuenta AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio" ;
		
		if (listIdsRecClaDeu != null && listIdsRecClaDeu.size() > 0){
			queryString += " AND (";
			int i=1;
			for (Long id:listIdsRecClaDeu){
				queryString += " deuda.recClaDeu.id = " + id;				 
				if (i<listIdsRecClaDeu.size())
					queryString += " OR ";
				i++;
			}
			queryString += " )";
		}
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
						.setEntity("cuenta", cuenta)
						.setInteger("periodo", periodo)
						.setInteger("anio", anio);
		
		List<Deuda> listDeuda = (ArrayList<Deuda>)query.list(); 

		return listDeuda; 
	}
	
	
	/**
	 * Obtiene un registro de deuda al que afecta una declaracion Jurada.
	 * 
	 * 
	 * @author Tecso
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public Deuda getByCuentaPeriodoAnioParaDJ(Cuenta cuenta, Long periodo, Integer anio){
		Deuda deuda;
		
		String queryString = "FROM " + this.deudaClass + " deuda " + 
							" WHERE deuda.cuenta = :cuenta AND " +
							" deuda.recClaDeu.esOriginal = 1 AND "+
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio" ;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
						.setEntity("cuenta", cuenta)
						.setLong("periodo", periodo)
						.setInteger("anio", anio);
		
		deuda = (Deuda) query.uniqueResult();	

		return deuda; 
	}
	
	
	/**
	 * Retorna la lista de deuda con estado estadoDeuda para la cuenta pasada como parametro,
	 * con fecha de vencimiento:
	 *
	 * 	- menor o igual que fechaVencimiento si el criterio es < 0
	 *  - igual a fechaVencimiento si el criterio es = 0
	 * 	- mayor o igual que fechaVencimiento si el criterio es > 0
	 *  
	 * @param cuenta
	 * @param fechaVencimiento
	 * @param criterio
	 * @return
	 * 
	 * */

	public List<Deuda> getListDeudaByFechaVencimientoAndEstado(Cuenta cuenta, 
			Date fechaVencimiento, EstadoDeuda estadoDeuda, Integer criterio){
		
		try {
			Session session = SiatHibernateUtil.currentSession();
			String sQuery = "";
	
			if (cuenta == null || fechaVencimiento == null || criterio == null)
				return null;
			
			// filtro la deuda para una determinada cuenta
			sQuery = "FROM " + this.deudaClass + " deuda " + 
					" WHERE deuda.estadoDeuda = :estadoDeuda AND " +
					" deuda.cuenta = :cuenta AND ";
				
			if (criterio < 0)
				sQuery += " deuda.fechaVencimiento <= :fechaVencimiento ";
			
			if (criterio == 0)
				sQuery += " deuda.fechaVencimiento = :fechaVencimiento ";
				
			if (criterio > 0)
				sQuery += " deuda.fechaVencimiento >= :fechaVencimiento ";
			
	    	Query query = session.createQuery(sQuery);
	
	    	// seteo los filtros
	    	query.setEntity("estadoDeuda", estadoDeuda );
	    	query.setEntity("cuenta", cuenta);
	    	query.setDate("fechaVencimiento", fechaVencimiento);
	    	
	    	
	    	log.debug("DeudaDAO.getListDeudaCanceladaByFechaVencimiento() : " +  sQuery);
	
	    	List<Deuda> listDeuda = (ArrayList<Deuda>)query.list(); 
	    	
	    	log.debug("DeudaDAO " + listDeuda.size() + " Registros de deuda encontrados.");
	    	
	    	return listDeuda;
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * Obtiene la lista de Deuda de una seleccion almacenada de Deuda
	 * @param selAlm
	 * @return List<Deuda>
	 */
	public List<Deuda> getListDeudaBySelAlm(SelAlmDeuda selAlmDeuda) {
		
		try {
			String queryString = "SELECT deuda.* FROM gde_"+ this.deudaClass + " deuda, gde_selAlmDet sad " +
				" WHERE sad.idSelAlm = "+ selAlmDeuda.getId() +" AND deuda.id = sad.idElemento";
			
			log.debug(queryString);
			
			queryString = String.format(queryString, selAlmDeuda.getId());
			Session session = currentSession();
	
			// obtenemos el resultado de la consulta
			Query query = session.createSQLQuery(queryString).addEntity(this.bOClass);
	
			return (ArrayList<Deuda>) query.list();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Obtiene la suma de los saldos de deuda cargada en los detalles de la Seleccion Almacenda 
	 * para el tipo de Detalle de Seleccion.
	 * @param  selAlmDeuda
	 * @param  tipoSelAlmDet
	 * @return Double
	 */
	public Double getSumaSaldoDeuda(SelAlmDeuda selAlmDeuda, TipoSelAlm tipoSelAlmDet) {

		Session session = SiatHibernateUtil.currentSession();

		String queryString = " SELECT SUM ( deuda.saldo)  ";
		if (tipoSelAlmDet.getEsTipoSelAlmDetDeudaAdm()){
			queryString += "FROM DeudaAdmin deuda, "; 
		}else{
			queryString += "FROM DeudaJudicial deuda, ";
		}
		
		queryString += 	" SelAlmDet sad  WHERE " +
				"sad.selAlm = :selAlm " +
				"AND deuda.id = sad.idElemento " +
				"AND sad.tipoSelAlmDet = :tipoSelAlmDet ";

		if(log.isDebugEnabled()) log.debug("queryString: " + queryString);
		
		Query query = session.createQuery(queryString)
			.setEntity("selAlm", selAlmDeuda)
			.setEntity("tipoSelAlmDet", tipoSelAlmDet);
		
		return (Double) query.uniqueResult(); 
	}

	/**
	 * 
	 * @param deudaExcProMasAgregarSearchPage
	 * @throws Exception
	 */
	public void exportBySearchPage(DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		
		String sqlBySearchPage = GdeDAOFactory.getDeudaAdminDAO().getSQLBySearchPage(deudaExcProMasAgregarSearchPage);
		if(ViaDeuda.ID_VIA_ADMIN == deudaExcProMasAgregarSearchPage.getProcesoMasivo().getViaDeuda().getId()){
			sqlBySearchPage = GdeDAOFactory.getDeudaAdminDAO().getSQLBySearchPage(deudaExcProMasAgregarSearchPage);
		}else{
			sqlBySearchPage = GdeDAOFactory.getDeudaJudicialDAO().getSQLBySearchPage(deudaExcProMasAgregarSearchPage);
		}
		
		// no buscamos deudas con cuentas repetidas como lo hace la inclusion		
		// string de la consulta 		
		String queryString = "SELECT rec.desrecurso, cta.numerocuenta, deu.anio, deu.periodo, rcd.descladeu, " +
				"deu.fechavencimiento, deu.importe " + sqlBySearchPage;

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		//genero la planilla de texto usando el buffer
		int indiceArchivo = 0;
		
		// formacion del fileName con el directorio de procesamiento de la corrida del envio judicial 
		Long idProcesoMasivo = deudaExcProMasAgregarSearchPage.getProcesoMasivo().getId();
		String processDir = AdpRun.getRun(ProcesoMasivo.getById(idProcesoMasivo).getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
		String fileName = processDir + File.separator + DeudaExcProMasAgregarSearchPage.FILE_NAME + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
		
		PlanillaVO planillaVO = new PlanillaVO(fileName);
		deudaExcProMasAgregarSearchPage.getListResult().add(planillaVO);
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));

		// --> Creacion del Encabezado del Resultado
		// recurso
		buffer.write("Recurso");
		// cuenta
		buffer.write(", Cuenta");
		// anio
		buffer.write(", Año");
		// periodo
		buffer.write(", Periodo");
		// clasif deuda
		buffer.write(", Clasif. Deuda");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		// importe
		buffer.write(", Importe;");
		// <-- Fin Creacion del Encabezado del Resultado

		buffer.newLine();

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		// ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		boolean resultadoVacio = true;

		// --> Resultado
		long c = 0;
		if(log.isDebugEnabled()) log.debug("generando archivo de deuda excluida: " + fileName);
		while ( rs.next()) {
			resultadoVacio = false;

			// recurso
			buffer.write(rs.getString("desrecurso"));
			// cuenta
			buffer.write(", " + rs.getString("numerocuenta") );
			// anio
			buffer.write(", " + rs.getLong("anio"));
			// periodo
			buffer.write(", " + rs.getLong("periodo"));
			// clasif deuda
			buffer.write(", " + rs.getString("descladeu") );
			// fecha de vencimiento
			buffer.write(", " + DateUtil.formatDate(rs.getDate("fechavencimiento"), DateUtil.dd_MM_YYYY_MASK) );
			// importe
			buffer.write(", " + rs.getDouble("importe") );
			c++; // incremento el contador de registros
			if(c == 30000 ){ 
				buffer.close();
				planillaVO.setCtdResultados(c); 
				indiceArchivo++;
				c = 0; // reinicio el contador de registros 
				
				fileName = processDir + File.separator + DeudaExcProMasAgregarSearchPage.FILE_NAME + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
				planillaVO = new PlanillaVO(fileName);
				deudaExcProMasAgregarSearchPage.getListResult().add(planillaVO);
				buffer = new BufferedWriter(new FileWriter(fileName, false));
				if(log.isDebugEnabled()) log.debug("generando archivo de deuda excluida: " + fileName);
			}else{
				// crea una nueva linea
				buffer.newLine();
			}
		}
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio){
			// Sin resultados
			buffer.write("No se encontraron registros de Deudas para los filtros seleccionados "  );
		}		 
		// <-- Fin Resultado vacio

		planillaVO.setCtdResultados(c);
		buffer.close();

		// no tiene tope la ctd de registros resultado
		// no se GENERA ARCHIVO DE FILTROS
	}

	
	/**
	 *  Obtiene los id de Deuda que existen en ConvenioDeuda pero no tienen el id de Convenio seteado. También obtiene
	 *  el id de Convenio a setear. 
	 * 
	 * @return List<Long[]>
	 */
	public List<Object[]> getListIdDeudaIdConvenioInconsistentes() {
		
		try {
			String queryString = "select cd.idDeuda, cd.idConvenio  from gde_convenioDeuda cd, gde_deudaAdmin da, gde_convenio c where da.id = cd.idDeuda " +
					" and c.id = cd.idConvenio and da.idConvenio is null and c.idEstadoConvenio =1";
	
			Session session = currentSession();
	
			// obtenemos el resultado de la consulta
			Query query = session.createSQLQuery(queryString);
			
			return (ArrayList<Object[]>) query.list();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 *  Obtiene los id de Deuda que existen en ConvenioDeuda pero no tienen el id de Convenio seteado. También obtiene
	 *  el id de Convenio a setear. 
	 * 
	 * @return List<Long[]>
	 */
	public List<Object[]> getListIdDeudaIdConvenioInconsistentesJudicial() {
		
		try {
			String queryString = "select cd.idDeuda, cd.idConvenio  from gde_convenioDeuda cd, gde_deudaJudicial da, gde_convenio c where da.id = cd.idDeuda " +
					" and c.id = cd.idConvenio and da.idConvenio is null and c.idEstadoConvenio =1";
	
			Session session = currentSession();
	
			// obtenemos el resultado de la consulta
			Query query = session.createSQLQuery(queryString);
			
			return (ArrayList<Object[]>) query.list();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 *  Obtiene los id de Deuda Administrativa que existen en ConvenioDeuda cancelados que deben cancelarse. También obtiene
	 *  el id de ConvenioDeuda correspondiente. 
	 * 
	 * @return List<Long[]>
	 */
	public List<Object[]> getListIdDeudaIdConvenioDeudaACancelarAdmin() {
		
		try {
			String queryString = "select cd.idDeuda, cd.id from gde_convenioDeuda cd, gde_convenio c, gde_deudaAdmin d where c.id = cd.idConvenio " +
					" and d.id = cd.idDeuda and c.idEstadoConvenio = 4 and d.idEstadoDeuda in (1,5)";
	
			Session session = currentSession();
	
			// obtenemos el resultado de la consulta
			Query query = session.createSQLQuery(queryString);
			
			return (ArrayList<Object[]>) query.list();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 *  Obtiene los id de Deuda Judicial que existen en ConvenioDeuda cancelados que deben cancelarse. También obtiene
	 *  el id de ConvenioDeuda correspondiente. 
	 * 
	 * @return List<Long[]>
	 */
	public List<Object[]> getListIdDeudaIdConvenioDeudaACancelarJudicial() {
		
		try {
			String queryString = "select cd.iddeuda, cd.id from gde_conveniodeuda cd, gde_convenio c, gde_deudajudicial d where c.id = cd.idconvenio " +
					" and d.id = cd.iddeuda and c.idestadoconvenio = 4 and d.idestadodeuda in (1,5)";
	
			Session session = currentSession();
	
			// obtenemos el resultado de la consulta
			Query query = session.createSQLQuery(queryString);
			
			return (ArrayList<Object[]>) query.list();
		}
		catch (Exception e) {
			return null;
		}
	}


	/**
	 * Obtiene una lista de Deudas con importe>0 y  saldo=0, filtrando por los valores pasados como parametro
	 * @param cuenta
	 * @param listIdsRecClaDeu
	 * @param fechaPagoDes
	 * @param fechaPagoHasta
	 * @return
	 */
	public List<Deuda> GetListImporteCancelado(Cuenta cuenta, Long[] listIdsRecClaDeu,Date fechaPagoDes, 
																						Date fechaPagoHasta) {
		log.debug("GetListImporteCancelado - enter");
		Session session = SiatHibernateUtil.currentSession();
		String sQuery = "";

		// filtro la deuda para una determinada cuenta
		sQuery = "FROM " + this.deudaClass + " deuda";
		
		boolean flagAnd = false;
		
		if(cuenta!=null){
			sQuery+= flagAnd?" AND ":" WHERE ";
			sQuery+= " deuda.cuenta = :cuenta ";
			flagAnd = true;
		}
			
		if(fechaPagoDes!=null){
			sQuery+= flagAnd?" AND ":" WHERE ";
			sQuery += "deuda.fechaPago >= :fechaPagoDes " ;
			flagAnd = true;			
		}
		
		if(fechaPagoHasta!=null){
			sQuery+= flagAnd?" AND ":" WHERE ";
			sQuery += "deuda.fechaPago <= :fechaPagoHasta " ;
			flagAnd = true;
		}
		
		// Si existe RecClaDeu a excluir en la consulta
		if (listIdsRecClaDeu != null && listIdsRecClaDeu.length > 0) {
			sQuery+= flagAnd?" AND ":" WHERE ";
			sQuery += "deuda.recClaDeu.id IN ("+ StringUtil.getStringComaSeparate(listIdsRecClaDeu) +")";
			flagAnd = true;
		}
					
    	Query query = session.createQuery(sQuery);

    	// seteo los filtros
    	if(cuenta!=null)
    		query.setEntity("cuenta", cuenta);
    	if(fechaPagoDes!=null)
    		query.setDate("fechaPagoDes", fechaPagoDes);
    	if(fechaPagoHasta!=null)
    		query.setDate("fechaPagoHasta", fechaPagoHasta);
    	
    	List<Deuda> listDeuda = (ArrayList<Deuda>)query.list(); 
    	
    	log.debug("GetListImporteCancelado - exit - " + listDeuda.size() + " Registros de deuda encontrados.");
    	return listDeuda;
	}		

	// ---> Reporte de totales de emision
	public AuxEmisionReport generarPDF4Report(AuxEmisionReport auxEmisionReport) throws Exception{
		log.debug("generarPDF4Report - enter");
		
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);						
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileName = idCorrida+"_ReporteEmision_"+ auxEmisionReport.getUserId();

		PlanillaVO planilla = new PlanillaVO();
		ContenedorVO contenedorVO = new ContenedorVO("Contenedor");			

		// Genera la tabla de filtros de la busqueda
		FilaVO filaDeCabecera = new FilaVO();		
		filaDeCabecera.add(new CeldaVO(auxEmisionReport.getRecurso().getDesRecurso(), "Recurso", "Recurso"));
		filaDeCabecera.add(new CeldaVO(auxEmisionReport.getRecclaDeu()!=null?auxEmisionReport.getRecclaDeu().getDesClaDeu():"Todos", "Clasificacion", "Clasificación"));
		filaDeCabecera.add(new CeldaVO(auxEmisionReport.getFechaDesdeView(), "fechaDesde", "Fecha Desde"));
		filaDeCabecera.add(new CeldaVO(auxEmisionReport.getFechaHastaView(), "fechaHasta", "Fecha Hasta"));
		TablaVO tablaFiltros = new TablaVO("Filtros Aplicados");
		tablaFiltros.add(filaDeCabecera);
		tablaFiltros.setTitulo("Filtros de Búsqueda");
		contenedorVO.setTablaFiltros(tablaFiltros);
		
		// busca deudas administrativas
		Long idRecClaDeu = auxEmisionReport.getRecclaDeu()!=null?auxEmisionReport.getRecclaDeu().getId():null;
		List<Object[]> listResult = getTotalEmision("gde_deudaadmin", auxEmisionReport.getRecurso().getId(), 
				idRecClaDeu, auxEmisionReport.getFechaDesde(), auxEmisionReport.getFechaHasta());

		// buscar deudas judiciales
		listResult.addAll(getTotalEmision("gde_deudajudicial", auxEmisionReport.getRecurso().getId(), 
				idRecClaDeu, auxEmisionReport.getFechaDesde(), auxEmisionReport.getFechaHasta()));

		// buscar deudas canceladas
		listResult.addAll(getTotalEmision("gde_deudacancelada", auxEmisionReport.getRecurso().getId(), 
				idRecClaDeu, auxEmisionReport.getFechaDesde(), auxEmisionReport.getFechaHasta()));

		// buscar deudas anuladas o prescriptas
		listResult.addAll(getTotalEmision("gde_deudaanulada", auxEmisionReport.getRecurso().getId(), 
				idRecClaDeu, auxEmisionReport.getFechaDesde(), auxEmisionReport.getFechaHasta()));

		Long totCantCuentas = 0L;
		Double totImpDeudas = 0D;
		for(Object[] obj:listResult){
			totCantCuentas += obj[0]!=null?((BigDecimal) obj[0]).longValue():0L;
			totImpDeudas += obj[1]!=null?((BigDecimal) obj[1]).doubleValue():0D;
		}
		log.debug("Total cant. cuentas:"+totCantCuentas+"      Total imp Deudas:"+totImpDeudas);
			
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Cantidad de Deudas"));
		filaCabecera.add(new CeldaVO("Importe Total Deudas"));
		TablaVO tablacontenido = new TablaVO("Contenido");
		tablacontenido.setFilaCabecera(filaCabecera);
		
		FilaVO filaContenido = new FilaVO();
		filaContenido.add(new CeldaVO(totCantCuentas.toString()));
		filaContenido.add(new CeldaVO("$"+StringUtil.redondearDecimales(totImpDeudas, 1, 2)));		
		tablacontenido.add(filaContenido);
		
		contenedorVO.add(tablacontenido);
				
		// Generacion del PrintModel		
		PrintModel printModel = new PrintModel();
		
		printModel.setRenderer(FormatoSalida.PDF.getId());
		printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
		printModel.cargarXsl("/mnt/publico/general/reportes/pageModel.xsl", PrintModel.RENDER_PDF);
		printModel.setTopeProfundidad(5);
		printModel.setData(contenedorVO);
		
		printModel.putCabecera("TituloReporte", "Reporte de Emisión");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxEmisionReport.getUserName());
							
		// Genera el PDF
		String fileNamePdf = fileName + ".pdf";			
		byte[] byteStream = printModel.getByteArray();
		FileOutputStream gesJudReportFile = new FileOutputStream(fileDir+"/"+fileNamePdf);
		gesJudReportFile.write(byteStream);
		gesJudReportFile.close();
		
		// Setea en el adapter, los datos del archivo generado
		planilla.setFileName(fileDir+"/"+fileNamePdf);
		planilla.setDescripcion("Reporte de Emisión");									
		auxEmisionReport.setReporteGenerado(planilla);
		
		log.debug("generarPDF4Report - exit");
		return auxEmisionReport;
	}
	
	// ---> Reporte de Distribucion de Totales Emitidos
	public AuxDistribucionReport generarPDF4Report(AuxDistribucionReport auxDistribucionReport) throws Exception{
		log.debug("generarPDF4Report - enter");
		
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);						
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileName = idCorrida+"_ReporteDistribucion_"+ auxDistribucionReport.getUserId();

		PlanillaVO planilla = new PlanillaVO();
		ContenedorVO contenedorVO = new ContenedorVO("Contenedor");			

		// Genera la tabla de filtros de la busqueda
		FilaVO filaDeCabecera = new FilaVO();		
		filaDeCabecera.add(new CeldaVO(auxDistribucionReport.getRecurso().getDesRecurso(), "Recurso", "Recurso"));
		filaDeCabecera.add(new CeldaVO(auxDistribucionReport.getFechaDesdeView(), "fechaDesde", "Fecha Desde"));
		filaDeCabecera.add(new CeldaVO(auxDistribucionReport.getFechaHastaView(), "fechaHasta", "Fecha Hasta"));
		TablaVO tablaFiltros = new TablaVO("Filtros Aplicados");
		tablaFiltros.add(filaDeCabecera);
		tablaFiltros.setTitulo("Filtros de Búsqueda");
		contenedorVO.setTablaFiltros(tablaFiltros);
		
		// busca deudas administrativas
		List<Object[]> listResult = getTotalEmision("gde_deudaadmin", auxDistribucionReport.getRecurso().getId(), 
						null,auxDistribucionReport.getFechaDesde(), auxDistribucionReport.getFechaHasta());

		// buscar deudas judiciales
		listResult.addAll(getTotalEmision("gde_deudajudicial", auxDistribucionReport.getRecurso().getId(), 
						null,auxDistribucionReport.getFechaDesde(), auxDistribucionReport.getFechaHasta()));

		// buscar deudas canceladas
		listResult.addAll(getTotalEmision("gde_deudacancelada", auxDistribucionReport.getRecurso().getId(), 
						null,auxDistribucionReport.getFechaDesde(), auxDistribucionReport.getFechaHasta()));

		// buscar deudas anuladas o prescriptas
		listResult.addAll(getTotalEmision("gde_deudaanulada", auxDistribucionReport.getRecurso().getId(), 
						null,auxDistribucionReport.getFechaDesde(), auxDistribucionReport.getFechaHasta()));

		Long totCantCuentas = 0L;
		Double totImpDeudas = 0D;
		for(Object[] obj:listResult){
			totCantCuentas += obj[0]!=null?((BigDecimal) obj[0]).longValue():0L;
			totImpDeudas += obj[1]!=null?((BigDecimal) obj[1]).doubleValue():0D;
		}
		log.debug("Total cant. cuentas:"+totCantCuentas+"      Total imp Deudas:"+totImpDeudas);
			
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Cantidad de Deudas"));
		filaCabecera.add(new CeldaVO("Importe Total Deudas"));
		TablaVO tablacontenido = new TablaVO("Contenido");
		tablacontenido.setFilaCabecera(filaCabecera);
		
		FilaVO filaContenido = new FilaVO();
		filaContenido.add(new CeldaVO(totCantCuentas.toString()));
		filaContenido.add(new CeldaVO("$"+StringUtil.redondearDecimales(totImpDeudas, 1, 2)));		
		tablacontenido.add(filaContenido);
		
		
		// Bloque Inicial con Totales de Deuda Emitido 
		ContenedorVO bloque = new ContenedorVO("Bloque");
		// Genera la tabla de filtros de la busqueda
		TablaVO tablaTituloBloque = new TablaVO("tablaTitulo");
		tablaTituloBloque.setTitulo("Información General");
		bloque.setTablaCabecera(tablaTituloBloque);
		bloque.add(tablacontenido);
		contenedorVO.getListBloque().add(bloque);
		
		//contenedorVO.add(tablacontenido);
			
		
		
		
		// Armar tablas con totales por Partida (distribucion estimada) 
		DistribucionReportHelper distribucionReportHelper = new DistribucionReportHelper(auxDistribucionReport.getRecurso(),
							auxDistribucionReport.getFechaDesde(),auxDistribucionReport.getFechaHasta());
		//for(TablaVO tablaDisVO: distribucionReportHelper.generarTablasPartida()){
		for(ContenedorVO bloqueDis: distribucionReportHelper.generarTablasPartida()){
			contenedorVO.getListBloque().add(bloqueDis);
			//contenedorVO.add(tablaDisVO);
		}
		
		// Generacion del PrintModel		
		PrintModel printModel = new PrintModel();
		
		printModel.setRenderer(FormatoSalida.PDF.getId());
		printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
		printModel.cargarXsl("/mnt/publico/general/reportes/pageModel.xsl", PrintModel.RENDER_PDF);
		printModel.setTopeProfundidad(5);
		printModel.setData(contenedorVO);
		
		printModel.putCabecera("TituloReporte", "Reporte de Distribución de Totales Emitidos");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxDistribucionReport.getUserName());
							
		// Genera el PDF
		String fileNamePdf = fileName + ".pdf";			
		byte[] byteStream = printModel.getByteArray();
		FileOutputStream gesJudReportFile = new FileOutputStream(fileDir+"/"+fileNamePdf);
		gesJudReportFile.write(byteStream);
		gesJudReportFile.close();
		
		// Setea en el adapter, los datos del archivo generado
		planilla.setFileName(fileDir+"/"+fileNamePdf);
		planilla.setDescripcion("Reporte de Distribución de Totales Emitidos");									
		auxDistribucionReport.setReporteGenerado(planilla);
		
		log.debug("generarPDF4Report - exit");
		return auxDistribucionReport;
	}
	
	/**
	 * Obtiene el total de cuentas y el total de importe deuda para las deudas de la tabla pasada como parametro, con los filtros pasados como parametro
	 * @param nombreTabla
	 * @param idRecurso
	 * @param idRecClaDeu
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return
	 */
	private List<Object[]> getTotalEmision(String nombreTabla, Long idRecurso, Long idRecClaDeu, Date fechaEmisionDesde, Date fechaEmisionHasta){
		log.debug("getTotalEmision - enter");
		/*String queryString ="select count(distinct deuda.idCuenta) cantCuentas, sum(deuda.importe) totalDeuda from "+
				nombreTabla+" deuda, pad_cuenta cuenta WHERE deuda.idcuenta=cuenta.id";
		
		if(idRecClaDeu!=null && idRecClaDeu.longValue()>0)
			queryString +=" and deuda.idreccladeu="+idRecClaDeu;
		
		
		queryString +=" and cuenta.idrecurso="+
			idRecurso+" and deuda.fechaVencimiento>= :fechaEmisionDesde and deuda.fechaVencimiento<= :fechaEmisionHasta";		
		 */
		String queryString ="select count(deuda.id), sum(deuda.importe) totalDeuda from "+
		nombreTabla+" deuda WHERE deuda.idRecurso="+idRecurso;
		
		if(idRecClaDeu!=null && idRecClaDeu.longValue()>0)
			queryString +=" and deuda.idRecClaDeu="+idRecClaDeu;
		
		queryString +=" and deuda.fechaVencimiento>= :fechaEmisionDesde and deuda.fechaVencimiento<= :fechaEmisionHasta";		
 
		Session session = SiatHibernateUtil.currentSession();
 		Query query = session.createSQLQuery(queryString);
 		query.setDate("fechaEmisionDesde", fechaEmisionDesde);
 		query.setDate("fechaEmisionHasta", fechaEmisionHasta);
 		
 		List<Object[]> listReturn = query.list();
 		
 		log.debug("Busco en la tabla:"+nombreTabla);
 		if(listReturn.size()>0){
	 		log.debug("cant deudas:"+listReturn.get(0)[0]);
	 		log.debug("cant importe deudas:"+listReturn.get(0)[1]);
 		}else{
 			log.debug("No trajo resultados");
 		}
 		
 		log.debug("getTotalEmision - exit");
		return listReturn;
	}
	// <--- Reporte de totales de emision
	
	/**
	 *  Obtiene los id de Deuda Admin CdM donde la suma del importe de los conceptos difiere del importe total de la deuda.
	 * 	<p></p><p><b>
	 *  SQLQuery realizada:</b></p>
	 *  <p><i>
	 *  select d.id, d.importe, d.saldo, SUM(c.importe) as sumImp, SUM(c.saldo) as sumSal 
	 *  from gde_deudaadmin d, gde_deuadmreccon c, pad_cuenta cc
 	 *  where d.id = c.iddeuda and
	 *  cc.idrecurso in (1,2) and cc.id = d.idcuenta and
	 *  numerocuenta > 817000000
	 *  group by 1,2,3
	 *  having SUM(c.importe) <> d.importe or SUM(c.saldo) <> d.saldo</i>
	 *  </p>
	 *  
	 * @return List<Long[]>
	 */
	public List<Object[]> getListIdDeudaAdminConConceptosInconsistentes() {
		try {
			String queryString = "select d.id, d.importe, d.saldo, SUM(c.importe) as sumImp, SUM(c.saldo) as sumSal" +
					" from gde_deudaAdmin d, gde_deuAdmRecCon c, pad_cuenta cc" +
					" where d.id = c.idDeuda and" +
					" cc.idRecurso in (1,2) and cc.id = d.idCuenta " +
					" and numeroCuenta > 817000000 " +
					" group by 1,2,3 " +
					" having SUM(c.importe) <> d.importe or SUM(c.saldo) <> d.saldo ";
			
			Session session = currentSession();
			
			// obtenemos el resultado de la consulta
			Query query = session.createSQLQuery(queryString);
			
			return (ArrayList<Object[]>) query.list();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 *  Obtiene los id de Deuda Judicial CdM donde la suma del importe de los conceptos difiere del importe total de la deuda.
	 * 	<p></p><p><b>
	 *  SQLQuery realizada:</b></p>
	 *  <p><i>
	 *  select d.id, d.importe, d.saldo, SUM(c.importe) as sumImp, SUM(c.saldo) as sumSal 
	 *  from gde_deudaJudicial d, gde_deuJudRecCon c, pad_cuenta cc
 	 *  where d.id = c.iddeuda and
	 *  cc.idrecurso in (1,2) and cc.id = d.idcuenta and
	 *  numerocuenta > 817000000
	 *  group by 1,2,3
	 *  having SUM(c.importe) <> d.importe or SUM(c.saldo) <> d.saldo</i>
	 *  </p>
	 *  
	 * @return List<Long[]>
	 */
	public List<Object[]> getListIdDeudaJudicialConConceptosInconsistentes() {
		try {
			String queryString = "select d.id, d.importe, d.saldo, SUM(c.importe) as sumImp, SUM(c.saldo) as sumSal" +
					" from gde_deudaJudicial d, gde_deuJudRecCon c, pad_cuenta cc" +
					" where d.id = c.idDeuda and" +
					" cc.idRecurso in (1,2) and cc.id = d.idCuenta " +
					" and numeroCuenta > 817000000 " +
					" group by 1,2,3 " +
					" having SUM(c.importe) <> d.importe or SUM(c.saldo) <> d.saldo ";
			
			Session session = currentSession();
			
			// obtenemos el resultado de la consulta
			Query query = session.createSQLQuery(queryString);
			
			return (ArrayList<Object[]>) query.list();
		}
		catch (Exception e) {
			return null;
		}
	}

	// ---> Reporte de Recaudado NUEVO
	public AuxRecaudadoReport generarPDF4Report(AuxRecaudadoReport auxRecaudadoReport) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);						
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileName = idCorrida+"_ReporteRecaudado_"+ auxRecaudadoReport.getUserId();

		PlanillaVO planilla = new PlanillaVO();
		ContenedorVO contenedorVO = new ContenedorVO("Contenedor");			
		
		// Invertemos la orientacion de la pagina
		Double newPageHeight = contenedorVO.getPageWidth();
		contenedorVO.setPageWidth(contenedorVO.getPageHeight());
		contenedorVO.setPageHeight(newPageHeight);
		
		// Genera la tabla de filtros de la busqueda
		FilaVO filaDeCabecera = new FilaVO();		
		filaDeCabecera.add(new CeldaVO(auxRecaudadoReport.getRecurso().getDesRecurso(), "Recurso", "Recurso"));
		filaDeCabecera.add(new CeldaVO(auxRecaudadoReport.getFechaDesdeView(), "fechaDesde", "Fecha Vto. Desde"));
		filaDeCabecera.add(new CeldaVO(auxRecaudadoReport.getFechaHastaView(), "fechaHasta", "Fecha Vto. Hasta"));
		if(auxRecaudadoReport.getFechaPagoHasta()!= null)filaDeCabecera.add(new CeldaVO(auxRecaudadoReport.getFechaPagoHastaView(), "fechaPagoHasta", "Fecha Pago Hasta"));
		TablaVO tablaFiltros = new TablaVO("Filtros Aplicados");
		tablaFiltros.add(filaDeCabecera);
		tablaFiltros.setTitulo("Filtros de Búsqueda");
		contenedorVO.setTablaFiltros(tablaFiltros);
		
		
		//crea el helper y obtiene las deudas
		RecaudadoReportHelper recaudadoReportHelper = new RecaudadoReportHelper(auxRecaudadoReport.getRecurso(),auxRecaudadoReport.getFechaDesde(),auxRecaudadoReport.getFechaHasta(),auxRecaudadoReport.getFechaPagoHasta());
		recaudadoReportHelper.generarPeriodosDeudas();
		
		
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Período"));
		filaCabecera.add(new CeldaVO("Total Emitido"));
		filaCabecera.add(new CeldaVO("Total pagado antes del Vto."));
		filaCabecera.add(new CeldaVO("%"));
		filaCabecera.add(new CeldaVO("Total pagado vencido"));
		filaCabecera.add(new CeldaVO("%"));		
		filaCabecera.add(new CeldaVO("Total impago en Convenio"));
		filaCabecera.add(new CeldaVO("%"));		
		filaCabecera.add(new CeldaVO("Total pago en Convenio"));
		filaCabecera.add(new CeldaVO("%"));		
		filaCabecera.add(new CeldaVO("Total anulado"));
		filaCabecera.add(new CeldaVO("%"));		
		filaCabecera.add(new CeldaVO("Total impago"));
		filaCabecera.add(new CeldaVO("%"));		
		
		TablaVO tablacontenido = new TablaVO("Contenido");
		tablacontenido.setFilaCabecera(filaCabecera);

		Double totPagadoAntesVto = 0D;
		Double totPagadoDespuesVto = 0D;
		Double totImpago = 0D;
		Double totEmitido = 0D;
		Double totImpagoEnConv = 0D;
		Double totPagadoEnConv = 0D;
		Double totAnulado = 0D; 

		for(String key:recaudadoReportHelper.getMapPeriodosDeudas().keySet()){
			Double[] totalesPeriodoActual = recaudadoReportHelper.getMapPeriodosDeudas().get(key);
			Double totalEmitidoTmp = totalesPeriodoActual[0]!=null?totalesPeriodoActual[0]:0D;
			Double totalPagadoAntesVtoTmp = totalesPeriodoActual[1]!=null?totalesPeriodoActual[1]:0D;
			Double totalPagadoVencidoTmp = totalesPeriodoActual[2]!=null?totalesPeriodoActual[2]:0D;
			Double totalImpagoEnConvTmp = totalesPeriodoActual[3]!=null?totalesPeriodoActual[3]:0D;
			Double totalPagoEnConvTmp = totalesPeriodoActual[4]!=null?totalesPeriodoActual[4]:0D;
			Double totalAnuladoTmp = totalesPeriodoActual[5]!=null?totalesPeriodoActual[5]:0D;
			//No se muestra en el reporte, solo se utiliza para calcular el total impago teniendo en cuenta los descuentos.
			//Double totalDeudaConvenioTmp = totalesPeriodoActual[6]!=null?totalesPeriodoActual[6]:0D;
			
			///agregar en 
			//Double totalImpagoTmp = totalEmitidoTmp-totalPagadoAntesVtoTmp-totalPagadoVencidoTmp-totalPagoEnConvTmp-totalAnuladoTmp;
			Double totalImpagoTmp = totalEmitidoTmp-totalPagadoAntesVtoTmp-totalPagadoVencidoTmp -totalImpagoEnConvTmp - totalPagoEnConvTmp - totalAnuladoTmp;
			// Genera la fila del reporte
			FilaVO filaContenido = new FilaVO();
				// periodo
			filaContenido.add(new CeldaVO(key));
				// total emitido en el periodo			
			filaContenido.add(new CeldaVO("$"+getValorFormateado(totalEmitidoTmp)));
			
				// pagado antes del vto			
			filaContenido.add(new CeldaVO("$"+getValorFormateado(totalPagadoAntesVtoTmp)));
			
			Double porcentaje = totalPagadoAntesVtoTmp/totalEmitidoTmp*100;
				// % pagado antes del vto			
			filaContenido.add(new CeldaVO(getValorFormateado(porcentaje)+" %"));
			
				// pagado vencido
			filaContenido.add(new CeldaVO("$"+getValorFormateado(totalPagadoVencidoTmp)));
			
			porcentaje = totalPagadoVencidoTmp/totalEmitidoTmp*100;
				// % pagado vencido
			filaContenido.add(new CeldaVO(getValorFormateado(porcentaje)+" %"));
						
				// impago en convenio
			filaContenido.add(new CeldaVO("$"+getValorFormateado(totalImpagoEnConvTmp)));
			
			porcentaje = totalImpagoEnConvTmp/totalEmitidoTmp*100;
				// % impago en convenio
			filaContenido.add(new CeldaVO(getValorFormateado(porcentaje)+" %"));// % impago en conv
				
				// pagado en convenio
			filaContenido.add(new CeldaVO("$"+getValorFormateado(totalPagoEnConvTmp)));
			
			porcentaje = totalPagoEnConvTmp/totalEmitidoTmp*100;
				// % pagado en convenio
			filaContenido.add(new CeldaVO(getValorFormateado(porcentaje)+" %"));// % pagado en conv
			
				// anulado
			filaContenido.add(new CeldaVO("$"+getValorFormateado(totalAnuladoTmp)));
			
			porcentaje = totalAnuladoTmp/totalEmitidoTmp*100;
				// % anulado
			filaContenido.add(new CeldaVO(getValorFormateado(porcentaje)+" %"));// % anulado
			
			// impago
			filaContenido.add(new CeldaVO("$"+getValorFormateado(totalImpagoTmp)));
			
			porcentaje = totalImpagoTmp/totalEmitidoTmp*100;
				// % impago
			filaContenido.add(new CeldaVO(getValorFormateado(porcentaje)+" %"));// % impago

			tablacontenido.add(filaContenido);
			
			// actualiza los totales
			totPagadoAntesVto +=totalPagadoAntesVtoTmp;
			totPagadoDespuesVto +=totalPagadoVencidoTmp;
			totImpago +=totalImpagoTmp;
			totEmitido += totalEmitidoTmp;
			totImpagoEnConv += totalImpagoEnConvTmp;
			totPagadoEnConv += totalPagoEnConvTmp;
			totAnulado += totalAnuladoTmp;
		}
		
		// Se agrega la fila con los totales generales
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("TOTAL"));
		filaPie.add(new CeldaVO("$"+getValorFormateado(totEmitido)));
		filaPie.add(new CeldaVO("$"+getValorFormateado(totPagadoAntesVto)));
		filaPie.add(new CeldaVO(getValorFormateado(totPagadoAntesVto/totEmitido*100)+" %"));
		filaPie.add(new CeldaVO("$"+getValorFormateado(totPagadoDespuesVto)));
		filaPie.add(new CeldaVO(getValorFormateado(totPagadoDespuesVto/totEmitido*100)+" %"));
		filaPie.add(new CeldaVO("$"+getValorFormateado(totImpagoEnConv))); 
		filaPie.add(new CeldaVO(getValorFormateado(totImpagoEnConv/totEmitido*100)+" %")); 
		filaPie.add(new CeldaVO("$"+getValorFormateado(totPagadoEnConv))); 
		filaPie.add(new CeldaVO(getValorFormateado(totPagadoEnConv/totEmitido*100)+" %")); 
		filaPie.add(new CeldaVO("$"+getValorFormateado(totAnulado))); 
		filaPie.add(new CeldaVO(getValorFormateado(totAnulado/totEmitido*100)+" %")); 
		filaPie.add(new CeldaVO("$"+getValorFormateado(totImpago)));
		filaPie.add(new CeldaVO(getValorFormateado(totImpago/totEmitido*100)+" %"));
		
		tablacontenido.addFilaPie(filaPie);
		
		contenedorVO.add(tablacontenido);
		
		// Generacion del PrintModel		
		PrintModel printModel = new PrintModel();
		
		printModel.setRenderer(FormatoSalida.PDF.getId());
		printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
		printModel.cargarXsl("/mnt/publico/general/reportes/pageModel.xsl", PrintModel.RENDER_PDF);
		printModel.setTopeProfundidad(5);
		printModel.setData(contenedorVO);
		
		printModel.putCabecera("TituloReporte", "Reporte de Recaudado");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxRecaudadoReport.getUserName());
							
		// Genera el PDF
		String fileNamePdf = fileName + ".pdf";			
		byte[] byteStream = printModel.getByteArray();
		FileOutputStream gesJudReportFile = new FileOutputStream(fileDir+"/"+fileNamePdf);
		gesJudReportFile.write(byteStream);
		gesJudReportFile.close();
		
		// Setea en el adapter, los datos del archivo generado
		planilla.setFileName(fileDir+"/"+fileNamePdf);
		planilla.setDescripcion("Reporte de Emisión");									
		auxRecaudadoReport.setReporteGenerado(planilla);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return auxRecaudadoReport;
	}
	
	// Se utilizo un metodo para que todos los valores tomen el mismo formato y sea mas mantenible (para cuando se cambia)
	private String getValorFormateado(Double valor){
		return StringUtil.redondearDecimales(valor, 1, 2);
	}
	/**
	 * Obtiene las deudas con los valores de emision pasados como parametros, agrupada por anio y periodo.
	 * @param fechaPagoHasta 
	 * @param pagadaAntesVto: si es NULL no se tiene en cuenta;<br>
	 * si es TRUE busca las que tienen fechaPago<=fechaVto;<br>
	 * si es FALSE busca las que tiene fechaPago>fechaVto
	 * @return list: Ej:<br> 
	 * 200801, 300000
	 * 200802, 600000
	 * 200803, 500000
	 */
	public List<Object[]> getDeudaEmiAgrupAnioPer(Long idRecurso, String nombreTabla, Date fechaEmisionDesde, Date fechaEmisionHasta, Date fechaPagoHasta, Boolean aplicarFechaHasta, Boolean pagadaAntesVto, Boolean deudaEnConvenio,Boolean deudaAnulada, Boolean deudaImpagaEnConvenio){ 
		log.debug("getDeudaEmiAgrupAnioPer - enter");

		/*	
		 * String queryString ="select deuda.periodo, deuda.anio, sum(deuda.importe) from "+nombreTabla+" deuda" +
		 * " where deuda.fechaemision>= :fechaEmisionDesde and deuda.fechaemision<= :fechaEmisionHasta" +
		 * " and deuda.idRecurso="+idRecurso;
		 */
		
		String queryString ="";
		if(!deudaEnConvenio){
			queryString +=" SELECT YEAR(deuda.fechavencimiento)*100 + MONTH(deuda.fechavencimiento), " +
			"SUM(deuda.importe) " +
			"FROM "+nombreTabla+" deuda ";

		} else {
			if(deudaImpagaEnConvenio!=null){
				if(deudaImpagaEnConvenio){
					queryString +="SELECT year(deuda.fechavencimiento)*100 + month(deuda.fechavencimiento), " +
					"SUM(conv.saldoenplan * (conv.capitalenplan/conv.totalenplan) ) impago " +
					"FROM " + nombreTabla + " deuda " +
					"	INNER JOIN gde_conveniodeuda conv ON (deuda.idconvenio = conv.idconvenio AND deuda.id = conv.iddeuda) ";
				} else {
					queryString +="SELECT year(deuda.fechavencimiento)*100 + month(deuda.fechavencimiento), " +
					"SUM((conv.totalenplan - conv.saldoenplan) * (conv.capitalenplan/conv.totalenplan) ) pago " +
					"FROM "+ nombreTabla +" deuda " +
					"	INNER JOIN gde_conveniodeuda conv ON (deuda.idconvenio = conv.idconvenio AND deuda.id = conv.iddeuda) ";
				}
			} else {
				queryString +=" SELECT YEAR(deuda.fechavencimiento)*100 + MONTH(deuda.fechavencimiento), " +
				"SUM(deuda.importe) " +
				"FROM "+nombreTabla+" deuda ";
			}
		}
		
		queryString += 	"WHERE 1=1 " +
						"AND deuda.fechaVencimiento>= :fechaVtoDesde " +
						"AND deuda.fechaVencimiento<= :fechaVtoHasta " +
						"AND deuda.idRecurso="+idRecurso;
		
		
		if (fechaPagoHasta != null && aplicarFechaHasta) {
			queryString += " AND deuda.fechaPago <= :fechaPagoH";
			if (pagadaAntesVto != null) {
				if (pagadaAntesVto) {
					queryString += " AND deuda.fechaPago <= deuda.fechavencimiento";
				} else {
					queryString += " AND deuda.fechaPago > deuda.fechavencimiento";
				}
			}
		} else {
			if (pagadaAntesVto != null) {
				if (pagadaAntesVto) {
					queryString += " AND deuda.fechaPago <= deuda.fechavencimiento";
				} else {
					queryString += " AND deuda.fechaPago > deuda.fechavencimiento";
				}
			}
		}
		
		if(deudaEnConvenio){
			if(deudaImpagaEnConvenio==null) {
				queryString +=" AND deuda.idConvenio IS NOT NULL ";
			}
		}
		
		if(deudaAnulada){
			queryString +=" AND deuda.idEstadoDeuda IN ("+EstadoDeuda.ID_ANULADA+","+EstadoDeuda.ID_CONDONADA
														  +","+EstadoDeuda.ID_PRESCRIPTA+") ";
		}

				
		//queryString +=" group by deuda.anio, deuda.periodo"+
		//	" order by deuda.anio, deuda.periodo";
		queryString +=" GROUP BY 1 ORDER BY 1";


		Session session = SiatHibernateUtil.currentSession();
 		Query query = session.createSQLQuery(queryString);
 		query.setDate("fechaVtoDesde", fechaEmisionDesde);
 		query.setDate("fechaVtoHasta", fechaEmisionHasta); 		
		if(fechaPagoHasta!=null && aplicarFechaHasta) query.setDate("fechaPagoH", fechaPagoHasta); 
	
 		List<Object[]> listReturn = query.list();
 		
 		log.debug("Busco en la tabla:"+nombreTabla);
 		log.debug("cant resultados:"+listReturn.size());	 	
 		
 		log.debug("getDeudaEmiAgrupAnioPer - exit");
		return listReturn;
	}
	// <--- Reporte de Recaudado NUEVO
	
	
	
	
	// ---> Reporte de deuda por procurador
	public AuxDeudaProcuradorReport generarPDF4Report(AuxDeudaProcuradorReport auxDeudaProcuradorReport)throws Exception{
		log.debug("generarPDF4Report - enter");
		Date horaInicio = new Date();
		
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);						
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileName = idCorrida+"_ReporteDeudaProcurador_"+ auxDeudaProcuradorReport.getUserId();

				
		Procurador procurador = auxDeudaProcuradorReport.getProcurador();
		Date fechaDesde = auxDeudaProcuradorReport.getFechaDesde();
		Date fechaHasta = auxDeudaProcuradorReport.getFechaHasta();

		DeudaProcuradorReportHelper reportHelper = new DeudaProcuradorReportHelper(procurador,fechaDesde,
																								fechaHasta);
		reportHelper.generarListaFilasReporte();
		List<FilaReporteDeudaProcurador> listFilaReporte = reportHelper.getListFilaReporte();
		
		PlanillaVO planilla = new PlanillaVO();
		ContenedorVO contenedorVO = new ContenedorVO("Contenedor");			

		// Genera la tabla de filtros de la busqueda
		FilaVO filaDeCabecera = new FilaVO();		
		filaDeCabecera.add(new CeldaVO(auxDeudaProcuradorReport.getProcurador()!=null?auxDeudaProcuradorReport.getProcurador().getId()+" - "+auxDeudaProcuradorReport.getProcurador().getDescripcion():"Todos", "Procurador", "Procurador"));
		filaDeCabecera.add(new CeldaVO(auxDeudaProcuradorReport.getFechaDesdeView(), "fechaDesde", "Fecha Vto. Desde"));
		filaDeCabecera.add(new CeldaVO(auxDeudaProcuradorReport.getFechaHastaView(), "fechaHasta", "Fecha Vto. Hasta"));
		TablaVO tablaFiltros = new TablaVO("Filtros Aplicados");
		tablaFiltros.add(filaDeCabecera);
		tablaFiltros.setTitulo("Filtros de Búsqueda");
		contenedorVO.setTablaFiltros(tablaFiltros);
		
		String strTipoReporte ="";
		TablaVO tablaContenido = new TablaVO("Contenido");
		
		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();
		
		if(listFilaReporte.isEmpty()){
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO(" "));
			tablaContenido.setFilaCabecera(filaCabecera);
			FilaVO fila = new FilaVO();			
			fila.add(new CeldaVO("No se encontraron resultados para la búsqueda"));
			tablaContenido.add(fila);		
			if(auxDeudaProcuradorReport.getTipoReporte().equals(DeudaProcuradorReportHelper.TIPO_REPORTE_RESUMIDO))
				strTipoReporte =" Resumido";
			else
				strTipoReporte =" Detallado";
			
		}else{
			
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Procurador"));
			filaCabecera.add(new CeldaVO("Zona"));
			filaCabecera.add(new CeldaVO("Sección"));
			if(auxDeudaProcuradorReport.getTipoReporte().equals(DeudaProcuradorReportHelper.TIPO_REPORTE_RESUMIDO)){
				strTipoReporte =" Resumido";
				filaCabecera.add(new CeldaVO("Cant. cuentas"));
				filaCabecera.add(new CeldaVO("Importe hist."));
				filaCabecera.add(new CeldaVO("Importe Act."));		
				filaCabecera.add(new CeldaVO("Saldo hist."));
				filaCabecera.add(new CeldaVO("Saldo Act."));
				
				setearFilasReporteDeuProcResumido(tablaContenido,listFilaReporte);
				
				tablaContenido.setFilaCabecera(filaCabecera);

				contenedorVO.add(tablaContenido);
				
				// Generacion del PrintModel		
				PrintModel printModel = new PrintModel();
				
				printModel.setRenderer(FormatoSalida.PDF.getId());
				printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
				printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
				printModel.cargarXsl("/mnt/publico/general/reportes/pageModel.xsl", PrintModel.RENDER_PDF);
				printModel.setTopeProfundidad(5);
				printModel.setData(contenedorVO);
				
				String tituloReporte = "Reporte de Deudas Por Procurador - "+strTipoReporte;
				printModel.putCabecera("TituloReporte", tituloReporte);
				printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
				printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
				printModel.putCabecera("Usuario", auxDeudaProcuradorReport.getUserName());
									
				// Genera el PDF
				String fileNamePdf = fileName + ".pdf";			
				byte[] byteStream = printModel.getByteArray();
				FileOutputStream deuProcReportFile = new FileOutputStream(fileDir+"/"+fileNamePdf);
				deuProcReportFile.write(byteStream);
				deuProcReportFile.close();
				
				// Setea en el adapter, los datos del archivo generado
				planilla.setFileName(fileDir+"/"+fileNamePdf);
				planilla.setDescripcion("Reporte de Deudas por Procurador - "+ strTipoReporte+".pdf");									
				auxDeudaProcuradorReport.getListReportesGenerado().add(planilla);
			}else{			
				strTipoReporte =" Detallado";
				
				// Genera el/los archivo/s csv, para excel
				BufferedWriter buffer = new BufferedWriter(new FileWriter(fileDir+"/"+fileName+".csv", false));
				log.debug("Creo el archivo CSV:"+fileDir+"/"+fileName+".csv");
				PlanillaVO planillaTmp1 = new PlanillaVO();
				planillaTmp1.setFileName(fileDir+"/"+fileName+".csv");
				planillaTmp1.setDescripcion("Reporte de Deudas por Procurador - "+ strTipoReporte+".csv");
				listPlanilla.add(planillaTmp1);
				
				long regCounter = 3;
				int numeroArchivo = 0;
				
				//cabecera
				buffer.write("REPORTE DE DEUDAS POR PROCURADOR - DETALLADO");
				buffer.newLine();
				buffer.write("Procurador;Zona;Seccion;Cuenta;Periodo/Anio;Importe Hist;Importe Act;Saldo Hist;Saldo Act");			
				buffer.newLine();			
				
				Double totSaldoHist = 0D;
				Double totSaldoAct = 0D;
				Double totImporteHist = 0D;
				Double totImporteAct = 0D;
				
				for(FilaReporteDeudaProcurador fila:listFilaReporte){
								
					// verifica si hay que generar otro archivo
					regCounter++;
					if(regCounter == 30000 ){ // - incluyendo a las filas del encabezado y considera que regCounter arranca en cero
						
						// cierra el buffer, genera una nueva planilla
						if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
						buffer.close();				
						numeroArchivo++;
						PlanillaVO planillaTmp = new PlanillaVO();
						planillaTmp.setFileName(fileDir+"/"+fileName+"_"+ numeroArchivo+".csv");
						planillaTmp.setDescripcion("Reporte de Deudas por Procurador - "+ strTipoReporte+"_"+numeroArchivo+".csv");
						listPlanilla.add(planillaTmp);
						buffer = new BufferedWriter(new FileWriter(fileDir+"/"+fileName+"_"+ numeroArchivo+".csv", false));
						buffer.newLine();
						buffer.write("REPORTE DE DEUDAS POR PROCURADOR - DETALLADO");
						buffer.write("Procurador;Zona;Sección;Cuenta;Período/Año;Importe Hist;Importe Act;Saldo Hist;Saldo Act");
						buffer.newLine();			
	
						regCounter = 4; // reinicio contador 
					}else{
						// crea una nueva linea
						buffer.newLine();
					}
					
					// escribe la fila				
					buffer.write(fila.getIdProcurador()+" - "+fila.getDesProcurador()+";"+fila.getZona()+";"+
							fila.getSeccion()+";"+fila.getNroCuenta()+";"+fila.getStrPeriodoAnio()+";"+
							StringUtil.redondearDecimales(fila.getImportehist(), 1, 2)+";"+
							StringUtil.redondearDecimales(fila.getImporteAct(), 1, 2)+";"+
							StringUtil.redondearDecimales(fila.getSaldoHist(), 1, 2)+";"+
							StringUtil.redondearDecimales(fila.getSaldoAct(), 1, 2));
					
					// actualiza los totales
					totImporteHist+=fila.getImportehist();
					totImporteAct +=fila.getImporteAct();
					totSaldoHist +=fila.getSaldoHist();
					totSaldoAct +=fila.getSaldoAct();
					
				}
				// escribe los totales
				buffer.newLine();
				buffer.write("TOTAL;;;;;"+StringUtil.redondearDecimales(totImporteHist, 1, 2)+";"+
											StringUtil.redondearDecimales(totImporteAct, 1, 2)+";"+
											StringUtil.redondearDecimales(totSaldoHist, 1, 2)+";"+
											StringUtil.redondearDecimales(totSaldoAct, 1, 2));			
				buffer.close();			
				auxDeudaProcuradorReport.setListReportesGenerado(listPlanilla);
				if(log.isDebugEnabled()) log.debug("Archivos excel generados:"+ numeroArchivo+1);			
				
				// datos para el pdf
				fileName = idCorrida+"_ReporteDeudaProcurador_"+ auxDeudaProcuradorReport.getUserId();			
				filaCabecera.add(new CeldaVO("Nro. Cuenta"));
				filaCabecera.add(new CeldaVO("Período/Año"));
				filaCabecera.add(new CeldaVO("Importe Hist."));
				filaCabecera.add(new CeldaVO("Importe Act."));;
				filaCabecera.add(new CeldaVO("Saldo Hist."));
				filaCabecera.add(new CeldaVO("Saldo Act."));
				setearFilasReporteDeuProcDetallado(tablaContenido,listFilaReporte);
			}
		}
				
		
		// Datos para logs
		Date horaFin = new Date();
		int cantDeudaProcesadas=listFilaReporte.size();		
		long difMillis = horaFin.getTime() - horaInicio.getTime();
		long segundos = difMillis/1000;
		long horas = segundos / 3600;
		segundos -= horas*3600;
		long minutos = segundos /60;
	    segundos -= minutos*60;

	    log.debug("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		log.debug("cant deudas procesadas:"+cantDeudaProcesadas);
		log.debug("Hora inicio:"+horaInicio+"       hora fin:"+horaFin);
		log.debug("Tiempo insumido:    "+horas+" horas   "+minutos+" minutos    "+segundos+" segundos");
		log.debug("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
		log.debug("generarPDF4Report - exit");

		return auxDeudaProcuradorReport;
			
	}
	
	
	private void setearFilasReporteDeuProcDetallado(TablaVO tablaContenido, List<FilaReporteDeudaProcurador> listFilaReporte) {
		log.debug("setearFilasReporteDeuProcDetallado - enter");
		Double totImporteHist = 0D;
		Double totImporteAct = 0D;
		Double totSaldoHist =0D;
		Double totSaldoAct = 0D;
		
		for(FilaReporteDeudaProcurador fila: listFilaReporte){
			FilaVO filaVO = new FilaVO();
			filaVO.add(new CeldaVO(fila.getDesProcurador()));
			filaVO.add(new CeldaVO(fila.getZona()));
			filaVO.add(new CeldaVO(fila.getSeccion()));
			filaVO.add(new CeldaVO(fila.getNroCuenta()));
			filaVO.add(new CeldaVO(fila.getStrPeriodoAnio()));
			filaVO.add(new CeldaVO(StringUtil.redondearDecimales(fila.getImportehist(), 1, 2)));
			filaVO.add(new CeldaVO(StringUtil.redondearDecimales(fila.getImporteAct(), 1, 2)));
			filaVO.add(new CeldaVO(StringUtil.redondearDecimales(fila.getSaldoHist(), 1, 2)));
			filaVO.add(new CeldaVO(StringUtil.redondearDecimales(fila.getSaldoAct(), 1, 2)));
			
			totSaldoAct+=fila.getSaldoAct();
			totSaldoHist+=fila.getSaldoHist();
			totImporteAct+=fila.getImporteAct();
			totImporteHist+=fila.getImportehist();
			
			tablaContenido.add(filaVO);
			
			/*log.debug("Agrego la fila:"+fila.getIdProcurador()+" "+fila.getDesProcurador()+"     "+
					fila.getZona()+"       "+fila.getSeccion()+"       "+
					fila.getNroCuenta()+"      "+fila.getImportehist()+"       "+
					fila.getImporteAct()+"       "+fila.getSaldoHist()+"      "+
					fila.getSaldoAct());
			*/
		}
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("TOTAL"));
		filaPie.add(new CeldaVO(" "));
		filaPie.add(new CeldaVO(" "));
		filaPie.add(new CeldaVO(" "));
		filaPie.add(new CeldaVO(" "));
		filaPie.add(new CeldaVO(StringUtil.redondearDecimales(totImporteHist, 1, 2)));
		filaPie.add(new CeldaVO(StringUtil.redondearDecimales(totImporteAct, 1, 2)));		
		filaPie.add(new CeldaVO(StringUtil.redondearDecimales(totSaldoHist, 1, 2)));
		filaPie.add(new CeldaVO(StringUtil.redondearDecimales(totSaldoAct, 1, 2)));		

		tablaContenido.addFilaPie(filaPie);
		log.debug("setearFilasReporteDeuProcDetallado - exit");
	}

	/**
	 * Genera la lista de filas para el reporte, agrupando por procurador-zona-seccion y calculando los 
	 * correspondientes totales.
	 * @param listFilaReporte
	 * @return
	 */
	private void setearFilasReporteDeuProcResumido(TablaVO tablaContenido, List<FilaReporteDeudaProcurador> listFilaReporte) {
		log.debug("setearFilasReporteDeuProcResumido: enter");
		Map<String, List<String>> mapFilaCtas = new LinkedHashMap<String, List<String>>();
		Map<String, Double[]> mapFilaTot = new LinkedHashMap<String, Double[]>();
		Double totImporteHist = 0D;
		Double totImporteAct = 0D;
		Double totSaldoHist = 0D;
		Double totSaldoAct = 0D;
		
		// agrupa totales por procurador-zona-seccion
		for(FilaReporteDeudaProcurador fila: listFilaReporte){
			String key = fila.getIdProcurador()+"-"+fila.getDesProcurador()+"-"+fila.getZona()+"-"+fila.getSeccion();
			Double valores[] = {0D,0D,0D, 0D, 0D};// cantcuentas, importeHist, importeAct, saldoHist, saldoAct

			if(mapFilaTot.containsKey(key)){
				valores=mapFilaTot.get(key);
			}
			
			// actualiza la cant de cuentas
			if(mapFilaCtas.containsKey(key)){
				int pos = mapFilaCtas.get(key).indexOf(fila.getNroCuenta());
				if(pos<0){// no la contiene
					mapFilaCtas.get(key).add(fila.getNroCuenta());
					valores[0]++;
				}
			}else{
				List<String> listCtas = new ArrayList<String>();
				listCtas.add(fila.getNroCuenta());
				mapFilaCtas.put(key,listCtas);
				valores[0]++;
			}
					
			// actualiza el importe hist 
			valores[1]+=fila.getImportehist();
			totImporteHist +=fila.getImportehist();
			
			// actualiza el importe act
			valores[2]+=fila.getImporteAct();
			totImporteAct +=fila.getImporteAct();
			
			// actualiza el saldo hist
			valores[3]+=fila.getSaldoHist();
			totSaldoHist +=fila.getSaldoHist();
			
			// actualiza el saldo act
			valores[4]+=fila.getSaldoAct();
			totSaldoAct +=fila.getSaldoAct();
			
			mapFilaTot.put(key, valores);	
			
			/*log.debug("Agrego la fila:"+fila.getIdProcurador()+" "+fila.getDesProcurador()+"     "+
					fila.getZona()+"       "+fila.getSeccion()+"       "+
					fila.getNroCuenta()+"      "+fila.getImportehist()+"       "+
					fila.getImporteAct()+"       "+fila.getSaldoHist()+"      "+
					fila.getSaldoAct());*/
		}
		
		// agrega cada fila generada anteriormente
		for(String keyFila: mapFilaTot.keySet()){			
			FilaVO fila = new FilaVO();			
			String[] splitKEY = keyFila.split("-");
			//fila.add(new CeldaVO(splitKEY[0]+" "+splitKEY[1])); //desProcurador
			fila.add(new CeldaVO(splitKEY[1])); //desProcurador
			fila.add(new CeldaVO(splitKEY[2])); // zona
			fila.add(new CeldaVO(splitKEY[3])); // seccion
			fila.add(new CeldaVO(String.valueOf(mapFilaTot.get(keyFila)[0].intValue())));//cant cuentas 
			fila.add(new CeldaVO(StringUtil.redondearDecimales(mapFilaTot.get(keyFila)[1], 1, 2)));//importe hist
			fila.add(new CeldaVO(StringUtil.redondearDecimales(mapFilaTot.get(keyFila)[2], 1, 2)));//importe act
			fila.add(new CeldaVO(StringUtil.redondearDecimales(mapFilaTot.get(keyFila)[3], 1, 2)));//saldo hist
			fila.add(new CeldaVO(StringUtil.redondearDecimales(mapFilaTot.get(keyFila)[4], 1, 2)));//saldo act
			tablaContenido.add(fila);
			
			
		}
		
		// agrega los totales por columna
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("TOTAL"));
		filaPie.add(new CeldaVO(" "));
		filaPie.add(new CeldaVO(" "));
		filaPie.add(new CeldaVO(" "));		
		filaPie.add(new CeldaVO(StringUtil.redondearDecimales(totImporteHist, 1, 2)));
		filaPie.add(new CeldaVO(StringUtil.redondearDecimales(totImporteAct, 1, 2)));
		filaPie.add(new CeldaVO(StringUtil.redondearDecimales(totSaldoHist, 1, 2)));
		filaPie.add(new CeldaVO(StringUtil.redondearDecimales(totSaldoAct, 1, 2)));

		tablaContenido.addFilaPie(filaPie);
		log.debug("setearFilasReporteDeuProcResumido: exit");
	}


	public List<Object[]> getList4DeuProReport(String nombreTabla, Long idProcurador, Date fechaVtoDesde,
			Date fechaVtoHasta,	Integer firstResult, Integer maxResults) {
		log.debug("getList4DeuProReport - enter");
		String queryString = "SELECT ";
		
		if(firstResult!=null)
			queryString += "SKIP "+firstResult;
		
		if(maxResults!=null)
			queryString +=" first "+maxResults;
		
		queryString+=" deu.idprocurador,deu.id, atrZona.strvalor zona, atrSeccion.strvalor seccion"+
				" FROM "+nombreTabla+" deu left join pad_cuenta cue on deu.idcuenta = cue.id" +
						" left join pad_objimp obj on cue.idobjimp = obj.id" +
						" left join pad_objimpatrval atrZona on obj.id = atrZona.idobjimp" +
						" left join pad_objimpatrval atrSeccion on obj.id = atrSeccion.idobjimp" +
				" WHERE  (atrZona.idtipobjimpatr = "+TipObjImpAtr.ID_ZONA_TIPO_PARCELA+") and " +
						"(atrSeccion.idtipobjimpatr ="+TipObjImpAtr.ID_SECCION_TIPO_PARCEL+") "+
					" and deu.fechavencimiento>= :fechaVtoDesde and " +
					"deu.fechavencimiento< :fechaVtoHasta and deu.idprocurador is not null" +
					" and idEstadoDeuda = 5"; //deuda impaga en estado judicial //fedel: bug 696
		
		if(idProcurador!=null && idProcurador.longValue()>0)
			queryString +=" and deu.idprocurador="+idProcurador;
		
		// test (ids de deuda cancelada y judicial) - sacar
		// para todos los proc con fechaVtoDesde 01/08/2002 y fecHasta 13/08/2002
		//queryString += " and (deu.id>=15092560 and deu.id<=15504975) ";
	//	queryString += " and (   (deu.id>=15092560 and deu.id<=15504975) "+
		//		" or (deu.id>=22042823 and deu.id<=22169399) ) ";
		
		queryString +=" ORDER BY deu.idprocurador";
		
		Session session = SiatHibernateUtil.currentSession();
 		Query query = session.createSQLQuery(queryString);
 		query.setDate("fechaVtoDesde", fechaVtoDesde);
 		query.setDate("fechaVtoHasta", fechaVtoHasta); 		
 			
 		List<Object[]> listReturn = query.list();
 		
 		log.debug("Consulta:"+queryString);
 		log.debug("Busco en la tabla:"+nombreTabla);
 		log.debug("cant resultados:"+listReturn.size());	 	
 		
 		log.debug("getDeudasProcurador - exit");
		return listReturn;

	}
	// <--- Reporte de deuda por procurador	

	/**
	 * Obtiene todos los id de deuda correspondiente al ServicioBanco 
	 * con id idServicioBanco tal que deuda.fechaVencimiento es 
	 * anterior o igual a fechaVencimiento
	 * 
	 * Metodo utilizado durante el Proceso de Prescripcion de Deuda.
	 *
	 * @param idServicioBanco
	 * @param fechaVencimiento
	 * @param idEstadoDeuda
	 * @return List<Long> listIdDeuda
	 * */
	@SuppressWarnings("unchecked")
	public List<Long> getListDeudaAPrescribir(Long idServicioBanco, Date fechaVencimiento, 
				Long idEstadoDeuda) {
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "select deuda.id from "+ this.deudaClass +" deuda, Sistema sistema ";
		strQuery += "where sistema.id = deuda.sistema.id ";
		strQuery +=  "and deuda.fechaVencimiento  <= :fechaVencimiento "; 
		strQuery +=  "and sistema.servicioBanco.id = :idServicioBanco "; 
		strQuery +=  "and deuda.estadoDeuda.id = :idEstadoDeuda ";	
		strQuery +=  "order by deuda.cuenta.id, deuda.anio, deuda.periodo ";

		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setDate("fechaVencimiento", fechaVencimiento)
							 .setLong("idServicioBanco", idServicioBanco)
							 .setLong("idEstadoDeuda",   idEstadoDeuda);
		
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": exit");
	
		return (ArrayList<Long>) query.list(); 
	}

	/**
	 * Obtiene una deuda por id con el convenio 
	 * seteado.
	 * 
	 * Metodo utilizado durante el Proceso de Prescripcion de Deuda
	 * para minimizar el numero de queries a la BD.
	 * 
	 * @param  idDeuda
	 * @return Deuda 
	 * */
	public Deuda getByIdWithConvenio(Long idDeuda) {
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "select deuda from "+ this.deudaClass +" deuda ";
		strQuery +=	"left join fetch deuda.convenio where deuda.id = :id";
		
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("id", idDeuda);
	
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": exit");
	
		return (Deuda) query.uniqueResult(); 
	}
	
	 /**
	  * Devuelve una lista de deuda con fecha vencimiento 
	  * menor a fechaVencimiento  
	  *
	  * @param idCuenta
	  * @param idRecurso
	  * @param idEstadoDeuda
	  * @param idViaDeuda
	  * @param fechaVencimiento
	  * @return
	  * @throws Exception
	  */
	@SuppressWarnings("unchecked") 
	public List<Deuda> getListDeudaBy(Long idCuenta, Long idRecurso, Long idEstadoDeuda,
			Long idViaDeuda, Date fechaVencimiento) throws Exception {
		 
	 	String strQuery = ""; 
	 	strQuery += "from " + this.deudaClass +" deuda ";
	 	strQuery +=	"where deuda.cuenta.id  = :idCuenta ";
	 	strQuery +=	  "and deuda.recurso.id = :idRecurso ";
	 	strQuery +=	  "and deuda.estadoDeuda.id = :idEstadoDeuda ";
	 	strQuery +=	  "and deuda.viaDeuda.id = :idViaDeuda ";
	 	strQuery +=	  "and deuda.fechaVencimiento < :fechaVencimiento ";
	 	strQuery +=	  "order by deuda.cuenta.id, anio, periodo ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idCuenta", idCuenta)
							 .setLong("idRecurso", idRecurso)
							 .setLong("idEstadoDeuda", idEstadoDeuda)
							 .setLong("idViaDeuda", idViaDeuda)
							 .setDate("fechaVencimiento", fechaVencimiento);
		
		return (ArrayList<Deuda>) query.list(); 
	 }


	
	/**
	 * Obtiene una lista de ids de deuda administrativa para una cuenta.
	 * 
	 * @param idCuenta
	 * @return List<Long>
	 */
	@SuppressWarnings("unchecked") 
	public List<Long> getListIdDeudaByIdCuenta(CuentaVO cuenta) {
		 
	 	String strQuery = ""; 
	 	strQuery += "SELECT deuda.id FROM " + this.deudaClass + " deuda ";
	 	
	 	if (cuenta.getId() != null)
	 		strQuery += "WHERE deuda.cuenta.id = " + cuenta.getId(); 
	 	else
	 		strQuery += "WHERE deuda.cuenta.numeroCuenta = '" + 
	 			StringUtil.formatNumeroCuenta(cuenta.getNumeroCuenta()) + "'";
	 	
	 	log.debug("getListIdDeudaByIdCuenta: " + strQuery);
	 	
	 	Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);

		return (ArrayList<Long>) query.list(); 
	 }
	
	/**
	 * Obtiene los totales de deuda por concepto para el atributo de asentamiento pasado. 
	 * 
	 * @param  idRecurso
	 * @param  nombreTablaDeuda
	 * @param  nombreTablaConcepto
	 * @param  fechaEmisionDesde
	 * @param  fechaEmisionHasta
	 * @param  valorAtributo
	 * @return listTotalesPorConcepto Ej:
	 * 		<br>1		145674.45</br>
	 * 		<br>2		342543.34</br>
	 */	
	public List<Object[]> getConEmiForReport(Long idRecurso, String nombreTablaDeuda,String nombreTablaConcepto,
							Date fechaEmisionDesde,	Date fechaEmisionHasta, String valorAtributo){
		log.debug("getConEmiForReport - enter");

		String queryString =" select concepto.idreccon, sum(concepto.importe) from "
								+nombreTablaDeuda+" deuda, " +nombreTablaConcepto+" concepto " +
				" where deuda.fechaVencimiento>= :fechaVtoDesde and deuda.fechaVencimiento<= :fechaVtoHasta" +
				" and deuda.idRecurso="+idRecurso +
				" and concepto.idDeuda=deuda.id and deuda.importe <> 0";
		if(valorAtributo != null)
			queryString += " and (deuda.atrAseVal like '%<AtrAse>"+valorAtributo+"</AtrAse>%' or deuda.atrAseVal='"+valorAtributo+"')";
						
		queryString +=" group by 1 order by 1";

		Session session = SiatHibernateUtil.currentSession();
 		Query query = session.createSQLQuery(queryString);
 		query.setDate("fechaVtoDesde", fechaEmisionDesde);
 		query.setDate("fechaVtoHasta", fechaEmisionHasta); 		
 			
 		List<Object[]> listReturn = (ArrayList<Object[]>) query.list();
 		
 		log.debug("Busco en la tablas:"+nombreTablaDeuda+","+nombreTablaConcepto);
 		log.debug("cant resultados:"+listReturn.size());	 	
 		
 		log.debug("getConEmiForReport - exit");
		return listReturn;
	}
	
	
	/**
	 * Devuelve Id, numero y anio de planilla para un registro de deuda.
	 * 
	 * @param deuda
	 * @return
	 */
	public String[] obtenerPlanilla(Deuda deuda) {

		Connection cn = null;
		String sql;
		
		try {
			
			cn = SiatJDBCDAO.getConnection();
			
			sql = "select pla.id, pla.nroplanilla, pla.anioplanilla from gde_plaenvdeupro pla, gde_plaenvdeupdet det " + 
				  "where pla.id = det.idplaenvdeupro and iddeuda = " + deuda.getId() +
				  " and pla.idEstPlaEnvDeuPr <> "+EstPlaEnvDeuPr.ID_ANULADA +
				  " and pla.idEstPlaEnvDeuPr <> "+EstPlaEnvDeuPr.ID_CANCELADA;
			
			String[] result = null; 
			
			ResultSet rs = cn.createStatement().executeQuery(sql);
			if (rs.next()) {
				result = new String[3];
				
				result[0] = rs.getString(1);
				result[1] = rs.getString(2);
				result[2] = rs.getString(3);
				
				log.debug(" planilla: " + result[0] + " - " + result[1] + "/" + result[2]);
			}

			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try { cn.close(); } catch (Exception ex) {}
		}
		
	}

	
}


