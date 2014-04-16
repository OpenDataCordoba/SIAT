//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CacheMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.DevolucionDeuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.SelAlmDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import ar.gov.rosario.siat.gde.buss.bean.TipoSelAlm;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasConsPorCtaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.SelAlmAgregarParametrosSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class DeudaAdminDAO extends DeudaDAO {

	private Log log = LogFactory.getLog(DeudaAdminDAO.class);	

	private static long migId = -1;
	
	public DeudaAdminDAO() {
		super(DeudaAdmin.class);
	}

	/**
	 * Obtiene todos los registros de deuda de la tabla gde_deudaAdmin.
	 * Indistintamente devolvera deuda en via Administrativa o Concurso y Quiebra.
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @return
	 */
	public List<DeudaAdmin> getListDeudaAdmin(Cuenta cuenta){
		Session session = SiatHibernateUtil.currentSession();
		
		EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
		
		String sQuery = "FROM DeudaAdmin d " +
						"WHERE d.cuenta = :cuenta AND " +
						" d.estadoDeuda = :estadoDeuda " ;
		
		// Filtros agregados para R4.
		if (cuenta.getLiqCuentaFilter() != null){
			// Aca no utilizamos RecClaDeu para evitar otro getById()
			if (!ModelUtil.isNullOrEmpty(cuenta.getLiqCuentaFilter().getRecClaDeu()))
				sQuery += " AND d.recClaDeu.id = " + cuenta.getLiqCuentaFilter().getRecClaDeu().getId();
			
			if (cuenta.getLiqCuentaFilter().getFechaVtoDesde() != null)
				sQuery += " AND d.fechaVencimiento >= :fechaVtoDesde ";
			
			if (cuenta.getLiqCuentaFilter().getFechaVtoHasta() != null)
				sQuery += " AND d.fechaVencimiento <= :fechaVtoHasta ";
			
			//06/08/09 Se sacan los filtros de declaracion jurada
			// Alguna de las opciones que necesita join con DecJur
			/*if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsJoinDeclaracion()){
				
				String listIdsDeudaDecJur = "";
				String qryDecJur = "SELECT decJur.idDeuda FROM gde_decjur decJur " +
								   "WHERE estado = 1 AND decJur.idCuenta = " + cuenta.getId();
				
				Query sqlQry = session.createSQLQuery(qryDecJur);
				
				List<Integer> listId = (ArrayList<Integer>) sqlQry.list();
				
				if(!ListUtil.isNullOrEmpty(listId)){
					listIdsDeudaDecJur = "(";
					for(Integer id: listId){
						if(!listIdsDeudaDecJur.equals("("))
							listIdsDeudaDecJur += ",";
						listIdsDeudaDecJur += id.toString();
					}
					listIdsDeudaDecJur +=")";
				}
				
				log.debug("listIdsDeudaDecJur: " + listIdsDeudaDecJur);				
			
			
				// En Declaracion Jurada
				if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsConDeclaracion() &&
						listIdsDeudaDecJur != ""){
					sQuery += " AND d.id IN " + listIdsDeudaDecJur; 
				}
				// Resultado vacio, ya que solicita deuda en declaracion, y no existe declaracion
				if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsConDeclaracion() &&
						listIdsDeudaDecJur == ""){
					return new ArrayList<DeudaAdmin>();
				}
				
				// No en Declaracion Jurada
				if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsSinDeclaracion() && 
						listIdsDeudaDecJur != ""){
					sQuery += " AND d.id NOT IN " + listIdsDeudaDecJur; 
				}
			
			// Si no necesitamos joinear con DecJur. 	
			} else {*/
				
			// Declarados Impagos
			if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsInpago()){
				sQuery += " AND d.saldo > 0 ";
			}
				
			// Declarados Pagos
			if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsPago()){
				sQuery += " AND d.importe > 0 AND d.saldo = 0 ";
			}				
			
			// No Determinados
			if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsNoDeterminado()){
				sQuery += " AND d.importe = 0 AND d.saldo = 0 ";
			}
			
			// Adeudado + No Determinados + Determinados Impagos
			if (cuenta.getLiqCuentaFilter().getEstadoPeriodo().getEsAdeudado()){
				sQuery += " AND (d.saldo > 0 OR (d.importe = 0 AND d.saldo = 0)) ";
			}
			
		}
		
		// Cambio en el order by por mantener gestion en dos vias.
		//sQuery +=	" ORDER BY d.idProcedimientoCyQ, d.anio, d.periodo";
		
		// Nuevo cambio en order by para soportar gravamenes
		//sQuery +=	" ORDER BY d.anio, d.periodo, d.idProcedimientoCyQ";
		
		sQuery += " ORDER BY d.fechaVencimiento, d.idProcedimientoCyQ, d.id";

		Query query = session.createQuery(sQuery)
						.setEntity("cuenta", cuenta)
						.setEntity("estadoDeuda", estadoDeuda);
		
		
		if (cuenta.getLiqCuentaFilter() != null ){
			if(cuenta.getLiqCuentaFilter().getFechaVtoDesde() != null)
				query.setDate("fechaVtoDesde", cuenta.getLiqCuentaFilter().getFechaVtoDesde());
			
			if(cuenta.getLiqCuentaFilter().getFechaVtoHasta() != null)
				query.setDate("fechaVtoHasta", cuenta.getLiqCuentaFilter().getFechaVtoHasta());

		}
		
		return query.list();		
	}
	
	public List<DeudaAdmin>getListDeudaAdminSinConvenio (Cuenta cuenta){
		Session session = SiatHibernateUtil.currentSession();
		
		EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
		
		String sQuery = "FROM DeudaAdmin d " +
						"WHERE d.cuenta = :cuenta AND " +
						" d.estadoDeuda = :estadoDeuda AND " +
						" d.idProcedimientoCyQ is null AND " +
						" d.reclamada = 0 AND " +
						" d.convenio.id is null " +
						" ORDER BY d.anio, d.periodo";

		Query query = session.createQuery(sQuery)
						.setEntity("cuenta", cuenta)
						.setEntity("estadoDeuda", estadoDeuda );
		
		return query.list();	
	}


	//TODO: este metodo es de la demo, quitarlo cuando sea conveniente
	public List<DeudaAdmin> obtenerDeudaAdmin(Cuenta cuenta) {
		Session session = SiatHibernateUtil.currentSession();
		String sQuery = "select d from DeudaAdmin d where d.cuenta = :cuenta";
		Query query = session.createQuery(sQuery).setEntity("cuenta", cuenta);
		return query.list();
	}

	/**
	 *  Devuelve el proximo valor de id a asignar. 
	 *  Para se inicializa obteniendo el ultimo id asignado el archivo de migracion con datos pasados como parametro
	 *  y luego en cada llamada incrementa el valor.
	 * 
	 * @return long - el proximo id a asignar.
	 * @throws Exception
	 */
	public long getNextId(String path, String nameFile) throws Exception{
		// Si migId==-1 buscar MaxId en el archivo de load. Si migId!=-1, migId++ y retornar migId;
		if(migId==-1){
			migId = this.getLastId(path, nameFile)+1;
		}else{
			migId++;
		}

		return migId;
	}

	/**
	 *  Inserta una linea con los datos de la Deuda Administrativa para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param deudaAdmin, output - La Deuda Administrativa a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(DeudaAdmin o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(migId == -1){
			 long idJudicial = this.getLastId(output.getPath(), "deudaJudicial.txt");
			 long idAdmin = this.getLastId(output.getPath(), output.getNameFile());
			 long idCancelada = this.getLastId(output.getPath(), "deudaCancelada.txt");
			 long idAnulada = this.getLastId(output.getPath(), "deudaAnulada.txt");
			 long idPrescripta = this.getLastId(output.getPath(), "deudaPrescripta.txt");
			 if(idAdmin>=idJudicial && idAdmin>=idCancelada && idAdmin>=idAnulada && idAdmin>=idPrescripta){
				 id = getNextId(output.getPath(), output.getNameFile());				 
			 }else{
				 if(idCancelada>=idJudicial && idCancelada>=idAnulada && idCancelada>=idPrescripta)
					 id = getNextId(output.getPath(), "deudaCancelada.txt");
				 else if(idJudicial>=idAnulada && idJudicial>=idPrescripta)
					 id = getNextId(output.getPath(), "deudaJudicial.txt");
				 else if(idAnulada>=idPrescripta)
					 id = getNextId(output.getPath(), "deudaAnulada.txt");
				 else
					 id = getNextId(output.getPath(), "deudaPrescripta.txt");
			 }
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idDeuda.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 migId = id;				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		 
		DecimalFormat decimalFormat = new DecimalFormat("0.0000000000");
		// Estrucura de la linea:
		// id|codrefpag|idcuenta|idsistema|idreccladeu|idviadeuda|idestadodeuda|idrecurso|anio|periodo|fechavencimiento|fechapago|importe|importebruto|saldo|actualizacion|fechaemision|estaimpresa|resto|reclamada|idconvenio|strConceptosProp|usuario|fechaultmdf|estado|
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getCodRefPag());
		line.append("|");
		line.append(o.getCuenta().getId());		 
		line.append("|");
		line.append(o.getSistema().getId());		 
		line.append("|");
		line.append(o.getRecClaDeu().getId());
		line.append("|");
		line.append(o.getViaDeuda().getId());
		line.append("|");
		line.append(o.getEstadoDeuda().getId());
		line.append("|");

		//line.append(o.getServicioBanco().getId()); se quito el servicio banco de la deuda
		//line.append("|");

		line.append(o.getRecurso().getId());
		line.append("|");
		line.append(o.getAnio());
		line.append("|");
		line.append(o.getPeriodo());
		line.append("|");
		if(o.getFechaVencimiento()!=null){
			line.append(DateUtil.formatDate(o.getFechaVencimiento(), "yyyy-MM-dd"));
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		if(o.getFechaPago()!=null){
			line.append(DateUtil.formatDate(o.getFechaPago(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(decimalFormat.format(o.getImporte()));
		line.append("|");
		line.append(decimalFormat.format(o.getImporteBruto()));
		line.append("|");
		line.append(decimalFormat.format(o.getSaldo()));
		line.append("|");
		if(o.getActualizacion()!=null)
			 line.append(decimalFormat.format(o.getActualizacion()));
		line.append("|");
		if(o.getFechaEmision()!=null){
			line.append(DateUtil.formatDate(o.getFechaEmision(), "yyyy-MM-dd"));		 
		} // Si es null no se inserta nada, viene el proximo pipe.
		line.append("|");
		line.append(o.getEstaImpresa());
		line.append("|");
		line.append(o.getResto());
		line.append("|");
		line.append(o.getReclamada());
		line.append("|");
		if(o.getConvenio()!=null)
			 line.append(o.getConvenio().getId());
		line.append("|");
		line.append(o.getStrConceptosProp());
 		line.append("|");
 		if(o.getAtrAseVal()!=null)
 			line.append(o.getAtrAseVal());
 		line.append("|");
 		if(o.getStrEstadoDeuda()!=null)
 			line.append(o.getStrEstadoDeuda());
		line.append("|");
		line.append(DemodaUtil.currentUserContext().getUserName());
		line.append("|");

		//StringBuffer actualDate = new StringBuffer(DateUtil.formatDate(Calendar.getInstance().getTime(), "yyyy-MM-dd"));
		//actualDate.append(" 00:00:00");
		line.append("2010-01-01 00:00:00");//actualDate.toString());

		line.append("|");
		line.append("1");
		line.append("|");

		output.addline(line.toString());

		// Seteamos el id generado en el bean.
		o.setId(id);

		return id;
	}

	
	public String getPrimerSQLBySearchPageForDeuda(ProcesoMasivo pm, Date fechaEnvio, Map<String,String> mapaFiltros) throws Exception {
		
		String     idTipoSelAlmDet = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_TIPO_SEL_ALM_DET);
		TipoSelAlm tipoSelAlmDet   = TipoSelAlm.getById(Long.valueOf(idTipoSelAlmDet)); 

		// determinacion de la tabla y la via de deuda
		String tablaDeuda = "gde_deudaAdmin"; 
		Long idViaDeuda = ViaDeuda.ID_VIA_ADMIN;
		if (tipoSelAlmDet.getEsTipoSelAlmDetDeudaJud()){
			tablaDeuda = "gde_deudaJudicial";
			idViaDeuda = ViaDeuda.ID_VIA_JUDICIAL;
		} 
		
		String queryString   = "SELECT DISTINCT (deu.id), deu.saldo, deu.idcuenta " +
			"FROM " + tablaDeuda + " deu " +
			" INNER JOIN pad_cuenta cta  ON (deu.idcuenta == cta.id) " +
	  		" LEFT JOIN pad_broche bro ON (cta.idBroche == bro.id) ";
	  	
		//fedel:19-may-2009: la validacion por exencion se hace en paso 2 deuda por 
		//deuda con analisis de periodos (exencion al vencimiento)
	  		//	" LEFT JOIN tmp_ctaexe ctaexe ON (cta.id == ctaexe.idCuenta) ";
		
		//"INNER JOIN def_recurso rec ON (cta.idrecurso == rec.id) ";
			//"INNER JOIN def_recClaDeu rcd ON (deu.idreccladeu == rcd.id) ";
		
		String[] filtroObStrings = {"",""};
				
		//String aplicaFiltroObjImp = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.APLICA_FILTRO_OBJ_IMP);
		
		String idsExencionesSI = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_EXENCIONES_SI);
		String idsExencionesNO = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_EXENCIONES_NO);
		
		String idsAtributosSI = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_ATRIBUTOS_SI);
		String idsAtributosNO = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_ATRIBUTOS_NO);
		
		String idObra = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_OBRA);
		String importeHistoricoDesde = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IMPORTE_HISTORICO_DESDE); 
		String importeHistoricoHasta = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IMPORTE_HISTORICO_HASTA);
		String numeroCuenta = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.NUMERO_CUENTA);
		String idsRecClaDeu = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IDS_REC_CLA_DEU);
		String fechaVencimientoDesde = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.FECHA_VENCIMIENTO_DESDE);
		String fechaVencimientoHasta = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.FECHA_VENCIMIENTO_HASTA);
		String idContribuyente = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_PERSONA);
		
		Recurso recurso = Recurso.getById(Long.valueOf(mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_RECURSO)));
		
		boolean aplicaFiltroObjImpFlag = recurso.getTipObjImp() != null;
		
		if (aplicaFiltroObjImpFlag){
			filtroObStrings = this.getSQLsForParamObjImp(mapaFiltros);
			log.debug("filtroObjStrings inners: " + filtroObStrings[0]);
			log.debug("filtroObjStrings wheres: " + filtroObStrings[1]);
			queryString += filtroObStrings[0];
		}
		
		// 2009-01-26 fedel: Desactivamos porque vamos a hacer que todo el manejo de las exenciones esta en el paso 2.
		// Vamos a usar el criterio de que si una cueexe tiene una exencion.envioJucial == 0, se descarta la deuda y no se envia a judicial.
		/*boolean aplicarFiltroExen = (!StringUtil.isNullOrEmpty(idsExencionesSI) || !StringUtil.isNullOrEmpty(idsExencionesNO) );
		//if (aplicarFiltroExen){
		//	queryString += "LEFT JOIN exe_cueexe cueExe ON (cta.id == cueExe.idcuenta) ";
		}*/
				
		boolean aplicarFiltroIdContr = !StringUtil.isNullOrEmpty(idContribuyente);
		
		boolean aplicarFiltroAtrib =  (!StringUtil.isNullOrEmpty(idsAtributosSI) || !StringUtil.isNullOrEmpty(idsAtributosNO) );
		if (aplicarFiltroAtrib || aplicarFiltroIdContr) {
			queryString += "LEFT JOIN pad_cuentatitular ctaTit ON (cta.id == ctaTit.idcuenta ) ";
		}
		
		// hacemos que funcione correctamente.
		// cambiamos por outer join
		if (aplicarFiltroAtrib){
			queryString += " LEFT JOIN pad_conatrval cav ON (cav.idcontribuyente == ctaTit.idcontribuyente)";
			queryString += " LEFT OUTER JOIN def_conatr ca ON (cav.idconatr == ca.id)";
		}
				
		boolean aplicarFiltroObra = !StringUtil.isNullOrEmpty(idObra);
		if (aplicarFiltroObra){
			queryString += "INNER JOIN cdm_plaobrdet pod ON (deu.idcuenta == pod.idcuentacdm) " +
					"INNER JOIN cdm_planillaobra po ON (pod.idplanillaobra == po.id)";
		}
		
		queryString +=" WHERE ";

		// filtro Recurso requerido
		queryString += " deu.idrecurso = " + mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_RECURSO);
		// debe estar en via administrativa si la via es administrativa y en via judicial si la via es judicial
		queryString += " AND deu.idViaDeuda = "+ idViaDeuda;
		// no debe estar cancelada (pagada): no hace falta aplicar un filtro

		// no debe estar reclamada
		queryString += " AND (deu.reclamada IS NULL OR deu.reclamada <> " + SiNo.SI.getId() +") ";
		// TODO no debe estar indeterminada:
		// no debe estar prescripta ni condonada: no hace falta aplicar un filtro
		// no debe estar asociada a ningun convenio, 
		queryString += " AND deu.idConvenio IS NULL ";
		
		boolean esAutoliquidable = Integer.valueOf(1).equals(recurso.getEsAutoliquidable());
		
		//funcionalidad para selecciones de recursos autoliquidables:
		//Si es es autoliquidable y NO es una envio judicial tenemos que permitir seleccionar cualquier saldo.
		//Sino agregamos el filto saldo <> 0
		if (esAutoliquidable && 
				(pm.getTipProMas().getEsSeleccionDeuda() || pm.getTipProMas().getEsReconfeccion() || pm.getTipProMas().getEsPreEnvioJudicial())) {
			//no agregamos filtro
		} else {
			queryString += " AND deu.saldo <> 0 ";
		}

		// 2010-02-01
		// fix-bug: 0005075
		if (idViaDeuda.longValue() == ViaDeuda.ID_VIA_ADMIN ) {
			log.info("DEUDA ADMIN");
			// tiene que tener idEstadoDeuda impaga
			queryString += " AND deu.idEstadoDeuda = 1 ";
			
		} else {
			log.info("DEUDA JUD");
			queryString += " AND deu.idEstadoDeuda = 5 ";

			
		}

		// debe estar activa
		queryString += " AND deu.estado = "+ Estado.ACTIVO.getId();
		
		// cuentas sin broche o con broches que no estan exentos
		queryString += " AND (cta.idBroche is NULL OR (bro.exentoEnvioJud != 1 OR bro.exentoEnvioJud is null)) ";

		// sacar las deudas con cuentas con exenciones que no envian. 
		// fedel:2009-may-19: ahora el analisis en en paso 2 deuda por deuda contra cada periodo de cueexe
		//queryString += " AND (ctaexe.idexencion is NULL)";


		// clasificacion deuda
		if (!StringUtil.isNullOrEmpty(idsRecClaDeu)){
			queryString += " AND deu.idRecClaDeu IN (" + idsRecClaDeu + ") ";
		}

		// filtro Fecha Vencimiento Desde
		if (!StringUtil.isNullOrEmpty(fechaVencimientoDesde) ) {
			queryString += " AND deu.fechaVencimiento >= TO_DATE('" + fechaVencimientoDesde + "','%d/%m/%Y')";
		}
		
		// filtro Fecha Vencimiento Hasta
		if (fechaVencimientoHasta != null ) {
			queryString += " AND deu.fechaVencimiento <= TO_DATE('" + fechaVencimientoHasta + "','%d/%m/%Y')";
		}
		// en el caso de un envio a judicial la fecha hasta ahora es requerido y 
		// previamente esta validada que no sea mayor a la fecha limite
		
		boolean aplicaAlTotalDeuda = "1".equals(mapaFiltros.get(SelAlmAgregarParametrosSearchPage.APLICA_AL_TOTAL_DEUDA));
		if(!aplicaAlTotalDeuda){
			// filtro Importe Historico Desde: ojo que es sobre el saldo y no sobre el importe
			if (importeHistoricoDesde != null ) {
				queryString += " AND deu.saldo >= " + importeHistoricoDesde;
			}
			
			// filtro Importe Historico Hasta: ojo que es sobre el saldo y no sobre el importe
			if (importeHistoricoHasta != null ) {
				queryString += " AND deu.saldo < " + importeHistoricoHasta;
			}
		}
		
		// filtro Importe Actualizado Desde: POR AHORA NO SE HACE
		//if (deudaIncProMasAgregarSearchPage.getImporteActualizadoDesde() != null ) {
		//	queryString += " AND deu.importe >= " + deudaIncProMasAgregarSearchPage.getImporteActualizadoDesde();
		//}
		// filtro Importe Actualizado Hasta: POR AHORA NO SE HACE
		//if (deudaIncProMasAgregarSearchPage.getImporteActualizadoHasta() != null ) {
		//	queryString += " AND deu.importe <= " + deudaIncProMasAgregarSearchPage.getImporteActualizadoHasta();
		//}
		
		//aplicaFiltroObjImp
		//if (aplicaFiltroObjImpFlag){
			queryString += filtroObStrings[1];
		//}


		// filtro exenciones
		// 2009-01-26 fedel: Desactivamos porque vamos a hacer que todo el manejo de las exenciones esta en el paso 2.
		// Vamos a usar el criterio de que si una cueexe tiene una exencion.envioJucial == 0, se descarta la deuda y no se envia a judicial.
		/*
		if (aplicarFiltroExen){
			
			if (!StringUtil.isNullOrEmpty(idsExencionesSI)){
				queryString += " AND cueExe.idexencion IN ( " + idsExencionesSI + " ) "; 
			}
			if (!StringUtil.isNullOrEmpty(idsExencionesNO)){
				queryString += " AND ( cueExe.idexencion NOT IN ( " + idsExencionesNO + " ) OR cueExe.idexencion IS NULL) ";
			}
		}*/
		
		// filtro idContribuyente
		String strFechaEnvio = DateUtil.formatDate(fechaEnvio, DateUtil.ddSMMSYYYY_MASK);
		if (aplicarFiltroIdContr){
			queryString += " AND ctaTit.idContribuyente = " + idContribuyente; 
			queryString += " AND (ctaTit.fechaHasta is null or ";
			queryString += " ctaTit.fechaHasta >= TO_DATE('" + strFechaEnvio + "','%d/%m/%Y'))";
		}
		
		// filtro atributos contribuyente
		// hacemos que funcione correctamente.
		// NOTA: solo funciona con 1 atributo de contribuyente y debe tener los valores 1 o 0.
		if (aplicarFiltroAtrib){
			if (!StringUtil.isNullOrEmpty(idsAtributosSI)){
				queryString += " AND (ca.idatributo IN ( " + idsAtributosSI + " ) and cav.valor = '1' "; 
				queryString += "      and (cav.fechahasta > TO_DATE('" + strFechaEnvio + "','%d/%m/%Y') or cav.fechahasta is null)";
				queryString += "      )";
			}
			
			if (!StringUtil.isNullOrEmpty(idsAtributosNO)){
				queryString += " AND ( ca.idatributo NOT IN ( " + idsAtributosNO + " ) and cav.valor = '0' ";
				queryString += "       and (cav.fechahasta > TO_DATE('" + strFechaEnvio + "','%d/%m/%Y') or cav.fechahasta is null) ";
				queryString += "       or ca.idatributo is null";
				queryString += "      )";

			}
		}

		// numero de cuenta 
		if (!StringUtil.isNullOrEmpty(numeroCuenta) ) {
			queryString += " AND cta.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
		}

		// obra
		if (aplicarFiltroObra ) {
			queryString += " AND po.idobra = " + idObra;
		}

		// No conviene realizar aca Order By - 06/10/08 Se agrega porque no se hace por cuenta en otro lado
		//queryString += " ORDER BY deu.idcuenta";
		
		return queryString;
	}

	/**
	 * Carga los detalles de la selAlmDet con las deudas usando como filtro el mapaFiltros
	 * @param mapaFiltros
	 * @throws Exception
	 */
	public void cargarSelAlmDeudaIncluida(Map<String,String> mapaFiltros, Date fechaEnvio, ProcesoMasivo pm) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		AdpRun.changeRunMessage("Creando filtros de busqueda...", 0);

		String     idTipoSelAlmDet = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_TIPO_SEL_ALM_DET);
		TipoSelAlm tipoSelAlmDet   = TipoSelAlm.getById(Long.valueOf(idTipoSelAlmDet)); 

		String strFechaEnvio = DateUtil.formatDate(fechaEnvio, DateUtil.ddSMMSYYYY_MASK);
		String sqlCuentasExentas = "select " +
		"  cueexe.idCuenta, cueexe.idExencion " + 
		" from exe_cueexe cueexe, exe_exencion exe " +
		" where " +
		" cueexe.idexencion = exe.id " +
		"   and cueexe.idEstadoCueExe = 10 " +
		"   and exe.enviajudicial = 0 " +
		"   and (cueExe.fechaHasta is null or " +
		"        cueExe.fechaHasta >= TO_DATE('" + strFechaEnvio + "','%d/%m/%Y'))";
		String createTempCuentasExentas = sqlCuentasExentas + " INTO TEMP tmp_ctaexe WITH NO LOG"; //tabla con las cuentas que estan excluidas por tener exencion que no envia.
		
		String sqlPrimerFiltro = getPrimerSQLBySearchPageForDeuda(pm, fechaEnvio, mapaFiltros);
		String createTempPrimerFiltro= sqlPrimerFiltro + " INTO TEMP primerFiltro WITH NO LOG";
		
		// string de la creacion de la tabla temporal para contar los deudas repetidas por cuenta
		String createTempSegFiltro = "SELECT pf.idcuenta, SUM(pf.saldo) AS saldo FROM primerFiltro pf " +
			"GROUP BY pf.idcuenta HAVING COUNT(pf.id) >= " + mapaFiltros.get(SelAlmAgregarParametrosSearchPage.CANTIDAD_MINIMA_DEUDA);
		
		boolean aplicatAlTotalDeuda = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.APLICA_AL_TOTAL_DEUDA).equals("1");
		if (aplicatAlTotalDeuda){
			String importeHistoricoDesde = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IMPORTE_HISTORICO_DESDE);
			if (importeHistoricoDesde != null ) {
				createTempSegFiltro += " AND SUM(pf.saldo) >= " + importeHistoricoDesde;
			}
			// filtro Importe Historico Hasta: ojo que es sobre el saldo y no sobre el importe
			String importeHistoricoHasta = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.IMPORTE_HISTORICO_HASTA);
			if (importeHistoricoHasta != null ) {
				createTempSegFiltro += " AND SUM(pf.saldo) < " + importeHistoricoHasta;
			}
		}
		createTempSegFiltro += " INTO TEMP segundoFiltro WITH NO LOG";

		long idSelAlm = Long.parseLong(mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_SEL_ALM_INC));

		// obtencion de la fecha de ult mod, estado activo y nombre de usuario para usarlo en el select para luego insertar
		String fecUltMdf    = DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
		Integer estadoActivo = Estado.ACTIVO.getId();
		String userName = DemodaUtil.currentUserContext().getUserName();
		if (userName == null) {
			userName = "siat";
		}

		String createInsertResultado = "SELECT " + idSelAlm + " idSelAlm, pf.id, " + tipoSelAlmDet.getId() +  " idTipoSelAlmDet, '" + userName + "' usuario, '" + fecUltMdf + "' fecUltMdf , " + estadoActivo + " estado , pf.idCuenta";
		createInsertResultado += " FROM primerFiltro pf INNER JOIN segundoFiltro sf ON (pf.idcuenta == sf.idcuenta)  ";

		// que no hayan sido agregadas a la selAlmDet de SelAlm teniendo en cuenta el tipo de selalmdet
		createInsertResultado += " LEFT JOIN gde_selalmdet sad ON (sad.idelemento == pf.id AND sad.idselalm = " + idSelAlm + " AND sad.idtiposelalmdet = " + tipoSelAlmDet.getId() + ") " +
				"WHERE sad.id IS NULL ORDER BY pf.idcuenta, pf.id ";
		createInsertResultado += " INTO TEMP insertResultado WITH NO LOG";
		
		String queryInsertSelAlmDet = "INSERT INTO gde_selalmdet (idselalm,idelemento,idTipoSelAlmDet,usuario,fechaultmdf,estado) SELECT SKIP ? FIRST ? idSelAlm, id, idTipoSelAlmDet, usuario, fecUltMdf, estado from insertResultado";

		//Ya armamos los query,
		// ahora nos conseguimos la connection JDBC de hibernate...
		// Hacemos un flush y un commit de lo que haya en hibernate por las dudas
		// para sincronizar hibernate.
		Long countRegs;
		Connection        con;
		PreparedStatement ps;
		Session session = currentSession();
		Transaction tx = currentSession().getTransaction();
		tx.commit();
		tx = session.beginTransaction();

		// IMPORTANTE: de aca para abajo operamos con JDBC, y NO usar nada de hibenate
		con = session.connection();

		// GG 061128
		int oldIsolation = con.getTransactionIsolation();
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

		AdpRun.changeRunMessage("Limpiando tablas temporales...", 0);

		// borrado de la tablas temporales si existe
		try {
			ps = con.prepareStatement("DROP TABLE tmp_ctaexe;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error ya que puede ser que la tabla no exista
		}

		// borrado de la tablas temporales si existe
		try {
			ps = con.prepareStatement("DROP TABLE primerFiltro;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error ya que puede ser que la tabla no exista
		}

		// borrado de la tablas temporales si existe
		try {
			ps = con.prepareStatement("DROP TABLE segundoFiltro;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error ya que puede ser que la tabla no exista
		}

		// borrado de la tablas temporales si existe
		try {
			ps = con.prepareStatement("DROP TABLE insertResultado;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error ya que puede ser que la tabla no exista
		}

		/* fedel:19-may-2009: ahora las deudas exentas por no envia a judicial
		 * se descartan una a una durante el paso dos.
		 * haciendo analisis de cuexe por periodos.
		 */
		/*
  		log.info(funcName + ": Creacion Temporal Cuentas exentas:  " + createTempCuentasExentas);
		// creacion de tabla temporal con las deudas repetidas y la suma de saldos
		AdpRun.changeRunMessage("Buscando Cuentas exentas...", 0);
		ps = con.prepareStatement(createTempCuentasExentas);
		ps.executeUpdate();		
		con.commit();
		ps.close();
		countRegs = countRegsTable("tmp_ctaexe");
		log.info(funcName + ": tmp_ctaexe creado con exito. Contiene registros=" + countRegs);		
		*/
		
		log.info(funcName + ": Creacion Temporal Primer Filtro:  " + createTempPrimerFiltro);
		// creacion de tabla temporal con las deudas repetidas y la suma de saldos
		AdpRun.changeRunMessage("Buscando Deuda. Etapa 1/5...", 0);
		ps = con.prepareStatement(createTempPrimerFiltro);
		ps.executeUpdate();		
		con.commit();
		ps.close();
		countRegs = countRegsTable("primerFiltro");
		log.info(funcName + ": primerFiltro creado con exito. Contiene registros=" + countRegs);		


		//creacion de segunda tabla temporal
		log.info(funcName + ": Creacion Temporal Segundo Filtro: " + createTempSegFiltro);
		AdpRun.changeRunMessage("Buscando Deuda. Etapa 2/5...", 0);
		ps = con.prepareStatement(createTempSegFiltro);
		ps.executeUpdate();
		con.commit();
		ps.close();
		countRegs = countRegsTable("segundoFiltro");
		log.info(funcName + ": segundoFiltro creado con exito. Contiene registros=" + countRegs);		

		//creacion de la tabla inserResultado tabla temporal igual a selAlmDet, pero con la deuda a insertar en selalmdet.
		log.info(funcName + ": Creacion Temporal Insert Resultado: " + createInsertResultado);
		AdpRun.changeRunMessage("Buscando deuda. Etapa 3/5...", 0);
		ps = con.prepareStatement(createInsertResultado);
		ps.executeUpdate();
		con.commit();
		ps.close();
		countRegs = countRegsTable("insertResultado");
		log.info(funcName + ": insertResultado creado con exito. Contiene registros=" + countRegs);		

		//finalmente insert en tabla del detalle de la selccion almacenada.
		//inserta de 'amount' selAlmDet leyendo las tablas temporales, 
		//y haciendo insert into selAlmDet.
		log.info(funcName + ": Insert SelAlmDet: " + queryInsertSelAlmDet);		
		AdpRun.changeRunMessage("Transfiriendo Deuda Etapa 4/5: ", 0);
		ps = con.prepareStatement(queryInsertSelAlmDet);
		long amount = 1000;
		long count = 0;
		while (true) {
			ps.setLong(1, amount*count); //SKIP
			ps.setLong(2, amount); //FIRST
			int n = ps.executeUpdate();
			con.commit();
			log.info(funcName + ": gde_selalmdet commit " + n + " registros");
			
			int porc = countRegs>0 ? (int) (n*100/countRegs) : 100;
			AdpRun.changeRunMessage("Transfiriendo Deuda Etapa 4/5 - " +  porc + "%", 30);
			
			if (n < 1000) {
				break;
			}
			count++;
		}
		// <-- Fin Resultado
		
		// borrado de la tabla temporal si existe
		AdpRun.changeRunMessage("Limpiando tablas Temporales. Etapa 5/5", 0);
		try {
			ps = con.prepareStatement("DROP TABLE tmp_ctaexe;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error ya que puede ser que la tabla no exista
		}
		try {
			ps = con.prepareStatement("DROP TABLE primerFiltro;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error 
		}
		try {
			ps = con.prepareStatement("DROP TABLE segundoFiltro;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error 
		}
		try {
			ps = con.prepareStatement("DROP TABLE insertResultado;");
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// no manejamos el error 
		}
		con.setTransactionIsolation(oldIsolation);

		AdpRun.changeRunMessage("Finalizado.", 0);
	}
	
	/**
	 * Obtiene la lista de Deudas Administrativas a partir de la lista de Ids
	 * @param listLong
	 * @return List<DeudaAdmin>
	 */
	private List<DeudaAdmin> getListDeudaAdminByListId(List<Long> listLong){

		String queryString = "FROM DeudaAdmin da WHERE da.id IN (:idsDeudaAdmin) ";

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		query.setParameterList("idsDeudaAdmin",listLong);

		return (ArrayList<DeudaAdmin>) query.list();
	}

	/**
	 * Obtiene la lista de Deudas Administrativas a partir del SearchPage
	 * @param deudaIncProMasConsPorCtaSearchPage
	 * @return List<DeudaAdmin>
	 */
	public List<DeudaAdmin> getListDeudaAdminIncluidaBySearchPage(DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPage){

		String queryString = " FROM DeudaAdmin da, SelAlmDet sad  " +
		"WHERE da.cuenta.id = " + deudaIncProMasConsPorCtaSearchPage.getCuenta().getId() +  
		" AND da.id = sad.idElemento" +
		" AND sad.selAlm.id = " + deudaIncProMasConsPorCtaSearchPage.getProcesoMasivo().getSelAlmInc().getId() +
		" AND sad.tipoSelAlmDet.id = " + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM;

		Session session = currentSession();

		Query query = session.createQuery("SELECT COUNT(da) " + queryString);
		Long cantidadMaxima = (Long) query.uniqueResult();
		deudaIncProMasConsPorCtaSearchPage.setMaxRegistros(cantidadMaxima);

		// TODO ver el order by

		// obtenemos el resultado de la consulta
		query = session.createQuery("SELECT da " + queryString);

		if (deudaIncProMasConsPorCtaSearchPage.isPaged()) {
			query.setMaxResults(deudaIncProMasConsPorCtaSearchPage.getRecsByPage().intValue());
			query.setFirstResult(deudaIncProMasConsPorCtaSearchPage.getFirstResult());
		}

		return (ArrayList<DeudaAdmin>) query.list();
	}


	// DEUDA EXCLUIDA

	/**
	 * Obtiene el SQL a partir del FROM de la busqueda de deuda a excluir de la agregacion del envio a judicial
	 * Utilizado en la creacion, en la eliminacion masiva y en la eliminacion por seleccion individual
	 * @param deudaExcProMasAgregarSearchPage
	 * @return String
	 * @throws Exception
	 */
	public String getSQLBySearchPage(DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws Exception {

		String queryString = " FROM gde_deudaAdmin deu " +
			"INNER JOIN pad_cuenta cta ON (deu.idcuenta == cta.id) " +
			"INNER JOIN def_recurso rec ON (deu.idrecurso == rec.id) " +
			"INNER JOIN def_recClaDeu rcd ON (deu.idreccladeu == rcd.id) ";
		
		// que no hayan sido agregadas a la selAlmDet de SelAlm 
		queryString += "LEFT JOIN gde_selalmdet sad ON (sad.idelemento == deu.id " +
			"AND sad.idselalm = " + deudaExcProMasAgregarSearchPage.getProcesoMasivo().getSelAlmExc().getId() + 
			" AND sad.idTipoSelAlmDet = " + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM + ") " +
			"WHERE sad.id IS NULL  "; 

		// filtro Recurso requerido
		queryString += " AND deu.idrecurso = " + deudaExcProMasAgregarSearchPage.getProcesoMasivo().getRecurso().getId();
		
		// debe estar en via administrativa
		queryString += " AND deu.idViaDeuda = "+ ViaDeuda.ID_VIA_ADMIN;
		// no debe estar cancelada (pagada): no hace falta aplicar un filtro
		// no debe estar reclamada
		queryString += " AND (deu.reclamada IS NULL OR deu.reclamada <> " + SiNo.SI.getId() +") ";
		// TODO no debe estar indeterminada:
		// no debe estar prescripta ni condonada: no hace falta aplicar un filtro
		// no debe estar asociada a ningun convenio, excepto que este recompuesto
		queryString += " AND deu.idConvenio IS NULL ";
		// no debe tener saldo cero
		queryString += " AND deu.saldo <> 0 ";
		// debe estar activa
		queryString += " AND deu.estado = "+ Estado.ACTIVO.getId();

		// clasificacion deuda
		if (deudaExcProMasAgregarSearchPage.getListIdRecClaDeu() != null &&          // puede ser nula 
				deudaExcProMasAgregarSearchPage.getListIdRecClaDeu().length > 0){
			queryString += " AND deu.idRecClaDeu IN (" + StringUtil.getStringComaSeparate(deudaExcProMasAgregarSearchPage.getListIdRecClaDeu()) + ") ";
		}
		// filtro Fecha Vencimiento Desde
		if (deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde() != null ) {
			queryString += " AND deu.fechaVencimiento >= TO_DATE('" + 
			DateUtil.formatDate(deudaExcProMasAgregarSearchPage.getFechaVencimientoDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
		}
		// filtro Fecha Vencimiento Hasta
		if (deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta() != null ) {
			queryString += " AND deu.fechaVencimiento <= TO_DATE('" + 
			DateUtil.formatDate(deudaExcProMasAgregarSearchPage.getFechaVencimientoHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
		}

		// numero de cuenta
		String numeroCuenta = deudaExcProMasAgregarSearchPage.getCuenta().getNumeroCuenta();
		if (!StringUtil.isNullOrEmpty(numeroCuenta) ) {
			queryString += " AND cta.numeroCuenta = '" + StringUtil.formatNumeroCuenta(numeroCuenta) + "'";
		}

		// TODO ver Order By
		
		return queryString;
	}

	/**
	 * Obtiene la lista de Deudas Administrativas a partir del SearchPage
	 * @param  deudaExcProMasAgregarSearchPage
	 * @return List<DeudaAdmin>
	 * @throws Exception
	 */
	public List<DeudaAdmin> getBySearchPage(DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		List<DeudaAdmin> listDeudaAdmin = new ArrayList<DeudaAdmin>();

		String sqlBySearchPage = getSQLBySearchPage(deudaExcProMasAgregarSearchPage);
		
		// no buscamos deudas con cuentas repetidas como lo hace la inclusion		
		// string de la consulta 		
		String queryString = "SELECT deu.id " + sqlBySearchPage;

		// TODO ver Order By

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		Connection        con;
		PreparedStatement ps;
		ResultSet         rs;

		// ahora nos conseguimos la connection JDBC de hibernate...
		con = SiatHibernateUtil.currentSession().connection();

		// GG 061128
		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
		con.setReadOnly(true);
		con.setAutoCommit(false);

		// ejecucion de la consulta de resultado
		ps = con.prepareStatement(queryString);
		rs = ps.executeQuery();
		
		// --> Resultado
		List<Long> listLong = new ArrayList<Long>();

		while ( rs.next()) {
			listLong.add(rs.getLong(1));
			//DeudaAdmin.getById(rs.getLong(1)); NO hacer esto
		}
		// <-- Fin Resultado
		rs.close();
		ps.close();

		if (listLong.size() > 0){
			listDeudaAdmin = getListDeudaAdminByListId(listLong);
		}
		return listDeudaAdmin;
	}

	/**
	 * Obtiene la lista de Deudas Administrativas de acuerdo a los filtros del SearchPage
	 * @param  deudaExcProMasConsPorCtaSearchPage
	 * @return List<DeudaAdmin>
	 */
	public List<DeudaAdmin> getListDeudaAdminExcluidaBySearchPage(DeudaProMasConsPorCtaSearchPage deudaExcProMasConsPorCtaSearchPage){

		// TODO ver utilizar el recurso como otro filtro agregado para mejorar la performance
		String queryString = " FROM DeudaAdmin da, SelAlmDet sad  " +
		"WHERE da.cuenta.id = " + deudaExcProMasConsPorCtaSearchPage.getCuenta().getId() +  
		" AND sad.selAlm.id = " + deudaExcProMasConsPorCtaSearchPage.getProcesoMasivo().getSelAlmExc().getId() +
		" AND da.id = sad.idElemento ";

		Session session = currentSession();

		Query query = session.createQuery("SELECT COUNT(da) " + queryString);
		Long cantidadMaxima = (Long) query.uniqueResult();
		deudaExcProMasConsPorCtaSearchPage.setMaxRegistros(cantidadMaxima);

		// TODO agregar el order by

		// obtenemos el resultado de la consulta
		query = session.createQuery("SELECT da " + queryString);

		if (deudaExcProMasConsPorCtaSearchPage.isPaged()) {
			query.setMaxResults(deudaExcProMasConsPorCtaSearchPage.getRecsByPage().intValue());
			query.setFirstResult(deudaExcProMasConsPorCtaSearchPage.getFirstResult());
		} 

		// TODO ORDER BY
		return (ArrayList<DeudaAdmin>) query.list();
	}


	/**
	 * Obtiene la lista de DeudaAdmin de una seleccion almacenada de Deuda
	 * @param selAlm
	 * @return List<DeudaAdmin>
	 */
	public List<DeudaAdmin> getListBySelAlm(SelAlmDeuda selAlmDeuda) {
		String queryString = "SELECT da.* FROM gde_deudaAdmin da, gde_selAlmDet sad " +
			" WHERE sad.idSelAlm = %s AND da.id = sad.idElemento";
		
		queryString = String.format(queryString, selAlmDeuda.getId());
		Session session = currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createSQLQuery(queryString).addEntity(DeudaAdmin.class);

		return (ArrayList<DeudaAdmin>) query.list();
	}
	
	/**
	 * Obtiene la lista de DeudaAdmin de manera paginada para la seleccion almacenada  
	 * aplica el tipo de Detalle de Seleccion Almanacenada de deuda administrativa a los selAlmDet
	 * @param selAlm
	 * @param firstResult
	 * @param maxResults
	 * @return List<DeudaAdmin>
	 */
	public List<DeudaAdmin> getListBySelAlm(SelAlmDeuda selAlmDeuda, Integer firstResult, Integer maxResults) {
		String queryString = "SELECT SKIP %s FIRST %s da.* " +
			" FROM gde_deudaAdmin da, gde_selAlmDet sad " +
			" WHERE sad.idSelAlm = %s " +
			"   AND sad.idTipoSelAlmDet = %s " +
			"   AND da.id = sad.idElemento ";
		
		queryString = String.format(queryString, firstResult, maxResults, selAlmDeuda.getId(), TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM);
		Session session = currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createSQLQuery(queryString).addEntity(DeudaAdmin.class);

		return (ArrayList<DeudaAdmin>) query.list();
	}


	/**
	 * Obtiene la lista de DeudaAdmin de manera paginada
	 * @param selAlm
	 * @param firstResult
	 * @param maxResults
	 * @return ScrollableResults
	 */
	public ScrollableResults getScrollBySelAlm(SelAlmDeuda selAlmDeuda) {
		String queryString = " FROM DeudaAdmin da, SelAlmDet sad  " +
							"WHERE sad.selAlm.id = " + selAlmDeuda.getId() +
							" AND da.id = sad.idElemento ";

		Session session = currentSession();

		// obtenemos el resultado de la consulta
		ScrollableResults sr = session.createQuery("SELECT da " + queryString)
			.setReadOnly(true)
			.setCacheMode(CacheMode.IGNORE)
			.scroll(ScrollMode.FORWARD_ONLY);
		return sr;
	}

	/**
	 * Elimina las Deudas Administrativas que corresponden al Envio Judicial
	 * Estas deudas estan cargadas en las Constancias de las Planillas del Envio
	 * @param procesoMasivo
	 * @return int
	 */
	public int deleteListDeudaAdminByProcesoMasivo (ProcesoMasivo procesoMasivo){

		String queryString = "DELETE FROM DeudaAdmin da WHERE da IN (" +
				"SELECT idDeuda FROM ConDeuDet cdd WHERE cdd.constanciaDeu.procesoMasivo = :procesoMasivo) ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);

		query.setEntity("procesoMasivo", procesoMasivo);
	    
		return query.executeUpdate();
	}
	
	public DeudaAdmin getByCodRefPag(Long codRefPag){
		DeudaAdmin deudaAdmin;
		String queryString = "from DeudaAdmin t where t.codRefPag = :codRefPag";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("codRefPag", codRefPag);
		deudaAdmin = (DeudaAdmin) query.uniqueResult();	

		return deudaAdmin; 
	}
	
	public DeudaAdmin getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		DeudaAdmin deudaAdmin;
		String queryString = "from DeudaAdmin t where t.cuenta.numeroCuenta = :nroCta and t.periodo = :per";
		queryString += " and t.anio = :anio and t.sistema.nroSistema = :nroSis and t.resto = :resto";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		// 14/09/2009: Con la introduccion de GrE y cuentas alfanumericas,
		// numeroCuenta no puede ser long nunca.
		query.setString("nroCta", nroCta.toString());
		query.setLong("per", periodo);
		query.setLong("anio", anio);
		query.setLong("nroSis", nroSistema);
		query.setLong("resto", resto);
		deudaAdmin = (DeudaAdmin) query.uniqueResult();	

		return deudaAdmin; 
	}
	
	public List<DeudaAdmin> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		List<DeudaAdmin> listDeudaAdmin;
		String queryString = "from DeudaAdmin t where t.cuenta.numeroCuenta = :nroCta and t.periodo = :per";
		queryString += " and t.anio = :anio and t.sistema.nroSistema = :nroSis and t.resto = :resto";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		// 14/09/2009: Con la introduccion de GrE y cuentas alfanumericas,
		// numeroCuenta no puede ser long nunca.
		query.setString("nroCta", nroCta.toString());
		query.setLong("per", periodo);
		query.setLong("anio", anio);
		query.setLong("nroSis", nroSistema);
		query.setLong("resto", resto);
		listDeudaAdmin = (ArrayList<DeudaAdmin>) query.list();	

		return listDeudaAdmin; 
	}

	/**
	 * Obtiene el String del SQL para la busqueda de deuda a incluir 
	 * el elemento de indice cero es la parte del INNER y 
	 * el elemento de indice uno es la parte del WHERE
	 * @param Map<String,String> mapaFiltros
	 * @return String[]
	 * @throws Exception
	 */
	public String[] getSQLsForParamObjImp(Map<String,String> mapaFiltros) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String[] resultado = {"",""};
		
		String idTipObjImp = mapaFiltros.get(SelAlmAgregarParametrosSearchPage.ID_TIP_OBJ_IMP);
		
		TipObjImp tipObjImp = TipObjImp.getByIdNull(Long.valueOf(idTipObjImp)); 
		
		String strFrom = " INNER JOIN pad_objImp oi ON (cta.idobjimp == oi.id AND oi.idtipobjimp == " + idTipObjImp + ") ";
		
		String strWhereAva = "";
		String alias = "";
		String strSectionWhereAva = "";

		int contAlias = 1; // Contador ulilizado para crear los nombres de los alias cada vez que se joinea con ObjImpAtrVal
		
		// Armamos filtros del SQL dinamicamente 
		// Recorro la definicion del tipo objeto imposible y por cada 
		// Atributo Valor que se encuentre "valorizado" agrego una clausula al SQL
		// Teniendo en cuenta la mismas reglas que se usaron para armar la GUI de busqueda (combo, checkbox, text, text desde y hasta) y 
		// el tipo de datos (Date, Long, String, Double).
		
		TipObjImpDefinition toid = tipObjImp.getDefinitionForBusqueda(Long.valueOf(idTipObjImp));

		for(TipObjImpAtrDefinition tipObjImpAtrDefinition: toid.getListTipObjImpAtrDefinition()){
			tipObjImpAtrDefinition.populateAtrVal4Busqueda( mapaFiltros);
		}
		
		if(!toid.poseeValoresCargados()){
			log.debug("no posee valores cargados");
			return resultado;	
		}
		
		for(TipObjImpAtrDefinition tipObjImpAtrDefinition: toid.getListTipObjImpAtrDefinition()){
			//tipObjImpAtrDefinition.populateAtrVal4Busqueda( mapaFiltros);
			// Si posee valor cargago en la GUI
			
			if ( tipObjImpAtrDefinition.poseeValorCargado() ){
								
				alias = "oiav";
				alias = alias + contAlias;
				strFrom += " INNER JOIN pad_objImpAtrVal "+ alias +" ON (oi.id == "+ alias +".idobjimp) ";
				 
				contAlias++;

				strSectionWhereAva += " AND " + alias + ".idtipobjimpatr = " + tipObjImpAtrDefinition.getTipObjImpAtr().getId();
				
				// Posee Dominio 
				if (tipObjImpAtrDefinition.getPoseeDominio()) { 
					
					// Admite busqueda por rango (Checkbox)
					if (tipObjImpAtrDefinition.getAdmBusPorRan()){
						// Bucle, si hay mas de un valor chequeado, utilizar el OR
						strSectionWhereAva += " and (";
						Iterator it = tipObjImpAtrDefinition.getListValor().iterator();
						
						while (it.hasNext()) {
							Object[] valor = (Object[])it.next();
							
							strSectionWhereAva += PadDAOFactory.getObjImpDAO().str4TipoDato(alias , tipObjImpAtrDefinition, (String)valor[0], "=");
							
							if (it.hasNext())
								strSectionWhereAva += " OR ";
						}
						
						strSectionWhereAva += " ) ";
						
					} else {	
						// No Adminte busqueda por rango (Combo) y existe valor seleccionado
						if (!tipObjImpAtrDefinition.getValorString().trim().equals("-1")){
							strSectionWhereAva +=  " AND " + PadDAOFactory.getObjImpDAO().str4TipoDato(alias , tipObjImpAtrDefinition, tipObjImpAtrDefinition.getValorString(), "=");
						}
					}
				}
				
				// No posee dominio
				if (!tipObjImpAtrDefinition.getPoseeDominio() ) {
					
					// Admite Busqueda por rango (Text desde y Text Hasta)
					if (tipObjImpAtrDefinition.getAdmBusPorRan()){
						// Desde
						if (!tipObjImpAtrDefinition.getValorDesdeString().trim().equals("")){
							strSectionWhereAva +=  " AND " + PadDAOFactory.getObjImpDAO().str4TipoDato(alias , tipObjImpAtrDefinition, tipObjImpAtrDefinition.getValorDesdeString(), ">=");
						}
						
						// Hasta
						if (!tipObjImpAtrDefinition.getValorHastaString().trim().equals("")){
							strSectionWhereAva +=  " AND " + PadDAOFactory.getObjImpDAO().str4TipoDato(alias , tipObjImpAtrDefinition, tipObjImpAtrDefinition.getValorHastaString(), "<=");							
						}
						
					// No admite Busqueda por rango (Text)
					} else {
						strSectionWhereAva +=  " AND " + PadDAOFactory.getObjImpDAO().str4TipoDato(alias , tipObjImpAtrDefinition, tipObjImpAtrDefinition.getValorString(), "=");
					}					
				}
				
				strWhereAva += strSectionWhereAva;
				
				log.debug("		Atributo: " + tipObjImpAtrDefinition.getAtributo().getCodAtributo());
				log.debug( strSectionWhereAva );
				
				strSectionWhereAva = "";
			}
		}
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": strFrom : " + strFrom);
	    if (log.isDebugEnabled()) log.debug(funcName + ": strWhereAva : " + strWhereAva);
	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	    
	    String[] salida = {strFrom,strWhereAva};
	    
		return salida;
	}

	 public int copiarDeudaAAdmin(DevolucionDeuda devolucionDeuda) throws Exception {
		 	
		 String camposComunesDeuda = "id, " +
									"codrefpag, " +
									"idcuenta, " +
									"idreccladeu, " +
									"idviadeuda, " +
									"idestadodeuda, " +
									"periodo, " +
									"anio, " +
									"fechaemision, " +
									"fechavencimiento, " +
									"importebruto, " +
									"importe, " +
									"saldo, " +
									"actualizacion, " +
									"strconceptosprop, " +
									"strestadodeuda, " +
									"estaimpresa, " +
									"idrepartidor, " +
									"idprocurador, " +
									"fechapago, " +
									"idprocedimientocyq, " +
									"actualizacioncyq, " +
									"idemision, " +
									"obsmotnopre, " +
									"reclamada, " +
									"idsistema, " +
									"resto, " +
									"idconvenio, " +
									"usuario, " +
									"fechaultmdf, " +
									"estado," +
									"idRecurso," +
									"atrAseVal" ;
		 
			String sqlInsert = "INSERT INTO gde_deudaAdmin " + 
			 "SELECT "+camposComunesDeuda+" FROM gde_deudajudicial dj WHERE dj.id IN (" +
			 	"SELECT ddd.iddeuda FROM gde_devDeuDet ddd " +
			 	"WHERE ddd.idDevolucionDeuda = " + devolucionDeuda.getId() + ")";

			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createSQLQuery(sqlInsert);

			return query.executeUpdate();
		}

 	/**
 	 * Retorna la deuda administrativa filtrando por idCuenta, 
 	 * anio, periodo y si esta o no impresa
 	 * @param idCuenta
 	 * @param anio
 	 * @param periodo
 	 * @param estaImpresa
 	 * @return List<DeudaAdmin>
 	 * */
	 public List<DeudaAdmin> getListDeudaAdminByIdCuentaAndAnioAndPeriodo
			(Long idCuenta, Integer anio, Integer periodo, Integer estaImpresa){
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	 		
			Session session = SiatHibernateUtil.currentSession();
			
			String queryString = "select * from gde_deudaAdmin t ";
			boolean flagAnd = false;
			
			// filtro por cuenta
	 		if (!(idCuenta == null)) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.idCuenta = " + idCuenta;
				flagAnd = true;
			}
			
			// filtro por anio
	 		if (!(anio == null)) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " Year(t.fechaVencimiento) = " + anio;
				flagAnd = true;
			}
			
	 		// filtro por periodo
	 		if (!(periodo == null)) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " Month(t.fechaVencimiento) = " + periodo;
				flagAnd = true;
			}

	 		// filtro por estaImpresa
	 		if (!(estaImpresa == null)) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " t.estaImpresa = " + estaImpresa;
				flagAnd = true;
			}
	 		
	 		//filtro por Deuda Administrativa
	 		EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
            queryString += flagAnd ? " and " : " where ";
            queryString += " t.idEstadoDeuda = "+ estadoDeuda.getId();

			
			if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	 		
	 		Query query = session.createSQLQuery(queryString).addEntity(DeudaAdmin.class);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return (ArrayList<DeudaAdmin>) query.list();		
		}

	 public int deleteListDeudaAdminByEmision (Emision emision){

		 String queryString = "DELETE FROM DeudaAdmin da WHERE da.emision = :emision ";

		 Session session = SiatHibernateUtil.currentSession();
		 Query query = session.createQuery(queryString);

		 query.setEntity("emision", emision);

		 return query.executeUpdate();
	 }


	 public List<DeudaAdmin> getByEmision(Emision emision) {
		String queryString = "from DeudaAdmin t where t.emision.id ="+emision.getId();
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);

		return query.list();
	}
	
	 /**
	  * Retorna la deuda administrativa (ordenada por periodo) 
	  * de la cuenta con id idCuenta, y con periodos que se encuentran
	  * en el intervalo [(periodoDesde,anioDesde), (periodoHasta,anioHasta)] 
	  * 
	  * @param idCuenta
	  * @param periodoDesde
	  * @param anioDesde
	  * @param periodoHasta
	  * @param anioHasta
	  * @return List<DeudaAdmin>
	  * */
	 public List<DeudaAdmin> getListDeudaAdminByRangoPeriodoAnio (Long idCuenta, 
			 	Integer periodoDesde, Integer anioDesde, 
			 	Integer periodoHasta, Integer anioHasta){
				
				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		 		
				Session session = SiatHibernateUtil.currentSession();
				
				EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
				
				String queryString = "select * from gde_deudaAdmin t ";
				boolean flagAnd = false;
				
				// filtro por cuenta
		 		if (!(idCuenta == null)) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.idCuenta = " + idCuenta;
					flagAnd = true;
				}
				
		 		// filtro por periodoDesde
		 		if (!(periodoDesde == null) ) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.periodo >= " + periodoDesde;
					flagAnd = true;
				}

		 		// filtro por anioDesde
		 		if (!(anioDesde == null) ) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.anio >= " + anioDesde;
					flagAnd = true;
				}
		 		
		 		
		 		// filtro por periodoHasta
		 		if (!(periodoHasta == null)) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.periodo <= " + periodoHasta;
					flagAnd = true;
				}
		 		
		 		// filtro por anioHasta
		 		if (!(anioHasta == null) ) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.anio <= " + anioHasta;
					flagAnd = true;
				}

		 		//Deuda Administrativa
	            queryString += flagAnd ? " and " : " where ";
	            queryString += " t.idEstadoDeuda = "+ estadoDeuda.getId();
	            queryString += " order by t.periodo";
	            
				
				if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		 		
		 		Query query = session.createSQLQuery(queryString).addEntity(DeudaAdmin.class);
				
				if (log.isDebugEnabled()) log.debug(funcName + ": exit");
				return (ArrayList<DeudaAdmin>) query.list();		
			}
	 
	 /**
	  * Retorna la lista de deuda de la tabla gde_deudaAdmin (con el anio y mes de fecha 
	  * de vencimiento pasados como parametro) para la cuenta asociada a un 
	  * detalle de la planilla de cuadra con id idPlanillaCuadra.
	  * 
	  * Este metodo es utiizado para acelerar las consultas en el Worker de 
	  * impresion de deuda CdM. 
	  *
	  * @param idPlanillaCuadra
	  * @param anio
	  * @param mes
	  * @param estaImpresa
	  * @return List<DeudaAdmin>
	  * */
	 
	 public List<DeudaAdmin> getListDeudaAdminByIdPlanillaCuadraAndAnioAndPeriodo
		(Long idPlanillaCuadra, Integer anio, Integer mes, Integer estaImpresa){
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select * from gde_deudaAdmin t where";
		
		
		if (idPlanillaCuadra == null)
			return null;
		
		queryString += " t.idCuenta in (select idcuentacdm from cdm_placuadet " +
								" where idplanillacuadra = "+ idPlanillaCuadra + ")";
		
		// filtro por anio
		if (!(anio == null)) 
			queryString += " and Year(t.fechaVencimiento) = " + anio;
			
		// filtro por mes
		if (!(mes == null))
			queryString += " and Month(t.fechaVencimiento) = " + mes;
			
		// filtro por estaImpresa
		if (!(estaImpresa == null)) 
			queryString += " and t.estaImpresa = " + estaImpresa;

		// filtro de deudas en convenio - (Mantis 4750)
		 queryString += " and t.idconvenio is null";

		// filtro por Deuda Administrativa
		queryString += " and t.idEstadoDeuda = " + EstadoDeuda.ID_ADMINISTRATIVA;

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		
		Query query = session.createSQLQuery(queryString).addEntity(DeudaAdmin.class);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return (ArrayList<DeudaAdmin>) query.list();		
	}

	 /**
	  * Retorna la lista de deuda de la tabla gde_deudaAdmin (con el anio y mes de fecha 
	  * de vencimiento pasados como parametro) para la cuenta asociada a un 
	  * detalle de la planilla de cuadra con id idPlanillaCuadra.
	  * 
	  * Este metodo es utiizado para acelerar las consultas en el Worker de 
	  * impresion de deuda CdM. 
	  *
	  * @param idPlanillaCuadra
	  * @param anio
	  * @param mes
	  * @param estaImpresa
	  * @return List<DeudaAdmin>
	  * */
	 
	 public List<DeudaAdmin> getListDeudaAdminByListIdPlanillaCuadraAndAnioAndPeriodo
		(Long[] listIdPlanillaCuadra, Integer anio, Integer mes, Integer estaImpresa){
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select * from gde_deudaAdmin t where";
		
		
		if (listIdPlanillaCuadra == null)
			return null;
		
		queryString += " t.idCuenta in (select idcuentacdm from cdm_placuadet " +
								" where idplanillacuadra in (" + StringUtil.getStringComaSeparate(listIdPlanillaCuadra) + "))";
		
		// filtro por anio
		if (!(anio == null)) 
			queryString += " and Year(t.fechaVencimiento) = " + anio;
			
		// filtro por mes
		if (!(mes == null))
			queryString += " and Month(t.fechaVencimiento) = " + mes;
			
		// filtro por estaImpresa
		if (!(estaImpresa == null)) 
			queryString += " and t.estaImpresa = " + estaImpresa;
			
		//filtro por Deuda Administrativa
		queryString += " and t.idEstadoDeuda = " + EstadoDeuda.ID_ADMINISTRATIVA;

		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		
		Query query = session.createSQLQuery(queryString).addEntity(DeudaAdmin.class);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return (ArrayList<DeudaAdmin>) query.list();		
	}

	 
	 
	 
	 /**	
	  * Retorna la deuda administrativa del Plan Contado de 
	  * Contribucion de Mejoras asociada a la cuenta pasada
	  * como parametro
	  *
	  * @param cuenta
	  * @return DeudaAdmin
	  */
	 public DeudaAdmin getDeudaAdminPlanContadoCdMByCuenta(Cuenta cuenta) {
			
		 	String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	 		
			Session session = SiatHibernateUtil.currentSession();
			
			EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
			
			String queryString = "FROM DeudaAdmin d " + "WHERE " +
									"d.cuenta = :cuenta AND " +
									"d.anio = :anio AND " +
									"d.estadoDeuda = :estadoDeuda ";
									

			Query query = session.createQuery(queryString)
								.setEntity("cuenta", cuenta)
								.setLong("anio", 1L) //Plan Contado
								.setEntity("estadoDeuda", estadoDeuda);
			
			return (DeudaAdmin) query.uniqueResult();	
	 }

	 /**
	  * Devuelve una lista de DeudaAdmin para un recurso y que se encuentren vencidas a una fecha de vencimiento dada
	  * Ordenada por cuenta, periodo y anio.
	  * 
	  * @author Cristian
	  * @param idRecurso
	  * @param fechaVencimiento
	  * @param skip
	  * @param first
	  * @return
	  * @throws Exception
	  */
	 @Deprecated
	 public List<DeudaAdmin> getListDeudaVencidaByIdRecursoYFecha(Long idRecurso, Date fechaVencimiento, Long skip, Long first) throws Exception {
		 
	 	String queryString = "SELECT SKIP " + skip + " FIRST " + first + " deuda.* " + 
			"FROM gde_deudaAdmin deuda "+
			"WHERE deuda.idRecurso = " + idRecurso + " AND " +
			"deuda.idEstadoDeuda = " + EstadoDeuda.ID_ADMINISTRATIVA + " AND " +
			"deuda.idViaDeuda = " + ViaDeuda.ID_VIA_ADMIN + " AND " +
			"deuda.fechaVencimiento < TO_DATE('" + DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') " +
			"ORDER BY idCuenta, periodo, anio ";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString).addEntity("deuda", DeudaAdmin.class);
		
		return (ArrayList<DeudaAdmin>) query.list(); 
	 }
	 
	 
	 /**
	  * Devuelve una lista de deuda vencida a la fechaVencimiento data para una cuenta y recurso.  
	  * 
	  * 
	  * @author Cristian
	  * @param idCuenta
	  * @param idRecurso
	  * @param fechaVencimiento
	  * @return
	  * @throws Exception
	  */
	 public List<DeudaAdmin> getListDeudaVencidaByIdCuentaIdRecursoFecha(Long idCuenta, Long idRecurso, 
			 Date fechaVencimiento) throws Exception {
		 
	 	String queryString = "SELECT deuda.* " + 
			"FROM gde_deudaAdmin deuda "+
			"WHERE deuda.idCuenta = " + idCuenta + " AND " +
			"deuda.idRecurso = " + idRecurso + " AND " +
			"deuda.idEstadoDeuda = " + EstadoDeuda.ID_ADMINISTRATIVA + " AND " +
			"deuda.idViaDeuda = " + ViaDeuda.ID_VIA_ADMIN + " AND " +
			"deuda.fechaVencimiento < TO_DATE('" + DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') " +
			"ORDER BY idCuenta, anio, periodo ";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString).addEntity("deuda", DeudaAdmin.class);
		
		return (ArrayList<DeudaAdmin>) query.list(); 
	 }
	 
		/**
		 * Copia la deuda judicial a deuda administrativa
		 * Setea la via cada deuda a Administrativa
		 * Setea el estado de cada deuda a Administrativa
		 * Nulea el procurador de cada deuda.
		 * @param  deudaJudicial
		 * @return int
		 * @throws Exception
		 */
		public int copiarDeudaJudicialAAdmin(DeudaJudicial deudaJudicial) throws Exception {
			
			Session session = SiatHibernateUtil.currentSession();

			// nuleo el procurador y realizo un flush para que lo tome el INSERT posterior
			deudaJudicial.setProcurador(null);
			session.flush();
			
			String camposSelect = "dj.id, codrefpag, idcuenta, idreccladeu, " + ViaDeuda.ID_VIA_ADMIN + " idviadeuda, " + 
				EstadoDeuda.ID_ADMINISTRATIVA + " idestadodeuda, periodo, anio, fechaemision, fechavencimiento, " +
				"importebruto, importe, saldo, actualizacion, strconceptosprop, strestadodeuda, estaimpresa, " +
				"idrepartidor, dj.idprocurador, fechapago, idprocedimientocyq, actualizacioncyq, idemision, " +
				"obsmotnopre, reclamada, idsistema, resto, idconvenio, dj.usuario, dj.fechaultmdf, dj.estado, " +
				"idrecurso, atraseval ";
			
			String sqlInsert = "INSERT INTO gde_deudaadmin " +
				"SELECT " + camposSelect + " FROM gde_deudajudicial dj " +
				"WHERE dj.id == " + deudaJudicial.getId();
			
			Query query = session.createSQLQuery(sqlInsert);
			
			return query.executeUpdate();
		}

		/**
	 	 * Retorna la deuda administrativa filtrando por idCuenta, 
	 	 * anio, periodo.
	 	 * @param idCuenta
	 	 * @param anio
	 	 * @param periodo
	 	 * @return List<DeudaAdmin>
	 	 * */
		 public List<DeudaAdmin> getListDeudaAdminForAsentamiento
				(Long idCuenta, Integer anio, Integer periodo){
				
				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		 		
				Session session = SiatHibernateUtil.currentSession();
				
				String queryString = "select * from gde_deudaAdmin t ";
				boolean flagAnd = false;
				
				// filtro por cuenta
		 		if (!(idCuenta == null)) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.idCuenta = " + idCuenta;
					flagAnd = true;
				}
				
				// filtro por anio
		 		if (!(anio == null)) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.anio = " + anio;
					flagAnd = true;
				}
				
		 		// filtro por periodo
		 		if (!(periodo == null)) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.periodo = " + periodo;
					flagAnd = true;
				}
		 		
		 		//filtro por Deuda Administrativa
		 		EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
	            queryString += flagAnd ? " and " : " where ";
	            queryString += " t.idEstadoDeuda = "+ estadoDeuda.getId();

				
				if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
		 		
		 		Query query = session.createSQLQuery(queryString).addEntity(DeudaAdmin.class);
				
				if (log.isDebugEnabled()) log.debug(funcName + ": exit");
				return (ArrayList<DeudaAdmin>) query.list();		
			}

	 
	/**
	 *  Devuelve la deuda Administrativa incluida en un procedimiento de Concurso y Quiebra, ordenada por cuenta
	 *  
	 * @param procedimiento
	 * @return
	 */	 
	 public List<DeudaAdmin> getByProcedimientoCyq(Long idProcedimiento){
		 String funcName = DemodaUtil.currentMethodName();
		 if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
		 Session session = SiatHibernateUtil.currentSession();
		 
		String sQuery = "FROM DeudaAdmin d " +
						"WHERE d.idProcedimientoCyQ = :idProcedimiento "; // + "ORDER BY d.cuenta, d.anio, d.periodo";
		
		log.debug(funcName + ": Query: " + sQuery);
		
		Query query = session.createQuery(sQuery).setLong("idProcedimiento", idProcedimiento );

		return query.list();	
		 
	 }
	
	/**
	 * periodoDesde-anioDesde y periodoHasta-anioHasta se toman como pares para filtro
	 * @param listCuenta
	 * @param periodoDesde
	 * @param anioDesde
	 * @param periodoHasta
	 * @param anioHasta
	 * @param listRecClaDeu
	 * @param cuentasPerAniosExcluir Array de String donde c/u tiene el formato: idCuenta-periodo-anio, sin separacion
			 periodo -> 2 lugares; anio -> 4 lugares
			 Ej: 556071012008  donde 556071= idCuenta   01=periodo   2008=anio
	 * @return
	 */
	public List<DeudaAdmin> getList(List<Cuenta> listCuenta,Integer periodoDesde, Integer anioDesde, 
			Integer periodoHasta,Integer anioHasta, List<RecClaDeu> listRecClaDeu, 
			String[] cuentasPerAniosExcluir) {
	 	String queryString = "SELECT deuda.* FROM gde_deudaAdmin deuda ";
	 	
	 	boolean flagAnd = false;
	 	
	 	// lista de cuentas
	 	if(listCuenta!=null && listCuenta.size()>0){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += "deuda.idCuenta IN("+StringUtil.getStringComaSeparate(
	 											ListUtilBean.getArrLongIdFromListBaseBO(listCuenta))+")";
	 		flagAnd = true;
	 	}

	 	// lista de recClaDeu
	 	if(listRecClaDeu!=null && listRecClaDeu.size()>0){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += "deuda.idRecClaDeu NOT IN("+StringUtil.getStringComaSeparate(
	 											ListUtilBean.getArrLongIdFromListBaseBO(listRecClaDeu))+")";
	 		flagAnd = true;
	 	}

	 	// periodoDesde - anioDesde
	 	if(periodoDesde!=null && anioDesde!=null){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += " (  (deuda.periodo >= "+periodoDesde + " AND "+"deuda.anio = "+anioDesde +") OR" +
	 				" (deuda.anio >"+anioDesde +") )";
	 		flagAnd = true;
	 	}
	 	
	 	// periodoHasta - anioHasta
	 	if(periodoHasta!=null && anioHasta!=null){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += " (  (deuda.periodo <= "+periodoHasta + " AND deuda.anio = "+anioHasta +") OR" +
	 				" (deuda.anio <"+anioHasta +") )";
	 		flagAnd = true;
	 	}
	 	
	 	// cuentasPeriodosAnios excluir
	 	if(cuentasPerAniosExcluir!=null){
	 		for(String valor: cuentasPerAniosExcluir){
		 		queryString+=flagAnd?" AND ":" WHERE ";
		 		queryString +="deuda.idcuenta*1000000+deuda.periodo*10000+deuda.anio <>"+valor;
		 		flagAnd = true;
	 		}
	 	}
	 	
 		queryString +=" ORDER BY anio, periodo ";
	
		Session session = SiatHibernateUtil.currentSession();
	
		Query query = session.createSQLQuery(queryString).addEntity("deuda", DeudaAdmin.class);
		
		return (ArrayList<DeudaAdmin>) query.list(); 
	}	 
	
	/**
	 * Obtiene una lista de deudaAdmin mediante cuenta, periodo, anio y lista de recClaDeu a EXCLUIR
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param listRecClaDeu
	 * @return
	 */
	public List<DeudaAdmin> getList(Cuenta cuenta,Integer periodo, Integer anio, List<RecClaDeu> listRecClaDeu) {
	 	String queryString = "SELECT deuda.* FROM gde_deudaAdmin deuda ";
	 	
	 	boolean flagAnd = false;
	 	
	 	// cuenta
	 	if(cuenta!=null){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += "deuda.idCuenta = "+cuenta.getId();
	 		flagAnd = true;
	 	}

	 	// lista de recClaDeu
	 	if(listRecClaDeu!=null && listRecClaDeu.size()>0){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += "deuda.idRecClaDeu NOT IN("+StringUtil.getStringComaSeparate(
	 											ListUtilBean.getArrLongIdFromListBaseBO(listRecClaDeu))+")";
	 		flagAnd = true;
	 	}

	 	// periodo
	 	if(periodo !=null){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += " deuda.periodo = "+periodo;
	 		flagAnd = true;
	 	}
	 	
	 	// anio
	 	if(anio != null){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += " deuda.anio = "+anio;
	 		flagAnd = true;
	 	}
	 	
	 	
 		queryString +=" ORDER BY anio, periodo ";
	
		Session session = SiatHibernateUtil.currentSession();
	
		Query query = session.createSQLQuery(queryString).addEntity("deuda", DeudaAdmin.class);
		
		return (ArrayList<DeudaAdmin>) query.list(); 
	}	 
	

	/**
	 * Obtiene la sumatoria de deuda.importe para las deudas con los valores pasados como parametro
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param recClaDeu
	 * @return
	 */
	public Double getImporteDeclarado(Cuenta cuenta,Integer periodo, Integer anio, List<RecClaDeu> listRecClaDeu) {
	 	String queryString = "SELECT sum(deuda.importe) FROM gde_deudaAdmin deuda ";
	 	
	 	boolean flagAnd = false;
	 	
	 	// lista de cuentas
	 	if(cuenta!=null){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += "deuda.idCuenta ="+cuenta.getId();
	 		flagAnd = true;
	 	}

	 	// lista de recClaDeu
	 	if(listRecClaDeu!=null && listRecClaDeu.size()>0){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += "deuda.idRecClaDeu IN("+StringUtil.getStringComaSeparate(
	 											ListUtilBean.getArrLongIdFromListBaseBO(listRecClaDeu))+")";
	 		flagAnd = true;
	 	}

	 	// periodoDesde - anioDesde
	 	if(periodo!=null && anio!=null){
	 		queryString+=flagAnd?" AND ":" WHERE ";
	 		queryString += " deuda.periodo = "+periodo + " AND "+"deuda.anio = "+anio;
	 		flagAnd = true;
	 	}
	 		 		 	
	 	log.debug("query:"+queryString);
	 	
		Session session = SiatHibernateUtil.currentSession();
	
		Query query = session.createSQLQuery(queryString);
		
		BigDecimal importeDeclarado = (BigDecimal) query.uniqueResult();
		log.debug("Obtuvo:"+importeDeclarado);
		
		return (importeDeclarado!=null?importeDeclarado.doubleValue():null); 
	}	

	/**
	 * Calcula la fecha de Vto para el periodo pasado como parametro.
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param listRecClaDeu
	 * @return
	 */
	public Date getFecVtoPeriodo(Cuenta cuenta,Integer periodo, Integer anio, 
			  															List<RecClaDeu> listRecClaDeu) {
		String queryString = "FROM DeudaAdmin deuda ";
		
		boolean flagAnd = false;
		
		// lista de cuentas
		if(cuenta!=null){
		queryString+=flagAnd?" AND ":" WHERE ";
		queryString += "deuda.cuenta.id ="+cuenta.getId();
		flagAnd = true;
		}
		
		// lista de recClaDeu
		if(listRecClaDeu!=null && listRecClaDeu.size()>0){
		queryString+=flagAnd?" AND ":" WHERE ";
		queryString += "deuda.recClaDeu.id IN("+StringUtil.getStringComaSeparate(
		ListUtilBean.getArrLongIdFromListBaseBO(listRecClaDeu))+")";
		flagAnd = true;
		}
		
		// periodoDesde - anioDesde
		if(periodo!=null && anio!=null){
		queryString+=flagAnd?" AND ":" WHERE ";
		queryString += " deuda.periodo = "+periodo + " AND "+"deuda.anio = "+anio;
		flagAnd = true;
		}
		
		log.debug("query:"+queryString);
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		DeudaAdmin deudaAdmin = (DeudaAdmin) query.uniqueResult();		
		
		return (deudaAdmin!=null?deudaAdmin.getFechaVencimiento():null); 
	}	
	
	public List<DeudaAdmin> getListDeudaAdminByPerAnioCuentaSinPagos(Long periodo, Long anio, Cuenta cuenta){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM DeudaAdmin da WHERE da.cuenta.id = "+cuenta.getId();
			   queryString += " AND da.periodo = :periodo AND da.anio= :anio";
			   queryString += " AND (da.recClaDeu.id =" + RecClaDeu.ID_DDJJ_ORIGINAL;
			   queryString += " OR da.recClaDeu.id = "+RecClaDeu.ID_DDJJ_RECTIFICATIVA_DREI + ")";
			   queryString += " AND da.id NOT IN (";
			   queryString += "SELECT DISTINCT d.id FROM DeudaAdmin d, PagoDeuda p WHERE d.id = p.idDeuda AND d.cuenta.id = "+cuenta.getId();
			   queryString += " AND d.periodo = :periodo AND d.anio= :anio";
			   queryString += " AND (d.recClaDeu.id =" + RecClaDeu.ID_DDJJ_ORIGINAL;
			   queryString += " OR d.recClaDeu.id = "+RecClaDeu.ID_DDJJ_RECTIFICATIVA_DREI + ")";
			   queryString += " AND (p.tipoPago.id != "+TipoPago.ID_PERCEPCION_DECLARADA;
			   queryString += " OR p.tipoPago.id != "+TipoPago.ID_RETENCION_DECLARADA + ")";
			   queryString += ")";
		
		log.debug("QUERYSTRING: "+queryString);
		
		
		Query query = session.createQuery(queryString).setLong("periodo", periodo)
													.setLong("anio", anio);
		List<DeudaAdmin> listDeudaAdmin =(List<DeudaAdmin>) query.list();
		
		return listDeudaAdmin;
	}

	 /**
	  * Devuelve una lista de deuda administrativa 
	  * vencida a la fechaVencimiento  
	  *
	  * @param idCuenta
	  * @param idRecurso
	  * @param fechaVencimiento
	  * @return
	  * @throws Exception
	  */
	@SuppressWarnings("unchecked") 
	public List<DeudaAdmin> getListDeudaAdminBy(Long idCuenta, Long idRecurso, Date fechaVencimiento) throws Exception {
		 
	 	String strQuery = ""; 
	 	strQuery += "from DeudaAdmin deuda ";
	 	strQuery +=	"where deuda.cuenta.id  = :idCuenta ";
	 	strQuery +=	  "and deuda.recurso.id = :idRecurso ";
	 	strQuery +=	  "and deuda.estadoDeuda.id = :idEstadoDeuda ";
	 	strQuery +=	  "and deuda.viaDeuda.id = :idViaDeuda ";
	 	strQuery +=	  "and deuda.fechaVencimiento < :fechaVencimiento ";
	 	strQuery +=	  "order by deuda.cuenta.id, anio, periodo ";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idCuenta", idCuenta)
							 .setLong("idRecurso", idRecurso)
							 .setLong("idEstadoDeuda", EstadoDeuda.ID_ADMINISTRATIVA)
							 .setLong("idViaDeuda", ViaDeuda.ID_VIA_ADMIN)
							 .setDate("fechaVencimiento", fechaVencimiento);
		
		return (ArrayList<DeudaAdmin>) query.list(); 
	 }
	
	 /**
	  * Devuelve una lista paginada de deuda 
	  * administrativa y sus numeros de cuenta   
	  *
	  * @param idRecurso
	  * @param anio
	  * @param periodo
	  * @param first
	  * @param size
	  * @return
	  */
	@SuppressWarnings("unchecked") 
	public List<Object[]> getListDeudaAdminBy(Long idRecurso, Long anio, Long periodo) {
		 
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": enter");

		String strQuery = ""; 
	 	strQuery += "select deuda.importe, deuda.fechaVencimiento, cuenta.numeroCuenta ";
	 	strQuery += "from gde_deudaAdmin deuda, pad_cuenta cuenta ";
	 	strQuery +=	"where deuda.idCuenta = cuenta.id ";
	 	strQuery +=	  " and deuda.idRecurso = " + idRecurso;
	 	strQuery +=	  " and deuda.anio = " + anio;
	 	strQuery +=	  " and deuda.periodo = " + periodo;
	 	
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);

	 	Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(strQuery)
                                .addScalar("importe", Hibernate.DOUBLE)
                                .addScalar("fechaVencimiento", Hibernate.DATE)
                                .addScalar("numeroCuenta", Hibernate.STRING);
		
		if (log.isDebugEnabled()) log.debug(DemodaUtil.currentMethodName() + ": exit");

		return (ArrayList<Object[]>) query.list(); 
	 }

	 /**
	  * Devuelve una lista de ids de deuda administrativa
   	  *
	  * @param idRecurso
	  * @param anio
	  * @param periodoDesde
	  * @param periodoHasta
	  * @return
	  */
	@SuppressWarnings("unchecked") 
	public List<Long> getListIdDeudaAdminBy(Recurso recurso, Integer anio, Integer periodoDesde, Integer periodoHasta) {
		 
	 	String strQuery = ""; 
	 	strQuery += "select deuda.id from DeudaAdmin deuda ";
	 	strQuery +=	"where deuda.recurso.id = :idRecurso ";
	 	strQuery +=	  "and deuda.anio = :anio ";
	 	strQuery +=	  "and (deuda.periodo >= :periodoDesde or deuda.periodo <= :periodoHasta) ";
	 	strQuery +=	  "and deuda.importe > 0 "; // Validar
	 	
	 	Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idRecurso", recurso.getId())
							 .setLong("anio", anio)
							 .setLong("periodoDesde", periodoDesde)
							 .setLong("periodoHasta", periodoHasta);

		return (ArrayList<Long>) query.list(); 
	 }

	public static void setMigId(long migId) {
		DeudaAdminDAO.migId = migId;
	}

	public static long getMigId() {
		return DeudaAdminDAO.migId;
	}
	
	
	/**
	 * Obtiene deuda para mostrar en el CU cierre Comercio, 
	 * la misma corresponde a periodos declarado impagos o no declarados. 
	 * 
	 * @param cuenta
	 * @return
	 */
	public List<DeudaAdmin> getDeudaForCierreComercio(Cuenta cuenta){
		Session session = SiatHibernateUtil.currentSession();
		
		EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
		
		String sQuery = "FROM DeudaAdmin d " +
						"WHERE d.cuenta = :cuenta AND " +
						" d.estadoDeuda = :estadoDeuda " ;

		// armamos cadena Join con declaracion jurada
		String strQryNotIn = "";

		String listIdsDeudaDecJur = "";
		String qryDecJur = "SELECT decJur.idDeuda FROM gde_decjur decJur " +
						   "WHERE estado = 1 AND decJur.idCuenta = " + cuenta.getId();
		
		Query sqlQry = session.createSQLQuery(qryDecJur);
		
		List<Integer> listId = (ArrayList<Integer>) sqlQry.list();
		
		if(!ListUtil.isNullOrEmpty(listId)){
			listIdsDeudaDecJur = "(";
			for(Integer id: listId){
				if(!listIdsDeudaDecJur.equals("("))
					listIdsDeudaDecJur += ",";
				listIdsDeudaDecJur += id.toString();
			}
			listIdsDeudaDecJur +=")";
		}
		
		log.debug("listIdsDeudaDecJur: " + listIdsDeudaDecJur);				
		
		// No en Declaracion Jurada
		if (listIdsDeudaDecJur != ""){
			strQryNotIn += " AND d.id NOT IN " + listIdsDeudaDecJur; 
		}
		
		sQuery += "AND (( d.importe > 0 AND d.saldo > 0 ) ";
		sQuery += "OR (d.importe = 0 AND d.saldo = 0 "+ strQryNotIn +" ))";
		
		log.debug("sQuery: " + sQuery);
		sQuery +=	" ORDER BY d.anio, d.periodo";

		Query query = session.createQuery(sQuery)
						.setEntity("cuenta", cuenta)
						.setEntity("estadoDeuda", estadoDeuda);
	
		return query.list();		
		
	}
	
	public List<DeudaAdmin>getListDreiEturNoDeclaradaByContribuyente(Contribuyente contribuyente, Date fechaDesde){
		
		Recurso drei = Recurso.getDReI();
		Recurso etur = Recurso.getETur();
		//En Drei y Etur el periodo vence al mes siguiente
		Date fechaVencimientoDesde = DateUtil.getFirstDayOfMonth(DateUtil.addMonthsToDate(fechaDesde, 1));
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT da FROM DeudaAdmin da, CuentaTitular ct WHERE da.cuenta.id = ct.cuenta.id AND (da.recurso.id = "+drei.getId();
		queryString += " OR da.recurso.id = "+etur.getId() +" )";
		queryString += " AND ct.contribuyente.id = "+contribuyente.getId();
		queryString += " AND da.importe = 0 AND da.saldo = 0";
		queryString += " AND (da.recClaDeu.id = "+drei.getRecClaDeuOriginal(new Date()).getId();
		queryString += " OR da.recClaDeu.id = "+etur.getRecClaDeuOriginal(new Date()).getId() + " )";
		queryString += " AND da.fechaVencimiento > :fechaVencimiento";
		
		log.debug("queryString: "+queryString);
		
		Query query= session.createQuery(queryString).setDate("fechaVencimiento", fechaVencimientoDesde);
		
		
		return (List<DeudaAdmin>)query.list();
		
	}

	
	// select * from gde_deudaadmin where idcuenta=1351613 and fechavencimiento <= '2009-09-16' and fechapago is null;

	 /**
	  *  Verifica si la cuenta posee deuda vencida en la cantidad de dias indicados y devuelve true o false.
	  *
	  * @param idCuenta
	  * @param idRecurso
	  * @param fecha
	  * @return
	  * @throws Exception
	  */
	public Boolean poseeDeudaVencida(Long idCuenta, Date fecha) throws Exception {
		 
		// Primero verificamos en Deuda Admin
	 	String strQuery = ""; 
	 	strQuery += "from DeudaAdmin deuda ";
	 	strQuery +=	"where deuda.cuenta.id  = :idCuenta ";
	 	strQuery +=	  "and deuda.fechaVencimiento < :fecha ";
	 	strQuery +=	  "and deuda.fechaPago is null ";

		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idCuenta", idCuenta)
							 .setDate("fecha", fecha);
		query.setMaxResults(1);
		DeudaAdmin deudaAdmin = (DeudaAdmin) query.uniqueResult();
		
		if(deudaAdmin != null){
			return true;
		}else{
			// Si no verificamos en Deuda Judicial
			strQuery = ""; 
		 	strQuery += "from DeudaJudicial deuda ";
		 	strQuery +=	"where deuda.cuenta.id  = :idCuenta ";
		 	strQuery +=	  "and deuda.fechaVencimiento < :fecha ";
		 	strQuery +=	  "and deuda.fechaPago is null ";

		    query = session.createQuery(strQuery)
								 .setLong("idCuenta", idCuenta)
								 .setDate("fecha", fecha);
			query.setMaxResults(1);
			DeudaJudicial deudaJudicial = (DeudaJudicial) query.uniqueResult();
			if(deudaJudicial != null){
				return true;				
			}
		}

		return false;
	 }
	
	 /**
	  *  Verifica si la cuenta posee deuda impaga a la fecha pasada y devuelve true o false.
	  *
	  * @param idCuenta
	  * @param idRecurso
	  * @param fecha
	  * @return
	  * @throws Exception
	  */
	public Boolean poseeDeudaImpaga(Long idCuenta, Date fecha) throws Exception {
		 
		// Primero verificamos en Deuda Admin
	 	String strQuery = ""; 
	 	strQuery += "from DeudaAdmin deuda ";
	 	strQuery +=	"where deuda.cuenta.id  = :idCuenta ";
	 	strQuery +=	  "and ((deuda.fechaPago is null and deuda.fechaEmision < :fecha) or";
	 	strQuery +=	  "(deuda.fechaPago is not null and deuda.fechaPago  >:fecha)) ";

		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("idCuenta", idCuenta)
							 .setDate("fecha", fecha);
		query.setMaxResults(1);
		DeudaAdmin deudaAdmin = (DeudaAdmin) query.uniqueResult();
		
		if(deudaAdmin != null){
			return true;
		}else{
			// Si no verificamos en Deuda Judicial
			strQuery = ""; 
		 	strQuery += "from DeudaJudicial deuda ";
		 	strQuery +=	"where deuda.cuenta.id  = :idCuenta ";
		 	strQuery +=	  "and ((deuda.fechaPago is null and deuda.fechaEmision < :fecha) or";
		 	strQuery +=	  "(deuda.fechaPago is not null and deuda.fechaPago  >:fecha) )";

		    query = session.createQuery(strQuery)
								 .setLong("idCuenta", idCuenta)
								 .setDate("fecha", fecha);
			query.setMaxResults(1);
			DeudaJudicial deudaJudicial = (DeudaJudicial) query.uniqueResult();
			if(deudaJudicial != null){
				return true;				
			}
		}

		return false;
	 }
	
	/**
	 * Obtiene un registro de deuda para Clasificacion de Deuda de Regimen Simplificado.
	 * 
	 * @author Tecso
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param nroSistema
	 * @param resto
	 * @return
	 */
	public Deuda getByCtaPerAnioSisResForRS(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		Deuda deuda;
		
		String queryString = "FROM DeudaAdmin deuda " + 
							" WHERE deuda.cuenta.numeroCuenta = :nroCta AND " +
							" deuda.sistema.nroSistema = :nroSis AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio AND" +
							" deuda.resto = :resto AND" +
							" deuda.recClaDeu.abrClaDeu = '"+RecClaDeu.ABR_RS+"'";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							.setString("nroCta", nroCta.toString())
							.setLong("nroSis", nroSistema)
							.setLong("periodo", periodo)
							.setLong("anio", anio)
							.setLong("resto", resto);
							
		deuda = (Deuda) query.uniqueResult();	

		return deuda; 
	}
	
	/**
	 * Obtiene un registro de deuda para Clasificacion de Deuda Original.
	 * 
	 * @author Tecso
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param nroSistema
	 * @param resto
	 * @return
	 */
	public Deuda getOriginalByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		Deuda deuda;
		
		String queryString = "FROM DeudaAdmin deuda " + 
							" WHERE deuda.cuenta.numeroCuenta = :nroCta AND " +
							" deuda.sistema.nroSistema = :nroSis AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio AND" +
							" deuda.resto = :resto AND" +
							" deuda.recClaDeu.esOriginal = 1";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
								.setString("nroCta", nroCta.toString())
								.setLong("nroSis", nroSistema)
								.setLong("periodo", periodo)
								.setLong("anio", anio)
								.setLong("resto", resto);
		
		deuda = (Deuda) query.uniqueResult();	

		return deuda; 
	}
	
	/**
	 * Obtiene un registro de deuda impaga para un Periodo y Clasificacion de Deuda de Interes Regimen Simplificado.
	 * 
	 * @author Tecso
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @param nroSistema
	 * @return
	 */
	public Deuda getByCtaPerAnioSisForIRS(Long idCuenta, Long periodo, Long anio, Long nroSistema){
		Deuda deuda;
		
		String queryString = "FROM DeudaAdmin deuda " + 
//							" WHERE deuda.cuenta.numeroCuenta = :nroCta AND " +
							" WHERE deuda.idCuenta = :idCuenta AND " +
							" deuda.sistema.nroSistema = :nroSis AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio AND" +
							" deuda.fechaPago IS NULL AND" +
							" deuda.recClaDeu.abrClaDeu = '"+RecClaDeu.ABR_IRS+"'";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
//							.setString("nroCta", nroCta.toString())
							.setLong("nroSis", nroSistema)
							.setLong("idCuenta", idCuenta)
							.setLong("periodo", periodo)
							.setLong("anio", anio);
							
		deuda = (Deuda) query.uniqueResult();	

		return deuda; 
	}
	
	
	/**
	 * Obtiene lista de deuda paga para un Periodo y Clasificacion de deuda de Interes Regimen Simplificado.
	 * 
	 * @author Tecso
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public List<Deuda> getListDeudaIRSByCtaPerAnioSis(Long idCuenta, Long periodo, Long anio){
		List<Deuda> listDeuda;
		
		String queryString = "FROM DeudaAdmin deuda " + 
							" WHERE deuda.idCuenta = :idCuenta AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio AND" +
							" deuda.fechaPago IS NOT NULL AND" +
							" deuda.recClaDeu.abrClaDeu = '"+RecClaDeu.ABR_IRS+"'";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							.setLong("idCuenta", idCuenta)
							.setLong("periodo", periodo)
							.setLong("anio", anio);
							
		listDeuda = query.list();	

		return listDeuda; 
	}
	
	/**
	 * Obtiene lista de deuda impaga para una Cuenta y un Periodo.
	 * 
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public List<Deuda> getListRectifForPagoByCtaPer(Long idCuenta, Long periodo, Long anio, Long idRecClaDeu){
		List<Deuda> listDeuda;
		
		String queryString = "FROM DeudaAdmin deuda " + 
							" WHERE deuda.idCuenta = :idCuenta AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.recClaDeu.id = :idRecClaDeu AND" +
							" deuda.anio = :anio AND" +
							" deuda.fechaPago IS NULL";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							.setLong("idCuenta", idCuenta)
							.setLong("periodo", periodo)
							.setLong("idRecClaDeu", idRecClaDeu)
							.setLong("anio", anio);
							
		listDeuda = query.list();	

		return listDeuda; 
	}
	
	/**
	 * Obtiene la sumatoria de pagos asentados por SIAT, (importe + actualizacion) para deuda paga con clasificacion Original o Rectificativa,
	 * cuenta y periodo fiscal declarado.
	 * Ignora los pagos generados por declaración jurada
	 * 
	 * @param nroCta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public Double getTotalPagosMismoPeriodo(Long idCuenta, Integer periodo, Integer anio){
		Double sumPagos;
		
		String subQueryStr="SELECT deuda.id FROM DeudaAdmin deuda "; 
			   subQueryStr+=" WHERE deuda.idCuenta = :idCuenta AND ";
			   subQueryStr+=" deuda.periodo = :periodo AND ";
			   subQueryStr+=" deuda.anio = :anio AND ";
			   subQueryStr+="(deuda.recClaDeu.abrClaDeu='"+ RecClaDeu.ABR_ORIG + "' OR ";
			   subQueryStr+=" deuda.recClaDeu.abrClaDeu='"+ RecClaDeu.ABR_RECTIF +"') AND ";
			   subQueryStr+=" deuda.fechaPago IS NOT NULL";
		
		String queryString="SELECT SUM(p.importe) FROM PagoDeuda p ";
			   queryString+=" WHERE p.idDeuda IN ("+subQueryStr+")";
			   queryString+=" AND p.tipoPago.id NOT IN ("+TipoPago.ID_RETENCION_DECLARADA +","
			   											 +TipoPago.ID_PERCEPCION_DECLARADA +","
			   											 +TipoPago.ID_CAMBIO_DE_COEFICIENTE+","
			   											 +TipoPago.ID_POR_RESOLUCION+","
			   											 +TipoPago.ID_RESTO_PERIODO_ANTERIOR+"))";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							 .setLong("idCuenta", idCuenta)
							 .setInteger("periodo", periodo)
							 .setInteger("anio", anio);
		
		sumPagos = (Double) query.uniqueResult();	
		if (sumPagos == null) sumPagos = 0D;

		return sumPagos; 
	}
	
	
	/**
	 * Obtiene el total de deuda (paga o impaga) con clasificacion "Original" y/o "Rectificativa" por cuenta para un mismo periodo.
	 * 
	 * @param idCuenta
	 * @param periodo
	 * @param anio
	 * @param paga
	 * @return
	 */
	public Double getTotalDeudaMismoPeriodo(Long idCuenta, Integer periodo, Integer anio, boolean paga){
		
		Double total;
		String queryString = "SELECT SUM(deuda.importe) FROM DeudaAdmin deuda " + 
							" WHERE deuda.idCuenta = :idCuenta AND " +
							" deuda.periodo = :periodo AND " +
							" deuda.anio = :anio AND" +
							" (deuda.recClaDeu.abrClaDeu = '"+ RecClaDeu.ABR_ORIG + "' OR "+
							" deuda.recClaDeu.abrClaDeu = '"+ RecClaDeu.ABR_RECTIF +"') AND ";
		
		if (paga)
			// Deuda paga
			queryString +=	" deuda.fechaPago IS NOT NULL ";
		else 
			//Deuda impaga
			queryString += " deuda.fechaPago IS NULL ";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							 .setLong("idCuenta", idCuenta)
							 .setInteger("periodo", periodo)
							 .setInteger("anio", anio);
		
		total = (Double) query.uniqueResult();	
		if (null == total) total = 0D;

		return total; 
	}
	
}
