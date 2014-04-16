//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatVO;


/**
 * Value Object del SiatScriptUsr
 * @author tecso
 *
 */
public class SiatScriptUsrVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "siatScriptUsrVO";
	
	private ProcesoVO proceso = new ProcesoVO();
	private SiatScriptVO siatScript = new SiatScriptVO();
	private UsuarioSiatVO usuarioSiat = new UsuarioSiatVO();
	
	private List<ProcesoVO> listProceso = new ArrayList<ProcesoVO>();
	private List<UsuarioSiatVO> listUsuarioSiat = new ArrayList<UsuarioSiatVO>(); 
	
		
	// Constructores
	public SiatScriptUsrVO() {
		super();
	}

	public SiatScriptUsrVO(int id, String desArea) {
		super();
		setId(new Long(id));
//		setDesArea(desArea);
	}

	// Getters y Setters
	public ProcesoVO getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoVO proceso) {
		this.proceso = proceso;
	}

	public SiatScriptVO getSiatScript() {
		return siatScript;
	}

	public void setSiatScript(SiatScriptVO siatScript) {
		this.siatScript = siatScript;
	}

	public UsuarioSiatVO getUsuarioSiat() {
		return usuarioSiat;
	}

	public void setUsuarioSiat(UsuarioSiatVO usuarioSiat) {
		this.usuarioSiat = usuarioSiat;
	}

	public void setListProceso(List<ProcesoVO> listProceso) {
		this.listProceso = listProceso;
	}

	public List<ProcesoVO> getListProceso() {
		return listProceso;
	}

	public void setListUsuarioSiat(List<UsuarioSiatVO> listUsuarioSiat) {
		this.listUsuarioSiat = listUsuarioSiat;
	}

	public List<UsuarioSiatVO> getListUsuarioSiat() {
		return listUsuarioSiat;
	}
}
