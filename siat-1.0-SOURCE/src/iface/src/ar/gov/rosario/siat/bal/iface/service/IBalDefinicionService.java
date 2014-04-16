//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.service;

import ar.gov.rosario.siat.bal.iface.model.AccionSelladoAdapter;
import ar.gov.rosario.siat.bal.iface.model.AccionSelladoVO;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoAdapter;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
import ar.gov.rosario.siat.bal.iface.model.EjercicioAdapter;
import ar.gov.rosario.siat.bal.iface.model.EjercicioSearchPage;
import ar.gov.rosario.siat.bal.iface.model.EjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.ImpSelAdapter;
import ar.gov.rosario.siat.bal.iface.model.ImpSelVO;
import ar.gov.rosario.siat.bal.iface.model.LeyParAcuAdapter;
import ar.gov.rosario.siat.bal.iface.model.LeyParAcuSearchPage;
import ar.gov.rosario.siat.bal.iface.model.LeyParAcuVO;
import ar.gov.rosario.siat.bal.iface.model.ParCueBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.ParCueBanVO;
import ar.gov.rosario.siat.bal.iface.model.ParSelAdapter;
import ar.gov.rosario.siat.bal.iface.model.ParSelVO;
import ar.gov.rosario.siat.bal.iface.model.PartidaAdapter;
import ar.gov.rosario.siat.bal.iface.model.PartidaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.bal.iface.model.SelladoAdapter;
import ar.gov.rosario.siat.bal.iface.model.SelladoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SelladoVO;
import ar.gov.rosario.siat.bal.iface.model.SistemaAdapter;
import ar.gov.rosario.siat.bal.iface.model.SistemaSearchPage;
import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanVO;
import ar.gov.rosario.siat.bal.iface.model.TipoIndetAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoIndetSearchPage;
import ar.gov.rosario.siat.bal.iface.model.TipoIndetVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public interface IBalDefinicionService {
	
	// ---> ABM Sistema
	public SistemaSearchPage getSistemaSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public SistemaSearchPage getSistemaSearchPageResult(UserContext usercontext, SistemaSearchPage sistemaSearchPage) throws DemodaServiceException;
	public SistemaAdapter getSistemaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SistemaAdapter getSistemaAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public SistemaVO createSistema(UserContext userContext, SistemaVO sistemaVO ) throws DemodaServiceException;
	public SistemaAdapter getSistemaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SistemaVO updateSistema(UserContext userContext, SistemaVO sistemaVO ) throws DemodaServiceException;
	public SistemaVO deleteSistema(UserContext userContext, SistemaVO sistemaVO ) throws DemodaServiceException;
	public SistemaVO activarSistema(UserContext userContext, SistemaVO sistemaVO ) throws DemodaServiceException;
	public SistemaVO desactivarSistema(UserContext userContext, SistemaVO sistemaVO ) throws DemodaServiceException;	
	// <--- ABM Sistema
	
	// ---> ABM Sellado
	public SelladoSearchPage getSelladoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public SelladoSearchPage getSelladoSearchPageResult(UserContext usercontext, SelladoSearchPage SelladoSearchPage) throws DemodaServiceException;
	public SelladoAdapter getSelladoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SelladoAdapter getSelladoAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public SelladoVO createSellado(UserContext userContext, SelladoVO selladoVO ) throws DemodaServiceException;
	public SelladoAdapter getSelladoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public SelladoVO updateSellado(UserContext userContext, SelladoVO selladoVO ) throws DemodaServiceException;
	public SelladoVO deleteSellado(UserContext userContext, SelladoVO selladoVO ) throws DemodaServiceException;
	public SelladoVO activarSellado(UserContext userContext, SelladoVO selladoVO ) throws DemodaServiceException;
	public SelladoVO desactivarSellado(UserContext userContext, SelladoVO selladoVO ) throws DemodaServiceException;
	
	// <--- ABM Sellado
	
	// ---> ABM ImpSel
	public ImpSelAdapter getImpSelAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ImpSelAdapter getImpSelAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ImpSelVO createImpSel(UserContext userContext, ImpSelVO impSelVO ) throws DemodaServiceException;
	public ImpSelAdapter getImpSelAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ImpSelVO updateImpSel(UserContext userContext, ImpSelVO impSelVO ) throws DemodaServiceException;
	public ImpSelVO deleteImpSel(UserContext userContext, ImpSelVO impSelVO ) throws DemodaServiceException;
	// <--- ABM ImpSel
	
	// ---> ABM AccionSellado
	public AccionSelladoAdapter getAccionSelladoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AccionSelladoAdapter getAccionSelladoAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AccionSelladoAdapter getAccionSelladoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AccionSelladoAdapter getAccionSelladoAdapterParamEsEspecial (UserContext userContext, AccionSelladoAdapter accionSelladoAdapter) throws DemodaServiceException;
	public AccionSelladoVO createAccionSellado(UserContext userContext, AccionSelladoVO accionSelladoVO) throws DemodaServiceException;
	public AccionSelladoVO updateAccionSellado(UserContext userContext, AccionSelladoVO accionSelladoVO) throws DemodaServiceException;
	public AccionSelladoVO deleteAccionSellado(UserContext userContext, AccionSelladoVO accionSelladoVO) throws DemodaServiceException;
	
	// <--- ABM AccionSellado
	
	// ---> ABM ParSel
	public ParSelAdapter getParSelAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ParSelAdapter getParSelAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ParSelAdapter getParSelAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ParSelVO createParSel(UserContext userContext, ParSelVO parSelVO) throws DemodaServiceException;
	public ParSelVO updateParSel(UserContext userContext, ParSelVO parSelVO) throws DemodaServiceException;
	public ParSelVO deleteParSel(UserContext userContext, ParSelVO parSelVO) throws DemodaServiceException;
	// <--- ABM ParSel
	
	// ---> ABM Ejercicio
	public EjercicioSearchPage getEjercicioSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public EjercicioSearchPage getEjercicioSearchPageResult(UserContext userContext, EjercicioSearchPage ejercicioSearchPage) throws DemodaServiceException;
	public EjercicioAdapter getEjercicioAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public EjercicioAdapter getEjercicioAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public EjercicioVO createEjercicio(UserContext userContext, EjercicioVO ejercicioVO) throws DemodaServiceException;
	public EjercicioAdapter getEjercicioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public EjercicioVO updateEjercicio(UserContext userContext, EjercicioVO ejercicioVO) throws DemodaServiceException;
	public EjercicioVO deleteEjercicio(UserContext userContext, EjercicioVO ejercicioVO) throws DemodaServiceException;
	public EjercicioVO activarEjercicio(UserContext userContext, EjercicioVO ejercicioVO ) throws DemodaServiceException;
	public EjercicioVO desactivarEjercicio(UserContext userContext, EjercicioVO ejercicioVO ) throws DemodaServiceException;	
	// <--- ABM Ejercicio
	
	// ---> ABM Partida
	public PartidaSearchPage getPartidaSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public PartidaSearchPage getPartidaSearchPageResult(UserContext userContext, PartidaSearchPage partidaSearchPage) throws DemodaServiceException;
    public PartidaAdapter getPartidaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public PartidaAdapter getPartidaAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public PartidaVO createPartida(UserContext userContext, PartidaVO partidaVO) throws DemodaServiceException;
	public PartidaAdapter getPartidaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PartidaVO updatePartida(UserContext userContext, PartidaVO partidaVO) throws DemodaServiceException;
	public PartidaVO deletePartida(UserContext userContext, PartidaVO partidaVO) throws DemodaServiceException;
	public PartidaVO activarPartida(UserContext userContext, PartidaVO partidaVO ) throws DemodaServiceException;
	public PartidaVO desactivarPartida(UserContext userContext, PartidaVO partidaVO ) throws DemodaServiceException;
	public PartidaAdapter imprimirPartida(UserContext userContext, PartidaAdapter partidaAdapterVO ) throws DemodaServiceException;
	// <--- ABM Partida	
	
	// ---> ABM Cuenta Banco
	public CuentaBancoSearchPage getCuentaBancoSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public CuentaBancoSearchPage getCuentaBancoSearchPageResult(UserContext userContext, CuentaBancoSearchPage cuentaBancoSearchPage) throws DemodaServiceException;
    public CuentaBancoAdapter getCuentaBancoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public CuentaBancoAdapter getCuentaBancoAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public CuentaBancoVO createCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO) throws DemodaServiceException;
	public CuentaBancoAdapter getCuentaBancoAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CuentaBancoVO updateCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO) throws DemodaServiceException;
	public CuentaBancoVO deleteCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO) throws DemodaServiceException;
	public CuentaBancoVO activarCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO ) throws DemodaServiceException;
	public CuentaBancoVO desactivarCuentaBanco(UserContext userContext, CuentaBancoVO cuentaBancoVO ) throws DemodaServiceException;
	public CuentaBancoAdapter imprimirCuentaBanco(UserContext userContext, CuentaBancoAdapter cuentaBancoAdapterVO ) throws DemodaServiceException;
	// <--- ABM Cuenta Banco	
	
	// ---> ABM Tipo Cuenta Banco
	public TipCueBanSearchPage getTipCueBanSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public TipCueBanSearchPage getTipCueBanSearchPageResult(UserContext userContext, TipCueBanSearchPage tipCueBanSearchPage) throws DemodaServiceException;
    public TipCueBanAdapter getTipCueBanAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	public TipCueBanAdapter getTipCueBanAdapterForCreate(UserContext userContext) throws DemodaServiceException;	
	public TipCueBanVO createTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO) throws DemodaServiceException;
	public TipCueBanAdapter getTipCueBanAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipCueBanVO updateTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO) throws DemodaServiceException;
	public TipCueBanVO deleteTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO) throws DemodaServiceException;
	public TipCueBanVO activarTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO ) throws DemodaServiceException;
	public TipCueBanVO desactivarTipCueBan(UserContext userContext, TipCueBanVO tipCueBanVO ) throws DemodaServiceException;
	public TipCueBanAdapter imprimirTipCueBan(UserContext userContext, TipCueBanAdapter tipCueBanAdapterVO ) throws DemodaServiceException;
	// <--- ABM Tipo Cuenta Banco	
	
	// ---> ABM Partida Cuenta Bancaria
	  public ParCueBanAdapter getParCueBanAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	  public ParCueBanAdapter getParCueBanAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;	
	  public ParCueBanAdapter getParCueBanAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	  public ParCueBanVO updateParCueBan(UserContext userContext, ParCueBanVO parCueBanVO) throws DemodaServiceException;
	  public ParCueBanVO deleteParCueBan(UserContext userContext, ParCueBanVO parCueBanVO) throws DemodaServiceException;
	  public ParCueBanVO createParCueBan(UserContext userContext, ParCueBanVO parCueBanVO) throws DemodaServiceException;
	  public ParCueBanAdapter imprimirParCueBan(UserContext userContext, ParCueBanAdapter parCueBanAdapterVO ) throws DemodaServiceException;
  // <--- ABM Partida Cuenta Bancaria
	  
  	// ---> ABM LeyParAcu
	public LeyParAcuSearchPage getLeyParAcuSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public LeyParAcuSearchPage getLeyParAcuSearchPageResult(UserContext usercontext, LeyParAcuSearchPage leyParAcuSearchPage) throws DemodaServiceException;
	public LeyParAcuAdapter getLeyParAcuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public LeyParAcuAdapter getLeyParAcuAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public LeyParAcuVO createLeyParAcu(UserContext userContext, LeyParAcuVO leyParAcuVO ) throws DemodaServiceException;
	public LeyParAcuVO updateLeyParAcu(UserContext userContext, LeyParAcuVO leyParAcuVO ) throws DemodaServiceException;
	public LeyParAcuVO deleteLeyParAcu(UserContext userContext, LeyParAcuVO leyParAcuVO ) throws DemodaServiceException;
	// <--- ABM LeyParAcu

	// ---> ABM TipoIndet
	public TipoIndetSearchPage getTipoIndetSearchPageInit(UserContext usercontext) throws DemodaServiceException;
	public TipoIndetSearchPage getTipoIndetSearchPageResult(UserContext usercontext, TipoIndetSearchPage TipoIndetSearchPage) throws DemodaServiceException;
	public TipoIndetAdapter getTipoIndetAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoIndetAdapter getTipoIndetAdapterForCreate(UserContext userContext) throws DemodaServiceException;
	public TipoIndetAdapter getTipoIndetAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public TipoIndetAdapter imprimirTipoIndet(UserContext userContext, TipoIndetAdapter tipoIndetAdapterVO ) throws DemodaServiceException;
	public TipoIndetVO createTipoIndet(UserContext userContext, TipoIndetVO TipoIndetVO ) throws DemodaServiceException;
	public TipoIndetVO updateTipoIndet(UserContext userContext, TipoIndetVO TipoIndetVO ) throws DemodaServiceException;
	public TipoIndetVO deleteTipoIndet(UserContext userContext, TipoIndetVO TipoIndetVO ) throws DemodaServiceException;
//	public TipoIndetVO activarTipoIndet(UserContext userContext, TipoIndetVO TipoIndetVO ) throws DemodaServiceException;
//	public TipoIndetVO desactivarTipoIndet(UserContext userContext, TipoIndetVO TipoIndetVO ) throws DemodaServiceException;	
	// <--- ABM TipoIndet
	
}
