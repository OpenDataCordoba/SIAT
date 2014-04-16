//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.RecConCuo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class RecConCuoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecConCuoDAO.class);	
	
	private static long migId = -1;
	
	public RecConCuoDAO() {
		super(RecConCuo.class);
	}

	public RecConCuo getByReciboConvenioYConvenioCuota(ReciboConvenio reciboConvenio, ConvenioCuota convenioCuota) {
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			Session session = SiatHibernateUtil.currentSession();
			
			String queryString = "from RecConCuo t ";
		    
			// Armamos filtros del HQL
			queryString += " where t.estado = "+ Estado.ACTIVO.getId();
	 		
	        queryString += " and t.reciboConvenio.id = " + reciboConvenio.getId();
	        
	        queryString += " and t.convenioCuota.id = " + convenioCuota.getId();
	        	    
		     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		    
		    Query query = session.createQuery(queryString);
		    RecConCuo recConCuo = (RecConCuo) query.uniqueResult();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return recConCuo; 
	}
	
	public RecConCuo getConDeuCuoByCuotaEnCuoSaldoPaga(ConvenioCuota convenioCuota) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecConCuo t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.reciboConvenio.esCuotaSaldo = 1";
        
        queryString += " and t.convenioCuota.id = " + convenioCuota.getId();
        
        queryString += " and t.reciboConvenio.fechaPago is not null";
        	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    RecConCuo recConCuo = (RecConCuo) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return recConCuo; 
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
	 *  Inserta una linea con los datos del RecConCuo para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param recConCuo, output - El RecConCuo a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(RecConCuo o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idRecConCuo.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		 
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		//id|idreciboconvenio|idconveniocuota|capitaloriginal|descapitaloriginal|totcapitaloriginal|interesfinanciero|desintfin|totintfin|actualizacion|desactualizacion|totactualizacion|importeSellado|usuario|fechaultmdf|estado
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getReciboConvenio().getId());
		line.append("|");
		line.append(o.getConvenioCuota().getId());		 
		line.append("|");
		line.append(decimalFormat.format(o.getCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getDesCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getInteresFinanciero()));
		line.append("|");
		line.append(decimalFormat.format(o.getDesIntFin()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotIntFin()));
		line.append("|");
		line.append(decimalFormat.format(o.getActualizacion()));
		line.append("|");
		line.append(decimalFormat.format(o.getDesActualizacion()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotActualizacion()));
		line.append("|");
		line.append(decimalFormat.format(o.getImporteSellado()));
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
		RecConCuoDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}
}
	
