//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.DevolucionDeuda;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class DeuAdmRecConDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(DeuAdmRecConDAO.class);	

	private static long migId = -1;
	
	public DeuAdmRecConDAO() {
		super(DeuAdmRecCon.class);
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
	  *  Inserta una linea con los datos del Concepto de Deuda Administrativa para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param deuAdmRecCon, output - El Concepto de Deuda Administrativa a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(DeuAdmRecCon o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(migId == -1){
			 long idJudicial = this.getLastId(output.getPath(), "deuJudRecCon.txt");
			 long idAdmin = this.getLastId(output.getPath(), output.getNameFile());
			 long idCancelada = this.getLastId(output.getPath(), "deuCanRecCon.txt");
			 long idAnulada = this.getLastId(output.getPath(), "deuAnuRecCon.txt");
			 long idPrescripta = this.getLastId(output.getPath(), "deuPreRecCon.txt");
			 if(idAdmin>=idJudicial && idAdmin>=idCancelada && idAdmin>=idAnulada && idAdmin>=idPrescripta){
				 id = getNextId(output.getPath(), output.getNameFile());				 
			 }else{
				 if(idCancelada>=idJudicial && idCancelada>=idAnulada && idCancelada>=idPrescripta)
					 id = getNextId(output.getPath(), "deuCanRecCon.txt");
				 else if(idJudicial>=idAnulada && idJudicial>=idPrescripta)
					 id = getNextId(output.getPath(), "deuJudRecCon.txt");
				 else if(idAnulada>=idPrescripta)
					 id = getNextId(output.getPath(), "deuAnuRecCon.txt");
				 else 
					 id = getNextId(output.getPath(), "deuPreRecCon.txt");
			 }
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idDeuRecCon.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 migId = id;				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		 
     	 DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		 // Estrucura de la linea:
		 // id|iddeudaadmin|idreccon|importebruto|importe|saldo|usuario|fechaultmdf|estado|
		 // falta agregar despues la actualizacion
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getDeuda().getId());
		 line.append("|");
		 line.append(o.getRecCon().getId());		 
		 line.append("|");
		 line.append(decimalFormat.format(o.getImporteBruto()));		 
		 line.append("|");
		 line.append(decimalFormat.format(o.getImporte()));
		 line.append("|");
		 line.append(decimalFormat.format(o.getSaldo()));
		 line.append("|");
		 line.append(DemodaUtil.currentUserContext().getUserName());
		 line.append("|");
		 
		 //StringBuffer actualDate = new StringBuffer(DateUtil.formatDate(Calendar.getInstance().getTime(), "yyyy-MM-dd"));
		 //actualDate.append(" 00:00:00");
		 line.append("2010-01-01 00:00:00");//actualDate.toString());
		 
		 line.append("|");
		 line.append("1");
		 line.append("|");
		 
		 output.addline(line.toString());
		 
		 // Seteamos el id generado en el bean.
		 o.setId(id);
	
		 return id;
	 }
	 
		public int deleteListDeuAdmRecConByDeudaAdmin (DeudaAdmin deudaAdmin){

			String queryString = "DELETE FROM DeuAdmRecCon darc WHERE darc.deuda = :deudaAdmin ";
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(queryString);

			query.setEntity("deudaAdmin", deudaAdmin);
		    
			return query.executeUpdate();
		}

		
		public int copiarAAdmin(DevolucionDeuda devolucionDeuda) throws Exception {

			String sqlInsert = "INSERT INTO gde_deuadmreccon " +
			"SELECT * FROM gde_deujudreccon djrc WHERE djrc.iddeuda IN ( " +
				"SELECT iddeuda FROM gde_devdeudet ddd  " +
				" WHERE ddd.idDevolucionDeuda = " + devolucionDeuda.getId() + " )";
				 
				Session session = SiatHibernateUtil.currentSession();
				Query query = session.createSQLQuery(sqlInsert);

				return query.executeUpdate();
			}

		public int deleteListDeuAdmRecConByEmision(Emision emision){

			String queryString = "DELETE FROM DeuAdmRecCon darc WHERE darc.deuda.emision = :emision ";

			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(queryString);

			query.setEntity("emision", emision);

			return query.executeUpdate();
		}

		/**
		 * Copia los DeuJudRecCon de la DeudaJudicial a DeuAdmRecCon.
		 * @param deudaJudicial
		 * @return int
		 * @throws Exception
		 */
		public int copiarAAdministrativa(DeudaJudicial deudaJudicial) throws Exception {

			String sqlInsert = "INSERT INTO gde_deuadmreccon " +
				"SELECT djrc.* FROM gde_deujudreccon djrc " +
				"WHERE djrc.idDeuda = " + deudaJudicial.getId();
				 
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createSQLQuery(sqlInsert);

			return query.executeUpdate();
			
		}

		public static void setMigId(long migId) {
			DeuAdmRecConDAO.migId = migId;
		}

		public static long getMigId() {
			return DeuAdmRecConDAO.migId;
		}
		
		public List<DeuAdmRecCon>getListByDeudaAdmin(DeudaAdmin deudaAdmin){
			Session session = SiatHibernateUtil.currentSession();
			
			String queryString = "FROM DeuAdmRecCon d where d.deuda.id = "+deudaAdmin.getId();
			
			
			Query query = session.createQuery(queryString);
			
			return (List<DeuAdmRecCon>)query.list();
		}
		
		public DeuAdmRecCon getByDeudaAndRecCon(Deuda deuda, RecCon recCon){
			
			Session session = SiatHibernateUtil.currentSession();
			
			String queryString = "FROM DeuAdmRecCon d where d.deuda.id = "+deuda.getId();
			queryString += " AND d.recCon.id = "+recCon.getId();
			
			Query query = session.createQuery(queryString);
			
			return (DeuAdmRecCon)query.uniqueResult();
		
		}

}
