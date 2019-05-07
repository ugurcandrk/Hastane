package db;

import java.sql.*;
import java.util.Properties;

public class DBTool {

    private java.sql.Connection conn;

    public DBTool() {
        connect();
    }

    //connection.properties dosyasındaki verileri kullanır
    //veri tabanı bağlatısını oluşturur
    public final void connect() {
        try {
            Properties p = PropertyLoader.loadProperties("connection");
            String ip = p.getProperty("db_ip");
            String usr = p.getProperty("db_user");
            String pass = p.getProperty("db_pass");
            String db = p.getProperty("db_name");

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + ip + "/" + db;
            conn = DriverManager.getConnection(url, usr, pass);

        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        try {
            
            String sql = "select * from kullanici";
            DBTool db=new DBTool();
            ResultSet r = db.getResultSet(sql);
            while(r.next())
            {
                System.out.println(r.getString("id")+ " " +r.getString("kul_adi")+ " " + r.getString("kul_soyadi"));
            }
            
           
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //insert, delete işlemleri için kullanılır
    //örnek sorgu 
    //insert into abone(aboneadi)values('yılmaz')
    public boolean execute(String query) {
        try {
            //bağlantı kapalıysa bağlan
            if (conn.isClosed()) {
                connect();
            }
            //sorguyu yürüt, işlemi gerçekleştir
            conn.createStatement().execute(query);
            //bağlantıyı kapat
            conn.close();
            //hata oluşmazsa true gönder işlem başarılı bir şekilde gerçekleşti
            return true;

        } catch (Exception e) {
            //hata olursa false gönder
            return false;
        }
    }

    //update(güncelleme) işlemleri için kullanılır
    //örnek sorgu 
    //update abone set abone_adi_soyadi = ' yılmaz' where abone_id = 222
    public String executeUpdate(String query) {
        try {
            int i = conn.createStatement().executeUpdate(query);

            conn.close();
            return i + "";
        } catch (Exception e) {
            return "";
        }
    }

    //veri tabanından verileri getiren fonksiyon
    //select işlemleri için kullanılır
    //örnek sorgu 
    //select * from abone
    public ResultSet getResultSet(String query) {
        try {
            if (conn.isClosed()) {
                connect();
            }

            //select sorgunu işle ve resulset e aktar
            //resultset veri tabanından gelen verilerin içinde tutulduğu java tablo yapısı
            ResultSet r = conn.createStatement().executeQuery(query);

            conn.close();
            return r;
        } catch (Exception e) {
            return null;
        } finally {
        }
    }
}
