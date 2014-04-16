package ar.gov.rosario.siat.${modulo}.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del ${Bean}
 * @author tecso
 *
 */
public class ${Bean}VO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "${bean}VO";
	
	//<#Propiedades#>
	<#Propiedades.Codigo#>
	private String cod${Bean};
	
	<#Propiedades.Codigo#>
	<#Propiedades.Descripcion#>
	private String des${Bean};
	
	<#Propiedades.Descripcion#>
	<#Propiedades.Integer#>
	private Integer ${propiedad};
	
	<#Propiedades.Integer#>
	<#Propiedades.Date#>
	private Date ${propiedad};
	
	<#Propiedades.Date#>
	<#Propiedades.Double#>
	private Double ${propiedad};
	
	<#Propiedades.Double#>
	<#Propiedades.Long#>
	private Long ${propiedad};
	
	<#Propiedades.Long#>
	<#Propiedades.String#>
	private String ${propiedad};
	
	<#Propiedades.String#>
	<#Propiedades.SiNo#>
	private SiNo ${propiedad} = SiNo.OpcionSelecionar;
	
	<#Propiedades.SiNo#>
	<#Propiedades.Composicion#>
	private ${ClaseComponete}VO ${claseComponete} = new ${ClaseComponete}VO();
	
	<#Propiedades.Composicion#>
	<#Propiedades.Lista#>
	private List<${ClaseComponete}VO> list${ClaseComponete} = new ArrayList<${ClaseComponete}O>();
	
	<#Propiedades.Lista#>
	//<#Propiedades#>
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ${Bean}VO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ${Bean}VO(int id, String desc) {
		super();
		setId(new Long(id));
		setDes${Bean}(desc);
	}
	
	// Getters y Setters
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
