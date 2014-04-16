//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ConDeuDet;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ConDeuDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConDeuDetDAO.class);	
	
	private static long migId = -1;
	
	public ConDeuDetDAO() {
		super(ConDeuDet.class);
	}
	
	/**
	 * Obtiene la lista de ConDeuDet del Envio Judicial paginando.
	 * @param  procesoMasivo
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<ConDeuDet>
	 */
	public List<ConDeuDet> getListConDeuDetByProMas(ProcesoMasivo procesoMasivo, Integer firstResult, Integer maxResults) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "FROM ConDeuDet cdd " +
				"WHERE cdd.constanciaDeu.procesoMasivo = :procesoMasivo ";// +
				//"ORDER BY cdd.constanciaDeu.procurador.descripcion";
		
		Session session = SiatHibernateUtil.currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createQuery(queryString).setEntity("procesoMasivo", procesoMasivo);

		if (firstResult != null){
			query.setFirstResult(firstResult);
		}
		if (maxResults != null){
			query.setMaxResults(maxResults);
		}

		return (ArrayList<ConDeuDet>) query.list();
	}
	
	/**
	 * Obtiene el ConDeuDet en estado activo para la deuda pasado como parametro.
	 * @param  deuda
	 * @return conDeuDet
	 */
	public ConDeuDet getConDeuDetVigenteByDeuda(Deuda deuda) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "FROM ConDeuDet c " +
				"WHERE c.idDeuda = "+deuda.getId()+" AND c.estado = "+Estado.ACTIVO.getId();

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		ConDeuDet conDeuDet = (ConDeuDet) query.uniqueResult();

		return conDeuDet;
	}


	/**
	 * Obtiene la lista de ConDeuDet filtrando por deuda y estado
	 * @param idDeuda si es nulo o <=0 no se tiene en cuenta
	 * @param idEstadoActivoInactivo si es nulo o <=0 no se tiene en cuenta
	 * @return
	 */
	public List<ConDeuDet> getByDeudaYEstado(Long idDeuda,	Integer idEstadoActivoInactivo) {
		String funcName = DemodaUtil.currentMethodName();
		
		String queryString = "from ConDeuDet t ";
	    boolean flagAnd = false;
	
		// filtro por idDeuda
 		if (idDeuda!=null && idDeuda>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.idDeuda =" + idDeuda;
			flagAnd = true;
		}

		// filtro por estado
 		if (idEstadoActivoInactivo!=null && idEstadoActivoInactivo>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.estado =" + idEstadoActivoInactivo;
			flagAnd = true;
		}
 		
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    List<ConDeuDet> listResult = (ArrayList<ConDeuDet>) query.list();
		
		return listResult;
	}
	
	/**
	 * Devuelve true si No Existe Constancia para la deuda (por ser migrada) 
	 * o si pertenece a La deuda esta en una Constancia y la misma esta en estado habilitada
	 * 
	 * @author Cristian
	 * @param idDeuda
	 * @return boolean
	 */
	public boolean existeEnConstanciaHabilitada(Long idDeuda) {
		String funcName = DemodaUtil.currentMethodName();
		
		String queryString = "from ConDeuDet t where " +
							" t.idDeuda =" + idDeuda;
 		
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		List<ConDeuDet> listResult = (ArrayList<ConDeuDet>) query.list();
		
		log.debug(funcName + ": ConDeuDet encontrados :" + listResult.size());
		
		// No existe en constancia, es deuda migrada
		if (listResult.size() == 0){
			return true;
		
		} else {
			
			ConDeuDet conDeuDet = listResult.get(0);
			
			log.debug(funcName + ": ConDeuDet estado Constancia: " + conDeuDet.getConstanciaDeu().getEstConDeu().getDesEstConDeu());
			
			// La Constancia en estado Habilitada
			if (conDeuDet.getConstanciaDeu().getFechaHabilitacion() != null){
				return true;
			
			} else {
				// La Constancia NO esta en estado Habilitada
				return false;
			}
		}
	}

	/**
	 * Obtiene la lista de ConDeuDet asociadas a la deuda y al proceso masivo
	 * @param  procesoMasivo
	 * @param  idDeuda
	 * @return List<ConDeuDet>
	 */
	public List<ConDeuDet> getListConDeuDetByProMasIdDeuda(ProcesoMasivo procesoMasivo, Long idDeuda) {

		String queryString = "FROM ConDeuDet cdd " +
				"WHERE cdd.idDeuda = :idDeuda AND cdd.constanciaDeu.procesoMasivo = :procesoMasivo ";

		Session session = SiatHibernateUtil.currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createQuery(queryString)
			.setEntity("procesoMasivo", procesoMasivo)
			.setLong("idDeuda", idDeuda);

		return (ArrayList<ConDeuDet>) query.list();
	}

	public static long getMigId() {
		return migId;
	}

	public static void setMigId(long migId) {
		ConDeuDetDAO.migId = migId;
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
	
	public Long createForLoad(ConDeuDet o, LogFile output) throws Exception {
		
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		//id|idconstanciadeu|iddeuda|observacion|usuario|fechaultmdf|estado
		
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getConstanciaDeu().getId());
		line.append("|");
		line.append(o.getIdDeuda());
		line.append("|");
		line.append(o.getObservacion());
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
