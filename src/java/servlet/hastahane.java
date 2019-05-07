/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBTool;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ugurcn
 */
@WebServlet(name = "hastahane", urlPatterns = {"/hastahane"})
public class hastahane extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String tur = request.getParameter("tur");
        try (PrintWriter out = response.getWriter()) {
            if (tur.equals("insert")) {
                String adi = request.getParameter("adi");
                String soyadi = request.getParameter("soyadi");
                String sifre = request.getParameter("sifre");
                String tc = request.getParameter("tc");
                String dogumtarihi = request.getParameter("dogumtarihi");
                insert(adi, soyadi, sifre, tc, dogumtarihi);
            } else if (tur.equals("login")) {
                String tc = request.getParameter("tc");
                String sifre = request.getParameter("sifre");
                out.println(giris(tc, sifre));

            } else if (tur.equals("login2")) {
                String tc = request.getParameter("tc");
                String sifre = request.getParameter("sifre");
                out.println(doktorgiris(tc, sifre));

            } else if (tur.equals("teshis")) {
                String id = request.getParameter("id");
                out.println(teshisler(id));

            } else if (tur.equals("ilac")) {
                String id = request.getParameter("id");
                out.println(ilaclar(id));

            } else if (tur.equals("ilaclar")) {
                String id = request.getParameter("id");
                out.println(tümilaclar(id));

            } else if (tur.equals("muayene")) {
                String id = request.getParameter("id");
                out.println(muayene(id));

            } else if (tur.equals("boykilo")) {
                String id = request.getParameter("id");
                out.println(boykilo(id));

            } else if (tur.equals("insert2")) {
                String boy = request.getParameter("boy");
                String kilo = request.getParameter("kilo");
                String id = request.getParameter("id");
                insert3(boy, kilo, id);
            } else if (tur.equals("doktor")) {
                out.println(doktorlistesi());

            } else if (tur.equals("sendmessage")) {
                String mesaj = request.getParameter("mesaj");
                String gonderen = request.getParameter("gonderen");
                String alici = request.getParameter("alici");

                insert2(gonderen, alici, mesaj);

            } else if (tur.equals("gelenmesaj")) {
                String benden = request.getParameter("benden");
                String alan = request.getParameter("alan");
                out.println(GelenMesajlar(benden, alan));
            } else if (tur.equals("ondangelenmesaj")) {
                String benden = request.getParameter("benden");
                String alan = request.getParameter("alan");
                out.println(OndanGelenMesajlar(benden, alan));
            } else if (tur.equals("randevu")) {
                out.println(randevubolumler());

            } else if (tur.equals("doktorlar")) {
                String bolum = request.getParameter("bolum");
                out.println(randevudoktorlar(bolum));

            }
            else if (tur.equals("randevual")) {
                String ben = request.getParameter("ben");
                String bolum = request.getParameter("bolum");
                String doktor = request.getParameter("doktor");
                String tarih = request.getParameter("tarih");
                String saat = request.getParameter("saat");
                randevual(ben, bolum,doktor,tarih,saat);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    void insert(String adi, String soyadi, String sifre, String tc, String dogumtarihi) {
        String sql = "insert into kullanici"
                + "(tc,kullanici_sifre,kullanici_adi,kullanici_soyadi,kullanici_dogumtarih) values("
                + "'" + tc + "',"
                + "'" + sifre + "',"
                + "'" + adi + "',"
                + "'" + soyadi + "',"
                + "'" + dogumtarihi + "')";
        DBTool db = new DBTool();
        db.execute(sql);
    }

    String giris(String tc, String sifre) {
        try {
            String sql = "select tc from kullanici where kullanici.tc = '" + tc + "'  and kullanici_sifre = '" + sifre + "'";
            DBTool db = new DBTool();
            ResultSet r = db.getResultSet(sql);
            while (r.next()) {
                return r.getString("tc");
            }
        } catch (Exception e) {
        }
        return "";
    }

    private String teshisler(String id) {
        String sql = "select teshisler.teshis_adi,teshisler.teshis_tarih,doktor.doktor_adi,doktor.doktor_soyadi,ilac_adi,ilac_kullanmasuresi,ilaclar.id,bolum_adi from teshisler,doktor,ilaclar,bolumler where kullanici_id='" + id + "' and teshisler.doktor_id=doktor.id and bolumler.bolum_doktor_id=doktor.id and teshisler.ilac_id=ilaclar.id order by teshis_tarih DESC ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"teshisveri\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"teshis\":\"" + r.getString("teshis_adi") + "\"";
                j += ",\"doktor\":\"" + r.getString("doktor_adi") + "\"";
                j += ",\"doktor_bolumu\":\"" + r.getString("bolum_adi") + "\"";
                j += ",\"doktorsoyadi\":\"" + r.getString("doktor_soyadi") + "\"";
                j += ",\"ilacadi\":\"" + r.getString("ilac_adi") + "\"";
                j += ",\"id\":\"" + r.getString("id") + "\"";
                j += ",\"ilackullanmasuresi\":\"" + r.getString("ilac_kullanmasuresi") + "\"";
                j += ",\"tarih\":\"" + r.getString("teshis_tarih") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        j += "]";
        j += "}";
        return j;

    }

    private String ilaclar(String id) {
        String sql = "select ilaclar.id,ilac_adi,ilac_toklukdurumu,ilac_vakti,yanetkileri,ilaclar.resim from ilaclar where ilaclar.id='" + id + "'";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"ilacveri\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";
                j += "\"ilacadi\":\"" + r.getString("ilac_adi") + "\"";
                j += ",\"ilactoklukdurumu\":\"" + r.getString("ilac_toklukdurumu") + "\"";
                j += ",\"resim\":\"" + r.getString("resim") + "\"";
                j += ",\"yanetkileri\":\"" + r.getString("yanetkileri") + "\"";
                j += ",\"ilacvakti\":\"" + r.getString("ilac_vakti") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        j += "]";
        j += "}";
        return j;
    }

    private String tümilaclar(String id) {
        String sql = "select ilac_adi,ilac_toklukdurumu,ilac_vakti,yanetkileri,ilaclar.resim,doktor_adi,doktor_soyadi from ilaclar,doktor,teshisler where teshisler.doktor_id=doktor.id and teshisler.ilac_id=ilaclar.id and teshisler.kullanici_id='"+id+"' order by teshis_tarih desc";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"ilaclarveri\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";
                j += "\"ilacadi\":\"" + r.getString("ilac_adi") + "\"";
                j += ",\"doktor_adi\":\"" + r.getString("doktor_adi") + "\"";
                j += ",\"doktor_soyadi\":\"" + r.getString("doktor_soyadi") + "\"";
                j += ",\"ilactoklukdurumu\":\"" + r.getString("ilac_toklukdurumu") + "\"";
                j += ",\"resim\":\"" + r.getString("resim") + "\"";
                j += ",\"yanetkileri\":\"" + r.getString("yanetkileri") + "\"";
                j += ",\"ilacvakti\":\"" + r.getString("ilac_vakti") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        j += "]";
        j += "}";
        return j;
    }

    private String muayene(String id) {
        String sql = "select teshis_tarih,doktor_adi,doktor_soyadi,bolum_adi from doktor,teshisler,bolumler where bolum_doktor_id=doktor.id and teshisler.doktor_id=doktor.id and teshisler.kullanici_id='"+id+"' order by teshis_tarih desc ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"muayeneveri\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"doktor\":\"" + r.getString("doktor_adi") + "\"";
                j += ",\"doktorsoyadi\":\"" + r.getString("doktor_soyadi") + "\"";
                j += ",\"doktor_bolumu\":\"" + r.getString("bolum_adi") + "\"";
                j += ",\"tarih\":\"" + r.getString("teshis_tarih") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        j += "]";
        j += "}";
        return j;
    }

    private String boykilo(String id) {
        String sql = "select * from boykilo  where boy_kilo_kullanici_id='" + id + "'  order by boy_kilo_tarih DESC ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"boykiloveri\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"boy\":\"" + r.getString("boy") + "\"";
                j += ",\"kilo\":\"" + r.getString("kilo") + "\"";
                j += ",\"tarih\":\"" + r.getString("boy_kilo_tarih") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        j += "]";
        j += "}";
        return j;

    }

    void insert3(String boy, String kilo, String id) {
        String sql = "insert into boykilo"
                + "(boy,kilo,boy_kilo_kullanici_id) values("
                + "'" + boy + "',"
                + "'" + kilo + "',"
                + "'" + id + "')";
        DBTool db = new DBTool();
        db.execute(sql);
    }

    private String doktorlistesi() {
        String sql = "select doktor.id, doktor_adi,doktor_soyadi,resim,bolum_adi from doktor,bolumler where bolumler.bolum_doktor_id=doktor.id order by doktor.id ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"doktor_list\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"doktoradi\":\"" + r.getString("doktor_adi") + "\"";
                j += ",\"doktorsoyadi\":\"" + r.getString("doktor_soyadi") + "\"";
                j += ",\"bolumu\":\"" + r.getString("bolum_adi") + "\"";
                j += ",\"id\":\"" + r.getString("id") + "\"";
                j += ",\"resim\":\"" + r.getString("resim") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        j += "]";
        j += "}";
        return j;
    }

    void insert2(String gonderen, String alici, String mesaj) {
        String sql = "insert into mesaj"
                + "(gonderen_id,alici_id,mesaj) values("
                + "'" + gonderen + "',"
                + "'" + alici + "',"
                + "'" + mesaj + "')";
        DBTool db = new DBTool();
        db.execute(sql);
    }

    private String GelenMesajlar(String benden, String alan) {
        String sql = "select mesaj from mesaj where  gonderen_id='" + benden + "' and alici_id='" + alan + "' order by tarih DESC";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"mesajveri\": [";
        db.execute(sql);

        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";
                j += "\"mesaj\":\"" + r.getString("mesaj") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
        }
        j += "]";
        j += "}";
        return j;
    }

    private String OndanGelenMesajlar(String benden, String alan) {
        String sql = "select mesaj,doktor_adi,doktor_soyadi,resim from mesaj,doktor where  gonderen_id='" + alan + "' and alici_id='" + benden + "' and doktor.id=mesaj.gonderen_id order by tarih";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"ondanmesajveri\": [";
        db.execute(sql);

        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";
                j += "\"mesaj\":\"" + r.getString("mesaj") + "\"";
                j += ",\"doktoradi\":\"" + r.getString("doktor_adi") + "\"";
                j += ",\"resim\":\"" + r.getString("resim") + "\"";
                j += ",\"doktorsoyadi\":\"" + r.getString("doktor_soyadi") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
        }
        j += "]";
        j += "}";
        return j;
    }

    private String randevubolumler() {
        String sql = "select * from bolumler";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"randevuveri\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"bolumler\":\"" + r.getString("bolum_adi") + "\"";
                j += ",\"bolumlerid\":\"" + r.getString("id") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        j += "]";
        j += "}";
        return j;
    }

    String doktorgiris(String tc, String sifre) {

        try {
            String sql = "select id from doktor where doktor.id = '" + tc + "'  and doktor_sifre = '" + sifre + "'";
            DBTool db = new DBTool();
            ResultSet r = db.getResultSet(sql);
            while (r.next()) {
                return r.getString("id");
            }
        } catch (Exception e) {
        }
        return "";
    }

    private String randevudoktorlar(String bolum) {
        String sql = "select doktor.id,doktor_adi,doktor_soyadi from doktor,bolumler where bolum_doktor_id=doktor.id and  bolumler.id='" + bolum + "' ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"doktorlarveri\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";
                j += "\"doktor_adi\":\"" + r.getString("doktor_adi") + "\"";
                j += ",\"doktorid\":\"" + r.getString("id") + "\"";
                j += ",\"doktor_soyadi\":\"" + r.getString("doktor_soyadi") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        j += "]";
        j += "}";
        return j;
    }

     void randevual(String ben, String bolum, String doktor, String tarih, String saat) {
         String sql = "insert into randevu  "
                + "(kullanici_id,doktor_id,bolum_id,randevu_tarih,randevu_saat) values("
                + "'" + ben + "',"
                + "'" + doktor + "',"
                + "'" + bolum + "',"
                + "'" + tarih + "',"
                + "'" + saat + "')";
        DBTool db = new DBTool();
        db.execute(sql);
    }

}
