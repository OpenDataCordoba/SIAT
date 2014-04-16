//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.bean.SinIndet;
import ar.gov.rosario.siat.bal.buss.cache.IndeterminadoCache;
import ar.gov.rosario.siat.bal.iface.model.DupliceSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetReingSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetSearchPage;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.model.ReingresoAdapter;
import coop.tecso.demoda.buss.dao.JDBCConnManager;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.ReportVO;

public class IndeterminadoJDBCDAO {

	private static final String DS_NAME = "java:comp/env/ds/siat"; //"java:comp/env/ds/indet";
       
    private Logger log = Logger.getLogger(IndeterminadoJDBCDAO.class);
	
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public IndeterminadoJDBCDAO() {
		
	}
    
    /**
     * Realiza un count sobre la tabla "indet_tot" con los criterios recibidos.
     * Si el recultados es mayor que cero devuelve true, sino false 
     * 
     * @author Cristian
     * @param nroSistema
     * @param nroCuenta
     * @param nroClave
     * @param resto
     * @return true o false
     * @throws DemodaServiceException
     */
    public boolean getEsIndeterminada(String nroSistema, String nroCuenta, String nroClave, String resto) throws Exception {
        
    	String funcName = "getEsIndeterminada()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	boolean indet = false;
    	
    	String sSql = "select count(*) as c from bal_indeterminado " +	// Se reemplaza indet_tot 	por bal_indeterminado
    		" where sistema like '%" + nroSistema  + "' " +  			// Se reemplaza sist_o	 	por sistema
    		" and  nroComprobante like '%" + nroCuenta +  "' " +		// Se reemplaza cuenta_o	por nroComprobante
    		" and  clave like '%" + nroClave +  "' " +					// Se reemplaza clave_o		por clave
    		" and  resto like '%" + resto + "' ";						// Se reemplaza resto_o		por resto

    	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	pstmt = con.prepareStatement(sSql);
    	rs = pstmt.executeQuery();

    	while (rs.next()) {        		
    		if (rs.getInt("c") > 0 )
    			indet = true;
    		else 
    			indet = false;
    	}

    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	return indet;
    }

    
    private IndetVO loadIndetVOFromIndetTot(ResultSet rs, IndetVO indetVO) throws Exception {
    	if (rs.getString("fechaPago") != null) {
    		indetVO.setFechaPago(DateUtil.getDate(rs.getString("fechaPago"), DateUtil.ddMMYYYY_MASK));
    	} else {
    		indetVO.setFechaPago(null);
    	}
    	return indetVO;
    }
    
    private IndetVO loadIndetVOFromReingreso(ResultSet rs, IndetVO indetVO) throws Exception {
    	if (rs.getString("fechaPago") != null) {
    		indetVO.setFechaPago(rs.getDate("fechaPago"));
    	} else {
    		indetVO.setFechaPago(null);
    	}
    	return indetVO;
    }
    
    public Map<String, IndetVO> getMapAllIndet() throws Exception {        
        Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
  	
    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);
    	/*Indet tot */
    	// Encontramos que es comun que haya indeterminados con mismo anio, resto, periodo y clave,
    	// por esto los agrupamos y nos quedamos con el de menor fecha de pago, ya que es lo que le conviene
    	// al saldo por caducidad.
    	String sql = " select sistema, nroComprobante, clave, resto, min(fechaPago) fechaPago "; 			// Se reemplaza sist_o por sistema, cuenta_o por nroComprobante, clave_o por clave, resto_o	por resto
    	sql += " from bal_indeterminado ";																// Se reemplaza indet_tot 	por bal_indeterminado
    	sql += " group by sistema, nroComprobante, clave, resto ";
    	
    	if (log.isDebugEnabled()) log.debug("getMapAllIndet - Query: " + sql);
    	pstmt = con.prepareStatement(sql);
    	rs = pstmt.executeQuery();

    	Map<String, IndetVO> ret = new HashMap<String, IndetVO>();
    	while (rs.next()) {
    		String s;
    		try {
    			s = IndeterminadoCache.formatKey(
    					rs.getString("sistema"),
    					rs.getString("nroComprobante"),
    					rs.getString("clave"),
    					rs.getString("resto"));
    			ret.put(s, loadIndetVOFromIndetTot(rs, new IndetVO()));
			} catch(java.lang.NumberFormatException e) {
    			log.warn("Indeterminados: Error en formato de registro en bal_indeterminado. Este registro sera ignorado:" + 
    					rs.getString("sistema") + "-" + 
    					rs.getString("nroComprobante") + "-" + 
    					rs.getString("clave") + "-" + 
    					rs.getString("resto"));
    		} catch(Exception e) {
    			log.warn("Indeterminados: Error en formato de registro en bal_indeterminado. Este registro sera ignorado:" + 
    					rs.getString("sistema") + "-" + 
    					rs.getString("nroComprobante") + "-" + 
    					rs.getString("clave") + "-" + 
						rs.getString("resto") + " Error: ", e);
    		}
    	}
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	

    	/*bal_reingIndetresos*/
    	try {
    		String fechaHoy = DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
    		//sql = "select sistema, nroComprobante, clave_r, resto_r from reingresos " + 
    		//" where  fechaReing is null " +  
    		//" or fechaReing = TO_DATE('" + fechaHoy + "','%d/%m/%Y')";

        	// Encontramos que es comun que haya indeterminados con mismo anio, resto, periodo y clave,
        	// por esto los agrupamos y nos quedamos con el de menor fecha de pago, ya que es lo que le conviene
        	// al saldo por caducidad.
        	sql = " select sistema, nroComprobante, clave_r, resto_r, min(fpago_r) fechaPago from reingresos " + 
        		" where  fechaReing is null " +  
        		" or fechaReing = TO_DATE('" + fechaHoy + "','%d/%m/%Y')" +
        		" group by sistema, nroComprobante, clave_r, resto_r ";
    		
    		if (log.isDebugEnabled()) log.debug("getMapAllIndet() - Query: " + sql);
    		pstmt = con.prepareStatement(sql);
    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    			String s;
    			try {
    				s = IndeterminadoCache.formatKey(
    						rs.getString("sistema"),
    						rs.getString("nroComprobante"),
    						rs.getString("clave_r"),
    						rs.getString("resto_r"));
        			ret.put(s, loadIndetVOFromReingreso(rs, new IndetVO()));
    			} catch(java.lang.NumberFormatException e) {
    				log.warn("Indeterminados: Error en formato de registro en bal_reingIndet. Este registro sera ignorado:" + 
    						rs.getString("sistema") + "-" + 
    						rs.getString("nroComprobante") + "-" + 
    						rs.getString("clave_r") + "-" + 
    						rs.getString("resto_r"));
    			} catch(Exception e) {
    				log.warn("Indeterminados: Error en formato de registro en bal_reingIndet. Este registro sera ignorado:" + 
    						rs.getString("sistema") + "-" + 
    						rs.getString("nroComprobante") + "-" + 
    						rs.getString("clave_r") + "-" + 
    						rs.getString("resto_r") + " Error: ", e);
    			}
    		}
    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}

    	} catch (Exception ex) {
    		log.error("Fallo carga de bal_reingIndet en cache.", ex);
    	}

    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug("getMapAllIndet(): exit");
    	return ret;
    }

    
    /**
     * Lee toda la tabla "bal_indeterminado".
     */
    public List<String> getAllIndetTot() throws Exception {
        
    	String funcName = "loadIndeterminados()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
  	

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	/*Indet tot */
    	String sql = "select sistema, nroComprobante, clave, resto from bal_indeterminado";
    	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
    	pstmt = con.prepareStatement(sql);
    	rs = pstmt.executeQuery();

    	List<String> ret = new ArrayList<String>();
    	while (rs.next()) {
    		String s;
    		try {
    			s = IndeterminadoCache.formatKey(
    					rs.getString("sistema"),
    					rs.getString("nroComprobante"),
    					rs.getString("clave"),
    					rs.getString("resto"));
    			ret.add(s);
			} catch(java.lang.NumberFormatException e) {
    			log.warn("Indeterminados: Error en formato de registro en bal_indeterminado. Este registro sera ignorado:" + 
    					rs.getString("sistema") + "-" + 
    					rs.getString("nroComprobante") + "-" + 
    					rs.getString("clave") + "-" + 
    					rs.getString("resto"));
    		} catch(Exception e) {
    			log.warn("Indeterminados: Error en formato de registro en bal_indeterminado. Este registro sera ignorado:" + 
    					rs.getString("sistema") + "-" + 
    					rs.getString("nroComprobante") + "-" + 
    					rs.getString("clave") + "-" + 
						rs.getString("resto") + " Error: ", e);
    		}
    	}
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	

    	/*bal_reingIndet*/
    	try {
    		String fechaHoy = DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
    		sql = "select sistema, nroComprobante, clave_r, resto_r from bal_reingIndet " + 
    		" where  fechaReing is null " +  
    		" or fechaReing = TO_DATE('" + fechaHoy + "','%d/%m/%Y')";


    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
    		pstmt = con.prepareStatement(sql);
    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    			String s;
    			try {
    				s = IndeterminadoCache.formatKey(
    						rs.getString("sistema"),
    						rs.getString("nroComprobante"),
    						rs.getString("clave_r"),
    						rs.getString("resto_r"));
    				ret.add(s);
    			} catch(java.lang.NumberFormatException e) {
    				log.warn("Indeterminados: Error en formato de registro en bal_reingIndet. Este registro sera ignorado:" + 
    						rs.getString("sistema") + "-" + 
    						rs.getString("nroComprobante") + "-" + 
    						rs.getString("clave_r") + "-" + 
    						rs.getString("resto_r"));
    			} catch(Exception e) {
    				log.warn("Indeterminados: Error en formato de registro en bal_reingIndet. Este registro sera ignorado:" + 
    						rs.getString("sistema") + "-" + 
    						rs.getString("nroComprobante") + "-" + 
    						rs.getString("clave_r") + "-" + 
    						rs.getString("resto_r") + " Error: ", e);
    			}
    		}
    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}

    	} catch (Exception ex) {
    		log.error("Fallo carga de bal_reingIndet en cache.", ex);
    	}

    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	return ret;
    }
    
    /**
     *  Obtiene un registro de Indeterminado y devuelve un Bean SinIndet con los datos.
     * 
     * @param nroSistema
     * @param nroCuenta
     * @param nroClave
     * @param resto
     * @return sinIndet
     * @throws Exception
     */
    public SinIndet getIndeterminada(String nroSistema, String nroCuenta, String nroClave, String resto) throws Exception {

    	String funcName = "getEsIndeterminada()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	SinIndet sinIndet = new SinIndet();
    	boolean existeIndetTot = false;
    	if (StringUtil.isNullOrEmpty(resto))resto="-1";


    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		String sSql = "select * from bal_indeterminado " +
    		" where sistema *1 = " + nroSistema  +  
    		" and  nroComprobante * 1 = " + nroCuenta +
    		" and  clave * 1 = " + nroClave +
    		" and  (resto * 1 = " + resto +
    		" or resto is null or resto = '')";

    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
    		pstmt = con.prepareStatement(sSql);
    		rs = pstmt.executeQuery();

    		while (rs.next()) {
    			existeIndetTot = true;
    			sinIndet.setSistema(rs.getLong("sistema"));
    			sinIndet.setNroComprobante(rs.getLong("nroComprobante"));
    			sinIndet.setClave(rs.getString("clave"));
    			if(rs.getString("resto") == null || "".equals(rs.getString("resto").trim())){
    				sinIndet.setResto(0L);
    			}else{
    				sinIndet.setResto(rs.getLong("resto"));    			
    			}
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				sinIndet.setImporteCobrado(0D);
    			}else{  			
    				sinIndet.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("importeBasico") == null || "".equals(rs.getString("importeBasico").trim())){
    				sinIndet.setBasico(0D);
    			}else{  			
    				sinIndet.setBasico(rs.getDouble("importeBasico"));
    			}
    			if(rs.getString("importeCalculado") == null || "".equals(rs.getString("importeCalculado").trim())){
    				sinIndet.setCalculado(0D);
    			}else{  			
    				sinIndet.setCalculado(rs.getDouble("importeCalculado"));
    			}
    			if(rs.getString("indice") == null || "".equals(rs.getString("indice").trim())){
    				sinIndet.setIndice(0D);
    			}else{  			
    				sinIndet.setIndice(rs.getDouble("indice"));
    			}
    			if(rs.getString("recargo") == null || "".equals(rs.getString("recargo").trim())){
    				sinIndet.setRecargo(0D);
    			}else{  			
    				sinIndet.setRecargo(rs.getDouble("recargo"));
    			}
    			if(rs.getString("partida") == null || "".equals(rs.getString("partida").trim())){
    				sinIndet.setPartida(null);
    			}else{  			
    				sinIndet.setPartida(Partida.getByIdNull(rs.getLong("partida")));
    			}
    			sinIndet.setCodIndeterminado(rs.getString("codindet"));
    			if(rs.getString("fechaPago") == null || "".equals(rs.getString("fechaPago").trim())){
    				sinIndet.setFechaPago(null);
    			}else{  			
    				sinIndet.setFechaPago(DateUtil.getDate(rs.getString("fechaPago"), DateUtil.ddMMYYYY_MASK));
    			}
    			if(rs.getString("caja") == null || "".equals(rs.getString("caja").trim())){
    				sinIndet.setCaja(0L);
    			}else{  			
    				sinIndet.setCaja(rs.getLong("caja"));
    			}
    			if(rs.getString("paquete_o") == null || "".equals(rs.getString("paquete_o").trim())){
    				sinIndet.setPaquete(0L);
    			}else{  			
    				sinIndet.setPaquete(rs.getLong("paquete_o"));
    			}
    			if(rs.getString("codpago_o") == null || "".equals(rs.getString("codpago_o").trim())){
    				sinIndet.setCodPago(0L);
    			}else{  			
    				sinIndet.setCodPago(rs.getLong("codpago_o"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				sinIndet.setFechaBalance(null);
    			}else{  			
    				sinIndet.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.ddMMYYYY_MASK));
    			}
    			sinIndet.setFiller(rs.getString("fi_o"));
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				sinIndet.setReciboTr(0L);
    			}else{  			
    				sinIndet.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			break;
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de indeterminado en bal_indeterminado.", ex);
    	}

    	try {
    		if (!existeIndetTot) {
    			//lo buscamos en bal_reingIndet.
    			String fechaHoy = DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
    			String sql = "select * from bal_reingIndet " +
    			" where sistema *1 = " + nroSistema  +  
    			" and  nroComprobante * 1 = " + nroCuenta +
    			" and  clave_r * 1 = " + nroClave +
    			" and (resto_r * 1 = " + resto + " or resto_r is null or resto_r = '')  " +
    			" and (fechaReing is null or fechaReing = TO_DATE('" + fechaHoy + "','%d/%m/%Y'))";

    			pstmt = con.prepareStatement(sql);
    			rs = pstmt.executeQuery();
    			while (rs.next()) {
    				existeIndetTot = true;
    				sinIndet.setSistema(rs.getLong("sistema"));
    				sinIndet.setNroComprobante(rs.getLong("nroComprobante"));
    				sinIndet.setClave(rs.getString("clave_r"));
    				if(rs.getString("resto_r") == null || "".equals(rs.getString("resto_r").trim())){
    					sinIndet.setResto(0L);
    				} else{
    					sinIndet.setResto(rs.getLong("resto_r"));    			
    				}
    				if(rs.getString("fpago_r") == null || "".equals(rs.getString("fpago_r").trim())){
    					sinIndet.setFechaPago(null);
    				}else{  			
    					sinIndet.setFechaPago(DateUtil.getDate(rs.getString("fpago_r"), DateUtil.ddMMYYYY_MASK));
    				}
    			}

    			try {rs.close();}    catch (Exception ex) {}
    			try {pstmt.close();} catch (Exception ex) {}
    		}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de indeterminado en reingreso.", ex);
    	}

    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	return sinIndet; 
    }
    
    /**
     * Filtra en bal_indeterminado por haciendo un select in listaCuentaO, y claveO.
     * Si claveO es null, o "" no se agrega a los filtros y solo filtra por nroComprobante
     * @param listCuentaO
     * @param claveO
     * @return
     * @throws Exception
     */
	public List<IndetVO> getListIndetByListCuentaOClaveO(List<Long> listCuentaO, String claveO) throws Exception {

    	String funcName = "getListIndetByListCuentaOClaveO()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	IndetVO indet = new IndetVO();
    	List<IndetVO> listIndet = new ArrayList<IndetVO>();
    	//boolean existeIndetTot = false;
 
    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		String sSql = "select * from bal_indeterminado " +
    		" where ";  
    		Long ultimoItem = listCuentaO.get(listCuentaO.size()-1);
    		for(Long item: listCuentaO){
    			sSql += " nroComprobante like '%" + item.toString() + "'";
    			if(!item.equals(ultimoItem)){
    				sSql += " or ";
    			}
    		}
    		/*if(claveO != null && !"".equals(claveO)){
    			sSql += " and  clave = '" + claveO + "'";    			
    		}*/

    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
    		pstmt = con.prepareStatement(sSql);
    		rs = pstmt.executeQuery();

    		while (rs.next()) {
    			indet = new IndetVO();
    			//existeIndetTot = true;
    			indet.setSistema(rs.getString("sistema"));
    			indet.setNroComprobante(rs.getString("nroComprobante"));
    			indet.setClave(rs.getString("clave"));
   				indet.setResto(rs.getString("resto"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				indet.setImporteCobrado(0D);
    			}else{  			
    				indet.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("importeBasico") == null || "".equals(rs.getString("importeBasico").trim())){
    				indet.setImporteBasico(0D);
    			}else{  			
    				indet.setImporteBasico(rs.getDouble("importeBasico"));
    			}
    			if(rs.getString("importeCalculado") == null || "".equals(rs.getString("importeCalculado").trim())){
    				indet.setImporteCalculado(0D);
    			}else{  			
    				indet.setImporteCalculado(rs.getDouble("importeCalculado"));
    			}
    			if(rs.getString("indice") == null || "".equals(rs.getString("indice").trim())){
    				indet.setIndice(0D);
    			}else{  			
    				indet.setIndice(rs.getDouble("indice"));
    			}
    			if(rs.getString("recargo") == null || "".equals(rs.getString("recargo").trim())){
    				indet.setRecargo(0D);
    			}else{  			
    				indet.setRecargo(rs.getDouble("recargo"));
    			}
    			indet.setPartida(rs.getString("partida"));
    			indet.setCodIndet(rs.getInt("codindet"));
    			if(rs.getString("fechaPago") == null || "".equals(rs.getString("fechaPago").trim())){
    				indet.setFechaPago(null);
    			}else{  			
    				indet.setFechaPago(DateUtil.getDate(rs.getString("fechaPago"), DateUtil.ddMMYYYY_MASK));
    			}
    			if(rs.getString("caja") == null || "".equals(rs.getString("caja").trim())){
    				indet.setCaja(0);
    			}else{  			
    				indet.setCaja(rs.getInt("caja"));
    			}
    			if(rs.getString("paquete_o") == null || "".equals(rs.getString("paquete_o").trim())){
    				indet.setPaquete(0);
    			}else{  			
    				indet.setPaquete(rs.getInt("paquete_o"));
    			}
    			if(rs.getString("codpago_o") == null || "".equals(rs.getString("codpago_o").trim())){
    				indet.setCodPago(0);
    			}else{  			
    				indet.setCodPago(rs.getInt("codpago_o"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				indet.setFechaBalance(null);
    			}else{  			
    				indet.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.ddMMYYYY_MASK));
    			}
    			indet.setFiller(rs.getString("fi_o"));
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				indet.setReciboTr(0L);
    			}else{  			
    				indet.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			indet.setNroIndeterminado(rs.getLong("id"));
    			indet.setTipoIngreso(rs.getInt("tpo_ingreso"));
    			indet.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				indet.setFechaHora(null);
    			}else{  			
    				indet.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			listIndet.add(indet);
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de indeterminado en bal_indeterminado.", ex);
    	}


    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return listIndet;
	}

	/** En este caso el searchPage retorna una lista de VO porque los indetermiandos estan en otra DB
	 * y por suerte no tenemos mapeos de hibernate a otras DB
	 * @param indetSearchPage
	 * @return
	 * @throws Exception
	 */
	public List<IndetVO> getBySearchPage(IndetSearchPage indetSearchPage) throws Exception {

    	String funcName = "getBySearchPage()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	IndetVO indet = new IndetVO();
    	List<IndetVO> listIndet = new ArrayList<IndetVO>();
    	//boolean existeIndetTot = false;
 
    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    	
    		String sSql = " from bal_indeterminado ";
    	    boolean flagAnd = false;
       		// Filtros aqui
    		
    		// filtro por Sistema
     		if(!StringUtil.isNullOrEmpty(indetSearchPage.getIndet().getSistema())){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " sistema = '" +  indetSearchPage.getIndet().getSistema()+"'";
    			flagAnd = true;
    		}

    		// filtro por Cuenta
     		if(!StringUtil.isNullOrEmpty(indetSearchPage.getIndet().getNroComprobante())){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " nroComprobante like '%" +  indetSearchPage.getIndet().getNroComprobante()+"'";
    			flagAnd = true;
    		}

     		// filtro por ImporteCobrado
     		if(indetSearchPage.getIndet().getImporteCobrado() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " importe = " +  indetSearchPage.getIndet().getImporteCobrado();
    			flagAnd = true;
    		}

     		// 	 filtro por Fecha Balance 
    		if (indetSearchPage.getIndet().getFechaBalance() !=null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " fechaBalance = '" +DateUtil.formatDate(indetSearchPage.getIndet().getFechaBalance(), DateUtil.ddMMYYYY_MASK) + "'";
    	      flagAnd = true;
    		}

     		// 	 filtro por Fecha Pago
    		if (indetSearchPage.getIndet().getFechaPago()!=null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " fechaPago = '"+DateUtil.formatDate(indetSearchPage.getIndet().getFechaPago(), DateUtil.ddMMYYYY_MASK)+"'";
    	      flagAnd = true;
    		}
    		
    		//	 filtro por ReciboTr
     		if(indetSearchPage.getIndet().getReciboTr() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " reciboTr = " +  indetSearchPage.getIndet().getReciboTr();
    			flagAnd = true;
    		}

    		//	 filtro por codIndeterminado
     		if(indetSearchPage.getIndet().getCodIndet() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " codindet = " +  indetSearchPage.getIndet().getCodIndet();
    			flagAnd = true;
    		}

    		//	 filtro por Paquete
     		if(indetSearchPage.getIndet().getPaquete() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " paquete_o = " +  indetSearchPage.getIndet().getPaquete();
    			flagAnd = true;
    		}

    		//	 filtro por Caja
     		if(indetSearchPage.getIndet().getCaja() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " caja = " +  indetSearchPage.getIndet().getCaja();
    			flagAnd = true;
    		}

    		// Order By
    		sSql += " order by id";

    	    Long cantidadMaxima;
			String queryCount = sSql;
			
			//int posFrom = queryCount.toLowerCase().indexOf("from");
			
			/*if (posFrom > 1){
				queryCount = queryCount.substring(posFrom, sSql.length()-1);
			}*/
			
			int pos = queryCount.toLowerCase().indexOf("order by");
			if (pos >= 0) {
				queryCount = queryCount.substring(0, pos);
			}

			pstmt = con.prepareStatement("select count(*) " + queryCount);
    		rs = pstmt.executeQuery();

    		cantidadMaxima = 0L;
    		while (rs.next()) {
    			cantidadMaxima = new Long(rs.getInt(1));
    			break;
    		}
    	    	
    		indetSearchPage.setMaxRegistros(cantidadMaxima);
	
		    
			/*if (indetSearchPage.isPaged()) {
				sSql = "select skip "+indetSearchPage.getFirstResult()+" first "+indetSearchPage.getRecsByPage().intValue()+" * "+sSql;
			}else{
				sSql = "select * "+sSql;				
			}*/
    		// Cambiamos la paginacion porque la base de indeterminados corre en una version anterior de informix y 
    		// no soporte el skip first.
    		sSql = "select * "+sSql;	
    		
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
			log.info(funcName + " - Query: " + sSql);
			// obtenemos el resultado de la consulta
			pstmt = con.prepareStatement(sSql);
			rs = pstmt.executeQuery();
    		
			long skip = 0;
			long first = 1;
    		while (rs.next()) {
    			if (indetSearchPage.isPaged() && skip < indetSearchPage.getFirstResult()) {
    				skip++;
    				continue;
    			}
    			indet = new IndetVO();
    			//existeIndetTot = true;
    			indet.setSistema(rs.getString("sistema"));
    			indet.setNroComprobante(rs.getString("nroComprobante"));
    			indet.setClave(rs.getString("clave"));
   				indet.setResto(rs.getString("resto"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				indet.setImporteCobrado(0D);
    			}else{  			
    				indet.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("importeBasico") == null || "".equals(rs.getString("importeBasico").trim())){
    				indet.setImporteBasico(0D);
    			}else{  			
    				indet.setImporteBasico(rs.getDouble("importeBasico"));
    			}
    			if(rs.getString("importeCalculado") == null || "".equals(rs.getString("importeCalculado").trim())){
    				indet.setImporteCalculado(0D);
    			}else{  			
    				indet.setImporteCalculado(rs.getDouble("importeCalculado"));
    			}
    			if(rs.getString("indice") == null || "".equals(rs.getString("indice").trim())){
    				indet.setIndice(0D);
    			}else{  			
    				indet.setIndice(rs.getDouble("indice"));
    			}
    			if(rs.getString("recargo") == null || "".equals(rs.getString("recargo").trim())){
    				indet.setRecargo(0D);
    			}else{  			
    				indet.setRecargo(rs.getDouble("recargo"));
    			}
    			indet.setPartida(rs.getString("partida"));
    			indet.setCodIndet(rs.getInt("codindet"));
    			if(rs.getString("fechaPago") == null || "".equals(rs.getString("fechaPago").trim())){
    				indet.setFechaPago(null);
    			}else{  			
    				indet.setFechaPago(DateUtil.getDate(rs.getString("fechaPago"), DateUtil.ddMMYYYY_MASK));
    			}
    			if(rs.getString("caja") == null || "".equals(rs.getString("caja").trim())){
    				indet.setCaja(0);
    			}else{  			
    				indet.setCaja(rs.getInt("caja"));
    			}
    			if(rs.getString("paquete_o") == null || "".equals(rs.getString("paquete_o").trim())){
    				indet.setPaquete(0);
    			}else{  			
    				indet.setPaquete(rs.getInt("paquete_o"));
    			}
    			if(rs.getString("codpago_o") == null || "".equals(rs.getString("codpago_o").trim())){
    				indet.setCodPago(0);
    			}else{  			
    				indet.setCodPago(rs.getInt("codpago_o"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				indet.setFechaBalance(null);
    			}else{  			
    				indet.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.ddMMYYYY_MASK));
    			}
    			indet.setFiller(rs.getString("fi_o"));
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				indet.setReciboTr(0L);
    			}else{  			
    				indet.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			indet.setNroIndeterminado(rs.getLong("id"));
    			indet.setTipoIngreso(rs.getInt("tpo_ingreso"));
    			indet.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				indet.setFechaHora(null);
    			}else{  			
    				indet.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			listIndet.add(indet);
    			if (indetSearchPage.isPaged()) {
    				first++;
    				if(first > indetSearchPage.getRecsByPage())
    					break;
    			}
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de indeterminado en bal_indeterminado.", ex);
    	}


    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return listIndet;
	}
	
	/**
	 * Recupera un intet_tot por nroIndeterminado
	 * @param nroIndeterminado
	 * @return
	 * @throws Exception
	 */
	public IndetVO getById(Long nroIndeterm) throws Exception {

    	String funcName = "getById(Long nroIndeterm)";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	IndetVO indet = new IndetVO();
 
    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		String sSql = "select * from bal_indeterminado " +
    		" where id = " + nroIndeterm;
    			
    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
    		pstmt = con.prepareStatement(sSql);
    		rs = pstmt.executeQuery();

    		while (rs.next()) {
    			indet = new IndetVO();
    			indet.setSistema(rs.getString("sistema"));
    			indet.setNroComprobante(rs.getString("nroComprobante"));
    			indet.setClave(rs.getString("clave"));
   				indet.setResto(rs.getString("resto"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				indet.setImporteCobrado(0D);
    			}else{  			
    				indet.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("importeBasico") == null || "".equals(rs.getString("importeBasico").trim())){
    				indet.setImporteBasico(0D);
    			}else{  			
    				indet.setImporteBasico(rs.getDouble("importeBasico"));
    			}
    			if(rs.getString("importeCalculado") == null || "".equals(rs.getString("importeCalculado").trim())){
    				indet.setImporteCalculado(0D);
    			}else{  			
    				indet.setImporteCalculado(rs.getDouble("importeCalculado"));
    			}
    			if(rs.getString("indice") == null || "".equals(rs.getString("indice").trim())){
    				indet.setIndice(0D);
    			}else{  			
    				indet.setIndice(rs.getDouble("indice"));
    			}
    			if(rs.getString("recargo") == null || "".equals(rs.getString("recargo").trim())){
    				indet.setRecargo(0D);
    			}else{  			
    				indet.setRecargo(rs.getDouble("recargo"));
    			}
    			indet.setPartida(rs.getString("partida"));
    			indet.setCodIndet(rs.getInt("codindet"));
    			if(rs.getString("fechaPago") == null || "".equals(rs.getString("fechaPago").trim())){
    				indet.setFechaPago(null);
    			}else{  			
    				indet.setFechaPago(DateUtil.getDate(rs.getString("fechaPago"), DateUtil.ddMMYYYY_MASK));
    			}
    			if(rs.getString("caja") == null || "".equals(rs.getString("caja").trim())){
    				indet.setCaja(0);
    			}else{  			
    				indet.setCaja(rs.getInt("caja"));
    			}
    			if(rs.getString("paquete_o") == null || "".equals(rs.getString("paquete_o").trim())){
    				indet.setPaquete(0);
    			}else{  			
    				indet.setPaquete(rs.getInt("paquete_o"));
    			}
    			if(rs.getString("codpago_o") == null || "".equals(rs.getString("codpago_o").trim())){
    				indet.setCodPago(0);
    			}else{  			
    				indet.setCodPago(rs.getInt("codpago_o"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				indet.setFechaBalance(null);
    			}else{  			
    				indet.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.ddMMYYYY_MASK));
    			}
    			indet.setFiller(rs.getString("fi_o"));
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				indet.setReciboTr(0L);
    			}else{  			
    				indet.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			indet.setNroIndeterminado(rs.getLong("id"));
    			indet.setTipoIngreso(rs.getInt("tpo_ingreso"));
    			indet.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				indet.setFechaHora(null);
    			}else{  			
    				indet.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			break;
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de indeterminado en bal_indeterminado.", ex);
    	}

    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return indet;
	}

	public void imprimirGenerico(IndetVO indet, ReportVO report) {
		// TODO Auto-generated method stub
		
	}
	
	public IndetVO createIndet(IndetVO indet) throws Exception{

    	String funcName = "createIndet()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps = null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    	
			long nroIndeterminado = 0;
			String sql = "";
			sql += " insert into bal_indeterminado"; 
			sql += " (sistema, nroComprobante, clave, resto, importe,";
			sql += " importeBasico, importeCalculado, indice, recargo, partida, codindet,";
			sql += " fechaPago, caja, paquete_o, codpago_o, fechaBalance, fi_o, reciboTr, tpo_ingreso,";
			sql += " usuarioUltMdf, fechaUltMdf)";
			sql += " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
			
			// insert bal_indeterminado
			ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, indet.getSistema());
			ps.setString(2, indet.getNroComprobante());
			ps.setString(3, indet.getClave());
			ps.setString(4, indet.getResto());
			if(indet.getImporteCobrado() != null)
				ps.setDouble(5, indet.getImporteCobrado());
			else
				ps.setDouble(5, 0D);
			if(indet.getImporteBasico() != null)
				ps.setDouble(6, indet.getImporteBasico());
			else
				ps.setDouble(6, 0D);
			if(indet.getImporteCalculado() != null)
				ps.setDouble(7, indet.getImporteCalculado());
			else
				ps.setDouble(7, 0D);
			if(indet.getIndice() != null)
				ps.setDouble(8, indet.getIndice());
			else
				ps.setDouble(8, 0D);
			if(indet.getRecargo() != null)
				ps.setDouble(9, indet.getRecargo());
			else
				ps.setDouble(9, 0D);
			ps.setString(10, indet.getPartida());
			ps.setInt(11, indet.getCodIndet());
			ps.setString(12, DateUtil.formatDate(indet.getFechaPago(), DateUtil.ddMMYYYY_MASK));
			ps.setInt(13, indet.getCaja());
			ps.setInt(14, indet.getPaquete());
			ps.setInt(15, indet.getCodPago());
			ps.setString(16, DateUtil.formatDate(indet.getFechaBalance(), DateUtil.ddMMYYYY_MASK));
			ps.setString(17, indet.getFiller());
			ps.setLong(18, indet.getReciboTr());
			ps.setInt(19, indet.getTipoIngreso());
			String userName = DemodaUtil.currentUserContext().getUserName();
    		if (userName != null) {
    			ps.setString(20,userName);
    		} else { 
    			ps.setString(20,"siat");
			}
    		
			ps.setTimestamp(21, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			
			ps.executeUpdate();
			
			nroIndeterminado = 0;
			if(ps != null){		
	    		ResultSet rsId = ps.getGeneratedKeys();
	    		while (rsId.next()) {
	    			nroIndeterminado = rsId.getInt(1);	 
	    	    }
	    		rsId.close();
	    	}
			
			indet.setNroIndeterminado(nroIndeterminado);
		
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al insertar indeterminado en bal_indeterminado.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
    	return indet;
	}

	public IndetVO updateIndet(IndetVO indet) throws Exception{

    	String funcName = "updateIndet()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps = null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		
			String sql = "";
			sql += " update bal_indeterminado set sistema = ?, nroComprobante=?, clave=?, resto=?, importe=?,";
			sql += " importeBasico=?, importeCalculado=?, indice=?, recargo=?, partida=?, codindet=?,";
			sql += " fechaPago=?, caja=?, paquete_o=?, codpago_o=?, fechaBalance=?, fi_o=?, reciboTr=?, tpo_ingreso=?,";
			sql += " usuarioUltMdf=?, fechaUltMdf=? where id=?";

			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
			
			// update bal_indeterminado
			ps = con.prepareStatement(sql);
			ps.setString(1, indet.getSistema());
			ps.setString(2, indet.getNroComprobante());
			ps.setString(3, indet.getClave());
			ps.setString(4, indet.getResto());
			ps.setDouble(5, indet.getImporteCobrado());
			ps.setDouble(6, indet.getImporteBasico());
			ps.setDouble(7, indet.getImporteCalculado());
			ps.setDouble(8, indet.getIndice());
			ps.setDouble(9, indet.getRecargo());
			ps.setString(10, indet.getPartida());
			ps.setInt(11, indet.getCodIndet());
			ps.setString(12, DateUtil.formatDate(indet.getFechaPago(), DateUtil.ddMMYYYY_MASK));
			ps.setInt(13, indet.getCaja());
			ps.setInt(14, indet.getPaquete());
			ps.setInt(15, indet.getCodPago());
			ps.setString(16, DateUtil.formatDate(indet.getFechaBalance(), DateUtil.ddMMYYYY_MASK));
			ps.setString(17, indet.getFiller());
			ps.setLong(18, indet.getReciboTr());
			ps.setInt(19, indet.getTipoIngreso());
			String userName = DemodaUtil.currentUserContext().getUserName();
    		if (userName != null) {
    			ps.setString(20,userName);
    		} else { 
    			ps.setString(20,"siat");
			}

			ps.setTimestamp(21, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			
			ps.setLong(22, indet.getNroIndeterminado());
			
			ps.executeUpdate();
			
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al modificar indeterminado en bal_indeterminado.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
    	return indet;
	}

	public void deleteIndet(IndetVO indet) throws Exception{

    	String funcName = "deleteIndet()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps = null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		
			String sql = " delete from bal_indeterminado where id=?";
			
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
			// delete bal_indeterminado
			ps = con.prepareStatement(sql);
		
			ps.setLong(1, indet.getNroIndeterminado());
			
			ps.executeUpdate();
			
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al eliminar indeterminado de bal_indeterminado.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
	}


	public IndetVO createReingreso(IndetVO indet) throws Exception{

    	String funcName = "createReingreso()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps = null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    	
			String sql = "";
			sql += " insert into bal_reingIndet"; 
			sql += " (sistema, nroComprobante, clave_r, resto_r, importe,";
			sql += " recargo_r, partida_r, codind_r, id, ";
			sql += " fpago_r, caja_r, paquete_r, codpago_r, fechaBalance, reciboTr, tpo_ingreso,";
			sql += " usuarioUltMdf, fechaUltMdf)";
			sql += " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			 
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
			
			// insert bal_indeterminado
			ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, new Integer(indet.getSistema().trim()));
			ps.setString(2, indet.getNroComprobante());
			ps.setString(3, indet.getClave());
			if(!StringUtil.isNullOrEmpty(indet.getResto()))
				ps.setInt(4, new Integer(indet.getResto().trim()));				
			else
				ps.setInt(4, 0);
			if(indet.getImporteCobrado() != null)
				ps.setDouble(5, indet.getImporteCobrado());
			else
				ps.setDouble(5, 0D);
			if(indet.getRecargo() != null)
				ps.setDouble(6, indet.getRecargo());
			else
				ps.setDouble(6, 0D);
			ps.setLong(7, new Long(indet.getPartida()));
			ps.setInt(8, indet.getCodIndet());
			ps.setLong(9, new Long(indet.getNroIndeterminado()));
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(indet.getFechaPago()); 
			ps.setTimestamp(10, new Timestamp(gc.getTimeInMillis()));
			ps.setInt(11, indet.getCaja());
			ps.setInt(12, indet.getPaquete());
			ps.setInt(13, indet.getCodPago());
			gc.setTime(indet.getFechaBalance()); 
			ps.setTimestamp(14, new Timestamp(gc.getTimeInMillis()));
			ps.setLong(15, indet.getReciboTr());
			ps.setInt(16, indet.getTipoIngreso());
			String userName = DemodaUtil.currentUserContext().getUserName();
    		if (userName != null) {
    			ps.setString(17,userName);
    		} else { 
    			ps.setString(17,"siat");
			}

			ps.setTimestamp(18, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			
			ps.executeUpdate();
					
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al insertar indeterminado en bal_reingIndet.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
    	return indet;
	}
	
	public IndetVO updateReingreso(IndetVO indet) throws Exception{

    	String funcName = "updateReingreso()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps = null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
			String sql = "";
			sql += " update bal_reingIndet set sistema = ?, nroComprobante=?, clave_r=?, resto_r=?, importe=?,";
			sql += " recargo_r=?, partida_r=?, codind_r=?, id=?, fechaReing=?, fpago_r=?,";
			sql += " caja_r=?, paquete_r=?, codpago_r=?, fechaBalance=?, reciboTr=?, tpo_ingreso=?, usuarioUltMdf=?, fechaUltMdf=?";
			sql += " where id=?";
			
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
			
			// insert bal_indeterminado
			ps = con.prepareStatement(sql);
			ps.setInt(1, new Integer(indet.getSistema()));
			ps.setString(2, indet.getNroComprobante());
			ps.setString(3, indet.getClave());
			ps.setInt(4, new Integer(indet.getResto()));
			if(indet.getImporteCobrado() != null)
				ps.setDouble(5, indet.getImporteCobrado());
			else
				ps.setDouble(5, 0D);
			if(indet.getRecargo() != null)
				ps.setDouble(6, indet.getRecargo());
			else
				ps.setDouble(6, 0D);
			ps.setLong(7, new Long(indet.getPartida()));
			ps.setInt(8, indet.getCodIndet());
			ps.setLong(9, new Long(indet.getNroIndeterminado()));
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(indet.getFechaReing());
			ps.setTimestamp(10,  new Timestamp(gc.getTimeInMillis()));
			gc.setTime(indet.getFechaPago()); 
			ps.setTimestamp(11, new Timestamp(gc.getTimeInMillis()));
			ps.setInt(12, indet.getCaja());
			ps.setInt(13, indet.getPaquete());
			ps.setInt(14, indet.getCodPago());
			gc.setTime(indet.getFechaBalance()); 
			ps.setTimestamp(15, new Timestamp(gc.getTimeInMillis()));
			ps.setLong(16, indet.getReciboTr());
			ps.setInt(17, indet.getTipoIngreso());
			String userName = DemodaUtil.currentUserContext().getUserName();
    		if (userName != null) {
    			ps.setString(18,userName);
    		} else { 
    			ps.setString(18,"siat");
			}
    		
			ps.setTimestamp(19, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			
			ps.setLong(20, indet.getNroReing());
			
			ps.executeUpdate();
					
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al modificar indeterminado en bal_reingIndet.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
    	return indet;
	}
	
	/**
	 *  Registro en tabla bal_indetAudit, donde se guardan los datos originales de un indeterminado antes de modificarlo
	 *  efectivamente en bal_indeterminado
	 * 
	 * @param indet
	 * @return
	 * @throws Exception
	 */
	public IndetVO createIndetModif(IndetVO indet) throws Exception{

    	String funcName = "createIndetModif()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps = null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    	
			long nroIndeterminado = 0;
			String sql = "";
			sql += " insert into bal_indetAudit"; 
			sql += " (id, sistema, nroComprobante, clave, resto, importe,";
			sql += " importeBasico, importeCalculado, indice, recargo, partida, codindet,";
			sql += " fechaPago, caja, paquete_o, codpago_o, fechaBalance, fi_o, reciboTr, tpo_ingreso,";
			sql += " usuarioUltMdf, fechaUltMdf)";
			sql += " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
			
			// insert bal_indetAudit
			ps = con.prepareStatement(sql);
			ps.setInt(1, indet.getNroIndeterminado().intValue());
			ps.setString(2, indet.getSistema());
			ps.setString(3, indet.getNroComprobante());
			ps.setString(4, indet.getClave());
			ps.setString(5, indet.getResto());
			if(indet.getImporteCobrado() != null)
				ps.setDouble(6, indet.getImporteCobrado());
			else
				ps.setDouble(6, 0D);
			if(indet.getImporteBasico() != null)
				ps.setDouble(7, indet.getImporteBasico());
			else
				ps.setDouble(7, 0D);
			if(indet.getImporteCalculado() != null)
				ps.setDouble(8, indet.getImporteCalculado());
			else
				ps.setDouble(8, 0D);
			if(indet.getIndice() != null)
				ps.setDouble(9, indet.getIndice());
			else
				ps.setDouble(9, 0D);
			if(indet.getRecargo() != null)
				ps.setDouble(10, indet.getRecargo());
			else
				ps.setDouble(10, 0D);
			ps.setString(11, indet.getPartida());
			ps.setInt(12, indet.getCodIndet());
			ps.setString(13, DateUtil.formatDate(indet.getFechaPago(), DateUtil.ddMMYYYY_MASK));
			ps.setInt(14, indet.getCaja());
			ps.setInt(15, indet.getPaquete());
			ps.setInt(16, indet.getCodPago());
			ps.setString(17, DateUtil.formatDate(indet.getFechaBalance(), DateUtil.ddMMYYYY_MASK));
			ps.setString(18, indet.getFiller());
			ps.setLong(19, indet.getReciboTr());
			ps.setInt(20, indet.getTipoIngreso());
			ps.setString(21, indet.getUsuario());
			ps.setTimestamp(22, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			
			ps.executeUpdate();
			
			nroIndeterminado = 0;
			if(ps != null){		
	    		ResultSet rsId = ps.getGeneratedKeys();
	    		while (rsId.next()) {
	    			nroIndeterminado = rsId.getInt(1);	 
	    	    }
	    		rsId.close();
	    	}
			
			indet.setNroIndeterminado(nroIndeterminado);
		
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al insertar indeterminado en bal_indetAudit.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
    	return indet;
	}

	/** En este caso el searchPage retorna una lista de VO porque los indetermiandos estan en otra DB
	 * y por suerte no tenemos mapeos de hibernate a otras DB
	 * @param dupliceSearchPage
	 * @return
	 * @throws Exception
	 */
	public List<IndetVO> getBySearchPage(DupliceSearchPage dupliceSearchPage) throws Exception {

    	String funcName = "getBySearchPage()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	IndetVO indet = new IndetVO();
    	List<IndetVO> listIndet = new ArrayList<IndetVO>();
    	//boolean existeIndetTot = false;
 
    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    	
    		String sSql = " from bal_duplice ";
    	    boolean flagAnd = false;
       		// Filtros aqui
    		
    		// filtro por Sistema
     		if(!StringUtil.isNullOrEmpty(dupliceSearchPage.getDuplice().getSistema())){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " sistema = '" +  dupliceSearchPage.getDuplice().getSistema()+"'";
    			flagAnd = true;
    		}

    		// filtro por Cuenta
     		if(!StringUtil.isNullOrEmpty(dupliceSearchPage.getDuplice().getNroComprobante())){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " nroComprobante like '%" +  dupliceSearchPage.getDuplice().getNroComprobante()+"'";
    			flagAnd = true;
    		}

     		// filtro por ImporteCobrado
     		if(dupliceSearchPage.getDuplice().getImporteCobrado() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " importe = " +  dupliceSearchPage.getDuplice().getImporteCobrado();
    			flagAnd = true;
    		}

     		// 	 filtro por Fecha Balance 
    		if (dupliceSearchPage.getDuplice().getFechaBalance() !=null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " fechaBalance = '" +DateUtil.formatDate(dupliceSearchPage.getDuplice().getFechaBalance(), DateUtil.ddMMYYYY_MASK) + "'";
    	      flagAnd = true;
    		}

     		// 	 filtro por Fecha Pago
    		if (dupliceSearchPage.getDuplice().getFechaPago()!=null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " fechaPago = '"+DateUtil.formatDate(dupliceSearchPage.getDuplice().getFechaPago(), DateUtil.ddMMYYYY_MASK)+"'";
    	      flagAnd = true;
    		}

    		//	 filtro por ReciboTr
     		if(dupliceSearchPage.getDuplice().getReciboTr() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " reciboTr = " +  dupliceSearchPage.getDuplice().getReciboTr();
    			flagAnd = true;
    		}

    		//	 filtro por codIndeterminado
     		if(dupliceSearchPage.getDuplice().getCodIndet() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " codindet = " +  dupliceSearchPage.getDuplice().getCodIndet();
    			flagAnd = true;
    		}

    		//	 filtro por Paquete
     		if(dupliceSearchPage.getDuplice().getPaquete() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " paquete_o = " +  dupliceSearchPage.getDuplice().getPaquete();
    			flagAnd = true;
    		}

    		//	 filtro por Caja
     		if(dupliceSearchPage.getDuplice().getCaja() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " caja = " +  dupliceSearchPage.getDuplice().getCaja();
    			flagAnd = true;
    		}

    		// Order By
    		sSql += " order by nroduplicado";

    	    Long cantidadMaxima;
			String queryCount = sSql;
			
			//int posFrom = queryCount.toLowerCase().indexOf("from");
			
			/*if (posFrom > 1){
				queryCount = queryCount.substring(posFrom, sSql.length()-1);
			}*/
			
			int pos = queryCount.toLowerCase().indexOf("order by");
			if (pos >= 0) {
				queryCount = queryCount.substring(0, pos);
			}

			pstmt = con.prepareStatement("select count(*) " + queryCount);
    		rs = pstmt.executeQuery();

    		cantidadMaxima = 0L;
    		while (rs.next()) {
    			cantidadMaxima = new Long(rs.getInt(1));
    			break;
    		}
    	    	
    		dupliceSearchPage.setMaxRegistros(cantidadMaxima);
	
		    
			/*if (dupliceSearchPage.isPaged()) {
				sSql = "select skip "+dupliceSearchPage.getFirstResult()+" first "+dupliceSearchPage.getRecsByPage().intValue()+" * "+sSql;
			}else{
				sSql = "select * "+sSql;				
			}*/
			// Cambiamos la paginacion porque la base de indeterminados corre en una version anterior de informix y 
    		// no soporte el skip first.
    		sSql = "select * "+sSql;	
    		
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
			
			// obtenemos el resultado de la consulta
			pstmt = con.prepareStatement(sSql);
			rs = pstmt.executeQuery();
    		
			long skip = 1;
			long first = 1;
    		while (rs.next()) {
    			if (dupliceSearchPage.isPaged() && skip < dupliceSearchPage.getFirstResult()) {
    				skip++;
    				continue;
    			}
    			
    			indet = new IndetVO();
    			//existeIndetTot = true;
    			indet.setSistema(rs.getString("sistema"));
    			indet.setNroComprobante(rs.getString("nroComprobante"));
    			indet.setClave(rs.getString("clave"));
   				indet.setResto(rs.getString("resto"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				indet.setImporteCobrado(0D);
    			}else{  			
    				indet.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("importeBasico") == null || "".equals(rs.getString("importeBasico").trim())){
    				indet.setImporteBasico(0D);
    			}else{  			
    				indet.setImporteBasico(rs.getDouble("importeBasico"));
    			}
    			if(rs.getString("importeCalculado") == null || "".equals(rs.getString("importeCalculado").trim())){
    				indet.setImporteCalculado(0D);
    			}else{  			
    				indet.setImporteCalculado(rs.getDouble("importeCalculado"));
    			}
    			if(rs.getString("indice") == null || "".equals(rs.getString("indice").trim())){
    				indet.setIndice(0D);
    			}else{  			
    				indet.setIndice(rs.getDouble("indice"));
    			}
    			if(rs.getString("recargo") == null || "".equals(rs.getString("recargo").trim())){
    				indet.setRecargo(0D);
    			}else{  			
    				indet.setRecargo(rs.getDouble("recargo"));
    			}
    			indet.setPartida(rs.getString("partida"));
    			indet.setCodIndet(rs.getInt("codindet"));
    			if(rs.getString("fechaPago") == null || "".equals(rs.getString("fechaPago").trim())){
    				indet.setFechaPago(null);
    			}else{  			
    				indet.setFechaPago(DateUtil.getDate(rs.getString("fechaPago"), DateUtil.ddMMYYYY_MASK));
    			}
    			if(rs.getString("caja") == null || "".equals(rs.getString("caja").trim())){
    				indet.setCaja(0);
    			}else{  			
    				indet.setCaja(rs.getInt("caja"));
    			}
    			if(rs.getString("paquete_o") == null || "".equals(rs.getString("paquete_o").trim())){
    				indet.setPaquete(0);
    			}else{  			
    				indet.setPaquete(rs.getInt("paquete_o"));
    			}
    			if(rs.getString("codpago_o") == null || "".equals(rs.getString("codpago_o").trim())){
    				indet.setCodPago(0);
    			}else{  			
    				indet.setCodPago(rs.getInt("codpago_o"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				indet.setFechaBalance(null);
    			}else{  			
    				indet.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.ddMMYYYY_MASK));
    			}
    			indet.setFiller(rs.getString("fi_o"));
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				indet.setReciboTr(0L);
    			}else{  			
    				indet.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			indet.setNroIndeterminado(rs.getLong("nroduplicado"));
    			indet.setTipoIngreso(0);//rs.getInt("tpo_ingreso"));
    			indet.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				indet.setFechaHora(null);
    			}else{  			
    				indet.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			listIndet.add(indet);
    			if (dupliceSearchPage.isPaged()) {
    				first++;
    				if(first > dupliceSearchPage.getRecsByPage())
    					break;
    			}
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de duplices.", ex);
    	}


    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return listIndet;
	}

	/**
	 * Recupera un duplice por nroIndeterminado
	 * @param nroIndeterminado
	 * @return
	 * @throws Exception
	 */
	public IndetVO getDupliceById(Long nroIndeterm) throws Exception {

    	String funcName = "getDupliceById(Long nroIndeterm)";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	IndetVO indet = new IndetVO();
 
    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		String sSql = "select * from bal_duplice " +
    		" where nroduplicado = " + nroIndeterm;
    			
    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
    		pstmt = con.prepareStatement(sSql);
    		rs = pstmt.executeQuery();

    		while (rs.next()) {
    			indet = new IndetVO();
    			indet.setSistema(rs.getString("sistema"));
    			indet.setNroComprobante(rs.getString("nroComprobante"));
    			indet.setClave(rs.getString("clave"));
   				indet.setResto(rs.getString("resto"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				indet.setImporteCobrado(0D);
    			}else{  			
    				indet.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("importeBasico") == null || "".equals(rs.getString("importeBasico").trim())){
    				indet.setImporteBasico(0D);
    			}else{  			
    				indet.setImporteBasico(rs.getDouble("importeBasico"));
    			}
    			if(rs.getString("importeCalculado") == null || "".equals(rs.getString("importeCalculado").trim())){
    				indet.setImporteCalculado(0D);
    			}else{  			
    				indet.setImporteCalculado(rs.getDouble("importeCalculado"));
    			}
    			if(rs.getString("indice") == null || "".equals(rs.getString("indice").trim())){
    				indet.setIndice(0D);
    			}else{  			
    				indet.setIndice(rs.getDouble("indice"));
    			}
    			if(rs.getString("recargo") == null || "".equals(rs.getString("recargo").trim())){
    				indet.setRecargo(0D);
    			}else{  			
    				indet.setRecargo(rs.getDouble("recargo"));
    			}
    			indet.setPartida(rs.getString("partida"));
    			indet.setCodIndet(rs.getInt("codindet"));
    			if(rs.getString("fechaPago") == null || "".equals(rs.getString("fechaPago").trim())){
    				indet.setFechaPago(null);
    			}else{  			
    				indet.setFechaPago(DateUtil.getDate(rs.getString("fechaPago"), DateUtil.ddMMYYYY_MASK));
    			}
    			if(rs.getString("caja") == null || "".equals(rs.getString("caja").trim())){
    				indet.setCaja(0);
    			}else{  			
    				indet.setCaja(rs.getInt("caja"));
    			}
    			if(rs.getString("paquete_o") == null || "".equals(rs.getString("paquete_o").trim())){
    				indet.setPaquete(0);
    			}else{  			
    				indet.setPaquete(rs.getInt("paquete_o"));
    			}
    			if(rs.getString("codpago_o") == null || "".equals(rs.getString("codpago_o").trim())){
    				indet.setCodPago(0);
    			}else{  			
    				indet.setCodPago(rs.getInt("codpago_o"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				indet.setFechaBalance(null);
    			}else{  			
    				indet.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.ddMMYYYY_MASK));
    			}
    			indet.setFiller(rs.getString("fi_o"));
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				indet.setReciboTr(0L);
    			}else{  			
    				indet.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			indet.setNroIndeterminado(rs.getLong("nroduplicado"));
    			indet.setTipoIngreso(0);//rs.getInt("tpo_ingreso"));
    			indet.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				indet.setFechaHora(null);
    			}else{  			
    				indet.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			break;
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de duplice en duplices.", ex);
    	}

    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return indet;
	}
	
	public void deleteDuplice(IndetVO duplice) throws Exception{

    	String funcName = "deleteDuplice()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps = null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		
			String sql = " delete from bal_duplice where nroduplicado=?";
			
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

			ps = con.prepareStatement(sql);
		
			ps.setLong(1, duplice.getNroIndeterminado());
			
			ps.executeUpdate();
			
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al eliminar duplice de duplices.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
	}
	
	/**
	 * Recupera un reingreso por nroReing
	 * @param nroReing
	 * @return
	 * @throws Exception
	 */
	public IndetVO getReingresoById(Long nroReing) throws Exception {

    	String funcName = "getReingresoById()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	IndetVO reingreso = null;// = new IndetVO();
 
    	// conecta con la base de reingreso via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		String sSql = "select * from bal_reingIndet " +
    		" where id = " + nroReing;
    			
    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
    		pstmt = con.prepareStatement(sSql);
    		rs = pstmt.executeQuery();

    		while (rs.next()) {
    			reingreso = new IndetVO();
    			reingreso.setSistema(rs.getString("sistema"));
    			reingreso.setNroComprobante(rs.getString("nroComprobante"));
    			reingreso.setClave(rs.getString("clave_r"));
   				reingreso.setResto(rs.getString("resto_r"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				reingreso.setImporteCobrado(0D);
    			}else{  			
    				reingreso.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("recargo_r") == null || "".equals(rs.getString("recargo_r").trim())){
    				reingreso.setRecargo(0D);
    			}else{  			
    				reingreso.setRecargo(rs.getDouble("recargo_r"));
    			}
    			reingreso.setPartida(rs.getString("partida_r"));
    			reingreso.setCodIndet(rs.getInt("codind_r"));
    			if(rs.getString("fpago_r") == null || "".equals(rs.getString("fpago_r").trim())){
    				reingreso.setFechaPago(null);
    			}else{  			
    				reingreso.setFechaPago(DateUtil.getDate(rs.getString("fpago_r"), DateUtil.yyyy_MM_dd_MASK));
    			}
    			if(rs.getString("caja_r") == null || "".equals(rs.getString("caja_r").trim())){
    				reingreso.setCaja(0);
    			}else{  			
    				reingreso.setCaja(rs.getInt("caja_r"));
    			}
    			if(rs.getString("paquete_r") == null || "".equals(rs.getString("paquete_r").trim())){
    				reingreso.setPaquete(0);
    			}else{  			
    				reingreso.setPaquete(rs.getInt("paquete_r"));
    			}
    			if(rs.getString("codpago_r") == null || "".equals(rs.getString("codpago_r").trim())){
    				reingreso.setCodPago(0);
    			}else{  			
    				reingreso.setCodPago(rs.getInt("codpago_r"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				reingreso.setFechaBalance(null);
    			}else{  			
    				reingreso.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.yyyy_MM_dd_MASK));
    			}
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				reingreso.setReciboTr(0L);
    			}else{  			
    				reingreso.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			reingreso.setNroIndeterminado(rs.getLong("id"));
    			reingreso.setNroReing(rs.getLong("id"));
    			if(rs.getString("fechaReing") == null || "".equals(rs.getString("fechaReing").trim())){
    				reingreso.setFechaReing(null);
    			}else{  			
    				reingreso.setFechaReing(DateUtil.getDate(rs.getString("fechaReing"), DateUtil.ddMMYYYY_MASK));
    			}
    			reingreso.setTipoIngreso(rs.getInt("tpo_ingreso"));
    			reingreso.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				reingreso.setFechaHora(null);
    			}else{  			
    				reingreso.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			break;
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de reingreso en bal_reingIndet.", ex);
    	}

    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return reingreso;
	}
	
	public void deleteReingreso(IndetVO reingreso) throws Exception{

    	String funcName = "deleteReingreso()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps = null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		
			String sql = " delete from bal_reingIndet where id=?";
			
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

			ps = con.prepareStatement(sql);
		
			ps.setLong(1, reingreso.getNroReing());
			
			ps.executeUpdate();
			
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al eliminar reingreso de bal_reingIndet.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
	}
	
	/** En este caso el reingreso con los filtros y  retorna una lista de VO porque los indeterminandos estan en otra DB
	 * y por suerte no tenemos mapeos de hibernate a otras DB
	 * @param indetVO
	 * @return
	 * @throws Exception
	 */
	public List<IndetVO> getListReingresoByFilter(ReingresoAdapter reingresoAdapter, List<Long> listNroReingresoExcluido) throws Exception {

    	String funcName = "getListReingresoByFilter()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	List<IndetVO> listIndet = new ArrayList<IndetVO>();
 
    	// conecta con la base de reingreso via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    	
    		String sSql = " from bal_reingIndet ";
    	    boolean flagAnd = false;

    	    IndetVO reingreso = reingresoAdapter.getReingreso().getIndet();
       		
    	    // Filtros aqui
    	    //  filtro por Fecha Pago Desde y Hasta
    		if (reingresoAdapter.getFechaDesde()!=null && reingresoAdapter.getFechaHasta() != null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " (fpago_r >= '"+DateUtil.formatDate(reingresoAdapter.getFechaDesde(), DateUtil.yyyy_MM_dd_MASK)+"'";
    			sSql += " and fpago_r <= '"+DateUtil.formatDate(reingresoAdapter.getFechaHasta(), DateUtil.yyyy_MM_dd_MASK)+"' )";
    	      flagAnd = true;
    		}
    	    
    	    // filtro por Sistema
     		if(!StringUtil.isNullOrEmpty(reingreso.getSistema())){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " sistema = '" +  reingreso.getSistema()+"'";
    			flagAnd = true;
    		}

    		// filtro por Cuenta
     		if(!StringUtil.isNullOrEmpty(reingreso.getNroComprobante())){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " nroComprobante like '%" +  reingreso.getNroComprobante()+"'";
    			flagAnd = true;
    		}

     		// filtro por ImporteCobrado
     		if(reingreso.getImporteCobrado() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " importe = " +  reingreso.getImporteCobrado();
    			flagAnd = true;
    		}

     		// 	 filtro por Fecha Balance 
    		if (reingreso.getFechaBalance() !=null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " fechaBalance = TO_DATE ('" +DateUtil.formatDate(reingreso.getFechaBalance(), DateUtil.yyyy_MM_dd_MASK) + "' ,'%Y-%m-%d') ";
    	      flagAnd = true;
    		}

     		// 	 filtro por Fecha Pago
    		if (reingreso.getFechaPago()!=null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " fpago_r = '"+DateUtil.formatDate(reingreso.getFechaPago(), DateUtil.yyyy_MM_dd_MASK)+"'";
    	      flagAnd = true;
    		}

    		//	 filtro por ReciboTr
     		if(reingreso.getReciboTr() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " reciboTr = " +  reingreso.getReciboTr();
    			flagAnd = true;
    		}

    		//	 filtro por codIndeterminado
     		if(reingreso.getCodIndet() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " codind_r = " +  reingreso.getCodIndet();
    			flagAnd = true;
    		}

    		//	 filtro por Paquete
     		if(reingreso.getPaquete() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " paquete_r = " +  reingreso.getPaquete();
    			flagAnd = true;
    		}

    		//	 filtro por Caja
     		if(reingreso.getCaja() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " caja_r = " +  reingreso.getCaja();
    			flagAnd = true;
    		}

     		if(!ListUtil.isNullOrEmpty(listNroReingresoExcluido)){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " id not in ( ";
     			for(Long nroReing: listNroReingresoExcluido){
     				if(listNroReingresoExcluido.indexOf(nroReing) == listNroReingresoExcluido.size()-1)
     					sSql += nroReing+") ";
     				else
     					sSql += nroReing+", ";
     			}
    			flagAnd = true;     			
     		}
     		
     		// Los bal_reingIndet se buscan solo los que no tienen fecha de reingreso
   			sSql += flagAnd ? " and " : " where ";	  
   			sSql += " fechaReing is null ";
    		
    		// Order By
    		sSql += " order by id";

 			sSql = "select * "+sSql;				
    		
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
			
			// obtenemos el resultado de la consulta
			pstmt = con.prepareStatement(sSql);
			rs = pstmt.executeQuery();
    		
    		while (rs.next()) {
    			reingreso = new IndetVO();
    			reingreso.setSistema(rs.getString("sistema"));
    			reingreso.setNroComprobante(rs.getString("nroComprobante"));
    			reingreso.setClave(rs.getString("clave_r"));
   				reingreso.setResto(rs.getString("resto_r"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				reingreso.setImporteCobrado(0D);
    			}else{  			
    				reingreso.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("recargo_r") == null || "".equals(rs.getString("recargo_r").trim())){
    				reingreso.setRecargo(0D);
    			}else{  			
    				reingreso.setRecargo(rs.getDouble("recargo_r"));
    			}
    			reingreso.setPartida(rs.getString("partida_r"));
    			reingreso.setCodIndet(rs.getInt("codind_r"));
    			if(rs.getString("fpago_r") == null || "".equals(rs.getString("fpago_r").trim())){
    				reingreso.setFechaPago(null);
    			}else{  			
    				reingreso.setFechaPago(DateUtil.getDate(rs.getString("fpago_r"), DateUtil.yyyy_MM_dd_MASK));
    			}
    			if(rs.getString("caja_r") == null || "".equals(rs.getString("caja_r").trim())){
    				reingreso.setCaja(0);
    			}else{  			
    				reingreso.setCaja(rs.getInt("caja_r"));
    			}
    			if(rs.getString("paquete_r") == null || "".equals(rs.getString("paquete_r").trim())){
    				reingreso.setPaquete(0);
    			}else{  			
    				reingreso.setPaquete(rs.getInt("paquete_r"));
    			}
    			if(rs.getString("codpago_r") == null || "".equals(rs.getString("codpago_r").trim())){
    				reingreso.setCodPago(0);
    			}else{  			
    				reingreso.setCodPago(rs.getInt("codpago_r"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				reingreso.setFechaBalance(null);
    			}else{
    				reingreso.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.yyyy_MM_dd_MASK));
    			}
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				reingreso.setReciboTr(0L);
    			}else{  			
    				reingreso.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			reingreso.setNroIndeterminado(rs.getLong("id"));
    			reingreso.setNroReing(rs.getLong("id"));
    			if(rs.getString("fechaReing") == null || "".equals(rs.getString("fechaReing").trim())){
    				reingreso.setFechaReing(null);
    			}else{  			
    				reingreso.setFechaReing(DateUtil.getDate(rs.getString("fechaReing"), DateUtil.yyyy_MM_dd_MASK));
    			}
    			reingreso.setTipoIngreso(rs.getInt("tpo_ingreso"));
    			reingreso.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				reingreso.setFechaHora(null);
    			}else{  			
    				reingreso.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			listIndet.add(reingreso);
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de reingreso en bal_reingIndet.", ex);
    	}


    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return listIndet;
	}
	
	public IndetVO createDuplice(IndetVO indet) throws Exception{

    	String funcName = "createDuplice()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement ps 	= null;
    	ResultSet         rs    = null;

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    	
			long nroIndeterminado = 0;
			String sql = "";
			sql += " insert into bal_duplice"; 
			sql += " (sistema, nroComprobante, clave, resto, importe,";
			sql += " importeBasico, importeCalculado, indice, recargo, partida, codindet,";
			sql += " fechaPago, caja, paquete_o, codpago_o, fechaBalance, fi_o, reciboTr,";
			sql += " usuarioUltMdf, fechaUltMdf)";
			sql += " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);
			
			// insert bal_indeterminado
			ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, indet.getSistema());
			ps.setString(2, indet.getNroComprobante());
			ps.setString(3, indet.getClave());
			ps.setString(4, indet.getResto());
			if(indet.getImporteCobrado() != null)
				ps.setDouble(5, indet.getImporteCobrado());
			else
				ps.setDouble(5, 0D);
			if(indet.getImporteBasico() != null)
				ps.setDouble(6, indet.getImporteBasico());
			else
				ps.setDouble(6, 0D);
			if(indet.getImporteCalculado() != null)
				ps.setDouble(7, indet.getImporteCalculado());
			else
				ps.setDouble(7, 0D);
			if(indet.getIndice() != null)
				ps.setDouble(8, indet.getIndice());
			else
				ps.setDouble(8, 0D);
			if(indet.getRecargo() != null)
				ps.setDouble(9, indet.getRecargo());
			else
				ps.setDouble(9, 0D);
			ps.setString(10, indet.getPartida());
			ps.setInt(11, indet.getCodIndet());
			ps.setString(12, DateUtil.formatDate(indet.getFechaPago(), DateUtil.ddMMYYYY_MASK));
			ps.setInt(13, indet.getCaja());
			ps.setInt(14, indet.getPaquete());
			ps.setInt(15, indet.getCodPago());
			ps.setString(16, DateUtil.formatDate(indet.getFechaBalance(), DateUtil.ddMMYYYY_MASK));
			ps.setString(17, indet.getFiller());
			ps.setLong(18, indet.getReciboTr());
			String userName = DemodaUtil.currentUserContext().getUserName();
    		if (userName != null) {
    			ps.setString(19,userName);
    		} else { 
    			ps.setString(19,"siat");
			}
			ps.setTimestamp(20, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			
			ps.executeUpdate();
			
			nroIndeterminado = 0;
			if(ps != null){		
	    		ResultSet rsId = ps.getGeneratedKeys();
	    		while (rsId.next()) {
	    			nroIndeterminado = rsId.getInt(1);	 
	    	    }
	    		rsId.close();
	    	}
			
			indet.setNroIndeterminado(nroIndeterminado);
		
			try {rs.close();}    catch (Exception ex) {}
    		try {ps.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo al insertar indeterminado en bal_duplice.", ex);
    		try {rs.close();}    catch (Exception exc) {}
    		try {ps.close();} catch (Exception exc) {}
    		try {con.close();}   catch (Exception exc) {}
    		throw ex;
    	}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
    	return indet;
	}
	
	
	/** En este caso el reingreso con los filtros y  retorna una lista de VO porque los indeterminandos estan en otra DB
	 * y por suerte no tenemos mapeos de hibernate a otras DB
	 * @param indetVO
	 * @return
	 * @throws Exception
	 */
	public List<IndetVO> getBySearchPage(IndetReingSearchPage indetReingSearchPage) throws Exception {

    	String funcName = "getBySearchPage()";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	List<IndetVO> listIndet = new ArrayList<IndetVO>();
 
    	// conecta con la base de reingreso via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    	
    		String sSql = " from bal_reingIndet ";
    	    boolean flagAnd = false;

    	    IndetVO reingreso = indetReingSearchPage.getIndetReing();
       		
    	    // Filtros aqui
    	    //  filtro por Fecha Pago Desde y Hasta
    		if (indetReingSearchPage.getFechaDesde()!=null && indetReingSearchPage.getFechaHasta() != null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " (fpago_r >= '"+DateUtil.formatDate(indetReingSearchPage.getFechaDesde(), DateUtil.yyyy_MM_dd_MASK)+"'";
    			sSql += " and fpago_r <= '"+DateUtil.formatDate(indetReingSearchPage.getFechaHasta(), DateUtil.yyyy_MM_dd_MASK)+"' )";
    	      flagAnd = true;
    		}
    	    
    	    // filtro por Sistema
     		if(!StringUtil.isNullOrEmpty(reingreso.getSistema())){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " sistema = '" +  reingreso.getSistema()+"'";
    			flagAnd = true;
    		}

    		// filtro por Cuenta
     		if(!StringUtil.isNullOrEmpty(reingreso.getNroComprobante())){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " nroComprobante like '%" +  reingreso.getNroComprobante()+"'";
    			flagAnd = true;
    		}

     		// filtro por ImporteCobrado
     		if(reingreso.getImporteCobrado() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " importe = " +  reingreso.getImporteCobrado();
    			flagAnd = true;
    		}

     		// 	 filtro por Fecha Balance 
    		if (reingreso.getFechaBalance() !=null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " fechaBalance = TO_DATE ('" +DateUtil.formatDate(reingreso.getFechaBalance(), DateUtil.yyyy_MM_dd_MASK) + "' ,'%Y-%m-%d') ";
    	      flagAnd = true;
    		}

     		// 	 filtro por Fecha Pago
    		if (reingreso.getFechaPago()!=null) {
    			sSql += flagAnd ? " and " : " where ";	  
    			sSql += " fpago_r = '"+DateUtil.formatDate(reingreso.getFechaPago(), DateUtil.yyyy_MM_dd_MASK)+"'";
    	      flagAnd = true;
    		}

    		//	 filtro por ReciboTr
     		if(reingreso.getReciboTr() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " reciboTr = " +  reingreso.getReciboTr();
    			flagAnd = true;
    		}

    		//	 filtro por codIndeterminado
     		if(reingreso.getCodIndet() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " codind_r = " +  reingreso.getCodIndet();
    			flagAnd = true;
    		}

    		//	 filtro por Paquete
     		if(reingreso.getPaquete() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " paquete_r = " +  reingreso.getPaquete();
    			flagAnd = true;
    		}

    		//	 filtro por Caja
     		if(reingreso.getCaja() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " caja_r = " +  reingreso.getCaja();
    			flagAnd = true;
    		}
     		
     		// filtro por Nro Reingreso
     		if(reingreso.getNroReing() != null){
     			sSql += flagAnd ? " and " : " where ";
     			sSql += " id = " +  reingreso.getNroReing();
    			flagAnd = true;
    		}

     		// Los bal_reingIndet se buscan solo los que no tienen fecha de reingreso
   			sSql += flagAnd ? " and " : " where ";	  
   			sSql += " fechaReing is null ";
    		
    		// Order By
    		sSql += " order by id";

      	    Long cantidadMaxima;
			String queryCount = sSql;
			
			int pos = queryCount.toLowerCase().indexOf("order by");
			if (pos >= 0) {
				queryCount = queryCount.substring(0, pos);
			}

			pstmt = con.prepareStatement("select count(*) " + queryCount);
    		rs = pstmt.executeQuery();

    		cantidadMaxima = 0L;
    		while (rs.next()) {
    			cantidadMaxima = new Long(rs.getInt(1));
    			break;
    		}
    	    	
    		indetReingSearchPage.setMaxRegistros(cantidadMaxima);
    		
 			sSql = "select * "+sSql;				
    		
			if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
			
			// obtenemos el resultado de la consulta
			pstmt = con.prepareStatement(sSql);
			rs = pstmt.executeQuery();
    		
			long skip = 1;
			long first = 1;
    		while (rs.next()) {
    			if (indetReingSearchPage.isPaged() && skip < indetReingSearchPage.getFirstResult()) {
    				skip++;
    				continue;
    			}
    			reingreso = new IndetVO();
    			reingreso.setSistema(rs.getString("sistema"));
    			reingreso.setNroComprobante(rs.getString("nroComprobante"));
    			reingreso.setClave(rs.getString("clave"));
   				reingreso.setResto(rs.getString("resto"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				reingreso.setImporteCobrado(0D);
    			}else{  			
    				reingreso.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("recargo") == null || "".equals(rs.getString("recargo").trim())){
    				reingreso.setRecargo(0D);
    			}else{  			
    				reingreso.setRecargo(rs.getDouble("recargo"));
    			}
    			reingreso.setPartida(rs.getString("partida"));
    			reingreso.setCodIndet(rs.getInt("codind_r"));
    			if(rs.getString("fpago_r") == null || "".equals(rs.getString("fpago_r").trim())){
    				reingreso.setFechaPago(null);
    			}else{  			
    				reingreso.setFechaPago(DateUtil.getDate(rs.getString("fpago_r"), DateUtil.yyyy_MM_dd_MASK));
    			}
    			if(rs.getString("caja_r") == null || "".equals(rs.getString("caja_r").trim())){
    				reingreso.setCaja(0);
    			}else{  			
    				reingreso.setCaja(rs.getInt("caja_r"));
    			}
    			if(rs.getString("paquete_r") == null || "".equals(rs.getString("paquete_r").trim())){
    				reingreso.setPaquete(0);
    			}else{  			
    				reingreso.setPaquete(rs.getInt("paquete_r"));
    			}
    			if(rs.getString("codpago_r") == null || "".equals(rs.getString("codpago_r").trim())){
    				reingreso.setCodPago(0);
    			}else{  			
    				reingreso.setCodPago(rs.getInt("codpago_r"));
    			}
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				reingreso.setFechaBalance(null);
    			}else{
    				reingreso.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.yyyy_MM_dd_MASK));
    			}
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				reingreso.setReciboTr(0L);
    			}else{  			
    				reingreso.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			reingreso.setNroIndeterminado(rs.getLong("id"));
    			reingreso.setNroReing(rs.getLong("id"));
    			if(rs.getString("fechaReing") == null || "".equals(rs.getString("fechaReing").trim())){
    				reingreso.setFechaReing(null);
    			}else{  			
    				reingreso.setFechaReing(DateUtil.getDate(rs.getString("fechaReing"), DateUtil.yyyy_MM_dd_MASK));
    			}
    			reingreso.setTipoIngreso(rs.getInt("tpo_ingreso"));
    			reingreso.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				reingreso.setFechaHora(null);
    			}else{  			
    				reingreso.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			listIndet.add(reingreso);
    			if (indetReingSearchPage.isPaged()) {
    				first++;
    				if(first > indetReingSearchPage.getRecsByPage())
    					break;
    			}
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de reingreso en bal_reingIndet.", ex);
    	}


    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return listIndet;
	}
	
	/**
	 * Recupera un intet_modif por nroIndeterminado
	 * @param nroIndeterminado
	 * @return
	 * @throws Exception
	 */
	public IndetVO getIndetModifById(Long nroIndeterm) throws Exception {

    	String funcName = "getIndetAuditById(Long nroIndeterm)";

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	IndetVO indet = new IndetVO();
 
    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	try {
    		String sSql = "select * from bal_indetAudit " +
    		" where id = " + nroIndeterm;
    			
    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);
    		pstmt = con.prepareStatement(sSql);
    		rs = pstmt.executeQuery();

    		while (rs.next()) {
    			indet = new IndetVO();
    			indet.setSistema(rs.getString("sistema"));
    			indet.setNroComprobante(rs.getString("nroComprobante"));
    			indet.setClave(rs.getString("clave"));
   				indet.setResto(rs.getString("resto"));    			
    			if(rs.getString("importe") == null || "".equals(rs.getString("importe").trim())){
    				indet.setImporteCobrado(0D);
    			}else{  			
    				indet.setImporteCobrado(rs.getDouble("importe"));
    			}
    			if(rs.getString("importeBasico") == null || "".equals(rs.getString("importeBasico").trim())){
    				indet.setImporteBasico(0D);
    			}else{  			
    				indet.setImporteBasico(rs.getDouble("importeBasico"));
    			}
    			if(rs.getString("importeCalculado") == null || "".equals(rs.getString("importeCalculado").trim())){
    				indet.setImporteCalculado(0D);
    			}else{  			
    				indet.setImporteCalculado(rs.getDouble("importeCalculado"));
    			}
    			if(rs.getString("indice") == null || "".equals(rs.getString("indice").trim())){
    				indet.setIndice(0D);
    			}else{  			
    				indet.setIndice(rs.getDouble("indice"));
    			}
    			if(rs.getString("recargo") == null || "".equals(rs.getString("recargo").trim())){
    				indet.setRecargo(0D);
    			}else{  			
    				indet.setRecargo(rs.getDouble("recargo"));
    			}
    			indet.setPartida(rs.getString("partida"));
    			indet.setCodIndet(rs.getInt("codindet"));
    			if(rs.getString("fechaPago") == null || "".equals(rs.getString("fechaPago").trim())){
    				indet.setFechaPago(null);
    			}else{  			
    				indet.setFechaPago(DateUtil.getDate(rs.getString("fechaPago"), DateUtil.ddMMYYYY_MASK));
    			}
    			if(rs.getString("caja") == null || "".equals(rs.getString("caja").trim())){
    				indet.setCaja(0);
    			}else{  			
    				indet.setCaja(rs.getInt("caja"));
    			}
    			
    			indet.setPaquete(0);
    			indet.setCodPago(0);
    			indet.setFiller("");
    
    			if(rs.getString("fechaBalance") == null || "".equals(rs.getString("fechaBalance").trim())){
    				indet.setFechaBalance(null);
    			}else{  			
    				indet.setFechaBalance(DateUtil.getDate(rs.getString("fechaBalance"), DateUtil.ddMMYYYY_MASK));
    			}
    			if(rs.getString("reciboTr") == null || "".equals(rs.getString("reciboTr").trim())){
    				indet.setReciboTr(0L);
    			}else{  			
    				indet.setReciboTr(rs.getLong("reciboTr"));    		
    			}
    			indet.setNroIndeterminado(rs.getLong("id"));
    			indet.setTipoIngreso(1);
    			indet.setUsuario(rs.getString("usuarioUltMdf"));
    			if(rs.getString("fechaUltMdf") == null || "".equals(rs.getString("fechaUltMdf").trim())){
    				indet.setFechaHora(null);
    			}else{  			
    				indet.setFechaHora(DateUtil.getDate(rs.getString("fechaUltMdf"), DateUtil.ddMMYYYY_MASK));
    			}
    			
    			break;
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    	} catch (Exception ex) {
    		log.error("Fallo busqueda de indeterminado en bal_indetAudit.", ex);
    	}

    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	
		return indet;
	}
}
