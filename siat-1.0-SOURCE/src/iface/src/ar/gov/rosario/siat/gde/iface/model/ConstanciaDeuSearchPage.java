//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * SearchPage del ConstanciaDeu
 * 
 * @author Tecso
 *
 */
public class ConstanciaDeuSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ConstanciaDeuSearchPageVO";
	
	private ConstanciaDeuVO constanciaDeu = new ConstanciaDeuVO();
	
	private Date fechaEnvioDesde;
	private Date fechaEnvioHasta;
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<EstConDeuVO> listEstConDeuVO = new ArrayList<EstConDeuVO>();
	
	/** Es para cuando hay que buscar constancias que no esten en determinados estados*/
	private List<EstConDeuVO> listEstConDeuVOExluir = new ArrayList<EstConDeuVO>(); 
	
	/** Es para deshabilitar en la seleccion contancias en ciertos estados **/
	private List<EstConDeuVO> listEstConDeuVOAExluirEnSeleccion = new ArrayList<EstConDeuVO>();
	
	private String fechaEnvioDesdeView = "";
	private String fechaEnvioHastaView = "";
	
	// flags de permisos
	private boolean recomponerConstanciaEnabled = true;
	private boolean habilitarEnabled=true;
	private boolean imprimirConstanciaEnabled = true;
	private boolean habilitarProcuradorEnabled=true;
	private boolean habilitarRecursoEnabled=true;
	private boolean habilitarCuentaEnabled=true;	
	private boolean habilitarEstadoEnabled=true;
	/** Si es true filtra por las que tienen fechaHabilitacion != null  */
	private boolean buscarSoloHabilitadas=false;
	private boolean buscarCreadasManualmente=false;
	private boolean anularConstanciaEnabled=true;
	
	// Constructores
	public ConstanciaDeuSearchPage() {       
       super(GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL);        
    }
	
	// Getters y Setters
	public ConstanciaDeuVO getConstanciaDeu() {
		return constanciaDeu;
	}
	public void setConstanciaDeu(ConstanciaDeuVO ConstanciaDeu) {
		this.constanciaDeu = ConstanciaDeu;
	}
	public Date getFechaEnvioDesde() {
		return fechaEnvioDesde;
	}

	public void setFechaEnvioDesde(Date fechaEnvioDesde) {
		this.fechaEnvioDesde = fechaEnvioDesde;
	}

	public Date getFechaEnvioHasta() {
		return fechaEnvioHasta;
	}

	public void setFechaEnvioHasta(Date fechaEnvioHasta) {
		this.fechaEnvioHasta = fechaEnvioHasta;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<EstConDeuVO> getListEstConDeuVO() {
		return listEstConDeuVO;
	}

	public void setListEstConDeuVO(List<EstConDeuVO> listEstConDeuVO) {
		this.listEstConDeuVO = listEstConDeuVO;
	}
	
	public List<EstConDeuVO> getListEstConDeuVOAExluirEnSeleccion() {
		return listEstConDeuVOAExluirEnSeleccion;
	}

	public void setListEstConDeuVOAExluirEnSeleccion(
			List<EstConDeuVO> listEstConDeuVOAExluirEnSeleccion) {
		this.listEstConDeuVOAExluirEnSeleccion = listEstConDeuVOAExluirEnSeleccion;
	}

	public String getRecomponerConstanciaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(recomponerConstanciaEnabled, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, GdeSecurityConstants.MTD_RECOMPONER_CONSTANCIA);
	}

	public void setRecomponerConstanciaEnabled(boolean recomponerConstanciaEnabled) {
		this.recomponerConstanciaEnabled = recomponerConstanciaEnabled;
	}

	public String getHabilitarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(habilitarEnabled, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, GdeSecurityConstants.MTD_HABILITAR_CONSTANCIA);
	}

	public void setHabilitarEnabled(boolean habilitarEnabled) {
		this.habilitarEnabled = habilitarEnabled;
	}

	public String getImprimirConstanciaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(imprimirConstanciaEnabled, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, GdeSecurityConstants.MTD_IMPRIMIR_CONSTANCIA);
	}

	public void setImprimirConstanciaEnabled(boolean imprimirConstancia) {
		this.imprimirConstanciaEnabled = imprimirConstancia;
	}
	
	public boolean isHabilitarCuentaEnabled() {
		return habilitarCuentaEnabled;
	}

	public void setHabilitarCuentaEnabled(boolean habilitarCuentaEnabled) {
		this.habilitarCuentaEnabled = habilitarCuentaEnabled;
	}

	public boolean isHabilitarEstadoEnabled() {
		return habilitarEstadoEnabled;
	}

	public void setHabilitarEstadoEnabled(boolean habilitarEstadoEnabled) {
		this.habilitarEstadoEnabled = habilitarEstadoEnabled;
	}

	public boolean isHabilitarProcuradorEnabled() {
		return habilitarProcuradorEnabled;
	}

	public void setHabilitarProcuradorEnabled(boolean habilitarProcuradorEnabled) {
		this.habilitarProcuradorEnabled = habilitarProcuradorEnabled;
	}

	public boolean isHabilitarRecursoEnabled() {
		return habilitarRecursoEnabled;
	}

	public void setHabilitarRecursoEnabled(boolean habilitarRecursoEnabled) {
		this.habilitarRecursoEnabled = habilitarRecursoEnabled;
	}

	public String getAnularConstanciaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(anularConstanciaEnabled, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, GdeSecurityConstants.MTD_ANULAR_CONSTANCIA);
	}

	public boolean getBuscarSoloHabilitadas() {
		return buscarSoloHabilitadas;
	}

	public void setBuscarSoloHabilitadas(boolean buscarSoloHabilitadas) {
		this.buscarSoloHabilitadas = buscarSoloHabilitadas;
	}

	public boolean getBuscarCreadasManualmente() {
		return buscarCreadasManualmente;
	}

	public void setBuscarCreadasManualmente(boolean buscarCreadasManualmente) {
		this.buscarCreadasManualmente = buscarCreadasManualmente;
	}

	public List<EstConDeuVO> getListEstConDeuVOExluir() {
		return listEstConDeuVOExluir;
	}

	public void setListEstConDeuVOExluir(List<EstConDeuVO> listEstConDeuVOExluir) {
		this.listEstConDeuVOExluir = listEstConDeuVOExluir;
	}

	// View getters
	public String getFechaEnvioDesdeView() {
		return fechaEnvioDesdeView;
	}

	public void setFechaEnvioDesdeView(String fechaEnvioDesdeView) {
		this.fechaEnvioDesdeView = fechaEnvioDesdeView;
	}

	public String getFechaEnvioHastaView() {
		return fechaEnvioHastaView;
	}

	public void setFechaEnvioHastaView(String fechaEnvioHastaView) {
		this.fechaEnvioHastaView = fechaEnvioHastaView;
	}

}
