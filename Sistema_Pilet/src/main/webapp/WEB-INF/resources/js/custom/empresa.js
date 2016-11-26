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
            console.log(this.id);            
            
            if(this.id == "TablRegi"){
            consObje([{name : 'codiObjePara', value : row.id.trim()}]); 
            }
            
            if(this.id == "TablDona"){
            consDonaObje([{name : 'codiObjePara', value : row.idDona.trim()}]); 
            console.log(row.idDona.trim());
            }
            if(this.id == "TablDocu"){
            consObjeDocu([{name : 'codiObjePara', value : row.idDocu.trim()}]); 
            }
            if(this.id == "TablSegu"){
            consObjeDocu([{name : 'codiObjePara', value : row.idDocu.trim()}]); 
            }
            if(this.id == "TablRegiHist"){
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
    $('#ModaForm').on('show.bs.modal', function() {
        INIT_OBJE_MODA();
    });
    $('#ModaForm').on('hide.bs.modal', function() {
        $("#TablRegi").bootstrapTable('uncheckAll');
    });
    $('#ModaDonaForm').on('show.bs.modal', function() {
       console.log("asafads");
       $("#TablRegi").bootstrapTable('uncheckAll');
    });
    $('#ModaDonaForm').on('hide.bs.modal', function() {
        $("#TablDona").bootstrapTable('uncheckAll');
         INIT_OBJE();
         console.log("Cierro donaciones");
    });
    INIT_OBJE();
});

function INIT_OBJE()
{
    $("#TablRegi").initBootTable();
    $("#TablDona").initBootTable();
    $(".select").selectpicker();    
    INIT_OBJE_MODA();
}
function INIT_OBJE_MODA()
{
    $("#FormRegi\\:btonElim").confirmation({container: '#FormRegi'});
    $("#FormDona\\:btonElim").confirmation({container: '#FormDona'});
    $("#FormEmpr\\:fech").initDatePick();
    $("#FormRegi\\:fech2").initDatePick();
}