package ar.gov.rosario.siat.${modulo}.buss.bean;

import java.util.List;

import javax.persistence.*;

import ar.gov.rosario.siat.${modulo}.iface.util.${Modulo}Error;
import ar.gov.rosario.siat.${modulo}.buss.dao.${Modulo}DAOFactory;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;

import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ${Bean} - ${Definicion_Bean}
 * 
 * @author tecso
 */
@Entity
@Table(name = "${tabla}")
public class ${Bean} extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	//<#Propiedades#>
	<#Propiedades.Codigo#>
	@Column(name = "cod${Bean}")
	private String cod${Bean};
	
	<#Propiedades.Codigo#>
	<#Propiedades.Descripcion#>
	@Column(name = "des${Bean}")
	private String des${Bean};
	
	<#Propiedades.Descripcion#>
	<#Propiedades.Integer#>
	@Column(name = "${propiedad}")
	private Integer ${propiedad};
	
	<#Propiedades.Integer#>
	<#Propiedades.Date#>
	@Column(name = "${propiedad}")
	private Date ${propiedad};
	
	<#Propiedades.Date#>
	<#Propiedades.Double#>
	@Column(name = "${propiedad}")
	private Double ${propiedad};
	
	<#Propiedades.Double#>
	<#Propiedades.Long#>
	@Column(name = "${propiedad}")
	private Long ${propiedad};
	
	<#Propiedades.Long#>
	<#Propiedades.String#>
	@Column(name = "${propiedad}")
	private String ${propiedad};
	
	<#Propiedades.String#>
	<#Propiedades.ManyToOne#>
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="${campo_relacional}") 
	private ${ClaseComponete} ${propiedad_componene};
	
	<#Propiedades.ManyToOne#>
	<#Propiedades.OneToMany#>
	@OneToMany()
	@JoinColumn(name="${idClaseComponete}")
	private List<${ClaseComponete}> list${ClaseComponete};
	
	<#Propiedades.OneToMany#>
	//<#Propiedades#>
	
	// Constructores
	public ${Bean}(){
		super();
		// Seteo de valores default			
	}
	
	public ${Bean}(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ${Bean} getById(Long id) {
		return (${Bean}) ${Modulo}DAOFactory.get${Bean}DAO().getById(id);
	}
	
	public static ${Bean} getByIdNull(Long id) {
		return (${Bean}) ${Modulo}DAOFactory.get${Bean}DAO().getByIdNull(id);
	}
	
	public static List<${Bean}> getList() {
		return (ArrayList<${Bean}>) ${Modulo}DAOFactory.get${Bean}DAO().getList();
	}
	
	public static List<${Bean}> getListActivos() {			
		return (ArrayList<${Bean}>) ${Modulo}DAOFactory.get${Bean}DAO().getListActiva();
	}
	
	
	// Getters y setters
	
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
		<#ValidateDelete.Bean#>
			if (GenericDAO.hasReference(this, ${BeanRelacionado}.class, "${bean}")) {
				addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
								${Modulo}Error.${BEAN}_LABEL, ${Modulo}Error.${BEAN_RELACIONADO}_LABEL );
			}
		<#ValidateDelete.Bean#>
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getCod${Bean}())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ${Modulo}Error.${BEAN}_COD${BEAN} );
		}
		
		if (StringUtil.isNullOrEmpty(getDes${Bean}())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ${Modulo}Error.${BEAN}_DES${BEAN});
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("cod${Bean}");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, ${Modulo}Error.${BEAN}_COD${BEAN});			
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ${Bean}. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		${Modulo}DAOFactory.get${Bean}DAO().update(this);
	}

	/**
	 * Desactiva el ${Bean}. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		${Modulo}DAOFactory.get${Bean}DAO().update(this);
	}
	
	/**
	 * Valida la activacion del ${Bean}
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ${Bean}
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
	<#MetodosBeanDetalle.CUD#>
	//	---> ABM ${BeanDetalle}
	public ${BeanDetalle} create${BeanDetalle}(${BeanDetalle} ${beanDetalle}) throws Exception {

		// Validaciones de negocio
		if (!${beanDetalle}.validateCreate()) {
			return ${beanDetalle};
		}

		${Modulo}DAOFactory.get${BeanDetalle}DAO().update(${beanDetalle});

		return ${beanDetalle};
	}
	
	public ${BeanDetalle} update${BeanDetalle}(${BeanDetalle} ${beanDetalle}) throws Exception {
		
		// Validaciones de negocio
		if (!${beanDetalle}.validateUpdate()) {
			return ${beanDetalle};
		}

		${Modulo}DAOFactory.get${BeanDetalle}DAO().update(${beanDetalle});
		
		return ${beanDetalle};
	}
	
	public ${BeanDetalle} delete${BeanDetalle}(${BeanDetalle} ${beanDetalle}) throws Exception {
	
		// Validaciones de negocio
		if (!${beanDetalle}.validateDelete()) {
			return ${beanDetalle};
		}
		
		${Modulo}DAOFactory.get${BeanDetalle}DAO().delete(${beanDetalle});
		
		return ${beanDetalle};
	}
	<#MetodosBeanDetalle.CUD#>
	//<#MetodosBeanDetalle#>
}
//<template>
	