package ar.gov.rosario.siat.${modulo}.iface.service;

import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}Adapter;
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}SearchPage;
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}VO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface I${Modulo}${Submodulo}Service {
	
	// ---> ABM ${Bean}
	public ${Bean}SearchPage get${Bean}SearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ${Bean}SearchPage get${Bean}SearchPageResult(UserContext usercontext, ${Bean}SearchPage ${bean}SearchPage) throws DemodaServiceException;

	public ${Bean}Adapter get${Bean}AdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ${Bean}Adapter get${Bean}AdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public ${Bean}Adapter get${Bean}AdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ${Bean}VO create${Bean}(UserContext userContext, ${Bean}VO ${bean}VO ) throws DemodaServiceException;
	public ${Bean}VO update${Bean}(UserContext userContext, ${Bean}VO ${bean}VO ) throws DemodaServiceException;
	public ${Bean}VO delete${Bean}(UserContext userContext, ${Bean}VO ${bean}VO ) throws DemodaServiceException;
	public ${Bean}VO activar${Bean}(UserContext userContext, ${Bean}VO ${bean}VO ) throws DemodaServiceException;
	public ${Bean}VO desactivar${Bean}(UserContext userContext, ${Bean}VO ${bean}VO ) throws DemodaServiceException;	
	public ${Bean}Adapter imprimir${Bean}(UserContext userContext, ${Bean}Adapter ${bean}AdapterVO ) throws DemodaServiceException;
	// <--- ABM ${Bean}
	
}