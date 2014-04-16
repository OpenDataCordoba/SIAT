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
import ar.gov.rosario.siat.gde.buss.bean.CtrlInfDeu;
import ar.gov.rosario.siat.gde.iface.model.TramiteAdapter;
import ar.gov.rosario.siat.gde.iface.model.TramiteSearchPage;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CtrlInfDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CtrlInfDeuDAO.class);	
	
	private static long migId = -1;
	
	public CtrlInfDeuDAO() {
		super(CtrlInfDeu.class);
	}
	
	
	public List<CtrlInfDeu> getBySearchPage(TramiteSearchPage tramiteSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Tramite t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TramiteSearchPage: " + tramiteSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tramiteSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro tramite excluidos
 		List<TramiteVO> listTramiteExcluidos = (List<TramiteVO>) tramiteSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listTramiteExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listTramiteExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(tramiteSearchPage.getTramite().getCodTramite())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codTramite)) like '%" + 
				StringUtil.escaparUpper(tramiteSearchPage.getTramite().getCodTramite()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tramiteSearchPage.getTramite().getDesTramite())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desTramite)) like '%" + 
				StringUtil.escaparUpper(tramiteSearchPage.getTramite().getDesTramite()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codTramite ";
		*/
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CtrlInfDeu> listTramite = (ArrayList<CtrlInfDeu>) executeCountedSearch(queryString, tramiteSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTramite;
	}
	
	/**
	 * Checkea el uso de un Recibo en Siat.
	 * 
	 * Devuelve el boolean correspondiente a la posibilidad de se utilizado
	 * 
	 * True si se puede, False si no se puede utilizar
	 * 
	 * 
	 * @author Cristian
	 * @param tramiteAdapter
	 * @return
	 */
	public boolean chkEsCtrlInfValido(TramiteAdapter tramiteAdapter){
		
		String queryString = "SELECT COUNT(t) FROM CtrlInfDeu t WHERE " +
							 " t.nroRecibo = :nroRecibo AND " +
							 " t.anioRecibo = :anioRecibo";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query;
		String sCodRefPag = tramiteAdapter.getTramite().getCodRefPag();
		
		if (sCodRefPag.indexOf("/") >= 0) {
			Long numero = 0L, anio = 0L;
			try { numero = Long.valueOf(sCodRefPag.split("/")[0]); } catch (Exception e) {}
			try { anio = Long.valueOf(sCodRefPag.split("/")[1]); } catch (Exception e) {}
			
			query = session.createQuery(queryString)
				.setLong("nroRecibo", numero)
				.setLong("anioRecibo", anio);
			log.debug("codId: " + tramiteAdapter.getTramite().getTipoTramite().getCodTipoTramite());
			log.debug("nroRecibo: " + numero);
			log.debug("anioRecibo: " + anio);
		} else {
			Long numero = 0L;
			try { numero = Long.valueOf(sCodRefPag); } catch (Exception e) {}
			query = session.createQuery(queryString)
				.setLong("nroRecibo", numero)
				.setLong("anioRecibo", 0);
			log.debug("codId: " + tramiteAdapter.getTramite().getTipoTramite().getCodTipoTramite());
			log.debug("nroRecibo: " + numero);
			log.debug("anioRecibo: " + 0);
		}

		Long cant = (Long) query.uniqueResult(); 
		log.debug("cant: " + cant);
	
		if (cant.longValue() >= 1)
			return false;
		else
			return true;		
	}
	
	
	 /**
	  *  Devuelve el proximo valor de id a asignar. 
	  *  Se inicializa obteniendo el ultimo id asignado en el archivo de migracion con datos pasados como parametro
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
	  *  Inserta una linea con los datos de la CtrlInfDeu para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param ctrlInfDeu, output - CtrlInfDeu a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(CtrlInfDeu o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|nrotramite|nrorecibo|aniorecibo|codid|idcuenta|fechahoragen|fechahoraimp|nroliquidacion|observacion|usuario|fechaultmdf|estado 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getNroTramite());		 
		 line.append("|");
		 line.append(o.getNroRecibo());		 
		 line.append("|");
		 line.append(o.getAnioRecibo());		 
		 line.append("|");
		 line.append(o.getCodId());		 
		 line.append("|");
		 line.append(o.getCuenta().getId());		 
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaHoraGen(), "yyyy-MM-dd HH:mm:SS"));
		 line.append("|");
		 if(o.getFechaHoraImp()!=null){
			line.append(DateUtil.formatDate(o.getFechaHoraImp(), "yyyy-MM-dd HH:mm:SS"));		 
		 } // Si es null no se inserta nada, viene el proximo pipe.
		 line.append("|");
		 line.append(o.getNroLiquidacion());		 
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

	 /**
	  *  Busca un registro de CtrlInfDeu (Tramite) para nroRecibo y anioRecibo indicados. Si se pasa el anio en cero el nroRecibo corresponde al codRefPag.
	  * 
	  * @param nroRecibo
	  * @param anioRecibo
	  * @return
	  * @throws Exception
	  */
	public CtrlInfDeu getByNroYAnio(Long nroRecibo, Long anioRecibo) throws Exception {
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
			String queryString = "from CtrlInfDeu t where ";
			queryString += " t.nroRecibo = "+nroRecibo;
			queryString += " and t.anioRecibo = "+anioRecibo;  
			
			Session session = SiatHibernateUtil.currentSession();
			
			Query query = session.createQuery(queryString);
			query.setMaxResults(1);
		
			CtrlInfDeu ctrlInfDeu =  (CtrlInfDeu) query.uniqueResult();	
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return ctrlInfDeu;
	}
		
}
