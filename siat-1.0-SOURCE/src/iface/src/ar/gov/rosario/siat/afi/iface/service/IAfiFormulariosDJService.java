//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.service;

import ar.gov.rosario.siat.afi.iface.model.ActLocAdapter;
import ar.gov.rosario.siat.afi.iface.model.DatosDomicilioAdapter;
import ar.gov.rosario.siat.afi.iface.model.DatosPagoCtaAdapter;
import ar.gov.rosario.siat.afi.iface.model.DecActLocAdapter;
import ar.gov.rosario.siat.afi.iface.model.ExeActLocAdapter;
import ar.gov.rosario.siat.afi.iface.model.ForDecJurAdapter;
import ar.gov.rosario.siat.afi.iface.model.ForDecJurSearchPage;
import ar.gov.rosario.siat.afi.iface.model.ForDecJurVO;
import ar.gov.rosario.siat.afi.iface.model.HabLocAdapter;
import ar.gov.rosario.siat.afi.iface.model.LocalAdapter;
import ar.gov.rosario.siat.afi.iface.model.OtrosPagosAdapter;
import ar.gov.rosario.siat.afi.iface.model.RetYPerAdapter;
import ar.gov.rosario.siat.afi.iface.model.SocioAdapter;
import ar.gov.rosario.siat.afi.iface.model.TotDerYAccDJAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IAfiFormulariosDJService {
	
	// ---> ABM ForDecJur
	public ForDecJurSearchPage getForDecJurSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public ForDecJurSearchPage getForDecJurSearchPageResult(UserContext usercontext, ForDecJurSearchPage forDecJurSearchPage) throws DemodaServiceException;
	public ForDecJurAdapter getForDecJurAdapterForView(UserContext userContext, CommonKey commonKey, Long idDecJur) throws DemodaServiceException;
	public ForDecJurAdapter imprimirForDecJur(UserContext userContext, ForDecJurAdapter forDecJurAdapterVO ) throws DemodaServiceException;
	
	public ForDecJurVO generarDecJur(UserContext userContext, ForDecJurVO forDecJurVO) throws DemodaServiceException;
	// <--- ABM ForDecJur
		
	// ---> ABM RetYPer	
	public RetYPerAdapter getRetYPerAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM RetYPer
		
	// ---> ABM TotDerYAccDJ	
	public TotDerYAccDJAdapter getTotDerYAccDJAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM TotDerYAccDJ
		
	// ---> ABM Socio	
	public SocioAdapter getSocioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM Socio
	
	// ---> ABM DatosDomicilio	
	public DatosDomicilioAdapter getDatosDomicilioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM DatosDomicilio
	
	// ---> ABM Local	
	public LocalAdapter getLocalAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM Local
	
	// ---> ABM DatosPagoCta	
	public DatosPagoCtaAdapter getDatosPagoCtaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM DatosPagoCta
	
	// ---> ABM OtrosPagos	
	public OtrosPagosAdapter getOtrosPagosAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM OtrosPagos	
	
	// ---> ABM DecActLoc	
	public DecActLocAdapter getDecActLocAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM DecActLoc
	
	// ---> ABM ActLoc	
	public ActLocAdapter getActLocAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM ActLoc
	
	// ---> ABM HabLoc	
	public HabLocAdapter getHabLocAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM HabLoc
	
	// ---> ABM ExeActLoc	
	public ExeActLocAdapter getExeActLocAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	// <--- ABM ExeActLoc
	
		
	
}