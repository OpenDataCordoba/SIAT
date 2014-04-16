//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.ws.drei;

/**
 * Implementacion de web service
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.CierreComercio;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.EstCue;
import ar.gov.rosario.siat.pad.buss.bean.PadCuentaManager;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.RecAtrCueV;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.Cuna;
import ar.gov.rosario.siat.pad.iface.model.MRCategoriaRS;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pad.iface.ws.drei.WSRegimenSimplificado;
import ar.gov.rosario.siat.rec.buss.bean.CatRSDrei;
import ar.gov.rosario.siat.rec.buss.bean.NovedadRS;
import ar.gov.rosario.siat.rec.buss.bean.TipoTramiteRS;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiVO;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class AdhesionRSWServiceHbmImpl implements WSRegimenSimplificado{ 
	private static Logger log = Logger.getLogger(AdhesionRSWServiceHbmImpl.class);

	//private static final int MES_INICIO=12;
	//private static final int ANIO_INICIO=2009;


	public String getCombCUNA() throws Exception {
		int cant=0;
		String ret="";
		
		// Obtengo la lista de categorias vigentes
		List<CatRSDrei> listCatRsDrei= CatRSDrei.getListVigentes();
		// paso a VO
		List<CatRSDreiVO> listCatRSDReiVO = ((ArrayList<CatRSDreiVO>) ListUtilBean.toVO(listCatRsDrei, 0));
		
		
		// cabecera de la 
		ret ="VIGENCIA|N, CUMUR|N, IMPDREI|N, IMPETUR|N,;";
		
		// habilitacion social: 3 items
		for (int i1=1; i1<=3; i1++) {
			
			// ingresos brutos: 4 items
			for (int i2=1; i2<=4; i2++) {
				
				// superficie: 5 items 
				for (int i3=1; i3<=5; i3++) {
					
					// adicionales: 3 itms
					for (int i4=1; i4<=3; i4++) {
						
						// adicional etur: 5 items
						for (int i5=1; i5<=5; i5++) {
							
							cant++;
							Cuna cuna = new Cuna(0, i1, i2, i3, i4, i5, listCatRSDReiVO);
							ret+= cuna.getTablaParametro();
							
						}
					}
				}
			}
		}
		ret += "cantidad de combinaciones generdas: " + cant + "\n";
		return ret;
	}
	
	
	
	/**
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public MRCategoriaRS procesarServicioRS (
			String tipoTransaccion, String usuario, Integer tipoUsuario, String cuit, String desCont, Integer tipoContribuyente, String isib, String nroCuenta, String listActividades, 
			Integer mesInicio, Integer anioInicio, Double ingBruAnu, Double supAfe, Double publicidad,  Double redHabSoc, Integer adicEtur, 
			Double precioUnitario, Integer canPer, boolean confirmado, String domicilioLocal, String telefono, String email, Integer mesBaja, Integer anioBaja, Integer motivoBaja, Long idTramite  ) throws Exception{
		
		
		
		try {
			MRCategoriaRS categoriaRS = new MRCategoriaRS();


			// procesa segun el tipo de transaccion
			if (tipoTransaccion.equals("GENERAR-CUMUR") ) {

				categoriaRS.setListCUMUR(getCombCUNA());
				return categoriaRS;

			}
			
			// quita los ceros a la izquierda
			nroCuenta = StringUtil.quitarCerosIzq(nroCuenta);
			
			// ** verifica los datos nulos segun el tipo de transaccion
			categoriaRS = validarNulos(categoriaRS, tipoTransaccion, usuario, tipoUsuario, cuit, desCont, tipoContribuyente, isib, 
					nroCuenta, listActividades, mesInicio, anioInicio, ingBruAnu, supAfe, publicidad, redHabSoc, adicEtur, 
					precioUnitario, canPer, confirmado, domicilioLocal, telefono, email, mesBaja, anioBaja, motivoBaja, idTramite);
			
			if (categoriaRS.getCodError().intValue()!=0) {
				return categoriaRS;
			}
			
			String msgDeuda="";
			// procesa segun el tipo de transaccion
			if (tipoTransaccion.equals("ADHESION") || tipoTransaccion.equals("MODIFICACION") || tipoTransaccion.equals("RECATEGORIZACION")) {

				categoriaRS = procesarAltaModifRecat(categoriaRS, tipoTransaccion, usuario, tipoUsuario, cuit, desCont, tipoContribuyente, isib, 
						nroCuenta, listActividades, mesInicio, anioInicio, ingBruAnu, supAfe, publicidad, redHabSoc, adicEtur, 
						precioUnitario, canPer, confirmado, domicilioLocal, telefono, email);

				
			} else if (tipoTransaccion.equals("BAJA")) {

				categoriaRS = procesarBaja(categoriaRS, tipoTransaccion, usuario, tipoUsuario, cuit, desCont, tipoContribuyente, nroCuenta, 
						mesBaja, anioBaja, motivoBaja, confirmado );

				
			} else if (tipoTransaccion.equals("LISTAR-TRAMITES") ) {

				categoriaRS = procesarListarTramites(categoriaRS, tipoTransaccion, usuario, tipoUsuario, cuit);

			} else if (tipoTransaccion.equals("VER-TRAMITE") ) {

				categoriaRS = procesarVerTramite(categoriaRS, tipoTransaccion, usuario, tipoUsuario, idTramite);
				
			} else {
				
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se identifica el tipo de tr�mite");
				
			}
			
			// agrega el mensaje de la acomodada de deuda
			categoriaRS.setMsgDeuda(msgDeuda);

		return categoriaRS;
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new DemodaServiceException(e);

		}
	}
	

		
	private MRCategoriaRS procesarAltaModifRecat(MRCategoriaRS categoriaRS, String tipoTransaccion, String usuario, Integer tipoUsuario, String cuit, String desCont, 
			Integer tipoContribuyente, String isib, String nroCuenta, String listActividades, Integer mesInicio, Integer anioInicio, Double ingBruAnu, Double supAfe, Double publicidad, 
			Double redHabSoc, Integer adicEtur, Double precioUnitario, Integer canPer, boolean confirmado, String domicilioLocal, String telefono, String email) throws Exception {
		
		
		Session session=null;
		Transaction tx =null;
		
		UserContext userContext = new UserContext();
		userContext.setUserName("siat");
		userContext.setIdUsuarioSiat(UsuarioSiat.ID_USUARIO_SIAT); 
		DemodaUtil.setCurrentUserContext(userContext);

		
		// aqui vamos a guardar la fecha del ultimo dia del mes
		Date fechaInicio;

		try {
			session=SiatHibernateUtil.currentSession();
			
			
			Recurso recurso = Recurso.getDReI();
			Cuenta cuenta;
			cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso.getId(), nroCuenta);
		
			// ** valida que el periodo sea mayor al inicio del regimen
			if (!( anioInicio.intValue() * 100 +  mesInicio.intValue() >= (SiatParam.getInteger(SiatParam.ANIO_INICIO_RS)*100 + SiatParam.getInteger(SiatParam.MES_INICIO_RS)) )) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_PERIODOINCORRECTO);
				categoriaRS.addDetalleError("Periodo/Anio incorrectos. Debe ser posterior al per�odo de entrada en vigencia del r�gimen");
				return categoriaRS;

			}

			// ** valida que el periodo sea menor o igual al periodo actual
			if (!( anioInicio.intValue() * 100 +  mesInicio.intValue() <= DateUtil.getAnio(new Date()) * 100 +  DateUtil.getMes(new Date()) ))  {
				
				// pero si ahora estamos en NOVIEMBRE-09 LO DEJAMOS PARA DICIEMBRE-09
				
				if (! ( (anioInicio.intValue() * 100 +  mesInicio.intValue())==200912 && 
						DateUtil.getAnio(new Date()) * 100 +  DateUtil.getMes(new Date()) == 200911)) {
				
					categoriaRS.setCodError(MRCategoriaRS.ERR_PERIODOINCORRECTO);
					categoriaRS.addDetalleError("Periodo/Anio incorrectos. Debe ser menor o igual al per�odo actual");
					return categoriaRS;
				}

			}

			// calcula la fecha de inicio como primer dia del periodo/anio
			fechaInicio = DateUtil.getFirstDatOfMonth(mesInicio, anioInicio);
			
			
			// ** verifica algunos tipos 
			//    tipo de tramite
			TipoTramiteRS tipoTramite = TipoTramiteRS.getByDes(tipoTransaccion);
			if (tipoTramite==null) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El tipo de tramite es incorrecto");
				return categoriaRS;
			}
			//    tipo de usuario
			if (! (tipoUsuario.intValue()==1 || tipoUsuario.intValue()==2 )) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El tipo de usuario es incorrecto");
				return categoriaRS;
			}
			//    tipo de contribuyente
			if (! (tipoContribuyente.intValue()==1 || tipoContribuyente.intValue()==2 )) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El Tipo de Contribuyente es incorrecto");
				return categoriaRS;
			}
			
			// ** valida que exista la cuenta 
			if (cuenta == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_NOEXISTECUENTA);
				categoriaRS.addDetalleError("La cuenta no existe en los registros de la Municipalidad de Rosario");
				return categoriaRS;
			}
			
			// genero una fecha muy a futuro para levantar los atributos
			Date fechaFuturo = DateUtil.getDate(2050, 01, 01);
			
			// obtiene los atributos actuales de la cuenta de DRei para usarlos mas adelante
			RecAtrCueV dreAtrCueRegVActual = cuenta.getRegimenDreiVigente(fechaFuturo);
			RecAtrCueV dreAtrCueCumVActual = cuenta.getValorCumurVigente(fechaFuturo);
			RecAtrCueV dreAtrCueCatVActual = cuenta.getValorCategoriaRSVigente(fechaFuturo);
			RecAtrCueV dreAtrCuePerVActual = cuenta.getValorPerIniRSVigente(fechaFuturo);

			// Guardamos el Valor de Periodo Inicio anterior al procesamiento del nuevo tramite para validacion
			String perVActualParaValidacion = null;
			if(dreAtrCuePerVActual != null)
				perVActualParaValidacion = dreAtrCuePerVActual.getValor();
			// Guardamos el Valor de CUMUR anterior al procesamiento del nuevo tramite para validacion			
			String cumurVActualParaValidacion = null;
			if(dreAtrCueCumVActual != null)
				cumurVActualParaValidacion = dreAtrCueCumVActual.getValor();
			
			log.debug("ATRIBUTO REGIMEN: "+ (dreAtrCueRegVActual!=null));
			if (dreAtrCueRegVActual != null) 
				log.debug("VALOR REGIMEN: "+dreAtrCueRegVActual.getValor());
			
			// si el tipo es alta 
			if (tipoTramite.getId().longValue()==TipoTramiteRS.ID_TRAMITE_ALTA) {
				
				
				// ** verifica que la cuenta no este dada de alta como regimen simplificado
				if (dreAtrCueRegVActual != null && !dreAtrCueRegVActual.getValor().equals(Cuenta.VALOR_REGIMEN_GENERAL)){
					categoriaRS.setCodError(MRCategoriaRS.ERR_CUENTAYATIENEALTARS);
					categoriaRS.addDetalleError("La cuenta ya est� dada de alta en el r�gimen simplificado.");
				}

				// ** valida que el cuit no tenga una cuenta ya adherida
				List<NovedadRS> listNovedadRS = NovedadRS.getListTramitesByCuit(cuit);
				if (listNovedadRS == null || listNovedadRS.size()==0) {
					// ok, no hay nada para este CUIT
					
				} else {
					
					NovedadRS ultimaNovedadRS = listNovedadRS.get(0);
					if (!ultimaNovedadRS.getTipoTransaccion().equals("BAJA") ) {
						categoriaRS.setCodError(MRCategoriaRS.ERR_CUENTAYATIENEALTARS);
						categoriaRS.addDetalleError("Ya existe un alta para la CUIT");
					}
				}
					
			} else {
				// si no es un alta 
				
				// ** verifica que la cuenta este con alta en regimen simplificado
				if (dreAtrCueRegVActual == null || dreAtrCueRegVActual.getValor().equals(Cuenta.VALOR_REGIMEN_GENERAL)) {
				
					categoriaRS.setCodError(MRCategoriaRS.ERR_MDF_NOTIENEALTA);
					categoriaRS.addDetalleError("La cuenta no tiene alta en el r�gimen simplificado.");
				}
				
				// ** verifica si el CUIT tiene otra adhesion activa para OTRA cuenta
				List<NovedadRS> listNovedadRS = NovedadRS.getListTramitesByCuit(cuit);
				if (listNovedadRS == null || listNovedadRS.size()==0) {
					// ok, no hay nada para este CUIT
					
				} else {

					// ** 	verifica si el CUIT tiene OTRA adhesion activa para otra cuenta
					NovedadRS ultimaNovedadRS = listNovedadRS.get(0);
					if (!ultimaNovedadRS.getTipoTransaccion().equals("BAJA") ) {
						
						// es una modificacion o un alta
						// verifica que no sea la misma cuenta que la que tenemos aca
						if (!nroCuenta.trim().equals(ultimaNovedadRS.getNroCuenta().trim())) {
							//
							categoriaRS.setCodError(MRCategoriaRS.ERR_CUENTAYATIENEALTARS);
							categoriaRS.addDetalleError("La CUIT posee una adhesi�n a otra cuenta");
							
						}
					}
					
				}
			}
			
			// ** valida que el contribuyente no este incluido en regimen de convenio multilateral de ingresos brutos
			if (isib!=null && isib.length() >= 3 ) {
				if ( Long.parseLong(isib.substring(0, 3))>900L) {
					categoriaRS.setCodError(MRCategoriaRS.ERR_CONVENIOMULTILATERAL);
					categoriaRS.addDetalleError("Cuenta en convenio multilateral de ingresos brutos.");
				}
			}
			
			// ** valida que el comercio no tenga inicio de cierre
			CierreComercio cierreComercio = CierreComercio.getByObjImp(cuenta.getObjImp());
			if (cierreComercio !=null) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_CUENTACONINICIODECIERRE);
				categoriaRS.addDetalleError("Cuenta con inicio de cierre de comercio.");
			}


			// ** valida que el contribuyente no posea mas de una cuenta
			
			/*
			 * dada la cuenta, obtiene el titular principal
			 * con el titularPrincipal
			 *    obtiene la lista de cuentas titular para el contribuyente para el recurso 
			 *    si la cuenta posee un inicio de cierre de comercio, esta OK.
			 */
			// obtiene el titular principal (por si es una soc. hecho)
			CuentaTitular ctp = cuenta.obtenerCuentaTitularPrincipal();
			
			// obtiene el contribuyente
			Contribuyente contr = ctp.getContribuyente();
			// busca las cuentas DREI de un contribuyente 
			List<Cuenta> listCuentas = contr.getListCueVig4Titular(recurso,null);
			// para cada cuenta de drei, verifica que este vigente y no tenga un inicio de cierre
			for (Cuenta otraCuenta : listCuentas) {
				// si es otra cuenta (distinta de la que se esta intentando la adhesion/modif/etc.)
				if (cuenta.getId().longValue() != otraCuenta.getId().longValue() ) {
					// si el estado es 1, esta activa y no tiene inicio de cierre. informamos error y salimos
					if(otraCuenta.getEstado().longValue() == 1L && otraCuenta.getFechaBaja() == null) {
							categoriaRS.setCodError(MRCategoriaRS.ERR_OTRASCUENTAS);
							categoriaRS.addDetalleError("El contribuyente posee otra cuenta activa (sin inicio de Cierre de Comercio)");							
					}
				}
			}

			
			// ** valida que el CUIT de la persona no sea ficticio e igual al pasado en la transaccion

			// obtiene la Persona
			Persona p = Persona.getByIdLight(ctp.getContribuyente().getId());
			// si no es un cuit ficticio
			if (p != null  && ( "R".equals(p.getCuit00()) || "C".equals(p.getCuit00()) ) ) {
				// genero lo que seria el CUIT02 ingresado, quitando los primeros dos caracteres y el ultimo
				String cuit02 = cuit.substring(2, 10);
				// si tiene un cuit correcto
				if (p.getCuit02()!=null) {
					// pero no coincide con el que nos estan pasando
					//if (!p.getCuit02().trim().equals(cuit02)) {
					if (!new Integer(p.getCuit02()).equals(new Integer(cuit02))) {
						categoriaRS.setCodError(MRCategoriaRS.ERR_CUITCUENTA);
						categoriaRS.addDetalleError("La CUIT informada no coincide con la CUIT registrada para la cuenta. " + p.getCuit02() + " - " + cuit02);
						return categoriaRS;
					}
				}	
			}
			
			// ** valida que las actividades ingresadas existan en el padron de actividades
			
			// primero vsalida que el string de actividades venga bien formateado: 
			String[] actividades;
			try {
				actividades = listActividades.split(",");
				List<RecConADec> listRecConADet = RecConADec.getByListCodConceptoRecurso(recurso.getId(), listActividades);
				
				if (listRecConADet==null || actividades==null || listRecConADet.size() != actividades.length ) {
					categoriaRS.setCodError(MRCategoriaRS.ERR_ACTIVIDADES);
					categoriaRS.addDetalleError("Se informan actividades que no existen en el padr�n");
				}
				
			} catch (Exception e) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_ACTIVIDADES);
				categoriaRS.addDetalleError("La lista de actividades est� mal formateada");
				
			}

			// ** verifica que al momento de producirse la adhesion, no existan habilitados ninguno de los rubros prohibidos
			String rubrosNoPerm = SiatParam.getString(SiatParam.RUBROS_NO_PERMITIDOS_EN_RS); 
						
			
			try {
				// con la cuenta busco el objimp
				TipObjImpDefinition tipObjImpDefinition = cuenta.getObjImp().getDefinitionValue(fechaInicio);
				for (TipObjImpAtrDefinition tipObjImpAtrDefinition : tipObjImpDefinition.getListTipObjImpAtrDefinition() ) {
					
					log.debug(" **WS** - valor atributo COD=: " + tipObjImpAtrDefinition.getCodTipoAtributo() + " - VAL:" + tipObjImpAtrDefinition.getValorString() );
					if ("Rubro".equals(tipObjImpAtrDefinition.getAtributo().getCodAtributo() )) {
						
						for(String strValor: tipObjImpAtrDefinition.getMultiValor()) {
							
							strValor = "|" + strValor + "|";
							log.debug(" **WS** - valor atributo rubro: " + strValor);
							
							if (rubrosNoPerm.indexOf(strValor) >= 0) {
								categoriaRS.setCodError(MRCategoriaRS.ERR_RUBROSCOMERCIO);
								categoriaRS.addDetalleError("El rubro " + strValor + " habilitado para el Comercio, est� excluido del R�gimen Simplificado");
								
							}
						}
					}
				}
				
			} catch (Exception e) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_RUBROSCOMERCIO);
				categoriaRS.addDetalleError("No se obtuvieron rubros habilitados para el comercio");
				
			}

			
			// ** validacion bizarra: no debe permitir habilitacion social!!!!!!!!!!!
			if ( !redHabSoc.equals(0D) ) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_ALICREDHABSOCFUERARANGO);
				categoriaRS.addDetalleError("La cuenta se encuentra fuera de los par�metros de Reducci�n por Habilitaci�n Social");
			}

			
			
			
			// **************************
			// aqui pasaron todas las validaciones generales, ahora pedimo el Cuna
			// **************************
			Date fechaDesde;
			
			// obtengo la lista de categorias vigentes
			List<CatRSDrei> listCatRsDrei= CatRSDrei.getListVigentes();

			// pasa las categorias a VO para invocar la construccion del Cuna
			List<CatRSDreiVO> listCatRSDReiVO = ((ArrayList<CatRSDreiVO>) ListUtilBean.toVO(listCatRsDrei, 0));


			// obtiene el cuna en funcion de los parametros ingresados por los usuarios
			Cuna cuna = new Cuna(canPer, precioUnitario, redHabSoc, ingBruAnu, supAfe, publicidad, adicEtur, listCatRSDReiVO);
			
			//
			if (cuna.getCodError().equals(0L)) {
				// estamos ok
				categoriaRS.setCodError(0);
				
				
			} else if (cuna.getCodError().equals(MRCategoriaRS.ERR_INESPERADO)) {
				categoriaRS.setCodError(cuna.getCodError());
				categoriaRS.addDetalleError(cuna.getDetalleError());
				
			} else if (cuna.getCodError().equals(MRCategoriaRS.ERR_CANTIDADPERSONAL)) {
				categoriaRS.setCodError(cuna.getCodError());
				categoriaRS.addDetalleError("Cantidad de personal fuera de par�metros de inclusi�n al r�gimen simplificado");

			} else if (cuna.getCodError().equals(MRCategoriaRS.ERR_PRECIOUNITARIO)) {
				categoriaRS.setCodError(cuna.getCodError());
				categoriaRS.addDetalleError("Precio unitariofuera de par�metros de inclusi�n al r�gimen simplificado");

			} else if (cuna.getCodError().equals(MRCategoriaRS.ERR_INGRESOSBRUTOSFUERARANGO)) {
				categoriaRS.setCodError(cuna.getCodError());
				categoriaRS.addDetalleError("Ingresos brutos anuales fuera de par�metros de inclusi�n al r�gimen simplificado.");

			} else if (cuna.getCodError().equals(MRCategoriaRS.ERR_SUPERFICIEFUERARANGO)) {
				categoriaRS.setCodError(cuna.getCodError());
				categoriaRS.addDetalleError("Superficie Habilitada fuera de par�metros de inclusi�n al r�gimen simplificado.");

				
			} else if (cuna.getCodError().equals(MRCategoriaRS.ERR_ALICADICIONALFUERARANGO)) {
				categoriaRS.setCodError(cuna.getCodError());
				categoriaRS.addDetalleError("Alicuota Adicionales fuera de rango");
				
			} else if (cuna.getCodError().equals(MRCategoriaRS.ERR_ADICETURFUERARANGO)) {
				categoriaRS.setCodError(cuna.getCodError());
				categoriaRS.addDetalleError("Adicional Etur fuera de rango");
				
			} else if (cuna.getCodError().equals(MRCategoriaRS.ERR_ALICREDHABSOCFUERARANGO)) {
				categoriaRS.setCodError(cuna.getCodError());
				categoriaRS.addDetalleError("Alicuota de reducci�n por habilitaci�n social fuera de par�metros de inclusi�n al r�gimen simplificado.");

			}
			
			//------------//
			
			// obtiene la cuenta etur a la fecha de inicio
			Recurso etur = Recurso.getETur();
			Cuenta cuentaEtur = PadDAOFactory.getCuentaDAO().getCuentaActivaByObjImpYRecursoYFecha(cuenta.getObjImp(), etur, fechaInicio);
			
			// Si EtuR=0 y tiene cuenta ETuR informo el error
			if (cuentaEtur!= null && cuna.getImporteEtur() ==0) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_FALTADECLARARETUR);
				categoriaRS.addDetalleError("Existe cuenta Etur y no se registra declaraci�n de adicionales.");
			}
			
			// ** Caso de RECATEGORIZACION - solo permitir la modificacion en los meses de recategorizacion (ENERO, MAYO, SEPTIEMBRE)
			if (tipoTramite.getId().longValue()==TipoTramiteRS.ID_TRAMITE_RECATEGORIZACION) {
				
				// verifica que sea el mes que corresponde
				if ( !( DateUtil.getMes(new Date()) == 1 || DateUtil.getMes(new Date()) == 5 || DateUtil.getMes(new Date()) == 9  )) {
					categoriaRS.setCodError(MRCategoriaRS.ERR_RCT_FUERATIEMPO);
					categoriaRS.addDetalleError("La recategorizacion solo puede efectuarse en los meses: Enero, Mayo o Septiembre");
				}
				
			}

			
			// ** Caso de MODIFICACION - no permite una modificacion retroactiva por un valor menor al actual
			if (tipoTramite.getId().longValue()==TipoTramiteRS.ID_TRAMITE_MODIFICACION.longValue()) {

				// si es una modificacion retroactiva, solo se permite modificacion en mas
				if ( anioInicio.intValue() * 100 +  mesInicio.intValue() < ( DateUtil.getAnio(new Date()) * 100 ) + DateUtil.getMes(new Date() )) {
					
					if (dreAtrCueCumVActual==null) {
						categoriaRS.setCodError(MRCategoriaRS.ERR_MDF_CUNAACTUAL);
						categoriaRS.addDetalleError("No existe valor de CUMUR actual. No es posible ejecutar modificaci�n.");
						
					} else {
						
						// obtiene un Cuna actual
						Cuna cunaActual = new Cuna(dreAtrCueCumVActual.getValor(), listCatRSDReiVO);
		
						if (cunaActual == null) {
							categoriaRS.setCodError(MRCategoriaRS.ERR_MDF_CUNAACTUAL);
							categoriaRS.addDetalleError("Error en la valorizacion del cuna actual durante la validacion de la modificacion");
							
						} else {
							
							if (cuna.getImporteTotal().doubleValue() < cunaActual.getImporteTotal().doubleValue()) {
								categoriaRS.setCodError(MRCategoriaRS.ERR_MDF_MENORVALOR);
								categoriaRS.addDetalleError("No es posible efectuar una modificaci�n retroactiva por un importe menor al actual");
							}
						}
					}
				}
			
			}
			
			// aqui tengo el CUNA listo
			// paso el cuit y la cuenta al cuna para luego pedirle el codigo de barras
			cuna.setCuit(cuit);
			cuna.setNroCuenta(nroCuenta);
			//
			if(cuna.getCodBar()==null) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_CODBAR);
				categoriaRS.addDetalleError("Error en la generacion del c�digo de barras");
			}
			
			if (categoriaRS.getCodError() != 0 )
				return categoriaRS;
			
			
			categoriaRS.setNroCategoria(cuna.getNroCategoria() );
			categoriaRS.setImporteDrei(cuna.getImporteDrei() );
			categoriaRS.setImporteAdicional(cuna.getImporteAdicional() );
			categoriaRS.setImporteEtur(cuna.getImporteEtur());
			categoriaRS.setCuna(cuna.getStrCuna());
			categoriaRS.setCodBar(cuna.getCodBar());
			categoriaRS.setCodBarComprimido(cuna.getCodBarComprimido());
			
			// setea la categoria
			categoriaRS.setDesCategoria(cuna.getDesCategoria());
			categoriaRS.setDesPublicidad(cuna.getDesPublicidad());
			categoriaRS.setDesEtur(cuna.getDesEtur());
			
			log.debug("WS** antes de entrar a confirmado ");

			// if confirmado
			if (confirmado) {
			
				// abre la transaccion
				tx= session.beginTransaction();
				
				log.debug("WS** adentro de confirmado ");
				
				// ** actualizacion de datos en la cuenta
				RecAtrCueV atrCueRegV = null;
				RecAtrCueV atrCueCumV = null;
				RecAtrCueV atrCueCatV = null;

				// obtiene los recAtrCue para DReI
				RecAtrCue dreAtrCueReg = RecAtrCue.getById(RecAtrCue.ID_REGIMEN_DREI);
				RecAtrCue dreAtrCueCum = RecAtrCue.getById(RecAtrCue.ID_CUMUR_DREI);
				RecAtrCue dreAtrCueCat = RecAtrCue.getById(RecAtrCue.ID_CATEGORIARS_DREI);
				RecAtrCue dreAtrCuePer = RecAtrCue.getById(RecAtrCue.ID_PERIODOINI_DREI);
				
				// obtiene los recAtrCue para Etur
				RecAtrCue etuAtrCueReg = RecAtrCue.getById(RecAtrCue.ID_REGIMEN_ETUR);
				RecAtrCue etuAtrCueCum = RecAtrCue.getById(RecAtrCue.ID_CUMUR_ETUR);
				RecAtrCue etuAtrCueCat = RecAtrCue.getById(RecAtrCue.ID_CATEGORIARS_ETUR);
				RecAtrCue etuAtrCuePer = RecAtrCue.getById(RecAtrCue.ID_PERIODOINI_ETUR);

				// REGIMEN
				// si es un alta, crea el regimen simplificado
				// si es una baja, setea el regimen general
				// si es modificacion o recategorizacion, no lo toca
					
				if (tipoTramite.getId().longValue()==TipoTramiteRS.ID_TRAMITE_BAJA.longValue()) {

					// si es una baja, setea el regimen general.
					if (dreAtrCueRegVActual != null){
						dreAtrCueRegVActual.setValor(Cuenta.VALOR_REGIMEN_GENERAL);
						PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCueRegVActual);
					}
					
				} else {
					
					// si existe el atrcueregvactual lo modifica
					log.debug("WS** graba regimen");
					if (dreAtrCueRegVActual != null) {
						dreAtrCueRegVActual.setValor(Cuenta.VALOR_REGIMEN_SIMPLIFICADO);
						PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCueRegVActual);
						
					} else {
						log.debug("WS** graba el regimen simplificado. cuenta=" + cuenta.getNumeroCuenta() );
						log.debug("WS** graba el regimen simplificado. dreatrCue=" + dreAtrCueReg.getId() );
						log.debug("WS** graba el regimen simplificado. valor=" + Cuenta.VALOR_REGIMEN_SIMPLIFICADO );
						
						// no esta el atributo (porque no se migro...)
						// setea el regimen simplificado
						atrCueRegV=new RecAtrCueV();
						atrCueRegV.setCuenta(cuenta);
						atrCueRegV.setFechaDesde(fechaInicio);
						atrCueRegV.setRecAtrCue(dreAtrCueReg);
						atrCueRegV.setValor(Cuenta.VALOR_REGIMEN_SIMPLIFICADO);
						PadDAOFactory.getRecAtrCueVDAO().update(atrCueRegV);
					}
				}
				
				log.debug("WS** verifica si corresponde categoria");
				// CATEGORIA
				// si no tiene categoria o si cambio por modificacion o recategorizacion
				if (dreAtrCueCatVActual == null || !dreAtrCueCatVActual.getValor().equals(categoriaRS.getNroCategoria().toString()) ) {
				
					log.debug("WS** graba categoria");
					
					if (dreAtrCueCatVActual !=null ) {
						dreAtrCueCatVActual.setValor(categoriaRS.getNroCategoria().toString());
						PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCueCatVActual);
						
					} else {
						// nueva categoria
						atrCueCatV = new RecAtrCueV();
						atrCueCatV.setCuenta(cuenta);
						atrCueCatV.setFechaDesde(fechaInicio);
						atrCueCatV.setRecAtrCue(dreAtrCueCat);
						atrCueCatV.setValor(categoriaRS.getNroCategoria().toString());
						PadDAOFactory.getRecAtrCueVDAO().update(atrCueCatV);
					}
				}

				log.debug("WS** verifica si corresponde CUMUR");
				// CUMUR
				if (dreAtrCueCumVActual == null || !dreAtrCueCumVActual.getValor().equals(categoriaRS.getCuna() ) ) {
					
					log.debug("WS** graba cumur");
					
					if (dreAtrCueCumVActual != null ) {
						dreAtrCueCumVActual .setValor(categoriaRS.getCuna());
						PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCueCumVActual);
					} else {
						// nuevo cumur
						atrCueCumV = new RecAtrCueV();
						atrCueCumV.setCuenta(cuenta);
						atrCueCumV.setFechaDesde(fechaInicio);
						atrCueCumV.setRecAtrCue(dreAtrCueCum);
						atrCueCumV.setValor(categoriaRS.getCuna());
						PadDAOFactory.getRecAtrCueVDAO().update(atrCueCumV);
					}
				}

				String perIni = "";
				log.debug("WS** verifica si corresponde periodo");

				// PERIODO INICIO
				if (dreAtrCuePerVActual== null || !dreAtrCuePerVActual.getValor().equals(perIni ) ) {
				
					log.debug("WS** graba periodo");

					
					String strMes="";
					if (mesInicio<10)
						strMes = "0" + mesInicio.toString();
					else
						strMes=mesInicio.toString();
					
					
					perIni= strMes + "/" + anioInicio.toString();
					
					if (dreAtrCuePerVActual != null ) {
						dreAtrCuePerVActual.setValor(perIni);
						PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCuePerVActual);
					
					} else {
						// nuevo cumur
						atrCueCumV = new RecAtrCueV();
						atrCueCumV.setCuenta(cuenta);
						atrCueCumV.setFechaDesde(fechaInicio);
						atrCueCumV.setRecAtrCue(dreAtrCuePer);
						atrCueCumV.setValor(perIni);
						PadDAOFactory.getRecAtrCueVDAO().update(atrCueCumV);
					}
				}

				
				log.debug("WS** fin de actualizacion de datos de la cuenta drei");
				
				// Etur
				// Verifica si el comercio posee cuenta de ETuR

				Double importeEtur = cuna.getImporteEtur();
				
				log.debug("Cuenta Etur nula: " + (cuentaEtur == null));
				log.debug("IMPORTE ETUR: "+importeEtur);
				if (cuentaEtur != null)log.debug("NRO CUENTA ETUR: " + cuentaEtur.getNumeroCuenta());

				// Si no existia pero declara importe Etur: creo una cuenta con identificaci�n de Declarada por el contribuyente
				if (cuentaEtur == null && importeEtur > 0){
					// Creo la cuenta
					cuentaEtur = new Cuenta();
					cuentaEtur.setRecurso(etur);
					cuentaEtur.setObjImp(cuenta.getObjImp());
					cuentaEtur.setNumeroCuenta(StringUtil.formatNumeroCuenta(cuenta.getNumeroCuenta()));
					cuentaEtur.setBroche(cuenta.getBroche());
					cuentaEtur.setCodGesCue(cuenta.getCodGesCue());
					cuentaEtur.setCuitTitPri(cuenta.getCuitTitPri());
					cuentaEtur.setDesDomEnv(cuenta.getDesDomEnv());
					cuentaEtur.setDomicilioEnvio(cuenta.getDomicilioEnvio());
					cuentaEtur.setFechaAlta(fechaInicio);
					cuentaEtur.setNomTitPri(cuenta.getNomTitPri());
					
					cuentaEtur.setPermiteEmision(SiNo.SI.getId());
					cuentaEtur.setPermiteImpresion(SiNo.SI.getId());
					cuentaEtur.setEstCue(EstCue.getById(EstCue.ID_ALTA_CUENTA_DECLARADA));
					
					PadCuentaManager.getInstance().createCuenta(cuentaEtur);
					
					if (cuentaEtur.hasErrorRecoverable()){
						categoriaRS.setCodError(MRCategoriaRS.ERR_CUENTAETUR);
						categoriaRS.addDetalleError(cuentaEtur.errorString());
						
						tx.rollback();
						//cuentaEtur.passErrorMessages(categoriaRS);
						log.debug("WS*** ERROR CUENTA ETUR:" + cuentaEtur.errorString());
						return categoriaRS;
					}
						
					session.flush();
					
					log.debug("ID CUENTA ETUR CREADA: "+ cuentaEtur.getId());
					//Genero los titulares de la cuenta
					//Date fechaDesde;
					for (CuentaTitular cuentaTitular : cuenta.getListCuentaTitularVigentes(fechaInicio)){
						CuentaTitular cueTit = new CuentaTitular();
						cueTit.setCuenta(cuentaEtur);
						cueTit.setContribuyente(cuentaTitular.getContribuyente());
						cueTit.setEsTitularPrincipal(cuentaTitular.getEsTitularPrincipal());
						fechaDesde = cuentaTitular.getFechaDesde();
						if (DateUtil.isDateBefore(fechaDesde, fechaInicio))
							fechaDesde = fechaInicio;
						cueTit.setFechaDesde(fechaDesde);
						cueTit.setFechaHasta(cuentaTitular.getFechaHasta());
						cueTit.setFechaNovedad(new Date());
						cueTit.setIdContribuyente(cuentaTitular.getIdContribuyente());
						cueTit.setEsAltaManual(cuentaTitular.getEsAltaManual());
						cueTit.setTipoTitular(cuentaTitular.getTipoTitular());
						PadDAOFactory.getCuentaTitularDAO().update(cueTit);
					}
						
				}
				session.flush();
				
				log.debug("WS** fin de la creacion de la cuenta etur");

				if (cuentaEtur != null) {
					// aqui replica la misma logica para la cuenta Etur

					// obtiene los atributos actuales de la cuenta de etur para usarlos mas adelante
					RecAtrCueV etuAtrCueRegVActual = cuentaEtur.getRegimenDreiVigente(fechaFuturo);
					RecAtrCueV etuAtrCueCumVActual = cuentaEtur.getValorCumurVigente(fechaFuturo);
					RecAtrCueV etuAtrCueCatVActual = cuentaEtur.getValorCategoriaRSVigente(fechaFuturo);
					RecAtrCueV etuAtrCuePerVActual = cuentaEtur.getValorPerIniRSVigente(fechaFuturo);
					

					// REGIMEN
					if (tipoTramite.getId().longValue()==TipoTramiteRS.ID_TRAMITE_BAJA.longValue()) {

						// si es una baja, setea el regimen general.
						if (etuAtrCueRegVActual != null){
							etuAtrCueRegVActual.setValor(Cuenta.VALOR_REGIMEN_GENERAL);
							PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCueRegVActual);
						}
						
					} else {
						
						// si existe el atrcueregvactual lo modifica
						if (etuAtrCueRegVActual != null) {
							etuAtrCueRegVActual.setValor(Cuenta.VALOR_REGIMEN_SIMPLIFICADO);
							PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCueRegVActual);
							
						} else {
							// no esta el atributo (porque no se migro...)
							// setea el regimen simplificado
							atrCueRegV=new RecAtrCueV();
							atrCueRegV.setCuenta(cuentaEtur);
							atrCueRegV.setFechaDesde(fechaInicio);
							atrCueRegV.setRecAtrCue(etuAtrCueReg);
							atrCueRegV.setValor(Cuenta.VALOR_REGIMEN_SIMPLIFICADO);
							PadDAOFactory.getRecAtrCueVDAO().update(atrCueRegV);
						}
					}
					
					// CATEGORIA
					// si no tiene categoria o si cambio por modificacion o recategorizacion
					if (etuAtrCueCatVActual == null || !etuAtrCueCatVActual.getValor().equals(categoriaRS.getNroCategoria().toString()) ) {
						
						if (etuAtrCueCatVActual !=null ) {
							etuAtrCueCatVActual.setValor(categoriaRS.getNroCategoria().toString());
							PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCueCatVActual);
							
						} else {
							// nueva categoria
							atrCueCatV = new RecAtrCueV();
							atrCueCatV.setCuenta(cuentaEtur);
							atrCueCatV.setFechaDesde(fechaInicio);
							atrCueCatV.setRecAtrCue(etuAtrCueCat);
							atrCueCatV.setValor(categoriaRS.getNroCategoria().toString());
							PadDAOFactory.getRecAtrCueVDAO().update(atrCueCatV);
						}
					}

					// CUMUR
					if (etuAtrCueCumVActual == null || !etuAtrCueCumVActual.getValor().equals(categoriaRS.getCuna() ) ) {
						
						if (etuAtrCueCumVActual != null ) {
							etuAtrCueCumVActual .setValor(categoriaRS.getCuna());
							PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCueCumVActual);
						} else {
							// nuevo cumur
							atrCueCumV = new RecAtrCueV();
							atrCueCumV.setCuenta(cuentaEtur);
							atrCueCumV.setFechaDesde(fechaInicio);
							atrCueCumV.setRecAtrCue(etuAtrCueCum);
							atrCueCumV.setValor(categoriaRS.getCuna());
							PadDAOFactory.getRecAtrCueVDAO().update(atrCueCumV);
						}
					}

					//String perIni = mesInicio.toString() + "/" + anioInicio.toString();
					
					// PERIODO INICIO
					if (etuAtrCuePerVActual== null || !etuAtrCuePerVActual.getValor().equals(perIni ) ) {
						
						if (etuAtrCuePerVActual != null ) {
							etuAtrCuePerVActual.setValor(perIni);
							PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCuePerVActual);
						} else {
							// nuevo periodo
							atrCueCumV = new RecAtrCueV();
							atrCueCumV.setCuenta(cuentaEtur);
							atrCueCumV.setFechaDesde(fechaInicio);
							atrCueCumV.setRecAtrCue(etuAtrCuePer);
							atrCueCumV.setValor(perIni);
							PadDAOFactory.getRecAtrCueVDAO().update(atrCueCumV);
						}
					}
					
				}

				// ******** GRABA LA NOVEDAD ************ //
				log.debug("WS** graba la novedad");

				// levanto la Categoria
				CatRSDrei catRSDrei = CatRSDrei.getCatByNro(cuna.getNroCategoria());

				//Creo la novedadRS
				NovedadRS novedadRS = new NovedadRS();
				
				novedadRS.setTipoUsuario(tipoUsuario);
				novedadRS.setTipoTramiteRS(tipoTramite);
				novedadRS.setCatRSDRei(catRSDrei);
				novedadRS.setCuentaDRei(cuenta);
				novedadRS.setCuentaEtur(cuentaEtur);
				novedadRS.setFechaTransaccion(new Date());
				novedadRS.setTipoTransaccion(tipoTransaccion);
				novedadRS.setUsrCliente(usuario);
				novedadRS.setCuit(cuit);
				novedadRS.setDesCont(desCont);
				novedadRS.setDomLocal(domicilioLocal);
				novedadRS.setTelefono(telefono);
				novedadRS.setEmail(email);
				novedadRS.setTipoCont(tipoContribuyente);
				novedadRS.setIsib(isib);
				novedadRS.setNroCuenta(nroCuenta);
				novedadRS.setListActividades(listActividades);
				novedadRS.setMesInicio(mesInicio);
				novedadRS.setAnioInicio(anioInicio);
				novedadRS.setPrecioUnitario(precioUnitario);
				novedadRS.setCanPer(canPer);
				novedadRS.setIngBruAnu(ingBruAnu);
				novedadRS.setSupAfe(supAfe);
				novedadRS.setPublicidad(publicidad);
				novedadRS.setRedHabSoc(redHabSoc);
				novedadRS.setAdicEtur(adicEtur);
				novedadRS.setNroCategoria(catRSDrei.getNroCategoria());
				novedadRS.setImporteDrei(categoriaRS.getImporteDrei());
				novedadRS.setImporteEtur(categoriaRS.getImporteEtur());
				novedadRS.setImporteAdicional(categoriaRS.getImporteAdicional());
				novedadRS.setDesCategoria(categoriaRS.getDesCategoria());
				novedadRS.setDesPublicidad(categoriaRS.getDesPublicidad());
				novedadRS.setDesEtur(categoriaRS.getDesEtur());
				novedadRS.setCuna(categoriaRS.getCuna());
				novedadRS.setCodBar(categoriaRS.getCodBar());
				novedadRS.setCodBarComprimido(categoriaRS.getCodBarComprimido());
				

				// Si es un tramite de Recategorizacion o Modificacion se verifica que el cumur cambie. Sino se guarda la novedad con otro estado 
				if (tipoTramite.getId().longValue() == TipoTramiteRS.ID_TRAMITE_RECATEGORIZACION.longValue() || tipoTramite.getId().longValue() == TipoTramiteRS.ID_TRAMITE_MODIFICACION.longValue()) {
					// obtiene un Cuna actual
					
					String perActual = perVActualParaValidacion;
					Integer mesActual = null;
					Integer anioActual = null;
					try{mesActual = Integer.valueOf(perActual.substring(0, 2));}catch (Exception e) {	}
					try{anioActual = Integer.valueOf(perActual.substring(3, 7));}catch (Exception e) {	}
					Date fechaActual = null;
					if(mesActual != null && anioActual != null)
						fechaActual = DateUtil.getFirstDatOfMonth(mesActual, anioActual);
					fechaInicio = DateUtil.getFirstDatOfMonth(mesInicio, anioInicio);
					// Comparamos el CUMUR nuevo (tomado de categoriaRS.getCuna()) contra el CUMUR anterior grabado al obtener los parametros actuales
					if (categoriaRS.getCuna().equals(cumurVActualParaValidacion) && fechaInicio != null && fechaActual != null && DateUtil.isDateAfterOrEqual(fechaInicio, fechaActual)) {
						novedadRS.setEstado(NovedadRS.REGISTRADO_NO_PROCESAR);
					}else{
						novedadRS.setEstado(NovedadRS.REGISTRADO);
					}
				}else{
					// registrado
					novedadRS.setEstado(NovedadRS.REGISTRADO);					
				}
				if(novedadRS.getEstado().intValue() == NovedadRS.REGISTRADO_NO_PROCESAR.intValue())
					novedadRS.setMsgDeuda("Modificaci�n que no incide en la deuda");
				else
					novedadRS.setMsgDeuda("Deuda existente sin intervenir ni emitida");
				RecDAOFactory.getNovedadRSDAO().update(novedadRS);
				
				
				session.flush();
				
				// actualiza el idTramite y la fecha de transaccion
				categoriaRS.setIdTramite(novedadRS.getId());
				categoriaRS.setFechaTransaccion(new Date());
				
				log.debug("WS** grabo la novedad y sale");
				// cierra la transaccion
				tx.commit();

			}	


			
			return categoriaRS;
				
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e);
			
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}


	private MRCategoriaRS procesarBaja(MRCategoriaRS categoriaRS, String tipoTransaccion, String usuario, Integer tipoUsuario, String cuit, String desCont, 
			Integer tipoContribuyente, String nroCuenta, Integer mesBaja, Integer anioBaja, Integer motivoBaja, boolean confirmado ) throws Exception {
		
		Session session=null;
		Transaction tx =null;

		UserContext userContext = new UserContext();
		userContext.setUserName("siat");
		userContext.setIdUsuarioSiat(UsuarioSiat.ID_USUARIO_SIAT); 
		DemodaUtil.setCurrentUserContext(userContext);

		// aqui vamos a guardar la fecha del ultimo dia del mes
		Date fechaInicio;

		String desBaja="";
		
		try {
			session=SiatHibernateUtil.currentSession();
			tx= session.beginTransaction();

			
			Recurso recurso = Recurso.getDReI();
			Cuenta cuenta;
			cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso.getId(), nroCuenta);
			

			// ** valida que el periodo sea mayor al inicio del regimen
		//	if (!( mesBaja.intValue() * 100 +  anioBaja.intValue() >= (SiatParam.getInteger(SiatParam.MES_INICIO_RS)*100 + SiatParam.getInteger(SiatParam.ANIO_INICIO_RS)) )) {
			if (!( anioBaja.intValue() * 100 +  mesBaja.intValue() >= (SiatParam.getInteger(SiatParam.ANIO_INICIO_RS)*100 + SiatParam.getInteger(SiatParam.MES_INICIO_RS)) )) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_PERIODOINCORRECTO);
				categoriaRS.addDetalleError("Periodo/Anio de baja. Debe ser posterior al per�odo de entrada en vigencia del r�gimen");
				return categoriaRS;

			}

			// ** valida que el periodo sea menor o igual al periodo actual
			if (!( anioBaja.intValue() * 100 +  mesBaja.intValue() <= DateUtil.getAnio(new Date()) * 100 +  DateUtil.getMes(new Date()) ))  {
				
				// pero si ahora estamos en septiembre, lo dejamos siempre y cuando haga un alta para octubre
				if (! ( (anioBaja.intValue() * 100 +  mesBaja.intValue())==200910 && 
						DateUtil.getAnio(new Date()) * 100 +  DateUtil.getMes(new Date()) == 200909)) {
				
					categoriaRS.setCodError(MRCategoriaRS.ERR_PERIODOINCORRECTO);
					categoriaRS.addDetalleError("Periodo/Anio incorrectos. Debe ser menor o igual al per�odo actual");
					return categoriaRS;
				}

			}
			
			// calcula la fecha de inicio como primer dia del periodo/anio
			fechaInicio = DateUtil.getFirstDatOfMonth(mesBaja, anioBaja);
			
			
			// ** verifica algunos tipos 
			//    tipo de tramite
			TipoTramiteRS tipoTramite = TipoTramiteRS.getByDes(tipoTransaccion);
			if (tipoTramite==null) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El tipo de tramite es incorrecto");
				return categoriaRS;
			}
			
			//    tipo de usuario
			if (! (tipoUsuario.intValue()==1 || tipoUsuario.intValue()==2 )) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El tipo de usuario es incorrecto");
				return categoriaRS;
			}
			
			//    tipo de contribuyente
			if (! (tipoContribuyente.intValue()==1 || tipoContribuyente.intValue()==2 )) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El Tipo de Contribuyente es incorrecto");
				return categoriaRS;
			}
			
			// ** verifica el  motivo de baja
			if (motivoBaja.intValue()==1) {
				desBaja="El presente tr�mite no significa la baja de la cuenta. Para la misma debe dirigirse al Centro Municipal de Dtristito correspondiente";
				categoriaRS.setDesBaja(desBaja);
				
			} else if (motivoBaja.intValue()==2) {
				desBaja="El presente tr�mite no significa la baja de la cuenta. A partir del pr�ximo per�odo debe comenzar a tributar mediante sistema SIAP, M�dulo DReI/Etur Municipalidad de Rosario";
				categoriaRS.setDesBaja(desBaja);
				
			} else {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("Motivo de baja incorrecto");
				return categoriaRS;
				
			}
			
			// ** valida que exista la cuenta 
			if (cuenta == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_NOEXISTECUENTA);
				categoriaRS.addDetalleError("La cuenta no existe en los registros de la Municipalidad de Rosario");
				return categoriaRS;
			}


			// genero una fecha muy a futuro para levantar los atributos
			Date fechaFuturo = DateUtil.getDate(2050, 01, 01);
			
			// obtiene los atributos actuales de la cuenta de DRei para usarlos mas adelante
			RecAtrCueV dreAtrCueRegVActual = cuenta.getRegimenDreiVigente(fechaFuturo);
			RecAtrCueV dreAtrCueCumVActual = cuenta.getValorCumurVigente(fechaFuturo);
			RecAtrCueV dreAtrCueCatVActual = cuenta.getValorCategoriaRSVigente(fechaFuturo);
			RecAtrCueV dreAtrCuePerVActual = cuenta.getValorPerIniRSVigente(fechaFuturo);
			
			log.debug("ATRIBUTO REGIMEN: "+ (dreAtrCueRegVActual!=null));
			if (dreAtrCueRegVActual != null) 
				log.debug("VALOR REGIMEN: "+dreAtrCueRegVActual.getValor());

				
			// ** verifica que la cuenta este con alta en regimen simplificado
			if (dreAtrCueRegVActual == null || dreAtrCueRegVActual.getValor().equals(Cuenta.VALOR_REGIMEN_GENERAL)) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_MDF_NOTIENEALTA);
				categoriaRS.addDetalleError("La cuenta no tiene alta en el r�gimen simplificado.");
			}
			

			// ** para el caso de Cese, verifica que tenga un inicio de cierre con cierre.periodoanio <= baja.periodoanio
			// ** valida que el comercio no tenga inicio de cierre

			if (motivoBaja.intValue()==1) {
				// Cese de actividades
				CierreComercio cierreComercio = CierreComercio.getByObjImp(cuenta.getObjImp());
				if (cierreComercio ==null) {
					categoriaRS.setCodError(MRCategoriaRS.ERR_BAJ_CIERRECOMERCIO);
					categoriaRS.addDetalleError("Para realizar una baja por Cese de activades debe realizar previamente el tr�mite de inicio de cierre de Comercio");
					
				} else {
					int periodoCese = DateUtil.getMes(cierreComercio.getFechaCeseActividad()) * 100 + DateUtil.getAnio(cierreComercio.getFechaCeseActividad());
					int periodoIngresado = mesBaja * 100 + anioBaja;
					//
					if ( periodoCese > periodoIngresado ) {
						categoriaRS.setCodError(MRCategoriaRS.ERR_BAJ_CIERRECOMERCIO);
						categoriaRS.addDetalleError("El periodo de baja debe ser mayor que la fecha de cese de actividades informada en el Inicio de Cierre de Comercio");
					}
				}
			}
			
			
			// **************************
			// aqui pasaron todas las validaciones generales....
			// **************************

			//
			Recurso etur = Recurso.getETur();
			Cuenta cuentaEtur = PadDAOFactory.getCuentaDAO().getCuentaActivaByObjImpYRecursoYFecha(cuenta.getObjImp(), etur, fechaInicio);
			
			if (categoriaRS.getCodError() != 0 )
				return categoriaRS;
			
			// IF CONFIRMADO -> REALIZA LA ACTUALIZACION EN LA CUENTA Y GENERA LA NOVEDAD //
			if (confirmado) {

				// REGIMEN: setea el regimen general
				if (dreAtrCueRegVActual != null){
					dreAtrCueRegVActual.setValor(Cuenta.VALOR_REGIMEN_GENERAL);
					PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCueRegVActual);
				}
					
				// categoria: setea la baja
				if (dreAtrCueCatVActual !=null ) {
					dreAtrCueCatVActual.setValor("-");
					PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCueCatVActual);
				}

				// cumur: setea la baja
				if (dreAtrCueCumVActual != null ) {
					dreAtrCueCumVActual.setValor("-");
					PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCueCumVActual);
				}
				
				// periodoalta
				if (dreAtrCuePerVActual != null ) {
					dreAtrCuePerVActual.setValor("-");
					PadDAOFactory.getRecAtrCueVDAO().update(dreAtrCuePerVActual);
				}
				
				// ** Etur
				if (cuentaEtur != null) {

					// obtiene los atributos actuales de la cuenta de etur para usarlos mas adelante
					RecAtrCueV etuAtrCueRegVActual = cuentaEtur.getRegimenDreiVigente(fechaFuturo);
					RecAtrCueV etuAtrCueCumVActual = cuentaEtur.getValorCumurVigente(fechaFuturo);
					RecAtrCueV etuAtrCueCatVActual = cuentaEtur.getValorCategoriaRSVigente(fechaFuturo);
					RecAtrCueV etuAtrCuePerVActual = cuentaEtur.getValorPerIniRSVigente(fechaFuturo);
					
					// REGIMEN: setea el regimen general
					if (etuAtrCueRegVActual != null){
						etuAtrCueRegVActual.setValor(Cuenta.VALOR_REGIMEN_GENERAL);
						PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCueRegVActual);
					}
						
					// categoria: setea la baja
					if (etuAtrCueCatVActual!=null ) {
						etuAtrCueCatVActual.setValor("-");
						PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCueCatVActual);
					}

					// cumur: setea la baja
					if (etuAtrCueCumVActual!= null ) {
						etuAtrCueCumVActual.setValor("-");
						PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCueCumVActual);
					}
					
					// periodoalta
					if (etuAtrCuePerVActual != null ) {
						etuAtrCuePerVActual.setValor("-");
						PadDAOFactory.getRecAtrCueVDAO().update(etuAtrCuePerVActual);
					}

					
				}

				// ******** GRABA LA NOVEDAD ************ //

				NovedadRS novedadRS = new NovedadRS();

				novedadRS.setTipoTransaccion(tipoTransaccion);
				novedadRS.setUsrCliente(usuario);
				novedadRS.setTipoUsuario(tipoUsuario);
				novedadRS.setCuit(cuit);
				novedadRS.setDesCont(desCont);
				novedadRS.setTipoCont(tipoContribuyente);
				novedadRS.setNroCuenta(nroCuenta);

				novedadRS.setMesBaja(mesBaja);
				novedadRS.setAnioBaja(anioBaja);
				novedadRS.setMotivoBaja(motivoBaja);
				
				
				novedadRS.setTipoTramiteRS(tipoTramite);
				novedadRS.setCuentaDRei(cuenta);
				novedadRS.setCuentaEtur(cuentaEtur);
				
				novedadRS.setFechaTransaccion(new Date());
				novedadRS.setDesBaja(desBaja);

				// registrado
				novedadRS.setEstado(NovedadRS.REGISTRADO);
				novedadRS.setMsgDeuda("Deuda existente sin intervenir ni emitida");
				RecDAOFactory.getNovedadRSDAO().update(novedadRS);
				
				
				session.flush();
				
				// actualiza el idTramite y la fecha de transaccion
				categoriaRS.setIdTramite(novedadRS.getId());
				categoriaRS.setFechaTransaccion(new Date());
				
			
			}
			tx.commit();
			
			
			return categoriaRS;
				
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			if (tx!=null)tx.rollback();
			throw new DemodaServiceException(e);
			
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
	}

	
	
	private MRCategoriaRS procesarListarTramites(MRCategoriaRS categoriaRS, String tipoTransaccion, String usuario, Integer tipoUsuario, String cuit) throws Exception {
		
		Session session=null;

		try {
			session=SiatHibernateUtil.currentSession();
			
			// ** verifica algunos tipos 
			// tipo de tramite
			TipoTramiteRS tipoTramite = TipoTramiteRS.getByDes(tipoTransaccion);
			if (tipoTramite==null) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El tipo de tramite es incorrecto");
				return categoriaRS;
			}
			
			// tipo de usuario
			if (! (tipoUsuario.intValue()==1 || tipoUsuario.intValue()==2 )) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El tipo de usuario es incorrecto");
				return categoriaRS;
			}

			// lista de novedades
			List<NovedadRS> listNovedadRS = NovedadRS.getListTramitesByCuit(cuit);
			if (listNovedadRS == null || listNovedadRS.size()==0) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_LST_NOHAYDATOS);
				categoriaRS.addDetalleError("No hay tramites para el cuit especificado");
				return categoriaRS;
			}
			
			log.debug("WS** - lista de tramites recuperados: " + listNovedadRS.size());
			// devuelva la lista de tramites en la categoria
			categoriaRS.setListNovedadRS(ListUtilBean.toVO(listNovedadRS,1));
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new DemodaServiceException(e);
			
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
		return categoriaRS;
	}

	
	private MRCategoriaRS procesarVerTramite(MRCategoriaRS categoriaRS, String tipoTransaccion, String usuario, Integer tipoUsuario, Long idTramite) throws Exception {
		Session session=null;

		try {
			session=SiatHibernateUtil.currentSession();
			
			// ** verifica algunos tipos 
			// tipo de tramite
			TipoTramiteRS tipoTramite = TipoTramiteRS.getByDes(tipoTransaccion);
			if (tipoTramite==null) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El tipo de tramite es incorrecto");
				return categoriaRS;
			}
			
			// tipo de usuario
			if (! (tipoUsuario.intValue()==1 || tipoUsuario.intValue()==2 )) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_TIPOINCORRECTO);
				categoriaRS.addDetalleError("El tipo de usuario es incorrecto");
				return categoriaRS;
			}

			// lista de novedades
			NovedadRS novedadRS= NovedadRS.getById(idTramite);
			if (novedadRS == null ) {
				categoriaRS.setCodError(MRCategoriaRS.ERR_LST_NOHAYDATOS);
				categoriaRS.addDetalleError("No hay tramites para el idTramite especificado");
				return categoriaRS;
			}
			
			List<NovedadRS> listNovedadRS = new ArrayList<NovedadRS>();
			listNovedadRS.add(novedadRS);
			
			// devuelva la lista de tramites en la categoria
			categoriaRS.setListNovedadRS(ListUtilBean.toVO(listNovedadRS,1));
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new DemodaServiceException(e);
			
		}finally{
			SiatHibernateUtil.closeSession();
		}
		
		return categoriaRS;

	}
	
		
		
	private MRCategoriaRS validarNulos(MRCategoriaRS categoriaRS, String tipoTransaccion, String usuario, Integer tipoUsuario, String cuit, String desCont, 
			Integer tipoContribuyente, String isib, String nroCuenta, String listActividades, 
			Integer mesInicio, Integer anioInicio, Double ingBruAnu, Double supAfe, Double publicidad,  Double redHabSoc, Integer adicEtur, 
			Double precioUnitario, Integer canPer, boolean confirmado, String domicilioLocal, String telefono, String email, Integer mesBaja, 
			Integer anioBaja, Integer motivoBaja, Long idTramite  ) {

		// ** verificacion de datos nulos
		if (tipoTransaccion == null) {
			categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
			categoriaRS.addDetalleError("tipoTransaccion es Nulo");
			return categoriaRS;		
		}
		if (usuario== null){
			categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
			categoriaRS.addDetalleError("usuario es Nulo");
			return categoriaRS;
		}
		if (tipoUsuario== null){
			categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
			categoriaRS.addDetalleError("tipoUsuario es Nulo");
			return categoriaRS;
		}
		
		
		if (tipoTransaccion.equals("ADHESION") || tipoTransaccion.equals("MODIFICACION") || tipoTransaccion.equals("RECATEGORIZACION")) {
			
			if (cuit == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("cuit es Nulo");
				return categoriaRS;
			} else {
				if (!StringUtil.isNumeric(cuit)) {
					categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
					categoriaRS.addDetalleError("La CUIT debe ser ingresada sin guiones");
					return categoriaRS;
					
				} else {
					if (cuit.length()!=11) {
						categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
						categoriaRS.addDetalleError("La CUIT debe ser de 11 caracteres");
						return categoriaRS;
					}
				}
			}
			
			
			if (desCont == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa la descripci�n del Contribuyente");
				return categoriaRS;
			}
			if (domicilioLocal== null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informan el domicilio del Local");
				return categoriaRS;
			}
			if (tipoContribuyente == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el tipo de contribuyente");
				return categoriaRS;
			}
			if (nroCuenta == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el n�mero de cuenta");
				return categoriaRS;
			}
			if (listActividades== null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa la lista de Actividades");
				return categoriaRS;
			}
			if (mesInicio == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el mes de inicio");
				return categoriaRS;
			}
			if (anioInicio == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el a�o de inicio");
				return categoriaRS;
			}
			if (precioUnitario== null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el precio Unitario");
				return categoriaRS;
			}
			if (canPer == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa la cantidad de personal");
				return categoriaRS;
			}
			if (ingBruAnu == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el ingreso bruto anual");
				return categoriaRS;
			}
			if (supAfe == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa la superficie afectada");
				return categoriaRS;
			}
			if (publicidad== null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa datos de publicidad");
				return categoriaRS;
			}
			if (adicEtur == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el adicional Etur");
				return categoriaRS;
			}
		

		} else if (tipoTransaccion.equals("BAJA")) {
			
			if (cuit == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa la CUIT");
				return categoriaRS;
			} else {
				if (!StringUtil.isNumeric(cuit)) {
					categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
					categoriaRS.addDetalleError("La CUIT debe ser ingresada sin guiones");
					return categoriaRS;
					
				}
			}

			if (desCont == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa la descripci�n del contribuyente");
				return categoriaRS;
			}
			if (tipoContribuyente == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el tipo de contribuyente");
				return categoriaRS;
			}
			if (nroCuenta == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el n�mero de cuentra");
				return categoriaRS;
			}

			if (mesBaja == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el mes de baja");
				return categoriaRS;
			}

			if (anioBaja == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el a�o de baja");
				return categoriaRS;
			}
			
			if (motivoBaja == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el motivo de baja");
				return categoriaRS;
			}

		} else if (tipoTransaccion.equals("LISTAR-TRAMITES") ) {
		
			if (cuit == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa la CUIT");
				return categoriaRS;
			} else {
				if (!StringUtil.isNumeric(cuit)) {
					categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
					categoriaRS.addDetalleError("La CUIT debe ser ingresada sin guiones");
					return categoriaRS;
					
				}
			}
			
		} else if (tipoTransaccion.equals("VER-TRAMITE") ) {
			
			if (idTramite == null){
				categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
				categoriaRS.addDetalleError("No se informa el idTramite");
				return categoriaRS;
			}
			
		} else {
			categoriaRS.setCodError(MRCategoriaRS.ERR_DATOSENTRADANULOS);
			categoriaRS.addDetalleError("No se identifica el tipo de tr�mite");
			
		}
			
		return categoriaRS;
		
	}
	
	
	
}
	
	
