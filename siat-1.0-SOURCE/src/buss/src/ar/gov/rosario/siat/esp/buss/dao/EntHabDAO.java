//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.esp.buss.bean.EntHab;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class EntHabDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EntHabDAO.class);	
	
	private static long migId = -1;
	
	public EntHabDAO(){
		super(EntHab.class);
	}
	
	public List<EntHab> getListByHabilitacion(Long idHabilitacion) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from EntHab t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idHabilitacion: " + idHabilitacion); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.habilitacion.id = " + idHabilitacion;

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<EntHab> listEntHab = (ArrayList<EntHab>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEntHab;
	}

	public static long getMigId() {
		return migId;
	}
	public static void setMigId(long migId) {
		EntHabDAO.migId = migId;
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

	public Long createForLoad(EntHab o, LogFile output) throws Exception {
		
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
// id|idPrecioEvento|idHabilitacion|idTipoEntrada|nroDesde|nroHasta|serie|descripcion|totalVendidas|totalRestantes|usuario|fechaultmdf|estado|
		
		StringBuffer line = new StringBuffer();
		line.append(id);
		line.append("|");
		line.append(o.getPrecioEvento().getId());
		line.append("|");
		line.append(o.getHabilitacion().getId());
		line.append("|");
		line.append(o.getTipoEntrada().getId());
		line.append("|");
		line.append(o.getNroDesde());
		line.append("|");
		line.append(o.getNroHasta());
		line.append("|");
		line.append(o.getSerie());
		line.append("|");
		line.append(o.getDescripcion());
		line.append("|");
		line.append(o.getTotalVendidas());
		line.append("|");
		line.append(o.getTotalRestantes());
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
	
	public Long getCantidadHabilitadaByHabilitacion(Long idHabilitacion, Long idEntHabAExcluir) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select sum(nroHasta-nroDesde) from EntHab t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idHabilitacion: " + idHabilitacion); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
        queryString += " and t.habilitacion.id = " + idHabilitacion;
        if(idEntHabAExcluir != null && idEntHabAExcluir > 0){
        	queryString += " and t.id <> " + idEntHabAExcluir;        	
        }
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    Long cantidad = (Long) query.uniqueResult();
		if(cantidad == null)
			return 0L;
	    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return cantidad;
	}

}
