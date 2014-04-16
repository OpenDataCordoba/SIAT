//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.buss.dao.SiatJDBCDAO;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.Tributo;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.CuentaObjImpSearchPage;
import ar.gov.rosario.siat.gde.iface.model.CuentasProcuradorSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import coop.tecso.demoda.buss.dao.JdbcUtil;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class CuentaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CuentaDAO.class);	
	private static Logger logSt = Logger.getLogger("migrania.statistics");
	
	private static long migId = -1;
	
	public CuentaDAO() {
		super(Cuenta.class);
	}

	/**
	 * Obtiene cuentas de un tributo por nro cuenta y codigo gestion personal.
	 *
	 * @param tributo
	 * @param nroCuenta
	 * @param codGestionPersonal
	 * @return Cuenta
	 * @throws Exception
	 */
	public Cuenta findByTributoNroCuentaCodGestion(Tributo tributo, String nroCuenta, String codGesCue) throws Exception {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "select c from Cuenta c " +
							"	where c.tributo = :tributo " +
							"	  and c.numeroCuenta = :numeroCuenta " +
							"     and c.codGesCue = :codGesCue";
    	Query query = session.createQuery(sQuery)
    					.setEntity("tributo", tributo )
    					.setString("numeroCuenta", nroCuenta)
    					.setString("codGesCue", codGesCue);
    	return (Cuenta) query.uniqueResult();
    }
	
	public List<Cuenta> getListBySearchPage(CuentaSearchPage cuentaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "FROM Cuenta c ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CuentaSearchPage: " + cuentaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (cuentaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
		  queryString += " (c.fechaBaja IS NULL OR c.fechaBaja > TO_DATE('" + 
					DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}
		
		// Filtros aqui recurso, nro cuenta y id contribuyente
		  
		// filtro por recurso
 		if (!ModelUtil.isNullOrEmpty(cuentaSearchPage.getCuentaTitular().getCuenta().getRecurso())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " c.recurso.id = " + 
			cuentaSearchPage.getCuentaTitular().getCuenta().getRecurso().getId();	
			flagAnd = true;
		}

		// filtro por numero de cuenta
 		String numeroCuentaStr = cuentaSearchPage.getCuentaTitular().getCuenta().getNumeroCuenta();
 		if (!StringUtil.isNullOrEmpty(numeroCuentaStr)) {
            queryString += flagAnd ? " and " : " where ";
            if (numeroCuentaStr.startsWith("-") && StringUtil.isNumeric(numeroCuentaStr)){
            	queryString += " c.id = " + numeroCuentaStr.substring(1);
    		} else {
    			queryString += " c.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuentaStr) + "'";
    		}
			flagAnd = true;
		}
 		 
 		// filtro por contribuyente titular de cuenta
 		ContribuyenteVO contribuyente = cuentaSearchPage.getCuentaTitular().getContribuyente();
 		if (!ModelUtil.isNullOrEmpty(contribuyente)) {
            queryString += flagAnd ? " and " : " where ";
            
            String queryCuentaTitular = "SELECT ct.cuenta.id FROM CuentaTitular ct WHERE ct.contribuyente.id = " + contribuyente.getId();
			queryString += " c.id IN (" + queryCuentaTitular +")"  ; 
			flagAnd = true;
		}
 		
 		// Order By - Solo agregamos orden si numero de cuenta o contribuyente no son nulos		
 		if (!StringUtil.isNullOrEmpty(numeroCuentaStr) || !ModelUtil.isNullOrEmpty(contribuyente)){
 			queryString += " ORDER BY c.recurso.desRecurso, c.fechaAlta ";		
 		}
 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		//List<Cuenta> listCuenta = (ArrayList<Cuenta>) executeCountedSearch("c.id", queryString, cuentaSearchPage);
	    List<Cuenta> listCuenta = (ArrayList<Cuenta>) executeCountedSearch(queryString, cuentaSearchPage);
	    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCuenta;
	}

	public Long createJdbc(Cuenta c) throws Exception{
		 long msPasados=0;
		 msPasados = System.currentTimeMillis();
		 long runId = 0;
		 String sql = "";
		 sql += " insert into pad_cuenta"; 
		 sql += " (idrecurso,idobjimp,numerocuenta,codgescue,iddomicilioenvio,";
		 sql += " fechaalta,fechabaja,usuario,fechaultmdf,estado)"; 
		 sql += " values (?,?,?,?,?,?,?,?,?,?,?,?)";
		 Connection cn = SiatHibernateUtil.currentSession().connection();

		 PreparedStatement ps = cn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		 ps.setLong(1, c.getRecurso().getId());
		 if(c.getObjImp()!=null){
			 ps.setLong(2, c.getObjImp().getId());	 
		 }else{
			 ps.setNull(5, java.sql.Types.INTEGER );
		 }
		 ps.setString(3, c.getNumeroCuenta());
		 ps.setString(4, c.getCodGesCue());
		 if(c.getDomicilioEnvio()!=null){
			 ps.setLong(5, c.getDomicilioEnvio().getId());	 
		 }else{
			 ps.setNull(5, java.sql.Types.INTEGER );
		 }
		 //ps.setInt(6, c.getCambioTitularIF());
		 //ps.setInt(7, c.getCambioObjImpIF());
		 ps.setTimestamp(6, new Timestamp(c.getFechaAlta().getTime()));//Timestamp.valueOf(DateUtil.formatDate(c.getFechaAlta(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));
		 if(c.getFechaBaja()!=null){
			 ps.setTimestamp(7, new Timestamp(c.getFechaBaja().getTime()));//Timestamp.valueOf(DateUtil.formatDate(c.getFechaBaja(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));		 
		 }else{
			 ps.setNull(7, java.sql.Types.TIMESTAMP );
		 }
		 ps.setString(8, "siat");
		 ps.setTimestamp(9, new Timestamp(Calendar.getInstance().getTimeInMillis()));//Timestamp.valueOf(DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));		 
		 ps.setInt(10, 1);
		 
		 ps.executeUpdate();
	     runId = this.getSerial(ps);
		 ps.close();
		 c.setId(runId);
		 msPasados = System.currentTimeMillis() - msPasados;
	     logSt.info("Save in db: "+c.getClass().getName()+" ----------------- t = "+msPasados+" mseg");
	   
		 return runId;
	 }
 
	 /**
	  * Obtiene la lista de cuentas secundarias Activas a partir de un objeto Imponible
	  * @param objImp Objeto imponible con id cargado.
	  * @return Lista de cuentas activas. Lista vacia si no posee.
	  * @throws Exception
	  */
	 public List<Cuenta> getListCuentaSecundariaActivaByObjImp(ObjImp objImp) throws Exception {
		 
		 String queryString = "from Cuenta t where t.objImp.id = " + objImp.getId()+
		 					  " and t.recurso.esPrincipal = " + SiNo.NO.getId()+
		 					  " and (t.fechaBaja IS NULL OR t.fechaBaja > TO_DATE('" + 
		 					  	DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";

		 Session session = SiatHibernateUtil.currentSession();

		 Query query = session.createQuery(queryString);
		 
		 return  (ArrayList<Cuenta>) query.list();
	 }
	 
	 
	 /**
	  * Obtiene la cuenta activa asociada al objeto imponible y al recurso.
	  * @param objImp Objeto imponible con id cargado, recurso Recurso con id cargado.
	  * @return	Cuenta activa. Si no posee null
	  * @throws Exception
	  */
	 public Cuenta getCuentaActivaByObjImpYRecurso(ObjImp objImp, Recurso recurso) throws Exception {
		 	
		 	Cuenta cuenta;
			
		    String queryString = "from Cuenta t where t.recurso.id = " + recurso.getId() + 
		    					 " and t.objImp.id = " + objImp.getId()+
		    					 " and (t.fechaBaja IS NULL OR t.fechaBaja > TO_DATE('" + 
		    						DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";;
			
			Session session = SiatHibernateUtil.currentSession();
			log.debug("queryString: "+queryString);

			Query query = session.createQuery(queryString);
			cuenta = (Cuenta) query.uniqueResult();	

			return cuenta; 
	 }

	 
	 /**
	  * Obtiene la cuenta activa asociada al objeto imponible y al recurso y a una fecha.
	  * @param objImp Objeto imponible con id cargado, recurso Recurso con id cargado.
	  * @return	Cuenta activa. Si no posee null
	  * @throws Exception
	  */
	 public Cuenta getCuentaActivaByObjImpYRecursoYFecha(ObjImp objImp, Recurso recurso, Date fecha) throws Exception {
		 	
		 	Cuenta cuenta;
			
		    String queryString = "from Cuenta t where t.recurso.id = " + recurso.getId() + 
		    					 " and t.objImp.id = " + objImp.getId()+
		    					 " and (t.fechaBaja IS NULL OR t.fechaBaja > TO_DATE('" + 
		    						DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";;
			
			Session session = SiatHibernateUtil.currentSession();
			log.debug("queryString: "+queryString);

			Query query = session.createQuery(queryString);
			cuenta = (Cuenta) query.uniqueResult();	

			return cuenta; 
	 }

	 
	 
	 /**
	  * Obtiene la cuenta asociada al objeto imponible y al recurso.
	  * @param objImp Objeto imponible con id cargado, recurso Recurso con id cargado.
	  * @return	Cuenta. Si no posee null
	  * @throws Exception
	  */
	 public Cuenta getCuentaByObjImpYRecurso(ObjImp objImp, Recurso recurso) throws Exception {
		 	
		 	Cuenta cuenta;
			
		    String queryString = "from Cuenta t where t.recurso.id = " + recurso.getId() + 
		    					 " and t.objImp.id = " + objImp.getId();
			
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString);
			cuenta = (Cuenta) query.uniqueResult();	

			return cuenta; 
	 }
	 
	 /**
	  * Obtiene las cuentas activas asociadas al objeto imponible.
	  * @param objImp Objeto imponible con id cargado.
	  * @return	List<Cuenta> activa. Si no posee null
	  * @throws Exception
	  */
	 public List<Cuenta> getListActivaByObjImp(ObjImp objImp) throws Exception {

		 String queryString = "from Cuenta t where t.objImp.id = " + objImp.getId()+
		 					  " and (t.fechaBaja IS NULL OR t.fechaBaja > TO_DATE('" + 
		 					  	DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";

		 Session session = SiatHibernateUtil.currentSession();

		 Query query = session.createQuery(queryString);
		 return  (ArrayList<Cuenta>) query.list();

	 }
	 
	 public List<Cuenta> getListByNumeroCuenta(String nroCuenta) {
		String queryString = "from Cuenta t where t.numeroCuenta = '" + nroCuenta + "'";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		return  (ArrayList<Cuenta>) query.list();
	}
	 
	 public List<Cuenta> getListByNumerosCuenta(List<String> nroCuenta) {
			String queryString = "from Cuenta t where t.numeroCuenta in (" + ListUtil.getStringList1(nroCuenta) + ")";
			
			Session session = SiatHibernateUtil.currentSession();
			
			Query query = session.createQuery(queryString);
			return  (ArrayList<Cuenta>) query.list();
	}
	 
	 /**
	  * Busca una cuenta por idRecurso y Numero Cuenta, 
	  * si no encuentra la combinacion devuelve null.  
	  * 
	  * @author Cristian
	  * @param idRecurso
	  * @param numeroCuenta
	  * @return
	  * @throws Exception
	  */
	 public Cuenta getByIdRecursoYNumeroCuenta(Long idRecurso, String numeroCuenta, Estado estado) throws Exception {
		 Cuenta cuenta;

		 String queryString = "from Cuenta t where t.recurso.id = " + idRecurso;

		 if (numeroCuenta.startsWith("-") && StringUtil.isNumeric(numeroCuenta)){
			 queryString += " and t.id = " + numeroCuenta.substring(1) ;
		 } else {  	
			 queryString += " and t.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
		 }

		 //TODO: puede traer problemas a causa de issue #8121
		 if (estado != null) {
			 queryString += " and t.estado = " + estado.getId();
		 }

		 Session session = SiatHibernateUtil.currentSession();

		 Query query = session.createQuery(queryString);

		 try{
			 cuenta = (Cuenta) query.uniqueResult();	

		 } catch (Exception e) {
			 // Si no encuentra la cuenta, o encuentra mas de un numero de cuenta para el recurso
			 log.error("Error:", e);
			 return null;			
		 } 

		 return cuenta; 
	 }
	 
	 /**
	  * Devuelve la cuenta por numero y recurso en cualquier estado
	  * @param idRecurso
	  * @param numeroCuenta
	  * @param estado
	  * @return
	  * @throws Exception
	  */
	 public Cuenta getByIdRecursoYNumeroCuentaForEC(Long idRecurso, String numeroCuenta) throws Exception {
		    Cuenta cuenta;
			
		    String queryString = "from Cuenta t where t.recurso.id = " + idRecurso + 
		    					 " and t.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
		    
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString);
			try{
				cuenta = (Cuenta) query.uniqueResult();	
			} catch (Exception e) {
				// Si no encuentra la cuenta, o encuentra mas de un numero de cuenta para el recurso
				return null;				
			} 

			return cuenta; 
		} 
	
	@Deprecated  
	public Cuenta getByIdRecursoYNumeroCuentaYCodGesPer(Long idRecurso, String numeroCuenta, String codGesPer, Estado estado) throws Exception {
	    Cuenta cuenta;
		
	    String queryString = "from Cuenta t where t.recurso.id = " + idRecurso + 
	    					 " and t.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) +  "'" +
	    					 " and t.codGesCue = '" + StringUtil.escapar(codGesPer).trim() + "'";
		
	    if (estado != null) {
	    	queryString += " and t.estado = " + estado.getId();
	    }
	    
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		try{
			cuenta = (Cuenta) query.uniqueResult();	
		} catch (Exception e) {
			// Si no encuentra la cuenta, o encuentra mas de un numero de cuenta para el recurso
			return null;				
		} 

		return cuenta; 
	}
	 
	/**
	 * Obtiene las cuentas relacionadas por el Objeto Imponible, a la que recibe como paramtro y la excluye. 
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @return
	 */
	public List<Cuenta> getListCuentasRelacionadas(Cuenta cuenta) throws Exception {
		 
		 String queryString = "from Cuenta c where c.objImp.id = " + cuenta.getObjImp().getId() + 
		 					  " and c.id <> " + cuenta.getId();

		 Session session = SiatHibernateUtil.currentSession();

		 Query query = session.createQuery(queryString);
		 return  (ArrayList<Cuenta>) query.list();
		 
	 }
	 
	 public Iterator getListNroCtaYIdCta(){
		 
		 Session session = SiatHibernateUtil.currentSession();
		 Query query = session.createQuery("select c.id, c.numeroCuenta from Cuenta c");
		 
		 return query.iterate();
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
	  *  Inserta una linea con los datos de la Cuenta para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param cuenta, output - Cuenta a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(Cuenta o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|idrecurso|idobjimp|numerocuenta|codgescue|iddomicilioenvio|idbroche|fechaalta|fechabaja|usuario|fechaultmdf|estado 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getRecurso().getId());		 
		 line.append("|");
		 if (o.getObjImp()==null)
			 line.append("");		 
		 else
			  line.append(o.getObjImp().getId());		 
		 line.append("|");
		 line.append(o.getNumeroCuenta());
		 line.append("|");
		 line.append(o.getCodGesCue());
		 line.append("|");
		 if(o.getDomicilioEnvio()!=null){
			 line.append(o.getDomicilioEnvio().getId());
		 } // Si es null no se inserta nada, viene el proximo pipe.
		 line.append("|");
		 if(o.getBroche()!=null){
			 line.append(o.getBroche().getId());
		 } // Si es null no se inserta nada, viene el proximo pipe.
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaAlta(), "yyyy-MM-dd"));
		 line.append("|");
		 if(o.getFechaBaja()!=null){
			line.append(DateUtil.formatDate(o.getFechaBaja(), "yyyy-MM-dd"));		 
		 } // Si es null no se inserta nada, viene el proximo pipe.
		 line.append("|");
		 line.append(DemodaUtil.currentUserContext().getUserName());
		 line.append("|");
		 
		 line.append("2010-01-01 00:00:00");
		 
		 line.append("|");
		 line.append(o.getEstado());

		 line.append("|");
		 if(o.getNomTitPri()!=null)
			 line.append(o.getNomTitPri());
		 line.append("|");
		 
		 if (o.getDesDomEnv()!=null)
			 line.append(o.getDesDomEnv());		 
		 line.append("|");

		 if (o.getPermiteImpresion() != null)
			 line.append(o.getPermiteImpresion());
		 line.append("|");

		 if (o.getCuitTitPri() != null)
			 line.append(o.getCuitTitPri());
		 line.append("|");

		 if (o.getObservacion() != null)
			 line.append(o.getObservacion());
		 line.append("|");

		 output.addline(line.toString());
		 
		 // Seteamos el id generado en el bean.
		 o.setId(id);
	
		 return id;
	 }

	/**
	 * Obtiene el total de cuentas distintas de las deudas para la Seleccion Almacenada y la via de la deuda 
	 * @param  selAlmDeuda
	 * @param  viaDeuda
	 * @return Long
	 */ 
/*
	 public Long getTotalDistintas(SelAlmDeuda selAlmDeuda, ViaDeuda viaDeuda) {

		Session session = SiatHibernateUtil.currentSession();

		String queryString = "";
		if(viaDeuda.getEsViaAdmin()){
			queryString = "SELECT COUNT (DISTINCT deuda.cuenta) " +
			"FROM DeudaAdmin deuda, SelAlmDet sad " +
			"WHERE sad.selAlm = :selAlm " +
			"AND sad.idElemento = deuda.id ";
			
		}else if(viaDeuda.getEsViaJudicial()){
			queryString = "SELECT COUNT (DISTINCT deuda.cuenta) " +
			"FROM DeudaJudicial deuda, SelAlmDet sad " +
			"WHERE sad.selAlm = :selAlm " +
			"AND sad.idElemento = deuda.id ";
		}
		
		Query query = session.createQuery(queryString)
			.setEntity("selAlm", selAlmDeuda);

		Long totalBySelAlmDeuda = (Long) query.uniqueResult();	

		return totalBySelAlmDeuda; 
	}
*/	
	/**
	 * Obtiene el total de cuentas distintas para la Seleccion Almacenada y el tipo Detalle de Seleccion Almacenada
	 *  
	 * @param  selAlmDeuda
	 * @param  tipoSelAlmDet
	 * @return Long
	 */
	public Long getTotalDistintas(SelAlmDeuda selAlmDeuda, TipoSelAlm tipoSelAlmDet) {

		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "";
		if(tipoSelAlmDet.getEsTipoSelAlmDetDeudaAdm()){
			queryString = "SELECT COUNT (DISTINCT deuda.cuenta) " +
			"FROM DeudaAdmin deuda, SelAlmDet sad " +
			"WHERE sad.selAlm = :selAlm " +
			"AND sad.idElemento = deuda.id " +
			"AND sad.tipoSelAlmDet = :tipoSelAlmDet";
			
		}else if(tipoSelAlmDet.getEsTipoSelAlmDetDeudaJud()){
			queryString = "SELECT COUNT (DISTINCT deuda.cuenta) " +
			"FROM DeudaJudicial deuda, SelAlmDet sad " +
			"WHERE sad.selAlm = :selAlm " +
			"AND sad.idElemento = deuda.id " +
			"AND sad.tipoSelAlmDet = :tipoSelAlmDet";
			
		}else if(tipoSelAlmDet.getEsTipoSelAlmDetConvCuot() ){
			queryString = "SELECT COUNT (DISTINCT convCuo.convenio.cuenta) " +
			"FROM ConvenioCuota convCuo, SelAlmDet sad " +
			"WHERE sad.selAlm = :selAlm " +
			"AND sad.idElemento = convCuo.id " +
			"AND sad.tipoSelAlmDet = :tipoSelAlmDet";
		}
		
		Query query = session.createQuery(queryString)
			.setEntity("selAlm", selAlmDeuda)
			.setEntity("tipoSelAlmDet", tipoSelAlmDet);

		Long totalBySelAlmDeuda = (Long) query.uniqueResult();	

		return totalBySelAlmDeuda; 
	}

	

	/**
	 * Realiza una busqueda paginada de cuentas que posean deudas en gestion judicial. Es decir viaDeuda = JUDICIAL y estadoDeuda = JUDICIAL
	 * Y filtra por recurso si el id seteado es >0 
	 * @param searchPage
	 * @return
	 */
	public List<Cuenta> getCuentasConDeudajudicial(CuentasProcuradorSearchPage searchPage) throws Exception {

		Session session = SiatHibernateUtil.currentSession();

		String queryString = "SELECT DISTINCT deudaJudicial.cuenta " +
				"FROM DeudaJudicial deudaJudicial " +
				"WHERE deudaJudicial.viaDeuda.id = " +ViaDeuda.ID_VIA_JUDICIAL + 
				" AND deudaJudicial.estadoDeuda.id ="+EstadoDeuda.ID_JUDICIAL;
		
		if(searchPage.getIdRecurso()>0)
			queryString+=" AND deudaJudicial.cuenta.recurso.id="+searchPage.getIdRecurso();
		
		if(!ModelUtil.isNullOrEmpty(searchPage.getProcuradorVO()))
			queryString+=" AND deudaJudicial.procurador.id="+searchPage.getProcuradorVO().getId();

		if(!StringUtil.isNullOrEmpty(searchPage.getCuenta().getNumeroCuenta())){
			String numeroCuentaNoCero = StringUtil.quitarCerosIzq(searchPage.getCuenta().getNumeroCuenta());
			queryString+=" AND deudaJudicial.cuenta.numeroCuenta='" + numeroCuentaNoCero + "'";
		}
		
		List<Cuenta> listCuenta = (ArrayList<Cuenta>) executeSearch(queryString, searchPage);	

		return listCuenta; 
	}

	 /**
	  * Obtiene las cuentas activas asociadas a un objeto impobilble en un CuentaObjImpSearchPage.
	  * (Se agrega filtro por lista de idRecurso habilitados por Area, solicitado en mantis 4752)
	  * @param cuentaObjImpSearchPage.
	  * @return	List<Cuenta> activa. Si no posee null
	  * @throws Exception
	  */
	 public List<Cuenta> getListActivaByCuentaObjImpSearchPage(CuentaObjImpSearchPage cuentaObjImpSearchPage) throws Exception {

		 String queryString = "from Cuenta t where t.objImp.id = " + cuentaObjImpSearchPage.getObjImp().getId()+
		 					  " and (t.fechaBaja IS NULL OR t.fechaBaja > TO_DATE('" + 
		 					  	DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";

		// Se agrega filtro por lista de idRecurso habilitados por Area (solicitado en mantis 4752)
		List<Recurso> listRecurso = Recurso.getListTributarios();
		
	 	if(!ListUtil.isNullOrEmpty(listRecurso)){
	 		String listIdRecurso = ListUtil.getStringIds(listRecurso);
	 		queryString += " and t.recurso.id in ("+listIdRecurso+")";	 		
	 	}else{
	 		return new ArrayList<Cuenta>();
	 	}
		 
		 return  (ArrayList<Cuenta>) executeCountedSearch(queryString, cuentaObjImpSearchPage);

	 }
	
	 //-----------------------------------------------------
	 public List<Cuenta> getCuentaActivaByListIdsYRecurso(Long[] listIdsObjImp, Recurso recurso) throws Exception {
		 	List<Cuenta> listCuenta;
			
		    String queryString = "from Cuenta t where t.recurso.id = " + recurso.getId() + 
		    					 " and t.objImp.id in (" + StringUtil.getStringComaSeparate(listIdsObjImp) + ")" +
		    					 " and (t.fechaBaja IS NULL OR t.fechaBaja > TO_DATE('" + 
		    						DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
			
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString);
			listCuenta = (ArrayList<Cuenta>) query.list();	

			return listCuenta; 
	 }
	 //-----------------------------------------------------

	 // Cuenta,ObjImp
	 public List<Object[]> getListCuentaActivaByIdsStrValor(Long idTipObjImp, Long idTipObjImpAtr, 
			 String strValor, Long skip, Long first, Date fechaEmision) throws Exception {
		 
		 	String queryString = "SELECT SKIP " + skip + " FIRST " + first + " cta.* , oi.* " + 
				"FROM pad_cuenta cta "+
				"INNER JOIN pad_objimp oi ON (cta.idobjimp == oi.id) "+
				"INNER JOIN pad_objimpatrval oiav ON (oi.id == oiav.idobjimp) "+ 
				"WHERE oi.idTipObjImp == " + idTipObjImp + " " +
				"AND cta.idRecurso = 14 " + // recurso TGI
				// 2008-12-01: No filtramos las cuentas excluidas, sino que las emitimos con rt 5
				//"AND (cta.esExcluidaEmision <> " + SiNo.SI.getBussId() + " OR cta.esExcluidaEmision is null) " + // cuentas no excluidas
				"AND oiav.idTipObjImpAtr == " + idTipObjImpAtr + " " +
				"AND oiav.strvalor == '" + strValor + "' " +
				"AND (oiav.fechaHasta IS NULL " + 
					"OR oiav.fechaHasta > " +
					"TO_DATE('" + DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) " +
				"AND (cta.fechaBaja IS NULL " + 
					"OR cta.fechaBaja > " +
					"TO_DATE('" + DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) " + 
				"ORDER BY cta.id";
			
		 	log.debug("queryString: " + queryString);
		 	Session session = SiatHibernateUtil.currentSession();

			Query query = session.createSQLQuery(queryString).addEntity("cta", Cuenta.class).addEntity("oi", ObjImp.class); //   addJoin("oi","cta.objImp");
			
			return (ArrayList<Object[]>) query.list(); 
	 }

	 /**
	  * Devuelve una lista paginada de cuentas para un id de recurso. 
	  * 
	  * 
	  * @author Cristian
	  * @param idRecurso
	  * @param skip
	  * @param first
	  * @return
	  * @throws Exception
	  */
	 public List<Cuenta> getListByRecurso(Long idRecurso, Long skip, Long first) throws Exception {
		 	List<Cuenta> listCuenta;
			
		    String queryString = "SELECT SKIP " + skip + " FIRST " + first + " cuenta.* " +
		    					 "FROM pad_cuenta cuenta " +
		    					 "WHERE cuenta.idRecurso = " + idRecurso;
			
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createSQLQuery(queryString).addEntity("cuenta", Cuenta.class);
			listCuenta = (ArrayList<Cuenta>) query.list();

			return listCuenta; 
	 }
	 
	 @Deprecated
	 public List<Cuenta>getListByRecursoContrib (Long idRecurso, Long idContribuyente)throws Exception{
		 List<Cuenta> listCuenta;

		 String queryString = "SELECT c FROM Cuenta c, CuentaTitular ct WHERE c.id = ct.cuenta.id ";
		 queryString += " AND c.recurso.id = "+idRecurso;
		 queryString += " AND ct.idContribuyente = " + idContribuyente;
		 queryString += " AND c.estado = 1";

		 Session session = SiatHibernateUtil.currentSession();
		 Query query = session.createQuery(queryString);

		 listCuenta = (List<Cuenta>)query.list();

		 return listCuenta;
	 }
	 
	 public List<Cuenta> getListByContrib (Long idContribuyente)throws Exception{
		 List<Cuenta> listCuenta;

		 String queryString = "SELECT c FROM Cuenta c, CuentaTitular ct WHERE c.id = ct.cuenta.id ";
				queryString += " AND ct.idContribuyente = " + idContribuyente;
				queryString += " AND c.estado = 1";  //TODO: puede traer problemas a causa de issue #8121
		 Session session = SiatHibernateUtil.currentSession();
		 Query query= session.createQuery(queryString);
		 listCuenta = (List<Cuenta>)query.list();

		 return listCuenta;
	 }

	 /**
	  * Obtiene la lista de cuentas excluidas
	  * @return List<Cuenta>
	  */
	 public List<Cuenta> getListCuentasExcluidas() {
		 Session session = SiatHibernateUtil.currentSession();

		 String sQuery = "SELECT DISTINCT (ces.cuenta) FROM CueExcSel ces " + 
		 				 "WHERE ces.estado = :estActivo";

		 Query query = session.createQuery(sQuery)
		 .setInteger("estActivo", Estado.ACTIVO.getId());

		 return  (ArrayList<Cuenta>) query.list();
	 }

	 /**
	  * Retorna la cantidad de cuentas para el recurso   
	  * con id idRecurso
	  * 
	  * @param idRecurso
	  * @return Long
	  * @throws Exception
	  */
	 public Long getCountByRecurso(Long idRecurso) throws HibernateException {

		    String queryString = "select count(*) from Cuenta cuenta " +
		    					 "where cuenta.recurso.id = :idRecurso";

			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString).setLong("idRecurso", idRecurso);
									
			return (Long) query.uniqueResult(); 
	 }

	 @SuppressWarnings("unchecked")
	 public List<Long> getListCuentaActivasBy(Recurso recurso, TipObjImpAtr tipObjImpAtr, 
			 String atrVal, Date fechaAnalisis) throws Exception {
	 	
		 if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");

		 Session session = SiatHibernateUtil.currentSession();
				
		String strQuery = "";
		strQuery += "select cuenta.id from Cuenta cuenta, ObjImp objImp, ObjImpAtrVal objImpAtrVal "; 
		strQuery += "where cuenta.recurso.id = :idRecurso ";
		strQuery +=   "and (cuenta.fechaBaja is null or cuenta.fechaBaja > :fecha) ";
		strQuery +=   "and (cuenta.esExcluidaEmision is null or cuenta.esExcluidaEmision <> :si) ";
		strQuery +=   "and cuenta.objImp.id = objImp.id ";
		strQuery +=   "and objImp.tipObjImp.id = :idTipObjImp ";
		strQuery +=   "and objImpAtrVal.objImp.id = objImp.id ";
		strQuery +=   "and objImpAtrVal.tipObjImpAtr.id = :idTipObjImpAtr ";
		strQuery +=   "and objImpAtrVal.strValor = :valor ";
		strQuery +=   "and (objImpAtrVal.fechaHasta is null or objImpAtrVal.fechaHasta > :fecha) ";
		
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Query query = session.createQuery(strQuery)
					.setLong("idRecurso",recurso.getId())
					.setDate("fecha", fechaAnalisis)
					.setString("valor",atrVal)
					.setInteger("si",SiNo.SI.getBussId())
					.setLong("idTipObjImp",recurso.getTipObjImp().getId())
					.setLong("idTipObjImpAtr",tipObjImpAtr.getId());
		
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");
				
		return (ArrayList<Long>) query.list(); 
	 }
	 
	 @SuppressWarnings("unchecked")
	 public List<Long> getListCuentaActivasBy(Recurso recurso, RecAtrCue recAtrCue, String atrVal,
			 Date fechaAnalisis) throws Exception {

		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");

		Session session = SiatHibernateUtil.currentSession();
	
		String strQuery = "";
		strQuery += "select cuenta.id from Cuenta cuenta, RecAtrCue recAtrCue, RecAtrCueV recAtrCueV ";
		strQuery += "where cuenta.recurso.id = :idRecurso ";
		strQuery +=   "and (cuenta.fechaBaja is null or cuenta.fechaBaja > :fecha) ";
		strQuery +=   "and (cuenta.esExcluidaEmision is null or cuenta.esExcluidaEmision <> :si) ";
		strQuery +=   "and recAtrCue.id = :idRecAtrCue ";
		strQuery +=   "and cuenta.recAtrCueV.id = recAtrCue.id";
		strQuery +=   "and cuenta.recAtrCueV.valor = :valor ";
		strQuery +=   "and (recAtrCueV.fechaHasta is null or recAtrCueV.fechaHasta > :fecha) ";
		strQuery +=   "and cuenta.esExcluidaEmision != " + SiNo.SI.getBussId();

		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
			
		Query query = session.createQuery(strQuery)
					.setLong("idRecurso",recurso.getId())
					.setDate("fecha", fechaAnalisis)
					.setInteger("si",SiNo.SI.getBussId())
					.setLong("idRecAtrCue", recAtrCue.getId())
					.setString("valor",atrVal);
		
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");
			
		return (ArrayList<Long>) query.list(); 
	 }
	 
	 @SuppressWarnings("unchecked")
	 public List<Long> getListCuentaActivasBy(Recurso recurso, Date fechaAnalisis) throws Exception {

		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");

		String strQuery = "";
		Session session = SiatHibernateUtil.currentSession();


		strQuery += "select cuenta.id from Cuenta cuenta ";
		strQuery += "where cuenta.recurso.id = :idRecurso ";
		strQuery +=   "and (cuenta.fechaBaja is null or cuenta.fechaBaja > :fecha) ";
		strQuery +=   "and (cuenta.esExcluidaEmision is null or cuenta.esExcluidaEmision <> :si) ";

		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
			
		Query query = session.createQuery(strQuery)
						.setLong("idRecurso",recurso.getId())
						.setDate("fecha", fechaAnalisis)
						.setInteger("si",SiNo.SI.getBussId());
		
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");
			
		return (ArrayList<Long>) query.list(); 
	 }
	 
	 /**
	 * Obtiene una cuenta por id con el objeto 
	 * imponible seteado.
	 * 
	 * Metodo utilizado durante el Proceso de Emision de TGI
	 * para minimizar el numero de queries a la BD.
	 * 
	 * @param  idCuenta
	 * @return Cuenta
	 */
	public Cuenta getByIdWithObjImp(Long idCuenta) {
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "select cuenta from Cuenta cuenta ";
		strQuery +=	"left join fetch cuenta.objImp where cuenta.id = " + idCuenta;
				
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery);
	
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": exit");
	
		return (Cuenta) query.uniqueResult(); 
	}
	
	 /**
	  * Devuelve la lista de id de cuentas para un id de recurso. 
	  * 	  
	  * @param idRecurso
	  * @return
	  * @throws Exception
	  */
	@SuppressWarnings("unchecked") 
	public List<Long> getListIdBy(Long idRecurso) throws Exception {
		 	
		 	String strQuery = "";
		    strQuery += "select cuenta.id from Cuenta cuenta ";
		    strQuery +=	 "where cuenta.recurso.id = :idRecurso";
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(strQuery)
								 .setLong("idRecurso", idRecurso);
			
			return (ArrayList<Long>) query.list();
	 }

	public static void setMigId(long migId) {
		CuentaDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

	/**
	 * 
	 * Devuelve el utimo numero de cuenta dado un recurso.
	 * 
	 * @param idRecurso
	 * @return String
	 */	
	public Long getLastNumeroCuenta(Long idRecurso) throws Exception {
		String queryString = "SELECT max(lpad(cta.numeroCuenta, 15, '0')) ";
		queryString  += "FROM pad_cuenta cta WHERE  cta.idRecurso = " + idRecurso;
		
		log.debug("queryString: " + queryString);
	 	Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString);
		String numeroCuenta = (String)query.uniqueResult();
		if (numeroCuenta == null) {
			return 0L;
		}
		
		long nmax = 0;
		if (StringUtil.isLong(numeroCuenta)) {
			nmax = Long.parseLong(numeroCuenta.trim());
		} else {
			//escaneamos los numerocuenta y nos quedamos con el mayor numerico
			String sql = "select numerocuenta from pad_cuenta where idrecurso = " + idRecurso;
			Connection cn = null;
			ResultSet rs = null;

			cn = SiatJDBCDAO.getConnection();
			rs = cn.createStatement().executeQuery(sql);

			while (rs.next()) { 
				long nc = 0;
				try { nc = rs.getLong("numerocuenta"); } catch (Exception e) { nc = 0;}
				nmax = Math.max(nmax, nc);
			}
			rs.close();
			cn.close();
		}
		
		return Long.valueOf(nmax);
	}

	public Long getIdCuenta(Long idRecurso, String numeroCuenta) throws Exception {
		Connection cn = SiatJDBCDAO.getConnection();
		
		try {
			String sql = "select id from pad_cuenta where idrecurso = %s and numerocuenta='%s'";
			sql = String.format(sql, idRecurso, numeroCuenta);
			Map<String, Object> r = JdbcUtil.queryRow(cn, sql);
			return ((Number)r.get("id")).longValue();
		} catch (Exception e) {
			log.info("info: No se pudo obtener id cuenta desde numero y recurso.", e);
		} finally {
			cn.close();
		}
		return null;
	}

	

}


