//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.DetAju;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class DetAjuDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(DetAjuDAO.class);
	private static long migId = -1;
	
	public DetAjuDAO() {
		super(DetAju.class);
	}
	
	/**
	 * Obtiene un DetAju por su codigo
	 */
	public DetAju getByCodigo(String codigo) throws Exception {
		DetAju detAju;
		String queryString = "from DetAju t where t.codDetAju = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		detAju = (DetAju) query.uniqueResult();	

		return detAju; 
	}
	
	public static void setMigId(long migId) {
		DetAjuDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
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
	 *  Inserta una linea con los datos del OrdenControl para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param OrdenControl, output - El OrdenControl a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(DetAju o, LogFile output) throws Exception {

		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|idordencontrol|idordconcue|fecha|usuario|fechaultmdf|estado|
		
		StringBuffer line = new StringBuffer();
		line.append(id);
		line.append("|");
		line.append(o.getOrdenControl().getId());
		line.append("|");
		line.append(o.getOrdConCue().getId());
		line.append("|");
		line.append(DateUtil.formatDate(o.getFecha(), "yyyy-MM-dd"));
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
