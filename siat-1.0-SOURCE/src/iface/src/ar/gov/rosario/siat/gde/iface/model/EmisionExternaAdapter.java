//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter del ConstanciaDeu
 * 
 * @author tecso
 */
public class EmisionExternaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(EmisionExternaAdapter.class);
	
	public static final String NAME = "uploadEventoGesJudAdapterVO";

	private String fileNameLogAnalisis ="";
	
	private String fileName = "";
	
	private byte[] fileData;

	private List<FilaUploadGesjudEvento> listFilaGrabar = new ArrayList<FilaUploadGesjudEvento>();
	
	private Integer cantLineasGrabadas = 0;
		
	// Flags
	private boolean verLogAnalisisEnabled 	= false;
	private boolean verResultadoCargaEnabled 	= false;
	private boolean resultExito   			= true;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}


	public String getFileNameLogAnalisis() {
		return fileNameLogAnalisis;
	}

	public void setFileNameLogAnalisis(String fileNameLogAnalisis) {
		this.fileNameLogAnalisis = fileNameLogAnalisis;
	}

	public Integer getCantLineasGrabadas() {
		return cantLineasGrabadas;
	}

	public void setCantLineasGrabadas(Integer cantLineasGrabadas) {
		this.cantLineasGrabadas = cantLineasGrabadas;
	}

	
	public List<FilaUploadGesjudEvento> getListFilaGrabar() {
		return listFilaGrabar;
	}

	public void setListFilaGrabar(List<FilaUploadGesjudEvento> listFilaGrabar) {
		this.listFilaGrabar = listFilaGrabar;
	}

	/**
	 * Devuelve el la ruta completa el archivo.<br>
	 * /tmp/fileName.txt
	 * @return
	 */
	public String getFileNameCompleto(){
		return "/tmp/"+getFileName()+".txt";
	}
	
	public String getCantEventosGrabar(){
		return (listFilaGrabar!=null?String.valueOf(listFilaGrabar.size()):"0");
	}
	
	/**
	 * Verifica si ya contiene en la lista de filasGrabar, una fila con la misma gesJud, el mismo evento y misma fecha de la fila pasada como parametro 
	 * @param fila
	 * @return
	 */
	public boolean contieneFilaAgregar(FilaUploadGesjudEvento fila) {
		log.debug("contieneFilaAgregar: enter");
		if(listFilaGrabar!=null){
			for(FilaUploadGesjudEvento filaContenida: listFilaGrabar){
				boolean igualGesJud = filaContenida.getDesGesJud().equals(fila.getDesGesJud());
				boolean igualEvento = filaContenida.getCodEvento().intValue()==fila.getCodEvento().intValue();
				boolean igualFecha = DateUtil.dateCompare(filaContenida.getFechaEvento(), fila.getFechaEvento())==0;
				if(igualGesJud && igualEvento && igualFecha)
					return true;
			}
		}
		log.debug("contieneFilaAgregar: exit");
		return false;
	}

	
	// flags getters
	public boolean getVerLogAnalisisEnabled() {
		return verLogAnalisisEnabled;
	}
	
	public void setVerLogAnalisisEnabled(boolean verLogAnalisisEnabled) {
		this.verLogAnalisisEnabled = verLogAnalisisEnabled;
	}

	public boolean getResultExito() {
		return resultExito;
	}

	public void setResultExito(boolean resultExito) {
		this.resultExito = resultExito;
	}

	public boolean isVerResultadoCargaEnabled() {
		return verResultadoCargaEnabled;
	}

	public void setVerResultadoCargaEnabled(boolean verResultadoEnabled) {
		this.verResultadoCargaEnabled = verResultadoEnabled;
	}	
}
