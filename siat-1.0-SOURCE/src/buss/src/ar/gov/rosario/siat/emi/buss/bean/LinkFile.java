//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Representa un archivo Link
 * para Pago Automatico de Servicios
 */
public class LinkFile {
	
	public static final String FILE_NAME_PREFIX = "P0581";
	
	public static final String CTRL_FILE_NAME_PREFIX = "C0581";

	private String fileName;

	private String ctrlFileName;
	
	private String absolutePath;
	
	private String ctrlAbsolutePath;
	
	private FileWriter fileWriter;

	private FileWriter ctrlFileWriter;
	
	private Integer anio;
	
	private Integer periodo;

	private int cantLineas;

	private Double totalOriginal;

	private Double totalAct;
	
	private Date fecUltVenc;
	
	/** 
     * Crea un archivo Link en outputDir con 
     * el siguiente formato de nombre:
     * 
     * nombre-fijo 	pic x(5) --> "P0581"
     * mes-dest 	pic x	 --> mes(hoy), si mes < 10 o "A", si mes = 10, o "B", si mes = 11 o "C" si mes = 12
	 * dia-dest 	pic 99	 --> dia(hoy)
     */
	public LinkFile(String outputDir, Integer anio, Integer periodo) throws IOException {
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        String nombreFijo = FILE_NAME_PREFIX;
        String ctrlNomFijo = CTRL_FILE_NAME_PREFIX;
        String mesDest = Integer.toHexString(month).toUpperCase();
    	String diaDest = formatInteger(day, 2);
    	
    	this.fileName = nombreFijo + mesDest + diaDest;
    	this.ctrlFileName = ctrlNomFijo + mesDest + diaDest;
    	this.absolutePath = outputDir + File.separator + this.fileName;
    	this.ctrlAbsolutePath = outputDir + File.separator + this.ctrlFileName;;
		this.fileWriter = new FileWriter(this.absolutePath);
		this.ctrlFileWriter = new FileWriter(this.ctrlAbsolutePath);
		this.anio = anio;
		this.periodo = periodo;
		this.cantLineas = 0;
		this.totalOriginal = 0D;
		this.totalAct = 0D;

		createEncabezado();
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public String getAbsolutePath() {
		return this.absolutePath;
	}

	public String getCtrlFileName() {
		return this.ctrlFileName;
	}

	public String getCtrlAbsolutePath() {
		return this.ctrlAbsolutePath;
	}

	/** 
	 * Registro Link:
	 * 
	 * sistema 		pic 99 		--> "02".
	 * cuenta 		pic 9(10)	--> numerocuenta 
	 * filler 		pic xxxx  	--> espacios
	 * vencim1 		pic 9(6)	--> gde_deudaadmin.fechavencimiento, con formato aammdd.
	 * importe1 	pic 9(6)v99 --> gde_deudaadmin.importe
	 * vencim2 		pic 9(6) 	--> ultDiaMes(gde_deudaadmin.fechavencimiento), con formato aammdd.
	 * importe2 	pic 9(6)v99	--> imp_act
	 * filler 		pic x(10)	--> espacios.
	 */
	public synchronized void createRegistro(String numCuenta, Date fecVen, Double importe, Double impAct) throws IOException {
		String sistema  = "02";
		String cuenta   = formatLeftZeroString(numCuenta, 10);
		String empty    = filler(4);
		String vencim1  = DateUtil.formatDate(fecVen, DateUtil.yyMMdd_MASK);
		String importe1 = formatDouble(importe, 6, 2);
		Date ultDiaMes 	= DateUtil.getLastDayOfMonth(fecVen);
		String vencim2 	= DateUtil.formatDate(ultDiaMes, DateUtil.yyMMdd_MASK);
		String importe2 = formatDouble(impAct, 6, 2);
		String empty2 	= filler(10);
	
		this.fileWriter.write(sistema);
		this.fileWriter.write(cuenta);
		this.fileWriter.write(empty);
		this.fileWriter.write(vencim1);
		this.fileWriter.write(importe1);
		this.fileWriter.write(vencim2);
		this.fileWriter.write(importe2);
		this.fileWriter.write(empty2);
		
		this.cantLineas++;
		// Redondeamos en la suma
		this.totalOriginal += NumberUtil.round(importe,2);
		this.totalAct	   += NumberUtil.round(impAct,2);
	}

	public synchronized  void close() throws IOException {
		createPie();
		this.fileWriter.close();
		this.ctrlFileWriter.close();
	}
	
	/**
	 * Encabezado del archivo Link:
	 * 
	 * identif		 pic x(13) --> "HRFACTURACION".
	 * cod-ente 	 pic xxx   --> 058
	 * fecha-proc 	 pic 9(6)  --> hoy()
	 * periodo2		 pic 999   --> periodo
	 * anio2 		 pic 99	   --> anio (en 2 digitos) 
	 * cod-servicio  pic 99	   --> 0
	 * filler 		 pic x(25) --> espacios 
	 */
	private void createEncabezado()  throws IOException {
		String identif   = "HRFACTURACION";
		String codEnte   = "058";
		String fechaProc = DateUtil.formatDate(new Date(), DateUtil.yyMMdd_MASK);
		String periodo2  = formatInteger(this.periodo, 3); 
		String anio2 	 = formatInteger(this.anio-2000, 2);
		String codServic = formatInteger(0, 2);
		String empty   	 = filler(25);
			
		this.fileWriter.write(identif);
		this.fileWriter.write(codEnte);
		this.fileWriter.write(fechaProc);
		this.fileWriter.write(periodo2);
		this.fileWriter.write(anio2);
		this.fileWriter.write(codServic);
		this.fileWriter.write(empty);
		
		String ctrlIdentif = "HRPASCTRL";        
		String ctrlFechaProc = DateUtil.formatDate(new Date(), DateUtil.YYYYMMDD_MASK);
		
		this.ctrlFileWriter.write(ctrlIdentif);
		this.ctrlFileWriter.write(ctrlFechaProc);
		this.ctrlFileWriter.write(codEnte);
		this.ctrlFileWriter.write(this.fileName);
	}

	/**
	 * Pie del archivo Link:
	 * 
	 * identif	  pic x(13) 	--> "TRFACTURACION".
	 * cantidad	  pic 9(8)		--> cantidad_lineas + 2
	 * importe-1  pic 9(14)v99	--> total_orig
	 * importe-2  pic 9(14)v99	--> total_act
	 * nada		  pic x			--> espacio
	 */
	private void createPie()  throws IOException {
		String identif  = "TRFACTURACION";
		String cantidad = formatInteger(this.cantLineas + 2, 8);
		String importe1 = formatDouble(this.totalOriginal, 14, 2);
		String importe2 = formatDouble(this.totalAct, 14, 2);
		String empty 	= filler(1);

		this.fileWriter.write(identif);
		this.fileWriter.write(cantidad);
		this.fileWriter.write(importe1);
		this.fileWriter.write(importe2);
		this.fileWriter.write(empty);

		// Flusheamos 
		this.fileWriter.flush();
		// Cerramos el archivo
		this.fileWriter.close();
		// Abrimos un file
		File f = new File(this.absolutePath);
		Long fileSize = f.length();
		
		this.ctrlFileWriter.write(formatLong(fileSize,10));
		this.ctrlFileWriter.write(filler(22));
		
		String periodo2  = formatInteger(this.periodo, 3); 
		String anio2 	 = formatInteger(this.anio-2000, 2);
		this.ctrlFileWriter.write("LOTES");
		this.ctrlFileWriter.write(periodo2);
		this.ctrlFileWriter.write(anio2);
		this.ctrlFileWriter.write(cantidad);
		this.ctrlFileWriter.write(importe1);
		this.ctrlFileWriter.write(importe2);
		this.ctrlFileWriter.write(filler(10));

		String ctrlEnd = "FINAL";
		Date fecha = DateUtil.getFirstDatOfMonth(this.periodo, this.anio);
		Date ultDiaMes 	= DateUtil.getLastDayOfMonth(fecha);
		this.fecUltVenc = ultDiaMes;
		this.ctrlFileWriter.write(ctrlEnd);
		this.ctrlFileWriter.write(cantidad);
		this.ctrlFileWriter.write(importe1);
		this.ctrlFileWriter.write(importe2);
		this.ctrlFileWriter.write(DateUtil.formatDate(ultDiaMes,DateUtil.YYYYMMDD_MASK));
		this.ctrlFileWriter.write(filler(7));
	}

	public Integer getCantidadRegistros() {
		return this.cantLineas;
	}
	
	public Double getImporteTotal() {
		return this.totalOriginal;
	}

	public Double getImporteTotalAct() {
		return this.totalAct;
	}
	
	public Date getFecUltVenc() {
		return this.fecUltVenc;
	}

	// Metodos auxiliares
	
	private static String formatLeftZeroString(String s, int size) {
		return StringUtil.fillWithCharToLeft(s, '0', size);	
	}

	// 21-07: en todos los casos formateamos los dobles con el estilo cobol
	private static String formatDouble(Double d, int enteros, int decimales) {
		return StringUtil.formatDoubleCblStyle(d, enteros, decimales);
	}

	private static String formatInteger(Integer n, int size){
		String num = StringUtil.formatInteger(n);
		return StringUtil.fillWithCharToLeft(num, '0', size);
	}

	private static String formatLong(Long l, int size){
		String num = StringUtil.formatLong(l);
		return StringUtil.fillWithCharToLeft(num, '0', size);
	}

	private static String filler(int size){
		return StringUtil.fillWithBlanksToRight("", size);
	}
	
	/**
	 * Crea los nombres de archivo segun el prefijo 
 	 */
	private static String createFileName(String prefix, Date fecha){
		Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        String mesDest = Integer.toHexString(month).toUpperCase();
    	String diaDest = formatInteger(day, 2);
    	
    	return prefix + mesDest + diaDest;
	}
	
	/**
	 * Modifica la fecha de proceso de un archivo Link ya creado. 
	 */
	public static File modificarFechaProc(File oldFile, Date newFechaProc) throws Exception {
		
		String path = oldFile.getParentFile().getAbsolutePath() + File.separator;
		String newFileName = path + createFileName(FILE_NAME_PREFIX, newFechaProc);
		File newFile = new File(newFileName);
		
		BufferedReader reader = new BufferedReader(new FileReader(oldFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

		// Por seguridad, trabajamos sobre una copia
		String line = "";
		while ((line = reader.readLine()) != null) {
		  writer.write(line);
		}

		reader.close();
		writer.close();

		// Modificamos la fecha de proceso
		RandomAccessFile raf = null;
		raf = new RandomAccessFile(newFile, "rw");
		raf.seek(16);
		String fechaProc = DateUtil.formatDate(newFechaProc, DateUtil.yyMMdd_MASK);
		raf.writeBytes(fechaProc);
		raf.close();

		// Borramos el archivo anterior
		oldFile.delete();
		
		return newFile;
	}

	/**
	 * Modifica la fecha de proceso de un archivo de control de Link ya creado. 
	 */
	public static File modificarFechaProcCtrl(File oldFile, Date newFechaProc) throws Exception {
		
		String path = oldFile.getParentFile().getAbsolutePath() + File.separator;
		String newFileName = path + createFileName(CTRL_FILE_NAME_PREFIX, newFechaProc);
		File newFile = new File(newFileName);
		
		BufferedReader reader = new BufferedReader(new FileReader(oldFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

		// Por seguridad, trabajamos sobre una copia
		String line = "";
		while ((line = reader.readLine()) != null) {
		  writer.write(line);
		}

		reader.close();
		writer.close();

		// Modificamos la fecha de proceso
		RandomAccessFile raf = null;
		raf = new RandomAccessFile(newFile, "rw");
		raf.seek(9);
		String fechaProc = DateUtil.formatDate(newFechaProc, DateUtil.YYYYMMDD_MASK);
		raf.writeBytes(fechaProc + "058" + createFileName(FILE_NAME_PREFIX, newFechaProc));
		raf.close();

		// Borramos el archivo anterior
		oldFile.delete();

		return newFile;
	}
}