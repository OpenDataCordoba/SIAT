//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.esp.buss.bean.Habilitacion;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionSearchPage;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionVO;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class HabilitacionDAO extends GenericDAO {

	private Log log = LogFactory.getLog(HabilitacionDAO.class);	
	private static long migId = -1;

	public HabilitacionDAO() {
		super(Habilitacion.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Habilitacion> getListBySearchPage(HabilitacionSearchPage habilitacionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Habilitacion t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del HabilitacionSearchPage: " + habilitacionSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (habilitacionSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros
 		List<HabilitacionVO> listHabilitacionExcluidos = (ArrayList<HabilitacionVO>) habilitacionSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listHabilitacionExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";
 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listHabilitacionExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Recurso
 		if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getRecurso())) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id = " +  habilitacionSearchPage.getHabilitacion().getRecurso().getId();
			flagAnd = true;
		}

 		// filtro por Cuenta
 		if (!StringUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getCuenta().getNumeroCuenta())) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.cuenta.numeroCuenta = '" + 
 				StringUtil.formatNumeroCuenta(habilitacionSearchPage.getHabilitacion().getCuenta().getNumeroCuenta()) + "'";
			flagAnd = true;
		}

 		// filtro por EstHab
 		if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getEstHab())) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.estHab.id = " +  habilitacionSearchPage.getHabilitacion().getEstHab().getId();
			flagAnd = true;
		}

 		// filtro por TipoHab
 		if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getTipoHab())) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoHab.id = " +  habilitacionSearchPage.getHabilitacion().getTipoHab().getId();
			flagAnd = true;
		}
 		// filtro por Descripcion de Habilitacion
 		if (habilitacionSearchPage.getHabilitacion().getDescripcion() != null) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.descripcion)) like '%" + 
			StringUtil.escaparUpper(habilitacionSearchPage.getHabilitacion().getDescripcion()) + "%'";
			flagAnd = true;
		}

 		// filtro por numero
 		if (habilitacionSearchPage.getHabilitacion().getNumero() != null) {
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.numero = " +  habilitacionSearchPage.getHabilitacion().getNumero();
			flagAnd = true;
		}

 		// filtro por anio
 		if(habilitacionSearchPage.getHabilitacion().getAnio()!=null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.anio = " +  habilitacionSearchPage.getHabilitacion().getAnio();
			flagAnd = true;
		}

 		// filtro por Fecha Habilitacion Desde
		if (habilitacionSearchPage.getFechaDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaHab >= TO_DATE('" + 
					DateUtil.formatDate(habilitacionSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// filtro por Fecha Habilitacion Hasta
		if (habilitacionSearchPage.getFechaHasta()!=null) {			

		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaHab <= TO_DATE('" + 
					DateUtil.formatDate(habilitacionSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

		// filtro por Lista de Cuentas del Titular
		if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getTitular())) { 
			/*queryString += flagAnd ? " and " : " where ";	  
			queryString += " t.cuenta.id in (";
			if (!ListUtil.isNullOrEmpty(habilitacionSearchPage.getListCuentaPersona())) {
			  for (CuentaVO cuenta: habilitacionSearchPage.getListCuentaPersona()) {
				  queryString += cuenta.getId();
				  queryString += ",";
			  }
			} 
			queryString += "-1)";
			flagAnd = true;*/
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.idPerHab = " + habilitacionSearchPage.getTitular().getId();
		  flagAnd = true;
		}

		// Filtro para traer planillas sin registros de entradas vendidas. Se utiliza en el Reporte de Habilitacion
		if(habilitacionSearchPage.getReporteSinEntradasVendidas()){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.id not in (select entVen.habilitacion.id from EntVen entVen)";
			flagAnd = true;
		}
		
		// filtro por Fecha Evento 
		if (habilitacionSearchPage.getFechaEventoDesde()!=null || habilitacionSearchPage.getFechaEventoHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  
		  if (habilitacionSearchPage.getFechaEventoDesde()!=null && habilitacionSearchPage.getFechaEventoHasta()!=null) {
			  String fechaEventoDesde = DateUtil.formatDate(habilitacionSearchPage.getFechaEventoDesde(), DateUtil.ddSMMSYYYY_MASK) ;
			  String fechaEventoHasta = DateUtil.formatDate(habilitacionSearchPage.getFechaEventoHasta(), DateUtil.ddSMMSYYYY_MASK) ;
			  queryString += " ((t.fecEveHas is null and t.fecEveDes >= TO_DATE('" +fechaEventoDesde+ "','%d/%m/%Y') " +
							" and t.fecEveDes <= TO_DATE('" +fechaEventoHasta+ "','%d/%m/%Y')) or (t.fecEveHas is not null and " +
						" (t.fecEveDes <= TO_DATE('" + fechaEventoDesde + "','%d/%m/%Y') and t.fecEveHas >= TO_DATE('" +fechaEventoDesde + "','%d/%m/%Y')) or " +
			  			" (t.fecEveDes <= TO_DATE('" + fechaEventoHasta + "','%d/%m/%Y') and t.fecEveHas >= TO_DATE('" +fechaEventoHasta + "','%d/%m/%Y')))) " ;
		  }
		  if (habilitacionSearchPage.getFechaEventoDesde()!=null && habilitacionSearchPage.getFechaEventoHasta()==null) {
			  String fechaEventoDesde = DateUtil.formatDate(habilitacionSearchPage.getFechaEventoDesde(), DateUtil.ddSMMSYYYY_MASK) ;
			  queryString += " ((t.fecEveHas is null and t.fecEveDes >= TO_DATE('" +fechaEventoDesde+ "','%d/%m/%Y'))" +
			  					" or (t.fecEveHas is not null and " +
			  					" t.fecEveDes <= TO_DATE('" + fechaEventoDesde + "','%d/%m/%Y') and t.fecEveHas >= TO_DATE('" +fechaEventoDesde + "','%d/%m/%Y'))) " ;
		  }
		  if (habilitacionSearchPage.getFechaEventoDesde()==null && habilitacionSearchPage.getFechaEventoHasta()!=null) {
			  String fechaEventoHasta = DateUtil.formatDate(habilitacionSearchPage.getFechaEventoHasta(), DateUtil.ddSMMSYYYY_MASK) ;
			  queryString += " ((t.fecEveHas is null and t.fecEveDes <= TO_DATE('" +fechaEventoHasta+ "','%d/%m/%Y'))" +
			  					" or (t.fecEveHas is not null and " +
			  					" t.fecEveDes <= TO_DATE('" + fechaEventoHasta + "','%d/%m/%Y') and t.fecEveHas >= TO_DATE('" +fechaEventoHasta + "','%d/%m/%Y'))) " ;
		  }
		  
		  flagAnd = true;
		}
		
 		// Order By
		queryString += " order by t.fechaHab DESC, t.numero DESC, t.anio DESC ";
		 
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Habilitacion> listHabilitacion = (ArrayList<Habilitacion>) 
			executeCountedSearch(queryString, habilitacionSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listHabilitacion;
	}
	
	public static void setMigId(long migId) {
		HabilitacionDAO.migId = migId;
	}

	public static long getMigId() {
		return HabilitacionDAO.migId;
	}


	/**
	 *  Inserta una linea con los datos de la Habilitacion para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param habilitacin, output - La Habilitacion a crear y el archivo al que se agrega la linea
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(Habilitacion o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		//   id|numero|anio|idrecurso|idcuenta|idtipocobro|fechahab|fecevedes|descripcion|lugarevento|horaacceso|idperhab|
		// cuit|observaciones|idesthab|idtipohab|idtipoevento|usuario|fechaultmdf|estado|
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getNumero());
		line.append("|");	
		line.append(o.getAnio());		 
		line.append("|");
		line.append(o.getRecurso().getId() );		 
		line.append("|");
		line.append(o.getCuenta().getId());
		line.append("|");
		line.append(o.getTipoCobro().getId());
		line.append("|");
		line.append(DateUtil.formatDate(o.getFechaHab(), "yyyy-MM-dd"));
		line.append("|");
		line.append(DateUtil.formatDate(o.getFecEveDes() , "yyyy-MM-dd"));
		line.append("|");
		line.append(o.getDescripcion());
		line.append("|");
		if (o.getLugarEvento() != null)
			line.append(o.getLugarEvento());
		line.append("|");
		if (o.getHoraAcceso() != null)
			line.append(DateUtil.formatDate(o.getHoraAcceso(), "HH:mm"));
		line.append("|");
		if (o.getIdPerHab() != null) {
			line.append(o.getIdPerHab());
		}
		line.append("|");
		line.append(o.getCuit());
		line.append("|");
		line.append(o.getObservaciones());
		line.append("|");
		line.append(o.getEstHab().getId());
		line.append("|");
		line.append(o.getTipoHab().getId() );
		line.append("|");
		line.append(o.getTipoEvento().getId() );
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
	 * Devuelve el proximo numero de Habiltacion a generar para un alta.
	 * Devuele el maximo numero para el anio actual ya que por 
	 * migracion pueden existir numeros repetido para distinos anios.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Integer getNextNumero(Integer anio, Long idTipoHab) throws Exception {
		try {
			String queryString = ""; 
			queryString += "select MAX(t.numero) + 1 ";
			queryString += "from Habilitacion t where t.anio = " + anio;
			queryString += " and t.tipoHab.id = " + idTipoHab;
			
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(queryString);
			query.setMaxResults(1);
		
			Integer  nextNumero = (Integer) query.uniqueResult();
			if(nextNumero == null)
				return 1;
			
			return nextNumero; 
			
		} catch (NonUniqueResultException e) {
			log.error("El el numero maximo no es unico", e);
			return null;
		}	
	}

	@SuppressWarnings("unchecked")
	public List<Habilitacion> getListHabSinEntVenBySearchPage(HabilitacionSearchPage habilitacionSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "";
		queryString += "from Habilitacion t";
		queryString += " where t.id not in (select entVen.habilitacion.id from EntVen entVen)";
		
		// Armamos filtros del HQL
		if (habilitacionSearchPage.getModoSeleccionar()) {
		  queryString += " and  t.estado = "+ Estado.ACTIVO.getId();
	    }
		
		// Filtros
 		List<HabilitacionVO> listHabilitacionExcluidos = (ArrayList<HabilitacionVO>) habilitacionSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listHabilitacionExcluidos)) {
 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listHabilitacionExcluidos);
			queryString += " and t.id NOT IN ("+ listIdExcluidos + ") "; 
		}
		
		// filtro por Recurso
 		if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getRecurso())) {
			queryString += " and t.recurso.id = " +  habilitacionSearchPage.getHabilitacion().getRecurso().getId();
		}

 		// filtro por Cuenta
 		if (!StringUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getCuenta().getNumeroCuenta())) {
			queryString += " and t.cuenta.numeroCuenta = '" +  habilitacionSearchPage.getHabilitacion().getCuenta().getNumeroCuenta() + "'";
 		}

 		// filtro por EstHab
 		if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getEstHab())) {
			queryString += " and t.estHab.id = " +  habilitacionSearchPage.getHabilitacion().getEstHab().getId();
		}

 		// filtro por TipoHab
 		if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getHabilitacion().getTipoHab())) {
			queryString += " and t.tipoHab.id = " +  habilitacionSearchPage.getHabilitacion().getTipoHab().getId();
		}

 		// filtro por Descripcion de Habilitacion
 		if (habilitacionSearchPage.getHabilitacion().getDescripcion() != null) {
			queryString += " and UPPER(TRIM(t.descripcion)) like '%" + 
			StringUtil.escaparUpper(habilitacionSearchPage.getHabilitacion().getDescripcion()) + "%'";
 		}

 		// filtro por numero
 		if (habilitacionSearchPage.getHabilitacion().getNumero() != null) {
			queryString += " and t.numero = " +  habilitacionSearchPage.getHabilitacion().getNumero();
		}

 		// filtro por anio
 		if(habilitacionSearchPage.getHabilitacion().getAnio()!=null){
			queryString += " and t.anio = " +  habilitacionSearchPage.getHabilitacion().getAnio();
		}

 		// filtro por Fecha Habilitacion Desde
		if (habilitacionSearchPage.getFechaDesde()!=null) {
	  		  queryString += " and (t.fechaHab >= TO_DATE('" + 
					DateUtil.formatDate(habilitacionSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
		}

 		// filtro por Fecha Habilitacion Hasta
		if (habilitacionSearchPage.getFechaHasta()!=null) {			
		  queryString += " and (t.fechaHab <= TO_DATE('" + 
					DateUtil.formatDate(habilitacionSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
		}

		// filtro por Lista de Cuentas del Titular
		if (!ModelUtil.isNullOrEmpty(habilitacionSearchPage.getTitular())) { 
		  queryString += " and t.idPerHab = " + habilitacionSearchPage.getTitular().getId();
		}

 		// Order By
		//queryString += " order by t.fechaHab DESC, t.numero DESC, t.anio DESC ";
		 
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		List<Habilitacion> listHabilitacion = (ArrayList<Habilitacion>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listHabilitacion;
	}

	/**
	 *  Obtener Habilitacion para el Numero, Anio y idCuenta indicados.
	 * 
	 * @param numero
	 * @param anio
	 * @param idCuenta
	 * @return
	 * @throws Exception
	 */
	public Habilitacion getByNroAnioYIdCuenta(Integer numero, Integer anio, Long idCuenta )  throws Exception {
		Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "select t from Habilitacion t " +
							"	where t.numero = :numero " +
							"	  and t.anio = :anio " +
							"     and t.cuenta.id = :idCuenta";
		
    	Query query = session.createQuery(sQuery)
    					.setInteger("numero", numero)
    					.setInteger("anio", anio)
    					.setLong("idCuenta", idCuenta);
    	
    	return (Habilitacion) query.uniqueResult();
		
	}
	
}
