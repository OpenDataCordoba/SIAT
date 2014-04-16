//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.service;

import ar.gov.rosario.siat.frm.iface.model.ForCamAdapter;
import ar.gov.rosario.siat.frm.iface.model.ForCamVO;
import ar.gov.rosario.siat.frm.iface.model.FormularioAdapter;
import ar.gov.rosario.siat.frm.iface.model.FormularioSearchPage;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IFrmFormularioService {
	
	// ---> ABM Formulario
	public FormularioSearchPage getFormularioSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public FormularioSearchPage getFormularioSearchPageResult(UserContext usercontext, FormularioSearchPage formularioSearchPage) throws DemodaServiceException;

	public FormularioAdapter getFormularioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FormularioAdapter getFormularioAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public FormularioAdapter getFormularioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public FormularioVO createFormulario(UserContext userContext, FormularioVO formularioVO ) throws DemodaServiceException;
	public FormularioVO updateFormulario(UserContext userContext, FormularioVO formularioVO ) throws DemodaServiceException;
	public FormularioVO deleteFormulario(UserContext userContext, FormularioVO formularioVO ) throws DemodaServiceException;
	public FormularioVO activarFormulario(UserContext userContext, FormularioVO formularioVO ) throws DemodaServiceException;
	public FormularioVO desactivarFormulario(UserContext userContext, FormularioVO formularioVO ) throws DemodaServiceException;	
	// <--- ABM Formulario

	// ---> ABM ForCam
	public ForCamAdapter getForCamAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ForCamAdapter getForCamAdapterForCreate(UserContext userContext,  CommonKey commonKeyIdForm) throws DemodaServiceException;
	public ForCamAdapter getForCamAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public ForCamVO createForCam(UserContext userContext, ForCamVO forCamVO ) throws DemodaServiceException;
	public ForCamVO updateForCam(UserContext userContext, ForCamVO forCamVO ) throws DemodaServiceException;
	public ForCamVO deleteForCam(UserContext userContext, ForCamVO forCamVO ) throws DemodaServiceException;
//	public ForCamVO activarForCam(UserContext userContext, ForCamVO forCamVO ) throws DemodaServiceException;
//	public ForCamVO desactivarForCam(UserContext userContext, ForCamVO forCamVO ) throws DemodaServiceException;	
	// <--- ABM ForCam
}
