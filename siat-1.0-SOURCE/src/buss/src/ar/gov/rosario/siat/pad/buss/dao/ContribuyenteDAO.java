//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.DomAtrVal;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.SerBanRec;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.gde.buss.bean.AuxContribuyenteCerReport;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.ConAtrVal;
import ar.gov.rosario.siat.pad.buss.bean.ConAtrValSec;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class ContribuyenteDAO extends GenericDAO {

	private static Logger log = Logger.getLogger(ContribuyenteDAO.class);
	
	private static long migId = -1;
	
	public ContribuyenteDAO() {
		super(Contribuyente.class);
	}

	
	/**
	 * Determina si existe un contribuyente con el id.
	 * @param id 
	 * @return boolean
	 */
	public boolean esContribuyenteById(Long id){
		
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "SELECT COUNT(contrib) FROM Contribuyente contrib " +
						" WHERE contrib.id = :id ";
    	Query query = session.createQuery(sQuery)
    					.setLong("id", id );
    	
    	Long cantidad = (Long) query.uniqueResult(); 				
    	return cantidad > 0;
	}
	
	public Contribuyente getByIdPersona(Long idPersona){
		
    	Session session = SiatHibernateUtil.currentSession();

    	String sQuery = "from Contribuyente c " +
						" where c.persona.id = "+idPersona;
    	
		Query query = session.createQuery(sQuery);
		
    	return (Contribuyente) query.uniqueResult();
	}
	 
   public List<Contribuyente> getContribuyenteCerBySec(ConAtrValSec conAtrValSec,List<Long>  listIdContribuyente, Date fechaReporte){
	   String stringIdContribuyente = ListUtil.getStringList(listIdContribuyente); 
    	Session session = SiatHibernateUtil.currentSession();

    	String sQuery = "SELECT c FROM Contribuyente c, ConAtrVal cav WHERE ";
    	sQuery +=  "c.id = cav.contribuyente.id AND cav.conAtr.id = 4"; 
    	sQuery += 				" and cav.valor * 1 >= :valorDesde";
    	if(conAtrValSec.getValorHasta()!=null){
    		sQuery += 				" AND (cav.valor * 1 <= :valorHasta )";
    	}
    	sQuery += 				" AND cav.fechaDesde <= :fechaReporte";
    	sQuery +=				" AND (cav.fechaHasta IS NULL OR cav.fechaHasta >= :fechaReporte )";
    	sQuery += 				" AND c.id IN ( "+stringIdContribuyente + ")";
    	
    	Query query = session.createQuery(sQuery).setInteger("valorDesde", conAtrValSec.getValorDesde())
												 .setInteger("valorHasta", conAtrValSec.getValorHasta())
												 .setDate("fechaReporte", fechaReporte);
		
    	return (List<Contribuyente>) query.list();
	}
   
	 
   public List<Contribuyente> getContribuyenteCerByAct(DomAtrVal domAtrVal,List<Long>  listIdContribuyente, Date fechaReporte){
	   String stringIdContribuyente = ListUtil.getStringList(listIdContribuyente); 
    	Session session = SiatHibernateUtil.currentSession();

    	String sQuery = "SELECT c FROM Contribuyente c, ConAtrVal cav WHERE ";
    	sQuery +=  "c.id = cav.contribuyente.id AND cav.conAtr.id = 4"; 
    	sQuery += 				" and cav.valor * 1 = "+ domAtrVal.getStrValor() ;
    	
    	sQuery += 				" AND cav.fechaDesde <= :fechaReporte";
    	sQuery +=				" AND (cav.fechaHasta IS NULL OR cav.fechaHasta >= :fechaReporte )";
    	sQuery += 				" AND c.id IN ( "+stringIdContribuyente + ")  ";
    	
    	Query query = session.createQuery(sQuery) .setDate("fechaReporte", fechaReporte);
		
    	return (List<Contribuyente>) query.list();
	}
   
   
   public List<Long> getContribuyenteCerByRec(Recurso recurso,List<Long>  listIdContribuyente){
	   String stringIdContribuyente = ListUtil.getStringList(listIdContribuyente); 
    	Session session = SiatHibernateUtil.currentSession();

    	String sQuery = "SELECT c.id FROM Contribuyente c, ConAtrVal cav, CuentaTitular ct, Cuenta cuenta WHERE ";
    	sQuery +=  "c.id = cav.contribuyente.id AND cav.contribuyente.id = ct.contribuyente.id AND ct.cuenta.id = cuenta.id "; 
    	sQuery +=  " AND cuenta.recurso.id = "+recurso.getId();
    	sQuery +=  " AND c.id IN ( "+stringIdContribuyente + ")  GROUP BY c.id ";
    	
    	Query query = session.createQuery(sQuery);
		
    	return (ArrayList<Long>) query.list();
	} 
   
   
	public List<Long> getContribuyenteCer(AuxContribuyenteCerReport auxContribuyenteCerReport) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = SiatHibernateUtil.currentSession();

		String strQuery = "SELECT contribuyente.id FROM Contribuyente contribuyente, ConAtrVal conAtrVal ";
		strQuery +=	 "WHERE contribuyente.id=conAtrVal.contribuyente.id AND conAtrVal.conAtr.id = "+ ConAtrVal.CONTRIBUYENTE_CER.toString();
		strQuery +=	 " AND conAtrVal.valor = '1' ";
		strQuery +=  "AND conAtrVal.fechaDesde  <= :fechaReporte ";
		strQuery +=  "AND (conAtrVal.fechaHasta >= :fechaReporte or conAtrVal.fechaHasta is null)";
	

		Query query = session.createQuery(strQuery)
							  .setDate("fechaReporte", auxContribuyenteCerReport.getFechaReporte());
        
		return (ArrayList<Long>) query.list();
		
	}
	
	 /**
	  *  Inserta una linea con los datos del Contribuyente para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param contribuyente, output - El Contribuyente a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(Contribuyente o, LogFile output) throws Exception {
		 
		 // Para el Contribuyente el Id es el de la Persona 
		 long id = o.getPersona().getId();
		 
		 // Estrucura de la linea:
		 // id|idpersona|fechadesde|nroIsib|usuario|fechaultmdf|estado|
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getPersona().getId());		 
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaDesde(), "yyyy-MM-dd"));
		 line.append("|");
		 if (o.getNroIsib() != null) {
		 	line.append(o.getNroIsib());
		 }
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
	  * Devuelve las cuentas en las que el contribuyente es titular principal. Las fechas se utilizan para la vigencia
	  * @param id
	  * @return
	  */
	 public List<Cuenta> getListCuentaVigentesForTitular(Long id, Long skip, Long first) {
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "SELECT SKIP " + skip + " FIRST " + first + " cuenta.* " +
				"FROM pad_cuentaTitular cuentaTitular, pad_cuenta cuenta " + 
				"WHERE cuentaTitular.idCuenta = cuenta.id AND " +
				"cuentaTitular.idContribuyente = :id AND "+
				"cuentaTitular.fechaDesde<=TO_DATE('"+ DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND "+
				"(cuentaTitular.fechaHasta IS NULL OR cuentaTitular.fechaHasta>=TO_DATE('"+ DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) "+
				"ORDER BY cuentaTitular.id";
				
    	Query query = session.createSQLQuery(sQuery)
    					.addEntity("cuenta", Cuenta.class)
    					.setLong("id", id );
    	
    	return query.list();    	
	}

		/**
		 * Obtiene la lista de idPersona encontradas en la tabla de Contribuyentes.
		 * 
		 * @return
		 * @throws Exception
		 */
		public List<Integer> getListIdPersona() throws Exception {
					
			Session session = SiatHibernateUtil.currentSession();
			
			String queryString = "select idPersona from pad_contribuyente";
		    					    
		    Query query = session.createSQLQuery(queryString);
		    List<Integer> listIdPersona = (ArrayList<Integer>) query.list();
			
			return listIdPersona; 
		}

	
	public int countCuentasTitular(Contribuyente contribuyente){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT ct FROM CuentaTitular ct WHERE ct.contribuyente = :contribuyente";
	    
		log.debug("countCuentasTitular :" + queryString);
		
	    Query queryCnt = session.createQuery(queryString)
	    						.setEntity("contribuyente", contribuyente);
		
	    return getCount(queryCnt);
	}
	
	
	public List<CuentaTitular> getListCuentasTitularLimit(Contribuyente contribuyente, int first){
		 
	 	String queryString = "SELECT FIRST " + first + " ctaTit.* FROM pad_cuentaTitular ctaTit " +
	 					     "WHERE ctaTit.idContribuyente = " + contribuyente.getId();
	 																									   
	 	log.debug("getListCuentasTitularLimit :" + queryString);
	 	
	 	Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString)
						     .addEntity("ctaTit", CuentaTitular.class);
		
		return query.list(); 
		
	}

	
	
	public List<CuentaTitular> getListCuentasTitularPaged(Contribuyente contribuyente, int skip, int first){
		 
	 	String queryString = "SELECT SKIP " + skip + " FIRST " + first + " ctaTit.* FROM pad_cuentaTitular ctaTit " +
	 					     "WHERE ctaTit.idContribuyente = " + contribuyente.getId();
	 																									   
	 	log.debug("getListCuentasTitularLimit :" + queryString);
	 	
	 	Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString)
						     .addEntity("ctaTit", CuentaTitular.class);
		
		return query.list(); 
		
	}

	public int countCuentasTitular(DeudaContribSearchPage deudaContribSearchPage){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT ct FROM CuentaTitular ct WHERE ct.contribuyente.id = "+deudaContribSearchPage.getContribuyente().getId();
		
		if(!ModelUtil.isNullOrEmpty(deudaContribSearchPage.getServicioBanco())){
	 		ServicioBanco servicioBanco = ServicioBanco.getById(deudaContribSearchPage.getServicioBanco().getId());
	 		List<Recurso> listRecurso = new ArrayList<Recurso>();
	 		for(SerBanRec serBanRec: servicioBanco.getListSerBanRec())
	 			listRecurso.add(serBanRec.getRecurso());
	 		String listIdRecurso = ListUtil.getStringIds(listRecurso);
	 		if(!StringUtil.isNullOrEmpty(listIdRecurso))
		 		queryString += " AND ct.cuenta.recurso.id in ("+listIdRecurso+")";
	 	}
			
		log.debug("countCuentasTitular :" + queryString);
		
	    Query queryCnt = session.createQuery(queryString);
	    						
		
	    return getCount(queryCnt);
	}
	
	public List<CuentaTitular> getListCuentasTitularPaged(DeudaContribSearchPage deudaContribSearchPage,Integer skip, Integer first) throws Exception{
		String queryString = "";
	 	if(skip != null && first != null){
	 		queryString = "SELECT SKIP " + skip + " FIRST " +first  + " ctaTit.* FROM pad_cuentaTitular ctaTit ";
	 		queryString += " , pad_cuenta cta ";
	 		queryString += " WHERE ctaTit.idContribuyente = " + deudaContribSearchPage.getContribuyente().getId();
	 		queryString += " AND cta.id = ctaTit.idCuenta";
	 	}else{
	 		queryString = "SELECT ctaTit.* FROM pad_cuentaTitular ctaTit";
	 		queryString += " , pad_cuenta cta ";
	 		queryString += " WHERE ctaTit.idContribuyente = " + deudaContribSearchPage.getContribuyente().getId();
	 		queryString += " AND cta.id = ctaTit.idCuenta";
	 	}
	 	
	 	// Si filtra por los recursos habilitados para el area del usuario
	 	List<Recurso> listRecurso = Recurso.getListTributarios();
	 	
	 	// Agrega Filtro por ServicioBanco (solo recursos del servicio banco y permitidos para el area)
	 	if(!ModelUtil.isNullOrEmpty(deudaContribSearchPage.getServicioBanco())){
	 		ServicioBanco servicioBanco = ServicioBanco.getById(deudaContribSearchPage.getServicioBanco().getId());
	 		listRecurso = new ArrayList<Recurso>();
	 		for(SerBanRec serBanRec: servicioBanco.getListSerBanRec())
	 			listRecurso.add(serBanRec.getRecurso());
	 		listRecurso = Recurso.filtrarPorArea(listRecurso);	
	 	}
	 	if(!ListUtil.isNullOrEmpty(listRecurso)){
	 		String listIdRecurso = ListUtil.getStringIds(listRecurso);
	 		queryString += " AND cta.idRecurso in ("+listIdRecurso+")";	 		
	 	}else{
	 		return new ArrayList<CuentaTitular>();
	 	}

	 	queryString += " ORDER BY cta.idRecurso, cta.numeroCuenta";
	 	
	 	log.debug("getListCuentasTitularLimit :" + queryString);
	 	
	 	Session session = SiatHibernateUtil.currentSession();

		Query query = null; 
		query = session.createSQLQuery(queryString).addEntity("ctaTit", CuentaTitular.class);
		
		return query.list(); 
		
	}
	
	/**
	 * Obtiene las cuentas vigentes para el contribuyente, filtrando por el recurso pasado como parametro
	 * @param recurso
	 * @param cuentaExcluir - Esta cuenta se va a excluir de la lista que retorna - si es null no se tiene en cuenta
	 * @return
	 */
	public List<Cuenta> getListCueVig4Titular(Recurso recurso, Contribuyente contribuyente, Cuenta cuentaExcluir) {
		log.debug("getListCueVig4Titular - enter");
    	Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "SELECT ct.cuenta FROM CuentaTitular ct, Cuenta c WHERE " +
				"ct.cuenta.recurso = :recurso"+
				" AND ct.fechaDesde>= :fechaDesde"+
				" AND  (ct.fechaHasta IS NULL OR ct.fechaHasta > :fechaHasta) "+
				" AND (c.fechaBaja IS NULL OR c.fechaBaja > :fechaHasta)";
		
		if(contribuyente!=null){
			sQuery +=" and ct.contribuyente =  :contribuyente ";
		}
		
		if(cuentaExcluir!=null){
			sQuery +=" and ct.cuenta != :cuenta";
		}
		
    	Query query = session.createQuery(sQuery);
    	query.setEntity("recurso", recurso);
    	query.setDate("fechaDesde", new Date());
    	query.setDate("fechaHasta", new Date());
    	
    	if(cuentaExcluir!=null)
    		query.setEntity("cuenta", cuentaExcluir);
    	
    	if(contribuyente!=null)
    		query.setEntity("contribuyente", contribuyente);
    	
    	log.debug("getListCueVig4Titular - exit");
    	return query.list();
	}


	public static void setMigId(long migId) {
		ContribuyenteDAO.migId = migId;
	}


	public static long getMigId() {
		return migId;
	}
	
}

