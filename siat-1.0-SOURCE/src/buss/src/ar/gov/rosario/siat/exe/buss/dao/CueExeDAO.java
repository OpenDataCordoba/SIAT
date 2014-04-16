//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.EstadoCueExe;
import ar.gov.rosario.siat.exe.iface.model.CueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.CueExeVO;
import ar.gov.rosario.siat.exe.iface.model.MarcaCueExeSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CueExeDAO extends GenericDAO {

	private Log log = LogFactory.getLog(CueExeDAO.class);	

	private static long migId = -1;
	
	public CueExeDAO() {
		super(CueExe.class);
	}
	
	@SuppressWarnings({"unchecked"})
	public List<CueExe> getBySearchPage(CueExeSearchPage cueExeSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		
		String strFrom = "from CueExe t ";
		String queryString = "";
		
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CueExeSearchPage: " + cueExeSearchPage.infoString()); 
		}
	
		// filtro por Id Cuenta
		if (cueExeSearchPage.getCueExe().getCuenta().getId() != null ) {
			queryString = strFrom + " where t.cuenta.id = " + cueExeSearchPage.getCueExe().getCuenta().getId();
			
		} else {
			
			// filtro cueExe excluidos
	 		List<CueExeVO> listCueExeExcluidos = (List<CueExeVO>) cueExeSearchPage.getListVOExcluidos();
	 		if (!ListUtil.isNullOrEmpty(listCueExeExcluidos)) {
	 			queryString += flagAnd ? " and " : " where ";
	
	 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCueExeExcluidos);
				queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
				flagAnd = true;
			}
	 		
	 		// Exencion
	 		if (!ModelUtil.isNullOrEmpty(cueExeSearchPage.getCueExe().getExencion())) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.exencion.id = " + cueExeSearchPage.getCueExe().getExencion().getId();
				flagAnd = true;
			
			// filtro por Recurso
	 		} else if (!ModelUtil.isNullOrEmpty(cueExeSearchPage.getCueExe().getRecurso())){
	 			
	 			queryString += flagAnd ? " and " : " where ";
				queryString += " t.exencion.recurso.id = " + cueExeSearchPage.getCueExe().getRecurso().getId();
				flagAnd = true;
	 		}
	 		
	 		// filtro por Numero Cuenta
	 		if (!StringUtil.isNullOrEmpty(cueExeSearchPage.getCueExe().getCuenta().getNumeroCuenta())) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.cuenta.numeroCuenta = '" + 
					StringUtil.formatNumeroCuenta(cueExeSearchPage.getCueExe().getCuenta().getNumeroCuenta()) +"'";
				flagAnd = true;
			}
	 		
	 		// filtro por fecha Desde
	 		Date fechaDesde = cueExeSearchPage.getCueExe().getFechaDesde();
	 		if (fechaDesde != null ) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.fechaDesde >= TO_DATE('" + 
					DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
				
				flagAnd = true;
			}
			
	 		// filtro por fecha Hasta
	 		Date fechaHasta = cueExeSearchPage.getCueExe().getFechaHasta(); 		
	 		if (fechaHasta != null ) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.fechaHasta <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
				
				flagAnd = true;
			}
	 		
	 		// Estado
	 		if (!ModelUtil.isNullOrEmpty(cueExeSearchPage.getCueExe().getEstadoCueExe())) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.estadoCueExe.id = " + cueExeSearchPage.getCueExe().getEstadoCueExe().getId();
				flagAnd = true;
	 		}
	 		
	 		// Descripcion de solicitante
	 		if(!StringUtil.isNullOrEmpty(cueExeSearchPage.getCueExe().getSolicDescripcion())){
	 			queryString += flagAnd ? " and " : " where ";
				queryString += "UPPER(TRIM(t.solicDescripcion)) like '%" + 
								StringUtil.escaparUpper(cueExeSearchPage.getCueExe().getSolicDescripcion()) + "%'";
				flagAnd = true;
	 		}
	 		
	 		// Estado en Historico
	 		if (!ModelUtil.isNullOrEmpty(cueExeSearchPage.getCueExe().getHisEstCueExe().getEstadoCueExe())) {
	            
	 			log.debug(funcName + " EstadoEnHistorico -> " + cueExeSearchPage.getEstadoEnHistorico() +
	 					" Estado en Historico: " + cueExeSearchPage.getCueExe().getHisEstCueExe().getEstadoCueExe().getId());
	 			
	 			strFrom = "SELECT t FROM CueExe t " +
	 							  "JOIN t.listHisEstCueExe as hisEstCueExe ";

	 			
	 			queryString += flagAnd ? " and " : " where ";
	 			
	 			if (cueExeSearchPage.getEstadoEnHistorico())
	 				queryString += " hisEstCueExe.estadoCueExe.id = " + cueExeSearchPage.getCueExe().getHisEstCueExe().getEstadoCueExe().getId();
	 			else
	 				queryString += " hisEstCueExe.estadoCueExe.id NOT IN (" + cueExeSearchPage.getCueExe().getHisEstCueExe().getEstadoCueExe().getId() + ")";
	 			
				flagAnd = true;
	 		}
	 		
	 		
	 		queryString = strFrom + queryString;
	 		
	 		// Order By
			queryString += " order by t.fechaDesde ";
		
		}
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CueExe> listCueExe = (ArrayList<CueExe>) executeCountedSearch(queryString, cueExeSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCueExe;
	}


	/**
	 * Obtiene las CueExe ACTIVAS para una cuenta, una exencion
	 * Utilizado para la migracion
	 *
	 */
	@SuppressWarnings({"unchecked"})
	public List<CueExe> getListByIdCuentaIdExencion(Long idCuenta, Long idExencion) throws Exception {
		
		String queryString = "FROM CueExe t WHERE t.cuenta.id = " + idCuenta + 
							 " AND t.exencion.id = " + idExencion + 
							 " AND t.estado = " + Estado.ACTIVO.getId();	
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		List<CueExe> listCueExe = query.list();
		
		return listCueExe;

	}

	/**
	 * Obtiene las CueExe ACTIVAS para una cuenta, una exencion
	 * Utilizado para la migracion
	 *
	 */
	@SuppressWarnings({"unchecked"})
	public List<CueExe> getListByIdCuentaIdExencionRangoFechas(Long idCuenta, Long idExencion, Date fechaDesde, Date fechaHasta) throws Exception {
		
		String queryString = "FROM CueExe t WHERE t.cuenta.id = " + idCuenta + 
							 " AND t.exencion.id = " + idExencion + 
							 " AND t.fechaDesde = :fechaDesde ";
		
		if (fechaHasta != null)
			queryString +=	" AND t.fechaHasta = :fechaHasta ";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
							.setDate("fechaDesde", fechaDesde);
		
		if (fechaHasta != null)
			query.setDate("fechaHasta", fechaHasta);
		
		List<CueExe> listCueExe = query.list();
		
		return listCueExe;

	}
	
	/**
	 * Obtiene las CueExe ACTIVAS para una cuenta, una exencion
	 * Utilizado para la migracion
	 *
	 */
	@SuppressWarnings({"unchecked"})
	public List<CueExe> getListByIdCuentaIdExencionRangoFechasWithoutTipoSujeto(Long idCuenta, Long idExencion, Date fechaDesde, Date fechaHasta) throws Exception {
		
		String queryString = "FROM CueExe t WHERE t.cuenta.id = " + idCuenta + 
							 " AND t.exencion.id = " + idExencion; 
		
		queryString += " AND t.tipoSujeto.id is not null ";
		
		queryString += " AND t.fechaDesde = :fechaDesde ";
		
		if (fechaHasta != null)
			queryString +=	" AND t.fechaHasta = :fechaHasta ";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString)
							.setDate("fechaDesde", fechaDesde);
		
		if (fechaHasta != null)
			query.setDate("fechaHasta", fechaHasta);
		
		List<CueExe> listCueExe = query.list();
		
		return listCueExe;

	}
	
	/**
	 * Obtiene las CueExe Vigentes a la Fecha pasada para una cuenta y una exencion
	 *
	 */
	public List<CueExe> getListVigentesByIdCuentaIdExencionIdEstado(Long idCuenta, Long idExencion, Date fecha, Long idEstado) throws Exception {
	
		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM CueExe cueExe WHERE " +
					  "cueExe.cuenta.id = :cuenta AND " +
					  "cueExe.exencion.id = :exencion AND " +
					  "cueExe.estadoCueExe.id = :estado AND " +
		  			  "cueExe.fechaDesde <= :fechaActual AND " +
		  			  "(cueExe.fechaHasta IS NULL OR " + 
		  			  "cueExe.fechaHasta > :fechaActual) ";

		List<CueExe> listCueExeVigentes = (ArrayList<CueExe>) session.createQuery(query)
										 	.setLong("cuenta", idCuenta)
										 	.setLong("exencion", idExencion)
										 	.setLong("estado", idEstado)
										 	.setDate("fechaActual", fecha)
										 	.list();

		return listCueExeVigentes;

	}
	
	/**
	 * Obtiene las CueExe Vigentes a la Fecha pasada para una cuenta y una exencion
	 *
	 */
	public List<CueExe> getListVigentesByIdCuentaIdExencionIdEstadoWithoutTipoSujeto(Long idCuenta, Long idExencion, Date fecha, Long idEstado) throws Exception {
	
		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM CueExe cueExe WHERE " +
					  "cueExe.cuenta.id = :cuenta AND " +
					  "cueExe.exencion.id = :exencion AND " +
					  "cueExe.estadoCueExe.id = :estado AND " +
					  "cueExe.tipoSujeto.id is not null AND " +
		  			  "cueExe.fechaDesde <= :fechaActual AND " +
		  			  "(cueExe.fechaHasta IS NULL OR " + 
		  			  "cueExe.fechaHasta > :fechaActual) ";

		List<CueExe> listCueExeVigentes = (ArrayList<CueExe>) session.createQuery(query)
										 	.setLong("cuenta", idCuenta)
										 	.setLong("exencion", idExencion)
										 	.setLong("estado", idEstado)
										 	.setDate("fechaActual", fecha)
										 	.list();

		return listCueExeVigentes;

	}
	
	/**
	 * Devuelve un Cue Exe a revocar.
	 * 
	 * @author Cristian
	 * @param idARevocar
	 * @return
	 */
	public CueExe getByIdSolCueExeVig(Long idARevocar){
		CueExe cueExe;
		String queryString = "FROM CueExe t WHERE t.solCueExeVig.id = " + idARevocar;
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		cueExe = (CueExe) query.uniqueResult();	

		return cueExe; 
	}
	
	
	@SuppressWarnings({"unchecked"})
	public List<CueExe> getListVigenteByCuenta(Cuenta cuenta) throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM CueExe cueExe WHERE " +
					  "cueExe.cuenta = :cuenta AND " +
					  "cueExe.estadoCueExe.id = :estadoCueExe " + 
		  			  " ORDER BY cueExe.fechaDesde";

		List<CueExe> listCueExeVigentes = session.createQuery(query)
										 	.setEntity("cuenta", cuenta)
										 	.setLong("estadoCueExe", EstadoCueExe.ID_HA_LUGAR)
										 	.list();

		return listCueExeVigentes;

	}

	/** Lista de cueexe en parametro fecha, dentro de fechaDesde y fechaHasta,
	 *  Activas y en estadoCueExe en CREADA  
	 */
	@SuppressWarnings({"unchecked"})
	public List<CueExe> getListEnTramiteByCuenta(Cuenta cuenta) {
		Session session = SiatHibernateUtil.currentSession();
		String query= "FROM CueExe cueExe WHERE " +
					  "cueExe.cuenta = :cuenta AND " +
					  "cueExe.estadoCueExe.id = :creada";
		
		List<CueExe> listCueExe = session.createQuery(query)
										 	.setEntity("cuenta", cuenta)
										 	.setLong("creada", EstadoCueExe.ID_CREADA)
										 	.list();

		return listCueExe;
	}

	
	/**
	 * Obtiene las exenciones que pueda tener una cuenta y que No Actualizen Deuda.
	 * 
	 * 
	 * @param cuenta
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked"})
	public List<CueExe> getListNoActDeudaByCuenta(Cuenta cuenta) throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM CueExe cueExe WHERE " +
					  "cueExe.cuenta = :cuenta AND " +
					  "cueExe.estadoCueExe.id = :estado AND " + 
		  			  "cueExe.exencion.actualizaDeuda = 0";
		
		List<CueExe> listCueExeVigentes = session.createQuery(query)
										 	.setEntity("cuenta", cuenta)
										 	.setLong("estado", EstadoCueExe.ID_HA_LUGAR)
										 	.list();

		return listCueExeVigentes;

	}

	/**
	 * Obtiene las exenciones ha lugar para todas las cuentas que No Actualizen Deuda.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CueExe> getListNoActDeuda() throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM CueExe cueExe WHERE " +
					  "cueExe.estadoCueExe.id = :estado AND " + 
		  			  "cueExe.exencion.actualizaDeuda = 0";

		List<CueExe> listCueExeVigentes = (ArrayList<CueExe>) session.createQuery(query)
										 	.setLong("estado", EstadoCueExe.ID_HA_LUGAR)
										 	.list();

		return listCueExeVigentes;

	}

	
	/**
	 * Retorna la lista de todas las excenciones historicas de la cuenta.
	 */
	@SuppressWarnings({"unchecked"})
	public List<CueExe> getListByCuenta(Cuenta cuenta) throws Exception {

		Session session = SiatHibernateUtil.currentSession();
		
		String query= "FROM CueExe cueExe WHERE cueExe.cuenta = :cuenta";

		List<CueExe> listCueExe = session.createQuery(query)
										 	.setEntity("cuenta", cuenta)
										 	.list();
		return listCueExe;
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
	  *  Inserta una linea con los datos de la CueExe para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param cueExe, output - Exencion a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(CueExe o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|idcuenta|idexencion|fechadese|fechahasta|fechaSolicitud|idEstadoCueExe|idTipoSujeto|usuario|fechaultmdf|estado| 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getCuenta().getId());		 
		 line.append("|");
		 line.append(o.getExencion().getId());		 
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaDesde(), "yyyy-MM-dd"));
		 line.append("|");
		 if(o.getFechaHasta()!=null){
			line.append(DateUtil.formatDate(o.getFechaHasta(), "yyyy-MM-dd"));		 
		 } // Si es null no se inserta nada, viene el proximo pipe.
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaDesde(), "yyyy-MM-dd")); // Fecha Solicitud
		 line.append("|");
		 line.append("10"); // IdEstadoCueExe : 10 (Ha Lugar)
		 line.append("|");
		 if(o.getTipoSujeto()!=null)
			line.append(o.getTipoSujeto().getId());		 
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
		 * Obtiene las exenciones que no tienen registro en HisEstadoCueExe.
		 * 
		 * @return listCueExe
		 * @throws Exception
		 */
		public List<CueExe> getListSinHisEstadoCueExe() throws Exception {

			Session session = SiatHibernateUtil.currentSession();
			//select count(e.id) from exe_cueexe e where e.id not in (select distinct idcueexe from exe_hisestcueexe);

			//String query= "h.cueExe FROM HisEstCueExe h";
			//String query= "FROM CueExe cueExe WHERE cueExe.listHisEstCueExe.size = 0";
			String query = "select e.id from exe_cueexe e where e.id not in (select distinct idcueexe from exe_hisestcueexe)";

			List<Integer> listIdCueExeConHisEstCueExe = (ArrayList<Integer>) session.createSQLQuery(query).list();
			
			//List<CueExe> listCueExeConHisEstCueExe = (ArrayList<CueExe>) session.createQuery(query)
			//										.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			//										.list();
			
			String ids = null;
			if(!ListUtil.isNullOrEmpty(listIdCueExeConHisEstCueExe)){
				ids = "(";
				for(Integer id: listIdCueExeConHisEstCueExe){
					if(!ids.equals("("))
						ids += ",";
					ids += id.toString();
				}
				ids +=")";
			}

			query= "FROM CueExe cueExe";
			if(ids != null)
				query += " WHERE cueExe.id not in "+ids;
			
			List<CueExe> listCueExeSinHisEstCueExe = (ArrayList<CueExe>) session.createQuery(query).list();

			return listCueExeSinHisEstCueExe;

		}

	@SuppressWarnings({"unchecked"})
	public List<CueExe> getMarcaCueExeBySearchPage(MarcaCueExeSearchPage marcaCueExeSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		
		String strFrom = "from CueExe t ";
		String queryString = "";
		
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del CueExeSearchPage: " + marcaCueExeSearchPage.infoString()); 
		}
	
		// filtro por Id Cuenta
		if (marcaCueExeSearchPage.getCueExe().getCuenta().getId() != null ) {
			queryString = strFrom + " where t.cuenta.id = " + marcaCueExeSearchPage.getCueExe().getCuenta().getId();
			
		} else {
			
			// filtro cueExe excluidos
	 		List<CueExeVO> listCueExeExcluidos = (List<CueExeVO>) marcaCueExeSearchPage.getListVOExcluidos();
	 		if (!ListUtil.isNullOrEmpty(listCueExeExcluidos)) {
	 			queryString += flagAnd ? " and " : " where ";
	
	 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listCueExeExcluidos);
				queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
				flagAnd = true;
			}
	 		
	 		// Exencion
	 		if (!ModelUtil.isNullOrEmpty(marcaCueExeSearchPage.getCueExe().getExencion())) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.exencion.id = " + marcaCueExeSearchPage.getCueExe().getExencion().getId();
				flagAnd = true;
			
			// filtro por Recurso
	 		} else if (!ModelUtil.isNullOrEmpty(marcaCueExeSearchPage.getCueExe().getRecurso())){
	 			
	 			queryString += flagAnd ? " and " : " where ";
				queryString += " t.exencion.recurso.id = " + marcaCueExeSearchPage.getCueExe().getRecurso().getId();
				flagAnd = true;
	 		}
	 		
	 		// filtro por Numero Cuenta
	 		if (!StringUtil.isNullOrEmpty(marcaCueExeSearchPage.getCueExe().getCuenta().getNumeroCuenta())) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.cuenta.numeroCuenta = '" + 
					StringUtil.formatNumeroCuenta(marcaCueExeSearchPage.getCueExe().getCuenta().getNumeroCuenta()) +"'";
				flagAnd = true;
			}
	 		
	 		// filtro por fecha Desde
	 		Date fechaDesde = marcaCueExeSearchPage.getCueExe().getFechaDesde();
	 		if (fechaDesde != null ) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.fechaSolicitud >= TO_DATE('" + 
					DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
				
				flagAnd = true;
			}
			
	 		// filtro por fecha Hasta
	 		Date fechaHasta = marcaCueExeSearchPage.getCueExe().getFechaHasta(); 		
	 		if (fechaHasta != null ) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.fechaSolicitud <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
				
				flagAnd = true;
			}
	 		
	 		// Estado
	 		if (!ModelUtil.isNullOrEmpty(marcaCueExeSearchPage.getCueExe().getEstadoCueExe())) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.estadoCueExe.id = " + marcaCueExeSearchPage.getCueExe().getEstadoCueExe().getId();
				flagAnd = true;
	 		}
	 		
	 		
	 		// Estado en Historico
	 		if (!ModelUtil.isNullOrEmpty(marcaCueExeSearchPage.getCueExe().getHisEstCueExe().getEstadoCueExe())) {
	            
	 			log.debug(funcName + " EstadoEnHistorico -> " + marcaCueExeSearchPage.getEstadoEnHistorico() +
	 					" Estado en Historico: " + marcaCueExeSearchPage.getCueExe().getHisEstCueExe().getEstadoCueExe().getId());
	 			
	 			strFrom = "SELECT t FROM CueExe t " +
	 							  "JOIN t.listHisEstCueExe as hisEstCueExe ";

	 			
	 			queryString += flagAnd ? " and " : " where ";
	 			
	 			if (marcaCueExeSearchPage.getEstadoEnHistorico())
	 				queryString += " hisEstCueExe.estadoCueExe.id = " + marcaCueExeSearchPage.getCueExe().getHisEstCueExe().getEstadoCueExe().getId();
	 			else
	 				queryString += " hisEstCueExe.estadoCueExe.id NOT IN (" + marcaCueExeSearchPage.getCueExe().getHisEstCueExe().getEstadoCueExe().getId() + ")";
	 			
				flagAnd = true;
	 		}
	 		
	 		
	 		queryString = strFrom + queryString;
	 		
	 		// Order By
			queryString += " order by t.fechaDesde ";
		
		}
		
		
		// Si imprimimos
		if (marcaCueExeSearchPage.getReport().getImprimir()){
			// realiza la ejecucion de la busqueda y la generacion del archivo del reporte
			executeCountedSearch(queryString, marcaCueExeSearchPage);
		}
		
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Session session = SiatHibernateUtil.currentSession();
	    Query query = session.createQuery(queryString);
	    query.setMaxResults(1000);
		List<CueExe> listCueExe = (ArrayList<CueExe>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listCueExe;
	}


	/**
	 * Retorna todas las CueExe con cuentas para el recurso y fecha de
	 * vigencia a la fecha pasados como parametro.
	 * Ademas se cargan, para cada CueExe, el Tipo de Sujeto
	 * y la Exencion correspondientes.
	 * 
	 * Nota: este metodo es utilizado durante la inicializacion del cache 
	 * de CueExe.
	 *
	 * @param recurso
	 * @param fecha
	 * @return listCueExe
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public List<CueExe> initializeCache(Recurso recurso, Date fecha) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = SiatHibernateUtil.currentSession();

		String queryString = "select cueExe from CueExe as cueExe "
				+ "left join fetch cueExe.exencion "
				+ "left join fetch cueExe.tipoSujeto "
				+ "where cueExe.estadoCueExe.id = :estadoCueExe and "
				+ "cueExe.cuenta.recurso = :recurso and "
				+ "(cueExe.fechaHasta is null or "
				+ " cueExe.fechaHasta >= :fecha)";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CueExe> listCueExeVigentes = session.createQuery(queryString)
				.setLong("estadoCueExe", EstadoCueExe.ID_HA_LUGAR)
				.setEntity("recurso", recurso)
				.setDate("fecha", fecha).list();

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return listCueExeVigentes;
	}
	

	/**
	 * Retorna todas las CueExe con cuentas para el recurso  
	 * pasado como parametro.
	 * Ademas se cargan, para cada CueExe, el Tipo de Sujeto
	 * y la Exencion correspondientes.
	 * 
	 * Nota: este metodo es utilizado durante la inicializacion del cache 
	 * de CueExe.
	 *
	 * @param recurso
	 * @return listCueExe
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public List<CueExe> initializeCache(Recurso recurso) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = SiatHibernateUtil.currentSession();

		String queryString = "select cueExe from CueExe as cueExe "
				+ "left join fetch cueExe.exencion "
				+ "left join fetch cueExe.tipoSujeto "
				+ "where cueExe.estadoCueExe.id = :estadoCueExe and "
				+ "cueExe.cuenta.recurso = :recurso";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<CueExe> listCueExe = session.createQuery(queryString)
				.setLong("estadoCueExe", EstadoCueExe.ID_HA_LUGAR)
				.setEntity("recurso", recurso).list();


		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return listCueExe;
	}
	
	/**
	 * Obtiene las CueExe para la exencion pasada como parametro.
	 * Utilizado para la migracion
	 */
	@SuppressWarnings("unchecked")
	public List<CueExe> getListByIdExencion(Long idExencion) throws Exception {
		
		String queryString = "FROM CueExe t WHERE t.exencion.id = " + idExencion;
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		List<CueExe> listCueExe = (ArrayList<CueExe>) query.list();
		
		return listCueExe;
	}
}
