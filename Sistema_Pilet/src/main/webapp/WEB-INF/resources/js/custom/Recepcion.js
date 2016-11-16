$(document).ready(function() {
    $.fn.initBootTableVisi = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeAlum([{name : 'codiObjeAlum', value : row.id.trim()}]);
        });
        return false;
    };
    INIT_OBJE_TABL_VISI();
});
function INIT_OBJE_TABL_VISI(){
    $("#TablVisi").initBootTableVisi();
}