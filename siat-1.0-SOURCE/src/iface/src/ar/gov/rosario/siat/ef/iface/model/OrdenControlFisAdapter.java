//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;

/**
 * Adapter del Supervisor
 * 
 * @author tecso
 */
public class OrdenControlFisAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "ordenControlFisAdapterVO";

	public static final String NAME_ORDENCONTROL_ORIG = "ordenControlOriginalFisAdapterVO";

    private OrdenControlVO ordenControl = new OrdenControlVO();
    
    private InicioInvVO inicioInv = new InicioInvVO();
    
    List<SupervisorVO> listSupervisor = new ArrayList<SupervisorVO>();
	List<InspectorVO>  listInspector = new ArrayList<InspectorVO>();
	List<InspectorVO> listInspectorSelec = new ArrayList<InspectorVO>();
	List<SupervisorVO> listSupervisorSelec = new ArrayList<SupervisorVO>();

    private List<OrdenControlVO> listOrdenControlAnt = new ArrayList<OrdenControlVO>();
    
  
    private Integer selectedInspector = null;
    private Integer selectedSupervisor = null;
    // flags
  
  
    private boolean cerrarOrdenBussEnabled;
    
    private boolean verHistoricoBussEnabled=true;
    private boolean verOrdenesAntBussEnabled=true;
    private boolean verListaOrdenesAnt=false;

	private boolean agregarPeriodoEnabled=true;
	private boolean eliminarPeriodoEnabled=true;
	
	private boolean agregarActaEnabled=true;
	private boolean eliminarActaEnabled=true;
	private boolean modificarActaEnabled=true;
	
	private boolean agregarInicioInvEnabled=true;
	private boolean modificarInicioInvEnabled=true;

	private boolean agregarPlaFueDatEnabled=true;
	private boolean modificarPlaFueDatEnabled= true;
	private boolean eliminarPlaFueDatEnabled=true;

	private boolean agregarComparacionEnabled=true;
	private boolean modificarComparacionEnabled=true;
	private boolean eliminarComparacionEnabled=true;

	private boolean agregarOrdConBasImpEnabled=true;
	private boolean cargarAjuOrdConBasImpEnabled=true;
	private boolean eliminarOrdConBasImpEnabled=true;

	private boolean cargarAlicuotasOrdConBasImpEnabled=true;

	private boolean agregarDetAjuEnabled=true;
	private boolean modificarDetAjuEnabled=true;
	private boolean eliminarDetAjuEnabled=true;

	private boolean enviarMesaEntradaEnabled=true;
	
	private boolean enviarMesaEntradaBussEnabled = true;
	private boolean modificarMesaEntradaEnabled = true;
	private boolean eliminarMesaEntradaEnabled = true;
	private boolean verMesaEntradaEnabled = true;
	private boolean agregarMesaEntradaEnabled = true;

	private boolean agregarAproOrdConEnabled = true;
	private boolean verAproOrdConEnabled = true;
	private boolean modificarAproOrdConEnabled = true;
	private boolean eliminarAproOrdConEnabled = true;

	private boolean agregarDetAjuDocSopEnabled = true;
	private boolean verDetAjuDocSopEnabled = true;
	private boolean modificarDetAjuDocSopEnabled = true;
	private boolean eliminarDetAjuDocSopEnabled = true;

	private boolean cerrarOrdenControlEnabled = true;
	private boolean administrarOrden = false;
	
    
	// Constructores
    public OrdenControlFisAdapter(){
    	super(EfSecurityConstants.ADM_ORDENCONTROLFIS);
    }

    //  Getters y Setters
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControlVO) {
		this.ordenControl = ordenControlVO;
	}

	public List<OrdenControlVO> getListOrdenControlAnt() {
		return listOrdenControlAnt;
	}
	
	public List<SupervisorVO> getListSupervisor() {
		return listSupervisor;
	}

	public void setListSupervisor(List<SupervisorVO> listSupervisor) {
		this.listSupervisor = listSupervisor;
	}

	public List<InspectorVO> getListInspector() {
		return listInspector;
	}

	public void setListInspector(List<InspectorVO> listInspector) {
		this.listInspector = listInspector;
	}	

	public void setListOrdenControlAnt(List<OrdenControlVO> listOrdenControlAnt) {
		this.listOrdenControlAnt = listOrdenControlAnt;
	}

	public boolean getVerHistoricoBussEnabled() {
		return verHistoricoBussEnabled;
	}

	public void setVerHistoricoBussEnabled(boolean verHistoricio) {
		this.verHistoricoBussEnabled = verHistoricio;
	}

	public boolean getVerOrdenesAntBussEnabled() {
		return verOrdenesAntBussEnabled;
	}

	public void setVerOrdenesAntBussEnabled(boolean verOrdenesAnt) {
		this.verOrdenesAntBussEnabled = verOrdenesAnt;
	}

	public boolean getVerListaOrdenesAnt() {
		return verListaOrdenesAnt;
	}

	public void setVerListaOrdenesAnt(boolean verListaOrdenesAnt) {
		this.verListaOrdenesAnt = verListaOrdenesAnt;
	}
    
	public InicioInvVO getInicioInv() {
		return inicioInv;
	}
	
	public void setInicioInv(InicioInvVO inicioInvVO) {
		this.inicioInv = inicioInvVO;
	}
	
	public List<InspectorVO> getListInspectorSelec() {
		return listInspectorSelec;
	}

	public void setListInspectorSelec(List<InspectorVO> listInspectorSelec) {
		this.listInspectorSelec = listInspectorSelec;
	}
	
	public List<SupervisorVO> getListSupervisorSelec() {
		return listSupervisorSelec;
	}

	public void setListSupervisorSelec(List<SupervisorVO> listSupervisorSelec) {
		this.listSupervisorSelec = listSupervisorSelec;
	}
	
    public boolean isAdministrarOrden() {
		return administrarOrden;
	}

	public void setAdministrarOrden(boolean administrarOrden) {
		this.administrarOrden = administrarOrden;
	}
	
		
	// View getters
	
	// flags permisos PeriodoOrden
	public String getAgregarPeriodoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarPeriodoEnabled, EfSecurityConstants.ABM_PERIODOORDEN, BaseSecurityConstants.AGREGAR);
	}
	
	public String getEliminarPeriodoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(eliminarPeriodoEnabled, EfSecurityConstants.ABM_PERIODOORDEN, BaseSecurityConstants.ELIMINAR);
	}
	
	// flags permisos Acta
	public String getAgregarActaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarActaEnabled, EfSecurityConstants.ABM_ACTA_ENC, BaseSecurityConstants.AGREGAR);
	}
	
	public String getModificarActaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(modificarActaEnabled, EfSecurityConstants.ABM_ACTA_ENC, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getEliminarActaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(eliminarActaEnabled, EfSecurityConstants.ABM_ACTA_ENC, BaseSecurityConstants.ELIMINAR);
	}
	
	// flags permisos InicioInv
	public String getAgregarInicioInvEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarInicioInvEnabled, EfSecurityConstants.ABM_INICIOINV, BaseSecurityConstants.AGREGAR);
	}
	
	public String getModificarInicioInvEnabled() {
		return SiatBussImageModel.hasEnabledFlag(modificarInicioInvEnabled, EfSecurityConstants.ABM_INICIOINV, BaseSecurityConstants.MODIFICAR);
	}	
	
	// flags permisos plaFueDat
	public String getAgregarPlaFueDatEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarPlaFueDatEnabled, EfSecurityConstants.ABM_PLAFUEDAT_ENC, BaseSecurityConstants.AGREGAR);
	}
	
	public String getModificarPlaFueDatEnabled() {
		return SiatBussImageModel.hasEnabledFlag(modificarPlaFueDatEnabled, EfSecurityConstants.ABM_PLAFUEDAT, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getEliminarPlaFueDatEnabled() {
		return SiatBussImageModel.hasEnabledFlag(eliminarPlaFueDatEnabled, EfSecurityConstants.ABM_PLAFUEDAT, BaseSecurityConstants.ELIMINAR);
	}
	
	// flags permisos Comparacion
	public String getAgregarComparacionEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarComparacionEnabled, EfSecurityConstants.ABM_COMPARACION_ENC, BaseSecurityConstants.AGREGAR);
	}

	public String getModificarComparacionEnabled(){
		return SiatBussImageModel.hasEnabledFlag(modificarComparacionEnabled, EfSecurityConstants.ABM_COMPARACION, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getEliminarComparacionEnabled(){
		return SiatBussImageModel.hasEnabledFlag(eliminarComparacionEnabled, EfSecurityConstants.ABM_COMPARACION, BaseSecurityConstants.ELIMINAR);
	}
	
	// flags permisos ordConBasImp
	public String getAgregarOrdConBasImpEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarOrdConBasImpEnabled, EfSecurityConstants.ABM_ORDCONBASIMP, BaseSecurityConstants.AGREGAR);
	}

	public String getEliminarOrdConBasImpEnabled(){
		return SiatBussImageModel.hasEnabledFlag(eliminarOrdConBasImpEnabled, EfSecurityConstants.ABM_ORDCONBASIMP, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getCargarAjuOrdConBasImpEnabled(){
		return SiatBussImageModel.hasEnabledFlag(cargarAjuOrdConBasImpEnabled, EfSecurityConstants.ABM_ORDCONBASIMP, EfSecurityConstants.ACT_CARGAR_AJUSTES);
	}	

	public String getCargarAlicuotasOrdConBasImpEnabled(){
		return SiatBussImageModel.hasEnabledFlag(cargarAlicuotasOrdConBasImpEnabled, EfSecurityConstants.ABM_ORDCONBASIMP, EfSecurityConstants.ACT_CARGAR_ALICUOTAS);
	}

	// flags permisos DetAju
	public String getAgregarDetAjuEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarDetAjuEnabled, EfSecurityConstants.ABM_DETAJU_ENC, BaseSecurityConstants.AGREGAR);
	}

	public String getModificarDetAjuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(modificarDetAjuEnabled, EfSecurityConstants.ABM_DETAJU, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getEliminarDetAjuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(eliminarDetAjuEnabled, EfSecurityConstants.ABM_DETAJU, BaseSecurityConstants.ELIMINAR);
	}

	public void setEliminarDetAjuEnabled(boolean eliminarDetAjuEnabled) {
		this.eliminarDetAjuEnabled = eliminarDetAjuEnabled;
	}
	
	public Integer getSelectedInspector() {
		return selectedInspector;
	}

	public void setSelectedInspector(Integer selectedInspector) {
		this.selectedInspector = selectedInspector;
	}

	public boolean getCerrarOrdenBussEnabled() {
		return cerrarOrdenBussEnabled;
	}

	public void setCerrarOrdenBussEnabled(boolean cerrarOrdenBussEnabled) {
		this.cerrarOrdenBussEnabled = cerrarOrdenBussEnabled;
	}

	public boolean getEnviarMesaEntradaBussEnabled() {
		return enviarMesaEntradaBussEnabled;
	}

	public void setEnviarMesaEntradaBussEnabled(boolean enviarMesaEntradaBussEnabled) {
		this.enviarMesaEntradaBussEnabled = enviarMesaEntradaBussEnabled;
	}
	
	public String getEnviarMesaEntradaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(enviarMesaEntradaEnabled, EfSecurityConstants.ADM_ORDENCONTROLFIS, EfSecurityConstants.MTD_ENVIAR_MESA_ENTRADA);
	}

	public String getAgregarMesaEntradaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarMesaEntradaEnabled , EfSecurityConstants.ABM_MESAENTRADA, BaseSecurityConstants.AGREGAR);
	}
	public String getVerMesaEntradaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(verMesaEntradaEnabled, EfSecurityConstants.ABM_MESAENTRADA, BaseSecurityConstants.VER);
	}
	public String getModificarMesaEntradaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(modificarMesaEntradaEnabled, EfSecurityConstants.ABM_MESAENTRADA, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarMesaEntradaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(eliminarMesaEntradaEnabled, EfSecurityConstants.ABM_MESAENTRADA, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getAgregarAproOrdConEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarAproOrdConEnabled , EfSecurityConstants.ABM_APROORDCON, BaseSecurityConstants.AGREGAR);
	}
	public String getVerAproOrdConEnabled(){
		return SiatBussImageModel.hasEnabledFlag(verAproOrdConEnabled, EfSecurityConstants.ABM_APROORDCON, BaseSecurityConstants.VER);
	}
	public String getModificarAproOrdConEnabled(){
		return SiatBussImageModel.hasEnabledFlag(modificarAproOrdConEnabled, EfSecurityConstants.ABM_APROORDCON, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarAproOrdConEnabled(){
		return SiatBussImageModel.hasEnabledFlag(eliminarAproOrdConEnabled, EfSecurityConstants.ABM_APROORDCON, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarDetAjuDocSopEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarDetAjuDocSopEnabled , EfSecurityConstants.ABM_DETAJUDOCSOP, BaseSecurityConstants.AGREGAR);
	}
	public String getVerDetAjuDocSopEnabled(){
		return SiatBussImageModel.hasEnabledFlag(verDetAjuDocSopEnabled, EfSecurityConstants.ABM_DETAJUDOCSOP, BaseSecurityConstants.VER);
	}
	public String getModificarDetAjuDocSopEnabled(){
		return SiatBussImageModel.hasEnabledFlag(modificarDetAjuDocSopEnabled, EfSecurityConstants.ABM_DETAJUDOCSOP, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarDetAjuDocSopEnabled(){
		return SiatBussImageModel.hasEnabledFlag(eliminarDetAjuDocSopEnabled, EfSecurityConstants.ABM_DETAJUDOCSOP, BaseSecurityConstants.ELIMINAR);
	}

	public String getCerrarOrdenControlEnabled(){
		return SiatBussImageModel.hasEnabledFlag(cerrarOrdenControlEnabled , EfSecurityConstants.ADM_ORDENCONTROLFIS, EfSecurityConstants.CERRAR_ORDENCONTROL);
	}

	// Permisos para ComAju
	public String getVerComAjuEnabled() {
		return SiatBussImageModel.hasEnabledFlag(EfSecurityConstants.ABM_COMAJU, BaseSecurityConstants.VER);
	}
	
	public String getModificarComAjuEnabled() {
		return SiatBussImageModel.hasEnabledFlag(EfSecurityConstants.ABM_COMAJU, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarComAjuEnabled() {
		return SiatBussImageModel.hasEnabledFlag(EfSecurityConstants.ABM_COMAJU, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarComAjuEnabled() {
		return SiatBussImageModel.hasEnabledFlag(EfSecurityConstants.ABM_COMAJU_ENC, BaseSecurityConstants.AGREGAR);
	}

	public Integer getSelectedSupervisor() {
		return selectedSupervisor;
	}

	public void setSelectedSupervisor(Integer selectedSupervisor) {
		this.selectedSupervisor = selectedSupervisor;
	}

}
