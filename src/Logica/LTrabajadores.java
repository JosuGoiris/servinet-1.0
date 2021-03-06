/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Datos.DPersona;
import Datos.DPuestos;
import Datos.DTrabajadores;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author josug
 */
public class LTrabajadores {
    Connection cn = ConexionSingleton.getConnection();
    private String sSQL = null;
    private String sSQL1 = null;
    private String sSQL2 = null;
    private String sSQL3 = null;
    
    public DefaultTableModel mostrarTrabajadores(String buscar){
        DefaultTableModel miModelo = null;
        
        String titulos [] = {"Id", "Nombres", "Apellidos", "Id", "Dirección", "Id", "Puesto", "Cédula de Identidad", "Teléfono", "Estado"};
        String dts [] = new String[10];
        
        miModelo = new DefaultTableModel(null, titulos);
        sSQL = "select t.idTrabajador, p.nombres, p.apellidos, d.idDireccion, d.nombreDireccion, pt.idPuestoTrabajo ,pt.nombrePuesto, p.cedulaIdent, p.telefono, \n"
                + "e.estado from tbltrabajador as t inner join tblpersona as p on t.personaId = p.idPersona inner join tblpuestotrabajo \n"
                + "as pt on t.puestotrabajoId  = pt.idPuestoTrabajo inner join tbldireccion as d on p.direccionId = d.idDireccion \n"
                + "inner join tblestadopersona as e on p.estadopersonaId = e.idEstadoPersona where \n"
                + "t.idTrabajador = '" + buscar + "' or p.nombres like '%" + buscar + "%'";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while(rs.next()){
                dts[0] = rs.getString("idTrabajador");
                dts[1] = rs.getString("nombres");
                dts[2] = rs.getString("apellidos");
                dts[3] = rs.getString("idDireccion");
                dts[4] = rs.getString("nombreDireccion");
                dts[5] = rs.getString("idPuestoTrabajo");
                dts[6] = rs.getString("nombrePuesto");
                dts[7] = rs.getString("cedulaIdent");
                dts[8] = rs.getString("telefono");
                dts[9] = rs.getString("estado");
                miModelo.addRow(dts);
            }
            return miModelo;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
    public String insertarTrabajador(DTrabajadores dTrab, DPersona dPer){
        String msg = null;
        sSQL = "insert into tblpersona(nombres, apellidos, cedulaIdent, telefono, direccionId, estadopersonaId) value(?,?,?,?, (select idDireccion from tbldireccion order by idDireccion desc limit 1), ?)";
        sSQL1 = "insert into tbltrabajador(puestotrabajoId, personaId) value((select idPuestoTrabajo from tblpuestotrabajo order by idPuestoTrabajo desc limit 1) , (select idPersona from tblpersona order by idPersona desc limit 1))";
        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);
            PreparedStatement pst1 = cn.prepareStatement(sSQL1);
            
            pst.setString(1, dPer.getNombre());
            pst.setString(2, dPer.getApellido());
            pst.setString(3, dPer.getCedulaIdent());
            pst.setString(4, dPer.getTelefono());
            pst.setInt(5, dPer.getEstadoPersonaId());
            
            int n = pst.executeUpdate();
            if(n != 0){
                int n2 = pst1.executeUpdate();
                msg = "si";
                return msg;
            }else{
                msg = "no";
            }
            return msg;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return msg;
        }
    }
}
