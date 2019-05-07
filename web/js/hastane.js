function ekle()
{
    var adi = $("#txtadi").val();
    var soyadi = $("#txtsoyadi").val();
    var sifre = $("#txtsifre").val();
    var tc = $("#txttcno").val();
    var dogumtarihi = $("#txtdogumtarihi").val();
    var veri = "tur=insert&adi=" + adi + "&soyadi=" + soyadi + "&sifre=" + sifre + "&dogumtarihi=" + dogumtarihi + "&tc=" + tc;
    console.log(veri);
    if (tc != "" && adi != "" && soyadi != "" && sifre != "" && tc.length == 11) {
        $.ajax({
            type: "POST",
            url: "hastahane",
            data: veri,
            success: function ()
            {
                alert("Kayıt Başarılı...");
                $.mobile.changePage("#pggiris");
            }
        });
    } else {
        alert("Hata.. Kayıt Yapılamadı !! ");
    }
}
function kaydet()
{
    var boy = $("#txtboy").val();
    var kilo = $("#txtkilo").val();
    var veri = "tur=insert2&boy=" + boy + "&kilo=" + kilo + "&id=" + getCookie('username');
    console.log(veri);
    if (boy != "" && kilo != "") {
        $.ajax({
            type: "POST",
            url: "hastahane",
            data: veri,
            success: function ()
            {
                alert("Kayıt Başarılı...");

                $.mobile.changePage("#pgboykilo");
            }
        });
    } else {
        alert("Hata.. Kayıt Yapılamadı !! ");
    }
}

function giris() {
    var tc = $("#txttc").val();
    var sifre = $("#txtkullanicisifre").val();
    var veri = "tur=login&tc=" + tc + "&sifre=" + sifre;
    console.log(veri);
    if (tc != "" && sifre != "" && tc.length == 11) {
        $.ajax({
            type: "POST",
            url: "hastahane",
            data: veri,
            success: function (msg)
            {
                console.log(msg);
                if (msg > 0) {
                    setCookie('username', msg);

                    $.mobile.changePage("#pganasayfa");
                }
                else
                    window.alert("Kullanıcı Adı veya Şifre Hatalı... ");
            }
        });
    }
    else {
        window.alert("Kullanıcı TC veya Şifre Hatalı... ");
    }
}

function doktorgirisi() {
    var tc = $("#txtdoktortc").val();
    var sifre = $("#txtdoktorsifre").val();
    var veri = "tur=login2&tc=" + tc + "&sifre=" + sifre;
    console.log(veri);
    if (tc != "" && sifre != "" && tc.length == 11) {
        $.ajax({
            type: "POST",
            url: "hastahane",
            data: veri,
            success: function (msg)
            {
                console.log(msg);
                if (msg > 0) {
                    setCookie('username', msg);

                    $.mobile.changePage("#pgdoktoranasayfa");
                }
                else
                    window.alert("Kullanıcı Adı veya Şifre Hatalı... ");
            }
        });
    }
    else {
        window.alert("Kullanıcı TC veya Şifre Hatalı... ");
    }
}

function login()
{

    if (getCookie('username') != '') {


        $.mobile.changePage("#pganasayfa");
    }
    else {
        $.mobile.changePage("#pganasayfa");
    }

}
$('#pgteshisler').live('pageshow', function () {
    $("#lstteshisler").empty();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=teshis&&id=" + getCookie('username'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).teshisveri, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li>'
                        +
                        '</li><li><table id="t01" >\n\
                           <tr>\n\
                            <td><font face="arial" size="4" color="red">Tarih</font></td>\n\
                            <td><font face="arial" size="4" color="red">Doktor Adı-Soyadı</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Doktor Bölümü</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Kullanılması Gereken İlaç</font></td>\n\
                            <td><font  face="arial" size="4" color="red">İlacın Kullanılması Gereken Süre</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Teşhis</font></td>\n\
                            <tr><td><b><font face="arial" size="3" color="black">' + veri.tarih + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.doktor + '            ' + veri.doktorsoyadi + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.doktor_bolumu + '</font></b></td>\n\
                            <td><b><a data-role="button" onclick="ilacbilgisi(' + veri.id + ')"><i><font size="3" face="arial" color="blue" >' + veri.ilacadi + '(<font color="black">"Tıklayın"</font>)</font></a></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.ilackullanmasuresi + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.teshis + '</font></b></td></tr>'
                '</table></li>';//isimin altında telefon numarasını göster.
                $("#lstteshisler").append(htm);
            });
            $("#lstteshisler").listview("refresh");

        }
    });
});

function ilacbilgisi(id)
{
    $("lstilaclistesi").empty();
    $.ajax({
        type: "POST",
        url: "hastahane", //servlet e gidecek
        data: "tur=ilac&&id=" + id,
        success: function (msg)
        {
            console.log(id)
            console.log(msg);
            $.each(JSON.parse(msg).ilacveri, function ()
            {
                var veri = this;
                var htm = '<li><label><font size="4" face="Arial" color="gray">İlacın Adı :</font> '
                        + veri.ilacadi +
                        '</label></li><li><label><font size="4" face="Arial" color="gray">Açlık-Tokluk :</font> '
                        + veri.ilactoklukdurumu +
                        '</label></li><li><label><font size="4" face="Arial" color="gray">İlacın Alınması Gereken Vakit :</font>  '
                        + veri.ilacvakti +
                        '</label></li><li><label><font size="4" face="Arial" color="gray">Yan Etkileri :</font>  '
                        + veri.yanetkileri +
                        '</label></li><li><label><IMG onmouseover="this.height=300;this.width=300;" onmouseout="this.height=100;this.width=100;" height=100 src="' + veri.resim + '" width=100 border=0> '
                '</label></li>';
                $("#lstilaclistesi").append(htm);
            });
            $("#lstilaclistesi").listview("refresh");
        }
    });

    $.mobile.changePage("#pgilacbilgisi");
}
$('#pgilaclar').live('pageshow', function () {
    $("#lstilaclar").empty();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=ilaclar&&id=" + getCookie('username'),
        success: function (msg)
        {


            console.log(msg);
            $.each(JSON.parse(msg).ilaclarveri, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li>'
                        +
                        '</li><li><table id="t01" >\n\
                           <tr>\n\
                            <td><font face="arial" size="4" color="red">İlacın Adı</font></td>\n\
                            <td><font face="arial" size="4" color="red">İlacı Yazan Doktor Adı - Soyadı</font></td>\n\
                            \n\
                            <td><font  face="arial" size="4" color="red">Açlık-Tokluk</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Alınması Gereken Vakit</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Yan Etkileri</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Resim</font></td>\n\
                            <tr><td><b><font face="arial" size="3" color="black">' + veri.ilacadi + ' </font></b></td>\n\
                            \n\
                            <td><b><font face="arial" size="3" color="black">' + veri.doktor_adi + ' ' + veri.doktor_soyadi + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.ilactoklukdurumu + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.ilacvakti + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.yanetkileri + '</font></b></td>\n\
                            <td><IMG onmouseover="this.height=300;this.width=300;" onmouseout="this.height=100;this.width=100;" height=100 src="' + veri.resim + '" width=100 border=0></td></tr>'
                '</table></li>';//isimin altında telefon numarasını göster.
                $("#lstilaclar").append(htm);
            });
            $("#lstilaclar").listview("refresh");
        }
    });
});
//function ilaclar()
//{
//
//    $("lstilaclar").empty();
//
//    $.ajax({
//        type: "POST",
//        url: "hastahane", //servlet e gidecek
//        data: "tur=ilaclar&&id=" + getCookie('username'),
//        success: function (msg)
//        {
//            $.each(JSON.parse(msg).ilaclarveri, function ()
//            {
//
//                var veri = this;
//                var htm = '<li><label><font size="4" face="Arial" color="gray">İlacın Adı:</font> '
//                        + veri.ilacadi +
//                        '</label></li><li><label><font size="4" face="Arial" color="gray">Açlık-Tokluk:</font> '
//                        + veri.ilactoklukdurumu +
//                        '</label></li><li><label><font size="4" face="Arial" color="gray">İlacın Alınması Gereken Vakit :</font>  '
//                        + veri.ilacvakti +
//                        '</label></li><li><label><font size="4" face="Arial" color="gray">Yan Etkileri:</font>  '
//                        + veri.yanetkileri +
//                        '</label></li><li><label><font size="4" face="Arial" color="gray">İlacı Yazan Doktor Adı:</font> '
//                        + veri.doktor_adi +
//                        ' <li><label><font size="4" face="Arial" color="gray">İlacı Yazan Doktor Soyadı:</font> '
//                        + veri.doktor_soyadi +
//                        '</label></li><li><label><IMG onmouseover="this.height=300;this.width=300;" onmouseout="this.height=100;this.width=100;" height=100 src="' + veri.resim + '" width=100 border=0> '
//                '</label></li><li></li>';
//                $("#lstilaclar").append(htm);
//            });
//            $("#lstilaclar").listview("refresh");
//        }
//    });
//
//    $.mobile.changePage("#pgilaclar");
//
//}
$('#pgmuayene').live('pageshow', function () {
    $("#lstmuayene").empty();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=muayene&&id=" + getCookie('username'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).muayeneveri, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li><label><font size="4" face="arial"  color="gray"><font color="black" face="arial">"' + veri.tarih + ' "Tarihli Muayene Kaydı </font>  </font>'
                        +
                        '</label><ul><li><label><font size="4" face="arial"  color="gray">Muayene Eden Doktor Adı : </font> '
                        + veri.doktor +
                        '</label></li><li><label><font size="4" face="arial"  color="gray">Muayene Eden Doktor Soyadı : </font> '
                        + veri.doktorsoyadi +
                        '</label></li><li><label><font size="4" face="arial"  color="gray"> Bölümü : </font> '
                        + veri.doktor_bolumu +
                        '</label></li></ul></li>';//isimin altında telefon numarasını göster.
                $("#lstmuayene").append(htm);
            });
            $("#lstmuayene").listview("refresh");
        }
    });
});
//function muayene()
//{
//    $("lstmuayene").empty();
//
//    $.ajax({
//        type: "POST",
//        url: "hastahane", //servlet e gidecek
//        data: "tur=muayene&&id=" + getCookie('username'),
//        success: function (msg)
//        {
//            $.each(JSON.parse(msg).muayeneveri, function ()
//            {
//                var veri = this;
//                var htm = '<li><label><font size="4" face="arial"  color="gray"><font color="black" face="arial">"' + veri.tarih + ' "Tarihli Muayene Kaydı </font>  </font>'
//                        +
//                        '</label><ul><li><label><font size="4" face="arial"  color="gray">Muayene Eden Doktor Adı : </font> '
//                        + veri.doktor +
//                        '</label></li><li><label><font size="4" face="arial"  color="gray">Muayene Eden Doktor Soyadı : </font> '
//                        + veri.doktorsoyadi +
//                        '</label></li><li><label><font size="4" face="arial"  color="gray"> Bölümü : </font> '
//                        + veri.doktor_bolumu +
//                        '</label></li></ul></li>';
//                $("#lstmuayene").append(htm);
//            });
//            $("#lstmuayene").listview("refresh");
//        }
//    });
//
//    $.mobile.changePage("#pgmuayene");
//
//}

$('#pgboykilo').live('pageshow', function () {
    $("#lstboykilo").empty();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=boykilo&&id=" + getCookie('username'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).boykiloveri, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li><label><font size="4" face="arial"   color="black"><font face="arial" color="black" >"' + veri.tarih + '"</font>  Tarihindeki Boy Bilgileri : </font>'
                        +
                        '</label></li><li><table id="t01" >\n\
                           <tr>\n\
                            <td><font face="arial" size="4" color="red">Boy</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Kilo</font></td></tr>\n\
                             <tr><td>' + veri.boy + '</td>\n\
                              <td>' + veri.kilo + '</td></tr> '
                        +
                        '</table></li>'; //isimin altında telefon numarasını göster.
                $("#lstboykilo").append(htm);
            });
            $("#lstboykilo").listview("refresh");
        }
    });
});
//function boykilo()
//{
//    $("lstboykilo").empty();
//
//    $.ajax({
//        type: "POST",
//        url: "hastahane", //servlet e gidecek
//        data: "tur=boykilo&&id=" + getCookie('username'),
//        success: function (msg)
//        {
//            $.each(JSON.parse(msg).boykiloveri, function ()
//            {
//                var veri = this;
//                var htm = '<li><label><font size="4" face="arial"   color="black"><font face="arial" color="black" >"' + veri.tarih + '"</font>  Tarihindeki Boy Bilgileri : </font>'
//                        +
//                        '</label></li><li><table id="t01" >\n\
//                           <tr>\n\
//                            <td><font face="arial" size="4" color="red">Boy</font></td>\n\
//                            <td><font  face="arial" size="4" color="red">Kilo</font></td></tr>\n\
//                             <tr><td>' + veri.boy + '</td>\n\
//                              <td>' + veri.kilo + '</td></tr> '
//                        +
//                        '</table></li>';
//                $("#lstboykilo").append(htm);
//            });
//            $("#lstboykilo").listview("refresh");
//        }
//    });
//
//    $.mobile.changePage("#pgboykilo");
//
//}
$('#pgdoktorlistesi').live('pageshow', function () {
    $("#lstdoktorlistesi").empty();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=doktor",
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).doktor_list, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li><a  onclick="FriendInfo(' + veri.id + ')"><img src="' + veri.resim + '" alt="" style="height: 100px;width: 100px" />' + veri.doktoradi + ' ' + veri.doktorsoyadi + //arkadaşını ismini soyismini getir.
                        '<br><br><p>' + veri.bolumu + '</p></a></li>';//isimin altında bolumu göster.
                $("#lstdoktorlistesi").append(htm);
            });
            $("#lstdoktorlistesi").listview("refresh");
        }
    });
});

$('#pgdoktoramesaj').live('pageshow', function () {
    $("#lstdoktoramesaj").empty();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=gelenmesaj&benden=" + getCookie('username') + "&alan=" + getCookie('friendid'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).mesajveri, function ()
            {
                var veri = this;
                var htm = '<li><a><font size="2" color="black"> ' + veri.mesaj + '</font><p align="right" ><font size="2" face="arial" align="right"  color="red"></p> ben </font>'
                '<p align="right"></p></a><ul><li>' + veri.mesaj +
                        '</li></ul></li>';
                $("#lstdoktoramesaj").append(htm);
            });
            $("#lstdoktoramesaj").listview("refresh");
        }
    });
});

$('#pgdoktoramesaj').live('pageshow', function () {
    $("#lstdoktoramesaj").empty();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=ondangelenmesaj&benden=" + getCookie('username') + "&alan=" + getCookie('friendid'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).ondanmesajveri, function ()
            {
                var veri = this;
                var htm = '<li><a><p align="right">' + veri.tarih + '</p><font size="2" color="black">' + veri.mesaj + '</font><p align="right" ><font size="2" face="arial" align="right"  color="red"></p><img align="right" src="' + veri.resim + '" alt="" style="height: 60px;width: 60px" /> dr. ' + veri.doktoradi + '  ' + '  ' + veri.doktorsoyadi + ' </font>'
                '<p align="right"></p></a><ul><li>' + veri.mesaj +
                        '</li></ul></li>';
                $("#lstdoktoramesaj").append(htm);
            });
            $("#lstdoktoramesaj").listview("refresh");
        }
    });
});

function FriendInfo(id)
{
    setCookie('friendid', id);
    $.mobile.changePage("#pgdoktoramesaj");
}

function SendMessage()
{
    $("#lstdoktoramesaj").listview("refresh");
    var mesaj = $("#txtmessage").val();
    var veri = "tur=sendmessage&mesaj=" + mesaj + "&gonderen=" + getCookie('username') + "&alici=" + getCookie('friendid');
    console.log(veri);
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: veri,
        success: function ()
        {
            alert(" Mesaj Gonderildi...");
            javascript:history.go(0)

        }
    });

    var htm = '<li><a data-theme="a">' + $("#txtmessage").val() +
            '<p align="right" >Ben</p></a><ul><li>' + $("#txtmessage").val() +
            '</li></ul></li>';
    $("#lstdoktoramesaj").append(htm);
    $("#lstdoktoramesaj").listview("refresh");
    $("#txtmessage").val('');
}

$('#pgrandevu').live('pageshow', function () {
    $("#lstbolumler").empty();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=randevu",
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).randevuveri, function ()
            {
                var veri = this;
                var htm = '<option  value="' + veri.bolumlerid + '">' + veri.bolumler + '</option>';
                $("#lstbolumler").append(htm);
            });
            $("#lstbolumler").listview("refresh");
        }
    });
});




function doktorgetir()
{
    var bolum = $("#lstbolumler").val();
    $("lstdoktorlar").empty();
    $.ajax({
        type: "POST",
        url: "hastahane", //servlet e gidecek
        data: "tur=doktorlar&&bolum=" + bolum,
        success: function (msg)
        {
            console.log(bolum)
            console.log(msg);
            $.each(JSON.parse(msg).doktorlarveri, function ()
            {
                var veri = this;
                var htm = '<option  value="' + veri.doktorid + '">' + veri.doktor_adi + '  ' + ' ' + '  ' + veri.doktor_soyadi + '</option>';
                $("#lstdoktorlar").append(htm);
            });
            $("#lstdoktorlar").listview("refresh");
        }
    });

    $.mobile.changePage("#pgrandevu");
}

function randevual()
{
     var bolum = $("#lstbolumler").val();
     var doktor = $("#lstdoktorlar").val();
     var tarih = $("#lsttarihler").val();
     var saat = $("#lstsaatler").val();
    $.ajax({
        type: "POST",
        url: "hastahane",
        data: "tur=randevual&ben=" + getCookie('username') + "&bolum=" + bolum + "&doktor=" + doktor + "&tarih=" + tarih + "&saat=" + saat,
        success: function (msg)
        {
            window.alert("Randevu Alındı !")

        }
    });

    $.mobile.changePage("#pganasayfa");
}



