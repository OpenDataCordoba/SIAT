//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Tipo de Proceso Masivo
 * Puede ser un Envio Judicial, Pre-Envio judicial, Reconfeccion ,etc
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipProMas")
public class TipProMas extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final Long ID_ENVIO_JUDICIAL = 1L;
	public static final Long ID_PRE_ENVIO_JUDICIAL = 2L;
	public static final Long ID_RECONFECCION = 3L;
	public static final Long ID_SELECCION_DEUDA = 4L;
	
	@Column(name = "desTipProMas")
	private String desTipProMas;

	//<#Propiedades#>
	
	// Constructores
	public TipProMas(){
		super();
	}
	
	public TipProMas(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipProMas getById(Long id) {
		return (TipProMas) GdeDAOFactory.getTipProMasDAO().getById(id);
	}
	
	public static TipProMas getByIdNull(Long id) {
		return (TipProMas) GdeDAOFactory.getTipProMasDAO().getByIdNull(id);
	}
	
	public static List<TipProMas> getList() {
		return (List<TipProMas>) GdeDAOFactory.getTipProMasDAO().getList();
	}
	
	public static List<TipProMas> getListActivos() {			
		return (List<TipProMas>) GdeDAOFactory.getTipProMasDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipProMas() {
		return desTipProMas;
	}

	public void setDesTipProMas(String desTipProMas) {
		this.desTipProMas = desTipProMas;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
	/*	if (StringUtil.isNullOrEmpty(getCodTipProMas())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPENVJUD_CODTIPENVJUD );
		}
		
		if (StringUtil.isNullOrEmpty(getDesTipProMas())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPENVJUD_DESTIPENVJUD);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codTipProMas");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, ExeError.TIPENVJUD_CODTIPENVJUD);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipProMas. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getTipProMasDAO().update(this);
	}

	/**
	 * Desactiva el TipProMas. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getTipProMasDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipProMas
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipProMas
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
	
	public AccionExp getAccionExpCorrespondiente(){
		if(TipProMas.ID_ENVIO_JUDICIAL.equals(this.getId())){
			return AccionExp.getById(AccionExp.ID_ENVIO_JUDICIAL);
		}else if(TipProMas.ID_PRE_ENVIO_JUDICIAL.equals(this.getId())){
			return AccionExp.getById(AccionExp.ID_PRE_ENVIO_JUDICIAL);
		}else if(TipProMas.ID_RECONFECCION.equals(this.getId())){
			return AccionExp.getById(AccionExp.ID_RECONFECCION_MASIVA);
		}else if (TipProMas.ID_SELECCION_DEUDA.equals(this.getId())){
			return AccionExp.getById(AccionExp.ID_SELECCION_DEUDA_GENERICA);
		}	
		return null;
	}
	
	public String getPrefijoInfoString(){
		String prefijo = " Proceso Masivo ";
		if(TipProMas.ID_ENVIO_JUDICIAL.equals(this.getId())){
			prefijo = " Envio a Judicial ";
		}else if(TipProMas.ID_PRE_ENVIO_JUDICIAL.equals(this.getId())){
			prefijo = " Pre envio a Judicial ";
		}else if(TipProMas.ID_RECONFECCION.equals(this.getId())){
			prefijo = " Reconfeccion Masiva ";
		}else if (TipProMas.ID_SELECCION_DEUDA.equals(this.getId())){
			prefijo = " Seleccion de deuda generica ";
		}	
		return prefijo;
	}
	
	
	public Boolean getEsEnvioJudicial(){
		return (TipProMas.ID_ENVIO_JUDICIAL.equals(this.getId()));
	}
	public Boolean getEsPreEnvioJudicial(){
		return (TipProMas.ID_PRE_ENVIO_JUDICIAL.equals(this.getId()));
	}
	public Boolean getEsReconfeccion(){
		return (TipProMas.ID_RECONFECCION.equals(this.getId()));
	}
	public Boolean getEsSeleccionDeuda(){
		return (TipProMas.ID_SELECCION_DEUDA.equals(this.getId()));
	}
	
	/**
	 * Obtiene la lista de Vias de Deudas para el Proceso Masivo de acuerdo al tipo de proceso masivo 
	 * @return List<ViaDeuda>
	 */	
	public List<ViaDeuda> getListViaDeudaForProcesoMasivo(){
		
		List<ViaDeuda> listViaDeudaForPM = new ArrayList<ViaDeuda>();
		
		ViaDeuda viaDeudaAdm = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
		ViaDeuda viaDeudaJud = ViaDeuda.getById(ViaDeuda.ID_VIA_JUDICIAL);

		if(TipProMas.ID_ENVIO_JUDICIAL.equals(this.getId())){
			listViaDeudaForPM.add(viaDeudaAdm);
		}else if(TipProMas.ID_PRE_ENVIO_JUDICIAL.equals(this.getId())){
			listViaDeudaForPM.add(viaDeudaAdm);
			listViaDeudaForPM.add(viaDeudaJud);
		}else if(TipProMas.ID_RECONFECCION.equals(this.getId())){
			listViaDeudaForPM.add(viaDeudaAdm);
		}else if(TipProMas.ID_SELECCION_DEUDA.equals(this.getId())){
			listViaDeudaForPM.add(viaDeudaAdm);
			listViaDeudaForPM.add(viaDeudaJud);
		}
		
		return listViaDeudaForPM;
	}
	
	/**
	 * Obtiene la lista de Tipos de Detalle de Seleccion Almanacenada para el Tipo de Proceso Masivo
	 * de acuerdo a la Via de Deuda
	 * @param  viaDeuda
	 * @return List<TipoSelAlm>
	 */
	public List<TipoSelAlm> getListTipoSelAlmDetForProcesoMasivo(ViaDeuda viaDeuda){
		
		List<TipoSelAlm> listTipoSelAlmDetForPM = new ArrayList<TipoSelAlm>();
		
		if(TipProMas.ID_ENVIO_JUDICIAL.equals(this.getId())){
			listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM));
		}else if(TipProMas.ID_PRE_ENVIO_JUDICIAL.equals(this.getId())){
			if(ViaDeuda.ID_VIA_ADMIN == viaDeuda.getId().longValue()){
				listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM));
				listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_ADM));
			}else{
				listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD));
				listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_JUD));
			}
		}else if(TipProMas.ID_RECONFECCION.equals(this.getId())){
			listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM));
		}else if(TipProMas.ID_SELECCION_DEUDA.equals(this.getId())){
			if(ViaDeuda.ID_VIA_ADMIN == viaDeuda.getId().longValue()){
				listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM));
				listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_ADM));
			}else{
				listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD));
				listTipoSelAlmDetForPM.add(TipoSelAlm.getById(TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_JUD));
			}
		}
		
		return listTipoSelAlmDetForPM;
	}


}
