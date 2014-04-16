//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

public class ExportPageModel extends PageModel {

	private byte[] File = {};
	private String fileOutPutName = "";

	// Getters y setters ******************************************************************************************


	public String getFileOutPutName() {
		return fileOutPutName;
	}
	public void setFileOutPutName(String fileOutPutName) {
		this.fileOutPutName = fileOutPutName;
	}


	public byte[] getFile() {
		return File;
	}
	public void setFile(byte[] file) {
		File = file;
	}
}
