<#import "parts/common.ftl" as c>
<#import "parts/loginParts.ftl" as l>

<@c.page>
<div class = "mb-1">Registration page
${message?ifExists} 
<@l.login "/registration" true/>
</@c.page>