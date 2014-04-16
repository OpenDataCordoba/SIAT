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
import ar.gov.rosario.siat.gde.buss.bean.ConDeuCuo;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ConDeuCuoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConDeuCuoDAO.class);	
	
	private static long migId = -1;
	
	public ConDeuCuoDAO() {
		super(ConDeuCuo.class);
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
	 *  Inserta una linea con los datos del ConDeuCuo para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param conDeuCuo, output - El ConDeuCuo a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(ConDeuCuo o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idConDeuCuo.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }

		//long id = getNextId(output.getPath(), output.getNameFile());
		 
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|idconveniocuota|idconveniodeuda|fechapago|saldoenplancub|espagototal|usuario|fechaultmdf|estado 
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getConvenioCuota().getId());
		line.append("|");
		line.append(o.getConvenioDeuda().getId());
		line.append("|");
		if(o.getFechaPago()!=null){
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(decimalFormat.format(o.getSaldoEnPlanCub()));
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
	public Boolean getEsPagoTotal (Long idConvenioDeuda)throws Exception{
		log.debug("Ingresando a Es Pago Total..........");
		ConDeuCuo conDeuCuo;
		String queryString = "from ConDeuCuo t where t.convenioDeuda.id = "+ idConvenioDeuda;
		queryString += " and t.esPagoTotal = " + 1;
		log.debug("queryString.."+queryString);
		try{
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(queryString);
			query.setMaxResults(1);
			conDeuCuo =(ConDeuCuo)query.uniqueResult();
			if (conDeuCuo == null){
				return false;
			}else{
				return true;
			}
		}catch (Exception e){
			log.debug("Pinchado por.."+e);
			return false;
		}
	}


	/**
	 * Obtiene el conDeuCuo con menor idConvenioDeuda, para el convenioCuota pasado como parametro
	 * @param id
	 * @return
	 */
	public ConDeuCuo getConMenorConvenioDeuda(Long idConvenioCuota) {
		String queryString = "from ConDeuCuo t where t.convenioCuota.id = "+ idConvenioCuota;
		queryString +=" ORDER BY t.convenioDeuda.id ASC";
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString).setMaxResults(1);
		log.debug("Va a buscar en ConDeuCuo - query:"+query.getQueryString());		
		return (ConDeuCuo) query.uniqueResult();
	}
	
	public List<ConDeuCuo> getConDeuCuoByConvenioCuota (ConvenioCuota convenioCuota) throws Exception {
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "FROM ConDeuCuo t WHERE t.convenioCuota.id = "+ convenioCuota.getId();
		queryString += " ORDER BY t.convenioDeuda.id DESC";
		Query query = session.createQuery(queryString);
		List<ConDeuCuo> listConDeuCuo = new ArrayList<ConDeuCuo>();
		listConDeuCuo = (List<ConDeuCuo>)query.list();
		
		return listConDeuCuo;
	}
	
	public List<ConDeuCuo> getConDeuCuoByConvenioCuotaAsc (ConvenioCuota convenioCuota) throws Exception {
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "FROM ConDeuCuo t WHERE t.convenioCuota.id = "+ convenioCuota.getId();
		queryString += " ORDER BY t.convenioDeuda.id ASC";
		Query query = session.createQuery(queryString);
		List<ConDeuCuo> listConDeuCuo = new ArrayList<ConDeuCuo>();
		listConDeuCuo = (List<ConDeuCuo>)query.list();
		
		return listConDeuCuo;
	}
	
	public List<ConDeuCuo> getConDeuCuoByConvenioCuotaOrderByConvenioDeuda (ConvenioCuota convenioCuota) throws Exception {
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "FROM ConDeuCuo t WHERE t.convenioCuota.id = "+ convenioCuota.getId();
		queryString += " ORDER BY t.id ASC";
		Query query = session.createQuery(queryString);
		List<ConDeuCuo> listConDeuCuo = new ArrayList<ConDeuCuo>();
		listConDeuCuo = (List<ConDeuCuo>)query.list();
		
		return listConDeuCuo;
	}
	
	public void deleteConDeuCuoByConvenioCuota (ConvenioCuota convenioCuota) throws Exception {
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "DELETE * FROM ConDeuCuo t WHERE t.convenioCuota.id = "+ convenioCuota.getId();

		Query query = session.createQuery(queryString);
		List<ConDeuCuo> listConDeuCuo = new ArrayList<ConDeuCuo>();
		query.executeUpdate();
		
	}
	
	public Date getMayorFechaPagoByConvenioDeuda(ConvenioDeuda convenioDeuda){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString="SELECT c.fechaPago FROM ConDeuCuo c WHERE c.convenioDeuda.id = "+ convenioDeuda.getId();
		queryString +=" ORDER BY c.fechaPago DESC";
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		Date fechaPago = (Date) query.uniqueResult();
		
		return fechaPago;
	}

	public static void setMigId(long migId) {
		ConDeuCuoDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

}
