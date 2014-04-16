//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ReciboSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ReciboVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ReciboDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ReciboDAO.class);
	
	public static String SEQUENCE_NRO_RECIBO = "gde_recibo_sq";
	
	public static String SEQUENCE_COD_REF_PAGO = "gde_recibo_cref_sq";
	
	private static long migId = -1;
	
	public ReciboDAO(){
		super(Recibo.class);
	}
	
	public Recibo getByCodRefPag(Long codRefPag){
		Recibo recibo;
		String queryString = "from Recibo t where t.codRefPag = :codRefPag";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("codRefPag", codRefPag);
		recibo = (Recibo) query.uniqueResult();	

		return recibo; 
	}
	
	
	public List<Recibo> getByCodRefPagImporte(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
				
		String queryString = "from Recibo t ";
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
           	queryString += "  t.totImporteRecibo = " + liqCodRefPagSearchPage.getImporte();
 			flagAnd = true;
		}

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Recibo> listRecibo = (ArrayList<Recibo>) executeCountedSearch(queryString, liqCodRefPagSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecibo;
	}
	
	/**
	 * Obtiene el Recibo por el Numero, Anio y Sistema.
	 * (TODO ver si se reemplaza por el getByNumero)
	 * 
	 * @param numero
	 * @param anio
	 * @param nroSistema
	 * @return
	 */
	public Recibo getByNroAnioSis(Long numero, Long anio, Long nroSistema){
		Recibo recibo;
		String queryString = "from Recibo t where t.nroRecibo = :numero";
		queryString += " and t.anio = :anio and t.deuda.sistema.nroSistema = :nroSis";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("numero", numero);
		query.setLong("anio", anio);
		query.setLong("nroSis", nroSistema);
		recibo = (Recibo) query.uniqueResult();	

		return recibo; 
	}
	
	/**
	 * Obtiene el Recibo por su Numero.
	 * 
	 * @param numero
	 * @return
	 */
	public Recibo getByNumero(Long numero){
		Recibo recibo;
		String queryString = "from Recibo t where t.nroRecibo = :numero";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("numero", numero);
		recibo = (Recibo) query.uniqueResult();	

		return recibo; 
	}

	public Long getNextNroRecibo(){
		return super.getNextVal(SEQUENCE_NRO_RECIBO);
	}
	
	public Long getNextCodRefPago(){
		return super.getNextVal(SEQUENCE_COD_REF_PAGO);
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
	 *  Inserta una linea con los datos del Recibo para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param recibo, output - El Recibo a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(Recibo o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idRecibo.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|codrefpag|idserviciobanco|idrecurso|idviadeuda|idcuenta|idcanal|nrorecibo|aniorecibo|fechageneracion|fechavencimiento|totcapitaloriginal|totactualizacion|idsellado|importesellado|totimporterecibo|estaimpreso|fechapago|idbancopago|usuario|fechaultmdf|estado
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getCodRefPag());
		line.append("|");
		line.append(o.getServicioBanco().getId());
		line.append("|");
		line.append(o.getRecurso().getId());		 
		line.append("|");
		line.append(o.getViaDeuda().getId());		 
		line.append("|");
		line.append(o.getCuenta().getId());		 
		line.append("|");
		line.append(o.getCanal().getId());		 
		line.append("|");
		line.append(o.getNroRecibo());		 
		line.append("|");
		line.append(o.getAnioRecibo());		 
		line.append("|");
		if(o.getFechaGeneracion()!=null){
			line.append(DateUtil.formatDate(o.getFechaGeneracion(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getFechaVencimiento()!=null){
			line.append(DateUtil.formatDate(o.getFechaVencimiento(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(decimalFormat.format(o.getTotCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotActualizacion()));
		line.append("|");
		if(o.getSellado() != null)
			line.append(o.getSellado().getId());		 
		line.append("|");
		if(o.getImporteSellado()!=null)
			line.append(decimalFormat.format(o.getImporteSellado()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotImporteRecibo()));
		line.append("|");
		line.append(o.getEstaImpreso());
		line.append("|");
		if(o.getFechaPago()!=null){
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getBancoPago()!=null)
			line.append(o.getBancoPago().getId());
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
	 * Cualquiera de los parametros, si es null o <0, no se tiene en cuenta
	 * @param numeroRecibo
	 * @param idRecurso
	 * @param fechaEmisionDesde
	 * @param fechaEmisionHasta
	 * @param idViaDeuda
	 * @param noLiquidables - valores posibles: null, 0 y 1
	 * @return
	 */
	public List<Recibo> getList(Long numeroRecibo, Long idRecurso,
			Date fechaEmisionDesde, Date fechaEmisionHasta, Long idViaDeuda, Long idProcurador, Integer noLiquidables) {
		String queryString ="from Recibo t ";
		boolean flagAnd = false;

		// numeroRecibo		
		if(numeroRecibo!=null && numeroRecibo.longValue()>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.nroRecibo="+numeroRecibo; 
			flagAnd = true;
		}
		
		// idRecurso
		if(idRecurso!=null && idRecurso.longValue()>0){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.recurso.id="+idRecurso; 
			flagAnd = true;			
		}
		
		// fechaEmisionDesde
		if(fechaEmisionDesde!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.fechaGeneracion>= TO_DATE('" + 
					DateUtil.formatDate(fechaEmisionDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
			flagAnd = true;			
		}
		
		// fechaEmisionHasta
		if(fechaEmisionHasta!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.fechaGeneracion<= TO_DATE('" + 
					DateUtil.formatDate(fechaEmisionHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"; 
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
 			queryString += " t.idProcurador="+idProcurador; 
			flagAnd = true;			
		}
		
		// noLiquidable
		if(noLiquidables!=null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.noLiqComPro="+noLiquidables; 
			flagAnd = true;			
		}
		
		Session session = SiatHibernateUtil.currentSession();
		return (ArrayList<Recibo>)session.createQuery(queryString).list();
	}
	
	
	public List<Recibo> getBySearchPage(ReciboSearchPage reciboSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Recibo t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ReciboSearchPage: " + reciboSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (reciboSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro recibo excluidos
 		List<ReciboVO> listReciboExcluidos = (List<ReciboVO>) reciboSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listReciboExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listReciboExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
 		
		// CodRefPago		
		if(reciboSearchPage.getRecibo().getCodRefPag() != null && reciboSearchPage.getRecibo().getCodRefPag().longValue()>0){
 			queryString += flagAnd ? " AND " : " WHERE ";
 			queryString += " t.codRefPag="+reciboSearchPage.getRecibo().getCodRefPag(); 
			flagAnd = true;
		}
		
		// NroRecibo		
		if(reciboSearchPage.getRecibo().getNroRecibo() != null){
 			queryString += flagAnd ? " AND " : " WHERE ";
 			
 			log.debug(" nroRecibo: " + reciboSearchPage.getRecibo().getNroRecibo().longValue());
 			
 			if (reciboSearchPage.getRecibo().getNroRecibo().longValue() < 0){
            	queryString += " t.id = " + reciboSearchPage.getRecibo().getNroRecibo().longValue() * -1;
 			} else {  	
 				queryString += " t.nroRecibo="+reciboSearchPage.getRecibo().getNroRecibo(); 
 			}
 			
 			flagAnd = true;
		}
 		
		// Recurso
		if(reciboSearchPage.getRecibo().getRecurso().getId() != null &&
				reciboSearchPage.getRecibo().getRecurso().getId().longValue() > -1){
 			queryString += flagAnd ? " AND " : " WHERE ";
 			queryString += " t.recurso.id="+reciboSearchPage.getRecibo().getRecurso().getId(); 
			flagAnd = true;
		}
		
		// Numero Cuenta
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta())) {
            queryString += flagAnd ? " AND " : " WHERE ";
            
            if (reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta().startsWith("-") && 
            		StringUtil.isNumeric(reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta())){
            	queryString += " t.cuenta.id = " + reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta().substring(1) ;
 			} else {  	
 				queryString += " t.cuenta.numeroCuenta = '" + 
 					StringUtil.formatNumeroCuenta(reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta()) + "'";
 			}
            
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.anioRecibo, t.nroRecibo ";
				
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Recibo> listRecibo = (ArrayList<Recibo>) executeCountedSearch(queryString, reciboSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecibo;
	}
	
	
		/**
	 * Cualquiera de los parametros, si es null o <0, no se tiene en cuenta
	 * @param listProcurador
	 * @param idRecurso
	 * @param idViaDeuda
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public List<Recibo> getList(List<Procurador> listProcurador, Long idRecurso, Long idViaDeuda,
								Integer firstResult, Integer maxResults) throws Exception {
		String queryString="SELECT ";
		if(firstResult!=null){
			queryString += " SKIP "+firstResult;
		}
		
		if(maxResults!=null){
			queryString += " FIRST "+maxResults;
		}
		
		boolean flagAnd = true;
		queryString +=" recibo.* from gde_recibo recibo ";
		
		if(idRecurso!=null){
			queryString +=" WHERE recibo.idRecurso="+idRecurso;
			flagAnd = true;
		}		
		
		if(listProcurador!=null && !listProcurador.isEmpty()){
 			String listIdProcurador = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listProcurador, 0, false));
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" recibo.idprocurador IN ("+listIdProcurador+") ";
 			flagAnd = true;
		}
		
		if(idViaDeuda!=null){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" recibo.idViaDeuda="+idViaDeuda;
 			flagAnd = true; 			
		}
		
		queryString +=" ORDER BY recibo.idProcurador";			

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString).addEntity(Recibo.class);
		return (List<Recibo>)query.list();	

	}

	/**
	 * Lista de todos los recibos deonde figura la deuda
	 * ordenada por fecha descendente
	 * @param deuda
	 * @return
	 * @throws Exception
	 */
	public List<Recibo> getListReciboByDeuda(Deuda deuda) throws Exception {
		List<Recibo> listRecibo;
		Session session = SiatHibernateUtil.currentSession();

		String sql = "select recibo from ReciboDeuda as reciboDeuda join reciboDeuda.recibo as recibo";
		sql += " where reciboDeuda.idDeuda = :idDeuda";
		
		Query query = session.createQuery(sql).setLong("idDeuda", deuda.getId());
		listRecibo = (ArrayList<Recibo>) query.list();	

		return listRecibo;
	}

	public static void setMigId(long migId) {
		ReciboDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}
	
	/**
	 * Cualquiera de los parametros, si es null o <0, no se tiene en cuenta
	 * @param listProcurador
	 * @param listRecurso
	 * @param idViaDeuda
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws Exception
	 */
	public List<Recibo> getListByListRecurso(List<Procurador> listProcurador, List<Recurso> listRecurso, Long idViaDeuda,
								Integer firstResult, Integer maxResults) throws Exception {
		String queryString="SELECT ";
		if(firstResult!=null){
			queryString += " SKIP "+firstResult;
		}
		
		if(maxResults!=null){
			queryString += " FIRST "+maxResults;
		}
		
		boolean flagAnd = true;
		queryString +=" recibo.* from gde_recibo recibo ";
		
		if(!ListUtil.isNullOrEmpty(listRecurso)){
			String listIdRecurso = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listRecurso, 0, false));
			queryString +=" WHERE  recibo.idRecurso IN ("+listIdRecurso+") ";
			flagAnd = true;
		}	
		
		if(listProcurador!=null && !listProcurador.isEmpty()){
 			String listIdProcurador = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listProcurador, 0, false));
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" recibo.idprocurador IN ("+listIdProcurador+") ";
 			flagAnd = true;
		}
		
		if(idViaDeuda!=null){
 			queryString += flagAnd?" AND ": " WHERE ";
 			queryString +=" recibo.idViaDeuda="+idViaDeuda;
 			flagAnd = true; 			
		}
		
		queryString +=" ORDER BY recibo.idProcurador";			

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString).addEntity(Recibo.class);
		return (List<Recibo>)query.list();	

	}

}
