package logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import persistencia.ConexionBD;

/**
 *
 * @author Miguel
 */
public class Producto {
    
    private String id;
    private String nombre;
    private double temperatura;
    private double valorBase;
    private double costoAlmacenamiento;

    public Producto(String id, String nombre, double temperatura, double valorBase) {
        this.id = id;
        this.nombre = nombre;
        this.temperatura = temperatura;
        this.valorBase = valorBase;
    }

    public Producto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }
    
    public double calcularCostoDeAlmacenamiento(double temp) {        
        if(temp>=0 && temp<=20){
            return costoAlmacenamiento = (valorBase * 0.2) + valorBase;
        }else{
            return costoAlmacenamiento = (valorBase * 0.1) + valorBase;
        }
    }
    
    public List<Producto> listarProductos(){
        
        ConexionBD conexion = new ConexionBD();
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try{
            
            ResultSet rs = conexion.consultarBD(sql);
            Producto p;
            
            while (rs.next()) {
                p = new Producto();
                p.setId(rs.getString("id"));
                p.setNombre(rs.getString("nombre"));
                p.setTemperatura(rs.getDouble("temperatura"));
                p.setValorBase(rs.getDouble("valorBase"));
                p.calcularCostoDeAlmacenamiento(p.getTemperatura());
                
                listaProductos.add(p);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al cargar datos" + e.getMessage());
        }finally{
            conexion.cerrarConexion();
        }
        
        return listaProductos;
    }
    
    public boolean guardarProducto() {
        ConexionBD conexion = new ConexionBD();
        String sql = "INSERT INTO productos(id,nombre,temperatura,valorBase) VALUES ('" + this.id + "','" + this.nombre + "'," + this.temperatura + "," + this.valorBase + ");";
        if (conexion.setAutoCommitBD(false)) {
            if (conexion.insertarBD(sql)) {
                conexion.commitBD();
                conexion.cerrarConexion();
                return true;
            } else {
                conexion.rollbackBD();
                conexion.cerrarConexion();
                return false;
            }
        } else {
            conexion.cerrarConexion();
            return false;
        }
    }
    
    public Producto seleccionarProductoId() {
        ConexionBD conexion = new ConexionBD();
        String sql = "SELECT * FROM productos WHERE id='" + id + "'";
        ResultSet rs = conexion.consultarBD(sql);
        Producto p = null;
        try {
            if (rs.next()) {
                p = new Producto();
                p.id = rs.getString("id");
                p.nombre = rs.getString("nombre");
                p.temperatura = rs.getDouble("temperatura");
                p.valorBase = rs.getDouble("valorBase");
                conexion.cerrarConexion();
            } else {
                conexion.cerrarConexion();
                return null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return p;
    }
    
    public List<Producto> listarProductosNoId(int opcion){        
        ConexionBD conexion = new ConexionBD();
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "";
        switch (opcion) {
            case 1:{
                sql = "SELECT * FROM productos WHERE nombre='" + nombre + "'";
                break;
            }case 2:{
                sql = "SELECT * FROM productos WHERE temperatura=" + temperatura;
                break;
            }case 3:{
                sql = "SELECT * FROM productos WHERE valorBase=" + valorBase;
                break;
            }case 4:{
                sql = "SELECT * FROM productos WHERE nombre='" + nombre + "'" + " AND temperatura=" + temperatura;
                break;
            }case 5:{
                sql = "SELECT * FROM productos WHERE nombre='" + nombre + "'" + " AND valorBase=" + valorBase;
                break;
            }case 6:{
                sql = "SELECT * FROM productos WHERE temperatura=" + temperatura + " AND valorBase=" + valorBase;
                break;
            }default:{
                sql = "SELECT * FROM productos WHERE nombre='" + nombre + "'" + " AND temperatura=" + temperatura + " AND valorBase=" + valorBase;
                break;
            }
        }
        try{
            ResultSet rs = conexion.consultarBD(sql);
            Producto p;
            while (rs.next()) {
                p = new Producto();
                p.setId(rs.getString("id"));
                p.setNombre(rs.getString("nombre"));
                p.setTemperatura(rs.getDouble("temperatura"));
                p.setValorBase(rs.getDouble("valorBase"));
                p.calcularCostoDeAlmacenamiento(p.getTemperatura());
                listaProductos.add(p);
            }           
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al cargar datos" + e.getMessage());
        }finally{
            conexion.cerrarConexion();
        }
        return listaProductos;
    }
    
    public boolean actualizarProducto() {
        ConexionBD conexion = new ConexionBD();
        String Sentencia = "UPDATE productos SET nombre='" + this.nombre + "',temperatura=" + this.temperatura + ",valorBase=" + this.valorBase + " WHERE id='" + id + "';";
        if (conexion.setAutoCommitBD(false)) {
            if (conexion.actualizarBD(Sentencia)) {
                conexion.commitBD();
                conexion.cerrarConexion();
                return true;
            } else {
                conexion.rollbackBD();
                conexion.cerrarConexion();
                return false;
            }
        } else {
            conexion.cerrarConexion();
            return false;
        }
    }
    
    public boolean borrarProducto() {
        String Sentencia = "DELETE FROM productos WHERE id='" + id + "'";
        ConexionBD conexion = new ConexionBD();
        if (conexion.setAutoCommitBD(false)) {
            if (conexion.borrarBD(Sentencia)) {
                conexion.commitBD();
                conexion.cerrarConexion();
                return true;
            } else {
                conexion.rollbackBD();
                conexion.cerrarConexion();
                return false;
            }
        } else {
            conexion.cerrarConexion();
            return false;
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "id=" + id + ", nombre=" + nombre + ", temperatura=" + temperatura + ", valorBase=" + valorBase + ", CostoAlmacenamiento=" + costoAlmacenamiento + '}';
    }
       
}
