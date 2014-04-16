//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter del PlaEnvDeuPro
 * 
 * @author tecso
 */
public class PlaEnvDeuProAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaEnvDeuProAdapterVO";
	
    private PlaEnvDeuProVO plaEnvDeuPro = new PlaEnvDeuProVO();

    private String[] idsConstanciasHabilitar;
    
    private String nombreArchivoCD="";
    
    // Flags para mostrar los botones habilitados o deshabilitados segun el estado de la planilla
    public String btnHabilitarPlanillaBussEnabled ="disabled"; 
    public String btnRecomponerPlanillaBussEnabled ="disabled";
    public String btnHabilitarConstanciasBussEnabled ="disabled";
    
    // flags para permisos ( para mostrar o no los botones)
	private boolean verConstanciasEnabled = true;
	private boolean habilitarConstanciasDeudaEnabled = true;
	private boolean verListadoConstanciasEnabled = true;
	private boolean habilitarPlanillaEnabled= true;
	private boolean recomponerPlanillaEnabled = true;
	private boolean recomponerConstanciaEnabled = true;
	private boolean traspasarConstanciaEnabled = true;
	
    // Constructores
    public PlaEnvDeuProAdapter(){
    	super(GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS);
    }
    
    /**
     * valida si la constancia con el id pasado por parametro, esta checkeada (Esto es para la pantalla de habilitar constancias, donde deben estar todas seleccionadas para realizar la habilitacion)
     * @param idConstancia
     * @return
     */
    public boolean tieneIdConstanciaCheckeado(long idConstancia){
    	for(String id:idsConstanciasHabilitar){
    		if(id!=null && id.trim().equals(String.valueOf(idConstancia)))
    			return true;
    	}
    	return false;
    }
    
    /**
     * Verifica si alguna constancia no fue seleccionada para habilitar
     * @return
     */
    public boolean tienenConstanciasNoCheckeadas(){
    	if(idsConstanciasHabilitar==null)
    		return true;
    	
    	for(ConstanciaDeuVO constancia: plaEnvDeuPro.getListConstanciaDeu()){
    		if(!tieneIdConstanciaCheckeado(constancia.getId()))
    			return true;
    	}
    	return false;
    }
    
    //  Getters y Setters
	public PlaEnvDeuProVO getPlaEnvDeuPro() {
		return plaEnvDeuPro;
	}

	public void setPlaEnvDeuPro(PlaEnvDeuProVO plaEnvDeuProVO) {
		this.plaEnvDeuPro = plaEnvDeuProVO;
	}
	
	// View getters
	public String getVerConstanciasEnabled() {
		return SiatBussImageModel.hasEnabledFlag(verConstanciasEnabled, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, GdeSecurityConstants.MTD_VER_CONSTANCIAS);		
	}

	public void setVerConstanciasEnabled(boolean verConstanciasEnabled) {
		this.verConstanciasEnabled = verConstanciasEnabled;
	}

	public String getVerListadoConstanciasEnabled() {
		return SiatBussImageModel.hasEnabledFlag(verListadoConstanciasEnabled, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, GdeSecurityConstants.MTD_VER_LISTADO_CONSTANCIAS);
	}

	public void setVerListadoConstanciasEnabled(boolean verListadoConstanciasEnabled) {
		this.verListadoConstanciasEnabled = verListadoConstanciasEnabled;
	}

	public String getRecomponerPlanillaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(recomponerPlanillaEnabled, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, GdeSecurityConstants.MTD_RECOMPONER_PLANILLA);		
	}

	public void setRecomponerPlanillaEnabled(boolean recomponerPlanillaEnabled) {
		this.recomponerPlanillaEnabled = recomponerPlanillaEnabled;
	}

	
	public String getHabilitarPlanillaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(habilitarPlanillaEnabled, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, GdeSecurityConstants.MTD_HABILITAR_PLANILLA);		
	}

	public void setHabilitarPlanillaEnabled(boolean habilitarPlanilla) {
		this.habilitarPlanillaEnabled = habilitarPlanilla;
	}

	
	public String getRecomponerConstanciaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(recomponerConstanciaEnabled, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, GdeSecurityConstants.MTD_RECOMPONER_CONSTANCIA);
	}

	public void setRecomponerConstanciaEnabled(boolean recomponerConstanciaEnabled) {
		this.recomponerConstanciaEnabled = recomponerConstanciaEnabled;
	}

	public String getTraspasarConstanciaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(traspasarConstanciaEnabled, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, GdeSecurityConstants.MTD_TRASPASAR_CONSTANCIA);
	}

	public void setTraspasarConstanciaEnabled(boolean traspasarConstanciaEnabled) {
		this.traspasarConstanciaEnabled = traspasarConstanciaEnabled;
	}

	public String getHabilitarConstanciasDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(habilitarConstanciasDeudaEnabled, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, GdeSecurityConstants.MTD_HABILITAR_CONSTANCIAS_DEUDA);
	}

	public void setHabilitarConstanciasDeudaEnabled(
			boolean habilitarConstanciasDeudaEnabled) {
		this.habilitarConstanciasDeudaEnabled = habilitarConstanciasDeudaEnabled;
	}

	public String getBtnHabilitarPlanillaBussEnabled() {
		return getStringBtnEnabled(btnHabilitarPlanillaBussEnabled);
	}

	public void setBtnHabilitarPlanillaBussEnabled(String getBtnHabilitarEnabled) {
		this.btnHabilitarPlanillaBussEnabled = getBtnHabilitarEnabled;
	}

	public String getBtnRecomponerPlanillaBussEnabled() {
		return getStringBtnEnabled(btnRecomponerPlanillaBussEnabled);
	}

	public void setBtnRecomponerPlanillaBussEnabled(String btnRecomponerPlanillaEnabled) {
		this.btnRecomponerPlanillaBussEnabled = btnRecomponerPlanillaEnabled;
	}

	public String getBtnHabilitarConstanciasBussEnabled() {
		return getStringBtnEnabled(btnHabilitarConstanciasBussEnabled);
	}

	public void setBtnHabilitarConstanciasBussEnabled(
			String btnHabilitarConstanciasEnabled) {
		this.btnHabilitarConstanciasBussEnabled = btnHabilitarConstanciasEnabled;
	}
	
	public String[] getIdsConstanciasHabilitar() {
		return idsConstanciasHabilitar;
	}
	
	public void setIdsConstanciasHabilitar(String[] idsConstanciasHabilitar) {
		this.idsConstanciasHabilitar = idsConstanciasHabilitar;
	}
	
	/**
	 * Genera el String para habilitar o deshabilitar un boton 
	 * @param value El valor de una de las banderas para habilitar/deshabilitar botones
	 * @return La cadena " disabled='disabled' " o una cadena vacia
	 */
	private String getStringBtnEnabled(String value){
		if(value.trim().equals(StringUtil.DISABLED)){
			return " disabled="+StringUtil.DISABLED;
		}else{
			return "";
		}
	}

	public String getNombreArchivoCD() {
		return nombreArchivoCD;
	}

	public void setNombreArchivoCD(String nombreArchivoCD) {
		this.nombreArchivoCD = nombreArchivoCD;
	}


}
