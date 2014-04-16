//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.LiqCom;
import ar.gov.rosario.siat.gde.buss.bean.LiqComPro;
import ar.gov.rosario.siat.gde.buss.bean.ProRec;
import ar.gov.rosario.siat.gde.buss.bean.ProRecCom;
import ar.gov.rosario.siat.gde.iface.model.LiqComSearchPage;
import ar.gov.rosario.siat.gde.iface.model.LiqComVO;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class LiqComDAO extends GenericDAO {

	private Log log = LogFactory.getLog(LiqComDAO.class);	
	
	public LiqComDAO() {
		super(LiqCom.class);
	}
	
	public List<LiqCom> getBySearchPage(LiqComSearchPage liqComSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from LiqCom t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del LiqComSearchPage: " + liqComSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (liqComSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui	
		
		// filtro liqCom excluidos
 		List<LiqComVO> listLiqComExcluidos = (List<LiqComVO>) liqComSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listLiqComExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listLiqComExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		

		// filtro por servicio banco
 		if (!ModelUtil.isNullOrEmpty(liqComSearchPage.getLiqCom().getServicioBanco())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.servicioBanco.id= "+liqComSearchPage.getLiqCom().getServicioBanco().getId();
			flagAnd = true;
		}
 		
		// filtro por recurso
 		if (!ModelUtil.isNullOrEmpty(liqComSearchPage.getLiqCom().getRecurso())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id= "+liqComSearchPage.getLiqCom().getRecurso().getId();
			flagAnd = true;
		}

		// filtro por procurador
 		if (!ModelUtil.isNullOrEmpty(liqComSearchPage.getLiqCom().getProcurador())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.procurador.id="+liqComSearchPage.getLiqCom().getProcurador().getId();
			flagAnd = true;
		}
 		
 		// 	filtro por estado proceso
 		if (!ModelUtil.isNullOrEmpty(liqComSearchPage.getLiqCom().getCorrida().getEstadoCorrida())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida.id="+liqComSearchPage.getLiqCom().getCorrida().getEstadoCorrida().getId();
			flagAnd = true;
		}

 		// filtro por fecha liq desde
 		Date fechaDesde = liqComSearchPage.getFechaLiqDesde();
 		if (fechaDesde != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaLiquidacion >= TO_DATE('" + 
				DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		}

 		// filtro por fecha liq Hasta
 		Date fechaHasta = liqComSearchPage.getFechaLiqHasta();
 		if (fechaHasta != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fechaLiquidacion <= TO_DATE('" + 
				DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			
			flagAnd = true;
		} 		 		 	
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<LiqCom> listLiqCom = (ArrayList<LiqCom>) executeCountedSearch(queryString, liqComSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listLiqCom;
	}

	/**
	 * Obtiene un LiqCom por su codigo
	 */
	public LiqCom getByCodigo(String codigo) throws Exception {
		LiqCom liqCom;
		String queryString = "from LiqCom t where t.codLiqCom = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		liqCom = (LiqCom) query.uniqueResult();	

		return liqCom; 
	}



	
	/**
	 * Escribe la parte del convenio en el buffer pasado como parametro
	 * @param buffer
	 * @param nroConvenio
	 * @param nroCuota
	 * @param nroCuotaImputada
	 * @param importeAcum
	 * @param actAcum
	 * @throws IOException
	 */
	private void escribirDatosConvenioCuota(BufferedWriter buffer, Integer nroConvenio, Integer nroCuota, Integer nroCuotaImputada,
											Double importeAcum, Double actAcum) throws IOException{
		buffer.write(String.valueOf(nroConvenio.intValue()));
		buffer.write(";");
		buffer.write(String.valueOf(nroCuota.intValue()));
		buffer.write(";");
		buffer.write(String.valueOf(nroCuotaImputada.intValue()));
		buffer.write(";");
		
		// escribir el importe acumulado
		buffer.write(StringUtil.redondearDecimales(importeAcum, 1, 2));
		buffer.write(";");
		
		// escribir la act acumulada
		buffer.write(StringUtil.redondearDecimales(actAcum, 1, 2));
		buffer.write(";");
		
		// escribir el total
		buffer.write(StringUtil.redondearDecimales(importeAcum+actAcum, 1, 2));
		buffer.write(";");
	}
	
	private void escribirDatosDeuda(BufferedWriter buffer, LiqComPro liqComPro, String periodoDeuda, Date fechaVtoDeuda,
									Date fechaVigencia, Double importeAplicado, Long anio) throws IOException{
		//imprime la deuda
		buffer.write(periodoDeuda);
		buffer.write(";");
		buffer.write(DateUtil.formatDate(fechaVtoDeuda, DateUtil.ddSMMSYYYY_MASK));
		buffer.write(";");
		
		// % de comision a la fecha de la deuda
		ProRec proRec = ProRec.getByIdProcuradorRecurso(liqComPro.getProcurador().getId(),
												liqComPro.getLiqCom().getRecurso().getId());
		ProRecCom proRecComVigente = ProRecCom.getVigente(proRec, fechaVtoDeuda, fechaVigencia, anio);
		buffer.write(StringUtil.redondearDecimales(proRecComVigente.getPorcentajeComision(),1,2));
		buffer.write(";");
		
		// importe aplicado a ese periodo de deuda
		buffer.write(StringUtil.redondearDecimales(importeAplicado, 1, 2));
		buffer.write(";");
		
		// importe de comision aplicada (lo que representa el % anterior)
		double importeComision = importeAplicado*proRecComVigente.getPorcentajeComision();
		buffer.write(StringUtil.redondearDecimales(importeComision, 1, 2));				
		// Fin imprime la deuda
	}
	
/*	public List<String> exportReportesLiqComPasoUno(LiqCom liqCom, String outputDir) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		// Lista de las Planillas generadas 
		List<String> listPlanillaName = new ArrayList<String>();
	
		int numeroArchivo = 1;
		//Genero el archivo de texto
		String idLiqCom = StringUtil.formatLong(liqCom.getId());
		String fileName = idLiqCom+"DetalleDeuda_"+numeroArchivo+".csv";
		
		listPlanillaName.add(fileName);
		
		BufferedWriter buffer = this.createEncForReportesLiqComPasoUno(new FileWriter(outputDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		// contador de registrosc
		long regCounter = 0;               

		List<LiqComPro> listLiqComPro = liqCom.getListLiqComPro();
		if(listLiqComPro!=null && !listLiqComPro.isEmpty()){
			List<LiqComProVO> listLiqComProVO = (ArrayList<LiqComProVO>) ListUtilBean.toVO(listLiqComPro, 0, false);
			String listIdLiqComPro = ListUtil.getStringIdsFromListModel(listLiqComProVO);
			
			if(listIdLiqComPro!=null && listIdLiqComPro.length()>0){
				String queryString = "SELECT procurador.descripcion, aux.idrecibodeuda, recibo.nrorecibo, " +
						"	cuota.numerocuota, convenio.nroconvenio, aux.importeaplicado, aux.importecomision" +
						" FROM gde_auxliqcomprodeu aux left join gde_recibodeuda recdeuda on " +
						"	aux.idrecibodeuda=recdeuda.id left join gde_recibo recibo on " +
						"	recdeuda.idrecibo=recibo.id left join gde_condeucuo condeucuo on " +
						"	aux.idcondeucuo=condeucuo.id left join gde_conveniocuota cuota on " +
						"	condeucuo.idconveniocuota=cuota.id left join gde_convenio convenio on " +
						"	cuota.idconvenio=convenio.id, gde_liqcompro liqComPro, gde_procurador procurador" +
						" WHERE aux.idliqcompro in("+listIdLiqComPro+") and aux.idliqcompro=liqComPro.id and " +
						"	liqComPro.idprocurador=procurador.id";
				
				if (log.isDebugEnabled()) log.debug("queryString: " + queryString);
		
				// Ejecucion de la consulta de resultado
				ps = con.prepareStatement(queryString);
				rs = ps.executeQuery();
		
				//Encabezado
				buffer.write("RECURSO: " +  liqCom.getRecurso().getDesRecurso().trim());
				buffer.newLine();
				buffer.write("PROCURADOR, DEUDA/RECIBO, CUOTA/CONVENIO, IMPORTE APLICADO, IMPORTE COMISION");
				buffer.newLine();
				
				while(rs.next()){
					resultadoVacio = false;
							
					log.debug("col1:"+rs.getString(1).trim() +"        col2:"+rs.getLong(2)+"       col3:"+rs.getLong(3)+
							"     col4:"+rs.getLong(4)+"       col5:"+rs.getLong(5)+"     col6:"+rs.getDouble(6)+
							"     col7:"+rs.getDouble(7));
					//Procurador
					buffer.write(rs.getString(1).trim());
		
					if(rs.getLong(2)>0){
						// Es un recibo, saca el idReciboDeuda y el nroRecibo
						buffer.write(", " +  rs.getLong(2)+"/"+rs.getLong(3));
						buffer.write(", " +  " ");						
					}else{
						// Es un convenio, saca el nroCuota y nroConvenio 
						buffer.write(", " +  " ");
						buffer.write(", " +  rs.getLong(4)+"/"+rs.getLong(5));
					}
										
					// importe aplicado
					String impApl = String.valueOf(rs.getDouble(6));
					buffer.write(", " + StringUtil.parseComaToPoint(StringUtil.redondearDecimales(Double.parseDouble(impApl),0, 2) ));
		
					// Importe comision
					String impCom = String.valueOf(rs.getDouble(7));
					buffer.write(", " +  StringUtil.parseComaToPoint(StringUtil.redondearDecimales(Double.parseDouble(impCom),0, 2) ));
		
					regCounter++;
					if(regCounter == 65534 ){ // incluyendo a las filas del encabezado y considera que regCounter arranca en cero
					
						// cierra el buffer, genera una nueva planilla
						if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
						buffer.close();				
						numeroArchivo++;
						fileName = idLiqCom+"DetalleDeuda_"+numeroArchivo+".csv";
						listPlanillaName.add(fileName);
						buffer = this.createEncForReportesLiqComPasoUno(new FileWriter(outputDir+"/"+fileName, false));
						regCounter = 0; // reinicio contador 
					}else{
						// crea una nueva linea
						buffer.newLine();
					}
		
				} // Fin del recorrida del ResultSet
				rs.close();
				ps.close();
				// <-- Fin Resultado
			}
		}
		
		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Deudas"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
		buffer.close();

		return listPlanillaName;
	}
*/	
	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForReportesLiqComPasoUno(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		
		// --> Creacion del Encabezado del Resultado

		// <-- Fin Creacion del Encabezado del Resultado
		
		buffer.newLine();
		
		return buffer;
	}


	private BufferedWriter createEncForReportesLiqComPasoDos(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		
		// --> Creacion del Encabezado del Resultado

		// <-- Fin Creacion del Encabezado del Resultado
		
		buffer.newLine();
		
		return buffer;
	}
	
	public List<String> exportReportesLiqComPasoDos(LiqCom liqCom,	String outputDir) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// Conseguimos la connection JDBC de hibernate...
		con = currentSession().connection();
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		// Lista de las Planillas generadas 
		List<String> listPlanillaName = new ArrayList<String>();
	
		int numeroArchivo = 1;
		//Genero el archivo de texto
		String idLiqCom = StringUtil.formatLong(liqCom.getId());
		String fileName = idLiqCom+"LiquidacionComision_"+numeroArchivo+".csv";
		
		listPlanillaName.add(fileName);
		
		BufferedWriter buffer = this.createEncForReportesLiqComPasoDos(new FileWriter(outputDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		// contador de registrosc
		long regCounter = 0;               

		List<LiqComPro> listLiqComPro = liqCom.getListLiqComPro();
		if(listLiqComPro!=null && !listLiqComPro.isEmpty()){
				String queryString = "SELECT  procurador.descripcion nombreProcurador, liqComPro.importeaplicado" +
						" importeAplicado, liqComPro.importecomision importeComision " +
						"FROM gde_liqcompro liqComPro, gde_procurador procurador" +
						" WHERE liqComPro.idprocurador=procurador.id and   liqComPro.idliqcom="+liqCom.getId() ;
		
				if (log.isDebugEnabled()) log.debug("queryString: " + queryString);
		
				// Ejecucion de la consulta de resultado
				ps = con.prepareStatement(queryString);
				rs = ps.executeQuery();
		
				//Encabezado
				buffer.newLine();
				buffer.write("PROCURADOR, IMPORTE APLICADO, IMPORTE COMISION");
				buffer.newLine();
				
				while(rs.next()){
					resultadoVacio = false;
										
					//Procurador
					buffer.write(rs.getString(1).trim());
		
					
					// importe aplicado
					String impApl = String.valueOf(rs.getDouble(2));
					buffer.write(", " + StringUtil.parseComaToPoint(StringUtil.redondearDecimales(Double.parseDouble(impApl),0, 2) ));
		
					// Importe comision
					String impCom = String.valueOf(rs.getDouble(3));
					buffer.write(", " +  StringUtil.parseComaToPoint(StringUtil.redondearDecimales(Double.parseDouble(impCom),0, 2) ));
		
					regCounter++;
					if(regCounter == 65534 ){ // incluyendo a las filas del encabezado y considera que regCounter arranca en cero
					
						// cierra el buffer, genera una nueva planilla
						if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
						buffer.close();				
						numeroArchivo++;
						fileName = idLiqCom+"LiquidacionComision_"+numeroArchivo+".csv";
						listPlanillaName.add(fileName);
						buffer = this.createEncForReportesLiqComPasoUno(new FileWriter(outputDir+"/"+fileName, false));
						regCounter = 0; // reinicio contador 
					}else{
						// crea una nueva linea
						buffer.newLine();
					}
		
				} // Fin del recorrida del ResultSet
				rs.close();
				ps.close();
				// <-- Fin Resultado
			}
		
		
		// --> Resultado vacio
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen registros de Deudas"  );
		}		 
		// <-- Fin Resultado vacio
		
		if(log.isDebugEnabled()) log.debug("Archivo generado: " + fileName + " ctdResultados: " + regCounter);
		buffer.close();

		return listPlanillaName;
	}


	/**
	 * Obtiene una LiqCom para el recurso y procurador pasado como parametros y que esten en estado  
	 * "En Preparación", "En espera comenzar", "Procesando" o "En espera continuar".<br>
	 * Los parametros pueden ser null
	 * 
	 * @param idRecurso
	 * @param idProcurador
	 */
	public List<LiqCom> getByRecursoProcuradorSinTerminar(Long idRecurso,
			Long idProcurador, Long idLiqComNotIn) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from LiqCom t ";
	    boolean flagAnd = false;

	    if(idLiqComNotIn!=null){
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.id!= "+idLiqComNotIn;
			flagAnd = true;	    	
	    }
	    
		// filtro por recurso
 		if (idRecurso>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.recurso.id= "+idRecurso;
			flagAnd = true;
		}

		// filtro por procurador
 		if (idProcurador>0) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " (t.procurador.id="+idProcurador;// procurador seleccionado
			queryString += " OR t.procurador IS NULL) ";//con opcion TODOS
			flagAnd = true;
		}
 		
 		// 	filtro por estado proceso
 		queryString += flagAnd ? " and " : " where ";
		queryString += " t.corrida.estadoCorrida.id IN("+EstadoCorrida.ID_EN_PREPARACION+", "+
				EstadoCorrida.ID_EN_ESPERA_COMENZAR+", "+EstadoCorrida.ID_PROCESANDO+", "+
				EstadoCorrida.ID_EN_ESPERA_CONTINUAR+") ";
		flagAnd = true;
		

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    Session session = SiatHibernateUtil.currentSession();
	    Query query = session.createQuery(queryString);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return query.list();
	}
}
