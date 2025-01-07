<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Arrays"%>
<%@page import="com.ingeint.scaleconnector.ws.feature.SCWSLocale"%>
<%@page import="com.ingeint.scaleconnector.ws.feature.SCWSFeature"%>

<%
    try {
        SCWSFeature.load(getServletContext());

        String locale = request.getParameter("locale");

        if(locale == null)
            locale = SCWSFeature.get("DEFAULT_LOCALE");
        else if(Arrays.asList(SCWSLocale.list()).contains(locale))
            SCWSFeature.set("DEFAULT_LOCALE",locale);
        else
            locale = SCWSFeature.get("DEFAULT_LOCALE");

        try {
            SCWSFeature.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SCWSLocale.load(getServletContext(), locale);
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="image/logo16px.png">
    <title><%=SCWSFeature.get("APP_NAME")%> - <%=SCWSFeature.get("VENDOR")%></title>

    <script src="jquery-2.0.3.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" charset="utf-8">
        $(document)
            .ready(
                function() {
                    $('#send')
                        .click(
                            function() {
                                $
                                    .ajax({
                                        dataType : 'json',
                                        url : 'get_response',
                                        data : {
                                            'requestType' : 'READ_PORT',
                                            'serialport' : $(
                                                'input:radio[name=serialport]:checked')
                                                .val(),
                                            'baud' : $(
                                                'input[name=baud]')
                                                .val(),
                                            'databits' : $(
                                                'input[name=databits]')
                                                .val(),
                                            'stopbits' : $(
                                                'input[name=stopbits]')
                                                .val(),
                                            'parity' : $(
                                                'input[name=parity]')
                                                .val(),
                                            'bytecount' : $(
                                                'input[name=bytecount]')
                                                .val(),
                                            'startcharacter' : $(
                                                'input[name=startcharacter]')
                                                .val(),
                                            'endcharacter' : $(
                                                'input[name=endcharacter]')
                                                .val(),
                                            'readings' : $(
                                                'input[name=readings]')
                                                .val(),
                                            'stabilitypos' : $(
                                                'input[name=stabilitypos]')
                                                .val(),
                                            'stability' : $(
                                                'input[name=stability]')
                                                .val(),
                                            'floatingpoint' : $(
                                                'input[name=floatingpoint]')
                                                .val(),
                                            'startcut' : $(
                                                'input[name=startcut]')
                                                .val(),
                                            'endcut' : $(
                                                'input[name=endcut]')
                                                .val()
                                        },
                                        success : function(data) {
                                            alert(data.serverMessage
                                                + "\n"
                                                + data.status
                                                + "\n- value: "
                                                + data.data.value
                                                + "\n- isstable: "
                                                + data.data.isstable
                                                + "\n- serialport: "
                                                + data.data.serialport);
                                        }
                                    });
                            });
                });
    </script>

    <style type="text/css" media="screen">
        * {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
        }

        body {
            padding-top: 50px;
            background-color: #eee;
        }

        #form {
            margin: auto;
            width: 500px;
            padding: 10px;
            border-radius: 10px;
            border: 1px solid #808080;
            box-shadow: 0 0 15px #808080;
        }

        #locale {
            margin: auto;
            width: 500px;
            padding: 10px;
        }

        #send {
            width: 500px;
            background-color: #ff8000;
            border: 1px solid #ff8000;
            color: #ffffff;
            padding-top: 5px;
            padding-bottom: 5px;
            margin-top: 10px;
            border-radius: 5px;
        }

        #send:hover {
            box-shadow: 0 0 5px #808080;
        }

        input[type=text] {
            width: 80px;
            padding-top: 5px;
            padding-bottom: 5px;
            border-radius: 5px;
            border: 1px solid #999999;
            float: right;
            text-indent: 5px;
        }

        #title {
            font-size: 20px;
            padding: 10px;
        }

        a {
            color: orange;
            text-decoration: none;
            font-family: sans-serif;
            font-size: 14px;
        }
    </style>

</head>

<body>

<form action="" method="post" accept-charset="utf-8" id="form">

    <table border="0" width="100%" cellspacing="5">
        <tr>
            <td colspan="4" align="center"><img alt="" src="image/logo.png"></td>
        </tr>

        <tr>
            <th colspan="4" align="center" id="title">Scale Connector</th>
        </tr>
        <tr>
            <td><%=SCWSLocale.get("serialport")%></td>
            <td align="left"><%@ page import="jssc.SerialPortList"%>
                <%
                    for (String portName : SerialPortList.getPortNames()) {
                        out.println("<input type=\"radio\" name=\"serialport\" value=\"" + portName + "\" checked> " + portName + "<br />");
                    }
                %></td>
            <td><%=SCWSLocale.get("bytecount")%></td>
            <td><input type="text" name="bytecount"
                       value="<%=SCWSFeature.get("DEFAULT_BYTECOUNT")%>" /></td>
        </tr>
        <tr>
            <td><%=SCWSLocale.get("baud")%></td>
            <td><input type="text" name="baud"
                       value="<%=SCWSFeature.get("DEFAULT_BAUD")%>" /></td>

            <td><%=SCWSLocale.get("parity")%></td>
            <td><input type="text" name="parity"
                       value="<%=SCWSFeature.get("DEFAULT_PARITY")%>" /></td>
        </tr>
        <tr>
            <td><%=SCWSLocale.get("databits")%></td>
            <td><input type="text" name="databits"
                       value="<%=SCWSFeature.get("DEFAULT_DATABITS")%>" /></td>
            <td><%=SCWSLocale.get("stopbits")%></td>
            <td><input type="text" name="stopbits"
                       value="<%=SCWSFeature.get("DEFAULT_STOPBITS")%>" /></td>
        </tr>
        <tr>
            <td><%=SCWSLocale.get("startcharacter")%></td>
            <td><input type="text" name="startcharacter"
                       value="<%=SCWSFeature.get("DEFAULT_STARTCHARACTER")%>" /></td>
            <td><%=SCWSLocale.get("endcharacter")%></td>
            <td><input type="text" name="endcharacter"
                       value="<%=SCWSFeature.get("DEFAULT_ENDCHARACTER")%>" /></td>
        </tr>

        <tr>
            <td><%=SCWSLocale.get("startcut")%></td>
            <td><input type="text" name="startcut"
                       value="<%=SCWSFeature.get("DEFAULT_STARTCUT")%>" /></td>
            <td><%=SCWSLocale.get("endcut")%></td>
            <td><input type="text" name="endcut"
                       value="<%=SCWSFeature.get("DEFAULT_ENDCUT")%>" /></td>
        </tr>

        <tr>
            <td><%=SCWSLocale.get("stabilitypos")%></td>
            <td><input type="text" name="stabilitypos"
                       value="<%=SCWSFeature.get("DEFAULT_STABILITYPOS")%>" /></td>
            <td><%=SCWSLocale.get("stability")%></td>
            <td><input type="text" name="stability"
                       value="<%=SCWSFeature.get("DEFAULT_STABILITY")%>" /></td>
        </tr>

        <tr>
            <td><%=SCWSLocale.get("readings")%></td>
            <td><input type="text" name="readings"
                       value="<%=SCWSFeature.get("DEFAULT_READINGS")%>" /></td>

            <td><%=SCWSLocale.get("floatingpoint")%></td>
            <td><input type="text" name="floatingpoint"
                       value="<%=SCWSFeature.get("DEFAULT_FLOATINGPOINT")%>" /></td>
        </tr>
    </table>

    <input type="button" value="<%=SCWSLocale.get("send")%>" id="send" />

</form>

<div id="locale">
    <%
        for (String locale : SCWSLocale.list()) {
            out.println("<a href=\"?locale=" + locale + "\">" + locale + "</a>");
        }
    %>

</div>

</body>
</html>