//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.esp.buss.bean.PrecioEvento;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class PrecioEventoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PrecioEventoDAO.class);	
	
	private static long migId = -1;
	
	public PrecioEventoDAO(){
		super(PrecioEvento.class);
	}
	
	public List<PrecioEvento> getListByHabilitacion(Long idHabilitacion) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from PrecioEvento t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idHabilitacion: " + idHabilitacion); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.habilitacion.id = " + idHabilitacion;

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<PrecioEvento> listPrecioEvento = (ArrayList<PrecioEvento>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listPrecioEvento;
	}

	
	public static long getMigId() {
		return migId;
	}
	public static void setMigId(long migId) {
		PrecioEventoDAO.migId = migId;
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
	
	public Long createForLoad(PrecioEvento o, LogFile output) throws Exception {
		
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|idHabilitacion|idTipoEntrada|precio|usuario|fechaultmdf|estado|
		
		DecimalFormat decimalFormat = new DecimalFormat("#.0000000000");
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getHabilitacion().getId());
		line.append("|");
		line.append(o.getTipoEntrada().getId());
		line.append("|");
		line.append(decimalFormat.format(o.getPrecio()));
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

