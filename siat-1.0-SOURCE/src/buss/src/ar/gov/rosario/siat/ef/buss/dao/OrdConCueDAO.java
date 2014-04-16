//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.OrdConCue;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;

public class OrdConCueDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OrdConCueDAO.class);

	private static long migId = -1;
	
	public OrdConCueDAO() {
		super(OrdConCue.class);
	}

	
	public OrdConCue getByCuentaOrdCon(OrdenControl ordenControl, Cuenta cuenta) {
		String queryString = "from OrdConCue t where t.ordenControl = :ordenControl AND" +
				" t.cuenta = :cuenta";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("ordenControl", ordenControl)
													  .setEntity("cuenta", cuenta);
		OrdConCue ordConCue = (OrdConCue) query.uniqueResult();	

		return ordConCue; 
	}


	public List<OrdConCue> getByOrdCon(OrdenControl ordenControl, List<OrdConCue> listNotIn) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "select distinct p.ordConCue from PeriodoOrden p where p.ordenControl = :ordenControl";
		if (listNotIn!=null)
			queryString += " AND p.ordConCue.id not In ("+ListUtil.getStringIds(listNotIn)  +")";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString).setEntity("ordenControl", ordenControl);
		
		return query.list();

	}

	public List<OrdConCue> getDistinct(OrdenControl ordenControl){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "select distinct(t) from OrdConCue t where t.ordenControl = :ordenControl ";
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString).setEntity("ordenControl", ordenControl);
		
		return query.list();		
	}

	public static void setMigId(long migId) {
		OrdConCueDAO.migId = migId;
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
	public Long createForLoad(OrdConCue o, LogFile output) throws Exception {

		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|idordencontrol|idcuenta|usuario|fechaultmdf|estado|
		
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getOrdenControl().getId());
		line.append("|");
		line.append(o.getCuenta().getId());
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
	
	public List<OrdConCue>getListInPeriodoOrden(OrdenControl ordenControl){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT DISTINCT p.ordConCue FROM PeriodoOrden p WHERE p.ordenControl.id = "+ordenControl.getId();
		
		Query query = session.createQuery(queryString);
		
		return (List<OrdConCue>) query.list();
	}

}
