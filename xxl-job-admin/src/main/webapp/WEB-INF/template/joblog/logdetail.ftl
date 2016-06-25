<!doctype html>
<html>
<head>
    <title>Console XX-JOB</title>
    <style>
        body {
            width: 100%;
            margin: 0 auto 0 auto;
            font-family: Arial, Helvetica;
            font-size: small;
            background-color: black;
        }

        /* ------------------------------------------------- */

        #tabs {
            background-color: #eee;
            background-image: url(data:image/gif;base64,R0lGODlhCAAIAJEAAMzMzP///////wAAACH5BAEHAAIALAAAAAAIAAgAAAINhG4nudroGJBRsYcxKAA7);
            overflow: hidden;
            width: 100%;
            margin: 0;
            padding: 0;
            list-style: none;
        }

        #tabs li {
            float: left;
            margin: 0 -15px 0 0;
        }

        #tabs a {
            float: left;
            position: relative;
            padding: 0 40px;
            height: 0;
            line-height: 30px;
            text-transform: uppercase;
            text-decoration: none;
            color: #fff;
            border-right: 30px solid transparent;
            border-bottom: 30px solid #3D3D3D;
            border-bottom-color: #777 \9;
            opacity: .3;
            filter: alpha(opacity=30);
        }

        #tabs a:hover,
        #tabs a:focus {
            border-bottom-color: #2ac7e1;
            opacity: 1;
            filter: alpha(opacity=100);
        }

        #tabs a:focus {
            outline: 0;
        }

        #tabs .current {
            z-index: 3;
            border-bottom-color: #3d3d3d;
            opacity: 1;
            filter: alpha(opacity=100);
        }

        /* ----------- */
        #content {
            background: black;
            border-top: 4px solid #D25A0B;
            padding: 2em;
            /* height: 220px; */
            color: white;
        }

        #content h2,
        #content h3,
        #content p {
            margin: 0 0 15px 0;
        }

        /* Demo page only */
        #about {
            color: #999;
            text-align: center;
            font: 0.9em Arial, Helvetica;
        }

        #about a {
            color: #777;
        }
    </style>
</head>
<body>
<#if logTypes.content?size > 1>
<ul id="tabs">
    <#list logTypes.content as logType>
        <#if logType?trim?length < 1 >
            <li><a href="#" name="#${logType}">Console</a></li>
        <#elseif logType == currentLogType>
            <#--<#if jobHandlerName??>&jobHandler=${jobHandlerName}</#if><#if logType??>&logType=${logType}</#if>-->
            <li><a href="joblog/logDetailPage?id=${logId}&jobHandler=${jobHandlerName!}&logType=${logType!}"
                   name="#${logType}" class="current">${logType}</a></li>
        <#else >
            <li><a href="joblog/logDetailPage?id=${logId}&jobHandler=${jobHandlerName!}&logType=${logType!}"
                   name="#${logType}">${logType}</a></li>
        </#if>
    </#list>
</ul>
</#if>

<div id="content">
    <div>
    <pre style="font-family: Arial, Helvetica;font-size: small;">
    <#if result.code == 200>
        <#list result.content as line>
            <#if line?is_string>
            ${line}
            <#elseif line?is_hash>
            <#-- 输出表头 -->
                <#if line_index == 0>
                <table>
                    <thead>
                            <tr>
                                <#list line?keys as k>
                                    <th>${k}</th>
                                </#list>
                            </tr>
                        </thead>
                <tbody>
                <#--输出tbody-->
                </#if>
            <tr>
                <#list map?keys as k>
                    <td>${map[k]}</td>
                </#list>
                </tr>
            <#-- 输出表结束符-->
                <#if !line_has_next>
                </tbody>
                </table>
                </#if>
            <#else >
                <#break >
            </#if>

        </#list>
    <#else>
        <h3 style="color: red;">ERROR</h3>
    ${result.msg}
    </#if>
    </pre>
    </div>
</div>
</body>
</html>