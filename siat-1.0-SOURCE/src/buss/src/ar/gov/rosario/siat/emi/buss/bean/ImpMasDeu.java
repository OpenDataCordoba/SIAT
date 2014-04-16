//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente a la 
 * Impresion Masiva de Deuda
 * 
 * @author tecso
 */
@Entity
@Table(name = "emi_impMasDeu")
public class ImpMasDeu extends BaseBO {
	
	public static final String ID_IMPMASDEU = "idImpMasDeu";
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCorrida") 
	private Corrida corrida;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAtributo") 
	private Atributo atributo;

	@Column(name="atrValor")
	private String atrValor;

	@Column(name="formatoSalida")
	private Integer formatoSalida;

	@Column(name="anio")
	private Integer anio;

	@Column(name="periodoDesde")
	private Integer periodoDesde;

	@Column(name="periodoHasta")
	private Integer periodoHasta;

	@Column(name="abrirPorBroche")
	private Integer abrirPorBroche;

	
	// Constructores
	public ImpMasDeu(){
		super();
	}
	
	public ImpMasDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ImpMasDeu getById(Long id) {
		return (ImpMasDeu) EmiDAOFactory.getImpMasDeuDAO().getById(id);
	}
	
	public static ImpMasDeu getByIdNull(Long id) {
		return (ImpMasDeu) EmiDAOFactory.getImpMasDeuDAO().getByIdNull(id);
	}
	
	public static List<ImpMasDeu> getList() {
		return (ArrayList<ImpMasDeu>) EmiDAOFactory.getImpMasDeuDAO().getList();
	}
	
	public static List<ImpMasDeu> getListActivos() {			
		return (ArrayList<ImpMasDeu>) EmiDAOFactory.getImpMasDeuDAO().getListActiva();
	}
	
	public static List<Long> getListidCuentasExcluidas() {			
		return (ArrayList<Long>) EmiDAOFactory.getImpMasDeuDAO().getListidCuentasExcluidas();
	}
	
	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
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

	public Atributo getAtributo() {
		return atributo;
	}

	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}

	public String getAtrValor() {
		return atrValor;
	}

	public void setAtrValor(String atrValor) {
		this.atrValor = atrValor;
	}
	
	public Integer getFormatoSalida() {
		return formatoSalida;
	}

	public void setFormatoSalida(Integer formatoSalida) {
		this.formatoSalida = formatoSalida;
	}
	
	public Integer getAbrirPorBroche() {
		return abrirPorBroche;
	}

	public void setAbrirPorBroche(Integer abrirPorBroche) {
		this.abrirPorBroche = abrirPorBroche;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.IMPMASDEU_RECURSO);
		}

		// Analizamos si existen los formularios segun el tipo de formato
		if (!FormatoSalida.getEsValido(getFormatoSalida())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.IMPMASDEU_FORMATOSALIDA);
		} else {
			if (getFormatoSalida().equals(FormatoSalida.PDF.getId()) && getRecurso() != null &&
				(getRecurso().getFormImpMasDeu() == null || StringUtil.isNullOrEmpty(getRecurso().getFormImpMasDeu().getXsl()))) {
				addRecoverableError(EmiError.IMPMASDEU_FOMULARIO_PDF_NO_ENCONTRADO);
			}
			if (getFormatoSalida().equals(FormatoSalida.TXT.getId()) && getRecurso() != null &&
				(getRecurso().getFormImpMasDeu() == null || StringUtil.isNullOrEmpty(getRecurso().getFormImpMasDeu().getXslTxt()))) {
				addRecoverableError(EmiError.IMPMASDEU_FOMULARIO_TXT_NO_ENCONTRADO);
			}
		}
		
		if (getAnio() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.IMPMASDEU_ANIO);
		}

		if (getPeriodoDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.IMPMASDEU_PERIODODESDE);
		}

		if (getPeriodoDesde() != null && getRecurso() != null &&
				!getRecurso().validatePeriodo(getPeriodoDesde())) {
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EmiError.IMPMASDEU_PERIODODESDE);
		}
		
		if (getPeriodoHasta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.IMPMASDEU_PERIODOHASTA);
		}

		if (getPeriodoHasta() != null && getRecurso() != null &&
				!getRecurso().validatePeriodo(getPeriodoHasta())) {
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO, EmiError.IMPMASDEU_PERIODOHASTA);
		}
		
		if (getPeriodoDesde() != null && getPeriodoHasta() != null && 
				(getPeriodoDesde() > getPeriodoHasta())) {
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					EmiError.IMPMASDEU_PERIODODESDE, EmiError.IMPMASDEU_PERIODOHASTA);
		}

		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Retorna el nombre del archivo de recibos para un broche determinado.  
	 */
	public String getReciboFileName(String broche) {
		Date fechaProceso = this.getFechaUltMdf();
		String codRecurso = this.getRecurso().getCodRecurso();

		String fileName = "Tr_";
		if (this.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DPUB)) {
			fileName += "cei_dp_en";
		} else if (this.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)) {
			fileName += "cei_drei_en";
		} else if (this.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)) {
			fileName += "cei_etur_en";
		} else {
			fileName +=  codRecurso.toLowerCase();
		}
		
		fileName += "_" + broche;
		fileName +=  (!StringUtil.isNullOrEmpty(this.getAtrValor())) ? "_" + this.getAtrValor() : "";
		fileName +=  "." + DateUtil.formatDate(fechaProceso, DateUtil.yyMMdd_MASK);
		
		return fileName;
	}

	/**
	 * Retorna el nombre del archivo de notificaciones para un broche determinado.  
	 */
	public String getNotifFileName(String broche) {
		Date fechaProceso = this.getFechaUltMdf();
		String codRecurso = this.getRecurso().getCodRecurso();

		
		String fileName = "Tn_";
		fileName +=  codRecurso.toLowerCase();
		fileName += "_" + broche;
		fileName +=  (!StringUtil.isNullOrEmpty(this.getAtrValor())) ? "_" + this.getAtrValor() : "";
		fileName +=  "." + DateUtil.formatDate(fechaProceso, DateUtil.yyMMdd_MASK);
		
		return fileName;
	}
	
	/**
	 * Retorna el nombre del archivo de padron para un broche determinado.  
	 */
	public String getPadronFileName(String broche) {
		Date fechaProceso = this.getFechaUltMdf();
		String codRecurso = this.getRecurso().getCodRecurso();

		
		String fileName = "Padron_";
		fileName +=  codRecurso.toLowerCase();
		fileName += "_" + broche;
		fileName +=  (!StringUtil.isNullOrEmpty(this.getAtrValor())) ? "_" + this.getAtrValor() : "";
		fileName +=  "." + DateUtil.formatDate(fechaProceso, DateUtil.yyMMdd_MASK);
		
		return fileName;
	}
	
	/**
	 * Reinicia el proceso de Impresion.
	 */
	public void reiniciar() throws Exception {
		Corrida corrida = this.getCorrida();
		
		// Obtenemos los archivos que se generaron
		List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
		if (!ListUtil.isNullOrEmpty(listFileCorrida)) {
			// Borramos los archivos del disco 
			for (FileCorrida fileCorrida: listFileCorrida) {
				File file = new File(fileCorrida.getFileName());
				file.delete();
			}
			// Los eliminamos de los registros de Adp
			corrida.deleteListFileCorridaByPaso(1);
		}

		corrida.reiniciar();
	}
	
}
