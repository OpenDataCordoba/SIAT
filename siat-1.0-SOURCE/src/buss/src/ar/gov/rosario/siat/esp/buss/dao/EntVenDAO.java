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
import ar.gov.rosario.siat.esp.buss.bean.EntVen;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class EntVenDAO extends GenericDAO {

	private Log log = LogFactory.getLog(EntVenDAO.class);	

	private static long migId = -1;
	
	public EntVenDAO(){
		super(EntVen.class);
	}
	
	public List<EntVen> getListByHabilitacion(Long idHabilitacion) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from EntVen t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idHabilitacion: " + idHabilitacion); 
		}
	
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.habilitacion.id = " + idHabilitacion;

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<EntVen> listEntVen = (ArrayList<EntVen>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listEntVen;
	}

	public static long getMigId() {
		return migId;
	}
	public static void setMigId(long migId) {
		EntVenDAO.migId = migId;
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

	public Long createForLoad(EntVen o, LogFile output) throws Exception {
		
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|idhabilitacion|identhab|totalvendidas|fechaemision|iddeuda|importe|esanulada|usuario|fechaultmdf|estado
		
		DecimalFormat decimalFormat = new DecimalFormat("#.000000000000");
		StringBuffer line = new StringBuffer();
		line.append(id);
		line.append("|");
		line.append(o.getHabilitacion().getId());
		line.append("|");
		line.append(o.getEntHab().getId());
		line.append("|");
		line.append(o.getTotalVendidas());
		line.append("|");
		line.append(DateUtil.formatDate(o.getFechaEmision(), "yyyy-MM-dd"));
		line.append("|"); //TODO: idDeuda ver si la podemos buscar
		line.append("|");
		line.append(decimalFormat.format(o.getImporte()));
		line.append("|");
		line.append(o.getEsAnulada());
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

	@SuppressWarnings("unchecked")
	public List<EntVen> getByIdDeuda(Long idDeuda) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = SiatHibernateUtil.currentSession();
		String queryString = "from EntVen t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.idDeuda = :idDeuda ";
 		
 		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString)
	    					 .setLong("idDeuda", idDeuda);
	    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return (ArrayList<EntVen>) query.list();
	}
	
}
