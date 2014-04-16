//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.util;


import ar.gov.rosario.siat.base.iface.util.BaseError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class DefError extends BaseError {
	// Use Codigos desde 4000 hasta 4999
	//static public String XXXXXX_XXXX_XXX   = addError(4000, "siat.base.xxxxxx");
	
	// ---> ABM Dominio Atributo
	// Dominio atrbuto
	public static final String DOMATR_LABEL         = addError(4000,"def.domAtr.label");
	public static final String DOMATR_CODDOMATR     = addError(4000,"def.domAtr.codDomAtr.label");
	public static final String DOMATR_DESDOMATR     = addError(4000,"def.domAtr.desDomAtr.label");
	public static final String DOMATR_CLASSFORNAME  = addError(4000,"def.domAtr.classForName.label");
	
	// Dominio atributo valor
	public static final String DOMATRVAL_LABEL      = addError(4000,"def.domAtrVal.label");
	public static final String DOMATRVAL_STRVALOR   = addError(4000,"def.domAtrVal.strValor.label");
	public static final String DOMATRVAL_DESVALOR   = addError(4000,"def.domAtrVal.desValor.label"); 
	public static final String DOMATRVAL_FECHADESDE = addError(4000,"def.domAtrVal.fechaDesde.label"); 
	public static final String DOMATRVAL_FECHAHASTA = addError(4000,"def.domAtrVal.fechaHasta.label"); 

	// Tipo Atributo
	public static final String TIPOATRIBUTO_LABEL   = addError(4000,"def.tipoAtributo.label");
	// <--- ABM Dominio Atributo

	// ---> ABM Categoria	
	public static final String CATEGORIA_LABEL        	= addError(4150, "def.categoria.label");
	public static final String CATEGORIA_DESCATEGORIA  		= addError(4051, "def.categoria.desCategoria.label");
	public static final String CATEGORIA_TIPO  		= addError(4052, "def.categoria.tipo.label");
	// ---> ABM Categoria	
	
	// ---> ABM Atributo
	public static final String ATRIBUTO_LABEL        	= addError(4054, "def.atributo.label");
	public static final String ATRIBUTO_CODATRIBUTO     = addError(4055, "def.atributo.codAtributo.label");
	public static final String ATRIBUTO_DESATRIBUTO     = addError(4056, "def.atributo.desAtributo.label");
	public static final String ATRIBUTO_MASCARAVISUAL  	= addError(4056, "def.atributo.mascaraVisual.label");
	public static final String ATRIBUTO_NO_MODIFICAR_VAL	= addError(4056, "def.atributo.noModificarValorizado");
	// <--- ABM Atributo
	
	// ---> ABM TipObjImp
	public static final String TIPOBJIMP_LABEL         = addError(4100, "def.tipObjImp.label");
	public static final String TIPOBJIMP_CODTIPOBJIMP  = addError(4101, "def.tipObjImp.codTipObjImp.label");
	public static final String TIPOBJIMP_DESTIPOBJIMP  = addError(4102, "def.tipObjImp.desTipObjImp.label");
	public static final String TIPOBJIMP_ESSIAT        = addError(4103, "def.tipObjImp.esSiat.label");
	public static final String TIPOBJIMP_FECHAALTA     = addError(4104, "def.tipObjImp.fechaAlta.label");
	public static final String TIPOBJIMP_FECHABAJA     = addError(4105, "def.tipObjImp.fechaBaja.label");
	public static final String TIPOBJIMP_ESTADO  	   = addError(4106,"def.tipObjImp.estado.label");
	public static final String TIPOBJIMP_SIN_ATR  	   = addError(4107,"def.tipObjImp.sinAtr");
	public static final String TIPOBJIMP_SIN_ATR_CLAVE  	   = addError(4108,"def.tipObjImp.sinAtrClave");
	public static final String TIPOBJIMP_SIN_ATR_CLAVE_FUNCIONAL   = addError(4109,"def.tipObjImp.sinAtrClaveFuncional");
	public static final String TIPOBJIMP_YA_TIENE_CLAVE 	   = addError(4110,"def.tipObjImp.yaTieneClave");
	public static final String TIPOBJIMP_YA_TIENE_CLAVE_FUNCIONAL  = addError(4111,"def.tipObjImp.yaTieneClaveFuncional");
	
	public static final String TIPOBJIMP_FECHAALTA_REF     = addError(41012, "def.tipObjImp.fechaAlta.ref");
	// <--- ABM TipObjImp

	// ---> ABM TipObjImpAtr	
	public static final String TIPOBJIMPATR_LABEL            = addError(4120, "def.tipObjImpAtr.label");	
	public static final String TIPOBJIMPATR_ESREQUERIDO  	 = addError(0,"def.tipObjImpAtr.esRequerido.label");
	public static final String TIPOBJIMPATR_ESMULTIVALOR  	 = addError(0,"def.tipObjImpAtr.esMultivalor.label");
	public static final String TIPOBJIMPATR_POSEEVIGENCIA  	 = addError(0,"def.tipObjImpAtr.poseeVigencia.label");
	public static final String TIPOBJIMPATR_ESCLAVE  	     = addError(0,"def.tipObjImpAtr.esClave.label");
	public static final String TIPOBJIMPATR_ESCLAVEFUNCIONAL = addError(0,"def.tipObjImpAtr.esClaveFuncional.label");
	public static final String TIPOBJIMPATR_ESCODGESCUE 	 = addError(0,"def.tipObjImpAtr.esCodGesCue.label");
	public static final String TIPOBJIMPATR_ESDOMICILIOENVIO = addError(0,"def.tipObjImpAtr.esDomicilioEnvio.label");
	public static final String TIPOBJIMPATR_VALORDEFECTO  	 = addError(0,"def.tipObjImpAtr.valorDefecto.label");
	public static final String TIPOBJIMPATR_ESVISCONDEU  	 = addError(0,"def.tipObjImpAtr.esVisConDeu.label");
	public static final String TIPOBJIMPATR_ESVISIBLE    	 = addError(0,"def.tipObjImpAtr.esVisible.label");
	public static final String TIPOBJIMPATR_ESATRIBUTOBUS    = addError(0,"def.tipObjImpAtr.esAtributoBus.label");
	public static final String TIPOBJIMPATR_ESATRIBUSMASIVA  = addError(0,"def.tipObjImpAtr.esAtriBusMasiva.label");
	public static final String TIPOBJIMPATR_ADMBUSPORRAN  	 = addError(0,"def.tipObjImpAtr.admBusPorRan.label");
	public static final String TIPOBJIMPATR_ESATRIBUTOSIAT   = addError(0,"def.tipObjImpAtr.esAtributoSIAT.label");
	public static final String TIPOBJIMPATR_ESUBICACION  	 = addError(0,"def.tipObjImpAtr.esUbicacion.label");
	public static final String TIPOBJIMPATR_POSCOLINT  	     = addError(0,"def.tipObjImpAtr.posColInt.label");
	public static final String TIPOBJIMPATR_POSCOLINTHAS  	 = addError(0,"def.tipObjImpAtr.posColIntHas.label");
	public static final String TIPOBJIMPATR_FECHADESDE  	 = addError(0,"def.tipObjImpAtr.fechaDesde.label");
	public static final String TIPOBJIMPATR_FECHAHASTA  	 = addError(0,"def.tipObjImpAtr.fechaHasta.label");
	public static final String TIPOBJIMPATR_ESTADO  	     = addError(0,"def.tipObjImpAtr.estado.label");
	// <--- ABM TipObjImpAtr

	// ---> ABM TipObjImpAreO	
	public static final String TIPOBJIMPAREO_LABEL           = addError(4140, "def.tipObjImpAreO.label");
	public static final String TIPOBJIMPAREO_ESTADO  	     = addError(0,"def.tipObjImpAreO.estado.label");
	// <--- ABM TipObjImpAreO

	// ---> ABM AreaOrigen	
	public static final String AREA_LABEL           = addError(4145, "def.area.label");
	public static final String AREA_CODAREA         = addError(4146, "def.area.codArea.label");
	public static final String AREA_DESAREA         = addError(4147, "def.area.desArea.label");
	public static final String AREA_ESTADO  	    = addError(4148, "def.area.estado.label");
	// <--- ABM AreaOrigen
	
	// ---> ABM Feriado	
	public static final String FERIADO_LABEL        	= addError(4150, "def.feriado.label");
	public static final String FERIADO_FECHAFERIADO  	= addError(4154, "def.feriado.fechaFeriado.label");
	public static final String FERIADO_DESFERIADO  		= addError(4154, "def.feriado.desFeriado.label");
	
	public static final String FERIADO_SEARCHPAGE_FECHADESDE  		= addError(4154, "def.feriadoSearchPage.fechaDesde.label");
	public static final String FERIADO_SEARCHPAGE_FECHAHASTA  		= addError(4154, "def.feriadoSearchPage.fechaHasta.label");
	

	// <--- ABM Feriado
	
	// ---> ABM Vencimiento 
	public static final String VENCIMIENTO_LABEL        	= addError(4201, "def.vencimiento.label");
	public static final String VENCIMIENTO_DIA  	= addError(4202, "def.vencimiento.dia.label");
	public static final String VENCIMIENTO_MES  	= addError(4203, "def.vencimiento.mes.label");
	public static final String VENCIMIENTO_CANTDIAS  	= addError(4204, "def.vencimiento.cantDias.label");
	public static final String VENCIMIENTO_CANTMES  	= addError(4205, "def.vencimiento.cantMes.label");
	public static final String VENCIMIENTO_DESVENCIMIENTO  		= addError(4206, "def.vencimiento.desVencimiento.label");
	public static final String VENCIMIENTO_ESHABIL		= addError(4207, "def.vencimiento.esHabil.label");
	public static final String VENCIMIENTO_ESULTIMO		= addError(4208, "def.vencimiento.esUltimo.label");
	
	public static final String VENCIMIENTO_DIA_FUERARANGO		= addError(4209, "def.vencimiento.dia.fueraRango.label");
	public static final String VENCIMIENTO_MES_FUERARANGO		= addError(4210, "def.vencimiento.mes.fueraRango.label");
	public static final String VENCIMIENTO_CANTSEMANA	=	addError (4211, "def.vencimiento.cantSemana.label");
	public static final String VENCIMIENTO_PRIMEROSEMANA	=	addError (4212,"def.vencimiento.primeroSemana.label");
	public static final String VENCIMIENTO_ULTIMOSEMANA	=	addError (4213, "def.vencimiento.ultimoSemana.label");
	// <--- ABM Vencimiento

	// ---> ABM Servicio Banco
	// Servicio Banco
	public static final String SERVICIOBANCO_LABEL        	= addError(4250, "def.servicioBanco.label");
	public static final String SERVICIOBANCO_CODSERVICIOBANCO 	= addError(4251, "def.servicioBanco.codServicioBanco.label");
	public static final String SERVICIOBANCO_DESSERVICIOBANCO 	= addError(4252, "def.servicioBanco.desServicioBanco.label");	
	public static final String SERVICIOBANCO_PARTIDAINDET 	= addError(4252, "def.servicioBanco.partidaIndet.label");
	public static final String SERVICIOBANCO_PARCUEPUE 	= addError(4252, "def.servicioBanco.parCuePue.label");
	public static final String SERVICIOBANCO_ESAUTOLIQUIDABLE 	= addError(4252, "def.servicioBanco.esAutoliquidable.label");
	public static final String SERVICIOBANCO_TIPOASENTAMIENTO 	= addError(4252, "def.servicioBanco.tipoAsentamiento.label");
	
	// Servicio Banco Recurso
	public static final String SERBANREC_LABEL        	= addError(4253, "def.serBanRec.label");
	public static final String SERBANREC_FECHADESDE  	= addError(4254, "def.serBanRec.fechaDesde.label");
	public static final String SERBANREC_FECHAHASTA  	= addError(4255, "def.serBanRec.fechaHasta.label");
	public static final String SERBANREC_RECURSO  		= addError(4256, "def.serBanRec.recurso.label");
	public static final String SERBANREC_SERVICIOBANCO 		= addError(4257, "def.serBanRec.servicioBanco.label");
	// <--- ABM Servicio Banco

	
	// ---> RECURSO
	//Recurso
	public static final String RECURSO_LABEL = addError(4300, "def.recurso.label");
	public static final String RECURSO_CODRECURSO = addError(4301, "def.recurso.codRecurso.label");
	public static final String RECURSO_DESRECURSO = addError(4302, "def.recurso.desRecurso.label");
	public static final String RECURSO_ESAUTOLIQUIDABLE = addError(4303, "def.recurso.esAutoliquidable.label");
	public static final String RECURSO_ESFISCALIZABLE = addError(4304, "def.recurso.esFiscalizable.label");
	public static final String RECURSO_ESLIBREDEUDA = addError(4305, "def.recurso.esLibreDeuda.label");
	public static final String RECURSO_GESDEUNOVEN = addError(4306, "def.recurso.gesDeuNoVen.label");
	public static final String RECURSO_DEUEXIVEN = addError(4307, "def.recurso.deuExiVen.label");
	public static final String RECURSO_GESCOBRANZA = addError(4308, "def.recurso.gesCobranza.label");
	public static final String RECURSO_PEREMIDEUMAS = addError(4309, "def.recurso.perEmiDeuMas.label");
	public static final String RECURSO_PEREMIDEUPUNTUAL = addError(4309, "def.recurso.perEmiDeuPuntual.label");
	public static final String RECURSO_PEREMIDEUEXT = addError(4310, "def.recurso.perEmiDeuExt.label");
	public static final String RECURSO_PERIMPMASDEU = addError(4309, "def.recurso.perImpMasDeu.label");
	public static final String RECURSO_FORMIMPMASDEU = addError(4309, "def.recurso.formImpMasDeu.label");
	public static final String RECURSO_GENNOTIF = addError(4309, "def.recurso.genNotif.label");
	public static final String RECURSO_FORMNOTIF = addError(4309, "def.recurso.formNotif.label");
	public static final String RECURSO_ESDEUDATITULAR = addError(4311, "def.recurso.esDeudaTitular.label");
	public static final String RECURSO_ESPRINCIPAL = addError(4312, "def.recurso.esPrincipal.label");
	public static final String RECURSO_ALTACTAPORIFACE = addError(4312, "def.recurso.altaCtaPorIface.label");
	public static final String RECURSO_BAJACTAPORIFACE = addError(4313, "def.recurso.bajaCtaPorIface.label");
	public static final String RECURSO_MODITITCTAPORIFACE = addError(4314, "def.recurso.modiTitCtaPorIface.label");
	public static final String RECURSO_ALTACTAPORPRI = addError(4315, "def.recurso.altaCtaPorPri.label");
	public static final String RECURSO_BAJACTAPORPRI = addError(4316, "def.recurso.bajaCtaPorPri.label");
	public static final String RECURSO_MODITITCTAPORPRI = addError(4317, "def.recurso.modiTitCtaPorPri.label");
	public static final String RECURSO_ALTACTAMANUAL = addError(4318, "def.recurso.altaCtaManual.label");
	public static final String RECURSO_BAJACTAMANUAL = addError(4319, "def.recurso.bajaCtaManual.label");
	public static final String RECURSO_MODITITCTAMANUAL = addError(4320, "def.recurso.modiTitCtaPorPri.label");
	public static final String RECURSO_CRIASIPRO = addError(4321, "def.recurso.criAsiPro.label");
	public static final String RECURSO_FECHAALTA = addError(4322, "def.recurso.fechaAlta.label");
	public static final String RECURSO_RECPRI = addError(4323, "def.recurso.recPri.label");
	public static final String RECURSO_CATEGORIA = addError(4324, "def.recurso.categoria.label");
	public static final String RECURSO_FECESAUT = addError(4325, "def.recurso.fecEsAut.label");
	public static final String RECURSO_FECESFIS = addError(4326, "def.recurso.fecEsFis.label");
	public static final String RECURSO_FECDESINTDIABAN = addError(4327, "def.recurso.fecDesIntDiaBan.label");
	public static final String RECURSO_ENVIAJUDICIAL = addError(4328, "def.recurso.enviaJudicial.label");
	public static final String RECURSO_FECHABAJA = addError(4329, "def.recurso.fechaBaja.label");
	public static final String RECURSO_FECHAALTA_REF = addError(4330, "def.recurso.fechaAlta.ref");
	public static final String RECURSO_PERIODODEUDA = addError(4331, "def.recurso.periodoDeuda.label");
	public static final String RECURSO_FORMPADFIR = addError(4332, "def.recurso.formPadFir.label");
	public static final String RECURSO_GENCODGES = addError(4333, "def.recurso.genCodGes.label");
	
	//Recurso AutoLiquidable
	public static final String RECURSO_AUTOLIQUIDABLE_LABEL = addError(4300, "def.recurso.label");
	
	// ABM RecAtrVal
	public static final String RECATRVAL_LABEL        	= addError(4334, "def.recAtrVal.label");
	public static final String RECATRVAL_FECHADESDE  	= addError(4335, "def.recAtrVal.fechaDesde.label");
	public static final String RECATRVAL_FECHAHASTA  	= addError(4336, "def.recAtrVal.fechaHasta.label");
	public static final String RECATRVAL_RECURSO  		= addError(4337, "def.recAtrVal.recurso.label");
	public static final String RECATRVAL_ATRIBUTO 		= addError(4338, "def.recAtrVal.atributo.label");
	public static final String RECATRVAL_VALOR 			= addError(4333, "def.recAtrVal.valor.label");
	
	// ABM RecCon
	public static final String RECCON_LABEL        	= addError(4339, "def.recCon.label");
	public static final String RECCON_FECHADESDE  	= addError(4340, "def.recCon.fechaDesde.label");
	public static final String RECCON_FECHAHASTA  	= addError(4341, "def.recCon.fechaHasta.label");
	public static final String RECCON_RECURSO  		= addError(4342, "def.recCon.recurso.label");
	public static final String RECCON_CODRECCON  		= addError(4343, "def.recCon.codRecCon.label");
	public static final String RECCON_DESRECCON  		= addError(4344, "def.recCon.desRecCon.label");
	public static final String RECCON_ABRRECCON  		= addError(4345, "def.recCon.abrRecCon.label");
	public static final String RECCON_PORCENTAJE  		= addError(4346, "def.recCon.porcentaje.label");
	public static final String RECCON_INCREMENTA  		= addError(4347, "def.recCon.incrementa.label");
	public static final String RECCON_ESVISIBLE  		= addError(4348, "def.recCon.esVisible.label");
	public static final String RECCON_ORDENVISUALIZACION= addError(4349, "def.recCon.ordenVisualizacion.label");
	
	// RecMinDec
	public static final String RECMINDEC_LABEL        		= addError(0, "def.recMinDec.title");
	public static final String RECMINDEC_IMPOSIBLE_AGREGAR	=addError(0,"def.recMinDec.imposibleAgregar");
	public static final String RECMINDEC_MINIMO				=addError(0,"def.recMinDec.minimo.label");
	public static final String RECMINDEC_FECHADESDE			=addError(0,"def.recMinDec.fechaDesde.label");
	public static final String RECMINDEC_FECHAHASTA			=addError(0,"def.recMinDec.fechaHasta.label");
	public static final String RECMINDEC_VALREFDES			=addError(0,"def.recMinDec.valRefDes.label");
	public static final String RECMINDEC_VALREFHAS			=addError(0,"def.recMinDec.valRefHas.label");
	
	// ABM RecClaDeu
	public static final String RECCLADEU_LABEL        	= addError(4350, "def.recClaDeu.label");
	public static final String RECCLADEU_FECHADESDE  	= addError(4351, "def.recClaDeu.fechaDesde.label");
	public static final String RECCLADEU_FECHAHASTA  	= addError(4352, "def.recClaDeu.fechaHasta.label");
	public static final String RECCLADEU_RECURSO  		= addError(4353, "def.recClaDeu.recurso.label");
	public static final String RECCLADEU_DESCLADEU  	= addError(4354, "def.recClaDeu.desClaDeu.label");
	public static final String RECCLADEU_ABRCLADEU  	= addError(4355, "def.recClaDeu.abrClaDeu.label");
	public static final String RECCLADEU_ESORIGINAL  	= addError(4355, "def.recClaDeu.esOriginal.label");
	public static final String RECCLADEU_ACTUALIZADEUDA = addError(4355, "def.recClaDeu.actualizaDeuda.label");

	// ABM RecGenCueAtrVa
	public static final String RECGENCUEATRVA_LABEL        	= addError(4356, "def.recGenCueAtrVa.label");
	public static final String RECGENCUEATRVA_FECHADESDE  	= addError(4357, "def.recGenCueAtrVa.fechaDesde.label");
	public static final String RECGENCUEATRVA_FECHAHASTA  	= addError(4358, "def.recGenCueAtrVa.fechaHasta.label");
	public static final String RECGENCUEATRVA_RECURSO  		= addError(4359, "def.recGenCueAtrVa.recurso.label");
	public static final String RECGENCUEATRVA_STRVALOR  	= addError(4360, "def.recGenCueAtrVa.strValor.label");
	public static final String RECGENCUEATRVA_ATRIBUTO  	= addError(4361, "def.recGenCueAtrVa.atributo.label");

	// ABM RecEmi
	public static final String RECEMI_LABEL        		  = addError(4362, "def.recEmi.label");
	public static final String RECEMI_FECHADESDE  	   	  = addError(4363, "def.recEmi.fechaDesde.label");
	public static final String RECEMI_FECHAHASTA  		  = addError(4364, "def.recEmi.fechaHasta.label");
	public static final String RECEMI_RECURSO  			  = addError(4365, "def.recEmi.recurso.label");
	public static final String RECEMI_TIPOEMISION  		  = addError(4366, "def.recEmi.tipoEmision.label");
	public static final String RECEMI_PERIODODEUDA  	  = addError(4367, "def.recEmi.periodoDeuda.label");
	public static final String RECEMI_FOREMI 	 		  = addError(4368, "def.recEmi.programa.label");
	public static final String RECEMI_CANTPERAEMI  		  = addError(4369, "def.recEmi.cantPerAEmi.label");
	public static final String RECEMI_FORVEN	   		  = addError(4370, "def.recEmi.forVen.label");
	public static final String RECEMI_ATRIBUTOEMISION	  = addError(4372, "def.recEmi.atributoEmision.label");
	public static final String RECEMI_CANTDIASGRACIA	  = addError(4374, "def.recEmi.cantDiasGracia.label");
	public static final String RECEMI_GENERANOTIFICACION  = addError(4375, "def.recEmi.generaNotificacion.label");
	
	// ABM RecAtrCueEmi
	public static final String RECATRCUEEMI_LABEL        	= addError(4376, "def.recAtrCueEmi.label");
	public static final String RECATRCUEEMI_FECHADESDE  	= addError(4377, "def.recAtrCueEmi.fechaDesde.label");
	public static final String RECATRCUEEMI_FECHAHASTA  	= addError(4378, "def.recAtrCueEmi.fechaHasta.label");
	public static final String RECATRCUEEMI_RECURSO  		= addError(4379, "def.recAtrCueEmi.recurso.label");
	public static final String RECATRCUEEMI_ATRIBUTO 		= addError(4380, "def.recAtrCueEmi.atributo.label");
	public static final String RECATRCUEEMI_ESVISCONDEU 	= addError(0, 	 "def.recAtrCueEmi.esVisConDeu.label");
	public static final String RECATRCUEEMI_ESVISREC 		= addError(0, 	 "def.recAtrCueEmi.esVisRec.label");
	
	// ABM RecAtrCue
	public static final String RECATRCUE_LABEL        	= addError(4381, "def.recAtrCue.label");
	public static final String RECATRCUE_FECHADESDE  	= addError(4382, "def.recAtrCue.fechaDesde.label");
	public static final String RECATRCUE_FECHAHASTA  	= addError(4383, "def.recAtrCue.fechaHasta.label");
	public static final String RECATRCUE_RECURSO  		= addError(4384, "def.recAtrCue.recurso.label");
	public static final String RECATRCUE_VALORDEFECTO  	= addError(4385, "def.recAtrCue.valorDefecto.label");
	public static final String RECATRCUE_ATRIBUTO  		= addError(4386, "def.recAtrCue.atributo.label");
	public static final String RECATRCUE_ESREQUERIDO  	= addError(4386, "def.recAtrCue.esRequerido.label");
	public static final String RECATRCUE_POSEEVIGENCIA  = addError(4386, "def.recAtrCue.poseeVigencia.label");
	public static final String RECATRCUE_ESVISCONDEU  	= addError(4386, "def.recAtrCue.esVisConDeu.label");	
	public static final String RECATRCUE_ESVISREC		= addError(0,	 "def.recAtrCue.esVisRec.label");
	public static final String MSG_ELIMINAR_RECATRCUE_CUENTASVALORIZADAS = addError(4386, "def.recAtrCue.msgEliminarRecAtrCueCuentasValorizadas");
	
	// ABM RecursoArea
	public static final String RECURSOAREA_LABEL 		= addError(4381, "def.recursoArea.label");
	public static final String RECURSOAREA_PERCREAEMI	= addError(4381, "def.perCreaEmi.label");
	public static final String MSG_RECURSOAREA_UNIQUE	= addError(4381, "def.recursoArea.msgUnique");
	
	// <--- RECURSO

	// ---> TIPO EMISION
	public static final String TIPOEMISION_LABEL        	= addError(4371, "def.tipoEmision.label");
	public static final String TIPOEMISION_DESTIPOEMISION  	= addError(4372, "def.tipoEmision.desTipoEmision.label");
	// <--- TIPO EMISION
	
	// ---> PERIODO DEUDA
	public static final String PERIODODEUDA_LABEL        	= addError(4373, "def.periodoDeuda.label");
	public static final String PERIODODEUDA_DESPERIODODEUDA  	= addError(4374, "def.periodoDeuda.desTipoEmision.label");
	// <--- PERIODO DEUDA
	
	// ---> GENCUE
	public static final String GENCUE_LABEL        	= addError(4375, "def.genCue.label");
	public static final String GENCUE_CODGENCUE  	= addError(4376, "def.genCue.codGenCue.label");
	public static final String GENCUE_DESGENCUE  	= addError(4377, "def.genCue.desGenCue.label");
	// <--- GENCUE
	
	// ---> GENCODGES
	public static final String GENCODGES_LABEL        	= addError(4378, "def.genCodGes.label");
	public static final String GENCODGES_CODGENCODGES  	= addError(4379, "def.genCodGes.codGenCodGes.label");
	public static final String GENCODGES_DESGENCODGES  	= addError(4380, "def.genCodGes.desGenCodGes.label");
	// <--- GENCODGES
	
	// ---> CONATR
	public static final String CONATR_ADMBUSPORRAN = addError(0, "def.conAtr.admBusPorRan.label");
	public static final String CONATR_ESATRIBUTOBUS = addError(0, "def.conAtr.esAtributoBus.label");
	public static final String CONATR_ESATRSEGMENTACION = addError(0, "def.conAtr.esAtrSegmentacion.label");
	public static final String CONATR_ESVISCONDEU = addError(0, "def.conAtr.esVisConDeu.label");
	public static final String CONATR_LABEL = addError(0, "def.conAtr.label");
	public static final String CONATR_VALORDEFECTO = addError(0, "def.conAtr.valorDefecto.label");
	 // <--- CONATR	
	 
	// ---> SECCION	
	public static final String SECCION_LABEL       = addError(0, "def.seccion.label");
	public static final String SECCION_DESCRIPCION = addError(0, "def.seccion.descripcion.label");
	// ---> SECCION	
	
	public static final String VIADEUDA_LABEL = addError(0, "def.viaDeuda.label");
	public static final String PROCURADOR_LABEL = addError(0, "def.procurador.label");
	
	//---> ABM Parametro	
	public static final String PARAMETRO_CODPARAM = addError(0, "def.parametro.codParam.label");
	public static final String PARAMETRO_DESPARAM = addError(0, "def.parametro.desParam.label");
	public static final String PARAMETRO_VALOR 	  = addError(0, "def.parametro.valor.label");
	public static final String PARAMETRO_LABEL    = addError(0, "def.parametro.label");
	//<--- ABM Parametro

	//---> ABM Calendario
	public static final String CALENDARIO_LABEL = addError(0, "def.calendario.label");
	public static final String CALENDARIO_RECURSO = addError(0, "def.calendario.recurso.label");
	public static final String CALENDARIO_PERIODO = addError(0, "def.calendario.periodo.label");
	public static final String CALENDARIO_PERIODO_LARGOSEIS_NUMERICO = addError(0, "def.calendario.periodoLargoSeisNumerico.label");
	public static final String CALENDARIO_FECHAVENCIMIENTO = addError(0, "def.calendario.fechaVencimiento.label"); 

	public static final String CALENDARIO_BUSQUEDA_FECHADESDE = addError(0, "def.calendarioSearchPage.fechaDesde.label");
	public static final String CALENDARIO_BUSQUEDA_FECHAHASTA = addError(0, "def.calendarioSearchPage.fechaHasta.label");
	
	public static final String CALENDARIO_FECHADESDE = addError(0, "def.calendario.fechaDesde.label");
	public static final String CALENDARIO_FECHAHASTA = addError(0, "def.calendario.fechaHasta.label ");
	//<--- ABM Calendario
	
	// ---> Zona
	public static final String ZONA_LABEL = addError(0, "def.zona.label");
	//<---  Zona

	//Usado para indicar el cache a refrescar
	public static final String CACHE_INDETERMINADO = "INDETERMINADOS";
	public static final String CACHE_DEFATRIBUTO = "DEFINICION_ATRIBUTOS";
	public static final String CACHE_SWE = "ROLES_SWE";
	public static final String CACHE_PARAMETRO = "PARAMETROS";
	public static final String CACHE_CASO = "TIPO_CASO";
	public static final String CACHE_FRASE = "FRASES";
	public static final String CACHE_ALL = null;

	// ---> ABM EmiMat
	public static final String EMIMAT_LABEL       = addError(0, "def.emiMat.label");
	public static final String EMIMAT_RECURSO 	  = addError(0, "def.emiMat.recurso.label");
	public static final String EMIMAT_CODEMIMAT   = addError(0, "def.emiMat.codEmiMat.label");
	// <--- ABM EmiMat
	
	// ---> ABM ColEmiMat
	public static final String COLEMIMAT_LABEL       = addError(0, "def.colEmiMat.label");
	public static final String COLEMIMAT_EMIMAT      = addError(0, "def.colEmiMat.emiMat.label");
	public static final String COLEMIMAT_CODCOLUMNA  = addError(0, "def.colEmiMat.codColumna.label");
	public static final String COLEMIMAT_TIPODATO 	 = addError(0, "def.colEmiMat.tipoDato.label");
	public static final String COLEMIMAT_TIPOCOLUMNA = addError(0, "def.colEmiMat.tipoColumna.label");
	public static final String COLEMIMAT_ORDEN 		 = addError(0, "def.colEmiMat.orden.label");
	// <--- ABM ColEmiMat

	//-->ABM RecConADec
	public static final String RECCONADEC_RECURSO=addError(0,"def.recConADec.recurso");
	public static final String RECCONADEC_TIPRECCONADEC=addError(0,"def.RecConADec.tipRecConADec");
	public static final String RECCONADEC_CODCONCEPTO = addError(0, "def.recConADec.codConcepto");
	public static final String RECCONADEC_DESCONCEPTO = addError(0, "def.recConADec.desConcepto");
	public static final String RECCONADEC_FECHADESDE = addError(0,"def.recConADec.fechaDesde");
	public static final String RECCONADEC_FECHAHASTA = addError(0, "def.recConADec.fechaHasta");
	public static final String RECCONADEC_TIENE_REFERENCIA=addError(0, "def.recConADec.referencia.error");
	public static final String RECCONADEC_EXISTE_LISTA_VAL=addError(0,"def.recConADecAdapter.valUni.delete.error");
	
	//-->ABM RecAli
	public static final String RECALI_LABEL=addError(0,"def.recAli.label");
	public static final String RECALI_RECTIPALI=addError(0,"def.recAli.recTipAli.label");
	public static final String RECALI_ALICUOTA=addError(0,"def.recAli.alicuota.label");
	public static final String RECALI_FECHADESDE=addError(0,"def.recAli.fechaDesde.label");
	public static final String RECALI_FECHAHASTA=addError(0,"def.recAli.fechaHasta.label");
	
	//--> ABM ValUnRecConADe
	public static final String VALUNRECCONADEC_VALORUNITARIO_LABEL=addError(0,"def.valUnRecConADeAdapter.legend");
	public static final String VALUNRECCONADEC_FECHADESDE_LABEL=addError(0,"def.valUnRecConADeAdapter.fechaDesde.label");
	public static final String VALUNRECCONADEC_RECCONADEC_LABEL=addError(0,"def.valUnRecConADeAdapter.recConADec.label");
	
	//-->ABM MinRecConADec
	public static final String MINRECCONADEC_MINIMO_LABEL=addError(0,"def.minRecConADec.minimo.label");
	public static final String MINRECCONADEC_FECHADESDE_LABEL=addError(0,"def.minRecConADec.fechaDesde.label");
	
	// --> ABM HistCodEmi
	public static final String HISTCODEMI_DESHISTCODEMI=addError(0,"def.histCodEmi.desHistCodEmi.label");
	
	// --> ABM CodEmi
	public static final String CODEMI_LABEL		   = addError(0,"def.codEmi.label");
	public static final String CODEMI_RECURSO	   = addError(0,"def.codEmi.recurso.label");
	public static final String CODEMI_TIPCODEMI	   = addError(0,"def.codEmi.tipCodEmi.label");
	public static final String CODEMI_NOMBRE	   = addError(0,"def.codEmi.nombre.label");
	public static final String CODEMI_DESCRIPCION  = addError(0,"def.codEmi.descripcion.label");
	public static final String CODEMI_FECHADESDE   = addError(0,"def.codEmi.fechaDesde.label");
	public static final String CODEMI_FECHAHASTA   = addError(0,"def.codEmi.fechaHasta.label");

	public static final String CODEMIADAPTER_CUENTA   = addError(0,"def.codEmiAdapter.cuenta.label");
	public static final String CODEMIADAPTER_ANIO 	  = addError(0,"def.codEmiAdapter.anio.label");
	public static final String CODEMIADAPTER_PERIODO  = addError(0,"def.codEmiAdapter.periodo.label");
	// <-- ABM CodEmi

	// ---> ABM Banco
	public static final String BANCO_LABEL        	= addError(0, "def.banco.label");
	public static final String BANCO_CODBANCO 		= addError(0, "def.banco.codBanco.label");
	public static final String BANCO_DESBANCO 		= addError(0, "def.banco.desBanco.label");	
	public static final String BANCO_REGISTROS_ASOCIADOS = addError(0, "def.banco.registrosAsociados.label");
	// <--- ABM Banco
	
	// ---> ABM SiatScript
	public static final String SIATSCRIPT_LABEL 	 	 = addError(0, "def.siatscript.label");
	public static final String SIATSCRIPT_CODSIATSCRIPT	 = addError(0, "def.siatScript.codSiatScript.label");
	public static final String SIATSCRIPT_DESSIATSCRIPT  = addError(0, "def.siatScript.desSiatScript.label");
	public static final String SIATSCRIPT_PATHSIATSCRIPT  = addError(0, "def.siatScript.pathSiatScript.label");
	public static final String SIATSCRIPT_READSIATSCRIPT  = addError(0, "def.siatScript.readSiatScript.label");	
	// <--- ABM SiatScript
	
	// ---> ABM SiatScriptUsr
	public static final String SIATSCRIPTUSR_LABEL			    = addError(0, "def.siatscriptusr.label");
	public static final String SIATSCRIPTUSR_CODSIATSCRIPTUSR   = addError(0, "def.siatscriptusr.label");
	public static final String SIATSCRIPTUSR_PROCESO 			= addError(0, "def.siatScript.proceso.desProceso.label");
	public static final String SIATSCRIPTUSR_USRSIAT			= addError(0, "def.siatScript.usuarioSiat.label");
	// <--- ABM SiatScriptUsr
	
	
}
