//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.gde.buss.bean.ProMasDeuExc;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoReportesDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.SelAlmVO;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

public class ProMasDeuExcDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProMasDeuExcDAO.class);	

	public ProMasDeuExcDAO() {
		super(ProMasDeuExc.class);
	}

	/**
	 * Exporta los reportes de deuda excluida del Envio Judicial
	 * @param  idProcesoMasivo
	 * @return List<PlanillaVO>
	 * @throws Exception
	 */
	public List<PlanillaVO> exportReportesDeudaExcluidaByProcesoMasivo(Long idProcesoMasivo) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// es necesario volver a recuperar el proceso masivo porque por procesamientos previos afectan al de sesion
		ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(idProcesoMasivo);
		String desRecurso = procesoMasivo.getRecurso().getCodRecurso();
		boolean deudaSigueTitular = Integer.valueOf(1).equals(procesoMasivo.getRecurso().getEsDeudaTitular());
		
		// ahora nos conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();

		int indiceArchivo = 0;
		//genero el archivo de texto
		
		// formacion del fileName con el directorio de procesamiento de la corrida del envio judicial 
		String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
		String idProMas = StringUtil.formatLong(procesoMasivo.getId());
		String fileName = processDir + File.separator + ProcesoMasivoReportesDeudaAdapter.FILE_NAME_DEUDA + "Exc_" + idProMas + "_" + indiceArchivo + ".csv";

		PlanillaVO planillaVO = new PlanillaVO(fileName);
		planillaVO.setTitulo(SelAlmVO.DEUDA_EXCLUIDA); // identificara el nombre del FileCorrida que se creara 
		listPlanilla.add(planillaVO);
		
		// Creacion del Buffer con su Encabezado del Resultado
		BufferedWriter buffer = crearEncabezadoProMasDeuExc(fileName);
		
		// --> Resultado
		boolean resultadoVacio = false;
		long c = 0;                     // contador de registros
		Map<Long, Long> idDeudaMap = new HashMap<Long, Long>();
		
		Long idTipoSelAlmDet = TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM;
		String tablaDeuda ="gde_deudaAdmin";
		if (procesoMasivo.getViaDeuda().getEsViaJudicial()){
			idTipoSelAlmDet = TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD;
			tablaDeuda = "gde_deudaJudicial";
		}

		String queryString = "SELECT pro.id idProcurador, pro.descripcion, me.desmotexc , cta.numerocuenta, ejde.desTitulPrincipal , da.anio, " +
		" da.periodo, rcd.descladeu, da.fechavencimiento, da.importe, da.saldo, da.id idDeuda " +
		(deudaSigueTitular ? " , tit.idcontribuyente " : "") +
		" FROM gde_proMasDeuExc ejde " +
		" INNER JOIN gde_motexc me ON (ejde.idmotexc == me.id) " +
		" INNER JOIN "+ tablaDeuda + " da ON (ejde.iddeuda == da.id) " +
		" LEFT JOIN gde_procurador pro ON (ejde.idprocurador == pro.id) " +
		" INNER JOIN pad_cuenta cta ON (da.idcuenta == cta.id) " +
		" INNER JOIN def_recClaDeu rcd ON (da.idreccladeu == rcd.id) " +
		(deudaSigueTitular ? " LEFT JOIN pad_cuentatitular tit ON (cta.id == tit.idcuenta " +
				"and tit.fechaDesde <= da.fechaVencimiento " +
				"and (tit.fechaHasta is null or tit.fechaHasta >= da.fechaVencimiento) )" : "") + 
		" WHERE ejde.idProcesoMasivo = " + idProcesoMasivo +
		" AND ejde.idTipoSelAlmDet = " + idTipoSelAlmDet +
		" ORDER BY cta.numerocuenta, da.anio, da.periodo " + 
		(deudaSigueTitular ? " , tit.esTitularPrincipal desc, tit.fechaDesde, tit.id "  : "");

		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			String desTitular = "";
			resultadoVacio = false;

			Long idDeuda = rs.getLong("idDeuda");
			if (deudaSigueTitular) {
				if (idDeudaMap.containsKey(idDeuda))
					continue;
				
				idDeudaMap.put(idDeuda, idDeuda);
			}
			
			// procurador, recurso, cuenta, titular principal, anio, periodo, clasificacion de la deuda, fechaVencimiento, importe emitido, saldo historico, saldo actualizado	 		
			// procurador
			//buffer.write(proMasDeuExc.getProcurador().getDescripcion());
			String idProc = rs.getString("idProcurador");
			buffer.write(idProc == null?" ":idProc );
			buffer.write(",");

			String descProc = rs.getString("descripcion");
			buffer.write(descProc == null?" ":descProc );
			// recurso
			buffer.write(", " + desRecurso);  
			// numeroCuenta
			buffer.write(", " +  rs.getString("numerocuenta"));
		
			// titularPrincipal
			if (deudaSigueTitular) {
				Long idContr = rs.getLong("idContribuyente");
				Persona persona = Persona.getByIdLight(idContr);
				if (persona != null) {
					desTitular = persona.getRepresent();
				} else {
					desTitular = "Sin Datos: idPersona = " + idContr;
				}
			} else {
				desTitular = rs.getString("desTitulPrincipal");			
			}
			buffer.write(", " +  desTitular);
			
			// anio
			buffer.write(", " +  rs.getLong("anio"));
			// periodo
			buffer.write(", " +  rs.getInt("periodo"));
			// clasificacion de la deuda
			buffer.write(", " +  rs.getString("descladeu"));
			// fecha de vencimiento
			buffer.write(", " + DateUtil.formatDate(rs.getDate("fechavencimiento"), DateUtil.dd_MM_YYYY_MASK));
			// importe
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("importe"),SiatParam.DEC_IMPORTE_DB));
			// saldo
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("saldo"),SiatParam.DEC_IMPORTE_DB));
			// desmotexc
			buffer.write(", " +  rs.getString("desmotexc"));
			// saldo actualizado NO LO MUESTRO
 
			c++;
			if(c == 30000 ){ // incluyendo a las filas del encabezado y considera que c arranca en uno
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();
				planillaVO.setCtdResultados(c); 
				indiceArchivo++;
				fileName = processDir + File.separator + ProcesoMasivoReportesDeudaAdapter.FILE_NAME_DEUDA + "Exc_" + idProMas + "_" + indiceArchivo + ".csv";
				planillaVO = new PlanillaVO(fileName);
				planillaVO.setTitulo(SelAlmVO.DEUDA_EXCLUIDA); // identificara el nombre del FileCorrida que se creara
				listPlanilla.add(planillaVO);
				
				buffer = crearEncabezadoProMasDeuExc(fileName);
				c = 0; // reinicio contador 
			}else{
				// crea una nueva linea
				buffer.newLine();
			}
		} // fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Deudas a Excluir"  );
		}		 
		// <-- Fin Resultado vacio
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		planillaVO.setCtdResultados(c);
		buffer.close();

		return listPlanilla;
	}
	
	public List<PlanillaVO> exportReportesConvenioCuotaExcluidoByProcesoMasivo(Long idProcesoMasivo) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;
		
		// es necesario volver a recuperar el proceso masivo porque por procesamientos previos afectan al de sesion
		ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(idProcesoMasivo);
		String desRecurso = procesoMasivo.getRecurso().getCodRecurso();

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();

		int indiceArchivo = 0;
		//genero el archivo de texto
		
		// formacion del fileName con el directorio de procesamiento de la corrida del envio judicial 
		String processDir = AdpRun.getRun(procesoMasivo.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
		String fileName = processDir + File.separator + ProcesoMasivoReportesDeudaAdapter.FILE_NAME_CONV_CUOTA + "Exc_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";

		PlanillaVO planillaVO = new PlanillaVO(fileName);
		planillaVO.setTitulo(SelAlmVO.CONV_CUOTA_EXCLUIDA); // identificara el nombre del FileCorrida que se creara
		listPlanilla.add(planillaVO);

		// --> Creacion del Buffer con su Encabezado del Resultado
		BufferedWriter buffer = crearEncabezadoProMasConvCuoExc(fileName);

		// --> Resultado
		boolean resultadoVacio = false;
		//int indiceSkip      = 0;        // inicializo el skip  
		//int incrementoSkip  = 100;      // incremento del skip
		long c = 0;                     // contador de registros

		// Determinacion del tipoSelAlmDet de acuerdo a la via de la deuda del proceso masivo
		Long idTipoSelAlmDet = TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_ADM;
		if (procesoMasivo.getViaDeuda().getEsViaJudicial()){
			idTipoSelAlmDet = TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_JUD;
		}
		
		String queryString = "SELECT pro.id idProcurador, pro.descripcion, cta.numerocuenta, pmde.desTitulPrincipal, conv.nroConvenio, cc.numerocuota, " +
			"cc.fechavencimiento, cc.capitalcuota, cc.interes, cc.importecuota, cc.actualizacion, me.desMotExc " +
			"FROM gde_proMasDeuExc pmde " +
			"INNER JOIN gde_conveniocuota cc ON (pmde.iddeuda = cc.id) " + 
			"INNER JOIN gde_convenio conv ON (cc.idconvenio = conv.id) " +
			" LEFT JOIN gde_procurador pro ON (conv.idprocurador = pro.id) " +
			" INNER JOIN pad_cuenta cta ON (conv.idcuenta = cta.id) " +
			" INNER JOIN gde_motexc me ON (pmde.idMotExc = me.id) " +
			" WHERE pmde.idProcesoMasivo = "+ idProcesoMasivo + 
			" AND pmde.idTipoSelAlmDet = "  + idTipoSelAlmDet +
			" ORDER BY cta.numerocuenta, conv.nroConvenio, cc.numerocuota";
		
		if (log.isDebugEnabled()) log.debug("queryString: " + queryString);

		// ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();

		while(rs.next()){
			resultadoVacio = false;
			
			// pro.descripcion, cta.numerocuenta, ejdi.desTitulPrincipal, cc.numerocuota,
			// cc.fechavencimiento, cc.capitalcuota, cc.interes, cc.importecuota, cc.actualizacion, cc.desMotExc 	 		
			// procurador
			String idProc = rs.getString("idProcurador");
			buffer.write(idProc == null?" ":idProc );
			buffer.write(",");
			
			String desc = rs.getString("descripcion");
			buffer.write(desc==null?"":desc );
			// recurso
			buffer.write(", " + desRecurso);  // por ahora no agregado al select
			// numeroCuenta
			buffer.write(", " +  rs.getString("numeroCuenta"));
			// titularPrincipal
			buffer.write(", " +  rs.getString("desTitulPrincipal"));
			// numeroConvenio
			buffer.write(", " +  rs.getLong("nroConvenio"));
			// numeroCuota
			buffer.write(", " +  rs.getLong("numeroCuota"));
			// fecha de vencimiento
			buffer.write(", " + DateUtil.formatDate(rs.getDate("fechaVencimiento"), DateUtil.dd_MM_YYYY_MASK));
			// cc.capitalcuota, 
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("capitalCuota"),SiatParam.DEC_IMPORTE_DB));
			// cc.interes, 
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("interes"),SiatParam.DEC_IMPORTE_DB));
			// cc.importecuota, 
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("importeCuota"),SiatParam.DEC_IMPORTE_DB));
			// cc.actualizacion
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("actualizacion"),SiatParam.DEC_IMPORTE_DB));
			// cc.actualizacion
			buffer.write(", " + NumberUtil.truncate(rs.getDouble("actualizacion"),SiatParam.DEC_IMPORTE_DB));
			// cc.desMotExc
			buffer.write(", " + rs.getString("desMotExc"));
			
			c++;
			if(c == 30000 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
				// cierra el buffer, genera una nueva planilla
				if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
				buffer.close();
				planillaVO.setCtdResultados(c); 
				indiceArchivo++;
				fileName = processDir + File.separator + ProcesoMasivoReportesDeudaAdapter.FILE_NAME_CONV_CUOTA + "Exc_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
				planillaVO = new PlanillaVO(fileName);
				listPlanilla.add(planillaVO);
				buffer = crearEncabezadoProMasConvCuoExc(fileName);
				c = 0; // reinicio contador 
			}else{
				// crea una nueva linea
				buffer.newLine();
			}

		} // fin del recorrida del ResultSet
		rs.close();
		ps.close();
		// <-- Fin Resultado

		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Cuotas de Convenio a Excluir"  );
		}		 
		// <-- Fin Resultado vacio
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + c);
		planillaVO.setCtdResultados(c);
		buffer.close();

		return listPlanilla;
	}

	
	/**
	 * Creacion del encabezado del archivo de deuda enviada por periodo
	 * @param  fileName
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter crearEncabezadoProMasDeuExc(String fileName) throws Exception{

		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// procurador, recurso, cuenta, titular principal, anio, periodo, clasificacion de la deuda, fechaVencimiento, importe emitido, saldo historico, saldo actualizado

		// procurador
		buffer.write("Nro.Procur");
		// procurador
		buffer.write(", Procurador");
		// recurso
		buffer.write(", Recurso");
		// cuenta
		buffer.write(", Cuenta");
		// titular Principal
		buffer.write(", Titular Principal");
		// anio
		buffer.write(", Anio");
		// periodo
		buffer.write(", Periodo");
		// clasificacion de la deuda
		buffer.write(", Clasific. Deuda");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		// importe emitido
		buffer.write(", Importe");
		// saldo historico: lo saco de la deuda
		buffer.write(", Saldo Hist.");
		// saldo actualizado a la fecha de envio: NO LO MUESTRO
		//desmotexc
		buffer.write(", Motivo Exclusion");
		
		buffer.newLine();

		return buffer;
	}
	
	private BufferedWriter crearEncabezadoProMasConvCuoExc(String fileName) throws Exception{
		 
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// procurador
		buffer.write("Nro.Procur");
		// procurador
		buffer.write(", Procurador");
		// recurso
		buffer.write(", Recurso");
		// cuenta
		buffer.write(", Cuenta");
		// titular Principal
		buffer.write(", Titular Principal");
		// nroConvenio
		buffer.write(", Nro. Convenio");
		// numeroCuota
		buffer.write(", Nro. Cuota");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		// capitalCuota
		buffer.write(", Capital");
		// interes
		buffer.write(", Interes");
		// importe
		buffer.write(", Importe");
		// actualizacion
		buffer.write(", Actualización");
		//desmotexc
		buffer.write(", Motivo Exclusion");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();

		return buffer;
	}


	/**
	 * Determina si contiene Deudas Excluidas el Envio Judicial
	 * @param  procesoMasivo
	 * @return boolean
	 */
	public boolean contieneDeudasExcluidas(ProcesoMasivo procesoMasivo){
		
		String queryString = "FROM ProMasDeuExc ejde WHERE ejde.procesoMasivo = :procesoMasivo ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query   query   = session.createQuery(queryString).setEntity("procesoMasivo", procesoMasivo);
		query.setMaxResults(1);
		
		return (query.list().size() > 0);
	}
	
	/**
	 * Obtiene la lista de ProMasDeuExc para el Envio Judicial de manera paginada 
	 * @param  procesoMasivo
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<ProMasDeuExc>
	 */
	public List<ProMasDeuExc> getListByProcesoMasivo(ProcesoMasivo procesoMasivo, Integer firstResult, Integer maxResults ){

		String queryString = " FROM ProMasDeuExc ejde  " +
		"WHERE ejde.procesoMasivo = :procesoMasivo";
		// TODO ver si ordenamos por procurador

		Session session = currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createQuery(queryString).setEntity("procesoMasivo", procesoMasivo);

		if (firstResult != null){
			query.setFirstResult(firstResult);
		}
		if (maxResults != null){
			query.setMaxResults(maxResults);
		}

		return (ArrayList<ProMasDeuExc>) query.list();
	}

	/**
	 * Borra los ProMasDeuExc del Envio Judicial
	 * @param  procesoMasivo
	 * @return int  
	 */
	public int deleteListByProcesoMasivo(ProcesoMasivo procesoMasivo){
	
		String queryString = "DELETE FROM ProMasDeuExc ejde WHERE ejde.procesoMasivo = :procesoMasivo ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		query.setEntity("procesoMasivo", procesoMasivo);
	    
		return query.executeUpdate();
	}

}
