//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.SiatScriptVO;
import ar.gov.rosario.siat.pro.iface.util.ProSecurityConstants;

/**
 * Adapter para enviar archivos desde el paso de una corrida
 *   
 * @author tecso
 */
public class EnvioArchivosAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "envioArchivosAdapterVO";

	public static final String PREVIEW = "envioArchivosPreviewAdapterVO";
	
 	public static final String PARAM_ID_PASOCORRIDA = "idPasoCorrida";

	private PasoCorridaVO pasoCorrida = new PasoCorridaVO();
	
	private SiatScriptVO siatScript = new SiatScriptVO();
	
	private List<SiatScriptVO> listSiatScript = new ArrayList<SiatScriptVO>();
	
	private List<FileCorridaVO> listFileCorrida = new ArrayList<FileCorridaVO>();

	private String usuario = "";
	
	private String password = "";

	private String[] listFileNameSelected;
	
	// Constructores
	public EnvioArchivosAdapter() {       
       super(ProSecurityConstants.ABM_CORRIDA);        
    }

	// Getters y Setters
	public PasoCorridaVO getPasoCorrida() {
		return pasoCorrida;
	}

	public void setPasoCorrida(PasoCorridaVO pasoCorrida) {
		this.pasoCorrida = pasoCorrida;
	}

	public SiatScriptVO getSiatScript() {
		return siatScript;
	}

	public void setSiatScript(SiatScriptVO siatScript) {
		this.siatScript = siatScript;
	}

	public List<SiatScriptVO> getListSiatScript() {
		return listSiatScript;
	}

	public void setListSiatScript(List<SiatScriptVO> listSiatScript) {
		this.listSiatScript = listSiatScript;
	}

	public List<FileCorridaVO> getListFileCorrida() {
		return listFileCorrida;
	}
	
	public void setListFileCorrida(List<FileCorridaVO> listFileCorrida) {
		this.listFileCorrida = listFileCorrida;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getListFileNameSelected() {
		return listFileNameSelected;
	}

	public void setListFileNameSelected(String[] listFileNameSelected) {
		this.listFileNameSelected = listFileNameSelected;
	}
}
