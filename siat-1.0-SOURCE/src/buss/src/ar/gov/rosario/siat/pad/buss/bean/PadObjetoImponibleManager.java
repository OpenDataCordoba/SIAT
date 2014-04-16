//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.GenCodGes;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.iface.model.AtrValDefinition;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.gde.buss.bean.CierreComercio;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Manejador del m&oacute;dulo Pad y submodulo ObjetoImponible
 * 
 * @author tecso
 * 
 */
public class PadObjetoImponibleManager {

	private static Logger log = Logger
			.getLogger(PadObjetoImponibleManager.class);

	private static final PadObjetoImponibleManager INSTANCE = new PadObjetoImponibleManager();

	/**
	 * Constructor privado
	 */
	private PadObjetoImponibleManager() {

	}

	/**
	 * Devuelve unica instancia
	 */
	public static PadObjetoImponibleManager getInstance() {
		return INSTANCE;
	}

	// ---> ABM ObjImp
	/**
	 * Alta de un objeto imponible con sus atributos valorizados. Cuenado
	 * corresponde realiza altas de cuentas por Recurso Principal y Secundario.
	 * 
	 * @param objImp
	 *            Objeto imponible instanciado con los clave, claveFunc y
	 *            fechaAlta valorizados.
	 * @param definition
	 *            Objeto definicion de TipObjImp con los valores de atributos
	 *            cargados.
	 */
	public ObjImp createObjImp(ObjImp objImp, TipObjImpDefinition definition) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		log.debug(funcName + " enter");
		
		// Validaciones de negocio
		if(!AtrValDefinition.ACT_INTERFACE.equals(definition.getAct())){
			if (!objImp.validateCreate()) {
				log.debug(" validando en AtrValDefinition.ACT_INTERFACE - no paso la valid");
				return objImp;
			}	
		}
		
		PadDAOFactory.getObjImpDAO().update(objImp);
		
		log.debug(funcName + " objImp creado: " + objImp.getId());
		
		// por cada atributo de definition
		// instancia ObjImpAtrVal, seta valor unico (porque es alta)
		// seta objimp padre
		// objImp.createObjImpAtrVal(objImpAtrVal)
		if(definition!=null) {			
			for (TipObjImpAtrDefinition tpid: definition.getListTipObjImpAtrDefinition()){
				
				if(!tpid.getEsMultivalor() && !StringUtil.isNullOrEmpty(tpid.getValorString())){
					ObjImpAtrVal objImpAtrVal = new ObjImpAtrVal();
					objImpAtrVal.setObjImp(objImp);
					objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(tpid.getTipObjImpAtr().getId()));
					objImpAtrVal.setStrValor(tpid.getValorString());
					if(tpid.getPoseeVigencia()){
						if((tpid.getFechaDesde()!=null)&&(tpid.getFechaNovedad()!=null)){
							objImpAtrVal.setFechaDesde(tpid.getFechaDesde());
							objImpAtrVal.setFechaHasta(tpid.getFechaHasta());
							objImpAtrVal.setFechaNovedad(tpid.getFechaNovedad());
						}else{
							objImpAtrVal.setFechaDesde(objImp.getFechaAlta());
							objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
						}
					} else {
						if (tpid.getFechaNovedad()!=null)
							objImpAtrVal.setFechaNovedad(tpid.getFechaNovedad());
						else
							objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
					}
					
					objImp.createObjImpAtrVal(objImpAtrVal);				
				}
				
				if(tpid.getEsMultivalor() && !ListUtil.isNullOrEmpty(tpid.getMultiValor())){
					for(String strValor: tpid.getMultiValor()){
						
						if (!StringUtil.isNullOrEmpty(strValor)){
							ObjImpAtrVal objImpAtrVal = new ObjImpAtrVal();
							objImpAtrVal.setObjImp(objImp);
							objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(tpid.getTipObjImpAtr().getId()));
							objImpAtrVal.setStrValor(strValor);
							if(tpid.getPoseeVigencia()){
								if((tpid.getFechaDesde()!=null)&&(tpid.getFechaNovedad()!=null)){
									objImpAtrVal.setFechaDesde(tpid.getFechaDesde());
									objImpAtrVal.setFechaHasta(tpid.getFechaHasta());
									objImpAtrVal.setFechaNovedad(tpid.getFechaNovedad());
								}else{
									objImpAtrVal.setFechaDesde(objImp.getFechaAlta());
									objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
								}
							} else {
								if (tpid.getFechaNovedad()!=null)
									objImpAtrVal.setFechaNovedad(tpid.getFechaNovedad());
								else
									objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
							}
							
							objImp.createObjImpAtrVal(objImpAtrVal);	
						}
					}
				}		
			}
		}
		
		if (objImp.getListObjImpAtrVal() != null)
			log.debug( funcName + " objImpAtrVal creadoa: " + objImp.getListObjImpAtrVal().size());
		
		// Analisis del comportamiento sobre cuentas en funcion de la
		// configuración del recurso
		// Busco el Recurso Principal asociado
		Recurso recPri = objImp.getTipObjImp().getRecursoPrincipal();
		// Si no encuentro un Recurso Principal termino aca.
		if(recPri == null){
			log.debug( funcName + " TipObjImp no posee RecursoPrincipal asociado, fin create.");
			return objImp;
		}
		
		log.debug( funcName + " TipObjImp.RecursoPrincipal asociado: " + recPri.getDesRecurso());
		log.debug( funcName + " " + recPri.getDesRecurso() + " -> realiza alta de cuenta por alta de objeto Imponible: " + (recPri.getAltaCtaPorIface()==1?"Si":"No"));
		
		// Con el principal asociado, verifico si "realiza alta de cuenta por
		// alta de objeto Imponible"
		if(recPri.getAltaCtaPorIface().intValue() == 1){
			// Domicilio de Envio
			Domicilio domicilioEnvio = null;
			String domicilioToParse = "";
			TipObjImpAtrDefinition domicilioTipObjImpDef = null;
			if (recPri.equals(Recurso.getDReI())){
				 domicilioTipObjImpDef = definition.getTipObjImpAtrDefinitionByCodigo("DomicilioFinca");
			}else{
				domicilioTipObjImpDef = definition.getTipObjImpAtrDefinitionByCodigo("DomicilioEnvio");
			}
			if(domicilioTipObjImpDef != null)
				domicilioToParse = domicilioTipObjImpDef.getValorString();
			
			if(Domicilio.isValidToParse(domicilioToParse)){
				domicilioEnvio = Domicilio.valueOf(domicilioToParse);
				if(domicilioEnvio != null){
					domicilioEnvio = PadDomicilioManager.getInstance().createDomicilio(domicilioEnvio);					
					if(domicilioEnvio.hasError())
						domicilioEnvio = null;
				}
			}
			
			// Obtener algoritmos para la generacion de numero de cuenta y
			// codigo de gestion personal.
			// Genera el numero de cuenta (generalmente se utiliza el que venga
			// informado)
			String numeroCuenta = objImp.getClave(); 
			// Se arma e inserta la cuenta por Recurso Principal asociado.
			Cuenta cuenta = new Cuenta();
			cuenta.setRecurso(recPri);
			cuenta.setObjImp(objImp);
			cuenta.setNumeroCuenta(StringUtil.formatNumeroCuenta(numeroCuenta));
			
			String codGesCue;
			if (GenCodGes.INTERFACE.equals(recPri.getGenCodGes().getId())){
				TipObjImpAtrDefinition codGesCueTOIADef = definition.getTipObjImpAtrDefinitionByCodigo("CodGesCue");
				codGesCue = codGesCueTOIADef.getValorString();
			}else{
				codGesCue = StringUtil.formatLong(cuenta.obtenerProxCodGesCue());
			}
			cuenta.setCodGesCue(codGesCue);
			cuenta.setFechaAlta(objImp.getFechaAlta());
			
			// Si el objImp es un comercio, setea en la cuenta el estado del
			// objImp "ALTA DE OFICIO"
			if(objImp.getTipObjImp().getId().equals(TipObjImp.COMERCIO))
				cuenta.setEstObjImp(EstObjImp.getById(EstObjImp.ID_EST_ALTA_OFICIO));
			
			if(domicilioEnvio!=null){
				cuenta.setDomicilioEnvio(domicilioEnvio);
				cuenta.setDesDomEnv(domicilioEnvio.getViewDomicilio());
				cuenta.updateCatDomEnvio();
			}
			cuenta.setEstado(Estado.CREADO.getId());
			
			cuenta = PadCuentaManager.getInstance().createCuenta(cuenta);
			
			if (cuenta.hasError()) {
				log.debug(funcName + " Error al crear cuenta...");
				cuenta.passErrorMessages(objImp);
				return objImp;
			}
			
			log.debug(funcName + " cuenta creada: " + cuenta.getId());
		
			// Analisis de las acciones sobre los recursos secundarios.
			List<Recurso> listRecursoSecundario = objImp.getTipObjImp().getListRecursoSecundario();
			boolean crearCuentaSec;
			if(!ListUtil.isNullOrEmpty(listRecursoSecundario)){
				
				log.debug(funcName + " Recursos Secundarios activos asociados al Tipo Objeto Imponible: " + listRecursoSecundario.size());
				
				for(Recurso recurso: listRecursoSecundario){
										
					crearCuentaSec = false;
					// Verifica la configuracion "alta de cuenta por alta de
					// cuenta del Recurso principal asociado"
					log.debug(funcName + " Recurso: " + recurso.getDesRecurso()
							+ " alta de cuenta por alta de cuenta del Recurso principal asociado: " + 
							(recurso.getAltaCtaPorPri()==1?"Si":"No")); 
					
					if(recurso.getAltaCtaPorPri()==1){
						// Cargo el Definition de Recurso con los Atributos
						// Valorizados requeridos para la creacion.
						RecursoDefinition recursoDefinition = recurso.getDefinitionRecGenCueAtrVaValue();
						
						log.debug(funcName + " Aributos Valorizados requeridos para la creacion: " + recursoDefinition.getListGenericAtrDefinition().size());
						
						recursoDefinition.logRecursoDefinition();
						
						// Verifico si existen Atributos Valorizados requeridos
						// para la creacion.
						if(!ListUtil.isNullOrEmpty(recursoDefinition.getListGenericAtrDefinition())){
						
							// crearCuentaSec = true;
							for(GenericAtrDefinition item: recursoDefinition.getListGenericAtrDefinition()){
								
								log.debug(funcName + " Comprobando valorizacion para: " + item.getAtributo().getDesAtributo() +
													 " item.getValorString(): " + item.getValorString()); 
								
								
								TipObjImpAtrDefinition tipObjImpAtrDef = definition.getTipObjImpAtrDefinitionByIdAtributo(item.getIdDefinition());
								// Si no existe el atributo, no se cumplen las
								// condiciones para la creacion de la segunda
								// cuenta
								if(tipObjImpAtrDef==null){
									// crearCuentaSec = false;
									log.debug(funcName + " El atributo no se encuentra en la definicion del tioObjImp. fin");
									break;
								}
								// Si el Atributio no es Multivalor se compara
								// el valor del mismo, si es Multivalor
								// se recorre la lista de Valores hasta que al
								// menos uno coincida.
								if(!tipObjImpAtrDef.getEsMultivalor()){
									
									log.debug(funcName + " El atributo encontrado, no es multivalor");
									// Si existe y posee el valor requirido, se
									// cumplen las condiciones para la creacion
									// de la segunda cuenta
									if(item.getValorString().equals(tipObjImpAtrDef.getValorString())){
										crearCuentaSec = true;
										log.debug(funcName + " Valor coincidente, crearCuentaSec = true");
										break;			
									}
									log.debug(funcName + " Valor no coincidente, crearCuentaSec = false");
									
								}else{
									
									log.debug(funcName + " El atributo encontrado, Es Multivalor, size: " + tipObjImpAtrDef.getMultiValor().size());
									
									for(String strValor: tipObjImpAtrDef.getMultiValor()){
										
										for (String valorItem:item.getMultiValor()){

											if(valorItem.equals(strValor)){
												log.debug(funcName + " Valor coincidente, crearCuentaSec = true");
												crearCuentaSec = true;
												break;																			
											}
										}
										
										if(crearCuentaSec)
											break;
										
										/***************************************
										 * Codigo anterior // Si al menos uno de
										 * los valores es igual al requirido, se
										 * cumplen las condiciones para la
										 * creacion de la segunda cuenta
										 * if(item.getValorString().equals(strValor)){
										 * log.debug(funcName + " Valor
										 * coincidente, crearCuentaSec = true");
										 * crearCuentaSec = true; break; }
										 */
									}
									
									
									if(crearCuentaSec){
										log.debug(funcName + " Se creara cuenta secundaria, crearCuentaSec = true");
										break;
									} else {
										log.debug(funcName + " Ningun valor coincidente, crearCuentaSec = false");
									}
								}
							}
						}
						
					}
					if(crearCuentaSec){
						// Obtener algoritmos para la generacion de numero de
						// cuenta y codigo de gestion personal.
						// Genera el numero de cuenta (generalmente se utiliza
						// el que venga informado)
						numeroCuenta = objImp.getClave();// definition.getValClave();
						// Se arma e inserta la cuenta por Recurso Secundario.
						cuenta = new Cuenta();
						cuenta.setRecurso(recurso);
						cuenta.setObjImp(objImp);
						cuenta.setNumeroCuenta(StringUtil.formatNumeroCuenta(numeroCuenta));
						if (GenCodGes.INTERFACE.equals(recPri.getGenCodGes().getId())){
							TipObjImpAtrDefinition codGesCueTOIADef = definition.getTipObjImpAtrDefinitionByCodigo("CodGesCue");
							codGesCue = codGesCueTOIADef.getValorString();
						}else{
							codGesCue = StringUtil.formatLong(cuenta.obtenerProxCodGesCue());
						}
						cuenta.setCodGesCue(codGesCue);
						cuenta.setFechaAlta(objImp.getFechaAlta());
						// Vuelvo a crear un Domicilio de Envio en la db para la
						// cuenta a crear.
						if(domicilioEnvio != null){
							domicilioEnvio = PadDomicilioManager.getInstance().createDomicilio(domicilioEnvio);					
							if(domicilioEnvio.hasError())
								domicilioEnvio = null;
						}
						if(domicilioEnvio!=null){
							cuenta.setDomicilioEnvio(domicilioEnvio);
							cuenta.setDesDomEnv(domicilioEnvio.getViewDomicilio());
						}
						cuenta.setEstado(Estado.CREADO.getId());
						
						cuenta = PadCuentaManager.getInstance().createCuenta(cuenta);
						
						log.debug(funcName + " cuenta secundaria creada " + cuenta.getId());
					}
			
				}
			}
		}

		SiatHibernateUtil.currentSession().flush();
		
		// Calcular valor de atributo de asentamiento para Recurso DRei (Determina la distribucion de partidas para DRei y depende de los rubros habilitados)
		if(recPri.getAtributoAse() != null && Recurso.COD_RECURSO_DReI.equals(recPri.getCodRecurso())){
			TipObjImpAtr tipObjImpAtrAse = TipObjImpAtr.getByIdAtributo(recPri.getAtributoAse().getId());
			TipObjImpDefinition toidRubros = objImp.getDefinitionValue(TipObjImpAtr.ID_RUBROS_TIPO_COMERCIO);
			TipObjImpAtrDefinition toidAtrRubros = null;
			if(!ListUtil.isNullOrEmpty(toidRubros.getListTipObjImpAtrDefinition()) && toidRubros.getListTipObjImpAtrDefinition().size() == 1)
				toidAtrRubros = toidRubros.getListTipObjImpAtrDefinition().get(0);
			// El valor por defecto es cero. (Distribucion general)
			String atrAseVal = "0";
			if(toidAtrRubros != null){
				List<String> listRubros = toidAtrRubros.getListMultiValor();
				// Si el comercio se encuentra habilitado como "Bingo" (rubro: rrrr), le corresponde valor 1
				String rubroBingo = SiatParam.getString(SiatParam.RUBRO_BINGO_DREI);
				if(!StringUtil.isNullOrEmpty(rubroBingo)){
					for(String rubro: listRubros){
						if(rubroBingo.equals(rubro)){
							atrAseVal = "1";
							break;
						}
					}					
				}
				// Si el comercio no se encuentra habilitado como "Bingo" pero si en alguno de los rubros: rrrr1,rrrr2,rrrr3,rrrr4. Le corresponde valor 2
				String listRubrosAseStr = SiatParam.getString(SiatParam.LISTA_RUBROS_DREI);
				if(!"1".equals(atrAseVal) && !StringUtil.isNullOrEmpty(listRubrosAseStr)){
					for(String rubro: listRubros){
						rubro = "|" + rubro + "|";
						if (listRubrosAseStr.indexOf(rubro) >= 0) {
							atrAseVal = "2";
							break;								
						}
					}					
				}
				
			}
			// Guardar el valor de atributo de asentamiento para Drei
			ObjImpAtrVal objImpAtrVal = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(tipObjImpAtrAse.getId(), objImp.getId());
			if(objImpAtrVal == null)
				objImpAtrVal = new ObjImpAtrVal();
			objImpAtrVal.setObjImp(objImp);
			objImpAtrVal.setTipObjImpAtr(tipObjImpAtrAse);
			objImpAtrVal.setStrValor(atrAseVal);
			objImpAtrVal.setFechaDesde(objImp.getFechaAlta());
			objImpAtrVal.setFechaHasta(null);
			objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
			
			objImp.createObjImpAtrVal(objImpAtrVal);	
		}
		
		// Emito deuda para cada cuenta creada desde la fecha de alta de las
		// cuentas hasta el ultimo periodo emitido del recurso
		if (objImp.getTipObjImp().equals(TipObjImp.getById(TipObjImp.COMERCIO))){
			for (Cuenta cta: objImp.getListCuentaActiva()){
				cta = GdeGDeudaManager.getInstance().createDeudaAdminFromCreateCuentaObjImp(cta);
				if (cta.hasError())
					cta.passErrorMessages(objImp);
			}
		}
		
		return objImp;
	}

	/**
	 * Alta de un objeto imponible con sus atributos valorizados. (Para
	 * M&oacute;dulo de Migraci&oacute;n)
	 * 
	 * @param objImp -
	 *            Objeto imponible instanciado con los clave, claveFunc y
	 *            fechaAlta valorizados.
	 * @param definition -
	 *            Objeto definicion de TipObjImp con los valores de atributos
	 *            cargados.
	 * @param objImpFile -
	 *            LogFile en el cual se inserta el ObjImp para hacer el load.
	 * @param objImpAtrValFile -
	 *            LogFile en el cual se insertan los ObjImpAtrVal para hacer el
	 *            load.
	 */
	public ObjImp createObjImpForMigrania(ObjImp objImp,
			TipObjImpDefinition definition, LogFile objImpFile,
			LogFile objImpAtrValFile) throws Exception {

		// PadDAOFactory.getObjImpDAO().createJdbc(objImp);
		// PadDAOFactory.getObjImpDAO().update(objImp);
		PadDAOFactory.getObjImpDAO().createForLoad(objImp, objImpFile);

		// por cada atributo de definition
		// instancia ObjImpAtrVal, seta valor unico (porque es alta)
		// seta objimp padre
		// objImp.createObjImpAtrVal(objImpAtrVal)
		if (definition != null) {
			for (TipObjImpAtrDefinition tpid : definition
					.getListTipObjImpAtrDefinition()) {
				if (!tpid.getValorString().equals("")) {
					ObjImpAtrVal objImpAtrVal = new ObjImpAtrVal();
					objImpAtrVal.setObjImp(objImp);
					objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(tpid
							.getTipObjImpAtr().getId()));
					objImpAtrVal.setStrValor(tpid.getValorString());
					if (tpid.getPoseeVigencia()) {
						if ((tpid.getFechaDesde() != null)
								&& (tpid.getFechaNovedad() != null)) {
							objImpAtrVal.setFechaDesde(tpid.getFechaDesde());
							objImpAtrVal.setFechaHasta(tpid.getFechaHasta());
							objImpAtrVal
									.setFechaNovedad(tpid.getFechaNovedad());
						} else {
							objImpAtrVal.setFechaDesde(objImp.getFechaAlta());
							objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
						}
					} else {
						if (tpid.getFechaNovedad() != null)
							objImpAtrVal
									.setFechaNovedad(tpid.getFechaNovedad());
						else
							objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
					}
					// PadDAOFactory.getObjImpAtrValDAO().createJdbc(objImpAtrVal);
					// objImp.createObjImpAtrVal(objImpAtrVal);
					PadDAOFactory.getObjImpAtrValDAO().createForLoad(
							objImpAtrVal, objImpAtrValFile);

				}
			}
		}

		return objImp;
	}

	/**
	 * Crea un objeto imponible con sus atributos transaccionalmente Este metodo
	 * es usado desde la migracion de objetos imponibles de GRE
	 */
	public ObjImp createObjImpMigra(ObjImp objImp,
			TipObjImpDefinition definition, LogFile objImpFile,
			LogFile objImpAtrValFile) throws Exception {

		// PadDAOFactory.getObjImpDAO().createForLoad(objImp, objImpFile);

		// realiza el alta del objimp por jdbc
		PadDAOFactory.getObjImpDAO().update(objImp);
		// por cada atributo de definition
		// instancia ObjImpAtrVal, seta valor unico (porque es alta)
		// seta objimp padre
		// objImp.createObjImpAtrVal(objImpAtrVal)
		if (definition != null) {
			for (TipObjImpAtrDefinition tpid : definition
					.getListTipObjImpAtrDefinition()) {
				if (!tpid.getValorString().equals("")) {
					ObjImpAtrVal objImpAtrVal = new ObjImpAtrVal();
					objImpAtrVal.setObjImp(objImp);
					objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(tpid
							.getTipObjImpAtr().getId()));
					objImpAtrVal.setStrValor(tpid.getValorString());
					if (tpid.getPoseeVigencia()) {
						if ((tpid.getFechaDesde() != null)
								&& (tpid.getFechaNovedad() != null)) {
							objImpAtrVal.setFechaDesde(tpid.getFechaDesde());
							objImpAtrVal.setFechaHasta(tpid.getFechaHasta());
							objImpAtrVal
									.setFechaNovedad(tpid.getFechaNovedad());
						} else {
							objImpAtrVal.setFechaDesde(objImp.getFechaAlta());
							objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
						}
					} else {
						if (tpid.getFechaNovedad() != null)
							objImpAtrVal
									.setFechaNovedad(tpid.getFechaNovedad());
						else
							objImpAtrVal.setFechaNovedad(objImp.getFechaAlta());
					}
					PadDAOFactory.getObjImpAtrValDAO().update(objImpAtrVal);
					// objImp.createObjImpAtrVal(objImpAtrVal);
					// PadDAOFactory.getObjImpAtrValDAO().createForLoad(objImpAtrVal,
					// objImpAtrValFile);

				}
			}
		}

		return objImp;
	}

	public ObjImp updateObjImp(ObjImp objImp) throws Exception {

		// Validaciones de negocio
		if (!objImp.validateUpdate()) {
			return objImp;
		}

		PadDAOFactory.getObjImpDAO().update(objImp);

		return objImp;
	}

	public ObjImp deleteObjImp(ObjImp objImp) throws Exception {

		// Validaciones de negocio
		if (!objImp.validateDelete()) {
			return objImp;
		}

		// Borro los Atributos valorizados del objeto imponible
		for (ObjImpAtrVal objImpAtrVal : objImp.getListObjImpAtrVal()) {
			PadDAOFactory.getObjImpAtrValDAO().delete(objImpAtrVal);
		}

		// borro el objeto imponible
		PadDAOFactory.getObjImpDAO().delete(objImp);

		return objImp;
	}

	/**
	 * Segun el contenido de los atributos de ObjImp recibido, da de
	 * alta o baja la cuenta para el recurso secundario.
	 * 
	 * @param objImp
	 * @param fechaNovedad
	 * @return
	 * @throws Exception
	 */
	public ObjImp altaBajaCuentaSecComercio(ObjImp objImp, Date fechaNovedad) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		int ALTAOP = 1, BAJAOP = 2, NOP = 0;
		int op = NOP; //determina la operacion a realizar luego del analisis de los atributos informados

		Recurso recursoEtur = Recurso.getByCodigo(Recurso.COD_RECURSO_ETuR);
		Recurso recursoDrei = Recurso.getByCodigo(Recurso.COD_RECURSO_DReI);
		Cuenta cuentaDrei = objImp.getCuentaByRecurso(recursoDrei);
		Cuenta cuentaEtur = objImp.getCuentaByRecurso(recursoEtur);

		// Cargo el Definition de Recurso con los Atributos Valorizados
		// requeridos para la creacion.
		RecursoDefinition recursoDefinition = recursoEtur.getDefinitionRecGenCueAtrVaValue();
		TipObjImpDefinition def = objImp.getDefinitionValue(fechaNovedad);

		log.debug(funcName + " Aributos Valorizados requeridos para la creacion: " 
				+ recursoDefinition.getListGenericAtrDefinition().size());

		recursoDefinition.logRecursoDefinition();

		// Verifico si existen Atributos Valorizados requeridos para la creacion.
		// este bloque determoina si es necesaria una operacion de ALTA
		if (cuentaEtur == null && !ListUtil.isNullOrEmpty(recursoDefinition.getListGenericAtrDefinition())) {
			// crearCuentaSec = true;
			for (GenericAtrDefinition item : recursoDefinition.getListGenericAtrDefinition()) {
				log.debug(funcName + " Comprobando valorizacion para: "	+ item.getAtributo().getDesAtributo() + " item.getValorString(): " + item.getValorString());

				// Si no existe el atributo, no se cumplen las condiciones para
				// la creacion de la segunda cuenta
				TipObjImpAtrDefinition tipObjImpAtrDef = def.getTipObjImpAtrDefinitionByIdAtributo(item.getIdDefinition());
				if (tipObjImpAtrDef == null) {
					log.debug(funcName + " El atributo no se encuentra en la definicion. Lo ignoramos");
					continue;
				}

				// Si el Atributio no es Multivalor se compara el valor del mismo, si es Multivalor se 
				// recorre la lista de Valores hasta que al menos uno coincida.
				if (!tipObjImpAtrDef.getEsMultivalor()) {
					// Si existe y posee el valor requirido, se cumplen las
					// condiciones para la creacion de la segunda cuenta
					log.debug(funcName + " El atributo encontrado, no es multivalor");
					if (item.getValorString().equals(tipObjImpAtrDef.getValorString())) {
						op = ALTAOP;
						log.debug(funcName + " Valor coincidente, crearCuentaSec = true");
						break;
					}
					log.debug(funcName + " Valor no coincidente, crearCuentaSec = false");

				} else {
					log.debug(funcName + " El atributo encontrado, Es Multivalor, size: " + tipObjImpAtrDef.getMultiValor().size());

					for(int i : tipObjImpAtrDef.getMultiValorIndex(fechaNovedad)) {
						String strValor = tipObjImpAtrDef.getMultiValor(i);
						Date multiFechaHasta = tipObjImpAtrDef.getMultiFechaHasta(i);

						if (multiFechaHasta != null) continue; // los que tiene fecha baja toman parte en el analisis de baja.

						for (String valorItem : item.getMultiValor()) {
							if (valorItem.equals(strValor)) {
								log.debug(funcName + " Valor coincidente, crearCuentaSec = true");
								op = ALTAOP;
								break;
							}
						}

						if (op != NOP) break;
					}

					if (op != NOP) break;
				}
			}
		}

		// Verifico si existen Atributos Valorizados requeridos para la Baja.
		// este bloque determina si es necesaria una operacion de BAJA
		if (cuentaEtur != null && !ListUtil.isNullOrEmpty(recursoDefinition.getListGenericAtrDefinition())) {
			// dada la forma en la que se informan las modificaciones de atributos simples, no es
			// posible informar una 'baja' de atributo simple.
			// por lo que solo hacemos el analisis de baja para los multivalores. 
			// (que por ahora para el caso comercio es lo que se necesita)
			log.debug(funcName + " Analisis de Baja...");
			for (GenericAtrDefinition item : recursoDefinition.getListGenericAtrDefinition()) {				
				
				// Si no existe el atributo, no se cumplen las condiciones para la creacion de la segunda cuenta
				TipObjImpAtrDefinition atrDef = def.getTipObjImpAtrDefinitionByIdAtributo(item.getIdDefinition());
				if (atrDef == null) {
					log.debug(funcName + " El atributo no se encuentra en la definicion. Lo ignoramos");
					continue;
				}

				
				log.debug("Analizando atrDef.valor: " + atrDef.getValorView() + " atrdef.idDefinition:" + atrDef.getIdDefinition());
				for (String valor : atrDef.getMultiValor()) {
					log.debug("Analizando atrDef.multiValor: " + valor + " atrdef.idDefinition:" + atrDef.getIdDefinition());
				}


				// Si el Atributio no es Multivalor se compara el valor del mismo, si es Multivalor se 
				// recorre la lista de Valores hasta que al menos uno coincida.
				if (atrDef.getEsMultivalor()) {
					Date fanalisis = DateUtil.addDaysToDate(fechaNovedad, 1); 
					// sumamos un dia, porque sino getMultiValorIndex() me va a retornar 
					// siempre el que estoy dando de baja. (retorna un intervalo que incluye a fanalisis)
					List<Integer> atrsVigente = atrDef.getMultiValorIndex(fanalisis);
					for(int i : atrsVigente) {
						log.debug(String.format("atr v:%s, fd:%s, fh:%s, fn:%s", 
								atrDef.getMultiValor(i),
								atrDef.getMultiFechaDesde(i),
								atrDef.getMultiFechaHasta(i),
								atrDef.getMultiFechaNovedad(i)));

					}

					//ahora recorremos la lista de los vigentes y buscamos si hay alguno de los requeridos para cta sec.
					//si no econtramos ninguno, es que hay que darla de baja.
					op = BAJAOP;
					for(int i : atrsVigente) {
						String vvig = atrDef.getMultiValor(i);
						for (String vreq : item.getMultiValor()){
							if (vvig.equals(vreq)) { op = NOP; break; } //si hay alguno vigente, requerido para la cta sec. no darlo de baja
						}
					}
					
					log.debug(funcName + " Analisis de Baja: Atributos Multivalor vigentes un dia posterior a fechaNovedad: " + atrsVigente.size());
				}
			}
		}
		log.debug(funcName + " creara cuenta secundaria? (0 nada, 1 alta, 2 baja)?, op=" + op);

		// Crear cuenta Etur si no existe.
		if (op == ALTAOP) {
			// Obtener algoritmos para la generacion de numero de cuenta
			// y codigo de gestion personal.
			// Genera el numero de cuenta (generalmente se utiliza el
			// que venga informado)
			String codGesCue = StringUtil.formatLong(cuentaDrei.obtenerProxCodGesCue());
			String numeroCuenta = StringUtil.formatNumeroCuenta(cuentaDrei.getNumeroCuenta());

			Cuenta cuenta = cuentaDrei.createCuentaSecundaria(recursoEtur, codGesCue, numeroCuenta, fechaNovedad);
			log.debug(funcName + " Cuenta secundaria creada " + cuenta.getId());
						
			if (objImp.getTipObjImp().equals(TipObjImp.getById(TipObjImp.COMERCIO))) {
				cuenta = GdeGDeudaManager.getInstance().createDeudaAdminFromCreateCuentaObjImp(cuenta);
				if (cuenta.hasError()) {
					cuenta.passErrorMessages(objImp);
				}
			}
		}

		if (op == BAJAOP) {
			// Si existe cuenta Etur, darla de baja
			if (cuentaEtur!=null && !(null != cuentaEtur.getEstCue() && cuentaEtur.getEstCue().getId().equals(EstCue.ID_ALTA_CUENTA_DECLARADA))) {
				cuentaEtur.setFechaBaja(fechaNovedad);
				PadCuentaManager.getInstance().updateCuenta(cuentaEtur);
				log.debug(funcName + " cuenta secundaria dada de baja " + cuentaEtur.getId());
			}
		}

		return objImp;
	}
	
	// ---> ABM AltaOficio
	public AltaOficio createAltaOficio(AltaOficio altaOficio) throws Exception {

		// Validaciones de negocio
		if (!altaOficio.validateCreate()) {
			return altaOficio;
		}

		PadDAOFactory.getAltaOficioDAO().update(altaOficio);

		return altaOficio;

	}

	public AltaOficio updateAltaOficio(AltaOficio altaOficio) throws Exception {

		// Validaciones de negocio
		if (!altaOficio.validateUpdate()) {
			return altaOficio;
		}

		PadDAOFactory.getAltaOficioDAO().update(altaOficio);

		return altaOficio;

	}

	/**
	 * Borra las cuentas titulares, la cuenta, el objImp (con sus atributos
	 * valorizados) y el altaOficio
	 * 
	 * @param altaOficio
	 * @return
	 * @throws Exception
	 */
	public AltaOficio deleteAltaOficio(AltaOficio altaOficio) throws Exception {

		// Validaciones de negocio
		if (!altaOficio.validateDelete()) {
			return altaOficio;
		}

		ObjImp objImpComercio = altaOficio.getObjImp();

		// Obtiene la cuenta de DreI
		Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI()
				.getId(), altaOficio.getObjImp().getClave());

		// borra las cuentas titulares de DreI
		for (CuentaTitular c : cuenta.getListCuentaTitular()) {
			c.setCuenta(null);
			c = cuenta.deleteCuentaTitular(c);
			if (c.hasError()) {
				c.passErrorMessages(altaOficio);
				return altaOficio;
			}
		}
		SiatHibernateUtil.currentSession().flush();
		log.debug("Borro las cuenta titulares de DReI");

		// borra la cuenta
		cuenta.setListCuentaTitular(null);
		cuenta = PadCuentaManager.getInstance().deleteCuenta(cuenta);
		if (cuenta.hasError()) {
			cuenta.passErrorMessages(altaOficio);
			return altaOficio;
		}
		SiatHibernateUtil.currentSession().flush();
		log.debug("Borro la cuenta de DReI");

		// Obtiene la cuenta de ETur
		cuenta = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getETur().getId(),
				altaOficio.getObjImp().getClave());
		if (cuenta != null) {
			// borra las cuentas titulares de DreI
			for (CuentaTitular c : cuenta.getListCuentaTitular()) {
				c.setCuenta(null);
				c = cuenta.deleteCuentaTitular(c);
				if (c.hasError()) {
					c.passErrorMessages(altaOficio);
					return altaOficio;
				}
			}
			SiatHibernateUtil.currentSession().flush();
			log.debug("Borro las cuenta titulares de ETur");

			// borra la cuenta
			cuenta.setListCuentaTitular(null);
			cuenta = PadCuentaManager.getInstance().deleteCuenta(cuenta);
			if (cuenta.hasError()) {
				cuenta.passErrorMessages(altaOficio);
				return altaOficio;
			}
			SiatHibernateUtil.currentSession().flush();
			log.debug("Borro la cuenta ETur");
		} else {
			log.debug("No tiene cuenta ETur");
		}

		// Borro los Atributos valorizados del objeto imponible
		for (ObjImpAtrVal objImpAtrVal : objImpComercio.getListObjImpAtrVal()) {
			PadDAOFactory.getObjImpAtrValDAO().delete(objImpAtrVal);
		}
		SiatHibernateUtil.currentSession().flush();
		log.debug("Borro los atributos del objImp");

		// borra la referencia del altaOficio al objImp lo borra
		PadDAOFactory.getObjImpDAO().delete(objImpComercio);
		SiatHibernateUtil.currentSession().flush();
		if (objImpComercio.hasError()) {
			objImpComercio.passErrorMessages(altaOficio);
			return altaOficio;
		}
		log.debug("Borro el objImp");

		// borra el altaOficio
		PadDAOFactory.getAltaOficioDAO().delete(altaOficio);
		log.debug("Borro el AltaOficio");

		return altaOficio;
	}

	// <--- ABM AltaOficio

	// ---> ABM CierreComercio

	public CierreComercio updateCierreComercio(CierreComercio cierreComercio)
			throws Exception {

		// Validaciones de negocio
		if (!cierreComercio.validateUpdate()) {
			return cierreComercio;
		}

		PadDAOFactory.getAltaOficioDAO().update(cierreComercio);

		return cierreComercio;

	}

	// <--- ABM CierreComercio
}
