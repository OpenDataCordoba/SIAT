//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.SelAlm;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.gde.iface.model.SelAlmAgregarParametrosSearchPage;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ConvenioCuotaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConvenioCuotaDAO.class);	
	
	private static long migId = -1;
	
	public ConvenioCuotaDAO() {
		super(ConvenioCuota.class);
	}
	
	/**
	 *  Obtiene una ConvenioCuota por si Codigo de Referencia de Pago.
	 * 
	 * @param codRefPag
	 * @return
	 */
	public ConvenioCuota getByCodRefPag(Long codRefPag){
		ConvenioCuota convenioCuota;
		String queryString = "from ConvenioCuota t where t.codRefPag = :codRefPag";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("codRefPag", codRefPag);
		convenioCuota = (ConvenioCuota) query.uniqueResult();	

		return convenioCuota; 
	}
	
	
	public List<ConvenioCuota> getByCodRefPagImporte(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
				
		String queryString = "from ConvenioCuota t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del LiqCodRefPagSearchPage: " + liqCodRefPagSearchPage.infoString()); 
		}
	
 		// filtro por Numero Convenio
 		if (liqCodRefPagSearchPage.getCodRefPag() != null) {
            queryString += flagAnd ? " and " : " where ";
			
           	queryString += "  t.codRefPag = " + liqCodRefPagSearchPage.getCodRefPag();
 			flagAnd = true;
		}
 		
 		if (liqCodRefPagSearchPage.getImporte() != null) {
            queryString += flagAnd ? " and " : " where ";
			
           	queryString += "  t.importeCuota = " + liqCodRefPagSearchPage.getImporte();
 			flagAnd = true;
		}

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ConvenioCuota> listConvenioCuota = (ArrayList<ConvenioCuota>) executeCountedSearch(queryString, liqCodRefPagSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listConvenioCuota;
	}
	
	/**
	 *  Obtiene un ConvenioCuota por su Numero de Cuota, Nro de Convenio y Nro de Sistema.
	 *  
	 * @param nroCuota
	 * @param nroConvenio
	 * @param idSistema
	 * @return
	 * @throws Exception
	 */
	public ConvenioCuota getByNroCuoNroConSis(Long nroCuota, Long nroConvenio, Long idSistema) throws Exception {
		ConvenioCuota convenioCuota;
		String queryString = "from ConvenioCuota t where t.numeroCuota = :nroCuota";
		queryString += " and t.convenio.nroConvenio = :nroConvenio and t.sistema.nroSistema = :nroSis";
	
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("nroCuota", nroCuota);
		query.setLong("nroConvenio", nroConvenio);
		query.setLong("nroSis", idSistema);
		convenioCuota = (ConvenioCuota) query.uniqueResult();	

		return convenioCuota; 
	}
	
	/**
	 *  Obtiene un ConvenioCuota por su Numero de Cuota y Id de Convenio.
	 *  
	 * @param nroCuota
	 * @param idConvenio
	 * @return
	 * @throws Exception
	 */
	public ConvenioCuota getByNroCuoIdCon(Long nroCuota, Long idConvenio) throws Exception {
		ConvenioCuota convenioCuota;
		String queryString = "from ConvenioCuota t where t.convenio.id = :idConvenio";
		queryString += " and t.numeroCuota = :nroCuota";
	
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("nroCuota", nroCuota);
		query.setLong("idConvenio", idConvenio);
		convenioCuota = (ConvenioCuota) query.uniqueResult();	

		return convenioCuota; 
	}
	
	/**
	 * Devuelve la primer cuota impaga de un convenio
	 * @param idConvenio
	 * @return
	 * @throws Exception
	 */
	public ConvenioCuota getPrimImpByIdConvenio (Long idConvenio) throws Exception {
		ConvenioCuota convenioCuota;
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM ConvenioCuota c where c.convenio.id = "+idConvenio;
		queryString += " AND c.estadoConCuo.id = "+ EstadoConCuo.ID_IMPAGO;
		queryString += " ORDER BY c.numeroCuota";
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		
		convenioCuota = (ConvenioCuota) query.uniqueResult();
		
		return convenioCuota;
		
	}
	
	/**
	 *  Obtiene la lista de Cuotas del Convenio con Fecha de Vencimiento menor a la Fecha de Pago
	 * 
	 * @param convenio
	 * @param fechaPago
	 * @return boolean
	 * @throws Exception
	 */
	public List<ConvenioCuota> getListByConvenioYFecha(Convenio convenio, Date fechaPago) throws Exception {
		List<ConvenioCuota> listConvenioCuota;
		String queryString = "from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		 queryString += " and t.fechaVencimiento < TO_DATE('" + 
			DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";

		 queryString += " order by t.numeroCuota";
		 
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		listConvenioCuota = (ArrayList<ConvenioCuota>) query.list();	

		return listConvenioCuota; 
	}

	public List<ConvenioCuota> getListByConvenioYFechaPagoHasta(Convenio convenio, Date fechaPago) throws Exception {
		List<ConvenioCuota> listConvenioCuota;
		String queryString = "from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		 queryString += " and t.fechaPago < TO_DATE('" + 
			DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";

		 queryString += " order by t.numeroCuota";
		 
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		listConvenioCuota = (ArrayList<ConvenioCuota>) query.list();	

		return listConvenioCuota; 
	}

	/**
	 * Obtiene una lista de cuotas a partir de un numero de cuota pasado como parametro
	 * @param convenio
	 * @param nroCuota
	 * @return
	 * @throws Exception
	 */
	public List<ConvenioCuota> getListByNroCuotaMayorIgualA (Convenio convenio, Integer nroCuota) throws Exception{
		List<ConvenioCuota> listConvenioCuota;
		String queryString = "from ConvenioCuota t where t.numeroCuota >= " + nroCuota;
		queryString +=" and t.convenio.id =" + convenio.getId();
		queryString += " order by t.numeroCuota";

		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		listConvenioCuota = (ArrayList<ConvenioCuota>) query.list();
		
		return listConvenioCuota;
	}
	public List<ConvenioCuota> getListPagasByNroCuotaMayorIgualA (Convenio convenio, Integer nroCuota) throws Exception{
		List<ConvenioCuota> listConvenioCuota;
		String queryString = "from ConvenioCuota t where t.numeroCuota >= " + nroCuota;
		queryString +=" and t.convenio.id =" + convenio.getId();
		queryString +=" and t.estadoConCuo.id <> " + EstadoConCuo.ID_IMPAGO;
		queryString += " order by t.fechaPago, t.numeroCuota";

		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		listConvenioCuota = (ArrayList<ConvenioCuota>) query.list();
		
		return listConvenioCuota;
	}

	/**
	 *  Valida si el Convenio pasado como parametro tiene algun pago a Cuenta en la DB.
	 *  
	 * @param convenio
	 * @return boolean
	 */
	public boolean tienePagoACuenta(Convenio convenio){
		ConvenioCuota convenioCuota;
		String queryString = "from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		queryString += " and t.estadoConCuo.id = "+EstadoConCuo.ID_PAGO_A_CUENTA;  
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		convenioCuota = (ConvenioCuota) query.uniqueResult();	
		
		if(convenioCuota!=null){
			return true;
		}else{
			return false;
		}
		
	}
	public List<ConvenioCuota> getListPagoACuentaByConvenio(Convenio convenio) throws Exception {
		List<ConvenioCuota> convenioCuota;
		String queryString ="from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		queryString += " and t.estadoConCuo.id = "+EstadoConCuo.ID_PAGO_A_CUENTA; 
		queryString += " order by t.numeroCuota, t.fechaPago";
 
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
	
		convenioCuota = (List<ConvenioCuota>) query.list();	
		return convenioCuota;
	}
	
	public List<ConvenioCuota> getListPagoACuentaByConvenioOrderByFechaPago(Convenio convenio) throws Exception {
		List<ConvenioCuota> convenioCuota;
		String queryString ="from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		queryString += " and t.estadoConCuo.id = "+EstadoConCuo.ID_PAGO_A_CUENTA; 
		queryString += " order by t.fechaPago, t.numeroCuota";
 
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
	
		convenioCuota = (List<ConvenioCuota>) query.list();	
		return convenioCuota;
	}
	
	/**
	 *  Valida si el Convenio pasado como parametro tiene algun pago Bueno en la DB.
	 *  
	 * @param convenio
	 * @return boolean
	 */
	public boolean tienePagosBuenos(Convenio convenio){
		ConvenioCuota convenioCuota;
		String queryString = "from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		queryString += " and t.estadoConCuo.id = "+EstadoConCuo.ID_PAGO_BUENO;  
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		convenioCuota = (ConvenioCuota) query.uniqueResult();	
		if(convenioCuota!=null){
			return true;
		}else{
		return false;
		}
	}
	
	/**
	 *  Valida si el Convenio pasado como parametro tiene todas las cuotas vencidas.
	 *  
	 * @param convenio
	 * @return boolean
	 */
	public boolean tieneTodasLasCuotasVencidas(Convenio convenio){
		ConvenioCuota convenioCuota;
		String queryString = "from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		queryString += " and t.fechaVencimiento > :date ";  
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setDate("date", new Date());
		query.setMaxResults(1);
		convenioCuota = (ConvenioCuota) query.uniqueResult();	
		if(convenioCuota==null){
			return true;
		}else{
		return false;
		}
	}
	
	
	/**
	 *  Valida si el Convenio pasado como parametro tiene todas las cuotas pagas y como pagos buenos.
	 *  
	 * @param convenio
	 * @return boolean
	 */
	public boolean tieneTodasCuotasPagosBuenos(Convenio convenio){
		ConvenioCuota convenioCuota;
		String queryString = "from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		queryString += " and t.estadoConCuo.id <> "+EstadoConCuo.ID_PAGO_BUENO;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		convenioCuota = (ConvenioCuota) query.uniqueResult();	

		if(convenioCuota != null)
			return false;
		else
			return true;
	}
	
	/**
	 *  Valida si el Convenio pasado como parametro tiene todas las cuotas pagas y al menos un pagos a cuenta.
	 *  
	 * @param convenio
	 * @return boolean
	 */
	public boolean pagoConCuotasPagosACuenta(Convenio convenio){
		ConvenioCuota convenioCuota;
		String queryString = "from ConvenioCuota t where t.convenio.id = "+convenio.getId();
	
		queryString += " and t.estadoConCuo.id = "+EstadoConCuo.ID_IMPAGO;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		convenioCuota = (ConvenioCuota) query.uniqueResult();	

		if(convenioCuota != null)
			return false;
		else{
			if(this.tienePagoACuenta(convenio)){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 *  Busca la mayor de las Fecha de Pago de las Cuotas del Convenio.
	 *  
	 * @param convenio
	 * @return Date
	 */
	public Date ultimaFechaPago(Convenio convenio){
		String queryString = "select max(t.fechaPago) from ConvenioCuota t where t.convenio.id = "+convenio.getId();
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		Date fechaPago = (Date) query.uniqueResult();

		return fechaPago;
	}
	
	public List<ConvenioCuota> getPagosBuenosbyIdConvenio (Long idConvenio){
		List<ConvenioCuota>listConvenioCuota;
		
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "FROM ConvenioCuota c WHERE c.convenio.id = "+ idConvenio;
		queryString += " AND c.estadoConCuo.id = "+ EstadoConCuo.ID_PAGO_BUENO;
		queryString += " ORDER BY c.nroCuotaImputada ASC";
		
		Query query = session.createQuery(queryString);
		
		listConvenioCuota = (List<ConvenioCuota>)query.list();
		
		return listConvenioCuota;
		
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
	 *  Inserta una linea con los datos del ConvenioCuota para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param convenioCuota, output - El ConvenioCuota a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(ConvenioCuota o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idConvenioCuota.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		// Obtenemos el valor del id autogenerado a insertar.
		//long id = getNextId(output.getPath(), output.getNameFile());
		 
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|idconvenio|idsistema|codrefpag|numerocuota|fechavencimiento|capitalcuota|interes|importecuota|fechaemision|fechapago|nrocuotaimputada|idliqcompro|idestadoconcuo|tipoPago|idPago|bancoPago|actualizacion|usuario|fechaultmdf|estado|

		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getConvenio().getId());
		line.append("|");
		line.append(o.getSistema().getId());		 
		line.append("|");
		line.append(o.getCodRefPag());		 
		line.append("|");
		line.append(o.getNumeroCuota());		 
		line.append("|");
		if(o.getFechaVencimiento()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaVencimiento(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(decimalFormat.format(o.getCapitalCuota()));
		line.append("|");
		line.append(decimalFormat.format(o.getInteres()));
		line.append("|");
		line.append(decimalFormat.format(o.getImporteCuota()));
		line.append("|");
		if(o.getFechaEmision()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaEmision(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getFechaPago()!=null){ 
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(o.getNroCuotaImputada());		 
		line.append("|");
		if(o.getIdLiqComPro()!=null){
			line.append(o.getIdLiqComPro());		 			
		}// Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(o.getEstadoConCuo().getId());		 
		line.append("|");
		if(o.getTipoPago()!=null){
			line.append(o.getTipoPago().getId());		 			
		}
		line.append("|");
		if(o.getIdPago()!=null){
			line.append(o.getIdPago());		 			
		}
		line.append("|");
		if(o.getBancoPago()!=null){			
			line.append(o.getBancoPago().getId());		 
		}
		line.append("|");
		if(o.getActualizacion()!=null)
			line.append(decimalFormat.format(o.getActualizacion()));
		line.append("|");
		if(o.getImportePago()!=null)
			line.append(decimalFormat.format(o.getImportePago()));
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

	public List<ConvenioCuota> getList(Long idConvenio, Date fechaPagoDesde,Date fechaPagoHasta, 
			boolean liquidadas, boolean noLiquidadas, Long idEstadoConCuo) throws Exception {
		Session session = SiatHibernateUtil.currentSession();
		Query query ;
		String queryString;
		
		queryString ="from ConvenioCuota t ";
		boolean flagAnd = false;

		// filtro por procurador		
		if(idConvenio!=null && idConvenio.longValue()>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.convenio.id="+ idConvenio; 
			flagAnd = true;
		}
		
		// filtro por fecha pago desde
		if(fechaPagoDesde!=null){
			  queryString += flagAnd ? " AND " : " 	WHERE ";
			  queryString += "t.fechaPago >= TO_DATE('" + 
				DateUtil.formatDate(fechaPagoDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			  flagAnd = true;
		}

		// filtro por fecha pago hasta
		if(fechaPagoHasta!=null){
			  queryString += flagAnd ? " AND " : " 	WHERE ";
			  queryString += "t.fechaPago <= TO_DATE('" + 
				DateUtil.formatDate(fechaPagoHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			  flagAnd = true;
		}		
		
		// filtro por liquidadas y no liquidadas
		if(liquidadas){
			queryString += flagAnd ? " AND " : " 	WHERE ";
			queryString += " t.idLiqComPro IS NOT NULL";
			flagAnd = true;
		}
		
		if(noLiquidadas){
			queryString += flagAnd ? " AND " : " 	WHERE ";
			queryString += " t.idLiqComPro IS NULL";
			flagAnd = true;			
		}	
		  
		if(idEstadoConCuo!=null){
			queryString += flagAnd ? " AND " : " 	WHERE ";
			queryString += " t.estadoConCuo.id="+idEstadoConCuo;
			flagAnd = true;
		}
						 
		query = session.createQuery(queryString);
			
		log.debug("Va a buscar convenioCuota - query:"+queryString);
		return (ArrayList<ConvenioCuota>) query.list();			
	}

	/**
	 * Cualquiera de los parametros, si es null o <0, no se tiene en cuenta
	 * @param numeroConvenio
	 * @param idRecurso
	 * @param fechaVtoDesde
	 * @param fechaVtoHasta
	 * @param idViaJudicial
	 * @param noLiquidables - valores posibles: null, 0 y 1
	 * @return
	 */
	public List<ConvenioCuota> getList(Integer numeroConvenio,
			Long idRecurso, Date fechaVtoDesde, Date fechaVtoHasta,
			Long idViaDeuda, Integer noLiquidables) {
		String queryString ="from ConvenioCuota t ";
		boolean flagAnd = false;

		// numero convenio		
		if(numeroConvenio!=null && numeroConvenio>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.convenio.nroConvenio="+numeroConvenio; 
			flagAnd = true;
		}
		
		// idRecurso
		if(idRecurso!=null && idRecurso.longValue()>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.convenio.recurso.id="+idRecurso; 
			flagAnd = true;			
		}
		
		// fechaVtoDesde
		if(fechaVtoDesde!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.fechaVencimiento>= TO_DATE('" + 
					DateUtil.formatDate(fechaVtoDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
			flagAnd = true;			
		}
		
		// fechaVtoHasta
		if(fechaVtoDesde!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.fechaVencimiento<= TO_DATE('" + 
					DateUtil.formatDate(fechaVtoHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
			flagAnd = true;			
		}
		
		// viaDeuda
		if(idViaDeuda!=null && idViaDeuda.longValue()>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.convenio.viaDeuda.id="+idViaDeuda; 
			flagAnd = true;			
		}
		
		if(noLiquidables!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.noLiqComPro="+noLiquidables; 
			flagAnd = true;			
		}
		
		Session session = SiatHibernateUtil.currentSession();
		return (ArrayList<ConvenioCuota>)session.createQuery(queryString).list();
	}
	
	
	public void cargarSelAlmConvenioCuotaIncluida(Map<String,String> mapaFiltros) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		AdpRun.changeRunMessage("Creando filtros de busqueda...", 0);

		String     idTipoSelAlmDet = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_TIPO_SEL_ALM_DET);
		TipoSelAlm tipoSelAlmDet   = TipoSelAlm.getById(Long.valueOf(idTipoSelAlmDet)); 
		
		String sqlPrimerFiltro = getPrimerSQLBySearchPageForConvenioCuota(mapaFiltros, tipoSelAlmDet);
		String createTempPrimerFiltro= sqlPrimerFiltro + " INTO TEMP primerFiltro WITH NO LOG";
		
		long idSelAlm = Long.parseLong(mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_SEL_ALM_INC));

		// obtencion de la fecha de ult mod, estado activo y nombre de usuario para usarlo en el select para luego insertar
		String fecUltMdf    = DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
		Integer estadoActivo = Estado.ACTIVO.getId();
		String userName = DemodaUtil.currentUserContext().getUserName();
		if (StringUtil.isNullOrEmpty(userName)) {
			userName = "siat";
		}

		String createInsertResultado = "SELECT " + idSelAlm + " idSelAlm, convcuot.id, " + tipoSelAlmDet.getId() +  " idTipoSelAlmDet, '" + userName + "' usuario, '" + fecUltMdf + "' fecUltMdf , " + estadoActivo + " estado";
		
		createInsertResultado += " FROM primerFiltro pf " +
			"INNER JOIN gde_conveniocuota convcuot ON (convcuot.idconvenio == pf.idconvenio) ";

		String fechaVencimiento = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.FECHA_VENCIMIENTO);
		
		// que no hayan sido agregadas a la selAlmDet de SelAlm 
		createInsertResultado += " LEFT JOIN gde_selalmdet sad ON (sad.idelemento == pf.idconvenio AND sad.idselalm = " + idSelAlm + " AND sad.idtiposelalmdet = " + tipoSelAlmDet.getId() + ") " +
			"WHERE sad.id IS NULL " + " AND convCuot.idEstadoConCuo = "+ EstadoConCuo.ID_IMPAGO +
		// filtro Fecha Vencimiento requerido 
			" AND convCuot.fechaVencimiento <= TO_DATE('" + fechaVencimiento + "','%d/%m/%Y')";
		
		createInsertResultado += " INTO TEMP insertResultado WITH NO LOG";
		
		String queryInsertSelAlmDet = "INSERT INTO gde_selalmdet (idselalm,idelemento, idTipoSelAlmDet,usuario,fechaultmdf,estado) " +
				"SELECT SKIP ? FIRST ? * from insertResultado";

		//Ya armamos los query,
		// ahora nos conseguimos la connection JDBC de hibernate...
		// Hacemos un flush y un commit de lo que haya en hibernate por las dudas
		// para sincronizar hibernate.
		Connection        con;
		PreparedStatement ps;
		Session session = currentSession();
		Transaction tx = currentSession().getTransaction();
		tx.commit();
		tx = session.beginTransaction();

		// IMPORTANTE: de aca para abajo operamos con JDBC, y NO usar nada de hibenate
		con = session.connection();

		// GG 061128
		int oldIsolation = con.getTransactionIsolation();
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		AdpRun.changeRunMessage("Limpiando tablas temporales...", 0);

		// borrado de la tablas temporales si existe
		try {
			ps = con.prepareStatement("DROP TABLE primerFiltro;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error ya que puede ser que la tabla no exista
		}

		// borrado de la tablas temporales si existe
		try {
			ps = con.prepareStatement("DROP TABLE insertResultado;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error ya que puede ser que la tabla no exista
		}
		
		// creacion de tabla temporal con las deudas repetidas y la suma de saldos
  		if (log.isDebugEnabled()) log.debug(funcName + ": Creacion Temporal Primer Filtro:  " + createTempPrimerFiltro);
		AdpRun.changeRunMessage("Buscando Cuotas de Convenios. Etapa 1/5...", 0);
		ps = con.prepareStatement(createTempPrimerFiltro);
		ps.executeUpdate();		
		con.commit();
		ps.close();
		Long countRegs = countRegsTable("primerFiltro");
		if (log.isDebugEnabled()) log.debug(funcName + ": primerFiltro creado con exito. Contiene registros=" + countRegs);		

		//creacion de la tabla inserResultado tabla temporal igual a selAlmDet, pero con los convenioCuota a insertar en selalmdet.
		if (log.isDebugEnabled()) log.debug(funcName + ": Creacion Temporal Insert Resultado: " + createInsertResultado);
		AdpRun.changeRunMessage("Buscando Cuotas de Convenios. Etapa 3/5...", 0);
		ps = con.prepareStatement(createInsertResultado);
		ps.executeUpdate();
		con.commit();
		ps.close();
		countRegs = countRegsTable("insertResultado");
		if (log.isDebugEnabled()) log.debug(funcName + ": insertResultado creado con exito. Contiene registros=" + countRegs);		

		//finalmente insert en tabla del detalle de la selccion almacenada.
		//inserta de 'amount' selAlmDet leyendo las tablas temporales, 
		//y haciendo insert into selAlmDet.
		if (log.isDebugEnabled()) log.debug(funcName + ": Insert SelAlmDet: " + queryInsertSelAlmDet);		
		AdpRun.changeRunMessage("Transfiriendo Deuda Etapa 4/5: ", 0);
		ps = con.prepareStatement(queryInsertSelAlmDet);
		long amount = 1000;
		long count = 0;
		while (true) {
			ps.setLong(1, amount*count); //SKIP
			ps.setLong(2, amount); //FIRST
			int n = ps.executeUpdate();
			con.commit();
			if (log.isDebugEnabled()) log.debug(funcName + ": gde_selalmdet commit " + n + " registros");
			
			int porc = countRegs>0 ? (int) (n*100/countRegs) : 100;
			AdpRun.changeRunMessage("Transfiriendo Deuda Etapa 4/5 - " +  porc + "%", 30);
			
			if (n < 1000) {
				break;
			}
			count++;
		}
		// <-- Fin Resultado
		
		// borrado de la tabla temporal si existe
		AdpRun.changeRunMessage("Limpiando tablas Temporales. Etapa 5/5", 0);
		try {
			ps = con.prepareStatement("DROP TABLE primerFiltro;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error 
		}
		try {
			ps = con.prepareStatement("DROP TABLE insertResultado;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error 
		}
		con.setTransactionIsolation(oldIsolation);

		AdpRun.changeRunMessage("Finalizado.", 0);
	}

	public String getPrimerSQLBySearchPageForConvenioCuota(Map<String,String> mapaFiltros, TipoSelAlm tipoSelAlmDet ) throws Exception {
		
		String queryString = "SELECT convcuot.idconvenio " +
			"FROM gde_conveniocuota convcuot " +
			"INNER JOIN gde_convenio conv ON (convcuot.idconvenio == conv.id) " +
			"INNER JOIN pad_cuenta cta  ON (conv.idcuenta == cta.id) "; 
		
		String fechaVencimiento = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.FECHA_VENCIMIENTO);
		String cantidadCuotasPlan = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.CANTIDAD_CUOTAS_PLAN);
		
		String idsExencionesSI = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_EXENCIONES_SI);
		String idsExencionesNO = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_EXENCIONES_NO);
		
		String idsAtributosSI = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_ATRIBUTOS_SI);
		String idsAtributosNO = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_ATRIBUTOS_NO);
		
		String idObra = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_OBRA);
		String numeroCuenta = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.NUMERO_CUENTA);
		
		String queryGroupByHaving =  " GROUP BY convcuot.idconvenio HAVING COUNT (convcuot.id) >= " + cantidadCuotasPlan;
		
		
		Recurso recurso = Recurso.getById(Long.valueOf(mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_RECURSO)));
		boolean aplicaFiltroObjImpFlag = recurso.getTipObjImp() != null;
		
		String[] filtroObStrings = {"",""};
		
		if (aplicaFiltroObjImpFlag){
			filtroObStrings = GdeDAOFactory.getDeudaAdminDAO().getSQLsForParamObjImp(mapaFiltros);
			log.debug("filtroObjStrings inners: " + filtroObStrings[0]);
			log.debug("filtroObjStrings wheres: " + filtroObStrings[1]);
			queryString += filtroObStrings[0];
		}
		
		boolean aplicarFiltroExen = (!StringUtil.isNullOrEmpty(idsExencionesSI) || !StringUtil.isNullOrEmpty(idsExencionesNO) );
		if (aplicarFiltroExen){
			queryString += "LEFT JOIN exe_cueexe cueExe ON (cta.id == cueExe.idcuenta) ";
		}
		
		boolean aplicarFiltroAtrib =  (!StringUtil.isNullOrEmpty(idsAtributosSI) || !StringUtil.isNullOrEmpty(idsAtributosNO) );
		if (aplicarFiltroAtrib){
			queryString += "LEFT JOIN pad_cuentatitular ctaTit ON (cta.id == ctaTit.idcuenta ) " +
				"LEFT JOIN pad_conatrval cav ON (cav.idcontribuyente == ctaTit.idcontribuyente) " +
				"LEFT JOIN def_conatr ca ON (cav.idconatr == ca.idatributo) ";
		}

		boolean aplicarFiltroObra = !StringUtil.isNullOrEmpty(idObra);
		if (aplicarFiltroObra){
			queryString += "INNER JOIN cdm_plaCuaDet pod ON (conv.idcuenta == pod.idcuentacdm) " +
					"INNER JOIN cdm_planillacuadra po ON (pod.idplanillacuadra == po.id)";
		}
		
		queryString +=" WHERE ";

		// filtro Recurso requerido
		queryString += " conv.idrecurso = " + mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_RECURSO);
		// puede estar en via administrativa o en via judicial
		if (tipoSelAlmDet.getEsTipoSelAlmDetConvCuotAdm()){
			queryString += " AND conv.idViaDeuda = "+ ViaDeuda.ID_VIA_ADMIN;
		}else{
			queryString += " AND conv.idViaDeuda = "+ ViaDeuda.ID_VIA_JUDICIAL;
		}
		// no debe estar cancelada (pagada): no hace falta aplicar un filtro
		
		// TODO no debe estar indeterminada: CONSULTA CON INDETERMINADOS EN PASO POSTERIOR
		// no debe estar prescripta ni condonada: no hace falta aplicar un filtro

		// filtro cantidadCuotasPlan requerido		
		queryString += " AND conv.cantidadCuotasPlan >= " + cantidadCuotasPlan;
		
		// estado de convenio vigente
		queryString += " AND conv.idestadoconvenio = "+ EstadoConvenio.ID_VIGENTE;
		
		// estado impago de la cuota del convenio
		queryString += " AND convCuot.idestadoconcuo = "+ EstadoConCuo.ID_IMPAGO;
		
		// debe estar activa
		queryString += " AND convCuot.estado = "+ Estado.ACTIVO.getId();

		// filtro Fecha Vencimiento requerido 
		queryString += " AND convCuot.fechaVencimiento <= TO_DATE('" + fechaVencimiento + "','%d/%m/%Y')";
		
		// en el caso de un envio a judicial la fecha hasta ahora es requerido y 
		// previamente esta validada que no sea mayor a la fecha limite
		
		//aplicaFiltroObjImp
		//if (aplicaFiltroObjImpFlag){
			queryString += filtroObStrings[1];
		//}

		// filtro exenciones
		if (aplicarFiltroExen){
			
			if (!StringUtil.isNullOrEmpty(idsExencionesSI)){
				queryString += " AND cueExe.idexencion IN ( " + idsExencionesSI + " ) "; 
			}
			if (!StringUtil.isNullOrEmpty(idsExencionesNO)){
				queryString += " AND ( cueExe.idexencion NOT IN ( " + idsExencionesNO + " ) OR cueExe.idexencion IS NULL) ";
			}
		}
		
		// filtro atributos
		if (aplicarFiltroAtrib){

			if (!StringUtil.isNullOrEmpty(idsAtributosSI)){
				queryString += " AND ca.idatributo IN ( " + idsAtributosSI + " ) "; 
			}
			if (!StringUtil.isNullOrEmpty(idsAtributosNO)){
				queryString += " AND ( ca.idatributo NOT IN ( " + idsAtributosNO + " ) OR ca.idAtributo IS NULL) ";
			}
		}

		// numero de cuenta 
		if (!StringUtil.isNullOrEmpty(numeroCuenta) ) {
			queryString += " AND cta.numeroCuenta = '" + 
				StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
		}

		// obra
		if (aplicarFiltroObra ) {
			queryString += " AND po.idobra = " + idObra;
		}

		queryString += queryGroupByHaving;
		if(log.isDebugEnabled()) log.debug("queryString: " + queryString);

		return queryString;
	}

	public Double getSumaSaldo(SelAlm selAlmDeuda, TipoSelAlm tipoSelAlmDet) {

		Session session = SiatHibernateUtil.currentSession();

		String queryString = " SELECT SUM ( convCuo.importeCuota)  " +
			"FROM ConvenioCuota convCuo, SelAlmDet sad  WHERE " + 
			"sad.selAlm = :selAlm " +
			"AND convCuo.id = sad.idElemento " +
			"AND sad.tipoSelAlmDet = :tipoSelAlmDet ";

		if(log.isDebugEnabled()) log.debug("queryString: " + queryString);
		
		Query query = session.createQuery(queryString)
			.setEntity("selAlm", selAlmDeuda)
			.setEntity("tipoSelAlmDet", tipoSelAlmDet);
		
		return (Double) query.uniqueResult(); 
	}
	
	/**
	 * Obtiene la lista de ConvenioCuota de manera paginada para la seleccion almacenada y 
	 * el tipo de Detalle de Seleccion Almanacenada
	 * @param selAlm
	 * @param tipoSelAlmDet
	 * @param firstResult
	 * @param maxResults
	 * @return List<ConvenioCuota>
	 */
	public List<ConvenioCuota> getListBySelAlm(SelAlmDeuda selAlmDeuda, TipoSelAlm tipoSelAlmDet, Integer firstResult, Integer maxResults) {
		String queryString = "SELECT SKIP %s FIRST %s cc.* " +
			"FROM gde_conveniocuota cc, gde_selAlmDet sad " +
			"WHERE sad.idSelAlm = %s " +
			"AND sad.idTipoSelAlmDet = %s " +
			"AND cc.id = sad.idElemento";
		
		queryString = String.format(queryString, firstResult, maxResults, selAlmDeuda.getId(), tipoSelAlmDet.getId());
		Session session = currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createSQLQuery(queryString).addEntity(ConvenioCuota.class);

		return (ArrayList<ConvenioCuota>) query.list();
	}
	
	public ConvenioCuota getImputada(ConvenioCuota convenioCuota)throws Exception{
		ConvenioCuota convCuo = null;
		
		String queryString = "FROM ConvenioCuota cc WHERE cc.convenio.id = "+convenioCuota.getConvenio().getId();
		queryString += " AND cc.nroCuotaImputada = " + convenioCuota.getNumeroCuota();
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		query.setMaxResults(1);
		
		convCuo = (ConvenioCuota) query.uniqueResult();
		
		return convCuo;
	}

	public static void setMigId(long migId) {
		ConvenioCuotaDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}


}
