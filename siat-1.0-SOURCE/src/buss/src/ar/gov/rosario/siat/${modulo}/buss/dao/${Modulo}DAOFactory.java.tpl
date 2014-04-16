package ar.gov.rosario.siat.${modulo}.buss.dao;

/**
 * Factory de <Nombre Largo Modulo> DAOs
 * 
 * @author tecso
 * 
 */
public class ${Modulo}DAOFactory {

    private static final ${Modulo}DAOFactory INSTANCE = new ${Modulo}DAOFactory();
    
    //<#Variable#>
	<#Variable.Dao#>
    private ${Bean}DAO             ${bean}DAO;
	<#Variable.Dao#>
    //<#Variable#>
    
    private ${Modulo}DAOFactory() {
        super();  
        //<#Constructor#>
		<#Constructor.Dao#>
        this.${bean}DAO                = new ${Bean}DAO();
		<#Constructor.Dao#>
        //<#Constructor#>
    }

	//<#Get#>
	<#Get.Dao#>
    public static ${Bean}DAO get${Bean}DAO() {
        return INSTANCE.${bean}DAO;
    }
	<#Get.Dao#>
    //<#Get#>
}
//<template>