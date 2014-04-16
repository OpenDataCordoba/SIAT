//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.OrdConCue;
import ar.gov.rosario.siat.ef.buss.bean.PeriodoOrden;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class PeriodoOrdenDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(PeriodoOrdenDAO.class);
	private static long migId = -1;
	
	public PeriodoOrdenDAO() {
		super(PeriodoOrden.class);
	}
	
	/**
	 * Obtiene un PeriodosOrden por su codigo
	 */
	public PeriodoOrden getByCodigo(String codigo) throws Exception {
		PeriodoOrden periodosOrden;
		String queryString = "from PeriodosOrden t where t.codPeriodosOrden = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		periodosOrden = (PeriodoOrden) query.uniqueResult();	

		return periodosOrden; 
	}

	public List<PeriodoOrden> getListByOrdConCue(OrdConCue ordConCue) {
		String queryString = "from PeriodoOrden t where t.ordConCue = :ordConCue ORDER BY t.anio, t.periodo";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("ordConCue", ordConCue);

		return query.list();
	}
	
	public static void setMigId(long migId) {
		PeriodoOrdenDAO.migId = migId;
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
	
	
	public Long createForLoad(PeriodoOrden o, LogFile output) throws Exception {

		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|idordencontrol|idordconcue|periodo|anio|usuario|fechaultmdf|estado|
		
		StringBuffer line = new StringBuffer();
		line.append(id);
		line.append("|");
		line.append(o.getOrdenControl().getId());
		line.append("|");
		line.append(o.getOrdConCue().getId());
		line.append("|");
		line.append(o.getAnio());
		line.append("|");
		line.append(o.getPeriodo());
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
