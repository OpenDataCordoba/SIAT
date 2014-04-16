//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.cyq.buss.bean.HisEstProced;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class HisEstProcedDAO extends GenericDAO {

	private Log log = LogFactory.getLog(HisEstProcedDAO.class);	
	
	private static long migId = -1;
	
	public HisEstProcedDAO() {
		super(HisEstProced.class);
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
	 *  Inserta una linea con los datos del ProEstPro para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param proEstPro, output - El ProEstPro a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(HisEstProced o, LogFile output) throws Exception {

		// Obtenemos el valor del id autogenerado a insertar.
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|idprocedimiento|idestprocedimiento|fechaproestpro|usuario|fechaultmdf|estado
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getProcedimiento().getId());
		line.append("|");
		line.append(o.getEstadoProced().getId());		 
		line.append("|");
		if(o.getFecha()!=null){ 
			line.append(DateUtil.formatDate(o.getFecha(), "yyyy-MM-dd HH:mm:ss"));		 
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

}
