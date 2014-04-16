//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.engine;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.AdpEngine;
import coop.tecso.adpcore.AdpProcess;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunState;


public class AdpDao {

	static private Logger log = Logger.getLogger(AdpDao.class);
	static private Connection nativeConnection = null;
	public static String DATASOURCE_CONTEXT = "java:comp/env/ds/siat";
	/**
	 * Unico punto de dependencia con demoda.
	 * Es para obtener el nombre del usuario logueado
	 */
	static synchronized private String currentUserName() {
		try { 
			return coop.tecso.demoda.iface.helper.DemodaUtil.currentUserContext().getUserName();
		} catch (Exception e) {
			return "siat";
		}
	}
		
	/** 
	 * Uses JNDI and Datasource.
	 */
	static synchronized private Connection connectionJNDI() throws Exception {
		Connection result = null;
		try {
			
			Context initialContext = new InitialContext();
			if ( initialContext == null){
				log.debug("JNDI problem. Cannot get InitialContext.");
			}
			DataSource datasource = (DataSource)initialContext.lookup(DATASOURCE_CONTEXT);
			if (datasource != null) {
				//Throwable e = new Exception().fillInStackTrace();
				log.debug(coop.tecso.demoda.buss.dao.JDBCConnManager.dataSourceInfo(DATASOURCE_CONTEXT));			
				result = datasource.getConnection();
			    result.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}
			else {
				log.debug("Failed to lookup datasource.");
			}
			return result;
		} catch (Exception e) {
			if (result != null) result.close();
			throw e;
		}
	}

	static synchronized public Connection connection() throws Exception {
		return connectionJNDI();
	}

	/* Es terrible, pero sirve bien para ejecutar los test. */
	static synchronized public void close(Connection cn) throws Exception {
		try { cn.close(); } catch (Exception e) {}
	}
	
    /**
     *  Obtiene el Id Serializado de un PreparedStatement.
     *  @param PreparedStatement
     *  @return Long
     */
    static synchronized public long getSerial(PreparedStatement ps) throws Exception{
    	long runId = 0;
    	if(ps != null){		
    		ResultSet rs = ps.getGeneratedKeys();
    		while (rs.next()) {
    			runId = rs.getInt(1);	 
    	    }
    		rs.close();
    	}
    	return runId;
    }

  	 /**
  	  * Obtiene el siguiente valor para la Secuencia
  	  * En caso de no existir devuelve null 
  	  * @param nameSequence
  	  * @return Long
  	  */	
  	 static synchronized public long getNextVal(Connection cn, String nameSequence) throws Exception {
        Statement ps;
        String sql;
        ResultSet rs;
        long nextid = 0;
        
        //ActividadKey
        sql = "select nextval('" + nameSequence+"') as key";            
        ps = cn.createStatement();
        rs = ps.executeQuery(sql);
        if (rs.next()) {
            nextid = rs.getLong("key");
        }
        ps.close();
        rs.close();
    
        return nextid;
   }

	/**
	 * Carga los datos del proceso desde su process.getId()
	 * @param processId 
	 * @param process a cargar con id seteado
	 * @throws IllegalArgumentException si no existe processId
	 */
	static synchronized public void loadProcess(Long processId, AdpProcess process) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "select * from pro_proceso where id=?";
			PreparedStatement ps = cn.prepareStatement(sql);
			
			ps.setLong(1, processId);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				throw new java.lang.IllegalArgumentException("No se encontro id de proceso: " + processId);
			}
			while (rs.next()) {
				loadProcessFromRs(process, rs);
			}
			rs.close();
			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: loadProcess():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	static synchronized private void loadProcessFromRs(AdpProcess process, ResultSet rs) throws Exception {
		process.setId(rs.getLong("id"));
		process.setCantPasos(rs.getLong("cantPasos"));
		process.setClassForName(rs.getString("classForName"));
		process.setCodProceso(rs.getString("codProceso"));
		process.setDesProceso(rs.getString("desProceso"));
		process.setDirectorioInput(rs.getString("directorioInput"));
		process.setEsAsincronico(rs.getBoolean("esAsincronico"));
		process.setTipoProgEjecucion(rs.getLong("idTipoProgEjec"));
		process.setSpCancel(rs.getString("spCancel"));
		process.setSpExecute(rs.getString("spExecute"));
		process.setSpExecute(rs.getString("spExecute"));
		process.setSpResume(rs.getString("spResume"));
		process.setTipoEjecucion(rs.getLong("idTipoEjecucion"));
		process.setEjecNodo(rs.getString("ejecNodo"));
		process.setLocked(rs.getLong("locked"));
		process.setCronExpression(rs.getString("cronExpression"));
	}
	
	/**
	 * Carga una corrida desde su id
	 * Si runId es 0 o null no busca datos en la tabla corrida,
	 * Pero si carga los parametros.
	 * @throws IllegalArgumentException si no existe runId
	 */
	static synchronized public void loadRun(Long runId, AdpRun run) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			if (runId != null && runId != 0L) {
				String sql = "select * from pro_corrida where id=?";
				PreparedStatement ps = cn.prepareStatement(sql);		
				ps.setLong(1, runId);
				ResultSet rs = ps.executeQuery();
				if (!rs.isBeforeFirst()) {
					rs.close();
					ps.close();
					close(cn);
					throw new java.lang.IllegalArgumentException("No se encontro id de corrida: " + runId);
				}
				while (rs.next()) {
					run.setId(rs.getLong("id"));
					run.setProcessId(rs.getLong("idProceso"));
					run.setFechaInicio(rs.getTimestamp("fechaInicio"));
					run.setFechaFin(rs.getTimestamp("fechaFin"));
					run.setFechaUltResume(rs.getTimestamp("fechaUltResume"));
					run.setIdEstadoCorrida(rs.getLong("idEstadoCorrida"));
					run.setMensajeEstado(rs.getString("mensajeEstado"));
					run.setObservacion(rs.getString("observacion"));
					run.setDesCorrida(rs.getString("desCorrida"));
					run.setPasoActual(rs.getLong("pasoActual"));
					run.setUsuario(rs.getString("usuario"));
				}
				rs.close();
				ps.close();
				close(cn);
			}
		
			loadRunParameters(run);
		} catch (Exception ex) {
			log.debug("AdpDao: loadRun():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}	
		
	/* Carga los nombres de los parametros en la corrida. 
	 * Y los valores en caso de ser una corrida existente (id != 0) 
	 */
	static synchronized public void loadRunParameters(AdpRun run) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			//qry sobre los valores de los atributos que NO tiene relacion con idProcesoPar. */
			String sqlValues = "select ppv.* from pro_procesoparval as ppv where ppv.idcorrida = ? ";
			PreparedStatement ps = cn.prepareStatement(sqlValues);
			ps.setLong(1, run.getId());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String codParVal = rs.getString("codparval");
				String valor = rs.getString("strValor");
				log.debug("Loading param value:" + codParVal + " " + valor);
				AdpParameter param = run.getAdpParameter(codParVal);
				if (param == null) { // si todavia no existe lo agregamos. 
					param = new AdpParameter();
					param.setKey(codParVal);
					run.loadParameter(codParVal, param);
				}

				param.setTemporal(rs.getLong("esTemporal") == 1);
				param.setIdProcesoParVal(rs.getLong("id"));
				param.setValue(valor);
			}

			rs.close();
			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: loadRunParameters():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	/* Carga los nombres de los parametros en la corrida. 
	 * Y los valores en caso de ser una corrida existente (id != 0) 
	 */
	static synchronized private void loadRunParameters0(AdpRun run) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			/* qry sobre los parametros de un proceso. obtiene los parametros a nivel definicion */
			String sqlParam = "select atr.codAtributo, atr.desAtributo, pp.idAtributo, pp.* from" +
				" pro_procesopar as pp, def_atributo as atr where pp.idAtributo = atr.id and pp.idproceso = ?";
			PreparedStatement ps = cn.prepareStatement(sqlParam);
			ps.setLong(1, run.getProcessId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("load param:" + rs.getString("codAtributo"));
				AdpParameter param = new AdpParameter();
				param.setIdAtributo(rs.getLong("idAtributo"));
				//param.setIdProcesoPar(rs.getLong("id"));
				param.setKey(rs.getString("codAtributo"));
				param.setDescription(rs.getString("desAtributo"));
				run.loadParameter(param.getKey(), param);
			}
			rs.close();
			ps.close();
		
			if (run.getId() != 0) {
				//qry sobre los valores de los atributos que estan relacionados con idProcesoPar. Si es que existen. */
				String sqlValues = "select atr.codAtributo, atr.desAtributo, pp.idAtributo, pp.esdirectorioinput, ppv.* from" +
					" pro_procesopar as pp, def_atributo as atr, pro_procesoparval as ppv where" +
					" pp.idAtributo = atr.id and ppv.idprocesopar = pp.id and ppv.idcorrida=?";
				ps = cn.prepareStatement(sqlValues);
				ps.setLong(1, run.getId());
				rs = ps.executeQuery();

				while (rs.next()) {
					log.debug("Loading param value:" + rs.getString("codAtributo") + rs.getString("strValor"));
					AdpParameter param = run.getAdpParameter(rs.getString("codAtributo"));
					param.setIdProcesoParVal(rs.getLong("id"));
					param.setValue(rs.getString("strValor"));
				}
		
				rs.close();
				ps.close();
			}
		} catch (Exception ex) {
			log.debug("AdpDao: loadRunParameter0():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	
	/**
	 * Inserta los registros de la corrida en pro_corrida y sus valores de parametros en pro_procesoAtrVal.
	 * La corrida se crea en estado EnPreparacion por lo que ignora el valor de run.getIdEstadoCorrida()
	 * @param run instancia de una corrida. Al salir modifica id con el nuevo id creado y idEstadoCorrida 
	 * con valor PREPARACION
	 * @param params parametros
	 * @throws Exception en caso de que falle la creacion de una corrida.
	 */
	public static synchronized void createRun(AdpRun run, Map<String, AdpParameter> params) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			long runId = 0;
			String sql = "";
			sql += " insert into pro_corrida"; 
			sql += " (idproceso, fechainicio, fechafin, fechaultresume, idestadocorrida,";
			sql += " mensajeestado, observacion, pasoactual, usuario, fechaultmdf, estado, desCorrida, id)";
			sql += " values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			// insert run
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setLong(1, run.getProcess().getId());
			ps.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis())); // fecha inicio
			ps.setTimestamp(3, null); // fecha fin
			ps.setTimestamp(4, null); // fecha ultresume
			ps.setLong(5, AdpRunState.PREPARACION.id());
			ps.setString(6, run.getMensajeEstado());
			ps.setString(7, run.getObservacion());
			ps.setLong(8, run.getPasoActual());
			ps.setString(9, currentUserName());
			ps.setTimestamp(10, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			ps.setLong(11, 1L);
			ps.setString(12, run.getDesCorrida());
			
			runId = getNextVal(cn, "pro_corrida_id_seq");
			ps.setLong(13, runId);
			ps.executeUpdate();

			// insert value of parameters		
			for (AdpParameter p: params.values()) {
				updateParameterValue(runId, p);
			}

			close(cn);
			run.setId(runId);
		
			//inicializamos los ids de los parametros cargados
			loadRunParameters(run);
		} catch (Exception ex) {
			log.debug("AdpDao: createRun():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	/**
	 * Obtiene el id del proceso desde el codigo de proceso
	 * @param processName codProceso en la tabla pro_proceso
	 * @return id del proceso
	 * @throws Exception si no existe proceso con ese nombre. O si existen probelmas con JDBC
	 */
	public static synchronized Long getProcessId(String processName) throws Exception {
		Connection cn = AdpDao.connection();
		Long result = 0L;
		try {
			String sql = "select id from pro_proceso where codProceso=?";
			PreparedStatement ps = cn.prepareStatement(sql);
		
			ps.setString(1, processName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				throw new java.lang.IllegalArgumentException("No se encontro codigo de proceso: " + processName);
			}
			while (rs.next()) {
				result = rs.getLong("id");
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getProcessId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return result;
	}

	/**
	 * Elimina una corrida.
	 * @throws IllegalArgumentException si no existe runId
	 */
	static synchronized public Long deleteRun(Long runId) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
			PreparedStatement ps = null;		
			cn.setAutoCommit(false);
			
			//valores de los parametros
			sql = "delete from pro_procesoparval where idCorrida=?";
			ps = cn.prepareStatement(sql);
			ps.setLong(1, runId);
			ps.executeUpdate();
			ps.close();

			//log de la corrida
			sql = "delete from pro_logcorrida where idCorrida=?";
			ps = cn.prepareStatement(sql);		
			ps.setLong(1, runId);
			ps.executeUpdate();
			ps.close();
		
			//pasos de la corrida
			sql = "delete from pro_pasocorrida where idCorrida=?";
			ps = cn.prepareStatement(sql);		
			ps.setLong(1, runId);
			ps.executeUpdate();
			ps.close();

			//archivos de la corrida
			sql = "delete from pro_filecorrida where idCorrida=?";
			ps = cn.prepareStatement(sql);
			ps.setLong(1, runId);
			ps.executeUpdate();
			ps.close();
		
			//corrida
			sql = "delete from pro_corrida where id=?";
			ps = cn.prepareStatement(sql);		
			ps.setLong(1, runId);
			long result = ps.executeUpdate();
			ps.close();
			
			cn.commit();
			return (long)result;
		} catch (Exception ex) {
			log.debug("AdpDao: deleteRun():", ex);
			cn.rollback();
			throw ex;
		} finally {
			close(cn);
		}
	}

	/**
	 * Elimina los archivos fisicos de una corrida.
	 * @throws IllegalArgumentException si no existe runId
	 */
	static synchronized public void deleteFiles(Long runId) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
			PreparedStatement ps = null;		
			cn.setAutoCommit(false);

			//archivos de la corrida
			sql = "select * from pro_filecorrida where idCorrida=?";
			ps = cn.prepareStatement(sql);
			ps.setLong(1, runId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				try {
					String filename = (String) rs.getString("filename");
					if(filename != null) {
						File df = new File(filename);
						if (df.exists())
							df.delete();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			ps.close();
			rs.close();
		} catch (Exception ex) {
			log.debug("AdpDao: deleteFiles():", ex);
			cn.rollback();
			throw ex;
		} finally {
			close(cn);
		}
	}

	/**
	 * Obtiene una lista de los Procesos con idTipoEjecucion == 2 (Por arribo de archivos)
	 * @return lista de AdpProcess que funcionan por arribo de archivos
	 * @throws Exception alguna excepcion de jdbc
	 */
	static synchronized public List<AdpProcess> getFileArriveProcess() throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "select * from pro_proceso where idTipoEjecucion=?";
			PreparedStatement ps = cn.prepareStatement(sql);
			List<AdpProcess> result = new ArrayList<AdpProcess>();
		
			ps.setLong(1, AdpProcess.TIPO_ARRIBODIR);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				AdpProcess process = new AdpProcess();
				loadProcessFromRs(process, rs);
				result.add(process);
			}
			rs.close();
			ps.close();
			return result;
		} catch (Exception ex) {
			log.debug("AdpDao: getFileArriveProcess():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	public static synchronized void insertLog(Long idCorrida, Long paso, String msg) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
		
			// insert value of parameters
			sql = " insert into pro_logcorrida";
			sql+= " (idCorrida, fecha, paso, log, usuario, fechaultmdf, estado)";
			sql+= " values (?, ?, ?, ?, ?, ?, ?) ";
			PreparedStatement ps = cn.prepareStatement(sql);

			ps.setLong(1, idCorrida);
			ps.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			ps.setLong(3, paso);
			ps.setString(4, msg);
			ps.setString(5, currentUserName());
			ps.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			ps.setLong(7, 1L);
			ps.executeUpdate();
				
			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: insertLog():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	/**
	 * 
	 * @param run
	 */
	public static synchronized void updateSate(AdpRun run) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
		
			// update state
			sql = " update pro_corrida set ";
			sql+= " fechaInicio=?, fechaUltResume=?, fechaFin=?, idEstadoCorrida=?, mensajeEstado=?, pasoActual=?, usuario=?, fechaultmdf=?, estado=?";
			sql+= " , nodoOwner = ? where id = ?";
        
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setTimestamp(1, run.getFechaInicio() == null ? null : new Timestamp(run.getFechaInicio().getTime()));
			ps.setTimestamp(2, run.getFechaUltResume() == null ? null : new Timestamp(run.getFechaUltResume().getTime()));
			ps.setTimestamp(3, run.getFechaFin() == null ? null : new Timestamp(run.getFechaFin().getTime()));
			ps.setLong(4, run.getIdEstadoCorrida());
			ps.setString(5, run.getMensajeEstado());
			ps.setLong(6, run.getPasoActual());
			ps.setString(7, currentUserName());
			ps.setTimestamp(8, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			ps.setLong(9, 1L);
			if (run.getIdEstadoCorrida().longValue() == AdpRunState.PROCESANDO.id()) {
				ps.setString(10, AdpEngine.nodeName());
			} else {
				ps.setString(10, null);
			}
			ps.setLong(11, run.getId());
		
			ps.executeUpdate();
				
			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: updateState():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}
	
	public static synchronized void updateParameterValue(Long runId, AdpParameter p) throws Exception {
		Connection cn = AdpDao.connection();

		try {
			if (runId == null || runId.longValue() <= 0)
				return;
		
		
			if (p.getIdProcesoParVal().longValue() == 0) {
				// insert value of parameters
				String sql = " insert into pro_procesoParVal";
				sql+= " (codparval, idcorrida, strvalor, esTemporal, usuario, fechaultmdf, estado, id)";
				sql+= " values (?, ?, ?, ?, ?, ?, ?, ?) ";

				PreparedStatement ps = cn.prepareStatement(sql);
				ps.setString(1, p.getKey());
				ps.setLong(2, runId);
				ps.setString(3, p.getValue());			
				ps.setLong(4, p.isTemporal() ? 1 : 0);
				ps.setString(5, currentUserName());
				ps.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
				ps.setLong(7, 1L);
				
				long id = getNextVal(cn, "pro_procesoparval_id_seq");
				ps.setLong(8, id);
				ps.executeUpdate();

				p.setIdProcesoParVal(id);
				ps.close();
			} else {
				String sql = "";
				// update state
				sql = " update pro_procesoParVal set strValor=?, esTemporal=?, usuario=?, fechaUltMdf=?, estado=? ";
				sql+= " where id = ?";

				PreparedStatement ps = cn.prepareStatement(sql);

				ps.setString(1, p.getValue());
				ps.setLong(2, p.isTemporal() ? 1 : 0);
				ps.setString(3, currentUserName());
				ps.setTimestamp(4, new Timestamp(Calendar.getInstance().getTimeInMillis()));
				ps.setLong(5, 1L);
				ps.setLong(6, p.getIdProcesoParVal());
				ps.executeUpdate();
				ps.close();
			}
		} catch (Exception ex) {
			log.debug("AdpDao: updateParameterValue():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	/**
	 * Borra todos los parametros con banderas esTemporal true
	 * @param runId
	 * @throws Exception
	 */
	public static synchronized void deleteTempParameters(Long runId) throws Exception {
		Connection cn = AdpDao.connection();
		
		try {
			if (runId == null || runId.longValue() <= 0)
				return;

			// insert value of parameters
			String sql = "delete from pro_procesoParVal where idCorrida = ? and esTemporal = ? ";

			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setLong(1, runId);
			ps.setLong(2, 1);
			ps.executeUpdate();
			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: deleteTempParameters():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}


	/**
	 * Actualiza o inserta un pasoCorrida. Los valores de las columnas se copian de AdpRun.
	 * Excepto el estadoCorrida que se toma el del parametro
	 * @throws Exception
	 */
	public static synchronized void updateMensajePasoCorrida(AdpRun run, Long idEstadoCorrida) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
		
			// update state
			sql = " update pro_pasoCorrida set fechaCorrida=?, idEstadoCorrida=?, observacion=?, usuario=?, fechaUltMdf=? ";
			sql+= " where idCorrida = ? and paso = ?";

			PreparedStatement ps = cn.prepareStatement(sql);

			ps.setTimestamp(1, run.getFechaInicio() == null ? null : new Timestamp(run.getFechaInicio().getTime()));
			ps.setLong(2, idEstadoCorrida);
			ps.setString(3, run.getMensajeEstado());
			ps.setString(4, currentUserName()); 
			ps.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));

			ps.setLong(6, run.getId());
			ps.setLong(7, run.getPasoActual());
			int n = ps.executeUpdate();
			ps.close();

			// no modfico nada entonces hay insertar el registro.
			if (n == 0) {
				// insert value of parameters
				sql = " insert into pro_pasocorrida ";
				sql+= " (idCorrida, paso, fechaCorrida, idEstadoCorrida, observacion, usuario, fechaultmdf, estado) ";
				sql+= " values (?,?,?,?,?,?,?,?) ";

				ps = cn.prepareStatement(sql);

				ps.setLong(1, run.getId());
				ps.setLong(2, run.getPasoActual());
				ps.setTimestamp(3, run.getFechaUltResume() == null ? null : new Timestamp(run.getFechaUltResume().getTime()));
				ps.setLong(4, idEstadoCorrida);
				ps.setString(5, run.getMensajeEstado());
				ps.setString(6, currentUserName());
				ps.setTimestamp(7, new Timestamp(Calendar.getInstance().getTimeInMillis()));
				ps.setLong(8, 1);

				ps.executeUpdate();
			}

			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: updateMensajePasoCorrida():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	
	/**
	 * Actualiza o inserta un pasoCorrida. Los valores de las columnas se copian de AdpRun.
	 * Excepto el estadoCorrida que se toma el del parametro
	 * @throws Exception
	 */
	public static synchronized void updatePasoCorrida(AdpRun run, Long idEstadoCorrida) throws Exception {
		Connection cn = AdpDao.connection();
		String sql = "";
		
		try {
			// update state
			sql = " update pro_pasoCorrida set fechaCorrida=?, idEstadoCorrida=?, observacion=?, usuario=?, fechaUltMdf=? ";
			sql+= " where idCorrida = ? and paso = ?";

			PreparedStatement ps = cn.prepareStatement(sql);

			ps.setTimestamp(1, run.getFechaInicio() == null ? null : new Timestamp(run.getFechaInicio().getTime()));
			ps.setLong(2, idEstadoCorrida);
			ps.setString(3, run.getMensajeEstado());
			ps.setString(4, currentUserName()); 
			ps.setTimestamp(5, new Timestamp(Calendar.getInstance().getTimeInMillis()));

			ps.setLong(6, run.getId());
			ps.setLong(7, run.getPasoActual());
			int n = ps.executeUpdate();
			ps.close();

			// no modfico nada entonces hay insertar el registro.
			if (n == 0) {
				// insert value of parameters
				sql = " insert into pro_pasocorrida ";
				sql+= " (idCorrida, paso, fechaCorrida, idEstadoCorrida, observacion, usuario, fechaultmdf, estado) ";
				sql+= " values (?,?,?,?,?,?,?,?) ";

				ps = cn.prepareStatement(sql);

				ps.setLong(1, run.getId());
				ps.setLong(2, run.getPasoActual());
				ps.setTimestamp(3, run.getFechaInicio() == null ? null : new Timestamp(run.getFechaInicio().getTime()));
				ps.setLong(4, idEstadoCorrida);
				ps.setString(5, run.getMensajeEstado());
				ps.setString(6, currentUserName());
				ps.setTimestamp(7, new Timestamp(Calendar.getInstance().getTimeInMillis()));
				ps.setLong(8, 1);

				ps.executeUpdate();
			}

			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: updatePasoCorrida():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	/**
	 * Retorna una lista de todos las corridas en estado En espera Comenzar de 
	 * con fechaInicio lista para ejecutar
	 * @param now fecha limite a considerar para el inicio de la ejecucion
	 * @return
	 */
	public static synchronized List<Long> getReadyRuns(Date now) throws Exception {
		Connection cn = AdpDao.connection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Long> result = new ArrayList<Long>();
		try {
		
			String sql = "select id from pro_corrida where idEstadoCorrida = 2 and fechaInicio <= ? order by fechaInicio asc";
			ps = cn.prepareStatement(sql);
			ps.setTimestamp(1, new Timestamp(now.getTime()));
			
			rs = ps.executeQuery();
			while (rs.next()) {
				Long id = new Long(rs.getLong("id"));
				result.add(id);
			}
			// aqui invertimos el orden para que los fechaInicio == null aparezcan primero para ser los primeros en procesar.
			Collections.reverse(result);
		} catch (Exception e) {
			log.error("Error al intentar determinar si existen corridas por ejecutar.", e);
			throw e;
		} finally {
			rs.close();
			ps.close();
			close(cn);
		}
		return result;
	}


	/**
	 * Retorna una lista de id de todas las corridas en estado procesando del nodo, pasado como paremetro. 
	 * @param nodoName, nombre del nodo con el cual se quiere verificar la ejecucion.
	 * @return
	 */
	public static synchronized List<Long> getZombiesRuns(String nodeName) throws Exception {
		Connection cn = AdpDao.connection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Long> result = new ArrayList<Long>();
		try {
			String sql = "select id from pro_corrida where idEstadoCorrida = 4 and nodoowner = ? order by fechaInicio desc";
			ps = cn.prepareStatement(sql);
			ps.setString(1, nodeName);
			
			rs = ps.executeQuery();
			while (rs.next()) {
				Long id = new Long(rs.getLong("id"));
				result.add(id);
				log.debug("getZombiesRuns(): idCorrida:" + id);
			}
		} catch (Exception e) {
			log.error("Error al intentar determinar si existen corridas en ejecucion.", e);
			throw e;
		} finally {
			rs.close();
			ps.close();
			close(cn);
		}
		return result;
	}

	public static synchronized void updateMessage(Long runId, String message) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
			// update state
			sql = " update pro_corrida set mensajeEstado=?, usuario=?, fechaUltMdf=?";
			sql+= " where id = ?";

			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, message);
			ps.setString(2, currentUserName());
			ps.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			ps.setLong(4, runId);
			ps.executeUpdate();
			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: updateMessage():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	public static synchronized void updateLock(String processName, Long value) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
			// update state
			sql = " update pro_proceso set locked=? where codProceso = ?";
			sql+= " where id = ?";

			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setLong(1, value);
			ps.setString(2, processName);
			int n = ps.executeUpdate();
			ps.close();
			if (n == 0)
				throw new java.lang.IllegalArgumentException("No se encontro codigo de proceso: " + processName);
		} catch (Exception ex) {
			log.debug("AdpDao: updateLock():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	public static synchronized void updateDescription(Long runId, String des) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
			// update state
			sql = " update pro_corrida set desCorrida=?, usuario=?, fechaUltMdf=?";
			sql+= " where id = ?";

			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, des);
			ps.setString(2, currentUserName());
			ps.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			ps.setLong(4, runId);
			ps.executeUpdate();
			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: updateDescription():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	public static synchronized void updateObs(Long runId, String obs) throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "";
			// update state
			sql = " update pro_corrida set observacion=?, usuario=?, fechaUltMdf=?";
			sql+= " where id = ?";

			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, obs);
			ps.setString(2, currentUserName());
			ps.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			ps.setLong(4, runId);
			ps.executeUpdate();
			ps.close();
		} catch (Exception ex) {
			log.debug("AdpDao: updateObs():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}

	
	/**
	 * Obtiene el id de la ultima corrida que termino OK para el codigo de proceso
	 * @param processName codProceso en la tabla pro_proceso
	 * @return idRun y null si no se encontro ninguno
	 */
	public static synchronized Long getLastRunEndOkIdByCodProcess(String processName) throws Exception {
		Connection cn = AdpDao.connection();
		Long result = 0L;
		try {
			String sql = "select max(c.id) as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and c.idestadocorrida=? ";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setLong(2, AdpRunState.FIN_OK.id());
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				return null;
			}
			while (rs.next()) {
				result = rs.getLong("runId");
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getLastRunEndOkId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return result;
	}

	/**
	 * Obtiene el id de la ultima corrida que termino OK para el codigo de proceso y el usuario indicado.
	 * @param processName codProceso en la tabla pro_proceso
	 * @return idRun y null si no se encontro ninguno
	 */
	public static synchronized Long getLastRunEndOkIdByCodProcess(String processName, String userName) throws Exception {
		Connection cn = AdpDao.connection();
		Long result = 0L;
		try {
			String sql = "select max(c.id) as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and c.idestadocorrida=? ";
			sql += " and c.usuario=? ";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setLong(2, AdpRunState.FIN_OK.id());
			ps.setString(3, userName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				return null;
			}
			while (rs.next()) {
				result = rs.getLong("runId");
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getLastRunEndOkId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return result;
	}
	
	/**
	 * Obtiene el id de la corrida que esta en procesando o por comenzar para el codigo de proceso
	 * @param processName codProceso en la tabla pro_proceso
	 * @return idRun y 0 si no se encontro ninguno
	 */
	public static synchronized Long getRunningRunIdByCodProcess(String processName) throws Exception {
		Connection cn = AdpDao.connection();
		Long result = 0L;
		try {
			String sql = "select max(c.id) as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and (c.idestadocorrida=? ";
			sql += " or c.idestadocorrida=?) ";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setLong(2, AdpRunState.PROCESANDO.id());
			ps.setLong(3, AdpRunState.ESPERA_COMENZAR.id());
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				return null;
			}
			while (rs.next()) {
				result = rs.getLong("runId");
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getRunningRunId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return result;
	}
	
	/**
	 * Obtiene el id de la corrida que esta en procesando o por comenzar para el codigo de proceso y el usuario indicado
	 * @param processName codProceso en la tabla pro_proceso
	 * @return idRun y null si no se encontro ninguno
	 */
	public static synchronized Long getRunningRunIdByCodProcess(String processName, String userName) throws Exception {
		Connection cn = AdpDao.connection();
		Long result = 0L;
		try {
			String sql = "select max(c.id) as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and (c.idestadocorrida=? ";
			sql += " or c.idestadocorrida=?) ";
			sql += " and c.usuario=? ";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setLong(2, AdpRunState.PROCESANDO.id());
			ps.setLong(3, AdpRunState.ESPERA_COMENZAR.id());
			ps.setString(4, userName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				return null;
			}
			while (rs.next()) {
				result = rs.getLong("runId");
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getRunningRunId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return result;
	}
	
	/**
	 * Obtiene la lista de id de las corridas para el codigo de proceso sin incluir la pasada como parametro.
	 * @param processName codProceso en la tabla pro_proceso
	 * @param runId
	 * @return listIdRun y null si no se encontro ninguno
	 */
	public static synchronized List<Long> getListOldestRunId(String processName, Long runIdAExcluir) throws Exception {
		Connection cn = AdpDao.connection();
		List<Long> listResult = new ArrayList<Long>();
		try {
			String sql = "select c.id as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and c.id <> ? ";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setLong(2, runIdAExcluir);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				return null;
			}
			while (rs.next()) {
				listResult.add(rs.getLong("runId"));
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getListOldestRunId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return listResult;
	}

	/**
	 * Obtiene la lista de id de las corridas para el codigo de proceso y el usuario indicado sin incluir la pasada como parametro.
	 * @param processName codProceso en la tabla pro_proceso
	 * @param runId
	 * @return listIdRun y null si no se encontro ninguno
	 */
	public static synchronized List<Long> getListOldestRunId(String processName, Long runIdAExcluir, String userName) throws Exception {
		Connection cn = AdpDao.connection();
		List<Long> listResult = new ArrayList<Long>();
		try {
			String sql = "select c.id as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and c.id <> ? and c.usuario=?";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setLong(2, runIdAExcluir);
			ps.setString(3, userName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				return null;
			}
			while (rs.next()) {
				listResult.add(rs.getLong("runId"));
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getListOldestRunId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return listResult;
	}
	
	/**
	 * Obtiene el id de la ultima corrida que termino con error o abortado para el codigo de proceso
	 * @param processName codProceso en la tabla pro_proceso
	 * @return idRun y null si no se encontro ninguno
	 */
	public static synchronized Long getLastRunEndWrongIdByCodProcess(String processName) throws Exception {
		Connection cn = AdpDao.connection();
		Long result = 0L;
		try {
			String sql = "select max(c.id) as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and (c.idestadocorrida=? or c.idestadocorrida=?)";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setLong(2, AdpRunState.FIN_ERROR.id());
			ps.setLong(3, AdpRunState.ABORT_EXCEP.id());
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				return null;
			}
			while (rs.next()) {
				result = rs.getLong("runId");
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getLastRunEndWrongId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return result;
	}
	
	/**
	 * Obtiene el id de la ultima corrida que termino con error o abortado para el codigo de proceso y el usuario indicado.
	 * @param processName codProceso en la tabla pro_proceso
	 * @return idRun y null si no se encontro ninguno
	 */
	public static synchronized Long getLastRunEndWrongIdByCodProcess(String processName, String userName) throws Exception {
		Connection cn = AdpDao.connection();
		Long result = 0L;
		try {
			String sql = "select max(c.id) as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and (c.idestadocorrida=? or c.idestadocorrida=?)";
			sql += " and c.usuario=? ";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setLong(2, AdpRunState.FIN_ERROR.id());
			ps.setLong(3, AdpRunState.ABORT_EXCEP.id());
			ps.setString(4, userName);
			ResultSet rs = ps.executeQuery();
			if (!rs.isBeforeFirst()) {
				rs.close();
				ps.close();
				close(cn);
				return null;
			}
			while (rs.next()) {
				result = rs.getLong("runId");
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getLastRunEndWrongId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return result;
	}
	
	/**
	 * Obtiene una lista de los Procesos con idTipoEjecucion == 3 (Ejecucion Periodica)
	 * @return lista de AdpProcess que funcionan por ejecucion periodica
	 * @throws Exception alguna excepcion de jdbc
	 */
	static synchronized public List<AdpProcess> getPeriodicProcess() throws Exception {
		Connection cn = AdpDao.connection();
		try {
			String sql = "select * from pro_proceso where idTipoEjecucion=?";
			PreparedStatement ps = cn.prepareStatement(sql);
			List<AdpProcess> result = new ArrayList<AdpProcess>();
		
			ps.setLong(1, AdpProcess.TIPO_PERIODIC);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				AdpProcess process = new AdpProcess();
				loadProcessFromRs(process, rs);
				result.add(process);
			}
			rs.close();
			ps.close();
			return result;
		} catch (Exception ex) {
			log.debug("AdpDao: getPeriodicProcess():", ex);
			throw ex;
		} finally {
			close(cn);
		}
	}
	
	/**
	 * Obtiene el id de la ultima corrida que termino con error o abortado para el codigo de proceso y el usuario indicado.
	 * @param processName codProceso en la tabla pro_proceso
	 * @return idRun y null si no se encontro ninguno
	 */
	public static synchronized Long getLastRunIdByCodProcess(String processName, String userName) throws Exception {
		Connection cn = AdpDao.connection();
		Long result = null;
		try {
			String sql = "select max(c.id) as runId from pro_corrida c, pro_proceso p ";
			sql += " where c.idProceso=p.id and p.codProceso=? ";
			sql += " and c.usuario=? ";
			PreparedStatement ps = cn.prepareStatement(sql);
			ps.setString(1, processName);
			ps.setString(2, userName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getLong("runId");
			}
			rs.close();
			ps.close();
			close(cn);
		} catch (Exception ex) {
			log.debug("AdpDao: getLastRunEndWrongId():", ex);
			throw ex;
		} finally {
			close(cn);
		}
		return result;
	}
	

}
