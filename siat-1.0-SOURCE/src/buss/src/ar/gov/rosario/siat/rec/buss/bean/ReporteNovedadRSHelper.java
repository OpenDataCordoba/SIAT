//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * 
 * Esta clase contiene mapas y metodos para la generacion de Reportes del Proceso de Aplicacion Masiva de Novedades RS. 
 * 
 * 
 * @author tecso
 *
 */
public class ReporteNovedadRSHelper {
	
	private Map<String, Boolean> mapaPorConvenio = new HashMap<String, Boolean>();
	private Map<String, Boolean> mapaPorPagos = new HashMap<String, Boolean>();
	private Map<String, Boolean> mapaPorAjustes = new HashMap<String, Boolean>();
	private Map<String, Boolean> mapaPorCierre = new HashMap<String, Boolean>();
		
	
	public ReporteNovedadRSHelper() {
	
	}
	
	
	/**
	 * Agrega un numero de cuenta al mapa de novedades con observaciones por periodo en convenio 
	 * @param numeroCuenta
	 */
	public void addCuentaMapaConvenio(String numeroCuenta){
		this.mapaPorConvenio.put(numeroCuenta, true);
	}
	
	/**
	 * Agrega un numero de cuenta al mapa de novedades con observaciones por periodo con pagos registrados 
	 * @param numeroCuenta
	 */
	public void addCuentaMapaPagos(String numeroCuenta){
		this.mapaPorPagos.put(numeroCuenta, true);
	}
	
	/**
	 * Agrega un numero de cuenta al mapa de novedades con observaciones por periodo con ajustes 
	 * @param numeroCuenta
	 */
	public void addCuentaMapaAjustes(String numeroCuenta){
		this.mapaPorAjustes.put(numeroCuenta, true);
	}
	
	/**
	 * Agrega un numero de cuenta al mapa de novedades con observaciones por inicio de cierre de comercio. Sin afectacion en la deuda. 
	 * @param numeroCuenta
	 */
	public void addCuentaMapaCierre(String numeroCuenta){
		this.mapaPorCierre.put(numeroCuenta, true);
	}
	
	/**
	 * Genera un Reporte pdf "Cuentas informadas por periodos en Convenio".
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfNovedadesPorConvenio(String fileDir, Corrida corrida) throws Exception{
		//	Encabezado:
		String fechaCorrida = DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK);
				
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_NOVEDADRS_CTAS_INF);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Cuentas informadas por periodos en Convenio");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", corrida.getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Cuentas informadas por periodos en Convenio");
		printModel.putCabecera("FechaEjecucion", fechaCorrida);
		printModel.putCabecera("CantidadCuentas", StringUtil.formatInteger(mapaPorConvenio.size()));
				
		// Armamos un Contenedor para las Tablas
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("ListaCuentas");
		tabla.setTitulo("Cuentas Informadas");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Numero Cuenta","nroCta"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		for(String ctaInfo: mapaPorConvenio.keySet()){
			fila.add(new CeldaVO(ctaInfo,"nroCta"));
			tabla.add(fila);
			fila = new FilaVO();
		}
		
		printModel.setData(tabla);
		printModel.setTopeProfundidad(3);

		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = corrida.getId().toString()+"ReporteCtaInfoPorConvenio.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();

		return fileName;
	}
	
	/**
	 * Genera un Reporte pdf "Cuentas informadas por periodos con Pagos registrados".
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfNovedadesPorPagos(String fileDir, Corrida corrida) throws Exception{
		//	Encabezado:
		String fechaCorrida = DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK);
				
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_NOVEDADRS_CTAS_INF);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Cuentas informadas por periodos con Pagos registrados");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", corrida.getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Cuentas informadas por periodos con Pagos registrados");
		printModel.putCabecera("FechaEjecucion", fechaCorrida);
		printModel.putCabecera("CantidadCuentas", StringUtil.formatInteger(mapaPorPagos.size()));
				
		// Armamos un Contenedor para las Tablas
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("ListaCuentas");
		tabla.setTitulo("Cuentas Informadas");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Numero Cuenta","nroCta"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		for(String ctaInfo: mapaPorPagos.keySet()){
			fila.add(new CeldaVO(ctaInfo,"nroCta"));
			tabla.add(fila);
			fila = new FilaVO();
		}
		
		printModel.setData(tabla);
		printModel.setTopeProfundidad(3);

		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = corrida.getId().toString()+"ReporteCtaInfoPorPagos.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();

		return fileName;
	}
	
	/**
	 * Genera un Reporte pdf "Cuentas informadas por periodos con Ajustes".
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfNovedadesPorAjustes(String fileDir, Corrida corrida) throws Exception{
		//	Encabezado:
		String fechaCorrida = DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK);
				
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_NOVEDADRS_CTAS_INF);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Cuentas informadas por periodos con Ajustes");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", corrida.getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Cuentas informadas por periodos con Ajustes");
		printModel.putCabecera("FechaEjecucion", fechaCorrida);
		printModel.putCabecera("CantidadCuentas", StringUtil.formatInteger(mapaPorAjustes.size()));
				
		// Armamos un Contenedor para las Tablas
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("ListaCuentas");
		tabla.setTitulo("Cuentas Informadas");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Numero Cuenta","nroCta"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		for(String ctaInfo: mapaPorAjustes.keySet()){
			fila.add(new CeldaVO(ctaInfo,"nroCta"));
			tabla.add(fila);
			fila = new FilaVO();
		}
		
		printModel.setData(tabla);
		printModel.setTopeProfundidad(3);

		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = corrida.getId().toString()+"ReporteCtaInfoPorAjustes.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();

		return fileName;
	}

	/**
	 * Genera un Reporte pdf "Cuentas informadas por bajas con cierre de comercio. Sin afectacion de Deuda."
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfNovedadesPorCierre(String fileDir, Corrida corrida) throws Exception{
		//	Encabezado:
		String fechaCorrida = DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK);
				
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_NOVEDADRS_CTAS_INF);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Cuentas informadas por bajas con cierre de comercio. Sin afectacion de Deuda.");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", corrida.getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Cuentas informadas por bajas con cierre de comercio. Sin afectacion de Deuda.");
		printModel.putCabecera("FechaEjecucion", fechaCorrida);
		printModel.putCabecera("CantidadCuentas", StringUtil.formatInteger(mapaPorCierre.size()));
				
		// Armamos un Contenedor para las Tablas
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("ListaCuentas");
		tabla.setTitulo("Cuentas Informadas");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Numero Cuenta","nroCta"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		for(String ctaInfo: mapaPorCierre.keySet()){
			fila.add(new CeldaVO(ctaInfo,"nroCta"));
			tabla.add(fila);
			fila = new FilaVO();
		}
		
		printModel.setData(tabla);
		printModel.setTopeProfundidad(3);

		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = corrida.getId().toString()+"ReporteCtaInfoPorCierre.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();

		return fileName;
	}
	
	
	/**
	 * <b>Genera Formularios para Control:</b> 
	 * <p>- Reporte de Cuentas informadas por periodos en Convenio: archivo pdf</p>
	 * <p>- Reporte de Cuentas informadas por periodos con Pagos registrados: archivo pdf</p>
	 * <p>- Reporte de Cuentas informadas por periodos con Ajustes: archivo pdf</p>
	 * <p>- Reporte de Cuentas informadas por bajas con cierre de comercio. Sin afectacion de Deuda: archivo pdf</p>
	 * 
	 */
	public void generarFormularios(String outputDir,Corrida corrida) throws Exception{
		
		//-> Reporte de Cuentas informadas por periodos en Convenio (PDF)
		String fileName = this.generarPdfNovedadesPorConvenio(outputDir,corrida);
		String nombre = "Cuentas informadas por periodos en Convenio";
		String descripcion = "Permite consultar las Cuentas con observaciones por periodos en Convenios al momento de la aplicación.";
		corrida.addOutputFile(nombre, descripcion, outputDir+fileName);
	
		//-> Reporte de Cuentas informadas por periodos con Pagos registrados (PDF)
		fileName = this.generarPdfNovedadesPorPagos(outputDir,corrida);
		nombre = "Cuentas informadas por periodos con Pagos registrados";
		descripcion = "Permite consultar las Cuentas con observaciones por periodos que registran pagos al momento de la aplicación.";
		corrida.addOutputFile(nombre, descripcion, outputDir+fileName);

		//-> Reporte de Cuentas informadas por periodos con Ajustes (PDF)
		fileName = this.generarPdfNovedadesPorAjustes(outputDir,corrida);
		nombre = "Cuentas informadas por periodos con Ajustes";
		descripcion = "Permite consultar las Cuentas con observaciones por periodos con ajustes al momento de la aplicación.";
		corrida.addOutputFile(nombre, descripcion, outputDir+fileName);
		
		//-> Reporte de Cuentas informadas por bajas con cierre de comercio. Sin afectacion de Deuda (PDF)
		fileName = this.generarPdfNovedadesPorCierre(outputDir,corrida);
		nombre = "Cuentas informadas por bajas con cierre de comercio. Sin afectacion de Deuda";
		descripcion = "Permite consultar las Cuentas en tramite de cierre al momento de la aplicación.";
		corrida.addOutputFile(nombre, descripcion, outputDir+fileName);

	}
	
}
