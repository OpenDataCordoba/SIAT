//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.dao;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cyq.buss.bean.ProDet;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ProDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProDetDAO.class);
	
	private static long migId = -1;
	
	public ProDetDAO() {
		super(ProDet.class);
	}
	
	
	/**
	 *  Devuelve una lista de Procedimiento Detalle, para el procedimiento y via deuda recibidos.
	 *  
	 *  Devuelve ordenado por cuenta, anio y periodo.
	 *  
	 * @param procedimiento
	 * @return
	 */	 
	 public List<ProDet> getListByProcedimientoViaDeuda(Procedimiento procedimiento, ViaDeuda viaDeuda){
		 String funcName = DemodaUtil.currentMethodName();
		 if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
		 Session session = SiatHibernateUtil.currentSession();
		 
		String sQuery = "FROM ProDet d " +
						"WHERE d.procedimiento = :procedimiento " +
						" AND d.viaDeuda = :viaDeuda " +
						"ORDER BY d.cuenta, d.procurador, d.anio, d.periodo";
		
		log.debug(funcName + ": Query: " + sQuery);
		
		Query query = session.createQuery(sQuery)
							 .setEntity("procedimiento", procedimiento )
							 .setEntity("viaDeuda", viaDeuda );

		return query.list();	
		 
	 }
	 
	 
	 /**
	  * Obtiene un detalle de procedimiento buscando por id de deuda y id procedimiento.
	  * 
	  * 
	  * @param idDeuda
	  * @return ProDet
	  */
	 public ProDet getByIdProcedimientoYDeuda(Long idProcedimiento, Long idDeuda){
		 
	 	ProDet proDet;
		
	    String queryString = "from ProDet t where t.procedimiento.id = " + idProcedimiento + " and t.idDeuda = " + idDeuda;
	    
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);

		try{
			proDet = (ProDet) query.uniqueResult();	
		} catch (Exception e) {
			// Si no encuentra el proDet, o encuentra mas de uno
			return null;				
		} 

		return proDet; 
	 
	 }
	 
	 /**
	  * 
	  * Devuelve un Distinct de cuentas para un procedimiento y recurso.
	  * 
	  * @param idProcedimiento
	  * @param idRecurso
	  * @return
	  */
	 public List<Cuenta> getListCuentaByIdRecurso(Long idProcedimiento, Long idRecurso) {

	    String queryString = "SELECT DISTINCT(t.cuenta) FROM ProDet t WHERE " +
	    					 "t.procedimiento.id = " + idProcedimiento +
	    					 " AND t.recurso.id = " + idRecurso;
	    
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
	 
		return query.list();
		 
	 }
	 
	 
	public static void setMigId(long migId) {
		ProDetDAO.migId = migId;
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
		if(migId==-1){
			migId = this.getLastId(path, nameFile)+1;
		}else{
			migId++;
		}

		return migId;
	}
	
	
	/**
	 *  Inserta una linea con los datos del ProDet para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param proDet, output - El ProDet a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(ProDet o, LogFile output) throws Exception {

		// Obtenemos el valor del id autogenerado a insertar.
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		//id|idprocedimiento|iddeuda|idcuenta|idviadeuda|idestadodeuda|idconvenio|importe|saldo|actualizacioncyq|codrefpag|idreccladeu|
		//anio|periodo|fechaemision|fechavencimiento|importebruto|actualizacion|strconceptosprop|strestadodeuda|fechapago|estaimpresa|
		//idprocurador|idrecurso|obsmotnopre|reclamada|idsistema|resto|usuario|fechaultmdf|estado|
		
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getProcedimiento().getId());
		line.append("|");
		line.append(o.getIdDeuda());
		line.append("|");
		line.append(o.getCuenta().getId());
		line.append("|");
		line.append(o.getViaDeuda().getId());
		line.append("|");
		line.append(o.getEstadoDeuda().getId());
		line.append("|");
		if (o.getConvenio()!= null)
			line.append(o.getConvenio().getId());
		line.append("|");
		if(o.getImporte()!=null)
			line.append(decimalFormat.format(o.getImporte()));
		line.append("|");
		if(o.getSaldo()!=null)
			line.append(decimalFormat.format(o.getSaldo()));
		line.append("|");
		if(o.getActualizacionCyq()!=null)
			line.append(decimalFormat.format(o.getActualizacionCyq()));
		line.append("|");
		line.append(o.getCodRefPag());
		line.append("|");
		line.append(o.getRecClaDeu().getId());
		line.append("|");
		line.append(o.getAnio());
		line.append("|");
		line.append(o.getPeriodo());
		line.append("|");
		if (o.getFechaEmision()!= null){
			line.append(DateUtil.formatDate(o.getFechaEmision(), "yyyy-MM-dd"));
		}
		line.append("|");
		if (o.getFechaVencimiento()!= null){
			line.append(DateUtil.formatDate(o.getFechaVencimiento(), "yyyy-MM-dd"));
		}		
		line.append("|");
		if(o.getImporteBruto()!=null)
			line.append(decimalFormat.format(o.getImporteBruto()));
		line.append("|");
		if(o.getActualizacion()!=null)
			line.append(decimalFormat.format(o.getActualizacion()));
		line.append("|");
		line.append(o.getStrConceptosProp());
		line.append("|");
		if (o.getStrEstadoDeuda() != null)
			line.append(o.getStrEstadoDeuda());
		line.append("|");
		if (o.getFechaPago()!= null){
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));
		}
		line.append("|");
		line.append(o.getEstaImpresa());
		line.append("|");
		if (o.getProcurador()!= null)
			line.append(o.getProcurador().getId());
		line.append("|");
		line.append(o.getRecurso().getId());
		line.append("|");
		if (o.getObsMotNoPre() != null)
			line.append(o.getObsMotNoPre());
		line.append("|");
		line.append(o.getReclamada());
		line.append("|");
		if (o.getSistema()!= null)
			line.append(o.getSistema().getId());
		line.append("|");
		if(o.getResto()!=null)
			line.append(decimalFormat.format(o.getResto()));
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
