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
import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Representa un archivo Banelco
 * para Pago Automatico de Servicios
 */
public class BanelcoFile {
	
	public static final String FILE_NAME_PREFIX = "fac1006.";

	private String fileName;
	
	private String absolutePath;

	private FileWriter fileWriter;

	private Integer anio;
	
	private Integer periodo;

	private int cantLineas;

	private Double totalOriginal;

	private Double totalAct;
	
	private Date fecUltVenc;
	
    /** 
     * Crea un archivo Banelco en outputDir con
     * el siguiente formato de nombre:
     * 
     * nombre-fijo 	pic x(8) --> "fac1006."
     * fecha-hoy 	pic 9(8) -->  hoy(), con formato ddmmaa
     */
	public BanelcoFile(String outputDir, Integer anio, Integer periodo) throws IOException {
    	String nombreFijo = FILE_NAME_PREFIX;
    	String fechaHoy	= DateUtil.formatDate(new Date(), DateUtil.ddMMYY_MASK);  
    	this.fileName  = nombreFijo + fechaHoy;
    	this.absolutePath = outputDir + File.separator + this.fileName;
    	this.fileWriter = new FileWriter(this.absolutePath);
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
	
	/** 
	 * Registro Banelco:
	 * 
	 * cod-reg-dd 			pic x 		--> "5"
	 * nro-ref-dd 			pic xxx	 	--> "058"
	 * sistema-dd 			pic 99	 	--> "02"
	 * cuenta-dd 			pic 9(10)	--> cuenta
	 * nada-dd1				pic xxxx	--> espacios
	 * periodo-dd 			pic 99		--> periodo
	 * anio-dd 				pic 9999	--> anio
	 * nada-dd2				pic x(14)	--> espacios
	 * cod-moneda-dd 		pic x 		--> "0"
	 * fecha-1er-vto-dd 	pic 9(8) 	--> gde_deudaadmin.fechavencimiento, con formato aaaammdd
	 * importe-1er-vto 		pic 9(9)v99	--> gde_deudaadmin.importe
	 * fecha-2do-vto-dd 	pic 9(8) 	--> ultDiaMes(gde_deudaadmin.fechavencimiento), con formato aaaammdd
	 * importe-2do-vto	 	pic 9(9)v99	--> imp_act
	 * fecha-3er-vto-dd 	pic 9(8) 	--> 0
	 * importe-3er-vto 		pic 9(9)v99 --> 0
	 * importe-minimo-dd 	pic 9(9)v99 --> 0
	 * fecha-prox-vto-dd 	pic 9(8) 	--> 0
	 * nro-ref-ant 			pic x(19)	--> "05802"||numerocuenta(10)||4 espacios
	 * 1-mensaje-atm-r 		pic x(16) 	--> "PAGO DE T.G.I. "
	 * 2-mensaje-atm-r 		pic 99		--> periodo
	 * 3-mensaje-atm-r 		pic x 		--> "/"
	 * 4-mensaje-atm-r 		pic 9999	--> anio
	 * nada-dd3				pic x(17)	--> espacios
	 * 1-ident-part-r 		pic x(7) 	--> "T.G.I. "
	 * 2-ident-part-r 		pic 99		--> periodo
	 * 3-ident-part-r 		pic x 		--> "/"
	 * 4-ident-part-r 		pic 9999	--> anio
	 * nada-dd-4			pic x		--> espacio
	 * nada-dd-5			pic x(9) 	--> "000000000"
	 */
	public synchronized void createRegistro(String numCuenta, Date fecVen, Double importe, Double impAct) throws IOException {
		String codRegDD	  	  = "5";
		String nroRefDD   	  = "058";
		String sistemaDD  	  = "02";
		String cuentaDD   	  = formatLeftZeroString(numCuenta, 10);
		String nadaDD1 	  	  = filler(4);
		String periodoDD 	  = formatInteger(periodo, 2);
		String anioDD	  	  = formatInteger(anio, 4);
		String nadaDD2    	  = filler(14);
		String codMonedDD 	  = "0";
		String fecha1erVtoDD  = DateUtil.formatDate(fecVen, DateUtil.YYYYMMDD_MASK);
		String import1erVtoDD = formatDouble(importe, 9,2);
		Date ultDiaMes		  = DateUtil.getLastDayOfMonth(fecVen);
		String fecha2erVtoDD  = DateUtil.formatDate(ultDiaMes, DateUtil.YYYYMMDD_MASK);
		String import2erVtoDD = formatDouble(impAct, 9,2);
		String fecha3erVtoDD  = formatInteger(0, 8);
		String import3erVtoDD = formatDouble(0D, 9,2);
		String importMinDD	  = formatDouble(0D, 9,2);
		String fechaProxVtoDD = formatInteger(0, 8);
		String nroRefAnt	  =  "05802"+formatLeftZeroString(numCuenta, 10) + filler(4);
		String msg1Atm	 	  = formatString("PAGO DE T.G.I. ", 16);
		String msg2Atm		  = formatInteger(periodo, 2);
		String msg3Atm		  = "/";
		String msg4Atm		  = formatInteger(anio, 4);
		String nadaDD3		  = filler(17);
		String idenPart1	  = formatString("T.G.I. ", 7);
		String idenPart2	  = formatInteger(periodo, 2);
		String idenPart3	  =  "/";
		String idenPart4	  = formatInteger(anio, 4);
		String nadaDD4		  = filler(1);
		String nadaDD5 		  = formatString("000000000", 9);

		this.fileWriter.write(codRegDD);
		this.fileWriter.write(nroRefDD);
		this.fileWriter.write(sistemaDD);
		this.fileWriter.write(cuentaDD);
		this.fileWriter.write(nadaDD1);
		this.fileWriter.write(periodoDD);
		this.fileWriter.write(anioDD);
		this.fileWriter.write(nadaDD2);
		this.fileWriter.write(codMonedDD);
		this.fileWriter.write(fecha1erVtoDD);
		this.fileWriter.write(import1erVtoDD);
		this.fileWriter.write(fecha2erVtoDD);
		this.fileWriter.write(import2erVtoDD);
		this.fileWriter.write(fecha3erVtoDD);
		this.fileWriter.write(import3erVtoDD);
		this.fileWriter.write(importMinDD);
		this.fileWriter.write(fechaProxVtoDD);
		this.fileWriter.write(nroRefAnt);
		this.fileWriter.write(msg1Atm);
		this.fileWriter.write(msg2Atm);
		this.fileWriter.write(msg3Atm);
		this.fileWriter.write(msg4Atm);
		this.fileWriter.write(nadaDD3);
		this.fileWriter.write(idenPart1);
		this.fileWriter.write(idenPart2);
		this.fileWriter.write(idenPart3);
		this.fileWriter.write(idenPart4);
		this.fileWriter.write(nadaDD4);
		this.fileWriter.write(nadaDD5);
		this.fileWriter.write("\n");
		
		this.cantLineas++;
		this.totalOriginal += NumberUtil.round(importe,2);
		this.totalAct	   += NumberUtil.round(impAct,2);
		this.fecUltVenc = ultDiaMes;
	}

	public synchronized  void close() throws IOException {
		createPie();
		this.fileWriter.close();
	}
	
	/**
	 * Encabezado del archivo Banelco:
	 * 
	 * cod-reg-hh 		 pic x 	  	--> "0"
	 * cod-banelco-hh 	 pic xxx  	--> "400"
	 * cod-ser-emp-hh	 pic xxxx 	--> "1006"
	 * fecha-archivo-hh  pic 9(8)	--> hoy()
	 * nada 			 pic x(184)	--> espacios
	 */
	private void createEncabezado()  throws IOException {
		
		String codRegHH 	= "0";
		String codBanelcoHH = "400";
		String codSerEmp	= "1006";
		String fecArchHH	= DateUtil.formatDate(new Date(), DateUtil.YYYYMMDD_MASK);
		String nada			= filler(184);
		
		this.fileWriter.write(codRegHH);
		this.fileWriter.write(codBanelcoHH);
		this.fileWriter.write(codSerEmp);
		this.fileWriter.write(fecArchHH);
		this.fileWriter.write(nada);
		this.fileWriter.write("\n");
	}

	/**
	 * Pie del archivo Banelco:
	 *
	 * cod-reg-tt 		pic x 		--> "9"
	 * cod-banelco-tt 	pic xxx 	--> "400"
	 * cod-ser-emp-tt 	pic xxxx 	--> "1006"
	 * fecha-archivo-tt pic 9(8)	--> hoy(), con formato aaaammdd
	 * cant-reg-pesos 	pic 9(7)	--> cant_lineas
	 * cant-reg-dolar 	pic 9(7)	--> 0
	 * oper-reg-pesos 	pic 9(9)v99	--> total_orig
	 * oper-reg-dolar 	pic 9(11) 	--> 0
	 * filler 			pic x(148) 	--> 00000....000
	 */
	private void createPie()  throws IOException {
		String codRegTT		= "9";
		String codBanelcoTT = "400";
		String codSerEmpTT	= "1006";
		String fecArchTT	= DateUtil.formatDate(new Date(), DateUtil.YYYYMMDD_MASK);
		String cantRegPesos = formatInteger(this.cantLineas, 7);
		String cantRegDolar = formatInteger(0, 7);
		String operRegPesos = formatDouble(this.totalOriginal, 9, 2);
		String operRegDolar = formatInteger(0, 11);
		String empty	 	= formatLeftZeroString("0", 148);

		this.fileWriter.write(codRegTT);
		this.fileWriter.write(codBanelcoTT);
		this.fileWriter.write(codSerEmpTT);
		this.fileWriter.write(fecArchTT);
		this.fileWriter.write(cantRegPesos);
		this.fileWriter.write(cantRegDolar);
		this.fileWriter.write(operRegPesos);
		this.fileWriter.write(operRegDolar);
		this.fileWriter.write(empty);
		this.fileWriter.write("\n");
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
	
	private static String formatLeftZeroString(String s, int size) {
		return StringUtil.fillWithCharToLeft(s, '0', size);	
	}

	// 21-07: en todos los casos formateamos los dobles con el estilo cobol
	private static String formatDouble(Double d, int enteros, int decimales) {
		return StringUtil.formatDoubleCblStyle(d, enteros, decimales);
	}

	private static String formatString(String s, int size){
		return StringUtil.fillWithBlanksToRight(s, size);
	}

	private static String formatInteger(Integer n, int size){
		String num = StringUtil.formatInteger(n);
		return StringUtil.fillWithCharToLeft(num, '0', size);
	}

	private static String filler(int size){
		return StringUtil.fillWithBlanksToRight("", size);
	}
	
	private static String createFileName(Date fecha){
		String fechaHoy	= DateUtil.formatDate(fecha, DateUtil.ddMMYY_MASK);    	
    	return FILE_NAME_PREFIX + fechaHoy;
	}
	
	/**
	 * Modifica la fecha de proceso de un archivo Banelco ya creado. 
	 */
	public static File modificarFechaProc(File oldFile, Date newFechaProc) throws Exception {
		
		String path = oldFile.getParentFile().getAbsolutePath() + File.separator;
		String newFileName = path + createFileName(newFechaProc);
		File newFile = new File(newFileName);
		
		BufferedReader reader = new BufferedReader(new FileReader(oldFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

	    // Por seguridad, trabajamos sobre copias
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	      writer.write(line);
	      writer.newLine();
	    }

		reader.close();
		writer.close();

		// Modificamos la fecha de proceso
		RandomAccessFile raf = null;
	    raf = new RandomAccessFile(newFile, "rw");
		String fechaProc = DateUtil.formatDate(newFechaProc, DateUtil.YYYYMMDD_MASK);
	    raf.seek(8);
	    raf.writeBytes(fechaProc);
	    raf.seek(newFile.length() - 193);
	    raf.writeBytes(fechaProc);
	    raf.close();
	    
		// Borramos el archivo anterior
		oldFile.delete();

		return newFile;
	}
}