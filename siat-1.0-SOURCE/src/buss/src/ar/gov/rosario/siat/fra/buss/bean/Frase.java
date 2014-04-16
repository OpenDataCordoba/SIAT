//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.buss.bean;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.fra.buss.dao.FraDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Frase
 * 
 * @author tecso
 */
@Entity
@Table(name = "fra_frase")

public class Frase extends BaseBO {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desFrase")
	private String desFrase;

	
	@Column(name = "modulo")
	private String modulo;
	
	@Column(name = "pagina")
	private String pagina;
	
	@Column(name = "etiqueta")
	private String etiqueta;
	
	@Column(name = "valorPublico")
	private String valorPublico;
	
	@Column(name = "valorPrivado")
	private String valorPrivado;
	
	
	
	
	// Constructores
	public Frase(){
		super();
		
	}
	
	public Frase(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Frase getById(Long id) {
		return (Frase) FraDAOFactory.getFraseDAO().getById(id);
	}
	
	public static Frase getByIdNull(Long id) {
		return (Frase) FraDAOFactory.getFraseDAO().getByIdNull(id);
	}
	
	public static Frase getByKey(String modulo, String pagina, String etiqueta) {
		return (Frase) FraDAOFactory.getFraseDAO().getByKey(modulo, pagina, etiqueta);
	}
	
	/**
	 * 
	 * @param key - Formato: modulo.pagina.etiqueta
	 * @return null si no la encuentra
	 */
	public static Frase getByKey(String key) {		
		String[] keySplit = key.split("\\.");
		String modulo = keySplit[0];
		String pagina = keySplit[1];
		String etiqueta = keySplit[2];

		return getByKey(modulo, pagina, etiqueta);
	}
	
	public static List<Frase> getList() {
		return (List<Frase>) FraDAOFactory.getFraseDAO().getList();
	}
	
	public static List<Frase> getListActivos() {			
		return (List<Frase>) FraDAOFactory.getFraseDAO().getListActiva();
	}
	
	
	// Getters y setters

	public String getDesFrase() {
		return desFrase;
	}

	public void setDesFrase(String desFrase) {
		this.desFrase = desFrase;
	}
	
		
	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	
    public String getPagina() {
		return pagina;
	}

	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public String getValorPublico() {
		return valorPublico;
	}

	public void setValorPublico(String valorPublico) {
		this.valorPublico = valorPublico;
	}

	public String getValorPrivado() {
		return valorPrivado;
	}

	public void setValorPrivado(String valorPrivado) {
		this.valorPrivado = valorPrivado;
	}

	@Transient
	public String getDescReport(){
		return this.getModulo()+" - "+this.getPagina()+" - "+this.getEtiqueta();
	}
	
	/**
	 * devuelve modulo.pagina.etiqueta
	 * @return
	 */
   public String getKey(){
	   return modulo+"."+pagina+"."+etiqueta;
   }
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) 
			return false;
		
		
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							FraError.Frase_LABEL, FraError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		if (StringUtil.isNullOrEmpty(getDesFrase())){
			//addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, FraError.Frase_DESFrase);
		}
		
		if (hasError()) {
			return false;
		}
				
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Frase. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		FraDAOFactory.getFraseDAO().update(this);
	}

	/**
	 * Desactiva el Frase. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		FraDAOFactory.getFraseDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Frase
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Frase
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}


	
	
}
