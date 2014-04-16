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
import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Representa un archivo para Debito Automatico
 */
public class DebitoFile {
	
	public static final String FILE_NAME_PREFIX = "debito.tgi";

	private static final String SEP = "|";
	
	private String fileName;

	private String absolutePath;
	
	private FileWriter fileWriter;

	private Integer anio;
	
	private Integer periodo;
	
	private Integer totalRegs;
	
	private Date fechaGen;

    /** 
     * Nombre archivo de Debito Automatico: debitos.tgi
     * @throws IOException 
     */
	public DebitoFile(String outputDir, Integer anio, Integer periodo) throws IOException {
		this.fileName = FILE_NAME_PREFIX;
		this.absolutePath = outputDir + File.separator + this.fileName;
		this.fileWriter = new FileWriter(this.absolutePath);
		this.anio = anio;
		this.periodo = periodo;
		this.fechaGen = new Date();
		this.totalRegs = 0;
	}
	
	public String getFileName() {
		return this.fileName;
	}

	public String getAbsolutePath() {
		return this.absolutePath;
	}

	/** 
	 * Registro Debito (con campos separados por "|"):
	 * sistema 		pic 99		--> "02"
	 * cuenta 		pic 9(10)	--> cuenta
	 * periodo 		pic 99		--> periodo
	 * anio 		pic 9(4) 	--> anio
	 * cuota 		pic 9(4) 	--> "0000"
	 * recibo 		pic 9(7) 	--> "0000000"
	 * anio-rec 	pic 9(4) 	--> "0000"
	 * ajuste 		pic 9(4) 	--> "0000"
	 * importe 		pic 9(9)v99	--> gde_deudaadmin.importe
	 * fecha-gene 	pic 9(8) 	--> fechaGen, con formato aaaammdd
	 * fecha-venc 	pic 9(8)	--> gde_deudaadmin.fechavencimiento, con formato aaaammdd
	 */
	public synchronized void createRegistro(String numCuenta,Date fecVen, Double imp, Double impAct) throws IOException {
		String sistema = "02";
		String cuenta   = formatLeftZeroString(numCuenta, 10);
		String periodo  = formatInteger(this.periodo, 2);
		String anio     = formatInteger(this.anio, 4);
		String cuota    = "0000";
		String recibo   = "0000000";
		String anioRec  = "0000";
		String ajuste   = "0000";
		String importe  = formatDouble(imp, 13);
		String fechaGen = DateUtil.formatDate(this.fechaGen, DateUtil.YYYYMMDD_MASK);
		String fecVto	= DateUtil.formatDate(fecVen, DateUtil.YYYYMMDD_MASK);
		
		this.fileWriter.write(sistema + SEP);
		this.fileWriter.write(cuenta + SEP);
		this.fileWriter.write(periodo + SEP);
		this.fileWriter.write(anio + SEP);
		this.fileWriter.write(cuota + SEP);
		this.fileWriter.write(recibo + SEP);
		this.fileWriter.write(anioRec + SEP);
		this.fileWriter.write(ajuste + SEP);
		this.fileWriter.write(importe + SEP);
		this.fileWriter.write(fechaGen + SEP);
		this.fileWriter.write(fecVto);
		this.fileWriter.write("\n");
		
		this.totalRegs++;
	}
	
	public synchronized void close() throws IOException {
		this.fileWriter.close();
	}
	
	public Integer getTotoalRegs() {
		return totalRegs;
	}
			
	private static String formatLeftZeroString(String s, int size) {
		return StringUtil.fillWithCharToLeft(s, '0', size);	
	}
	
	private static String formatInteger(Integer n, int size){
		String num = StringUtil.formatInteger(n);
		return StringUtil.fillWithCharToLeft(num, '0', size);
	}

	private static String formatDouble(Double d, int size){
		String num = StringUtil.formatDouble(d);
		return StringUtil.fillWithCharToLeft(num, '0', size);
	}

	private static String createFileName() {
		return FILE_NAME_PREFIX;
	}
	
	/**
	 * Modifica la fecha de proceso de un archivo de Debitos ya creado. 
	 */
	public static File modificarFechaProc(File oldFile, Date newFechaProc) throws Exception {
		
		String path = oldFile.getParentFile().getAbsolutePath() + File.separator;
		String tmpFileName = path + createFileName() + ".tmp";
		File tmpFile = new File(tmpFileName);
		
		BufferedReader reader = new BufferedReader(new FileReader(oldFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));

	    // Por seguridad, trabajamos sobre copias
		String strNewfechaProc = DateUtil.formatDate(newFechaProc, DateUtil.YYYYMMDD_MASK);
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	      // Como el campo para la fecha de proceso
	      // es el unico con longitud 8, es seguro
	      // utilizar replace. 
	      String oldFechaProc = line.substring(59, 67);
	      writer.write(line.replace("|" + oldFechaProc + "|","|" + strNewfechaProc +"|"));
	      writer.newLine();
	    }

		reader.close();
		writer.close();

		// Borramos el archivo anterior
		oldFile.delete();

		String newFileName = path + createFileName();
		File newFile = new File(newFileName);
		tmpFile.renameTo(newFile);

		return newFile;
	}
	
}