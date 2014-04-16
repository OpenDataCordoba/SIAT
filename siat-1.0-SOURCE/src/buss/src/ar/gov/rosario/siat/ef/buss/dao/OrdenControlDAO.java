//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.buss.bean.TipoOrden;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class OrdenControlDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OrdenControlDAO.class);
	
	private static String SEQUENCE_NRO_ORDEN_CONTROL ="ef_ordenControl_sq";

	private static long migId = -1;
	
	public OrdenControlDAO() {
		super(OrdenControl.class);
	}
	
	public List<OrdenControl> getBySearchPage(OrdenControlSearchPage ordenControlSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from OrdenControl t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OrdenControlSearchPage: " + ordenControlSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (ordenControlSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
				
		// filtro ordenControl excluidos
 		List<OrdenControlVO> listOrdenControlExcluidos = (List<OrdenControlVO>) ordenControlSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listOrdenControlExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listOrdenControlExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
 		// Origen
 		if (!ModelUtil.isNullOrEmpty(ordenControlSearchPage.getOrdenControl().getOrigenOrden())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.origenOrden.id = "+ordenControlSearchPage.getOrdenControl().getOrigenOrden().getId();
			flagAnd = true;
		}

 		// estadoOrden
 		if (!ModelUtil.isNullOrEmpty(ordenControlSearchPage.getOrdenControl().getEstadoOrden())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoOrden.id = "+ordenControlSearchPage.getOrdenControl().getEstadoOrden().getId();
			flagAnd = true;
		}
 		
 		// TipoOrden
 		if (!ModelUtil.isNullOrEmpty(ordenControlSearchPage.getOrdenControl().getTipoOrden())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoOrden.id = "+ordenControlSearchPage.getOrdenControl().getTipoOrden().getId();
			flagAnd = true;
		}
 		
 		if (ordenControlSearchPage.getOrdenControl().getNumeroOrden()!=null) {
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.numeroOrden = "+ordenControlSearchPage.getOrdenControl().getNumeroOrden();
			flagAnd = true;
 		}
 		
 		if (ordenControlSearchPage.getOrdenControl().getAnioOrden()!=null) {
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.anioOrden = "+ordenControlSearchPage.getOrdenControl().getAnioOrden();
			flagAnd = true;
 		}
 		
 		if (!ModelUtil.isNullOrEmpty(ordenControlSearchPage.getOrdenControl().getContribuyente().getPersona())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.contribuyente.id = "+ordenControlSearchPage.getOrdenControl().getContribuyente().getPersona().getId();
			flagAnd = true;
 		}
 		
 		// Order By
		queryString += " order by t.id DESC ";

		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OrdenControl> listOrdenControl = (ArrayList<OrdenControl>) executeCountedSearch(queryString, ordenControlSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOrdenControl;
	}

	/**
	 * Obtiene un OrdenControl por su codigo
	 */
	public OrdenControl getByCodigo(String codigo) throws Exception {
		OrdenControl ordenControl;
		String queryString = "from OrdenControl t where t.codOrdenControl = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		ordenControl = (OrdenControl) query.uniqueResult();	

		return ordenControl; 
	}
	
	public List<OrdenControl> getListOrdenControl(UserContext userContext) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from OrdenControl t ";
	    boolean flagAnd = false;

		
	
		// Armamos filtros del HQL
		
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		
		
		
 		// Inspector
 		if (userContext.getIdInspector()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.inspector.id = "+ userContext.getIdInspector();
			flagAnd = true;
		}

 		
 		// Order By
		queryString += " order by t.anioOrden, t.numeroOrden";

		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OrdenControl> listOrdenControl = (ArrayList<OrdenControl>) executeSearch(queryString);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOrdenControl;
	}


	public Long getNextNroOrden() {
		return super.getNextVal(SEQUENCE_NRO_ORDEN_CONTROL);
	}
	
	/**
	 * devuelve las cantidad de ordenes de estado distinto a cerrado para un inspector ordenadas por grupos
	 */
	public  String getOrdenAbiertaPorGrupo(Inspector inspector) throws Exception {
		String rta="";
		if(inspector!=null){    
			String queryString = "select t.tipoOrden.id, count(*) from OrdenControl t where t.inspector.id ="+inspector.getId();
			queryString+=" group by t.tipoOrden.id ";
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString);

			List<Object[]> listResult = (ArrayList<Object[]>) query.list();

			for(Object[] object :listResult){
				Long idTipoOrden = (Long) object[0];
				Long cant = (Long) object[1];
				TipoOrden tipoOrden =TipoOrden.getById(idTipoOrden);

				rta+= cant.toString()+" "+tipoOrden.getDesTipoOrden()+", ";
			}
		}
		return rta;
	}

	public static OrdenControl getByNumeroAnio(Integer numero, Integer anio){
		OrdenControl ordenControl;
		String queryString = "from OrdenControl t where t.numeroOrden ="+numero+" and t.anioOrden="+anio;
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		ordenControl = (OrdenControl) query.uniqueResult();	

		return ordenControl; 
	}

	public static OrdenControl getByCaso(String idcaso){
		OrdenControl ordenControl;
		String queryString = "from OrdenControl t where t.idCaso ='"+idcaso+"'";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		ordenControl = (OrdenControl) query.uniqueResult();	

		return ordenControl; 

	}
	
	public static OrdenControl getByNumeroAnioCaso(Integer numero, Integer anio, String idcaso){
		OrdenControl ordenControl;
		String queryString = "from OrdenControl t";
		
		boolean flagAnd=false;
		if (numero!=null && anio != null){
			queryString +=" where t.numeroOrden ="+numero+" and t.anioOrden="+anio;
			flagAnd=true;
		}
		
		if(!StringUtil.isNullOrEmpty(idcaso)){
			queryString += (flagAnd)?" and ":" where ";
			queryString +="t.idCaso='"+idcaso +"'";
		}
		
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		ordenControl = (OrdenControl) query.uniqueResult();	

		return ordenControl; 
	}

	public static void setMigId(long migId) {
		OrdenControlDAO.migId = migId;
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
	
	/**
	 *  Inserta una linea con los datos del OrdenControl para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param OrdenControl, output - El OrdenControl a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(OrdenControl o, LogFile output) throws Exception {

		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		// id|numeroorden|anioorden|idContribuyente|idtipoorden|idestadoorden|idorigenorden|observacion|fechaemision|usuario|fechaultmdf|estado|
		
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getNumeroOrden());
		line.append("|");
		line.append(o.getAnioOrden());		 
		line.append("|");
		line.append(o.getContribuyente().getId());
		line.append("|");
		line.append(o.getTipoOrden().getId());
		line.append("|");
		line.append(o.getEstadoOrden().getId());
		line.append("|");
		line.append(o.getOrigenOrden().getId());
		line.append("|");
		line.append(o.getObservacion());
		line.append("|");
		line.append(DateUtil.formatDate(o.getFechaEmision(), "yyyy-MM-dd"));
		line.append("|");

		line.append(DemodaUtil.currentUserContext().getUserName());
		line.append("|");
		line.append("2010-01-01 00:00:00");
		line.append("|");
		line.append("1");
		line.append("|");

		if (o.getIdCaso() != null) {
			line.append(o.getIdCaso());
		}
		line.append("|");			
		output.addline(line.toString());

		// Seteamos el id generado en el bean.
		o.setId(id);

		return id;
	}
	
}

