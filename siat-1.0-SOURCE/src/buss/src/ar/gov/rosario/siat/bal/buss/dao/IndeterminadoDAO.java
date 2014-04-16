//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Indeterminado;
import ar.gov.rosario.siat.bal.buss.bean.ReingIndet;
import ar.gov.rosario.siat.bal.buss.cache.IndeterminadoCache;
import ar.gov.rosario.siat.bal.iface.model.IndetSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.ReportVO;

public class IndeterminadoDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(IndeterminadoDAO.class);
	
	public IndeterminadoDAO(){
		super(Indeterminado.class);
	}
	
	public List<IndetVO> getListBySearchPage(IndetSearchPage indetSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Indeterminado t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del IndeterminadoSearchPage: " + indetSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (indetSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro por Sistema
		if(!StringUtil.isNullOrEmpty(indetSearchPage.getIndet().getSistema())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.sistema = " +  Long.valueOf(indetSearchPage.getIndet().getSistema());
			flagAnd = true;
		}

 		// filtro por NroComprobante
		if(!StringUtil.isNullOrEmpty(indetSearchPage.getIndet().getNroComprobante())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroComprobante = " +  indetSearchPage.getIndet().getNroComprobante();
			flagAnd = true;
		}

 		// 	 filtro por Importe Combrado
		if(indetSearchPage.getIndet().getImporteCobrado() != null){
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t. importe = " +  indetSearchPage.getIndet().getImporteCobrado();
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Balance
		if (indetSearchPage.getIndet().getFechaBalance() !=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.fechaBalance = " + sqlDate(indetSearchPage.getIndet().getFechaBalance());
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Pago 
		if (indetSearchPage.getIndet().getFechaPago() !=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " t.fechaPago = " + sqlDate(indetSearchPage.getIndet().getFechaPago());
	      flagAnd = true;
		}
		
		//filtro por Recibo Tr
		if(indetSearchPage.getIndet().getReciboTr() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.reciboTr = " + indetSearchPage.getIndet().getReciboTr();
			flagAnd = true;
		}

		//filtro por CodTipoIndet
		if(indetSearchPage.getIndet().getCodIndet() != null){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.tipoIndet.codTipoIndet = '" + indetSearchPage.getIndet().getCodIndet().toString()+"'";
			flagAnd = true;
		}

		//	 filtro por Caja
 		if(indetSearchPage.getIndet().getCaja() != null){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.caja = " +  Long.valueOf(indetSearchPage.getIndet().getCaja());
			flagAnd = true;
		}
			
 		// Order By
		queryString += " order by t.id";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Indeterminado> listIndeterminado = (ArrayList<Indeterminado>) executeCountedSearch(queryString, indetSearchPage);
		
		List<IndetVO> listIndetVO = new ArrayList<IndetVO>();
		for(Indeterminado indeterminado: listIndeterminado){
			listIndetVO.add(indeterminado.toIndetVO());
		}
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listIndetVO;
	}

	
	 /**
     * Realiza un count sobre la tabla "bal_indeterminado" con los criterios recibidos.
     * Si el recultados es mayor que cero devuelve true, sino false 
     * 
     * (Para utilizarse del IndeterminadoFacade y reemplazar la llamada del IndeterminadoJDBCDAO)
     * 
     * @param nroSistema
     * @param nroComprobante
     * @param clave
     * @param resto
     * @return true o false
     * @throws DemodaServiceException
     */
	public boolean getEsIndeterminada(String nroSistema, String nroComprobante, String clave, String resto) throws Exception {
	String funcName = "getEsIndeterminada()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
		String queryString = "from Indeterminado t where ";
		queryString += " t.sistema = "+nroSistema;  
		queryString += " and t.nroComprobante = "+nroComprobante;
		queryString += " and t.clave = "+clave;
		queryString += " and t.resto = "+resto;
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		Indeterminado indeterminado= (Indeterminado) query.uniqueResult();	
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		if(indeterminado != null)
			return true;
		else
			return false;	    	 
    }

    
   public Map<String, IndetVO> getMapAllIndet() throws Exception {        

    	/* bal_indeterminado */
    	// Encontramos que es comun que haya indeterminados con mismo anio, resto, periodo y clave,
    	// por esto los agrupamos y nos quedamos con el de menor fecha de pago, ya que es lo que le conviene
    	// al saldo por caducidad.

	    String queryString = "from Indeterminado t order by t.fechaPago";
				
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		Map<String, IndetVO> ret = new HashMap<String, IndetVO>();
		
		List<Indeterminado> list = (ArrayList<Indeterminado>) query.list();
		for(Indeterminado indeterminado : list) {
			String s;
			s = IndeterminadoCache.formatKey(
					indeterminado.getSistema(),
					indeterminado.getNroComprobante().toString(),
					indeterminado.getClave(),
					indeterminado.getResto());
			ret.put(s, indeterminado.toIndetVO());
		}

    	/*bal_reingIndet*/
		queryString = "from ReingIndet t where t.fechaReingreso is null ";
		queryString += " or t.fechaReingreso = " + sqlDate(new Date());
		queryString += " order by t.fechaPago";
		
		session = SiatHibernateUtil.currentSession();
		query = session.createQuery(queryString);
		
		List<ReingIndet> listReingIndet = (ArrayList<ReingIndet>) query.list();
		for(ReingIndet reingIndet : listReingIndet) {
			String s;
			s = IndeterminadoCache.formatKey(
					reingIndet.getSistema(),
					reingIndet.getNroComprobante().toString(),
					reingIndet.getClave(),
					reingIndet.getResto());
			ret.put(s, reingIndet.toIndetVO());
		}
    		
    	if (log.isDebugEnabled()) log.debug("getMapAllIndet(): exit");
    	return ret;
    }

   /**
    * Filtra en bal_indeterminado por haciendo un select in listaNroComprobante, y clave.
    * Si clave es null, o "" no se agrega a los filtros y solo filtra por nroComprobante
    * @param listCuentaO
    * @param claveO
    * @return
    * @throws Exception
    */
	public List<IndetVO> getListIndetByListNroComprobanteClave(List<Long> listNroComprobante, String clave) throws Exception {

	   	String funcName = "getListIndetByListNroComprobanteClave()";
	
	   	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	
	   	List<IndetVO> listIndet = new ArrayList<IndetVO>();
		
	    String queryString = "from Indeterminado t where ";  
	    Long ultimoItem = listNroComprobante.get(listNroComprobante.size()-1);
	    for(Long item: listNroComprobante){
	    	queryString += " t.nroComprobante = " + item ;
	    	if(!item.equals(ultimoItem)){
	    		queryString += " or ";
	    	}
	    }
	    Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		List<Indeterminado> list = (ArrayList<Indeterminado>) query.list();
		for(Indeterminado indeterminado : list) {
			listIndet.add(indeterminado.toIndetVO());
		}
	
	   	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	   	
		return listIndet;
	}
	
	
	public void imprimirGenerico(IndetVO indet, ReportVO report) {
		// TODO Auto-generated method stub

	}
}
