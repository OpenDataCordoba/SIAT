//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class PadError extends DemodaError {
	//	 Use Codigos desde 13000 hasta 13999
    // static public String XXXXXX_XXXX_XXX   = addError(13000, "pad.xxxxxx");


	//	 ---> Calle
	public static final String CALLE_LABEL = addError(0, "pad.calle.label");	
	public static final String CALLE_CODCALLE = addError(0, "pad.calle.codCalle.label");
	public static final String CALLE_NOMBRECALLE = addError(0, "pad.calle.nombreCalle.label");
	//	 <--- Calle
	
	
	
	public static final String DOMATRVAL_LABEL      = addError(4000,"def.domAtrVal.label");
	public static final String DOMATRVAL_STRVALOR   = addError(4000,"def.domAtrVal.strValor.label");
	public static final String DOMATRVAL_DESVALOR   = addError(4000,"def.domAtrVal.desValor.label"); 
	public static final String DOMATRVAL_FECHADESDE = addError(4000,"def.domAtrVal.fechaDesde.label"); 
	public static final String DOMATRVAL_FECHAHASTA = addError(4000,"def.domAtrVal.fechaHasta.label"); 

	// ---> Persona
	public static final String PERSONA_LABEL           = addError(0, "pad.persona.label");
	public static final String PERSONA_TIPOPERSONA     = addError(0, "pad.persona.tipoPersona.label");
	public static final String PERSONA_APELLIDO        = addError(0, "pad.persona.apellido.label");
	public static final String PERSONA_APELLIDOMATERNO = addError(0, "pad.persona.apellidoMaterno.label");
	public static final String PERSONA_NOMBRES         = addError(0, "pad.persona.nombres.label");
	public static final String PERSONA_SEXO            = addError(0, "pad.persona.sexo.label");
	public static final String PERSONA_FECHANACIMIENTO = addError(0, "pad.persona.fechaNacimiento.label");
	public static final String PERSONA_TELEFONO        = addError(0, "pad.persona.telefono.label");
	public static final String PERSONA_CUIT            = addError(0, "pad.persona.cuit.label");
	public static final String PERSONA_RAZONSOCIAL     = addError(0, "pad.persona.razonSocial.label");
	public static final String CUIT_INVALIDO           = addError(0, "pad.persona.cuit.invalido");
	public static final String PERSONA_FISICA_FILTROS_REQ_BUSQ   = addError(4001, "pad.persona.fisica.filtrosRequeridosDeBusqueda");
	public static final String PERSONA_JURIDICA_FILTROS_REQ_BUSQ = addError(4001, "pad.persona.juridica.filtrosRequeridosDeBusqueda");
	//	 <--- Persona

	// ---> Documento
	public static final String DOCUMENTO_LABEL = addError(0, "pad.documento.label");
	public static final String DOCUMENTO_NUMERO = addError(0, "pad.documento.numero.label");
	public static final String DOCUMENTO_NUMERO_REF = addError(0, "pad.documento.numero.ref");	
	// <--- Documento
	
	 // <--- TIPO DOCUMENTO
	public static final String TIPODOCUMENTO_LABEL = addError(0, "pad.tipoDocumento.label");
	public static final String TIPODOCUMENTO_DESCRIPCION = addError(0, "pad.tipoDocumento.descripcion.label");
	public static final String TIPODOCUMENTO_ABREVIATURA = addError(0, "pad.tipoDocumento.abreviatura.label");	
	 // <--- TIPO DOCUMENTO	
	
	 // ---> DOMICILIO
	public static final String DOMICILIO_LABEL = addError(0, "pad.domicilio.label");
	public static final String DOMICILIO_CODCALLE = addError(0, "pad.domicilio.codCalle.label");
	public static final String DOMICILIO_LETRACALLE = addError(0, "pad.domicilio.letraCalle.label");
	public static final String DOMICILIO_MONOBLOCK = addError(0, "pad.domicilio.monoblock.label");
	public static final String DOMICILIO_NOMCALLE = addError(0, "pad.domicilio.nomCalle.label");
	public static final String DOMICILIO_NUMERO = addError(0, "pad.domicilio.numero.label");
	public static final String DOMICILIO_NUMERO_REF = addError(0, "pad.domicilio.numero.ref");
	public static final String DOMICILIO_PISO = addError(0, "pad.domicilio.piso.label");
	public static final String DOMICILIO_DEPTO = addError(0, "pad.domicilio.depto.label");
	public static final String DOMICILIO_LOCALIDAD = addError(0, "pad.domicilio.localidad.label");
	public static final String DOMICILIO_BIS = addError(0, "pad.domicilio.bis.label");
	public static final String DOMICILIO_REFERENCIAGEOGRAFICA = addError(0, "pad.domicilio.referenciaGeografica.label");	
	public static final String DOMICILIO_CODPOSTAL = addError(0, "pad.domicilio.codPostal.label");
	public static final String DOMICILIO_CODSUBPOSTAL = addError(0, "pad.domicilio.codSubpostal.label");
	public static final String DOMICILIO_NUMEROHASTA = addError(0, "pad.domicilio.numeroHasta.label");
	public static final String DOMICILIO_ESVALIDO = addError(0, "pad.domicilio.esValidado.label");
	
	public static final String DOMICILIO_LOCALIDAD_INEXISTENTE = addError(0, "pad.domicilio.localidadInexistente");
	public static final String DOMICILIO_ALTURA_CALLE_INCORRECTO = addError(0, "pad.domicilio.alturaIncorrecta");
	public static final String DOMICILIO_NOMBRE_CALLE_INCORRECTO = addError(0, "pad.domicilio.nombreCalleIncorrecto");
	public static final String DOMICILIO_NOMBRE_CALLE_NO_ENCONTRADA  = addError(0, "pad.domicilio.calleNoEncontrada");
	public static final String DOMICILIO_LETRA_CALLE_INCORRECTO = addError(0, "pad.domicilio.letraCalleIncorrecta");
	public static final String DOMICILIO_BIS_INCORRECTO = addError(0, "pad.domicilio.bisIncorrecto");
	public static final String DOMICILIO_INVALIDO = addError(0, "pad.domicilio.domicilioInvalido");
	
	public static final String DOMICILIO_VALIDO = addError(0, "pad.domicilio.domicilioValido");
	public static final String CALLE_NOMBRE_CALLE_CTD_CARACT_INCORRECTA = addError(4001, "pad.calle.nombreCalle.ctdCaracteresIncorrecta");
	//	 <--- Domicilio
	
	// ---> Provincia
	public static final String PROVINCIA_DESCRIPCION = addError(0, "pad.provincia.descripcion.label");
	public static final String PROVINCIA_LABEL = addError(0, "pad.provincia.label");
	//	 <--- Provincia

	// ---> Localidad
	public static final String LOCALIDAD_CODPOSTAL = addError(0, "pad.localidad.codPostal.label");
	public static final String LOCALIDAD_CODSUBPOSTAL = addError(0, "pad.localidad.codSubPostal.label");
	public static final String LOCALIDAD_DESCRIPCIONPOSTAL = addError(0, "pad.localidad.descripcionPostal.label");
	public static final String LOCALIDAD_LABEL = addError(0, "pad.localidad.label");
	//	 <--- Localidad
	
	// ---> Objeto Imponible	
	public static final String OBJIMP_LABEL = addError(0, "pad.objImp.label");
	public static final String OBJIMP_CLAVE = addError(0, "pad.objImp.clave.label");
	public static final String OBJIMP_CLAVEFUNCIONAL = addError(0, "pad.objImp.claveFuncional.label");
	public static final String OBJIMP_FECHAALTA = addError(0, "pad.objImp.fechaAlta.label");
	public static final String OBJIMP_FECHABAJA = addError(0, "pad.objImp.fechaBaja.label");
	
	public static final String OBJIMPATRVAL_LABEL = addError(0, "pad.objImpAtrVal.label");
	//	 <--- Objeto Imponible
	
	//	 ---> Contribuyente
	public static final String CONTRIBUYENTE_LABEL       = addError(0, "pad.contribuyente.label");
	public static final String CONTRIBUYENTE_FECHADESDE  = addError(0, "pad.contribuyente.fechaDesde.label");
	public static final String CONTRIBUYENTE_FECHAHASTA  = addError(0, "pad.contribuyente.fechaHasta.label");
	public static final String CONTRIBUYENTE_CASODOMFIS = addError(0, "pad.contribuyente.casoDomFis.label");
	public static final String CONTRIBUYENTE_FECHADESDE_INGBRU=addError(0,"pad.contribuyente.fechaNroIsib.label");
	//	 <--- Contribuyente

	//	 ---> Atributos Valorizados del Contribuyente
	public static final String CONATRVAL_LABEL       = addError(0, "pad.conAtrVal.label");
	//	 <--- Atributos Valorizados del Contribuyente
	
	//	 ---> Cuenta
	public static final String CUENTA_LABEL                    = addError(0, "pad.cuenta.label");
	public static final String CUENTA_PERS_NO_ES_CONTRIB       = addError(0, "pad.cuenta.persNoEsContribuyente");
	public static final String CUENTA_PERS_JURID_NO_ES_CONTRIB = addError(0, "pad.cuenta.persJuridNoEsContribuyente");
	public static final String CUENTA_CONTRIB_NO_ES_TIT_CTA    = addError(0, "pad.cuenta.contribNoEsTitularCuenta");
	public static final String CUENTA_FILTROS_REQ_BUSQ         = addError(0, "pad.cuenta.filtrosRequeridosDeBusqueda");
	public static final String CUENTA_SIN_DOMICILIO_ENVIO      = addError(0, "pad.cuenta.sinDomicilioEnvio");
	public static final String CUENTA_FECHAALTA                = addError(0, "pad.cuenta.fechaAlta.label");
	public static final String CUENTA_FECHABAJA                = addError(0, "pad.cuenta.fechaBaja.label");
	
	public static final String CUENTA_RECURSO               = addError(0, "pad.cuenta.recurso.label");
	public static final String CUENTA_OBJIMP                = addError(0, "pad.cuenta.objImp.label");
	public static final String CUENTA_NUMEROCUENTA          = addError(0, "pad.cuenta.numeroCuenta.label");
	public static final String CUENTA_CAMBIOTITULARIF       = addError(0, "pad.cuenta.cambioTitularIF.label");
	public static final String CUENTA_CAMBIOOBJIMPIF        = addError(0, "pad.cuenta.cambioObjImpIF.label");
	public static final String CUENTA_SIN_TITULARES  	    = addError(0, "pad.cuenta.sinTitulares");
	public static final String CUENTA_CON_TITULARES  	    = addError(0, "pad.cuenta.conTitulares");
	
	public static final String CUENTA_NUMEROCUENTA_REF		= addError(0, "pad.cuenta.numeroCuenta.ref");
	
	public static final String CUENTA_NO_NUMERO_CUENTA_PARA_RECURSO  = addError(0, "pad.cuenta.noNumeroCuentaParaRecurso");
	public static final String CUENTA_EXISTE_NUMERO_CUENTA_PARA_RECURSO  = addError(0, "pad.cuenta.existeNumeroCuentaParaRecurso");
	public static final String CUENTA_NO_ALTACTAMANUAL       = addError(0, "pad.cuenta.noAltaCtaManual.label");
	public static final String CUENTA_NOVIGENTE              = addError(0, "pad.cuenta.noVigente");
	public static final String CUENTA_PRINCIPAL_NOOBJIMP     = addError(0,"pad.cuenta.principalNoObjImp"); //cuenta principal sin objeto imponible
	//	 <--- Cuenta
	
	//	 ---> EstadoCuenta
	public static final String ESTADOCUENTA_LABEL			= addError(0,"pad.estCue.label");
	public static final String ESTADOCUENTA_DESCRIPCION		= addError(0,"pad.estCue.descipcion.label");
	//	 <--- EstadoCuenta

	//	 ---> CuentaTitular
	public static final String CUENTATITULAR_LABEL  	    = addError(0, "pad.cuentaTitular.label");
	public static final String CUENTATITULAR_CUENTA  	    = addError(0, "pad.cuentaTitular.cuenta.label");
	public static final String CUENTATITULAR_CONTRIBUYENTE  = addError(0, "pad.cuentaTitular.contribuyente.label");
	public static final String CUENTATITULAR_TIPO_TITULAR   = addError(0, "pad.cuentaTitular.tipoTitular.label");
	public static final String CUENTATITULAR_FECHA_DESDE    = addError(0, "pad.cuentaTitular.fechaDesde.label");
	public static final String CUENTATITULAR_FECHA_HASTA    = addError(0, "pad.cuentaTitular.fechaHasta.label");
	public static final String CUENTATITULAR_FECHA_NOVEDAD  = addError(0, "pad.cuentaTitular.fechaNovedad.label");
	public static final String CUENTATITULAR_ES_ALTA_MANUAL = addError(0, "pad.cuentaTitular.esAltaManual.label");
	public static final String CUENTATITULAR_TITULAR_DUPLICADO = addError(0, "pad.cuentaTitular.titularesDuplicados");
	public static final String CUENTATITULAR_ES_TITULAR_PRINCIPAL = addError(0, "pad.cuentaTitular.esTitularPrincipal.label");
	public static final String CUENTATITULAR_REGISTRO_CONFLICTO  = addError(0, "pad.cuentaTitular.registroEnConflicto.label");
	
	//	 <--- CuentaTitular	
	
	// ---> CuentaTitular
	public static final String CUENTAREL_LABEL  	    = addError(0, "pad.cuentaRel.label");
	public static final String CUENTAREL_DESTINO_IGUAL_ORIGEN 	 = addError(0, "pad.cuentaRel.origenIgualDestino.error");
	// <--- CuentaTitular
	
	// ---> Tipo Objeto Imponible	
	public static final String TIPOBJIMP_LABEL        = addError(0, "pad.tipObjImp.label");
	public static final String TIPOBJIMP_CODTIPOBJIMP = addError(0, "pad.tipObjImp.codTipObjImp.label");
	public static final String TIPOBJIMP_DESTIPOBJIMP = addError(0, "pad.tipObjImp.desTipObjImp.label");
	public static final String TIPOBJIMP_ESSIAT       = addError(0, "pad.tipObjImp.esSiat.label");
	public static final String TIPOBJIMP_FECHAALTA    = addError(0, "pad.tipObjImp.fechaAlta.label");	
	public static final String TIPOBJIMP_FECHABAJA    = addError(0, "pad.tipObjImp.fechaBaja.label");
	//	 <--- Tipo Objeto Imponible
	
	// ---> Cambiar Domicilio de Envio
	public static final String CAMBIAR_DOM_ENVIO_LABEL  = addError(0, "pad.cambiarDomEnvio.label");

	public static final String CAMBIAR_DOM_ENVIO_NUMEROCUENTA_INVALIDO  = addError(0, "pad.cambiarDomEnvio.numeroCuenta.invalido");
	public static final String CAMBIAR_DOM_ENVIO_CUENTA_INEXISTENTE  = addError(0, "pad.cambiarDomEnvio.cuenta.inexistente");
	public static final String CAMBIAR_DOM_ENVIO_CODGESCUE_INVALIDO  = addError(0, "pad.cambiarDomEnvio.codGesCue.invalido");
	public static final String CAMBIAR_DOM_ENVIO_CODGESCUE_NO_PERT  = addError(0, "pad.cambiarDomEnvio.codGesCue.noPertenece");
	
	public static final String CAMBIAR_DOM_ENVIO_SIN_CUENTAS_SELEC  = addError(0, "pad.cambiarDomEnvio.sinCuentasSeleccionadas");
	public static final String CAMBIAR_DOM_ENVIO_LOCALIDAD_REQUERIDA = addError(0, "pad.cambiarDomEnvioAdapter.localidadRequrida");	
	public static final String CAMBIAR_DOM_ENVIO_CALLE_REQUERIDA = addError(0, "pad.cambiarDomEnvioAdapter.calleRequrida");
	public static final String CAMBIAR_DOM_ENVIO_ALTURA_REQUERIDA = addError(0, "pad.cambiarDomEnvioAdapter.alturaRequrida");
	public static final String CAMBIAR_DOM_ENVIO_CALLE_INEXISTENTE = addError(0, "pad.cambiarDomEnvioAdapter.calleInexistente");
	public static final String CAMBIAR_DOM_ENVIO_CUENTAS_SELECCIONADAS_INACTIVAS = addError(0, "pad.cambiarDomEnvioAdapter.cuentasSeleccionadasInactivas");	
	
	public static final String CAMBIAR_DOM_ENVIO_NOMBRE_REQUERIDO    = addError(0, "pad.cambiarDomEnvioAdapter.nombreRequrido");	
	public static final String CAMBIAR_DOM_ENVIO_APELLIDO_REQUERIDO  = addError(0, "pad.cambiarDomEnvioAdapter.apellidoRequrido");
	public static final String CAMBIAR_DOM_ENVIO_TIPODOC_REQUERIDO   = addError(0, "pad.cambiarDomEnvioAdapter.tipoDocRequrido");
	public static final String CAMBIAR_DOM_ENVIO_NUMERODOC_REQUERIDO = addError(0, "pad.cambiarDomEnvioAdapter.numeroDocRequrido");
	public static final String CAMBIAR_DOM_ENVIO_DOMICILIO_NUEVO     = addError(0, "pad.cambiarDomEnvioAdapter.domicilioNuevo");
	public static final String CAMBIAR_DOM_ENVIO_ES_ORIGEN_WEB       = addError(0, "pad.cambiarDomEnvioAdapter.esOrigenWeb");	

	// <--- Cambiar Domicilio de Envio	
	
	// ---> Repartidor
	public static final String REPARTIDOR_LABEL  	    = addError(13600, "pad.repartidor.label");
	public static final String REPARTIDOR_NROREPARTIDOR  	    = addError(13601, "pad.repartidor.nroRepartidor.label");
	public static final String REPARTIDOR_DESREPARTIDOR  	    = addError(13602, "pad.repartidor.desRepartidor.label");
	public static final String REPARTIDOR_RECURSO  = addError(13603, "def.recurso.label");
	public static final String REPARTIDOR_PERSONA  = addError(13604, "pad.persona.label");
	public static final String REPARTIDOR_LEGAJO  = addError(13605, "pad.repartidor.legajo.label");
	public static final String REPARTIDOR_ZONA  = addError(13606, "def.zona.label");
	public static final String REPARTIDOR_TIPOREPARTIDOR  = addError(13607, "pad.tipoRepartidor.label");
	public static final String REPARTIDOR_BROCHE  = addError(13608, "pad.broche.label");
	// <--- Repartidor	
	
	// ---> CriRepCat
	public static final String CRIREPCAT_LABEL  	    = addError(13609, "pad.criRepCat.label");
	public static final String CRIREPCAT_REPARTIDOR  	    = addError(13610, "pad.criRepCat.repartidor.label");
	public static final String CRIREPCAT_SECCION  	    = addError(13611, "def.seccion.label");
	public static final String CRIREPCAT_CATASTRALDESDE  = addError(13612, "pad.criRepCat.catastralDesde.label");
	public static final String CRIREPCAT_CATASTRALHASTA  = addError(13613, "pad.criRepCat.catastralHasta.label");
	public static final String CRIREPCAT_FECHADESDE  = addError(13614, "pad.criRepCat.fechaDesde.label");
	public static final String CRIREPCAT_FECHAHASTA  = addError(13615, "pad.criRepCat.fechaHasta.label");
	// <--- CriRepCat	

	// ---> CriRepCalle
	public static final String CRIREPCALLE_LABEL  	    = addError(13616, "pad.criRepCalle.label");
	public static final String CRIREPCALLE_REPARTIDOR  	    = addError(13617, "pad.criRepCalle.repartidor.label");
	public static final String CRIREPCALLE_ZONA  	    = addError(13618, "def.zona.label");
	public static final String CRIREPCALLE_NRODESDE  = addError(13619, "pad.criRepCalle.nroDesde.label");
	public static final String CRIREPCALLE_NROHASTA  = addError(13620, "pad.criRepCalle.nroHasta.label");
	public static final String CRIREPCALLE_FECHADESDE  = addError(13621, "pad.criRepCalle.fechaDesde.label");
	public static final String CRIREPCALLE_FECHAHASTA  = addError(13622, "pad.criRepCalle.fechaHasta.label");
	public static final String CRIREPCALLE_CALLE  = addError(13622, "pad.calle.label");
	// <--- CriRepCalle	

	// ---> Broche
	public static final String BROCHE_LABEL  	    = addError(13623, "pad.broche.label");
	public static final String BROCHE_DESBROCHE  	    = addError(13624, "pad.broche.desBroche.label");
	public static final String BROCHE_RECURSO  = addError(13625, "def.recurso.label");
	public static final String BROCHE_TIPOBROCHE  = addError(13626, "pad.tipoBroche.label");
	public static final String BROCHE_PERSONA  = addError(13627, "pad.persona.label");
	public static final String BROCHE_STRDOMICILIO  = addError(13628, "pad.broche.strDomicilio.label");
	public static final String BROCHE_DOMICILIO  = addError(13629, "pad.domicilio.label");
	public static final String BROCHE_REPARTIDOR  = addError(13630, "pad.repartidor.label");
	public static final String BROCHE_TELEFONO  = addError(13631, "pad.broche.telefono.label");
	public static final String BROCHE_EXENTOENVIOJUD  = addError(13631, "pad.broche.exentoEnvioJud.label");
	public static final String BROCHE_PERMITEIMPRESION  = addError(13631, "pad.broche.permiteImpresion.label");
	
	public static final String BROCHE_NROCUENTA_INVALIDO  = addError(13632, "pad.broche.nroCuentaInvalido");
	// <--- Broche
	
	// ---> TipoBroche
	public static final String TIPOBROCHE_LABEL  	    = addError(13633, "pad.tipoBroche.label");
	public static final String TIPOBROCHE_DESTIPOBROCHE     = addError(13634, "pad.tipoBroche.desTipoBroche.label");
	// <--- TipoBroche	
	
	// ---> BroCue
	public static final String BROCUE_LABEL  	    = addError(13635, "pad.broCue.label");
	public static final String BROCUE_BROCHE  = addError(13636, "pad.broche.label");
	public static final String BROCUE_CUENTA  = addError(13637, "pad.cuenta.label");
	public static final String BROCUE_FECHAALTA  = addError(13638, "pad.broCue.fechaAlta.label");
	public static final String BROCUE_FECHABAJA  = addError(13639, "pad.broCue.fechaBaja.label");
	public static final String BROCUE_CASO  = addError(13640, "pad.caso.label");
	// <--- BroCue
	
	// ---> TipoRepartidor
	public static final String TIPOREPARTIDOR_LABEL      = addError(13641, "pad.tipoRepartidor.label");
	// <--- TipoRepartidor
	
	// ---> CueExcSel
	public static final String CUEEXCSEL_LABEL        = addError(0, "pad.cueExcSel.label");
	public static final String CUEEXCSEL_CUENTA_LABEL = addError(0,"pad.cueExcSel.cuenta.label");
	public static final String CUEEXCSEL_CUENTA_RECURSO_LABEL = addError(0,"pad.cueExcSel.cuenta.recurso.label");
	public static final String CUEEXCSEL_AREA_LABEL = addError(0,"pad.cueExcSel.area.label");

	public static final String CUEEXCSEL_CUENTA_EXCLUIDA  = addError(0, "pad.cueExcSel.cuentaExcluida.label");
	// <--- CueExcSel
	
	//	 ---> CueExcSelDeu
	public static final String CUEEXCSELDEU_DEUDA_LABEL        = addError(0, "pad.cueExcSelDeu.label");
	public static final String CUEEXCSELDEU_DEUDA_EXCLUIDA_DE_CUENTA = addError(0, "pad.cueExcSelDeu.deudaExcluidaDeCuenta.label");
	public static final String CUEEXCSELDEU_MSJ_NINGUNO_SELECTED = addError(0, "pad.cueExcSelDeu.msj.ningunoSelected.label");
	//	 <--- CueExcSelDeu
	
	public static final String BUZONCAMBIOS_TIPOMODIFICACION_LABEL = addError(0, "pad.buzonCambios.tipoModificacion.label");
	public static final String BUZONCAMBIOS_TIPOPERSONA_LABEL = addError(0, "pad.buzonCambios.tipoPersona.label");
	public static final String BUZONCAMBIOS_TIPODOCUMENTO_LABEL = addError(0, "pad.buzonCambios.tipoDocumento.label");
	public static final String BUZONCAMBIOS_TIPOYNRODOCUMENTO_LABEL = addError(0, "pad.buzonCambios.tipoyNumeroDocumento.label");
	public static final String BUZONCAMBIOS_CUIT00_LABEL = addError(0, "pad.buzonCambios.cuit00.label");
	public static final String BUZONCAMBIOS_CUIT01_LABEL = addError(0, "pad.buzonCambios.cuit01.label");
	public static final String BUZONCAMBIOS_CUIT02_LABEL = addError(0, "pad.buzonCambios.cuit02.label");
	public static final String BUZONCAMBIOS_CUIT03_LABEL = addError(0, "pad.buzonCambios.cuit03.label");
	public static final String BUZONCAMBIOS_APELLIDO_LABEL = addError(0, "pad.buzonCambios.apellido.label");
	public static final String BUZONCAMBIOS_NOMBRES_LABEL = addError(0, "pad.buzonCambios.nombres.label");
	public static final String BUZONCAMBIOS_RAZONSOCIAL_LABEL = addError(0, "pad.buzonCambios.razonSocial.label");
	public static final String BUZONCAMBIOS_CONTACTO_LABEL = addError(0, "pad.buzonCambios.contacto.label");
	public static final String BUZONCAMBIOS_OBSERVACIONES_LABEL = addError(0, "pad.buzonCambios.observaciones.label");

	// ---> AltaOficio
	public static final String ALTAOFICIO_LABEL =  addError(0, "pad.altaOficio.label");
	public static final String ALTAOFICIO_NROACTA_LABEL = addError(0, "pad.altaOficio.nroActa.label");
	public static final String ALTAOFICIO_FECHA = addError(0, "pad.altaOficio.fecha.label");
	public static final String ALTAOFICIO_CONTR_LABEL = addError(0, "pad.contribuyente.label");
	public static final String ALTAOFICIO_DOMICILIO_NOVALIDADO = addError(0, "pad.altaOficioAdapterVO.error.domicilioNovalidado");
	// <--- Alta Oficio
		
	//--> adhesion al rs - ws

	
	//public static final int ERR_ErrorServicio=1;
	public static final String ADHESIONRS_WEB_SERVICE_SERVICIO=addError(0,"pad.adhesionRS.ws.servicio");

	//public static final int ERR_NoExisteCuenta= 10;
	public static final String ADHESIONRS_WEB_SERVICE_NOEXISTECUENTA=addError(0,"pad.adhesionRS.ws.noexistecuenta");

	//public static final int ERR_ConvenioMultilateral = 20;
	public static final String ADHESIONRS_WEBSERVICE_CONVMULT = addError(0,"pad.adhesionRS.ws.convmult");

	//public static final int ERR_ContribCuentasMultiples	=30;
	public static final String ADHESIONRS_WEBSERVICE_MULTIPLES_CUENTAS=addError(0,"pad.adhesionRS.ws.multiplescuentas");

	//	public static final int ERR_CuentaYaTieneAltaRS=40;
	public static final String ADHESIONRS_WEB_SERVICE_ALTA_YATIENEALTA=addError(0,"pad.adhesionRS.ws.altayatienealta");

	//public static final int ERR_CantidadPersonal=50;
	public static final String ADHESIONRS_WEBSERVICE_CANPER=addError(0,"pad.adhesionRS.ws.canper");
	
	//public static final int ERR_CuentaConInicioDeCierre=60;
	public static final String ADHESIONRS_WEBSERVICE_INICIERRE=addError(0,"pad.adhesionRS.ws.inicierre");

	// public static final int ERR_FaltaDeclararETUR=70;
	public static final String ADHESIONRS_WEBSERVICE_ETUR_NODECLARADO=addError(0,"pad.adhesionRS.ws.nodeclarado");

	
	//public static final int ERR_MDF_NoTieneAlta=92;
	public static final String ADHESIONRS_WEBSERVICE_MDF_NOTIENEALTA=addError(0,"pad.adhesionRS.ws.mdfnotienealta");

	
	//public static final int ERR_AlicRedHabSocFueraRango=110;
	public static final String ADHESIONRS_WEBSERVICE_ALICREDHABSOC=addError(0,"pad.adhesionRS.ws.alicredhab");
	
	//public static final int ERR_IngBrutosFueraRango=120;
	public static final String ADHESIONRS_WEBSERVICE_INGBRU=addError(0,"pad.adhesionRS.ws.ingbru");
	
	//public static final int ERR_SuperficieFueraRango=130;
	public static final String ADHESIONRS_WEBSERVICE_SUPFUERARANGO=addError(0,"pad.adhesionRS.ws.supfueraRango");
	
	//public static final int ERR_AlicAdicionalFueraRango=140;
	public static final String ADHESIONRS_WEBSERVICE_ALICADICFUERAN=addError(0,"pad.adhesionRS.ws.alicadicfueran");
	
	//public static final int ERR_AdicEturFueraRango=150;
	public static final String ADHESIONRS_WEBSERVICE_ADICETURFUERAN=addError(0,"pad.adhesionRS.ws.adiceturfueran");
	
	//public static final int ERR_Inesperado=200;
	public static final String ADHESIONRS_WEBSERVICE_ERROR_CUNA=addError(0,"pad.adhesionRS.ws.errorcuna");

	
	
	






	
	
	
}
