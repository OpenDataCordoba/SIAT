//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.buss.dao.SiatJDBCDAO;
import ar.gov.rosario.siat.pad.buss.bean.CueExcSel;
import ar.gov.rosario.siat.pad.buss.bean.CueExcSelDeu;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class CueExcSelDeuDAO extends GenericDAO {
	
	//private Logger log = Logger.getLogger(CueExcSelDAO.class);
	
	private static long migId = -1;
	
	public CueExcSelDeuDAO(){
		super(CueExcSelDeu.class);
	}
	
	/**
	 * Obtiene la lista de CueExcSelDeu activas para una cueExcSel
	 * @param  cueExcSel
	 * @return List<CueExcSelDeu>
	 */
	public List<CueExcSelDeu> getListCueExcSelDeuActivas(CueExcSel cueExcSel){
		
		Session session = SiatHibernateUtil.currentSession();
    	
		String sQuery = "FROM CueExcSelDeu cesd " + 
						"WHERE cesd.cueExcSel = :cueExcSel " +
						"AND cesd.estado = :estActivo";
		
    	Query query = session.createQuery(sQuery)
    					.setEntity("cueExcSel", cueExcSel)
    					.setInteger("estActivo", Estado.ACTIVO.getId());
    	
    	return (ArrayList<CueExcSelDeu>) query.list();
	}

	/**
	  * Busca una Deuda de una Cuenta Excluida, 
	  * si no encuentra la combinacion devuelve null.  
	  * 
	  * @param idCueExcSel
	  * @param idDeuda
	  * @return 
	  * @throws Exception
	 */ 
	public CueExcSelDeu getCueExcSelDeuByCueExcSelYDeuda(Long idCueExcSel, Long idDeuda) throws Exception{
	    CueExcSelDeu cueExcSelDeu;
		
	    String queryString = "from CueExcSelDeu t where t.cueExcSel.id = " + idCueExcSel +
	    					 " and t.idDeuda = " + idDeuda;
	    					 
	    Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		try{
			cueExcSelDeu = (CueExcSelDeu) query.uniqueResult();
			
			return cueExcSelDeu;
			
		} catch (Exception e) {
			
			return null;				
		} 
	}

	public void loadAllExcDeuMap(Map<String, String> mapArea, Map<String, String> mapCuenta) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {
			String sql = "";
			sql += " select excdeu.idDeuda, exccue.idArea, exccue.idCuenta from ";
			sql += "   pad_cueexcseldeu excdeu, ";
			sql += "   pad_cueexcsel exccue "; 
			sql += " where ";
			sql += "   excdeu.idcueexcsel = exccue.id ";
			sql += "   and excdeu.estado = 1 ";

			con = SiatJDBCDAO.getConnection();
			con.setReadOnly(true);
			con.setAutoCommit(false);

			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			mapArea.clear();
			mapCuenta.clear();
			while (rs.next()) {
				String idDeuda = rs.getString("iddeuda");
				String idArea = rs.getString("idarea");
				String idCuenta = rs.getString("idcuenta");
				
				{ //carga mapa de idDeuda, lista de idAreas
					String idsDeuArea = mapArea.get(idDeuda);
					if (idsDeuArea == null)
						idsDeuArea = idArea;
					else 
						idsDeuArea += "," + idArea;
					mapArea.put(idDeuda, idsDeuArea);
				}
				
				{ //carga mapa de idCeunta, lista de idDeudas
					String idsDeu = mapCuenta.get(idCuenta);
					if (idsDeu== null)
						idsDeu= idDeuda;
					else 
						idsDeu += "," + idDeuda;
					mapCuenta.put(idCuenta, idsDeu);
				}

			}

			rs.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try { ps.close();} catch (Exception e) {};
			try { con.close();} catch (Exception e) {};	
		}
	}
	
	 /**
	  *  Devuelve el proximo valor de id a asignar. 
	  *  Se inicializa obteniendo el ultimo id asignado en el archivo de migracion con datos pasados como parametro
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
	  *  Inserta una linea con los datos de la CueExcSelDeu para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param cueExcSelDeu, output - CueExcSelDeu a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(CueExcSelDeu o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|idcueexcsel|iddeuda|observacion|usuario|fechaultmdf|estado
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getCueExcSel().getId());		  
		 line.append("|");
		 line.append(o.getIdDeuda());		 
		 line.append("|");
		 line.append(o.getObservacion());		 
		 line.append("|");
		 line.append(DemodaUtil.currentUserContext().getUserName());
		 line.append("|");
		 if(o.getFechaUltMdf()!=null){
			 line.append(DateUtil.formatDate(o.getFechaUltMdf(), "yyyy-MM-dd HH:mm:SS"));
		 }else{
			 line.append("2010-01-01 00:00:00");			 
		 }
		 line.append("|");
		 line.append("1");
		 line.append("|");
	      
		 output.addline(line.toString());
		 
		 // Seteamos el id generado en el bean.
		 o.setId(id);
	
		 return id;
	 }

	
}
