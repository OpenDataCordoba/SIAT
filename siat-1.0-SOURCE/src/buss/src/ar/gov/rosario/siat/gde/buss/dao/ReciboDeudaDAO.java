//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboDeuda;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ReciboDeudaDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(ReciboDeudaDAO.class);	
	
	private static long migId = -1;
	
	public ReciboDeudaDAO(){
		super(ReciboDeuda.class);
	}
	
	public ReciboDeuda getByReciboYDeuda(Recibo recibo, Deuda deuda){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM ReciboDeuda t ";
			   queryString += " WHERE t.estado = "+ Estado.ACTIVO.getId();
			   queryString += " AND t.recibo.id = " + recibo.getId();
			   queryString += " AND t.idDeuda = " + deuda.getId();
        	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    ReciboDeuda reciboDeuda = (ReciboDeuda) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return reciboDeuda; 
	}

	public List<DeuAdmRecCon> getListDeuAdmRecCon(Long idDeuda) {
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM DeuAdmRecCon t WHERE t.deuda.id ="+idDeuda;
		
		Query query = session.createQuery(queryString);
		return (ArrayList<DeuAdmRecCon>)query.list();		
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
	 *  Inserta una linea con los datos del ReciboDeuda para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param reciboDeuda, output - El ReciboDeuda a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(ReciboDeuda o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idReciboDeuda.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		 
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|idrecibo|iddeuda|capitalOriginal|desCapitalOriginal|actualizacion|desActualizacion|totcapital|totactualizacion|usuario|fechaultmdf|estado

		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getRecibo().getId());
		line.append("|");
		line.append(o.getIdDeuda());		 
		line.append("|");
		line.append(decimalFormat.format(o.getCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getDesCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getActualizacion()));
		line.append("|");
		line.append(decimalFormat.format(o.getDesActualizacion()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotCapital()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotActualizacion()));
		line.append("|");
		line.append(DemodaUtil.currentUserContext().getUserName());
		line.append("|");

		line.append("2010-01-01 00:00:00");

		line.append("|");
		line.append("1");
		line.append("|");

		if (o.getRectificativa() != null) {
			line.append(o.getRectificativa());
		}
		line.append("|");

		
		output.addline(line.toString());

		// Seteamos el id generado en el bean.
		o.setId(id);

		return id;
	}

	/**
	 * Obtiene los reciboDeuda en la via pasada como parametro dentro del periodo ingresado, para los procuradores pasados como parametro
	 * @param listProcurador si es null no se tiene en cuenta
	 * @param fechaPagoDesde
	 * @param fechaPagoHasta
	 * @param noLiquidadas si es TRUE, filtra por las no liquidadas
	 * @return
	 * @throws Exception 
	 */
	public List<ReciboDeuda> get(List<Procurador> listProcurador, Long idViaDeuda, Date fechaPagoDesde, Date fechaPagoHasta, boolean noLiquidados) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from ReciboDeuda t ";
		boolean flagAnd =false;
		
/*		// filtro lista de procuradores
		if(listProcurador!=null && !listProcurador.isEmpty()){
 			String listIdProcurador = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listProcurador, 0, false));
 			queryString += " t.deuda.procurador.id IN ("+ listIdProcurador + ") "; 
			flagAnd = true;
		}*/

		// filtro por viaDeuda
		if(idViaDeuda!=null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " t.recibo.viaDeuda.id="+idViaDeuda;
			flagAnd = true;
		}
		
		// filtro por fecha Pago Desde
		if(fechaPagoDesde!=null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " t.recibo.fechaPago >= TO_DATE('" + 
			DateUtil.formatDate(fechaPagoDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
		
		// filtro por fecha Pago Hasta
		if(fechaPagoDesde!=null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " t.recibo.fechaPago <= TO_DATE('" + 
			DateUtil.formatDate(fechaPagoHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}			
		
		// filtro por no liquidados
		if(noLiquidados){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " t.recibo.idLiqComPro IS NULL";
			flagAnd = true;			
		}
		
	    Query query = session.createQuery(queryString);
	    List<ReciboDeuda> listReciboDeuda = (ArrayList<ReciboDeuda>)query.list();
	    
	    // Filtra por listProcurador
	    List<ReciboDeuda> listResult = new ArrayList<ReciboDeuda>();
	    for(ReciboDeuda reciboDeuda: listReciboDeuda){
	    	Long idProcurador = reciboDeuda.getDeuda().getProcurador().getId();
	    	for(Procurador p: listProcurador){
	    		if (p.getId().equals(idProcurador)) {
	    			listResult.add(reciboDeuda);
	    			break;
	    		}				
			}
	    }
	    return listResult;	    
	}

	public static void setMigId(long migId) {
		ReciboDeudaDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}
}
