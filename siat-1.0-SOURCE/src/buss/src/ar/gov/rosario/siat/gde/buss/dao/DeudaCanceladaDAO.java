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
import ar.gov.rosario.siat.gde.buss.bean.DeudaCancelada;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class DeudaCanceladaDAO extends DeudaDAO {
	
	private Log log = LogFactory.getLog(DeudaCanceladaDAO.class);	
	
	private static long migId = -1;
	
	public DeudaCanceladaDAO() {
		super(DeudaCancelada.class);
	}

	 public DeudaCancelada getByCodRefPag(Long codRefPag){
			DeudaCancelada deudaCancelada;
			String queryString = "from DeudaCancelada t where t.codRefPag = :codRefPag";
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString).setLong("codRefPag", codRefPag);
			deudaCancelada = (DeudaCancelada) query.uniqueResult();	

			return deudaCancelada; 
	}

	 public DeudaCancelada getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
			DeudaCancelada deudaCancelada;
			String queryString = "from DeudaCancelada t where t.cuenta.numeroCuenta = :nroCta and t.periodo = :per";
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
			deudaCancelada = (DeudaCancelada) query.uniqueResult();	

			return deudaCancelada; 
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
		 *  Inserta una linea con los datos de la Deuda Cancelada para luego realizar un load desde Informix.
		 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
		 * @param deudaCancelada, output - La Deuda Cancelada a crear y el Archivo al que se le agrega la linea.
		 * @return long - El id generado.
		 * @throws Exception
		 */
		public Long createForLoad(DeudaCancelada o, LogFile output) throws Exception {

			 // Obtenemos el valor del id autogenerado a insertar.
			 long id = 0;
			 if(getMigId() == -1){
				 long idJudicial = this.getLastId(output.getPath(), "deudaJudicial.txt");
				 long idAdmin = this.getLastId(output.getPath(), "deudaAdmin.txt");
				 long idCancelada = this.getLastId(output.getPath(), output.getNameFile());
				 long idAnulada = this.getLastId(output.getPath(), "deudaAnulada.txt");
				 long idPrescripta = this.getLastId(output.getPath(), "deudaPrescripta.txt");
				 if(idCancelada>=idJudicial && idCancelada>=idAdmin && idCancelada>=idAnulada && idCancelada>=idPrescripta){
					 id = getNextId(output.getPath(), output.getNameFile());				 
				 }else{
					 if(idAdmin>=idJudicial && idAdmin>=idAnulada && idAdmin>=idPrescripta)
						 id = getNextId(output.getPath(), "deudaAdmin.txt");
					 else if(idJudicial>=idAnulada && idJudicial>=idPrescripta)
						 id = getNextId(output.getPath(), "deudaJudicial.txt");
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
				 setMigId(id);				 
			 }else{
				 id = getNextId(output.getPath(), output.getNameFile());
			 }
			
			DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
			// Estrucura de la linea:
			// id|codrefpag|idcuenta|idsistema|idreccladeu|idviadeuda|idestadodeuda|idRecurso|anio|periodo|fechavencimiento|fechapago|importe|importebruto|saldo|actualizacion|fechaemision|estaimpresa|resto|reclamada|idprocurador|idconvenio|usuario|fechaultmdf|estado|
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

			//line.append(o.getServicioBanco().getId()); se quito el servicio banco de la deuda
			//line.append("|");
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

			line.append("2010-01-01 00:00:00");

			line.append("|");
			line.append("1");
			line.append("|");

			output.addline(line.toString());

			// Seteamos el id generado en el bean.
			o.setId(id);

			return id;
		}

		public List<DeudaCancelada> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
			List<DeudaCancelada> listDeudaCancelada;
			String queryString = "from DeudaCancelada t where t.cuenta.numeroCuenta = :nroCta and t.periodo = :per";
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
			listDeudaCancelada = (ArrayList<DeudaCancelada>) query.list();	

			return listDeudaCancelada; 
		}

		public static void setMigId(long migId) {
			DeudaCanceladaDAO.migId = migId;
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
			
			String queryString = "FROM DeudaCancelada deuda " + 
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
			
			String queryString = "FROM DeudaCancelada deuda " + 
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
