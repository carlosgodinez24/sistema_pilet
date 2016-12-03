$(document).ready(function() {

  $.fn.initBootTableVisiExce = function() {
        $(this).bootstrapTable('destroy');
        $(this).bootstrapTable().
        unbind('check.bs.table').on('check.bs.table', function (e, row) {
            consObjeExce([{name : 'codiObjeExce', value : row.state.trim()}]);
        });
        return false;
    };
    $('#ModaFormImpoExce').on('show.bs.modal', function() {
        INIT_OBJE_MODA();
    });
    $('#ModaFormImpoExce').on('hide.bs.modal', function() {
        $("#TablRegi").bootstrapTable('uncheckAll');
    });
    INIT_OBJE_MODA_EXCE();
});
function INIT_OBJE_MODA_EXCE()
{
    $("#FormImpoExce\\:TablDocuExce").bootstrapTable();
}