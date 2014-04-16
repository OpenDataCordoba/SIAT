//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAnulada;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class DeudaAnuladaDAO extends DeudaDAO {

	private Log log = LogFactory.getLog(DeudaAnuladaDAO.class);	
	
	private static long migId = -1;
	
	public DeudaAnuladaDAO() {
		super(DeudaAnulada.class);
	}

	public DeudaAnulada getByCodRefPag(Long codRefPag){
		DeudaAnulada deudaAnulada;
		String queryString = "from DeudaAnulada t where t.codRefPag = :codRefPag";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("codRefPag", codRefPag);
		deudaAnulada = (DeudaAnulada) query.uniqueResult();	

		return deudaAnulada; 
	}

	public DeudaAnulada getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		DeudaAnulada deudaAnulada;
		String queryString = "from DeudaAnulada t where t.cuenta.numeroCuenta = :nroCta and t.periodo = :per";
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
		query.setMaxResults(1);
		deudaAnulada = (DeudaAnulada) query.uniqueResult();	

		return deudaAnulada; 
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
	  *  Inserta una linea con los datos de la Deuda Anulada para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param deudaAnulada, output - La Deuda Anulada a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(DeudaAnulada o, LogFile output) throws Exception {
		 
		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(migId == -1){
			 long idAnulada = this.getLastId(output.getPath(), output.getNameFile());
			 long idPrescripta = this.getLastId(output.getPath(), "deudaPrescripta.txt");
			 long idJudicial = this.getLastId(output.getPath(), "deudaJudicial.txt");
			 long idAdmin = this.getLastId(output.getPath(), "deudaAdmin.txt");
			 long idCancelada = this.getLastId(output.getPath(), "deudaCancelada.txt");
			 if(idAnulada>=idAdmin && idAnulada>=idCancelada && idAnulada>=idJudicial && idAnulada>=idPrescripta){
				 id = getNextId(output.getPath(), output.getNameFile());				 
			 }else{
				 if(idCancelada>=idAdmin && idCancelada>=idJudicial && idCancelada>=idPrescripta)
					 id = getNextId(output.getPath(), "deudaCancelada.txt");
				 else if(idAdmin >= idJudicial && idAdmin >= idPrescripta)
					 id = getNextId(output.getPath(), "deudaAdmin.txt");
				 else if(idJudicial >= idPrescripta)
					 id = getNextId(output.getPath(), "deudaJudicial.txt");
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
		 // id|codrefpag|idcuenta|idsistema|idreccladeu|idviadeuda|idestadodeuda|idrecurso|anio|periodo|fechavencimiento|fechapago|importe|importebruto|saldo|actualizacion|fechaemision|estaimpresa|resto|reclamada|idprocurador|idconvenio|fechaanulacion|idmotanudeu|usuario|fechaultmdf|estado|
   	 StringBuffer line = new StringBuffer();
		 line.append(id);		 
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

		// line.append(o.getServicioBanco().getId()); se quitó el servicio banco de la deuda
		// line.append("|");
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
		 line.append(DateUtil.formatDate(o.getFechaAnulacion(), "yyyy-MM-dd"));
		 line.append("|");
		 line.append(o.getMotAnuDeu().getId());		 
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
	 * Obtiene todos los registros de deuda de la tabla gde_deudaAnulada.
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @return
	 */
	public List<DeudaAnulada> getListDeudaAnulada(Cuenta cuenta){
		Session session = SiatHibernateUtil.currentSession();
		
		String sQuery = "FROM DeudaAnulada d " +
						"WHERE d.cuenta = :cuenta " +						
						" ORDER BY d.viaDeuda, d.anio, d.periodo";

		Query query = session.createQuery(sQuery)
						.setEntity("cuenta", cuenta);
		
		return query.list();		
	}
	
	public List<DeudaAnulada> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		List<DeudaAnulada> listDeudaAnulada;
		String queryString = "from DeudaAnulada t where t.cuenta.numeroCuenta = :nroCta and t.periodo = :per";
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
		listDeudaAnulada = (ArrayList<DeudaAnulada>) query.list();	

		return listDeudaAnulada; 
	}

	public static void setMigId(long migId) {
		DeudaAnuladaDAO.migId = migId;
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
		
		String queryString = "FROM DeudaAnulada deuda " + 
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
		
		deuda = (Deuda) query.setMaxResults(1).uniqueResult();	

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
		
		String queryString = "FROM DeudaAnulada deuda " + 
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
