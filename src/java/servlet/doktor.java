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
 * @author ugurcan
 */
@WebServlet(name = "doktor", urlPatterns = {"/doktor"})
public class doktor extends HttpServlet {

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
            if (tur.equals("doktormuayene")) {
                String id = request.getParameter("id");
                out.println(muayene(id));

            } else if (tur.equals("doktorrandevu")) {
                String id = request.getParameter("id");
                out.println(doktorrandevuler(id));

            } else if (tur.equals("doktorilaclar")) {
                out.println(tümilaclar());

            } else if (tur.equals("ilacinsert")) {
                String ilacadi = request.getParameter("ilacadi");
                String acliktokluk = request.getParameter("acliktokluk");
                String vakit = request.getParameter("vakit");
                String kullanmasuresi = request.getParameter("kullanmasuresi");
                String yannetkileri = request.getParameter("yannetkileri");
                ilacekle(ilacadi, acliktokluk, vakit, yannetkileri, kullanmasuresi);
            } else if (tur.equals("muayeneedilmemishastalar")) {
                String id = request.getParameter("id");
                out.println(muayeneedilmemishastalar(id));

            } else if (tur.equals("randevuonay")) {
                String doktor = request.getParameter("doktor");
                String kullanici = request.getParameter("kullanici");
                String durum = request.getParameter("state");
                out.println(randevuonay(doktor, kullanici, durum));
            } else if (tur.equals("ilac")) {
                out.println(ilac());

            } else if (tur.equals("teshisinsert")) {
                String ilac_id = request.getParameter("ilac");
                String kullanici_id = request.getParameter("kullanici");
                String doktor_id = request.getParameter("doktor");
                String teshis = request.getParameter("teshis");
                teshisekle(ilac_id, kullanici_id, doktor_id, teshis);
            } else if (tur.equals("teshisonay")) {
                String doktor = request.getParameter("doktor");
                String kullanici = request.getParameter("kullanici");
                String durum = request.getParameter("state");
                out.println(teshisonay(doktor, kullanici, durum));
            } else if (tur.equals("hastalarim")) {
                String id = request.getParameter("id");
                out.println(hastalarim(id));

            } else if (tur.equals("hastamailler")) {
                out.println(hastalistesi());

            } else if (tur.equals("gelenmesaj")) {
                String benden = request.getParameter("benden");
                String alan = request.getParameter("alan");
                out.println(GelenMesajlar(benden, alan));
            } else if (tur.equals("ondangelenmesaj")) {
                String benden = request.getParameter("benden");
                String alan = request.getParameter("alan");
                out.println(OndanGelenMesajlar(benden, alan));
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

    private String muayene(String id) {
        String sql = "select muayene_tarih,kullanici_adi,kullanici_soyadi,kullanici_dogumtarih from kullanici,muayene where muayene.muayene_kullanici_id=kullanici.tc and muayene.muayene_doktor_id='" + id + "' order by muayene_tarih DESC ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"doktormuayeneveri\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"hastaadi\":\"" + r.getString("kullanici_adi") + "\"";
                j += ",\"hastasoyadi\":\"" + r.getString("kullanici_soyadi") + "\"";
                j += ",\"dogumtarihi\":\"" + r.getString("kullanici_dogumtarih") + "\"";
                j += ",\"tarih\":\"" + r.getString("muayene_tarih") + "\"";
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

    private String doktorrandevuler(String id) {
        String sql = "select kullanici_id,saatler.saat,tarihler.tarih,kullanici_adi,kullanici_soyadi,kullanici.tc from kullanici,saatler,tarihler,randevu where randevu.kullanici_id=kullanici.tc  and  randevu_tarih=tarihler.id and randevu_saat=saatler.id and randevu.doktor_id='" + id + "' and randevu_onay='0' order by tarih,saat   ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"doktorrandevuler\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"hastaadi\":\"" + r.getString("kullanici_adi") + "\"";
                j += ",\"hastasoyadi\":\"" + r.getString("kullanici_soyadi") + "\"";
                j += ",\"tarih\":\"" + r.getString("tarih") + "\"";
                j += ",\"id\":\"" + r.getString("kullanici_id") + "\"";
                j += ",\"saat\":\"" + r.getString("saat") + "\"";
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

    private String tümilaclar() {
        String sql = "select eklenme_tarihi,ilac_adi,ilac_toklukdurumu,ilac_vakti,yanetkileri,ilaclar.resim from ilaclar order by eklenme_tarihi desc";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"doktorilaclar\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";
                j += "\"ilacadi\":\"" + r.getString("ilac_adi") + "\"";
                j += ",\"ilactoklukdurumu\":\"" + r.getString("ilac_toklukdurumu") + "\"";
                j += ",\"resim\":\"" + r.getString("resim") + "\"";
                j += ",\"eklenme_tarihi\":\"" + r.getString("eklenme_tarihi") + "\"";
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

    void ilacekle(String ilacadi, String acliktokluk, String vakit, String yannetkileri, String kullanmasuresi) {
        String sql = "insert into ilaclar"
                + "(ilac_adi,ilac_toklukdurumu,ilac_vakti,ilac_kullanmasuresi,yanetkileri) values("
                + "'" + ilacadi + "',"
                + "'" + acliktokluk + "',"
                + "'" + vakit + "',"
                + "'" + kullanmasuresi + "',"
                + "'" + yannetkileri + "')";
        DBTool db = new DBTool();
        db.execute(sql);
    }

    private String muayeneedilmemishastalar(String id) {
        String sql = "select kullanici_id,saatler.saat,tarihler.tarih,kullanici_adi,kullanici_soyadi,kullanici.tc from kullanici,saatler,tarihler,randevu where randevu.kullanici_id=kullanici.tc  and  randevu_tarih=tarihler.id and randevu_saat=saatler.id and randevu.doktor_id='" + id + "' and randevu_onay='1' order by tarih,saat   ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"muayeneedilmemishastalar\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"hastaadi\":\"" + r.getString("kullanici_adi") + "\"";
                j += ",\"hastasoyadi\":\"" + r.getString("kullanici_soyadi") + "\"";
                j += ",\"id\":\"" + r.getString("kullanici_id") + "\"";
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

    private String randevuonay(String doktor, String kullanici, String durum) {
        String sql = "update randevu set randevu_onay = '" + durum + "'"
                + " where kullanici_id='" + kullanici + "' and doktor_id='" + doktor + "'";
        DBTool db = new DBTool();
        if (db.execute(sql)) {
            return "success";
        } else {
            return "not success";
        }

    }

    private String ilac() {
        String sql = "select * from ilaclar";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"ilac\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"ilac_adi\":\"" + r.getString("ilac_adi") + "\"";
                j += ",\"id\":\"" + r.getString("id") + "\"";
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

    void teshisekle(String ilac_id, String kullanici_id, String doktor_id, String teshis) {
        String sql = "insert into teshisler"
                + "(ilac_id,kullanici_id,doktor_id,teshis_adi) values("
                + "'" + ilac_id + "',"
                + "'" + kullanici_id + "',"
                + "'" + doktor_id + "',"
                + "'" + teshis + "')";
        DBTool db = new DBTool();
        db.execute(sql);
    }

    private String teshisonay(String doktor, String kullanici, String durum) {
        String sql = "update randevu set randevu_onay = '" + durum + "'"
                + " where kullanici_id='" + kullanici + "' and doktor_id='" + doktor + "'";
        DBTool db = new DBTool();
        if (db.execute(sql)) {
            return "success";
        } else {
            return "not success";
        }
    }

    private String hastalarim(String id) {
        String sql = "select kullanici_id,kullanici_dogumtarih,teshisler.teshis_adi,teshisler.teshis_tarih,kullanici_adi,kullanici_soyadi,ilac_adi,ilac_kullanmasuresi,ilaclar.id from teshisler,kullanici,ilaclar where doktor_id='" + id + "' and teshisler.kullanici_id=kullanici.tc and  teshisler.ilac_id=ilaclar.id order by teshis_tarih DESC ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"hastalarim\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"teshis\":\"" + r.getString("teshis_adi") + "\"";
                j += ",\"hastaadi\":\"" + r.getString("kullanici_adi") + "\"";
                j += ",\"hastasoyadi\":\"" + r.getString("kullanici_soyadi") + "\"";
                j += ",\"dogumtarihi\":\"" + r.getString("kullanici_dogumtarih") + "\"";
                j += ",\"ilacadi\":\"" + r.getString("ilac_adi") + "\"";
                j += ",\"id\":\"" + r.getString("kullanici_id") + "\"";
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

    private String hastalistesi() {
        String sql = "select kullanici_dogumtarih,kullanici.tc, kullanici_adi,kullanici_soyadi,resim from kullanici  order by kullanici.tc ";
        DBTool db = new DBTool();
        ResultSet r = db.getResultSet(sql);
        boolean virgul = false;
        String j = "{\"hastamailler\": [";
        try {

            while (r.next()) {           // sağ taraf postgresql den cekilecek
                j += (virgul) ? "," : "";
                j += "{";

                j += "\"hastaadi\":\"" + r.getString("kullanici_adi") + "\"";
                j += ",\"hastasoyadi\":\"" + r.getString("kullanici_soyadi") + "\"";
                j += ",\"id\":\"" + r.getString("tc") + "\"";
                j += ",\"resim\":\"" + r.getString("resim") + "\"";
                j += ",\"kullanici_dogumtarih\":\"" + r.getString("kullanici_dogumtarih") + "\"";
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
        String sql = "select tarih,mesaj,kullanici_adi,kullanici_soyadi,resim from mesaj,kullanici where  gonderen_id='" + alan + "' and alici_id='" + benden + "' and kullanici.tc=mesaj.gonderen_id order by tarih";
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
                j += ",\"doktoradi\":\"" + r.getString("kullanici_adi") + "\"";
                j += ",\"resim\":\"" + r.getString("resim") + "\"";
                j += ",\"tarih\":\"" + r.getString("tarih") + "\"";
                j += ",\"doktorsoyadi\":\"" + r.getString("kullanici_soyadi") + "\"";
                j += "}";
                virgul = true;
            }
        } catch (SQLException ex) {
        }
        j += "]";
        j += "}";
        return j;
    }

}
