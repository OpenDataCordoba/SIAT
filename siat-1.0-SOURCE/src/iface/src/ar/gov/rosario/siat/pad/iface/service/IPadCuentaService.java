//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.service;


import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.CambiarDomEnvioAdapter;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelAdapter;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelDeuAdapter;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelDeuVO;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaRelAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaRelVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.EstCueAdapter;
import ar.gov.rosario.siat.pad.iface.model.EstCueSearchPage;
import ar.gov.rosario.siat.pad.iface.model.EstCueVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueVAdapter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IPadCuentaService {

	// ---> ABM CuentaTitular
	public CuentaTitularAdapter getCuentaTitularAdapterForView(UserContext userContext, CommonKey cuentaTitularKey) throws DemodaServiceException;
	public CuentaTitularAdapter getCuentaTitularAdapterForCreate(UserContext userContext, CommonKey cuentaKey, CommonKey personaKey) throws DemodaServiceException;
	public CuentaTitularAdapter getCuentaTitularAdapterForUpdate(UserContext userContext, CommonKey cuentaTitularKey) throws DemodaServiceException;
	public CuentaTitularVO createCuentaTitular(UserContext userContext, CuentaTitularVO cuentaTitularVO) throws DemodaServiceException;
	public CuentaTitularVO updateCuentaTitular(UserContext userContext, CuentaTitularVO cuentaTitularVO) throws DemodaServiceException;
	public CuentaTitularVO deleteCuentaTitular(UserContext userContext, CuentaTitularVO cuentaTitularVO) throws DemodaServiceException;
	public CuentaTitularVO marcarTitularPrincipal(UserContext userContext, CuentaTitularVO cuentaTitularVO) throws DemodaServiceException;
	
	// <--- ABM CuentaTitular
	
	// ---> ABM Cuenta	
	public CuentaSearchPage getCuentaSearchPageInit(UserContext userContext, CuentaSearchPage cuentaSearchPageFiltro) throws DemodaServiceException;
	public CuentaSearchPage getCuentaSearchPageParamTitular(UserContext userContext, CuentaSearchPage cuentaSearchPage) throws DemodaServiceException;
	public CuentaSearchPage getCuentaSearchPageResult(UserContext userContext, CuentaSearchPage cuentaSearchPage) throws DemodaServiceException;

	//TODO por ahora utilizamos este  metodo para ver, editar, borrar, etc. talvez sea necesario abrirlo en varios metodos.
	public CuentaAdapter getCuentaAdapterForView(UserContext userContext, CommonKey idCuenta) throws DemodaServiceException;

	public CuentaAdapter getCuentaAdapterForCreate(UserContext userContext, RecursoVO recursoVO, PersonaVO persona, boolean esCMD) throws DemodaServiceException;
	
	public CuentaAdapter getCuentaAdapterParamObjImp(UserContext userContext, CuentaAdapter cuentaAdapterVO) throws DemodaServiceException;
	
	public CuentaAdapter getCuentaAdapterParamRecurso(UserContext userContext, CuentaAdapter cuentaAdapterVO) throws DemodaServiceException;
	
	public CuentaAdapter paramPersona(UserContext userContext, CuentaAdapter cuentaAdapter, Long selectedId) throws DemodaServiceException;
	
	public CuentaAdapter getCuentaAdapterForUpdateDomicilioEnvio(UserContext userContext, CommonKey idCuenta) throws DemodaServiceException;
	
	public CuentaVO createCuenta(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException;
	
	public CuentaVO updateCuenta(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException;
	
	public CuentaVO deleteCuenta(UserContext userContext, CuentaVO cuenta) throws DemodaServiceException;

	public CuentaVO activarCuenta(UserContext userContext, CuentaVO cuenta) throws DemodaServiceException;

	public CuentaVO desactivarCuenta(UserContext userContext, CuentaVO cuenta) throws DemodaServiceException;
	
	public DomicilioVO updateCuentaDomicilioEnvio(UserContext userContext, CuentaVO domicilioVO) throws DemodaServiceException;
	
	public BroCueVO paramAsignarBroche(UserContext userContext, CuentaVO cuentaVO, CommonKey idBroche) throws DemodaServiceException;
	public BroCueVO paramQuitarBroche(UserContext userContext, CuentaVO cuentaVO) throws DemodaServiceException;
	
	public BroCueVO asignarBroche(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException;
	public BroCueVO modificarBroche(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException;
	
	public TipObjImpVO obtenerTipObjImpFromRecurso(Long idRecurso) throws DemodaServiceException;
	
	public RecAtrCueVAdapter getRecAtrCueVAdapterForView(UserContext userContext, CommonKey recAtrCueKey, CommonKey cuentaKey) throws DemodaServiceException;
	public RecAtrCueVAdapter updateRecAtrCueV(UserContext userContext, RecAtrCueVAdapter recAtrCueVAdapterVO) throws DemodaServiceException;
	// <--- ABM Cuenta	
	
	// ---> ABM EstCue
	public EstCueSearchPage getEstCueSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public EstCueSearchPage getEstCueSearchPageResult(UserContext usercontext, EstCueSearchPage estCueSearchPage) throws DemodaServiceException;

	public EstCueAdapter getEstCueAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public EstCueAdapter getEstCueAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public EstCueAdapter getEstCueAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public EstCueAdapter imprimirEstCue(UserContext userContext, EstCueAdapter estCueAdapterVO ) throws DemodaServiceException;
	public EstCueVO createEstCue(UserContext userContext, EstCueVO estCueVO ) throws DemodaServiceException;
	public EstCueVO updateEstCue(UserContext userContext, EstCueVO estCueVO ) throws DemodaServiceException;
	public EstCueVO deleteEstCue(UserContext userContext, EstCueVO estCueVO ) throws DemodaServiceException;
	// <--- ABM EstCue	
	
	// ---> ABM Cambiar domicilio de envio WEB
	public CambiarDomEnvioAdapter getCambiarDomEnvioAdapterInitTGI(UserContext userContext) throws DemodaServiceException;
	public CambiarDomEnvioAdapter getCambiarDomEnvioAdapterInit(UserContext userContext, CommonKey recursoKey) throws DemodaServiceException;
	public CambiarDomEnvioAdapter getCambiarDomEnvioIngresar(UserContext userContext, CambiarDomEnvioAdapter cambiarDomEnvioAdapter) throws DemodaServiceException;
	public CambiarDomEnvioAdapter getCambiarDomEnvioBuscarOtraLocalidad(UserContext userContext, CambiarDomEnvioAdapter cambiarDomEnvioAdapter) throws DemodaServiceException;

	public CambiarDomEnvioAdapter cambiarDomEnvioCargarNuevoDom(UserContext userContext, CambiarDomEnvioAdapter cambiarDomEnvioAdapter) throws DemodaServiceException;	
	public CambiarDomEnvioAdapter cambiarDomEnvioSeleccionarCalle(UserContext userContext, CambiarDomEnvioAdapter cambiarDomEnvioAdapter) throws DemodaServiceException;	
	
	public CambiarDomEnvioAdapter updateDomEnvio(UserContext userContext, CambiarDomEnvioAdapter cambiarDomEnvioAdapter) throws DemodaServiceException;
	//<--- ABM Cambiar domicilio de envio WEB
	
	// ---> ABM Cuenta Excluida Selleccionada
	public CueExcSelSearchPage getCueExcSelSearchPageInit(UserContext userContext) throws DemodaServiceException;
	public CueExcSelSearchPage getCueExcSelSearchPageResult(UserContext userContext, CueExcSelSearchPage cueExcSelSearchPage) throws DemodaServiceException;
	public CueExcSelSearchPage getCueExcSelSearchPageParamCuenta(UserContext userContext, CueExcSelSearchPage cueExcSelSearchPage) throws DemodaServiceException;
	
	public CueExcSelAdapter getCueExcSelAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CueExcSelAdapter getCueExcSelAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public CueExcSelAdapter getCueExcSelAdapterParamCuenta(UserContext userContext, CueExcSelAdapter cueExcSelAdapter) throws DemodaServiceException;
	
	public CueExcSelVO createCueExcSel(UserContext userContext, CueExcSelVO cueExcSelVO) throws DemodaServiceException;
	public CueExcSelVO activarCueExcSel(UserContext userContext, CueExcSelVO cueExcSelVO) throws DemodaServiceException;
	public CueExcSelVO desactivarCueExcSel(UserContext userContext, CueExcSelVO cueExcSelVO) throws DemodaServiceException;
	// <--- ABM Cuenta Excluida Selleccionadas
	
	//	 ---> ABM Deuda Cuenta Excluida Seleccionada
	public CueExcSelDeuAdapter getCueExcSelDeuAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CueExcSelDeuAdapter getCueExcSelDeuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;

	public CueExcSelDeuVO createCueExcSelDeu(UserContext userContext, CueExcSelDeuVO cueExcSelDeuVO) throws DemodaServiceException;
	public CueExcSelDeuAdapter createCueExcSelDeuList(UserContext userContext, CueExcSelDeuAdapter cueExcSelDeuAdapter) throws DemodaServiceException;
	public CueExcSelDeuVO activarCueExcSelDeu(UserContext userContext, CueExcSelDeuVO cueExcSelDeuVO) throws DemodaServiceException;
	public CueExcSelDeuVO desactivarCueExcSelDeu(UserContext userContext, CueExcSelDeuVO cueExcSelDeuVO) throws DemodaServiceException;
	//	 <--- ABM Deuda Cuenta Excluida Seleccionada
	
	//  ---> ABM Cuenta, Relacionar Cuentas
	public CuentaAdapter getCuentaAdapterForRelacionar(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException;

	public CuentaRelAdapter getCuentaRelAdapterForView(UserContext userContext, CommonKey cuentaRelKey) throws DemodaServiceException;
	public CuentaRelAdapter getCuentaRelAdapterForCreate(UserContext userContext, CommonKey cuentaKey) throws DemodaServiceException;
	public CuentaRelAdapter getCuentaRelAdapterParamCuenta(UserContext userContext, CuentaRelAdapter cuentaRelAdapter) throws DemodaServiceException ;

	public CuentaRelVO createCuentaRel(UserContext userContext, CuentaRelVO cuentaRelVO) throws DemodaServiceException;
	public CuentaRelVO updateCuentaRel(UserContext userContext, CuentaRelVO cuentaRelVO) throws DemodaServiceException;
	public CuentaRelVO deleteCuentaRel(UserContext userContext, CuentaRelVO cuentaRelVO) throws DemodaServiceException;
	// <--- ABM Cuenta, Relacionar Cuentas
	

}

