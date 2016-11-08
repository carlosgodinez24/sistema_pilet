$(document).ready(function() {
    INIT_FORM();
});

function otroParen(val){
    if(val  === "Otro"){
        document.getElementById("ParenDiv").setAttribute("style","display:box;");
    }else{
        document.getElementById("ParenDiv").setAttribute("style","display:none;");
       $("#FormRegi\\:pareEven").val("");
    }
}
function selectedItem(val){
    $("#FormRegi\\:seleParen option:eq("+val+")").prop('selected', true);
}

function INIT_FORM(){
    $("#FormRegi\\:seleParen").change(function() {
        otroParen(this.options[this.selectedIndex].innerHTML);
      });
      var select = document.getElementById("FormRegi:seleParen");
      otroParen(select[select.selectedIndex].innerHTML);
}