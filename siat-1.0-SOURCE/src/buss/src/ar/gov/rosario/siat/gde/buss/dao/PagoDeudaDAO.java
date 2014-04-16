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
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class PagoDeudaDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(PagoDeudaDAO.class);	

	private static long migId = -1;
	
	public PagoDeudaDAO() {
		super(PagoDeuda.class);
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
	 * Verifica si hubo Pagos posteriores a la fecha de realizar un Saldo por Caducidad
	 * en las deudas comprendidas por el Convenio
	 * @param idDeuda
	 * @param idConvenio
	 * @param fechaSaldo
	 * @return Boolean
	 * @throws Exception
	 */
	public boolean tienePagosPostASalPorCadConvenio (Long idDeuda, Long idConvenio, Date fechaSaldo) throws Exception{
		PagoDeuda pagoDeuda;
		String queryString = "from PagoDeuda t where t.idDeuda = :idDeuda ";
			   queryString += " and t.fechaPago >= :fechaSaldo";
			   queryString += " and t.idPago <> :idConvenio";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString).setLong("idDeuda", idDeuda);
			  query.setLong("idConvenio", idConvenio);
			  query.setDate("fechaSaldo", fechaSaldo);
			  query.setMaxResults(1);
			  
		pagoDeuda = (PagoDeuda) query.uniqueResult();
		if (pagoDeuda !=null){
			return true;
		}else{
			return false;
		}
		
	}
	/**
	 * Obtiene una lista de Pagos efectuados para una deuda dentro de un convenio determinado
	 * @param idDeuda
	 * @param idConvenio
	 * @return List<PagoDeuda>
	 * @throws Exception
	 */
	public List<PagoDeuda> getListPagosByIdConvenio (Long idDeuda, Long idConvenio) throws Exception{
		List <PagoDeuda> pagosDeudas;
		String queryString = "from PagoDeuda t where t.idDeuda = :idDeuda ";
			   queryString += " and (t.tipoPago.id = :idPagoBueno";
			   queryString += " OR t.tipoPago.id = :idPagoACuenta";
			   queryString += ") AND t.idPago = :idConvenio";
			   
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString).setLong("idDeuda", idDeuda);
			  query.setLong("idPagoBueno", TipoPago.ID_PAGO_BUENO);
			  query.setLong("idPagoACuenta", TipoPago.ID_PAGO_A_CUENTA);
			  query.setLong("idConvenio", idConvenio);
		pagosDeudas = (List<PagoDeuda>) query.list();
		return pagosDeudas;
		
	}
	
	public void deletePagosDeConvenio(Long idConvenio)throws Exception{
		
		String queryString = "DELETE FROM PagoDeuda t WHERE t.idPago = :idConvenio ";
			   queryString += "AND (t.tipoPago.id = :idPagoBueno";
			   queryString += " OR t.tipoPago.id = :idPagoACuenta"+")";
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString).setLong("idConvenio", idConvenio);
			  query.setLong("idPagoBueno", TipoPago.ID_PAGO_BUENO);
			  query.setLong("idPagoACuenta", TipoPago.ID_PAGO_A_CUENTA);
		
		query.executeUpdate();
	}

	
	public void deletePagosDDJJ(List<DecJur> listDecJur, String idsTipoPago)throws Exception{
		
		if (ListUtil.isNullOrEmpty(listDecJur) || StringUtil.isNullOrEmpty(idsTipoPago)) 
			return;
		
		String idsDecJur = ListUtil.getStringIds(listDecJur);
		String queryString = "DELETE FROM PagoDeuda p WHERE ";
			   queryString += " p.idPago IN ("+idsDecJur+") AND";
		 	   queryString += " p.tipoPago.id IN ("+idsTipoPago+")";
			
		log.debug("QUERYSTRING: "+queryString);
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
			  
		query.executeUpdate();
	}

	/**
	 *  Inserta una linea con los datos del Pago de Deuda para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param pagoDeuda, output - El PagoDeuda a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(PagoDeuda o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(migId == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idPagoDeuda.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 migId = id;				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		 
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|iddeuda|idtipopago|idpago|fechapago|importe|actualizacion|idbancopago|espagototal|usuario|fechaultmdf|estado
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getIdDeuda());
		line.append("|");
		line.append(o.getTipoPago().getId());		 
		line.append("|");
		if(o.getIdPago()!=null)
			line.append(o.getIdPago());		 
		line.append("|");
		if(o.getFechaPago()!=null){
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(decimalFormat.format(o.getImporte()));
		line.append("|");
		line.append(decimalFormat.format(o.getActualizacion()));
		line.append("|");
		line.append(o.getBancoPago().getId());
		line.append("|");
		line.append(o.getEsPagoTotal());
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
	 * Devuelve una lista de PagoDeuda filtrando por el idDeuda pasado por parametro
	 * @param idDeuda
	 * @return
	 */
	public List<PagoDeuda> getByDeuda(Long idDeuda) {
		String queryString = "from PagoDeuda t where t.idDeuda = "+idDeuda +
			" ORDER BY t.fechaPago";
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		List<PagoDeuda> listResult = (ArrayList<PagoDeuda>)query.list();

		return listResult;
	}
	
	public List<PagoDeuda> getByDeudaFecha(Long idDeuda,Date fecha) {
		String queryString = "from PagoDeuda t where t.idDeuda = "+idDeuda +
							 " and t.fechaPago > :fecha" ;
			
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString).setDate("fecha", fecha);
		List<PagoDeuda> listResult = (ArrayList<PagoDeuda>)query.list();

		return listResult;
	}
	
	public List<PagoDeuda> getByConvenioyPagoCuenta (Convenio convenio){
		
		List<PagoDeuda>listPagoDeuda;
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "FROM PagoDeuda p WHERE  p.tipoPago.id = "+TipoPago.ID_PAGO_A_CUENTA;
			   queryString += " AND p.idPago = " + convenio.getId();
			   queryString += " ORDER BY p.fechaPago, p.id ASC";
		
		Query query = session.createQuery(queryString);
		
		listPagoDeuda = (List<PagoDeuda>)query.list();
		
		return listPagoDeuda;
	}

	public static void setMigId(long migId) {
		PagoDeudaDAO.migId = migId;
	}

	public static long getMigId() {
		return PagoDeudaDAO.migId;
	}
	
	
	/**
	 * Retorna los pagos posteriores o iguales a la fecha pasada de cada iddeuda pasado en la lista
	 * @param listIds
	 * @param fecha
	 * @return
	 */
	public List<PagoDeuda>getListByListIdDeudayFecha(List<Long> listIds,Date fecha){
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "FROM PagoDeuda p WHERE p.idDeuda IN ("+ListUtil.getStringIds(listIds)+" )";
			   queryString += " AND p.fechaPago >= :fecha";
		
		Query query = session.createSQLQuery(queryString).setDate("fecha", fecha);
		
		return (List<PagoDeuda>) query.list();
	}

	
	/**
	 * Devuelve el PagoDeuda con fecha mas reciente del tipo pasado como parametro
	 * @param idDeuda
	 * @return
	 */
	public PagoDeuda getMoreRecentByDeudaAndTipoPago(Long idDeuda, Long idTipoPago) {
		String queryString="FROM PagoDeuda t WHERE t.idDeuda = "+idDeuda;
			   queryString+=" AND t.tipoPago.id = "+idTipoPago;
			   queryString+=" ORDER BY t.fechaPago DESC";
			
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);

		return (PagoDeuda) query.uniqueResult();
	}
	
}
