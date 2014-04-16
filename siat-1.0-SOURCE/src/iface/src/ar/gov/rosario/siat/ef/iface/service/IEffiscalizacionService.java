//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.service;

import ar.gov.rosario.siat.ef.iface.model.ActaAdapter;
import ar.gov.rosario.siat.ef.iface.model.ActaVO;
import ar.gov.rosario.siat.ef.iface.model.AliComFueColAdapter;
import ar.gov.rosario.siat.ef.iface.model.AproOrdConAdapter;
import ar.gov.rosario.siat.ef.iface.model.AproOrdConVO;
import ar.gov.rosario.siat.ef.iface.model.ComAjuAdapter;
import ar.gov.rosario.siat.ef.iface.model.ComAjuVO;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteAdapter;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteVO;
import ar.gov.rosario.siat.ef.iface.model.ComparacionAdapter;
import ar.gov.rosario.siat.ef.iface.model.ComparacionVO;
import ar.gov.rosario.siat.ef.iface.model.DetAjuAdapter;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDocSopAdapter;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDocSopVO;
import ar.gov.rosario.siat.ef.iface.model.DetAjuVO;
import ar.gov.rosario.siat.ef.iface.model.InicioInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.InicioInvVO;
import ar.gov.rosario.siat.ef.iface.model.MesaEntradaAdapter;
import ar.gov.rosario.siat.ef.iface.model.MesaEntradaVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConBasImpAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdConDocAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdConDocVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlFisAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlFisSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.model.PeriodoOrdenAdapter;
import ar.gov.rosario.siat.ef.iface.model.PeriodoOrdenVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatColAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatColVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatDetAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatDetVO;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatVO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.UserContext;

public interface IEffiscalizacionService {

	// ---> ADM Orden Control Fiscal 
	public OrdenControlFisSearchPage getOrdenControlFisSearchPageInit(UserContext usercontext) throws DemodaServiceException;	
	public OrdenControlFisSearchPage getOrdenControlFisSearchPageResult(UserContext usercontext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException;
	public OrdenControlFisSearchPage getOrdenControlFisSearchPageParamPersona(UserContext userContext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException;
	public OrdenControlFisSearchPage getOrdenControlFisSearchPageParamInspector(UserContext userContext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException;
	public OrdenControlFisSearchPage getOrdenControlFisSearchPageParamOrigenOrden(UserContext userContext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException;
	public OrdenControlFisSearchPage getOrdenControlFisSearchPageParamSupervisor(UserContext userContext, OrdenControlFisSearchPage ordenControlFisSearchPage) throws DemodaServiceException;
	public OrdenControlFisAdapter getOrdenControlFisAdapterForView(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException;
	public OrdenControlFisAdapter getOrdenControlFisAdapterForViewHistoricos(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException;
	public OrdenControlFisAdapter getOrdenControlFisAdapterForViewOrdenesAnt(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException;
	public OrdenControlFisAdapter getOrdenControlFisAdapterForViewOrdenAnterior(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException;
	public OrdenControlVO updateOrdenControlFis(UserContext userContext, OrdenControlVO ordenControl) throws DemodaServiceException;
	public OrdenControlFisAdapter getOrdenControlFisAdapterForAdmin(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException;
	public OrdenControlFisAdapter getAsignarOrdenControlFisAdapterInit(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException;
	public OrdenControlVO enviarMesaEntrada(UserContext userContext, OrdenControlVO ordenControlVO) throws DemodaServiceException;
	public OrdenControlFisAdapter getParamSearchInspector(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException;	
	public OrdenControlFisAdapter getParamSearchSupervisor(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException;
	public OrdenControlFisAdapter getParamSearchSupervisorSelect(UserContext userContext, OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException;
	public OrdenControlVO asignarOrdenControl(UserContext userContext,OrdenControlFisAdapter ordenControlFisAdapter) throws DemodaServiceException;

	// --> ABM periodoOrden
	public PeriodoOrdenAdapter getPeriodoOrdenAdapterForAgregar(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException;
	public PeriodoOrdenAdapter getPeriodoOrdenAdapterResult(UserContext userContext, PeriodoOrdenAdapter periodoOrdenAdapter) throws DemodaServiceException;
	public PeriodoOrdenAdapter getPeriodoOrdenAdapterForView(UserContext userSession, CommonKey commonKey)throws DemodaServiceException;
	public PeriodoOrdenAdapter agregarListPeriodoOrden(UserContext userSession,PeriodoOrdenAdapter periodoOrdenAdapter)throws DemodaServiceException;
	public PeriodoOrdenVO deletePeriodoOrden(UserContext userSession,PeriodoOrdenVO periodoOrden)throws DemodaServiceException;
	// <-- ABM periodoOrden

	// --> ABM Actas
	public ActaAdapter getActaAdapterForCreate(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException;
	public ActaAdapter getActaAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ActaAdapter getActaAdapterParamTipoActa(UserContext userContext, ActaAdapter actaAdapter) throws DemodaServiceException;
	public ActaAdapter getActaAdapterParamPersona(UserContext userContext, ActaAdapter actaAdapter) throws DemodaServiceException;
	public ActaVO createActa(UserContext userContext,ActaVO actaVO)throws DemodaServiceException;
	public ActaVO updateActa(UserContext userContext,ActaAdapter actaAdapter)throws DemodaServiceException;
	public ActaAdapter getActaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ActaVO deleteActa(UserContext userContext,ActaVO actaVO)throws DemodaServiceException;
	public ActaAdapter getActaAdapter4UpdateObsDoc(UserContext userContext, ActaAdapter actaAdapter) throws DemodaServiceException;
	public OrdConDocVO updateObsOrdConDoc(UserContext userContext, OrdConDocVO ordConDocVO) throws DemodaServiceException;
	public PrintModel imprimirActaIni(UserContext userContext, ActaAdapter actaAdapterVO)throws DemodaServiceException;
	public PrintModel imprimirActaReqInf(UserContext userContext, ActaAdapter actaAdapterVO)throws DemodaServiceException;
	public PrintModel imprimirActaProc(UserContext userContext, ActaAdapter actaAdapterVO)throws DemodaServiceException;
	// <-- ABM Actas

	// ---> ABM OrdConDoc
	public OrdConDocAdapter getOrdConDocAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OrdConDocAdapter getOrdConDocAdapterForCreate(UserContext userContext, CommonKey commonKeyIdActa) throws DemodaServiceException;
	public OrdConDocAdapter createOrdConDoc(UserContext userContext,OrdConDocAdapter ordConDocAdapterVO) throws DemodaServiceException;
	public OrdConDocVO deleteOrdConDoc(UserContext userContext,OrdConDocVO ordConDoc) throws DemodaServiceException;

	// ---> ABM InicioInv
	public InicioInvAdapter getInicioInvAdapterForCreate(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException;
	public InicioInvAdapter getInicioInvAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public InicioInvVO createInicioInv(UserContext userContext,InicioInvVO inicioInvVO)throws DemodaServiceException;
	public InicioInvVO updateInicioInv(UserContext userContext,InicioInvVO inicioInvVO)throws DemodaServiceException;
	// <--- ABM InicioInv

	// ---> ABM PlaFueDat
	public PlaFueDatAdapter getPlaFueDatAdapterForCreate(UserContext userContext, CommonKey commonKeyIdOrdCon) throws DemodaServiceException;
	public PlaFueDatAdapter getPlaFueDatAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlaFueDatVO createPlaFueDat(UserContext userContext,PlaFueDatVO plaFueDatVO)throws DemodaServiceException;
	public PlaFueDatAdapter getPlaFueDatAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlaFueDatVO deletePlaFueDat(UserContext userContext,PlaFueDatVO plaFueDatVO)throws DemodaServiceException;
	public PlaFueDatVO updatePlaFueDat(UserContext userContext,PlaFueDatVO plaFueDatVO)throws DemodaServiceException;
	public PlaFueDatAdapter generarModificarPlanilla(UserContext userContext,PlaFueDatAdapter plaFueDatAdapter)throws DemodaServiceException;
	public PlaFueDatAdapter getPlaFueDatAdapterForUpdateDetalle(UserContext userContext,PlaFueDatAdapter plaFueDatAdapter) throws DemodaServiceException;
	public PrintModel imprimirPlaFueDat(UserContext userContext,PlaFueDatAdapter plaFueDatAdapterVO)throws DemodaServiceException;
	// <--- ABM PlaFueDat

	// ---> ABM PlaFueDatDet
	public PlaFueDatAdapter updatePlaFueDatDet(UserContext userContext, PlaFueDatAdapter plaFueDatAdapter)throws DemodaServiceException;
	public PlaFueDatDetAdapter getPlaFueDatDetAdapterForView(UserContext userContext,CommonKey commonKey) throws DemodaServiceException;
	public PlaFueDatDetVO deletePlaFueDatDet(UserContext userContext, PlaFueDatDetVO plaFueDatDetVO)throws DemodaServiceException;
	public PlaFueDatDetAdapter getPlaFueDatDetAdapterForCreate(UserContext userContext,CommonKey ckIdPlaFueDat) throws DemodaServiceException;
	public PlaFueDatDetAdapter createPlaFueDatDet(UserContext userContext, PlaFueDatDetAdapter plaFueDatDetAdapter) throws DemodaServiceException;
	// <--- ABM PlaFueDatDet		

	// ---> ABM PlaFueDatCol
	public PlaFueDatColAdapter createPlaFueDatCol(UserContext userContext, PlaFueDatColAdapter plaFueDatColAdapter) throws DemodaServiceException;
	public PlaFueDatColVO deletePlaFueDatCol(UserContext userContext, PlaFueDatColVO plaFueDatColVO) throws DemodaServiceException;
	public PlaFueDatColAdapter getPlaFueDatColAdapterForCreate(UserContext userContext, CommonKey commonKeyIdPlaFueDat) throws DemodaServiceException;
	public PlaFueDatColAdapter getPlaFueDatColAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlaFueDatColAdapter getPlaFueDatColAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public PlaFueDatColAdapter updatePlaFueDatCol(UserContext userContext, PlaFueDatColAdapter plaFueDatColAdapter) throws DemodaServiceException;
	// <--- ABM PlaFueDatCol



	// ---> ADM SolicitudEmiPerRetro
	public OrdenControlAdapter getSolicitudEmiPerRetroInit(UserContext userContext) throws DemodaServiceException;	
	public OrdenControlAdapter getSolicitudEmiPerRetroAdapterParamOrdenControl(UserContext userContext, OrdenControlAdapter ordenControlAdapter) throws DemodaServiceException;
	public OrdenControlAdapter getSolicitudEmiPerRetroAdapterParamOrdConCue(UserContext userContext, OrdenControlAdapter ordenControlAdapter) throws DemodaServiceException;
	public OrdenControlAdapter enviarSolicitud(UserContext userContext, OrdenControlAdapter ordenControlAdapterVO) throws DemodaServiceException;
	// <--- ADM SolicitudEmiPerRetro


	// ---> ABM Comparacion
	public ComparacionAdapter getComparacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ComparacionAdapter getComparacionAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException;
	public ComparacionAdapter getComparacionAdapterForUpdate(UserContext userContext, CommonKey commonKeyComparacion) throws DemodaServiceException;
	public ComparacionVO createComparacion(UserContext userContext, ComparacionVO comparacionVO) throws DemodaServiceException;
	public ComparacionVO updateComparacion(UserContext userContext, ComparacionVO comparacionVO) throws DemodaServiceException;
	public ComparacionVO deleteComparacion(UserContext userContext, ComparacionVO comparacionVO) throws DemodaServiceException;
	public ComparacionAdapter imprimirComparacion(UserContext userContext, ComparacionAdapter comparacionAdapterVO)throws DemodaServiceException;

	// <--- ABM Comparacion

	// ---> ABM CompFuente
	public CompFuenteAdapter getCompFuenteAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public CompFuenteAdapter getCompFuenteAdapterForCreate(UserContext userContext, CommonKey commonKeyComparacion) throws DemodaServiceException;
	public CompFuenteAdapter getCompFuenteAdapterParamPlaFueDat(UserContext userContext, CompFuenteAdapter compFuenteAdapter) throws DemodaServiceException;
	public CompFuenteVO createCompFuente(UserContext userContext, CompFuenteVO compFuenteVO) throws DemodaServiceException;
	public CompFuenteVO deleteCompFuente(UserContext userContext, CompFuenteVO compFuenteVO) throws DemodaServiceException;
	public ComparacionAdapter createCompFuenteRes(UserContext userContext, ComparacionAdapter comparacionAdapter) throws DemodaServiceException;
	public ComparacionAdapter deleteCompFuenteRes(UserContext userContext, ComparacionAdapter comparacionAdapter) throws DemodaServiceException;
	// <--- ADM CompFuente

	// ---> ABM OrdConBasImp
	public OrdConBasImpAdapter getOrdConBasImpAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException;
	public OrdConBasImpAdapter getOrdConBasImpAdapterParamCompFuenteRes(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter getOrdConBasImpAdapterParamSelecFuente(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter createOrdConBasImp(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter getOrdConBasImpAdapterForAjustes(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OrdConBasImpAdapter getOrdConBasImpAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public OrdConBasImpAdapter getOrdConBasImpAdapter4UpdateAjustes(UserContext userContext, CommonKey commonKeyPlaFueDatCom, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter updateAjustes(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter deleteOrdConBasImp(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter getOrdConBasImpAdapter4AjustarPeriodo(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter ajustarPeriodos(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter getOrdConBasImpAdapterForAlicuotas(UserContext userContext,  CommonKey commonKey) throws DemodaServiceException;
	public OrdConBasImpAdapter getOrdConBasImpAdapterForUpdateAlicuota(UserContext userContext,OrdConBasImpAdapter ordConBasImpAdapter) throws DemodaServiceException;
	public OrdConBasImpAdapter updateAlicuotas(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter)throws DemodaServiceException;
	public PrintModel imprimirOrdConBasImp(UserContext userContext, CommonKey commonKey)throws DemodaServiceException;
	public OrdConBasImpAdapter updateMasivoAlicuotas(UserContext userContext, OrdConBasImpAdapter ordConBasImpAdapter)throws DemodaServiceException;
	// <--->ABM OrdConBasImp
	
	// ---> ABM DetAju
	public DetAjuAdapter getDetAjuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DetAjuAdapter getDetAjuAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException;
	public DetAjuAdapter getDetAjuAdapterForUpdate(UserContext userContext, CommonKey commonKeyDetAju) throws DemodaServiceException;
	public DetAjuVO createDetAju(UserContext userContext, DetAjuVO detAjuVO) throws DemodaServiceException;
	public DetAjuVO updateDetAju(UserContext userContext, DetAjuVO detAjuVO) throws DemodaServiceException;
	public DetAjuVO deleteDetAju(UserContext userContext, DetAjuVO detAjuVO) throws DemodaServiceException;
	public DetAjuAdapter agregarMasivo(UserContext userContext, DetAjuAdapter detAjuAdapter) throws DemodaServiceException;
	public DetAjuAdapter getDetAjuAdapter4ModifRetencion(UserContext userContext, DetAjuAdapter detAjuAdapter) throws DemodaServiceException;
	public DetAjuAdapter modificarRetencion(UserContext userContext, DetAjuAdapter detAjuAdapter) throws DemodaServiceException;
	// <--- ABM DetAju

	// ---> ABM AliComFueCol
	public AliComFueColAdapter getAliComFueColAdapterInit(UserContext userContext, CommonKey commonKeyDetAju) throws DemodaServiceException;
	public AliComFueColAdapter getAliComFueColAdapterFor4Hist(UserContext userContext, CommonKey commonKey, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException;
	public AliComFueColAdapter getAliComFueColAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AliComFueColAdapter getAliComFueColAdapterForCreate(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException;
	public AliComFueColAdapter getAliComFueColAdapterForUpdate(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException;
	public AliComFueColAdapter createAliComFueCol(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException;
	public AliComFueColAdapter updateAliComFueCol(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException;
	public AliComFueColAdapter deleteAliComFueCol(UserContext userContext, AliComFueColAdapter aliComFueColAdapter) throws DemodaServiceException;
	public DetAjuAdapter imprimirDetAju(UserContext userContext, DetAjuAdapter detAjuAdapterVO,boolean impAjuPos)throws DemodaServiceException;

	// ---> ABM MesaEntrada 	
	public MesaEntradaAdapter getMesaEntradaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public MesaEntradaAdapter getMesaEntradaAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException;
	public MesaEntradaAdapter getMesaEntradaAdapterForUpdate(UserContext userContext, CommonKey commonKeyMesaEntrada) throws DemodaServiceException;
	public MesaEntradaVO createMesaEntrada(UserContext userContext, MesaEntradaVO mesaEntradaVO) throws DemodaServiceException;
	public MesaEntradaVO updateMesaEntrada(UserContext userContext, MesaEntradaVO mesaEntradaVO) throws DemodaServiceException;
	public MesaEntradaVO deleteMesaEntrada(UserContext userContext, MesaEntradaVO mesaEntradaVO) throws DemodaServiceException;
	// <--- ABM MesaEntrada

	// ---> ABM AproOrdCon
	public AproOrdConAdapter getAproOrdConAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public AproOrdConAdapter getAproOrdConAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException;
	public AproOrdConAdapter getAproOrdConAdapterForUpdate(UserContext userContext, CommonKey commonKeyAproOrdCon) throws DemodaServiceException;
	public AproOrdConAdapter getAproOrdConAdapterParamEstado(UserContext userContext, AproOrdConAdapter aproOrdConAdapter) throws DemodaServiceException;
	public AproOrdConAdapter getAproOrdConAdapterParamAjuste(UserContext userContext, AproOrdConAdapter aproOrdConAdapter) throws DemodaServiceException;
	public AproOrdConVO createAproOrdCon(UserContext userContext, AproOrdConVO aproOrdConVO) throws DemodaServiceException;
	public AproOrdConVO updateAproOrdCon(UserContext userContext, AproOrdConVO aproOrdConVO) throws DemodaServiceException;
	public AproOrdConVO deleteAproOrdCon(UserContext userContext, AproOrdConVO aproOrdConVO) throws DemodaServiceException;
	public AproOrdConAdapter quitarCaso(UserContext userContext, AproOrdConAdapter aproOrdConAdapter)throws DemodaServiceException;
	// <--- ABM AproOrdCon

	// ---> ABM DetAjuDocSop 	
	public DetAjuDocSopAdapter getDetAjuDocSopAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public DetAjuDocSopAdapter getDetAjuDocSopAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdConDoc) throws DemodaServiceException;
	public DetAjuDocSopAdapter getDetAjuDocSopAdapterForUpdate(UserContext userContext, CommonKey commonKeyDetAjuDocSop) throws DemodaServiceException;
	public DetAjuDocSopVO createDetAjuDocSop(UserContext userContext, DetAjuDocSopVO detAjuDocSopVO) throws DemodaServiceException;
	public DetAjuDocSopVO updateDetAjuDocSop(UserContext userContext, DetAjuDocSopVO detAjuDocSopVO) throws DemodaServiceException;
	public DetAjuDocSopVO deleteDetAjuDocSop(UserContext userContext, DetAjuDocSopVO detAjuDocSopVO) throws DemodaServiceException;
	public OrdenControlVO cerrarOrdenControl(UserContext userContext, OrdenControlVO ordenControlVO) throws DemodaServiceException;
	public DetAjuDocSopAdapter cambiarDocumentacionParam(UserContext userContext, DetAjuDocSopAdapter detAjuDocSopAdapter) throws DemodaServiceException;
	// <--- ABM DetAjuDocSop

	// ---> ABM ComAju
	public ComAjuAdapter getComAjuAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException;
	public ComAjuAdapter getComAjuAdapterForCreate(UserContext userContext, CommonKey commonKeyOrdCon) throws DemodaServiceException;
	public ComAjuAdapter getComAjuAdapterForUpdate(UserContext userContext, CommonKey commonKeyComAju) throws DemodaServiceException;
	public ComAjuVO createComAju(UserContext userContext, ComAjuVO comAjuVO) throws DemodaServiceException;
	public ComAjuVO updateComAju(UserContext userContext, ComAjuVO comAjuVO) throws DemodaServiceException;
	public ComAjuVO deleteComAju(UserContext userContext, ComAjuVO comAjuVO) throws DemodaServiceException;
	public ComAjuAdapter imprimirComAju(UserContext userContext, ComAjuAdapter comAjuAdapterVO)throws DemodaServiceException;
	
	public DetAjuAdapter updateActualizacion(UserContext userContext, DetAjuAdapter detAjuAdapter) throws DemodaServiceException;
	// <--- ABM ComAju
}
