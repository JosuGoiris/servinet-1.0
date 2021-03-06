/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Datos.DTipoUsuario;
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
public class LTipoUsuario {
    Connection cn = ConexionSingleton.getConnection();
    private String sSQL = null;
    private String sSQL1 = null;
    
    public DefaultTableModel mostrarTipos(String buscar){
        DefaultTableModel miModelo = null;
        
        String titulos [] = {"Id","Nombre del Tipo", "Estado"};
        String dts [] = new String[3];
        
        miModelo = new DefaultTableModel(null, titulos);
        sSQL = "select tu.idTipoUsuario, tu.nombre, etu.estado from tbltipousuario \n"
                + "as tu inner join tblestadotipousuario as etu on tu.estadotipoId = etu.idEstadoTipoU \n"
                + "where tu.idTipoUsuario = '" + buscar + "' or tu.nombre like '%" + buscar + "'";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while(rs.next()){
                dts[0] = rs.getString("idTipoUsuario");
                dts[1] = rs.getString("nombre");
                dts[2] = rs.getString("estado");
                miModelo.addRow(dts);
            }
            return miModelo;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
    public String insertarTipos(DTipoUsuario dTipo){
        String msg = null;
        sSQL = "insert into tbltipousuario(nombre, estadotipoId) value(?, ?)";
        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);
            
            pst.setString(1, dTipo.getNombre());
            pst.setInt(2, dTipo.getEstadotipoId());
            
            int n = pst.executeUpdate();
            if(n != 0){
                msg = "si";
                return msg;
            }else{
                msg = "no";
                return msg;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return msg;
        }
    }
}
