//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorSearchPage;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ProcuradorDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProcuradorDAO.class);	
	
	private static long migId = -1;
	
	public ProcuradorDAO() {
		super(Procurador.class);
	}
	
	public List<Procurador> getBySearchPage(ProcuradorSearchPage procuradorSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Procurador t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ProcuradorSearchPage: " + procuradorSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (procuradorSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(procuradorSearchPage.getProcurador().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
				StringUtil.escaparUpper(procuradorSearchPage.getProcurador().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
		// filtro por domicilio
 		if (!StringUtil.isNullOrEmpty(procuradorSearchPage.getProcurador().getDomicilio())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.domicilio)) like '%" + 
				StringUtil.escaparUpper(procuradorSearchPage.getProcurador().getDomicilio()) + "%'";
			flagAnd = true;
		}
 		
		// filtro por tipoProcurador
 		if(!ModelUtil.isNullOrEmpty(procuradorSearchPage.getProcurador().getTipoProcurador())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoProcurador = " +  procuradorSearchPage.getProcurador().getTipoProcurador().getId();
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.id ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Procurador> listProcurador = (ArrayList<Procurador>) executeCountedSearch(queryString, procuradorSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listProcurador;
	}

	/**
	 * Obtiene un Procurador por su codigo
	 */
	public Procurador getByCodigo(String codigo) throws Exception {
		Procurador procurador;
		String queryString = "from Procurador t where t.codProcurador = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		procurador = (Procurador) query.uniqueResult();	

		return procurador; 
	}

	/**
	 * Obtiene la lista de Procuradores activos e inactivos sin tener en cuenta la vigencia de la fecha
	 * @param  recurso
	 * @return List<Procurador>
	 */
	public List<Procurador> getListByRecurso(Recurso recurso){

		String queryString = "SELECT DISTINCT pr.procurador " +
				"FROM ProRec pr WHERE " +
				"pr.recurso = :recurso ";
				
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).
			setEntity("recurso", recurso);
		
		return (ArrayList<Procurador>) query.list();	
	}

	
	/**
	 * Obtiene la lista de Procuradores activos aplicando los filtros: recurso, fecha
	 * @param  recurso
	 * @param  fecha
	 * @return List<Procurador>
	 */
	public List<Procurador> getListActivosByRecursoFecha(Recurso recurso, Date fecha){

		String queryString = "SELECT DISTINCT pr.procurador " +
				"FROM ProRec pr WHERE " +
				"pr.recurso = :recurso AND " +
				"pr.fechaDesde <= :fecha AND " +
				"(pr.fechaHasta IS NULL OR pr.fechaHasta > :fecha ) AND " +             // pr.fechaHasta es nuleable
				"pr.procurador.estado = :estActivo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).
			setEntity("recurso", recurso).
			setDate("fecha", fecha).
			setInteger("estActivo", Estado.ACTIVO.getId());
		
		return (ArrayList<Procurador>) query.list();	
	}
	
	/**
	 * Obtiene la lista de Procuradores aplicando los filtros: recurso, fecha
	 * @param  recurso
	 * @param  fecha
	 * @return List<Procurador>
	 */
	public List<Procurador> getListByRecursoFecha(Recurso recurso, Date fecha){

		String queryString = "SELECT DISTINCT pr.procurador " +
				"FROM ProRec pr WHERE " +
				"pr.recurso = :recurso AND " +
				"pr.fechaDesde <= :fecha AND " +
				"(pr.fechaHasta IS NULL OR pr.fechaHasta > :fecha )";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).
			setEntity("recurso", recurso).
			setDate("fecha", fecha);
		
		return (ArrayList<Procurador>) query.list();	
	}
	
	/**
	 * Obtiene la lista de Procuradores activos aplicando los filtros: recurso, fecha y descripcion
	 * @param  recurso
	 * @param  fecha
	 * @param  desc
	 * @return List<Procurador>
	 */
	public List<Procurador> getListActivosByRecursoFechaDescripcion(Recurso recurso, Date fecha, String desc){

		log.debug("recurso.id: " + recurso.getId());
		log.debug("fecha: " + fecha);
		log.debug("desc: " + desc);
		
		String queryString = "SELECT DISTINCT prdh.proRec.procurador " +
				"FROM ProRecDesHas prdh WHERE " +
				"prdh.proRec.recurso = :recurso AND " +
				"prdh.fechaDesde <= :fecha AND " +
				"(prdh.fechaHasta IS NULL OR prdh.fechaHasta > :fecha ) AND " +        
				"prdh.desde <= :desc AND " +
				"prdh.hasta >= :desc AND " +                        
				"prdh.proRec.fechaDesde <= :fecha AND " +
				"(prdh.proRec.fechaHasta IS NULL OR prdh.proRec.fechaHasta > :fecha)  AND " +
				"prdh.proRec.procurador.estado = :estActivo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).
			setEntity("recurso", recurso).
			setDate("fecha", fecha).
			setString("desc", desc).
			setInteger("estActivo", Estado.ACTIVO.getId());
		
		return (ArrayList<Procurador>) query.list();	
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
	  *  Inserta una linea con los datos de la Procurador para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param procurador, output - Exencion a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(Procurador o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 //long id = getNextId(output.getPath(), output.getNameFile());
		 long id = o.getId();
		 // Estrucura de la linea:
		 // id|descripcion|domicilio|telefono|horarioatencion|idtipoprocurador|observacion|usuario|fechaultmdf|estado 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getDescripcion());		 
		 line.append("|");
		 if(o.getDomicilio() != null)
			 line.append(o.getDomicilio());		 
		 line.append("|");
		 if(o.getTelefono() != null)
			 line.append(o.getTelefono());		 
		 line.append("|");
		 if(o.getHorarioAtencion() != null)
			 line.append(o.getHorarioAtencion());		 
		 line.append("|");
		 line.append(o.getTipoProcurador().getId());
		 line.append("|");
		 if(o.getObservacion() != null)
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
		 * Obtiene la lista de Procuradores activos aplicando los filtros: lista de recurso, fecha
		 * @param  listRecurso
		 * @param  fecha
		 * @return List<Procurador>
		 */
		public List<Procurador> getListActivosByListRecursoFecha(List<Recurso> listRecurso, Date fecha) throws Exception {

			String listIdRecurso = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listRecurso, 0, false));
			String queryString = "SELECT DISTINCT pr.procurador " +
					"FROM ProRec pr WHERE " +
					"pr.recurso  IN ("+listIdRecurso+") AND " +
					"pr.fechaDesde <= :fecha AND " +
					"(pr.fechaHasta IS NULL OR pr.fechaHasta > :fecha ) AND " +             // pr.fechaHasta es nuleable
					"pr.procurador.estado = :estActivo";
			Session session = SiatHibernateUtil.currentSession();

			Query query = session.createQuery(queryString).
				setDate("fecha", fecha).
				setInteger("estActivo", Estado.ACTIVO.getId());
			
			return (ArrayList<Procurador>) query.list();	
		}
}
