<body style="color:white;background-color:black;">
<pre>
<br>
<#if result.code == 200>
    <#list result.content as line>
    ${line}
    </#list>
<#else>${result.msg}</#if>
</pre>
</body>
