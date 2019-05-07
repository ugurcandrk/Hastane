$('#pgdoktormuayene').live('pageshow', function () {
    $("#lstmuayenehasta").empty();
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=doktormuayene&&id=" + getCookie('username'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).doktormuayeneveri, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li><label><font size="4" face="arial"  color="gray"><font color="black" face="arial">"' + veri.tarih + ' "Tarihli Muayene Kaydı </font>  </font>'
                        +
                        '</label><ul><li><label><font size="4" face="arial"  color="gray">Muayene Edilen Hasta Adı : </font> '
                        + veri.hastaadi +
                        '</label></li><li><label><font size="4" face="arial"  color="gray">Muayene Edilen Hasta Soyadı : </font> '
                        + veri.hastasoyadi +
                        '</label></li><li><label><font size="4" face="arial"  color="gray"> Dogum Tarihi : </font> '
                        + veri.dogumtarihi +
                        '</label></li></ul></li>';//isimin altında telefon numarasını göster.
                $("#lstmuayenehasta").append(htm);
            });
            $("#lstmuayenehasta").listview("refresh");
        }
    });
});

$('#pgdoktorrandevuler').live('pageshow', function () {
    $("#lstdoktorrandevuler").empty();
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=doktorrandevu&&id=" + getCookie('username'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).doktorrandevuler, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li><a href="#pgconfirm" onclick="ConfirmID(' + veri.id + ')" >' + veri.hastaadi + ' ' + veri.hastasoyadi + //arkadaşını ismini soyismini getir.
                        '<br><br><p><font  color="black" >Randevu Tarihi:  </font>  ' + '' + ' <font  color="red" size="2" >' + veri.tarih + '</font><br><br><font  color="black" >Randevu Saati: </font> ' + ' <font  color="red" size="2" >' + veri.saat + ' </font></p></a></li>';//isimin altında telefon numarasını göster.
                $("#lstdoktorrandevuler").append(htm);
            });
            $("#lstdoktorrandevuler").listview("refresh");
        }
    });
});

var kullanici_id;
function ConfirmID(id)
{
    kullanici_id = id;

}

function muayeneyeal(state)
{

    var durum = 0;
    if (state) {
        durum = 1;
    }
    console.log(durum);
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=randevuonay&doktor=" + getCookie('username') + "&kullanici=" + kullanici_id + "&state=" + durum,
        success: function (msg)
        {
            window.alert("Muayeneye Alındı !")
        }

    });

    $.mobile.changePage("#pgdoktorrandevuler");

}




$('#pgdoktorilaclar').live('pageshow', function () {
    $("#lstdoktorilaclar").empty();
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=doktorilaclar",
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).doktorilaclar, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li>'
                        +
                        '</li><li><table id="t01" >\n\
                           <tr>\n\
                            <td><font face="arial" size="3" color="red">İlacın Adı</font></td>\n\
                            <td><font  face="arial" size="3" color="red">Açlık-Tokluk</font></td>\n\
                            <td><font  face="arial" size="3" color="red">Alınması Gereken Vakit</font></td>\n\
                            <td><font  face="arial" size="3" color="red">Yan Etkileri</font></td>\n\
                            <td><font  face="arial" size="3" color="red">Eklenme Tarihi</font></td>\n\
                            <td><font  face="arial" size="3" color="red">Resim</font></td>\n\
                            <tr><td><b><font face="arial" size="3" color="black">' + veri.ilacadi + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.ilactoklukdurumu + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.ilacvakti + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.yanetkileri + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.eklenme_tarihi + '</font></b></td>\n\
                            <td><IMG onmouseover="this.height=300;this.width=300;" onmouseout="this.height=100;this.width=100;" height=100 src="' + veri.resim + '" width=100 border=0></td></tr>'
                '</table></li>';//isimin altında telefon numarasını göster.
                $("#lstdoktorilaclar").append(htm);
            });
            $("#lstdoktorilaclar").listview("refresh");
        }
    });
});

function ilacekle()
{
    var ilacinadi = $("#txtilacadi").val();
    var acliktokluk = $("#txtacliktokluk").val();
    var vakit = $("#txtvakit").val();
    var kullanmasuresi = $("#txtkullanmasuresi").val();
    var yannetkileri = $("#txtyanetkileri").val();
    var veri = "tur=ilacinsert&ilacadi=" + ilacinadi + "&acliktokluk=" + acliktokluk + "&vakit=" + vakit + "&yannetkileri=" + yannetkileri + "&kullanmasuresi=" + kullanmasuresi;
    console.log(veri);
    if (ilacinadi != "" && acliktokluk != "" && vakit != "" && yannetkileri != "" && kullanmasuresi != "") {
        $.ajax({
            type: "POST",
            url: "doktor",
            data: veri,
            success: function ()
            {
                alert("Kayıt Başarılı...");

                $.mobile.changePage("#pgdoktorilaclar");
            }
        });
    } else {
        alert("Hata.. Kayıt Yapılamadı !! ");
    }
}

$('#pgmuayeneedilmemishastalar').live('pageshow', function () {
    $("#lstmuayeneedilmemishastalar").empty();
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=muayeneedilmemishastalar&&id=" + getCookie('username'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).muayeneedilmemishastalar, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li><a href="#pgteshisekle" onclick="ConfirmID(' + veri.id + ')" >' + veri.hastaadi + ' ' + veri.hastasoyadi + //arkadaşını ismini soyismini getir.
                        '</a></li>';//isimin altında telefon numarasını göster.
                $("#lstmuayeneedilmemishastalar").append(htm);
            });
            $("#lstmuayeneedilmemishastalar").listview("refresh");
        }
    });
});

$('#pgteshisekle').live('pageshow', function () {
    $("#lstilacclar").empty();
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=ilac",
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).ilac, function ()
            {
                var veri = this;
                var htm = '<option  value="' + veri.id + '">' + veri.ilac_adi + '</option>';
                $("#lstilacclar").append(htm);
            });
            $("#lstilacclar").listview("refresh");
        }
    });
});

function onayla(state)
{
    var durum = 2;
    if (state) {
        durum = 2;
    }
    var ilac_id = $("#lstilacclar").val();
    var teshis = $("#txtteshis").val();
    console.log(durum);
    if (teshis != "") {
        $.ajax({
            type: "POST",
            url: "doktor",
            data: "tur=teshisinsert&doktor=" + getCookie('username') + "&kullanici=" + kullanici_id + "&teshis=" + teshis + "&ilac=" + ilac_id,
            success: function (msg)
            {

            }
        });



        $.ajax({
            type: "POST",
            url: "doktor",
            data: "tur=teshisonay&doktor=" + getCookie('username') + "&kullanici=" + kullanici_id + "&state=" + durum,
            success: function (msg)
            {
                window.alert("Teşhis Eklendi !")
                javascript:history.go(0)
            }
        });
        $.mobile.changePage("#pgmuayeneedilmemishastalar");
    }
    else {
        window.alert("Teşhisi Boş Bırakmayınız !")
    }
}

$('#pghastalarım').live('pageshow', function () {
    $("#lsthastalarım").empty();
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=hastalarim&&id=" + getCookie('username'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).hastalarim, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li>'
                        +
                        '</li><li><table id="t01" >\n\
                           <tr>\n\
                            <td><font face="arial" size="4" color="red">Tarih</font></td>\n\
                            <td><font face="arial" size="4" color="red">Hasta Adı - Soyadı</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Dogum Tarihi</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Kullanılması Gereken İlaç</font></td>\n\
                            <td><font  face="arial" size="4" color="red">İlacın Kullanılması Gereken Süre</font></td>\n\
                            <td><font  face="arial" size="4" color="red">Teşhis</font></td>\n\
                            <tr><td><b><font face="arial" size="3" color="black">' + veri.tarih + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.hastaadi + '' + ' ' + veri.hastasoyadi + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.dogumtarihi + '</font></b></td>\n\
                            <td><b><a data-role="button" ><i><font size="3" face="arial" color="blue" >' + veri.ilacadi + '</font></a></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.ilackullanmasuresi + '</font></b></td>\n\
                            <td><b><font face="arial" size="3" color="black">' + veri.teshis + '</font></b></td></tr>'
                '</table></li>';//isimin altında telefon numarasını göster.
                $("#lsthastalarım").append(htm);
            });
            $("#lsthastalarım").listview("refresh");

        }
    });
});

$('#pgdoktormailler').live('pageshow', function () {
    $("#lstdoktormailler").empty();
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=hastamailler",
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).hastamailler, function () {
                var veri = this;
                console.log(veri);
                var htm = '<li><a  onclick="FriendInfo(' + veri.id + ')"><img src="' + veri.resim + '" alt="" style="height: 100px;width: 100px" />' + veri.hastaadi + ' ' + veri.hastasoyadi + //arkadaşını ismini soyismini getir.
                        '<br><br><p>' + veri.kullanici_dogumtarih + '</p></a></li>';//isimin altında telefon numarasını göster.
                $("#lstdoktormailler").append(htm);
            });
            $("#lstdoktormailler").listview("refresh");
        }
    });
});

function FriendInfo(id)
{
    setCookie('friendid', id);
    $.mobile.changePage("#pghastayamesaj");
}

function SendMessage()
{
    $("#lsthastayamesaj").listview("refresh");
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
    $("#lsthastayamesaj").append(htm);
    $("#lsthastayamesaj").listview("refresh");
    $("#txtmessage").val('');
}

$('#pghastayamesaj').live('pageshow', function () {
    $("#lsthastayamesaj").empty();
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
                $("#lsthastayamesaj").append(htm);
            });
            $("#lsthastayamesaj").listview("refresh");
        }
    });
});

$('#pghastayamesaj').live('pageshow', function () {
    $("#lsthastayamesaj").empty();
    $.ajax({
        type: "POST",
        url: "doktor",
        data: "tur=ondangelenmesaj&benden=" + getCookie('username') + "&alan=" + getCookie('friendid'),
        success: function (msg)
        {
            console.log(msg);
            $.each(JSON.parse(msg).ondanmesajveri, function ()
            {
                var veri = this;
                var htm = '<li><a><font size="2" color="black">' + veri.mesaj + '</font><p align="right" ><font size="2" face="arial" align="right"  color="red"></p><img align="right" src="' + veri.resim + '" alt="" style="height: 60px;width: 60px" /> ' + veri.doktoradi + '  ' + '  ' + veri.doktorsoyadi + ' </font>'
                '<p align="right"></p></a><ul><li>' + veri.mesaj +
                        '</li></ul></li>';
                $("#lsthastayamesaj").append(htm);
            });
            $("#lsthastayamesaj").listview("refresh");
        }
    });
});