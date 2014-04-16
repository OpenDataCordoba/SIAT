//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.service;

import java.util.List;

import ar.gov.rosario.siat.base.iface.model.ISiatModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.model.SistemaOrigenVO;
import ar.gov.rosario.siat.cas.iface.model.UsoExpedienteAdapter;
import ar.gov.rosario.siat.cas.iface.model.UsoExpedienteSearchPage;
import coop.tecso.demoda.buss.bean.BaseBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface ICasCasoService {
	
	// ---> Validacion de Casos
	public boolean validarCaso(UserContext usercontext, ISiatModel modelVO) throws DemodaServiceException;
	
	public List<SistemaOrigenVO> getListSistemaOrigenInit() throws DemodaServiceException;
	// <--- Fin Validacion de Casos
	
	// ---> Registrar log de uso de Casos	
	public void registrarUsoExpediente(SiatBussImageModel modelVO, BaseBean baseBean, 
			BaseBean accionExp, BaseBean cuenta, String descripion) throws DemodaServiceException;
	
	// <--- Fin Registrar log de uso de Casos

	
	// ---> Consultar Uso Expediente
	public UsoExpedienteSearchPage getUsoExpedienteSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public UsoExpedienteSearchPage getUsoExpedienteSearchPageResult(UserContext usercontext, UsoExpedienteSearchPage usoExpedienteSearchPage) throws DemodaServiceException;
	public UsoExpedienteAdapter imprimirConsultaExpediente(UserContext userContext, UsoExpedienteAdapter usoExpedienteAdapter) throws  DemodaServiceException;
	public UsoExpedienteAdapter getUsoExpedienteAdapterForView(UserContext userSession, CommonKey commonKey) throws DemodaServiceException;
	// <--- Consultar Uso Expediente
	
	// ---> Formateo de idCaso
	public CasoVO construirCasoVO(String idCaso);
	public String getIdFormateado(CasoVO caso);
	// <--- Formateo de idCaso
	
	
	
}