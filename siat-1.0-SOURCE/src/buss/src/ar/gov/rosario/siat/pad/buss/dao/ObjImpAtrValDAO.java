//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.pad.buss.bean.ObjImpAtrVal;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ObjImpAtrValDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(ObjImpAtrValDAO.class);	
	private static Logger logSt = Logger.getLogger("migrania.statistics");
	
	private static long migId = -1;
	
	public ObjImpAtrValDAO() {
		super(ObjImpAtrVal.class);
	}

	// TODO: A nivel funcional, ser'ia importante poder obtener dos vistas sobre los cambios en vigencias. en un caso debe mostrar lo que coresponde
	// en otro caso, toda la lista de novedades... 
	// para los casos en que requiere conocer la lista de los atributos de un objimp, hay que realizar los cortes correspondientes.
	// el otro caso es dejar las listas para ver.
	
	
	
	
	/**
	 *   
	 * Obtiene todos los ObjImpAtrVal para un id de ObjImp
	 * 
	 * original: ordenado por TipObjImpAtr y fechaNovedad desc, lo cual permite hacer un corte de control al valorizar la definicion
	 * y cuando corresponda la vigencia.
	 * 
	 * modificado 21.04.08 - ahora recibe fechaAnalisis. Corta todas las novedades mayores a la fechaAnalisis. Ordenado por TipObjImp y fechaDesde
	 * 
	 * es utilizado desde:
	 *      ObjImpAtrVal.getListByIdObjImp()
	 *           ObjImp.getDefinitionValue()
	 *              Cuenta.getDefinitionValue()
	 *                 Cuenta.tieneTodosLosAtributos()
	 *                    Cuenta.getListDesEspVigentes()
	 *                       Cuenta.reconfeccionar()
	 *                       
	 *                    
	 *              Cuenta.updateDomicilioEnvio()
	 *                 PadCuentaServiceHbmImpl.updateCuentaDomicilioEnvio()
	 *                 
	 *                 padCuentaServiceHbmImpl.updateDomEnvio()
	 *                ...
	 *                
	 *              PadObjetoImponibleServiceHbmImpl.getObjImpAdapterForView()
	 *           
	 * @param id
	 * @return
	 */
	public List<ObjImpAtrVal> getListByIdObjImp(Long id, Date fechaAnalisis) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();

	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idObjImp: " + id); 
		}

		String queryString = "";

		queryString += " from ObjImpAtrVal t ";
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.objImp.id = " + id;
		queryString += " and t.fechaNovedad <= :fechaAnalisis ";
		queryString += " order by t.tipObjImpAtr.id, t.fechaDesde desc, t.fechaNovedad desc, t.id desc";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString + "fechaAnalisis: " + fechaAnalisis);
	    
		Query query = session.createQuery(queryString)
							 .setDate("fechaAnalisis", fechaAnalisis);
		
		
		List<ObjImpAtrVal> listObjImpAtrVal = (ArrayList<ObjImpAtrVal>) query.list();
		
		// cortada...
		// aqui tengo la lista de ObjImpAtrVal ordenada por fechaDesde. Tengo que limar los que no corresponden por saltos
		List <ObjImpAtrVal> listObjImpAtrValCortada = cortaValoresPisados(listObjImpAtrVal);
				
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listObjImpAtrValCortada;
	}
	
	/**
	 *  Esta funcion recibe una lista de ObjImpAtrVal ordenada por: TipObjImpAtr + fechaDesde
	 *  Realiza un corte de control por TipObjImpAtr. Dentro de cada corte, descarta los registros que no sigan un orden correlativo 
	 *  de fechaNovedad.
	 * 
	 * @param objImpAtrVal
	 * @return
	 * @throws DemodaServiceException 
	 * @throws Exception
	 */
	private List<ObjImpAtrVal> cortaValoresPisados(List<ObjImpAtrVal> listObjImpAtrVal) {
		List<ObjImpAtrVal> listObjImpAtrValRet = new ArrayList<ObjImpAtrVal>();
		
		String funcName =  DemodaUtil.currentMethodName();
		log.debug(funcName + " entrando #### ");
		
		long idTipObjImpAtr = -1;
		ObjImpAtrVal elem = null;
		
		
		for (int i=0; i< listObjImpAtrVal.size(); i++) {
			// necesito del pr'oximo elemento

			// si existe un proximo (i+1) < length
			
			//    si es del mismo tipobjimpatr
			//        si la fecha del proximo  afuera el que estoy parado
			// 
		
			elem = (ObjImpAtrVal) listObjImpAtrVal.get(i);
			idTipObjImpAtr = elem.getTipObjImpAtr().getId();//elem.getIdTipObjImpAtr().longValue();
			log.debug("FD: " + (elem.getFechaDesde()!=null?elem.getFechaDesde().toString():null) + " - FN: " + (elem.getFechaNovedad()!=null?elem.getFechaNovedad().toString():null) + " - Valor: " + elem.getStrValor());
			
			// si hay proximo elemento
			if ((i+1) < listObjImpAtrVal.size()) {

				// obtengo el proximo elemento
				ObjImpAtrVal elemProx = (ObjImpAtrVal) listObjImpAtrVal.get(i+1);
				long idTipObjImpAtrProx = elemProx.getTipObjImpAtr().getId();//elemProx.getIdTipObjImpAtr().longValue();

				// si es del mismo idTipObjImpAtr
				if (idTipObjImpAtr==idTipObjImpAtrProx) {

					// obtenemos la definicion.
					TipObjImpAtr definition =  TipObjImpAtr.getByIdFromCache(elem.getTipObjImpAtr().getTipObjImp().getId(), elem.getTipObjImpAtr().getId());//elem.getIdTipObjImpAtr());
					
					// si la fecha de novedad de elemProx es menor a la fecha novedades de elem, lo agrego. sino, no.
					// Si es multivalor
					if (definition != null && definition.getEsMultivalor().intValue() == 1) {
						listObjImpAtrValRet.add(elem);
						log.debug("MULTIVALOR AGREGADO");
					} else {
					// Si NO es multivalor		
						if (DateUtil.isDateBeforeOrEqual(elemProx.getFechaNovedad(), elem.getFechaNovedad())) {
							listObjImpAtrValRet.add(elem);
							log.debug("AGREGADO");
							
						} else {
							log.debug("DESCARTADO");
							log.debug(funcName + " valor no agregado: " + elem.getId());
						}					
					}
					
				} else {
					listObjImpAtrValRet.add(elem);
					log.debug("AGREGADO");

				}
				
				
			} else {
				listObjImpAtrValRet.add(elem);
				log.debug("AGREGADO");
				
			}
			
			
		}
		
		log.debug(funcName + " saliendo");
		
		return listObjImpAtrValRet;
	}
	
	
	
	
	/**
	 * Obtiene una lista de ObjImpAtrVal para un TipObjImpAtr (definicion de atributo) y para un ObjetoImponible
	 * 
	 * cambio: 21.04.08 - elimina los registros que tienen fechaNovedad mayor que la fechaAnalisis. adem'as ordena por fechaDesde
	 * 
	 * utilizado desde:
	 *   ObjImpAtrVal.getListByObjImpAtrVal()
	 *     ObjImp.getDefinitionValue()
	 *        PadObjetoImponibleServiceHbmImpl.getObjImpAtrValAdapterForView()
	 * 
	 * @param idTipObjImpAtr
	 * @param idObjImp
	 * @return
	 */
	public List<ObjImpAtrVal> getListByIdObjImpAtrVal(Long idTipObjImpAtr, Long idObjImp, Date fechaAnalisis) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: tipObjImpAtr.id: " + idTipObjImpAtr); 
		}

		String queryString = "";
	
		queryString += " from ObjImpAtrVal t ";
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.objImp.id = " + idObjImp;
		queryString += " and t.tipObjImpAtr.id = " + idTipObjImpAtr;
		queryString += " and t.fechaNovedad <= :fechaAnalisis ";
		queryString += " order by t.fechaDesde desc, t.fechaNovedad desc, id desc";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString)
						     .setDate("fechaAnalisis", fechaAnalisis);
		
		List<ObjImpAtrVal> listObjImpAtrVal = (ArrayList<ObjImpAtrVal>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + " listObjImpAtrVal.size(): " + listObjImpAtrVal.size());

		// cortada...
		// aqui tengo la lista de ObjImpAtrVal ordenada por fechaDesde. Tengo que limar los que no corresponden por saltos
		List <ObjImpAtrVal> listObjImpAtrValCortada = cortaValoresPisados(listObjImpAtrVal);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listObjImpAtrValCortada;
		
	}
	
	/**
	 * Obtiene el ObjImpAtrVal vigente para un TipObjImpAtr y un ObjetoImponible
	 * 
	 * cambio: 21.04.08: no se muestra lo que tenga fechaNovedad mayor que la fechaAnalisis. adem'as se muestra ordenado por fechaDesde
	 * 
	 * utilizado por:
	 *   ObjImpAtrVal.getVigenteByIdObjImpAtrVal()  
	 *     Cuenta.getValorAtributo()
	 *     ObjImp.updateObjImpAtrDefinition()
	 *     
	 *     PlaCuaDet.toVOConCuentaInfo()
	 *     PlaCuaDet.toVOWhitCatastrales()
	 *     
	 *     RecCdmManager.getListObjImpAgrupada()
	 *   
	 *     RecCdMServiceHbmImpl.getListPlaCuaDetAgrupadasForSP()
	 *     RecCdMServiceHbmImpl.getPlaCuaDetVO()
	 *     
	 *     Funciones.listarCatastrales()   
	 * 
	 * 
	 * @param idTipObjImpAtr
	 * @param idObjImp
	 * @return
	 */
	public ObjImpAtrVal getVigenteByIdObjImpAtrVal(Long idTipObjImpAtr, Long idObjImp, Date fechaAnalisis) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: tipObjImpAtr.id: " + idTipObjImpAtr); 
		}
	
		String queryString = "";

		/*
		 *  Ahora alcanza con cortar las novedades mayores a la fecha de analisis y obtener el registro con maxima fecha de analisis.
		 */
		queryString += " from ObjImpAtrVal t ";
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.objImp.id = " + idObjImp;
		queryString += " and t.tipObjImpAtr.id = " + idTipObjImpAtr;
		queryString += " and t.fechaNovedad <= :fechaAnalisis ";
		queryString += " order by t.fechaDesde desc, t.fechaNovedad desc, id desc";
		
		Query query = session.createQuery(queryString)
							 .setDate("fechaAnalisis", fechaAnalisis);
		
		List<ObjImpAtrVal> listObjImpAtrVal = (ArrayList<ObjImpAtrVal>) query.list();
		
		ObjImpAtrVal objImpAtrVal = null;
		if (listObjImpAtrVal.size() >= 1)
			objImpAtrVal = listObjImpAtrVal.get(0);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return objImpAtrVal;
	}
	
	public ObjImpAtrVal getVigenteByIdObjImpAtrValAndValue(Long idTipObjImpAtr, Long idObjImp, Date fechaAnalisis, String valor) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: tipObjImpAtr.id: " + idTipObjImpAtr); 
		}
	
		String queryString = "";

		/*
		 *  Ahora alcanza con cortar las novedades mayores a la fecha de analisis y obtener el registro con maxima fecha de analisis.
		 */
		queryString += " from ObjImpAtrVal t ";
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.objImp.id = " + idObjImp;
		queryString += " and t.tipObjImpAtr.id = " + idTipObjImpAtr;
		queryString += " and t.fechaNovedad <= :fechaAnalisis ";
		queryString += " and t.strValor = :valor";
		queryString += " and t.fechaNovedad <= :fechaAnalisis ";
		queryString += " and (t.fechaHasta IS NULL OR t.fechaHasta >= :fechaAnalisis)";
		queryString += " order by t.fechaDesde desc, t.fechaNovedad desc, id desc";
		
		Query query = session.createQuery(queryString)
							 .setDate("fechaAnalisis", fechaAnalisis)
							 .setString("valor", valor);
		
		List<ObjImpAtrVal> listObjImpAtrVal = (ArrayList<ObjImpAtrVal>) query.list();
		log.debug("queryString: "+queryString);
		log.debug("valor: "+valor);
		log.debug("lista atributos: "+listObjImpAtrVal.size());
		ObjImpAtrVal objImpAtrVal = null;
		if (listObjImpAtrVal.size() >= 1)
			objImpAtrVal = listObjImpAtrVal.get(0);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return objImpAtrVal;
	}
	
	
	/**
	 * Este metodo carga un mapa con datos de cuentas necesarios para la emision tgi (podria ser cualquier otra)
	 * El mapa contiene datos asociados a numero cuenta
	 * @param idTipObjImpAtr
	 * @param fechaAnalisis
	 * @return Mapa cagado con clave: numerocuenta, y valores idcuenta y valor de atributo idTipObjImpAtr para cada cuenta.
	 * IMPORTANTE:
	 * Si se obtiene mas de un valor de atributo para la fecha de analisis se toma el primer ordenado por fechaDesde , fechaNovedad y id de atrval
	 * Esto es asi, para ser coherente con el sistema de recuperacion de valores de atributos.
	 * Por razones de performance para la emision no es posible reusar el metodo que trae los valores por objeto imponible. 
	 */
	public void getMapCuentaValTipObjImpAtrVal(Map<String, String> map, Long idRecurso, Long idTipObjImpAtr, Date fechaAnalisis) throws Exception {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: tipObjImpAtr.id: " + idTipObjImpAtr); 
		}
	
		String sql = "";
		sql = "select cta.id idCuenta, cta.numerocuenta nroCuenta, ov.strvalor atrVal from pad_cuenta cta, pad_objimpatrval ov" +
			" where " +
			" cta.idrecurso = " + idRecurso + 
			" and ov.estado = " + Estado.ACTIVO.getId() +
			" and ov.idobjImp = cta.idobjimp" +
			" and ov.idtipObjImpAtr = " + idTipObjImpAtr +
			" and ov.fechaNovedad <= :fechaAnalisis" +  
			" order by cta.id, ov.fechaDesde desc, ov.fechaNovedad desc, ov.id";

		Query query = session.createSQLQuery(sql).addScalar("idCuenta", Hibernate.LONG)
												 .addScalar("nroCuenta", Hibernate.STRING)
												 .addScalar("atrVal", Hibernate.STRING)
												 .setDate("fechaAnalisis", fechaAnalisis);
		List<Object[]> list = query.list();
		for(Object[] row: list) {
			Long idCuenta = (Long) row[0];
			String nroCuenta = (String) row[1];
			String atrVal = (String) row[2];
			String key = nroCuenta; 

			String value = map.get(key);
			if (value != null) continue; //solo tenemos encuenta el primer valor recuperado para esta cuenta. (ver javadoc arriba)
			
			value = idCuenta + ";" + atrVal;
			map.put(key, value);
		}
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
	}
	
	
	/**
	 * 
	 * Obtiene una lista de ObjImpAtrVal para le Objeto Imponible correspondiente al id y 
	 * donde los ids de TipObjImpAtr esten dentro de la lista recibida
	 * 
	 * cambio: 21.04.08 - toma los que tengan fechaNovedad anterior a la fechaAnalisis. ordenado por fechaDesde
	 * 
	 * se utiliza desde:
	 *    ObjImpatrVal.getListByIdObjImpldsTipObjImpAtr()
	 *       ObjImp.getDefinitionValueForWeb()
	 *          LiqDeudaBeanHelper.getCuenta()
	 *          LiqDeudaBeanHelper.getLiqConvenioCuentaAdapter()
	 *          LiqDeudaBeanHelper.
	 *          LiqDeudaBeanHelper.
	 * 
	 *          LiqFormConvenioBeanHelper. getLiqFormConvenioInit()
	 *          
	 *          LiqReconfeccionBeanHelper.getReconfeccion()
	 * 
	 * @author Cristian
	 * @param id
	 * @param listIds
	 * @return
	 */
	public List<ObjImpAtrVal> getListByIdObjImpIdsTipObjImpAtr(Long id, Long[] listIds, Date fechaAnalisis) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTipObjImp: " + id); 
		}
	
		String queryString = "";
		
		queryString += " from ObjImpAtrVal t ";
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.objImp.id = " + id;
		queryString += " and t.tipObjImpAtr.id in ( " + StringUtil.getStringComaSeparate(listIds) + ") ";
		queryString += " and t.fechaNovedad <= :fechaAnalisis ";
		queryString += " order by t.tipObjImpAtr, t.fechaDesde desc, t.fechaNovedad desc, id desc";

		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString)
							 .setDate("fechaAnalisis", fechaAnalisis);
		
		
		List<ObjImpAtrVal> listObjImpAtrVal = (ArrayList<ObjImpAtrVal>) query.list();
			
		// cortada...
		// aqui tengo la lista de ObjImpAtrVal ordenada por fechaDesde. Tengo que limar los que no corresponden por saltos
		List <ObjImpAtrVal> listObjImpAtrValCortada = cortaValoresPisados(listObjImpAtrVal);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listObjImpAtrValCortada;

	}

	
	
	/*------------------*/
	
	
	 public Long createJdbc(ObjImpAtrVal o) throws Exception{
		 long msPasados=0;
		 msPasados = System.currentTimeMillis();
		 long runId = 0;
		 String sql = "";
		 sql += " insert into pad_objimpatrval"; 
		 sql += " (idobjimp,idtipobjimpatr,strvalor,fechadesde,fechahasta,fechanovedad,usuario,fechaultmdf,estado)";
		 sql += " values (?,?,?,?,?,?,?,?,?)";
		 
		 Connection cn = SiatHibernateUtil.currentSession().connection();
		 
		 PreparedStatement ps = cn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		 ps.setLong(1, o.getObjImp().getId());
		 ps.setLong(2, o.getTipObjImpAtr().getId());
		 ps.setString(3, o.getStrValor());
		 if(o.getFechaDesde()!=null){
			 ps.setTimestamp(4, new Timestamp(o.getFechaDesde().getTime()));//Timestamp.valueOf(DateUtil.formatDate(o.getFechaDesde(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));		 
		 }else{
			 ps.setNull(4, java.sql.Types.TIMESTAMP );
		 }
		 if(o.getFechaHasta()!=null){
			 ps.setTimestamp(5, new Timestamp(o.getFechaHasta().getTime()));//Timestamp.valueOf(DateUtil.formatDate(o.getFechaHasta(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));		 
		 }else{
			 ps.setNull(5, java.sql.Types.TIMESTAMP );
		 }
		 if(o.getFechaNovedad()!=null){
			 ps.setTimestamp(6, new Timestamp(o.getFechaNovedad().getTime()));//Timestamp.valueOf(DateUtil.formatDate(o.getFechaNovedad(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));		 
		 }else{
			 ps.setNull(6, java.sql.Types.TIMESTAMP );
		 }
		 ps.setString(7, "siat");
		 ps.setTimestamp(8, new Timestamp(Calendar.getInstance().getTimeInMillis()));//Timestamp.valueOf(DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK)));		 
		 ps.setInt(9, 1);
		 
		 ps.executeUpdate();
	   
	 	 runId = this.getSerial(ps); 
	     ps.close();
		 o.setId(runId);
		 msPasados = System.currentTimeMillis() - msPasados;
	     logSt.info("Save in db: "+o.getClass().getName()+" ----------------- t = "+msPasados+" mseg");
		 return runId;
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
	  *  Inserta una linea con los datos del Atributo Valorizado del Objeto Imponible para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param objImpatrval, output - Atributo Valorizado del Objeto Imponible a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(ObjImpAtrVal o, LogFile output) throws Exception {
		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());

		 // Estrucura de la linea:
		 // id|idobjimp|idtipobjimpatr|strvalor|fechadesde|fechahasta|fechanovedad|usuario|fechaultmdf|estado
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getObjImp().getId());		 
		 line.append("|");
		 line.append(o.getTipObjImpAtr().getId());  //TODO: si es necesario optimizacion, cambiar por "o.getIdTipObjImpAtr()" 
		 line.append("|");
		 line.append(o.getStrValor());
		 line.append("|");
		 if(o.getFechaDesde()!=null){
			 line.append(DateUtil.formatDate(o.getFechaDesde(), "yyyy-MM-dd"));
		 } // Si es null no se inserta nada, viene el proximo pipe.
		 line.append("|");
		 if(o.getFechaHasta()!=null){
			line.append(DateUtil.formatDate(o.getFechaHasta(), "yyyy-MM-dd"));		 
		 } // Si es null no se inserta nada, viene el proximo pipe.
		 line.append("|");
		 if(o.getFechaNovedad()!=null){
				line.append(DateUtil.formatDate(o.getFechaNovedad(), "yyyy-MM-dd"));		 
		 } // Si es null no se inserta nada, viene el proximo pipe.	 
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

	 /** Obtiene una lista de Atributo-Valor (de tipo especificado en listIdsTipObjImpAtr) 
	  * para cada Objeto Imponible con id en la lista listIdsObjImp
	  * 
	  * @param listIdsObjImp
	  * @param listIdsTipObjImpAtr
	  * @return List<ObjImpAtrVal>
	  */
	public List<ObjImpAtrVal> getListObjImpAtrValByListIdDAO(Long[] listIdsObjImp, Long[] listIdsTipObjImpAtr) {			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			Session session = SiatHibernateUtil.currentSession();
			
			String queryString = "";

			queryString += " from ObjImpAtrVal t ";
			queryString += " where t.estado = "+ Estado.ACTIVO.getId();
			queryString += " and t.objImp.id in ( " + StringUtil.getStringComaSeparate(listIdsObjImp) + ")";
			queryString += " and t.tipObjImpAtr.id in (" + StringUtil.getStringComaSeparate(listIdsTipObjImpAtr) + ")";
			queryString += " and t.fechaNovedad <= :fechaAnalisis ";
			queryString += " order by t.objImp, t.tipObjImpAtr";

			if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		    
			Query query = session.createQuery(queryString)
								 .setDate("fechaAnalisis", new Date());
			
			List<ObjImpAtrVal> listObjImpAtrVal = (ArrayList<ObjImpAtrVal>) query.list();
				
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return listObjImpAtrVal;

	}

	public int delete(Long idObjImp, Long idTipObjImpAtr){
		String queryString = "delete from ObjImpAtrVal t ";
		queryString += " where t.objImp.id = " + idObjImp+" AND t.tipObjImpAtr.id="+idTipObjImpAtr;
	
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
 
		return query.executeUpdate();
	}

	/**
	 *  Obtiene una lista de Atributos de Objeto Imponible con id de ObjImp y fechaDesde igual a los pasados.
	 *  <p><i>(Se utiliza desde la migracion)</i></p>
	 *  
	 * @param id
	 * @param fechaDesde
	 * @return
	 */
	public List<ObjImpAtrVal> getListByIdObjImpAndFechaDesde(Long id, Date fechaDesde) {			

		Session session = SiatHibernateUtil.currentSession();

		String queryString = "";

		queryString += " from ObjImpAtrVal t ";
		queryString += " where t.objImp.id = " + id;
		queryString += " and t.fechaDesde = :fechaDesde ";
		
		Query query = session.createQuery(queryString)
							 .setDate("fechaDesde", fechaDesde);
		
		List<ObjImpAtrVal> listObjImpAtrVal = (ArrayList<ObjImpAtrVal>) query.list();
		
		return listObjImpAtrVal;
	}

	public static void setMigId(long migId) {
		ObjImpAtrValDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

}
