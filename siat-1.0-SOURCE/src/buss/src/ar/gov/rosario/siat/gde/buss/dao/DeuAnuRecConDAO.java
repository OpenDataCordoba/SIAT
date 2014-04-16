//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.gde.buss.bean.DeuAnuRecCon;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class DeuAnuRecConDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(DeuAnuRecConDAO.class);	

	private static long migId = -1;
	
	public DeuAnuRecConDAO() {
		super(DeuAnuRecCon.class);
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
	  *  Inserta una linea con los datos del Concepto de Deuda Anulada para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param deuAnuRecCon, output - El Concepto de Deuda Anulada a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(DeuAnuRecCon o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(migId == -1){
			 long idAnulada = this.getLastId(output.getPath(), output.getNameFile());
			 long idPrescripta = this.getLastId(output.getPath(), "deuPreRecCon.txt");
			 long idJudicial = this.getLastId(output.getPath(), "deuJudRecCon.txt");
			 long idAdmin = this.getLastId(output.getPath(), "deuAdmRecCon.txt");
			 long idCancelada = this.getLastId(output.getPath(), "deuCanRecCon.txt");
			 if(idAnulada>=idAdmin && idAnulada>=idCancelada && idAnulada>=idJudicial && idAnulada>=idPrescripta){
				 id = getNextId(output.getPath(), output.getNameFile());				 
			 }else{
				 if(idCancelada>=idAdmin && idCancelada>=idJudicial && idCancelada>=idPrescripta)
					 id = getNextId(output.getPath(), "deuCanRecCon.txt");
				 else if(idAdmin >= idJudicial && idAdmin >= idPrescripta)
					 id = getNextId(output.getPath(), "deuAdmRecCon.txt");
				 else if(idJudicial >= idPrescripta)
					 id = getNextId(output.getPath(), "deuJudRecCon.txt");
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
		 line.append("2010-01-01 00:00:00");
		 line.append("|");
		 line.append("1");
		 line.append("|");
		 
		 output.addline(line.toString());
		 
		 // Seteamos el id generado en el bean.
		 o.setId(id);
	
		 return id;
	 }

	public static void setMigId(long migId) {
		DeuAnuRecConDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}

}
