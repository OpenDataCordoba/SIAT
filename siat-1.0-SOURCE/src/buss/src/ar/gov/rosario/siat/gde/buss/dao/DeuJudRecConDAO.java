//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeuJudRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.DevolucionDeuda;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class DeuJudRecConDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(DeuJudRecConDAO.class);	

	private static long migId = -1;
	
	public DeuJudRecConDAO() {
		super(DeuJudRecCon.class);
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
	  *  Inserta una linea con los datos del Concepto de Deuda Judicial para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param deuJudRecCon, output - El Concepto de Deuda Judicial a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(DeuJudRecCon o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(migId == -1){
			 long idJudicial = this.getLastId(output.getPath(), output.getNameFile());
			 long idAdmin = this.getLastId(output.getPath(), "deuAdmRecCon.txt");
			 long idCancelada = this.getLastId(output.getPath(), "deuCanRecCon.txt");
			 long idAnulada = this.getLastId(output.getPath(), "deuAnuRecCon.txt");
			 long idPrescripta = this.getLastId(output.getPath(), "deuPreRecCon.txt");
			 if(idJudicial>=idCancelada && idJudicial>=idAdmin && idJudicial>=idAnulada && idJudicial>=idPrescripta){
				 id = getNextId(output.getPath(), output.getNameFile());				 
			 }else{
				 if(idAdmin>=idCancelada && idAdmin>=idAnulada && idAdmin>=idPrescripta)
					 id = getNextId(output.getPath(), "deuAdmRecCon.txt");
				 else if(idCancelada>=idAnulada && idCancelada>=idPrescripta)
					 id = getNextId(output.getPath(), "deuCanRecCon.txt");
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
		 
     	 DecimalFormat decimalFormat = new DecimalFormat("#.0000000000");
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

		public int copiarAJudicialJdbc(ProcesoMasivo procesoMasivo) throws Exception {
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");

			// TODO ver si copiamos todos los campos
			String sqlInsert = "INSERT INTO gde_deujudreccon " +
				"SELECT * FROM gde_deuadmreccon darc WHERE darc.iddeuda IN ( " +
					"SELECT iddeuda FROM gde_condeudet cdd  " +
					"INNER JOIN gde_constanciadeu cd ON (cdd.idconstanciadeu == cd.id) " + 
					" WHERE cd.idProcesoMasivo = " + procesoMasivo.getId() + " )";
			
			if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + sqlInsert);

			Connection        con;
			PreparedStatement ps;

			// ahora nos conseguimos la connection JDBC de hibernate...
			con = SiatHibernateUtil.currentSession().connection();
			con.setAutoCommit(false);
			// ejecucion de la consulta de resultado
			ps = con.prepareStatement(sqlInsert);
			int resultado = ps.executeUpdate();
			ps.close();
			
			return resultado;
		}

		public int copiarAJudicial(DeudaAdmin deudaAdmin) throws Exception {

			String sqlInsert = "INSERT INTO gde_deujudreccon " +
			"SELECT darc.* FROM gde_deuadmreccon darc " +
			"WHERE darc.iddeuda == " + deudaAdmin.getId();

			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createSQLQuery(sqlInsert);

			return  query.executeUpdate();
		}
		
		public int deleteListDeuJudRecConByDevolucionDeuda (DevolucionDeuda devolucionDeuda){

			String queryString = "DELETE FROM DeuJudRecCon djrc WHERE djrc.deuda IN (" +
					"SELECT idDeuda FROM DevDeuDet ddd WHERE ddd.devolucionDeuda = :devolucionDeuda) ";
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(queryString);

			query.setEntity("devolucionDeuda", devolucionDeuda);
		    
			return query.executeUpdate();
		}

		
		public int deleteListDeuJudRecConByDeudaJudicial (DeudaJudicial deudaJudicial){

			String queryString = "DELETE FROM DeuJudRecCon djrc WHERE djrc.deuda = :deudaJudicial ";
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(queryString);

			query.setEntity("deudaJudicial", deudaJudicial);
		    
			return query.executeUpdate();
		}


		 public long deleteListDeudaJudicialAdministrativaByProcesoMasivo(ProcesoMasivo procesoMasivo){

			 Session session = SiatHibernateUtil.currentSession();
			 int first = 5000;
			 long totreg = 0;
			 while (true) {
				 
				 String sqlQuery = "SELECT FIRST " + first + " pmdi.idDeuda FROM gde_proMasDeuInc pmdi " +
				 "INNER JOIN gde_deudaAdmin da ON (pmdi.idDeuda == da.id) " +
				 "INNER JOIN gde_deudaJudicial dj ON (pmdi.idDeuda == dj.id) " +
				 "WHERE pmdi.idProcesoMasivo=" + procesoMasivo.getId();

				Query query = session.createSQLQuery(sqlQuery).addScalar("idDeuda",Hibernate.LONG);
				 
				List<Long> ids = (ArrayList<Long>) query.list();
				StringBuilder sb = new StringBuilder("");
				boolean primero = true;
				for(Long id: ids) {
					if(!primero) sb.append(", "); primero = false;
					sb.append(id);
				}
				
				String sqlDelete = "DELETE FROM gde_deuJudRecCon WHERE id IN (" + sb.toString() + ")";

				Query queryDelete = session.createSQLQuery(sqlDelete);
				 int n = queryDelete.executeUpdate();
				 session.getTransaction().commit();
				 session.beginTransaction();

				 totreg += n; 
				 //AdpRun.changeRunMessage("Borrando DeuJudRecCon - " + totreg + " registros.", 0);
				 if (n < first)
					 break;
			 }
			 return totreg;
		 }

		public static void setMigId(long migId) {
			DeuJudRecConDAO.migId = migId;
		}

		public static long getMigId() {
			return migId;
		}
		
		
		public DeuJudRecCon getByDeudaAndRecCon(Deuda deuda, RecCon recCon){
			
			Session session = SiatHibernateUtil.currentSession();
			
			String queryString = "FROM DeuJudRecCon d where d.deuda.id = "+deuda.getId();
			queryString += " AND d.recCon.id = "+recCon.getId();
			
			Query query = session.createQuery(queryString);
			
			return (DeuJudRecCon)query.uniqueResult();
		
		}

		
}
