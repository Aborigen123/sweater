<#import "parts/common.ftl" as c>
<#import "parts/loginParts.ftl" as l>

<@c.page>
<#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
   <div class="alert alert-danger" role="alert">
        ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
    </div>
</#if>

<#if message??>
   <div class="alert alert-${messageType}" role="alert">
        ${message}
    </div>
</#if>
Login page
<@l.login "/login" false/>
</@c.page>
