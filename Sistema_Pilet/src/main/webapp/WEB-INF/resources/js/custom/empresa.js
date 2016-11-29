/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function() {
   

    $.fn.initBootTable = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            
            if(this.id == "TablRegi"){
            consObje([{name : 'codiObjePara', value : row.id.trim()}]); 
            }            
            if(this.id == "TablDona"){
            consDonaObje([{name : 'codiObjePara', value : row.idDona.trim()}]); 
            console.log(row.idDona.trim());
            }
            if(this.id == "TablSegu"){
            consSeguObje([{name : 'codiObjePara', value : row.idSegu.trim()}]); 
            }
            if(this.id == "TablRegiHist"){
            consObjeDocu([{name : 'codiObjePara', value : row.idDocu.trim()}]); 
            }
            if(this.id == "TablDocu"){
            consObjeDocu([{name : 'codiObjePara', value : row.idDocu.trim()}]); 
            }
        });
        return false;
    };
    $.fn.initDatePick = function() {
        $(this).datepicker({
            format: "dd/mm/yyyy",
            language: "es",
            orientation: "top auto",
            autoclose: true,
            todayHighlight: true
        }).on('show.bs.modal', function(e) {
            // Quitar el conflicto con bootstrap modal "show.bs.modal"
            e.stopPropagation();
        }).on('hide.bs.modal', function(e) {
            // Quitar el conflicto con bootstrap modal "hide.bs.modal"
            e.stopPropagation();
        });
    };
   
    $('#ModaForm').on('hide.bs.modal', function() {
        $("#TablRegi").bootstrapTable('uncheckAll');
    });
    $('#ModaDonaForm').on('show.bs.modal', function() {
       $("#TablRegi").bootstrapTable('uncheckAll');
    });
    $('#ModaDonaForm').on('hide.bs.modal', function() {
        $("#TablDona").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro donaciones");
    });
    $('#ModaSeguForm').on('show.bs.modal', function() {
       console.log("asafads");
       $("#TablSegu").bootstrapTable('uncheckAll');
    });
    $('#ModaSeguForm').on('hide.bs.modal', function() {
        $("#TablSegu").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro donaciones");
    });
    INIT_OBJE();
});

function INIT_OBJE()
{
    $("#TablRegi").initBootTable();
    $("#TablDona").initBootTable();
    $("#TablSegu").initBootTable();
    $(".select").selectpicker();    
    $("#TablDocu").initBootTable();
    INIT_OBJE_MODA();
    INIT_OBJE_FILE();
    
}
function INIT_OBJE_MODA()
{
    $("#FormRegi\\:btonElim2").confirmation({container: '#FormRegi'});
    $("#FormDona\\:btonElim").confirmation({container: '#FormDona'});
    $("#FormSegu\\:btonElim").confirmation({container: '#FormSegu'}); 
     $("#FormRegiDocu\\:btonElimDocu").confirmation({container: '#FormRegiDocu'});
    $("#FormEmpr\\:fech").initDatePick();
    $("#FormRegiDocu\\:fech").initDatePick();
    $("#FormRegi\\:fech2").initDatePick();
    $("#FormSegu\\:fech").initDatePick();
    $("#FormSegu\\:fech2").initDatePick();
  
}