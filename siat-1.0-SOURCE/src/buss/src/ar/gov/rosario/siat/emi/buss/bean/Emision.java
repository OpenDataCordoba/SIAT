//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;
import org.hibernate.NonUniqueResultException;

import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.QryTable;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Calendario;
import ar.gov.rosario.siat.def.buss.bean.CodEmi;
import ar.gov.rosario.siat.def.buss.bean.PeriodoDeuda;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipCodEmi;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.GenericDefinition;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.bean.TipoSujeto;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a Emision
 * 
 * @author tecso
 */
@Entity
@Table(name = "emi_emision")
public class Emision extends BaseBO {
	
	@Transient
	private Logger log = Logger.getLogger(Emision.class);

	private static final long serialVersionUID = 1L;
	
	public static final String SIAT_PARAM_ALFAX = "EMISION_ALFAX"; 
	
	public static final String ADP_PARAM_ID ="idEmision";
	public static final String ADP_PARAM_ANIO = "anio";
	public static final String ADP_PARAM_PERIODO ="periodo";

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idtipoemision") 
	private TipoEmision tipoEmision;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@ManyToOne(optional=true, fetch=FetchType.EAGER) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;

	@Column(name="anio")
	private Integer anio;

	@Column(name="periodoDesde")
	private Integer periodoDesde;

	@Column(name="periodoHasta")
	private Integer periodoHasta;

	@Column(name="cantDeuPer")
	private Integer cantDeuPer;
	
	@Column(name="fechaemision")
	private Date fechaEmision;

	@Column(name="observacion")
	private String observacion;
	
    @Column(name="idCaso") 
	private String idCaso;

	@ManyToOne() 
    @JoinColumn(name="idcorrida")
	private Corrida corrida;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idatributo")
	private Atributo atributo;
	
	@Column(name="valor")
	private String valor;
	
	@OneToMany(mappedBy="emision", fetch=FetchType.LAZY)
	@JoinColumn(name="idEmision")
	private List<EmiValEmiMat> listEmiValEmiMat;

	@OneToMany(mappedBy="emision", fetch=FetchType.LAZY)
	@JoinColumn(name="idEmision")
	private List<AuxDeuda> listAuxDeuda;

	@Transient
	private Map<String, RecCon> mapCodRecCon; 

	@Transient
	private ScriptEngineManager engineManager;

	@Transient
	private ScriptEngine engine;
	
	@Transient
	private String codigo;
	
	@Transient
	private StringWriter stringWriter;
	
	// Constructores
	public Emision(){
		super();
	}
	
	public Emision(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Emision getById(Long id) {
		return (Emision) EmiDAOFactory.getEmisionDAO().getById(id);
	}
	
	public static Emision getByIdNull(Long id) {
		return (Emision) EmiDAOFactory.getEmisionDAO().getByIdNull(id);
	}
	
	public static List<Emision> getList() {
		return (ArrayList<Emision>) EmiDAOFactory.getEmisionDAO().getList();
	}
	
	public static List<Emision> getListActivos() {			
		return (ArrayList<Emision>) EmiDAOFactory.getEmisionDAO().getListActiva();
	}

	// Getters y setters
	public TipoEmision getTipoEmision() {
		return tipoEmision;
	}

	public void setTipoEmision(TipoEmision tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getCantDeuPer() {
		return cantDeuPer;
	}

	public void setCantDeuPer(Integer cantDeuPer) {
		this.cantDeuPer = cantDeuPer;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	public Atributo getAtributo() {
		return atributo;
	}

	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public List<EmiValEmiMat> getListEmiValEmiMat() {
		return listEmiValEmiMat;
	}

	public void setListEmiValEmiMat(List<EmiValEmiMat> listEmiValEmiMat) {
		this.listEmiValEmiMat = listEmiValEmiMat;
	}
	
	public List<AuxDeuda> getListAuxDeuda() {
		return listAuxDeuda;
	}

	public void setListAuxDeuda(List<AuxDeuda> listAuxDeuda) {
		this.listAuxDeuda = listAuxDeuda;
	}

	// Validaciones para pruebas desde el mantenedor de
	// logica expuesta.
	public boolean validateCreateForTest() throws Exception {
		// limpiamos la lista de errores
		clearError();

		return this.validate();
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones extras: para emisiones individuales o extraordinarias.
		if (this.getTipoEmision().getId().equals(TipoEmision.ID_INDIVIDUAL) || 
			this.getTipoEmision().getId().equals(TipoEmision.ID_EXTRAORDINARIA)) {
		
			// Si no es un recurso con periodicidad ESPORADICO,
			// validamos que no exista la deuda a emitir
			if (!recurso.getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO)) {
				for (int periodo = periodoDesde; periodo <= periodoHasta; periodo++) {
					String errorMsg = null;
					try { 
						if (Deuda.existeOriginal(getCuenta(), Long.valueOf(periodo), Long.valueOf(anio))) {
							errorMsg = "Ya existe deuda original para el periodo: " + periodo + "/" + anio;
							this.addRecoverableValueError(errorMsg);
						}
					} catch (NonUniqueResultException e) {
						errorMsg = "Deuda original duplicada para el periodo: " + periodo + "/" + anio;
						this.addRecoverableValueError(errorMsg);
					}
				}
			}
			
			if (hasError()) {
				return false;
			}
		}
		
		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		if (!EstadoCorrida.ID_EN_PREPARACION.equals(this.getCorrida().getEstadoCorrida().getId())) {
			addRecoverableError(EmiError.EMISION_ESTADO_INVALIDO);			
		}

		if (hasError()) {
			return false;
		}

		return true;		
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();
		
		if (this.getTipoEmision().getId().equals(TipoEmision.ID_MASIVA)) {
			// Solo se puede eliminar si el estado de la emision es "En Preparacion".
			Long idEstadoCorrida = this.getCorrida().getEstadoCorrida().getId();
			if (!EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida)){
				addRecoverableError(EmiError.EMISION_ELIMINAR_NO_PERMITIDO);
			}
		}
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		} else if (!getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG) && 
				   	!getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP)) {

			if (getTipoEmision() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOEMISION_LABEL);
			}
			
			if (getRecurso() != null) {
				// Validamos que este configurado el servicio banco
				Sistema sistema = Sistema.getSistemaEmision(getRecurso());
				if (sistema == null || sistema.getServicioBanco() == null) {
					addRecoverableError(EmiError.EMISION_ERROR_SERBANREC);
				}
				
				// Validamos que exista una clasificacion de deuda vigenete
				RecClaDeu recClaDeu = recurso.getRecClaDeuOriginal(new Date());
				if (recClaDeu == null) {
					addRecoverableError(EmiError.EMISION_ERROR_RECCLADEU);
				}
			}
			
			if (getFechaEmision() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
						EmiError.EMISION_FECHAEMISION);
			}
			
			if (!getRecurso().getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO)) {
	
				if (getAnio() == null) {
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISION_ANIO);
				}

				if (getAnio() != null && (getAnio() < 1900 || getAnio() > 3000)) {
					addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EmiError.EMISION_ANIO);
				}
		
				if (getPeriodoDesde() == null) {
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.RESLIQDEU_PERIODO_DESDE);
				}
		
				if (getPeriodoDesde() != null && getRecurso() != null &&
						!getRecurso().validatePeriodo(getPeriodoDesde())) {
					addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EmiError.RESLIQDEU_PERIODO_DESDE);
				}
				
				if (getPeriodoHasta() == null) {
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.RESLIQDEU_PERIODO_HASTA);
				}
		
				if (getPeriodoHasta() != null && getRecurso() != null &&
						!getRecurso().validatePeriodo(getPeriodoHasta())) {
					addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EmiError.RESLIQDEU_PERIODO_HASTA);
				}
				
				if (getPeriodoDesde() != null && getPeriodoHasta() != null && (getPeriodoDesde() > getPeriodoHasta())) {
					addRecoverableError(BaseError.MSG_VALORMAYORQUE, EmiError.RESLIQDEU_PERIODO_DESDE, EmiError.RESLIQDEU_PERIODO_HASTA);
				}
			}
		} else {
			if (getTipoEmision() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOEMISION_LABEL);
			}
			
			if (getRecurso() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
			}
			
			if (getFechaEmision() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISION_FECHAEMISION);
			}
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	public DeudaAdmin createDeudaAdminFromAuxDeuda(AuxDeuda auxDeuda, Cuenta cuentaCDM) throws Exception {

		DeudaAdmin deudaAdmin = new DeudaAdmin();
		
		deudaAdmin.setCodRefPag(auxDeuda.getCodRefPag());
		deudaAdmin.setCuenta(cuentaCDM);
		deudaAdmin.setRecClaDeu(auxDeuda.getRecClaDeu());
		deudaAdmin.setViaDeuda(auxDeuda.getViaDeuda());
		deudaAdmin.setEstadoDeuda(auxDeuda.getEstadoDeuda());
		deudaAdmin.setAnio(auxDeuda.getAnio());
		deudaAdmin.setPeriodo(auxDeuda.getPeriodo());
		deudaAdmin.setFechaEmision(auxDeuda.getFechaEmision());
		deudaAdmin.setFechaVencimiento(auxDeuda.getFechaVencimiento());
		deudaAdmin.setImporte(auxDeuda.getImporte());
		deudaAdmin.setImporteBruto(auxDeuda.getImporteBruto());
		deudaAdmin.setSaldo(auxDeuda.getSaldo());
		deudaAdmin.setActualizacion(auxDeuda.getActualizacion());
		deudaAdmin.setSistema(auxDeuda.getSistema());
		deudaAdmin.setResto(auxDeuda.getResto());
		deudaAdmin.setRepartidor(auxDeuda.getRepartidor());
		deudaAdmin.setEstaImpresa(0);
		
		// Validaciones de negocio
		if (!deudaAdmin.validateCreate()) {
			return deudaAdmin;
		}

		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);

		// obtener la lista de los RecCon del recurso
		//List<RecCon> listRecCon = cuentaCDM.getRecurso().getListRecCon();
		
		// crear los conceptos
		// capital
		DeuAdmRecCon deuAdmRecCon = new DeuAdmRecCon();
		deuAdmRecCon.setDeuda(deudaAdmin);

		// de la lista tomamos el que tiene codigo=Capital
		//RecCon recConCapital = new RecCon(); // en lugar de esto, iterar la lista y tomar capital
		
		RecCon recConCapital=null;
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP))
			recConCapital = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_CAPITAL_PAV);
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG))
			recConCapital = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_CAPITAL_GAS);
		deuAdmRecCon.setRecCon(recConCapital );
		deuAdmRecCon.setImporte(auxDeuda.getConc1());
		deuAdmRecCon.setImporteBruto(auxDeuda.getConc1());
		deuAdmRecCon.setSaldo(auxDeuda.getConc1());
		
		// update
		deudaAdmin.createDeuAdmRecCon(deuAdmRecCon);
		
		// interes
		DeuAdmRecCon deuAdmRecCon2 = new DeuAdmRecCon();
		deuAdmRecCon2.setDeuda(deudaAdmin);
		// de la lista tomamos el que tiene codigo=Capital
		RecCon recConInteres=null;
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP))
			recConInteres = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_INTERES_PAV);
		if (cuentaCDM.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG))
			recConInteres = RecCon.getByIdRecursoAndCodigo(cuentaCDM.getRecurso().getId(), RecCon.COD_INTERES_GAS);
		deuAdmRecCon2.setRecCon(recConInteres);
		deuAdmRecCon2.setImporteBruto(auxDeuda.getConc2());
		deuAdmRecCon2.setImporte(auxDeuda.getConc2());
		deuAdmRecCon2.setSaldo(auxDeuda.getConc2());
		
		// update
		deudaAdmin.createDeuAdmRecCon(deuAdmRecCon2);
		
		return deudaAdmin;
	}

	public void initializeMapCodRecCon() {
		mapCodRecCon = new HashMap<String, RecCon>();
		Recurso recurso = this.getRecurso();
		List<RecCon> listRecCon = RecCon.getListVigentesByIdRecurso(recurso.getId(), null, new Date());
		
		for (RecCon conc: listRecCon) {
			mapCodRecCon.put(conc.getOrdenVisualizacion().toString(), conc);
		}
	}
	
	public void reiniciarCorrida() throws Exception{
		
		// borrar el archivo ALFAX generado en el paso 1
		this.deleteALFAX();

		// borrar los archivos cal_defi y det_cal_defi de ENTRADA, PROCESADO_ERROR Y PROCESADO_OK
		this.deleteArchivosCalDefi();
		
		// borrar los registros AuxDeuda
		EmiDAOFactory.getAuxDeudaDAO().deleteAuxDeudaByIdEmision(this);
		
		// reinicia la corrida a traves de ADP
		this.getCorrida().reiniciar();
	}

	private void deleteALFAX() throws Exception{
		List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(this.getCorrida(), 1);
		for (FileCorrida fileCorrida : listFileCorrida) {
			this.getCorrida().deleteFileCorrida(fileCorrida);
		}
	}
	
	private void deleteArchivosCalDefi() throws Exception{
		// recupero la corrida
		AdpRun run = AdpRun.getRun(this.getCorrida().getId());
        if(run == null){
        	//log.error("no se pudo obtener la Corrida");
        	this.addRecoverableValueError("No se pudo obtener la Corrida");
			return;
		}
        String idZona = run.getParameter(Zona.ID_ZONA);
		
		// borra los archivos calc_defi y det_calc_defi de ENTRADA, PROCESADO_ERROR Y PROCESADO_OK
    	// Constantes para la emision ALFAX
    	 String BASE_FILE_NAME_CAL_DEFI     = "calc_defi.z";
    	 String BASE_FILE_NAME_DET_CAL_DEFI = "det_calc_defi.z";

		String fileNameCalDefi    = BASE_FILE_NAME_CAL_DEFI + idZona;
		String fileNameDetCalDefi = BASE_FILE_NAME_DET_CAL_DEFI + idZona;
		
		String inputDir = run.getProcessDir(AdpRunDirEnum.ENTRADA); // "/mnt/siat/EmisionTgi/entrada"
		this.deleteFile(inputDir+ File.separator +fileNameCalDefi);
		this.deleteFile(inputDir+ File.separator +fileNameDetCalDefi);
		inputDir = run.getProcessDir(AdpRunDirEnum.PROCESADO_ERROR);
		this.deleteFile(inputDir+ File.separator +fileNameCalDefi);
		this.deleteFile(inputDir+ File.separator +fileNameDetCalDefi);
		inputDir = run.getProcessDir(AdpRunDirEnum.PROCESADO_OK);
		this.deleteFile(inputDir+ File.separator +fileNameCalDefi);
		this.deleteFile(inputDir+ File.separator +fileNameDetCalDefi);
	}
	
	private void deleteFile(String fileNameCompleto){
		File file = new File(fileNameCompleto);
		if(file.exists()){
			file.delete();
		}
	}

	/**
	 *	Retorna true si y solo si esta activada 
	 * 	la emision de TGI con ALFAX 
	 */
	public static boolean isAlfaxParamEnabled() {
		try {
			 return SiatParam.getInteger(SIAT_PARAM_ALFAX) == 1;
		
		} catch (Exception e) {
			return false;
		}
	}
	
	/** 
	 * Copia la lista de matrices de parametros asociadas al recurso
	 *  
	 */
	public void copiarListEmiMat() throws Exception {
		try  {
			List<ValEmiMat> listMatrices = ValEmiMat.getListBy(this.getRecurso()); 
		    
			for (ValEmiMat mat: listMatrices) {
		    	EmiValEmiMat emiValEmiMat = new EmiValEmiMat();
		    	emiValEmiMat.setEmiMat(mat.getEmiMat());
		    	emiValEmiMat.setValores(mat.getValores());
		    	emiValEmiMat.setEmision(this);
		    	
		    	EmiEmisionManager.getInstance().createEmiValEmiMat(emiValEmiMat);
		    }
		} catch (Exception e) {
			String errorMsg = "No se pudieron copiar las tablas de parametros";
			log.error(errorMsg);
			throw new Exception(errorMsg);
		}
	}

	/** 
	 * Elimina la lista de matrices de parametros asociadas a la emision
	 *  
	 */
	public void deleteListEmiMat() throws Exception {
		try  {
			for (EmiValEmiMat m: this.getListEmiValEmiMat()) {
				EmiEmisionManager.getInstance().deleteEmiValEmiMat(m);
			}
		} catch (Exception e) {
			String errorMsg = "No se pudieron eliminar las tablas de parametros";
			log.error(errorMsg);
			throw new Exception(errorMsg);
		}
	}

	/** 
	 * Elimina la lista deudas auxiliares relacionadas
	 *  
	 */
	public void deleteListAuxDeuda() throws Exception {
		try  {
			for (AuxDeuda auxDeuda: this.getListAuxDeuda()) {
				this.deleteAuxDeuda(auxDeuda);
			}
		} catch (Exception e) {
			String errorMsg = "No se pudieron eliminar las deudas auxiliares";
			log.error(errorMsg);
			throw new Exception(errorMsg);
		}
	}

	
	/**
	 * Inicializa el engine de scripting para 
	 * efectuar los calculos 
	 */
	public void ininitializaEngine(String codigo) throws Exception {
		try {
			// Inicializamos el engine
			this.engineManager = new ScriptEngineManager();
			this.engine = engineManager.getEngineByName("JavaScript");

			// Seteamos el codigo del calculo
			this.codigo = codigo;
			
			// Evaluamos las librerias
			List<CodEmi> listCodEmi = CodEmi.getListActivosBy(TipCodEmi.ID_LIBRERIA);
			for (CodEmi lib: listCodEmi) {
				this.engine.eval(lib.getCodigo());
			}
			
			// Copiamos las matrices de parametros
			List<ValEmiMat> listMatrices = ValEmiMat.getListBy(this.getRecurso()); 
			for (ValEmiMat matriz: listMatrices) {
				// Obtenemos la matriz
				String valores = matriz.getValores();
				String [] data = valores.split(";");
				// Obtenemos el header
				String header = data[0];
				// Obtenemos el body
				String body = data[1];
				
				String key   = matriz.getEmiMat().getCodEmiMat();
				QryTable tab = new QryTable(header, body);
				this.engine.put(key, tab);
			}

			// TODO Generalizar creando un wrapper de JDBC
			//		que pueda ser utilizado desde el engine
			//		de javascript.

			// Seteamos los algoritmos de vencimientos
			this.engine.put("vencimiento", new Vencimiento());

			// Seteamos los algoritmos de calendarios.
			this.engine.put("calendario", new Calendario());
			
			// Seteamos el buffer de log
			this.stringWriter = new StringWriter();
			
			if (this.getRecurso().getTipObjImp() != null) {  
				this.toid = this.getRecurso().getTipObjImp().getDefinitionForAll();
			} else {
				this.toid = TipObjImp.getByCodigo("PARCELA").getDefinitionForManual();
			}
			
			if (!ListUtil.isNullOrEmpty(this.getRecurso().getListRecAtrCue())) {
				this.recAtrCueDef = new RecAtrCueDefinition();
			}
			
		} catch (Exception e) {
			String errorMsg = "No se pudo inicializar el engine";
			throw new DemodaServiceException(errorMsg + e.getMessage(), e);
		}
	}
	
	public void addTable(String tabName, String strTable) {

		// Obtenemos la matriz
		String valores = strTable;
		String [] data = valores.split(";");
		// Obtenemos el header
		String header = data[0];
		// Obtenemos el body
		String body = data[1];
		
		String key   = tabName;
		QryTable tab = new QryTable(header, body);
		this.engine.put(key, tab);
	}

	public void addElementToContext(String cod, Object value) {
		this.engine.put(cod, value);
	}
	
	
	public AuxDeuda eval(Cuenta cuenta, List<CueExe> listCueExe, Integer anio, Integer periodo) throws Exception {
		return eval(cuenta, listCueExe, anio, periodo, null); 
	}

	public AuxDeuda eval(Cuenta cuenta, List<CueExe> listCueExe, Integer anio, 
			Integer periodo,List<GenericAtrDefinition> listAtributos) throws Exception {
		
		ObjImp objImp = cuenta.getObjImp();
		
		Date fechaAnalisis = null;
		if (getRecurso().getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO)) {
			fechaAnalisis = getFechaEmision(); 
		} else {
			fechaAnalisis = DateUtil.getFirstDatOfMonth(periodo, anio);
		}
		
		AtrContainer atributos = new AtrContainer(cuenta, fechaAnalisis, listAtributos);
		DeudaTmp deudaTmp = new DeudaTmp();

		// Seteamos el context
		this.engine.put("cuenta", cuenta);
		this.engine.put("objImp", objImp);
		this.engine.put("listCueExe", listCueExe);
		this.engine.put("anio", anio);
		this.engine.put("periodo", periodo);
		this.engine.put("atributos", atributos);
		this.engine.put("deudaTmp", deudaTmp);
		PrintWriter printWriter = new PrintWriter(stringWriter);
		this.engine.put("log", printWriter);

		// Evaluamos el programa
		this.engine.eval(codigo);
	
		// Obtenemos los datos del context
		deudaTmp = (DeudaTmp) engine.get("deudaTmp");
		
		Double concepto1 = deudaTmp.concepto1;
		Double concepto2 = deudaTmp.concepto2;
		Double concepto3 = deudaTmp.concepto3;
		Double concepto4 = deudaTmp.concepto4;
		Double importe = deudaTmp.importe;
		Double importeBruto = deudaTmp.importeBruto;
		
		if (deudaTmp.cancelar) {
			log.info("La emision de deuda ha sido cancelada.");
			return null; 
		} 
		
		// Obtenemos el atributo de asentamiento
		Atributo atrAse = this.getRecurso().getAtributoAse();
		String atrAsentamiento = atributos.getUsedAtrValues();
		if (atrAse != null) {
			atrAsentamiento += StringUtil.writeXMLNode("AtrAse", (atributos.getValor(atrAse.getCodAtributo()) + ""));
		}

		Long idRecClaDeu = deudaTmp.idRecClaDeu;
		
		Date fechaVencimiento = null;
		if (deudaTmp.fechaVencimiento != null) {
			fechaVencimiento = deudaTmp.fechaVencimiento;
		} else if (recurso.getVencimiento() != null) {
			Vencimiento vencimineto = recurso.getVencimiento();
			fechaVencimiento = Vencimiento.getFechaVencimiento(fechaAnalisis, vencimineto.getId());
		} else {
			throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula");
		}
		
		// Obtenemos los atributos valorizados al momento de la emision
		String strAtrVal = atributos.getStrAtrVal();
		// Guardamos la informacion de la Exencion
		String exeInfo = "<Exencion>\n" + getStrExeInfo(deudaTmp.codExencion, listCueExe) + "</Exencion>\n";
		strAtrVal += exeInfo;
		
		String strExencion = getXMLContentByTag("CodExencion", exeInfo) 
			+ ","  + getXMLContentByTag("CodTipoSujeto", exeInfo);
		
		String leyenda ="";
		
		if (getRecurso().getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO)) {
			 leyenda ="Tasa/Derecho";
		}
		if (!StringUtil.isNullOrEmpty(this.getObservacion())) {
			leyenda = this.getObservacion();
		}

		
		// Creamos la deuda auxiliar
		AuxDeuda auxDeuda = this.createAuxDeuda(cuenta,anio,periodo, fechaVencimiento, idRecClaDeu, atrAsentamiento,
				importe,importeBruto,concepto1, concepto2, concepto3, concepto4, leyenda, strAtrVal, strExencion);
		

		printWriter.close();
		auxDeuda.setLogMessage(this.stringWriter.toString());
		
		return auxDeuda;
	}

	@Transient
	private TipObjImpDefinition toid;
	
	@Transient
	private RecAtrCueDefinition recAtrCueDef = null;

	// Contenedor de atributos para la 
	// entrada del evaluador.
	public class AtrContainer {

		// Todos los Atributos
		private Map<String, GenericAtrDefinition> atributos;
		
		// Atributos utilizados durante el calculo
		private Map<Long, String> usedAtrValues; 

		public AtrContainer(Cuenta cuenta, Date fechaAnalisis, List<GenericAtrDefinition> listAtributos) 
			throws Exception {

			this.atributos 	   = new HashMap<String, GenericAtrDefinition>(); 
			this.usedAtrValues = new HashMap<Long,String>();
		
			GenericDefinition definition = cuenta
				.getCuentaDefinitionValue(toid,null,recAtrCueDef, fechaAnalisis);

			// Creamos el mapa de atributos. 
			List<GenericAtrDefinition> listGenericAtrDef;
			if ((listGenericAtrDef = definition.getListGenericAtrDef()) != null) {
				for (GenericAtrDefinition def: listGenericAtrDef) {
					this.atributos.put(def.getAtributo().getCodAtributo(), def);
				}
			}
			
			// Atributos extra
			if (listAtributos != null) {
				for (GenericAtrDefinition def: listAtributos) {
					this.atributos.put(def.getAtributo().getCodAtributo(), def);
				}
			}
		}
		
		public Object getValor(String codAtributo) {
		
			GenericAtrDefinition atrDef = this.atributos.get(codAtributo);
			if (atrDef != null) {
				Long idAtributo = atrDef.getAtributo().getId();
				String atrVal   = atrDef.getValorString();
	
				if (!usedAtrValues.containsKey(idAtributo))
					this.usedAtrValues.put(idAtributo, atrVal);
				
				return atrDef.convertFromDB(atrVal);
			}
			
			return null;
		}

		public String getUsedAtrValues() throws Exception {
			String atrValues = "";
			for (Long key: this.usedAtrValues.keySet()) {
				atrValues += "<A"  + key  + ">";
				atrValues += this.usedAtrValues.get(key);
				atrValues += "</A" + key  + ">";
			}
			
			// Si los atributos usados superan los 255 caracteres
			// abortamos la generacion de deuda, ya que se perderia
			// informacion.
			if (atrValues.length() > 255) {
				throw new Exception("La string de atributos supera los 255 caracteres.");
			}
			
			return atrValues;
		}

		public String getStrAtrVal() {
			Set<String> setAtrKeys = atributos.keySet();
			String strAtrVal = "";
			if (setAtrKeys != null) {
				for (String key: setAtrKeys) {
					GenericAtrDefinition def = atributos.get(key);
					if (def.getValorString() != null && !StringUtil.isNullOrEmpty(def.getValorString())) {
						String codAtributo = def.getAtributo().getCodAtributo();
					 	strAtrVal += writeXMLNode(codAtributo, def.getValorString());
					}					
				}
			}

			return strAtrVal;
		}
	}

	/***
	 * Por ahora harcodeamos las leyendas para TASA
	 */
	public String getStrExeInfo(String codExencion, List<CueExe> listCueExe) {

		String noExe = "";
		noExe += writeXMLNode("CodExencion", "");
		noExe += writeXMLNode("CodTipoSujeto", "");
		noExe += writeXMLNode("DesExencion", "");
		if (listCueExe == null) return noExe;
		
		CueExe cueExe = null;
		// Obtenemos el CueExe
		for (CueExe c: listCueExe) {
			if (c.getExencion().getCodExencion().equals(codExencion)) {
				cueExe = c;
			}
		}
		
		if (cueExe == null) return noExe;

		String leyenda = "";
		String msgVigencia = "";

		Date fechaHasta = cueExe.getFechaHasta();
		if (cueExe.getFechaHasta() != null) {
			String mesAnio = DateUtil.getMes(fechaHasta) + "/" + DateUtil.getAnio(fechaHasta); 
			msgVigencia += " Vigente hasta: " + mesAnio;  
		}
		
		if (codExencion.equals(Exencion.COD_EXENCION_EXENTO_TOTAL)) {
			leyenda = "Exento Cod. Trib. Municipal Art 76.";
		}

		TipoSujeto tipoSujeto = cueExe.getTipoSujeto();
		if (codExencion.equals(Exencion.COD_EXENCION_EXENTO_5_MINIMOS)) {
			if (tipoSujeto != null  && 
				( TipoSujeto.COD_JUBILADO_PROPIETARIO.equals(tipoSujeto.getCodTipoSujeto()) ||
				  TipoSujeto.COD_JUBILADO_INQUILINO.equals(tipoSujeto.getCodTipoSujeto())) ) {
				leyenda = "Obligatorio notificar cambios sobre declaración jurada (Ord.4846/09 y modif.)";
			} else {
				leyenda = "Exento Cod. Trib. Municipal Art 76.";
			}
		}

		if (codExencion.equals(Exencion.COD_EXENCION_EXENTO_50_PORCIENTO)) {
			leyenda = "Exento 50% - Cochera.";
		}

		if (codExencion.equals(Exencion.COD_EXENCION_EXENTO_25_PORCIENTO)) {
			leyenda = "Exento 75% - Baulera.";
		}

		if (codExencion.equals(Exencion.COD_EXENCION_QUITA_SOBRETASA)) {
			leyenda = "Quita de sobretasa por obra en construccion.";
		}

		if (codExencion.equals(Exencion.COD_EXENCION_PREDIO_SUJETO_EXPROPIACION)) {
			leyenda = "Quita de sobretasa por predio sujeto a expropiacion.";
		}

		if (codExencion.equals(Exencion.COD_EXENCION_QUITA_SOBRETASA_INMUEBLES)) {
			leyenda = "Quita de sobretasa por inmueble afectado.";
		}

		if (codExencion.equals(Exencion.COD_EXENCION_QUITA_SOBRETASA_NU)) {
			leyenda = "Quita de sobretasa por nuevas urbanizaciones.";
		}

		if (codExencion.equals(Exencion.COD_EXENCION_CASO_SOCIAL)) {
			leyenda = "CASO SOCIAL: PARA SU RENOVACION DEBERA PRESENTAR NUEVA SOLICITUD EN C.M.D.";
		}


		leyenda += msgVigencia;
		String codTipoSujeto = (cueExe.getTipoSujeto() != null) ? cueExe.getTipoSujeto().getCodTipoSujeto() : ""; 
		String xmlData = writeXMLNode("CodExencion", codExencion); 
		xmlData += writeXMLNode("CodTipoSujeto", codTipoSujeto);
		xmlData += writeXMLNode("DesExencion", leyenda.toUpperCase()); 

		return xmlData;
	}

	private String writeXMLNode(String tagName, String content) {
		return StringUtil.writeXMLNode(tagName, content) + "\n";
	}
	
	
	// Contenedor para la salida del evaluador
	public static class DeudaTmp {
    	public double importe = 0;
    	public double importeBruto = 0;
    	public Date   fechaVencimiento = null;
    	public Long   idRecClaDeu = null;
    	public double concepto1 = 0;
    	public double concepto2 = 0;
    	public double concepto3 = 0;
    	public double concepto4 = 0;
    	public String codExencion = "";
    	public boolean cancelar = false;
    }
	
	/**
	 * Crea un registro en la tabla de Deuda Auxiliar. 
	 *
	 * @param cuenta
	 * @param anio
	 * @param periodo
	 * @param fechaVencimiento
	 * @param atrAsentamiento
	 * @param importe
	 * @param importeBruto
	 * @param concepto1
	 * @param concepto2
	 * @param concepto3
	 * @param concepto4
	 * 
	 * @return auxDeuda
	 */
	public AuxDeuda createAuxDeuda(Cuenta cuenta, Integer anio, Integer periodo,
			Date fechaVencimiento, Long idRecClaDeu, String atrAsentamiento,Double importe, Double importeBruto,
			Double concepto1, Double concepto2,Double concepto3,Double concepto4, 
			String leyenda, String strAtrVal, String strExencion) {

		Recurso recurso = this.getRecurso();
		Sistema sistema = Sistema.getSistemaEmision(recurso);
		ServicioBanco servicioBanco = sistema.getServicioBanco();
		AuxDeuda auxDeuda = new AuxDeuda();
		Long codRefPag =  GdeDAOFactory.getDeudaDAO().getNextCodRefPago(); 
		auxDeuda.setCodRefPag(codRefPag);
		auxDeuda.setCuenta(cuenta);
		
		RecClaDeu recClaDeuOriginal = null;
		if (idRecClaDeu == null) {
			recClaDeuOriginal= recurso.getRecClaDeuOriginal(new Date());
		} else {
			recClaDeuOriginal = RecClaDeu.getById(idRecClaDeu);
		}
		auxDeuda.setRecClaDeu(recClaDeuOriginal);
		auxDeuda.setRecurso(recurso);
		auxDeuda.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
		auxDeuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
		auxDeuda.setServicioBanco(servicioBanco);
		auxDeuda.setPeriodo(periodo.longValue());
		auxDeuda.setAnio(anio.longValue());
		auxDeuda.setFechaEmision(this.getFechaEmision());
		auxDeuda.setFechaVencimiento(fechaVencimiento);
		auxDeuda.setImporteBruto(NumberUtil.truncate(importeBruto,SiatParam.DEC_IMPORTE_DB));
		auxDeuda.setImporte(NumberUtil.truncate(importe,SiatParam.DEC_IMPORTE_DB));
		auxDeuda.setSaldo(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB));
		auxDeuda.setActualizacion(0D);
		auxDeuda.setSistema(sistema);
		auxDeuda.setResto(0L);
		auxDeuda.setObraFormaPago(null);
		auxDeuda.setEmision(this);
		auxDeuda.setAtrAseVal(atrAsentamiento);
		auxDeuda.setConc1(concepto1);
		auxDeuda.setConc2(concepto2);
		auxDeuda.setConc3(concepto3);
		auxDeuda.setConc4(concepto4);
		auxDeuda.setLeyenda(leyenda);
		auxDeuda.setStrAtrVal(strAtrVal);
		auxDeuda.setStrExencion(strExencion);
		
		return auxDeuda;
	}
	
	/**
	 * Elimina un registro de la tabla de Deuda Auxiliar. 
	 *
	 * @param  auxDeuda
	 * @return auxDeuda
	 */
	public AuxDeuda deleteAuxDeuda(AuxDeuda auxDeuda) throws Exception {
		
		// Validaciones de negocio
		EmiDAOFactory.getEmisionDAO().delete(auxDeuda);
		
		return auxDeuda;
	}
	
	public DeudaAdmin createDeudaAdminFromAuxDeuda(AuxDeuda auxDeuda) throws Exception {

		DeudaAdmin deudaAdmin = new DeudaAdmin();
		
		deudaAdmin.setCodRefPag(auxDeuda.getCodRefPag());
		deudaAdmin.setCuenta(auxDeuda.getCuenta());
		deudaAdmin.setRecClaDeu(auxDeuda.getRecClaDeu());
		deudaAdmin.setRecurso(auxDeuda.getRecurso());
		deudaAdmin.setViaDeuda(auxDeuda.getViaDeuda());
		deudaAdmin.setEstadoDeuda(auxDeuda.getEstadoDeuda());
		deudaAdmin.setAnio(auxDeuda.getAnio());
		deudaAdmin.setPeriodo(auxDeuda.getPeriodo());
		deudaAdmin.setFechaEmision(auxDeuda.getFechaEmision());
		deudaAdmin.setFechaVencimiento(auxDeuda.getFechaVencimiento());
		deudaAdmin.setImporte(auxDeuda.getImporte());
		deudaAdmin.setImporteBruto(auxDeuda.getImporteBruto());
		deudaAdmin.setSaldo(auxDeuda.getSaldo());
		deudaAdmin.setActualizacion(auxDeuda.getActualizacion());
		deudaAdmin.setSistema(auxDeuda.getSistema());
		deudaAdmin.setResto(auxDeuda.getResto());
		deudaAdmin.setRepartidor(auxDeuda.getRepartidor());
		deudaAdmin.setEstaImpresa(SiNo.NO.getBussId());
		deudaAdmin.setEmision(auxDeuda.getEmision());
		deudaAdmin.setReclamada(SiNo.NO.getBussId());
		deudaAdmin.setEmiAtrVal(auxDeuda.getAtrAseVal());
		
		deudaAdmin.setStrEstadoDeuda(auxDeuda.getLeyenda());
		
		// Creamos la deuda
		GdeDAOFactory.getDeudaAdminDAO().update(deudaAdmin);

		// Creamos los conceptos
		List<DeuAdmRecCon> listDeuAdmRecCon = new ArrayList<DeuAdmRecCon>();
		
		// Concepto 1
		if (auxDeuda.getConc1() != null && (auxDeuda.getConc1() != 0D || 
				(auxDeuda.getRecurso().getEsAutoliquidable() != null && 
				auxDeuda.getRecurso().getEsAutoliquidable().equals(SiNo.SI.getBussId()) && 
				this.mapCodRecCon.get("1") != null))) {

			if (auxDeuda.getConc1() < 0D) 
				throw new Exception("El concepto de deuda 1 no puede ser negativo");
			
			RecCon recCon1 = this.mapCodRecCon.get("1");
			if (recCon1 == null) 
				throw new Exception("El concepto de recurso con codigo 1 es inexistente");
			
			DeuAdmRecCon deuAdmRecCon1 = new DeuAdmRecCon();
			deuAdmRecCon1.setDeuda(deudaAdmin);
			deuAdmRecCon1.setRecCon(recCon1);
			deuAdmRecCon1.setImporte(auxDeuda.getConc1());
			deuAdmRecCon1.setImporteBruto(auxDeuda.getConc1());
			deuAdmRecCon1.setSaldo(auxDeuda.getConc1());
			
			listDeuAdmRecCon.add(deuAdmRecCon1);
		}

		// Concepto 2
		if (auxDeuda.getConc2() != null && (auxDeuda.getConc2() != 0D || 
				(auxDeuda.getRecurso().getEsAutoliquidable() != null && 
				 auxDeuda.getRecurso().getEsAutoliquidable().equals(SiNo.SI.getBussId()) && 
					this.mapCodRecCon.get("2") != null))) {

			if (auxDeuda.getConc2() < 0D) 
				throw new Exception("El concepto de deuda 2 no puede ser negativo");

			RecCon recCon2 = this.mapCodRecCon.get("2");
			if (recCon2 == null) 
				throw new Exception("El concepto de recurso con codigo 2 es inexistente");

			DeuAdmRecCon deuAdmRecCon2 = new DeuAdmRecCon();
			deuAdmRecCon2.setDeuda(deudaAdmin);
			deuAdmRecCon2.setRecCon(recCon2);
			deuAdmRecCon2.setImporte(auxDeuda.getConc2());
			deuAdmRecCon2.setImporteBruto(auxDeuda.getConc2());
			deuAdmRecCon2.setSaldo(auxDeuda.getConc2());
			
			listDeuAdmRecCon.add(deuAdmRecCon2);				
		}
		
		// Concepto 3
		if (auxDeuda.getConc3() != null && (auxDeuda.getConc3() != 0D || 
				(auxDeuda.getRecurso().getEsAutoliquidable() != null && 
				 auxDeuda.getRecurso().getEsAutoliquidable().equals(SiNo.SI.getBussId()) && 
					this.mapCodRecCon.get("3") != null))) {

			if (auxDeuda.getConc3() < 0D) 
				throw new Exception("El concepto de deuda 3 no puede ser negativo");

			RecCon recCon3 = this.mapCodRecCon.get("3");
			if (recCon3 == null) 
				throw new Exception("El concepto de recurso con codigo 3 es inexistente");

			DeuAdmRecCon deuAdmRecCon3 = new DeuAdmRecCon();
			deuAdmRecCon3.setDeuda(deudaAdmin);
			deuAdmRecCon3.setRecCon(recCon3);
			deuAdmRecCon3.setImporte(auxDeuda.getConc3());
			deuAdmRecCon3.setImporteBruto(auxDeuda.getConc3());
			deuAdmRecCon3.setSaldo(auxDeuda.getConc3());
		
			listDeuAdmRecCon.add(deuAdmRecCon3);
		}

		// Concepto 4
		if (auxDeuda.getConc4() != null && (auxDeuda.getConc4() != 0D || 
				(auxDeuda.getRecurso().getEsAutoliquidable() != null && 
				 auxDeuda.getRecurso().getEsAutoliquidable().equals(SiNo.SI.getBussId()) && 
					this.mapCodRecCon.get("4") != null))) {
			
			if (auxDeuda.getConc4() < 0D) 
				throw new Exception("El concepto de deuda 4 no puede ser negativo");

			RecCon recCon4 = this.mapCodRecCon.get("4");
			if (recCon4 == null) 
				throw new Exception("El concepto de recurso con codigo 4 es inexistente");

			DeuAdmRecCon deuAdmRecCon4 = new DeuAdmRecCon();
			deuAdmRecCon4.setDeuda(deudaAdmin);
			deuAdmRecCon4.setRecCon(recCon4);
			deuAdmRecCon4.setImporte(auxDeuda.getConc4());
			deuAdmRecCon4.setImporteBruto(auxDeuda.getConc4());
			deuAdmRecCon4.setSaldo(auxDeuda.getConc4());
			
			listDeuAdmRecCon.add(deuAdmRecCon4);
		}
		
		// Graba la lista de DeuAdmRecCon
		for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
			deudaAdmin.createDeuAdmRecCon(deuAdmRecCon);
		}

		// Grabamos la string de conceptos en la deuda
		deudaAdmin.setStrConceptosByListRecCon(listDeuAdmRecCon);

		return deudaAdmin;
	}

	private static String getXMLContentByTag(String tag, String data) {
		return StringUtil.getXMLContentByTag(data, tag);
	}

	@Override
	public String infoString() {
		String info = " Emision";
		
		if (tipoEmision != null) {
			info += " - Tipo Emision: " + tipoEmision.getDesTipoEmision();
		}
		if (recurso != null) {
			info += " - Recurso: " + recurso.getDesRecurso();
		}
		if (fechaEmision != null) {
			info += " - Fecha de Emision: " + DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_HH_MM_MASK);
		}
		if (idCaso != null) {
			info += " - Id Caso: " + idCaso;
		}
		if (corrida != null) {
			info += " - Corrida: " + corrida.getDesCorrida();
		}
		if (atributo != null) {
			info += " - Atributo: " + atributo.getDesAtributo();
		}
		if (valor != null) {
			info += " - con Valor: " + valor;
		}
		if (observacion != null) {
			info += " - Observacion: "+ observacion;
		}
			
		return info;
	}
}
		

