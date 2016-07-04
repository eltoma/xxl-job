<#--list 中元素为map类型使用当前这个view展示-->
<#list callBack as line>
<#-- 输出表头 -->
    <#if line_index == 0>
    <table>
        <thead>
        <tr>
            <#list head as k_head>
                <th>${k_head}</th>
            </#list>
        </tr>
        </thead>
    <tbody>
    </#if>
<#--输出tbody-->
<tr>
<#--<#list line.keySet() as k_body>-->
    <#list head as k_body>
        <td>${line.get(k_body)!}</td>
    </#list>
</tr>
<#-- 输出表结束符-->
    <#if !line_has_next>
    </tbody>
    </table>
    </#if>

</#list>