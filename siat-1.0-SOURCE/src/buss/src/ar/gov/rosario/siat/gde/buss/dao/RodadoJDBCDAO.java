//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.rod.buss.bean.EstadoCivil;
import ar.gov.rosario.siat.rod.buss.bean.Marca;
import ar.gov.rosario.siat.rod.buss.bean.Modelo;
import ar.gov.rosario.siat.rod.buss.bean.Propietario;
import ar.gov.rosario.siat.rod.buss.bean.TipoCarga;
import ar.gov.rosario.siat.rod.buss.bean.TipoDoc;
import ar.gov.rosario.siat.rod.buss.bean.TipoFabricacion;
import ar.gov.rosario.siat.rod.buss.bean.TipoMotor;
import ar.gov.rosario.siat.rod.buss.bean.TipoPago;
import ar.gov.rosario.siat.rod.buss.bean.TipoPropietario;
import ar.gov.rosario.siat.rod.buss.bean.TipoTramite;
import ar.gov.rosario.siat.rod.buss.bean.TipoUso;
import ar.gov.rosario.siat.rod.buss.bean.TipoVehiculo;
import coop.tecso.demoda.buss.dao.JDBCConnManager;
import coop.tecso.demoda.iface.helper.StringUtil;

public class RodadoJDBCDAO {

	private static final String DS_NAME = "java:comp/env/ds/rodados";
       
    private Logger log = Logger.getLogger(RodadoJDBCDAO.class);
	
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public RodadoJDBCDAO() {
		
	}

    /**
     * Recupera los Tipos de Fabricacion
     */
    public List<TipoFabricacion> getListTipoFabricacion() throws Exception {
    	String funcName = "getListTipoFabricacion()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoFabricacion";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, "METALICA");
        	
        	rs = pstmt.executeQuery();

        	List<TipoFabricacion> listTipoFabricacion = new ArrayList<TipoFabricacion>();

        	while (rs.next()) {
        		TipoFabricacion tt = new TipoFabricacion();
        		tt.setCodFab(rs.getInt("codFab"));
        		tt.setDesFab(rs.getString("desFab"));

        		listTipoFabricacion.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoFabricacion;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }
    
    /**
     * Recupera los Tipos de Vehiculo
     */
    public List<TipoVehiculo> getListTipoVehiculo() throws Exception {
    	String funcName = "getListTipoVehiculo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoVehiculo";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, "METALICA");
        	
        	rs = pstmt.executeQuery();

        	List<TipoVehiculo> listTipoVehiculo = new ArrayList<TipoVehiculo>();

        	while (rs.next()) {
        		TipoVehiculo tt = new TipoVehiculo();
        		tt.setCodTipoGen(rs.getString("codTipoGen"));
        		tt.setDesTipoGen(rs.getString("desTipoGen"));
        		tt.setCodTipoEsp(rs.getString("codTipoEsp"));
        		tt.setDesTipoEsp(rs.getString("desTipoEsp"));

        		listTipoVehiculo.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoVehiculo;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Tipos de Carga
     */
    public List<TipoCarga> getListTipoCarga() throws Exception {
    	String funcName = "getListTipoCarga()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoCarga";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, "METALICA");
        	
        	rs = pstmt.executeQuery();

        	List<TipoCarga> listTipoCarga = new ArrayList<TipoCarga>();

        	while (rs.next()) {
        		TipoCarga tt = new TipoCarga();
        		tt.setCodTipoCarga(rs.getString("codTipoCarga"));
        		tt.setDesTipoCarga(rs.getString("desTipoCarga"));
        		
        		listTipoCarga.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoCarga;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Tipos de Doc
     */
    public List<TipoDoc> getListTipoDoc() throws Exception {
    	String funcName = "getListTipoDoc()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoDoc";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, "METALICA");
        	
        	rs = pstmt.executeQuery();

        	List<TipoDoc> listTipoDoc = new ArrayList<TipoDoc>();

        	while (rs.next()) {
        		TipoDoc tt = new TipoDoc();
        		tt.setCodTipoDoc(rs.getInt("codTipoDoc"));
        		tt.setDesTipoDoc(rs.getString("desTipoDoc"));
        		
        		listTipoDoc.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoDoc;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Estado Civil
     */
    public List<EstadoCivil> getListEstadoCivil() throws Exception {
    	String funcName = "getListEstadoCivil()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_estadoCivil";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, "METALICA");
        	
        	rs = pstmt.executeQuery();

        	List<EstadoCivil> listEstadoCivil = new ArrayList<EstadoCivil>();

        	while (rs.next()) {
        		EstadoCivil tt = new EstadoCivil();
        		tt.setCodEstCiv(rs.getInt("codEstCiv"));
        		tt.setDesEstCiv(rs.getString("desEstCiv"));
        		
        		listEstadoCivil.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listEstadoCivil;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Tipo de Motor
     */
    public List<TipoMotor> getListTipoMotor() throws Exception {
    	String funcName = "getListTipoMotor()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoMotor";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);

        	
        	rs = pstmt.executeQuery();

        	List<TipoMotor> listTipoMotor = new ArrayList<TipoMotor>();

        	while (rs.next()) {
        		TipoMotor tt = new TipoMotor();
        		tt.setCodTipoMotor(rs.getInt("codTipoMotor"));
        		tt.setDesTipoMotor(rs.getString("desTipoMotor"));
        		
        		listTipoMotor.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoMotor;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Tipo de Pago
     */
    public List<TipoPago> getListTipoPago() throws Exception {
    	String funcName = "getListTipoPago()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoPago";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);

        	
        	rs = pstmt.executeQuery();

        	List<TipoPago> listTipoPago = new ArrayList<TipoPago>();

        	while (rs.next()) {
        		TipoPago tt = new TipoPago();
        		tt.setCodPago(rs.getInt("codPago"));
        		tt.setDesPago(rs.getString("desPago"));
        		
        		listTipoPago.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoPago;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Tipo de Tramite
     */
    public List<TipoTramite> getListTipoTramite() throws Exception {
    	String funcName = "getListTipoTramite()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoTramite";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);

        	
        	rs = pstmt.executeQuery();

        	List<TipoTramite> listTipoTramite = new ArrayList<TipoTramite>();

        	while (rs.next()) {
        		TipoTramite tt = new TipoTramite();
        		tt.setCodTipoTramite(rs.getInt("codTipoTramite"));
        		tt.setDesTipoTramite(rs.getString("desTipoTramite"));
        		tt.setRubros(rs.getString("rubros"));
        		listTipoTramite.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoTramite;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Modelos
     */
    public List<Modelo> getListModelo() throws Exception {
    	String funcName = "getListModelo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_modelo";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);

        	rs = pstmt.executeQuery();

        	List<Modelo> listModelo = new ArrayList<Modelo>();

        	while (rs.next()) {
        		Modelo tt = new Modelo();
        		tt.setCodModelo(rs.getInt("codModelo"));
        		tt.setCodMarca(rs.getInt("codMarca"));
        		tt.setDesModelo(rs.getString("desModelo"));
        		tt.setCodTipoGen(rs.getString("codTipoGen"));
        		tt.setCodTipoEsp(rs.getString("codTipoEsp"));
        		listModelo.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listModelo;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Tipo de Uso
     */
    public List<TipoUso> getListTipoUso() throws Exception {
    	String funcName = "getListTipoUso()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoUso";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);

        	rs = pstmt.executeQuery();

        	List<TipoUso> listTipoUso = new ArrayList<TipoUso>();

        	while (rs.next()) {
        		TipoUso tt = new TipoUso();
        		tt.setCodUso(rs.getInt("codUso"));
        		tt.setDesUso(rs.getString("desUso"));
        		listTipoUso.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoUso;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    
    /**
     * Recupera los TipoPropietario
     */
    public List<TipoPropietario> getListTipoPropietario() throws Exception {
    	String funcName = "getListTipoPropietario()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoPropietario";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);

        	rs = pstmt.executeQuery();

        	List<TipoPropietario> listTipoPropietario = new ArrayList<TipoPropietario>();

        	while (rs.next()) {
        		TipoPropietario tt = new TipoPropietario();
        		tt.setCodTipoProp(rs.getInt("codTipoProp"));
        		tt.setDesTipoProp(rs.getString("desTipoProp"));
        		listTipoPropietario.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoPropietario;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    
    /**
     * Recupera los Tipo de tramite por su codigo
     */
    public TipoTramite getTipoTramiteByCodigo(Integer codigo) throws Exception {
    	String funcName = "getTipoTramiteByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoTramite WHERE codTipoTramite="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoTramite tt = new TipoTramite();
        	
        	if(rs.next()){
	    		tt.setCodTipoTramite(rs.getInt("codTipoTramite"));
	    		tt.setDesTipoTramite(rs.getString("desTipoTramite"));
	    		tt.setRubros(rs.getString("rubros"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }
    
    /**
     * Recupera los Modelos por su codigo
     */
    public Modelo getModeloByCodigo(Integer codigo) throws Exception {
    	String funcName = "getModeloByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_modelo WHERE codModelo="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	Modelo tt = new Modelo();
        	
        	if(rs.next()){
	    		tt.setCodModelo(rs.getInt("codModelo"));
	    		tt.setCodMarca(rs.getInt("codMarca"));
	    		tt.setCodTipoEsp(rs.getString("codTipoEsp"));
	    		tt.setCodTipoGen(rs.getString("codTipoGen"));
	    		tt.setDesModelo(rs.getString("desModelo"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }


    public List<Modelo> getModeloBySearchPage(String descrip) throws Exception {
    	String funcName = "getModeloBySearchPage()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_modelo WHERE desModelo='"+descrip+"'";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	List<Modelo> listModelo = new ArrayList<Modelo>();
        	
        	if(rs.next()){
        		Modelo tt = new Modelo();
	    		tt.setCodModelo(rs.getInt("codModelo"));
	    		tt.setCodMarca(rs.getInt("codMarca"));
	    		tt.setCodTipoEsp(rs.getString("codTipoEsp"));
	    		tt.setCodTipoGen(rs.getString("codTipoGen"));
	    		tt.setDesModelo(rs.getString("desModelo"));
	    		listModelo.add(tt);
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listModelo;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
	}
    
    /**
     * Busca modelo por su codigo y descripcion
     * @param codigo
     * @param descrip
     * @return
     * @throws Exception
     */
    public List<Modelo> getModeloByCodDes(Integer codigo,String descrip) throws Exception {
    	String funcName = "getModeloByCodDes()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
       
        	//String sql = "SELECT * from rod_modelo";
        	String sql= "SELECT mod.*, mar.desmarca, tip.destipogen, tip.destipoesp "+ 
        	"FROM rod_modelo mod, rod_marca mar, rod_tipovehiculo tip "+
        	"WHERE mod.codmarca = mar.codmarca AND "+ 
        	"mod.codtipogen = tip.codtipogen AND "+ 
        	"mod.codtipoesp = tip.codtipoesp";
        	if(!StringUtil.isNullOrEmpty(descrip) || codigo!=null){
	        	 
	        	if(!StringUtil.isNullOrEmpty(descrip)){
	        		sql+=" and UPPER(TRIM(desModelo)) like '%"+StringUtil.escaparUpper(descrip)+"%' ";
	        		
	        	}
	        	if(codigo!=null){
	        		sql+=" and codModelo="+codigo;
	        	}
        	
        	}
        	//String sql = "SELECT * from rod_modelo WHERE UPPER(TRIM(desModelo)) like '%"+StringUtil.escaparUpper(descrip)+"%' "+" and codModelo="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
      
        	
        	rs = pstmt.executeQuery();
        	
        	List<Modelo> listModelo = new ArrayList<Modelo>();
        	
        	while(rs.next()){
        		Modelo tt = new Modelo();
	    		tt.setCodModelo(rs.getInt("codModelo"));
	    		tt.setCodMarca(rs.getInt("codMarca"));
	    		tt.setCodTipoEsp(rs.getString("codTipoEsp"));
	    		tt.setCodTipoGen(rs.getString("codTipoGen"));
	    		tt.setDesModelo(rs.getString("desModelo"));
	    		
	    		tt.getMarca().setCodMarca(rs.getInt("codMarca"));
	    		tt.getMarca().setDesMarca(rs.getString("desMarca"));
	    		tt.getTipoVehiculo().setCodTipoEsp(rs.getString("codTipoEsp"));
	    		tt.getTipoVehiculo().setCodTipoGen(rs.getString("codTipoGen"));
	    		tt.getTipoVehiculo().setDesTipoEsp(rs.getString("desTipoEsp"));
	    		tt.getTipoVehiculo().setDesTipoGen(rs.getString("desTipoGen"));
	    		
	    		listModelo.add(tt);
	    		
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listModelo;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
	}

    public TipoVehiculo getTipoVehiculoByCodGenCodEsp(String codGen, String codEsp) throws Exception {
    	String funcName = "getTipoVehiculoByCodGenCodEsp()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
       
        	String sql = "SELECT * from rod_tipoVehiculo";
        	if(!StringUtil.isNullOrEmpty(codGen) || !StringUtil.isNullOrEmpty(codEsp)){
	        	 sql+=" WHERE ";
	        	if(!StringUtil.isNullOrEmpty(codGen)){
	        		sql+=" codTipoGen='"+codGen+"'";
	        		if(!StringUtil.isNullOrEmpty(codEsp)){
		        		sql+=" and codTipoEsp='"+codEsp+"'";
		        	}
	        	}else{
	        		if(!StringUtil.isNullOrEmpty(codEsp)){
		        		sql+=" and codTipoEsp='"+codEsp+"'";
		        	}
	        	}
        	}
        	//String sql = "SELECT * from rod_modelo WHERE UPPER(TRIM(desModelo)) like '%"+StringUtil.escaparUpper(descrip)+"%' "+" and codModelo="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoVehiculo tt = new TipoVehiculo();
        	if(rs.next()){
	    		tt.setCodTipoEsp(rs.getString("codTipoEsp"));
	    		tt.setCodTipoGen(rs.getString("codTipoGen"));
	    		tt.setDesTipoEsp(rs.getString("desTipoEsp"));
	    		tt.setDesTipoGen(rs.getString("desTipoGen"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
	}

    /**
     * Recupera los Marca
     */
    public List<Marca> getListMarca() throws Exception {
    	String funcName = "getListMarca()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_marca";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, "METALICA");
        	
        	rs = pstmt.executeQuery();

        	List<Marca> listMarca = new ArrayList<Marca>();

        	while (rs.next()) {
        		Marca tt = new Marca();
        		tt.setCodMarca(rs.getInt("codMarca"));
        		tt.setDesMarca(rs.getString("desMarca"));
        		
        		listMarca.add(tt);
        	}

        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listMarca;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera las Marcas por su codigo
     */
    public Marca getMarcaByCodigo(Integer codigo) throws Exception {
    	String funcName = "getMarcaByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_marca WHERE codMarca="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	Marca tt = new Marca();
        	
        	if(rs.next()){
	    		tt.setCodMarca(rs.getInt("codMarca"));
	    		tt.setDesMarca(rs.getString("desMarca"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

   
    /**
     * Recupera las Tipo de Documento por su codigo
     */
    public TipoDoc getTipoDocByCodigo(Integer codigo) throws Exception {
    	String funcName = "getTipoDocByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoDoc WHERE codTipoDoc="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoDoc tt = new TipoDoc();
        	
        	if(rs.next()){
	    		tt.setCodTipoDoc(rs.getInt("codTipoDoc"));
	    		tt.setDesTipoDoc(rs.getString("desTipoDoc"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }
    
    /**
     * Recupera los Estados Civilies por su codigo
     */
    public EstadoCivil getEstadoCivilByCodigo(Integer codigo) throws Exception {
    	String funcName = "getEstadoCivilByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_estadoCivil WHERE codEstCiv="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	EstadoCivil tt = new EstadoCivil();
        	
        	if(rs.next()){
	    		tt.setCodEstCiv(rs.getInt("codEstCiv"));
	    		tt.setDesEstCiv(rs.getString("desEstCiv"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

      
    /**
     * Recupera los Tipo Motor por su codigo
     */
    public TipoMotor getTipoMotorByCodigo(Integer codigo) throws Exception {
    	String funcName = "getTipoMotorByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoMotor WHERE codTipoMotor="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoMotor tt = new TipoMotor();
        	
        	if(rs.next()){
	    		tt.setCodTipoMotor(rs.getInt("codTipoMotor"));
	    		tt.setDesTipoMotor(rs.getString("desTipoMotor"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }


    /**
     * Recupera los Tipo Pago por su codigo
     */
    public TipoPago getTipoPagoByCodigo(Integer codigo) throws Exception {
    	String funcName = "getTipoPagoByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoPago WHERE codPago="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoPago tt = new TipoPago();
        	
        	if(rs.next()){
	    		tt.setCodPago(rs.getInt("codPago"));
	    		tt.setDesPago(rs.getString("desPago"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los Tipo Uso por su codigo
     */
    public TipoUso getTipoUsoByCodigo(Integer codigo) throws Exception {
    	String funcName = "getTipoUsoByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoUso WHERE codUso="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoUso tt = new TipoUso();
        	
        	if(rs.next()){
	    		tt.setCodUso(rs.getInt("codUso"));
	    		tt.setDesUso(rs.getString("desUso"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los TipoCarga por su codigo
     */
    public TipoCarga getTipoCargaByCodigo(String codigo) throws Exception {
    	String funcName = "getTipoCargaByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoCarga WHERE codTipoCarga='"+codigo+"'";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoCarga tt = new TipoCarga();
        	
        	if(rs.next()){
	    		tt.setCodTipoCarga(rs.getString("codTipoCarga"));
	    		tt.setDesTipoCarga(rs.getString("desTipoCarga"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los TipoFabricacion por su codigo
     */
    public TipoFabricacion getTipoFabricacionByCodigo(Integer codigo) throws Exception {
    	String funcName = "getTipoFabricacionByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoFabricacion WHERE codFab="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoFabricacion tt = new TipoFabricacion();
        	
        	if(rs.next()){
	    		tt.setCodFab(rs.getInt("codFab"));
	    		tt.setDesFab(rs.getString("desFab"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Recupera los TipoPropietario por su codigo
     */
    public TipoPropietario getTipoPropietarioByCodigo(Integer codigo) throws Exception {
    	String funcName = "getTipoPropietarioByCodigo()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
        	String sql = "SELECT * from rod_tipoPropietario WHERE codTipoProp="+codigo;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	TipoPropietario tt = new TipoPropietario();
        	
        	if(rs.next()){
	    		tt.setCodTipoProp(rs.getInt("codTipoProp"));
	    		tt.setDesTipoProp(rs.getString("desTipoProp"));
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tt;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }

    /**
     * Busca Propietario por su tipo y numero de doc
     * @param tipo
     * @param numoer
     * @return
     * @throws Exception
     */
    public List<Propietario> getPropietarioByDoc(Integer tipo,Long numero) throws Exception {
    	String funcName = "getPropietarioByCodDes()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
 
        try {
       
        	
        	String sql = "SELECT * from rod_propietario WHERE codTipoDoc="+tipo+" AND nroDoc="+numero;
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	//pstmt.setString(1, codigo.toString());
        	
        	rs = pstmt.executeQuery();
        	
        	List<Propietario> listPropietario = new ArrayList<Propietario>();
        	
        	while(rs.next()){
        		Propietario tt = new Propietario();
	    		tt.setApellidoORazon(rs.getString("apellidoORazon"));
	    		tt.setCodEstCiv(rs.getInt("codEstCiv"));
	    		tt.setCodSexo(rs.getInt("codSexo"));
	    		tt.setCodTipoDoc(rs.getInt("codTipoDoc"));
	    		tt.setCodTipoProp(rs.getInt("codTipoProp"));
	    		tt.setDesEstCiv(rs.getString("desEstCiv"));
	    		tt.setDesSexo(rs.getString("desSexo"));
	    		tt.setDesTipoDoc(rs.getString("desTipoDoc"));
	    		tt.setDesTipoProp(rs.getString("desTipoProp"));
	    		tt.setFechaNac(rs.getDate("fechaNac"));
	    		
	    		listPropietario.add(tt);
	    		
        	}
    		
        	rs.close();
        	pstmt.close();
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listPropietario;        	
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
	}

}

