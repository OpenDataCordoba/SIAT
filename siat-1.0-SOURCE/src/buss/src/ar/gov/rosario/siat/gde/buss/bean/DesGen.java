//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Descuentos Generales
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_desGen")
public class DesGen extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "desDesGen")
	private String desDesGen;
	
	@Column(name = "porDes")
	private Double porDes;
		
    @Column(name="idCaso")
	private String idCaso;
	
	@Column(name = "leyendaDesGen")
	private String leyendaDesGen;
	
	
	public DesGen(){
		super();
	}

	// Getters y Setters
	
	public String getDesDesGen(){
		return desDesGen;
	}
	public void setDesDesGen(String desDesGen){
		this.desDesGen = desDesGen;
	}
	public Double getPorDes(){
		return porDes;
	}
	public void setPorDes(Double porDes){
		this.porDes = porDes;
	}
	
	public String getPorDesString(){
		return this.porDes.toString();
	}
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public String getLeyendaDesGen(){
		return leyendaDesGen;
	}
	public void setLeyendaDesGen(String leyendaDesGen){
		this.leyendaDesGen = leyendaDesGen;
	}
	
	// Metodos de Clase
	public static DesGen getById(Long id) {
		return (DesGen) GdeDAOFactory.getDesGenDAO().getById(id); 
							
	}
	
	public static DesGen getByIdNull(Long id) {
		return (DesGen) GdeDAOFactory.getDesGenDAO().getByIdNull(id);
	}
	
	public static List<DesGen> getList() {			
		return (ArrayList<DesGen>) GdeDAOFactory.getDesGenDAO().getList(); 
	}
	public static List<DesGen> getListActivos() {			
		return (ArrayList<DesGen>) GdeDAOFactory.getDesGenDAO().getListActiva(); 
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
	
		this.validate();
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
	
		this.validate();
		
		if (hasError()) {
			return false;
		}
	
		return !hasError();
	}

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
				
		//Validaciones de Requeridos
		if (StringUtil.isNullOrEmpty(getDesDesGen())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESGEN_DESDESGEN);
		}
		if (getPorDes()==null || getPorDes().doubleValue()<=0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESGEN_PORDES);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique

		// Otras Validaciones
	
		
		return !hasError();
	}
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, Recibo.class, "desGen") ||
				GenericDAO.hasReference(this, ReciboConvenio.class, "desGen")
				|| GenericDAO.hasReference(this, Recibo.class, "desGen")
				) {
			//|| GenericDAO.hasReference(this, RecConCuo.class, "desGen")
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				GdeError.DESGEN_LABEL , GdeError.RECIBO_LABEL);
		}
		
		if (GenericDAO.hasReference(this, SerBanDesGen.class, "desGen")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				GdeError.DESGEN_LABEL , GdeError.SERBANDESGEN_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	
	/**
	 * Valida la activacion del Descuento General
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Descuento General
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}	
	// Metodos de negocio
	
	/**
	 * Activa el Descuento General. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getDesGenDAO().update(this);
	}

	/**
	 * Desactiva el Descuento General. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getDesGenDAO().update(this);
	}

	/**
	 * Obtiene el Descuento General vigente para un Recurso. Sólo puede haber 1
	 * @param recurso
	 * @return
	 */
	public static List<DesGen> getVigente(Recurso recurso) {		
		return GdeDAOFactory.getDesGenDAO().getVigente(recurso, new Date());
	}

	/**
	 * Obtiene el Descuento General vigente a una fecha para un Recurso. Sólo puede haber 1
	 * @param recurso
	 * @return
	 */
	public static List<DesGen> getVigente(Recurso recurso, Date fecha) {
		return GdeDAOFactory.getDesGenDAO().getVigente(recurso, fecha);
	}

	@Override
	public String infoString() {
		String ret = " Descuento General";
		
		if(desDesGen!=null){
			ret +=" - Descripcion: "+desDesGen;
		}
		
		if (porDes != null){
			ret +=" - Porcentaje Descuento: "+ porDes + " %";
		}
			
	    if (leyendaDesGen != null){
	    	ret +=" - Leyenda: "+ leyendaDesGen;
	    }
		
		if(idCaso!=null){
			ret +=" - Caso: "+idCaso;
		}
		
		return ret;
	}
}
