/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function generateAuthCode() {
    var authCodeContainer = document.getElementById("registration-form:authCode");
    var password = authCodeContainer.textContent;
    console.log(password);
    var authCode = CryptoJS.SHA1(password).toString();
    console.log(authCode);
    debugger;
    authCodeContainer.innerHTML = authCode;
    remoteCreateUsername();
}
