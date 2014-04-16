//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;

/**
 * Adapter de Proceso de Asentamiento Delegado
 * 
 * @author tecso
 */
public class ProcesoAseDelAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "procesoAseDelAdapterVO";
	public static final String ENC_NAME = "aseDelAdapterVO";

	private AseDelVO aseDel = new AseDelVO();
	private PasoCorridaVO pasoCorrida1 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida2 = new PasoCorridaVO();

	private List<FileCorridaVO> listFileCorrida1 = new ArrayList<FileCorridaVO>();
	private List<FileCorridaVO> listFileCorrida2 = new ArrayList<FileCorridaVO>();
		
	//Flags
	private String paramPaso = "";
	private String paramEstadoEjercicio = "";
	private boolean paramActivar = true;
	private boolean paramReprogramar = false;
	private boolean paramCancelar = false;
	private boolean paramReiniciar = false;
	private boolean paramModificar = false;
	private boolean paramContinuar = false;
	
	public ProcesoAseDelAdapter(){
		super(BalSecurityConstants.ABM_PROCESO_ASEDEL);
		ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_PROCESO_ASEDEL_ENC;
	}

	// Getters y Setters
	public AseDelVO getAseDel() {
		return aseDel;
	}
	public void setAseDel(AseDelVO aseDel) {
		this.aseDel = aseDel;
	}
	public String getParamEstadoEjercicio() {
		return paramEstadoEjercicio;
	}
	public void setParamEstadoEjercicio(String paramEstadoEjercicio) {
		this.paramEstadoEjercicio = paramEstadoEjercicio;
	}
	public String getParamPaso() {
		return paramPaso;
	}
	public void setParamPaso(String paramPaso) {
		this.paramPaso = paramPaso;
	}
	public PasoCorridaVO getPasoCorrida1() {
		return pasoCorrida1;
	}
	public void setPasoCorrida1(PasoCorridaVO pasoCorrida1) {
		this.pasoCorrida1 = pasoCorrida1;
	}
	public PasoCorridaVO getPasoCorrida2() {
		return pasoCorrida2;
	}
	public void setPasoCorrida2(PasoCorridaVO pasoCorrida2) {
		this.pasoCorrida2 = pasoCorrida2;
	}
	public List<FileCorridaVO> getListFileCorrida1() {
		return listFileCorrida1;
	}
	public void setListFileCorrida1(List<FileCorridaVO> listFileCorrida1) {
		this.listFileCorrida1 = listFileCorrida1;
	}
	public List<FileCorridaVO> getListFileCorrida2() {
		return listFileCorrida2;
	}
	public void setListFileCorrida2(List<FileCorridaVO> listFileCorrida2) {
		this.listFileCorrida2 = listFileCorrida2;
	}

	public boolean isParamActivar() {
		return paramActivar;
	}
	public void setParamActivar(boolean paramActivar) {
		this.paramActivar = paramActivar;
	}
	public boolean isParamCancelar() {
		return paramCancelar;
	}
	public void setParamCancelar(boolean paramCancelar) {
		this.paramCancelar = paramCancelar;
	}
	public boolean isParamReiniciar() {
		return paramReiniciar;
	}
	public void setParamReiniciar(boolean paramReiniciar) {
		this.paramReiniciar = paramReiniciar;
	}
	public boolean isParamReprogramar() {
		return paramReprogramar;
	}
	public void setParamReprogramar(boolean paramReprogramar) {
		this.paramReprogramar = paramReprogramar;
	}
	public boolean isParamModificar() {
		return paramModificar;
	}
	public void setParamModificar(boolean paramModificar) {
		this.paramModificar = paramModificar;
	}
	public boolean isParamContinuar() {
		return paramContinuar;
	}
	public void setParamContinuar(boolean paramContinuar) {
		this.paramContinuar = paramContinuar;
	}
}
