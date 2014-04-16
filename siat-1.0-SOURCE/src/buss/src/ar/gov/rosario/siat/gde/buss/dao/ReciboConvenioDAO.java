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
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ReciboSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ReciboVO;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ReciboConvenioDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ReciboConvenioDAO.class);
	
	private static long migId = -1;
	
	private static final String SEQUENCE_NRO_RECIBO = "gde_reccon_nro_sq";
	
	private static final String SEQUENCE_COD_REF_PAGO = "gde_reccon_cref_sq";
	
	public ReciboConvenioDAO() {
		super(ReciboConvenio.class);
	}
	
	/**
	 *  Obtiene el Recibo de Cuota (ReciboConvenio) por su Codigo de Referencia de Pago  
	 * @param codRefPag
	 * @return
	 */
	public ReciboConvenio getByCodRefPag(Long codRefPag){
		ReciboConvenio reciboConvenio;
		String queryString = "from ReciboConvenio t where t.codRefPag = :codRefPag";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("codRefPag", codRefPag);
		reciboConvenio = (ReciboConvenio) query.uniqueResult();	

		return reciboConvenio; 
	}
	
	
	public List<ReciboConvenio> getByCodRefPagImporte(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
				
		String queryString = "from ReciboConvenio t ";
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

		List<ReciboConvenio> listReciboConvenio = (ArrayList<ReciboConvenio>) executeCountedSearch(queryString, liqCodRefPagSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listReciboConvenio;
	}
	
	/**
	 *  Obtiene el Recibo de Cuota (ReciboConvenio) por Nro de Recibo, Nro de Sistema y Anio. 
	 * @param numero
	 * @param anio
	 * @param nroSistema
	 * @return
	 */
	public ReciboConvenio getByNroAnioSis(Long numero, Long anio, Long nroSistema){
		ReciboConvenio reciboConvenio;
		String queryString = "from ReciboConvenio t where t.nroRecibo = :numero";
		queryString += " and t.anioRecibo = :anio and t.convenio.sistema.nroSistema = :nroSis";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("numero", numero);
		query.setLong("anio", anio);
		query.setLong("nroSis", nroSistema);
		reciboConvenio = (ReciboConvenio) query.uniqueResult();	

		return reciboConvenio; 
	}
	
	/**
	 *  Obtiene el Recibo de Cuota (ReciboConvenio) por su N&uacutemero. 
	 * @param numero
	 * @return reciboConvenio
	 */
	public ReciboConvenio getByNumero(Long numero){
		ReciboConvenio reciboConvenio;
		String queryString = "from ReciboConvenio t where t.nroRecibo = :numero";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("numero", numero);
		reciboConvenio = (ReciboConvenio) query.uniqueResult();	

		return reciboConvenio; 
	}
	
	/**
	 *  Obtiene el Recibo de Cuota (ReciboConvenio) por Nro de Recibo y Anio. 
	 * @param numero
	 * @param anio
	 * @return
	 */
	public ReciboConvenio getByNroYAnio(Long numero, Long anio){
		ReciboConvenio reciboConvenio;
		String queryString = "from ReciboConvenio t where t.nroRecibo = :numero";
		queryString += " and t.anioRecibo = :anio";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("numero", numero);
		query.setLong("anio", anio);
		reciboConvenio = (ReciboConvenio) query.uniqueResult();	

		return reciboConvenio; 
	}
	
	/**
	 *  Obtiene el Recibo de Cuota (ReciboConvenio) por su N&uacutemero. 
	 * @param numero
	 * @param idserviciobanco
	 * @return reciboConvenio
	 */
	public ReciboConvenio getByNumeroYSerBan(Long numero, Long idSerBan){
		ReciboConvenio reciboConvenio;
		String queryString = "from ReciboConvenio t where t.nroRecibo = :numero";
		queryString += " and t.servicioBanco.id = :idSerBan";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("numero", numero)
													  .setLong("idSerBan", idSerBan);
		reciboConvenio = (ReciboConvenio) query.uniqueResult();	

		return reciboConvenio; 
	}
	
	/**
	 *  Obtiene el Recibo de Cuota (ReciboConvenio) por Nro de Recibo y Anio. 
	 * @param numero
	 * @param anio
	 * @param idserviciobanco
	 * @return
	 */
	public ReciboConvenio getByNroYAnioYSerBan(Long numero, Long anio, Long idSerBan){
		ReciboConvenio reciboConvenio;
		String queryString = "from ReciboConvenio t where t.nroRecibo = :numero";
		queryString += " and t.anioRecibo = :anio";
		queryString += " and t.servicioBanco.id = :idSerBan";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("numero", numero)
		  											  .setLong("idSerBan", idSerBan);
		query.setLong("anio", anio);
		reciboConvenio = (ReciboConvenio) query.uniqueResult();	

		return reciboConvenio; 
	}
	
	/**
	 * Verifica si existe una cuota Saldo emitida sin vencer a la fecha actual
	 */
	public boolean tieneCuotaSaldo (Convenio convenio){

		ReciboConvenio reciboConvenio;
		Date fecha = new Date();
		String queryString = "from ReciboConvenio t where t.fechaPago is null";
		queryString += " and t.fechaVencimiento >= :fecha ";
		queryString += " and t.esCuotaSaldo = 1";
		queryString += " and t.convenio.id = :idConvenio";
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString).setDate("fecha", fecha);
		query.setLong("idConvenio", convenio.getId());
		query.setMaxResults(1);
		reciboConvenio=(ReciboConvenio) query.uniqueResult();
		if (reciboConvenio != null){
			return true;
		}else{
			return false;
		}
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
	 *  Inserta una linea con los datos del ReciboConvenio para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param reciboConvenio, output - El ReciboConvneio a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(ReciboConvenio o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idReciboConvenio.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|idconvenio|codrefpag|nrorecibo|aniorecibo|idserviciobanco|idcanal|fechageneracion|escuotasaldo|fechavencimiento|idsellado|importesellado|fechapago|idbancopago|totimporterecibo|estaimpreso|usuario|fechaultmdf|estado
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getConvenio().getId());		 
		line.append("|");
		line.append(o.getCodRefPag());
		line.append("|");
		line.append(o.getNroRecibo());		 
		line.append("|");
		line.append(o.getAnioRecibo());		 
		line.append("|");
		line.append(o.getServicioBanco().getId());
		line.append("|");
		line.append(o.getCanal().getId());		 
		line.append("|");
		if(o.getFechaGeneracion()!=null){
			line.append(DateUtil.formatDate(o.getFechaGeneracion(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getEsCuotaSaldo()!=null){			
			line.append(o.getEsCuotaSaldo());		 
		}
		line.append("|");
		if(o.getFechaVencimiento()!=null){
			line.append(DateUtil.formatDate(o.getFechaVencimiento(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getSellado()!=null)
			line.append(o.getSellado().getId());		 
		line.append("|");
		if(o.getImporteSellado()!=null)
			line.append(decimalFormat.format(o.getImporteSellado()));
		line.append("|");
		if(o.getFechaPago()!=null){
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getBancoPago()!=null)
			line.append(o.getBancoPago().getId());
		line.append("|");
		line.append(decimalFormat.format(o.getTotImporteRecibo()));
		line.append("|");
		line.append(o.getEstaImpreso());
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


	public List<ReciboConvenio> getBySearchPage(ReciboSearchPage reciboSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ReciboConvenio t ";
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
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.codRefPag="+reciboSearchPage.getRecibo().getCodRefPag(); 
			flagAnd = true;
		}
		
		// NroRecibo		
		if(reciboSearchPage.getRecibo().getNroRecibo() != null){
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			
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
 			queryString += flagAnd ? " AND " : " 	WHERE ";
 			queryString += " t.convenio.recurso.id="+reciboSearchPage.getRecibo().getRecurso().getId(); 
			flagAnd = true;
		}
		
		// Numero Cuenta
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta())) {
            queryString += flagAnd ? " AND " : " 	WHERE ";
			
            if (reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta().startsWith("-") && 
            		StringUtil.isNumeric(reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta())){
            	queryString += " t.convenio.cuenta.id = " + reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta().substring(1) ;
 			} else {  	
 				queryString += " t.convenio.cuenta.numeroCuenta = '" + 
 					StringUtil.formatNumeroCuenta(reciboSearchPage.getRecibo().getCuenta().getNumeroCuenta()) + "'";
 			}
            
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.anioRecibo, t.nroRecibo ";
				
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ReciboConvenio> listReciboConvenio = (ArrayList<ReciboConvenio>) executeCountedSearch(queryString, reciboSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listReciboConvenio;
	}

	/**
	 * Lista de todas los recibos de convenio donde figura esta cuota
	 * ordenada por fecha descendente
	 * @param cuota
	 * @return
	 */
	public List<ReciboConvenio> getReciboConvenioByCuota(ConvenioCuota cuota) throws Exception {
		List<ReciboConvenio> listReciboConvenio;
		Session session = SiatHibernateUtil.currentSession();
				
		String sql = "select recibo from RecConCuo as recConCuo join recConCuo.reciboConvenio as recibo";
		sql += " where recConCuo.convenioCuota.id = :idCuota";
		
		Query query = session.createQuery(sql).setLong("idCuota", cuota.getId());
		listReciboConvenio = (ArrayList<ReciboConvenio>) query.list();	

		return listReciboConvenio;
	}

	public static void setMigId(long migId) {
		ReciboConvenioDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

}
