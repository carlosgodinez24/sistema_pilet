$(document).ready(function() {
    $.fn.initBootTableAlum = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeAlum([{name : 'codiObjeAlum', value : row.id.trim()}]);
        });
        return false;
    };
    INIT_OBJE_CITA();
});
function INIT_OBJE_CITA(){
    $("#FormRegi\\:btonAgreVisi").confirmation({container: '#FormRegi'});
    
 //$("#TablAlum").initBootTableAlum();
}
function INIT_OBJE_TABL(){
    $("#TablAlum").initBootTableAlum();
    //$("#TablVisiCita").bootstrapTable();
     //$("#TablInvo").bootstrapTable();
    //setMessage('MESS_INFO', 'Atención', 'Inicializando...');
}