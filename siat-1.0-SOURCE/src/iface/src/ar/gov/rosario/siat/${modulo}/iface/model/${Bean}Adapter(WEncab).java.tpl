package ar.gov.rosario.siat.${modulo}.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.${modulo}.iface.util.${Modulo}SecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ${Bean}
 * 
 * @author tecso
 */
public class ${Bean}Adapter extends SiatAdapterModel{
	
	public static final String NAME = "${bean}AdapterVO";
	public static final String ENC_NAME = "enc${Bean}AdapterVO";
	
    private ${Bean}VO ${bean} = new ${Bean}VO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public ${Bean}Adapter(){
    	super(${Modulo}SecurityConstants.ABM_${BEAN});
    	ACCION_MODIFICAR_ENCABEZADO = ${Modulo}SecurityConstants.ABM_${BEAN}_ENC;
    }
    
    //  Getters y Setters
	public ${Bean}VO get${Bean}() {
		return ${bean};
	}

	public void set${Bean}(${Bean}VO ${bean}VO) {
		this.${bean} = ${bean}VO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	// Permisos para ${BeanDetalle}
	public String getVer${BeanDetalle}Enabled() {
		return SiatBussImageModel.hasEnabledFlag
			(${Modulo}SecurityConstants.ABM_${BEANDETALLE}, BaseSecurityConstants.VER);
	}
	
	public String getModificar${BeanDetalle}Enabled() {
		return SiatBussImageModel.hasEnabledFlag
			(${Modulo}SecurityConstants.ABM_${BEANDETALLE}, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminar${BeanDetalle}Enabled() {
		return SiatBussImageModel.hasEnabledFlag
			(${Modulo}SecurityConstants.ABM_${BEANDETALLE}, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregar${BeanDetalle}Enabled() {
		return SiatBussImageModel.hasEnabledFlag
			(${Modulo}SecurityConstants.ABM_${BEANDETALLE}, BaseSecurityConstants.AGREGAR);
	}
}