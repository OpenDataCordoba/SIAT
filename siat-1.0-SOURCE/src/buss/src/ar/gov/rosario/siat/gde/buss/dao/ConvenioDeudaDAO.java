//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;

public class ConvenioDeudaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConvenioDeudaDAO.class);	
	
	private static long migId = -1;
	
	public ConvenioDeudaDAO() {
		super(ConvenioDeuda.class);
	}
	
	@Deprecated
	public List<ConvenioDeuda> getListByIdConvenio(Long idConvenio){
		
		 String queryString = "from ConvenioDeuda t where t.convenio.id = " + idConvenio;

		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		return  (ArrayList<ConvenioDeuda>) query.list();
	}
	
	/**
	 *  Obtiene el registro de ConvenioDeuda con la Deuda a Cancelar. Buscando los registros para el convenio
	 *  pasado y con SaldoEnPlan mayor que cero ordenados por fecha de vencimiento de la deuda. Luego elije el que
	 *  contiene la deuda mas antigua.
	 * 
	 * @param convenio
	 * @return convenioDeuda
	 * @throws Exception
	 */
	public ConvenioDeuda getDeudaACancelarByConvenioYAse(Convenio convenio, Asentamiento asentamiento) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from ConvenioDeuda t ";
	    
		// Armamos filtros del HQL		
		queryString += " where t.convenio.id = " + convenio.getId();
		queryString += " and t.saldoEnPlan > 0";
		queryString += " and t.id NOT IN (select a.convenioDeuda.id from AuxConDeu a where a.asentamiento.id = "+asentamiento.getId()+" )";
		
		queryString += " order by t.fecVenDeuda";
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<ConvenioDeuda> listConvenioDeuda = (ArrayList<ConvenioDeuda>) query.list();
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	    
	    if(!ListUtil.isNullOrEmpty(listConvenioDeuda)){
	    	return listConvenioDeuda.get(0);
	    }else
	    	return null;
	}
	
	public ConvenioDeuda getDeudaMasAntiguaACancelar (Convenio convenio){
		Session session = SiatHibernateUtil.currentSession();
		ConvenioDeuda convenioDeuda;
		
		String queryString = " FROM ConvenioDeuda t WHERE t.convenio.id = "+ convenio.getId();
		queryString += " AND t.saldoEnPlan > 0";
		queryString += " ORDER BY t.fecVenDeuda ASC";
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		convenioDeuda = (ConvenioDeuda)query.uniqueResult();
		
		return convenioDeuda;
		
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
	 * Devuelve los registros de ConvenioDeuda en el que el saldo de la deuda original es mayor a 0
	 * Se utiliza para aplicar pagos a cuenta
	 * @param convenio
	 * @return
	 * @throws Exception
	 */
	
	public List<ConvenioDeuda> getListConDeuConSaldo(Convenio convenio) throws Exception {
		List<ConvenioDeuda> listConvenioDeudaConSaldo;
		String queryString ="from ConvenioDeuda t where t.convenio.id = "+convenio.getId();
	
		queryString += " and t.saldoEnPlan >= "+0.1D; 
		queryString += " order by t.fecVenDeuda";
 
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		listConvenioDeudaConSaldo = (ArrayList<ConvenioDeuda>) query.list();	

		return listConvenioDeudaConSaldo; 
	}

	/**
	 *  Inserta una linea con los datos del ConvenioDeuda para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param convenioDeuda, output - El ConvenioDeuda a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(ConvenioDeuda o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idConvenioDeuda.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }

		 //long id = getNextId(output.getPath(), output.getNameFile());
		 
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|idconvenio|iddeuda|capitaloriginal|capitalenplan|actualizacion|actenplan|totalenplan|fechapago|saldoenplan|fecvendeuda|usuario|fechaultmdf|estado 
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getConvenio().getId());
		line.append("|");
		line.append(o.getIdDeuda());		 
		line.append("|");
		line.append(decimalFormat.format(o.getCapitalOriginal()));
		line.append("|");
		line.append(decimalFormat.format(o.getCapitalEnPlan()));
		line.append("|");
		line.append(decimalFormat.format(o.getActualizacion()));
		line.append("|");
		line.append(decimalFormat.format(o.getActEnPlan()));
		line.append("|");
		line.append(decimalFormat.format(o.getTotalEnPlan()));
		line.append("|");
		if(o.getFechaPago()!=null){
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(decimalFormat.format(o.getSaldoEnPlan()));
		line.append("|");
		if(o.getFecVenDeuda()!=null){
			line.append(DateUtil.formatDate(o.getFecVenDeuda(), "yyyy-MM-dd"));		 
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

	/**
	 * Devuelve los registros de ConvenioDeuda en para todos los Convenios Vigentes.
	 * 
	 * @return
	 * @throws Exception
	 */
	
	public List<ConvenioDeuda> getListParaConveniosVigentes() throws Exception {
		List<ConvenioDeuda> listConvenioDeuda;
		String queryString ="from ConvenioDeuda t where t.convenio.estadoConvenio.id = "+EstadoConvenio.ID_VIGENTE;
	
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		listConvenioDeuda = (ArrayList<ConvenioDeuda>) query.list();	

		return listConvenioDeuda; 
	}
	
	/**
	 *  Obtiene la lista de ConvenioDeudas para el Convenio indicado y que tengan saldo > 0.
	 *  (Se ordena de forma descendente por fecha de vencimieto para usarla en una lista LIFO)
	 *  
	 * @param convenio
	 * @return listConvenioDeuda
	 * @throws Exception
	 */
	public List<ConvenioDeuda> getListWithSaldo(Convenio convenio) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from ConvenioDeuda t ";
	    
		// Armamos filtros del HQL		
		queryString += " where t.convenio.id = " + convenio.getId();
		queryString += " and t.saldoEnPlan > 0";
		// Se ordena de forma descendente por fecha de vencimieto para usarla en una lista LIFO
		queryString += " order by t.fecVenDeuda desc";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<ConvenioDeuda> listConvenioDeuda = (ArrayList<ConvenioDeuda>) query.list();
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	    
    	return listConvenioDeuda;
	}
	
	
	
	/**
	 *  
	 *	Verifica que la deuda incluida en el convenio se encuentre en DeudaAdmin o DeudaJudicial (segun la via del convenio)
	 *  y con el estado de deuda indicado.  
	 * 
	 * @param convenio
	 * @param idEstadoDeuda
	 * @return boolean
	 * @throws Exception
	 */
	public boolean esConvenioConsistente(Convenio convenio) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String sqlQueryString = null;
		if(convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN){
			sqlQueryString = "select * from gde_convenioDeuda cd left join gde_deudaAdmin da on (da.id = cd.idDeuda)";
			sqlQueryString += " where cd.idConvenio = " + convenio.getId() +" and cd.saldoEnPlan > 0";
			sqlQueryString += " and ((da.idConvenio is null or da.idConvenio <> " + convenio.getId();
			sqlQueryString += ") or (da.idEstadoDeuda is null or da.idEstadoDeuda <> " + EstadoDeuda.ID_ADMINISTRATIVA +"))";
		}
		if(convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
			sqlQueryString = "select * from gde_convenioDeuda cd left join gde_deudaJudicial da on (da.id = cd.idDeuda)";
			sqlQueryString += " where cd.idConvenio = " + convenio.getId() + " and cd.saldoEnPlan > 0";
			sqlQueryString += " and ((da.idConvenio is null or da.idConvenio <> " + convenio.getId();
			sqlQueryString += ") or (da.idEstadoDeuda is null or da.idEstadoDeuda <> " + EstadoDeuda.ID_JUDICIAL +"))";
		}
		if(convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_CYQ){
			// TODO Ver que validacion de consistencia debemos realizar para CyQ. (la deuda puede estar en estado Admin? Judicial? ambos?)
			return true;
		}
		if(sqlQueryString == null)
			return false;
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + sqlQueryString);
	    
	    Query query = session.createSQLQuery(sqlQueryString);
	    query.setMaxResults(1);
	    Object result = (Object) query.uniqueResult();
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	    
	    if(result != null)
	    	return false;
	    else
	    	return true;
	}
	
	/**
	 * Verifica que para todos los convenio deudas que aplicaron pagos a cuenta
	 * exista al menos un ConDeuCuo grabado 
	 * @param convenio
	 * @return
	 */
	public boolean faltanConDeuCuo(Convenio convenio){
		
		Session session = SiatHibernateUtil.currentSession();
		List<ConvenioDeuda> convenioDeudas;
		String sqlString= "SELECT * FROM gde_convenioDeuda cd WHERE (cd.capitalEnPlan + cd.actEnPlan) > cd.saldoEnPlan";
		sqlString += " AND cd.idConvenio = "+convenio.getId();
		sqlString += " AND cd.fechaPago is null AND cd.id NOT IN (SELECT cdc.idConvenioDeuda FROM gde_conDeuCuo cdc, gde_convenioDeuda cdd";
		sqlString += " WHERE cdc.fechaPago is not null AND cdc.idconvenioDeuda = cdd.id and cdd.idConvenio = "+convenio.getId() + ")";
		
		log.debug("queryString: "+sqlString);
		Query query = session.createSQLQuery(sqlString);
		convenioDeudas = (ArrayList<ConvenioDeuda>) query.list();
		log.debug("Lista de ConvenioDeudas sin condeucuo del convenio "+convenio.getNroConvenio()+" : "+convenioDeudas.size());
		
		return convenioDeudas.size()>0;
	}

	public static void setMigId(long migId) {
		ConvenioDeudaDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}
}

