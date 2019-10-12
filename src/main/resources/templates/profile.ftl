<#import "parts/common.ftl" as c>

<@c.page>
${message?ifExists} 
<h5>${username}</h5>
<form  method="post">
 
       
         
   <div class="form-group row">
        <label class="col-sm-2 col-form-label">Password:</label>
        <div class="col-sm-6">
        <input type="password" name="password"/> 
        </div>
        </div>
        
        
           <div class="form-group row">
        <label class="col-sm-2 col-form-label">Email:</label>
        <div class="col-sm-6">
        <input type="email" name="email" placeholder="email" value"${email!''}"/> <!--!'' - якщо емейл не вкажемо то буде відображуватися пуста лінійка -->
        </div>
        </div>
        
        
        
        
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">Save</button>
</form>
</@c.page>
