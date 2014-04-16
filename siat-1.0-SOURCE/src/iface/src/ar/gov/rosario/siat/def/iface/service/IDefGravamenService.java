//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.service;

import java.util.List;

import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.CalendarioAdapter;
import ar.gov.rosario.siat.def.iface.model.CalendarioSearchPage;
import ar.gov.rosario.siat.def.iface.model.CalendarioVO;
import ar.gov.rosario.siat.def.iface.model.CategoriaAdapter;
import ar.gov.rosario.siat.def.iface.model.CategoriaSearchPage;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.model.FeriadoAdapter;
import ar.gov.rosario.siat.def.iface.model.FeriadoSearchPage;
import ar.gov.rosario.siat.def.iface.model.FeriadoVO;
import ar.gov.rosario.siat.def.iface.model.RecAliAdapter;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueAdapter;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueEmiVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrValAdapter;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuAdapter;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecConADecAdapter;
import ar.gov.rosario.siat.def.iface.model.RecConAdapter;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.RecEmiVO;
import ar.gov.rosario.siat.def.iface.model.RecGenCueAtrVaAdapter;
import ar.gov.rosario.siat.def.iface.model.RecMinDecAdapter;
import ar.gov.rosario.siat.def.iface.model.RecMinDecVO;
import ar.gov.rosario.siat.def.iface.model.RecursoAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoSearchPage;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ValUnRecConADeAdapter;
import ar.gov.rosario.siat.def.iface.model.VencimientoAdapter;
import ar.gov.rosario.siat.def.iface.model.VencimientoSearchPage;
import ar.gov.rosario.siat.def.iface.model.VencimientoVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IDefGravamenService {
	
	// ---> ABM Feriado
	public FeriadoSearchPage getFeriadoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public FeriadoSearchPage getFeriadoSearchPageResult(UserContext userContext, FeriadoSearchPage feriadoSearchPage) throws DemodaServiceException;
	public FeriadoAdapter getFeriadoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public FeriadoAdapter getFeriadoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public FeriadoVO createFeriado(UserContext userContext, FeriadoVO feriadoVO) throws DemodaServiceException;
	public FeriadoVO activarFeriado(UserContext userContext, FeriadoVO feriadoVO ) throws DemodaServiceException;
	public FeriadoVO desactivarFeriado(UserContext userContext, FeriadoVO feriadoVO ) throws DemodaServiceException;
	// <--- ABM Feriado

	// ---> ABM Vencimiento
	public VencimientoSearchPage getVencimientoSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public VencimientoSearchPage getVencimientoSearchPageResult(UserContext userContext, VencimientoSearchPage vencimientoSearchPage) throws DemodaServiceException;
	
	public VencimientoAdapter getVencimientoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public VencimientoAdapter getVencimientoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public VencimientoAdapter getVencimientoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
		
	public VencimientoVO createVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException;
	public VencimientoVO updateVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException;
	public VencimientoVO deleteVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException;
	public VencimientoVO activarVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException;
	public VencimientoVO desactivarVencimiento(UserContext userContext, VencimientoVO vencimientoVO ) throws DemodaServiceException;
	public VencimientoAdapter imprimirVencimiento(UserContext userContext, VencimientoAdapter vencimientoAdapterVO)throws DemodaServiceException;

	// <--- ABM Vencimiento
	
	// ---> ABM Categoria
	public CategoriaSearchPage getCategoriaSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public CategoriaSearchPage getCategoriaSearchPageResult(UserContext userContext, CategoriaSearchPage categoriaSearchPage) throws DemodaServiceException;

	public CategoriaAdapter getCategoriaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CategoriaAdapter getCategoriaAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public CategoriaAdapter getCategoriaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public CategoriaVO createCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException;
	public CategoriaVO updateCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException;
	public CategoriaVO deleteCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException;
	public CategoriaVO activarCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException;
	public CategoriaVO desactivarCategoria(UserContext userContext, CategoriaVO categoriaVO ) throws DemodaServiceException;
	// <--- ABM Categoria
	
	// ---> ABM Recurso
	public RecursoSearchPage getRecursoSearchPageInit(UserContext userContext, boolean esNoTrib) throws DemodaServiceException;
	public RecursoSearchPage getRecursoSearchPageResult(UserContext userContext, RecursoSearchPage recursoSearchPage) throws DemodaServiceException;
	public RecursoSearchPage getRecursoSearchPageParamTipoCategoria(UserContext userContext, RecursoSearchPage recursoSearchPage) throws DemodaServiceException; 
	
	public RecursoAdapter getRecursoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecursoAdapter getRecursoAdapterForCreate(UserContext userContext, boolean esNoTrib) throws DemodaServiceException;	
	public RecursoAdapter getRecursoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecursoAdapter getRecursoAdapterParamTipObjImp(UserContext userContext, RecursoAdapter recursoAdapter) throws DemodaServiceException;
	public RecursoAdapter getRecursoAdapterParamEsPrincipal(UserContext userContext, RecursoAdapter recursoAdapter) throws DemodaServiceException;
	public RecursoAdapter getRecursoAdapterParamEnviaJudicial(UserContext userContext, RecursoAdapter recursoAdapter) throws DemodaServiceException;
	public RecursoAdapter getRecursoAdapterParamPerEmiDeuMas(UserContext userContext, RecursoAdapter recursoAdapter) throws DemodaServiceException;
	public RecursoAdapter getRecursoAdapterParamPerImpMasDeu(UserContext userContext, RecursoAdapter recursoAdapterVO) throws DemodaServiceException;
	public RecursoAdapter getRecursoAdapterParamGenNotImpMas(UserContext userContext, RecursoAdapter recursoAdapterVO) throws DemodaServiceException;
	public RecursoAdapter getRecursoAdapterParamGenPadFir(UserContext userContext, RecursoAdapter recursoAdapterVO) throws DemodaServiceException;
	
	public RecursoVO createRecurso(UserContext userContext, RecursoAdapter recursoAdapterVO) throws DemodaServiceException;
	public RecursoVO updateRecurso(UserContext userContext, RecursoVO recursoVO) throws DemodaServiceException;
	public RecursoVO deleteRecurso(UserContext userContext, RecursoVO recursoVO) throws DemodaServiceException;
	public RecursoVO activarRecurso(UserContext userContext, RecursoVO recursoVO) throws DemodaServiceException;
	public RecursoVO desactivarRecurso(UserContext userContext, RecursoVO recursoVO) throws DemodaServiceException;
	// <--- ABM Recurso
	
	// ---> ABM RecAtrVal
	public RecAtrValAdapter getRecAtrValAdapterForView(UserContext userContext, CommonKey recursoCommonKey, CommonKey atributoCommonKey) throws DemodaServiceException;
	public RecAtrValAdapter getRecAtrValAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public List<AtributoVO> getListAtributoRecAtrVal(UserContext userContext, Long idRecurso) throws DemodaServiceException;
	public RecAtrValAdapter paramAtributoRecAtrVal(UserContext userContext, RecAtrValAdapter recAtrValAdapter, Long selectedId) throws DemodaServiceException;
	
	public RecAtrValAdapter createRecAtrVal(UserContext userContext, RecAtrValAdapter recAtrValAdapterVO) throws DemodaServiceException;
	public RecAtrValAdapter updateRecAtrVal(UserContext userContext, RecAtrValAdapter recAtrValAdapterVO) throws DemodaServiceException;
	public RecAtrValAdapter deleteRecAtrVal(UserContext userContext, RecAtrValAdapter recAtrValAdapterVO) throws DemodaServiceException;
	// <--- ABM RecAtrVal
	
	// ---> ABM RecCon
	public RecConAdapter getRecConAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecConAdapter getRecConAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public RecConAdapter getRecConAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public RecConVO createRecCon(UserContext userContext, RecConVO recConVO) throws DemodaServiceException;
	public RecConVO updateRecCon(UserContext userContext, RecConVO recConVO) throws DemodaServiceException;
	public RecConVO deleteRecCon(UserContext userContext, RecConVO recConVO) throws DemodaServiceException;
	// <--- ABM RecCon
	
	// ---> ABM RecClaDeu
	public RecClaDeuAdapter getRecClaDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecClaDeuAdapter getRecClaDeuAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public RecClaDeuAdapter getRecClaDeuAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public RecClaDeuVO createRecClaDeu(UserContext userContext, RecClaDeuVO recClaDeuVO) throws DemodaServiceException;
	public RecClaDeuVO updateRecClaDeu(UserContext userContext, RecClaDeuVO recClaDeuVO) throws DemodaServiceException;
	public RecClaDeuVO deleteRecClaDeu(UserContext userContext, RecClaDeuVO recClaDeuVO) throws DemodaServiceException;
	// <--- ABM RecClaDeu

	// ---> ABM RecGenCueAtrVa
	public RecGenCueAtrVaAdapter getRecGenCueAtrVaAdapterForView(UserContext userContext, CommonKey recursoCommonKey, CommonKey atributoCommonKey) throws DemodaServiceException;
	public RecGenCueAtrVaAdapter getRecGenCueAtrVaAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public RecGenCueAtrVaAdapter getRecGenCueAtrVaAdapterParamAtributo(UserContext userContext, RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO) throws DemodaServiceException; 
	
	public RecGenCueAtrVaAdapter createRecGenCueAtrVa(UserContext userContext, RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO) throws DemodaServiceException;
	public RecGenCueAtrVaAdapter updateRecGenCueAtrVa(UserContext userContext, RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO) throws DemodaServiceException;
	public RecGenCueAtrVaAdapter deleteRecGenCueAtrVa(UserContext userContext, RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO) throws DemodaServiceException;
	// <--- ABM RecGenCueAtrVa

	// ---> ABM RecEmi
	public RecEmiAdapter getRecEmiAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecEmiAdapter getRecEmiAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public RecEmiAdapter getRecEmiAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public RecEmiVO createRecEmi(UserContext userContext, RecEmiVO recEmiVO) throws DemodaServiceException;
	public RecEmiVO updateRecEmi(UserContext userContext, RecEmiVO recEmiVO) throws DemodaServiceException;
	public RecEmiVO deleteRecEmi(UserContext userContext, RecEmiVO recEmiVO) throws DemodaServiceException;
	// <--- ABM RecEmi

	// ---> ABM RecAtrCueEmi
	public RecAtrCueEmiAdapter getRecAtrCueEmiAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecAtrCueEmiAdapter getRecAtrCueEmiAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public RecAtrCueEmiAdapter getRecAtrCueEmiAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public List<AtributoVO> getListAtributoRecAtrCueEmi(UserContext userContext, Long idRecurso) throws DemodaServiceException;
	public RecAtrCueEmiAdapter paramAtributo(UserContext userContext, RecAtrCueEmiAdapter recAtrCueEmiAdapter) throws DemodaServiceException;
	
	public RecAtrCueEmiVO createRecAtrCueEmi(UserContext userContext, RecAtrCueEmiVO recAtrCueEmiVO) throws DemodaServiceException;
	public RecAtrCueEmiVO updateRecAtrCueEmi(UserContext userContext, RecAtrCueEmiVO recAtrCueEmiVO) throws DemodaServiceException;
	public RecAtrCueEmiVO deleteRecAtrCueEmi(UserContext userContext, RecAtrCueEmiVO recAtrCueEmiVO) throws DemodaServiceException;
	// <--- ABM RecAtrCueEmi
	
	// ---> ABM RecAtrCue
	public RecAtrCueAdapter getRecAtrCueAdapterForView(UserContext userContext, CommonKey recursoCommonKey, CommonKey atributoCommonKey) throws DemodaServiceException;
	public RecAtrCueAdapter getRecAtrCueAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public List<AtributoVO> getListAtributoRecAtrCue(UserContext userContext, Long idRecurso) throws DemodaServiceException;
	public RecAtrCueAdapter paramAtributoRecAtrCue(UserContext userContext, RecAtrCueAdapter recAtrCueAdapter, Long selectedId) throws DemodaServiceException;
	
	public RecAtrCueAdapter createRecAtrCue(UserContext userContext, RecAtrCueAdapter recAtrCueAdapterVO) throws DemodaServiceException;
	public RecAtrCueAdapter updateRecAtrCue(UserContext userContext, RecAtrCueAdapter recAtrCueAdapterVO) throws DemodaServiceException;
	public RecAtrCueAdapter deleteRecAtrCue(UserContext userContext, RecAtrCueAdapter recAtrCueAdapterVO) throws DemodaServiceException;
	// <--- ABM RecAtrCue
	
	//	 ---> ABM Calendario
	public CalendarioSearchPage getCalendarioSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public CalendarioSearchPage getCalendarioSearchPageResult(UserContext userContext, CalendarioSearchPage searchPage) throws DemodaServiceException;
	
	public CalendarioAdapter getCalendarioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CalendarioAdapter getCalendarioAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public CalendarioAdapter getCalendarioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	
	public CalendarioVO createCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException;
	public CalendarioVO updateCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException;	
	public CalendarioVO deleteCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException;
	public CalendarioVO activarCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException;
	public CalendarioVO desactivarCalendario(UserContext userContext, CalendarioVO calendarioVO) throws DemodaServiceException;
	//	 <--- ABM Calendario
	
	// ---> ABM RecConADec
	public RecConADecAdapter getRecConADecAdapterForView(UserContext userContext, CommonKey recConADecKey) throws DemodaServiceException;
	public RecConADecAdapter getRecConADecAdapterForCreate(UserContext userContext, CommonKey recursoKey) throws DemodaServiceException;
	public RecConADecAdapter createRecConADec(UserContext userContext, RecConADecAdapter recConADecAdapter) throws DemodaServiceException;
	public RecConADecAdapter updateRecConADec(UserContext userContext, RecConADecAdapter recConADecAdapter) throws DemodaServiceException;
	public RecConADecAdapter deleteRecConADec(UserContext userContext, RecConADecAdapter recConADecAdapter) throws DemodaServiceException;
	public RecConADecAdapter getRecConADecAdapterParamTipRecConADec(UserContext userContext, RecConADecAdapter recConADecAdapter) throws DemodaServiceException;
	
	public ValUnRecConADeAdapter getValUnRecConADeAdapterForCreate(UserContext userContext, CommonKey recConADecCommonKey)throws DemodaServiceException;
	public ValUnRecConADeAdapter createValUnRecConADe(UserContext userContext, ValUnRecConADeAdapter valUnRecConADeAdapter) throws DemodaServiceException;
	public ValUnRecConADeAdapter getValUnRecConADeAdapterForUpdate(UserContext userContext, CommonKey valUnRecConADecCommonKey)throws DemodaServiceException;
	public ValUnRecConADeAdapter updateValUnRecConADe(UserContext userContext, ValUnRecConADeAdapter valUnRecConADeAdapter) throws DemodaServiceException;
	public ValUnRecConADeAdapter getValUnRecConADeAdapterForView(UserContext userContext, CommonKey valUnRecConADecCommonKey)throws DemodaServiceException;
	public ValUnRecConADeAdapter deleteValUnRecConADe(UserContext userContext, ValUnRecConADeAdapter valUnRecConADeAdapter) throws DemodaServiceException;
	
	// ---> ABM RecMinDec
	public RecMinDecAdapter getRecMinDecAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecMinDecAdapter getRecMinDecAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public RecMinDecAdapter getRecMinDecAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public RecMinDecVO createRecMinDec(UserContext userContext, RecMinDecVO recMinDecVO) throws DemodaServiceException;
	public RecMinDecVO updateRecMinDec(UserContext userContext, RecMinDecVO recMinDecVO) throws DemodaServiceException;
	public RecMinDecVO deleteRecMinDec(UserContext userContext, RecMinDecVO recMinDecVO) throws DemodaServiceException;
	// <--- ABM RecMinDec

	// ---> ABM RecAli
	public RecAliAdapter getRecAliAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public RecAliAdapter getRecAliAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public RecAliAdapter getRecAliAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public RecAliVO createRecAli(UserContext userContext, RecAliVO recAliVO) throws DemodaServiceException;
	public RecAliVO updateRecAli(UserContext userContext, RecAliVO recAliVO) throws DemodaServiceException;
	public RecAliVO deleteRecAli(UserContext userContext, RecAliVO recAliVO) throws DemodaServiceException;
	
	public RecAliAdapter getRecAliAdapterParamTipoAlicuota(UserContext userContext, RecAliAdapter recAliAdapter) throws DemodaServiceException;
	// <--- ABM RecAli
}
